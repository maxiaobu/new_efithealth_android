package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.task.SendMyInfoTask;
import com.efithealth.app.task.SendMyInfoTask.DataCallBack;
import com.efithealth.app.utils.ToastCommom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class RevampAddress extends Activity {
	private EditText K_nick, K_phone, K_address;
	private ImageView back;
	private ProgressDialog dialog;
	private Map<String, String> map;
	private List<String> lists = new ArrayList<String>();
	private String topidList="";

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.activity_personalinfo_address);
		super.onCreate(arg0);
		initView();
		initData();
	}

	private void initView() {
		K_nick = (EditText) findViewById(R.id.K_nick_address);
		K_phone = (EditText) findViewById(R.id.K_phone_address);
		K_address = (EditText) findViewById(R.id.K_address_address);
		back = (ImageView) findViewById(R.id.back_address_address);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	private void initData() {
		map = MyApplication.getInstance().getMyInfo();
		K_nick.setText(map.get("addressname"));
		K_phone.setText(map.get("addresphone"));
		K_address.setText(map.get("address"));

	}

	public void setTopImg() {
		lists.clear();
		Map<String, String> map_top = MyApplication.getInstance()
				.getImgWallInfo();
		String topImgList = map_top.get("topImgList");
		topidList = map_top.get("topidList");

		if (!topImgList.equals("")) {
			String[] imglist = topImgList.split(",");
			for (int i = 0; i < imglist.length; i++) {
				lists.add(imglist[i]);
			}
		}

	}


	// 确认修改
	public void btn_myinfo_click_address(View v) {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存中...");
		dialog.show();
		Map<String, String> content = new HashMap<String, String>();
		content.put("memid", MyApplication.getInstance().getMemid());
		content.put("nickname", map.get("nickname"));
		content.put("signature", map.get("mysign"));
		content.put("recaddress", K_address.getText().toString());
		content.put("recname", K_nick.getText().toString());
		content.put("recphone", K_phone.getText().toString());
		content.put("birthday", map.get("brithday"));
		content.put("gender", map.get("sex"));
		content.put("dimg",topidList);

		SendMyInfoTask task = new SendMyInfoTask(RevampAddress.this,
				Constant.URL_MYINFO_UPDATE, lists, map.get("headImgUrl"),
				content);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				dialog.dismiss();
				if (data == null) {
					ToastCommom.getInstance().ToastShow(RevampAddress.this,
							"服务器连接错误...");
					return;
				}

				String code = data.getString("msgFlag");// ["1","1"]
				Log.i("String code1", code);
				if (code.equals("[\"1\",\"1\"]")) {
					Log.i("flag", code.equals("[\"1\",\"1\"]") + "");
					ToastCommom.getInstance().ToastShow(RevampAddress.this,
							"修改成功");
					MyApplication.getInstance().update_local_myinfo();
					Intent intent=new Intent();
					intent.putExtra("nick", K_nick.getText().toString());
					intent.putExtra("phone", K_phone.getText().toString());
					intent.putExtra("address", K_address.getText().toString());
					RevampAddress.this.setResult(11,intent);
					finish();
				} else {
					ToastCommom.getInstance().ToastShow(RevampAddress.this,
							"服务器端错误:" + data.getString("msgContent"));
				}

			}
		});
	}

}
