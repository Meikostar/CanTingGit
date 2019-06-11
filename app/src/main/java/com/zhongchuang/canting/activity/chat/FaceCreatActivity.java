package com.zhongchuang.canting.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.pswpop.CommonAdapter;
import com.zhongchuang.canting.widget.pswpop.KeybordModel;
import com.zhongchuang.canting.widget.pswpop.SelectPopupWindow;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceCreatActivity extends BaseAllActivity implements BaseContract.View {

    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_bg1)
    ImageView ivBg1;
    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.iv_bg2)
    ImageView ivBg2;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.iv_bg3)
    ImageView ivBg3;
    @BindView(R.id.tv_text3)
    TextView tvText3;
    @BindView(R.id.iv_bg4)
    ImageView ivBg4;
    @BindView(R.id.tv_text4)
    TextView tvText4;
    @BindView(R.id.gridView)
    GridView gridView;
    private BasesPresenter presenter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_face_creat);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);

    }
   private String mCurrPsw="";
    @Override
    public void bindEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setNumber(){
        tvText1.setVisibility(View.GONE);
        tvText2.setVisibility(View.GONE);
        tvText3.setVisibility(View.GONE);
        tvText4.setVisibility(View.GONE);
        if(data.size()==1){
            ivBg1.setVisibility(View.GONE);
            tvText1.setText(data.get(0));
            tvText1.setVisibility(View.VISIBLE);
        }else if(data.size()==2){
            ivBg1.setVisibility(View.GONE);
            tvText1.setText(data.get(0));
            ivBg2.setVisibility(View.GONE);
            tvText2.setText(data.get(1));
            tvText1.setVisibility(View.VISIBLE);
            tvText2.setVisibility(View.VISIBLE);
        }else if(data.size()==3){
            ivBg1.setVisibility(View.GONE);
            tvText1.setText(data.get(0));
            ivBg2.setVisibility(View.GONE);
            tvText2.setText(data.get(1));
            ivBg3.setVisibility(View.GONE);
            tvText3.setText(data.get(2));
            tvText1.setVisibility(View.VISIBLE);
            tvText2.setVisibility(View.VISIBLE);
            tvText3.setVisibility(View.VISIBLE);
        }else if(data.size()==4){
            ivBg1.setVisibility(View.GONE);
            tvText1.setText(data.get(0));
            ivBg2.setVisibility(View.GONE);
            tvText2.setText(data.get(1));
            ivBg3.setVisibility(View.GONE);
            tvText3.setText(data.get(2));
            ivBg4.setVisibility(View.VISIBLE);
            tvText4.setText(data.get(3));
            tvText1.setVisibility(View.VISIBLE);
            tvText2.setVisibility(View.VISIBLE);
            tvText3.setVisibility(View.VISIBLE);
            tvText4.setVisibility(View.VISIBLE);
        }
    }
    private List<KeybordModel> list;
    private List<String> data=new ArrayList<>();
    @Override
    public void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            KeybordModel m = new KeybordModel();
            m.setKey(keys[i]);
            list.add(m);
        }
        GirdViewAdapter adapter = new GirdViewAdapter(this, list, R.layout.item_face_keyboard);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //对空按钮不作处理
                if (i != 9) {
                    if (i == 11) {
                        //删除
                        if (data.size() > 0)
                            data.remove(data.size()-1);
                        setNumber();
                    } else {
                        //非删除按钮
                        if(data.size()<4){
                            data.add(list.get(i).getKey());
                        }
                        setNumber();
                        if(data.size()==4){

                        }else {

                        }
                    }
                }

            }
        });
    }

    @Override
    public <T> void toEntity(T entity, int type) {

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
    String[] keys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete"};
    class GirdViewAdapter extends CommonAdapter<KeybordModel> {

        public GirdViewAdapter(Context context, List<KeybordModel> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolders holder, KeybordModel item, int position) {
            if (item.getKey().equals("delete")) {
                holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector_gray));

            } else if (TextUtils.isEmpty(item.getKey())) {
                holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector_gray));

            } else {
                holder.getView(R.id.rela_item).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_bg_selector));
                holder.getView(R.id.iv_delete).setVisibility(View.GONE);

                holder.setText(R.id.tv_key, item.getKey());
//                holder.setText(R.id.tv_key_eng, item.getKeyEng());
            }
        }
    }
}
