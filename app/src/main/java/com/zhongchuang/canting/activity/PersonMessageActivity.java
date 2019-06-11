package com.zhongchuang.canting.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.activity.mine.PersonManActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseLoginActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.UpPhotoBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.PersonInfoPresenter;
import com.zhongchuang.canting.presenter.impl.PersonInfoPresenterImpl;
import com.zhongchuang.canting.utils.DateUtils;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.UploadUtil;
import com.zhongchuang.canting.viewcallback.GetUserInfoViewCallback;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.picker.TimePickerView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/14.
 */

public class PersonMessageActivity extends BaseLoginActivity implements GetUserInfoViewCallback {

    @BindView(R.id.person_photo)
    ImageView personPhoto;
    @BindView(R.id.nick_et)
    EditText nickEt;
    @BindView(R.id.id_et)
    TextView idEt;
    @BindView(R.id.bridthday_btn)
    TextView bridthdayBtn;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.save_btn)
    TextView saveBtn;
    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_woman)
    RadioButton radioWoman;
    @BindView(R.id.tv_sva)
    TextView tvSva;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_first)
    LinearLayout llFirst;
    @BindView(R.id.editor)
    TextView editor;

    private PersonInfoPresenter personInfoPresenter;
    private Unbinder unbind;
    private Context mContext;
    private TimePickerView timePickerView;
    private int yearStr;
    private int monthStr;
    private int dayStr;
    private Gson mGson;
    private UserInfo userInfo = CanTingAppLication.getInstance().getUserInfo();
    private String id;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private int type = 0;

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.personal_message);
        mContext = this;
        type = getIntent().getIntExtra("type", 0);
        int loginCounts = SpUtil.getInt(PersonMessageActivity.this, "loginCounts", 0);
        if (loginCounts == 0 || loginCounts == 1 || loginCounts == 2) {
            SpUtil.putInt(this, "loginCounts", loginCounts + 1);

        }
        unbind = ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        String mobileNumber = SpUtil.getMobileNumber(this);//手机号

        idEt.setText(mobileNumber);
        initView();
        setEvents();
    }

    private void initView() {
        mGson = new Gson();
        personInfoPresenter = new PersonInfoPresenterImpl(this);
        Map<String, String> map = new HashMap<>();
        String token = SpUtil.getString(this, "token", "");
        map.put("token", token);
        if (TextUtil.isNotEmpty(id)) {
            map.put("userInfoId", id);
        } else {
            map.put("userInfoId", SpUtil.getUserInfoId(this));
        }
        if (type == 0) {
            personInfoPresenter.getUserInfo(map);
            llFirst.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            llFirst.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
        }

        setBridthDayTv(null);
    }

    private void setEvents() {
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonMessageActivity.this, NewPersonDetailActivity.class);
                intent.putExtra("id", SpUtil.getUserInfoId(PersonMessageActivity.this));
                startActivity(intent);
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonMessageActivity.this, HomeActivitys.class);
                startActivity(intent);
                PersonMessageActivity.this.finish();
            }
        });
        tvSva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSunbmit();
            }
        });
    }


    private void showTimeSelector() {

        if (timePickerView == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1990, 9, 18);
            timePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    setBridthDayTv(date);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText( getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.xz))//确认按钮文字
                    .setContentSize(20)//滚轮文字大小
                    .setTitleSize(18)//标题文字大小
                    .setTitleText(getString(R.string.xzrq))//标题文字
                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                    .setTitleColor(getResources().getColor(R.color.text_color_57))//标题文字颜色
                    .setSubmitColor(getResources().getColor(R.color.original))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.text_color_159))//取消按钮文字颜色
//                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                    .setRange(1950, 2010)
                    .setDate(calendar)
                    .setLabel( getString(R.string.yea),  getString(R.string.yeu),  getString(R.string.riis), "", "", "")
                    .setLineSpacingMultiplier(2f)
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)
                    .build();
        }
        timePickerView.show();
    }

    private void setBridthDayTv(Date date) {
        if (date != null) {
            String str = DateUtils.formatDate(date, "yyyy-MM-dd");
            String strs = DateUtils.formatDate(date, "yyyy-MM-dd");
            if (userInfo == null) {
                userInfo = new UserInfo();
            }
            userInfo.birthday = strs;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            LogUtil.d(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + getString(R.string.nxzdrq) + str);
            bridthdayBtn.setText(str);
            yearStr = calendar.get(Calendar.YEAR);
            monthStr = calendar.get(Calendar.MONTH) + 1;
            dayStr = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            bridthdayBtn.setText("");
            yearStr = 0;
            monthStr = 0;
            dayStr = 0;
        }
    }

    private boolean checkData() {
        return !TextUtils.isEmpty(nickEt.getText().toString());
    }

    private void changeSunbmit() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        if (radioMan.isChecked()) {
            userInfo.sex = 1 + "";
        } else {
            if (radioWoman.isChecked()) {
                userInfo.sex = 2 + "";
            } else {
                userInfo.sex = 3 + "";
            }
        }
        userInfo.nickname = nickEt.getText().toString().trim();
        userInfo.accountId = idEt.getText().toString().trim();
        userInfo.personalitySign = "";
        Map<String, String> map = new HashMap<>();
        String token = SpUtil.getString(this, "token", "");

        if (!TextUtils.isEmpty(userInfo.headImage)) {
            map.put("headImage", userInfo.headImage);
        } 
        if (!TextUtils.isEmpty(userInfo.nickname)) {
            map.put("nickname", userInfo.nickname);
        } else {
            ToastUtils.showNormalToast(getString(R.string.qtxndnc));
            return;
        }

