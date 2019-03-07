package com.zhongchuang.canting.fragment.mall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.EditorOrderActivity;
import com.zhongchuang.canting.activity.mall.OrderDetailActivity;
import com.zhongchuang.canting.adapter.OrderDelAdapter;
import com.zhongchuang.canting.adapter.OrderMangerAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.CancelParam;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Params;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/25.
 */

public class OrderMangerfagment extends BaseFragment implements BaseContract.View {


    @BindView(R.id.listview_all_city)
    ListView listView;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;

    private Unbinder bind;


    private OrderMangerAdapter adapter;

    public OrderMangerfagment() {
    }

    private int type = 1;

    public void setType(int type) {
        this.type = type;
    }

    private BasesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_manger, container, false);
        bind = ButterKnife.bind(this, view);
        adapter = new OrderMangerAdapter(getActivity());
        listView.setAdapter(adapter);

        loadingView.showPager(LoadingPager.STATE_LOADING);
        initView();
        return view;
    }
    private List<Oparam> dat = new ArrayList<>();

    private OrderParam order = new OrderParam();
    private void initView() {


//        showPress();
        adapter.setListener(new OrderMangerAdapter.selectItemListener() {
            @Override
            public void delete(OrderData data, int poistion) {
                if (poistion == -1) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("id", data.protList.get(0).order_id);
                    intent.putExtra("transaction_id", data.protList.get(0).transaction_id);
                    startActivity(intent);
                } else if (poistion == -2){
                    if (data.protList.get(0).order_type.equals("8")||data.protList.get(0).order_type.equals("7")) {
                        showPopwindow(data.protList.get(0).order_id);
                    }
                }else {
                    if (data.protList.get(0).order_type.equals("3")) {
                        showPopwindows(data.protList.get(0).order_id);

                    } else if (data.protList.get(0).order_type.equals("1")) {
                        if(poistion==-4){
                            dat.clear();
                            order.userInfoId = SpUtil.getUserInfoId(getActivity());

                            if(TextUtil.isNotEmpty(data.transaction_id)){
                                order.transaction_id = data.transaction_id + "";
                            }else {
                                order.transaction_id = data.protList.get(0).transaction_id + "";
                            }

                            for (OrderData datas : data.protList) {


                                Oparam oparam = new Oparam();
                                oparam.productSkuId = datas.product_sku_id;
                                oparam.number = datas.prod_number;
                                if (datas.pro_site.equals("1")||datas.pro_site.equals("3")) {
                                    oparam.integralPrice = "0";
                                } else {
                                    oparam.integralPrice = datas.integral_price;
                                }
                                dat.add(oparam);


                            }
                            order.productList=dat;
                            Intent intent = new Intent(getActivity(), EditorOrderActivity.class);
                            intent.putExtra("data", order);
                            intent.putExtra("type", data.proSite.equals("1")?1:2);
                            startActivity(intent);
                        }else {
                            showPopwind(data.protList.get(0).order_id);
                        }

                    } else if (data.protList.get(0).order_type.equals("2")) {
                        showPopwinds(data.protList);

                    }
                }


            }
        });

    }

    public void showPopwind(final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(getString(R.string.qdqxgdd));
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
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
                showPress(getString(R.string.xqz));
                presenter.receiptGoods( "",id);
                dialog.dismiss();
            }
        });
    }
    public void showPopwindows(final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(R.string.qdysdbblm);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
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
                showPress(getString(R.string.qrz));
                presenter.receiptGoods("7", id);
                dialog.dismiss();
            }
        });
    }
    private List<Params> par=new ArrayList<>();
    public void showPopwinds(final List<OrderData> protList) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        ListView listView = null;
        View views = View.inflate(getActivity(), R.layout.chooe_base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);
        listView = (ListView) views.findViewById(R.id.listview);
        delAdapter=new OrderDelAdapter(getActivity());
        delAdapter.setDatas(protList);

        listView.setAdapter(delAdapter);
//        title.setText("确定取消该订单？");
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
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
                showPress(getString(R.string.xqz) );
                if(protList.get(0).order_type.equals("1")){
                    presenter.receiptGoods( "",protList.get(0).order_id);
                }else {
                    par.clear();
                    List<OrderData> data1 = delAdapter.getData();
                    for(OrderData data:data1){
                        if(data.isChoose){
                            Params param = new Params();
                            param.id=data.id;
                            param.proSite=data.pro_site;
                            par.add(param);
                        }
                    }
                   presenter.cancelOrder(par);
                }

                dialog.dismiss();
            }
        });
    }
    private OrderDelAdapter delAdapter;
    public void showPopwindow(final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;

        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.chooe_base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(R.string.qdscgdd);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
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
                showPress(getString(R.string.shanchuzhong));
                presenter.deleteOrder( id);
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();

    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(presenter==null){
                presenter = new BasesPresenter(this);
            }

//            showPress("加载中...");
            presenter.favoriteList(1 + "", 100 + "", type + "");
            //相当于Fragment的onResume

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    @Override
    public <T> void toEntity(T entity, int types) {

        if (types == 5) {
            presenter.favoriteList(1 + "", 100 + "", type + "");
        } else {
            hidePress();
            List<OrderData> data = (List<OrderData>) entity;
            if (data != null && data.size() > 0) {
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(data);
            } else {
                loadingView.setContent(getString(R.string.zwsj));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        hidePress();
        loadingView.setContent(getString(R.string.zwsj));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
