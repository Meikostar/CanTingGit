package com.zhongchuang.canting.activity.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.easeui.bean.RedPacketInfo;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.ImageUploadView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.popupwindow.PopView_UploadImg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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


//发布朋友圈
public class SendDynamicActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.piuv_remark_image)
    ImageUploadView piuvRemarkImage;
    @BindView(R.id.txt_send)
    TextView txtSend;
    @BindView(R.id.ll_bottom_button)
    View llBottomButton;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.iv_video_cover)
    ImageView ivVideoCover;
    @BindView(R.id.chatting_status_btn)
    ImageView chattingStatusBtn;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    private int upload_position;

    private BasesPresenter presenter;
    private List<File> files = new ArrayList<>();
    private PopView_UploadImg popView_uploadImg;
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();
    private int i = 1;
    private String url;
    private String content;

    private int count = 9;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_send_dynamic);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        navigationBar.setNavigationBarListener(this);
        haveFous(false);
    }
    private String video_url;
    @Override
    public void bindEvents() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_url="";
                rlBg.setVisibility(View.GONE);
                llBg.setVisibility(View.VISIBLE);
            }
        });
        piuvRemarkImage.setOnActionListener(new ImageUploadView.OnActionListener() {
            @Override
            public void onItemDelete(int position) {

                img_path.remove(position);
                if(img_path.size()==0){
                    rlBg.setVisibility(View.GONE);
                    llBg.setVisibility(View.VISIBLE);
                }
                if (img_path.size() == 0 && TextUtil.isEmpty(etContent.getText().toString())) {
                    haveFous(false);

                }
                notifyImageDataChange();
            }

            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onAddMoreClick() {
                if (count - img_path.size() >= 1) {
                    showAddPhotoWindow(count - img_path.size());

                }

            }

            @Override
            public void onItemPositionChange() {
                //mUploadView.notifyItemChange(0);
            }
        });
        etContent.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void afterTextChanged4ClearEdit(Editable s) {

            }

            @Override
            public void changeText(CharSequence s) {
                if (TextUtil.isNotEmpty(s.toString())) {
                    content = s.toString();
                    haveFous(true);
                } else {
                    content = "";
                    haveFous(false);
                }

            }
        });
        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(getString(R.string.fbz));
                url = "";
                haveFous(false);
                if (img_path != null && img_path.size() > 0) {

                    getUpToken(img_path.get(0).getForderPath());
                } else {
                    if(TextUtil.isNotEmpty(video_url)){
                          presenter.addInfo(etContent.getText().toString(), video_url);
                    }else {
                        if (TextUtil.isEmpty(etContent.getText().toString())) {
                            dimessProgress();

                            return;
                        } else {
                            presenter.addInfo(etContent.getText().toString(), "");
                        }
                    }



                }

            }
        });
        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                PermissionGen.with(SendDynamicActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA)
                        .request();

            }
        });
    }

    public void haveFous(boolean isFous) {
        if (isFous) {
            txtSend.setBackground(getResources().getDrawable(R.drawable.login_selector));
            txtSend.setClickable(true);
        } else {

            txtSend.setBackground(getResources().getDrawable(R.drawable.hui_bg));
            txtSend.setClickable(false);
        }
    }

    @Override
    public void initData() {

    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        showAddPhotoWindow(count - img_path.size());

    }


    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardFailed() {

    }


    /**
     * 删除照片和修改相册的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == 101) {
            Log.i("CJT", "picture");
            String path = data.getStringExtra("path");
            if (TextUtil.isNotEmpty(CanTingAppLication.video_path)) {
                String[] split = CanTingAppLication.video_path.split("%#%");
                if (split.length == 2) {
                    if (split[1].equals("1")) {
                        rlBg.setVisibility(View.GONE);
                        llBg.setVisibility(View.VISIBLE);
                        piuvRemarkImage.setVisibility(View.VISIBLE);
                        UploadFileBean bean = new UploadFileBean(1);
                        bean.setForderPath(split[0]);
                        img_path.add(bean);
                        haveFous(true);
                        notifyImageDataChange();
                        upload_position = 0;
                    }
                } else {
                    llBg.setVisibility(View.GONE);
                    piuvRemarkImage.setVisibility(View.GONE);
                    rlBg.setVisibility(View.VISIBLE);
                    String videoPath = split[0];
                    File file = new File(path);
                    Glide.with(SendDynamicActivity.this).load(StringUtil.changeUrl(split[2])).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = DensityUtil.dip2px(SendDynamicActivity.this, 150);
                            double w=(resource.getWidth())*1.0;
                            int h=resource.getHeight();
                            double fx=width/w;
                            int height= (int) (h*fx);


                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( DensityUtil.dip2px(SendDynamicActivity.this, 150),height>DensityUtil.dip2px(SendDynamicActivity.this, 150)?height-25:height);
                             params.leftMargin=DensityUtil.dip2px(SendDynamicActivity.this, 16);
                             params.topMargin=DensityUtil.dip2px(SendDynamicActivity.this, 15);
                             params.bottomMargin=DensityUtil.dip2px(SendDynamicActivity.this, 15);

                            rlBg.setLayoutParams(params);
                            RelativeLayout.LayoutParams params1=new RelativeLayout.LayoutParams(DensityUtil.dip2px(SendDynamicActivity.this, 150),height>DensityUtil.dip2px(SendDynamicActivity.this, 150)?height-25:height);
                            params1.setMargins(12,12,12,12);
                            ivVideoCover.setLayoutParams(params1);
                            ivVideoCover.setImageBitmap(resource);
                        }
                    });

                    if (split.length == 5) {
                        video_url=split[0]+"!@##@!"+split[2]+"!@##@!"+split[4];

                    }
                }
            }


            ivVideoCover.setVisibility(View.VISIBLE);
            llBg.setVisibility(View.GONE);
            ivVideoCover.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        if (resultCode == 102) {
            Log.i("CJT", "video");
            String path = data.getStringExtra("path");
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //上传照片
                case 66:
                    List<String> imgs = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
                    rlBg.setVisibility(View.GONE);
                    llBg.setVisibility(View.VISIBLE);
                    piuvRemarkImage.setVisibility(View.VISIBLE);
                    for (String imgPath : imgs) {
                        if (imgPath != null) {
                            UploadFileBean bean = new UploadFileBean(1);
                            bean.setForderPath(imgPath);
                            img_path.add(bean);
                        }
                    }

                    haveFous(true);
                    notifyImageDataChange();
                    upload_position = 0;
                    break;

            }
        }
    }


    public void getUpImgSuccdess(String info) {
        if (img_path.size() > i) {
            if (i == 1) {
                url = info;
            } else {
                url = url + "," + info;
            }
            upFlile(img_path.get(i).getForderPath());
            i++;
        } else {
            if (i == 1) {
                url = info;
            } else {
                url = url + "," + info;
            }

            presenter.addInfo(etContent.getText().toString(), url);
        }
    }

    private String token;

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
                token = response.body().data.upToken;
                upFlile(path);
            }

            @Override
            public void onFailure(Call<TOKEN> call, Throwable t) {

            }
        });
    }

    public void upFlile(String path) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {


                getUpImgSuccdess(urls);

            }
        });

    }

    private void notifyImageDataChange() {
        if (img_path == null || img_path.size() == 0) {
            ivAddPhoto.setVisibility(View.VISIBLE);
            piuvRemarkImage.setVisibility(View.GONE);
        } else {
            piuvRemarkImage.setVisibility(View.VISIBLE);
            ivAddPhoto.setVisibility(View.GONE);
        }
        piuvRemarkImage.setData(img_path);
    }

    public void showAddPhotoWindow(int count) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_add_photo_windows, null);

        view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotos();
            }
        });
        view.findViewById(R.id.tv_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SendDynamicActivity.this, CameraActivity.class), 100);
                mWindowAddPhoto.dismiss();
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowAddPhoto.dismiss();


            }
        });
        mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
        if (count == 3 || count == 1) {
            mWindowAddPhoto.showAtLocation(llBottomButton, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(this.findViewById(R.id.ll_bottom_button), Gravity.BOTTOM, 0, 0);
        }


    }


    public void getPhotos() {
        Picker.from(this)
                .count(count - img_path.size())
                .enableCamera(true)
                .setEngine(new ImageLoaderEngine())
                .setAdd_watermark(false)
                .forResult(66);
        mWindowAddPhoto.dismiss();
    }

    private PhotoPopupWindow mWindowAddPhoto;

    public void sendSuccess() {
        dimessProgress();
        finish();
        showToasts(getString(R.string.fbcg));
        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.QFRIED_FRESH, ""));
    }


    @Override
    public <T> void toEntity(T entity, int type) {
        sendSuccess();
    }

    @Override
    public void toNextStep(int type) {

    }


    private MarkaBaseDialog dialog;

    public void showPopwindows() {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText(R.string.sftcccbj);
        dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                dialog.dismiss();

            }
        });
    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
        haveFous(true);
        showToasts(msg);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtil.isNotEmpty(etContent.getText().toString()) || (img_path.size() > 0)) {
                showPopwindows();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



}