//        String userInfoId = SpUtil.getUserInfoId(this);
//            map.put("user_info_id", userInfoId);


        map.put("sex", userInfo.sex + "");
        map.put("userInfoId", SpUtil.getUserInfoId(this));

//        if (!TextUtils.isEmpty(userInfo.accountId)) {
//            map.put("accountId", userInfo.accountId);
//        }
        if (!TextUtils.isEmpty(userInfo.birthday)) {
            map.put("birthday", userInfo.birthday);
        }
        if (!TextUtils.isEmpty(userInfo.personalitySign)) {
            map.put("personalitySign", userInfo.personalitySign);
        }
        personInfoPresenter.submitChange(map);
        hidePress();
    }

    @OnClick({R.id.back_btn, R.id.save_btn, R.id.person_photo, R.id.bridthday_btn})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.save_btn:   //保存
                changeSunbmit();
                break;
            case R.id.person_photo:  //头像
                PermissionGen.with(PersonMessageActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();
                break;
            case R.id.bridthday_btn:  //选择生日
                showTimeSelector();
                break;
        }
    }

    private int pos = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == 0) {
                this.finish();
            } else {

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        View view = LayoutInflater.from(PersonMessageActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
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
                        imageUri = FileProvider.getUriForFile(PersonMessageActivity.this, "com.zhongchuang.canting.provider", fileUri);
                    PhotoUtils.takePicture(PersonMessageActivity.this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    Toast.makeText(PersonMessageActivity.this,  getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
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
        mWindowAddPhoto = new PhotoPopupWindow(PersonMessageActivity.this).bindView(view);
        mWindowAddPhoto.showAtLocation(backBtn, Gravity.BOTTOM, 0, 0);

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
                        Toast.makeText(PersonMessageActivity.this,  getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    upPhotos(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg" );
                    path = Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg";
                    upPhotos(path);
                    Glide.with(this).load(path).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(personPhoto);
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

    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardFailed() {
        ToastUtils.showNormalToast(getString(R.string.hqqxsb));
    }

    @Override
    public void onResultSuccess(BaseResponse response) {
        ToastUtils.showEnterMsgImage(response.getMessage(), -1);
        SpUtil.putString(this, "name", nickEt.getText().toString().trim());//环信登录密码
        SpUtil.putInt(this, "loginCounts", 8);//环信登录密码
        CanTingAppLication.getInstance().mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (type == 1) {
                    Intent intent = new Intent(PersonMessageActivity.this, HomeActivitys.class);
                    startActivity(intent);
                    finish();

                } else {
                    finish();
                }

            }
        }, 600);
    }

    private void upPhotos(final String path) {
        final Map<String, String> map = new HashMap<>();
        final Map<String, File> maps = new HashMap<>();
        maps.put("file", new File(path));
        String userInfoId = SpUtil.getUserInfoId(this);
        map.put("userInfoId", userInfoId);
        showPress(getString(R.string.scz));
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = "";
                try {
                    url = UploadUtil.UpLoadImg(PersonMessageActivity.this, "common/upload/images", map, maps);


                    hidePress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onStart();
                subscriber.onNext(url);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        UpPhotoBean info = mGson.fromJson(s, UpPhotoBean.class);
                        if (userInfo == null) {
                            userInfo = new UserInfo();
                        }
                        SpUtil.putString(PersonMessageActivity.this, "ava", info.data.get(0).path);
                        userInfo.headImage = info.data.get(0).path;

                    }
                });


//        //添加文件的
//
//        ContentBody contentBody=new FileBody(file);
//        map.put("file",contentBody);
//        netService api = HttpUtil.getInstance().create(netService.class);
//        api.upPhotos(map).enqueue(new BaseCallBack<BaseBean1>() {
//            @Override
//            public void onSuccess(BaseBean1 goodsSpeCate) {
//
//            }
//
//            @Override
//            public void onOtherErr(int code, String t) {
//                super.onOtherErr(code, t);
//                ToastUtils.showNormalToast(t);
//            }
//        });
    }

    public String path;

    @Override
    public void onFail(int code, String msg) {
        ToastUtils.showEnterMsgImage(msg, -1);
    }

    @Override
    protected void _onDestroy() {
        unbind.unbind();
    }

    private String headurl;

    @Override
    public void getUserSuccess(UserInfo userInfos) {
        if (!TextUtils.isEmpty(userInfos.headImage)) {
            Glide.with(this).load(StringUtil.changeUrl(userInfos.headImage)).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(personPhoto);
            if (userInfo == null) {
                userInfo = new UserInfo();
            }
            userInfo.headImage = userInfos.headImage;
        }
        if (!TextUtils.isEmpty(userInfos.nickname)) {
            SpUtil.putString(this, "name", userInfos.nickname);//环信登录密码
            nickEt.setText(userInfos.nickname);
        }
        if (!TextUtils.isEmpty(userInfos.sex)) {
            if (userInfos.sex.equals("1")) {
                radioMan.setChecked(true);
            } else if (userInfos.sex.equals("2")) {
                radioWoman.setChecked(true);
            }
        }

        if (!TextUtils.isEmpty(userInfos.mobileNumber)) {
            idEt.setText(userInfos.mobileNumber);
        }

        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        if (!TextUtils.isEmpty(userInfos.birthdayDay) && (!userInfos.birthdayYear.equals("null"))) {
            bridthdayBtn.setText(userInfos.birthdayYear +  getString(R.string.yea) + userInfos.birthdayMonth +  getString(R.string.yeu) + userInfos.birthdayDay +  getString(R.string.riis));
            userInfo.birthday = userInfos.birthdayYear + "-" + userInfos.birthdayMonth + "-" + userInfos.birthdayDay;
        }

    }


}
