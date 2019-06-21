package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.mall.ShopListSearchActivity;
import com.zhongchuang.canting.adapter.VideoItemItemdapter;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivityMin;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;
import com.zhongchuang.canting.widget.GoodsSearchView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class SearchLiveActivity extends BaseActivity1 implements OtherContract.View {


    @BindView(R.id.gsv)
    GoodsSearchView gsv;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private OtherPresenter presenter;
    private String content;
    private int type;
    private VideoItemItemdapter mZhiBoHotRecyAdapter;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_search_live);
        ButterKnife.bind(this);
        presenter = new OtherPresenter(this);

        content = getIntent().getStringExtra("data");
        type = getIntent().getIntExtra("type", 0);
        if (TextUtil.isNotEmpty(content)) {
            gsv.setKeyWords(content);
            contents = content;
        }
        if(type==1){
            gsv.setYunEditHint("请输入你想看的直播视频");
        }else {
            gsv.setYunEditHint("请输入你想搜索的视频");
        }

        mZhiBoHotRecyAdapter = new VideoItemItemdapter(this);
        listview.setAdapter(mZhiBoHotRecyAdapter);
        loadingView.showPager(LoadingPager.STATE_LOADING);

    }

    private String contents;


    private Subscription mSubscription;

    @Override
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.SEAECH) {
                    finish();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        loadingView.setContent(getString(R.string.qsrssnr));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
        gsv.registerListener(new GoodsSearchView.OnSearchBoxClickListener() {
            @Override
            public void onClean() {

            }

            @Override
            public void onCancle() {
                if (TextUtil.isNotEmpty(contents)) {
                    closeKeyBoard();
                    loadingView.showPager(LoadingPager.STATE_LOADING);
                    String searchKeyWords = gsv.getSearchKeyWords();
                    if(TextUtil.isNotEmpty(searchKeyWords)){
                        if(type==1){
                            presenter.searchDirectByNameOrCategory(searchKeyWords,null,null,null);
                        }else {
                            presenter.searchVideoByNameOrCategory(searchKeyWords,null,null,null);
                        }


                    }
                } else {
                    showToasts(getString(R.string.qsrssnr));
                }
            }

            @Override
            public void finishs() {
                finish();
            }

            @Override
            public void onKeyWordsChange(String keyWords) {
                contents = keyWords;
            }
        });
        //条目点击事件的操作
        //TODO
        mZhiBoHotRecyAdapter.setOnItemClickListener(new VideoItemItemdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position, VideoData dataBean) {
//                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
                if(position==-1){

                }else {
                    if (type==0) {
                        String token = SpUtil.getString(SearchLiveActivity.this, "token", "");
                        if (token == null || token.equals("")) {
                            ToastUtils.showNormalToast("你还没有登录，快去登录吧!");
                            Intent gotoLogin = new Intent(SearchLiveActivity.this, LoginActivity.class);
                            startActivity(gotoLogin);
                            return;
                        }
                        if (!dataBean.new_type.equals("0")) {
                            CanTingAppLication.landType = 6;
                            Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivity.class);
                            intent.putExtra("type", 3);
                            intent.putExtra("url", dataBean.video_url);
                            intent.putExtra("name", dataBean.video_name);
                            intent.putExtra("room_info_id", dataBean.room_info_id);
                            intent.putExtra("id", dataBean.user_info_id);
                            startActivity(intent);
                        } else {
                            if (dataBean.video_type.equals("2")) {
                                CanTingAppLication.landType = 6;
                                Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivity.class);
                                intent.putExtra("url", dataBean.video_url);
                                intent.putExtra("name", dataBean.video_name);
                                intent.putExtra("room_info_id", dataBean.room_info_id);
                                intent.putExtra("id", dataBean.user_info_id);
                                startActivity(intent);
                            } else if (dataBean.video_type.equals("3")) {
                                CanTingAppLication.landType = 8;
                                Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivity.class);
                                intent.putExtra("url", dataBean.video_url);
                                intent.putExtra("type", 3);
                                intent.putExtra("name", dataBean.video_name);
                                intent.putExtra("room_info_id", dataBean.room_info_id);
                                intent.putExtra("id", dataBean.user_info_id);
                                startActivity(intent);

                            } else {
                                CanTingAppLication.landType = 8;
                                Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivityMin.class);
                                intent.putExtra("url", dataBean.video_url);
                                intent.putExtra("name", dataBean.video_name);
                                intent.putExtra("room_info_id", dataBean.room_info_id);
                                intent.putExtra("id", dataBean.user_info_id);
                                startActivity(intent);

                            }
                        }

                    } else {

                        if (dataBean.type.equals("2")) {
                            CanTingAppLication.landType = 0;
                            Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivity.class);
                            intent.putExtra("id", dataBean.user_info_id);
                            intent.putExtra("room_info_id", dataBean.room_info_id);
                            startActivity(intent);
                        } else {
                            CanTingAppLication.landType = 1;
                            Intent intent = new Intent(SearchLiveActivity.this, AliyunPlayerSkinActivityMin.class);
                            intent.putExtra("id", dataBean.user_info_id);
                            intent.putExtra("room_info_id", dataBean.room_info_id);
                            startActivity(intent);
                        }


                    }
                }





            }
        });


    }


    public static final int from = 4;

    @Override
    public void initData() {

    }


    private List<VideoData> lists;
    @Override
    public <T> void toEntity(T entity, int types) {
        lists= (List<VideoData>) entity;

        if(lists==null||lists.size()==0){
            if (loadingView != null) {
                if(type==1){
                    loadingView.setContent(getString(R.string.zwzbxx));
                }else {
                    loadingView.setContent("暂无录播视频");
                }

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
                if(type==1){
                    loadingView.setContent(getString(R.string.zwzbxx));
                }else {
                    loadingView.setContent("暂无录播视频");
                }
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }

        }


        closeKeyBoard();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        if(type==1){
            loadingView.setContent(getString(R.string.zwzbxx));
        }else {
            loadingView.setContent("暂无录播视频");
        }
    }



}
