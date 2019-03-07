package com.zhongchuang.canting.been;

import java.util.List;

public class ProvinceModel {
	private String version;
	private String provinceCode;
	private String provinceName;
	private List<CityModel> cityList;
	private List<ProvinceModel> provinceCityList;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	public List<ProvinceModel> getProvinceCityList() {
		return provinceCityList;
	}

	public void setProvinceCityList(List<ProvinceModel> provinceCityList) {
		this.provinceCityList = provinceCityList;
	}
}
