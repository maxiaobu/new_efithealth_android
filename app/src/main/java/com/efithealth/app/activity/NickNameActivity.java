package com.efithealth.app.activity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.ToastCommom;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NickNameActivity extends Activity {

	public static final int RESULT = 0;
	private String strPass, sex, nick;
	private String mobphone;
	private EditText nickName;
	private RadioGroup rdg;
	private RadioButton boy, girl;

	private String szImei;

	public String checkbox_state = "0";
	public CheckBox checkbox;

	ProgressDialog dialog;

	private boolean progressShow = false;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_nickname);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		strPass = getIntent().getStringExtra("strPass");
		mobphone = getIntent().getStringExtra("mobphone");
		checkbox_state = getIntent().getStringExtra("checkbox");
		Log.i("NickNameActivity1", checkbox_state);
		checkbox.setChecked(checkbox_state.equals("1"));
		nickName = (EditText) findViewById(R.id.regId);
		rdg = (RadioGroup) findViewById(R.id.rdgId);

		boy = (RadioButton) findViewById(R.id.boy);
		girl = (RadioButton) findViewById(R.id.girl);

		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		szImei = TelephonyMgr.getDeviceId();

		dialog = new ProgressDialog(NickNameActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		

		rdg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == boy.getId()) {
					sex = "1";
				}
				if (arg1 == girl.getId()) {
					sex = "0";
				}
			}
		});
		
		
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.agree:
			startActivityForResult(new Intent(NickNameActivity.this, RegisterAgreeActivity.class), RESULT);
			break;
		case R.id.regOrLogin:
			if (checkbox.isChecked()) {
				checkbox_state = "1";
				Log.i("NickNameActivity3", checkbox_state);
			}else{
				checkbox_state = "0";
			}
			register();
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
					Log.i("NickNameActivity", checkbox_state);
				}
				break;
			default:
				break;
			}
		}
	}

	public void register() {
		nick = nickName.getText().toString();
		if (nick.equals("")) {
			ToastCommom.getInstance().ToastShow(NickNameActivity.this, "昵称不能为空");
			return;
		}
		if (checkbox_state.equals("0")) {
			ToastCommom.getInstance().ToastShow(NickNameActivity.this, "请勾选同意《弈健康服务协议》");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("mempass", strPass); // 传递参数
		map.put("nickname", nick); // 传递参数
		map.put("gender", sex); // 传递参数
		map.put("mobphone", mobphone); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(NickNameActivity.this, Constant.URL_REGISTER, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(NickNameActivity.this, "服务器繁忙...");
				} else {
					try {
						String msgFlag = data.getString("msgFlag");
						if (msgFlag.equals("1")) {
							ToastCommom.getInstance().ToastShow(NickNameActivity.this, data.getString("msgContent"));
							Login();
						} else {
							ToastCommom.getInstance().ToastShow(NickNameActivity.this, data.getString("msgContent"));
						}
					} catch (JSONException e) {
						ToastCommom.getInstance().ToastShow(NickNameActivity.this, "数据解析错误...");
						e.printStackTrace();
					}
				}

			}
		});

	}

	public void Login() {
		dialog.setMessage(getString(R.string.Is_landing));
		dialog.show();
		progressShow = true;
		Map<String, String> map = new HashMap<String, String>();

		map.put("mobphone", mobphone); // 传递参数
		map.put("mempass", strPass);
		map.put("phonedeviceno", szImei);

		LoadDataFromServer task = new LoadDataFromServer(NickNameActivity.this, Constant.URL_LOGIN, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(NickNameActivity.this, "服务器繁忙...");
				}
				try {
					String msgFlag = data.getString("msgFlag");
					if (msgFlag.equals("1")) {

						HXLogin(data);
					} else if (msgFlag.equals("0")) {
						dialog.dismiss();
						ToastCommom.getInstance().ToastShow(NickNameActivity.this, "账号或密码错误...");
					} else {
						dialog.dismiss();
						ToastCommom.getInstance().ToastShow(NickNameActivity.this, "服务器繁忙请重试...");
					}

				} catch (JSONException e) {
					dialog.dismiss();
					ToastCommom.getInstance().ToastShow(NickNameActivity.this, "数据解析错误...");
					e.printStackTrace();
				}

			}

		});

	}

	private void HXLogin(final JSONObject json) {
		try {
			final String nickname = json.getString("nickname");
			final String memid = json.getString("memid");
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(memid, strPass, new EMCallBack() {
				@Override
				public void onSuccess() {
					if (!progressShow) {
						return;
					}
					// 登陆成功，保存用户名密码
					MyApplication.getInstance().setUserName(memid);
					MyApplication.getInstance().setPassword(strPass);
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.setMessage(getString(R.string.list_is_for));
						}
					});
					try {
						// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
						// ** manually load all local groups and
						m_handler.sendMessage(new Message());
						// EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						// 处理好友和群组
						// initializeContacts();

					} catch (Exception e) {
						e.printStackTrace();
						// 取好友或者群聊失败，不让进入主页面
						runOnUiThread(new Runnable() {
							public void run() {
								dialog.dismiss();
								MyApplication.getInstance().logout(true, null);
								ToastCommom.getInstance().ToastShow(NickNameActivity.this,
										getString(R.string.login_failure_failed));
							}
						});
						return;
					}
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(nickname);
					if (!updatenick) {
						Log.e("LoginActivity", "update current user nick fail");
					}
					if (!NickNameActivity.this.isFinishing() && dialog.isShowing()) {
						dialog.dismiss();
					}
					SharedPreferencesUtils.setParam(NickNameActivity.this, "memid", memid);
					SharedPreferencesUtils.setParam(NickNameActivity.this, "nickname", nickname);
					// 进入主页面
					startActivity(new Intent(NickNameActivity.this, MainActivity.class));

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
							ToastCommom.getInstance().ToastShow(NickNameActivity.this,
									getString(R.string.Login_failed) + message);
						}
					});

				}
			});

		} catch (JSONException e1) {
			dialog.dismiss();
			e1.printStackTrace();
		}

	}

	public Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateData();
		}

	};

	public void updateData() {
		MyApplication.getInstance().update_loacl_friend();
		MyApplication.getInstance().update_local_myinfo();
		MyApplication.getInstance().update_loacl_indexdata();
	}
}
