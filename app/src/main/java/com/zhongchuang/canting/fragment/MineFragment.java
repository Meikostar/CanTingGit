package com.zhongchuang.canting.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.MineCodeActivity;
import com.zhongchuang.canting.activity.PersonMessageActivity;
import com.zhongchuang.canting.activity.WebViewActivity;
import com.zhongchuang.canting.activity.mall.AddressListActivity;
import com.zhongchuang.canting.activity.mall.SettingActivity;
import com.zhongchuang.canting.activity.mine.CodeUploadActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.activity.mine.ProfitChargeActivity;
import com.zhongchuang.canting.activity.mine.WpChargeActivity;
import com.zhongchuang.canting.adapter.ProfileItemAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.been.PROFILE_ITEM;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.SharePopWindow;
import com.zhongchuang.canting.widget.StickyScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/12/4.
 */

public class MineFragment extends LazyFragment implements BaseContract.View, AdapterView.OnItemClickListener {


    @BindView(R.id.line)
    View line;
    @BindView(R.id.person_pic)
    ImageView personPic;
    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_get)
    TextView tvGet;
    @BindView(R.id.tv_cz)
    TextView tvCz;
    @BindView(R.id.ll_cz)
    LinearLayout llCz;
    @BindView(R.id.tv_kf)
    TextView tvKf;
    @BindView(R.id.ll_gj)
    LinearLayout llGj;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.ll_lt)
    LinearLayout llLt;
    @BindView(R.id.ll_bgs)
    LinearLayout llBgs;
    @BindView(R.id.tv_ye)
    TextView tvYe;
    @BindView(R.id.ll_zb)
    LinearLayout llZb;
    @BindView(R.id.grid_content)
    NoScrollGridView gridView;
    @BindView(R.id.scrollView)
    StickyScrollView scrollView;
    @BindView(R.id.ll_profit)
    LinearLayout llProfit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private BasesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.personal_page, container, false);
        ButterKnife.bind(this, viewRoot);
        initView();
        presenter = new BasesPresenter(this);
        presenter.getUserIntegral();
        setLoginMessage();
        showSelectType();
        setAdapter();
        llBgs.setFocusable(true);
        llBgs.setFocusableInTouchMode(true);
        scrollView.setFocusable(false);
        return viewRoot;
    }

    private ProfileItemAdapter adapter;

    private void setAdapter() {
        adapter = new ProfileItemAdapter(getContext(), getGridData());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    private SharePopWindow shopBuyWindow;
    private int state = 1;

    private void showSelectType() {
        shopBuyWindow = new SharePopWindow(getActivity());
        shopBuyWindow.setSureListener(new SharePopWindow.ClickListener() {
            @Override
            public void clickListener(int types) {
                ShareUtils.showMyShare(getActivity(), "", "http://www.gwlaser.tech");
            }
        });

    }

    private void setLoginMessage() {
        String phone = SpUtil.getString(getActivity(), "mobileNumber", "");
        String token = SpUtil.getString(getActivity(), "token", "");
        String name = SpUtil.getName(getActivity());
        String avar = SpUtil.getString(getActivity(), "ava", "");
        if (TextUtils.isEmpty(token) || token.equals("") || TextUtils.isEmpty(token) || token.equals("")) {
            phone = getString(R.string.qdl);

        } else {

            personName.setText(name);


        }
        if (presenter != null) {
            presenter.getUserIntegral();
            presenter.hostInfo();
        }

        Glide.with(getActivity()).load(StringUtil.changeUrl(avar)).asBitmap().transform(new CircleTransform(getActivity())).placeholder(R.drawable.editor_ava).into(personPic);


    }


    private static final int[] itemimg2 = new int[]{R.drawable.mines1, R.drawable.mines2,
            R.drawable.mines5, R.drawable.mines6, R.drawable.mines7, R.drawable.mines8, R.drawable.mines9};
    protected Bundle fragmentArgs;
    private String shopid;

    private ArrayList<PROFILE_ITEM> getGridData() {
        String[] itemname2 = new String[]{getString(R.string.hygl), getString(R.string.skmsc), getString(R.string.appxzm),
                getString(R.string.lxkf), getString(R.string.shdz), getString(R.string.wdxc), getString(R.string.shezhi)};
        ArrayList<PROFILE_ITEM> item_list = new ArrayList<PROFILE_ITEM>();

        for (int i = 0; i < 7; i++) {
            PROFILE_ITEM item = new PROFILE_ITEM();
            item.item_name = itemname2[i];
            item.item_resource = itemimg2[i];
            item_list.add(item);
        }

        return item_list;
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
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPersonDetailActivity.class);
                intent.putExtra("id", SpUtil.getUserInfoId(getActivity()) + "");
                startActivity(intent);

            }
        });

        llCz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WpChargeActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("content", bean.money_buy_integral);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });

        llGj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WpChargeActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("content", bean.jewel_integral);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });
        llLt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WpChargeActivity.class);
                intent.putExtra("type", 3);
                intent.putExtra("content", bean.chat_integral);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });
        llZb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), WpChargeActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("content", bean.direct_integral);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });
        tvGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(code);
                ToastUtil.showToast(getActivity(), getString(R.string.fzcg));
            }
        });


    }

    private String code = "888888";

    @Override
    public void onResume() {
        super.onResume();
        setLoginMessage();
//        if (null != mProfileHelper) {
//            mProfileHelper.getMyProfile();
//        }
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


    private Host data;
    private Ingegebean bean;

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 19) {
            bean = (Ingegebean) entity;
            if (bean == null) {
                return;
            }
            if (TextUtil.isNotEmpty(bean.money_buy_integral)) {
                tvCz.setText(bean.money_buy_integral);
            }
            if (TextUtil.isNotEmpty(bean.chat_integral)) {
                tvJf.setText(bean.chat_integral);
            }
            if (TextUtil.isNotEmpty(bean.jewel_integral)) {
                tvKf.setText(bean.jewel_integral);
            }
            if (TextUtil.isNotEmpty(bean.direct_integral)) {
                tvYe.setText(bean.direct_integral);
            }
            if (TextUtil.isNotEmpty(bean.profit_count)) {
                tvProfit.setText("￥"+bean.profit_count);
            }
            if (TextUtil.isNotEmpty(bean.invitation_code)) {
                code = bean.invitation_code;
                tvCode.setText(bean.invitation_code);
            }
            tvCharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfitChargeActivity.class);
                    intent.putExtra("profit", bean.profit_count);
                    startActivity(intent);
                }
            });
            llProfit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfitChargeActivity.class);
                    intent.putExtra("profit", bean.profit_count);
                    startActivity(intent);
                }
            });
        } else {
            data = (Host) entity;
            if (TextUtil.isNotEmpty(data.integralTotal)) {
//                CanTingAppLication.totalintegral = Double.valueOf(data.integralTotal);
            }
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0://会员管理
                intent = new Intent(getActivity(), WebViewActivity.class);


                intent.putExtra(WebViewActivity.WEBTITLE, R.string.huiyuan);
                intent.putExtra(WebViewActivity.WEBURL, CanTingAppLication.url);

                startActivity(intent);
                break;
            case 1://积分充值
                startActivity(new Intent(getActivity(), CodeUploadActivity.class));
                break;
            case 2://我的钱包
//                startActivity(new Intent(getActivity(), MineCodeActivity.class));
                shopBuyWindow.showPopView(line);
                break;
//            case 3://我的二维码
//                shopBuyWindow.showPopView(line);
//                break;
            case 3://APP下载码
                Intent intentc = new Intent(getActivity(), ChatActivity.class);
//                intentc.putExtra("userId", "urio1116552863061901312");//信联
//                intentc.putExtra("userId", "urio1087627518581669888");//对了
                intentc.putExtra("userId", "urio1098110334129930240");//对了
                startActivity(intentc);
                break;
            case 4://联系客服
                startActivity(new Intent(getActivity(), AddressListActivity.class));
                break;
            case 5://收货地址
                //打开本地相册
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //设定结果返回
                startActivityForResult(i, 0);
                break;

            case 6://我的相册
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}
