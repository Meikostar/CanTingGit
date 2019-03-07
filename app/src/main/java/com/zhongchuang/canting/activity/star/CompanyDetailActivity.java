package com.zhongchuang.canting.activity.star;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyDetailActivity extends BaseTitle_Activity {


    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.person_code)
    TextView personCode;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.ll_devp)
    LinearLayout llDevp;
    @BindView(R.id.ll_product)
    LinearLayout llProduct;
    @BindView(R.id.ll_products)
    LinearLayout llProducts;
    @BindView(R.id.ll_man)
    LinearLayout llMan;
    @BindView(R.id.ll_chat)
    LinearLayout llChat;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;

    @Override
    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_company, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("title", "全球加盟方案");
                intent.putExtra("titles", "云志全球价值共享平台\n" +
                        "社区运营中心特许加盟方案");
                intent.putExtra("content", "特许使用‘云志’全球统一标识\n" +
                        "配赠50寸智能电视1台\n" +
                        "配赠AI人工智能语音设备1台\n" +
                        "配赠智能商务直播设备1套\n" +
                        "配赠商务直播权限2个\n" +
                        "配赠高端茶台（含茶具）1套\n" +
                        "配赠精品武夷山大红袍10斤（商务接待）\n" +
                        "配赠智能烹饪设备2套\n" +
                        "配赠商务礼品20份（价值5万元\n" +
                        "配赠4个A4会员资");
                startActivity(intent);
            }
        });
        llDevp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("titles", "云志平台综合定位");
                intent.putExtra("title", "平台综合定位");
                intent.putExtra("content", "核心宗旨:打造全球首个跨境多元智能共享平台\n" +
                        "   \n" +
                        "1、宏观定位:\n" +
                        "★用科技优化全球产业结构、促进产业升级、助推朝阳产业发展、洞悉未来产业趋势动向、促进产业结构优化、掀起产业融合大潮。\n" +
                        "2、顶层设计定位:\n" +
                        "★汇聚全球顶尖专家、联通全球各界政要、集聚全球产业资源、领动全球产业发展。\n" +
                        "3、战略定位:\n" +
                        "★联合全球专业团队、打造核心竞争力。\n" +
                        "★培育全球各类盈利渠道、逐步由弱到强。\n" +
                        "★运用全球投资创业群体、加速终端渗透。\n" +
                        "★打造全球化多维立体架构、天地人合力推展。\n" +
                        "4、平台定位（中国大陆）:\n" +
                        "★脱虚向实、确保平台永续发展。\n" +
                        "★资源共享、分享全球消费价值。\n" +
                        "★不忘初心、创造和谐幸福愿景。\n" +
                        "5、产品定位:\n" +
                        "★质优价廉、提供实用广普健康产品。\n" +
                        "★多重营销策略、广泛覆盖所有客群。\n" +
                        "6、模式定位:\n" +
                        "★合法合规、屏蔽区域运营风险。\n" +
                        "★合理分配、福祉更多参与人员。\n" +
                        "★严进严出、提升平台会员质量。\n" +
                        "★多重工具、降低市场推广难度。\n" +
                        "★兼容并包、符合全球客户创富心理。\n" +
                        "★简单易懂、简化客户认可周期。");
                startActivity(intent);
            }
        });
        llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("titles", "云志全球激励方案");
                intent.putExtra("title", "云志全球激励方案");
                intent.putExtra("content", "为加速云志全球价值共享平台运营效率，鼓励首批消费会员快速成功，自2018年10月1日起，平台启动激励机制，具体方案如下:\n" +
                        "1、前100名A4会员额外配赠:\n" +
                        "★10000美金产品。\n" +
                        "★A4会员资格/1个（含金库）\n" +
                        "\n" +
                        "2、2018年10月31日前，个人直接推广销售5万美金产品，平台配赠2个A4会员资格（限前50名）。\n" +
                        "3、2018年10月31日前，个人直接推广销售5万美金产品，平台提供5万元人民币市场开发经费（限前50名）。\n" +
                        "4、2018年10月31日前，个人直接推广销售10万美金产品，平台配赠10万元以上MPV商用车一辆（限前50名）。\n" +
                        "5、2018年10月31日前，个人直接推广销售5万美金产品，平台奖励欧洲运营中心15日商务考察名额1个（含全部费用＋10000购物金）。\n" +
                        "备注:平台中国大陆地区奖励由各市场总监在总激励中支付。");
                startActivity(intent);
            }
        });
        llProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("titles", "云志全球价值共享平台优势");
                intent.putExtra("title", "云志全球价值共享平台优势");
                intent.putExtra("content", "1、全球同步商务直播，让您足不出户什么都不用说，就可以开拓全球市场。\n" +
                        "2、点对点移动社交软件，让您瞬间结交天下好友（聊天就能赚钱）。\n" +
                        "3、全球化移动电商平台，让您动动手指，就能找到想要的全球优品。\n" +
                        "4、合法合规、不伤人脉，还能让您轻松赚大钱。\n" +
                        "5、没有圈套、不设陷阱，让您安心做事业。\n" +
                        "6、无需学习、不用复制，让您无门槛轻松创业。\n" +
                        "7、喝茶聊天、自由自在，让您交着朋友还赚钱。\n" +
                        "8、只要消费一次，就能让您成为平台的老板。\n" +
                        "9、真实的数字价值，让您可以真正提现、购物和自建金库变现（智能收益）。\n" +
                        "10、只要有人消费，您将永远自动获取全球价值拆分收益。");
                startActivity(intent);
            }
        });
        llMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("titles", "云志全球价值共享平台优势");
                intent.putExtra("title", "云志全球价值共享平台优势");
                intent.putExtra("content", "为加速云志全球价值共享平台运营效率，鼓励首批消费会员快速成功，自2018年10月1日起，平台启动激励机制，具体方案如下:\n" +
                        "1、前100名A4会员额外配赠:\n" +
                        "★10000美金产品。\n" +
                        "★A4会员资格/1个（含金库）\n" +
                        "\n" +
                        "2、2018年10月31日前，个人直接推广销售5万美金产品，平台配赠2个A4会员资格（限前50名）。\n" +
                        "3、2018年10月31日前，个人直接推广销售5万美金产品，平台提供5万元人民币市场开发经费（限前50名）。\n" +
                        "4、2018年10月31日前，个人直接推广销售10万美金产品，平台配赠10万元以上MPV商用车一辆（限前50名）。\n" +
                        "5、2018年10月31日前，个人直接推广销售5万美金产品，平台奖励欧洲运营中心15日商务考察名额1个（含全部费用＋10000购物金）。\n" +
                        "备注:平台中国大陆地区奖励由各市场总监在总激励中支付。");
                startActivity(intent);

            }
        });
        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, AboutlActivity.class);
                intent.putExtra("titles", "云志全球消费宏利分享计划");
                intent.putExtra("title", "云志全球消费宏利分享计划");
                intent.putExtra("content", "声明:全球合作伙伴，为配合相关国家和地区法律法规、行业操守，经平台国际律师团队研讨，云志全球消费宏利分享计划将于2018年10月1日全球同步拓展（全球运营中心通行）。\n" +
                        "\n" +
                        "1、消费产品:全球采购名、新、特、优、精品（中国大陆地区采取海外直购和平行进口、含大陆产品）。\n" +
                        "\n" +
                        "2、消费方式:\n" +
                        "☞手机app线上购物\n" +
                        "☞运营中心体验购物\n" +
                        "☞人员直荐分享购物\n" +
                        "\n" +
                        "3、消费标准:\n" +
                        "A1会员  $100美金\n" +
                        "A2会员  $500美金\n" +
                        "A3会员  $1000美金\n" +
                        "A4会员  $10000美金\n" +
                        "\n" +
                        "4、全球消费宏利分润\n" +
                        "1）直推分润:6%、7%、8%、9%、\n" +
                        "2）团购累计分润:\n" +
                        "0～1万美金5%\n" +
                        "1～5万美金10%\n" +
                        "5～10万美金15%\n" +
                        "10～20万美金20%\n" +
                        "20～30万美金25%\n" +
                        "3）全球宏利分润（共计10%）:\n" +
                        "2支30万美金消费团  2%加权\n" +
                        "3支30万美金消费团  2%加权\n" +
                        "4支30万美金消费团  2%加权\n" +
                        "5支30万美金消费团  2%加权\n" +
                        "8支30万美金消费团  2%加权\n" +
                        "4）个人贡献分润（全球消费额加权分润、共计5%）:\n");
                startActivity(intent);
            }
        });
    }

    private String userInfoId;


    @Override
    public void onResume() {
        super.onResume();

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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean isTitleShow() {
        return false;
    }


    private List<ZhiBo_GuanZhongBean.DataBean> cooks = new ArrayList<>();


    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }


}

