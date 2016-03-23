package com.michaelallan.android.redditgrabber;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Myko on 3/23/2016.
 */
public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_webview);

        mWebView = (WebView) findViewById(R.id.redditWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        Log.i(TAG, "onCreate: link is: " + getIntent().getExtras().getString(RedditGrabberFragment.LINK_URL));
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl(getIntent().getExtras().getString(RedditGrabberFragment.LINK_URL));
    }



}
