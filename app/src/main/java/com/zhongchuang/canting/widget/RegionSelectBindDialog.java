package com.zhongchuang.canting.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.CityModel;
import com.zhongchuang.canting.been.DistrictModel;
import com.zhongchuang.canting.been.ProvinceModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/29
 * 版本:
 ***/
public class RegionSelectBindDialog {
    private Context mContext;
    private View mView;
    private PopupWindow mPopupWindow;
    private ViewGroup mViewGroup;
    private FrameLayout mFrameLayout;

    private Button mButtonCancel;
    private Button mButtonConfirm;

    private CycleWheelView mCycleWheelViewProvince;
    private CycleWheelView mCycleWheelViewCity;
    private CycleWheelView mCycleWheelViewDistrict;

    /**
     * 所有省
     */
    private List<String> mProvinceDatas = new ArrayList<>();
    /**
     * key - 省 value - 市
     */
    private Map<String, List<String>> mCitisDatasMap = new HashMap<String, List<String>>();
    /**
     * key - 市 values - 区
     */
    private Map<String, List<String>> mDistrictDatasMap = new HashMap<String, List<String>>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    private int mCurrentProviceIndex;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    private int mCurrentCityIndex;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName ="";
    private int mCurrentDistrictIndex;

    /**
     * 当前区的邮政编码
     */
    private String mCurrentZipCode ="";

    private BindClickListener mBindClickListener;
    private boolean haveSetCity = false;
    private boolean haveSetDistrict = false;

    public RegionSelectBindDialog(Context mContext, String s, String c, String x) {
        mCurrentProviceName = s;
        mCurrentCityName = c;
        mCurrentDistrictName = x;
        this.mContext = mContext;
        initView();
    }

    public void setBindClickListener(BindClickListener mBindClickListener){
        this.mBindClickListener=mBindClickListener;
    }
    public interface BindClickListener{
        void site(String province, String city, String district);
    }

