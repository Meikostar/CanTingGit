package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.utils.StringUtil;


/**
 * Created by WZH on 2016/12/25.
 */

public class MemberAdapters extends BaseListAdapter<BEAN> {
    public MemberAdapters(Context ctx) {
        super(ctx);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_menber, null, false);
            viewHolder.avatar = convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BEAN member = datas.get(position);
        if (!TextUtils.isEmpty(member.touxiang)) {
            Glide.with(ctx).load(StringUtil.changeUrl(member.touxiang)).into(viewHolder.avatar);
        }
        return  convertView ;
    }
    public class ViewHolder{
        ImageView avatar ;
    }
}
