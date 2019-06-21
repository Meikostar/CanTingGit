package com.zhongchuang.canting.activity.live;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.FindFriendActivity;
import com.zhongchuang.canting.activity.chat.ChatMessageActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.ImageUploadView;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.valuesfeng.picker.ImageSelectActivity;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.widget.ImageLoaderEngine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * 记录编辑
 */
public class RoomInfoActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_room)
    ClearEditText etRoom;
    @BindView(R.id.et_room_der)
    ClearEditText etRoomDer;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_name)
    TextView tv_name;
    private List<File> files = new ArrayList<>();
    private BasesPresenter presenter;
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_sale_after);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        presenter.getDirectRoomInfo();

    }

    private String url;

    @Override
    public void bindEvents() {

        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                View view = LayoutInflater.from(RoomInfoActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
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
                                imageUri = FileProvider.getUriForFile(RoomInfoActivity.this, "com.zhongchuang.canting.provider", fileUri);
                            PhotoUtils.takePicture(RoomInfoActivity.this, imageUri, CODE_CAMERA_REQUEST);
                        } else {
                            Toast.makeText(RoomInfoActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
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
                mWindowAddPhoto = new PhotoPopupWindow(RoomInfoActivity.this).bindView(view);
                mWindowAddPhoto.showAtLocation(ivAddPhoto, Gravity.BOTTOM, 0, 0);

            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtil.isEmpty(url)) {
                    showToasts(getString(R.string.qxztp));
                    return;
                }
                if (TextUtil.isEmpty(etRoom.getText().toString())) {
                    showToasts(getString(R.string.fjmcbnwk));
                    return;
                }
                if (TextUtil.isEmpty(etRoomDer.getText().toString())) {
                    showToasts(getString(R.string.qsrzbmt));
                    return;
                }
                showProgress(getString(R.string.scz));
                presenter.upRoomInfo(etRoom.getText().toString(), "", url.contains("http:")?url:QiniuUtils.baseurl+url, etRoomDer.getText().toString().trim());
            }
        });
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {

            }

            @Override
            public void navigationimg() {

            }
        });
    }

    private int poition;

    @Override
    public void initData() {

    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        Picker.from(this)
                .count(1)
                .enableCamera(true)
                .setEngine(new ImageLoaderEngine())
                .setAdd_watermark(false)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardFailed() {
//        showToast(getString(R.string.getqxdefault));
    }

    private int REQUEST_CODE_CHOOSE = 3;
    private int upload_position;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 356, output_Y = 200;
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
                        Toast.makeText(RoomInfoActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:

                    path=Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg";
                    Glide.with(RoomInfoActivity.this).load(path).asBitmap().placeholder(R.drawable.moren2).into(ivAddPhoto);

                    showProgress(getString(R.string.scz));
                    getUpToken(path);
                    notifyImageDataChange();
                    upload_position = 0;
                default:

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
    /**
     * 删除照片和修改相册的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public String path;



    public void upFlile(String path, String token) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {

                dimessProgress();
                url = urls;
            }
        });

    }

    private void notifyImageDataChange() {
        if (img_path == null || img_path.size() == 0) {
            ivAddPhoto.setImageDrawable(getResources().getDrawable(R.drawable.add_img));
        } else {
            Glide.with(this).load(new File(path)).asBitmap().placeholder(R.drawable.add_img).into(ivAddPhoto);
        }

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
    private Hands data;
    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 3) {
             data = (Hands) entity;
            if (data != null) {
                url=data.room_image;
                Glide.with(RoomInfoActivity.this).load(StringUtil.changeUrl(data.room_image)).asBitmap().placeholder(R.drawable.moren2).into(ivAddPhoto);
                if (!TextUtil.isEmpty(data.direct_see_name)) {
                    etRoom.setText(data.direct_see_name);
                    etRoom.setFocusable(false);
                    tv_name.setVisibility(View.GONE);
                }
                if (!TextUtil.isEmpty(data.direct_overview)) {
                    etRoomDer.setText(data.direct_overview);
                }

            }
        } else {
            showToasts(getString(R.string.zccg));
            finish();
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
}
