package com.efithealth.app.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.fragment.FragmentNearbyPerson;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 发现
 * 
 * @author star
 * 
 */
@SuppressLint({ "SetJavaScriptEnabled", "InflateParams", "NewApi" })
public class FragmentFind extends BaseFragment {
	public static boolean flag_find = false;

	private LinearLayout ll_title;
	private TextView txt_near, txt_friend, txt_hot;
	private ImageView img_publish;
	private ViewPager viewPager;
	private List<View> pageViews = new ArrayList<View>();
	private List<WebView> list_web = new ArrayList<WebView>();
	private int selected_index = 0;
	private String path = "?memid=" + MyApplication.getInstance().getMemid()
			+ "&longitude=" + MyApplication.getInstance().mLongitude
			+ "&latitude=" + MyApplication.getInstance().mLatitude;
	private String[] url = new String[] {
			"file:///android_asset/peopelNearby.html"
					+ MainActivity.instance.getPath(),
			"file:///android_asset/friendDynList.html",
			"file:///android_asset/hotDynList.html" };
	
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.activity_find, container,
				false);
//		 pageViews.clear();
//			list_web.clear();
			ll_title = (LinearLayout) inflate.findViewById(R.id.ll_title);
			txt_near = (TextView) inflate.findViewById(R.id.txt_near);
			txt_friend = (TextView) inflate.findViewById(R.id.txt_friend);
			txt_hot = (TextView) inflate.findViewById(R.id.txt_hot);
			img_publish = (ImageView) inflate.findViewById(R.id.img_publish);
			viewPager = (ViewPager) inflate.findViewById(R.id.viewPager);
			txt_near.setOnClickListener(itemClickListener);
			txt_friend.setOnClickListener(itemClickListener);
			txt_hot.setOnClickListener(itemClickListener);
			img_publish.setOnClickListener(publishClickListener);

			Log.i("path", "find       " + MainActivity.instance.getPath());
			for (int i = 0; i < url.length; i++) {
				// 添加webview
				View web = inflater.inflate(R.layout.item_webview, null);
				final WebView web_load = (WebView) web.findViewById(R.id.web_load);
				final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) web
						.findViewById(R.id.swipe_container);
				// 设置刷新时动画的颜色，可以设置4个
				swipeLayout.setColorScheme(
						android.R.color.holo_blue_light,
						android.R.color.holo_red_light,
						android.R.color.holo_orange_light,
						android.R.color.holo_green_light);
				swipeLayout
						.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

							@Override
							public void onRefresh() {
								// 重新刷新页面
								web_load.loadUrl(web_load.getUrl());
							}
						});
				// 设置WebView支持JavaScript
				web_load.getSettings().setJavaScriptEnabled(true);
				/*** 打开本地缓存提供JS调用 **/
				web_load.getSettings().setDomStorageEnabled(true);
				// Set cache size to 8 mb by default. should be more than enough
				web_load.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
				// This next one is crazy. It's the DEFAULT location for your app's
				// cache
				// But it didn't work for me without this line.
				// UPDATE: no hardcoded path. Thanks to Kevin Hawkins
				String appCachePath = getActivity().getApplicationContext()
						.getCacheDir().getAbsolutePath();
				web_load.getSettings().setAppCachePath(appCachePath);
				web_load.getSettings().setAllowFileAccess(true);
				web_load.getSettings().setAppCacheEnabled(true);
				// 在js中调用本地java方法
				web_load.addJavascriptInterface(
						new jsToAndroid_Find(getActivity()), "mobile");

				// 去掉webview滚动条
				web_load.setHorizontalScrollBarEnabled(false);// 水平不显示
				web_load.setVerticalScrollBarEnabled(false); // 垂直不显示

				// 添加客户端支持

				// 设置进度条
				web_load.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						if (newProgress == 100) {
							// 隐藏进度条
							swipeLayout.setRefreshing(false);
						} else {
							if (!swipeLayout.isRefreshing())
								swipeLayout.setRefreshing(true);
						}

						super.onProgressChanged(view, newProgress);
					}
				});

				web_load.getSettings().setDefaultTextEncodingName("utf-8");
				web_load.loadUrl(url[i]);
				// 设置WebView中的客户端的行为
				web_load.setWebViewClient(new WebViewClient() {
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
				list_web.add(web_load);
				pageViews.add(web);
			}
			viewPager.setAdapter(new GuidePageAdapter());
			viewPager.setOnPageChangeListener(new GuidePageChangeListener());
			selectChange();

		
		return inflate;
	}

	@Override
	public void onResume() {
		
		if (flag_find) {
			String id = (String) SharedPreferencesUtils.getParam(getActivity(),
					"dynid_find1", "");
			String fr = (String) SharedPreferencesUtils.getParam(getActivity(),
					"fr_find1", "");
			if (fr.equals("hot")) {
				list_web.get(2).loadUrl("javascript:refreshDyn('" + id + "')");
				Log.i("djy", "onResume          " + id + "            " + fr);
			} else if (fr.equals("friend")) {
				list_web.get(1).loadUrl("javascript:refreshDyn('" + id + "')");
				Log.i("djy", "onResume          " + id + "            " + fr);
			}
			SharedPreferencesUtils
					.deleteSharedData(getActivity(), "dynid_find1");
			SharedPreferencesUtils.deleteSharedData(getActivity(), "fr_find1");
			flag_find = false;
		}
		super.onResume();
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
			InputMethodManager manager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
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

	// 指引页面数据适配器
	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// 指引页面更改事件监听器
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int pageIndex) {
			selected_index = pageIndex;
			selectChange();
		}
	}

	/**
	 * 发布动态
	 */
	OnClickListener publishClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			startActivity(new Intent(getActivity(), PublishActiviy.class));
		}
	};

	/**
	 * 点击标题，更改选中内容
	 */
	private OnClickListener itemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView checkView = (TextView) v;
			int childCount = ll_title.getChildCount();
			for (int i = 0; i < childCount; i++) {
				TextView titleView = (TextView) ll_title.getChildAt(i);
				if (checkView.equals(titleView)) {
					selected_index = i;
					viewPager.setCurrentItem(selected_index);
					break;
				}
			}
			selectChange();
		}
	};

	/**
	 * 选中类别更改时，对应标题颜色更改
	 */
	private void selectChange() {
		int childCount = ll_title.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TextView titleView = (TextView) ll_title.getChildAt(i);
			if (selected_index == i) {
				titleView.setTextColor(getResources().getColor(R.color.brown));
				titleView.setBackground(getResources().getDrawable(
						R.drawable.button_big_shape));
			} else {
				titleView.setTextColor(getResources().getColor(R.color.gray));
				titleView.setBackgroundColor(getResources().getColor(
						R.color.transparent));
			}
		}
	}

}