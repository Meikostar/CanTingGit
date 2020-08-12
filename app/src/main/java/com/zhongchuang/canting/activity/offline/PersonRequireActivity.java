package com.zhongchuang.canting.activity.offline;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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
import com.zhongchuang.canting.activity.mine.clip.ClipActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.AreaDto;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.ToastUtil;
import com.zhongchuang.canting.widget.popupwindow.ShopTypeWindows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.valuesfeng.picker.ImageSelectActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonRequireActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.civ_user_avatar)
    ImageView      ivImg44;

    @BindView(R.id.et_content1)
    EditText       etContent1;
    @BindView(R.id.et_content2)
    EditText       etContent2;
    @BindView(R.id.et_content3)
    EditText     etContent3;
    @BindView(R.id.iv_img11)
    ImageView    ivImg11;
    @BindView(R.id.iv_img22)
    ImageView    ivImg22;
    @BindView(R.id.iv_img33)
    ImageView    ivImg33;
    @BindView(R.id.cb_register_check_box)
    CheckBox     cbRegisterCheckBox;
    @BindView(R.id.tv_submit)
    TextView     tvSubmit;
    @BindView(R.id.tv_type)
    TextView     tvType;
    @BindView(R.id.line)
    View         line;
    @BindView(R.id.ll_choose)
    LinearLayout ll_choose;

    private ShopTypeWindows shoptypeWindow;
    private String ids;
    private BasesPresenter presenter;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_person_reuqair);
        ButterKnife.bind(this);
        tvTitleText.setText("个人申请");
        presenter = new BasesPresenter(this);
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {
        getTags();
        getUpToken();
        shoptypeWindow = new ShopTypeWindows(this);
        shoptypeWindow.setSureListener(new ShopTypeWindows.ClickListener() {
            @Override
            public void clickListener(String id, String name) {
                ids=id;
                tvType.setText(name);


            }
        });
    }
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();
    private Gson mGson;
    private String url;
    private int i = 1;
    private int states = 0;
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
            states = 0;
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
                        SpUtil.putString(PersonRequireActivity.this, "ava", QiniuUtils.baseurl+urls);

                    }else {
                        getUpImgSuccdess(urls);
                    }



                }
            });
        }


    }
    private String token;
    private List<AreaDto>  dto =null;//标签数据
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
    public void getTags(){
        HashMap<String, String> map = new HashMap<String, String>();
        presenter.getInducts(map);


    }


    @OnClick({

            R.id.iv_img11,
            R.id.iv_img22,
            R.id.iv_img33,
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
            case R.id.rl_logo:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(4);
                break;

            case R.id.tv_submit:
                upSellers();
                break;
        }

    }

    private int code;

    public void selectPictur(int poition) {
        if (poition == 1) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG1;
        } else if (poition == 2) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG2;
        } else if (poition == 3) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG3;
        } else if (poition == 4) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG4;
        }
        closeKeyBoard();
        Glide.get(PersonRequireActivity.this).clearMemory();

        type = 1;
        closeKeyBoard();
        PermissionGen.with(PersonRequireActivity.this)
                .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }


    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;


    private List<String> imgs = new ArrayList<>();
    private List<String> imgs1 = new ArrayList<>();
    private String       content;
    private String       contents;

    public void upSellers() {

        if (TextUtils.isEmpty(etContent1.getText().toString().trim())) {
            ToastUtil.showToast("请输入店铺名称");
            return;
        }
        if (TextUtils.isEmpty(etContent2.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(etContent3.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人电话");
            return;
        }
        if (TextUtils.isEmpty(ids)) {
            ToastUtil.showToast("请输入选择行业分类");
            return;
        }
        if (TextUtils.isEmpty(img1)) {
            ToastUtil.showToast("请上传法人身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(img2)) {
            ToastUtil.showToast("请上传法人身份证背面照");
            return;
        }
        if (TextUtils.isEmpty(img3)) {
            ToastUtil.showToast("请上传品牌授权资质");
            return;
        }

        if (TextUtils.isEmpty(img4)) {
            ToastUtil.showToast("请上传logo");
            return;
        }

        if (!cbRegisterCheckBox.isChecked()) {
            ToastUtil.showToast("请同意相关条款");
            return;
        }

        content = "";
        imgs1.clear();
        imgs.clear();
        imgs1.add(img3);
        imgs.add(img1);
        imgs.add(img2);
        Gson g = new Gson();
        content = g.toJson(imgs);
        contents = g.toJson(imgs1);
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "personal");
        map.put("shop_name", etContent1.getText().toString().trim());
        map.put("name", etContent2.getText().toString().trim());
        map.put("phone", etContent3.getText().toString().trim());
        map.put("industry", ids);
        map.put("logo", img4);
        map.put("id_cards[0]", img1);
        map.put("id_cards[1]", img2);
        map.put("credentials", contents);
//        StoreInfo info=new StoreInfo();
//        info.type="personal";
//        info.shop_name=etContent1.getText().toString().trim();
//        info.name=etContent2.getText().toString().trim();
//        info.phone=etContent3.getText().toString().trim();
//        info.industry=ids;
//        info.logo=img4;
//        info.id_cards=imgs;
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
    private void notifyImageDataChange() {
//        if (img_path == null || img_path.size() == 0) {
//            ivAddPhoto.setVisibility(View.VISIBLE);
//            piuvRemarkImage.setVisibility(View.GONE);
//        } else {
//            piuvRemarkImage.setVisibility(View.VISIBLE);
//            ivAddPhoto.setVisibility(View.GONE);
//        }
//        piuvRemarkImage.setData(img_path);
    }
    /**
     * 删除照片和修改相册的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private String urls;
    private static final int CLIP_PHOTO = 0x04;
    private ArrayList<String> urs = new ArrayList<>();
    private ArrayList<UploadFileBean> mImageAttachmentList;//图片附件
    private int upload_position;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 180, output_Y = 180;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //上传照片
                case 66:
                    urls = "";
                    urs.clear();
                    List<String> imgs = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
                    urs.addAll(imgs);
                    Intent intent = new Intent(PersonRequireActivity.this, ClipActivity.class);
                    intent.putExtra("data", urs);
                    startActivityForResult(intent, CLIP_PHOTO);
                    upload_position = 0;
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
                                if (TextUtil.isNotEmpty(urls)) {
                                    urls = imgPath.getUrl_();
                                } else {
                                    urls = urls + "," + imgPath.getUrl_();
                                }
                            }
                        }


                        notifyImageDataChange();
                        upload_position = 0;
                        states = 1;
                        i = 1;
                        if (TextUtil.isNotEmpty(token)) {
                            handler.sendEmptyMessage(1);
                        } else {
                            getUpToken();
                        }

                    }
                    break;
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
                        Toast.makeText(PersonRequireActivity.this, getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    upPhotos(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg" );
                    path = Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg";
                    status=1;
                    handler.sendEmptyMessage(2);
//                    Glide.with(this).load(path).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(img);
                default:
            }
        }
    }

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

    public String path;
    private int status=0;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private int type = 0;

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type == 9) {


        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
