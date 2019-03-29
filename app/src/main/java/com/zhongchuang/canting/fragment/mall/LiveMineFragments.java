package com.zhongchuang.canting.fragment.mall;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.encoder.VideoCodecs;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.RecordDetailActivity;
import com.zhongchuang.canting.activity.chat.SendDynamicActivity;
import com.zhongchuang.canting.activity.chat.SumbitJfActivity;
import com.zhongchuang.canting.activity.live.GiftRecordActivity;
import com.zhongchuang.canting.activity.live.LeaveContentActivity;
import com.zhongchuang.canting.activity.live.LiveActivity;
import com.zhongchuang.canting.activity.live.LiveHandActivity;
import com.zhongchuang.canting.activity.live.MineVideoActivity;
import com.zhongchuang.canting.activity.live.MinetCareLiveActivity;
import com.zhongchuang.canting.activity.live.MinetCareLiveActivitys;
import com.zhongchuang.canting.activity.live.RoomInfoActivity;
import com.zhongchuang.canting.allive.importer.MediaActivity;
import com.zhongchuang.canting.allive.pusher.ui.activity.PushConfigActivity;
import com.zhongchuang.canting.allive.pusher.ui.activity.VideoRecordConfigActivity;
import com.zhongchuang.canting.allive.recorder.AliyunVideoRecorder;
import com.zhongchuang.canting.allive.recorder.util.Common;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.been.LIVEBEAN;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleImageView;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.Custom_ProfileOrderClickBtn;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.waitLoading.ShapeLoadingDialog;

import java.io.File;
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
import rx.Subscription;
import rx.functions.Action1;

;

/**
 * Created by Administrator on 2017/12/4.
 */

public class LiveMineFragments extends LazyFragment implements BaseContract.View {


    @BindView(R.id.person_pic)
    CircleImageView personPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.btn_care)
    Custom_ProfileOrderClickBtn btnCare;
    @BindView(R.id.btn_cares)
    Custom_ProfileOrderClickBtn btnCares;
    @BindView(R.id.tv_all)
    Custom_ProfileOrderClickBtn tvAll;
    @BindView(R.id.tv_gift)
    Custom_ProfileOrderClickBtn tvGift;
    @BindView(R.id.tv_jfs)
    TextView tvJfs;
    @BindView(R.id.ll_total)
    RelativeLayout llTotal;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rl_hand)
    RelativeLayout rlHand;
    @BindView(R.id.tv_application)
    TextView tvApplication;
    @BindView(R.id.tv_applications)
    TextView tvApplications;
    @BindView(R.id.rl_live)
    RelativeLayout rlLive;
    @BindView(R.id.rl_lives)
    RelativeLayout rlLives;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_videos)
    TextView tvVideos;
    private BasesPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Uri imageUri;
    private Uri cropImageUri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.live_mines, container, false);
        ButterKnife.bind(this, viewRoot);
        presenter = new BasesPresenter(this);
        presenter.getUserIntegral();
        initView();
        setListener();
        initAssetPath();

//        copyAssets();
        return viewRoot;
    }

    private String[] mEffDirs;

    private void initAssetPath() {
        String path = StorageUtils.getCacheDirectory(CanTingAppLication.getInstance()).getAbsolutePath() + File.separator + Common.QU_NAME + File.separator;
        File filter = new File(new File(path), "filter");

        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        mEffDirs = new String[list.length + 1];
        mEffDirs[0] = null;
        for (int i = 0; i < list.length; i++) {
            mEffDirs[i + 1] = filter.getPath() + "/" + list[i];
        }
    }
    private int playTypes=1;

    private void setListener() {
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SumbitJfActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("cout", tvJf.getText().toString());
                startActivity(intent);
            }
        });
        rlHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LeaveContentActivity.class);
