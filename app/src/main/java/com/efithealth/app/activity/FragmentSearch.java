package com.efithealth.app.activity;

import com.efithealth.R;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class FragmentSearch extends BaseFragment {

	private boolean flag = false;
	private String title_back_m = "搜索结果页";
	private TextView title_id_tv;

	private View view;
	private Handler mHandler = new Handler();

	private RelativeLayout home_title_rl;
	private LinearLayout home_title_ll;

	private WebView webview;
	private ImageView search_titel_image2, search_titel_image;
	private TextView searchId;
	private LinearLayout btn_search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search, null);
		FragmentHome.flag=false;
		return view;
	}
	
	@Override
	public void onDestroyView() {
		FragmentHome.flag=true;
		super.onDestroyView();
	}

	// js调用借口类
	public class jsToAndroid extends BaseJsToAndroid {
		public jsToAndroid(Context context) {
			super(context);
		}
		
		@JavascriptInterface
		public void personalInfo(){
			Intent intent = new Intent();
			intent.setClass(getActivity(), MyInfo.class);
			startActivity(intent);
		}

		
		/**
		 * 根据打开页面控制android UI修改
		 * 
		 * @param title
		 * @param isback
		 * @param btn
		 * @param navbar
		 */
		@JavascriptInterface
		public void loadPageData(final String title, String isback, String btn, String navbar) {
			Log.i("html", title);
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					title_back_m = title;
					title_id_tv.setText(title);
					//////////////////////////////////////////////////
					if (title.equals("我")) {
						MainActivity.instance.setTabColor(3);
					}
					if (title.equals("搜索结果页")) {
						home_title_rl.setVisibility(View.VISIBLE);
						home_title_ll.setVisibility(View.GONE);
						flag = true;

					} else {
						flag = false;

					}
				}
			});
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;

		webview = (WebView) view.findViewById(R.id.webview_search111);
		webview.loadUrl("file:///android_asset/search.html");
		webview.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		webview.getSettings().setJavaScriptEnabled(true);
		// //back键设置
		webview.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
						Log.i("title", title_back_m);
						if (title_back_m.equals("搜索页")) {
							MainActivity.instance.returnBack();
							return true;

						} else if (title_back_m.equals("搜索结果页")) {
							webview.goBack();
							home_title_rl.setVisibility(View.GONE);
							home_title_ll.setVisibility(View.VISIBLE);

							return true;
						} else if (title_back_m.equals("我")) {
							System.exit(0);
						} else {

							webview.goBack();
							return true;
						}

					}
				}

				return false;
			}
		});

		// 在js中调用本地java方法
		webview.addJavascriptInterface(new jsToAndroid(getActivity()), "mobile");
		// 添加客户端支持
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				Log.i("title", title);
				title_back_m = title;
				title_id_tv.setText(title);
			}
		});
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 页面加载完成 通过js回调函数实现获取页面信息
				view.loadUrl("javascript:window.mobile.loadPageData("
						+ "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
			}

		});

		title_id_tv = (TextView) view.findViewById(R.id.searchId3);
		search_titel_image2 = (ImageView) view.findViewById(R.id.search_titel_image2);
		search_titel_image2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i("title", title_back_m);
				if (flag) {
					webview.loadUrl("file:///android_asset/search.html");
					home_title_rl.setVisibility(View.GONE);
					home_title_ll.setVisibility(View.VISIBLE);
				} else {

					webview.goBack();
				}

			}
		});
		searchId = (EditText) view.findViewById(R.id.searchId2);
		//
		home_title_ll = (LinearLayout) view.findViewById(R.id.home_title_ll);
		home_title_ll.setVisibility(View.VISIBLE);
		home_title_rl = (RelativeLayout) view.findViewById(R.id.home_title_rl);
		home_title_rl.setVisibility(View.GONE);
		//
		// 搜索后退监听
		search_titel_image = (ImageView) view.findViewById(R.id.search_titel_image);
		search_titel_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();

			}
		});

		btn_search = (LinearLayout) view.findViewById(R.id.btn_search111);
		btn_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.d("debug", "btn_search click");
				String str = searchId.getText().toString();
				webview.loadUrl("javascript:search('" + str + "')");
			}

		});

	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(getView().getWindowToken());
			if (title_back_m.equals("搜索结果页")) {
				flag = true;
			}

		}
	}

}
