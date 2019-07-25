package com.zhongchuang.canting.fragment.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.live.SearchLiveActivity;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.LiveItemBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.location.LocationUtil;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.valuesfeng.picker.tablayout.SlidingScaleTabLayout;

/**
 * Created by Administrator on 2017/10/25.
 */
@SuppressLint("ValidFragment")
public class VideoLiveMoreFragment extends Fragment implements OtherContract.View {


    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.edt_search_box)
    TextView edtSearchBox;
    @BindView(R.id.iv_search_box_search)
    ImageView ivSearchBoxSearch;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.back)
    ImageView back;


    private List<Fragment> list_zhibofragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;

    private Context mContext;

    public static String liveId;
    private OtherPresenter presenter;

    public VideoLiveMoreFragment() {

    }

    public VideoLiveMoreFragment(Context context, String secondId, int type) {
        this.mContext = context;
        this.secondId = secondId;
        this.type = type;
    }


    private String secondId;
    private int type;
    private GAME gameinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_live_fragment, container, false);
        bind = ButterKnife.bind(this, view);

        presenter = new OtherPresenter(this);
        presenter.getThirdList(secondId);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (TextUtil.isNotEmpty(LocationUtil.city)) {
            tvCity.setText(LocationUtil.city);
        }
        if (type == 1) {
            edtSearchBox.setText("请输入相关直播视频");
        } else {
            edtSearchBox.setText("请输入相关录播视频");
        }
        ivSearchBoxSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CONTENT, cont));
            }
        });
        edtSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchLiveActivity.class).putExtra("type", type));
            }
        });

        return view;

    }

    private String cont = "";
    public List<GAME> data = new ArrayList<>();
    private List<String> title = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private String[] titles;

    private void initFragMents(int poistion) {
        list_zhibofragment = new ArrayList<>();


        for (int i = 0; i < firstData.size(); i++) {
            VideoMoreFragment gameFragment = new VideoMoreFragment(getActivity());
            gameFragment.setType(firstData.get(i).id, type);
            list_zhibofragment.add(gameFragment);
        }

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), list_zhibofragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(firstData.size() - 1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(poistion);
        titles = title.toArray(new String[title.size()]);
        tablayout.setViewPager(viewpagerMain, titles);
        tablayout.setmTabsContainer(new SlidingScaleTabLayout.TabSelectionListener() {
            @Override
            public void selection(int currentIndex) {

                viewpagerMain.setCurrentItem(currentIndex, false);
            }
        });
    }


    /**
     * 6.0权限处理
     **/
    private boolean bPermission = false;
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private Unbinder bind;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    private List<LiveItemBean> firstData;

    @Override
    public <T> void toEntity(T entity, int type) {
        firstData = (List<LiveItemBean>) entity;
        if (firstData == null || firstData.size() == 0) {
            return;
        }
        for (LiveItemBean bean : firstData) {
            title.add(bean.sec_category_name);
        }
        initFragMents(0);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


}



