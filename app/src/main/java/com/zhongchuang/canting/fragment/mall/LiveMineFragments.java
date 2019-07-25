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
import com.zhongchuang.canting.been.LiveTypeBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.been.aliLive;
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
import com.zhongchuang.canting.widget.LiveItemSelectBindDialog;
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
        presenter.getLiveCategory();
        initView();
        setListener();
        initAssetPath();


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
    private MarkaBaseDialog dialogs;

    public void showPopwindows() {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        View view = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);
        view = views.findViewById(R.id.line_center);

        title.setText("您录播的视频需要云端进行编辑和存储，需要 3 分钟后你方可看到录播视频！");
        sure.setText("知道了");
        dialogs = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialogs.show();
        view.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogs.dismiss();

            }
        });
    }
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
                showHints(1);




            }
        });

        tvApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canPlay()){
                    return;
                }
                playTypes=1;
                showHints(2);

//                isAnchor();
            }
        });
        tvApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canPlay()){
                    return;
                }
                playTypes=1;
                views=null;
                showPopwind();
                state=2;
//                isAnchor();
            }
        });
    }

     private int playType=1;//1 直播被禁用 2，直播ok 3，未开通
     private int chooseType=0;//1 直播被禁用 2，直播ok 3，未开通
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
        TextView sure = views.findViewById(R.id.txt_sure);
        TextView cancel = views.findViewById(R.id.txt_cancel);
        TextView title = views.findViewById(R.id.txt_title);
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

                }else if(bean.type == SubscriptionBean.LIVE_FINISH_FRESH){
                    if(chooseType==1){
                        showPopwindows();
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
     private String live_third_id;
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
    private TextView tv_type = null;
    private View views = null;
    private LinearLayout ll_type = null;
    private TextView tv_select = null;
    private ClearEditText content = null;
    private ImageView iv_photo = null;
    private ImageView iv_close = null;
    private   MarkaBaseDialog dialog;
    public  static List<LiveTypeBean> datas;
    public void showPopwind() {
        if(TextUtil.isNotEmpty(live_third_id)&&live_third_id.equals("0")){
            showHint();
            return;
        }

        EditText reson = null;
        if(views==null){
            views = View.inflate(getActivity(), R.layout.zb_upload_dailog, null);
            sure = views.findViewById(R.id.tv_send);
            tv_type = views.findViewById(R.id.tv_type);
            ll_type = views.findViewById(R.id.ll_type);
            tv_select = views.findViewById(R.id.tv_select);
            content = views.findViewById(R.id.et_content);
            iv_photo = views.findViewById(R.id.iv_photo);
            iv_close = views.findViewById(R.id.iv_close);
            if(TextUtil.isNotEmpty(third_category_name)){
                tv_type.setText(third_category_name);
            }
            dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
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
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeKeyBoard(v);
                    dialog.dismiss();
                }
            });
            tv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeKeyBoard(v);
                    showAddPhotoWindow();
                    dialog.dismiss();
                }
            });
            iv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeKeyBoard(v);
                    showAddPhotoWindow();
                    dialog.dismiss();
                }
            });
            ll_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(datas==null){
                        presenter.getLiveCategory();
                        states=1;
                        return;
                    }
                    showLiveItemSelector();
                    dialog.dismiss();
                }
            });
        }else {
            dialog.show();
        }



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
    public void showChooseAddPhotoWindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_photo_window, null);
        TextView live= view.findViewById(R.id.tv_photo);
        TextView lives= view.findViewById(R.id.tv_cancel);
        live.setText("屏内直播");
        lives.setText("屏内直播并开启录播");
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteConfig();
                mWindowAddPhotos.dismiss();
            }
        });
        lives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views=null;
                showPopwind();
                state=1;
                mWindowAddPhotos.dismiss();


            }
        });
        mWindowAddPhotos = new PhotoPopupWindow(getActivity()).bindView(view);

        mWindowAddPhotos.showAtLocation(tvName, Gravity.BOTTOM, 0, 0);


    }
    private int state;
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
            int output_X = 360, output_Y = 360;
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
                img_path="";
                imgUrl = QiniuUtils.baseurl + urls;
                presenter.addConfig(imgUrl,content.getText().toString(),liveThirdId);

                presenter.addLiveRecordVod();
            }
        });

    }

    /**
     * 关闭键盘
     */
    public void closeKeyBoard(View v) {

            InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
            if ( imm.isActive( ) ) {
                imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

            }
        }
    public void showHint() {
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        TextView sure = views.findViewById(R.id.txt_sure);
        TextView cancel = views.findViewById(R.id.txt_cancel);
        TextView title = views.findViewById(R.id.txt_title);

        title.setText("为了直播模块有更好的体验，现加入直播分类，第一次需选直播分类。后面直播可根据需要是否更该直播分类！");
        sure.setText("选择分类");
        cancel.setVisibility(View.GONE);
        views.findViewById(R.id.line_center).setVisibility(View.GONE);
        cancel.setTextColor(getResources().getColor(R.color.color6));
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveItemSelector();
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
    public void showHints(final int type) {
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        TextView sure = views.findViewById(R.id.txt_sure);
        TextView cancel = views.findViewById(R.id.txt_cancel);
        TextView title = views.findViewById(R.id.txt_title);
        title.setText("为了用户更好的体验，您每次直播可以根据您内容选择合适的分类");
        cancel.setText("选择分类");
        sure.setText("不更换");
        cancel.setTextColor(getResources().getColor(R.color.color6));
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){
                    showChooseAddPhotoWindow();
                }else {
                    presenter.deleteConfig();
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==1){
                    sta=3;
                }else {
                    sta=4;
                }

                showLiveItemSelector();
                dialog.dismiss();
            }
        });
    }
    private LiveItemSelectBindDialog mSelectBindDialog;
    private String mCurrentProviceName = "广东";
    private String third_category_name = "广东";
    private String mCurrentCityName = "中山";
    private String mCurrentDistrictName = "古镇镇";
    public void showLiveItemSelector() {
        if (CanTingAppLication.province == null) {
            return;
        }
        if (mSelectBindDialog == null) {
            mSelectBindDialog = new LiveItemSelectBindDialog(getActivity(), mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
            mSelectBindDialog.setBindClickListener(new LiveItemSelectBindDialog.BindClickListener() {
                @Override
                public void site(int provinces, int citys, int districts) {

                    String area = "";
                    if(provinces!=-1){
                        if(tv_type!=null){
                            tv_type.setText(datas.get(provinces).secondList.get(citys).thirdList.get(districts).sec_category_name);
                        }

                        liveFirstId=datas.get(provinces).firstid;
                        livesecondId=datas.get(provinces).secondList.get(citys).secondid;
                        liveThirdId=datas.get(provinces).secondList.get(citys).thirdList.get(districts).id;
                        live_third_id=datas.get(provinces).secondList.get(citys).thirdList.get(districts).id;
                        third_category_name=datas.get(provinces).secondList.get(citys).thirdList.get(districts).sec_category_name;
                        presenter.updateCategory(liveFirstId,livesecondId,liveThirdId);
                    }
                    if(sta==0){
//                        showPopwind();
                    }else if(sta==3||sta==4) {
                        if(sta==3){
                            showChooseAddPhotoWindow();
                        }else {
                            presenter.deleteConfig();
                        }
                        sta=0;
                    }else {
                        sta=0;
                    }

                }
            });
            mSelectBindDialog.setDisMissListener(new LiveItemSelectBindDialog.DisMissListener() {
                @Override
                public void dismissListener() {
                    if(sta==0){
                        showPopwind();
                    }
                }
            });
        }
        mSelectBindDialog.show();
    }

   private String liveFirstId;
   private String livesecondId;
   private String liveThirdId;
    public void showPopwindows(final String var1, final String var2, final String var3) {
        TextView content = null;
        TextView desc = null;
        ImageView iv_close = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.live_popow_view, null);
        title = views.findViewById(R.id.txt_title);
        content = views.findViewById(R.id.txt_content);
        desc = views.findViewById(R.id.txt_desc);
        iv_close = views.findViewById(R.id.iv_close);

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
    private int states;
    private int sta;

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 19) {
            Ingegebean bean = (Ingegebean) entity;
            tvJf.setText(bean.direct_gift_integral);
        } else if (type == 29) {
            if(state==1){
                aliLive aliLive = (aliLive) entity;
                presenter.updateType(aliLive.id,2+"");
                chooseType=1;
                Intent intent = new Intent(getActivity(), VideoRecordConfigActivity.class);
                startActivity(intent);
            }else {
                aliLive aliLive = (aliLive) entity;
                chooseType=1;
                Intent intent = new Intent(getActivity(), PushConfigActivity.class);
                intent.putExtra("id",aliLive.id);
                startActivity(intent);
            }

        } else if (type == 69) {
            if(playTypes==1){
                chooseType=0;
//                aliLive aliLive = (aliLive) entity;
                Intent intent = new Intent(getActivity(), PushConfigActivity.class);
//                intent.putExtra("id",aliLive.id);
                startActivity(intent);
            }else {

                chooseType=1;

                Intent intent = new Intent(getActivity(), VideoRecordConfigActivity.class);
                startActivity(intent);

            }

        }else if (type == 999) {
            aliLive aliLive = (aliLive) entity;
            if (aliLive != null && aliLive.liveUrl != null) {

                SpUtil.putString(getActivity(), "chatroomsId", aliLive.liveUrl.chatrooms_id);

                }

        }else if (type == 998) {
        datas= (List<LiveTypeBean>) entity;
        if(states!=0){
            states=0;
            showLiveItemSelector();
        }
        } else if (type == 5||type == 7||type == 299) {
        } else {
            data = (Host) entity;

            live_third_id=data.live_third_id;
            liveThirdId=data.live_third_id;
            if(TextUtil.isNotEmpty(live_third_id)&&live_third_id.equals("0")){
                sta=1;
                showHint();
            }
            third_category_name=data.third_category_name;
            if (data != null && TextUtil.isNotEmpty(data.play_type)) {
                if (data.play_type.equals("0")) {
                    playType=1;
                } else {
                    if (data != null && TextUtil.isNotEmpty(data.direct_see_name)) {
                        playType=2;
                        String chatRoomId = SpUtil.getChatRoomId(getActivity());
                        if(TextUtil.isEmpty(chatRoomId)){
                            presenter.create("");
                        }else {
                            presenter.getLiveUrl(SpUtil.getUserInfoId(getActivity()));
                        }

                    } else {
                        playType=3;

                    }

                }
                SpUtil.putString(getActivity(), "room_id", data.room_info_id);
            }

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
