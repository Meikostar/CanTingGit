package com.zhongchuang.canting.activity.mall;

import android.content.Intent;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.pay.ALiPayActivity;
import com.zhongchuang.canting.adapter.OrderAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.been.BaseBe;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.ProductBuy;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.been.pay.WpParam;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.PayPopWindow;
import com.zhongchuang.canting.widget.payWindow;
import com.zhongchuang.canting.wxapi.WXPayEntryActivity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 填写订单
 */
public class EditorOrderActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_add_car)
    TextView tvAddCar;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    private OrderAdapter adapter;
    private BasesPresenter presenter;
    private OrderParam param;
    private int type;
    private String companyType;
    private String cont;
    private double wpSum;
    private String orderNO;
    private String productName;
    private String points;
    private String orderAmount;
    private int prosite;
    private double sumDhz;
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .clientId("AbRWviKkeFvCy5nfE41aLxY8sS6AVGVymTZDhb1SAVyULO2sKcFvk1ALzm7weAHgSzInMv7-ZzsVfQKO");
            .clientId("ATxM3pja08ScAwqB2crhgTVp0S-81tZcS_H-S54vinXbxwJzY9fkvTS4CdpJ5QDXCp1Yn6AjWCRmvLWH");
    @Override
    public void initViews() {
        setContentView(R.layout.activity_editor_order);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        navigationBar.setNavigationBarListener(this);
        adapter = new OrderAdapter(this);
        param = (OrderParam) getIntent().getSerializableExtra("data");
        type = getIntent().getIntExtra("type", 1);
        cont = getIntent().getStringExtra("cont");
        companyType = getIntent().getStringExtra("companyType");

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        listview.setAdapter(adapter);
        presenter.getUserAddress("0");
        presenter.getPoints(SpUtil.getMobileNumber(this));
        showSelectType();
        adapter.setType(type);
        if(TextUtil.isNotEmpty(companyType)){
            param.companyType=companyType;
        }else {
            param.companyType=CanTingAppLication.CompanyType;
        }
        presenter.accountMoney(param);
        showProgress(getString(R.string.jzz));
    }

    private List<Oparam> dat = new ArrayList<>();

    private String actual_amount;
    public void onBuyPressed() {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal((Double.valueOf(actual_amount)*(100.0/687))+""), "USD", "Recharge",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, RQ_PAYPAL_PAY);
    }
    @Override
    public void bindEvents() {
        adapter.setListener(new OrderAdapter.selectItemListener() {
            @Override
            public void delete(OrderData data, int poistion) {
                Intent intent = new Intent(EditorOrderActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", data.mId);
                startActivity(intent);
            }
        });
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorOrderActivity.this, AddressListActivity.class);
                intent.putExtra("state",2);
                startActivityForResult(intent,11);
            }
        });

        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtil.isEmpty(adressId)) {
                    showToasts(getString(R.string.qtjshdz));
                    return;
                }

                actual_amount=totalPrice+"";
                if(TextUtil.isEmpty(param.proSite)){
                    if(type==0){
                        return;
                    }else {
                        param.proSite=type+"";
                    }
                }
                if(param.proSite.equals("1")){
                    shopBuyWindow.setData("￥ "+totalPrice+"");
//                    shopBuyWindow.showPopView(navigationBar);
                    payPopWindow.showPopView(navigationBar);
                }else {
                    param.addressId = adressId;
                    List<OrderData> datas = adapter.getDatas();
                    int a = 0;
                    for (Oparam oparam : param.productList) {
                        oparam.phoneMessage = datas.get(0).phoneMassege;
                        a++;
                    }
                    param.payType="0";
                    param.integralPayType="1";

                    if(TextUtil.isNotEmpty(companyType)){
                        param.companyType=companyType;
                    }else {
                        param.companyType=CanTingAppLication.CompanyType;
                    }
                    presenter.submitOrder(param);
                    showProgress(getString(R.string.tjz));
                }


            }
        });
    }

    private payWindow shopBuyWindow;
    private int state=1;
    private void showSelectType() {
        shopBuyWindow = new payWindow(this);
        shopBuyWindow.setSureListener(new payWindow.ClickListener() {
            @Override
            public void clickListener(int types) {


                if(types==1){
                    param.payType="2";
                    state=1;
//                    presenter.submitOrders(param);
                }else if(types==2){
                    param.payType="1";
                    state=2;
//                    presenter.submitOrder(param);
                }else if(types==3) {
                    param.payType = "3";
                    state=3;
//                    presenter.submitOrder(param);
                }else {
                    List<OrderData> datas = adapter.getDatas();
                    int i = 0;
                    if (param.data != null) {
                        for (OrderParam data : param.data) {
                            param.addressId = adressId;
                            param.proSite = data.proSite;
                            param.userInfoId = data.userInfoId;
                            int a = 0;
                            for (Oparam oparam : data.productList) {
                                oparam.phoneMessage = datas.get(i).phoneMassege;
                                dat.add(oparam);
                                a++;
                            }
                            i++;
                        }
                        param.productList = dat;
                    } else {

                        param.addressId = adressId;
                        int a = 0;
                        for (Oparam oparam : param.productList) {
                            oparam.phoneMessage = datas.get(0).phoneMassege;
                            a++;
                        }
                        if(datas.get(0).proSite.equals("3")){
                            orderAmount=totalPrice+"";
                            productName=datas.get(0).protList.get(0).pro_name;
                            wpSum=totalInter;
                            orderNO=param.productList.get(0).productSkuId;
                            prosite=3;
                        }else  if(datas.get(0).proSite.equals("2")){
                            prosite=2;
                            orderAmount=totalPrice+"";
                            productName=datas.get(0).protList.get(0).pro_name;
                            wpSum=totalInter;
                            orderNO=param.productList.get(0).productSkuId;
                        }else  if(datas.get(0).proSite.equals("1")){
                            prosite=1;
                            orderAmount=totalPrice+"";
                            productName=datas.get(0).protList.get(0).pro_name;
                            wpSum=totalInter;
                            orderNO=param.productList.get(0).productSkuId;
                        }
                    }
                    if(state==1){
                        param.payType="2";
                        param.integralPayType="2";
                        if(TextUtil.isNotEmpty(companyType)){
                            param.companyType=companyType;
                        }else {
                            param.companyType=CanTingAppLication.CompanyType;
                        }
                        presenter.submitOrders(param);
                    }else if(state==2){
                        param.payType="1";
                        param.integralPayType="1";
                        if(TextUtil.isNotEmpty(companyType)){
                            param.companyType=companyType;
                        }else {
                            param.companyType=CanTingAppLication.CompanyType;
                        }
                        presenter.submitOrder(param);
                    }else if(state==3) {
                        param.payType = "3";
                        param.integralPayType = "3";
                        if(TextUtil.isNotEmpty(companyType)){
                            param.companyType=companyType;
                        }else {
                            param.companyType=CanTingAppLication.CompanyType;
                        }
                        presenter.submitOrderpal(param);
                    }
                    showProgress(getString(R.string.tjz));
                }


            }
        });

    }


    @Override
    public void initData() {
        showPayType();
    }
    private PayPopWindow payPopWindow;

    private void showPayType() {
        payPopWindow = new PayPopWindow(this);
        payPopWindow.setSureListener(new PayPopWindow.ClickListener() {
            @Override
            public void clickListener(int types) {
                finish();
            }
        });

    }
    private String adressId;
    private String paypalId;

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 2) {
            List<AddressBase> addressBases = (List<AddressBase>) entity;
            if (addressBases == null || addressBases.size() == 0) {
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("state",2);
                startActivityForResult(intent,11);
                showToasts(getString(R.string.nhwtjshdz));
            } else {
                for (AddressBase base : addressBases) {
                    if (base.is_default.equals("true")) {
                        adressId = base.address_id;
                        tvAddress.setText(base.shipping_address + base.detailed_address);
                        tvName.setText(base.shipping_name + "     " + base.link_number);
                    }
                }


            }
        } else if (type == 6) {
            List<OrderData> datas = (List<OrderData>) entity;
            if (TextUtil.isNotEmpty(cont)) {
                for (OrderData data : datas) {
                    data.protList.get(0).proSku = cont;
                }
            }
            adapter.setData(datas);
            for (OrderData data : datas) {
                totalPrice=0;
                totalInter=0;
                if (data.proSite.equals("1")) {
                    totalPrice = totalPrice + Double.valueOf(data.totalPrice);

                    totalInter = totalInter + Double.valueOf(data.protList.get(0).market_price);
                    dw = "￥";
                } if (data.proSite.equals("3")) {
                    totalPrice = totalPrice + Double.valueOf(data.totalPrice);

                    totalInter = totalInter + Double.valueOf(data.totalIntegralPrice);
                    dw = "￥";
                } else {
                    totalPrice = totalPrice + Double.valueOf(data.totalIntegralPrice);
                    totalInter = totalInter + Double.valueOf(data.totalIntegralPrice);
                    dw = "兑换值";
                }
            }
            tvAddCar.setText(totalPrice + "  " + dw);
        } else if (type == 8) {
            WEIXINREQ weixinreq = (WEIXINREQ) entity;
            if(weixinreq!=null){
                payPopWindow.showPopView(navigationBar);
//                Intent intent = new Intent(EditorOrderActivity.this, WXPayEntryActivity.class);
//                intent.putExtra("weixinreq", weixinreq);
//                startActivityForResult(intent, RQ_WEIXIN_PAY);
            }else {
                List<OrderData> datas = adapter.getDatas();
                prosite=2;
                orderAmount="0";
                productName=datas.get(0).protList.get(0).pro_name;
                wpSum=totalInter;
                orderNO=param.productList.get(0).productSkuId;
                WpParam wpParam = new WpParam();
                wpParam.orderAmount=orderAmount;
                wpParam.orderNO=orderNO;
                wpParam.points=wpSum+"";
                wpParam.productName=productName;
                wpParam.phone= SpUtil.getMobileNumber(this);

                presenter.shoppingCut(wpParam);


            }

        }else if (type == 9) {
            String alisign = (String) entity;
            if(alisign!=null){
//                Intent intent = new Intent(this, ALiPayActivity.class);
//                intent.putExtra("signedstr",alisign);
//                startActivityForResult(intent, RQ_ALIPAY_PAY);
                payPopWindow.showPopView(navigationBar);
            }

        }else if (type == 16) {
            paypalId=(String)entity;
           onBuyPressed();

        }else if (type == 18) {
            showToasts(getString(R.string.zfcg));
            finish();
        }else if (type == 123) {
            showToasts(getString(R.string.zfcg));
            finish();
        }else if (type == 222) {
            BaseResponse base = (BaseResponse) entity;
            if(TextUtil.isNotEmpty(base.message)){
                sumDhz=Double.valueOf(base.message);
            }
        }else if (type == 321) {
            showToasts(getString(R.string.zfcg));
            finish();
        }
    }

    private int RQ_WEIXIN_PAY=12;
    private int RQ_PAYPAL_PAY=16;
    private int RQ_ALIPAY_PAY=10;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode == RQ_WEIXIN_PAY) {

                if(prosite==3){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);

                    presenter.shoppingCut(wpParam);

                }else    if(prosite==2){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.orderAmount=orderAmount;
                    wpParam.phone= SpUtil.getMobileNumber(this);
                    presenter.shoppingCut(wpParam);
                }else {
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);
                    presenter.shoppingGive(wpParam);
                }
