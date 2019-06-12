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
import android.widget.TextView;


import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.live.MineVideoActivity;
import com.zhongchuang.canting.adapter.ZhiBoHotRecyAdapter;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivityMin;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
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
public class ZhiBo_HotFrag extends LazyFragment implements GetLiveViewCallBack {
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private ZhiBoHotRecyAdapter mZhiBoHotRecyAdapter;
    private LiveStreamPresenter presenter;
    private Context mContext;

    public ZhiBo_HotFrag() {
    }

    public ZhiBo_HotFrag(Context context) {
        this.mContext = context;
    }
    private String id="0";
    private int state=-1;
    private String ids="0";
    public void setType(String type,int state){
        id=type;
        this.state=state;
    }

    private GridLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhibo_hot, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new LiveStreamPresenterImpl(this);
        layoutManager = new GridLayoutManager(this.getActivity(), 2);
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        mZhiBoHotRecyAdapter = new ZhiBoHotRecyAdapter(mContext, new ArrayList<ZhiBo_GuanZhongBean.DataBean>());
        mSuperRecyclerView.setAdapter(mZhiBoHotRecyAdapter);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage=1;
                if(id.equals("-1")){
                    getLatestVideoList(1);
                }else {
                    requestData(1);

                }


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.CONTENT){
                    cont= (String) bean.content;
                    if(TextUtil.isEmpty(cont)){
                        id=ids;
                    }else {
                        ids=id;
                        id="0";
                    }
                   reflash();
                }else if(bean.type==SubscriptionBean.SIGN){
                    reflash();
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
//        if(state==0){
//            reflash();
//        }

        return view;
    }
    private Subscription mSubscription;
   private String cont="";
   private boolean hot;
    public void setCont(String content){
        cont=content;
    }

    private void requestData(int type) {
        if(currpage==1){
            hot=false;
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", currpage+"");
        map.put("pageSize", "12");
        map.put("dirTypeId", id+"");
        if(id.equals("0")){
            if(!hot){
                map.put("isEnabled", 1+"");
            }else {
                map.put("isEnabled", 0+"");
            }

        }
        if(TextUtil.isNotEmpty(cont)){
            map.put("directSeeName",  cont);
        }


//        map.put("userLng", "");
//        map.put("userLat", "");
        map.put("userInfoId", CanTingAppLication.userId);

        presenter.getLiveRoomData(map,type);
    }

    private void getLatestVideoList(int type) {
        if(currpage==1){
            hot=false;
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", currpage+"");
        map.put("pageSize", "12");
        map.put("userInfoId", CanTingAppLication.userId);

        presenter.getLatestVideoList(map,type);
    }

    private void reflash(){
        if(mSuperRecyclerView!=null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable(){
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }
    private List<ZhiBo_GuanZhongBean.DataBean> cooks=new ArrayList<>();
    public void onDataLoaded(int loadType, final boolean haveNext, List<ZhiBo_GuanZhongBean.DataBean> list) {
        if(list!=null&&list.size()>0){
            hot = list.get(list.size() - 1).is_enabled != 1;
        }
        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            cooks.clear();
            for (ZhiBo_GuanZhongBean.DataBean info : list) {
                cooks.add(info);
            }
        } else {
            for (ZhiBo_GuanZhongBean.DataBean info : list) {
                cooks.add(info);
            }
        }
        if (cooks != null && cooks.size() != 0) {
           if(loadingView!=null){
               loadingView.showPager(LoadingPager.STATE_SUCCEED);
           }


        } else {
            if(loadingView!=null){
                loadingView.setContent(getString(R.string.zwzbxx));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }

        }

        if(id.equals("-1")){
            mZhiBoHotRecyAdapter.setType(1);
        }else {
            mZhiBoHotRecyAdapter.setType(0);
        }
        mZhiBoHotRecyAdapter.setData(cooks);

        if(mSuperRecyclerView!=null){
            mSuperRecyclerView.hideMoreProgress();

            if (haveNext) {
                mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                    @Override
                    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                        mSuperRecyclerView.showMoreProgress();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (haveNext)
                                    if(mSuperRecyclerView!=null){
                                        mSuperRecyclerView.hideMoreProgress();
                                    }
                                currpage++;
                                if(id.equals("-1")){
                                    getLatestVideoList(2);
                                }else {
                                    requestData(2);

                                }

                            }
                        }, 2000);
                    }
                }, 1);
            } else {

                if(mSuperRecyclerView!=null){
                    mSuperRecyclerView.removeMoreListener();
                    mSuperRecyclerView.hideMoreProgress();
                }
            }

        }




    }

    private void itemClick() {
        //条目点击事件的操作
        //TODO
        mZhiBoHotRecyAdapter.setOnItemClickListener(new ZhiBoHotRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,ZhiBo_GuanZhongBean.DataBean dataBean) {
//                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();

             String   token = SpUtil.getString(getActivity(),"token","");
                if(token ==null|| token.equals("")){
                    ToastUtils.showNormalToast("你还没有登录，快去登录吧!");
                    Intent gotoLogin=new Intent(getActivity(), LoginActivity.class);
                    startActivity(gotoLogin);
                    return ;
                }
                if(id.equals("-1")){
                    if(!dataBean.new_type.equals("0")){
                        CanTingAppLication.landType=6;
                        Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("url",dataBean.video_url);
                        intent.putExtra("name",dataBean.video_name);
                        intent.putExtra("room_info_id",dataBean.room_info_id);
                        intent.putExtra("id",dataBean.user_info_id);
                        startActivity(intent);
                    }else {
                        if(dataBean.video_type.equals("2")){
                            CanTingAppLication.landType=6;
                            Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                            intent.putExtra("url",dataBean.video_url);
                            intent.putExtra("name",dataBean.video_name);
                            intent.putExtra("room_info_id",dataBean.room_info_id);
                            intent.putExtra("id",dataBean.user_info_id);
                            startActivity(intent);
                        }else if(dataBean.video_type.equals("3")) {
                            CanTingAppLication.landType=8;
                            Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                            intent.putExtra("url",dataBean.video_url);
                            intent.putExtra("type",3);
                            intent.putExtra("name",dataBean.video_name);
                            intent.putExtra("room_info_id",dataBean.room_info_id);
                            intent.putExtra("id",dataBean.user_info_id);
                            startActivity(intent);

                        }else  {
                            CanTingAppLication.landType=8;
                            Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivityMin.class);
                            intent.putExtra("url",dataBean.video_url);
                            intent.putExtra("name",dataBean.video_name);
                            intent.putExtra("room_info_id",dataBean.room_info_id);
                            intent.putExtra("id",dataBean.user_info_id);
                            startActivity(intent);

                        }
                    }

                }else {

                        if(dataBean.type.equals("2")){
                            CanTingAppLication.landType=0;
                            Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                            intent.putExtra("id", dataBean.user_info_id);
                            intent.putExtra("room_info_id", dataBean.room_info_id);
                            startActivity(intent);
                        }else {
                            CanTingAppLication.landType=1;
                            Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivityMin.class);
                            intent.putExtra("id", dataBean.user_info_id);
                            intent.putExtra("room_info_id", dataBean.room_info_id);
                            startActivity(intent);
                        }


                }



            }
        });
        mZhiBoHotRecyAdapter.setOnItemLongClickListener(new ZhiBoHotRecyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // Toast.makeText(MainActivity.this,"long click "+imgList.get(position),Toast.LENGTH_SHORT).show();
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
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }
        unbinder.unbind();
    }

    @Override
    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }



    @Override
    public void onFail(int code, String msg) {
        hidePress();
        loadingView.setContent(getString(R.string.zwzbxx));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
        ToastUtils.showNormalToast(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        reflash();

    }
    private View views=null;
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
    @Override
    public void successLive(ZhiBo_GuanZhongBean zhiBo_guanZhongBean, int loadtype) {
        hidePress();
//        mZhiBoHotRecyAdapter.setData(zhiBo_guanZhongBean.getData());
        onDataLoaded(loadtype,!(zhiBo_guanZhongBean.getData().size()<12),zhiBo_guanZhongBean.getData());
    }

    @Override
    public void successRecordLive(ZhiBo_GuanZhongBean zhiBo_guanZhongBean, int loadtype) {
        hidePress();
//        mZhiBoHotRecyAdapter.setData(zhiBo_guanZhongBean.getData());
        onDataLoaded(loadtype,!(zhiBo_guanZhongBean.getData().size()<12),zhiBo_guanZhongBean.getData());
    }

    private boolean isFirst=false;
    @Override
    public void lazyView() {

        reflash();


    }
}
