package com.zhongchuang.canting.fragment.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.adapter.VideoItemItemdapter;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivityMin;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/8.
 */

@SuppressLint("ValidFragment")
public class PopularLiveFragment extends LazyFragment implements   OtherContract.View {

    Unbinder unbinder;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.listview)
    ListView listview;
    private VideoItemItemdapter mZhiBoHotRecyAdapter;
    private Context mContext;
    private OtherPresenter presenter;
    public PopularLiveFragment() {
    }

    public PopularLiveFragment(Context context) {
        this.mContext = context;
    }

    private String id = "0";


    public void setType(String id) {
        this.id = id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mZhiBoHotRecyAdapter = new VideoItemItemdapter(mContext);
        listview.setAdapter(mZhiBoHotRecyAdapter);
        loadingView.showPager(LoadingPager.STATE_LOADING);



        itemClick();


        return view;
    }

    private void itemClick() {
        //条目点击事件的操作
        //TODO
        mZhiBoHotRecyAdapter.setOnItemClickListener(new VideoItemItemdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position, VideoData dataBean) {
//                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();


                          if (dataBean.type.equals("2")) {
                              CanTingAppLication.landType = 0;
                              Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                              intent.putExtra("id", dataBean.user_info_id);
                              intent.putExtra("room_info_id", dataBean.room_info_id);
                              startActivity(intent);
                          } else {
                              CanTingAppLication.landType = 1;
                              Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivityMin.class);
                              intent.putExtra("id", dataBean.user_info_id);
                              intent.putExtra("room_info_id", dataBean.room_info_id);
                              startActivity(intent);
                          }









            }
        });



    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private View views = null;
    private TextView tv_content = null;
    private ImageView close = null;
    private TextView title = null;
    private EditText reson = null;

    public void showPopwindow(String name) {

        views = View.inflate(getActivity(), R.layout.tell_popwindow_view, null);
        tv_content = views.findViewById(R.id.tv_content);
        close = views.findViewById(R.id.close);

        tv_content.setText(name);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }




    private boolean isFirst = false;

    @Override
    public void lazyView() {
        if(presenter==null)
          presenter=new OtherPresenter(this);
         presenter.getHotDirect();


    }
    private VideoData data;
    private List<VideoData> lists;
    @Override
    public <T> void toEntity(T entity, int type) {
        lists= (List<VideoData>) entity;

       if(lists==null||lists.size()==0){
           if (loadingView != null) {
               loadingView.setContent(getString(R.string.zwzbxx));
               loadingView.showPager(LoadingPager.STATE_EMPTY);
           }
           return;
       }

        mZhiBoHotRecyAdapter.setData(lists);
        if (lists.size() != 0) {
            if (loadingView != null) {
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
            }


        } else {
            if (loadingView != null) {
                loadingView.setContent(getString(R.string.zwzbxx));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }

        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
