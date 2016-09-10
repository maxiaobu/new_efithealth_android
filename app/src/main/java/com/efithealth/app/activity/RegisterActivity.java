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

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {

	private static final int RESULT = 0;

	public CheckBox checkbox;
	public Button send_button, sendIdYin, sendIdNext;
	public EditText number;
	public EditText code;
	public TimeCount time;
	public String str_code;
	public String phonenum;
	public String checkbox_state = "0";

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		send_button = (Button) findViewById(R.id.sendId);
		sendIdYin = (Button) findViewById(R.id.sendIdYin);
		number = (EditText) findViewById(R.id.number);
		code = (EditText) findViewById(R.id.sendPassId);
		sendIdNext = (Button) findViewById(R.id.sendIdNext);
		time = new TimeCount(60000, 1000);
	}

	public void onTextClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.agree:
			startActivityForResult(new Intent(RegisterActivity.this, RegisterAgreeActivity.class), RESULT);
			break;
		case R.id.sendId:
			sendcode();
			break;
		case R.id.sendIdNext:
			iscodeok();
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RESULT:
				String result = data.getExtras().getString("result");
				if (result.equals("agree")) {
					checkbox.setChecked(true);
					checkbox_state = "1";
				}
				break;
			default:
				break;
			}
		}
	}

	private void sendcode() {
		phonenum = number.getText().toString();
		if (!phonenum.equals("")) {
			if (phonenum.length() == 11) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobphone", phonenum); // 传递参数
				LoadDataFromServer task = new LoadDataFromServer(RegisterActivity.this, Constant.URL_SENDCODE, map);
				task.getData(new DataCallBack() {
					@Override
					public void onDataCallBack(JSONObject data) {
						if (data == null) {
							ToastCommom.getInstance().ToastShow(RegisterActivity.this, "服务器繁忙...");
						} else {
							try {
								String msgFlag = data.getString("msgFlag");
								if (msgFlag.equals("1")) {
									ToastCommom.getInstance().ToastShow(RegisterActivity.this, "已发送");

									send_button.setVisibility(View.GONE);
									sendIdYin.setVisibility(View.VISIBLE);
									time.start();

									code.addTextChangedListener(new TextWatcher() {

										@Override
										public void afterTextChanged(Editable arg0) {
											// TODO Auto-generated method stub
											str_code = code.getText().toString();
											if (str_code.equals("") || str_code.length() != 4) {

											} else {
												sendIdYin.setVisibility(View.GONE);
												sendIdNext.setVisibility(View.VISIBLE);
											}
										}

										@Override
										public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
										}

										@Override
										public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
										}
									});
								} else {
									ToastCommom.getInstance().ToastShow(RegisterActivity.this,
											data.getString("msgContent"));
									finish();
								}
							} catch (JSONException e) {
								ToastCommom.getInstance().ToastShow(RegisterActivity.this, "数据解析错误...");
								e.printStackTrace();
							}
						}

					}

				});
			} else {
				ToastCommom.getInstance().ToastShow(RegisterActivity.this, "手机号为11位数字！");
			}
		} else {
			ToastCommom.getInstance().ToastShow(RegisterActivity.this, "手机号不能为空！");
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			send_button.setVisibility(View.VISIBLE);
			sendIdYin.setVisibility(View.GONE);
			send_button.setText("重新发送验证码");
			time.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			// submit.setClickable(false);// 防止重复点击
			sendIdYin.setText(millisUntilFinished / 1000 + "(s)");
		}
	}

	public void iscodeok() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobphone", phonenum); // 传递参数
		map.put("identcode", str_code); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(RegisterActivity.this, Constant.URL_SENDCODE_CHECK, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(RegisterActivity.this, "服务器繁忙...");
				} else {
					try {
						String msgFlag = data.getString("msgFlag");
						if (msgFlag.equals("1")) {
							Intent intent = new Intent();
							intent.setClass(RegisterActivity.this, PassActivity.class);
							intent.putExtra("mobphone", phonenum);
							intent.putExtra("checkbox", checkbox.isChecked() ? "1" : "0");
							startActivity(intent);
							finish();
						} else {
							code.setText("");
							send_button.setVisibility(View.VISIBLE);
							send_button.setText("发送短信验证码");
							sendIdNext.setVisibility(View.GONE);
							ToastCommom.getInstance().ToastShow(RegisterActivity.this, data.getString("msgContent"));
						}
					} catch (JSONException e) {
						ToastCommom.getInstance().ToastShow(RegisterActivity.this, "数据解析错误...");
						e.printStackTrace();
					}
				}

			}

		});
	}
}
