package com.zhongchuang.canting.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.activity.CaptureActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;

import com.zhongchuang.canting.easeui.ui.BaseActivity;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.fragment.TongXunLuFragment;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StatusBarUtils;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/11/28.
 */

public class TongXunLuActivity extends BaseActivity {
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    private FragmentTransaction mTransaction;
    private TongXunLuFragment tongXunLuFragment;
   private Subscription mSubscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor( this,getResources().getColor(R.color.wordColor));
        setContentView(R.layout.activity_tongxunlu);
        ButterKnife.bind(this);
        tongXunLuFragment = new TongXunLuFragment();
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.fragment_container, tongXunLuFragment);
        mTransaction.commit();
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.OPEN){
                    PermissionGen.with(TongXunLuActivity.this)
                            .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                            .permissions(Manifest.permission.CAMERA)
                            .request();

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
    }
    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        Intent intent = new Intent(this, CaptureActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String result = data.getStringExtra("result");
            String[] userids = result.split(",");
            showPopwindow(userids[0],userids[1]);
        }
    }
    public void showPopwindow(final String name,final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(getString(R.string.add)+name+getString(R.string.why));
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendRequest(name, id);
                dialog.dismiss();
            }
        });
    }
    private void addFriendRequest(final String nickName, String hxNameId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getString(this,"userInfoId",""));
        map.put("token", SpUtil.getString(this,"token",""));
        map.put("chatUserId", hxNameId);

        Call<BaseResponse> call = api.addFriend(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse bs = response.body();

                    Toast.makeText(TongXunLuActivity.this, getString(R.string.hyqqfscg)+ nickName + getString(R.string.hy), Toast.LENGTH_SHORT).show();

               finish();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LogUtil.d(t.toString());
                Toast.makeText(TongXunLuActivity.this,  getString(R.string.tjhysb) , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }
    }
}
