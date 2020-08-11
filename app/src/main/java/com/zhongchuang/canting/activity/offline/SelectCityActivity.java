package com.zhongchuang.canting.activity.offline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.been.SmgBaseBean1;
import com.zhongchuang.canting.been.SmgBaseBean2;
import com.zhongchuang.canting.utils.PinYinUtils;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.CYBChangeCityGridViewAdapter;
import com.zhongchuang.canting.adapter.ContactAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.base.BroadcastManager;
import com.zhongchuang.canting.been.AllCityDto;
import com.zhongchuang.canting.been.CityDto;
import com.zhongchuang.canting.been.HotCityDto;
import com.zhongchuang.canting.been.SecondLevelCityCto;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.InputUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.QGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * 选择城市页面
 */
public class SelectCityActivity extends BaseAllActivity implements  BaseContract.View{
    public static final String CURRENT_CITY = "current_city";
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.indexableLayout)
    IndexableLayout indexableLayout;
    private BasesPresenter presenter;
    private ContactAdapter mAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    private CYBChangeCityGridViewAdapter cybChangeCityGridViewAdapter;
    private List<AllCityDto> allCityList = new ArrayList<AllCityDto>();
    private List<CityDto> cList = new ArrayList<>();
    private List<HotCityDto> hList = new ArrayList<>();
    private String cName;
    private String fromStr;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_select_city);
        ButterKnife.bind(this);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
        presenter = new BasesPresenter(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromStr = bundle.getString("from");
        }
        initAdapter();
    }

    @Override
    public void bindEvents() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(tv_title_right, () -> {
            finish();
        });

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityDto>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityDto entity) {
                if (originalPosition >= 0) {
                    CanTingAppLication.city_id = entity.id;
                    CanTingAppLication.city_name = entity.getCityName();
                    String cityName = entity.getCityName();
                    if (fromStr.equals("entityStore")) {

                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);

                        finish();
                    } else if (fromStr.equals("entityStores")) {
                        Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
                        intent.putExtra("id", entity.id);
                        setResult(RESULT_OK, intent);
                        //                            finish();
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITYS, cityName);
                        finish();
                    } else {

                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);

                        Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
                        intent.putExtra("city", cityName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {

        getHotCityList();
        getAllCityList();
    }



    public void initAdapter() {
        mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        indexableLayout.setOverlayStyle_Center();
        // indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        // indexableLayout.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "☆",null, null));
        //构造函数里3个参数,分别对应 (IndexBar的字母索引, IndexTitle, 数据源), 不想显示哪个就传null, 数据源传null时,代表add一个普通的View
        //mMenuHeaderAdapter = new MenuHeaderAdapter("↑", null, initMenuDatas());
        //indexableLayout.addHeaderAdapter(mMenuHeaderAdapter);
        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("", null, bannerList);
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(SelectCityActivity.this).inflate(R.layout.item_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            // 数据源为null时, 该方法不用实现
            final VH vh = (VH) holder;
            //cList = new ArrayList<>();
            //for(int i = 0; i<city.length; i++){
            //    hList.add(city[i]);
            //}
            cybChangeCityGridViewAdapter = new CYBChangeCityGridViewAdapter(SelectCityActivity.this, hList);
            vh.head_home_change_city_gridview.setAdapter(cybChangeCityGridViewAdapter);
            vh.item_header_city_dw.setText(cName);
            vh.head_home_change_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CanTingAppLication.city_id = hList.get(position).getId();
                    CanTingAppLication.city_name = hList.get(position).getName();
                    if (TextUtil.isNotEmpty(fromStr)) {
                        if (fromStr.equals("entityStore")) {
                            //                            Intent intent = new Intent(SelectCityActivity.this, EntityProductActivity.class);
                            //                            intent.putExtra("city", hList.get(position).getName());
                            //                            setResult(RESULT_OK, intent);
                            //                            finish();
                            BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, hList.get(position).getName());
                            finish();
                        } else if (fromStr.equals("entityStores")) {
                            Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
                            intent.putExtra("id", hList.get(position).getId());
                            setResult(RESULT_OK, intent);
                            //                            finish();
                            BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITYS, hList.get(position).getName());
                            finish();
                        } else {
                            String cityName = hList.get(position).getName();
                            BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);
                            finish();
                        }
                    }

                }
            });
            vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromStr.equals("entityStore")) {
                        //                        Intent intent = new Intent(SelectCityActivity.this, EntityProductActivity.class);
                        //                        intent.putExtra("city", vh.item_header_city_dw.getText().toString());
                        //                        setResult(RESULT_OK, intent);
                        //                        finish();
//                        CanTingAppLication.city_id = CanTingAppLication.city_ids;
                        CanTingAppLication.city_name = vh.item_header_city_dw.getText().toString();
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, vh.item_header_city_dw.getText().toString());
                        finish();
                    } else if (fromStr.equals("entityStores")) {
                        //                            Intent intent = new Intent(SelectCityActivity.this, EntityProductActivity.class);
                        //                            intent.putExtra("city", hList.get(position).getName());
                        //                            setResult(RESULT_OK, intent);
                        //                            finish();
//                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITYS, vh.item_header_city_dw.getText().toString());
                        finish();
                    } else {
                        String cityName = vh.item_header_city_dw.getText().toString();
//                        BaseApplication.city_id = BaseApplication.city_ids;
                        CanTingAppLication.city_name = cityName;
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);
                        finish();
                    }
                }
            });

            vh.tv_re_loction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    vh.item_header_city_dw.setText(cName);
                    CanTingAppLication.city_name = cName;
                    if (fromStr.equals("entityStore")) {
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cName);
                        finish();
                        //                        Intent intent = new Intent(SelectCityActivity.this, EntityProductActivity.class);
                        //                        intent.putExtra("city", cName);
                        //                        setResult(RESULT_OK, intent);
                        //                        finish();
                    } else if (fromStr.equals("entityStores")) {
                        //                            Intent intent = new Intent(SelectCityActivity.this, EntityProductActivity.class);
                        //                            intent.putExtra("city", hList.get(position).getName());
                        //                            setResult(RESULT_OK, intent);
                        //                            finish();
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITYS, cName);
                        finish();
                    } else {
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cName);
                        finish();
                    }
                }
            });

            vh.et_search_str.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //防止两次发送请求
                    if (actionId == EditorInfo.IME_ACTION_SEND ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        switch (event.getAction()) {
                            case KeyEvent.ACTION_UP:
                                String seachStr = vh.et_search_str.getText().toString().trim();
                                filterData(seachStr);
                                InputUtil.HideKeyboard(vh.et_search_str);
                                return true;
                            default:
                                return true;
                        }
                    }
                    return false;
                }
            });

            vh.et_search_str.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    if (TextUtils.isEmpty(s.toString())) {
                        vh.tv_search_contacts_view.setVisibility(View.VISIBLE);
                        mAdapter.setDatas(cList);
                    } else {
                        vh.tv_search_contacts_view.setVisibility(View.GONE);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable edit) {
                }
            });
        }

        private class VH extends RecyclerView.ViewHolder {
            GridView head_home_change_city_gridview;
            TextView item_header_city_dw;
            TextView tv_re_loction;
            TextView tv_search_contacts_view;
            ClearEditText et_search_str;

            public VH(View itemView) {
                super(itemView);
                head_home_change_city_gridview = (QGridView) itemView.findViewById(R.id.item_header_city_gridview);
                item_header_city_dw = itemView.findViewById(R.id.item_header_city_dw);
                tv_re_loction = itemView.findViewById(R.id.tv_re_loction);
                tv_search_contacts_view = itemView.findViewById(R.id.tv_search_contacts_view);
                et_search_str = itemView.findViewById(R.id.et_search_str);
            }
        }
    }

    private List<CityDto> initDatas() {
        for (int i = 0; i < allCityList.size(); i++) {
            List<SecondLevelCityCto> sList = allCityList.get(i).getChildren();
            for (int j = 0; j < sList.size(); j++) {
                CityDto cd = new CityDto();
                cd.setCityName(sList.get(j).getName());
                cd.setCityCode(sList.get(j).getCode());
                cd.id = (sList.get(j).getId());
                cList.add(cd);
            }
        }
        return cList;
    }

    private void getHotCityList() {

        presenter.getHotCityList();

    }

    private void getAllCityList() {
        presenter.getAllCityList();
        //showLoadDialog();

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type == 1){
            SmgBaseBean1 bean1 = (SmgBaseBean1) entity;
            List<AllCityDto> aList = bean1.data;
            if (aList != null) {
                allCityList.addAll(aList);
                mAdapter.setDatas(initDatas());

            }
        } else {
            SmgBaseBean2 bean2 = (SmgBaseBean2) entity;
            List<HotCityDto> hotList = bean2.data;
            if (hotList != null) {
                hList.addAll(hotList);
                mBannerHeaderAdapter.notifyDataSetChanged();
            }
        }

    }



    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CityDto> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = cList;
        } else {
            filterDateList.clear();
            if (cList != null && !cList.isEmpty()) {
                for (CityDto item : cList) {
                    String name = item.getCityName() == null ? "*" : item.getCityName();
                    if (name.indexOf(filterStr.toString()) != -1 ||
                            PinYinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                            //不区分大小写
                            || PinYinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                            || PinYinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                            || PinYinUtils.getPingYin(name).toLowerCase().contains(filterStr.toString())
                            || PinYinUtils.getPingYin(name).toUpperCase().contains(filterStr.toString())
                    ) {
                        filterDateList.add(item);
                    }
                }
            }
        }
        mAdapter.setDatas(filterDateList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
