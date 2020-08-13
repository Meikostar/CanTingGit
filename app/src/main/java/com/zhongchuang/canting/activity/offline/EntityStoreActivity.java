package com.zhongchuang.canting.activity.offline;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.youth.banner.loader.ImageLoader;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.ClassifyOneAdapters;
import com.zhongchuang.canting.adapter.FirstClassAdapter;
import com.zhongchuang.canting.adapter.GoodsAttrAdapters;
import com.zhongchuang.canting.adapter.GoodsAttrAdapterss;
import com.zhongchuang.canting.adapter.SecondClassAdapter;
import com.zhongchuang.canting.allive.utils.ScreenUtils;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.base.BroadcastManager;
import com.zhongchuang.canting.been.AreaDto;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.BannerItemDto;
import com.zhongchuang.canting.been.BaseDto;
import com.zhongchuang.canting.been.ConfigDto;
import com.zhongchuang.canting.been.FirstClassItem;
import com.zhongchuang.canting.been.GoodsAttrDto;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Param;
import com.zhongchuang.canting.been.Params;
import com.zhongchuang.canting.been.RecommendListDto;
import com.zhongchuang.canting.been.SecondClassItem;
import com.zhongchuang.canting.been.SmgBaseBean3;
import com.zhongchuang.canting.been.SmgBaseBean4;
import com.zhongchuang.canting.been.SmgBaseBean5;
import com.zhongchuang.canting.been.SmgBaseBean6;
import com.zhongchuang.canting.been.SmgBaseBean7;
import com.zhongchuang.canting.been.SmgParam;
import com.zhongchuang.canting.been.StoreCategoryDto;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.LocationUtils;
import com.zhongchuang.canting.utils.MapUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.UIUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.HorizontalDividerItemDecoration;
import com.zhongchuang.canting.widget.banner.BannerView;

import org.apache.tools.ant.types.Commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 实体店铺
 * Created by rzb on 2019/6/16.
 */
public class EntityStoreActivity extends BaseAllActivity implements View.OnClickListener, BaseContract.View {

    private EntityStoreAdapter     mEntityStoreAdapter;
    private List<RecommendListDto> storeLists      = new ArrayList<RecommendListDto>();
    //使用PopupWindow只显示一级分类
    private PopupWindow            levelsAllPopupWindow;
    private PopupWindow            levelsFjPopupWindow;
    private PopupWindow            levelsSxPopupWindow;
    //只显示一个ListView
    private RecyclerView recyclerView;
    private TextView               tv_one;
    private TextView               tv_two;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    //分类数据
    private List<FirstClassItem>   singleFirstList = new ArrayList<FirstClassItem>();
    ;
    //使用PopupWindow显示一级分类和二级分类
    private PopupWindow levelsPopupWindow;
    //左侧和右侧两个ListView
    private ListView    leftLV, rightLV;
    //左侧一级分类的数据
    private List<FirstClassItem>  firstList  = new ArrayList<FirstClassItem>();
    //右侧二级分类的数据
    private List<SecondClassItem> secondList = new ArrayList<SecondClassItem>();
    private List<SecondClassItem> secondLists = new ArrayList<SecondClassItem>();

    private BasesPresenter presenter;

