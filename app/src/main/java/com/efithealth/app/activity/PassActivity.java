package com.efithealth.app.activity;

import com.efithealth.R;
import com.efithealth.app.utils.ToastCommom;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

public class PassActivity extends Activity {

	public static final int RESULT = 0;
	public String mobphone;
	public String checkbox_state = "0";
	public CheckBox checkbox;
	private EditText passId, passIdA;

	String strPass, strPassA;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pass);
		checkbox = (CheckBox) findViewById(R.id.checkbox);

		passId = (EditText) findViewById(R.id.passId);
		passIdA = (EditText) findViewById(R.id.passIdA);

		mobphone = getIntent().getStringExtra("mobphone");
		checkbox_state = getIntent().getStringExtra("checkbox");
		checkbox.setChecked(checkbox_state.equals("1"));
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.agree:
			startActivityForResult(new Intent(PassActivity.this, RegisterAgreeActivity.class), RESULT);
			break;
		case R.id.nextId1:
			strPass = passId.getText().toString();
			strPassA = passIdA.getText().toString();
			if (!strPass.equals("")) {
				if (!strPass.equals(strPassA)) {
					ToastCommom.getInstance().ToastShow(PassActivity.this, "密码不一致");
				} else {
					Intent intent = new Intent();
					intent.setClass(PassActivity.this, NickNameActivity.class);
					intent.putExtra("strPass", strPass);
					intent.putExtra("mobphone", mobphone);
					intent.putExtra("checkbox", checkbox.isChecked() ? "1" : "0");
					startActivity(intent);
					finish();
				}
			} else {
				ToastCommom.getInstance().ToastShow(PassActivity.this, "密码不能为空");
			}
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

}
