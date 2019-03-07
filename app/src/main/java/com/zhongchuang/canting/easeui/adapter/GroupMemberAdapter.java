package com.zhongchuang.canting.easeui.adapter;

/**
 * Created by Administrator on 2018/1/6.
 */


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.easeui.bean.USER_AVATAR;
import com.zhongchuang.canting.easeui.widget.EaseImageView;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.CircleTransform;

import java.util.List;


/**
 * Created by syj on 2016/12/6.
 */
public class GroupMemberAdapter extends MyAdapter<USER_AVATAR> {

    private boolean isCreator;
    private String groupid;
    public GroupMemberAdapter(Context context, List<USER_AVATAR> datas, boolean isCreator) {
        super(context, datas, R.layout.em_item_group_member);
        this.isCreator = isCreator;
        mDatas=datas;
    }
    public void setGroupid(String groupid){
        this.groupid=groupid;
    }
    @Override
    public int getCount() {
        int count = 0;
        if (mDatas != null) {
            count = mDatas.size();
        }
        if (isCreator)
            count += 2;
        else count += 1;
        return count;
    }

    @Override
    public USER_AVATAR getItem(int position) {
        if (position >= mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public void convert(MyViewHolder holder, USER_AVATAR group_user, int position) {
        EaseImageView imageView = holder.getView(R.id.avatar);
        TextView name = holder.getView(R.id.name);
        ImageView group_hat = holder.getView(R.id.group_hat);
        group_hat.setVisibility(View.GONE);
        if (position == mDatas.size()) {
            name.setText("");
            imageView.setImageResource(R.drawable.qunjia);
            return;
        }
        if (position == mDatas.size() + 1) {
            name.setText("");
            imageView.setImageResource(R.drawable.qunjian);
            return;
        }
//        USER byChatUserName = UserInfoCacheSvc.getByChatUserName(group_user.getName());
//        if (byChatUserName==null||TextUtils.isEmpty(byChatUserName.friend_nickname)){
//            if (TextUtils.isEmpty(group_user.getNickname())){
//
//            }else {
//                name.setText(group_user.getNickname());
//            }
//        }else {
//            name.setText(byChatUserName.friend_nickname);
//        }



            if(!TextUtils.isEmpty(group_user.user_group_name)&&(!group_user.user_group_name.equals("\"\""))){
                name.setText(group_user.user_group_name);
            }else {
                if(!TextUtils.isEmpty(group_user.getName())){
                    name.setText(group_user.getName());
                }
            }

        name.setVisibility(View.VISIBLE);
        //name.setText(group_user.getUserinfo().user_name);
        Glide.with(mContext).load(StringUtil.changeUrl(group_user.getUser_avatar())).asBitmap().transform(new CircleTransform(mContext)).placeholder(R.drawable.dingdantouxiang).into(imageView);
//        Glide.with(mContext).load(group_user.getUser_avatar()).asBitmap().placeholder(R.drawable.dingdantouxiang).into(imageView);
//        imageView.setImageWithURL(mContext, group_user.getUser_avatar(), R.drawable.dingdantouxiang);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = MyViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        if (position == mDatas.size()) {
            convert(holder, null, position);
        } else if (position == mDatas.size() + 1) {
            convert(holder, null, position);
        } else
            convert(holder, mDatas.get(position), position);
        return holder.getConvertView();
    }

}
