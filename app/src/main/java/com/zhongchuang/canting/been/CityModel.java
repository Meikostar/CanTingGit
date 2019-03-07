package com.zhongchuang.canting.been;

import java.util.List;

/**
 * 选择地区时内容地级市实体
 */
public class CityModel {
	private String cityCode;
	private String cityName;
	private List<DistrictModel> areaList;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<DistrictModel> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<DistrictModel> areaList) {
		this.areaList = areaList;
	}
}
