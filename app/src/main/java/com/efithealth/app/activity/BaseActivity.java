/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.efithealth.app.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.efithealth.applib.controller.HXSDKHelper;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// 处理小米系统的状态栏
		Window window = getWindow();
		Class<? extends Window> clazz = window.getClass();
		int tranceFlag = 0;
		int darkModeFlag = 0;
		Class<?> layoutParams;
		try {
			layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
			tranceFlag = field.getInt(layoutParams);
			field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			// 只需要状态栏透明
			// extraFlagField.invoke(window, tranceFlag, tranceFlag);
			// 状态栏透明且黑色字体
			extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag
					| darkModeFlag);
			// 清除黑色字体
			// extraFlagField.invoke(window, 0, darkModeFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();

		// umeng
		MobclickAgent.onResume(this);
	}



	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	// 获取点击事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
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
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
