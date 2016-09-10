package com.efithealth.app.activity;

import com.efithealth.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class RegisterAgreeActivity extends Activity {
	private WebView webView;

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "InlinedApi" })
	@Override
	protected void onCreate(Bundle arg0) {
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_registeragree);
		webView = (WebView) findViewById(R.id.webview);	
		
		webView.loadUrl("file:///android_asset/registerAgree.html");
		webView.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		webView.getSettings().setJavaScriptEnabled(true);			
		// 在js中调用本地java方法
		webView.addJavascriptInterface(new jsToAndroid(), "mobile");
		// 添加客户端支持
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setDefaultTextEncodingName("utf-8");	
		
	}
	public class jsToAndroid {
		@JavascriptInterface
		public void registerCancel(){
			 Intent intent = new Intent();            
             intent.putExtra("result", "cancle");
             setResult(RESULT_OK, intent);
             finish();
		}
		@JavascriptInterface
		public void registerAgree(){
			Intent intent = new Intent();            
            intent.putExtra("result", "agree");
            setResult(RESULT_OK, intent);
            finish();
		}
	}
}
