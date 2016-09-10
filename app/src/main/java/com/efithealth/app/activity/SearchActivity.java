package com.efithealth.app.activity;

import com.efithealth.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

@SuppressLint("JavascriptInterface")
public class SearchActivity extends BaseActivity {
	private WebView webview;
	private Button btn_search;
	private EditText searchId;
	private ImageView titel_iv;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle arg0) {
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_search);
		
		

		webview = (WebView) findViewById(R.id.webview_search);
		webview.loadUrl("file:///android_asset/search.html");
		webview.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		webview.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		webview.addJavascriptInterface(new jsToAndroid_search(), "mobile");
		// 添加客户端支持
		webview.setWebChromeClient(new WebChromeClient());
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				webview.loadUrl(url);
				return true;
			}
		});
		
		searchId = (EditText) findViewById(R.id.searchId);
		titel_iv = (ImageView) findViewById(R.id.search_titel_image);
		titel_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("debug", "titel_iv click");
				SearchActivity.this.finish();
			}
		});
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Log.d("debug", "btn_search click");
				
				webview.loadUrl("javascript:search('"+searchId.getText().toString()+"')");
			}
		});
		
	}

	private class jsToAndroid_search {
		
		
		
		

	}
}
