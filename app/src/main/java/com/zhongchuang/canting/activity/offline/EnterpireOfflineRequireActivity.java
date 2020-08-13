package com.zhongchuang.canting.activity.offline;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.EditorPersonDetailActivity;
import com.zhongchuang.canting.activity.mine.clip.ClipActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.AreaDto;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.OfflineBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.ui.EaseBaiduMapActivity;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.ToastUtil;
import com.zhongchuang.canting.utils.location.LocationUtil;
import com.zhongchuang.canting.widget.ImageUploadView;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.TimeSelectorDialog;
import com.zhongchuang.canting.widget.popupwindow.ShopTypeWindows;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.ImageSelectActivity;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.widget.ImageLoaderEngine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnterpireOfflineRequireActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.civ_user_avatar)
    ImageView      ivlogo;

    @BindView(R.id.et_content1)
    EditText       etContent1;
    @BindView(R.id.et_content2)
    EditText       etContent2;
    @BindView(R.id.et_content3)
    EditText       etContent3;
    @BindView(R.id.iv_img11)
    ImageView      ivImg11;
    @BindView(R.id.iv_img22)
    ImageView      ivImg22;
    @BindView(R.id.iv_img33)
    ImageView      ivImg33;
    @BindView(R.id.cb_register_check_box)
    CheckBox       cbRegisterCheckBox;
    @BindView(R.id.tv_submit)
    TextView       tvSubmit;
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo;
    @BindView(R.id.iv_img44)
    ImageView      ivImg44;
    @BindView(R.id.iv_img55)
    ImageView      ivImg55;
    @BindView(R.id.tv_type)
    TextView       tvType;
    @BindView(R.id.tv_time)
    TextView       tv_time;
    @BindView(R.id.tv_location)
    TextView       tv_location;
    @BindView(R.id.line)
    View           line;
    @BindView(R.id.ll_choose)
    LinearLayout   ll_choose;
    @BindView(R.id.ll_time)
    LinearLayout   ll_time;
    @BindView(R.id.ll_location)
    LinearLayout   ll_location;

    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.piuv_remark_image)
    ImageUploadView piuvRemarkImage;

    private BasesPresenter presenter;
    private String ids;
    private ShopTypeWindows shoptypeWindow;
    private String logo;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_enterprise_offline);
        ButterKnife.bind(this);
        tvTitleText.setText("线下店铺申请入驻");
        presenter = new BasesPresenter(this);
        presenter.getHomeBanner("1");
        getUpToken();
        bean = new OfflineBean();
        dto =new ArrayList<>();
    }
    private OfflineBean bean;
    private String token;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                if(img_path.size()!=0){
                    upFlile(img_path.get(0).getForderPath());
                }
            }else {
                if (TextUtil.isNotEmpty(path)&&TextUtil.isNotEmpty(token)) {
                    upFlile(path);
                } else {
                    getUpToken();
                }
            }


            return false;

        }
    });

    private String url;
    private int i = 1;
    public void getUpImgSuccdess(String info) {
        if (img_path.size() > i) {
            if (i == 1) {
                url = info;
            } else {
                if (TextUtil.isNotEmpty(url)) {
                    url = url + "," + info;
                } else {
                    url = info;
                }

            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            upFlile(img_path.get(i++).getForderPath());

        } else {
            if (i == 1) {
                url = info;
            } else {
                url = url + "," + info;
            }
//            presenter.addInfo(etContent.getText().toString(), url);
        }
    }

    public void upFlile(String path) {

        if (path.contains("http") || path.contains("\"\"")) {

            getUpImgSuccdess(path);
        } else {
            QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
                @Override
                public void completeListener(String urls) {
                    if(status==1){
                        status=0;
                      switch (code){
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG0 :
                              bean.head_image = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.head_image)).asBitmap().placeholder(R.drawable.moren).into(ivlogo);
                              break;
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 :
                              bean.business_url = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.business_url)).asBitmap().placeholder(R.drawable.moren3).into(ivImg11);

                              break;
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG2 :
                              bean.front_id_card = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.front_id_card)).asBitmap().placeholder(R.drawable.moren3).into(ivImg22);

                              break;
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG3 :
                              bean.negative_id_card = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.negative_id_card)).asBitmap().placeholder(R.drawable.moren3).into(ivImg33);

                              break;
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG4 :
                              bean.license_img = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.license_img)).asBitmap().placeholder(R.drawable.moren3).into(ivImg44);

                              break;
                          case Constants.INTENT_REQUESTCODE_VERIFIED_IMG5 :
                              bean.brand_img = QiniuUtils.baseurl+urls;
                              Glide.with(EnterpireOfflineRequireActivity.this).load(StringUtil.changeUrl( bean.brand_img)).asBitmap().placeholder(R.drawable.moren3).into(ivImg55);

                              break;
                      }

                    }else {
                        getUpImgSuccdess(urls);
                    }



                }
            });
        }


    }
    private TimeSelectorDialog selectorDialog;
    private String star;
    private String end;
    private String mTime;
    public void showDailog(final  int type){
        mTime="";
        if (selectorDialog == null) {
            selectorDialog = new TimeSelectorDialog(this);
        }
        selectorDialog.setDate(new Date(System.currentTimeMillis()))
                .setBindClickListener(new TimeSelectorDialog.BindClickListener() {
                    @Override
                    public void time(String time) {
                        if(type==1){
                            star = time;
                            showDailog(2);

                        }else {
                            end = time;
                            mTime=star +"——"+ end;
                            tv_time.setText(mTime);
                        }
                    }
                });
        selectorDialog.show(ivAddPhoto);
    }
    @Override
    public void bindEvents() {
        piuvRemarkImage.setOnActionListener(new ImageUploadView.OnActionListener() {
            @Override
            public void onItemDelete(int position) {
                img_path.remove(position);
                upload_position = 0;
                i = 1;
                if (TextUtil.isNotEmpty(token)) {
                    handler.sendEmptyMessage(1);
                } else {
                    getUpToken();
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
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoptypeWindow.selectData(dto, "选择行业分类" );
                closeKeyBoard();
                shoptypeWindow.showAsDropDown(line);
            }
        });
        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(1);
            }
        });
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterpireOfflineRequireActivity.this, EaseBaiduMapActivity.class);
                intent.putExtra("latitude", LocationUtil.latitude);
                intent.putExtra("longitude", LocationUtil.latitude);
                intent.putExtra("address", LocationUtil.city);
                intent.putExtra("state",1);
                startActivityForResult(intent,999);
            }
        });

        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                closeKeyBoard();
                PermissionGen.with(EnterpireOfflineRequireActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA)
                        .request();

            }
        });
    }

    private int count = 9;
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
    public void showAddPhotoWindow(int count) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_add_photo_window, null);

        view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotos();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowAddPhoto.dismiss();


            }
        });
        mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
        mWindowAddPhoto.showAtLocation(ivAddPhoto, Gravity.BOTTOM, 0, 0);


    }

    @Override
    public void initData() {
        getTags();
        shoptypeWindow = new ShopTypeWindows(this);
        shoptypeWindow.setSureListener(new ShopTypeWindows.ClickListener() {
            @Override
            public void clickListener(String id, String name) {
                ids=id;
                tvType.setText(name);
            }
        });
    }
    private List<AreaDto> dto =null;//标签数据
    public void getTags(){
//        DataManager.getInstance().getInducts(new DefaultSingleObserver<Param>() {
//            @Override
//            public void onSuccess(Param result) {
//                dissLoadDialog();
//
//                if(result!=null){
//                    dto=result.data;
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                dissLoadDialog();
//                if (ApiException.getInstance().isSuccess()) {
//
//                } else {
//                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
//                }
//
//            }
//        });

    }


    @OnClick({
            R.id.iv_img11,
            R.id.iv_img22,
            R.id.iv_img33,
            R.id.iv_img44,
            R.id.iv_img55,
            R.id.rl_logo,

            R.id.tv_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_img11:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(1);
                break;
            case R.id.iv_img22:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(2);
                break;
            case R.id.iv_img33:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(3);
                break;
            case R.id.iv_img44:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(4);
                break;
            case R.id.iv_img55:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(5);
                break;
            case R.id.rl_logo:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(0);
                break;

            case R.id.tv_submit:
                upSellers();
                break;
        }

    }

    private int code;

    public void selectPictur(int poition) {
        type = 1;
        if (poition == 0) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG0;
        } else if (poition == 1) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG1;
        } else if (poition == 2) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG2;
        } else if (poition == 3) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG3;
        } else if (poition == 4) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG4;
        } else if (poition == 5) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG5;
        }
        PermissionGen.with(EnterpireOfflineRequireActivity.this)
                .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
        closeKeyBoard();



    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        if (type == 1) {
            View view = LayoutInflater.from(EnterpireOfflineRequireActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
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
                            imageUri = FileProvider.getUriForFile(EnterpireOfflineRequireActivity.this, "com.zhongchuang.canting.provider", fileUri);
                        PhotoUtils.takePicture(EnterpireOfflineRequireActivity.this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(EnterpireOfflineRequireActivity.this, getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
                        Log.e("asd", "设备没有SD卡");
                    }
                    mWindowAddPhotos.dismiss();
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

                    mWindowAddPhotos.dismiss();
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhotos.dismiss();
                }
            });
            mWindowAddPhotos = new PhotoPopupWindow(EnterpireOfflineRequireActivity.this).bindView(view);
            mWindowAddPhotos.showAtLocation(ivAddPhoto, Gravity.BOTTOM, 0, 0);
        } else {
            showAddPhotoWindow(count - img_path.size());
        }


    }

    public PhotoPopupWindow mWindowAddPhotos;
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
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();
    private static final int CLIP_PHOTO = 0x04;
    private ArrayList<String> urs = new ArrayList<>();
    private ArrayList<UploadFileBean> mImageAttachmentList;//图片附件
    private int upload_position;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 360, output_Y = 360;
        int output_X1 = 320, output_Y1 = 180;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //上传照片
                case 66:
                    urs.clear();
                    List<String> imgs = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
                    piuvRemarkImage.setVisibility(View.VISIBLE);
                    for (String imgPath : imgs) {
                        if (imgPath != null) {
                            UploadFileBean bean = new UploadFileBean(1);
                            bean.setForderPath(imgPath);
                            img_path.add(bean);
                        }
                    }
                    notifyImageDataChange();
                    upload_position = 0;
                    if (TextUtil.isNotEmpty(token)) {
                        handler.sendEmptyMessage(1);
                    } else {
                        getUpToken();
                    }
                    break;
                case CLIP_PHOTO:
                    if (data.getParcelableArrayListExtra("result") != null && data.getParcelableArrayListExtra("result").size() > 0) {
                        if (mImageAttachmentList == null) {
                            mImageAttachmentList = new ArrayList<>();
                        }
                        mImageAttachmentList.clear();
                        ArrayList list = data.getParcelableArrayListExtra("result");

                        mImageAttachmentList.addAll(list);
                        for (UploadFileBean imgPath : mImageAttachmentList) {
                            if (imgPath != null) {
                                UploadFileBean bean = new UploadFileBean(1);
                                bean.setForderPath(imgPath.getUrl_());
                                img_path.add(bean);
                            }
                        }


                        notifyImageDataChange();
                        upload_position = 0;

                        if (TextUtil.isNotEmpty(token)) {
                            handler.sendEmptyMessage(1);
                        } else {
                            getUpToken();
                        }

                    }
                    break;
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    if (code == Constants.INTENT_REQUESTCODE_VERIFIED_IMG0) {
                        PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);

                    }else {
                        PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 16, 9, output_X1, output_Y1, CODE_RESULT_REQUEST);

                    }
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zhongchuang.canting.provider", new File(newUri.getPath()));
                        if (code == Constants.INTENT_REQUESTCODE_VERIFIED_IMG0) {
                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);

                        }else {
                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 16, 9, output_X1, output_Y1, CODE_RESULT_REQUEST);

                        }
                    } else {
                        Toast.makeText(EnterpireOfflineRequireActivity.this, getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    upPhotos(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg" );
                    path = Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg";
                    status=1;
                    handler.sendEmptyMessage(2);
//                    Glide.with(this).load(path).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(img);
                case 999:
                    bean.latitude =data.getDoubleExtra("latitude",0.0)+"";
                    bean.longitude =data.getDoubleExtra("longitude",0.0)+"";
                    bean.merAddress = data.getStringExtra("address");
                    tv_location.setText(bean.merAddress);
                default:
            }
        }
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
    private void getUpToken() {
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
                handler.sendEmptyMessage(1);

            }

            @Override
            public void onFailure(Call<TOKEN> call, Throwable t) {

            }
        });
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    public String path;
    private int status=0;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private int type = 1;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;

    private void uploadImg(String imgPath, final int poition) {

    }

    private List<String> imgs = new ArrayList<>();
    private List<String> imgs1 = new ArrayList<>();
    private List<String> imgs2 = new ArrayList<>();
    private List<String> imgs3 = new ArrayList<>();
    private String       content;
    private String       contents;
    private String       content1;
    private String       content2;

    public void upSellers() {


        if (TextUtils.isEmpty(etContent1.getText().toString().trim())) {
            ToastUtil.showToast("请输入店铺名称");
            return;
        }
        bean.merName =etContent1.getText().toString().trim();
        if (TextUtils.isEmpty(etContent2.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人姓名");
            return;
        }
        bean.linkMan =etContent2.getText().toString().trim();
        if (TextUtils.isEmpty(etContent3.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人电话");
            return;
        }
        bean.linkPhone =etContent1.getText().toString().trim();
        bean.merPhone =etContent1.getText().toString().trim();
        bean.creat_phone =SpUtil.getMobileNumber(this);
        if (TextUtils.isEmpty(tvType.getText().toString().trim())) {
            ToastUtil.showToast("请输入选择行业分类");
            return;
        }
        bean.category_id =ids;
        if (TextUtils.isEmpty(img1)) {
            ToastUtil.showToast("请添加企业营业执照");
            return;
        }
        bean.business_url =img1;
        if (TextUtils.isEmpty(img2)) {
            ToastUtil.showToast("请添加法人身份证正面");
            return;
        }
        bean.front_id_card =img2;
        if (TextUtils.isEmpty(img3)) {
            ToastUtil.showToast("请添加法人身份证反面");
            return;
        }
        bean.negative_id_card =img3;
        if (TextUtils.isEmpty(img4)) {
            ToastUtil.showToast("添加行业经营许可证");
            return;
        }
        bean.license_img =img4;
        if (TextUtils.isEmpty(img5)) {
            ToastUtil.showToast("添加品牌授权资质");
            return;
        }
        bean.brand_img =img5;
        if (TextUtils.isEmpty(img6)) {
            ToastUtil.showToast("请上传logo");
            return;
        }
        bean.shop_logo =img6;
        if (!cbRegisterCheckBox.isChecked()) {
            ToastUtil.showToast("请同意相关条款");
            return;
        }
        if(TextUtil.isNotEmpty(bean.shop_urls)){
            bean.shop_urls=url;
        }

        content = "";
        imgs1.clear();
        imgs.clear();
        imgs2.clear();
        imgs3.clear();
        imgs1.add(img5);
        imgs2.add(img1);
        imgs3.add(img4);
        imgs.add(img2);
        imgs.add(img3);
        Gson g = new Gson();
        content = g.toJson(imgs);
        contents = g.toJson(imgs1);
        content1 = g.toJson(imgs2);
        content2 = g.toJson(imgs3);
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "enterprise");
        map.put("shop_name", etContent1.getText().toString().trim());
        map.put("name", etContent2.getText().toString().trim());
        map.put("phone", etContent3.getText().toString().trim());
        map.put("industry", ids);
        map.put("logo", img6);
        map.put("id_cards[0]", img1);
        map.put("id_cards[1]", img2);
//        map.put("id_cards", content);
        map.put("credentials", contents);
        map.put("ext[business_license]", content1);
        map.put("ext[license]", content2);
//        StoreInfo info=new StoreInfo();
//        info.type="enterprise";
//        info.shop_name=etContent1.getText().toString().trim();
//        info.name=etContent2.getText().toString().trim();
//        info.phone=etContent3.getText().toString().trim();
//        info.industry=ids;
//        info.logo=img6;
//        info.id_cards=imgs;
//        info.credentials=imgs1;
//        StoreInfo storeInfo = new StoreInfo();
//        storeInfo.license=img1;
//        storeInfo.business_license=img4;
//
//        info.ext=storeInfo;
//        info.ext.business_license=img4;
//        info.credentials=imgs1;
//        DataManager.getInstance().upSellers(new DefaultSingleObserver<HttpResult<Object>>() {
//            @Override
//            public void onSuccess(HttpResult<Object> result) {
//                dissLoadDialog();
//                ToastUtil.showToast("提交成功，请等待审核");
//                finish();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                dissLoadDialog();
//                if (ApiException.getInstance().isSuccess()) {
//                    ToastUtil.showToast("提交成功，请等待审核");
//                    finish();
//                } else {
//                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
//                }
//
//            }
//        }, info);
    }


    List<Banner> category;
    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 6) {
            Home home = (Home) entity;
            category = home.category;
            dto.clear();
            if(category!=null && category.size()>0){
                for(Banner banner :category ){
                    AreaDto areaDto = new AreaDto();
                    areaDto.id = banner.id;
                    areaDto.title = banner.category_name;
                    dto.add(areaDto);
                }
            }

        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
