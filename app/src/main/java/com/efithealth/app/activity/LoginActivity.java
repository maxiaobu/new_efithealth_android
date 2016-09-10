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

import image.Bimp;
import image.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.app.MyApplication;
import com.efithealth.app.db.UserDao;
import com.efithealth.app.domain.User;
import com.efithealth.app.utils.CommonUtils;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.applib.controller.HXSDKHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	
	private EditText et_userNumber, et_userPass;
	private Button b_loginId;
	private String usernum, passnum;
	// 手机imei
	private String szImei;
	ProgressDialog dialog;
	
	private boolean progressShow = false;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		
		et_userNumber = (EditText) findViewById(R.id.userNumber);
		et_userPass = (EditText) findViewById(R.id.userPass);
		//测试用
		/*et_userNumber.setText("15840225546");
		et_userPass.setText("123456");*/
		
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		szImei = TelephonyMgr.getDeviceId();
		b_loginId = (Button) findViewById(R.id.loginId);
		

		b_loginId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {			
				
				usernum = et_userNumber.getText().toString();
				passnum = et_userPass.getText().toString();
				if (usernum.equals("") || passnum.equals("")) {
					ToastCommom.getInstance().ToastShow(LoginActivity.this, "手机号和密码不能为空！");
				} else {
					if (!CommonUtils.isNetWorkConnected(LoginActivity.this)) {
						ToastCommom.getInstance().ToastShow(LoginActivity.this, getString(R.string.network_isnot_available));						
						return;
					}else{					
						
						dialog.setMessage(getString(R.string.Is_landing));
						dialog.show();	
						progressShow = true;
						usernum = et_userNumber.getText().toString().trim();
						passnum = et_userPass.getText().toString().trim();
						Map<String, String> map = new HashMap<String, String>();

						map.put("mobphone", usernum); // 传递参数
						map.put("mempass", passnum);
						map.put("phonedeviceno", szImei);

						LoadDataFromServer task = new LoadDataFromServer(LoginActivity.this, Constant.URL_LOGIN, map);
						task.getData(new DataCallBack() {
							@Override
							public void onDataCallBack(JSONObject data) {
								if (data == null) {
									ToastCommom.getInstance().ToastShow(LoginActivity.this, "返回数据错误...");
								}
								try {
									String msgFlag = data.getString("msgFlag");
									if (msgFlag.equals("1")) {
										Bimp.bmp.clear();
										Bimp.drr.clear();
										Bimp.drrTop.clear();
										Bimp.max = 0;
										Bimp.act_bool = true;
										FileUtils.deleteDir();
										login(data);
									} else if (msgFlag.equals("0")) {
										dialog.dismiss();
										ToastCommom.getInstance().ToastShow(LoginActivity.this, "账号或密码错误...");
									} else {
										dialog.dismiss();
										ToastCommom.getInstance().ToastShow(LoginActivity.this, "服务器繁忙请重试...");
									}

								} catch (JSONException e) {
									dialog.dismiss();
									ToastCommom.getInstance().ToastShow(LoginActivity.this, "数据解析错误...");
									e.printStackTrace();
								}

							}

						});	
					}					
				}

			}
		});
	}
	
	/**
	 * 登录方法
	 * @param json
	 */
	private void login(final JSONObject json) {
		 try {
	            final String nickname =  "马小布";	     //  json.getString("nickname")
	            final String memid = "M000455";	  // json.getString("memid")
	            // 调用sdk登陆方法登陆聊天服务器
	            EMChatManager.getInstance().login(memid, passnum, new EMCallBack() {
	                @Override
	                public void onSuccess() {
	                	if(!progressShow){
	                		return;
	                	}
	                    // 登陆成功，保存用户名密码
	                    MyApplication.getInstance().setUserName(memid);
	                    MyApplication.getInstance().setPassword(passnum);
	                  /*  runOnUiThread(new Runnable() {
	                        public void run() {
	                            dialog.setMessage(getString(R.string.list_is_for));
	                        }
	                    });*/
	                    try {
	                    	 SharedPreferences sp = getSharedPreferences("memidcun", Context.MODE_PRIVATE);  
	                         SharedPreferences.Editor editor = sp.edit();
	                         editor.putString( "memid", memid);
	                         editor.commit();
	                    	SharedPreferencesUtils.setParam(LoginActivity.this, "memid", memid);
		    				SharedPreferencesUtils.setParam(LoginActivity.this, "nickname", nickname);
	                    	m_handler.sendMessage(new Message());
	                     // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
	    					// ** manually load all local groups and
	    				    EMGroupManager.getInstance().loadAllGroups();
	    					EMChatManager.getInstance().loadAllConversations();
	    					// 处理好友和群组
	    					//initializeContacts();

	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        // 取好友或者群聊失败，不让进入主页面
	                        runOnUiThread(new Runnable() {
	                            public void run() {
	                                dialog.dismiss();
	                                MyApplication.getInstance().logout(true,null);
	                              //  ToastCommom.getInstance().ToastShow(LoginActivity.this, getString(R.string.login_failure_failed));
	                            }
	                        });
	                        return;
	                    }
	                    // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
	    				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(nickname);
	    			/*	if (!updatenick) {
	    					Log.e("LoginActivity", "update current user nick fail");
	    				}
	    				if (!LoginActivity.this.isFinishing() && dialog.isShowing()) {
	    					
	    				}*/
	    				
	    				dialog.dismiss();
	    				// 进入主页面
	    				startActivity(new Intent(LoginActivity.this,MainActivity.class));
	    				
	    				finish();
	                }

	                @Override
	                public void onProgress(int progress, String status) {
	                }

	                @Override
	                public void onError(final int code, final String message) {
	                	if (!progressShow) {
	    					return;
	    				}
	    				runOnUiThread(new Runnable() {
	    					public void run() {
	    						dialog.dismiss();
		                        ToastCommom.getInstance().ToastShow(LoginActivity.this, getString(R.string.Login_failed)+message);
	    					}
	    				});

	                }
	            });

	        } catch (JSONException e1) {
	        	dialog.dismiss();
	            e1.printStackTrace();
	        }

	}	

	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	public void forgetPass(View v) {
		startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
	}

	public void register(View v) {
		startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
	}
	public Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	updateData();
        }
           
    };
	public void updateData(){
		MyApplication.getInstance().update_loacl_friend();
 		MyApplication.getInstance().update_local_myinfo();
 		MyApplication.getInstance().update_loacl_indexdata();
	}
}
