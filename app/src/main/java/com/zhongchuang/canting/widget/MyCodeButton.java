package com.zhongchuang.canting.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MyCodeButton extends Button implements OnClickListener {
	private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
	private String textafter = "s后再次获取";
	private String textbefore = "获取验证码";
	private final String TIME = "time";
	private final String CTIME = "ctime";
	private OnClickListener mOnclickListener;
	private Timer t;
	private TimerTask tt;
	private long time;
	private boolean isStrat = false;
	Map<String, Long> map = new HashMap<String, Long>();

	public MyCodeButton(Context context) {
		super(context);
		setOnClickListener(this);

	}

	public MyCodeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MyCodeButton.this.setText(time / 1000 + textafter);
			time -= 1000;
			if (time < 0) {
				reSet();
			}
		};
	};

	private void initTimer() {
		time = lenght;
		t = new Timer();
		tt = new TimerTask() {

			@Override
			public void run() {
				han.sendEmptyMessage(0x01);
			}
		};
	}


	private void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (t != null)
			t.cancel();
		t = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof MyCodeButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}

	@Override
	public void onClick(View v) {
		if (mOnclickListener != null)
			mOnclickListener.onClick(v);
		if (isStrat){
			initTimer();
			this.setText(time / 1000 + textafter);
			this.setEnabled(false);
			t.schedule(tt, 0, 1000);
		}

		// t.scheduleAtFixedRate(task, delay, period);
	}

	/**
	 * 和activity的onDestroy()方法同步
	 */
	public void onDestroy() {
		if (MyApp.map == null)
			MyApp.map = new HashMap<String, Long>();
		MyApp.map.put(TIME, time);
		MyApp.map.put(CTIME, System.currentTimeMillis());
		clearTimer();
	}

	/**
	 * 和activity的onCreate()方法同步
	 */
	public void onCreate(Bundle bundle) {

		if (MyApp.map == null)
			return;
		if (MyApp.map.size() <= 0)// 这里表示没有上次未完成的计时
			return;
		long time = System.currentTimeMillis() - MyApp.map.get(CTIME)
				- MyApp.map.get(TIME);
		MyApp.map.clear();
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			t.schedule(tt, 0, 1000);
			this.setText(time + textafter);
			this.setEnabled(false);
		}
	}

	/** * 设置计时时候显示的文本 */
	public MyCodeButton setTextAfter(String text1) {
		this.textafter = text1;
		return this;
	}

	/** * 设置点击之前的文本 */
	public MyCodeButton setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
		return this;
	}

	/**
	 * 设置到计时长度
	 * 
	 * @param lenght
	 *            时间 默认毫秒
	 * @return
	 */
	public MyCodeButton setLenght(long lenght) {
		this.lenght = lenght;
		return this;
	}

	public boolean isStrat() {
		return isStrat;
	}

	public void setIsStrat(boolean isStrat) {
		this.isStrat = isStrat;
	}

	public void reSet(){
        MyCodeButton.this.setEnabled(true);
        isStrat = false;
        MyCodeButton.this.setText(textbefore);
        clearTimer();
    }




}