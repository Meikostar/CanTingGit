package com.zhongchuang.canting.activity.mall;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.GridView;
import android.widget.ListView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ShopListSearchActivitys;
import com.zhongchuang.canting.adapter.GirdTypedapter;
import com.zhongchuang.canting.adapter.TypeAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Catage;
import com.zhongchuang.canting.been.Type;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.GoodsSearchViews;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class ShopTypeSearchActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.gds)
    GoodsSearchViews gds;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.grid_view)
    GridView gridView;
    private TypeAdapter adapter;
    private GirdTypedapter adapters;
    private List<Fragment> mFragments;
    private int type = 1;
    private List<Type> data = new ArrayList<>();
    private BasesPresenter presenter;
    private String stort_id;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_type);
        ButterKnife.bind(this);
        showProgress(getString(R.string.jzz));
        type = getIntent().getIntExtra("type", 1);
        stort_id = getIntent().getStringExtra("id");
        presenter = new BasesPresenter(this);
        presenter.getAllCateList();
        adapter = new TypeAdapter(this);
        adapters = new GirdTypedapter(this);
        listview.setAdapter(adapter);
        gridView.setAdapter(adapters);


    }

    @Override
    public void bindEvents() {
        gds.registerListener(new GoodsSearchViews.OnSearchBoxClickListener() {
            @Override
            public void onClean() {

            }

            @Override
            public void onCancle() {

            }

            @Override
            public void finishs(int types) {
                if (types == 1) {
                    finish();
                } else {
                    Intent intent = new Intent(ShopTypeSearchActivity.this, SearchGoodActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }

            }

            @Override
            public void onKeyWordsChange(String keyWords) {

            }
        });
        adapters.setListener(new GirdTypedapter.ItemClickListener() {
            @Override
            public void itemClick(String cont) {
                Intent intent = new Intent(ShopTypeSearchActivity.this, ShopListSearchActivitys.class);
                intent.putExtra("state", type);
                intent.putExtra("data", cont);
                startActivity(intent);
            }
        });
        adapter.setListener(new TypeAdapter.ItemClickListener() {
            @Override
            public void itemclick(int poistion, String id) {
                if (TextUtil.isNotEmpty(id)) {
                    presenter.getSecondList(id);
                } else {
                    adapters.setData(dat);

                }

            }
        });

    }


    @Override
    public void initData() {


    }

    private int cout = 0;
    private List<Catage> dat;

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        Catage catage = (Catage) entity;
        if (type == 2) {
            cout = 0;
            if (catage.firstList != null && catage.firstList.size() != 0) {
                data.clear();
                Type tpes = new Type();
                tpes.cont = "全部";
                data.add(tpes);
                for (Catage cat : catage.firstList) {

                    Type tpe = new Type();
                    if (TextUtil.isNotEmpty(stort_id)) {

                        if (stort_id.equals(cat.id)) {
                            tpe.cont = cat.category_name;
                            tpe.isChoose = true;
                            tpe.id = cat.id;
                            presenter.getSecondList(cat.id);
                            data.add(tpe);
                        } else {
                            if (!cat.category_name.equals(getString(R.string.gengduo))) {
                                tpe.cont = cat.category_name;
                                tpe.isChoose = false;
                                tpe.id = cat.id;
                                data.add(tpe);
                            }
                        }


                    } else {

                        if (!cat.category_name.equals(getString(R.string.gengduo))) {
                            tpe.cont = cat.category_name;
                            tpe.isChoose = false;
                            tpe.id = cat.id;
                            data.add(tpe);
                        }

                    }


                    cout++;
                }
                adapter.setData(data);
            }
            dat = catage.secondList;
            adapters.setData(catage.secondList);
        } else {
            adapters.setData(catage.secondList);
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
    }
}