//                if (TextUtil.isNotEmpty(data.leave_massege)) {
//                    intent.putExtra("data", data.leave_massege);
//                } else {
//                    intent.putExtra("data", "");
//                }
//
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), MineVideoActivity.class);
                startActivity(intent);
            }
        });
        btnCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinetCareLiveActivity.class);
                intent.putExtra("data", data.room_info_id);
                startActivity(intent);
            }
        });
        rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinetCareLiveActivity.class);
                intent.putExtra("data", data.room_info_id);
                startActivity(intent);
            }
        });
        btnCares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MinetCareLiveActivitys.class);
                startActivity(intent);
            }
        });
        llTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecordDetailActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomInfoActivity.class);
                startActivity(intent);
            }
        });
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LiveHandActivity.class);
                startActivity(intent);
            }
        });
        tvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GiftRecordActivity.class);
                startActivity(intent);
            }
        });
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!canPlay()){
                    return;
                }
                int min = 2000;
                int max = 3600000;
                int gop = 5;
                int bitrate = 0;
                AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                        .setResolutionMode(mResolutionMode)
                        .setRatioMode(mRatioMode)
                        .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                        .setFilterList(mEffDirs)
                        .setBeautyLevel(80)
                        .setBeautyStatus(true)
                        .setCameraType(CameraType.FRONT)
                        .setFlashType(FlashType.ON)
                        .setNeedClip(true)
                        .setMaxDuration(max)
                        .setMinDuration(min)
                        .setVideoQuality(mVideoQuality)
                        .setGop(gop)
                        .setVideoBitrate(bitrate)
                        .setVideoCodec(mVideoCodec)
                        /**
                         * 裁剪参数
                         */
                        .setMinVideoDuration(4000)
                        .setMaxVideoDuration(3599 * 1000)
                        .setMinCropDuration(3000)
                        .setFrameRate(28)
                        .setCropMode(ScaleMode.PS)
                        .build();
                AliyunVideoRecorder.startRecord(getActivity(), recordParam);
            }
        });

        tvVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canPlay()){
                    return;
                }
                playTypes=2;
                presenter.deleteConfig();


            }
        });

        tvApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canPlay()){
                    return;
                }
                playTypes=1;
                presenter.deleteConfig();
//                isAnchor();
            }
        });
        tvApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canPlay()){
                    return;
                }
                showPopwind();
