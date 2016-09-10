package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ChatActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.adapter.ChatHistoryAdapter;
import com.efithealth.app.db.InviteMessgeDao;
import com.efithealth.app.domain.User;
import com.efithealth.app.javabean.GroupHistoryList;
import com.efithealth.app.javabean.GroupList;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.applib.controller.HXSDKHelper;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentTalkGroup extends BaseFragment {
	private View v;
	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatHistoryAdapter adapter;
	private boolean hidden;
	private LinearLayout ll;
	private List<GroupList> list = new ArrayList<>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_talk_group, container, false);
		return v;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ll = (LinearLayout) v.findViewById(R.id.jump_group_list);
		ll.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.setTabSelection(603);
			}
		});
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) getView().findViewById(R.id.list);

		String str = "";
		for (int i = 0; i < loadUsersWithRecentChat().size(); i++) {
			if (i == loadUsersWithRecentChat().size() - 1) {
				str += "'" + loadUsersWithRecentChat().get(i).getUsername() + "'";
			} else {
				str += "'" + loadUsersWithRecentChat().get(i).getUsername() + "',";
			}
		}
		getData(str);
		final String st = getResources().getString(
				R.string.Cant_chat_with_yourself);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EMContact emContact = adapter.getItem(position);
				String managerid="";
				String groupid="";
				if (adapter.getItem(position).getUsername()
						.equals(MyApplication.getInstance().getUserName()))
					Toast.makeText(getActivity(), st, 0).show();
				else {
					String user = emContact.getUsername();
					for (int i = 0; i < list.size(); i++) {
						if (user.equals(list.get(i).getImid())) {
							managerid=list.get(i).getManagerid();
							groupid=list.get(i).getGroupid();
						}
					}
					// 进入聊天页面
					Intent intent = new Intent(getActivity(),
							ChatActivity.class);
					if (emContact instanceof EMGroup) {
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("managerid", managerid);
						intent.putExtra("groupid", groupid);
						intent.putExtra("groupId",
								((EMGroup) emContact).getGroupId());
					}
					startActivity(intent);
				}
			}
		});
		// 注册上下文菜单
		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getActivity().getCurrentFocus()
										.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setPositiveButton("删除会话和消息", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						EMContact tobeDeleteUser = loadUsersWithRecentChat()
								.get(arg2);
						// 删除此会话
						EMChatManager.getInstance().deleteConversation(
								tobeDeleteUser.getUsername(), true);
						InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
								getActivity());
						inviteMessgeDao.deleteMessage(tobeDeleteUser
								.getUsername());
						adapter.remove(tobeDeleteUser);
						EMChatManager.getInstance().deleteConversation(
								tobeDeleteUser.getEid());
						adapter.notifyDataSetChanged();

						// 更新消息未读数
						MainActivity.instance.updateUnreadLabel();

					}

				});

				builder.create();
				builder.show();

				return true;
			}
		});

	}

	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo
	// menuInfo) {
	// super.onCreateContextMenu(menu, v, menuInfo);
	// getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
	// }
	//
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// if (item.getItemId() == R.id.delete_message) {
	// EMContact tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo)
	// item.getMenuInfo()).position);
	// // 删除此会话
	// EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername(),true);
	// InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
	// inviteMessgeDao.deleteMessage(tobeDeleteUser.getUsername());
	// adapter.remove(tobeDeleteUser);
	// EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getEid());
	// adapter.notifyDataSetChanged();
	//
	// // 更新消息未读数
	// MainActivity.instance.updateUnreadLabel();
	// return true;
	// }
	// return super.onContextItemSelected(item);
	// }

	/**
	 * 刷新页面
	 */
	public void refresh() {
		adapter = new ChatHistoryAdapter(getActivity(),
				R.layout.row_chat_history, loadUsersWithRecentChat(), list);
		listView.setAdapter(adapter);
		if (adapter!=null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @param context
	 * @return
	 */
	private List<EMContact> loadUsersWithRecentChat() {
		List<EMContact> resultList = new ArrayList<EMContact>();
		for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(group.getGroupId());
			if (conversation.getMsgCount() > 0) {
				resultList.add(group);
			}
			if (adapter!=null) {
				adapter.notifyDataSetChanged();
			}
		}
		// 排序
		sortUserByLastChatTime(resultList);
		return resultList;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortUserByLastChatTime(List<EMContact> contactList) {
		Collections.sort(contactList, new Comparator<EMContact>() {
			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance()
						.getConversation(user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance()
						.getConversation(user2.getUsername());

				EMMessage user2LastMessage = conversation2.getLastMessage();
				EMMessage user1LastMessage = conversation1.getLastMessage();
				if (user2LastMessage.getMsgTime() == user1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	private void getData(final String str) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_GROUP_CHAT_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentTalkGroup", response);
						Gson gson = new Gson();
						GroupHistoryList groupHistoryList = gson.fromJson(
								response, GroupHistoryList.class);
						String msg = groupHistoryList.getMsgFlag();
						String con = groupHistoryList.getMsgContent();
						if (msg.equals("1")) {
							list = groupHistoryList.getGroupList();
							adapter = new ChatHistoryAdapter(getActivity(), 1,
									loadUsersWithRecentChat(), list);
							// 设置adapter
							listView.setAdapter(adapter);
						} else {
							Toast.makeText(getActivity(), con, 0).show();
						}
					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("groupids", str);
				return map;
			}
		};
		queue.add(request);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

}