    private void initView(){

        mView = View.inflate(mContext, R.layout.view_region_select_bind_dialog,null);
        mViewGroup = (ViewGroup) ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        mCycleWheelViewProvince = (CycleWheelView)mView.findViewById(R.id.cwv_province);
        mCycleWheelViewCity = (CycleWheelView)mView.findViewById(R.id.cwv_city);
        mCycleWheelViewDistrict = (CycleWheelView)mView.findViewById(R.id.cwv_district);
        mButtonCancel = (Button)mView.findViewById(R.id.but_cancel);
        mButtonConfirm = (Button)mView.findViewById(R.id.but_confirm);
        mFrameLayout = (FrameLayout)mView.findViewById(R.id.fl_rsbd_region);
        initNetDatas();
        setProvince();
        setCity();
        setDistrict();
        mButtonCancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                dismiss();
                if(mBindClickListener!=null){
                    mBindClickListener.site(mCycleWheelViewProvince.getSelectLabel(),mCycleWheelViewCity.getSelectLabel(),
                            mCycleWheelViewDistrict.getSelectLabel());
                }
            }
        });
        mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 省份
     */
    private void setProvince(){
        mCycleWheelViewProvince.setLabels(mProvinceDatas);
        try {
            mCycleWheelViewProvince.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        mCycleWheelViewProvince.setLabelSelectSize(14f);
        mCycleWheelViewProvince.setLabelSize(12f);
        mCycleWheelViewProvince.setAlphaGradual(0.7f);
        mCycleWheelViewProvince.setCycleEnable(true);
        mCycleWheelViewProvince.setSelection(mCurrentProviceIndex);
        mCycleWheelViewProvince.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewProvince.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewProvince.setLabelSelectColor(Color.parseColor("#1e90ff"));
        mCycleWheelViewProvince.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewProvince.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                List<String> city = mCitisDatasMap.get(mProvinceDatas.get(position));
                //if (city!=null && city.size()>0) {
                mCycleWheelViewCity.setLabels(city);
                List<String> list = mDistrictDatasMap.get(mCycleWheelViewCity.getSelectLabel());
                //if (list != null && list.size() > 0) {
                //   subscriptions.remove(subscriptions.size()-1);
                mCycleWheelViewDistrict.setLabels(list);
                if (!haveSetCity){
                    haveSetCity = true;
                    mCycleWheelViewCity.setSelection(mCurrentCityIndex);
                }
                // }
                //}
            }
        });
    }

    /**
     * 市
     */
    private void setCity(){
        List<String> city = mCitisDatasMap.get(mProvinceDatas.get(0));
        //if (city!=null && city.size()>0){
        mCycleWheelViewCity.setLabels(city);
        // }
        try {
            mCycleWheelViewCity.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        mCycleWheelViewCity.setAlphaGradual(0.7f);
        mCycleWheelViewCity.setLabelSelectSize(14f);
        mCycleWheelViewCity.setLabelSize(12f);
        mCycleWheelViewCity.setCycleEnable(true);
        mCycleWheelViewCity.setSelection(mCurrentCityIndex);
        mCycleWheelViewCity.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewCity.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewCity.setLabelSelectColor(Color.parseColor("#1e90ff"));
        mCycleWheelViewCity.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewCity.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                List<String> lab= mCycleWheelViewCity.getLabels();
                if(lab!=null && lab.size()>1){
                    List<String> list=mDistrictDatasMap.get(label);
                    //if(list!=null && list.size()>0){
                    //    subscriptions.remove(subscriptions.size()-1);
                    mCycleWheelViewDistrict.setLabels(list);
                    if (!haveSetDistrict){
                        haveSetDistrict = true;
                        mCycleWheelViewDistrict.setSelection(mCurrentDistrictIndex);
                    }
                    // }
                }

            }
        });
    }

    /**
     *区
     */
    private void setDistrict(){
        List<String> list=mDistrictDatasMap.get(mCitisDatasMap.get(mProvinceDatas.get(0)).get(0));
        //list.remove(list.size()-1);
        //if (list!=null && list.size()>0){
        mCycleWheelViewDistrict.setLabels(list);
        //}
        try {
            mCycleWheelViewDistrict.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        mCycleWheelViewDistrict.setLabelSelectSize(14f);
        mCycleWheelViewDistrict.setLabelSize(12f);
        mCycleWheelViewDistrict.setAlphaGradual(0.7f);
        mCycleWheelViewDistrict.setCycleEnable(true);
        mCycleWheelViewDistrict.setSelection(mCurrentDistrictIndex);
        mCycleWheelViewDistrict.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewDistrict.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewDistrict.setLabelSelectColor(Color.parseColor("#1e90ff"));
        mCycleWheelViewDistrict.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewDistrict.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {

            }
        });
    }

    public void show(){
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else {
            //   mPopupWindow.showAsDropDown(parentView,0,0);
            mPopupWindow.showAtLocation(mViewGroup, Gravity.BOTTOM, 0, 0);
        }
    }

    public void dismiss(){
        if (mPopupWindow!=null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void initNetDatas(){
        parseAddressData(CanTingAppLication.province.getProvinceCityList());
    }



    private void parseAddressData(List<ProvinceModel> provinceList) {
        for(int i=0;i<provinceList.size();i++){
            if (!TextUtils.isEmpty(provinceList.get(i).getProvinceName())&&provinceList.get(i).getProvinceName().contains(mCurrentProviceName)){

                mCurrentProviceIndex = i;
            }
            mProvinceDatas.add(i,provinceList.get(i).getProvinceName());
            List<CityModel> cityList = provinceList.get(i).getCityList();
            List<String> cityNames = new ArrayList<>();
            if (cityList!=null && cityList.size()>0){
                for(int j=0;j<cityList.size();j++){
                    if (!TextUtils.isEmpty(cityList.get(j).getCityName())&&cityList.get(j).getCityName().contains(mCurrentCityName)){

                        mCurrentCityIndex = j;
                    }
                    cityNames.add(j,cityList.get(j).getCityName());
                    List<DistrictModel> districtList = cityList.get(j).getAreaList();
                    List<String> distrinctName = new ArrayList<>();
                    if (districtList!=null && districtList.size()>0){
                        for(int k=0;k<districtList.size();k++){
                            distrinctName.add(k,districtList.get(k).getAreaName());
                            if (!TextUtils.isEmpty(districtList.get(k).getAreaName())&&districtList.get(k).getAreaName().contains(mCurrentDistrictName)){

                                mCurrentDistrictIndex = k;
                            }
                        }
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityList.get(j).getCityName(), distrinctName);
                }
            }
            // 省-市的数据，保存到mCitisDatasMap
            mCitisDatasMap.put(provinceList.get(i).getProvinceName(), cityNames);
        }
    }


}