    private             List<BannerItemDto>  adsList    = new ArrayList<BannerItemDto>();
    public static final int                  GETCITY    = 1001;
    private             String               sitId      = null;
    private             String               categoryId = null;
    private             String               areaId     = null;
    private             String               distance   = null;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_entity_store);
        ButterKnife.bind(this);
        initLevelsAllPopup();
        initeSxPopup();
        initAdapter();
        presenter = new BasesPresenter(this);
        presenter.getHomeBannerss("1");
    }

    @Override
    public void bindEvents() {
        bindClickEvent(store_layout_back, () -> {
            finish();
        });

        BroadcastManager.getInstance(this).addAction(Constants.CHOICE_CITYS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String cName = intent.getStringExtra("String");
                    tv_location.setText(cName);
                    tvLocation.setText(cName);

                    tvLocations.setText(cName);
                    if(tvLocats!=null){
                        tvLocats.setText(cName);
                    }


                }
            }
        });
        //        bindClickEvent(find, () -> {
        //            Bundle bundle = new Bundle();
        //            bundle.putString("includeStr", "st");
        //            gotoActivity(ProductSearchActivity.class,false, bundle);
        //        });

        bindClickEvent(store_ll_location, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStores");
            gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
        });

        bindClickEvent(layout_all, () -> {
            if (levelsAllPopupWindow.isShowing()) {
                levelsAllPopupWindow.dismiss();
            }
            levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
//            levelsAllPopupWindow.showAsDropDown();
            levelsAllPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        bindClickEvent(layout_sx, () -> {
            if (levelsSxPopupWindow == null) {
                return;
            }
            if (levelsSxPopupWindow.isShowing()) {
                levelsSxPopupWindow.dismiss();

            }
            levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

            levelsSxPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        bindClickEvent(layout_near, () -> {
            if (levelsPopupWindow == null) {
                return;
            }
            if (levelsPopupWindow.isShowing()) {
                levelsPopupWindow.dismiss();

            }
            tvLocats.setText(tv_location.getText().toString());
            levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

//            levelsPopupWindow.showAsDropDown(findViewById(R.id.store_div_line_three));
            levelsPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtil.isEmpty(s.toString())){
                    shop_name = "";

                }else {
                    shop_name=s.toString();
                }
                getShopList(TYPE_PULL_REFRESH);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        find.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                //点击搜索要做的操作
                if (TextUtil.isNotEmpty(find.getText().toString())) {

                    shop_name = find.getText().toString();

                } else {
                    shop_name = "";


                }

                getShopList(TYPE_PULL_REFRESH);
                closeKeyBoard();

                return true;
            }
        });
    }

    @Override
    public void initData() {

        getTopBanner();
        getConfigs();
        getTagsList();
        getInducts();
        getLocation();

    }


    private String searchKey;

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).destroy(Constants.CHOICE_CITYS);
    }

    @Override
    public void onBackPressed() {

        if (levelsSxPopupWindow!=null&&levelsSxPopupWindow.isShowing()) {
            levelsSxPopupWindow.dismiss();
        }else {
            if (levelsAllPopupWindow!=null&&levelsAllPopupWindow.isShowing()) {
                levelsAllPopupWindow.dismiss();
            }else {
                if (levelsPopupWindow!=null&&levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();
                }else {
                    finish();
                }
            }
        }


    }

    private GoodsAttrAdapters goodsAttrAdapter;
    private GoodsAttrAdapterss goodsAttrAdapters;
    List<GoodsAttrDto> gadList  = new ArrayList<>();
    List<GoodsAttrDto> gadLists = new ArrayList<>();

    private void initeSxPopup() {
        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高

        levelsSxPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_store_entity_sx, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_select_commodity);
        tv_one = (TextView) view.findViewById(R.id.but_one);
        tv_two = (TextView) view.findViewById(R.id.but_two);
        iv_backs = (ImageView) view.findViewById(R.id.iv_back);
        tvLocations = (TextView) view.findViewById(R.id.tv_location);
        finds3 = (TextView) view.findViewById(R.id.find);
        layout_all1 = (RelativeLayout) view.findViewById(R.id.layout_all);
        layout_sxs = (RelativeLayout) view.findViewById(R.id.layout_sx);
        layout_near2 = (RelativeLayout) view.findViewById(R.id.layout_near);
        finds3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        layout_sxs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
            }
        });
        layout_all1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();

                if (levelsAllPopupWindow == null) {
                    return;
                }
                if (levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();

                }
                levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        layout_near2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
                if (levelsPopupWindow == null) {
                    return;
                }
                if (levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();

                }
                tvLocats.setText(tv_location.getText().toString());
                levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        tvLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        tvLocations.setText(location);
        levelsSxPopupWindow.setContentView(view);
        levelsSxPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));

        levelsSxPopupWindow.setFocusable(false);
        levelsSxPopupWindow.setClippingEnabled(false);
        levelsSxPopupWindow.setHeight(height + 120);
        levelsSxPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsSxPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        tv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsAttrDto> data = goodsAttrAdapter.getData();
                for (GoodsAttrDto dto : data) {
                    for (BaseDto baseDto : dto.data) {
                        baseDto.isChoose = false;
                    }
                }

                goodsAttrAdapter.notifyDataSetChanged();
                onsaleId="";
                serviceId="";
                per_consumption="";
                distance="";


            }
        });
        tv_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsAttrDto> data = goodsAttrAdapter.getData();
                int a=0;
                onsaleId="";
                serviceId="";
                per_consumption="";
                distance="";
                for (GoodsAttrDto dto : data) {
                    for (BaseDto baseDto : dto.data) {
                        if(baseDto.isChoose){
                            if(dto.getKey().equals("优惠和权益")){
                                if(TextUtil.isNotEmpty(onsaleId)){
                                    onsaleId=onsaleId+","+baseDto.id;
                                }else {
                                    onsaleId=baseDto.id;
                                }

                            }
                            if(dto.getKey().equals("服务")){
                                if(TextUtil.isNotEmpty(serviceId)){
                                    serviceId=serviceId+","+baseDto.id;
                                }else {
                                    serviceId=baseDto.id;
                                }

                            } if(dto.getKey().equals("价格")){

                                if(TextUtil.isNotEmpty(per_consumption)){
                                    per_consumption=per_consumption+","+baseDto.id;
                                }else {
                                    per_consumption=baseDto.id;
                                }
                            }   if(dto.getKey().equals("距离")){
                                if(TextUtil.isNotEmpty(distance)){
                                    distance=distance+","+baseDto.id;
                                }else {
                                    distance=baseDto.id;
                                }

                            }

                        }


                    }
                }
                getShopList(TYPE_PULL_REFRESH);
                levelsSxPopupWindow.dismiss();
            }
        });
        iv_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setFocusable(false);
        goodsAttrAdapter = new GoodsAttrAdapters(gadList, this);
        goodsAttrAdapter.setGoodsSpecListener(new GoodsAttrAdapters.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String attStrs, String key) {

                mMap.put(key, attStrs);

            }
        });
        recyclerView.setAdapter(goodsAttrAdapter);
    }

    private RecyclerView        rvOne;
    private RecyclerView        rvTwo;
    private ImageView           iv_back;
    private ImageView           iv_backs;
    private ImageView           iv_backss;
    private TextView            tvLocation;
    private TextView            tvLocations;
    private ClassifyOneAdapters mClassifyOneAdapter;
    private String              location;

    private void initLevelsAllPopup() {
        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高
        levelsAllPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_store_entity_all, null);

        rvOne = (RecyclerView) view.findViewById(R.id.rv_one);
        rvTwo = (RecyclerView) view.findViewById(R.id.rv_two);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        finds2 = (TextView) view.findViewById(R.id.find);
        layout_near1 = (RelativeLayout) view.findViewById(R.id.layout_near);
        layout_alls = (RelativeLayout) view.findViewById(R.id.layout_alls);
        layout_sx2 = (RelativeLayout) view.findViewById(R.id.layout_sx);
        layout_alls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
            }
        });
        finds2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        layout_sx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                if (levelsSxPopupWindow == null) {
                    return;
                }
                if (levelsSxPopupWindow.isShowing()) {
                    levelsSxPopupWindow.dismiss();

                }
                levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        layout_near1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                if (levelsPopupWindow == null) {
                    return;
                }
                if (levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();

                }
                tvLocats.setText(tv_location.getText().toString());
                levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        mClassifyOneAdapter = new ClassifyOneAdapters();
        rvOne.setLayoutManager(new LinearLayoutManager(this));
        rvOne.setAdapter(mClassifyOneAdapter);
        rvOne.setSelected(true);
        rvOne.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.parseColor("#f1f1f1"))
                .size(UIUtil.dp2px(1))
                .build());
        rvOne.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePos = mClassifyOneAdapter.selctedPos;  //之前的位置
                mClassifyOneAdapter.selctedPos = position; //之后选择的位置
                StoreCategoryDto item = (StoreCategoryDto) adapter.getItem(position);
                if (position != prePos) {//更新item的状态
                    mClassifyOneAdapter.notifyItemChanged(prePos);
                    mClassifyOneAdapter.notifyItemChanged(position);

                    goodsAttrAdapters.setNewData(maps.get(item.getId() + ""));
                }


            }
        });
        levelsAllPopupWindow.setContentView(view);
        levelsAllPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));
        levelsAllPopupWindow.setFocusable(false);
        levelsAllPopupWindow.setClippingEnabled(false);
        levelsAllPopupWindow.setHeight(height + 120);
        levelsAllPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsAllPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTwo.setLayoutManager(mLinearLayoutManager);
        rvTwo.setFocusable(false);
        goodsAttrAdapters = new GoodsAttrAdapterss(gadLists, this);
        goodsAttrAdapters.setGoodsSpecListener(new GoodsAttrAdapterss.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String id, String key,boolean isselect) {

                levelsAllPopupWindow.dismiss();
                if(isselect){
                    industryId = id;
                }else {
                    industryId = "";
                }
                getShopList(TYPE_PULL_REFRESH);
            }
        });
        rvTwo.setAdapter(goodsAttrAdapters);
    }

    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManagers;


    private RecyclerView rvOnes;
    private RecyclerView rvTwos;

    private TextView            tvLocationss;
    private ClassifyOneAdapters mClassifyOneAdaps;



    private Map<String, String> mMap = new HashMap<>();

    private void getTopBanner() {
//        //showLoadDialog();
//        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
//            @Override
//            public void onSuccess(HttpResult<BannerInfoDto> result) {
//                //dissLoadDialog();
//                if (result != null) {
//                    if (result.getData() != null) {
//
//                        List<BannerItemDto> bannerList = result.getData().near_seller_banner;
//                        startBanner(bannerList);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                //dissLoadDialog();
//            }
//        }, "near_seller_banner");

    }

    private BannerItemDto ban;

    @Override
    public void onResume() {
        super.onResume();
        if(TextUtil.isNotEmpty(CanTingAppLication.city_id)){
//            ShareUtil.getInstance().save("city_id",CanTingAppLication.city_id);
        }if(TextUtil.isNotEmpty(CanTingAppLication.city_name)){
//            ShareUtil.getInstance().save("city_name",CanTingAppLication.city_name);
            tv_location.setText(CanTingAppLication.city_name);
        }

    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onStop() {

        super.onStop();
    }


    @Override
    public void onClick(View view) {

    }
    private int loadtype;
    @Override
    public <T> void toEntity(T entity, int type) {
        if(type == 2){
            SmgBaseBean3 baseBean3 = (SmgBaseBean3) entity;
            onDataLoaded(loadtype, baseBean3.data.size() == Constants.PAGE_SIZE, baseBean3.data);

        } else if( type == 6){
            ConfigDto result = (ConfigDto) entity;
            if (result.data != null) {
                List<String> strLists = result.data.search_distance;
                for (int i = 0; i < strLists.size(); i++) {
                    SecondClassItem secondClassItem = new SecondClassItem();
                    String jl;
                    try{
                        long l = Long.parseLong(strLists.get(i));
                        jl=(l/1000.0)+"km";
                    }catch(Exception e){
                        jl="1km";
                    }
                    secondClassItem.setName(jl);
                    secondList.add(secondClassItem);
                }
            }
        }else if( type == 8){
            SmgBaseBean5 result = (SmgBaseBean5) entity;
            gadList.clear();
            if (result != null) {

                if (result.data.onsale != null) {
                    GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                    goodsAttrDto.setKey("优惠和权益");
                    goodsAttrDto.data = result.data.onsale;
                    gadList.add(goodsAttrDto);
                }
                if (result.data.service != null) {
                    GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                    goodsAttrDto.setKey("服务");
                    goodsAttrDto.data = result.data.service;
                    gadList.add(goodsAttrDto);

                }
                if (result.getData().search_price != null && result.getData().search_price.size() > 0) {
                    List<BaseDto> lists = new ArrayList<>();
                    for (List<String> dist : result.getData().search_price) {
                        BaseDto baseDto = new BaseDto();
                        baseDto.name = dist.get(0);
                        baseDto.id = dist.get(1) + ":::" + dist.get(2);
                        lists.add(baseDto);
                    }
                    GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                    goodsAttrDto.setKey("价格");
                    goodsAttrDto.data = lists;
                    gadList.add(goodsAttrDto);
                }
                if (result.getData().search_distance != null && result.getData().search_distance.size() > 0) {
                    List<BaseDto> lists = new ArrayList<>();
                    for (String dist : result.getData().search_distance) {
                        BaseDto baseDto = new BaseDto();
                        baseDto.name = dist;
                        baseDto.id = dist;
                        lists.add(baseDto);
                    }
//                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
//                        goodsAttrDto.setKey("距离");
//                        goodsAttrDto.data = lists;
//                        gadList.add(goodsAttrDto);
                }
                goodsAttrAdapter.setNewData(gadList);
            }
        }else if( type == 9){
            SmgBaseBean4 result = (SmgBaseBean4) entity;
            if (result != null) {

                firstId = result.getData().get(0).id;
                for (Params par : result.getData()) {
                    StoreCategoryDto dto = new StoreCategoryDto();
                    dto.setId(Long.valueOf(par.id));
                    dto.title = par.title;
                    dtos.add(dto);
                    List<GoodsAttrDto> gadList = new ArrayList<>();
                    for (Params ms : par.children.data) {
                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                        goodsAttrDto.setKey(ms.title);
                        List<BaseDto> basList = new ArrayList<>();
                        for (Params mss : ms.children.data) {
                            BaseDto baseDto = new BaseDto();
                            baseDto.id = mss.id;
                            baseDto.name = mss.title;
                            basList.add(baseDto);
                        }
                        goodsAttrDto.data = basList;
                        gadList.add(goodsAttrDto);
                    }
                    maps.put(par.id, gadList);
                }
                mClassifyOneAdapter.setNewData(dtos);
                goodsAttrAdapters.setNewData(maps.get(firstId + ""));

            }
        } else if(type == 12){
            firstList.clear();
            SmgBaseBean6 result = (SmgBaseBean6) entity;
            if (result.data != null) {
                FirstClassItem firstClassItemNear = new FirstClassItem();
                firstClassItemNear.setName("附近");
                firstClassItemNear.setId(1963);
                firstClassItemNear.setSecondList(secondList);
                firstList.add(firstClassItemNear);
                for (int i = 0; i < result.data.size(); i++) {
                    FirstClassItem firstClassItem = new FirstClassItem();
                    firstClassItem.setId(Integer.valueOf(result.data.get(i).getId()));
                    firstClassItem.setName(result.data.get(i).getName());
                    firstClassItem.setSecondList(secondLists);
                    firstList.add(firstClassItem);
                }
            }
            initLevelsNearPopup();
        }else if(type == 18){
            String city_ids = SpUtil.getString(this,"city_id","");
            String city_name = SpUtil.getString(this,"city_name","");
            SmgBaseBean7 result = (SmgBaseBean7) entity;
            if(TextUtil.isNotEmpty(city_name)){
                tv_location.setText(city_name);
                tvLocation.setText(city_name);
                tvLocations.setText(city_name);
                location=city_name;
            }else {
                location = result.getData().getName();
                tv_location.setText(location);
                tvLocation.setText(location);
                tvLocations.setText(location);
            }
            if(TextUtil.isNotEmpty(city_ids)){
                getArea(city_ids);
                city_id=city_ids;
                sitId=city_ids;
            }else {
                city_id=result.getData().id;
                sitId = result.getData().getId();
                getArea(sitId);
            }

            getShopList(TYPE_PULL_REFRESH);

        }else if(type == 66){
            Home home = (Home) entity;
            banners = home.banner;
            bannerAdapter.setData(banners);
        }

    }
    private  List<Banner> banners;
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }




    private void getConfigs() {
        presenter.getConfigs();
        //showLoadDialog();

    }


    private List<StoreCategoryDto>          dtos = new ArrayList<>();
    private Map<String, List<GoodsAttrDto>> maps = new HashMap<>();
    private String                          firstId;

    public void getInducts() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("no_tree", "1");
        map.put("parent_id", "0");
        map.put("include", "children.children");
        presenter.getInducts(map);

    }

    public void getTagsList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("group_by_type", "1");
        map.put("type", "onsale,service");

        presenter.getAllTags(map);

    }

    private void getLocation() {
        HashMap<String, String> map = new HashMap<>();
        map.put("lat", LocationUtils.Lat + "");
        map.put("lng", LocationUtils.longt + "");
        presenter.getLocation(map);

    }


    private void getArea(String parendId) {

        //showLoadDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("parent_id", parendId);
        presenter.getArea(map);

    }

    private String industryId;
    private String onsaleId;
    private String serviceId;
    private String distanceId;
    private String per_consumption;
    private String area_id;
    private String shop_name;

    private void getShopList(int loadtype) {
        this.loadtype = loadtype;
        //showLoadDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("extra_include", "distance,industry,onsale,service,area");
        if (TextUtil.isNotEmpty(industryId)) {
            map.put("industry", industryId);
        }
        if (TextUtil.isNotEmpty(onsaleId)) {
            map.put("onsale", onsaleId);
        }
        if (TextUtil.isNotEmpty(serviceId)) {
            map.put("service", serviceId);
        }
        if (TextUtil.isNotEmpty(distance)) {
            map.put("filter[scopeDistanceIn]", distance);
        }
        if (TextUtil.isNotEmpty(per_consumption)) {
            map.put("per_consumption", per_consumption);
        }
        if (TextUtil.isNotEmpty(area_id)) {
            map.put("filter[area_id]", area_id);
        }
        if (TextUtil.isNotEmpty(shop_name)) {
            map.put("filter[shop_name]", shop_name);
        }
        if (TextUtil.isNotEmpty(city_id)) {
            map.put("city_id", city_id);
        }
        presenter.getShopList(map);

    }
    private List<RecommendListDto> dats  = new ArrayList<>();
    public void onDataLoaded(int loadType, final boolean haveNext, List<RecommendListDto> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            dats.clear();
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        } else {
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        }

        mEntityStoreAdapter.setNewData(dats);

        mEntityStoreAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        getShopList(TYPE_PULL_MORE);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }
     private LinearLayout   store_layout_back;
    private ClearEditText find;
    private  LinearLayout   store_ll_location;
    private  TextView       tv_location;
    private BannerView banner;
    private  RelativeLayout layout_all;
    private  RelativeLayout layout_all1;
    private  RelativeLayout layout_all2;
    private  TextView       tv_all;
    private  ImageView      iv_all;
    private  RelativeLayout layout_near;
    private  RelativeLayout layout_near1;
    private  RelativeLayout layout_alls;
    private  RelativeLayout layout_near2;
    private  RelativeLayout layout_sx;
    private  RelativeLayout layout_sxs;
    private  RelativeLayout layout_sx1;
    private  RelativeLayout layout_nears;
    private  RelativeLayout layout_sx2;

    private TextView        tv_near;
   private ImageView       iv_near;

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_entity_headview, null);
        banner = mHeaderView.findViewById(R.id.bannerView);
        store_layout_back = mHeaderView.findViewById(R.id.store_layout_back);
        find = mHeaderView.findViewById(R.id.find);
        store_ll_location = mHeaderView.findViewById(R.id.store_ll_location);
        tv_location = mHeaderView.findViewById(R.id.tv_location);
        layout_all = mHeaderView.findViewById(R.id.layout_all);
        tv_all = mHeaderView.findViewById(R.id.tv_all);
        iv_all = mHeaderView.findViewById(R.id.iv_all);
        layout_near = mHeaderView.findViewById(R.id.layout_near);
        layout_sx = mHeaderView.findViewById(R.id.layout_sx);
        tv_near = mHeaderView.findViewById(R.id.tv_near);
        iv_near = mHeaderView.findViewById(R.id.iv_near);
        mEntityStoreAdapter.addHeaderView(mHeaderView);
        bannerAdapter = new BannerAdapters(this);
        banner.setAdapter(bannerAdapter);
    }
    private BannerAdapters bannerAdapter;
    private LinearLayoutManager layoutManager;
    private final int                            TYPE_PULL_REFRESH = 888;
    private final int                            TYPE_PULL_MORE    = 889;
    private       int                            currpage          = 1;//第几页
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private void initAdapter() {

        mEntityStoreAdapter = new EntityStoreAdapter(storeLists, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(EntityStoreActivity.this);

        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        layoutManager = new LinearLayoutManager(this);
        //        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mEntityStoreAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                getShopList(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

        initHeaderView();
    }


    private TextView tvLocats;
    private TextView finds1;
    private TextView finds2;
    private TextView finds3;

    private void initLevelsNearPopup() {

        levelsPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.levels_popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);
        tvLocats = (TextView) view.findViewById(R.id.tv_location);
        finds1 = (TextView) view.findViewById(R.id.find);
        iv_backss = (ImageView) view.findViewById(R.id.iv_back);
        layout_sx1 = (RelativeLayout) view.findViewById(R.id.layout_sx);
        layout_nears = (RelativeLayout) view.findViewById(R.id.layout_near);
        layout_all2 = (RelativeLayout) view.findViewById(R.id.layout_all);
        tvLocats.setText(location);
        levelsPopupWindow.setContentView(view);
        tvLocats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        layout_nears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
            }
        });
        layout_sx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

            }
        });
        layout_all2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                if (levelsAllPopupWindow == null) {
                    return;
                }
                if (levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();

                }
                levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });

        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高
        levelsPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));
        levelsPopupWindow.setFocusable(false);
        levelsPopupWindow.setClippingEnabled(false);
        levelsPopupWindow.setHeight(height + 120);
        levelsPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                leftLV.setSelection(0);
                rightLV.setSelection(0);
            }
        });
        iv_backss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
            }
        });
        finds1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(this, firstList);
        leftLV.setAdapter(firstAdapter);
        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<SecondClassItem>();
        secondList.addAll(firstList.get(0).getSecondList());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(this, secondList);
        rightLV.setAdapter(secondAdapter);
        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList.get(position).getSecondList();
                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }
                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    levelsPopupWindow.dismiss();
                    iv_near.setImageResource(R.mipmap.icon_arrow_up);
                    tv_near.setTextColor(getResources().getColor(R.color.my_color_black));
                    int firstId = firstList.get(position).getId();
                    String selectedName = firstList.get(position).getName();

                    area_id=firstId+"";
                    distance="";
                    getShopList(TYPE_PULL_REFRESH);

//                    handleResult(firstId, "", selectedName);

                }

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                //根据左侧一级分类选中情况，更新背景色
                SecondClassAdapter adapter = (SecondClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                levelsPopupWindow.dismiss();
                int firstPosition = firstAdapter.getSelectedPosition();
                int firstId = firstList.get(firstPosition).getId();
                int secondId = firstList.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = secondList.get(position).getName();
                area_id="";
                tv_near.setText(selectedName);
                distance=selectedName+"";
                getShopList(TYPE_PULL_REFRESH);
            }
        });
    }

    //刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2, SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(int firstId, String secondId, String selectedName) {
        //String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        //mainTab1TV.setText(selectedName);
    }
    private String city_id;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GETCITY:
                    //CityDto city= (CityDto) data.getExtras().getSerializable("city");
                    //if(city!= null) {
                    //    String cityName = city.getCityName();
                    //    tv_location.setText(cityName.substring(0,cityName.length()-1));
                    //}
                    city_id = data.getExtras().getString("id");
                    if(TextUtil.isNotEmpty(city_id)){
                        getArea(city_id);
                    }else {
                        getArea(CanTingAppLication.city_id);

                    }

                    getShopList(TYPE_PULL_REFRESH);
                    break;
            }
        }
    }


}
