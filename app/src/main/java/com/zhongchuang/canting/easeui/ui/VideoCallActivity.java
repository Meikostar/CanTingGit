/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhongchuang.canting.easeui.ui;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallManager.EMCameraDataProcessor;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMVideoCallHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.hyphenate.util.EMLog;
import com.superrtc.sdk.VideoView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.BannerAdaps;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.io.File;
import java.util.Date;
import java.util.UUID;


public class VideoCallActivity extends CallActivity implements OnClickListener, BaseContract.View {

    private boolean isMuteState;
    private boolean isHandsfreeState;
    private boolean isAnswered;
    private boolean endCallTriggerByMe = false;
    private boolean monitor = true;

    // 视频通话画面显示控件，这里在新版中使用同一类型的控件，方便本地和远端视图切换
    protected EMCallSurfaceView localSurface;
    protected EMCallSurfaceView oppositeSurface;
    private int surfaceState = -1;

    private TextView callStateTextView;


    private ImageView refuseBtn;
    private ImageView answerBtn;
    private ImageView muteImage;
    private TextView iv_type;
    private ImageView handsFreeImage;
    private TextView nickTextView;
    private Chronometer chronometer;
    private RelativeLayout rootContainer;
    private LinearLayout topContainer;
    private LinearLayout bottomContainer;
    private TextView monitorTextView;
    private TextView netwrokStatusVeiw;
    private BannerView bannerView;
    private BasesPresenter presenter;
    private LinearLayout ll_bg2;
    private LinearLayout ll_bg1;
    private LinearLayout ll_bg3;
    private LinearLayout ll_bg4;
    private RelativeLayout rl_bg;
    private Handler uiHandler;

    private boolean isInCalling;

    boolean isRecording = false;
    //    private Button recordBtn;
    private EMVideoCallHelper callHelper;
    private Button toggleVideoBtn;

    private BrightnessDataProcess dataProcessor = new BrightnessDataProcess();

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 22) {
            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);

        } else {
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

    // dynamic adjust brightness
    class BrightnessDataProcess implements EMCameraDataProcessor {
        byte yDelta = 0;

        synchronized void setYDelta(byte yDelta) {
            Log.d("VideoCallActivity", "brigntness uDelta:" + yDelta);
            this.yDelta = yDelta;
        }

        // data size is width*height*2
        // the first width*height is Y, second part is UV
        // the storage layout detailed please refer 2.x demo CameraHelper.onPreviewFrame
        @Override
        public synchronized void onProcessData(byte[] data, Camera camera, final int width, final int height, final int rotateAngel) {
            int wh = width * height;
            for (int i = 0; i < wh; i++) {
                int d = (data[i] & 0xFF) + yDelta;
                d = d < 16 ? 16 : d;
                d = d > 235 ? 235 : d;
                data[i] = (byte) d;
            }
        }
    }

    private BannerAdaps bannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.em_activity_video_call);

        DemoHelper.getInstance().isVideoCalling = true;
        callType = 1;

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        uiHandler = new Handler();
        presenter = new BasesPresenter(this);
        presenter.getBanners(1 + "");


        rootContainer = findViewById(R.id.root_layout);
        refuseBtn = findViewById(R.id.btn_refuse_call);
        answerBtn = findViewById(R.id.btn_answer_call);

        muteImage = findViewById(R.id.iv_mute);
        iv_type = findViewById(R.id.iv_type);
        handsFreeImage = findViewById(R.id.iv_handsfree);
        callStateTextView = findViewById(R.id.tv_call_state);
        nickTextView = findViewById(R.id.tv_nick);
        chronometer = findViewById(R.id.chronometer);
        ll_bg2 = findViewById(R.id.ll_bg2);
        ll_bg1 = findViewById(R.id.ll_bg1);
        ll_bg3 = findViewById(R.id.ll_bg3);
        ll_bg4 = findViewById(R.id.ll_bg4);
        RelativeLayout btnsContainer = findViewById(R.id.ll_btns);
        rl_bg = findViewById(R.id.rl_bg);
        topContainer = findViewById(R.id.ll_top_container);
        bottomContainer = findViewById(R.id.ll_bottom_container);
        monitorTextView = findViewById(R.id.tv_call_monitor);
        netwrokStatusVeiw = findViewById(R.id.tv_network_status);
        bannerView = findViewById(R.id.bannerView);
