package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.CityListAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity;
import com.zhongchuang.canting.been.Contury;
import com.zhongchuang.canting.been.PREFIX;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.SideLetterBars;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CityPickerActivity extends BaseActivity implements View.OnClickListener, NavigationBar.NavigationBarListener {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private NavigationBar navigationBar;
    //  private ListView mResultListView;
    private SideLetterBars mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;

    private SuperRecyclerView mSuperRecyclerView;
    private CityListAdapter mCityAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private int type;//用于判读那个界面0默认是国家选择
    private int status;//用于判读那个界面0默认是国家选择
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private final int TYPE_REMOVE = 3;

   private Contury contury;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        type = getIntent().getIntExtra("type", 0);
        contury=CanTingAppLication.data;
        status = 0;

        initView();

        initDatass();

        navigationBar.setNavigationBarListener(this);

    }


    public void initDatass() {


        List<PREFIX> list = CanTingAppLication.data.data;
        if (list != null && list.size() > 0) {
            mCityAdapter = new CityListAdapter(this, list, status);
            mListView.setAdapter(mCityAdapter);
            mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                @Override
                public void onCityClick(String name) {
                    back(name);
                }

                @Override
                public void onLocateClick() {

                }
            });
        }
        for(PREFIX data:contury.data){
            map.put(data.countryName,data.phoneCode+"#"+data.countryCode);
        }
    }
   private List<PREFIX> datas=new ArrayList<>();
    private Map<String,String> map= new HashMap();
    private void initView() {

        mListView = (ListView) findViewById(R.id.listview_all_city);
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBars) findViewById(R.id.side_letter_bars);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBars.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        if (type == 0) {
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String keyword = s.toString();
                    if(TextUtil.isNotEmpty(keyword)){
                        datas.clear();
                        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry<String, String> entry = entries.next();
                            if(entry.getKey().contains(s.toString())){
                                PREFIX prefix = new PREFIX();
                                String[] split = entry.getValue().split("#");
                                prefix.countryCode=split[1
                                        ];
                                prefix.phoneCode=split[0];
                                prefix.countryName=entry.getKey();
                                datas.add(prefix);
                            }
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        }
                        mCityAdapter=null;
                        if(datas.size()>0){
                            mCityAdapter = new CityListAdapter(CityPickerActivity.this,datas, status);
                            mListView.setAdapter(mCityAdapter);
                            mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                                @Override
                                public void onCityClick(String name) {
                                    back(name);
                                }

                                @Override
                                public void onLocateClick() {

                                }
                            });
                        }else {

                                mCityAdapter = new CityListAdapter(CityPickerActivity.this,contury.data, status);
                                mListView.setAdapter(mCityAdapter);
                                mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                                    @Override
                                    public void onCityClick(String name) {
                                        back(name);
                                    }

                                    @Override
                                    public void onLocateClick() {

                                    }
                                });

                        }
                    }else {
                        mCityAdapter=null;
                        mCityAdapter = new CityListAdapter(CityPickerActivity.this,contury.data, status);
                        mListView.setAdapter(mCityAdapter);
                        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                            @Override
                            public void onCityClick(String name) {
                                back(name);
                            }

                            @Override
                            public void onLocateClick() {

                            }
                        });
                    }
                }
            });
        } else {

        }


//        mResultListView = (ListView) findViewById(R.id.listview_search_result);
//        mResultListView.setAdapter(mResultAdapter);
//        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                back("");
//            }
//        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);

        clearBtn.setOnClickListener(this);

    }


    private void back(String city) {
        // ToastUtils.showToast(this, "点击的城市：" + city);

        CanTingAppLication.code=city;
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                //  mResultListView.setVisibility(View.GONE);
                break;

        }
    }

//    @Override
//    public void getConturySuccess(List<PREFIX> prefices) {
////        if(prefices.size()!=0){
////            ShareDataManager.getInstance().Save(CityPickerActivity.this,ShareDataManager.PERFIX_DATA, new Gson().toJson(prefices));
////        }
//        if (prefices != null && prefices.size() > 0) {
//            emptyView.setVisibility(View.GONE);
//            mCityAdapter = new CityListAdapter(this, prefices, status);
//            mListView.setAdapter(mCityAdapter);
//            mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
//                @Override
//                public void onCityClick(String name) {
//                    back(name);
//                }
//
//                @Override
//                public void onLocateClick() {
//
//                }
//            });
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//        }
//
//
//    }


    @Override
    public void navigationLeft() {
        finish();
    }

    @Override
    public void navigationRight() {

    }

    @Override
    public void navigationimg() {

    }


}
