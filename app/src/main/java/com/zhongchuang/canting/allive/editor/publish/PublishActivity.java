package com.zhongchuang.canting.allive.editor.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.common.UploadStateType;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.aliyun.common.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunICompose;
import com.aliyun.qupai.editor.AliyunIComposeCallBack;
import com.zhongchuang.canting.allive.vodupload_demo.data.ItemInfo;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.LiveItemBean;
import com.zhongchuang.canting.been.LiveTypeBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.fragment.mall.LiveMineFragments;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.ListVideoWindow;
import com.zhongchuang.canting.widget.LiveItemSelectBindDialog;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.waitLoading.ShapeLoadingDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.valuesfeng.picker.ImageSelectActivity;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.widget.ImageLoaderEngine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macpro on 2017/11/6.
 */

public class PublishActivity extends Activity implements View.OnClickListener ,   OtherContract.View {
    private static final String TAG = PublishActivity.class.getName();
    public long startTime ;
    public long endTime ;

    public static final String KEY_PARAM_CONFIG = "project_json_path";
    public static final String KEY_PARAM_THUMBNAIL = "svideo_thumbnail";

    private View mActionBar;
    private LinearLayout ll_choose;
    private ImageView mIvLeft;
    private ProgressBar mProgress;
    private ImageView mCoverImage, mCoverBlur;
    private EditText mVideoDesc;
    private View mCoverSelect;
    private View mComposeProgressView;
    private TextView mComposeProgress;
    private View mComposeIndiate;
    private TextView mComposeStatusText, mComposeStatusTip;
    //    private TextView mDescCount;
    private TextView mPublish;
    private TextView tvType;

