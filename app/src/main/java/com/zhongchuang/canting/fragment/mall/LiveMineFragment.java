package com.zhongchuang.canting.fragment.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.PersonMessageActivity;
import com.zhongchuang.canting.activity.live.CareLiveActivity;
import com.zhongchuang.canting.activity.live.GiftHandActivity;
import com.zhongchuang.canting.activity.live.LiveHandActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.ui.GroupsActivity;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleImageView;
import com.zhongchuang.canting.widget.CircleTransform;
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
 * Created by Administrator on 2017/12/4.
 */

public class LiveMineFragment extends LazyFragment implements BaseContract.View {


    @BindView(R.id.person_pic)
    CircleImageView personPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.rl_care)
    RelativeLayout rlCare;
    @BindView(R.id.rl_hand)
    RelativeLayout rlHand;
    @BindView(R.id.tv_application)
    TextView tvApplication;
    private BasesPresenter presenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.live_mine, container, false);
        ButterKnife.bind(this, viewRoot);
        presenter=new BasesPresenter(this);
        presenter.hostInfo();
        initView();
        setLoginMessage();
        return viewRoot;
    }

    private void setLoginMessage() {
        String phone = SpUtil.getString(getActivity(), "mobileNumber", "");
        String avar = SpUtil.getString(getActivity(), "ava", "");

        Glide.with(getActivity()).load(StringUtil.changeUrl(avar)).asBitmap().transform(new CircleTransform(getActivity())).placeholder(R.drawable.editor_ava).into(personPic);
        tvPhone.setText(phone);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

//        fragmentArgs = getArguments();
//
//        shopid = fragmentArgs.getString("id");


        super.onActivityCreated(savedInstanceState);
    }

    //    private ProfileInfoHelper mProfileHelper;
    private Subscription mSubscription;

    private void initView() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.SIGN) {
                    setLoginMessage();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        rlCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CareLiveActivity.class));
            }
        });
        rlHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LiveHandActivity.class));
            }
        });
        personPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPersonDetailActivity.class);
                intent.putExtra("id", SpUtil.getUserInfoId(getActivity()) + "");
                startActivity(intent);
                startActivity(new Intent(getActivity(), PersonMessageActivity.class));
            }
        });

        tvApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        showPopwindows(getString(R.string.nhwktzb),getString(R.string.xktlxkf),"255601345");

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        setLoginMessage();
        if (null != presenter) {
            presenter.hostInfo();

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("second done destroy");



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void lazyView() {

    }

    public  void initData(){
        Glide.with(getActivity()).load(StringUtil.changeUrl(SpUtil.getAvar(getActivity()))).asBitmap().placeholder(R.drawable.editor_ava).into(personPic);
        if(TextUtil.isNotEmpty(data.nickname)){
            tvName.setText(getString(R.string.nc) +data.nickname);
        } if(TextUtil.isNotEmpty(data.mobile_number)){
            tvPhone.setText(getString(R.string.dianhua)+data.mobile_number);
        } if(TextUtil.isNotEmpty(data.user_integral)){
            tvMoney.setText(data.user_integral+getString(R.string.jf));
        }
    }
    private Host data;
    private void upRoomInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(getActivity()));


        HXRequestService api = retrofit.create(HXRequestService.class);


        Call<Host> baseResponseCall = api.hostInfo(map);
        baseResponseCall.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                data=response.body().data;
                initData();
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {

            }
        });
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        data= (Host) entity;
        if(data!=null){
            initData();
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    public void showPopwindows(final String var1,final String var2,final String var3) {
        TextView content = null;
        TextView desc = null;
        ImageView iv_close = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.live_popow_view, null);
        title = views.findViewById(R.id.txt_title);
        content = views.findViewById(R.id.txt_content);
        desc = views.findViewById(R.id.txt_desc);
        iv_close = views.findViewById(R.id.iv_close);

        title.setText(var1);
        content.setText(var2);
        desc.setText(var3);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}
