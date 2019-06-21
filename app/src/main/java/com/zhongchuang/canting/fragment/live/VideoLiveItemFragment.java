package com.zhongchuang.canting.fragment.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.live.MoreVideoActivity;
import com.zhongchuang.canting.adapter.VideoItemdapter;
import com.zhongchuang.canting.adapter.ZhiBoHotRecyAdapter;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivityMin;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.presenter.impl.LiveStreamPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.viewcallback.GetLiveViewCallBack;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/11/8.
 */

@SuppressLint("ValidFragment")
public class VideoLiveItemFragment extends LazyFragment implements   OtherContract.View {

    Unbinder unbinder;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.listview)
    ListView listview;
    private VideoItemdapter mZhiBoHotRecyAdapter;
    private Context mContext;
    private OtherPresenter presenter;
    public VideoLiveItemFragment() {
    }

    public VideoLiveItemFragment(Context context) {
        this.mContext = context;
    }

    private String id = "0";
    private int state = -1;
    private String ids = "0";

    public void setType(String id, int state) {
        this.id = id;
        this.state = state;
    }


    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mZhiBoHotRecyAdapter = new VideoItemdapter(mContext);
        listview.setAdapter(mZhiBoHotRecyAdapter);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        currpage = 1;

        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.CONTENT) {
                    cont = (String) bean.content;
                    if (TextUtil.isEmpty(cont)) {
                        id = ids;
                    } else {
                        ids = id;
                        id = "0";
                    }
                } else if (bean.type == SubscriptionBean.SIGN) {

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        itemClick();


        return view;
    }

    private Subscription mSubscription;
    private String cont = "";
    private boolean hot;

    public void setCont(String content) {
        cont = content;
    }


    private void itemClick() {
        //条目点击事件的操作
        //TODO
        mZhiBoHotRecyAdapter.setOnItemClickListener(new VideoItemdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position, VideoData dataBean) {
//                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
                  if(position==-1){
                      Intent intent = new Intent(getActivity(), MoreVideoActivity.class);
                      intent.putExtra("id",dataBean.secondId);
                      intent.putExtra("type",1);
                      startActivity(intent);
                  }else {

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
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
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

        if (TextUtil.isNotEmpty(id)) {
            presenter.getDefaultLiveAndCategory(id);
        } else {
            loadingView.setContent(getString(R.string.zwzbxx));
            loadingView.showPager(LoadingPager.STATE_EMPTY);
        }

    }
    private VideoData data;
    private List<VideoData> lists=new ArrayList<>();
    @Override
    public <T> void toEntity(T entity, int type) {
       data= (VideoData) entity;
       lists.clear();
       if(data==null||data.videoList==null){
           if (loadingView != null) {
               loadingView.setContent(getString(R.string.zwzbxx));
               loadingView.showPager(LoadingPager.STATE_EMPTY);
           }
           return;
       }
       for(VideoData bean:data.videoList){
           if(bean.directRoom!=null&&bean.directRoom.size()>0){
               lists.add(bean);

           }
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

            loadingView.setContent(getString(R.string.zwzbxx));

    }
}
