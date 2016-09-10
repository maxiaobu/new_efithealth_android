package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ChatActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.MipcaActivityCapture;
import com.efithealth.app.adapter.ChatAllHistoryAdapter;
import com.efithealth.app.db.InviteMessgeDao;
import com.efithealth.app.domain.User;
import com.efithealth.app.menu.ActionItem;
import com.efithealth.app.menu.TitlePopup;
import com.efithealth.app.menu.TitlePopup.OnItemOnClickListener;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.UserUtils;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.applib.controller.HXSDKHelper;

public class FragmentTalkMessage extends BaseFragment {

	private View v;
	private ListView talk_lv;
	private ChatAllHistoryAdapter adapter;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private Map<String, User> contactList;
	private InputMethodManager inputMethodManager;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_talk_message, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		contactList = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getContactList();
		talk_lv = (ListView) v.findViewById(R.id.list_talk);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		// 设置adapter
		talk_lv.setAdapter(adapter);

		final String st2 = getResources().getString(
				R.string.Cant_chat_with_yourself);
		talk_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(MyApplication.getInstance().getUserName()))
					ToastCommom.getInstance().ToastShow(getActivity(), st2);
				else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(),
							ChatActivity.class);
					if (conversation.isGroup()) {
						if (conversation.getType() == EMConversationType.ChatRoom) {
							// it is group chat
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_CHATROOM);
							intent.putExtra("groupId", username);
						} else {
							// it is group chat
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_GROUP);
							intent.putExtra("groupId", username);
						}

					} else {
						// it is single chat
						intent.putExtra("userId", username);
						intent.putExtra("nickname",
								UserUtils.getUserInfo(username).getNick());
					}
					startActivity(intent);
				}
			}
		});
		// 注册上下文菜单
		registerForContextMenu(talk_lv);

		talk_lv.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
		super.onActivityCreated(savedInstanceState);
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean handled = false;
		boolean deleteMessage = false;
		if (item.getItemId() == R.id.delete_message) {
			deleteMessage = true;
			handled = true;
		} else if (item.getItemId() == R.id.delete_conversation) {
			deleteMessage = false;
			handled = true;
		}
		EMConversation tobeDeleteCons = adapter
				.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(
				tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
				deleteMessage);
		InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
		adapter.remove(tobeDeleteCons);
		adapter.notifyDataSetChanged();

		// 更新消息未读数
		((MainActivity) getActivity()).updateUnreadLabel();

		return handled ? true : super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		List<EMConversation> list1 = new ArrayList<EMConversation>();
		List<EMConversation> list = new ArrayList<EMConversation>();
		list1 = EMChatManager.getInstance().getConversationsByType(
				EMConversationType.Chat);
		synchronized (list) {
			for (EMConversation conversation : list1) {
				if (conversation.getAllMessages().size() != 0) {
					String memid = conversation.getUserName();
					Map<String, String> map = new HashMap<String, String>();
					map.put("memid", memid);
					LoadDataFromServer task_club_message = new LoadDataFromServer(
							getActivity(), Constant.URL_TARINFO, map);
					task_club_message.getData(new DataCallBack() {
						@Override
						public void onDataCallBack(JSONObject data) {
							Log.d("===获取聊天对象头像", data.toString());
							JSONObject jsonfriends = data
									.getJSONObject("memInfo");
							if (jsonfriends != null) {
								String f_memeid = jsonfriends
										.getString("memid").toLowerCase();
								String f_nickname = jsonfriends
										.getString("nickname");
								String f_headimg = jsonfriends
										.getString("imgsfilename");
								User f_user = UserUtils.getUserInfo(f_memeid);
								f_user.setAvatar(f_headimg);
								f_user.setNick(f_nickname);
								UserUtils.saveUserInfo(f_user);
								Log.e("MyApplication", "好友信息更新成功");
								if (adapter != null)
									adapter.notifyDataSetChanged();
							}
						}
					});
					list.add(conversation);
				}
			}
		}
		// 排序
		sortUserByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortUserByLastChatTime(List<EMConversation> contactList) {
		Collections.sort(contactList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation user1,
					final EMConversation user2) {
				EMConversation conversation1 = EMChatManager.getInstance()
						.getConversation(user1.getUserName());
				EMConversation conversation2 = EMChatManager.getInstance()
						.getConversation(user2.getUserName());

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
		if (!hidden && !((MainActivity) getActivity()).isConflict) {
			refresh();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (((MainActivity) getActivity()).isConflict) {
			outState.putBoolean("isConflict", true);
		} else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
			outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
		}
	}

}
