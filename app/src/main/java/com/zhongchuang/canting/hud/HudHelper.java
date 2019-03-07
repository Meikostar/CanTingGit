package com.zhongchuang.canting.hud;

import android.content.Context;



public class HudHelper {

	private KProgressHUD hud;
//	private HudHelper helper;

	public static HudHelper getInstance(){
//		if (helper==null) {
		HudHelper	helper = new HudHelper();
//		}
		return helper;
	}
	

	public void showPress(Context mContext) {
		if (hud==null) {
			hud =  KProgressHUD.create(mContext);
//		    hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
//			hud.setWindowColor(mContext.getResources().getColor(R.color.b_transtlant));
		}
		if (!hud.isShowing()){
			hud.show();
		}
	}

	public void showPress(Context mContext,String label) {
		if (hud==null) {
			hud =  KProgressHUD.create(mContext);
//		    hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
//			hud.setWindowColor(mContext.getResources().getColor(R.color.b_transtlant));
		}
		hud.setLabel(label);
		if (!hud.isShowing()){
			hud.show();
		}
	}
	
	public void hide() {
		if (hud!=null) {
			if (hud.isShowing()) {
				hud.dismiss();
			}
		}
	}
	
}
