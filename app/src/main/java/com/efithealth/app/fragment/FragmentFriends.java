package com.efithealth.app.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.efithealth.R;
import com.efithealth.app.activity.FragmentFind;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.fragment.FragmentNearbyPerson.jsToAndroid_Find;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentFriends extends BaseFragment{
	private View v;
	private WebView wb;
	public static boolean flag_find=false;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v=inflater.inflate(R.layout.fragment_friends, container,false);
		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		wb=(WebView) v.findViewById(R.id.find_friends_wb);
		wb.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		wb.addJavascriptInterface(
				new jsToAndroid_Find(getActivity()), "mobile");

		// 去掉webview滚动条
		wb.setHorizontalScrollBarEnabled(false);// 水平不显示
		wb.setVerticalScrollBarEnabled(false); // 垂直不显示

		// 添加客户端支持

		// 设置进度条
		wb.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
//				if (newProgress == 100) {
//					// 隐藏进度条
//					swipeLayout.setRefreshing(false);
//				} else {
//					if (!swipeLayout.isRefreshing())
//						swipeLayout.setRefreshing(true);
//				}

				super.onProgressChanged(view, newProgress);
			}
		});

		wb.getSettings().setDefaultTextEncodingName("utf-8");
		wb.loadUrl("file:///android_asset/friendDynList.html");
		// 设置WebView中的客户端的行为
		wb.setWebViewClient(new WebViewClient() {
			// 让WebView对点击网页中的URL做出响应
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url,
					Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 页面加载完成 通过js回调函数实现获取页面信息
				view.loadUrl("javascript:window.mobile.loadPageData("
						+ "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	
	public class jsToAndroid_Find extends BaseJsToAndroid {

		public jsToAndroid_Find(Context context) {
			super(context);
		}

		@JavascriptInterface
		public void imgView(final String urls, final int position) {

			new Handler().post(new Runnable() {

				@Override
				public void run() {
					List<String> list = new ArrayList<String>();

					String[] urlArr = urls.split("&");
					for (int i = 0; i < urlArr.length; i++) {

						list.add(urlArr[i]);
						Log.i("djy", position + "----------" + urlArr[i]);
					}
					Intent intent = new Intent(getActivity(),
							ImagePagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);
					bundle.putSerializable("urls", (Serializable) list);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});

		}
	}

	@Override
	public void onResume() {
		if (flag_find) {
			String id = (String) SharedPreferencesUtils.getParam(getActivity(),
					"dynid_find", "");
			String fr = (String) SharedPreferencesUtils.getParam(getActivity(),
					"fr_find", "");
			if (fr.equals("friend")) {
				wb.loadUrl("javascript:refreshDyn('" + id + "')");
				Log.i("djy", "onResume          " + id + "            " + fr);
			}
			SharedPreferencesUtils
					.deleteSharedData(getActivity(), "dynid_find");
			SharedPreferencesUtils.deleteSharedData(getActivity(), "fr_find");
			flag_find = false;
		}
		super.onResume();
	}
}
