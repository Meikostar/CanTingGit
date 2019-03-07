package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.adapter.LiveCareAdapter;
import com.zhongchuang.canting.adapter.RecordGiftAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CareLiveActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.zhuye_geren)
    ImageView zhuyeGeren;
    @BindView(R.id.serch_edit)
    TextView serchEdit;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private Intent perIntent;
    private Unbinder bind;

    private BasesPresenter presenter;

    private View view;



  private int state;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_care);
        ButterKnife.bind(this);
        state=getIntent().getIntExtra("state",0);

        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        presenter.anchorsList();

        adapter = new LiveCareAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {
        adapter.setListener(new LiveCareAdapter.selectItemListener() {
            @Override
            public void delete(Care data, int poistion) {
                if(poistion==1){
                    Intent intentc = new Intent(CareLiveActivity.this, ChatActivity.class);
                    intentc.putExtra("userId", data.anchors_id);
                    startActivity(intentc);
                }else if(poistion==2){
                    if(data.is_enabled.equals("1")){
//                        Intent intent = new Intent(CareLiveActivity.this, DemoGuest.class);
//                        intent.putExtra("room",data.room_info_id);
//                        intent.putExtra("id",data.room_info_id);
//                        startActivity(intent);
                    }else {
                        showToasts(getString(R.string.zbyxb));
                    }

                }else if(poistion==3){
                    showProgress(getString(R.string.xqz));
                    presenter.focusTV(data.room_info_id,data.anchors_id,"2");
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    private LiveCareAdapter adapter;






    @Override
    public void onResume() {
        super.onResume();

//        mainBannertwo.startTurning(4000);
//        mainBannerThree.startTurning(4000);
    }

    @Override
    public void onStop() {
        super.onStop();
        dimessProgress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }






    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==9){
            dimessProgress();
            List<Care> data= (List<Care>) entity;
            if(data!=null&&data.size()>0){
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(data);
            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
                loadingView.setContent(getString(R.string.zwsj));
            }
        }else {
            presenter.anchorsList();
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        loadingView.showPager(LoadingPager.STATE_EMPTY);
        loadingView.setContent(getString(R.string.zwsj));
        dimessProgress();
    }
}

