package com.zhongchuang.canting.fragment.live;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
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
import com.zhongchuang.canting.widget.ClearEditText;
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
public class VideoLiveFragment extends Fragment implements   OtherContract.View {


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


    private List<Fragment> list_zhibofragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;

    private Context mContext;
    private String token;

    public static String pushURL;
    public static String roomID;
    public static String liveId;
    public static String roomPic;
    private OtherPresenter presenter;

    public VideoLiveFragment() {

    }

    public VideoLiveFragment(Context context) {
        this.mContext = context;
    }


    private int current = 0;
    private GAME gameinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_live_fragment, container, false);
        bind = ButterKnife.bind(this, view);
        checkPublishPermission();
        presenter=new OtherPresenter(this);
        presenter.getFirstCategoryList();
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

        if(TextUtil.isNotEmpty(LocationUtil.city)){
            tvCity.setText(LocationUtil.city);
        }
        edtSearchBox.setText("搜索直播视频");
        ivSearchBoxSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CONTENT, cont));
            }
        });
        edtSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchLiveActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
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

        PopularLiveFragment fragment=new PopularLiveFragment(getActivity());
        list_zhibofragment.add(fragment);
        for (int i = 0; i < firstData.size(); i++) {
            VideoLiveItemFragment gameFragment = new VideoLiveItemFragment(getActivity());
            gameFragment.setType(firstData.get(i).id, i);
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
                current = currentIndex;
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

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions.toArray(new String[0]),
                        WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
    private List<LiveItemBean> firstData;
    @Override
    public <T> void toEntity(T entity, int type) {
        firstData= (List<LiveItemBean>) entity;
        title.add("热门");
        for(LiveItemBean bean:firstData){
            title.add(bean.category_name);
        }
        initFragMents(0);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }







    /*@OnClick({R.id.shouye_zhubo, R.id.zhubo_guanzhong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shouye_zhubo:

                  Intent zhuboIntent = new Intent(getActivity(), ZhuBoActivity.class);
                startActivity(zhuboIntent);

                break;
            case R.id.zhubo_guanzhong:

                Intent guanZhongIntent = new Intent(getActivity(), GuanZhongActivity.class);
                startActivity(guanZhongIntent);
                break;
        }
    }*/


}



