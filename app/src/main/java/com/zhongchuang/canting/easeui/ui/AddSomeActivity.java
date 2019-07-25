package com.zhongchuang.canting.easeui.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.bean.PLACE;
import com.zhongchuang.canting.easeui.bean.USER;
import com.zhongchuang.canting.easeui.bean.USER_AVATAR;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class AddSomeActivity extends BaseActivity implements BaseContract.View {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private int type = 0;

    private BasesPresenter presenter;
    private addSomeFragment gategoryFragment;

    private String ids;



    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_fb);
        ButterKnife.bind(this);
        initViews();
        bindEvents();

    }


    public void initViews() {
        presenter = new BasesPresenter(this);
        viewpagerMain.setScanScroll(false);
        ids = getIntent().getStringExtra("ids");

        addFragment();

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setCurrentItem(0);
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type != 0) {

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);

    }

    private void addFragment() {
        mFragments = new ArrayList<>();
        gategoryFragment = new addSomeFragment();

        gategoryFragment.setType(ids, type);
        mFragments.add(gategoryFragment);

    }

    public void bindEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                bnbHome.setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EaseUser> contacts = gategoryFragment.contactListLayout.getContacts();
                list.clear();
                int i = 0;
                chatUserId="";
                for (EaseUser user : contacts) {
                    if (user.isChoose) {
                        if (i == 0) {
                            chatUserId = user.userid;
                        } else {
                            chatUserId = chatUserId + "," + user.userid;
                        }
                        list.add(user.userid);
                        i++;
                    }

                }
//
                if(TextUtil.isEmpty(chatUserId)){
                    ToastUtils.showNormalToast(getString(R.string.qxzcy));
                    return;
                }


//                gategoryFragment.addMenber();
            }
        });

    }

     private String chatUserId;
    public List<String> list = new ArrayList<>();



    public void initData() {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            RxBus.getInstance().unSub(mSubscription);
        }
    }



    @Override
    public <T> void toEntity(T entity, int type) {
        ToastUtils.showNormalToast(getString(R.string.czcg));
        Intent intent = new Intent();
        intent.putExtra("name", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