//        recordBtn = (Button) findViewById(R.id.btn_record_video);
        ImageView switchCameraBtn = findViewById(R.id.btn_switch_camera);
        Button captureImageBtn = findViewById(R.id.btn_capture_image);
        SeekBar YDeltaSeekBar = findViewById(R.id.seekbar_y_detal);
        bannerAdapter = new BannerAdaps(this);
        refuseBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);

        muteImage.setOnClickListener(this);
        iv_type.setOnClickListener(this);
        handsFreeImage.setOnClickListener(this);
        rootContainer.setOnClickListener(this);
//        recordBtn.setOnClickListener(this);
        switchCameraBtn.setOnClickListener(this);
        captureImageBtn.setOnClickListener(this);
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
        YDeltaSeekBar.setOnSeekBarChangeListener(new YDeltaSeekBarListener());

        msgid = UUID.randomUUID().toString();
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
        username = getIntent().getStringExtra("username");

        chatmessage = (CHATMESSAGE) getIntent().getSerializableExtra(EaseConstant.EXTRA_CHATMSG);
        if (chatmessage != null) {
            fava = chatmessage.getUserinfo().hx_favater;
            name = chatmessage.getUserinfo().hx_fname;
            callExt = "";
            callExt = chatmessage.getUserinfo().hx_fuid + "," + chatmessage.getUserinfo().hx_fname + "," + (TextUtil.isEmpty(chatmessage.getUserinfo().hx_favater) ? "zx" : chatmessage.getUserinfo().hx_favater);
        } else {
            callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
            if(TextUtil.isNotEmpty(callExt)){
                String[] split = callExt.split(",");
                if(split!=null&&split.length==3){
                    name=split[1];
                }
            }
        }
        if (!TextUtils.isEmpty(name)) {
            nickTextView.setText(name);
        } else {
            nickTextView.setText(username);
        }

        // local surfaceview
        localSurface = findViewById(R.id.local_surface);
        localSurface.setOnClickListener(this);
        localSurface.setZOrderMediaOverlay(true);
        localSurface.setZOrderOnTop(true);

        // remote surfaceview
        oppositeSurface = findViewById(R.id.opposite_surface);

        // set call state listener
        addCallStateListener();
        if (!isInComingCall) {// outgoing call
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);


            ll_bg4.setVisibility(View.GONE);
            ll_bg3.setVisibility(View.VISIBLE);
            ll_bg1.setVisibility(View.VISIBLE);
            ll_bg2.setVisibility(View.VISIBLE);
            String st = getResources().getString(R.string.Are_connected_to_each_other);
            callStateTextView.setText(st);
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
            handler.sendEmptyMessage(MSG_CALL_MAKE_VIDEO);
            handler.postDelayed(new Runnable() {
                public void run() {
                    streamID = playMakeCallSounds();
                }
            }, 300);
        } else { // incoming call

            callStateTextView.setText("Ringing");
            if (EMClient.getInstance().callManager().getCallState() == EMCallStateChangeListener.CallState.IDLE
                    || EMClient.getInstance().callManager().getCallState() == EMCallStateChangeListener.CallState.DISCONNECTED) {
                // the call has ended
                finish();
                return;
            }
            ll_bg2.setVisibility(View.GONE);
            ll_bg1.setVisibility(View.GONE);
            localSurface.setVisibility(View.INVISIBLE);
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
        }

        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);

        // get instance of call helper, should be called after setSurfaceView was called
        callHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        EMClient.getInstance().callManager().setCameraDataProcessor(dataProcessor);
    }

    class YDeltaSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            dataProcessor.setYDelta((byte) (20.0f * (progress - 50) / 50.0f));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallView() {
        if (surfaceState == 0) {
            surfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(oppositeSurface, localSurface);
        } else {
            surfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
        }
    }

    private int state;
    private long start;
    private long end;
    private CountDownTimer countDownTimer1;
    /**
     * set call state listener
     */
    void addCallStateListener() {
        callStateListener = new EMCallStateChangeListener() {

            @Override
            public void onCallStateChanged(final CallState callState, final CallError error) {
                switch (callState) {

                    case CONNECTING: // is connecting
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                rl_bg.setVisibility(View.GONE);
                                callStateTextView.setText(R.string.Are_connected_to_each_other);
                            }

                        });
                        break;
                    case CONNECTED: // connected

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                rootContainer.setBackground(null);
                                localSurface.setVisibility(View.VISIBLE);

                                rl_bg.setVisibility(View.GONE);
//                            callStateTextView.setText(R.string.have_connected_with);
                            }

                        });
                        break;

                    case ACCEPTED: // call is accepted
                        surfaceState = 0;
                        handler.removeCallbacks(timeoutHangup);
