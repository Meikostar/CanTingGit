package com.zhongchuang.canting.activity.mine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.chat.AddFriendActivity;
import com.zhongchuang.canting.activity.mall.SearchGoodActivity;
import com.zhongchuang.canting.activity.mall.ShopListSearchActivity;
import com.zhongchuang.canting.adapter.BannerMineAdapter;
import com.zhongchuang.canting.adapter.HandGitsAdapter;
import com.zhongchuang.canting.adapter.QFriendsAdapter;
import com.zhongchuang.canting.adapter.VideoLiveAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.BaseType;
import com.zhongchuang.canting.been.CommetLikeBean;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.been.UserInfoBean;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.PersonInfoPresenter;
import com.zhongchuang.canting.presenter.impl.PersonInfoPresenterImpl;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.viewcallback.GetUserInfoViewCallback;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;
import com.zhongchuang.canting.widget.RegularListView;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.StickyScrollView;
import com.zhongchuang.canting.widget.banner.BannerView;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;
import com.zhongchuang.canting.widget.popupwindow.PopView_CancelOrSure;
import com.zhongchuang.canting.widget.popupwindow.PopView_Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.valuesfeng.picker.tablayout.SlidingScaleTabLayout;
import rx.Subscription;
import rx.functions.Action1;

public class NewPersonDetailActivity extends BaseAllActivity implements BaseContract.View, GetUserInfoViewCallback {


    @BindView(R.id.stub_info)
    ViewStub stubInfo;
    @BindView(R.id.stub_style)
    ViewStub stubStyle;
    @BindView(R.id.stub_video)
    ViewStub stubVideo;
    @BindView(R.id.banner)
    com.youth.banner.Banner banner;
    @BindView(R.id.tv_name)
    TextView tvName;


    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_dj)
    TextView tvDj;
    @BindView(R.id.iv_care)
    ImageView ivCare;
    @BindView(R.id.tablayout)
    SlidingScaleTabLayout tablayout;
    @BindView(R.id.ll_isblack)
    LinearLayout llIsblack;
    @BindView(R.id.ll_friend)
    LinearLayout ll_friend;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_editor)
    Button ivEditor;
    @BindView(R.id.sticky)
    StickyScrollView sticky;

    private BannerMineAdapter bannerMineAdapter;
    private BasesPresenter presenters;
    private String[] titles = {"资料", "动态", "视频"};
    private PersonInfoPresenter personInfoPresenter;
    private View footer_view;//底部加载更多
    private String userInfoId;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_new_person_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        userInfoId = SpUtil.getUserInfoId(CanTingAppLication.getInstance());
        personInfoPresenter = new PersonInfoPresenterImpl(this);
        presenters = new BasesPresenter(this);
        presenters.getUserInformation(id);

        footer_view = LayoutInflater.from(this).inflate(R.layout.lp_footer_refresh, null);//底部加载view

        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.setOffscreenPageLimit(2);
        tablayout.setViewPager(viewPager, titles);
        tablayout.setmTabsContainer(new SlidingScaleTabLayout.TabSelectionListener() {
            @Override
            public void selection(int poition) {
                selectPosition(poition);
            }
        });
        bannerMineAdapter = new BannerMineAdapter(this);
