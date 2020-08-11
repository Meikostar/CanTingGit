package com.zhongchuang.canting.been;

import java.io.Serializable;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by rzb on 2019/7/9.
 */
public class CityDto implements IndexableEntity, Serializable {
    private String cityName;
    private String cityCode;
    public String id;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String getFieldIndexBy() {
        return cityName;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.cityName = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
    }
}
