package com.zhongchuang.canting.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.utils.TextUtil;

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

    private String urls;
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
        webviewWebView.getSettings().setDomStorageEnabled(true);        argee.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webviewWebView.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                urls=request.getUrl().toString();
                if (urls.startsWith("http://") || urls.startsWith("https://")) { //加载的url是http/https协议地址
                    view.loadUrl(urls);
                    return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走

                } else { //加载的url是自定义协议地址

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WebView", "onPageStarted : " + url);
                if (!TextUtil.isEmpty(url)) {
                    super.onPageStarted(view,url,favicon);
                    Log.d("WebView","Handle url with system~~");
                    return;
                } else {
                    // Do your special things
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView","onPageFinished : " + url);
                if (!TextUtil.isEmpty(url)) {
                    super.onPageFinished(view,url);
                    Log.d("WebView","Handle url with system~~");
                    return;
                } else {
                    // Do your special things
                }
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webviewWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // TODO: 2017-5-6 处理下载事件
                downloadByBrowser(url);
            }
        });

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

    private String url="";
    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
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
