package com.zhongchuang.canting.activity;


import android.app.Activity;

import com.zhongchuang.canting.base.BaseActivity;

import java.util.Stack;



/**
 * 功能描述:管理app 中activity
 * 作者:chenwei
 * 时间:2016/12/16
 * 版本:
 */
public class MActivityManager {
	
   private static MActivityManager instance;
   
   //private Map<String,BaseActivity> actMap;
	private Stack<Activity> activitys;

	private BaseActivity topbaseActivity;
   
   private MActivityManager()
   {
 
   }
   
   public synchronized static MActivityManager getInstance()
   {
	   if(instance==null)
	   {
		   instance=new MActivityManager();
		  
	   }
	   return instance;
   }

	public void addACT(Activity act){
		if (activitys == null) {
			activitys = new Stack<>();
		}
		activitys.push(act);
	}


	public void delACT(Activity act){
		if(activitys==null)
			return;
		if (activitys.size()>0) {
			for (int i = 0; i < activitys.size(); i++) {
				Object activity = activitys.get(i);
				if (activity.equals(act)) {
					try {
						activitys.remove(i);
						((Activity)activity).finish();
					}catch (Exception e){}
				}
			}
		}else {
			return;
		}
	}

	public void delACT(String name) {
		if(activitys==null)
			return;
		if (activitys.size()>0) {
			for (int i = 0; i < activitys.size(); i++) {
				Object activity = activitys.get(i);
				if (activity.getClass().getName().equals(name)) {
					((Activity)activity).finish();
					activitys.remove(i);
				}
			}
		}else {
			return;
		}
	}

	public void delACT(Class<? extends Activity> name) {
		if(activitys==null)
			return;
		if (activitys.size()>0) {
			for (int i = 0; i < activitys.size(); i++) {
				Object activity = activitys.get(i);
				if (activity.getClass().getName().equals(name.getName())) {
					((Activity)activity).finish();
					activitys.remove(i);
				}
			}
		}else {
			return;
		}
	}

	public void delAllACTWithoutCurrent(){
		if (activitys!=null && activitys.size()>0) {
			while (activitys.size()>0) {
				Object activity = activitys.pop();
				((Activity)activity).finish();
			}
		}else {
			return;
		}
	}

	public Activity getTopbaseActivity(){
		return activitys.peek();
	}

}
