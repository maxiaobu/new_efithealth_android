package com.efithealth.app.activity;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.app.MyApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	
	
	private static final int sleepTime = 3000;

	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}

		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);

	

		
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		new Thread(new Runnable() {
			public void run() {
				if (DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					Message message = new Message();
					m_handler.sendMessage(message);
					long start = System.currentTimeMillis();
					//EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					
					
					
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();

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