//                        countDownTimer1 = new CountDownTimer(1000*60*60*36, 1000) {
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//                                chatTime=millisUntilFinished;
//                            }
//
//                            @Override
//                            public void onFinish() {
//
//                                countDownTimer1.cancel();
//                            }
//                        }.start();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (soundPool != null)
                                        soundPool.stop(streamID);
                                    EMLog.d("EMCallManager", "soundPool stop ACCEPTED");
                                } catch (Exception e) {
                                }
                                openSpeakerOn();
                                ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                        ? R.string.direct_call : R.string.relay_call);
                                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                                isHandsfreeState = true;
                                isInCalling = true;
                                rl_bg.setVisibility(View.GONE);
                                chronometer.setVisibility(View.VISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                // call durations start
                                chronometer.start();
                                nickTextView.setVisibility(View.INVISIBLE);
                                callStateTextView.setText(R.string.In_the_call);
//                            recordBtn.setVisibility(View.VISIBLE);
                                callingState = CallingState.NORMAL;
                                startMonitor();
                            }

                        });
                        break;
                    case NETWORK_DISCONNECTED:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.VISIBLE);
                                netwrokStatusVeiw.setText(R.string.network_unavailable);
                            }
                        });
                        break;
                    case NETWORK_UNSTABLE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.VISIBLE);
                                if (error == CallError.ERROR_NO_DATA) {
                                    netwrokStatusVeiw.setText(R.string.mythsj);
                                } else {
                                    netwrokStatusVeiw.setText(R.string.wlbwd);
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
                    case VIDEO_PAUSE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VIDEO_PAUSE", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case VIDEO_RESUME:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VIDEO_RESUME", Toast.LENGTH_SHORT).show();
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
                    case DISCONNECTED: // call is disconnected
                        handler.removeCallbacks(timeoutHangup);
                        fError = error;
                        if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
