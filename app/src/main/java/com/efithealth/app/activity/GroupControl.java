package com.efithealth.app.activity;

import com.efithealth.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GroupControl extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.group_control_activity);

		ImageView back = (ImageView) findViewById(R.id.back_control);
		RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.control_rl1);
		RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.control_rl2);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		rl1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent intent=new Intent(GroupControl.this,GroupDataControl.class);
				intent.putExtra("id", getIntent().getStringExtra("id"));
				intent.putExtra("str1", getIntent().getStringExtra("str1"));
				intent.putExtra("str2", getIntent().getStringExtra("str2"));
				startActivity(intent);
			}
		});
		
		rl2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent tttt = new Intent(GroupControl.this,
						GroupPersonActivity.class);
				tttt.putExtra("flag", "0");
				tttt.putExtra("id", getIntent().getExtras().getString("id"));
				startActivity(tttt);
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
