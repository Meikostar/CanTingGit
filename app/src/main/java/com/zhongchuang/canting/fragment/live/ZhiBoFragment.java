package com.zhongchuang.canting.fragment.live;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseNevgTitle;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/25.
 */
@SuppressLint("ValidFragment")
public class ZhiBoFragment extends Fragment {


    @BindView(R.id.base_bars)
    BaseNevgTitle bnbHome;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.edt_search_box)
    ClearEditText edtSearchBox;
    @BindView(R.id.iv_search_box_search)
    ImageView ivSearchBoxSearch;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;


    private List<Fragment> list_zhibofragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;

    private Context mContext;
    private String token;

    public static String pushURL;
    public static String roomID;
    public static String liveId;
    public static String roomPic;


    public ZhiBoFragment() {

    }

    public ZhiBoFragment(Context context) {
        this.mContext = context;
    }


    private int current=0;
    private GAME gameinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.zhibo1, container, false);
        bind = ButterKnife.bind(this, view);
        checkPublishPermission();

        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bnbHome.setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bnbHome.setOnChangeListener(new BaseNevgTitle.OnChangeListener() {
            @Override
            public void onChagne(int currentIndex) {
                current = currentIndex;
                bnbHome.setSelect(currentIndex);
                viewpagerMain.setCurrentItem(currentIndex, false);
            }
        });

        ivSearchBoxSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CONTENT,cont));
            }
        });
      edtSearchBox.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
          @Override
          public void afterTextChanged4ClearEdit(Editable s) {
             if(TextUtil.isNotEmpty(s.toString())){
                 cont=s.toString();

             }else {
                 cont="";
                 RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CONTENT,""));
             }
          }

          @Override
          public void changeText(CharSequence s) {

          }
      });

        return view;

    }
    private String cont="";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameinfo = (GAME) getArguments().getSerializable("data");
        if(gameinfo.data==null){
            return;
        }

        if(gameinfo==null){
            gameinfo=new GAME();
        }else {
            int i=0;
            for(GAME game:gameinfo.data){
                if(game.isChoose){
                    gameinfo.data.get(0).isChoose=false;
                    game.isChoose=true;
                    i=1;
                }

            }
            if(i==0){
                gameinfo.data.get(0).isChoose=true;
            }

        }
        initFragMents();
        bnbHome.setDatas(gameinfo.data);
    }

    private void initFragMents() {
        list_zhibofragment = new ArrayList<>();
        if(gameinfo==null||gameinfo.data==null){
            return;
        }
        for (int i = 0; i < gameinfo.data.size(); i++) {
            ZhiBo_HotFrag gameFragment = new ZhiBo_HotFrag(getActivity());
            gameFragment.setType(gameinfo.data.get(i).id,i);
            list_zhibofragment.add(gameFragment);
        }

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), list_zhibofragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(gameinfo.data.size() - 1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(current);

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
                        (String[]) permissions.toArray(new String[0]),
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



