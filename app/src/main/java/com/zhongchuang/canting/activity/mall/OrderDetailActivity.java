package com.zhongchuang.canting.activity.mall;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.adapter.OrderItemAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RegularListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.rl_menu)
    RegularListView rlMenu;
    @BindView(R.id.et_content)
    TextView etContent;
    @BindView(R.id.tv_cout)
    TextView tvCout;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.ll_bottom_button)
    View llBottomButton;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_re_time)
    TextView tvReTime;


    private String id;
    private String transaction_id;

    private ArrayList<UploadFileBean> img_path = new ArrayList<>();
    private OrderItemAdapter adapter;
    private BasesPresenter presenter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {
                if(data==null){
                    return;
                }
                if(TextUtil.isNotEmpty(data.merchantUserId)){
                    Intent intentc = new Intent(OrderDetailActivity.this, ChatActivity.class);
                    intentc.putExtra("userId",data.merchantUserId );
                    startActivity(intentc);
                }else {
                    showToasts(getString(R.string.sjwkt));
                }

            }

            @Override
            public void navigationimg() {

            }
        });
        id = getIntent().getStringExtra("id");
        transaction_id = getIntent().getStringExtra("transaction_id");
        presenter = new BasesPresenter(this);
        presenter.orderDetails(id);
        adapter = new OrderItemAdapter(this);
        rlMenu.setAdapter(adapter);

    }

    @Override
    public void bindEvents() {

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwind(id);
            }
        });
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(content);
                showToasts(getString(R.string.fzcg));
            }
        });
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dat.clear();
                order.userInfoId = SpUtil.getUserInfoId(OrderDetailActivity.this);
                order.proSite = data.proSite + "";
                    for (OrderData data : data.protList) {


                            Oparam oparam = new Oparam();
                            oparam.productSkuId = data.product_sku_id;
                            oparam.number = data.prod_number;
                            if (data.pro_site.equals("1")) {
                                oparam.integralPrice = "0";
                            } else {
                                oparam.integralPrice = data.integral_price;
                            }
                            dat.add(oparam);


                }
                order.productList=dat;
                order.transaction_id=transaction_id;
                Intent intent = new Intent(OrderDetailActivity.this, EditorOrderActivity.class);
                intent.putExtra("data", order);
                intent.putExtra("type", data.proSite.equals("1")?1:2);
                startActivity(intent);
            }
        });
    }

    private int poition;

    @Override
    public void initData() {

    }

    public void showPopwind(final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(OrderDetailActivity.this, R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText(R.string.qdqxgdd);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(OrderDetailActivity.this).setMessageView(views).create();
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
                showProgress(getString(R.string.qxzs));
                presenter.receiptGoods( "",id);
                dialog.dismiss();
            }
        });
    }

    private List<Oparam> dat = new ArrayList<>();
   private OrderData data;
    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if(type==7){
            data = (OrderData) entity;

            if(data!=null){
                if (TextUtil.isNotEmpty(data.orderType)) {
                    if (data.orderType.equals("1")) {
                        tvState.setText(R.string.ddmjfk);
                        llBg.setVisibility(View.VISIBLE);
                        tvCreatTime.setText(getString(R.string.cjsj) + TimeUtil.formatTtimeName(data.protList.get(0).create_time));
                    } else if ( data.orderType.equals("2")) {
                        tvState.setText(getString(R.string.ddsjfh));
                        llBg.setVisibility(View.GONE);
                        tvPayTime.setText(getString(R.string.fksj) + TimeUtil.formatTtimeName(data.protList.get(0).payment_time));
                        tvCreatTime.setText(getString(R.string.cjsj) + TimeUtil.formatTtimeName(data.protList.get(0).create_time));
                        tvPayTime.setVisibility(View.VISIBLE);
                    }  else if ( data.orderType.equals("4")|| data.orderType.equals("8")) {
                        tvState.setText(getString(R.string.ddyqx));
                        llBg.setVisibility(View.GONE);
                        tvPayTime.setText(getString(R.string.fksj) + TimeUtil.formatTtimeName(data.protList.get(0).payment_time));
                        tvCreatTime.setText(getString(R.string.cjsj)+ TimeUtil.formatTtimeName(data.protList.get(0).create_time));
                        tvSendTime.setText(getString(R.string.qxddsj) + TimeUtil.formatTtimeName(data.protList.get(0).make_targain_time));
                        tvPayTime.setVisibility(View.VISIBLE);
                        tvSendTime.setVisibility(View.VISIBLE);
                    }else if (data.orderType.equals("3")) {
                        tvCreatTime.setText(getString(R.string.cjsj) + TimeUtil.formatTtimeName(data.protList.get(0).create_time));
                        tvPayTime.setText(getString(R.string.fksj) + TimeUtil.formatTtimeName(data.protList.get(0).payment_time));
                        tvSendTime.setText(getString(R.string.fhsj) + TimeUtil.formatTtimeName(data.protList.get(0).send_time));
                        tvPayTime.setVisibility(View.VISIBLE);
                        tvSendTime.setVisibility(View.VISIBLE);

                        llBg.setVisibility(View.VISIBLE);
                        tvState.setText(getString(R.string.dsh));
                        tvCancel.setVisibility(View.GONE);
                    } else if (data.orderType.equals("7")) {
                        tvState.setText(getString(R.string.jywc));
                        tvCreatTime.setText(getString(R.string.cjsj) + TimeUtil.formatTtimeName(data.protList.get(0).create_time));
                        tvPayTime.setText(getString(R.string.fksj)+ TimeUtil.formatTtimeName(data.protList.get(0).payment_time));
                        tvSendTime.setText(getString(R.string.fhsj)+ TimeUtil.formatTtimeName(data.protList.get(0).send_time));
                        tvReTime.setText(getString(R.string.shsj) + TimeUtil.formatTtimeName(data.protList.get(0).make_targain_time));
                        tvPayTime.setVisibility(View.VISIBLE);
                        tvSendTime.setVisibility(View.VISIBLE);
                        tvReTime.setVisibility(View.VISIBLE);

                    }
                }
                if(TextUtil.isNotEmpty(data.proSite)){
                    if(data.proSite.equals("1")){
                        tvTotal.setText(data.totalPrice+"  ￥ ");
                    }else if(data.proSite.equals("3")){
                        tvTotal.setText(data.totalPrice+"  ￥ ");
                    }else {
                        tvTotal.setText( data.totalIntegralPrice+getString(R.string.sjf));
                    }
                }

                if (TextUtil.isNotEmpty(data.protList.get(0).phone_number)) {
                    tvPhone.setText(getString(R.string.dhs)+data.protList.get(0).phone_number);
                }
                if (TextUtil.isNotEmpty(data.protList.get(0).address)) {
                    tvAddress.setText(getString(R.string.shdzs)+data.protList.get(0).address);
                }
                if (TextUtil.isNotEmpty(data.protList.get(0).order_id)) {
                    content=data.protList.get(0).order_id;
                    tvCode.setText(getString(R.string.ddbhs) + data.protList.get(0).order_id);
                }
                if (TextUtil.isNotEmpty(data.protList.get(0).nick_name)) {
                    tvName.setText(getString(R.string.shrs) + data.protList.get(0).nick_name);
                }
                if(TextUtil.isNotEmpty(data.totalNumber)){
                    tvCout.setText(getString(R.string.gong)+"\t"+data.totalNumber+"\t"+getString(R.string.jsp));
                }
                if(data.proSite.equals("2")){

                    tvSum.setText(getString(R.string.hjs) + data.totalIntegralPrice+getString(R.string.jf));

                }else {
                    tvSum.setText(getString(R.string.hjs)+ data.totalPrice+"￥");
                }


                adapter.setData(data.protList);
            }

        }else {
            presenter.orderDetails(id);
        }

    }
    private OrderParam order = new OrderParam();
   private String content="meiko";
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


}
