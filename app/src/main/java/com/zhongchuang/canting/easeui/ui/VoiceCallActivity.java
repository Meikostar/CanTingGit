/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhongchuang.canting.easeui.ui;

import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.BannerAdapterss;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.util.UUID;

import butterknife.BindView;

/**
 * 语音通话页面
 * 
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener , BaseContract.View {

	private ImageView refuseBtn;
	private ImageView answerBtn;
	private ImageView muteImage;
	private ImageView handsFreeImage;

	private boolean isMuteState;
	private boolean isHandsfreeState;
	
	private TextView callStateTextView;
	private boolean endCallTriggerByMe = false;
	private Chronometer chronometer;
	String st1;
	private LinearLayout ll_bg2;
	private LinearLayout ll_bg1;
	private LinearLayout ll_bg3;
	private LinearLayout ll_bg4;
    private TextView netwrokStatusVeiw;
    private boolean monitor = false;
    private BasesPresenter presenter;
    private BannerAdapterss bannerAdapter;
   public BannerView bannerView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
        	finish();
        	return;
        }
		setContentView(R.layout.em_activity_voice_call);
		
		DemoHelper.getInstance().isVoiceCalling = true;
		callType = 0;

		refuseBtn = (ImageView) findViewById(R.id.btn_refuse_call);
		answerBtn = (ImageView) findViewById(R.id.btn_answer_call);
        bannerView = (BannerView) findViewById(R.id.bannerView);

		muteImage = (ImageView) findViewById(R.id.iv_mute);
		handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        TextView nickTextView = (TextView) findViewById(R.id.tv_nick);
        TextView durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
        ll_bg2 = (LinearLayout) findViewById(R.id.ll_bg2);
        ll_bg1 = (LinearLayout) findViewById(R.id.ll_bg1);
        ll_bg3 = (LinearLayout) findViewById(R.id.ll_bg3);
        ll_bg4 = (LinearLayout) findViewById(R.id.ll_bg4);
		netwrokStatusVeiw = (TextView) findViewById(R.id.tv_network_status);

		refuseBtn.setOnClickListener(this);
		answerBtn.setOnClickListener(this);
		muteImage.setOnClickListener(this);
		handsFreeImage.setOnClickListener(this);
        presenter = new BasesPresenter(this);
        presenter.getBanners(3 + "");
        bannerAdapter = new BannerAdapterss(this);
        bannerView.setAdapter(bannerAdapter);


        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {
//                Banner banner= (Banner) o;
//                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
//                intent.putExtra("id", banner.product_sku_id);
//                intent.putExtra("type", 2);
//                startActivity(intent);
            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		addCallStateListener();
		msgid = UUID.randomUUID().toString();

		username = getIntent().getStringExtra("username");

		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
        chatmessage= (CHATMESSAGE) getIntent().getSerializableExtra(EaseConstant.EXTRA_CHATMSG);
        if(chatmessage!=null){
            fava = chatmessage.getUserinfo().hx_favater;
            name = chatmessage.getUserinfo().hx_fname;
            callExt="";
            callExt=chatmessage.getUserinfo().hx_fuid+","+chatmessage.getUserinfo().hx_fname+","+ (TextUtil.isEmpty(chatmessage.getUserinfo().hx_favater)?"":chatmessage.getUserinfo().hx_favater);
        }else {
            callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
        }

		nickTextView.setText(username);
		if (!isInComingCall) {// outgoing call
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.em_outgoing, 1);


			ll_bg4.setVisibility(View.GONE);
			ll_bg3.setVisibility(View.VISIBLE);
			ll_bg1.setVisibility(View.VISIBLE);
			ll_bg2.setVisibility(View.VISIBLE);
			st1 = getResources().getString(R.string.Are_connected_to_each_other);
			callStateTextView.setText(st1);
			handler.sendEmptyMessage(MSG_CALL_MAKE_VOICE);
            handler.postDelayed(new Runnable() {
                public void run() {
                    streamID = playMakeCallSounds();
                }
            }, 300);
        } else { // incoming call

            ll_bg2.setVisibility(View.GONE);
            ll_bg1.setVisibility(View.GONE);
			Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);
	}

	/**
	 * set call state listener
	 */
	void addCallStateListener() {
	    callStateListener = new EMCallStateChangeListener() {
            
            @Override
            public void onCallStateChanged(CallState callState, final CallError error) {
                // Message msg = handler.obtainMessage();
                EMLog.d("EMCallManager", "onCallStateChanged:" + callState);
                switch (callState) {
                
                case CONNECTING:
                    runOnUiThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            callStateTextView.setText(st1);
                        }
                    });
                    break;
                    case CONNECTED:
                    start=  System.currentTimeMillis();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String st3 ="已经和对方建立连接";
                            callStateTextView.setText(st3);
                        }
                    });
                    break;

                case ACCEPTED:
                    handler.removeCallbacks(timeoutHangup);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (soundPool != null)
                                    soundPool.stop(streamID);
                            } catch (Exception e) {
                            }
                            if(!isHandsfreeState)
                                closeSpeakerOn();
                            //show relay or direct call, for testing purpose
                            ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                    ? R.string.direct_call : R.string.relay_call);
                            chronometer.setVisibility(View.VISIBLE);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            // duration start
                            chronometer.start();
                            String str4 = getResources().getString(R.string.In_the_call);
                            callStateTextView.setText(str4);
                            callingState = CallingState.NORMAL;
                            startMonitor();
                        }
                    });
                    break;
                case NETWORK_UNSTABLE:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            netwrokStatusVeiw.setVisibility(View.VISIBLE);
                            if(error == CallError.ERROR_NO_DATA){
                                netwrokStatusVeiw.setText(R.string.no_call_data);
                            }else{
                                netwrokStatusVeiw.setText(R.string.network_unstable);
                            }
                        }
                    });
                    break;
                case NETWORK_NORMAL:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            netwrokStatusVeiw.setVisibility(View.INVISIBLE);
                        }
                    });
                    break;
                case VOICE_PAUSE:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "VOICE_PAUSE", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case VOICE_RESUME:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "VOICE_RESUME", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case DISCONNECTED:
                    handler.removeCallbacks(timeoutHangup);
                     fError = error;
                    end=System.currentTimeMillis();
                    if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stops();
                                    }
                                });
                            }
                        }, 25000);
                    }else {
                        if(start>0&&!isInComingCall){
                            presenter.addChatDetail(username,1+"",start+"",end+"");
                        }else {
                            stops();
                        }
                    }

                    break;

                default:
                    break;
                }

            }
        };
		EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
	}
    private long start;
    private long end;
	public  EMCallStateChangeListener.CallError fError;
    public void stops(){
        runOnUiThread(new Runnable() {
            private void postDelayedCloseMsg() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("AAA", "CALL DISCONNETED");
                                removeCallStateListener();
                                saveCallRecord();

                                finish();
                            }
                        });
                    }
                }, 200);
            }

            @Override
            public void run() {
                chronometer.stop();
                callDruationText = chronometer.getText().toString();
                String st1 = getResources().getString(R.string.Refused);
                String st2 = getResources().getString(R.string.The_other_party_refused_to_accept);
                String st3 = getResources().getString(R.string.Connection_failure);
                String st4 = getResources().getString(R.string.The_other_party_is_not_online);
                String st5 = getResources().getString(R.string.The_other_is_on_the_phone_please);

                String st6 = getResources().getString(R.string.The_other_party_did_not_answer_new);
                String st7 = getResources().getString(R.string.hang_up);
                String st8 = getResources().getString(R.string.The_other_is_hang_up);

                String st9 = getResources().getString(R.string.did_not_answer);
                String st10 = getResources().getString(R.string.Has_been_cancelled);
                String st11 = getResources().getString(R.string.hang_up);

                if (fError == EMCallStateChangeListener.CallError.REJECTED) {
                    callingState = CallingState.BEREFUSED;
                    callStateTextView.setText(st2);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                    callStateTextView.setText(st3);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                    callingState = CallingState.OFFLINE;
                    callStateTextView.setText(st4);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                    callingState = CallingState.BUSY;
                    callStateTextView.setText(st5);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                    callingState = CallingState.NO_RESPONSE;
                    callStateTextView.setText(st6);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == EMCallStateChangeListener.CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED){
                    callingState = CallingState.VERSION_NOT_SAME;
                    callStateTextView.setText(R.string.call_version_inconsistent);
                } else {
                    if (isRefused) {
                        callingState = CallingState.REFUSED;
                        callStateTextView.setText(st1);
                    }
                    else if (isAnswered) {
                        end=System.currentTimeMillis();
                        callingState = CallingState.NORMAL;
                        if (endCallTriggerByMe) {
//                                        callStateTextView.setText(st7);
                        } else {
                            callStateTextView.setText(st8);
                        }
                    } else {
                        if (isInComingCall) {
                            callingState = CallingState.UNANSWERED;
                            callStateTextView.setText(st9);
                        } else {
                            if (callingState != CallingState.NORMAL) {
                                callingState = CallingState.CANCELLED;
                                callStateTextView.setText(st10);
                            }else {
                                callStateTextView.setText(st11);
                            }
                        }
                    }
                }
                postDelayedCloseMsg();
            }

        });
    }
    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }
   private int state=0;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refuse_call:
		    if(state==0){
                isRefused = true;
                refuseBtn.setEnabled(false);
                handler.sendEmptyMessage(MSG_CALL_REJECT);
            }else {
                refuseBtn.setEnabled(false);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(R.string.hanging_up));
                handler.sendEmptyMessage(MSG_CALL_END);
            }

			break;

		case R.id.btn_answer_call:
		    answerBtn.setEnabled(false);
		    closeSpeakerOn();
            callStateTextView.setText(R.string.zcjts);
            state=1;

            ll_bg4.setVisibility(View.GONE);
            ll_bg3.setVisibility(View.VISIBLE);
            ll_bg2.setVisibility(View.VISIBLE);
            ll_bg1.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(MSG_CALL_ANSWER);
			break;


		case R.id.iv_mute:
			if (isMuteState) {
				muteImage.setImageResource(R.drawable.em_icon_mute_normal);
                try {
                    EMClient.getInstance().callManager().resumeVoiceTransfer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
				isMuteState = false;
			} else {
				muteImage.setImageResource(R.drawable.em_icon_mute_on);
                try {
                    EMClient.getInstance().callManager().pauseVoiceTransfer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
				isMuteState = true;
			}
			break;
		case R.id.iv_handsfree:
			if (isHandsfreeState) {
				handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
				closeSpeakerOn();
				isHandsfreeState = false;
			} else {
				handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
				openSpeakerOn();
				isHandsfreeState = true;
			}
			break;
		default:
			break;
		}
	}
	
    @Override
    protected void onDestroy() {
        DemoHelper.getInstance().isVoiceCalling = false;
        stopMonitor();
        super.onDestroy();
    }

	@Override
	public void onBackPressed() {
		callDruationText = chronometer.getText().toString();
	}

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor(){
        monitor = true;
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                ? R.string.direct_call : R.string.relay_call);
                    }
                });
                while(monitor){
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "CallMonitor").start();
    }

    void stopMonitor() {

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==22){
            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);
        }else {
            stops();
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        stops();
    }
}
