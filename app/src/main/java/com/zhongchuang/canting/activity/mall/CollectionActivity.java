package com.zhongchuang.canting.activity.mall;

import android.widget.ListView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.CollectionAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Favor;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的收藏
 */
public class CollectionActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private CollectionAdapter adapter;

    private BasesPresenter presenter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        presenter.shoFavorite();
        loadingView.showPager(LoadingPager.STATE_LOADING);
        navigationBar.setNavigationBarListener(this);
        adapter = new CollectionAdapter(this, null, listview);
        listview.setAdapter(adapter);

    }

    @Override
    public void bindEvents() {
        adapter.setListener(new CollectionAdapter.selectItemListener() {
            @Override
            public void delete(String id, int type, int poistion) {
                showProgress(getString(R.string.qxzs));
              presenter.addShop(id,"0");
            }
        });

    }


    @Override
    public void initData() {

    }


    @Override
    public <T> void toEntity(T entity, int type) {

        if(type==22){
            presenter.shoFavorite();
        }else {
            dimessProgress();
            List<Favor> data= (List<Favor>) entity;
            if(data!=null&&data.size()>0){
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(data);
            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
                loadingView.setContent(getString(R.string.zwscdp));
            }
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        loadingView.showPager(LoadingPager.STATE_EMPTY);
        loadingView.setContent(getString(R.string.zwscdp));
        dimessProgress();
    }

}
