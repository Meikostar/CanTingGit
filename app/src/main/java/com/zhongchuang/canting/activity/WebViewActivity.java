package com.zhongchuang.canting.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseTitle_Activity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewActivity extends BaseTitle_Activity {

    public static final String WEBURL = "weburl";
    public static final String WEBTITLE = "webtitle";
    public static final String TYPE = "type";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.navigationBar)
    LinearLayout navigationBar;
    @BindView(R.id.webview_webView)
    WebView webviewWebView;
    @BindView(R.id.web_back)
    ImageView webBack;
    @BindView(R.id.goForward)
    ImageView goForward;
    @BindView(R.id.reload)
    ImageView reload;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.rl_rigter)
    RelativeLayout rlRigter;

    private TextView title;
    private ImageView back;


    private RelativeLayout mRelativeLayoutbg;
    private Button argee;
    private int type = -1;


    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.webview, null);
    }

    @Override
    public boolean isTitleShow() {
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(WEBURL);
        String webTitle = intent.getStringExtra(WEBTITLE);
        type = intent.getIntExtra(TYPE, -1);

        if(!TextUtils.isEmpty(webTitle)){
            tvTitle.setText(webTitle);
        }


        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRelativeLayoutbg = (RelativeLayout) findViewById(R.id.rl_rigter);
        argee = (Button) findViewById(R.id.btn_submit);
        if (type != -1) {
            mRelativeLayoutbg.setVisibility(View.VISIBLE);
        }

        webviewWebView.getSettings().setJavaScriptEnabled(true);
        argee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webviewWebView.setWebViewClient(new WebViewClient() { //通过webView打开链接，不调用系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Toast.makeText(getApplicationContext(), "网络连接失败 ,请连接网络。", Toast.LENGTH_SHORT).show();
            }
        });


        WebSettings webSettings = webviewWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);


        if (null != url) {
            webviewWebView.loadUrl(url);
        }

        webviewWebView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (webviewWebView.canGoBack()) {
                    webviewWebView.goBack();
                } else {

                }
            }
        });



        goForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webviewWebView.goForward();

            }
        });

        reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webviewWebView.reload();
            }
        });


    }


    @Override
    protected void onDestroy() {
        webviewWebView.destroy();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webviewWebView.canGoBack()) {
                webviewWebView.goBack();
            } else {
                finish();
            }
        }
        return true;
    }
}
