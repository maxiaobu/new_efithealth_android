package com.efithealth.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMConversation.EMConversationType;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
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
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ChatAllHistoryFragment extends Fragment implements View.OnClickListener {

	private static final int QECODE_RESULT = 1;
	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
//	private EditText query;
//	private ImageButton clearSearch;
	public RelativeLayout errorItem;

	public TextView errorText;
	private boolean hidden;

	private TitlePopup titlePopup;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;

		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(getActivity(), R.string.menu_findfriend, R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(getActivity(), R.string.menu_addchat, R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(getActivity(), R.string.menu_qrcode, R.drawable.icon_menu_sao));

		ImageView addimg = (ImageView) getView().findViewById(R.id.chat_add);

		addimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				titlePopup.show(getView().findViewById(R.id.chat_add));
			}
		});

		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);

		conversationList.addAll(loadConversationsWithRecentChat());
		listView = (ListView) getView().findViewById(R.id.list);
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		// 设置adapter
		listView.setAdapter(adapter);

		final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				Log.i("namenamenamename", username);
				if (username.equals(MyApplication.getInstance().getUserName()))
					ToastCommom.getInstance().ToastShow(getActivity(), st2);
				else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					if (conversation.isGroup()) {
						if (conversation.getType() == EMConversationType.ChatRoom) {
							// it is group chat
							intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
							intent.putExtra("groupId", username);
						} else {
							// it is group chat
							intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
							intent.putExtra("groupId", username);
						}

					} else {
						// it is single chat
						intent.putExtra("userId", username);
						intent.putExtra("nickname", UserUtils.getUserInfo(username).getNick());
					}
					startActivity(intent);
				}
			}
		});
		// 注册上下文菜单
		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
		// 搜索框
		// query = (EditText) getView().findViewById(R.id.query);
		// query.setVisibility(View.GONE);
		// String strSearch = getResources().getString(R.string.search);
		// query.setHint(strSearch);
		// 搜索框中清除button
		// clearSearch = (ImageButton)
		// getView().findViewById(R.id.search_clear);
		/*
		 * query.addTextChangedListener(new TextWatcher() { public void
		 * onTextChanged(CharSequence s, int start, int before, int count) {
		 * adapter.getFilter().filter(s); if (s.length() > 0) {
		 * clearSearch.setVisibility(View.VISIBLE); } else {
		 * clearSearch.setVisibility(View.INVISIBLE); } }
		 * 
		 * public void beforeTextChanged(CharSequence s, int start, int count,
		 * int after) { }
		 * 
		 * public void afterTextChanged(Editable s) { } });
		 * clearSearch.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { query.getText().clear();
		 * hideSoftKeyboard(); } });
		 */
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow()
				.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
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
		EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
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
		
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					String memid = conversation.getUserName();
					Map<String, String> map = new HashMap<String, String>();
					map.put("memid", memid);
					LoadDataFromServer task_club_message = new LoadDataFromServer(getActivity(), Constant.URL_TARINFO,
							map);
					task_club_message.getData(new DataCallBack() {
						@Override
						public void onDataCallBack(JSONObject data) {
							Log.d("===获取聊天对象头像", data.toString());
							JSONObject jsonfriends = data.getJSONObject("memInfo");
							if (jsonfriends != null) {
								String f_memeid = jsonfriends.getString("memid").toLowerCase();
								String f_nickname = jsonfriends.getString("nickname");
								String f_headimg = jsonfriends.getString("imgsfilename");
								User f_user = UserUtils.getUserInfo(f_memeid);
								f_user.setAvatar(f_headimg);
								f_user.setNick(f_nickname);
								UserUtils.saveUserInfo(f_user);
								// localUsers.put(f_memeid, f_user);
								// UserUtils.updateUserInfo(localUsers);
								Log.e("MyApplication", "好友信息更新成功");
							}
							// {"memInfo":{"birth":"","updatetime":{"nanos":0,"time":1461758733000,"minutes":5,"seconds":33,"hours":20,"month":3,"year":116,"timezoneOffset":-480,"day":3,"date":27},"remark":"","posiupdatetime":{"nanos":0,"time":1461758733000,"minutes":5,"seconds":33,"hours":20,"month":3,"year":116,"timezoneOffset":-480,"day":3,"date":27},"gendername":"男","concernnum":3,"checkopinion":"","pkeyListStr":"","createuser":"M000014","isclubadminname":"否","headpage":"0","memname":"","istopfile":[],"imagefile":[],"gender":"1","clubid":"","longitude":0E-10,"coursetimes":0,"signature":"","courseprice":0,"evascore":0,"status":"1","nickname":"宝宝","dynamicnum":0,"nowtimestr":"","curMemrole":"mem","ispushname":"开启","linkurl":"","ycoinnum":800,"modifyuser":"","isstealth":"0","dayadd":"","nowTime":0,"coachcertname":"无","istrans":"1","applydescr":"","resinform":"","imgsfile":"/image/bmember/M000014_1461746175845_s.png","lessontotal":0,"latitude":0E-10,"imgfileFileName":"","lessonresent":0,"clubname":"","phonedeviceno":"990006202091731","imgsfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000014_1461746175845_s.png","memrole":"","birthday":{"nanos":0,"time":315504000000,"minutes":0,"seconds":0,"hours":0,"month":0,"year":80,"timezoneOffset":-480,"day":2,"date":1},"statusname":"有效","evatimes":0,"ispush":"1","follownum":1,"coachadept":"","recname":"高原","recphone":"13941689367","createtime":{"nanos":0,"time":1461400213000,"minutes":30,"seconds":13,"hours":16,"month":3,"year":116,"timezoneOffset":-480,"day":6,"date":23},"identity":"","ycoincashnum":0,"distance":0,"coachprice":0,"headpagename":"未设置","imgpfilename":"http://efithealthresource.oss-cn-beijing.aliyuncs.com/image/bmember/M000014_1461746175845_p.png","imgpfile":"/image/bmember/M000014_1461746175845_p.png","mempass":"e10adc3949ba59abbe56e057f20f883e","recaddress":"沈阳市和平区市府大路北三经街北市家园三号楼一单元1601","sorttype":"","applydatestr":"","isclubadmin":"0","identcode":"","distancestr":"","mobphone":"13941689367","evascoretotal":0,"memid":"M000014","coachcert":"0"},"msgContent":"显示会员信息","msgFlag":"1"}
						}
					});
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(
							new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
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

	@Override
	public void onClick(View v) {
	}

	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 查找好友
				MainActivity.instance.onTabClicked(getView().findViewById(R.id.chat_add));
				break;
			case 1:// 发起聊天
//				MainActivity.instance.setFromIndex(2);
				MainActivity.instance.setTabSelection(202);
				break;
			case 2:// 扫一扫
				Intent intent = new Intent(getActivity(), MipcaActivityCapture.class);
				startActivityForResult(intent, QECODE_RESULT);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == QECODE_RESULT) {
			if (data == null) {
				ToastCommom.getInstance().ToastShow(getActivity(), "取消");
			} else {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("data");
				ToastCommom.getInstance().ToastShow(getActivity(), scanResult);
			}
		}
	}
}
