package com.zhongchuang.canting.activity.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.been.ProvinceModel;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MCheckBox;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.NormalNameValueItem;
import com.zhongchuang.canting.widget.RegionSelectBindDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class NewAddressActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tpl_title)
    NavigationBar navigationBar;
    @BindView(R.id.nnvi_name)
    NormalNameValueItem nnviName;
    @BindView(R.id.nnvi_phone)
    NormalNameValueItem nnviPhone;
    @BindView(R.id.nnvi_area)
    NormalNameValueItem nnviArea;
    @BindView(R.id.nnvi_detail)
    NormalNameValueItem nnviDetail;
    @BindView(R.id.check_set_default)
    MCheckBox checkSetDefault;
    private String mCurrentProviceName = "广东";
    private String mCurrentCityName = "中山";
    private String mCurrentDistrictName = "古镇镇";
    private AddressBase addressBase;
    private BasesPresenter presenter;

    @Override
    public void initViews() {
        setContentView(R.layout.layout_activity_new_delivery_address);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        addressBase = (AddressBase) getIntent().getSerializableExtra("data");
        if (CanTingAppLication.province == null) {
            Observable.create(new Observable.OnSubscribe<JSONObject>() {

                @Override
                public void call(Subscriber<? super JSONObject> subscriber) {
                    JSONObject json = TextUtil.getJson("city.json", NewAddressActivity.this);
                    subscriber.onNext(json);
                    subscriber.onCompleted();//结束异步任务

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject json) {
                            JSONObject dataJson = json.optJSONObject("data");
                            CanTingAppLication.province = new Gson().fromJson(dataJson.toString(), ProvinceModel.class);

                        }
                    });

        }
    }

    private String addressId;

    @Override
    public void bindEvents() {
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {

                if (TextUtil.isEmpty(nnviName.getContent())) {
                    showToasts(getString(R.string.qsrshr));
                    return;
                }
                if (TextUtil.isEmpty(nnviPhone.getContent())) {
                    showToasts(getString(R.string.qsrsjh));

                    return;
                }
                if (TextUtil.isEmpty(nnviArea.getContent())) {
                    showToasts(getString(R.string.qxzdq));

                    return;
                }
                if (TextUtil.isEmpty(nnviDetail.getContent())) {
                    showToasts(getString(R.string.qsrxxdz));

                    return;
                }
                if (nnviPhone.getContent().length() < 11) {
                    showToasts(getString(R.string.sjhmcdbd));
                    return;
                }
                if (!StringUtil.isMobileNO(nnviPhone.getContent())) {
                    showToasts(getString(R.string.qsrzqdsjhm));

                    return;
                }
                showProgress(getString(R.string.tjz));
                presenter.addUserAddress(addressId, nnviName.getContent(), nnviPhone.getContent(), nnviArea.getContent(), nnviDetail.getContent(), checkSetDefault.isCheck() ? "1" : "0");

            }

            @Override
            public void navigationimg() {

            }
        });
        nnviArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressSelector();
            }
        });

    }

    public static final int TYPE_NEW = 1;
    public static final int TYPE_EDIT = 2;
    public static final int FROM_SELECT = 3;
    public static final int FROM_MANAGER = 4;
    public static final int from = 4;

    @Override
    public void initData() {
        if (addressBase != null) {
            if(addressBase.is_default.equals("true")){
                checkSetDefault.setChecked(true);
            }else {
                checkSetDefault.setChecked(false);
            }
            addressId = addressBase.address_id;
            String[] split = addressBase.detailed_address.split(",");
            if (split != null && split.length == 3) {
                mCurrentProviceName = split[0];
                mCurrentCityName = split[1];
                mCurrentDistrictName = split[2];
            } else if (split != null && split.length == 2) {
                mCurrentProviceName = split[0];
                mCurrentCityName = split[1];
                mCurrentDistrictName = split[1];
            }
            navigationBar.setNaviTitle(getString(R.string.bjshdz));
            initAddressView(addressBase);
        } else {
            checkSetDefault.setChecked(true);
            navigationBar.setNaviTitle(getString(R.string.xzshdz));
        }


    }

    private void initAddressView(AddressBase mAddress) {
        nnviName.setContent(mAddress.shipping_name);
        nnviPhone.setContent(mAddress.link_number);
        nnviArea.setContent(mAddress.shipping_address);
        nnviDetail.setContent(mAddress.detailed_address);
        if (mAddress.is_default.equals("1")) {
            checkSetDefault.setChecked(true);
        }
    }

    private RegionSelectBindDialog mSelectBindDialog;

    public void showAddressSelector() {
        closeKeyBoard();
        if (CanTingAppLication.province == null) {
            return;
        }
        if (mSelectBindDialog == null) {
            mSelectBindDialog = new RegionSelectBindDialog(this, mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
            mSelectBindDialog.setBindClickListener(new RegionSelectBindDialog.BindClickListener() {
                @Override
                public void site(String provinces, String citys, String districts) {
                    String area = "";
                    if (StringUtil.isNotEmpty(provinces)) {
                        area = provinces;
                    }
                    if (StringUtil.isNotEmpty(citys)) {
                        area = area + " ," + citys;
                    }
                    if (StringUtil.isNotEmpty(districts)) {
                        area = area + " ," + districts;
                    }
                    nnviArea.setContent(area.trim());
                }
            });
        }
        mSelectBindDialog.show();
    }





    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (addressBase != null) {
            showToasts(getString(R.string.bjcg));
        } else {
            showToasts(getString(R.string.tjcg));
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

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
