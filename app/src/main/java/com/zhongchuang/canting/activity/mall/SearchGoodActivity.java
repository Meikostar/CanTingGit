package com.zhongchuang.canting.activity.mall;

import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;
import com.zhongchuang.canting.widget.GoodsSearchView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class SearchGoodActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.gsv)
    GoodsSearchView gsv;
    @BindView(R.id.fbl_hot)
    FlexboxLayout fblHot;
    @BindView(R.id.fbl_old)
    FlexboxLayout fblOld;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.delete)
    ImageView delete;
    private BasesPresenter presenter;
    private String content;
    private int type;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_search_good);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        presenter.seaContentList();
        content=getIntent().getStringExtra("data");
        type=getIntent().getIntExtra("type",1);
        if(TextUtil.isNotEmpty(content)){
            gsv.setKeyWords(content);
            contents=content;
        }

    }
    private String contents;


    private Subscription mSubscription;
    @Override
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.SEAECH){
                    finish();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        gsv.registerListener(new GoodsSearchView.OnSearchBoxClickListener() {
            @Override
            public void onClean() {

            }

            @Override
            public void onCancle() {
              if(TextUtil.isNotEmpty(contents)){
                  closeKeyBoard();
                  RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SEAECH,""));
                  Intent intent = new Intent(SearchGoodActivity.this, ShopListSearchActivity.class);
                  intent.putExtra("data",contents);
                  intent.putExtra("state",type);
                  startActivity(intent);
                  finish();

              }else {
                  showToasts(getString(R.string.qsrssnr));
              }
            }

            @Override
            public void finishs() {
                finish();
            }

            @Override
            public void onKeyWordsChange(String keyWords) {
                contents=keyWords;
            }
        });
       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               showPopwindow();
           }
       });
    }
    public void showPopwindow() {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(R.string.qdqklss);
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
                 presenter.clearSearch();
                dialog.dismiss();
            }
        });
    }
    public static final int from = 4;

    @Override
    public void initData() {

    }

    private List<Home> data1 = new ArrayList<>();
    private List<Home> datas = new ArrayList<>();

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter(FlexboxLayout fblGarnish) {
        fblGarnish.removeAllViews();
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(TextUtil.isEmpty(datas.get(i).sea_name) ? datas.get(i).u_name : datas.get(i).sea_name);
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {
                        for (int j = 0; j < datas.size(); j++) {
                            if (j == position) {
                                closeKeyBoard();
                                Intent intent = new Intent(SearchGoodActivity.this, ShopListSearchActivity.class);
                                intent.putExtra("data",TextUtil.isEmpty(datas.get(j).sea_name) ? datas.get(j).u_name : datas.get(j).sea_name);
                                startActivity(intent);
                            }

                        }
                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(String content) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(this, 10);
        lp.leftMargin = DensityUtil.dip2px(this, 15);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(this).inflate(R.layout.dish_item, null);
        view.setBg(R.drawable.selector_bgs);
        view.setColors(R.color.slow_black);
        String name = content;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name) + 12;
        view.setSize(with, 30, 13, 0);
        view.setLayoutParams(lp);
        view.setCustomText(content);

        return view;
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==1){
            Home home = (Home) entity;
            datas = home.admin;
            setTagAdapter(fblHot);
            if (home.user != null && home.user.size() != 0) {
                llBg.setVisibility(View.VISIBLE);
                datas = home.user;
                setTagAdapter(fblOld);
            } else {
                llBg.setVisibility(View.GONE);
            }
        }else {
            llBg.setVisibility(View.GONE);
        }


        closeKeyBoard();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


}
