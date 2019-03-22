package com.zhongchuang.canting.fragment.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.fragment.message.QFriendCircleFragment;
import com.zhongchuang.canting.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

;

/**
 * Created by Administrator on 2017/10/25.
 */
@SuppressLint("ValidFragment")
public class ZBLiveFragment extends Fragment {


    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_live_content)
    TextView tvLiveContent;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_detail)
    TextView tvDetail;


    private List<Fragment> list_zhibofragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;

    private Context mContext;
    public static String liveId;


    public ZBLiveFragment() {

    }

    public ZBLiveFragment(Context context) {
        this.mContext = context;
    }


    private int current = 0;
    private GAME gameinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zblive_fragment, container, false);
        bind = ButterKnife.bind(this, view);
        viewpagerMain.setNoCanScroll(true);

        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(0);
            }
        });
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(1);
            }
        });
        return view;

    }
    public void selectFragment(int poistion){
        switch (poistion){
            case 0:
                tvVideo.setBackground(getResources().getDrawable(R.drawable.alivc_choose));
                tvVideo.setTextColor(getResources().getColor(R.color.white));
                tvDetail.setTextColor(getResources().getColor(R.color.color9));
                tvDetail.setBackground(null);
                viewpagerMain.setCurrentItem(0);
                break;
            case 1:
                tvDetail.setBackground(getResources().getDrawable(R.drawable.alivc_chooses));
                tvDetail.setTextColor(getResources().getColor(R.color.white));
                tvVideo.setTextColor(getResources().getColor(R.color.color9));
                tvVideo.setBackground(null);
                viewpagerMain.setCurrentItem(1);
                break;
        }
    }
    private String cont = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFragMents();

    }

    private QFriendCircleFragment handFragment3;
    private ZBVideoFragment gameFragment;

    private void initFragMents() {
        list_zhibofragment = new ArrayList<>();

        handFragment3 = new QFriendCircleFragment();
        gameFragment = new ZBVideoFragment(getActivity());
        handFragment3.setType(6);
        list_zhibofragment.add(gameFragment);
        list_zhibofragment.add(handFragment3);


        mainViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), list_zhibofragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(current);

    }


    private Unbinder bind;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
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



