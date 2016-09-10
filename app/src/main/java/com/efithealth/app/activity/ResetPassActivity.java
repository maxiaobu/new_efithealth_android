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
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ResetPassActivity extends Activity {

	private Button nextId;
	private EditText passId,passIdA;
	private String strPass,strPassA;

	private String mobphone;
    private TextView textview;
	private CheckBox checkbox;
	
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pass);
		
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		checkbox.setVisibility(View.INVISIBLE);
		textview = (TextView) findViewById(R.id.agree);
		textview.setVisibility(View.INVISIBLE);
		nextId = (Button) findViewById(R.id.nextId1);
		nextId.setText("重置密码");
		passId = (EditText) findViewById(R.id.passId);
		passIdA = (EditText) findViewById(R.id.passIdA);
		mobphone = getIntent().getStringExtra("mobphone");
		
		
	}
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;		
		case R.id.nextId1:
			strPass = passId.getText().toString();
			strPassA = passIdA.getText().toString();
			if(!strPass.equals("")){
			if(!strPass.equals(strPassA)){
				ToastCommom.getInstance().ToastShow(ResetPassActivity.this, "密码不一致");				
			}else{
				resetpassword();
			}
			}else{
				ToastCommom.getInstance().ToastShow(ResetPassActivity.this, "密码不能为空");		
			}
			break;		
		default:
			break;
		}
	}
	public void resetpassword(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobphone", mobphone); // 传递参数
		map.put("mempass", strPass); // 传递参数
		
		LoadDataFromServer task = new LoadDataFromServer(ResetPassActivity.this, Constant.URL_RESETPASSWORD, map);
		
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(ResetPassActivity.this, "服务器繁忙...");
				}else{
					try {
						String msgFlag = data.getString("msgFlag");
						if (msgFlag.equals("1")) {
							ToastCommom.getInstance().ToastShow(ResetPassActivity.this, data.getString("msgContent"));
							finish();
						}else{
													
							ToastCommom.getInstance().ToastShow(ResetPassActivity.this, data.getString("msgContent"));
							
						}
					}catch (JSONException e) {							
						ToastCommom.getInstance().ToastShow(ResetPassActivity.this, "数据解析错误...");
						e.printStackTrace();
					}
				}
				
			}		
			
		});
	}
}
