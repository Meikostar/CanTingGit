package com.zhongchuang.canting.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.zhongchuang.canting.R;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.TextFormater;
import com.zhongchuang.canting.activity.chat.PlayVideoActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.model.EaseImageCache;
import com.zhongchuang.canting.easeui.utils.EaseCommonUtils;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;

import java.io.File;
import java.util.HashMap;

public class EaseChatRowVideo extends EaseChatRowFile {
    private static final String TAG = "EaseChatRowVideo";

    private ImageView imageView;
    private TextView sizeView;
    private TextView timeLengthView;

    public EaseChatRowVideo(Context context, int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context, chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_video : R.layout.ease_row_sent_video, this);
    }

    protected RelativeLayout rl_bbg;
    protected TextView tv_reback;
    protected TextView tv_userid;
    public static final String EXETEND = "rb_extend";
    @Override
    protected void onFindViewById() {
        imageView = findViewById(R.id.chatting_content_iv);
        sizeView = findViewById(R.id.chatting_size_iv);
        timeLengthView = findViewById(R.id.chatting_length_iv);
        ImageView playView = findViewById(R.id.chatting_status_btn);
        percentageView = findViewById(R.id.percentage);
        tv_reback = findViewById(R.id.tv_reback);
        tv_userid = findViewById(R.id.tv_userid);
        rl_bbg = findViewById(R.id.rl_bbg);
    }
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
    @Override
    protected void onSetUpView() {
        String currentUser = EMClient.getInstance().getCurrentUser();
        String video_url = message.getStringAttribute(EaseConstant.EXTRA_SEND, "");
        String image_url = message.getStringAttribute(EaseConstant.EXTRA_NAME, "");
        String video_size = message.getStringAttribute(EaseConstant.EXTRA_GRAPID, "");
        String content = message.getStringAttribute(EXETEND, "");

        if(TextUtil.isEmpty(content)){
            long timelength=message.getIntAttribute(EaseConstant.EXTRA_RED_IS_ALL,0);
            Glide.with(context).load(StringUtil.changeUrl(image_url)).asBitmap().placeholder(R.drawable.ease_default_image).into(imageView);
            sizeView.setText(video_size+" M");
            timeLengthView.setText(formatTimeS(timelength));
            imageView.setImageResource(R.drawable.ease_default_image);
            if(progressBar!=null){
                progressBar.setVisibility(View.GONE);
            }
            if(percentageView!=null){
                percentageView.setVisibility(View.GONE);
            }
            if(message.getChatType()== EMMessage.ChatType.GroupChat){
                tv_userid.setVisibility(VISIBLE);
            }else {
                tv_userid.setVisibility(GONE);
            }
            tv_reback.setVisibility(GONE);
            rl_bbg.setVisibility(VISIBLE);
        }else {
            tv_reback.setVisibility(VISIBLE);
            tv_userid.setVisibility(GONE);
            rl_bbg.setVisibility(GONE);

            tv_reback.setText(content);

        }


    }
    public static String formatTimeS(long duration) {
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;
        if( second < 10 ){
            time += "0" ;
        }
        time += second ;
        return time ;

    }


    @Override
    protected void onBubbleClick() {
        if (message != null) {
            String video_url = message.getStringAttribute(EaseConstant.EXTRA_SEND, "");
            long timelength = message.getIntAttribute(EaseConstant.EXTRA_RED_IS_ALL, 0);
            CanTingAppLication.landType = 2;

            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.putExtra("video_length", timelength);
            intent.putExtra("path", video_url);
            context.startActivity(intent);


        }
    }
    /**
     * show video thumbnails
     *
     * @param localThumb   local path for thumbnail
     * @param iv
     * @param thumbnailUrl Url on server for thumbnails
     * @param message
     */
    private void showVideoThumbView(final String localThumb, final ImageView iv, String thumbnailUrl, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(localThumb);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ease_default_image);
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    if (new File(localThumb).exists()) {
                        return ImageUtils.decodeScaleImage(localThumb, 160, 160);
                    } else {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        EaseImageCache.getInstance().put(localThumb, result);
                        iv.setImageBitmap(result);

                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                EMClient.getInstance().chatManager().downloadThumbnail(message);
                            }
                        }

                    }
                }
            }.execute();
        }

    }

}
