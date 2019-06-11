package com.zhongchuang.canting.utils.location;

/**
 * 获取当前位置
 */

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MyLocationData;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.utils.SpUtil;


public class LocationUtil implements BDLocationListener {
    private static String TAG = "LocationUtil";

    LocationClient mLocClient;
    private static LocationUtil instance;
    private Context mContext;
    private boolean isLocated = false;
    private long mLastStartLocationTime;
    /** 纬度
     */
    public static double latitude = 22.64573;
    /***
     * 经度
     */
    public static double longitude = 114.014616;
    /**
     * 定位的用户信息
     * */
    public static String province = "广东";
    public static String city = "";
    public static MyLocationData location;

    /**
     * 定义重新定位的时候回调接口
     */
    public interface ReLocationListenner {
        void relocation();
    }

    private ReLocationListenner mRelocationListener;

    public void setRelocationListerner(ReLocationListenner relocationListener) {
        mRelocationListener = relocationListener;
    }

    public static LocationUtil shareInstance() {
        return instance;
    }

    public static void initUtil(Context context) {
        SDKInitializer.initialize(context);
        instance = new LocationUtil(context);
    }

    private LocationUtil(Context context) {
        mContext = context;
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);

    }

    public void startLocation() {
        if (!isLocated
                || mLastStartLocationTime <= (System.currentTimeMillis() - 300000L)) {
            mLastStartLocationTime = System.currentTimeMillis();
            mLocClient.start();
        } else {
            if (mRelocationListener != null) {
                mRelocationListener.relocation();
            }
        }
    }

    public void stopLocation() {
        mLocClient.stop();
    }

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location.getLocType() != 61 && location.getLocType() != 161) {

//            ToastUtils.showNormalToast("失败");
            // toast.show();暂时不显示定位失败
            isLocated = false;
        } else {
             LocationUtil.location = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            SpUtil.putString(mContext, "la", location.getLatitude()+"");
            SpUtil.putString(mContext, "lg", location.getLongitude()+"");
            SpUtil.putString(mContext, "adress", location.getAddress().address);

//            ToastUtils.showNormalToast(location.getAddress().address);
            LocationUtil.latitude = location.getLatitude();
            LocationUtil.longitude = location.getLongitude();
            province = location.getProvince();
            city = location.getCity();

            isLocated = true;
            mLocClient.stop();
            if (mRelocationListener != null) {
                mRelocationListener.relocation();
            }
        }
    }
}
