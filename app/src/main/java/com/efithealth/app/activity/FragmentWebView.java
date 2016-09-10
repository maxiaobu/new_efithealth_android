package com.efithealth.app.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.image.ImagePagerActivity;

import kpswitch.util.KPSwitchConflictUtil;
import kpswitch.util.KeyboardUtil;
import kpswitch.widget.KPSwitchFSPanelLinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.fragment.FragmentMe;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import net.sourceforge.simcpux.Constants;

public class FragmentWebView extends BaseFragment {

	public View view;
	public static WebView webview;
	public ImageView back, button;
	public TextView set;
	public Handler mHandler;
	public int isback_test = 0;
	private String title = "";
	private String url = "file:///android_asset/empty.html";
	private KPSwitchFSPanelLinearLayout panel_root;
	private OnGlobalLayoutListener ogll;
	private int height;
	private boolean isDynamic = false;
	public static boolean flag_upData = false;
	private String me_flag="";

	
	
	@Override
	public void onDestroy() {
		if (set.getText().equals("教练列表")||set.getText().equals("club")||set.getText().equals("配餐列表")) {
			FragmentHome.flag=true;
		}
		if (me_flag.equals("yes")) {
			FragmentMe.handler.sendEmptyMessage(0);
		}
		SharedPreferencesUtils.deleteSharedData(getActivity(), "me_change");
		super.onDestroyView();
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		me_flag=(String) SharedPreferencesUtils.getParam(getActivity(), "me_change", "");
		FragmentHome.flag=false;
		final View view = inflater.inflate(R.layout.fragment_webview,
				container, false);

		panel_root = (KPSwitchFSPanelLinearLayout) view
				.findViewById(R.id.panel_root234);
		// 监听键盘的
		KeyboardUtil.attach(getActivity(), panel_root);
		KPSwitchConflictUtil.attach(panel_root, null, view,
				new KPSwitchConflictUtil.SwitchClickListener() {
					@Override
					public void onClickSwitch(boolean switchToPanel) {
						if (switchToPanel) {
							view.clearFocus();
						} else {
							view.requestFocus();
						}
					}
				});
		// 监听键盘的
		ogll = new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getActivity().getWindow().getDecorView()
						.getWindowVisibleDisplayFrame(rect);
				WindowManager wm = getActivity().getWindowManager();
				height = wm.getDefaultDisplay().getHeight();

				int h = height - (rect.bottom - rect.top);
				boolean flag123 = h > height / 3;
				if (flag123) {
					if (isDynamic) {
						MainActivity.instance.titlelayout
								.setVisibility(View.GONE);
					}
					panel_root.setVisibility(View.VISIBLE);
				} else {
					if (isDynamic) {
						MainActivity.instance.titlelayout
								.setVisibility(View.VISIBLE);
					}
					panel_root.setVisibility(View.GONE);
				}

			}
		};
		// 开始监听键盘是否显示
		getActivity().getWindow().getDecorView().getViewTreeObserver()
				.addOnGlobalLayoutListener(ogll);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {
					View view = getActivity().getCurrentFocus();
					if (isHideInput(view, ev)) {
						HideSoftInput(view.getWindowToken());
					}
				}
				return true;
			}
		});
		return view;
	}

	@Override
	public void onPause() {
		hehe();
		HideSoftInput(getView().getWindowToken());
		super.onPause();
	}

	public static void reloadView(){
		if (flag_upData&&webview.getUrl().indexOf("blackerList.html")!=-1) {
			webview.loadUrl("javascript:reloadData()");
			flag_upData = false;
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak",
			"JavascriptInterface" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		webview = (WebView) getView().findViewById(R.id.webview);
		back = (ImageView) getView().findViewById(R.id.back);
		button = (ImageView) getView().findViewById(R.id.button);
		set = (TextView) getView().findViewById(R.id.set);
		set.setText(title);
		webview.loadUrl(url);
		webview.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setPluginState(PluginState.ON);

		// 在js中调用本地java方法
		webview.addJavascriptInterface(new jsToAndroid(getActivity()), "mobile");
		// 添加客户端支持
		webview.setWebChromeClient(new WebChromeClient());
		webview.getSettings().setDefaultTextEncodingName("utf-8");

		/*** 打开本地缓存提供JS调用 **/
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getActivity().getApplicationContext()
				.getCacheDir().getAbsolutePath();
		webview.getSettings().setAppCachePath(appCachePath);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setAppCacheEnabled(true);

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

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isDynamic) {
					MainActivity.instance.titlelayout
							.setVisibility(View.VISIBLE);
					isDynamic = false;
				}
				hehe();
				HideSoftInput(getView().getWindowToken());
				MainActivity.instance.returnBack();
			}
		});

		// 非UI线程发消息给UI线程，UI线程去修改UI
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundleData = msg.getData();
				if (bundleData.get("title").toString().equals("我")) {
					MainActivity.instance.setTabColor(3);
				}
				if (bundleData.get("title").toString().equals("动态详情页")) {
					isDynamic = true;
				} else {
					isDynamic = false;
				}
				
				if (bundleData.get("title").toString().equals("订单确认")) {
					
				}
				set.setText(bundleData.get("title").toString());
				if (bundleData.get("back").toString().equals("1")) {
					back.setVisibility(View.VISIBLE);
					isback_test = 0;
				} else if (bundleData.get("back").toString().equals("close")) {
					isback_test = 3;
					back.setVisibility(View.VISIBLE);
				} else if (bundleData.get("back").toString().equals("2")) {
					isback_test = 1;
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
			}
		};

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void hehe() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			getActivity().getWindow().getDecorView().getViewTreeObserver()
					.removeOnGlobalLayoutListener(ogll);
		} else {
			getActivity().getWindow().getDecorView().getViewTreeObserver()
					.removeGlobalOnLayoutListener(ogll);
		}
	}

	public FragmentWebView(String url) {
		this.url = url;
	}

	// js调用借口类
	public class jsToAndroid extends BaseJsToAndroid {
		public jsToAndroid(Context context) {
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

		@JavascriptInterface
		public void shared(String head) {
			SharedPreferencesUtils.setParam(getActivity(), "headpage", head);
		}

		@JavascriptInterface
		public void setRefreshDynid(String dynid, String str) {
			SharedPreferencesUtils.setParam(getActivity(), "dynid_find1", dynid);
			SharedPreferencesUtils.setParam(getActivity(), "fr_find1", str);

		}

		@JavascriptInterface
		public void backExe() {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					MainActivity.instance.returnBack();
				}
			});
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
		public void loadPageData(String title, String isback, String btn,
				String navbar) {
			Message msg = new Message();
			Bundle bundleData = new Bundle();
			bundleData.putString("title", title);
			bundleData.putString("back", isback);
			bundleData.putString("btn", btn);
			bundleData.putString("navbar", navbar);
			msg.setData(bundleData);
			mHandler.sendMessage(msg);
		}

		// 修改收货信息
		@JavascriptInterface
		public void personalInfo() {
			Intent intent = new Intent();
			intent.setClass(getActivity(), RevampAddress.class);
			startActivityForResult(intent, 11);
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
			startActivityForResult(intent, 11);
		}

		/**
		 * 微信支付
		 * 
		 * @param ordno
		 *            订单类型
		 * @param ordamt
		 *            支付金额
		 */
		@JavascriptInterface
		public void pay(String ordno, String ordamt) {
			// 第一步：与服务器连接获取信息
			Map<String, String> map = new HashMap<String, String>();
			map.put("ordno", ordno);
			map.put("orderamt", ordamt);
			LoadDataFromServer task_club_message = new LoadDataFromServer(
					getActivity(), Constant.URL_WEIXIN_PAY, map);
			task_club_message.getData(new DataCallBack() {
				@SuppressWarnings("deprecation")
				@Override
				public void onDataCallBack(JSONObject json) {
					Log.d("===获取支付信息", json.toString());
					if (!"".equals(json.toString())
							&& json.toString().length() > 0) {
						IWXAPI api = WXAPIFactory.createWXAPI(getActivity(),
								"wxe774923041503e14");// wxb4ba3c02aa476ea1
						api.registerApp(Constants.APP_ID);
						PayReq req = new PayReq();
						// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
						req.appId = json.getString("appid");
						req.partnerId = json.getString("partnerid");
						req.prepayId = json.getString("prepayid");
						req.nonceStr = json.getString("noncestr");
						req.timeStamp = json.getString("timestamp");
						req.packageValue = json.getString("package");
						req.sign = json.getString("sign");
						// req.extData = "app data"; // optional
						// Toast.makeText(getActivity(), "正常调起支付",
						// Toast.LENGTH_SHORT).show();
						// {"sign":"08D2C90C23898811DD26B633F3F7365D",
						// "timestamp":"1461580514",
						// "noncestr":"XdOUYDEjMB4Aadxv",
						// "partnerid":"1335056901",
						// "prepayid":"wx20160425183516650b8b66a50302526203",
						// "package":"Sign=WXPay",
						// "appid":"wxe774923041503e14"}
						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						boolean isReq = api.sendReq(req);
						Log.e("aa", "" + isReq);
						if (isReq) {
							MainActivity.instance.setTabSelection(0);
						}

					} else {
						Toast.makeText(getActivity(), "没有支付信息",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	// @Override
	// public void onResp(BaseResp resp) {
	// if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
	// Toast.makeText(getActivity(), resp.errCode, Toast.LENGTH_LONG).show();
	// }
	// // 0 成功 展示成功页面
	// // -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
	// // -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 11) {
			webview.loadUrl("javascript:initMemberInfo()");
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			MainActivity.instance.hideTitleBar(0);
			HideSoftInput(getView().getWindowToken());
		}
	}
	
	// 判定是否需要隐藏
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
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
}
