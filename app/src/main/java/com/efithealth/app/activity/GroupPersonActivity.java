package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.adapter.GroupPersonAdapter;
import com.efithealth.app.javabean.GroupPerson;
import com.efithealth.app.javabean.Member;
import com.efithealth.app.javabean.MemberMolder;
import com.efithealth.app.javabean.Memberlist;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupPersonActivity extends BaseActivity {

	private GroupPersonAdapter adapter;
	private List<Memberlist> list = new ArrayList<>();
	private ListView groupLv;
	private String flag = "";
	private TextView tv_name;
	private LinearLayout ll_error;

	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.group_person_activity);
		flag = getIntent().getStringExtra("flag");
		ll_error = (LinearLayout) findViewById(R.id.ll_error);
		ImageView back = (ImageView) findViewById(R.id.back_group_person);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (flag.equals("2")) {
					upViewData();
				} else if (flag.equals("0")) {
					GroupSimpleDetailActivity.flag_re = true;
					GroupSimpleDetailActivity.flag_re1 = true;
				}
				finish();
			}
		});
		tv_name = (TextView) findViewById(R.id.set_group_person);
		groupLv = (ListView) findViewById(R.id.group_person_lv);
		if (flag.equals("2")) {
			tv_name.setText("入群申请");
			getData(1, Constant.URL_GROUP_SH);
		} else if (flag.equals("1")) {
			tv_name.setText("群成员");
			getData(1, Constant.URL_GROUP_PERSON);
		} else {
			tv_name.setText("群成员管理");
			getData(1, Constant.URL_GROUP_PERSON);
		}
	}

	private void getData(final int key, String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(this)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("djy", "GroupDynamicActivity" + response);
						Gson gson = new Gson();
						GroupPerson member = gson.fromJson(response,
								GroupPerson.class);
						String msg = member.getMsgFlag();
						String con = member.getMsgContent();
						if (msg.equals("1")) {
							list = member.getMemberlist();
							if (list != null) {
								if (list.size() > 0) {
									ll_error.setVisibility(View.GONE);
									showData(key);
								} else {
									ll_error.setVisibility(View.VISIBLE);
								}
							} else {
								ll_error.setVisibility(View.VISIBLE);
							}
						}

					}

				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("groupid", getIntent().getStringExtra("id"));
				return map;
			}
		};
		queue.add(request);
	}

	private void showData(int key) {
		switch (key) {
		case 1:
			adapter = new GroupPersonAdapter(GroupPersonActivity.this, list,
					getIntent().getStringExtra("id"), flag);
			groupLv.setAdapter(adapter);
			break;
		}

	}

	private void upViewData() {
		setResult(RESULT_OK, null);
	}

	@Override
	public void onBackPressed() {
		if (flag.equals("2")) {
			upViewData();
		} else if (flag.equals("0")) {
			GroupSimpleDetailActivity.flag_re = true;
			GroupSimpleDetailActivity.flag_re1 = true;
		}
		finish();
		super.onBackPressed();
	}
}
