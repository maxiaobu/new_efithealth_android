package com.efithealth.app.activity;

import com.efithealth.R;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我
 * @author star
 *
 */
public class FragmentMy extends BaseFragment {
	public static boolean flag_my=false;
	public View view;
	public WebView searchId;
	public ImageView back, button;
	public TextView set;
	public Handler mHandler;
	public int isback_test = 0;
	private String m_url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my, container, false);
	}

	@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;
		m_url = "file:///android_asset/personal.html";

		//btn_class_manage = (Button) getView().findViewById(R.id.btn_class_manage);
		// btn_class_manage.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// MainActivity.instance.onTabClicked(btn_class_manage);
		// }
		// });

		searchId = (WebView) getView().findViewById(R.id.webview);
		back = (ImageView) getView().findViewById(R.id.back);
		button = (ImageView) getView().findViewById(R.id.button);
		set = (TextView) getView().findViewById(R.id.set);
		searchId.loadUrl(m_url);
		searchId.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		searchId.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		searchId.addJavascriptInterface(new jsToAndroid(getActivity()), "mobile");
		// 添加客户端支持
		searchId.setWebChromeClient(new WebChromeClient());
		searchId.getSettings().setDefaultTextEncodingName("utf-8");
		set.setText("我");
		searchId.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				searchId.loadUrl(url);

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
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isback_test == 1) {

					searchId.goBackOrForward(-2);
				} else {
					searchId.goBack();
				}

			}
		});

		// 非UI线程发消息给UI线程，UI线程去修改UI
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundleData = msg.getData();

				set.setText(bundleData.get("title").toString());
				if (bundleData.get("back").toString().equals("1")) {
					back.setVisibility(View.VISIBLE);
					isback_test = 0;
				} else if (bundleData.get("back").toString().equals("2")) {
					isback_test = 1;
					back.setVisibility(View.VISIBLE);
				}else if (bundleData.get("isback").toString().equals("close")) {
				back.setVisibility(View.VISIBLE);
				} 
				else if (bundleData.get("isback").toString().equals("close")) {
				back.setVisibility(View.VISIBLE);
				} else {
					isback_test = 0;
					back.setVisibility(View.INVISIBLE);
				}
				
				if (bundleData.get("btn").toString().equals("1")) {
					button.setVisibility(View.VISIBLE);
				} else {
					button.setVisibility(View.INVISIBLE);
				}
				
				if (bundleData.get("navbar").toString().equals("1")) {
					MainActivity.instance.hideTitleBar(1);
				} else {
					MainActivity.instance.hideTitleBar(0);
				}
				
				if (bundleData.get("navbar").toString().equals("1")) {
					MainActivity.instance.hideTitleBar(1);
				} else {
					MainActivity.instance.hideTitleBar(0);
				}
			}
		};

	}

	// js调用借口类
	public class jsToAndroid extends BaseJsToAndroid {
		
		public jsToAndroid(Context context) {
			super(context);
		}

		//储存首页顶部是 天气 还是 课程预约 的标记
		@JavascriptInterface
		public void shared(String head){
			Log.i("frag", "my  "+head);
			SharedPreferencesUtils.setParam(getActivity(), "headpage", head);
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
		public void loadPageData(String title, String isback, String btn, String navbar) {

			Message msg = new Message();
			Bundle bundleData = new Bundle();
			bundleData.putString("title", title);
			bundleData.putString("back", isback);
			bundleData.putString("btn", btn);
			bundleData.putString("navbar", navbar);
			bundleData.putString("isback", isback);
			msg.setData(bundleData);
			mHandler.sendMessage(msg);
		}

		/**
		 * 个人信息
		 * 
		 * @param memid
		 */
		@JavascriptInterface
		public void modifyMemberInfo() {
			Intent intent = new Intent();
			intent.setClass(getActivity(), MyInfo.class);
			startActivity(intent);
		}
	}

	/**
	 * 获取是否为主页
	 * 
	 * @return
	 */
	public boolean getWebViewUrl() {
		String url = searchId.getUrl();
		if (url.equals(m_url)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onResume() {
		if (flag_my) {
			searchId.loadUrl("javascript:initData()");
		}
		Log.i("djy","onResume1");
		super.onResume();
	}

	public void refrush() {
		searchId.reload();

	}
	public void refrush1() {
		searchId.loadUrl(m_url);

	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(getView().getWindowToken());
		}
	}
	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}














