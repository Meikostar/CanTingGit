package com.zhongchuang.canting.activity.mall;

import android.widget.ListView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.SaleMangerAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.widget.NavigationBar;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class SaleMangerActivity extends BaseActivity1 {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.listview)
    ListView listview;

    private SaleMangerAdapter adapter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_sale_manger);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        adapter = new SaleMangerAdapter(this);
        listview.setAdapter(adapter);

    }


    @Override
    public void bindEvents() {


    }

    public static final int from = 4;

    @Override
    public void initData() {
//        if (type == TYPE_EDIT) {
//            navigationBar.setNaviTitle("编辑收货地址");
//        } else {
//            navigationBar.setNaviTitle("新增收货地址");
//        }
//        if (from == FROM_SELECT) {
//            navigationBar.setRightText("保存并使用");
//        } else {
//            navigationBar.setRightText("保存");
//        }

    }


}