//                isAnchor();
            }
        });
    }

     private int playType=1;//1 直播被禁用 2，直播ok 3，未开通
    public boolean canPlay(){

        boolean canplay=false;
        if(playType==1){
            canplay=false;
            showPopwindows(getString(R.string.zbzhybjy), getString(R.string.ryywqzdkf), "25825512212");
        }else if(playType==2){
            canplay=true;
        }else if(playType==3){
            canplay=false;
            showDailog();
        }
        return canplay;
    }

    public void showDailog() {
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        TextView sure = (TextView) views.findViewById(R.id.txt_sure);
        TextView cancel = (TextView) views.findViewById(R.id.txt_cancel);
        TextView title = (TextView) views.findViewById(R.id.txt_title);
        title.setText(R.string.nhwtxxzbjxgxx);

        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomInfoActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    protected Bundle fragmentArgs;
    private String shopid;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

//        fragmentArgs = getArguments();
//
//        shopid = fragmentArgs.getString("id");


        super.onActivityCreated(savedInstanceState);
    }

    //    private ProfileInfoHelper mProfileHelper;
    private Subscription mSubscription;

    private int mResolutionMode, mRatioMode;
    private VideoQuality mVideoQuality;
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;

    private void initView() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.DOWN_COMPELTE) {
                    if (shapeLoadingDialog != null && shapeLoadingDialog.isShowing()) {
                        shapeLoadingDialog.dismiss();
                    }

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        mResolutionMode = AliyunSnapVideoParam.RESOLUTION_540P;
        mVideoQuality = VideoQuality.HD;
        mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(getActivity())
                .loadText("直播文件下载中...")
                .build();
        if (!CanTingAppLication.isComplete) {
            shapeLoadingDialog.show();

        }

        shapeLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!CanTingAppLication.isComplete) {
                    shapeLoadingDialog.show();
                }
            }
        });


    }

    private ShapeLoadingDialog shapeLoadingDialog;

    @Override
    public void onResume() {
        super.onResume();
        if (null != presenter) {
            presenter.hostInfo();
            presenter.getUserIntegral();

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("second done destroy");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void lazyView() {

    }

    public void initData() {
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity()).load(StringUtil.changeUrl(data.room_image)).asBitmap().placeholder(R.drawable.editor_ava).into(personPic);
        if (TextUtil.isNotEmpty(data.direct_see_name)) {
            tvName.setText(getString(R.string.fjmc) + data.direct_see_name);
        }
        if (TextUtil.isNotEmpty(data.direct_overview)) {
            tvAbout.setText(getString(R.string.zbbt) + data.direct_overview);
        }

        if (TextUtil.isNotEmpty(data.leave_massege)) {
            tvContent.setText(getString(R.string.lys) + data.leave_massege);
        }

    }

    private TextView sure = null;
    private TextView tv_select = null;
    private ClearEditText content = null;
    private ImageView iv_photo = null;
    private   MarkaBaseDialog dialog;
    public void showPopwind() {

        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.zb_upload_dailog, null);
        sure = (TextView) views.findViewById(R.id.tv_send);
        tv_select = (TextView) views.findViewById(R.id.tv_select);
        content = (ClearEditText) views.findViewById(R.id.et_content);
        iv_photo = (ImageView) views.findViewById(R.id.iv_photo);

        dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtil.isEmpty(img_path)) {
                    showTomast("请上传封面");
                    return;
                }
                if (TextUtil.isEmpty(content.getText().toString())) {
                    showTomast("请输入标题");
                    return;
                }

                getUpToken(img_path);
                dialog.dismiss();
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                showAddPhotoWindow();
                dialog.dismiss();
            }
        });
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                showAddPhotoWindow();
                dialog.dismiss();
            }
        });
    }


    public void showAddPhotoWindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_photo_window, null);

        view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotos();
                mWindowAddPhotos.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowAddPhotos.dismiss();


            }
        });
        mWindowAddPhotos = new PhotoPopupWindow(getActivity()).bindView(view);

        mWindowAddPhotos.showAtLocation(tvName, Gravity.BOTTOM, 0, 0);


    }

    public void getPhotos() {
        Picker.from(this)
                .count(1)
                .enableCamera(true)
                .setEngine(new ImageLoaderEngine())
                .setAdd_watermark(false)
                .forResult(66);

    }

    /**
     * 删除照片和修改相册的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    private String img_path;
    private String path;
    private File fileCropUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            int output_X = 180, output_Y = 180;
            switch (requestCode) {
                //上传照片
                case 66:
                    path=Environment.getExternalStorageDirectory().getPath() + "/"+ System.currentTimeMillis()+".jpg";
                     fileCropUri = new File(path);
                    List<String> imgs = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
                    if (imgs != null && imgs.size() > 0) {
                        img_path = imgs.get(0);
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.fromFile(new File(img_path));
                        PhotoUtils.cropImageUris(this, newUri, cropImageUri, 16, 9, output_X, output_Y, 99);
                        img_path="";
                    }


                    break;
                case 99:
                    Glide.with(getActivity()).load(path).asBitmap().placeholder(R.drawable.editor_ava).into(iv_photo);
                    tv_select.setVisibility(View.GONE);
                    if(dialog!=null){
                        dialog.show();
                    }
                    img_path=path;
                    break;
            }
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

    private String imgUrl;

    public void upFlile(String path, String token) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {
                imgUrl = QiniuUtils.baseurl + urls;
                presenter.addConfig(imgUrl,content.getText().toString());
                presenter.addLiveRecordVod();
            }
        });

    }

    /**
     * 关闭键盘
     */
    public void closeKeyBoard() {
        try {
            if (getActivity().getCurrentFocus() == null) {
                return;
            }
            IBinder iBinder = getActivity().getCurrentFocus().getWindowToken();
            if (iBinder == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(iBinder, inputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopwindows(final String var1, final String var2, final String var3) {
        TextView content = null;
        TextView desc = null;
        ImageView iv_close = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.live_popow_view, null);
        title = (TextView) views.findViewById(R.id.txt_title);
        content = (TextView) views.findViewById(R.id.txt_content);
        desc = (TextView) views.findViewById(R.id.txt_desc);
        iv_close = (ImageView) views.findViewById(R.id.iv_close);

        title.setText(var1);
        content.setText(var2);
        desc.setText(var3);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private Host data;


    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 19) {
            Ingegebean bean = (Ingegebean) entity;
            tvJf.setText(bean.direct_gift_integral);
        } else if (type == 29) {
            Intent intent = new Intent(getActivity(), PushConfigActivity.class);
            startActivity(intent);
        } else if (type == 69) {
            if(playTypes==1){
                Intent intent = new Intent(getActivity(), PushConfigActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), VideoRecordConfigActivity.class);
                startActivity(intent);
            }

        } else if (type == 7) {
        } else {
            data = (Host) entity;
            if (data != null && TextUtil.isNotEmpty(data.play_type)) {
                if (data.play_type.equals("0")) {
                    playType=1;
                } else {
                    if (data != null && TextUtil.isNotEmpty(data.direct_see_name)) {
                        playType=2;
                        String chatRoomId = SpUtil.getChatRoomId(getActivity());
                        if(TextUtil.isEmpty(chatRoomId)){
                            presenter.create();
                        }

                    } else {
                        playType=3;

                    }

                }
            }
            SpUtil.putString(getActivity(), "room_id", data.room_info_id);
            if (this != null) {
                initData();
            }
        }


    }


    private PhotoPopupWindow mWindowAddPhotos;


    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        if(TextUtil.isNotEmpty(msg)){
            ToastUtils.showNormalToast(msg);
        }

    }
}
