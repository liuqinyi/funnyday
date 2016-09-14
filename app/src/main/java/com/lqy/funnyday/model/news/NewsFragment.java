package com.lqy.funnyday.model.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.lqy.funnyday.R;
import com.lqy.funnyday.util.JavaScriptUtil;

/**
 * Created by mrliu on 16-7-18.
 */
public class NewsFragment extends Fragment{
    private Context context;
    private View view;
    private static NewsFragment newsFragment;
    private String label;

    /**
     *  UI of Menu
     * */
    private Toolbar toolbar;
    private Button toolbarBtn;
    private TextView toolbarTitle;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * webview 相关
     */
    private WebView webView;
    private WebSettings webSettings; //设置属性对象

    public static NewsFragment getInstance(String label) {
        newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_news, null);
        context = this.getActivity();

        initRefresh();
        initToolbar();
        initWebView();

        return view;
    }

    private void initRefresh() {
        swipeRefreshLayout  = (SwipeRefreshLayout)view.findViewById(R.id.swipe_contain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                webView.loadUrl(webView.getUrl());
            }
        });
        swipeRefreshLayout.setColorSchemeColors(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
    }

    /**
     * 初始化顶部导航栏
     * */
    private void initToolbar() {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);

        toolbarBtn = (Button) view.findViewById(R.id.toolbar_btn);
        toolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);

        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        webView = (WebView) view.findViewById(R.id.webView);
        webView.requestFocusFromTouch(); //设置获取手势焦点
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //设置webView可执行JS
        webSettings.setDefaultTextEncodingName("UTF-8");
        /**
         * 实例化WebClient对象
         * */
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                view.loadUrl("file:///android_asset/html/error.html");
            }

        };
        /**
         * 实例化WebChromeClient对象
         * 主要用来辅助WebView处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
         * */
        WebChromeClient webChromeClient = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbarTitle.setText(title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    swipeRefreshLayout.setRefreshing(false);
                }else if (!swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        };
        /**
         * 下载监听器
         * */
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        };
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setDownloadListener(downloadListener);
        webView.addJavascriptInterface(new JavaScriptUtil(),"Android"); //为JS调用Android方法声明，JS通过Android调用JavaScript内方法
        webView.loadUrl("http://news.163.com/");

    }
}