//           if (requestCode == RQ_WEIXIN_PAY) {
//                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
//            }
            }else if(requestCode==11){
              AddressBase base= (AddressBase) data.getSerializableExtra("data");
                adressId = base.address_id;
                tvAddress.setText(base.shipping_address + base.detailed_address);
                tvName.setText(base.shipping_name + "     " + base.link_number);
            }else if(requestCode==10){
                if(prosite==3){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);

                    presenter.shoppingCut(wpParam);

                }else    if(prosite==2){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);

                    presenter.shoppingCut(wpParam);
                }else {
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);
                    presenter.shoppingGive(wpParam);
                }
            }else if(requestCode==RQ_PAYPAL_PAY){
                presenter.success("",paypalId);
                if(prosite==3){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);

                    presenter.shoppingCut(wpParam);

                }else    if(prosite==2){
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);
                    presenter.shoppingCut(wpParam);
                }else {
                    WpParam wpParam = new WpParam();
                    wpParam.orderAmount=orderAmount;
                    wpParam.orderNO=orderNO;
                    wpParam.points=wpSum+"";
                    wpParam.productName=productName;
                    wpParam.phone= SpUtil.getMobileNumber(this);
                    presenter.shoppingGive(wpParam);
                }
            }
        }

    }
    private double totalPrice;
    private double totalInter;
    private String dw;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void toNextStep(int type) {
    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
        showToasts(msg);
    }


}
