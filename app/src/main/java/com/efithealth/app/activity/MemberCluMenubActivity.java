package com.efithealth.app.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.activity.FragmentMemberClub.jsToAndroid;
import com.efithealth.app.entity.MemberClub;
import com.efithealth.app.entity.RequestFlag;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.DrawableUtil;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.view.MyScrollViewWeb;
import com.efithealth.app.view.MyScrollViewWeb.OnScrollChangedListener;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MemberCluMenubActivity extends BaseActivity {
	public static boolean flag_find = false;

	private LinearLayout ll_title, ll_title_top, ll_title1, ll_title_top1;
	private TextView txt_info, txt_info1, txt_course, txt_dynamic,
			txt_dynamic1, txt_info_top, txt_course_top, txt_dynamic_top,
			txt_info_top1, txt_dynamic_top1;
	private ViewPager viewPager;
	private WebView web_load_title;
	private List<View> pageViews = new ArrayList<View>();
	private List<WebView> list_web = new ArrayList<WebView>();
	private int selected_index = 0;
	private String[] url = new String[] {
			"file:///android_asset/member_info.html",
			"file:///android_asset/member_course.html",
			"file:///android_asset/member_dynamic.html" };
	private String url_title = "file:///android_asset/member_top.html";
	private MyScrollViewWeb scroll_Web;
	private LinearLayout mTabTop = null;
	private int height = 0;
	private boolean isFirstLoad = true;
	// 低栏
	private LinearLayout ll_my_bottom;
	private RelativeLayout rl_follow, rl_black, rl_chat, rl_bind;
	private TextView txt_follow, txt_black, txt_bind;
	String page = "";
	String tarid = "";
	String role = "";
	String memid = "";
	private ImageView img_back, img_black, img_follow;
	Map<String, String> map = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_member_club);
		initView();
	}

	private void initView() {
		height = getWindowManager().getDefaultDisplay().getHeight();
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		ll_title1 = (LinearLayout) findViewById(R.id.ll_title1);
		ll_title_top = (LinearLayout) findViewById(R.id.ll_title_top);
		ll_title_top1 = (LinearLayout) findViewById(R.id.ll_title_top1);
		txt_info = (TextView) findViewById(R.id.txt_info);
		txt_info1 = (TextView) findViewById(R.id.txt_info1);
		txt_course = (TextView) findViewById(R.id.txt_course);
		txt_dynamic = (TextView) findViewById(R.id.txt_dynamic);
		txt_dynamic1 = (TextView) findViewById(R.id.txt_dynamic1);
		txt_info_top = (TextView) findViewById(R.id.txt_info_top);
		txt_info_top1 = (TextView) findViewById(R.id.txt_info_top1);
		txt_course_top = (TextView) findViewById(R.id.txt_course_top);
		txt_dynamic_top = (TextView) findViewById(R.id.txt_dynamic_top);
		txt_dynamic_top1 = (TextView) findViewById(R.id.txt_dynamic_top1);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		scroll_Web = (MyScrollViewWeb) findViewById(R.id.scroll_Web);
		mTabTop = (LinearLayout) findViewById(R.id.id_tab_member_club);
		web_load_title = (WebView) findViewById(R.id.web_load_title);

		ll_my_bottom = (LinearLayout) findViewById(R.id.ll_my_bottom);
		rl_follow = (RelativeLayout) findViewById(R.id.rl_follow);
		rl_black = (RelativeLayout) findViewById(R.id.rl_black);
		rl_chat = (RelativeLayout) findViewById(R.id.rl_chat);
		rl_bind = (RelativeLayout) findViewById(R.id.rl_bind);
		txt_follow = (TextView) findViewById(R.id.txt_follow);
		txt_black = (TextView) findViewById(R.id.txt_black);
		txt_bind = (TextView) findViewById(R.id.txt_bind);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_black = (ImageView) findViewById(R.id.img_black);
		img_follow = (ImageView) findViewById(R.id.img_follow);

		// 给背景设置圆角图片
		BitmapDrawable bbb = new DrawableUtil().ImgToCorner(this,
				R.drawable.daoda, 15);// 依次传入activity,图片ID,角度
		ll_title.setBackgroundDrawable(bbb);
		ll_title_top.setBackgroundDrawable(bbb);
		ll_title1.setBackgroundDrawable(bbb);
		ll_title_top1.setBackgroundDrawable(bbb);

		// 加载头
		Object extras = SharedPreferencesUtils.getParam(this, "page", "");
		if (extras != null && extras.toString().length() > 0) {
			page = extras.toString().substring(extras.toString().indexOf("?"));
			// 截去图片地址
			String[] params = page.indexOf("&memphoto") > 0 ? page.substring(1,
					page.indexOf("&memphoto")).split("&") : page.substring(1)
					.split("&");
			map.clear();
			for (int i = 0; i < params.length; i++) {
				String[] values = params[i].split("=");
				map.put(values[0], values.length > 1 ? values[1] : "");
			}
			tarid = map.get("tarid");
			role = map.get("role");
			memid = SharedPreferencesUtils.getParam(this, "memid", "")
					.toString();
		}
		// 加载头
		web_load_title.loadUrl(url_title + page);
		web_load_title.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		web_load_title.getSettings().setJavaScriptEnabled(true);
		// 在js中调用本地java方法
		web_load_title.addJavascriptInterface(new jsToAndroid(this), "mobile");
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

		LayoutInflater inflater = getLayoutInflater();

		if (role.equals("mem")) {
			ll_title1.setVisibility(View.VISIBLE);
			ll_title.setVisibility(View.GONE);
			ll_title_top1.setVisibility(View.VISIBLE);
			ll_title_top.setVisibility(View.GONE);
		} else {
			ll_title.setVisibility(View.VISIBLE);
			ll_title1.setVisibility(View.GONE);
			ll_title_top.setVisibility(View.VISIBLE);
			ll_title_top1.setVisibility(View.GONE);
		}

		for (int i = 0; i < url.length; i++) {
			View web = inflater.inflate(R.layout.item_webview2, null);

			final WebView web_load = (WebView) web
					.findViewById(R.id.web_load12);
			if (role.equals("mem")) {
				// 添加webview
				if (i == 0) {
					web_load.loadUrl(url[0] + page);
					// 设置WebView支持JavaScript
					web_load.getSettings().setJavaScriptEnabled(true);
					web_load.addJavascriptInterface(new jsToAndroid(this),
							"mobile");
					// 在js中调用本地java方法
					// 设置进度条
					web_load.setWebChromeClient(new WebChromeClient() {
						@Override
						public void onProgressChanged(WebView view,
								int newProgress) {
							super.onProgressChanged(view, newProgress);
						}

					});

					web_load.getSettings().setDefaultTextEncodingName("utf-8");

					// 设置WebView中的客户端的行为
					web_load.setWebViewClient(new WebViewClient() {
						// 让WebView对点击网页中的URL做出响应
						@Override
						public boolean shouldOverrideUrlLoading(WebView view,
								String url) {
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
						}
					});
					pageViews.add(web);
					list_web.add(web_load);
				} else if (i == 2) {
					web_load.loadUrl(url[2] + page);
					// 设置WebView支持JavaScript
					web_load.getSettings().setJavaScriptEnabled(true);
					web_load.addJavascriptInterface(new jsToAndroid(this),
							"mobile");
					// 在js中调用本地java方法
					// 设置进度条
					web_load.setWebChromeClient(new WebChromeClient() {
						@Override
						public void onProgressChanged(WebView view,
								int newProgress) {
							super.onProgressChanged(view, newProgress);
						}

					});

					web_load.getSettings().setDefaultTextEncodingName("utf-8");

					// 设置WebView中的客户端的行为
					web_load.setWebViewClient(new WebViewClient() {
						// 让WebView对点击网页中的URL做出响应
						@Override
						public boolean shouldOverrideUrlLoading(WebView view,
								String url) {
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
						}
					});
					pageViews.add(web);
					list_web.add(web_load);
				}
			} else {
				// 添加webview
				web_load.loadUrl(url[i] + page);

				// 设置WebView支持JavaScript
				web_load.getSettings().setJavaScriptEnabled(true);
				web_load.addJavascriptInterface(new jsToAndroid(this), "mobile");
				// 在js中调用本地java方法
				// 设置进度条
				web_load.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						super.onProgressChanged(view, newProgress);
					}

				});

				web_load.getSettings().setDefaultTextEncodingName("utf-8");

				// 设置WebView中的客户端的行为
				web_load.setWebViewClient(new WebViewClient() {
					// 让WebView对点击网页中的URL做出响应
					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
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
					}
				});
				pageViews.add(web);
				list_web.add(web_load);
			}

		}
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		// TODO

		// viewPager.addOnPageChangeListener(new
		// ViewPager.SimpleOnPageChangeListener() {
		// @Override
		// public void onPageSelected(int position) {
		// super.onPageSelected(position); ViewGroup.LayoutParams params =
		// viewPager.getLayoutParams();
		// params.height =pageViews.get(position).getHeight();
		// }});
		selectChange();

		// 返回
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				back();
			}
		});
		txt_info.setOnClickListener(itemClickListener);
		txt_course.setOnClickListener(itemClickListener);
		txt_dynamic.setOnClickListener(itemClickListener);
		txt_info1.setOnClickListener(itemClickListener);
		txt_dynamic1.setOnClickListener(itemClickListener);
		txt_info_top.setOnClickListener(itemClickListener);
		txt_course_top.setOnClickListener(itemClickListener);
		txt_dynamic_top.setOnClickListener(itemClickListener);
		txt_info_top1.setOnClickListener(itemClickListener);
		txt_dynamic_top1.setOnClickListener(itemClickListener);
		rl_follow.setOnClickListener(onBottonClickListener);
		rl_black.setOnClickListener(onBottonClickListener);
		rl_chat.setOnClickListener(onBottonClickListener);
		rl_bind.setOnClickListener(onBottonClickListener);
		scroll_Web.setOnScrollListener(scrollListener);
		if (tarid.equals(memid)) {
			ll_my_bottom.setVisibility(View.GONE);
		} else {
			// 加载分类标题
			new AnotherTask().execute("");
			ll_my_bottom.setVisibility(View.VISIBLE);
		}
		MainActivity.instance.hideTitleBar(1);

	}

	/**
	 * 获得教练绑定的俱乐部信息
	 * 
	 */
	private class AnotherTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			// 对UI组件的更新操作
			String data = result.toString();
			Gson gson = new Gson();
			MemberClub member = gson
					.fromJson(data.toString(), MemberClub.class);
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
					img_follow.setImageDrawable(getResources().getDrawable(
							R.drawable.unfollow));
				} else {
					txt_follow.setText("关注");
					txt_follow.setTag("follow");
					img_follow.setImageDrawable(getResources().getDrawable(
							R.drawable.follow));
				}
				// 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)
				// 点击后触发事件的类型:0代表解除拉黑,1代表拉黑
				if (member.getIsblacker().equals("1")) {
					txt_black.setText("已拉黑");
					txt_black.setTag("unblack");
					img_black.setImageDrawable(getResources().getDrawable(
							R.drawable.black));
					// FragmentWebView.flag_upData = false;
				} else {
					txt_black.setText("拉黑");
					txt_black.setTag("black");
					img_black.setImageDrawable(getResources().getDrawable(
							R.drawable.unblack));
					// FragmentWebView.flag_upData = true;
				}
				Log.d("成功", data.toString());
			} else {
				Toast.makeText(MemberCluMenubActivity.this, "没有用户关系信息",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("tarid", tarid);
			map.put("tarrole", role);
			map.put("memid", memid);
			LoadDataFromServer task_club_message = new LoadDataFromServer(
					MemberCluMenubActivity.this, Constant.URL_MEMBER_CLUB, map);
			String data = task_club_message.getData();
			return data;
		}
	}

	private void back() {
		MemberCluMenubActivity.this.finish();
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:// 绑定个人主页底栏
				Bundle bundle = msg.getData();
				String data = bundle.get("data").toString();
				Gson gson = new Gson();
				MemberClub member = gson.fromJson(data.toString(),
						MemberClub.class);
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
						img_follow.setImageDrawable(getResources().getDrawable(
								R.drawable.unfollow));
					} else {
						txt_follow.setText("关注");
						txt_follow.setTag("follow");
						img_follow.setImageDrawable(getResources().getDrawable(
								R.drawable.follow));
					}
					// 1已拉黑 0拉黑(拉黑后的用户统一取消关注isfollow=0显示为关注)
					// 点击后触发事件的类型:0代表解除拉黑,1代表拉黑
					if (member.getIsblacker().equals("1")) {
						txt_black.setText("已拉黑");
						txt_black.setTag("unblack");
						img_black.setImageDrawable(getResources().getDrawable(
								R.drawable.black));
					} else {
						txt_black.setText("拉黑");
						txt_black.setTag("black");
						img_black.setImageDrawable(getResources().getDrawable(
								R.drawable.unblack));
					}
					Log.d("成功", data.toString());
				} else {
					Toast.makeText(MemberCluMenubActivity.this, "没有用户关系信息",
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

	// js调用借口类
	public class jsToAndroid extends BaseJsToAndroid {
		public jsToAndroid(Context context) {
			super(context);
		}

		@Override
		@JavascriptInterface
		public void popNewWindow(String page) {
			if (page.indexOf("file:///android_asset/") < 0)
				page = "file:///android_asset/" + page;
			if (role.equals("mem")) {
				if (selected_index == 1) {
					Intent intent = new Intent(MemberCluMenubActivity.this,
							FindActivity.class);
					intent.putExtra("url", page);
					startActivity(intent);
				} else {
					
					MainActivity.instance.setTabWebViewSelection(page);
					finish();
				}
			} else {
				if (selected_index == 2) {
					Intent intent = new Intent(MemberCluMenubActivity.this,
							FindActivity.class);
					intent.putExtra("url", page);
					startActivity(intent);
				} else {
					finish();
					MainActivity.instance.setTabWebViewSelection(page);
				}
			}
		}

		// 显示动态里的图片
		@JavascriptInterface
		public void imgView(final String urls, final int position) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					List<String> list = new ArrayList<String>();

					String[] urlArr = urls.split("&");
					for (int i = 0; i < urlArr.length; i++) {

						list.add(urlArr[i]);
						Log.i("djy", position + "----------" + urlArr[i]);
					}
					Intent intent = new Intent(MemberCluMenubActivity.this,
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

	/**
	 * 非登录用户，与登录用户关系点击事件
	 */
	OnClickListener onBottonClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// 关注拉黑后刷新web_load_title
			switch (view.getId()) {
			case R.id.rl_follow:// 关注
				requestFollowOrBlack("follow", Constant.URL_FOLLOWER,
						txt_follow.getTag().toString());// 关注、取消关注
				break;
			case R.id.rl_black:// 拉黑
				requestFollowOrBlack("black", Constant.URL_BLACKE, txt_black
						.getTag().toString());// 拉黑、解除拉黑
				break;
			case R.id.rl_chat:// 聊天
				web_load_title.loadUrl("javascript:talk()");
				break;
			case R.id.rl_bind:// 绑定
				// ?tarid=M000766&clubid=C000318&coachid=M000818&role=clubadmin&type=unbind
				String url = "";
				if (txt_bind.getText().toString().equals("绑定"))// 跳转到动态详情页
					url = "file:///android_asset/bindClub.html?clubid="
							+ map.get("clubid") + "&coachid=" + memid;
				else
					url = "file:///android_asset/unbindClub.html?clubid="
							+ map.get("clubid") + "&coachid=" + memid;
				MainActivity.instance.setTabWebViewSelection(url);
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
	private void requestFollowOrBlack(final String operator, String url,
			final String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("memid", memid);
		map.put("tarid", tarid);
		LoadDataFromServer task_club_message = new LoadDataFromServer(
				MemberCluMenubActivity.this, url, map);
		task_club_message.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				Gson gson = new Gson();
				RequestFlag result = gson.fromJson(data.toString(),
						RequestFlag.class);
				if ("1".equals(result.getMsgFlag())) {
					if (operator.equals("follow")) { // 关注
						if (type.equals("unfollow")) {
							txt_follow.setText("关注");
							txt_follow.setTag("follow");
							img_follow.setImageDrawable(getResources()
									.getDrawable(R.drawable.follow));
						} else if (type.equals("follow")) {
							txt_follow.setText("已关注");
							txt_follow.setTag("unfollow");
							img_follow.setImageDrawable(getResources()
									.getDrawable(R.drawable.unfollow));
						}
					} else if (operator.equals("black")) { // 拉黑
						if (type.equals("unblack")) {
							txt_black.setText("拉黑");
							txt_black.setTag("black");
							img_black.setImageDrawable(getResources()
									.getDrawable(R.drawable.unblack));
							txt_follow.setText("关注");
							FragmentWebView.flag_upData = true;
						} else if (type.equals("black")) {
							FragmentWebView.flag_upData = false;
							txt_black.setText("已拉黑");
							txt_black.setTag("unblack");
							txt_follow.setText("关注");
							img_follow.setImageDrawable(getResources()
									.getDrawable(R.drawable.follow));
							img_black.setImageDrawable(getResources()
									.getDrawable(R.drawable.black));
						}
					}
					// 刷新web_load_title
					web_load_title.loadUrl("javascript:initData()");
				} else {
					Toast.makeText(MemberCluMenubActivity.this,
							result.getMsgContent(), Toast.LENGTH_LONG).show();
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
					if (role.equals("mem")) {
						if (j == 0) {
							WebView view = (WebView) viewPager.getChildAt(j)
									.findViewById(R.id.web_load12);
							if (view.getUrl().equals(url[selected_index])) {
								isCheckChild = true;
								currentHeight = (int) pxToDp(view
										.getContentHeight());
								break;
							}
							if (isCheckChild)
								break;
						}
						if (j == 1) {
							WebView view = (WebView) viewPager.getChildAt(j)
									.findViewById(R.id.web_load12);
							if (view.getUrl().equals(url[selected_index])) {
								isCheckChild = true;
								currentHeight = (int) pxToDp(view
										.getContentHeight());
								break;
							}
							if (isCheckChild)
								break;
						}
					} else {
						WebView view = (WebView) viewPager.getChildAt(j)
								.findViewById(R.id.web_load12);
						if (view.getUrl().equals(url[selected_index])) {
							isCheckChild = true;
							currentHeight = (int) pxToDp(view
									.getContentHeight());
							break;
						}
						if (isCheckChild)
							break;
					}
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
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
				getResources().getDisplayMetrics());
	}

	public float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
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
			// int childCount = ll_title.getChildCount();
			for (int i = 0; i < pageViews.size(); i++) {
				TextView titleView;
				TextView titleViewTop;
				if (role.equals("mem")) {
					titleView = (TextView) ll_title1.getChildAt(i);
					titleViewTop = (TextView) ll_title_top1.getChildAt(i);
				} else {
					titleView = (TextView) ll_title.getChildAt(i);
					titleViewTop = (TextView) ll_title_top.getChildAt(i);
				}
				if (checkView.equals(titleView)
						|| checkView.equals(titleViewTop)) {
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
	@SuppressLint("NewApi")
	private void selectChange() {
		int childCount;
		if (role.equals("mem")) {
			childCount = ll_title1.getChildCount();
		} else {
			childCount = ll_title.getChildCount();
		}
		for (int i = 0; i < childCount; i++) {
			TextView titleView;
			TextView titleViewTop;
			if (role.equals("mem")) {
				titleView = (TextView) ll_title1.getChildAt(i);
				titleViewTop = (TextView) ll_title_top1.getChildAt(i);
			} else {
				titleView = (TextView) ll_title.getChildAt(i);
				titleViewTop = (TextView) ll_title_top.getChildAt(i);
			}
			if (selected_index == i) {
				titleView.setTextColor(getResources().getColor(R.color.brown));
				titleView.setBackground(getResources().getDrawable(
						R.drawable.button_big_shape));
				titleViewTop.setTextColor(getResources()
						.getColor(R.color.brown));
				titleViewTop.setBackground(getResources().getDrawable(
						R.drawable.button_big_shape));
				// 重绘viewpaper高度
				int child = viewPager.getChildCount();
				boolean isCheckChild = false;
				int currentHeight = 0;
				for (int j = 0; j < child; j++) {
					WebView view = (WebView) viewPager.getChildAt(j)
							.findViewById(R.id.web_load12);
					for (int k = 0; k < child; k++) {
						if (role.equals("mem")) {
							if (i == 0) {
								if (view.getUrl().contains(url[0])) {
									isCheckChild = true;
									currentHeight = (int) pxToDp(view
											.getContentHeight());
									break;
								}
							}
							if (i == 1) {
								if (view.getUrl().contains(url[2])) {
									isCheckChild = true;
									currentHeight = (int) pxToDp(view
											.getContentHeight());
									break;
								}
							}
						} else {
							if (view.getUrl().contains(url[i])) {
								isCheckChild = true;
								currentHeight = (int) pxToDp(view
										.getContentHeight());
								break;
							}
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
				titleView.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				titleViewTop
						.setTextColor(getResources().getColor(R.color.gray));
				titleViewTop.setBackgroundColor(getResources().getColor(
						R.color.transparent));
			}
		}
	}
	
	@Override
	protected void onResume() {
		if (flag_find) {
			String id = (String) SharedPreferencesUtils.getParam(this,
					"dynid_find1", "");
			if (role.equals("mem")) {
				list_web.get(1).loadUrl("javascript:refreshDyn('" + id + "')");
			} else {
				list_web.get(2).loadUrl("javascript:refreshDyn('" + id + "')");
			}
			SharedPreferencesUtils
					.deleteSharedData(this, "dynid_find1");
			flag_find = false;
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {

		MainActivity.instance.hideTitleBar(0);
		SharedPreferencesUtils.deleteSharedData(MemberCluMenubActivity.this,
				"page");
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		MemberCluMenubActivity.this.finish();
		super.onBackPressed();
	}

}
