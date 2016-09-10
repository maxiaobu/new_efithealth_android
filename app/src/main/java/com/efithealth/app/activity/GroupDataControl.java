package com.efithealth.app.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.task.VolleySingleton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GroupDataControl extends BaseActivity {

	private EditText et1,et2;
	private String str1="",str2="";
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.group_data_control_activity);

		str1=getIntent().getStringExtra("str1");
		str2=getIntent().getStringExtra("str2");
		
		ImageView back = (ImageView) findViewById(R.id.back_control_data);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		et1=(EditText) findViewById(R.id.data_control_et1);
		et2=(EditText) findViewById(R.id.data_control_et2);
		
		et1.setText(str1);
		et2.setText(str2);
		Button btn=(Button) findViewById(R.id.control_btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				str1=et1.getText().toString();
				str2=et2.getText().toString();
				getData(1, Constant.URL_GROUP_MES);
			}
		});
	}
	
	private void getData(int type,String url){
		RequestQueue queue=VolleySingleton.getVolleySingleton(this).getRequestQueue();
		StringRequest request=new StringRequest(Method.POST, url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.i("GroupDataControl", response);
				try {
					//{"msgFlag":"1","msgContent":"","1":"修改信息成功"}

					JSONObject jsonObject= new JSONObject(response);
					String msg=jsonObject.getString("msgFlag");
					if (msg.equals("1")) {
						Toast.makeText(GroupDataControl.this, "修改信息成功", 0).show();
						GroupSimpleDetailActivity.flag_re=true;
					}else{
						Toast.makeText(GroupDataControl.this, "修改信息失败，请重试", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map=new HashMap<>();
				map.put("groupid", getIntent().getStringExtra("id"));
				map.put("summary", str1);
				map.put("bulletin", str2);
				return map;
			}
		};
		queue.add(request);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
