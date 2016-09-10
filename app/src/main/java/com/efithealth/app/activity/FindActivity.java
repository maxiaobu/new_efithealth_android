package com.efithealth.app.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.image.ImagePagerActivity;
import kpswitch.util.KPSwitchConflictUtil;
import kpswitch.util.KeyboardUtil;
import kpswitch.widget.KPSwitchFSPanelLinearLayout;

import com.efithealth.R;
import com.efithealth.app.fragment.FragmentFriends;
import com.efithealth.app.utils.BaseJsToAndroid;
import com.efithealth.app.utils.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.TextView;

public class FindActivity extends BaseActivity{
	public WebView webview;
	public ImageView back, button;
	public TextView set;
	public Handler mHandler;
	public int isback_test = 0;
	private String title = "动态详情页";
	private String url = "file:///android_asset/empty.html";
	private KPSwitchFSPanelLinearLayout panel_root;
	private OnGlobalLayoutListener ogll;
	private int height;
	@SuppressWarnings("deprecation")
	@SuppressLint({ "InlinedApi", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_find__webview);
		
		FragmentFind.flag_find=true;
		FragmentMemberClub.flag_find=true;
		FragmentFriends.flag_find=true;
		
		panel_root = (KPSwitchFSPanelLinearLayout)findViewById(R.id.panel_root_find);
		
		//监听键盘的
				KeyboardUtil.attach(this, panel_root);
		        KPSwitchConflictUtil.attach(panel_root, null, findViewById(R.id.rootView_find_1),
		                new KPSwitchConflictUtil.SwitchClickListener() {
		                    @Override
		                    public void onClickSwitch(boolean switchToPanel) {
		                        if (switchToPanel) {
		                        	findViewById(R.id.rootView_find_1).clearFocus();
		                        } else {
		                        	findViewById(R.id.rootView_find_1).requestFocus();
		                        }
		                    }
		                });
		// 监听键盘的
		ogll = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getWindow().getDecorView()
						.getWindowVisibleDisplayFrame(rect);
				WindowManager wm = getWindowManager();
				height = wm.getDefaultDisplay().getHeight();

				int h = height - (rect.bottom - rect.top);
				boolean flag123 = h > height / 3;
				if (flag123) {
					panel_root.setVisibility(View.VISIBLE);
				} else {
					panel_root.setVisibility(View.GONE);
				}

			}
		};
		// 开始监听键盘是否显示
		getWindow().getDecorView().getViewTreeObserver()
				.addOnGlobalLayoutListener(ogll);
		webview = (WebView) findViewById(R.id.webview);
		back = (ImageView) findViewById(R.id.back_find_1);
		button = (ImageView) findViewById(R.id.button);
		set = (TextView) findViewById(R.id.set);
		set.setText(title);
		Intent intent=getIntent();
		url=intent.getStringExtra("url");
		webview.loadUrl(url);
		webview.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setPluginState(PluginState.ON);
		
		// 在js中调用本地java方法
		webview.addJavascriptInterface(new jsToAndroid(FindActivity.this), "mobile");
		// 添加客户端支持
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				set.setText(title);
			}
		});
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);
				return true;
			}

		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				unRegister();
				FindActivity.this.finish();
			}
		});

	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void unRegister() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().getDecorView().getViewTreeObserver()
					.removeOnGlobalLayoutListener(ogll);
		} else {
			getWindow().getDecorView().getViewTreeObserver()
					.removeGlobalOnLayoutListener(ogll);
		}
	}
	// js调用借口类
		public class jsToAndroid extends BaseJsToAndroid{

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
						Intent intent = new Intent(FindActivity.this,
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
			public void setRefreshDynid(String dynid,String str){
				
				SharedPreferencesUtils.setParam(FindActivity.this, "dynid_find1", dynid);
				SharedPreferencesUtils.setParam(FindActivity.this, "fr_find1", str);
				SharedPreferencesUtils.setParam(FindActivity.this, "dynid_find", dynid);
				SharedPreferencesUtils.setParam(FindActivity.this, "fr_find", str);
				
			}
		}
		/**
		 * 覆盖手机返回键
		 */
		 @Override
		    public boolean dispatchKeyEvent(KeyEvent event) {
		        if (event.getAction() == KeyEvent.ACTION_UP &&
		                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		            if (panel_root.getVisibility() != View.GONE) {
		                KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root);
		                panel_root.setVisibility(View.GONE);
		                return true;
		            }
		        }
		        return super.dispatchKeyEvent(event);
		    }
		@Override
		public void onBackPressed() {
			unRegister();
			finish();
		}
}
