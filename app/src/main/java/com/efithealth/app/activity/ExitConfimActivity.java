package com.efithealth.app.activity;

import com.easemob.EMCallBack;
import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/*
 * 退出登录选项
 */

public class ExitConfimActivity extends Activity {

	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exitconfim);
		layout=(LinearLayout)findViewById(R.id.exit_layout2);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
		
			MyApplication.getInstance().logout(true, new EMCallBack() {

				@Override
				public void onError(int arg0, String arg1) {
					
					
				}

				@Override
				public void onProgress(int arg0, String arg1) {
					
					
				}

				@Override
				public void onSuccess() {
					FragmentHome.flag=true;
					// 重新显示登陆页面
					MainActivity.instance.finish();
					SharedPreferencesUtils.clearData(ExitConfimActivity.this);
					finish();
					startActivity(new Intent(ExitConfimActivity.this, LoginActivity.class));
					
					
				}
				
				
			});
    	
    }  
	
	
}
