package com.zhongchuang.canting.activity.mine;

import android.Manifest;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.clip.ClipActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UpPhotoBean;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.been.UserInfoBean;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.PersonInfoPresenter;
import com.zhongchuang.canting.presenter.impl.PersonInfoPresenterImpl;
import com.zhongchuang.canting.utils.DateUtils;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.UploadUtil;
import com.zhongchuang.canting.viewcallback.GetUserInfoViewCallback;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.ImageUploadView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.picker.TimePickerView;
import com.zhongchuang.canting.widget.popupwindow.PopView_UploadImg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


//发布朋友圈
public class EditorPersonDetailActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_editor)
    TextView tvEditor;
    @BindView(R.id.but_save)
    Button butSave;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.piuv_remark_image)
    ImageUploadView piuvRemarkImage;
    @BindView(R.id.tv_nc)
    TextView tvNc;
    @BindView(R.id.ll_nc)
    LinearLayout llNc;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.ll_yj)
    LinearLayout llYj;
    @BindView(R.id.ll_qm)
    LinearLayout llQm;
    @BindView(R.id.tv_jx)
    TextView tvJx;
    @BindView(R.id.ll_jx)
    LinearLayout llJx;
    @BindView(R.id.tv_zy)
    TextView tvZy;
    @BindView(R.id.ll_zy)
    LinearLayout llZy;
    @BindView(R.id.ll_xx)
    LinearLayout llXx;
    @BindView(R.id.ll_bq)
    LinearLayout llBq;
    @BindView(R.id.ll_zj)
    LinearLayout llZj;
    @BindView(R.id.ll_sm)
    LinearLayout llSm;
    @BindView(R.id.tv_dy)
    TextView tvDy;
    @BindView(R.id.ll_dy)
    LinearLayout llDy;
    @BindView(R.id.tv_sj)
    TextView tvSj;
    @BindView(R.id.ll_sj)
    LinearLayout llSj;
    @BindView(R.id.tv_song)
    TextView tvSong;
    @BindView(R.id.ll_song)
    LinearLayout llSong;
    @BindView(R.id.ll_bottom_button)
    View llBottomButton;
    @BindView(R.id.tv_qm)
    TextView tvQm;
    @BindView(R.id.tv_xx)
    TextView tvXx;
    @BindView(R.id.tv_bq)
    TextView tvBq;
    @BindView(R.id.tv_sm)
    TextView tvSm;
    @BindView(R.id.tv_ava)
    TextView tvAva;
    @BindView(R.id.iv_img)
    ImageView img;
    @BindView(R.id.tv_sh)
    TextView tvSh;
    @BindView(R.id.ll_sh)
    LinearLayout llSh;
    @BindView(R.id.tv_zw)
    TextView tvZw;
    @BindView(R.id.ll_zw)
    LinearLayout llZw;
    @BindView(R.id.tv_xz)
    TextView tvXz;
    @BindView(R.id.ll_xz)
    LinearLayout llXz;
    @BindView(R.id.tv_cn)
    TextView tvCn;
    @BindView(R.id.ll_cn)
    LinearLayout llCn;
    private int upload_position;

    private BasesPresenter presenter;
    private List<File> files = new ArrayList<>();
    private PopView_UploadImg popView_uploadImg;
    private ArrayList<UploadFileBean> img_path = new ArrayList<>();
    private int i = 1;
    private String url;
    private String content;

    private int count = 9;
    private UserInfoBean bean;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_editor_person_detail);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);

        bean = (UserInfoBean) getIntent().getSerializableExtra("bean");
        if (bean == null) {
            return;
        }
        mGson = new Gson();


        if(TextUtil.isNotEmpty(bean.head_image)){
            Glide.with(this).load(StringUtil.changeUrl(bean.head_image)).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(img);

        }
        if (TextUtil.isNotEmpty(bean.birthday_year) && !bean.birthday_year.equals("null")) {
            bean.birthday = bean.birthday_year + " - " + bean.birthday_month + " - " + bean.birthday_day;
        }
        if (TextUtil.isNotEmpty(bean.image_url)) {
            String[] split = bean.image_url.split(",");
            img_path.clear();


            for (String imgPath : split) {
                if (imgPath != null) {
                    UploadFileBean bean = new UploadFileBean(1);
                    if (!imgPath.contains("http")) {
                        imgPath = QiniuUtils.baseurl + imgPath;
                    }
                    bean.setForderPath(imgPath);
                    img_path.add(bean);


                }
            }
            notifyImageDataChange();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (TextUtil.isNotEmpty(bean.nickname)) {
            tvNc.setText(bean.nickname);
            setTextColor(tvNc);
        }
        llNc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.nickname)) {
                    intent.putExtra("info", bean.nickname);
                }

                intent.putExtra("data", "昵称");
                startActivityForResult(intent, 666);
                state = 1;
            }
        });
        if (TextUtil.isNotEmpty(bean.birthday_year) && !bean.birthday_year.equals("null")) {
            tvDate.setText(bean.birthday_year + " - " + bean.birthday_month + " - " + bean.birthday_day);
            setTextColor(tvDate);
        }
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });
        if (TextUtil.isNotEmpty(bean.personality_sign)) {
            tvQm.setText(bean.personality_sign);
            setTextColor(tvQm);
        }
        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("保存中...");
                changeSunbmit();

                if (img_path.size() > 0) {
                    presenter.saveInformation(bean);
                } else {
                    bean.image_url = "";
                    presenter.saveInformation(bean);
                }


            }
        });
        llQm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.personality_sign)) {
                    intent.putExtra("info", bean.personality_sign);
                }

                intent.putExtra("data", "签名");
                startActivityForResult(intent, 666);
                state = 2;
            }
        });
        if (TextUtil.isNotEmpty(bean.home_town)) {
            tvJx.setText(bean.home_town);
            setTextColor(tvJx);
        }
        llJx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.home_town)) {
                    intent.putExtra("info", bean.home_town);
                }

                intent.putExtra("data", "家乡");
                startActivityForResult(intent, 666);
                state = 3;
            }
        });
        if (TextUtil.isNotEmpty(bean.job)) {
            tvZy.setText(bean.job);
            setTextColor(tvZy);
        }
        llZy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.job)) {
                    intent.putExtra("info", bean.job);
                }

                intent.putExtra("data", "职业");
                startActivityForResult(intent, 666);
                state = 4;
            }
        });
        if (TextUtil.isNotEmpty(bean.graduate_school)) {
            tvXx.setText(bean.graduate_school);
            setTextColor(tvXx);
        }
        llXx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.graduate_school)) {
                    intent.putExtra("info", bean.graduate_school);
                }

                intent.putExtra("data", "学校");
                startActivityForResult(intent, 666);
                state = 5;
            }
        });
        if (TextUtil.isNotEmpty(bean.label)) {
            tvBq.setText(bean.label);
            setTextColor(tvBq);
        }
        llBq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddFlagActivity.class);

                if (TextUtil.isNotEmpty(bean.label)) {
                    intent.putExtra("data", bean.label);
                }
                startActivityForResult(intent, 666);
                state = 6;
            }
        });
        if (TextUtil.isNotEmpty(bean.personal_statement)) {
            tvSm.setText(bean.personal_statement);
            setTextColor(tvSm);
        }
        llSm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, EditorSecondActivity.class);
                if (TextUtil.isNotEmpty(bean.personal_statement)) {
                    intent.putExtra("info", bean.personal_statement);
                }

                intent.putExtra("data", "个人说明");
                startActivityForResult(intent, 666);
                state = 7;
            }
        });
        if (TextUtil.isNotEmpty(bean.movie)) {
            if (!TextUtil.isEmpty(bean.movie)) {
                String[] split = bean.movie.split("&&");
                if (split.length == 2) {
                    tvDy.setText(split[0] + "," + split[1]);
                } else {
                    tvDy.setText(bean.movie);
                }
            }
            tvDy.setText(bean.movie);
            setTextColor(tvDy);
        }
        llDy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.movie)) {
                    intent.putExtra("data", bean.movie);
                }

                intent.putExtra("title", "电影");
                intent.putExtra("type", 1);
                startActivityForResult(intent, 666);
                state = 8;
            }
        });
        if (TextUtil.isNotEmpty(bean.music)) {
            tvSong.setText(bean.music);
            setTextColor(tvSong);
        }
        llSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.music)) {
                    intent.putExtra("data", bean.music);
                }

                intent.putExtra("title", "音乐");
                intent.putExtra("type", 2);
                startActivityForResult(intent, 666);
                state = 9;
            }
        });
        if (TextUtil.isNotEmpty(bean.book)) {
            tvSj.setText(bean.book);
            setTextColor(tvSj);
        }
        llSj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.book)) {
                    intent.putExtra("data", bean.book);
                }
                intent.putExtra("type", 3);
                intent.putExtra("title", "小说");
                startActivityForResult(intent, 666);
                state = 10;
            }
        });

        if (TextUtil.isNotEmpty(bean.cocial_card)) {
            tvSh.setText(bean.cocial_card);
            setTextColor(tvSh);
        }
        llSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.cocial_card)) {
                    intent.putExtra("data", bean.cocial_card);
                }

                intent.putExtra("title", "社会名片");
                intent.putExtra("type", 4);
                startActivityForResult(intent, 666);
                state = 11;
            }
        });
        if (TextUtil.isNotEmpty(bean.self_personality)) {
            tvZw.setText(bean.self_personality);
            setTextColor(tvZw);
        }
        llZw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.self_personality)) {
                    intent.putExtra("data", bean.self_personality);
                }

                intent.putExtra("title", "自我个性");
                intent.putExtra("type", 5);
                startActivityForResult(intent, 666);
                state = 12;
            }
        });
        if (TextUtil.isNotEmpty(bean.current_state)) {
            tvXz.setText(bean.current_state);
            setTextColor(tvXz);
        }
        llXz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.current_state)) {
                    intent.putExtra("data", bean.current_state);
                }

                intent.putExtra("title", "现在状态");
                intent.putExtra("type", 6);
                startActivityForResult(intent, 666);
                state = 13;
            }
        });
        if (TextUtil.isNotEmpty(bean.superpower)) {
            tvCn.setText(bean.superpower);
            setTextColor(tvCn);
        }
        llCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPersonDetailActivity.this, AddLikeFlagActivity.class);
                if (TextUtil.isNotEmpty(bean.superpower)) {
                    intent.putExtra("data", bean.superpower);
                }

                intent.putExtra("title", "超能力");
                intent.putExtra("type", 7);
                startActivityForResult(intent, 666);
                state = 14;
            }
        });
        setPersent();
    }

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

    public PhotoPopupWindow mWindowAddPhotos;


    public void setTextColor(TextView view) {
        view.setTextColor(getResources().getColor(R.color.color5));
    }

    private int cout = 0;

    public void setPersent() {

        if (TextUtil.isNotEmpty(bean.image_url)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.nickname)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.birthday_year) && !bean.birthday_year.equals("null")) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.personality_sign)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.home_town)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.job)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.graduate_school)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.label)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.personal_statement)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.movie)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.music)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.book)) {
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.superpower)) {
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.current_state)) {
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.self_personality)) {
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.cocial_card)) {
            cout++;
        }

        tvEditor.setText("完成度" + cout * 100 / 16 + "%");
    }

    private int state;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void bindEvents() {
        piuvRemarkImage.setOnActionListener(new ImageUploadView.OnActionListener() {
            @Override
            public void onItemDelete(int position) {
                img_path.remove(position);
                upload_position = 0;
                states = 1;
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.get(EditorPersonDetailActivity.this).clearMemory();

                type = 1;
                closeKeyBoard();
                PermissionGen.with(EditorPersonDetailActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();
            }
        });
        tvAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.get(EditorPersonDetailActivity.this).clearMemory();
                type = 1;
                closeKeyBoard();
                PermissionGen.with(EditorPersonDetailActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();
            }
        });
        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                closeKeyBoard();
                PermissionGen.with(EditorPersonDetailActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA)
                        .request();

            }
        });


    }

    private TimePickerView timePickerView;
    private int yearStr;
    private int monthStr;
    private int dayStr;

    private void setBridthDayTv(Date date) {
        if (date != null) {
            String str = DateUtils.formatDate(date, "yyyy-MM-dd");
            String strs = DateUtils.formatDate(date, "yyyy-MM-dd");
            bean.birthday = strs;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            LogUtil.d(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + getString(R.string.nxzdrq) + str);
            tvDate.setText(str);
            setTextColor(tvDate);
            yearStr = calendar.get(Calendar.YEAR);
            monthStr = calendar.get(Calendar.MONTH) + 1;
            dayStr = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            tvDate.setText("");
            yearStr = 0;
            monthStr = 0;
            dayStr = 0;
        }
    }

    private void showTimeSelector() {

        if (timePickerView == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1990, 9, 18);
            timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    setBridthDayTv(date);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText(getString(R.string.cancel))//取消按钮文字
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
                    .setLabel(getString(R.string.yea), getString(R.string.yeu), getString(R.string.riis), "", "", "")
                    .setLineSpacingMultiplier(2f)
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)
                    .build();
        }
        timePickerView.show();
    }

    public void haveFous(boolean isFous) {

    }

    @Override
    public void initData() {

    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        if (type == 1) {
            View view = LayoutInflater.from(EditorPersonDetailActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
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
                            imageUri = FileProvider.getUriForFile(EditorPersonDetailActivity.this, "com.zhongchuang.canting.provider", fileUri);
                        PhotoUtils.takePicture(EditorPersonDetailActivity.this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(EditorPersonDetailActivity.this, getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
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
            mWindowAddPhotos = new PhotoPopupWindow(EditorPersonDetailActivity.this).bindView(view);
            mWindowAddPhotos.showAtLocation(tvAva, Gravity.BOTTOM, 0, 0);
        } else {
            showAddPhotoWindow(count - img_path.size());
        }


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
    private String urls;
    private static final int CLIP_PHOTO = 0x04;
    private ArrayList<String> urs = new ArrayList<>();

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
                    Intent intent = new Intent(EditorPersonDetailActivity.this, ClipActivity.class);
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
                case 666:
                    String content = data.getStringExtra("data");
                    switch (state) {
                        //上传照片
                        case 1:
                            tvNc.setText(content);
                            setTextColor(tvNc);
                            bean.nickname = content;
                            break;
                        case 2:
                            tvQm.setText(content);
                            setTextColor(tvQm);
                            bean.personality_sign = content;
                            break;
                        case 3:
                            tvJx.setText(content);
                            setTextColor(tvJx);
                            bean.home_town = content;
                            break;
                        case 4:
                            tvZy.setText(content);
                            bean.job = content;
                            setTextColor(tvZy);
                            break;
                        case 5:
                            tvXx.setText(content);
                            bean.graduate_school = content;
                            setTextColor(tvXx);
                            break;
                        case 6:
                            tvBq.setText(content);
                            bean.label = content;
                            setTextColor(tvBq);
                            break;
                        case 7:
                            tvSm.setText(content);
                            bean.personal_statement = content;
                            setTextColor(tvSm);
                            break;
                        case 8:

                            bean.movie = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvDy.setText(split[0] + "," + split[1]);
                                } else {
                                    tvDy.setText(content);
                                }
                            }
                            setTextColor(tvDy);
                            break;
                        case 9:
                            bean.music = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvSong.setText(split[0] + "," + split[1]);
                                } else {
                                    tvSong.setText(content);
                                }
                            }

                            setTextColor(tvSong);
                            break;
                        case 10:

                            bean.book = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvSj.setText(split[0] + "," + split[1]);
                                } else {
                                    tvSj.setText(content);
                                }
                            }

                            setTextColor(tvSj);
                            break;
                        case 11:

                            bean.cocial_card = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvSh.setText(split[0] + "," + split[1]);
                                } else {
                                    tvSh.setText(content);
                                }
                            }

                            setTextColor(tvSh);
                            break;
                        case 12:

                            bean.self_personality = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvZw.setText(split[0] + "," + split[1]);
                                } else {
                                    tvZw.setText(content);
                                }
                            }

                            setTextColor(tvZw);
                            break;
                        case 13:

                            bean.current_state = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvXz.setText(split[0] + "," + split[1]);
                                } else {
                                    tvXz.setText(content);
                                }
                            }

                            setTextColor(tvXz);
                            break;
                        case 14:

                            bean.superpower = content;
                            if (!TextUtil.isEmpty(content)) {
                                String[] split = content.split("&&");
                                if (split.length == 2) {
                                    tvCn.setText(split[0] + "," + split[1]);
                                } else {
                                    tvCn.setText(content);
                                }
                            }

                            setTextColor(tvCn);
                            break;

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
                        Toast.makeText(EditorPersonDetailActivity.this, getString(R.string.sbmysdk), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    upPhotos(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg" );
                    path = Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg";
                    upPhotos(path);
                    Glide.with(this).load(path).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(img);
                default:
            }
        }
    }

    private int states = 0;

    private void changeSunbmit() {
        if(userInfo!=null&&TextUtil.isNotEmpty(userInfo.birthday)){
            userInfo.birthday = userInfo.birthdayYear + "-" + userInfo.birthdayMonth + "-" + userInfo.birthdayDay;
            userInfo.personalitySign = "";
        }



    }

    private void upPhotos(final String path) {

        final Map<String, String> map = new HashMap<>();
        final Map<String, File> maps = new HashMap<>();
        maps.put("file", new File(path));
        String userInfoId = SpUtil.getUserInfoId(this);
        map.put("userInfoId", userInfoId);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = "";
                try {
                    url = UploadUtil.UpLoadImg(EditorPersonDetailActivity.this, "common/upload/images", map, maps);


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

                        if(info.data.get(0).path.contains("http://119.23.212.8:8080")){
                            String imgs = info.data.get(0).path.replace("http://119.23.212.8:8080", "http://120.78.148.31:8080");
                            info.data.get(0).path=imgs;
                        }
                        SpUtil.putString(EditorPersonDetailActivity.this, "ava", info.data.get(0).path);
                        userInfo.headImage = info.data.get(0).path;
                        Glide.with(EditorPersonDetailActivity.this).load(userInfo.headImage).asBitmap().transform(new CircleTransform(EditorPersonDetailActivity.this)).placeholder(R.drawable.editor_ava).into(img);
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

    private UserInfo userInfo;
    public String path;
    private ArrayList<UploadFileBean> mImageAttachmentList;//图片附件

    private Gson mGson;

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
            bean.image_url = url;
            states = 0;
//            presenter.addInfo(etContent.getText().toString(), url);
        }
    }

    private String token;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(img_path.size()!=0){
                upFlile(img_path.get(0).getForderPath());
            }

            return false;

        }
    });

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

    public void upFlile(String path) {

        if (path.contains("http") || path.contains("\"\"")) {

            getUpImgSuccdess(path);
        } else {
            QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
                @Override
                public void completeListener(String urls) {

                    getUpImgSuccdess(urls);


                }
            });
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


        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.MINE_FRESH, userInfo==null?new UserInfo():userInfo));
    }


    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 123) {
            SpUtil.putString(this,"name",bean.nickname);
            sendSuccess();
        }

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
            if ((img_path.size() > 0)) {
                showPopwindows();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }




}
