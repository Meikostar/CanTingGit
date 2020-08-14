package com.zhongchuang.canting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.MainActivity;
import com.zhongchuang.canting.activity.chat.ChatSplashActivity;
import com.zhongchuang.canting.activity.mall.EditorOrderActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallActivity;
import com.zhongchuang.canting.activity.offline.EntityStoreActivity;
import com.zhongchuang.canting.activity.offline.StoreDetailActivity;
import com.zhongchuang.canting.activity.pay.ALiPayActivity;
import com.zhongchuang.canting.activity.shop.AppStoreActivity;
import com.zhongchuang.canting.adapter.HomeItemdapter;
import com.zhongchuang.canting.adapter.HomeItemdapters;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.been.AppInfo;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.payWindow;
import com.zhongchuang.canting.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Administrator on 2017/10/27.
 */

public class Fragment_more_app extends BaseFragment implements BaseContract.View {


    Unbinder unbinder;
    @BindView(R.id.grid_content1)
    NoScrollGridView gridContent1;
    @BindView(R.id.card)
    CardView card;

    private BasesPresenter presenter;


    private TimeCount timeCount;
    private HomeItemdapters homedapter;

    private int[] homeimg1 = {R.drawable.homes_4, R.drawable.homes_1, R.drawable.homes_2,
            R.drawable.homes_3, R.drawable.homes_5, R.drawable.homes_10, R.drawable.homes_6, R.drawable.homes_7, R.drawable.homes_8, R.drawable.homes_9};
    private GAME messageGroup=HomeActivitys.messageGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        homedapter = new HomeItemdapters(getActivity());
        gridContent1.setAdapter(homedapter);
        showSelectType();
        if(CanTingAppLication.CompanyType.equals("2")){
            presenter = new BasesPresenter(this);
            presenter.getHomeSpecie();
            presenter.getAppInfo();
        }else {
            homedapter.setItemClick(new HomeItemdapters.ItemClikListener() {
                @Override
                public void itemClick(int poistion) {
                    switch (poistion) {
                        case 1: //商城
//                        Intent intentsss = new Intent(HomeActivitys.this, FaceCreatActivity.class);


//                            Intent intent = new Intent(getActivity(), ShopMallActivity.class);
//                            intent.putExtra("type", 1);
//                            startActivity(intent);
                            Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                            startActivity(intent);

                            break;
                        case 2://乐聊
                            Intent etintent = new Intent(getActivity(), EntityStoreActivity.class);
                            startActivity(etintent);
//                            Intent intentsss = new Intent(getActivity(), ShopCompsiteMallActivity.class);
//                            intentsss.putExtra("type", 1);
//                            startActivity(intentsss);
                            break;
                        case 3://乐聊
                            Intent intentss = new Intent(getActivity(), ShopMallActivity.class);
                            intentss.putExtra("type", 2);
                            startActivity(intentss);


                            break;
                        case 0://直播

                            if (HomeActivitys.isLogin) {
                                Intent intent2 = new Intent(getActivity(), ChatSplashActivity.class);

//                            intent2.putExtra("data", data);
                                intent2.putExtra("type", 1);
                                startActivity(intent2);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                            break;
                        case 4://我的

                            if (HomeActivitys.isLogin) {
                                Intent intents = new Intent(getActivity(), ChatSplashActivity.class);
                                if (HomeActivitys.messageGroup == null) {
                                    return;
                                }
                                intents.putExtra("data", HomeActivitys.messageGroup);
                                startActivity(intents);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                            break;
                        case 5://同城

                            if (HomeActivitys.isLogin) {
                                Intent intent3 = new Intent(getActivity(), MainActivity.class);
                                intent3.putExtra("type", 3);
                                startActivity(intent3);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                            break;
                        case 6: //应用
//                            ShareUtils.showMyShareApp(getActivity(), "", "");
                            ShareBean shareBean = new ShareBean();
                            shareBean.img_ = "https://ifun.xjxlsy.cn/h5/images/logo.png";
                            shareBean.content_ = "快来了体验数字时代app吧！";
//                shareBean.content_ = data.direct_see_name + getString(R.string.zzgszbklgkb);
                            shareBean.title_ = "数字时代";
                            shareBean.url_ = com.zhongchuang.canting.db.Constant.APP_SHARE;
                            CanTingAppLication.shareBean = shareBean;
                            ShareUtils.showMyShares(getActivity(), getString(R.string.jiguang), "http://www.gwlaser.tech");
                            break;
                        case 7: //应用
                            Intent intent4 = new Intent(getActivity(), AppStoreActivity.class);
                            intent4.putExtra("type", 1);
                            startActivity(intent4);
                            break;

                    }
                }
            });

        }


        initView();

        return view;
    }

    private payWindow shopBuyWindow;
    private int state=1;
    private void showSelectType() {
        shopBuyWindow = new payWindow(getActivity());
        shopBuyWindow.setSureListener(new payWindow.ClickListener() {
            @Override
            public void clickListener(int types) {

                if(types==1){

                    state=1;

                }else if(types==2){
                    state=2;

                }else if(types==3) {

                    state=3;
//                    presenter.submitOrder(param);
                }else if(types==0) {
                    if(state==1){
                        presenter.appPay(info.app_sum,"2");
                    }else {
                        presenter.appPays(info.app_sum,"1");
                    }
                }

            }
        });

    }
    private int cout;
    private HomeActivitys activitys;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitys = ((HomeActivitys) activity);//通过强转成宿主activity，就可以获取到传递过来的数据
        activitys.setListener(new HomeActivitys.MessageNotifyListener() {
            @Override
            public void messageNotify(int couts) {
                cout=couts;
                if(!CanTingAppLication.CompanyType.equals("2")){
                    setData(cout);
                }

            }
        });
    }

    private int type;
    private int cont;
    private List<HOMES> datas = new ArrayList<>();
    public void setData(int cout) {
        String[] indepent1 = { getString(R.string.zb),getString(R.string.fhsc), getString(R.string.cjzg),"线下商城",
                getString(R.string.ll), getString(R.string.grzx), getString(R.string.appfx), getString(R.string.yy), getString(R.string.appfx),"VIP商城"};
        datas.clear();
        cont = 0;
        for (int url : homeimg1) {
            HOMES homes = new HOMES();
            homes.name = indepent1[cont];
            homes.url = url;
            if (cont == 4) {
                homes.cout = cout;
            }
            cont++;
            datas.add(homes);
        }
        if(homedapter!=null){
            homedapter.setData(datas);
            homedapter.notifyDataSetChanged();
        }else {
            homedapter = new HomeItemdapters(getActivity());
            homedapter.setData(datas);
            homedapter.notifyDataSetChanged();
        }




        cont = 0;



    }
    public void setType(int type) {
        this.type = type;
    }

    private void initView() {


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode == RQ_WEIXIN_PAY) {
                ToastUtil.showToast(getActivity(),getString(R.string.zfcg));
                CanTingAppLication.isPay=true;
//           if (requestCode == RQ_WEIXIN_PAY) {
//                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
//            }
            }else if(requestCode==10){
                ToastUtil.showToast(getActivity(),getString(R.string.zfcg));
                CanTingAppLication.isPay=true;
            }else if(requestCode==RQ_PAYPAL_PAY){
//                presenter.success("",paypalId);

            }
        }

    }
    private int RQ_WEIXIN_PAY=12;
    private int RQ_PAYPAL_PAY=16;
    private int RQ_ALIPAY_PAY=10;
    private AppInfo info;
    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==16){
            info= (AppInfo) entity;
        }else  if (type == 8) {
            WEIXINREQ weixinreq = (WEIXINREQ) entity;
            if(weixinreq!=null){
                Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
                intent.putExtra("weixinreq", weixinreq);
                startActivityForResult(intent, RQ_WEIXIN_PAY);
            }else {
                ToastUtil.showToast(getActivity(),getString(R.string.gmcg));
                getActivity().finish();
            }

        }else if (type == 9) {
            String alisign = (String) entity;
            if(alisign!=null){
                Intent intent = new Intent(getActivity(), ALiPayActivity.class);
                intent.putExtra("signedstr",alisign);
                startActivityForResult(intent, RQ_ALIPAY_PAY);

            }

        }else  {
            Home home = (Home) entity;
            datas.clear();
            if(home!=null&&home.category!=null){
                List<Banner>    category = home.category;
                for (Banner ban : category) {
                    HOMES homes = new HOMES();
                    homes.name = ban.category_name;
                    homes.urls = ban.category_image;
                    if (ban.category_name.equals("聊")) {
                        homes.cout = cout;
                    }
                    cont++;
                    datas.add(homes);
                }
                if(homedapter!=null){
                    homedapter.setData(datas);
                    homedapter.notifyDataSetChanged();
                }else {
                    homedapter = new HomeItemdapters(getActivity());
                    homedapter.setData(datas);
                    homedapter.notifyDataSetChanged();
                }
                gridContent1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String content= datas.get(position).name;
                        if(content.contains("聊")){
                            if (HomeActivitys.isLogin) {
                                Intent intents = new Intent(getActivity(), ChatSplashActivity.class);
                                if (HomeActivitys.messageGroup == null) {
                                    return;
                                }
                                intents.putExtra("data", HomeActivitys.messageGroup);
                                startActivity(intents);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }else if(content.contains("播")){
                            if (HomeActivitys.isLogin) {
                                Intent intent2 = new Intent(getActivity(), ChatSplashActivity.class);

//                            intent2.putExtra("data", data);
                                intent2.putExtra("type", 1);
                                startActivity(intent2);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }else if(content.contains("企业专供")){
                            Intent intentsss = new Intent(getActivity(), ShopCompsiteMallActivity.class);
                            intentsss.putExtra("type", 1);
                            startActivity(intentsss);
                        }else if(content.contains("厂家直供")){
                            Intent intent = new Intent(getActivity(), ShopMallActivity.class);
                            intent.putExtra("type", 1);
                            startActivity(intent);
                        }else if(content.contains("数字电商")){
                            Intent intentss = new Intent(getActivity(), ShopMallActivity.class);
                            intentss.putExtra("type", 2);
                            startActivity(intentss);
                        }else if(content.contains("个人中心")){
                            if (HomeActivitys.isLogin) {
                                Intent intent3 = new Intent(getActivity(), MainActivity.class);
                                intent3.putExtra("type", 3);
                                startActivity(intent3);
                            } else {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }else if(content.contains("APP分享")){
                            ShareBean shareBean = new ShareBean();
                            shareBean.img_ = "https://ifun.xjxlsy.cn/h5/images/logo.png";
                            shareBean.content_ = "快来了体验数字时代app吧！";
//                shareBean.content_ = data.direct_see_name + getString(R.string.zzgszbklgkb);
                            shareBean.title_ = "数字时代";
                            shareBean.url_ = com.zhongchuang.canting.db.Constant.APP_SHARE;
                            CanTingAppLication.shareBean = shareBean;
                            ShareUtils.showMyShares(getActivity(), getString(R.string.jiguang), "http://www.gwlaser.tech");
//                            ShareUtils.showMyShareApp(getActivity(), "", "");
                        }else if(content.contains("应用")){
                            if(CanTingAppLication.CompanyType.equals("2")){
                                if (!HomeActivitys.isLogin) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    return;
                                }
                                if(CanTingAppLication.isPay){
                                    Intent intent4 = new Intent(getActivity(), AppStoreActivity.class);
                                    intent4.putExtra("type", 1);
                                    startActivity(intent4);
                                }else {
                                    if(info!=null){
                                        showPopwindows(info);
                                    }
                                }

                            }else {
                                Intent intent4 = new Intent(getActivity(), AppStoreActivity.class);
                                intent4.putExtra("type", 1);
                                startActivity(intent4);
                            }


                        }

                    }
                });

            }else {
                setData(cout);
            }

        }

    }
    private MarkaBaseDialog dialog;

    public void showPopwindows(AppInfo info) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText("应用为收费功能，你需要支付 ￥"+info.app_sum+"方可使用！");
        dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        sure.setText("去支付");
        cancel.setText("稍后支付");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopBuyWindow.setData("￥ "+info.app_sum+"");
                shopBuyWindow.showPopView(gridContent1);

                dialog.dismiss();

            }
        });
    }
    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    //计时器
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


        }

        @Override
        public void onFinish() {


        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        unbinder.unbind();
        if(timeCount!=null){
            timeCount.cancel();
        }


    }

    private ProgressDialog mDialog;


    private void initData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}




