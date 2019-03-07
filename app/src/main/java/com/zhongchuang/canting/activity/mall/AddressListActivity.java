package com.zhongchuang.canting.activity.mall;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.AddressAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.loadingView.BaseLoadingPager;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class AddressListActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private AddressAdapter adapter;
    private BasesPresenter presenter;
    private int state;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        loadingView.showPager(BaseLoadingPager.STATE_LOADING);
        presenter = new BasesPresenter(this);
        presenter.getUserAddress("0");
        state=getIntent().getIntExtra("state",0);
        adapter = new AddressAdapter(this);
        listview.setAdapter(adapter);

    }


    @Override
    public void bindEvents() {

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddressListActivity.this, NewAddressActivity.class),1);

            }
        });

        adapter.setOnCheckAllListener(new AddressAdapter.onCheckAllListener() {
            @Override
            public void checkAll(AddressBase isAll, int type) {
                if (type == 1) {//设为默认
                    showProgress(getString(R.string.szz));
                    presenter.alterDefaultAddress(isAll.address_id);
                } else if (type == 2) {//编辑
                    Intent intent = new Intent(AddressListActivity.this, NewAddressActivity.class);
                    intent.putExtra("data",isAll);
                    startActivityForResult(intent,1);
                }  else if (type == 3) {//编辑
                    showPopwindow(isAll.address_id,isAll.is_default);
                }else {//删除
                    if(state==2){
                        Intent intent=new Intent();
                        intent.putExtra("data",isAll);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                }
            }
        });
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


    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 2) {
            List<AddressBase> data = (List<AddressBase>) entity;
            if(data!=null&&data.size()>0){
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
                loadingView.setContent(getString(R.string.nhmtjshdz));
            }

            adapter.setData(data);
            if(state!=0){
                if(states==1){
                    states=0;
                    Intent intent=new Intent();
                    intent.putExtra("data",data.get(data.size()-1));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

        }else if(type==3){
            presenter.getUserAddress("0");
            showToasts(getString(R.string.sccg));
        }else if(type==4){
            presenter.getUserAddress("0");
        }
    }
    private View views=null;
    private TextView sure = null;
    private TextView cancel = null;
    private TextView title = null;
    private EditText reson = null;
    public void showPopwindow(final String id,final String isdefat) {

        views = View.inflate(this, R.layout.del_friend, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.tv_title);
        reson = (EditText) views.findViewById(R.id.edit_reson);
        title.setText(R.string.qdscgdz);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final EditText finalReson = reson;
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(getString(R.string.scz));
                presenter.deleteAdress(id,isdefat.equals("true")?"1":"0");
                dialog.dismiss();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(TextUtil.isNotEmpty(SpUtil.getUserInfoId(this))){
                states=1;
                presenter.getUserAddress("0");
            }
        }

    }
    private int states=0;
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
        dimessProgress();
    }


}
