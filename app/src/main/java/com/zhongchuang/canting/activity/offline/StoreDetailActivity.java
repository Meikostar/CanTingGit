package com.zhongchuang.canting.activity.offline;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.RecommendListDto;
import com.zhongchuang.canting.been.SmgBaseBean3;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreDetailActivity extends BaseActivity1 implements  BaseContract.View{


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.rl_gr)
    RelativeLayout rlGr;
    @BindView(R.id.rl_qy)
    RelativeLayout rlQy;
    @BindView(R.id.rl_xx)
    RelativeLayout rl_xx;
    private BasesPresenter presenter;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_sq);
        ButterKnife.bind(this);
        mTitleText.setText("店铺申请");

    }

    @Override
    public void bindEvents() {
        rlGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(PersonRequireActivity.class);
                finish();
            }
        });


        rlQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(EnterpireRequireActivity.class);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_title_back

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        if(presenter == null){
            presenter = new BasesPresenter(this);
        }

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("creat_phone", SpUtil.getMobileNumber(this));
        presenter.getShopList(map);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        SmgBaseBean3 baseBean3 = (SmgBaseBean3) entity;
        if(baseBean3!=null && baseBean3.data!=null && baseBean3.data.list!=null&&baseBean3.data.list.size()>0){
            if(baseBean3.data.list.size()==1){
                if(baseBean3.data.list.get(0).audit_status ==1){
                    if(baseBean3.data.list.get(0).account_source==2){
                        showToasts("审核通过");
                        rl_xx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToasts("审核通过");
                            }
                        });
                    }else {
                        rl_xx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                gotoActivity(EnterpireOfflineRequireActivity.class);
                                finish();
                            }
                        });
                    }

                }else    if(baseBean3.data.list.get(0).audit_status ==2){
                    showToasts("审核拒绝");
                    rl_xx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoActivity(EnterpireOfflineRequireActivity.class);
                            finish();
                        }
                    });
                }else {
                    showToasts("审核中");
                    rl_xx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToasts("审核中");
                        }
                    });

                }
            }else {
                int state=0;
             for(RecommendListDto dto :baseBean3.data.list){
                 if(dto.account_source==2){
                     state=1;
                     if(dto.audit_status ==1){
                         showToasts("审核通过");
                         rl_xx.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 showToasts("审核通过");
                             }
                         });
                     }else   if(dto.audit_status ==2){
                         showToasts("审核拒绝");
                         rl_xx.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 gotoActivity(EnterpireOfflineRequireActivity.class);
                                 finish();
                             }
                         });
                     }else  {
                         showToasts("审核中");
                         rl_xx.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 showToasts("审核中");
                             }
                         });
                     }
                 }
             }
             if(state==0){
                 rl_xx.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         gotoActivity(EnterpireOfflineRequireActivity.class);
                         finish();
                     }
                 });
             }
            }

        }else {
            rl_xx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gotoActivity(EnterpireOfflineRequireActivity.class);
                    finish();
                }
            });
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
