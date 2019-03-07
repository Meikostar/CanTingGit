package com.zhongchuang.canting.widget.share;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;


import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.utils.ThirdShareManager;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by honghouyang on 16/8/30.
 */
public class SharePopupWindow extends PopupWindow {

    private Context context;
    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;

    public SharePopupWindow(Context cx) {
        this.context = cx;
        setPlatformActionListener(platformActionListener);
    }

    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }

    public void showShareWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.share_layout, null);

        GridView gridView = (GridView) view.findViewById(R.id.share_gridview);
        ShareAdapter adapter = new ShareAdapter(context);
        gridView.setAdapter(adapter);

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        mGoodsShareBean=new ShareBean();
        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    private class ShareItemClickListener implements OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(type!=0){
                shares(position);
            }else {
                share(position);
            }

            pop.dismiss();

        }
    }
    public int type;
    public void setType(int type){
        this.type=type;
    }
    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {

        if (position == 0) {
            ThirdShareManager.getInstance().shareWeChat(mGoodsShareBean,false);
        } else if (position == 1) {
            ThirdShareManager.getInstance().shareWeChat(mGoodsShareBean,true);
        }
    }
    /**
     * 分享
     *
     * @param position
     */
    private void shares(int position) {

        if (position == 0) {
            ThirdShareManager.getInstance().shareWeChats(CanTingAppLication.shareBean,true);
        } else if (position == 1) {
            ThirdShareManager.getInstance().shareWeChats(CanTingAppLication.shareBean,false);
        }
    }
    private ShareBean mGoodsShareBean;
    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            ShareParams sp = new ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getText());
//            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            shareParams = sp;
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = "QQ";
                break;
            case 1:
                platform = "QZone";
                break;
            case 2:
                platform = "Wechat";
                break;
            case 3:
                platform = "WechatMoments";
                break;

        }
        return platform;
    }

    private void weibo() {
        ShareParams sp = new ShareParams();
        sp.setText(shareParams.getTitle() + shareParams.getUrl());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());

        Platform weibo = ShareSDK.getPlatform("SinaWeibo");

        weibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        weibo.share(sp);
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        ShareParams sp = new ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());

        Platform qzone = ShareSDK.getPlatform("QZone");

        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    private void qq() {
        ShareParams sp = new ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform("QQ");
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }

    /**
     * 分享到短信
     */
    private void shortMessage() {
        ShareParams sp = new ShareParams();
        sp.setAddress("");
        sp.setText(shareParams.getText() + "这是网址《" + shareParams.getUrl() + "》很给力哦！");

        Platform circle = ShareSDK.getPlatform("ShortMessage");
        circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

}
