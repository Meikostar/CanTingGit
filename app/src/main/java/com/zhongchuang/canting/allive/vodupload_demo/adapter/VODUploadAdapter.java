package com.zhongchuang.canting.allive.vodupload_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodupload_demo.data.ItemInfo;

import java.util.List;

//import com.aliyun.alivcsolution.vodupload_demo.data.ItemInfo;

/**
 * Created by Leigang on 16/11/7.
 */
public class VODUploadAdapter extends ArrayAdapter<ItemInfo> {

    private int resourceId;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public VODUploadAdapter(Context context, int textViewResourceId,
                        List<ItemInfo> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        resourceId = textViewResourceId;
    }

    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemInfo info = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView title = view.findViewById(R.id.item_file);
        title.setText(info.getFile());
        TextView progress = view.findViewById(R.id.item_progress);
        progress.setText(info.getProgress() + "%");
        TextView content = view.findViewById(R.id.item_oss);
        content.setText(info.getOss());
        TextView status = view.findViewById(R.id.item_status);
        status.setText(info.getStatus());
        return view;
    }

}
