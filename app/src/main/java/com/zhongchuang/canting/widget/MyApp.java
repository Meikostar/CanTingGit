package com.zhongchuang.canting.widget;

import java.util.Map;

import android.app.Application;

public class MyApp extends Application {


	public static Map<String, Long> map;

	public static Map<String, Long> getMap() {
		return map;
	}

	public static void setMap(Map<String, Long> map) {
		MyApp.map = map;
	}
}