    private String mConfig;
    private String mOutputPath = Environment.getExternalStorageDirectory() + File.separator + "output_compose_video.mp4";
    private String mThumbnailPath;
    private AliyunICompose mCompose;
    private boolean mComposeCompleted;
   private ShapeLoadingDialog shapeLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_svideo_activity_publish);
        initView();
        mConfig = getIntent().getStringExtra(KEY_PARAM_CONFIG);
        mThumbnailPath = getIntent().getStringExtra(KEY_PARAM_THUMBNAIL);
        datas= LiveMineFragments.datas;
        mCompose = ComposeFactory.INSTANCE.getInstance();
        mCompose.init(this);
        presenter=new OtherPresenter(this);
        presenter.getLiveToken();
        presenter.getFirstCategoryList();
        type=getIntent().getIntExtra("type",1);
        uploader = new VODUploadClientImpl(getApplicationContext());
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("上传中...")
                .build();


        //这里合成开始
        startTime = System.currentTimeMillis();


        int ret = mCompose.compose(mConfig, mOutputPath, mCallback);
        if(ret != AliyunErrorCode.OK)
        {
            initUpload();
            mComposeIndiate.setActivated(true);
            mCoverSelect.setEnabled(mComposeCompleted);
        }
        View root = (View) mActionBar.getParent();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getApplication()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager.isActive()) {
                    inputManager
                            .hideSoftInputFromWindow(mVideoDesc.getWindowToken(), 0);
                }
            }
        });
        new MyAsyncTask(this).execute(mThumbnailPath);
        ll_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datas!=null){
                    if(datas==null){
                        presenter.getLiveCategory();

                        return;
                    }
                    showLiveItemSelector();

                }

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
            mSelectBindDialog = new LiveItemSelectBindDialog(this, mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
            mSelectBindDialog.setBindClickListener(new LiveItemSelectBindDialog.BindClickListener() {
                @Override
                public void site(int provinces, int citys, int districts) {

                    String area = "";


                    liveThirdId=datas.get(provinces).secondList.get(citys).thirdList.get(districts).id;
                    tvType.setText(datas.get(provinces).secondList.get(citys).thirdList.get(districts).sec_category_name);
                }
            });
        }
        mSelectBindDialog.show();
    }

    private String liveThirdId;

    private Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            showPopwindows();
            return false;
        }
    });
    private MarkaBaseDialog dialogs;

    public void showPopwindows() {
        sta=1;
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        View view = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);
        view = views.findViewById(R.id.line_center);

        title.setText("您上传的视频如果出现红边可现在点击处理，也可以看具体效果后在“上传视频记录”进行修复");
        sure.setText("视频正常");
        dialogs = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialogs.show();

        cancel.setText("去红边");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.uploadVideo(imgUrl,mVideoDesc.getText().toString(),url,3,liveThirdId);
                dialogs.dismiss();

            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.uploadVideo(imgUrl,mVideoDesc.getText().toString(),url,type,liveThirdId);
                dialogs.dismiss();

            }
        });
    }

    private int sta=0;
    public void initUpload(){
        String fineName = mOutputPath;
        String ossName  = "video/" + SpUtil.getUserInfoId(this)+"/"+TimeUtil.formatTtimeNames(System.currentTimeMillis())+"."+TimeUtil.formatRedTime(System.currentTimeMillis()); //vodPath + index + ".mp4";

        uploader.addFile(fineName, "oss-cn-shenzhen.aliyuncs.com", Constant.FILE_NAME, ossName, getVodInfo());
        url= ossName;
    }
    private String url;
    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle("标题" );
        vodInfo.setDesc("描述." );
        vodInfo.setCateId(1);
        vodInfo.setIsProcess(true);
        vodInfo.setCoverUrl(mThumbnailPath);
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        return vodInfo;
    }
    private VODUploadCallback callback = new VODUploadCallback() {
        @Override
        public void onUploadSucceed(UploadFileInfo info) {
            OSSLog.logDebug("onsucceed ------------------" + info.getFilePath());
            shapeLoadingDialog.dismiss();
            handler.sendEmptyMessage(1);


        }

        @Override
        public void onUploadFailed(UploadFileInfo info, String code, String message) {
            OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);


        }

        @Override
        public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
            OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);


        }

        @Override
        public void onUploadTokenExpired() {
            OSSLog.logError("onExpired ------------- ");
            // 实现时，重新获取STS临时账号用于恢复上传
//            uploader.resumeWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
        }

        @Override
        public void onUploadRetry(String code, String message) {
            OSSLog.logError("onUploadRetry ------------- ");
        }

        @Override
        public void onUploadRetryResume() {
            OSSLog.logError("onUploadRetryResume ------------- ");
        }

        @Override
        public void onUploadStarted(UploadFileInfo uploadFileInfo) {
            OSSLog.logError("onUploadStarted ------------- ");

        }
    };
    private VODUploadClient uploader;
    private void initThumbnail(Bitmap thumbnail) {

        mCoverBlur.setImageBitmap(thumbnail);
        mCoverImage.setImageBitmap(thumbnail);

    }
    private OtherPresenter presenter;
    private List<LiveTypeBean> datas;

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==12){
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.LIVE_FINISH,""));
            finish();
        }else if(type==19) {
            aliLive aliLive= (aliLive) entity;
            if(aliLive!=null&& TextUtil.isNotEmpty(aliLive.token)){
                type=1;
                uploader.init(aliLive.accessKeyId, aliLive.accesskeysecret, aliLive.token, aliLive.expiration, callback);
            }
        }else if(type==998) {
             datas= (List<LiveTypeBean>) entity;
            showLiveItemSelector();
        }

    }
    private int type=1;
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        if(shapeLoadingDialog!=null){
            shapeLoadingDialog.dismiss();
           ToastUtil.showToast(this,"上传失败");
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
                imgUrl=QiniuUtils.baseurl+urls;

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                 if(sta==1){
                     if(TextUtil.isNotEmpty(liveThirdId)){
                         presenter.uploadVideo(imgUrl,mVideoDesc.getText().toString(),url,type,liveThirdId);
                     }

                 }else {
                     finish();

                 }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    static class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<PublishActivity> ref;
        private float maxWidth;

        MyAsyncTask(PublishActivity activity) {
            ref = new WeakReference<>(activity);
//            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
//            maxWidth = screenWidth * 0.75f;
            maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    240, activity.getResources().getDisplayMetrics());
        }

        @Override
        protected Bitmap doInBackground(String... thumbnailPaths) {
            String path = thumbnailPaths[0];
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            File thumbnail = new File(path);
            if (!thumbnail.exists()) {
                return null;
            }
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);
            float bw = opt.outWidth;
            float bh = opt.outHeight;
            float scale;
            if (bw > bh) {
                scale = bw / maxWidth;
            } else {
                scale = bh / maxWidth;
            }
            boolean needScaleAfterDecode = scale != 1;
            opt.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(path, opt);
            if (needScaleAfterDecode) {
                bmp = scaleBitmap(bmp, scale);
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null && ref != null && ref.get() != null) {
                ref.get().initThumbnail(bitmap);
            }
        }
    }

    private static Bitmap scaleBitmap(Bitmap bmp, float scale) {
        Matrix mi = new Matrix();
        mi.setScale(1 / scale, 1 / scale);
        Bitmap temp = bmp;
        bmp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), mi, false);
        temp.recycle();
        return bmp;
    }

    private void initView() {
        mActionBar = findViewById(R.id.action_bar);
        ll_choose = findViewById(R.id.ll_choose);
        mActionBar = findViewById(R.id.action_bar);
        mActionBar.setBackgroundColor(
                getResources().getColor(R.color.white));
//        mPublish = (TextView) findViewById(R.id.tv_right);
        mPublish = findViewById(R.id.tv_rights);
        tvType = findViewById(R.id.tv_type);
        mIvLeft = findViewById(R.id.iv_left);
        mIvLeft.setOnClickListener(this);
//        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_icon_back);
        mPublish.setText(R.string.publish);
        mIvLeft.setVisibility(View.VISIBLE);
        mPublish.setVisibility(View.VISIBLE);
        mProgress = findViewById(R.id.publish_progress);
        mComposeProgressView = findViewById(R.id.compose_progress_view);
        mCoverBlur = findViewById(R.id.publish_cover_blur);
        mCoverImage = findViewById(R.id.publish_cover_image);
        mVideoDesc = findViewById(R.id.publish_desc);
        mComposeIndiate = findViewById(R.id.image_compose_indicator);
//        mDescCount = (TextView) findViewById(R.id.publish_desc_count);
        mPublish.setEnabled(mComposeCompleted);
        mPublish.setOnClickListener(this);
        mCoverSelect = findViewById(R.id.publish_cover_select);
        mCoverSelect.setEnabled(mComposeCompleted);
        mCoverSelect.setOnClickListener(this);
        mComposeProgress = findViewById(R.id.compose_progress_text);
        mComposeStatusText = findViewById(R.id.compose_status_text);
        mComposeStatusTip = findViewById(R.id.compose_status_tip);
        mVideoDesc.addTextChangedListener(new TextWatcher() {

            private int start;
            private int end;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                start = mVideoDesc.getSelectionStart();
                end = mVideoDesc.getSelectionEnd();

                int count = count(s.toString());
                // 限定EditText只能输入10个数字
                if (count > 20 && start > 0) {
                    Log.d(AliyunTag.TAG, "超过10个以后的数字");

                    s.delete(start - 1, end);
                    mVideoDesc.setText(s);
                    mVideoDesc.setSelection(s.length());
                }
            }
        });
    }

    private int count(String text) {
        int len = text.length();
        int skip;
        int letter = 0;
        int chinese = 0;
//		int count = 0;
//		int sub = 0;
        for (int i = 0; i < len; i += skip) {
            int code = text.codePointAt(i);
            skip = Character.charCount(code);
            if (code == 10) {
                continue;
            }
            String s = text.substring(i, i + skip);
            if (isChinese(s)) {
                chinese++;
            } else {
                letter++;
            }

        }
        letter = letter % 2 == 0 ? letter / 2 : (letter / 2 + 1);
        int result = chinese + letter;
        return result;
    }

    // 完整的判断中文汉字和符号
    private boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
    private PhotoPopupWindow mWindowAddPhotos;
    public void getPhotos() {
        Picker.from(this)
                .count(1)
                .enableCamera(true)
                .setEngine(new ImageLoaderEngine())
                .setAdd_watermark(false)
                .forResult(66);

    }
    private String img_path;
    private String path;
    private File fileCropUri;
    private Uri cropImageUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
                        PhotoUtils.cropImageUris(PublishActivity.this, newUri, cropImageUri, 16, 9, output_X, output_Y, 99);
                        img_path="";
                    }


                    break;
                case 99:
                    new MyAsyncTask(this).execute(path);
                    break;
                case 0:
                    path = data.getStringExtra(CoverEditActivity.KEY_PARAM_RESULT);
                    new MyAsyncTask(this).execute(path);
                    break;
            }
        }
    }
    public void showAddPhotoWindow() {
        View view = LayoutInflater.from(PublishActivity.this).inflate(R.layout.view_add_photo_window, null);
      TextView text=  view.findViewById(R.id.tv_cancel);
      text.setText("从视频里选择封面");
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
                Intent intent = new Intent(PublishActivity.this, CoverEditActivity.class);
                intent.putExtra(CoverEditActivity.KEY_PARAM_VIDEO, mOutputPath);
                startActivityForResult(intent, 0);
                mWindowAddPhotos.dismiss();


            }
        });
        mWindowAddPhotos = new PhotoPopupWindow(PublishActivity.this).bindView(view);

        mWindowAddPhotos.showAtLocation(mPublish, Gravity.BOTTOM, 0, 0);


    }
    @Override
    public void onClick(View v) {
        if (v == mPublish) {

            if(TextUtils.isEmpty(path)){
                ToastUtil.showToast(PublishActivity.this,"请上传封面");
                return;

            }
            if(TextUtils.isEmpty(mVideoDesc.getText().toString())){
                ToastUtil.showToast(PublishActivity.this,"请输入视频标题");
                return;

            }
            if(TextUtils.isEmpty(liveThirdId)){
                ToastUtil.showToast(PublishActivity.this,"请选择视频分类");
                return;

            }
            shapeLoadingDialog.show();
            getUpToken(path);
            uploader.start();
//            mPublish.setEnabled(false);
//            Intent intent = new Intent(this, UploadActivity.class);
//            intent.putExtra(UploadActivity.KEY_UPLOAD_VIDEO, mOutputPath);
//            intent.putExtra(UploadActivity.KEY_UPLOAD_THUMBNAIL, mThumbnailPath);
//            if (!TextUtils.isEmpty(mVideoDesc.getText())) {
//                intent.putExtra(UploadActivity.KEY_UPLOAD_DESC, mVideoDesc.getText().toString());
//            }
//            startActivity(intent);
        } else if (v == mCoverSelect) {

            showAddPhotoWindow();

        } else if (v == mIvLeft) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (mComposeCompleted) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setTitle(R.string.video_composeing_cancel_or_go)
                    .setNegativeButton(R.string.goback_to_editor, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mComposeCompleted) {
                                finish();
                            } else {
                                mCompose.cancelCompose();
                                finish();
                            }
                        }
                    })
                    .setPositiveButton(R.string.go_ahead_compose, null).create();
            dialog.show();
        }
    }



    private final AliyunIComposeCallBack mCallback = new AliyunIComposeCallBack() {
        @Override

        public void onComposeError(int errorCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mComposeProgress.setVisibility(View.GONE);

                    mComposeIndiate.setVisibility(View.VISIBLE);
                    mComposeIndiate.setActivated(false);
                    mComposeStatusTip.setText(R.string.backtoeditorandtryagain);
                    mComposeStatusText.setText(R.string.compose_failed);
                }
            });
        }

        @Override
        public void onComposeProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mComposeProgress.setText(progress + "%");
                    mProgress.setProgress(progress);
                }
            });
        }

        @Override
        public void onComposeCompleted() {

            //这里合成结束
            endTime = System.currentTimeMillis();


            mComposeCompleted = true;
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mOutputPath);
            initUpload();
            Bitmap bmp = mmr.getFrameAtTime(0);
            if(bmp == null) {
                Log.e(TAG, "Compose error");
                return ;
            }
            float w = bmp.getWidth();
            float h = bmp.getHeight();
            int maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    240, getResources().getDisplayMetrics());
            float scale;
            if (w > h) {
                scale = w / maxWidth;
            } else {
                scale = h / maxWidth;
            }
            final Bitmap thumbnail = scaleBitmap(bmp, scale);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCoverImage.setVisibility(View.VISIBLE);
                    initThumbnail(thumbnail);
                    mPublish.setEnabled(mComposeCompleted);
                    mProgress.setVisibility(View.GONE);
                    mComposeProgress.setVisibility(View.GONE);

                    mComposeIndiate.setVisibility(View.VISIBLE);
                    mComposeIndiate.setActivated(true);
                    mComposeStatusTip.setVisibility(View.GONE);
                    mComposeStatusText.setText(R.string.compose_success);
                    mComposeProgressView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mComposeProgressView.setVisibility(View.GONE);
                            mCoverSelect.setVisibility(View.VISIBLE);
                            mCoverSelect.setEnabled(mComposeCompleted);
                        }
                    }, 2000);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompose.release();
    }
}
