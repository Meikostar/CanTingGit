package com.zhongchuang.canting.widget.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;


/**
 * Created by honghouyang on 16/8/30.
 */
public class ShareAdapter extends BaseAdapter {

    private static String[] shareNames = new String[]{  CanTingAppLication.getInstance().getString(R.string.wx), CanTingAppLication.getInstance().getString(R.string.pyq)};
    private int[] shareIcons = new int[]{
            R.mipmap.yy_img_share_wx,R.mipmap.yy_img_share_pyq};

    private LayoutInflater inflater;

    public ShareAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shareNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_item, null);
        }
        ImageView shareIcon = convertView.findViewById(R.id.share_icon1);
        TextView shareTitle = convertView.findViewById(R.id.share_title1);
        shareIcon.setImageResource(shareIcons[position]);
        shareTitle.setText(shareNames[position]);

        return convertView;
    }
}
