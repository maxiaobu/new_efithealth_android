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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.fragment.FragmentFriendGroup;
import com.efithealth.app.javabean.Group;
import com.efithealth.app.javabean.GroupDetailsModle;
import com.efithealth.app.javabean.GroupPerson;
import com.efithealth.app.javabean.Memberlist;
import com.efithealth.app.javabean.PublicGroup;
import com.efithealth.app.task.VolleySingleton;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GroupSimpleDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back, iv_head, iv_head_q, iv_gl, iv_gl_q;
	private TextView tv_name, tv_fl, tv_introduction, tv_address, tv_gg,
			tv_name_q, tv_jj_q, tv_num, tv_qcy, tv_commit,tv_num1;
	private LinearLayout ll_ewm, ll_dyn;
	private String clubid = "", img = "", name = "", str1 = "", str2 = "",
			glyid = "";
	private int flag = 0;
	private List<Memberlist> list = new ArrayList<>();
	public static boolean flag_re = false;
	public static boolean flag_re1 = false;
	private GridView gv;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_group_simle_details);
		glyid = getIntent().getExtras().getString("managerid");
		initView();
		if (glyid.equals(MyApplication.getInstance().getMemid())) {
			iv_gl.setVisibility(View.VISIBLE);
			iv_gl_q.setVisibility(View.VISIBLE);
			tv_commit.setVisibility(View.GONE);
			tv_commit.setEnabled(false);
			iv_gl.setEnabled(true);
			iv_gl_q.setEnabled(true);
		} else {
			iv_gl.setVisibility(View.GONE);
			iv_gl_q.setVisibility(View.GONE);
			tv_commit.setVisibility(View.VISIBLE);
			tv_commit.setEnabled(true);
			iv_gl.setEnabled(false);
			iv_gl_q.setEnabled(false);
		}

		getData(1, Constant.URL_GROUP_DEATILS);
		getData(2, Constant.URL_GROUP_PERSON);
		getData(3, Constant.URL_ME);
	}

	/**
	 * 
	 */
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.back_sim_group);
		iv_gl = (ImageView) findViewById(R.id.gl_sim_group);
		iv_gl_q = (ImageView) findViewById(R.id.gl_sim_group_person);
		iv_head = (ImageView) findViewById(R.id.group_sim_avatar);
		iv_head_q = (ImageView) findViewById(R.id.group_sim_head_q);
		tv_name = (TextView) findViewById(R.id.group_sim_name);
		tv_fl = (TextView) findViewById(R.id.group_sim_fl);
		gv = (GridView) findViewById(R.id.qwerty);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		tv_address = (TextView) findViewById(R.id.group_sim_address);
		tv_gg = (TextView) findViewById(R.id.group_sim_gg);
		tv_name_q = (TextView) findViewById(R.id.group_sim_name_q);
		tv_jj_q = (TextView) findViewById(R.id.group_sim_jj_q);
		tv_num = (TextView) findViewById(R.id.group_sim_num);
		tv_num1 = (TextView) findViewById(R.id.group_sim_num1);
		tv_qcy = (TextView) findViewById(R.id.group_simle_details_dynamic_person);
		tv_commit = (TextView) findViewById(R.id.btn_add_to_group);
		ll_ewm = (LinearLayout) findViewById(R.id.group_sim_ewm);
		ll_dyn = (LinearLayout) findViewById(R.id.froup_simle_details_dynamic);

		gv.setEnabled(false);
		ll_ewm.setEnabled(false);
		tv_commit.setEnabled(false);
		iv_gl.setEnabled(false);
		iv_gl_q.setEnabled(false);
		iv_back.setOnClickListener(this);
		iv_gl.setOnClickListener(this);
		iv_gl_q.setOnClickListener(this);
		tv_commit.setOnClickListener(this);
		ll_ewm.setOnClickListener(this);
		ll_dyn.setOnClickListener(this);
		tv_qcy.setOnClickListener(this);

	}

	private void getData(final int type, String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(this)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("GroupSimpleDetailActivity", response);
						showData(type, response);
					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				if (type == 2) {
					map.put("groupid",
							getIntent().getExtras().getString("groupid"));
				} else if (type == 3) {
					map.put("memid",
							getIntent().getExtras().getString("managerid"));
				} else {
					map.put("groupid",
							getIntent().getExtras().getString("groupid"));
					map.put("memid", MyApplication.getInstance().getMemid());
				}
				return map;
			}
		};
		queue.add(request);
	}

	private void showData(int type, String json) {
		Gson gson = new Gson();
		if (type == 1) {
			GroupDetailsModle detailsModle = gson.fromJson(json,
					GroupDetailsModle.class);
			String msg = detailsModle.getMsgFlag();
			String con = detailsModle.getMsgContent();
			if (msg.equals("1")) {
				Group group = detailsModle.getGroup();
				clubid = group.getClubid();
				img = group.getImgsfilename();
				name = group.getGname();
				str1 = group.getSummary();
				str2 = group.getBulletin();
				ll_ewm.setEnabled(true);
				iv_gl.setEnabled(true);
				iv_gl_q.setEnabled(true);
				if (group.getMemcheckstatus().equals("0")) {
					tv_commit.setText("正在审核");
					flag = 0;
				} else if (group.getMemcheckstatus().equals("1")) {
					tv_commit.setText("退出该群");
					flag = 2;
				} else if (group.getMemcheckstatus().equals("2")) {
					tv_commit.setText("申请被驳回，重新申请入群");
					flag = 1;
				} else {
					tv_commit.setText("申请入群");
					flag = 1;
				}
				tv_commit.setEnabled(true);
				ImageLoader.getInstance().displayImage(Constant.URL_RESOURCE+group.getImgsfile(),
						iv_head,
						MyApplication.getInstance().initHeadDisImgOption());
				tv_name.setText(group.getGname());
				tv_fl.setText(group.getGsortname());
				tv_introduction.setText(str1);
				tv_address.setText(group.getAddress());
				tv_gg.setText(str2);
				tv_num.setText(group.getMemberuplimit());
			} else {
				Toast.makeText(GroupSimpleDetailActivity.this, con, 0).show();
			}
		} else if (type == 2) {
			GroupPerson member = gson.fromJson(json, GroupPerson.class);
			String msg = member.getMsgFlag();
			String con = member.getMsgContent();
			if (msg.equals("1")) {
				list = member.getMemberlist();
				tv_num1.setText(list.size() + "/" );
				gv.setAdapter(new MyAdapter());
			} else {
				Toast.makeText(GroupSimpleDetailActivity.this, con, 0).show();
			}
		} else if (type == 3) {
			try {
				JSONObject object = new JSONObject(json);
				String msg = object.getString("msgFlag");
				String con = object.getString("msgContent");
				if (msg.equals("1")) {
					JSONObject obj = object.getJSONObject("member");
					ImageLoader.getInstance().displayImage(
							obj.getString("imgsfilename"), iv_head_q,
							MyApplication.getInstance().initHeadDisImgOption());
					tv_name_q.setText(obj.getString("nickname"));
					tv_jj_q.setText(obj.getString("signature"));

				} else {
					Toast.makeText(GroupSimpleDetailActivity.this, con, 0)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (type == 4) {
			try {
				JSONObject object = new JSONObject(json);
				String msg = object.getString("msgFlag");
				String con = object.getString("msgContent");
				if (msg.equals("1")) {
					tv_commit.setText("申请入群");
					Toast.makeText(GroupSimpleDetailActivity.this, "入群申请提交成功",
							0).show();
					tv_commit.setText("正在审核");
					flag = 0;
				} else {
					Toast.makeText(GroupSimpleDetailActivity.this, con, 0)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (type == 5) {
			try {
				JSONObject object = new JSONObject(json);
				String msg = object.getString("msgFlag");
				String con = object.getString("msgContent");
				if (msg.equals("1")) {
					Toast.makeText(GroupSimpleDetailActivity.this, "您已成功退出该群",
							0).show();

				} else {
					Toast.makeText(GroupSimpleDetailActivity.this, con, 0)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			GroupDetailsModle detailsModle = gson.fromJson(json,
					GroupDetailsModle.class);
			String msg = detailsModle.getMsgFlag();
			String con = detailsModle.getMsgContent();
			if (msg.equals("1")) {
				Group group = detailsModle.getGroup();
				tv_introduction.setText(group.getSummary());
				tv_gg.setText(group.getBulletin());
			} else {
				Toast.makeText(GroupSimpleDetailActivity.this, con, 0).show();
			}
		}
	}

	@Override
	protected void onResume() {
		if (flag_re) {
			Log.i("djy", "onResume");
			if (flag_re1) {
				getData(2, Constant.URL_GROUP_PERSON);
				flag_re1 = false;
			}else{
				getData(6, Constant.URL_GROUP_DEATILS);
				FragmentFriendGroup.flag=true;
			}
			flag_re = false;
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0==2) {
				Log.i("djy", "onActivityResult+       2");
				getData(2, Constant.URL_GROUP_PERSON);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onClick(View key) {
		switch (key.getId()) {
		case R.id.back_sim_group:
			finish();
			break;

		case R.id.gl_sim_group:
			Intent ttt = new Intent(GroupSimpleDetailActivity.this,
					GroupControl.class);
			ttt.putExtra("id", getIntent().getExtras().getString("groupid"));
			ttt.putExtra("str1", str1);
			ttt.putExtra("str2", str2);
			startActivity(ttt);
			break;

		case R.id.gl_sim_group_person:
			Intent tttt = new Intent(GroupSimpleDetailActivity.this,
					GroupPersonActivity.class);
			tttt.putExtra("flag", "2");
			tttt.putExtra("id", getIntent().getExtras().getString("groupid"));
			startActivityForResult(tttt, 2);
			break;

		case R.id.btn_add_to_group:
			if (flag == 0) {
				Toast.makeText(GroupSimpleDetailActivity.this, "申请正在审核中，请等候", 0)
						.show();
			} else if (flag == 2) {
				getData(5, Constant.URL_GROUP_QUIT);
			} else {
				getData(4, Constant.URL_GROUP_JOIN);
			}
			break;

		case R.id.group_sim_ewm:
			Intent intent = new Intent(GroupSimpleDetailActivity.this,
					QRCodeActivity.class);
			intent.putExtra("groupid",
					getIntent().getExtras().getString("groupid"));
			intent.putExtra("clubid", clubid);
			intent.putExtra("img", img);
			intent.putExtra("name", name);
			intent.putExtra("yes", "1");
			startActivity(intent);
			break;

		case R.id.froup_simle_details_dynamic:
			Intent intent1 = new Intent(GroupSimpleDetailActivity.this,
					GroupDynamicActivity.class);
			intent1.putExtra("groupid",
					getIntent().getExtras().getString("groupid"));
			startActivity(intent1);

			break;

		case R.id.group_simle_details_dynamic_person:
			Intent intent2 = new Intent(GroupSimpleDetailActivity.this,
					GroupPersonActivity.class);
			intent2.putExtra("flag", "1");
			intent2.putExtra("id",
					getIntent().getExtras().getString("groupid"));
			startActivity(intent2);
			break;
		}

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return (list.size() > 4 ? 4 : list.size());
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			v = LayoutInflater.from(GroupSimpleDetailActivity.this).inflate(
					R.layout.item_group_simple_image_view, null);
			ImageView iv = (ImageView) v.findViewById(R.id.aasdfghjkl);
			ImageLoader.getInstance().displayImage(
					Constant.URL_RESOURCE+list.get(position).getImgsfile(), iv,
					MyApplication.getInstance().initHeadDisImgOption());
			return v;
		}

	}

	// private TextView tv_add_group;
	// private TextView tv_admin;
	// private TextView tv_name;
	// private TextView tv_introduction;
	// private EMGroup group;
	// private String groupid;
	// private ProgressBar progressBar;
	// private ImageView back_sim_group;
	// private TextView tvPerson;
	//
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_group_simle_details);
	// tv_name = (TextView) findViewById(R.id.name);
	// back_sim_group=(ImageView) findViewById(R.id.back_sim_group);
	// back_sim_group.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// finish();
	// }
	// });
	// tvPerson=(TextView)
	// findViewById(R.id.group_simle_details_dynamic_person);
	// tvPerson.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// Intent intent=new
	// Intent(GroupSimpleDetailActivity.this,GroupPersonActivity.class);
	//
	// startActivity(intent);
	// }
	// });
	// tv_admin = (TextView) findViewById(R.id.tv_admin);
	// tv_add_group = (TextView) findViewById(R.id.btn_add_to_group);
	// tv_introduction = (TextView) findViewById(R.id.tv_introduction);
	// progressBar = (ProgressBar) findViewById(R.id.loading);
	// LinearLayout ll_groupDynamic=(LinearLayout)
	// findViewById(R.id.froup_simle_details_dynamic);
	// ll_groupDynamic.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// MainActivity.instance.setTabSelection(604);
	// }
	// });
	// tv_add_group.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// addToGroup();
	// }
	// });
	// // EMGroupInfo groupInfo = (EMGroupInfo)
	// getIntent().getSerializableExtra("groupinfo");
	// String groupname = null;
	// if(getIntent().getExtras() != null){
	// groupname =
	// getIntent().getExtras().getString("groupName");//groupInfo.getGname();
	// groupid =
	// getIntent().getExtras().getString("groupId");//groupInfo.getGroupid();
	// }else{
	// group = PublicGroupsSeachActivity.searchedGroup;
	// if(group == null)
	// return;
	// groupname = group.getGroupName();
	// groupid = group.getGroupId();
	// }
	//
	// tv_name.setText(groupname);
	//
	//
	// if(group != null){
	// showGroupDetail();
	// return;
	// }
	// new Thread(new Runnable() {
	//
	// public void run() {
	// //从服务器获取详情
	// try {
	// group = EMGroupManager.getInstance().getGroupFromServer(groupid);
	// runOnUiThread(new Runnable() {
	// public void run() {
	// showGroupDetail();
	// }
	// });
	// } catch (final EaseMobException e) {
	// e.printStackTrace();
	// final String st1 =
	// getResources().getString(R.string.Failed_to_get_group_chat_information);
	// runOnUiThread(new Runnable() {
	// public void run() {
	// progressBar.setVisibility(View.INVISIBLE);
	// Toast.makeText(GroupSimpleDetailActivity.this, st1+e.getMessage(),
	// 1).show();
	// }
	// });
	// }
	//
	// }
	// }).start();
	//
	// }
	//
	// //加入群聊
	// public void addToGroup(){
	// String st1 = getResources().getString(R.string.Is_sending_a_request);
	// final String st2 = getResources().getString(R.string.Request_to_join);
	// final String st3 =
	// getResources().getString(R.string.send_the_request_is);
	// final String st4 =
	// getResources().getString(R.string.Join_the_group_chat);
	// final String st5 =
	// getResources().getString(R.string.Failed_to_join_the_group_chat);
	// final ProgressDialog pd = new ProgressDialog(this);
	// // getResources().getString(R.string)
	// pd.setMessage(st1);
	// pd.setCanceledOnTouchOutside(false);
	// pd.show();
	// new Thread(new Runnable() {
	// public void run() {
	// try {
	// //如果是membersOnly的群，需要申请加入，不能直接join
	// if(group.isMembersOnly()){
	// EMGroupManager.getInstance().applyJoinToGroup(groupid, st2);
	// }else{
	// EMGroupManager.getInstance().joinGroup(groupid);
	// }
	// runOnUiThread(new Runnable() {
	// public void run() {
	// pd.dismiss();
	// if(group.isMembersOnly())
	// Toast.makeText(GroupSimpleDetailActivity.this, st3, 0).show();
	// else
	// Toast.makeText(GroupSimpleDetailActivity.this, st4, 0).show();
	// tv_add_group.setEnabled(false);
	// }
	// });
	// } catch (final EaseMobException e) {
	// e.printStackTrace();
	// runOnUiThread(new Runnable() {
	// public void run() {
	// pd.dismiss();
	// Toast.makeText(GroupSimpleDetailActivity.this, st5+e.getMessage(),
	// 0).show();
	// }
	// });
	// }
	// }
	// }).start();
	// }
	//
	// private void showGroupDetail() {
	// progressBar.setVisibility(View.INVISIBLE);
	// //获取详情成功，并且自己不在群中，才让加入群聊按钮可点击
	// if(!group.getMembers().contains(EMChatManager.getInstance().getCurrentUser()))
	// tv_add_group.setEnabled(true);
	// tv_name.setText(group.getGroupName());
	// tv_admin.setText(group.getOwner());
	// tv_introduction.setText(group.getDescription());
	// }
	//
}