//        bannerView.setAdapter(bannerMineAdapter);
        viewPager.setCurrentItem(0);
        selectPosition(0);
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.MINE_FRESH) {
                    userInfo = (UserInfo) bean.content;

                    if (TextUtil.isEmpty(userInfo.headImage)) {
                        return;
                    }

                    changeSunbmit();

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

    private Subscription mSubscription;

    private String id;

    @Override
    public void getUserSuccess(UserInfo response) {

    }

    @Override
    public void onResultSuccess(BaseResponse baseResponse) {

    }

    @Override
    public void onFail(int code, String msg) {

    }

    private UserInfo userInfo;

    private void changeSunbmit() {


        Map<String, String> map = new HashMap<>();


        if (!TextUtils.isEmpty(userInfo.headImage)) {
            map.put("headImage", userInfo.headImage);
        }
        if (!TextUtils.isEmpty(userInfo.nickname)) {
            map.put("nickname", userInfo.nickname);
        } else {
            ToastUtils.showNormalToast(getString(R.string.qtxndnc));
            return;
        }

//        String userInfoId = SpUtil.getUserInfoId(this);
//            map.put("user_info_id", userInfoId);


        map.put("sex", userInfo.sex + "");
        map.put("userInfoId", SpUtil.getUserInfoId(this));

//        if (!TextUtils.isEmpty(userInfo.accountId)) {
//            map.put("accountId", userInfo.accountId);
//        }
        if (!TextUtils.isEmpty(userInfo.birthday)) {
            map.put("birthday", userInfo.birthday);
        }
        if (!TextUtils.isEmpty(userInfo.personalitySign)) {
            map.put("personalitySign", userInfo.personalitySign);
        }
        personInfoPresenter.submitChange(map);

    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            return (int) view.getTag();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return "标题位置" + position;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TextView textView = new TextView(NewPersonDetailActivity.this);
            textView.setTag(position);
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void bindEvents() {
        ivEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPersonDetailActivity.this, EditorPersonDetailActivity.class);
                intent.putExtra("bean", bean);
                startActivityForResult(intent, 666);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int cout = 0;
    private int sate = 0;

    public String getXz(int month, int day) {
        String content = "";
        if (month == 1) {
            if (day > 19) {
                content = "水瓶座";
            } else {
                content = "摩羯座";
            }
        } else if (month == 2) {
            if (day > 18) {
                content = "双鱼座";
            } else {
                content = "水瓶座";
            }
        } else if (month == 3) {
            if (day > 20) {
                content = "白羊座";
            } else {
                content = "双鱼座";
            }
        } else if (month == 4) {
            if (day > 19) {
                content = "金牛座";
            } else {
                content = "白羊座";
            }
        } else if (month == 5) {
            if (day > 20) {
                content = "双子座";
            } else {
                content = "金牛座";
            }
        } else if (month == 6) {
            if (day > 21) {
                content = "巨蟹座";
            } else {
                content = "双子座";
            }
        } else if (month == 7) {
            if (day > 22) {
                content = "狮子座";
            } else {
                content = "巨蟹座";
            }
        } else if (month == 8) {
            if (day > 22) {
                content = "处女座";
            } else {
                content = "狮子座";
            }
        } else if (month == 9) {
            if (day > 22) {
                content = "天秤座";
            } else {
                content = "处女座";
            }
        } else if (month == 10) {
            if (day > 23) {
                content = "天蝎座";
            } else {
                content = "天秤座";
            }
        } else if (month == 11) {
            if (day > 21) {
                content = "射手座";
            } else {
                content = "天蝎座";
            }
        } else if (month == 12) {
            if (day > 21) {
                content = "摩羯座";
            } else {
                content = "射手座";
            }
        }
        return content;
    }

    private List<Banner> img_path = new ArrayList<>();

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Banner url = (Banner) path;
            if (url == null) {
                return;
            }
            if (Util.isOnMainThread()) {
                Glide.with(context).load(url.image_url).asBitmap().placeholder(R.drawable.moren).into(imageView);
            }
            //Glide 加载图片简单用法


        }
    }

    private void startBanner() {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(img_path);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
//        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public void setPersent() {
        cout = 0;
        if (userInfoId.equals(id)) {
            ivEditor.setVisibility(View.VISIBLE);
            tvGx.setText("自己");
        } else {
            presenters.friendInfo(id);
            ivEditor.setVisibility(View.GONE);
        }
        if (tvLocations == null) {
            return;
        }
        if (TextUtil.isNotEmpty(bean.image_url)) {

            cout++;
        }
        if (TextUtil.isNotEmpty(bean.image_url)) {
            String[] split = bean.image_url.split(",");
            img_path.clear();
            if (TextUtil.isNotEmpty(bean.head_image)) {
                Banner beans = new Banner();
                beans.poition = 1;
                beans.image_url = bean.head_image;
                img_path.add(beans);
            }
            int a = 2;
            for (String imgPath : split) {
                if (imgPath != null) {
                    Banner beans = new Banner();

                    if (!imgPath.contains("http")) {
                        imgPath = QiniuUtils.baseurl + imgPath;
                    }
                    beans.poition = a;
                    a++;
                    beans.image_url = imgPath;
                    img_path.add(beans);


                }
            }
//            bannerMineAdapter.setTotal(img_path.size());
//            bannerMineAdapter.setData(img_path);
        } else {
            img_path.clear();

            if (TextUtil.isNotEmpty(bean.head_image)) {
                Banner beans = new Banner();
                beans.poition = 1;
                beans.image_url = bean.head_image;
                img_path.add(beans);
//                bannerMineAdapter.setTotal(img_path.size());
//                bannerMineAdapter.setData(img_path);
            }
        }
        startBanner();
        if (TextUtil.isNotEmpty(bean.nickname)) {
            tvName.setText(bean.nickname);
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.user_info_id)) {
            tvHao.setText(bean.user_info_id.substring(5));

        }

        String dates = TimeUtil.formatTtimes(bean.create_time);
        tv_date.setText(dates);

        if (TextUtil.isNotEmpty(bean.birthday_year) && !bean.birthday_year.equals("null")) {
            tvXz.setText(getXz(Integer.valueOf(bean.birthday_month), Integer.valueOf(bean.birthday_day)));
            String year = TimeUtil.formatToYear(System.currentTimeMillis());
            int date = Integer.valueOf(year) - Integer.valueOf(bean.birthday_year);
            tvDate.setText(date + "");
            cout++;
        }


        if (TextUtil.isNotEmpty(bean.personality_sign)) {
            tvQm.setText(bean.personality_sign);
            sate = 1;
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.home_town)) {
            tvJx.setText(bean.home_town);
            sate = 1;
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.job)) {
            tvZy.setText(bean.job);
            sate = 1;
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.graduate_school)) {
            tvXx.setText(bean.graduate_school);
            sate = 1;
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.label)) {

            chooses.clear();
            String[] split = bean.label.split(",");
            for (String name : split) {
                BaseType baseType = new BaseType();
                baseType.name = name;
                baseType.isChoos = true;
                chooses.add(baseType);
            }
            setTagAdapter(fblBq);
            cout++;
        }
        if (TextUtil.isNotEmpty(bean.cocial_card)) {

            chooses1.clear();
            String[] split = bean.cocial_card.split(",");
            for (String name : split) {
                BaseType baseType = new BaseType();
                baseType.name = name;
                baseType.isChoos = true;
                chooses1.add(baseType);
            }
            setTagAdapter1(fblSh);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.self_personality)) {

            chooses2.clear();
            String[] split = bean.self_personality.split(",");
            for (String name : split) {
                BaseType baseType = new BaseType();
                baseType.name = name;
                baseType.isChoos = true;
                chooses2.add(baseType);
            }
            setTagAdapter2(fblZw);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.current_state)) {

            chooses3.clear();
            String[] split = bean.current_state.split(",");
            for (String name : split) {
                BaseType baseType = new BaseType();
                baseType.name = name;
                baseType.isChoos = true;
                chooses3.add(baseType);
            }
            setTagAdapter3(fblXz);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.superpower)) {

            chooses4.clear();
            String[] split = bean.superpower.split(",");
            for (String name : split) {
                BaseType baseType = new BaseType();
                baseType.name = name;
                baseType.isChoos = true;
                chooses4.add(baseType);
            }
            setTagAdapter4(fblCn);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.personal_statement)) {
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.movie)) {
            tvDy.setText(bean.movie);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.music)) {
            tvYy.setText(bean.music);
            cout++;
        }

        if (TextUtil.isNotEmpty(bean.book)) {
            tvSj.setText(bean.book);
            cout++;
        }
        if (cout <= 2) {
            tvDjs.setText("Lv.1");
            tvDj.setText("Lv.1");
            tvData.setText("您的资料太少了");
        } else if (cout > 2 && cout <= 5) {
            tvDjs.setText("Lv.2");
            tvDj.setText("Lv.2");
            tvData.setText("您的魅力值一般");
        } else if (cout > 5 && cout <= 7) {
            tvDjs.setText("Lv.3");
            tvDj.setText("Lv.3");
            tvData.setText("您的魅力值较高");
        } else if (cout > 7 && cout < 10) {
            tvDjs.setText("Lv.4");
            tvDj.setText("Lv.4");
            tvData.setText("您的魅力值很高");
        } else if (cout > 10 && cout < 14) {
            tvDjs.setText("Lv.5");
            tvDj.setText("Lv.5");
            tvData.setText("您的魅力值爆表");
        } else {
            tvDj.setText("Lv.6");
            tvDjs.setText("Lv.6");
            tvData.setText("您的魅力值爆表");
        }


    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (chooses.size() > 0) {
            for (int i = 0; i < chooses.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {
                        for (int j = 0; j < datas.size(); j++) {
                            if (j == position) {

                            }

                        }
                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter1(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (chooses1.size() > 0) {
            for (int i = 0; i < chooses1.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses1.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter2(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (chooses2.size() > 0) {
            for (int i = 0; i < chooses2.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses2.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter3(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (chooses3.size() > 0) {
            for (int i = 0; i < chooses3.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses3.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter4(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (chooses4.size() > 0) {
            for (int i = 0; i < chooses4.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses4.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(BaseType content) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(this, 10);
        lp.leftMargin = DensityUtil.dip2px(this, 7);
        lp.rightMargin = DensityUtil.dip2px(this, 7);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(this).inflate(R.layout.dish_item, null);
        if (content.isChoos) {
            view.setBg(R.drawable.blue_regle);
            view.setColors(R.color.white);
        } else {
            view.setBg(R.drawable.black_rectangle_flag);
            view.setColors(R.color.color6);
        }
        int width = (int) DensityUtil.getWidth(this) / 3;
        String name = content.name;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name);
        view.setSize(with + 34, 40, 15, 1);
        view.setLayoutParams(lp);
        view.setCustomText(content.name);

        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 666) {
                presenters.getUserInformation(id);
            }
        }


    }

    private int state;

    @Override
    public void initData() {

    }

    public void selectPosition(int position) {
        switch (position) {
            case 0:
                updateUI(1);

                break;
            case 1:
                updateUI(2);

                break;
            case 2:
                updateUI(3);

                break;
        }
    }

    /**
     * 根据tab点击事件更新UI
     *
     * @param id
     */

    private String username = SpUtil.getNick(this);
    private String userid = SpUtil.getUserInfoId(this);
    private QFriendsAdapter qFriendsAdapter;
    private VideoLiveAdapter liveAdapter;
    private HandGitsAdapter handGitsAdapter;
    private FrameLayout container;
    private RegularListView relist_info;
    private ListView relist_infos;
    private LinearLayout ll_rbg;
    private RegularListView relist_hand;
    private View stubStyles;
    private View stubInfos;
    private View stubVideos;
    private LoadingPager loadingView;

    private TextView tvLocations;

    private ImageView ivCares;
    private TextView tvDjs;
    private TextView tvData;
    private LinearLayout llZh;
    private TextView tvInfo;
    private TextView tvHao;
    private TextView tv_date;
    private LinearLayout llInfo;
    private TextView tvJx;
    private LinearLayout llJx;
    private LinearLayout llXz;
    private TextView tvZy;
    private TextView tvDy;
    private TextView tvYy;
    private TextView tvSj;
    private LinearLayout llZy;
    private TextView tvXx;
    private LinearLayout llXx;
    private TextView tvQm;
    private TextView tvXz;
    private LinearLayout llQm;
    private LinearLayout llPerson;
    private ImageView ivArrows;
    private FlexboxLayout fblBq;
    private FlexboxLayout fblSh;
    private FlexboxLayout fblZw;
    private FlexboxLayout fblXz;
    private FlexboxLayout fblCn;
    private LinearLayout llBq;
    private TextView tvSm;
    private TextView tvGx;
    private LinearLayout llOther;

    private void updateUI(int poistion) {


        if (stubInfos != null)
            stubInfo.setVisibility(View.GONE);
        if (stubStyles != null)
            stubStyle.setVisibility(View.GONE);
        if (stubVideos != null)
            stubVideo.setVisibility(View.GONE);
        switch (poistion) {
            case 1:
                if (stubInfos == null) {

                    stubInfos = stubInfo.inflate();
                    tvLocations = (TextView) findViewById(R.id.tv_location);
                    ivCares = (ImageView) findViewById(R.id.iv_care);
                    ivArrows = (ImageView) findViewById(R.id.iv_arrows);
                    tvDjs = (TextView) findViewById(R.id.tv_djs);
                    tvData = (TextView) findViewById(R.id.tv_data);
                    llZh = (LinearLayout) findViewById(R.id.ll_zh);
                    llJx = (LinearLayout) findViewById(R.id.ll_jx);
                    llXz = (LinearLayout) findViewById(R.id.ll_xz);
                    llZy = (LinearLayout) findViewById(R.id.ll_zy);
                    llInfo = (LinearLayout) findViewById(R.id.ll_info);
                    llXx = (LinearLayout) findViewById(R.id.ll_xx);
                    llQm = (LinearLayout) findViewById(R.id.ll_qm);
                    llPerson = (LinearLayout) findViewById(R.id.ll_person);
                    llOther = (LinearLayout) findViewById(R.id.ll_other);
                    llBq = (LinearLayout) findViewById(R.id.ll_bq);
                    tvInfo = (TextView) findViewById(R.id.tv_info);
                    tvHao = (TextView) findViewById(R.id.tv_hao);
                    tv_date = (TextView) findViewById(R.id.tv_dates);
                    tvJx = (TextView) findViewById(R.id.tv_jx);
                    tvZy = (TextView) findViewById(R.id.tv_zy);
                    tvDy = (TextView) findViewById(R.id.tv_dy);
                    tvYy = (TextView) findViewById(R.id.tv_yy);
                    tvSj = (TextView) findViewById(R.id.tv_sj);
                    tvXx = (TextView) findViewById(R.id.tv_xx);
                    tvQm = (TextView) findViewById(R.id.tv_qm);
                    tvXz = (TextView) findViewById(R.id.tv_xz);
                    fblBq = (FlexboxLayout) findViewById(R.id.fbl_bq);
                    fblSh = (FlexboxLayout) findViewById(R.id.fbl_sh);
                    fblZw = (FlexboxLayout) findViewById(R.id.fbl_zw);
                    fblXz = (FlexboxLayout) findViewById(R.id.fbl_xz);
                    fblCn = (FlexboxLayout) findViewById(R.id.fbl_cn);
                    tvSm = (TextView) findViewById(R.id.tv_sm);
                    tvGx = (TextView) findViewById(R.id.tv_gx);

                } else {
                    stubInfos.setVisibility(View.VISIBLE);
                }

                break;
            case 2:


                if (stubStyles == null) {

                    stubStyles = stubStyle.inflate();
                    relist_info = (RegularListView) findViewById(R.id.list_info);
                    relist_infos = (ListView) findViewById(R.id.list_infos);

                    ll_rbg = (LinearLayout) findViewById(R.id.ll_rbg);
                    loadingView = (LoadingPager) findViewById(R.id.loadingViewzb);
                    qFriendsAdapter = new QFriendsAdapter(NewPersonDetailActivity.this);
                    presenters.getFriendCirclesList(1, -1 + "", id);
//                    relist_info.addFooterView(footer_view);
//                    relist_info.addFooterView(footer_view);
                    tablayout.setFocusable(true);
                    tablayout.setFocusableInTouchMode(true);
                    tablayout.requestFocus();


                    intListener();
                } else {
                    stubStyle.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                if (stubVideos == null) {

                    stubVideos = stubVideo.inflate();


                } else {
                    stubVideo.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    public int Dp2Px(Context context, float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }




    private PopView_Comment popComment;
    private PopView_CancelOrSure popView_cancelOrSure;
    private PopView_CancelOrSure popView_cancelOrSures;
    private Context mContext = this;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (banner != null) {
            banner.releaseBanner();

        }

    }

    public void intListener() {


        popView_cancelOrSure = new PopView_CancelOrSure(this);
        popView_cancelOrSure.setTitle(getString(R.string.qrscgtd));
        popView_cancelOrSure.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenters.delFriendCircle(qfd_id, sendId);
                }

            }
        });

        popView_cancelOrSures = new PopView_CancelOrSure(this);
        popView_cancelOrSures.setTitle(getString(R.string.qrscgpl));
        popView_cancelOrSures.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenters.delFriendComment(qfd_id, sendId, fromId, Id);
                }

            }
        });
        popComment = new PopView_Comment(this);
        popComment.setOnPop_CommentListenerr(new PopView_Comment.OnPop_CommentListener() {
            @Override
            public void chooseDeviceConfig(String commentStr) {
                sendNewGoodsComment(commentStr);
            }
        });
        qFriendsAdapter.setListener(new QFriendsAdapter.QFriendClickListener() {
            @Override
            public void clicks(int poistions, int commentPositions, QfriendBean infos) {
                commentPosition = commentPositions;
                qfd_id = infos.id;

                if (commentPositions == -1) {
                    commentPosition = -1;
                    to_id = userid;
                    toId = infos.user_info_id;
                    sendId = infos.user_info_id;
                    info = infos;
                    poistion = poistions;
                    popComment.showPopView("");
                } else if (commentPositions == -2) {
                    poistion = poistions;
                    isProse = false;
                    datas.clear();
                    if (infos.likeList != null && infos.likeList.size() > 0) {
                        for (CommetLikeBean commetLikeBean : infos.likeList) {
                            if (commetLikeBean.from_uid.equals(userid)) {
                                isProse = true;
                            } else {
                                datas.add(commetLikeBean);
                            }
                        }
                        if (!isProse) {
                            CommetLikeBean bean = new CommetLikeBean();
                            bean.from_nickname = username;
                            bean.from_uid = userid;
                            datas.add(bean);
                        }
                    }
                    list.get(poistions).likeList = datas;
                    presenters.changeFriendLike(qfd_id, isProse ? "2" : "1");
                } else if (commentPositions == -3) {
                    sendId = infos.user_info_id;
                    poistion = poistions;
                    popView_cancelOrSure.showPopView();
                } else {
                    to_id = infos.commentList.get(commentPosition).from_uid;
                    toId = infos.commentList.get(commentPosition).from_uid;
                    popComment.showPopView("");
                    if (TextUtil.isNotEmpty(infos.commentList.get(commentPosition).from_uname)) {
                        popComment.setTextHints(getString(R.string.hf) + infos.commentList.get(commentPosition).from_uname + ":");

                    } else {
                        popComment.setTextHints(getString(R.string.pl) + infos.commentList.get(commentPosition).from_uname + ":");
                    }

                    poistion = poistions;
                    info = infos;

                }
            }

            @Override
            public void click(int poistions, int commentPosition, QfriendBean infos) {
                qfd_id = infos.id;
                sendId = infos.user_info_id;
                Id = infos.commentList.get(commentPosition).id;
                fromId = infos.commentList.get(commentPosition).from_uid;

                poistion = poistions;
                popView_cancelOrSures.showPopView();
            }
        });
    }

    private void sendNewGoodsComment(String commentStr) {
        String content = commentStr.trim();

        presenters.addFriendComment(qfd_id, to_id, userid, content);


    }

    private List<BaseType> chooses = new ArrayList<>();//标签数据
    private List<BaseType> chooses1 = new ArrayList<>();//标签数据
    private List<BaseType> chooses2 = new ArrayList<>();//标签数据
    private List<BaseType> chooses3 = new ArrayList<>();//标签数据
    private List<BaseType> chooses4 = new ArrayList<>();//标签数据

    /**
     * 朋友圈
     */
    public static final String AVATER = "hx_avater";
    public static final String NAME = "hx_name";
    public static final String UID = "hx_uid";
    private boolean isProse;
    private List<CommetLikeBean> datas = new ArrayList<>();
    private String sendId;
    private String Id;
    private String toId;
    private String fromId;
    private String qfd_id;
    private String to_id;
    public static final String GID = "hx_gid";
    private int poistion;
    private int type = 0;
    private int commentPosition;
    private String myname;
    List<QfriendBean> list = new ArrayList<>();
    List<QfriendBean> lists;
    List<String> prase_list = new ArrayList<>();
    QfriendBean info;
    FriendInfo infos;
    public static final String FUID = "hx_fuid";
    public static final String FNAME = "hx_fname";
    private List<QfriendBean> data = new ArrayList<>();

    private UserInfoBean bean;

    @Override
    public <T> void toEntity(T entity, int type) {
        sticky.smoothScrollTo(0, 20);


        if (type == 66) {//评论

            if (TextUtil.isNotEmpty(userid) && TextUtil.isNotEmpty(sendId)) {
                if (!toId.equals(sendId)) {
                    //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                    final EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", toId);
                    message.setAttribute("em_robot_message", true);
                    message.setAttribute(AVATER, SpUtil.getAvar(this));
                    message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                    message.setAttribute(FUID, sendId);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                            if (conv == null) {
                                return;
                            }
                            conv.removeMessage(message.getMsgId());
                        }
                    }).start();

                }
                final EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", sendId);
                message.setAttribute("em_robot_message", true);
                message.setAttribute(AVATER, SpUtil.getAvar(this));
                message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                message.setAttribute(FUID, sendId);
                EMClient.getInstance().chatManager().sendMessage(message);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                        if (conv == null) {
                            return;
                        }
                        conv.removeMessage(message.getMsgId());
                    }
                }).start();
            }
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    setWithOrHeigth();
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);

                    loadingView.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }
            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard();
        } else if (type == 77) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    setWithOrHeigth();
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);

                    loadingView.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }


            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard();
        } else if (type == 88) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    setWithOrHeigth();
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);

                    loadingView.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }


            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard();
        } else if (type == 321) {
            bean = (UserInfoBean) entity;
            setPersent();

        } else if (type == 79) {
            infos = (FriendInfo) entity;
            if (infos.isFriend != 1) {
                tvGx.setText("陌生人");
                ll_friend.setVisibility(View.VISIBLE);
                ivCare.setVisibility(View.GONE);
                ll_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FriendSearchBean.DataBean dataBean = new FriendSearchBean.DataBean();
                        dataBean.setNickname(infos.nickname);
                        dataBean.setRingLetterName(id);
                        Intent intent = new Intent(NewPersonDetailActivity.this, AddFriendActivity.class);
                        intent.putExtra("data", HomeActivitys.messageGroup);
                        intent.putExtra("datas", dataBean);
                        startActivityForResult(intent, 5);


                    }
                });
            } else {
                tvGx.setText("好友");
                ll_friend.setVisibility(View.GONE);
                ivCare.setVisibility(View.VISIBLE);
            }

        } else if (type == 99) {
            presenters.getFriendCirclesList(1, -1 + "", id);
        } else if (type == 55 || type == 1) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            if (datas != null) {
                relist_infos.setVisibility(View.VISIBLE);
                ll_rbg.setVisibility(View.VISIBLE);
                relist_info.setVisibility(View.GONE);
                setWithOrHeigth();
                relist_info.setFocusable(false);
                relist_infos.setFocusable(false);
                relist_infos.setAdapter(qFriendsAdapter);
                list.addAll(datas);
                qFriendsAdapter.setDatas(datas);
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }

//            onDataLoaded(TYPE_PULL_REFRESH, false, datas);
        }


    }

    public void setInfo() {

    }

    public void setWithOrHeigth() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) stubStyle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        stubStyle.setLayoutParams(params);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


}
