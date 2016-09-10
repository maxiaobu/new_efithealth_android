package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.entity.Course;
import com.efithealth.app.entity.CourseAllList;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 全部课程
 * 
 * @author star
 *
 */
@SuppressLint({ "InflateParams", "SetJavaScriptEnabled" })
public class FragmentCourse extends BaseFragment {
	private HorizontalScrollView hsv_title;
	private LinearLayout ll_title;
	private ViewPager viewPager;
	private List<View> pageViews = new ArrayList<View>();
	private int selected_index = 0;
	private String url = "file:///android_asset/essayList.html?tagid=";
	// "file:///android_asset/essayList.html?tagid=T000001",
	private int width = 0;// 当前手机屏幕宽度，判断滚动
	private List<Course> tagList = null;
	private ImageView img_back;
	private View view;
	private TextView set_tv_course;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_course, container, false);
		FragmentHome.flag=false;
		return view;
	}
	@Override
	public void onDestroyView() {
		FragmentHome.flag=true;
		super.onDestroyView();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled" , "HandlerLeak" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;
		width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		hsv_title = (HorizontalScrollView) getView().findViewById(R.id.hsv_title);
		ll_title = (LinearLayout) getView().findViewById(R.id.ll_title);
		viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
		img_back = (ImageView) getView().findViewById(R.id.img_back);
		set_tv_course=(TextView) getView().findViewById(R.id.set_tv_course);
		// 返回
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				back();
			}
		});
		// 加载分类标题
		request();
		MainActivity.instance.hideTitleBar(1);
	}

	private void back() {
		MainActivity.instance.hideTitleBar(0);
		MainActivity.instance.returnBack();
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:// 是否显示底栏
				Bundle bundleData = msg.getData();
				set_tv_course.setText(bundleData.getString("title"));
				if (bundleData.get("navbar").toString().equals("1")) {
					MainActivity.instance.hideTitleBar(1);
					Log.e("FragmentCourse", "隐藏");
				} else {
					MainActivity.instance.hideTitleBar(0);
					Log.e("FragmentCourse", "显示");
				}
				break;
			}
		}
	};

	// js调用借口类
	public class jsToAndroid {
		@JavascriptInterface
		public void alertInfo(String type, String content) {
			ToastCommom.getInstance().ToastShow(getActivity(), content);
		}

		/**
		 * 任何一页控制
		 *            底栏
		 */
		@JavascriptInterface
		public void popNewWindow(String page) {
			if (page.indexOf("file:///android_asset/") < 0)
				page = "file:///android_asset/" + page;
			MainActivity.instance.setTabWebViewSelection(page);
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
			msg.setData(bundleData);
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 获得教练绑定的俱乐部信息
	 */
	private void request() {
		Map<String, String> map = null;
		LoadDataFromServer task_club_message = new LoadDataFromServer(getActivity(), Constant.URL_CATEGORY, map);
		task_club_message.getData(new DataCallBack() {
			@SuppressWarnings("deprecation")
			@Override
			public void onDataCallBack(JSONObject data) {
				Log.d("===获取文章分类集合", data.toString());
				Gson gson = new Gson();
				CourseAllList titleList = gson.fromJson(data.toString(), CourseAllList.class);
				if ("1".equals(titleList.getMsgFlag())) {
					LayoutInflater inflater = getActivity().getLayoutInflater();
					// 分类标题样式
					LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					int margin = 80;
					int margin_top = 20;
					llparams.setMargins(margin, margin_top, margin, margin_top);
					tagList = titleList.getTagList();
					for (int i = 0; i < tagList.size(); i++) {
						TextView tv = new TextView(getActivity());
						tv.setText(tagList.get(i).getEtname());
						tv.setLayoutParams(llparams);
						tv.setOnClickListener(itemClickListener);
						ll_title.addView(tv);
						// 添加webview
						View web = inflater.inflate(R.layout.item_webview, null);
						final WebView web_load = (WebView) web.findViewById(R.id.web_load);
						final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) web
								.findViewById(R.id.swipe_container);
						// 设置刷新时动画的颜色，可以设置4个
						swipeLayout.setColorScheme(android.R.color.holo_blue_light,
								android.R.color.holo_red_light, android.R.color.holo_orange_light,
								android.R.color.holo_green_light);
						swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

							@Override
							public void onRefresh() {
								// 重新刷新页面
								web_load.loadUrl(web_load.getUrl());
							}
						});
						// 添加客户端支持
						// 设置进度条
						web_load.setWebChromeClient(new WebChromeClient() {
							public void onReceivedTitle(WebView view, String title) {
								set_tv_course.setText(title);
							};
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
						web_load.getSettings().setUseWideViewPort(true); 
						web_load.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS); 
						web_load.getSettings().setLoadWithOverviewMode(true); 
						web_load.addJavascriptInterface(new jsToAndroid(), "mobile");
						// 在js中调用本地java方法
						// web_load.addJavascriptInterface(new
						// jsToAndroid_Find(),
						// "mobile");
						web_load.getSettings().setDefaultTextEncodingName("utf-8");
						web_load.loadUrl(url + tagList.get(i).getEtid());
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
								// view.loadUrl("javascript:loadPageData()");
							}
						});
						pageViews.add(web);
					}
					viewPager.setAdapter(new GuidePageAdapter());
					viewPager.setOnPageChangeListener(new GuidePageChangeListener());
					selectChange();

				} else {
					Toast.makeText(getActivity(), "没有文章分类信息", Toast.LENGTH_LONG).show();
				}

			}
		});

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
				// 滑动时对应标题位置显示
				int scrollX = hsv_title.getScrollX();
				int left = titleView.getLeft();
				int view_width = titleView.getWidth();
				if (left + view_width > width + scrollX) {// 后移
					hsv_title.setScrollX(left + view_width - width + 80);
				} else if (scrollX > left) {// 前移
					hsv_title.setScrollX(left - 80);
				}
			} else {
				titleView.setTextColor(getResources().getColor(R.color.black));
			}
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(view.getWindowToken());
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