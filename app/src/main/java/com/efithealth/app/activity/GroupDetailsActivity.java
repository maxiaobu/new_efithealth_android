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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.GroupSimpleDetailActivity.MyAdapter;
import com.efithealth.app.fragment.FragmentFriendGroup;
import com.efithealth.app.javabean.Group;
import com.efithealth.app.javabean.GroupDetailsModle;
import com.efithealth.app.javabean.GroupPerson;
import com.efithealth.app.javabean.Memberlist;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.UserUtils;
import com.efithealth.app.widget.ExpandGridView;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GroupDetailsActivity extends BaseActivity implements OnClickListener {

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
	public static GroupDetailsActivity instance;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_group_details);
		instance = this;
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
						Log.i("GroupDetailsActivity", response);
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
				Toast.makeText(GroupDetailsActivity.this, con, 0).show();
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
				Toast.makeText(GroupDetailsActivity.this, con, 0).show();
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
					Toast.makeText(GroupDetailsActivity.this, con, 0)
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
					exitGrop();
					Toast.makeText(GroupDetailsActivity.this, "您已成功退出该群",
							0).show();
					
				} else {
					Toast.makeText(GroupDetailsActivity.this, con, 0)
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
				Toast.makeText(GroupDetailsActivity.this, con, 0).show();
			}
		}
	}
	
	private void exitGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
				    EMGroupManager.getInstance().exitFromGroup(getIntent().getExtras().getString("groupId"));
					runOnUiThread(new Runnable() {
						public void run() {
							setResult(RESULT_OK);
							finish();
							if(ChatActivity.activityInstance != null)
							    ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.Exit_the_group_chat_failure) + " " + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
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
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
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
			Intent ttt = new Intent(GroupDetailsActivity.this,
					GroupControl.class);
			ttt.putExtra("id", getIntent().getExtras().getString("groupid"));
			ttt.putExtra("str1", str1);
			ttt.putExtra("str2", str2);
			startActivity(ttt);
			break;

		case R.id.gl_sim_group_person:
			Intent tttt = new Intent(GroupDetailsActivity.this,
					GroupPersonActivity.class);
			tttt.putExtra("flag", "2");
			tttt.putExtra("id", getIntent().getExtras().getString("groupid"));
			startActivityForResult(tttt, 2);
			break;

		case R.id.btn_add_to_group:
			
				getData(5, Constant.URL_GROUP_QUIT);
			
			break;

		case R.id.group_sim_ewm:
			Intent intent = new Intent(GroupDetailsActivity.this,
					QRCodeActivity.class);
			intent.putExtra("groupid",
					getIntent().getExtras().getString("groupid"));
			intent.putExtra("clubid", clubid);
			intent.putExtra("img", img);
			intent.putExtra("name", name);
			startActivity(intent);
			break;

		case R.id.froup_simle_details_dynamic:
			Intent intent1 = new Intent(GroupDetailsActivity.this,
					GroupDynamicActivity.class);
			intent1.putExtra("groupid",
					getIntent().getExtras().getString("groupid"));
			startActivity(intent1);

			break;

		case R.id.group_simle_details_dynamic_person:
			Intent intent2 = new Intent(GroupDetailsActivity.this,
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
			v = LayoutInflater.from(GroupDetailsActivity.this).inflate(
					R.layout.item_group_simple_image_view, null);
			ImageView iv = (ImageView) v.findViewById(R.id.aasdfghjkl);
			ImageLoader.getInstance().displayImage(
					Constant.URL_RESOURCE+list.get(position).getImgsfile(), iv,
					MyApplication.getInstance().initHeadDisImgOption());
			return v;
		}
		

	}
//	private static final String TAG = "GroupDetailsActivity";
//	private static final int REQUEST_CODE_ADD_USER = 0;
//	private static final int REQUEST_CODE_EXIT = 1;
//	private static final int REQUEST_CODE_EXIT_DELETE = 2;
//	private static final int REQUEST_CODE_CLEAR_ALL_HISTORY = 3;
//	private static final int REQUEST_CODE_ADD_TO_BALCKLIST = 4;
//	private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;
//
//	String longClickUsername = null;
//
//	private ExpandGridView userGridview;
//	private String groupId;
//	private ProgressBar loadingPB;
//	private Button exitBtn;
//	private Button deleteBtn;
//	private EMGroup group;
//	private GridAdapter adapter;
//	private int referenceWidth;
//	private int referenceHeight;
//	private ProgressDialog progressDialog;
//
//	private RelativeLayout rl_switch_block_groupmsg;
//	/**
//	 * 屏蔽群消息imageView
//	 */
//	private ImageView iv_switch_block_groupmsg;
//	/**
//	 * 关闭屏蔽群消息imageview
//	 */
//	private ImageView iv_switch_unblock_groupmsg;
//
	
//	
//	String st = "";
//	// 清空所有聊天记录
//	private RelativeLayout clearAllHistory;
//	private RelativeLayout blacklistLayout;
//	private RelativeLayout changeGroupNameLayout;
//    private RelativeLayout idLayout;
//    private TextView idText;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getWindow().addFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
//	    // 获取传过来的groupid
//        groupId = getIntent().getStringExtra("groupId");
//        group = EMGroupManager.getInstance().getGroup(groupId);
//
//        // we are not supposed to show the group if we don't find the group
//        if(group == null){
//            finish();
//            return;
//        }
//        
//		setContentView(R.layout.activity_group_details);
//		instance = this;
//		st = getResources().getString(R.string.people);
//		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
//		userGridview = (ExpandGridView) findViewById(R.id.gridview);
//		loadingPB = (ProgressBar) findViewById(R.id.progressBar);
//		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
//		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
//		blacklistLayout = (RelativeLayout) findViewById(R.id.rl_blacklist);
//		changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
//		idLayout = (RelativeLayout) findViewById(R.id.rl_group_id);
//		idLayout.setVisibility(View.VISIBLE);
//		idText = (TextView) findViewById(R.id.tv_group_id_value);
//		
//		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
//
//		iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
//		iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);
//
//		rl_switch_block_groupmsg.setOnClickListener(this);
//
//		Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
//		referenceWidth = referenceDrawable.getIntrinsicWidth();
//		referenceHeight = referenceDrawable.getIntrinsicHeight();
//
//
//		idText.setText(groupId);
//		if (group.getOwner() == null || "".equals(group.getOwner())
//				|| !group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
//			exitBtn.setVisibility(View.GONE);
//			deleteBtn.setVisibility(View.GONE);
//			blacklistLayout.setVisibility(View.GONE);
//			changeGroupNameLayout.setVisibility(View.GONE);
//		}
//		// 如果自己是群主，显示解散按钮
//		if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
//			exitBtn.setVisibility(View.GONE);
//			deleteBtn.setVisibility(View.VISIBLE);
//		}
//		
//		((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + st);
//		
//		List<String> members = new ArrayList<String>();
//		members.addAll(group.getMembers());
//		
//		adapter = new GridAdapter(this, R.layout.grid, members);
//		userGridview.setAdapter(adapter);
//
//		// 保证每次进详情看到的都是最新的group
//		updateGroup();
//
//		// 设置OnTouchListener
//		userGridview.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					if (adapter.isInDeleteMode) {
//						adapter.isInDeleteMode = false;
//						adapter.notifyDataSetChanged();
//						return true;
//					}
//					break;
//				default:
//					break;
//				}
//				return false;
//			}
//		});
//
//		clearAllHistory.setOnClickListener(this);
//		blacklistLayout.setOnClickListener(this);
//		changeGroupNameLayout.setOnClickListener(this);
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		String st1 = getResources().getString(R.string.being_added);
//		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
//		String st3 = getResources().getString(R.string.chatting_is_dissolution);
//		String st4 = getResources().getString(R.string.are_empty_group_of_news);
//		String st5 = getResources().getString(R.string.is_modify_the_group_name);
//		final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
//		final String st7 = getResources().getString(R.string.change_the_group_name_failed_please);
//		String st8 = getResources().getString(R.string.Are_moving_to_blacklist);
//		final String st9 = getResources().getString(R.string.failed_to_move_into);
//		
//		final String stsuccess = getResources().getString(R.string.Move_into_blacklist_success);
//		if (resultCode == RESULT_OK) {
//			if (progressDialog == null) {
//				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
//				progressDialog.setMessage(st1);
//				progressDialog.setCanceledOnTouchOutside(false);
//			}
//			switch (requestCode) {
//			case REQUEST_CODE_ADD_USER:// 添加群成员
//				final String[] newmembers = data.getStringArrayExtra("newmembers");
//				progressDialog.setMessage(st1);
//				progressDialog.show();
//				addMembersToGroup(newmembers);
//				break;
//			case REQUEST_CODE_EXIT: // 退出群
//				progressDialog.setMessage(st2);
//				progressDialog.show();
//				exitGrop();
//				break;
//			case REQUEST_CODE_EXIT_DELETE: // 解散群
//				progressDialog.setMessage(st3);
//				progressDialog.show();
//				deleteGrop();
//				break;
//			case REQUEST_CODE_CLEAR_ALL_HISTORY:
//				// 清空此群聊的聊天记录
//				progressDialog.setMessage(st4);
//				progressDialog.show();
//				clearGroupHistory();
//				break;
//
//			case REQUEST_CODE_EDIT_GROUPNAME: //修改群名称
//				final String returnData = data.getStringExtra("data");
//				if(!TextUtils.isEmpty(returnData)){
//					progressDialog.setMessage(st5);
//					progressDialog.show();
//					
//					new Thread(new Runnable() {
//						public void run() {
//							try {
//							    EMGroupManager.getInstance().changeGroupName(groupId, returnData);
//								runOnUiThread(new Runnable() {
//									public void run() {
//										((TextView) findViewById(R.id.group_name)).setText(returnData + "(" + group.getAffiliationsCount()
//												+ st);
//										progressDialog.dismiss();
//										Toast.makeText(getApplicationContext(), st6, 0).show();
//									}
//								});
//								
//							} catch (EaseMobException e) {
//								e.printStackTrace();
//								runOnUiThread(new Runnable() {
//									public void run() {
//										progressDialog.dismiss();
//										Toast.makeText(getApplicationContext(), st7, 0).show();
//									}
//								});
//							}
//						}
//					}).start();
//				}
//				break;
//			case REQUEST_CODE_ADD_TO_BALCKLIST:
//				progressDialog.setMessage(st8);
//				progressDialog.show();
//				new Thread(new Runnable() {
//					public void run() {
//						try {
//						    EMGroupManager.getInstance().blockUser(groupId, longClickUsername);
//							runOnUiThread(new Runnable() {
//								public void run() {
//								    refreshMembers();
//									progressDialog.dismiss();
//									Toast.makeText(getApplicationContext(), stsuccess, 0).show();
//								}
//							});
//						} catch (EaseMobException e) {
//							runOnUiThread(new Runnable() {
//								public void run() {
//									progressDialog.dismiss();
//									Toast.makeText(getApplicationContext(), st9, 0).show();
//								}
//							});
//						}
//					}
//				}).start();
//
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	private void refreshMembers(){
//	    adapter.clear();
//        
//        List<String> members = new ArrayList<String>();
//        members.addAll(group.getMembers());
//        adapter.addAll(members);
//        
//        adapter.notifyDataSetChanged();
//	}
//	
//	/**
//	 * 点击退出群组按钮
//	 * 
//	 * @param view
//	 */
//	public void exitGroup(View view) {
//		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);
//
//	}
//
//	/**
//	 * 点击解散群组按钮
//	 * 
//	 * @param view
//	 */
//	public void exitDeleteGroup(View view) {
//		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
//				REQUEST_CODE_EXIT_DELETE);
//
//	}
//
//	/**
//	 * 清空群聊天记录
//	 */
//	public void clearGroupHistory() {
//
//		EMChatManager.getInstance().clearConversation(group.getGroupId());
//		progressDialog.dismiss();
//		// adapter.refresh(EMChatManager.getInstance().getConversation(toChatUsername));
//
//	}
//
//	/**
//	 * 退出群组
//	 * 
//	 * @param groupId
//	 */
//	private void exitGrop() {
//		String st1 = getResources().getString(R.string.Exit_the_group_chat_failure);
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//				    EMGroupManager.getInstance().exitFromGroup(groupId);
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							setResult(RESULT_OK);
//							finish();
//							if(ChatActivity.activityInstance != null)
//							    ChatActivity.activityInstance.finish();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), getResources().getString(R.string.Exit_the_group_chat_failure) + " " + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
//	}
//
//	/**
//	 * 解散群组
//	 * 
//	 * @param groupId
//	 */
//	private void deleteGrop() {
//		final String st5 = getResources().getString(R.string.Dissolve_group_chat_tofail);
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//				    EMGroupManager.getInstance().exitAndDeleteGroup(groupId);
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							setResult(RESULT_OK);
//							finish();
//							if(ChatActivity.activityInstance != null)
//							    ChatActivity.activityInstance.finish();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), st5 + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
//	}
//
//	/**
//	 * 增加群成员
//	 * 
//	 * @param newmembers
//	 */
//	private void addMembersToGroup(final String[] newmembers) {
//		final String st6 = getResources().getString(R.string.Add_group_members_fail);
//		new Thread(new Runnable() {
//			
//			public void run() {
//				try {
//					// 创建者调用add方法
//					if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
//					    EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);
//					} else {
//						// 一般成员调用invite方法
//					    EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);
//					}
//					runOnUiThread(new Runnable() {
//						public void run() {
//						    refreshMembers();
//							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount()
//									+ st);
//							progressDialog.dismiss();
//						}
//					});
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), st6 + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
//	}
//
//	@Override
//	public void onClick(View v) {
//		String st6 = getResources().getString(R.string.Is_unblock);
//		final String st7 = getResources().getString(R.string.remove_group_of);
//		switch (v.getId()) {
//		case R.id.rl_switch_block_groupmsg: // 屏蔽群组
//			if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
//				EMLog.d(TAG, "change to unblock group msg");
//				if (progressDialog == null) {
//	                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
//	                progressDialog.setCanceledOnTouchOutside(false);
//	            }
//				progressDialog.setMessage(st6);
//				progressDialog.show();
//				new Thread(new Runnable() {
//                    public void run() {
//                        try {
//                            EMGroupManager.getInstance().unblockGroupMessage(groupId);
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
//                                    iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
//                                    progressDialog.dismiss();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), st7, 1).show();
//                                }
//                            });
//                            
//                        }
//                    }
//                }).start();
//				
//			} else {
//				String st8 = getResources().getString(R.string.group_is_blocked);
//				final String st9 = getResources().getString(R.string.group_of_shielding);
//				EMLog.d(TAG, "change to block group msg");
//				if (progressDialog == null) {
//                    progressDialog = new ProgressDialog(GroupDetailsActivity.this);
//                    progressDialog.setCanceledOnTouchOutside(false);
//                }
//				progressDialog.setMessage(st8);
//				progressDialog.show();
//				new Thread(new Runnable() {
//                    public void run() {
//                        try {
//                            EMGroupManager.getInstance().blockGroupMessage(groupId);
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
//                                    iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
//                                    progressDialog.dismiss();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), st9, 1).show();
//                                }
//                            });
//                        }
//                        
//                    }
//                }).start();
//			}
//			break;
//
//		case R.id.clear_all_history: // 清空聊天记录
//			String st9 = getResources().getString(R.string.sure_to_empty_this);
//			Intent intent = new Intent(GroupDetailsActivity.this, AlertDialog.class);
//			intent.putExtra("cancel", true);
//			intent.putExtra("titleIsCancel", true);
//			intent.putExtra("msg", st9);
//			startActivityForResult(intent, REQUEST_CODE_CLEAR_ALL_HISTORY);
//			break;
//
//		case R.id.rl_blacklist: // 黑名单列表
//			startActivity(new Intent(GroupDetailsActivity.this, GroupBlacklistActivity.class).putExtra("groupId", groupId));
//			break;
//
//		case R.id.rl_change_group_name:
//			startActivityForResult(new Intent(this, EditActivity.class).putExtra("data", group.getGroupName()), REQUEST_CODE_EDIT_GROUPNAME);
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	/**
//	 * 群组成员gridadapter
//	 * 
//	 * @author admin_new
//	 * 
//	 */
//	private class GridAdapter extends ArrayAdapter<String> {
//
//		private int res;
//		public boolean isInDeleteMode;
//		private List<String> objects;
//
//		public GridAdapter(Context context, int textViewResourceId, List<String> objects) {
//			super(context, textViewResourceId, objects);
//			this.objects = objects;
//			res = textViewResourceId;
//			isInDeleteMode = false;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, final ViewGroup parent) {
//		    ViewHolder holder = null;
//			if (convertView == null) {
//			    holder = new ViewHolder();
//				convertView = LayoutInflater.from(getContext()).inflate(res, null);
//				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
//				holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
//				holder.badgeDeleteView = (ImageView) convertView.findViewById(R.id.badge_delete);
//				convertView.setTag(holder);
//			}else{
//			    holder = (ViewHolder) convertView.getTag();
//			}
//			final LinearLayout button = (LinearLayout) convertView.findViewById(R.id.button_avatar);
//			// 最后一个item，减人按钮
//			if (position == getCount() - 1) {
//			    holder.textView.setText("");
//				// 设置成删除按钮
//			    holder.imageView.setImageResource(R.drawable.smiley_minus_btn);
////				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_minus_btn, 0, 0);
//				// 如果不是创建者或者没有相应权限，不提供加减人按钮
//				if (!group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
//					// if current user is not group admin, hide add/remove btn
//					convertView.setVisibility(View.INVISIBLE);
//				} else { // 显示删除按钮
//					if (isInDeleteMode) {
//						// 正处于删除模式下，隐藏删除按钮
//						convertView.setVisibility(View.INVISIBLE);
//					} else {
//						// 正常模式
//						convertView.setVisibility(View.VISIBLE);
//						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
//					}
//					final String st10 = getResources().getString(R.string.The_delete_button_is_clicked);
//					button.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							EMLog.d(TAG, st10);
//							isInDeleteMode = true;
//							notifyDataSetChanged();
//						}
//					});
//				}
//			} else if (position == getCount() - 2) { // 添加群组成员按钮
//			    holder.textView.setText("");
//			    holder.imageView.setImageResource(R.drawable.smiley_add_btn);
////				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
//				// 如果不是创建者或者没有相应权限
//				if (!group.isAllowInvites() && !group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
//					// if current user is not group admin, hide add/remove btn
//					convertView.setVisibility(View.INVISIBLE);
//				} else {
//					// 正处于删除模式下,隐藏添加按钮
//					if (isInDeleteMode) {
//						convertView.setVisibility(View.INVISIBLE);
//					} else {
//						convertView.setVisibility(View.VISIBLE);
//						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
//					}
//					final String st11 = getResources().getString(R.string.Add_a_button_was_clicked);
//					button.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							EMLog.d(TAG, st11);
//							// 进入选人页面
//							startActivityForResult(
//									(new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class).putExtra("groupId", groupId)),
//									REQUEST_CODE_ADD_USER);
//						}
//					});
//				}
//			} else { // 普通item，显示群组成员
//				final String username = getItem(position);
//				convertView.setVisibility(View.VISIBLE);
//				button.setVisibility(View.VISIBLE);
////				Drawable avatar = getResources().getDrawable(R.drawable.default_avatar);
////				avatar.setBounds(0, 0, referenceWidth, referenceHeight);
////				button.setCompoundDrawables(null, avatar, null, null);
//				holder.textView.setText(username);
//				UserUtils.setUserAvatar(getContext(), username, holder.imageView);
//				// demo群组成员的头像都用默认头像，需由开发者自己去设置头像
//				if (isInDeleteMode) {
//					// 如果是删除模式下，显示减人图标
//					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
//				} else {
//					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
//				}
//				final String st12 = getResources().getString(R.string.not_delete_myself);
//				final String st13 = getResources().getString(R.string.Are_removed);
//				final String st14 = getResources().getString(R.string.Delete_failed);
//				final String st15 = getResources().getString(R.string.confirm_the_members);
//				button.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if (isInDeleteMode) {
//							// 如果是删除自己，return
//							if (EMChatManager.getInstance().getCurrentUser().equals(username)) {
//								startActivity(new Intent(GroupDetailsActivity.this, AlertDialog.class).putExtra("msg", st12));
//								return;
//							}
//							if (!NetUtils.hasNetwork(getApplicationContext())) {
//								Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable), 0).show();
//								return;
//							}
//							EMLog.d("group", "remove user from group:" + username);
//							deleteMembersFromGroup(username);
//						} else {
//							// 正常情况下点击user，可以进入用户详情或者聊天页面等等
//							// startActivity(new
//							// Intent(GroupDetailsActivity.this,
//							// ChatActivity.class).putExtra("userId",
//							// user.getUsername()));
//
//						}
//					}
//
//					/**
//					 * 删除群成员
//					 * 
//					 * @param username
//					 */
//					protected void deleteMembersFromGroup(final String username) {
//						final ProgressDialog deleteDialog = new ProgressDialog(GroupDetailsActivity.this);
//						deleteDialog.setMessage(st13);
//						deleteDialog.setCanceledOnTouchOutside(false);
//						deleteDialog.show();
//						new Thread(new Runnable() {
//
//							@Override
//							public void run() {
//								try {
//									// 删除被选中的成员
//								    EMGroupManager.getInstance().removeUserFromGroup(groupId, username);
//									isInDeleteMode = false;
//									runOnUiThread(new Runnable() {
//
//										@Override
//										public void run() {
//											deleteDialog.dismiss();
//											refreshMembers();
//											((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "("
//													+ group.getAffiliationsCount() + st);
//										}
//									});
//								} catch (final Exception e) {
//									deleteDialog.dismiss();
//									runOnUiThread(new Runnable() {
//										public void run() {
//											Toast.makeText(getApplicationContext(), st14 + e.getMessage(), 1).show();
//										}
//									});
//								}
//
//							}
//						}).start();
//					}
//				});
//
//				button.setOnLongClickListener(new OnLongClickListener() {
//
//					@Override
//					public boolean onLongClick(View v) {
//					    if(EMChatManager.getInstance().getCurrentUser().equals(username))
//					        return true;
//						if (group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())) {
//							Intent intent = new Intent(GroupDetailsActivity.this, AlertDialog.class);
//							intent.putExtra("msg", st15);
//							intent.putExtra("cancel", true);
//							startActivityForResult(intent, REQUEST_CODE_ADD_TO_BALCKLIST);
//							longClickUsername = username;
//						}
//						return false;
//					}
//				});
//			}
//			return convertView;
//		}
//
//		@Override
//		public int getCount() {
//			return super.getCount() + 2;
//		}
//	}
//
//	protected void updateGroup() {
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					final EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(groupId);
//					// 更新本地数据
//					EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
//
//					runOnUiThread(new Runnable() {
//						public void run() {
//							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount()
//									+ ")");
//							loadingPB.setVisibility(View.INVISIBLE);
//							refreshMembers();
//							if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
//								// 显示解散按钮
//								exitBtn.setVisibility(View.GONE);
//								deleteBtn.setVisibility(View.VISIBLE);
//							} else {
//								// 显示退出按钮
//								exitBtn.setVisibility(View.VISIBLE);
//								deleteBtn.setVisibility(View.GONE);
//							}
//
//							// update block
//							EMLog.d(TAG, "group msg is blocked:" + group.getMsgBlocked());
//							if (group.isMsgBlocked()) {
//								iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
//								iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
//							} else {
//								iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
//								iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
//							}
//						}
//					});
//
//				} catch (Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							loadingPB.setVisibility(View.INVISIBLE);
//						}
//					});
//				}
//			}
//		}).start();
//	}
//
//	public void back(View view) {
//		setResult(RESULT_OK);
//		finish();
//	}
//
//	@Override
//	public void onBackPressed() {
//		setResult(RESULT_OK);
//		finish();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		instance = null;
//	}
//	
//	private static class ViewHolder{
//	    ImageView imageView;
//	    TextView textView;
//	    ImageView badgeDeleteView;
//	}

}