//                            chronometer.start();
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
                        } else {
                            if (start > 0) {
                                end = System.currentTimeMillis();
                                if (status == 1) {
                                    presenter.addChatDetail(username, 2 + "", start + "", end + "");
                                } else {
                                    stops();
                                }

                            } else {
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
    private long chatTime;
    public EMCallStateChangeListener.CallError fError;

    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }

    public void stops() {
        runOnUiThread(new Runnable() {
            private void postDelayedCloseMsg() {
                uiHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        removeCallStateListener();
                        if(countDownTimer1!=null){
                            countDownTimer1.cancel();
                        }
                        saveCallRecord();
                        Animation animation = new AlphaAnimation(1.0f, 0.0f);
                        animation.setDuration(1200);
                        rootContainer.startAnimation(animation);
                        finish();
                    }

                }, 200);
            }

            @Override
            public void run() {
                chronometer.stop();
                callDruationText = chronometer.getText().toString();
                String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
                String s2 = getResources().getString(R.string.Connection_failure);
                String s3 = getResources().getString(R.string.The_other_party_is_not_online);
                String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
                String s5 = getResources().getString(R.string.The_other_party_did_not_answer);

                String s6 = getResources().getString(R.string.hang_up);
                String s7 = getResources().getString(R.string.The_other_is_hang_up);
                String s8 = getResources().getString(R.string.did_not_answer);
                String s9 = getResources().getString(R.string.Has_been_cancelled);
                String s10 = getResources().getString(R.string.Refused);

                if (fError == EMCallStateChangeListener.CallError.REJECTED) {
                    callingState = CallingState.BEREFUSED;
                    callStateTextView.setText(s1);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                    callStateTextView.setText(s2);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                    callingState = CallingState.OFFLINE;
                    callStateTextView.setText(s3);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                    callingState = CallingState.BUSY;
                    callStateTextView.setText(s4);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                    callingState = CallingState.NO_RESPONSE;
                    callStateTextView.setText(s5);
                } else if (fError == EMCallStateChangeListener.CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == EMCallStateChangeListener.CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                    callingState = CallingState.VERSION_NOT_SAME;
                } else {
                    if (isRefused) {
                        callingState = CallingState.REFUSED;
                        callStateTextView.setText(s10);
                    } else if (isAnswered) {
                        end = System.currentTimeMillis();
                        callingState = CallingState.NORMAL;
                        if (endCallTriggerByMe) {
//                                        callStateTextView.setText(s6);
                        } else {
                            callStateTextView.setText(s7);
                        }
                    } else {
                        if (isInComingCall) {
                            callingState = CallingState.UNANSWERED;
                            callStateTextView.setText(s8);
                        } else {
                            if (callingState != CallingState.NORMAL) {
                                callingState = CallingState.CANCELLED;
                                callStateTextView.setText(s9);
                            } else {
                                callStateTextView.setText(s6);
                            }
                        }
                    }
                }
                Toast.makeText(VideoCallActivity.this, callStateTextView.getText(), Toast.LENGTH_SHORT).show();
                postDelayedCloseMsg();
            }

        });
    }

    private int status;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_surface:
                changeCallView();
                break;
            case R.id.iv_type:
                if (status == 0) {
                    Toast.makeText(getApplicationContext(), R.string.ykqmfyjf, Toast.LENGTH_LONG).show();
                    status = 1;
                    start = System.currentTimeMillis();
                    iv_type.setText(R.string.gbs);

                    bannerView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.ggygbnbnmf, Toast.LENGTH_LONG).show();
                    status = 0;
                    iv_type.setText(R.string.zjfs);
                    bannerView.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_refuse_call: // decline the call\
                if (state == 0) {
                    isRefused = true;
                    refuseBtn.setEnabled(false);
                    handler.sendEmptyMessage(MSG_CALL_REJECT);
                } else {
                    refuseBtn.setEnabled(false);
                    chronometer.stop();
                    endCallTriggerByMe = true;
                    callStateTextView.setText(getResources().getString(R.string.hanging_up));
                    if (isRecording) {
//                        callHelper.stopVideoRecord();
                    }
                    EMLog.d(TAG, "btn_hangup_call");
                    handler.sendEmptyMessage(MSG_CALL_END);
                }

                break;

            case R.id.btn_answer_call: // answer the call
                EMLog.d(TAG, "btn_answer_call clicked");
                state = 1;
                answerBtn.setEnabled(false);
                openSpeakerOn();
                if (ringtone != null)
                    ringtone.stop();

                callStateTextView.setText("answering...");
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                isAnswered = true;
                isHandsfreeState = true;


                ll_bg4.setVisibility(View.GONE);
                ll_bg3.setVisibility(View.VISIBLE);
                ll_bg2.setVisibility(View.VISIBLE);
                ll_bg1.setVisibility(View.VISIBLE);
                localSurface.setVisibility(View.VISIBLE);
                break;


            case R.id.iv_mute: // mute
                if (isMuteState) {
                    // resume voice transfer
                    muteImage.setImageResource(R.drawable.em_icon_mute_normal);
                    try {
                        EMClient.getInstance().callManager().resumeVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = false;
                } else {
                    // pause voice transfer
                    muteImage.setImageResource(R.drawable.em_icon_mute_on);
                    try {
                        EMClient.getInstance().callManager().pauseVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree: // handsfree
                if (isHandsfreeState) {
                    // turn off speaker
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
        /*
        case R.id.btn_record_video: //record the video
            if(!isRecording){
//                callHelper.startVideoRecord(PathUtil.getInstance().getVideoPath().getAbsolutePath());
                callHelper.startVideoRecord("/sdcard/");
                EMLog.d(TAG, "startVideoRecord:" + PathUtil.getInstance().getVideoPath().getAbsolutePath());
                isRecording = true;
                recordBtn.setText(R.string.stop_record);
            }else{
                String filepath = callHelper.stopVideoRecord();
                isRecording = false;
                recordBtn.setText(R.string.recording_video);
                Toast.makeText(getApplicationContext(), String.format(getString(R.string.record_finish_toast), filepath), Toast.LENGTH_LONG).show();
            }
            break;
        */
            case R.id.root_layout:
                if (callingState == CallingState.NORMAL) {
                    if (bottomContainer.getVisibility() == View.VISIBLE) {
                        bottomContainer.setVisibility(View.GONE);
                        topContainer.setVisibility(View.GONE);
                        oppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);

                    } else {
                        bottomContainer.setVisibility(View.VISIBLE);
                        topContainer.setVisibility(View.VISIBLE);
                        oppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFit);
                    }
                }
                break;
            case R.id.btn_switch_camera: //switch camera
                handler.sendEmptyMessage(MSG_CALL_SWITCH_CAMERA);
                break;
            case R.id.btn_capture_image:
                DateFormat df = new DateFormat();
                Date d = new Date();
                File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                final String filename = storage.getAbsolutePath() + "/" + DateFormat.format("MM-dd-yy--h-mm-ss", d) + ".jpg";
//                EMClient.getInstance().callManager().getVideoCallHelper().takePicture(filename);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoCallActivity.this, "saved image to:" + filename, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DemoHelper.getInstance().isVideoCalling = false;
        stopMonitor();
        if (isRecording) {
//            callHelper.stopVideoRecord();
            isRecording = false;
        }
        localSurface.getRenderer().dispose();
        localSurface = null;
        oppositeSurface.getRenderer().dispose();
        oppositeSurface = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        callDruationText = chronometer.getText().toString();
        super.onBackPressed();
    }

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor() {
        monitor = true;
        new Thread(new Runnable() {
            public void run() {
                while (monitor) {
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            monitorTextView.setText("WidthxHeight："+callHelper.getVideoWidth()+"x"+callHelper.getVideoHeight()
//                                    + "\nDelay：" + callHelper.getVideoLatency()
//                                    + "\nFramerate：" + callHelper.getVideoFrameRate()
//                                    + "\nLost：" + callHelper.getVideoLostRate()
//                                    + "\nLocalBitrate：" + callHelper.getLocalBitrate()
//                                    + "\nRemoteBitrate：" + callHelper.getRemoteBitrate());

                            ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                    ? R.string.direct_call : R.string.relay_call);
                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "CallMonitor").start();
    }

    void stopMonitor() {
        monitor = false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (isInCalling) {
            try {
                EMClient.getInstance().callManager().pauseVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInCalling) {
            try {
                EMClient.getInstance().callManager().resumeVideoTransfer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

}
