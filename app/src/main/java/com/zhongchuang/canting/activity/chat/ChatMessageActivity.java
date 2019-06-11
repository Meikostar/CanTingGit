package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.adapter.ChatMenberBasedapter;
import com.zhongchuang.canting.adapter.ChatMessageAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UpPhotoBean;
import com.zhongchuang.canting.easeui.ui.AddFriendActivity;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.UploadUtil;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ChatMessageActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.grid_content)
    NoScrollGridView grid_content;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_red)
    TextView tvRed;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    private BasesPresenter presenter;
    private ChatMessageAdapter adapter;
    private GAME game;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private Gson mGson;

    public void initViews() {
        setContentView(R.layout.activity_chat_message);
        ButterKnife.bind(this);
        mGson = new Gson();
        game = (GAME) getIntent().getSerializableExtra("game");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        adapter = new ChatMessageAdapter(this);
        grid_content.setAdapter(adapter);
        if (game != null) {
            if (TextUtil.isNotEmpty(game.chatGroupName)) {
                tvSearch.setText(game.chatGroupName);
            }
            presenter.getFrendList(game.id);
        }
    }

    private String ids;


    @Override
    public void bindEvents() {
        adapter.setOnItemClickListener(new ChatMenberBasedapter.OnItemClickListener() {
            @Override
            public void onItemClick(FriendListBean data, int type) {


                if (type == 0) {
                    Intent intentc = new Intent(ChatMessageActivity.this, ChatActivity.class);
                    intentc.putExtra("userId", data.chat_user_id);
                    startActivity(intentc);
                } else if (type == 1) {
                    Intent intent = new Intent(ChatMessageActivity.this, AddFriendActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("name", game.chatGroupName);
                    intent.putExtra("group_ids", game.id);
                    intent.putExtra("ids", ids);
                    startActivityForResult(intent, 0);
                } else if (type == 2) {
                    Intent intent = new Intent(ChatMessageActivity.this, AddFriendActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("name", game.chatGroupName);
                    intent.putExtra("group_ids", game.id);
                    intent.putExtra("ids", ids);
                    startActivityForResult(intent, 1);
                }

            }
        });
        tvRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (FriendListBean bean : list) {
                            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(bean.chat_user_id);
                            if (conversation != null) {
                                conversation.markAllMessagesAsRead();
                            }

                        }

                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ChatMessageActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
                TextView tv_camera = view.findViewById(R.id.tv_camera);
                TextView tv_choose = view.findViewById(R.id.tv_choose);
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hasSdcard()) {
                            imageUri = Uri.fromFile(fileUri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                //通过FileProvider创建一个content类型的Uri
                                imageUri = FileProvider.getUriForFile(ChatMessageActivity.this, "com.zhongchuang.canting.provider", fileUri);
                            PhotoUtils.takePicture(ChatMessageActivity.this, imageUri, CODE_CAMERA_REQUEST);
                        } else {
                            Toast.makeText(ChatMessageActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
                            Log.e("asd", getString(R.string.sbmysdk));
                        }
                        mWindowAddPhoto.dismiss();
                    }
                });
                tv_choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //设定结果返回
                        startActivityForResult(i, CODE_GALLERY_REQUEST);

                        mWindowAddPhoto.dismiss();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWindowAddPhoto.dismiss();
                    }
                });
                mWindowAddPhoto = new PhotoPopupWindow(ChatMessageActivity.this).bindView(view);
                mWindowAddPhoto.showAtLocation(tvChange, Gravity.BOTTOM, 0, 0);

            }
        });

    }

    /**
     * 调用系统相册
     *
     * @param view
     */
    public void getPhoto(View view) {
        PhotoUtils.openPic(ChatMessageActivity.this, CODE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 180, output_Y = 180;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zhongchuang.canting.provider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(ChatMessageActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    showProgress(getString(R.string.scz));
                    getUpToken(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
                default:
                    presenter.getFrendList(game.id);
                    break;
            }
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    public PhotoPopupWindow mWindowAddPhoto;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            dimessProgress();
            return false;
        }
    });

    @Override
    public void initData() {

    }


    public void setData() {

    }
    private void getUpToken(final String path) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);


        Call<TOKEN> call = api.getUpToken();
        call.enqueue(new Callback<TOKEN>() {
            @Override
            public void onResponse(Call<TOKEN> call, Response<TOKEN> response) {
                upFlile(path, response.body().data.upToken);
            }

            @Override
            public void onFailure(Call<TOKEN> call, Throwable t) {

            }
        });
    }
    public void upFlile(String path, String token) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {

                presenter.alterGroupImage(game.id, QiniuUtils.baseurl+urls);
            }
        });

    }

    public String paths;
    private List<FriendListBean> list;

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 18) {
            list = (List<FriendListBean>) entity;
            if (list != null && list.size() > 0) {
                adapter.setData(list);
                int i = 0;
                if (list != null && list.size() != 0) {
                    for (FriendListBean bean : list) {
                        if (i == 0) {
                            ids = bean.chat_user_id;
                        } else {
                            ids = ids + "," + bean.chat_user_id;
                        }
                        i++;
                    }
                }

                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ChatMessageActivity.this, com.zhongchuang.canting.activity.chat.GroupSetActivity.class);
                        intent.putExtra("name",game);
                        startActivity(intent);
                    }
                });
                tvAdd.setText(getString(R.string.sz));
            } else {
                tvAdd.setText(R.string.add);
                tvAdd.setVisibility(View.VISIBLE);
                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatMessageActivity.this, AddFriendActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("name", game.chatGroupName);
                        intent.putExtra("group_ids", game.id);
                        intent.putExtra("ids", "");
                        startActivityForResult(intent, 0);
                    }
                });
//                tvChange.setVisibility(View.GONE);
                tvRed.setVisibility(View.GONE);
                loadingView.setContent(getString(R.string.gzzwcy));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
        } else {
            showToasts(getString(R.string.ghcg));
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
        showToasts(msg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }



}
