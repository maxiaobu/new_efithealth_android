package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.efithealth.R;
import com.efithealth.app.activity.FragmentWebView.jsToAndroid;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订单列表
 * 
 * @author star
 *
 */
@SuppressLint({ "InflateParams", "SetJavaScriptEnabled", "NewApi" })
public class FragmentOrderList extends BaseFragment {
	private LinearLayout ll_title;
	private TextView txt_course, txt_forder;
	private ViewPager viewPager;
	private List<View> pageViews = new ArrayList<View>();
	private int selected_index = 1;
	private String[] url = new String[] { "file:///android_asset/corderList.html",
			"file:///android_asset/forderList.html" };
	private ImageView img_back;

	
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_list, container, false);
		ll_title = (LinearLayout) view.findViewById(R.id.ll_title_order);
		txt_course = (TextView) view.findViewById(R.id.txt_course_order);
		txt_forder = (TextView) view.findViewById(R.id.txt_forder_order);
		img_back = (ImageView) view.findViewById(R.id.order_img_back);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		txt_course.setOnClickListener(itemClickListener);
		txt_forder.setOnClickListener(itemClickListener);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.instance.setTabSelection(3);
			}
		});
		for (int i = 0; i < url.length; i++) {
			// 添加webview
			View web = inflater.inflate(R.layout.item_webview, null);
			final WebView web_load = (WebView) web.findViewById(R.id.web_load);
			final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) web.findViewById(R.id.swipe_container);
			// 设置刷新时动画的颜色，可以设置4个
			swipeLayout.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light,
					android.R.color.holo_orange_light, android.R.color.holo_green_light);
			swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh() {
					// 重新刷新页面
					web_load.loadUrl(web_load.getUrl());
				}
			});
			// 设置WebView支持JavaScript
			web_load.getSettings().setJavaScriptEnabled(true);
			// 在js中调用本地java方法
			web_load.addJavascriptInterface(new BaseJsToAndroid(getActivity()), "mobile");
			// 在js中调用本地java方法
			// web_load.addJavascriptInterface(new
			// jsToAndroid_Find(),
			// "mobile");
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
			// 设置WebView支持JavaScript
			web_load.getSettings().setJavaScriptEnabled(true);
			// 在js中调用本地java方法
			// web_load.addJavascriptInterface(new
			// jsToAndroid_Find(),
			// "mobile");
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
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
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
			pageViews.add(web);
		}
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		selectChange();
		// view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
		// view.setFocusableInTouchMode(true);
		// view.setOnKeyListener(backlistener);
		return view;
	}

	public void refresh() {
		int child = viewPager.getChildCount();
		for (int i = 0; i < child; i++) {
			WebView wv = (WebView) viewPager.getChildAt(i).findViewById(R.id.web_load);
			wv.loadUrl(wv.getUrl());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
				titleView.setBackground(getResources().getDrawable(R.drawable.button_big_shape));
			} else {
				titleView.setTextColor(getResources().getColor(R.color.gray));
				titleView.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
		}
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