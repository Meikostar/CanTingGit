package com.zhongchuang.canting.easeui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.MineCodeActivity;
import com.zhongchuang.canting.activity.PersonMessageActivity;
import com.zhongchuang.canting.activity.WebViewActivity;
import com.zhongchuang.canting.activity.chat.ChatHandActivity;
import com.zhongchuang.canting.activity.chat.QfriendActivity;
import com.zhongchuang.canting.activity.chat.RecordDetailActivity;
import com.zhongchuang.canting.activity.chat.SumbitJfActivity;
import com.zhongchuang.canting.activity.mall.SettingActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleImageView;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.Custom_ProfileOrderClickBtn;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/12/4.
 */

public class MessageMineFragment extends LazyFragment implements BaseContract.View {


    @BindView(R.id.person_pic)
    CircleImageView personPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.btn_care)
    Custom_ProfileOrderClickBtn btnCare;
    @BindView(R.id.btn_cares)
    Custom_ProfileOrderClickBtn btnCares;
    @BindView(R.id.tv_all)
    Custom_ProfileOrderClickBtn tvAll;
    @BindView(R.id.tv_jfs)
    TextView tvJfs;
    @BindView(R.id.ll_total)
    RelativeLayout llTotal;
    @BindView(R.id.rl_set)
    RelativeLayout rlSet;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rl_tell)
    RelativeLayout rlTell;
    @BindView(R.id.img_3)
    ImageView img3;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.imgs)
    ImageView imgs;
    @BindView(R.id.ll_qf)
    RelativeLayout llQf;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.message_mine, container, false);
        ButterKnife.bind(this, viewRoot);
        initView();
        setLoginMessage();
        return viewRoot;
    }

    private void setLoginMessage() {
        String phone = SpUtil.getString(getActivity(), "mobileNumber", "");
        String token = SpUtil.getString(getActivity(), "token", "");
        String avar = SpUtil.getString(getActivity(), "ava", "");
        String nick = SpUtil.getName(getActivity());
        if (TextUtils.isEmpty(token) || token.equals("") || TextUtils.isEmpty(token) || token.equals("")) {
            phone = getString(R.string.qdl);
        }

        if (personPic != null) {

            Glide.with(getActivity()).load(StringUtil.changeUrl(avar)).asBitmap().transform(new CircleTransform(getActivity())).placeholder(R.drawable.editor_ava).into(personPic);


            tvAbout.setText(phone);
            if (TextUtil.isNotEmpty(nick)) {
                tvName.setText( getString(R.string.ncs)+ nick);
            }
        }

    }

    protected Bundle fragmentArgs;
    private String shopid;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        presenter = new BasesPresenter(this);
        presenter.getUserIntegral();
        presenter.getCircleImage(null);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SumbitJfActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("cout", tvJf.getText().toString());
                startActivity(intent);
            }
        });
        llTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecordDetailActivity.class);

                startActivity(intent);
            }
        });
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MineCodeActivity.class));
            }
        });
        personPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoPersonMessIntent = new Intent(getActivity(), NewPersonDetailActivity.class);
                gotoPersonMessIntent.putExtra("id", SpUtil.getUserInfoId(getActivity()) + "");
                startActivity(gotoPersonMessIntent);
            }
        });
        rlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        btnCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatHandActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        btnCares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatHandActivity.class));
            }
        });
        rlTell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);

                intent.putExtra(WebViewActivity.WEBTITLE, getString(R.string.lljfhqgz));
                intent.putExtra(WebViewActivity.WEBURL, "http://119.23.212.8:8088/editText/index.html");
                startActivity(intent);
            }
        });
        llQf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QfriendActivity.class));

            }
        });

    }

    private BasesPresenter presenter;

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.getUserIntegral();
        }
        setLoginMessage();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("second done destroy");



    }

    @Override
    public void lazyView() {

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==99){
            List<String> data= (List<String>) entity;
            if(data!=null&&data.size()>0){
                if(data.size()==1){
                  img3.setVisibility(View.INVISIBLE);
                  img2.setVisibility(View.INVISIBLE);
                  img1.setVisibility(View.VISIBLE);
                  Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                }else  if(data.size()==2){
                    img3.setVisibility(View.INVISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                    Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(1)).asBitmap().placeholder(R.drawable.moren).into(img2);
                }else  if(data.size()==3){
                    img3.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                    Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(1)).asBitmap().placeholder(R.drawable.moren).into(img2);
                    Glide.with(getActivity()).load(QiniuUtils.baseurl+data.get(2)).asBitmap().placeholder(R.drawable.moren).into(img3);
                }
            }
        }else {
            Ingegebean bean = (Ingegebean) entity;
            if (TextUtil.isNotEmpty(bean.chat_presenter_integral)) {
                tvJf.setText(bean.chat_presenter_integral);
            }
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
