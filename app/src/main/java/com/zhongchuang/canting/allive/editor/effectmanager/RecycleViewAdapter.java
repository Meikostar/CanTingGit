/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.editor.effectmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.downloader.DownloaderManager;
import com.zhongchuang.canting.allive.downloader.FileDownloaderModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecycleViewAdapter extends StateController.StateAdapter<RecycleViewAdapter.PasterViewHolder> {

    private List<FileDownloaderModel> list;

    public RecycleViewAdapter(List<FileDownloaderModel> list) {
        this.list = list;
    }

    class PasterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvDesc, mTvRightButton;
        private ImageView mIvIcon;
        private FileDownloaderModel mData;
        private int mPosition;

        public void updateData(int position, FileDownloaderModel data) {
            //this.mData = data;
            this.mPosition = position;
            mData = list.get(position);
            mTvName.setText(mData.getName());
            mTvDesc.setText(mData.getDescription());
            Glide.with(mIvIcon.getContext()).load(mData.getIcon()).into(mIvIcon);
        }

        public PasterViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            mTvRightButton = (TextView) itemView.findViewById(R.id.tv_right_button);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mTvRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 0) {
                        FileDownloaderModel model = list.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        //TODO：删除资源
                        DownloaderManager.getInstance().deleteTask(model.getId());
                    }
                    //notifyDataSetChanged();
                }
            });
            mTvRightButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public PasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aliyun_svideo_layout_effect_manager_list_item, parent, false);
        return new PasterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PasterViewHolder holder, int position) {
        FileDownloaderModel msg = list.get(position);
        holder.updateData(position, msg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
