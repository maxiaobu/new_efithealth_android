package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.activity.FragmentMy.jsToAndroid;
import com.efithealth.app.entity.MemberClub;
import com.efithealth.app.entity.RequestFlag;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.UserUtils;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.view.MyScrollViewWeb;
import com.efithealth.app.view.MyScrollViewWeb.OnScrollChangedListener;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人主页
 * 
 * @author star
 *
 */
@SuppressLint({ "InflateParams", "SetJavaScriptEnabled", "NewApi", "JavascriptInterface" })
public class MemberClubActivity extends BaseActivity {
	private LinearLayout ll_title, ll_title_top;
	private TextView txt_info, txt_course, txt_dynamic, txt_info_top, txt_course_top, txt_dynamic_top;
	private ViewPager viewPager;
	private WebView web_load_title;
	private List<View> pageViews = new ArrayList<View>();
	private int selected_index = 0;
	private String[] url = new String[] { "file:///android_asset/essayList.html?tagid=T000001",
			"file:///android_asset/essayList.html?tagid=T000002",
			"file:///android_asset/essayList.html?tagid=T000011" };
	private String url_title = "file:///android_asset/member_top.html";
	private MyScrollViewWeb scroll_Web;
	private LinearLayout mTabTop = null;
	private int height = 0;
	private ImageView img_back;
	private boolean isFirstLoad = true;
	// 低栏
	private LinearLayout ll_my_bottom;
	private RelativeLayout rl_follow, rl_black, rl_chat, rl_bind;
	private TextView txt_follow, txt_black, txt_chat, txt_bind;
	String page = "";
	String tarid = "";
	String role = "";
	String memid = "";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_club);

		height = getWindowManager().getDefaultDisplay().getHeight();
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		ll_title_top = (LinearLayout) findViewById(R.id.ll_title_top);
		txt_info = (TextView) findViewById(R.id.txt_info);
		txt_course = (TextView) findViewById(R.id.txt_course);
		txt_dynamic = (TextView) findViewById(R.id.txt_dynamic);
		txt_info_top = (TextView) findViewById(R.id.txt_info_top);
		txt_course_top = (TextView) findViewById(R.id.txt_course_top);
		txt_dynamic_top = (TextView) findViewById(R.id.txt_dynamic_top);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		scroll_Web = (MyScrollViewWeb) findViewById(R.id.scroll_Web);
		mTabTop = (LinearLayout) findViewById(R.id.id_tab_member_club);
		web_load_title = (WebView) findViewById(R.id.web_load_title);
		img_back = (ImageView) findViewById(R.id.img_back);

		ll_my_bottom = (LinearLayout) findViewById(R.id.ll_my_bottom);
		rl_follow = (RelativeLayout) findViewById(R.id.rl_follow);
		rl_black = (RelativeLayout) findViewById(R.id.rl_black);
		rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
		rl_bind = (RelativeLayout) findViewById(R.id.rl_bind);
		txt_follow = (TextView) findViewById(R.id.txt_follow);
		txt_black = (TextView) findViewById(R.id.txt_black);
		txt_chat = (TextView) findViewById(R.id.txt_chat);
		txt_bind = (TextView) findViewById(R.id.txt_bind);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			page = extras.getString("page");
			tarid = extras.getString("tarid");
			role = extras.getString("role");
			memid = SharedPreferencesUtils.getParam(MemberClubActivity.this, "memid", "").toString();
			url_title = url_title + page;
			for (int i = 0; i < url.length; i++) {
				// url[i] = url[i] + page;
			}
			if (tarid.equals(memid)) {
				ll_my_bottom.setVisibility(View.GONE);
			} else {
				// 加载分类标题
				request();
				ll_my_bottom.setVisibility(View.VISIBLE);
			}
		}

		LayoutInflater inflater = getLayoutInflater();
		for (int i = 0; i < url.length; i++) {
			// 添加webview
			View web = inflater.inflate(R.layout.item_webview2, null);
			final WebView web_load = (WebView) web.findViewById(R.id.web_load);
			// 设置WebView支持JavaScript
			web_load.getSettings().setJavaScriptEnabled(true);
			// 在js中调用本地java方法
			// 设置进度条
			web_load.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
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
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
				}
			});
			pageViews.add(web);
		}
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		selectChange();

		// 加载头
		web_load_title.loadUrl(url_title);
		web_load_title.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		web_load_title.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		web_load_title.addJavascriptInterface(new jsToAndroid(), "mobile");
		// 添加客户端支持
		web_load_title.setWebChromeClient(new WebChromeClient());
		web_load_title.getSettings().setDefaultTextEncodingName("utf-8");
		web_load_title.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				web_load_title.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
		txt_info.setOnClickListener(itemClickListener);
		txt_course.setOnClickListener(itemClickListener);
		txt_dynamic.setOnClickListener(itemClickListener);
		txt_info_top.setOnClickListener(itemClickListener);
		txt_course_top.setOnClickListener(itemClickListener);
		txt_dynamic_top.setOnClickListener(itemClickListener);
		rl_follow.setOnClickListener(onBottonClickListener);
		rl_black.setOnClickListener(onBottonClickListener);
		rl_chat.setOnClickListener(onBottonClickListener);
		rl_bind.setOnClickListener(onBottonClickListener);
		scroll_Web.setOnScrollListener(scrollListener);
		// 返回
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				back(img_back);
			}
		});
	}

	// js调用借口类
	public class jsToAndroid {
		/**
		 * 获取会员ID
		 * 
		 * @return
		 */
		@JavascriptInterface
		public void toChat(String tarid, String memnickname, String memphoto) {
			// 进入聊天页面
			Intent intent = new Intent(MemberClubActivity.this, ChatActivity.class);
			intent.putExtra("userId", tarid.toLowerCase());
			intent.putExtra("nickname", memnickname);
			intent.putExtra("headimg", memphoto);
			startActivity(intent);
		}
	}

	/**
	 * 非登录用户，与登录用户关系点击事件
	 */
	OnClickListener onBottonClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// 关注拉黑后刷新web_load_title
			switch (view.getId()) {
			case R.id.rl_follow:// 关注
				requestFollowOrBlack("follow", Constant.URL_FOLLOWER, txt_follow.getTag().toString());// 关注、取消关注
				break;
			case R.id.rl_black:// 拉黑
				requestFollowOrBlack("black", Constant.URL_BLACKE, txt_black.getTag().toString());// 拉黑、解除拉黑
				break;
			case R.id.rl_chat:// 聊天
				web_load_title.loadUrl("javascript:talk()");
				break;
			case R.id.rl_bind:// 绑定
//				Intent intent = new Intent();
//				intent.setClass(MemberClubActivity.this, CourseActivity.class);
//				startActivity(intent);
//				MainActivity.instance.setTabSelection(202);
				break;
			}
		}
	};

	/**
	 * 关注、取消关注 或 拉黑、解除拉黑操作
	 * 
	 * @param operator操作follow关注
	 * @param url地址
	 * @param type操作类型
	 * 
	 */
	private void requestFollowOrBlack(final String operator, String url, final String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("memid", memid);
		map.put("tarid", tarid);
		LoadDataFromServer task_club_message = new LoadDataFromServer(MemberClubActivity.this, url, map);
		task_club_message.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				Gson gson = new Gson();
				RequestFlag result = gson.fromJson(data.toString(), RequestFlag.class);
				if ("1".equals(result.getMsgFlag())) {
					if (operator.equals("follow")) { // 关注
						if (type.equals("unfollow")) {
							txt_follow.setText("关注");
							txt_follow.setTag("follow");
						} else if (type.equals("follow")) {
							txt_follow.setText("已关注");
							txt_follow.setTag("unfollow");
						}
					} else if (operator.equals("black")) { // 拉黑
						if (type.equals("unblack")) {
							txt_black.setText("拉黑");
							txt_black.setTag("black");
							txt_follow.setText("关注");
						} else if (type.equals("black")) {
							txt_black.setText("已拉黑");
							txt_black.setTag("unblack");
						}
					}
					// 刷新web_load_title
					web_load_title.loadUrl("javascript:initData()");
				} else {
					Toast.makeText(MemberClubActivity.this, result.getMsgContent(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * 获得教练绑定的俱乐部信息
	 * 
	 */
	private void request() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tarid", tarid);
		map.put("tarrole", role);
		map.put("memid", memid);
		LoadDataFromServer task_club_message = new LoadDataFromServer(MemberClubActivity.this, Constant.URL_MEMBER_CLUB,
				map);
		task_club_message.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				// TODO 自动生成的方法存根
				Log.d("===获取个人主页底栏分类", data.toString());
				Gson gson = new Gson();
				MemberClub member = gson.fromJson(data.toString(), MemberClub.class);
				if ("1".equals(member.getMsgFlag())) {
					// 1已绑定 0绑定 else不显示
					if (member.getIsbind() == null) {
						rl_bind.setVisibility(View.GONE);
					} else {
						if (member.getIsbind().equals("1")) {
							txt_bind.setText("已绑定");
						} else if (member.getIsbind().equals("0")) {
							txt_bind.setText("绑定");
						}
					}
					// 1已关注 0关注 点击后触发事件的类型:0代表取消订阅,1代表订阅
					if (member.getIsfollow().equals("1")) {
						txt_follow.setText("已关注");
						txt_follow.setTag("unfollow");
					} else {
						txt_follow.setText("关注");
						txt_follow.setTag("follow");
					}
					// 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)
					// 点击后触发事件的类型:0代表解除拉黑,1代表拉黑
					if (member.getIsblacker().equals("1")) {
						txt_black.setText("已拉黑");
						txt_black.setTag("unblack");
					} else {
						txt_black.setText("拉黑");
						txt_black.setTag("black");
						txt_follow.setText("关注");
					}
				} else {
					Toast.makeText(MemberClubActivity.this, "没有用户关系信息", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * 滚动显示、隐藏头文件
	 */
	OnScrollChangedListener scrollListener = new OnScrollChangedListener() {
		@Override
		public void onScrollChanged(int x, int y, int oldX, int oldY) {
			if (y >= dpToPx(160)) {
				mTabTop.setVisibility(View.VISIBLE);
			} else {
				mTabTop.setVisibility(View.GONE);
			}
			// 初次加载首页赋值
			if (isFirstLoad && selected_index == 0) {
				int child = viewPager.getChildCount();
				boolean isCheckChild = false;
				int currentHeight = 0;
				for (int j = 0; j < child; j++) {
					WebView view = (WebView) viewPager.getChildAt(j).findViewById(R.id.web_load);
					if (view.getUrl().equals(url[selected_index])) {
						isCheckChild = true;
						currentHeight = (int) pxToDp(view.getContentHeight());
						break;
					}
					if (isCheckChild)
						break;
				}
				if (mTabTop.getVisibility() == View.VISIBLE) {
					scroll_Web.setScrollY((int) dpToPx(160));
				}
				LayoutParams params = viewPager.getLayoutParams();
				params.height = currentHeight < height ? height : currentHeight;
				viewPager.setLayoutParams(params);
				isFirstLoad = false;
			}
		}
	};

	public float pxToDp(float px) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	public float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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
				TextView titleViewTop = (TextView) ll_title_top.getChildAt(i);
				if (checkView.equals(titleView) || checkView.equals(titleViewTop)) {
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
			TextView titleViewTop = (TextView) ll_title_top.getChildAt(i);
			if (selected_index == i) {
				titleView.setTextColor(getResources().getColor(R.color.brown));
				titleView.setBackground(getResources().getDrawable(R.drawable.button_big_shape));
				titleViewTop.setTextColor(getResources().getColor(R.color.brown));
				titleViewTop.setBackground(getResources().getDrawable(R.drawable.button_big_shape));
				// 重绘viewpaper高度
				int child = viewPager.getChildCount();
				boolean isCheckChild = false;
				int currentHeight = 0;
				for (int j = 0; j < child; j++) {
					WebView view = (WebView) viewPager.getChildAt(j).findViewById(R.id.web_load);
					for (int k = 0; k < url.length; k++) {
						if (view.getUrl().equals(url[i])) {
							isCheckChild = true;
							currentHeight = (int) pxToDp(view.getContentHeight());
							break;
						}
					}
					if (isCheckChild)
						break;
				}
				if (mTabTop.getVisibility() == View.VISIBLE) {
					scroll_Web.setScrollY((int) dpToPx(160));
				}
				LayoutParams params = viewPager.getLayoutParams();
				params.height = currentHeight < height ? height : currentHeight;
				viewPager.setLayoutParams(params);
			} else {
				titleView.setTextColor(getResources().getColor(R.color.gray));
				titleView.setBackgroundColor(getResources().getColor(R.color.transparent));
				titleViewTop.setTextColor(getResources().getColor(R.color.gray));
				titleViewTop.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
		}
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}