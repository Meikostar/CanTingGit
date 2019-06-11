package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.WebViewActivity;
import com.zhongchuang.canting.adapter.Menberdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Version;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 会员管理
 */
public class MenberMangerActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.grid_content)
    NoScrollGridView girdView;
    private BasesPresenter presenter;
    private Menberdapter adapter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_menber);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        adapter = new Menberdapter(this);
        girdView.setAdapter(adapter);
        presenter.getVersionAndUrl();
    }

    @Override
    public void bindEvents() {


        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


              Intent  intent = new Intent(MenberMangerActivity.this, WebViewActivity.class);

                intent.putExtra(WebViewActivity.WEBTITLE,list.get(position).name);
                intent.putExtra(WebViewActivity.WEBURL, list.get(position).description);

                startActivity(intent);


            }
        });
    }

    @Override
    public void initData() {

    }

    private List<Version> datas = new ArrayList<>();
   private int cont;
    public void setData(int cout) {


    }
   private List<Version> list;
    @Override
    public <T> void toEntity(T entity, int type) {
        list= (List<Version>) entity;
        list.remove(0);
        adapter.setData(list);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
    }


}
