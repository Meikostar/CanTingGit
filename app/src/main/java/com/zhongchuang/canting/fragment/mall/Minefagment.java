package com.zhongchuang.canting.fragment.mall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.BalanceActivity;
import com.zhongchuang.canting.activity.RechargeActivity;
import com.zhongchuang.canting.activity.mall.AddressListActivity;
import com.zhongchuang.canting.activity.mall.CollectionActivity;
import com.zhongchuang.canting.activity.mall.OrderMangerActivity;
import com.zhongchuang.canting.activity.mall.RechargeDbActivity;
import com.zhongchuang.canting.activity.mall.RechargeJfActivity;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.City;
import com.zhongchuang.canting.been.OrderType;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.Custom_ProfileOrderClickBtn;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/25.
 */

public class Minefagment extends BaseFragment implements BaseContract.View {

    Unbinder unbinder;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_user)
    LinearLayout rlUser;
    @BindView(R.id.img_line_bottom)
    View imgLineBottom;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.btn_wait_confirm)
    Custom_ProfileOrderClickBtn btnWaitConfirm;
    @BindView(R.id.btn_wait_receive)
    Custom_ProfileOrderClickBtn btnWaitReceive;
    @BindView(R.id.btn_wait_delivery)
    Custom_ProfileOrderClickBtn btnWaitDelivery;
    @BindView(R.id.btn_wait_pay)
    Custom_ProfileOrderClickBtn btnWaitPay;
    @BindView(R.id.rl_jf)
    RelativeLayout rlJf;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @BindView(R.id.rl_save)
    RelativeLayout rlSave;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.tv_cout3)
    TextView tvCout3;
    @BindView(R.id.tv_cout4)
    TextView tvCout4;
    @BindView(R.id.ll_jf)
    LinearLayout llJf;

    private BasesPresenter presenter;

    public Minefagment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_mine_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {

        presenter = new BasesPresenter(this);
        if (TextUtil.isNotEmpty(SpUtil.getUserInfoId(getActivity()))) {
            presenter.userInfo();
        }
        rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderMangerActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);

            }
        });
        rlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectionActivity.class));
            }
        });
        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressListActivity.class));
            }
        });
        rlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RechargeDbActivity.class));
            }
        });
        rlJf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RechargeActivity.class));
            }
        });
        llJf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                intent.putExtra("data",integral);
                startActivity(intent);

            }
        });
        btnWaitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderMangerActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        btnWaitDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderMangerActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });

        btnWaitReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderMangerActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        btnWaitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderMangerActivity.class);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        });
//        showPress();


    }


    @Override
    public void onResume() {
        super.onResume();
        if (TextUtil.isNotEmpty(SpUtil.getUserInfoId(getActivity()))) {
            presenter.userInfo();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(getActivity()==null){
            return;
        }
        OrderType orderType = (OrderType) entity;
        if(btnWaitConfirm==null&&orderType.order_type==null){
            return;
        }
        for (City dadta : orderType.order_type) {

            if (dadta.order_type.equals("1")) {
                btnWaitConfirm.setUnReadNum(Integer.valueOf(dadta.total));
            } else if (dadta.order_type.equals("3")) {
                btnWaitDelivery.setUnReadNum(Integer.valueOf(dadta.total));
            } else if (dadta.order_type.equals("7")) {
                btnWaitPay.setUnReadNum(Integer.valueOf(dadta.total));
            } else if (dadta.order_type.equals("2")) {

                btnWaitReceive.setUnReadNum(Integer.valueOf(dadta.total));
            }
        }
        integral=orderType.user_integral;
        if(getActivity()!=null){
            tvCout3.setText(orderType.user_integral);
            tvCout4.setText(orderType.user_token_money);
        }


    }
   private String integral;
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
