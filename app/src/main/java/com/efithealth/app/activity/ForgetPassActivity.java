package com.efithealth.app.activity;



import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.ToastCommom;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPassActivity extends Activity {
	
	private Button sendId, sendIdYin,sendIdNext;
	private TimeCount time;
    private EditText sendPassId,number;
    private String phonenum,code;
  
	
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpass);
		
		sendId = (Button) findViewById(R.id.sendId);
		sendIdYin = (Button) findViewById(R.id.sendIdYin);
		sendIdNext = (Button) findViewById(R.id.sendIdNext);
		sendPassId = (EditText) findViewById(R.id.sendPassId);	
		number = (EditText) findViewById(R.id.number);
		
		time = new TimeCount(60000, 1000);
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.toRegister:
			startActivity(new Intent(ForgetPassActivity.this,RegisterActivity.class));
			finish();			
			break;
		case R.id.toLogin:
			finish();
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
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			sendId.setVisibility(View.VISIBLE);
			sendIdYin.setVisibility(View.GONE);
			sendId.setText("重新发送验证码");
			time.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
//			submit.setClickable(false);// 防止重复点击
			sendIdYin.setText(millisUntilFinished / 1000 + "(s)");
		}
	}
	public void sendcode(){
		phonenum = number.getText().toString();
		if (!phonenum.equals("")) {
			if (phonenum.length() == 11) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobphone", phonenum); // 传递参数
				LoadDataFromServer task = new LoadDataFromServer(ForgetPassActivity.this, Constant.URL_SENDCODE_FORGET, map);
				task.getData(new DataCallBack() {
					@Override
					public void onDataCallBack(JSONObject data) {
						if (data == null) {
							ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "服务器繁忙...");
						}else{
							try {
								String msgFlag = data.getString("msgFlag");
								if (msgFlag.equals("1")) {
									ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "已发送");
									
									
									sendId.setVisibility(View.GONE);
									sendIdYin.setVisibility(View.VISIBLE);									
									time.start();
									
									sendPassId.addTextChangedListener(new TextWatcher() {

										
										@Override
										public void afterTextChanged(Editable arg0) {
											// TODO Auto-generated method stub
											 code = sendPassId.getText().toString();
											if (code.equals("") || code.length() != 4) {

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
								}else{
									ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, data.getString("msgContent"));
									finish();
								}
							}catch (JSONException e) {							
								ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "数据解析错误...");
								e.printStackTrace();
							}
						}
						
					}
					
				});			
			} else {
				ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "手机号为11位数字！");				
			}
		} else {
			ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "手机号不能为空！");				
		}
	}
	public void iscodeok(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobphone", phonenum); // 传递参数
		map.put("identcode", code); // 传递参数
		
		LoadDataFromServer task = new LoadDataFromServer(ForgetPassActivity.this, Constant.URL_SENDCODE_CHECK_FORGET, map);
		
		task.getData(new DataCallBack() {			
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "服务器繁忙...");
				}else{
					try {
						String msgFlag = data.getString("msgFlag");
						if (msgFlag.equals("1")) {
							Intent intent = new Intent();
							intent.setClass(ForgetPassActivity.this, ResetPassActivity.class);
							intent.putExtra("mobphone", phonenum);
							startActivity(intent);							
							finish();
						}else{
							sendPassId.setText("");
							sendId.setVisibility(View.VISIBLE);
							sendId.setText("发送短信验证码");
							sendIdNext.setVisibility(View.GONE);							
							ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, data.getString("msgContent"));
							
						}
					}catch (JSONException e) {							
						ToastCommom.getInstance().ToastShow(ForgetPassActivity.this, "数据解析错误...");
						e.printStackTrace();
					}
				}
				
			}
		});
	}
}
