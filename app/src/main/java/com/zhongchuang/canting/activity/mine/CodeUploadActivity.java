package com.zhongchuang.canting.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Codes;
import com.zhongchuang.canting.been.Hands;
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
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.PhotoPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.widget.ImageLoaderEngine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * 二维码上传
 */
public class CodeUploadActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.iv_add_photos)
    ImageView ivAddPhotos;
    @BindView(R.id.tv_zfb)
    TextView tvZfb;
    private List<File> files = new ArrayList<>();
    private BasesPresenter presenter;
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/code.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_code.jpg");
    private File fileCropUris = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_codes.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_code_upload);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(CodeUploadActivity.this).clearDiskCache();// BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
            }
        }).start();
        presenter.getCodes(1+"");
        presenter.getCodes(2+"");

    }

    private String url;

    @Override
    public void bindEvents() {

        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                selectPic();


            }
        });
        ivAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                selectPic();


            }
        });
        tvWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                selectPic();
            }
        });
        tvZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                selectPic();
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

    private int type=1;//1 wx ,2 zfb
    private View view;
    private String url_wx;
    private String url_zfb;
    public void selectPic(){

        if(view==null){
            view = LayoutInflater.from(CodeUploadActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
            TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
            TextView tv_choose = (TextView) view.findViewById(R.id.tv_choose);
            TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
            tv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            //通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(CodeUploadActivity.this, "com.zhongchuang.canting.provider", fileUri);
                        PhotoUtils.takePicture(CodeUploadActivity.this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(CodeUploadActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
                        Log.e("asd", "设备没有SD卡");
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

            mWindowAddPhoto = new PhotoPopupWindow(CodeUploadActivity.this).bindView(view);
            mWindowAddPhoto.showAtLocation(ivAddPhoto, Gravity.BOTTOM, 0, 0);
        }else {
            mWindowAddPhoto.showAtLocation(ivAddPhoto, Gravity.BOTTOM, 0, 0);
        }


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
        int output_X = 280, output_Y = 280;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(type==1?fileCropUri:fileCropUris);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(type==1?fileCropUri:fileCropUris);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zhongchuang.canting.provider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(CodeUploadActivity.this, getString(R.string.sbmys), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if(type==1){
                        path = Environment.getExternalStorageDirectory().getPath() + "/crop_code.jpg";
                    }else {
                        paths = Environment.getExternalStorageDirectory().getPath() + "/crop_codes.jpg";
                    }


                    if(type==1){
                        Glide.with(CodeUploadActivity.this).load(path).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhoto);
                        tvWx.setText(R.string.ghewm);
                        tvWx.setBackground(getResources().getDrawable(R.drawable.hui_bg));
                    }else {
                        Glide.with(CodeUploadActivity.this).load(paths).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhotos);
                        tvZfb.setText(getString(R.string.ghewm));
                        tvZfb.setBackground(getResources().getDrawable(R.drawable.hui_bg));
                    }


                    showProgress(getString(R.string.scz));
                    getUpToken(type==1?path:paths);
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
    public String paths;


    public void upFlile(String path, String token) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {

                presenter.uploadCode(type+"",QiniuUtils.baseurl+urls);
                if(type==1){
                    url_wx=QiniuUtils.baseurl+urls;
                }else {
                    url_zfb=QiniuUtils.baseurl+urls;
                }
                if(type==1){
                    Glide.with(CodeUploadActivity.this).load(StringUtil.changeUrl(url_wx)).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhoto);
                }else {
                    Glide.with(CodeUploadActivity.this).load(StringUtil.changeUrl(url_zfb)).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhotos);
                }
            }
        });

    }

    private void notifyImageDataChange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(CodeUploadActivity.this).clearDiskCache();// BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
            }
        }).start();
            if(type==1){
                Glide.with(this).load(new File(path)).asBitmap().placeholder(R.drawable.add_img).into(ivAddPhoto);
            }else {
                Glide.with(this).load(new File(paths)).asBitmap().placeholder(R.drawable.add_img).into(ivAddPhotos);
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
        if (type == 5) {

            showToasts(getString(R.string.shangccg));

        } else  if (type == 1) {
            Codes code= (Codes) entity;
            if(code==null||TextUtil.isEmpty(code.code_url)){
                return;
            }
            Glide.with(this).load(StringUtil.changeUrl(code.code_url)).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhoto);
            tvWx.setText(getString(R.string.ghewm));
            tvWx.setBackground(getResources().getDrawable(R.drawable.hui_bg));

        }else  if (type == 2) {
            Codes code= (Codes) entity;
            if(code==null||TextUtil.isEmpty(code.code_url)){
                return;
            }
            Glide.with(this).load(StringUtil.changeUrl(code.code_url)).asBitmap().placeholder(R.drawable.codedown).into(ivAddPhotos);
            tvZfb.setText(getString(R.string.ghewm));
            tvZfb.setBackground(getResources().getDrawable(R.drawable.hui_bg));

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
