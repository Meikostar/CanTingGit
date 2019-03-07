package com.zhongchuang.canting.utils.location;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by linquandong on 15/8/17.
 * 百度地理编码工具
 */
public class GeoCoderUtils implements OnGetGeoCoderResultListener {

    private GeoCoder mSearch;
    public String city;
    public String province;

    private GeoCoderUtils(){
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    public void getLocalInfo(LatLng latLng){
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
        }
        //获取地理编码结果
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有找到检索结果
        }
        //获取反向地理编码结果
//        province = reverseGeoCodeResult.getAddress()
    }
}
