package com.zhongchuang.canting.adapter.recycle;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mine.ImageListWitesActivity;
import com.zhongchuang.canting.adapter.viewholder.ImageViewHolder;
import com.zhongchuang.canting.been.CommetLikeBean;
import com.zhongchuang.canting.been.GoodsComentBean;
import com.zhongchuang.canting.been.GoodsImageBean;
import com.zhongchuang.canting.been.IMG;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.bigimage.BigImageActivity;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.GoodsCommentListView;
import com.zhongchuang.canting.widget.MultiImageView;
import com.zhongchuang.canting.widget.PraiseTextView;


import java.util.ArrayList;
import java.util.List;

/***
 * 功能描述:今日新款适配器
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/
public class QFriendRecyleAdapter extends BaseRecycleViewAdapter {

    private Context context;
    private IMG guide_img = new IMG();
    private String userid = SpUtil.getUserInfoId(context);

    private QFriendClickListener listener;

    public interface QFriendClickListener {
        void clicks(int poistion, int commentPosition, QfriendBean infos);
        void click(int poistion, int commentPosition, QfriendBean infos);

    }

    public void setListener(QFriendClickListener listener) {
        this.listener = listener;
    }

    public QFriendRecyleAdapter(Context context) {
        this.context = context;

    }

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_qfriend, parent, false);


        viewHolder = new ImageViewHolder(view);


        return viewHolder;
    }

    public boolean isProse;
     private List<String> imgs=new ArrayList<>();
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolders, final int position) {

        ImageViewHolder viewHolder = (ImageViewHolder) viewHolders;
        final QfriendBean infos = (QfriendBean) datas.get(position);
        final List<CommetLikeBean> comment = infos.commentList;
        boolean hasComment = comment.size() > 0 ? true : false;
        viewHolder.iv_shop_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(TextUtil.isEmpty(infos.user_name)){
            if (TextUtil.isNotEmpty(infos.friendCircles.user_name)) {
                viewHolder.tv_shop_name.setText(infos.friendCircles.user_name);
            }
            viewHolder.tv_good_name.setText(TimeUtil.formatToFileDi(infos.friendCircles.create_time));
            Glide.with(context).load(StringUtil.changeUrl(infos.friendCircles.getUser_avatar())).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.dingdantouxiang).into(viewHolder.iv_shop_head);

        }else {
            if (TextUtil.isNotEmpty(infos.user_name)) {
                viewHolder.tv_shop_name.setText(infos.user_name);
            }
            viewHolder.tv_good_name.setText(TimeUtil.formatToFileDi(infos.create_time));
            Glide.with(context).load(StringUtil.changeUrl(infos.getUser_avatar())).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.dingdantouxiang).into(viewHolder.iv_shop_head);

        }


        viewHolder.rl_qf1.setVisibility(View.VISIBLE);

        if (TextUtil.isNotEmpty(infos.getDynamic())) {
            viewHolder.tv_titel.setVisibility(View.VISIBLE);
            viewHolder.tv_titel.setText(infos.getDynamic());
        } else {
            viewHolder.tv_titel.setVisibility(View.GONE);
        }
//            viewHolder.tv_count.setText(context.getString(R.string.liulang)+infos.getBrowse_num()+context.getString(R.string.ci));
        isProse = false;
        if (infos.likeList != null && infos.likeList.size() > 0) {
            for (CommetLikeBean commetLikeBean : infos.likeList) {
                if (commetLikeBean.from_uid.equals(userid)) {
                    isProse = true;
                }
            }
        } else {
            viewHolder.iv_prases.setImageResource(R.drawable.qf2);
        }
        if (isProse) {
            viewHolder.iv_prases.setImageResource(R.drawable.qf3);
        } else {
            viewHolder.iv_prases.setImageResource(R.drawable.qf2);
        }

        if (type != 0) {
            viewHolder.iv_down_show.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_down_show.setVisibility(View.GONE);
        }
        String images="";
        if(TextUtil.isNotEmpty(infos.post_image)){
            images=infos.post_image;
        }
        // viewHolder.tv_count.setText(infos.getPraise_num());

        if (TextUtil.isNotEmpty(images)) {
            String[] split = images.split(",");
            if (split != null && split.length > 0) {
                viewHolder.multiImageView.setVisibility(View.VISIBLE);
                List<GoodsImageBean> lists = new ArrayList<>();
                for (String imgs : split) {
                    GoodsImageBean imageBean = new GoodsImageBean();
                    if(imgs.contains("http://")){
                        imageBean.url_ = imgs;
                    }else {
                        imageBean.url_ = QiniuUtils.baseurl+imgs;
                    }

                    lists.add(imageBean);
                }
                viewHolder.multiImageView.setList(lists);
            }


            viewHolder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    imgs.clear();
                    if (guide_img.child_list.size() > 0) {
                        guide_img.child_list.clear();
                    }
                    if (TextUtil.isNotEmpty(infos.post_image)) {
                        String[] split = infos.post_image.split(",");
                        if (split != null && split.length > 0) {
                            for (String img_url : split) {
                                IMG tem_img = new IMG();
                                tem_img.type = IMG.IMG_URL;
                                if(img_url.contains("http://")){
                                    tem_img.img_url = img_url;
                                }else {
                                    tem_img.img_url = QiniuUtils.baseurl+img_url;
                                }
                                imgs.add(tem_img.img_url);
                                guide_img.child_list.add(tem_img);
                            }
                        }
                    }

                    String[] toBeStored = imgs.toArray(new String[imgs.size()]);
                    Intent intent = new Intent(context, BigImageActivity.class);
                    intent.putExtra("images", toBeStored);
                    intent.putExtra("page", position);
                    context.startActivity(intent);
//                        Intent intent = new Intent(context, ImageListWitesActivity.class);
//                        intent.putExtra("img", guide_img);
//                        intent.putExtra("position", position);
//                        context.startActivity(intent);
                }
            });
        } else {
            viewHolder.multiImageView.setVisibility(View.GONE);
        }
        viewHolder.iv_down_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clicks(position, -3, infos);
            }
        });

        if (infos.likeList != null && infos.likeList.size() > 0) {
            viewHolder.mPraiseTextView.setVisibility(View.VISIBLE);
            List<PraiseTextView.PraiseInfo> mPraiseInfos = new ArrayList<>();
            int i = 0;
            for (CommetLikeBean name : infos.likeList) {
                if (i == 0) {
                    mPraiseInfos.add(new PraiseTextView.PraiseInfo().setNickname("\t" + name.from_nickname));
                } else {
                    mPraiseInfos.add(new PraiseTextView.PraiseInfo().setNickname(name.from_nickname));
                }
            }
            viewHolder.mPraiseTextView.setNameTextColor(R.color.slow_black);//设置名字字体颜色
            viewHolder.mPraiseTextView.setIcon(R.drawable.qf3);//设置图标
            viewHolder.mPraiseTextView.setMiddleStr(",");//设置分割文本
            viewHolder.mPraiseTextView.setIconSize(new Rect(0, 0, 50, 50));//设置图标大小，默认与字号匹配
            viewHolder.mPraiseTextView.setData(mPraiseInfos);//设置数据
        } else {
            viewHolder.mPraiseTextView.setVisibility(View.GONE);
        }
        viewHolder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clicks(position, -1, infos);
            }
        });
        viewHolder.iv_prase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clicks(position, -2, infos);


            }
        });


        if (hasComment) {
            viewHolder.lv_comments.setOnItemClickListener(new GoodsCommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int commentPosition) {
                    if (!comment.get(commentPosition).from_uid.equals(userid)) {
                        listener.clicks(position, commentPosition, infos);
                    }

                }
            });
            viewHolder.lv_comments.setOnItemLongClickListener(new GoodsCommentListView.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int commentPosition) {
                    if (infos.user_info_id.equals(userid)||comment.get(commentPosition).from_uid.equals(userid)) {
                        listener.click(position, commentPosition, infos);
                    }
                }
            });
            viewHolder.lv_comments.setDatas(comment, infos.user_info_id);
        } else {
            viewHolder.lv_comments.setDatas(null, "");
        }


    }


//    @Override
//    public int getItemViewType(int position) {
//
//        int type=TodayGoodsBean.TYPE_IMAGE;
//
//        if(datas!=null && datas.size()>0 && position>=0)
//        {
//            TodayGoodsBean item = (TodayGoodsBean) datas.get(position);
//
//            type=item.getType();
//        }
//
//        return type;
//    }

    @Override
    public int getItemCount() {

        int count = 0;

        if (datas != null && datas.size() > 0) {
            count = datas.size();
        }

        return count;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


}