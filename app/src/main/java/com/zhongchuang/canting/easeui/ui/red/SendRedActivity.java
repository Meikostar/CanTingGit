package com.zhongchuang.canting.easeui.ui.red;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
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

public class SendRedActivity extends BaseAllActivity implements OtherContract.View, SelectPopupWindow.OnPopWindowClickListener {


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
    @BindView(R.id.et_peak_message)
    EditText etPeakMessage;
    @BindView(R.id.ll_peak_amount_layout)
    LinearLayout llPeakAmountLayout;
    @BindView(R.id.tv_amount_for_show)
    TextView tvAmountForShow;
    @BindView(R.id.btn_putin)
    Button btnPutin;
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
    private ImageView ivClose;
    private OtherPresenter presenter;

    private SelectPopupWindow menuWindow;


    public void initViews() {
        setContentView(R.layout.activity_send_red);
        ButterKnife.bind(this);

        menuWindow = new SelectPopupWindow(this, this);

        presenter = new OtherPresenter(this);

         tvRecord.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(SendRedActivity.this, RedOut_In_DetailActivity.class);
                 startActivity(intent);
             }
         });
    }

    private View views = null;
    private int chat_type=1;

    public void showPopwindow() {

        views = View.inflate(this, R.layout.pay_type_item, null);
        tvLel= views.findViewById(R.id.tv_lel);
        tvQk= views.findViewById(R.id.tv_qk);
        tvZb= views.findViewById(R.id.tv_zb);
        tvCz= views.findViewById(R.id.tv_cz);

        ivType1= views.findViewById(R.id.iv_type1);
        ivType2= views.findViewById(R.id.iv_type2);
        ivType3= views.findViewById(R.id.iv_type3);
        ivType4= views.findViewById(R.id.iv_type4);
        ivClose= views.findViewById(R.id.iv_close);
        llCz= views.findViewById(R.id.ll_cz);
        llLel= views.findViewById(R.id.ll_lel);
        llQk= views.findViewById(R.id.ll_qk);
        llZb= views.findViewById(R.id.ll_zb);


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


    }
    private  MarkaBaseDialog dialog;
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

    public void bindEvents() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etPeakAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtil.isNotEmpty(s.toString())) {
                    tvAmountForShow.setText(s.toString() + "积分");
                } else {
                    tvAmountForShow.setText("");
                }
            }
        });
        btnPutin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyBoard();
                if (TextUtil.isEmpty(etPeakAmount.getText().toString())) {
                    showToasts(getString(R.string.qsrjfs));
                    return;
                }
                if (!CanTingAppLication.isSetting) {
                    showToasts(getString(R.string.qszzfmm));
                    Intent intent = new Intent(SendRedActivity.this, PaySettingActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    return;
                }

              showPop();
            }
        });
    }

    public void showPop(){
        menuWindow.setAumount(etPeakAmount.getText().toString());
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
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
            presenter.sendRed(etPeakAmount.getText().toString(), "1", chat_type+"", SpUtil.getUserInfoId(SendRedActivity.this),
                    (TextUtil.isEmpty(etPeakMessage.getText().toString()) ? getString(R.string.djdl) : etPeakMessage.getText().toString()), "2");
        } else {
            dimessProgress();
            RedInfo infos = (RedInfo) entity;
            if (infos == null) {
                return;
            }
            RedPacketInfo info = new RedPacketInfo();
            info.type = "1";
            info.userInfoId = SpUtil.getUserInfoId(this);
            info.send_cout = "1";
            info.leavMessage = TextUtil.isEmpty(etPeakMessage.getText().toString()) ? getString(R.string.gxfc) : etPeakMessage.getText().toString();
            info.sendType = "2";
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
        dimessProgress();
        showToasts(msg);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {

        if (complete) {
            menuWindow.setAumount("");
            showProgress("");
            presenter.verifyPassword(psw);
        }

    }

    @Override
    public void onPopWindowTypeListener(String type) {
        if(type.equals("1")){
            menuWindow.dismiss();
            showPopwindow();
        }else {
            Intent intent = new Intent(SendRedActivity.this, ChangPaySmsPActivity.class);
            startActivity(intent);

        }

    }


}
