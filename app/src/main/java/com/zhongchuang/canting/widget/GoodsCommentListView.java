package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.CommetLikeBean;
import com.zhongchuang.canting.been.GoodsComentBean;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/***
 * 功能描述:今日新款评论ListView
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/

public class GoodsCommentListView extends LinearLayout {

    private int itemColor;

    private int itemSelectorColor;

    private OnItemClickListener onItemClickListener;

    private OnItemLongClickListener onItemLongClickListener;

    private List<CommetLikeBean> mDatas = new ArrayList<>();

    private LayoutInflater layoutInflater ;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public String user_id;

    public void setDatas(List<CommetLikeBean> datas,String user_id){
        mDatas.clear();
        this.user_id=user_id;
        if(datas != null ){
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public List<CommetLikeBean> getDatas(){
        return mDatas;
    }

    public GoodsCommentListView(Context context) {
        super(context);
    }

    public GoodsCommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public GoodsCommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GoodCommentsListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.GoodCommentsListView_item_color, getResources().getColor(R.color.good_comments_item_default));
            itemSelectorColor = typedArray.getColor(R.styleable.GoodCommentsListView_item_selector_color, getResources().getColor(R.color.good_comments_item_selector_default));

        }finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged(){

        removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position){

        if(layoutInflater == null){

            layoutInflater = LayoutInflater.from(getContext());

        }

        View convertView = layoutInflater.inflate(R.layout.item_friend_comment, null, false);

        TextView commentTv = convertView.findViewById(R.id.tv_comment);

        final TodayNewGoodsMovementMethod circleMovementMethod = new TodayNewGoodsMovementMethod(itemSelectorColor, itemSelectorColor);

        final CommetLikeBean bean = mDatas.get(position);
        String name="";
        if(TextUtil.isNotEmpty(bean.from_uname)){
             name=bean.from_uname;
        }else {
             name = bean.from_uname;
        }


        String id = bean.from_uid;

        String toReplyName ="";
        if(bean.from_uid.equals(bean.to_uid)){

                toReplyName="";

        }else {
            toReplyName = bean.to_uname;
        }




        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(setClickableSpan(name, id+""));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(getContext().getString(R.string.fh));

            builder.append(setClickableSpan(toReplyName, bean.to_uid+""));
        }

        builder.append(": ");
        //转换表情字符
        String contentBodyStr="";
        if(TextUtil.isNotEmpty(bean.content)){
            contentBodyStr= bean.content;
        }else {
            contentBodyStr= bean.content;
        }


        builder.append(StringUtil.formatUrlString(contentBodyStr));

        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final String id) {
        if(TextUtil.isEmpty(textStr)){
            textStr="zx";
        }
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ClickableSpan(){
                                    @Override
                                    public void onClick(View widget) {
                                        //Toast.makeText(YunShlApplication.mYunShlApplication, textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }



}
