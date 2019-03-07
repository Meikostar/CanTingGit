package com.zhongchuang.canting.easeui.ui.red;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.pay.ChangPaySmsPActivity;
import com.zhongchuang.canting.activity.pay.PaySettingActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.easeui.bean.RedPacketInfo;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MCheckBox;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.pswpop.SelectPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendRedsActivity extends BaseAllActivity implements OtherContract.View, SelectPopupWindow.OnPopWindowClickListener {


    ImageView ivClose;
    TextView tvLel;
    MCheckBox ivType1;
    LinearLayout llLel;
    TextView tvZb;
    MCheckBox ivType2;
    LinearLayout llZb;
    TextView tvQk;
    MCheckBox ivType3;
    LinearLayout llQk;
    TextView tvCz;
    MCheckBox ivType4;
    LinearLayout llCz;
    TextView tvSure;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.et_peak_amount)
    EditText etPeakAmount;
    @BindView(R.id.ll_peak_num_layout)
    LinearLayout llPeakNumLayout;
    @BindView(R.id.tv_group_member_num)
    TextView tvGroupMemberNum;
    @BindView(R.id.tv_peak_amount_icon)
    TextView tvPeakAmountIcon;
    @BindView(R.id.et_peak_num)
    EditText etPeakNum;
    @BindView(R.id.ll_peak_amount_layout)
    LinearLayout llPeakAmountLayout;
    @BindView(R.id.tv_peak_type)
    TextView tvPeakType;
    @BindView(R.id.et_peak_message)
    EditText etPeakMessage;
    @BindView(R.id.tv_amount_for_show)
    TextView tvAmountForShow;
    @BindView(R.id.btn_putin)
    Button btnPutin;
    private OtherPresenter presenter;


    private SelectPopupWindow menuWindow;
    private String group_id;

    public void initViews() {
        setContentView(R.layout.activity_sends_red);
        ButterKnife.bind(this);
        group_id = (String) getIntent().getStringExtra("id");
        presenter = new OtherPresenter(this);
        menuWindow = new SelectPopupWindow(this, this);
        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendRedsActivity.this, RedOut_In_DetailActivity.class);
                startActivity(intent);
            }
        });
    }


    public void bindEvents() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPutin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtil.isEmpty(etPeakAmount.getText().toString())) {
                    showToasts(getString(R.string.qsrjfs));
                    return;
                }
                if (TextUtil.isEmpty(etPeakNum.getText().toString())) {
                    showToasts(getString(R.string.qsrhbgs));
                    return;
                }
                if (Integer.valueOf(etPeakAmount.getText().toString()) < Integer.valueOf(etPeakNum.getText().toString())) {
                    showToasts(getString(R.string.jfsydyhbgs));
                    return;
                }
                if (!CanTingAppLication.isSetting) {
                    showToasts(getString(R.string.szzfmm));
                    Intent intent = new Intent(SendRedsActivity.this, PaySettingActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    return;
                }
                closeKeyBoard();
                showPop();
            }
        });

    }

    private View views = null;
    private int chat_type = 1;
    private MarkaBaseDialog dialog;

    public void showPopwindow(int type) {
        if(views==null){

            views = View.inflate(this, R.layout.pay_type_item, null);
            tvLel = (TextView) views.findViewById(R.id.tv_lel);
            tvQk = (TextView) views.findViewById(R.id.tv_qk);
            tvZb = (TextView) views.findViewById(R.id.tv_zb);
            tvCz = (TextView) views.findViewById(R.id.tv_cz);
            ivType1 = (MCheckBox) views.findViewById(R.id.iv_type1);
            ivType2 = (MCheckBox) views.findViewById(R.id.iv_type2);
            ivType3 = (MCheckBox) views.findViewById(R.id.iv_type3);
            ivType4 = (MCheckBox) views.findViewById(R.id.iv_type4);
            ivClose = (ImageView) views.findViewById(R.id.iv_close);
            llCz = (LinearLayout) views.findViewById(R.id.ll_cz);
            llLel = (LinearLayout) views.findViewById(R.id.ll_lel);
            llQk = (LinearLayout) views.findViewById(R.id.ll_qk);
            llZb = (LinearLayout) views.findViewById(R.id.ll_zb);


            dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
            dialog.show();
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            llLel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectType(1);
                }
            });
            llZb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectType(2);
                }
            });
            llQk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectType(3);
                }
            });
            llCz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectType(4);
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    menuWindow.setType(chat_type);
                    showPop();
                }
            });
        }else {
            dialog.show();
        }



    }

    public void selectType(int type) {
        chat_type = type;
        if (type == 1) {
            ivType1.setChecked(true);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
        } else if (type == 2) {
            ivType1.setChecked(false);
            ivType2.setChecked(true);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
        } else if (type == 3) {
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(true);
            ivType4.setChecked(false);
        } else if (type == 4) {
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(true);
        }
        dialog.dismiss();
    }

    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 999) {
            presenter.sendRed(etPeakAmount.getText().toString(), etPeakNum.getText().toString(), chat_type + "", group_id,
                    (TextUtil.isEmpty(etPeakMessage.getText().toString()) ?  getString(R.string.djdl) : etPeakMessage.getText().toString()), "1");
        } else {
            dimessProgress();
            RedInfo infos = (RedInfo) entity;
            if (infos == null) {
                return;
            }
            RedPacketInfo info = new RedPacketInfo();
            info.type = "1";
            info.userInfoId = SpUtil.getUserInfoId(this);
            info.send_cout = etPeakNum.getText().toString();
            info.leavMessage = TextUtil.isEmpty(etPeakMessage.getText().toString()) ?  getString(R.string.gxfc) : etPeakMessage.getText().toString();
            info.sendType = "1";
            info.send_jf = etPeakAmount.getText().toString();
            info.redEnvelopeId = infos.redEnvelopeId;
            Intent intent = new Intent();
            intent.putExtra("data", info);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
        dimessProgress();
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            menuWindow.setAumount("");
            showProgress("");
            presenter.verifyPassword(psw);
        }
    }

    public void showPop() {
        menuWindow.setAumount(etPeakAmount.getText().toString());
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowTypeListener(String type) {
        if(type.equals("1")){
            menuWindow.dismiss();
            showPopwindow(chat_type);
        }else {
            Intent intent = new Intent(SendRedsActivity.this, ChangPaySmsPActivity.class);
            startActivity(intent);

        }
    }
}
