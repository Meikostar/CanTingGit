package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/11/9.
 */

public class ZhiBoCareRecyAdapter extends RecyclerView.Adapter<ZhiBoCareRecyAdapter.ViewHolder> {


    @BindView(R.id.care_pic)
    ImageView carePic;
    @BindView(R.id.care_people_num)
    TextView carePeopleNum;
    @BindView(R.id.care_room_name)
    TextView careRoomName;
    @BindView(R.id.care_liver_name)
    TextView careLiverName;
    private Context mContext;
    private ArrayList<Integer> careList;

    public ZhiBoCareRecyAdapter(Context context, ArrayList<Integer> imgCareList) {
        this.mContext = context;
        this.careList = imgCareList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zhibo_care_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        ButterKnife.bind(this, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.carePic.setImageResource(careList.get(position));
        holder.carePeopleNum.setText("00" + position + mContext.getString(R.string.rzgk));
        holder.careRoomName.setText(careRoomName.getText().toString().trim() + position);
        holder.careLiverName.setText("00" + position+mContext.getString(R.string.zbfj));

    }

    @Override
    public int getItemCount() {
        return careList.size();
    }

    @OnClick(R.id.care_pic)
    public void onViewClicked() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView carePic;
        TextView carePeopleNum;
        TextView careRoomName;
        TextView careLiverName;

        public ViewHolder(View itemView) {
            super(itemView);
            carePic = itemView.findViewById(R.id.care_pic);
            carePeopleNum = itemView.findViewById(R.id.care_people_num);
            careRoomName = itemView.findViewById(R.id.care_room_name);
            careLiverName = itemView.findViewById(R.id.care_liver_name);
        }
    }
}
