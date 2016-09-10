package com.efithealth.app.activity;

import com.efithealth.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ClubListActivity extends BaseActivity{
	private WebView clubListId;
	private String memid;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_clublist);
		clubListId = (WebView) findViewById(R.id.clubListId);
		clubListId.loadUrl("file:///android_asset/clubList.html");
		clubListId.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		clubListId.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		clubListId.addJavascriptInterface(new JsInterface(this), "mobile");
		// 添加客户端支持
		clubListId.setWebChromeClient(new WebChromeClient());
		clubListId.getSettings().setDefaultTextEncodingName("utf-8");   
		clubListId.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				clubListId.loadUrl(url);
				return true;
			}
		});
	}
	private class JsInterface {
		private Context mContext;
		private WebView webView;

		public JsInterface(Context context) {
			this.mContext = context;
		}

		// 在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
		@JavascriptInterface
		public String getmemid() {
			SharedPreferences sharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
        	// 使用getString方法获得value，注意第2个参数是value的默认值
        	memid=sharedPreferences.getString("memid", ""); 
			return memid;
		}
		@JavascriptInterface
		public void closeview() {
//			webView.loadUrl("javascript:showInfoFromJava('" + name+":"+memid + "')");
//			webView.loadUrl("file:///android_asset/"+url);
			finish();
		}
	}
}
