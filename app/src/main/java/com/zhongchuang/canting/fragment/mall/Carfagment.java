package com.zhongchuang.canting.fragment.mall;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.EditorOrderActivity;
import com.zhongchuang.canting.activity.mall.ShopMallDetailActivity;
import com.zhongchuang.canting.adapter.ShopCarAllAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.MCheckBox;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/10/25.
 */

public class Carfagment extends BaseFragment implements BaseContract.View {


    @BindView(R.id.listview_all_city)
    ListView listView;
    @BindView(R.id.iv_choose)
    MCheckBox ivChoose;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;

    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private Unbinder bind;

    private BasesPresenter presenter;
    private ShopCarAllAdapter adapter;

    public Carfagment() {
    }

    private int status;

    @SuppressLint("ValidFragment")
    public Carfagment(int type) {
        status = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.car_fragment, container, false);
        bind = ButterKnife.bind(this, view);
        presenter = new BasesPresenter(this);
        adapter = new ShopCarAllAdapter(getActivity());
        listView.setAdapter(adapter);

        if (TextUtil.isNotEmpty(SpUtil.getUserInfoId(getActivity()))) {
            ll_bg.setVisibility(View.VISIBLE);
//            presenter.getMyShopCart(1, 1 + "", 100 + "", status + "");
        } else {
            ll_bg.setVisibility(View.GONE);
            loadingView.showPager(LoadingPager.STATE_EMPTY);
            loadingView.setImg(R.drawable.empty1);
            loadingView.setContent(getString(R.string.qnhwdl));

        }
        initView();
        return view;
    }

    private Subscription mSubscription;
    private String skuid;
    private int type;

    private void initView() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.CAR_CHANGE) {
                    int cont = (int) bean.content;
                    if (cont == 1) {
                        llPay.setVisibility(View.GONE);
                        llDelete.setVisibility(View.VISIBLE);
                        type = 1;
                    } else {
                        llPay.setVisibility(View.VISIBLE);
                        llDelete.setVisibility(View.GONE);
                        type = 0;
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> datas = adapter.getDatas();
                for (Product data : datas) {
                    for (Product dat : data.protList) {
                        if (dat.isChoose) {
                            if (TextUtil.isNotEmpty(skuid)) {
                                skuid = skuid + "," + dat.product_sku_id;
                            } else {
                                skuid = dat.product_sku_id;
                            }
                        }
                    }
                }

                if (TextUtil.isNotEmpty(skuid)) {
                    presenter.deletProduct(skuid);
                    showPress(getString(R.string.scz));
                } else {
                    ToastUtils.showNormalToast(getString(R.string.nhwxzysc));
                }

            }
        });

        adapter.setOnCheckAllListener(new ShopCarAllAdapter.onCheckAllListener() {
            @Override
            public void checks(Product content) {
                if (content!=null) {
                    Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
                    intent.putExtra("id", content.product_sku_id);
                    intent.putExtra("type", status);
                    intent.putExtra("companyType", content.company_type);
                    startActivity(intent);
                } else {
                    cout = 0;
                    couts = 0;
                    totalMoney = 0;
                    List<Product> datas = adapter.getDatas();
                    for (Product product : datas) {
                        for (Product data : product.protList) {

                            if (data.isChoose) {
                                cout = cout + Integer.valueOf(data.number);
                                couts++;
                                if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                                    totalMoney = totalMoney + Double.valueOf(data.pro_price) * Double.valueOf(data.number);
                                } else {
                                    totalMoney = totalMoney + Double.valueOf(data.integral_price) * Double.valueOf(data.number);
                                }
                            }

                        }
                    }
                    tvPay.setText(getString(R.string.qjss) + cout + ")");
                    tvDelete.setText(getString(R.string.shanchu) + couts + ")");
                    tvTotal.setText(totalMoney + (datas.get(0).protList.get(0).pro_site.equals("1")||datas.get(0).protList.get(0).pro_site.equals("3") ? "￥" : getString(R.string.jf)));

                }

            }
        });
        ivChoose.setOnCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cout = 0;
                    couts = 0;
                    totalMoney = 0;

                    for (Product products : product.data) {
                        for (Product data : products.protList) {
                            data.isChoose = true;
                            cout = cout + Integer.valueOf(data.number);
                            couts++;
                            if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                                totalMoney = totalMoney + Double.valueOf(data.pro_price) * Double.valueOf(data.number);
                            } else {
                                totalMoney = totalMoney + Double.valueOf(data.integral_price) * Double.valueOf(data.number);
                            }
                        }
                    }
                    tvPay.setText(getString(R.string.qjss) + cout + ")");
                    tvDelete.setText(getString(R.string.shanchu) +"("+ couts + ")");
                    tvTotal.setText(getString(R.string.heji) +(product.data.get(0).protList.get(0).pro_site.equals("1") ||product.data.get(0).protList.get(0).pro_site.equals("3")? "￥" : "")+ totalMoney + (product.data.get(0).protList.get(0).pro_site.equals("1") ||product.data.get(0).protList.get(0).pro_site.equals("3")? "" : getString(R.string.jf)));
                } else {
                    cout = 0;
                    totalMoney = 0;

                    for (Product products : product.data) {
                        for (Product data : products.protList) {
                            data.isChoose = false;

                        }
                    }
                    tvPay.setText(R.string.qjs);
                    tvDelete.setText(R.string.shanc);
                    tvTotal.setText(0 + (product.data.get(0).protList.get(0).pro_site.equals("1") ||product.data.get(0).protList.get(0).pro_site.equals("3")? "￥" :getString(R.string.jf)  ));
                }
                adapter.setData(product.data);
            }
        });
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.userInfoId = SpUtil.getUserInfoId(getActivity());
                order.proSite = status + "";
                dat.clear();
                List<Product> datas = adapter.getDatas();
                for (Product product : datas) {
                    for (Product data : product.protList) {

                        if (data.isChoose) {
                            Oparam oparam = new Oparam();
                            oparam.productSkuId = data.product_sku_id;
                            oparam.number = data.number;
                            oparam.company_type = data.company_type;
                            if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                                oparam.integralPrice = "0";
                            } else {
                                oparam.integralPrice = data.integral_price;
                            }
                            dat.add(oparam);
                        }


                    }
                }
                if (order != null&&dat.size()>0) {
                    order.productList = dat;
                    Intent intent = new Intent(getActivity(), EditorOrderActivity.class);
                    intent.putExtra("data", order);
                    intent.putExtra("type", status);
                    intent.putExtra("companyType", dat.get(0).company_type);
                    startActivity(intent);
                } else {
                    ToastUtils.showNormalToast(getString(R.string.nhwxsp));
                }
            }
        });
    }

    private double totalMoney;
    private int cout;
    private int couts;
    private OrderParam order = new OrderParam();
    private List<Oparam> dat = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtil.isNotEmpty(SpUtil.getUserInfoId(getActivity()))) {
            if (presenter != null) {
                presenter.getMyShopCart(1, 1 + "", 100 + "", status + "");
            }
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
        if (bind != null) {
            bind.unbind();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private Product product = null;

    @Override
    public <T> void toEntity(T entity, int type) {
        hidePress();
        if (type == 1) {
            product = (Product) entity;
            if (product != null && product.data != null && product.data.size() > 0) {
                ll_bg.setVisibility(View.VISIBLE);
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(product.data);
            } else {
                ll_bg.setVisibility(View.GONE);
                loadingView.setImg(R.drawable.empty1);
                loadingView.setContent(getString(R.string.gwchmybb));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
        } else if (type == 2) {
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CAR_CHANGEA, ""));
            presenter.getMyShopCart(1, 1 + "", 100 + "", status + "");

        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        ll_bg.setVisibility(View.GONE);
        loadingView.setImg(R.drawable.empty1);
        loadingView.setContent(getString(R.string.gwchmybb));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
    }
}
