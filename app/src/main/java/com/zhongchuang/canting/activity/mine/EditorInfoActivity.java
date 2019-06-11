package com.zhongchuang.canting.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseLoginActivity;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/14.
 */

public class EditorInfoActivity extends BaseLoginActivity implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.et_content1)
    ClearEditText etContent1;
    @BindView(R.id.et_content2)
    ClearEditText etContent2;
    @BindView(R.id.et_content3)
    ClearEditText etContent3;
    @BindView(R.id.tv_save)
    TextView tvSave;
    private String id;

    private int type = 0;
    private BasesPresenter presenter;
    private FriendInfo info;
    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editor_infos);


        ButterKnife.bind(this);
        info= (FriendInfo) getIntent().getSerializableExtra("info");
        id = getIntent().getStringExtra("id");
        presenter = new BasesPresenter(this);

        initView();
        setEvents();
    }

    @Override
    protected void _onDestroy() {

    }

    private void initView() {
         if(info==null){
             return;
         }
        if(TextUtil.isNotEmpty(info.remark_message)){
            etContent3.setText(info.remark_message);
        }
        if(TextUtil.isNotEmpty(info.remark_phone)){
            String[] split = info.remark_phone.split(",");
            if(split.length==2){
                etContent1.setText(split[0]);
                etContent2.setText(split[1]);
            }else if(split.length==1){
                etContent1.setText(split[0]);
            }

        } if(TextUtil.isNotEmpty(info.remark_name)){
            etContent.setText(info.remark_name);
        }


    }

    private String content="";
    private String content1="";
    private String content2="";
    private String content3="";
    private void setEvents() {
     tvSave.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             content=etContent.getText().toString();
             content2=etContent2.getText().toString();
             content1=etContent1.getText().toString();
             content1=(TextUtil.isNotEmpty(content1)?(TextUtil.isNotEmpty(content2)?content1+","+content2:content1):""+(TextUtil.isNotEmpty(content2)?content2:""));
             content3=etContent3.getText().toString();
             presenter.addRemark(id,content,content1,content3);
         }
     });
    }


    @Override
    public <T> void toEntity(T entity, int type) {
        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FRIEND_RESH,content));
        ToastUtils.showNormalToast(getString(R.string.bccg));
       finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }



}
