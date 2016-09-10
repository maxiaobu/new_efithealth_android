/**
 chat_add * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.efithealth.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.efithealth.app.Constant;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.app.MyApplication;
import com.efithealth.R;
import com.efithealth.app.db.InviteMessgeDao;
import com.efithealth.app.db.UserDao;
import com.efithealth.app.domain.InviteMessage;
import com.efithealth.app.domain.InviteMessage.InviteMesageStatus;
import com.efithealth.app.domain.User;
import com.efithealth.app.fragment.FragmentCoachList;
import com.efithealth.app.fragment.FragmentEvaluate;
import com.efithealth.app.fragment.FragmentFinds;
import com.efithealth.app.fragment.FragmentFriendGroup;
import com.efithealth.app.fragment.FragmentHotDynamic;
import com.efithealth.app.fragment.FragmentIssueCourse;
import com.efithealth.app.fragment.FragmentMassage;
import com.efithealth.app.fragment.FragmentMassageDetails;
import com.efithealth.app.fragment.FragmentMassageOrder;
import com.efithealth.app.fragment.FragmentMassagePersonList;
import com.efithealth.app.fragment.FragmentMe;
import com.efithealth.app.fragment.FragmentMyGroup;
import com.efithealth.app.fragment.FragmentPayAmount;
import com.efithealth.app.fragment.FragmentTalk;
import com.efithealth.app.fragment.OrderListFragment;
import com.efithealth.app.utils.CommonUtils;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.umeng.analytics.MobclickAgent;
@SuppressLint("DefaultLocale")
public class MainActivity extends BaseActivity implements EMEventListener {

	protected static final String TAG = "MainActivity";
	public static MainActivity instance = null;
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;
	private ImageView[] mTabs;
	private TextView[] textviews;
	private long exitTime;
	public int currentTabIndex = 0;
	public int currentPage = 0;
	private List<Integer> states = new ArrayList<Integer>();

	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	private MyConnectionListener connectionListener = null;
	private MyGroupChangeListener groupChangeListener = null;
	public LinearLayout titlelayout;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		instance = this;
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			DemoHXSDKHelper.getInstance().logout(true, null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.activity_main);
		initView();
		// MobclickAgent.setDebugMode( true );
		// --?--
		MobclickAgent.updateOnlineConfig(this);

		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		FragmentHome homefragment = new FragmentHome();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, homefragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
		init();
		Log.e("MainActivityInit:", getSupportFragmentManager()
				.getBackStackEntryCount() + "");
		// 异步获取当前用户的昵称和头像
		// ((DemoHXSDKHelper)
		// HXSDKHelper.getInstance()).getUserProfileManager().asyncGetCurrentUserInfo();
	}

	private void init() {
		// setContactListener监听联系人的变化等
		// EMContactManager.getInstance().setContactListener(new
		// MyContactListener());
		// 注册一个监听连接状态的listener

		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);

		// groupChangeListener = new MyGroupChangeListener();
		// 注册群聊相关的listener
		// EMGroupManager.getInstance().addGroupChangeListener(groupChangeListener);

		// 内部测试方法，请忽略
		// registerInternalDebugReceiver();
	}

	public static void asyncFetchGroupsFromServer() {
		HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

			@Override
			public void onSuccess() {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

				if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}

			@Override
			public void onError(int code, String message) {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

		});
	}

	static void asyncFetchContactsFromServer() {
		HXSDKHelper.getInstance().asyncFetchContactsFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> usernames) {
						Context context = HXSDKHelper.getInstance()
								.getAppContext();

						System.out.println("----------------"
								+ usernames.toString());
						EMLog.d("roster", "contacts size: " + usernames.size());
						Map<String, User> userlist = new HashMap<String, User>();
						for (String username : usernames) {
							User user = new User();
							user.setUsername(username);
							setUserHearder(username, user);
							userlist.put(username, user);
						}
						// 添加user"申请与通知"
						User newFriends = new User();
						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
						String strChat = context
								.getString(R.string.Application_and_notify);
						newFriends.setNick(strChat);

						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
						// 添加"群聊"
						User groupUser = new User();
						String strGroup = context
								.getString(R.string.group_chat);
						groupUser.setUsername(Constant.GROUP_USERNAME);
						groupUser.setNick(strGroup);
						groupUser.setHeader("");
						userlist.put(Constant.GROUP_USERNAME, groupUser);

						// 添加"聊天室"
						User chatRoomItem = new User();
						String strChatRoom = context
								.getString(R.string.chat_room);
						chatRoomItem.setUsername(Constant.CHAT_ROOM);
						chatRoomItem.setNick(strChatRoom);
						chatRoomItem.setHeader("");
						userlist.put(Constant.CHAT_ROOM, chatRoomItem);

						// 添加"Robot"
						User robotUser = new User();
						String strRobot = context
								.getString(R.string.robot_chat);
						robotUser.setUsername(Constant.CHAT_ROBOT);
						robotUser.setNick(strRobot);
						robotUser.setHeader("");
						userlist.put(Constant.CHAT_ROBOT, robotUser);

						// 存入内存
						((DemoHXSDKHelper) HXSDKHelper.getInstance())
								.setContactList(userlist);
						// 存入db
						UserDao dao = new UserDao(context);
						List<User> users = new ArrayList<User>(userlist
								.values());
						dao.saveContactList(users);

						HXSDKHelper.getInstance().notifyContactsSyncListener(
								true);

						if (HXSDKHelper.getInstance()
								.isGroupsSyncedWithServer()) {
							HXSDKHelper.getInstance().notifyForRecevingEvents();
						}

						((DemoHXSDKHelper) HXSDKHelper.getInstance())
								.getUserProfileManager()
								.asyncFetchContactInfosFromServer(usernames,
										new EMValueCallBack<List<User>>() {

											@Override
											public void onSuccess(
													List<User> uList) {
												((DemoHXSDKHelper) HXSDKHelper
														.getInstance())
														.updateContactList(uList);
												((DemoHXSDKHelper) HXSDKHelper
														.getInstance())
														.getUserProfileManager()
														.notifyContactInfosSyncListener(
																true);
											}

											@Override
											public void onError(int error,
													String errorMsg) {
											}
										});
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyContactsSyncListener(
								false);
					}

				});
	}

	static void asyncFetchBlackListFromServer() {
		HXSDKHelper.getInstance().asyncFetchBlackListFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> value) {
						EMContactManager.getInstance().saveBlackList(value);
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								true);
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								false);
					}

				});
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	
	private static void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);

		titlelayout = (LinearLayout) findViewById(R.id.titlelayout);
		mTabs = new ImageView[4];
		mTabs[0] = (ImageView) findViewById(R.id.iv_home);
		mTabs[1] = (ImageView) findViewById(R.id.iv_chat);
		mTabs[2] = (ImageView) findViewById(R.id.iv_find);
		mTabs[3] = (ImageView) findViewById(R.id.iv_my);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

		textviews = new TextView[4];
		textviews[0] = (TextView) findViewById(R.id.tv_home);
		textviews[1] = (TextView) findViewById(R.id.tv_chat);
		textviews[2] = (TextView) findViewById(R.id.tv_find);
		textviews[3] = (TextView) findViewById(R.id.tv_my);
		textviews[0].setTextColor(0xFFFF631B);

	}

	public void hideTitleBar(int ishow) {
		if (ishow == 1) {
			titlelayout.setVisibility(View.GONE);
		} else {
			titlelayout.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_home:
			currentTabIndex = 0;
			currentPage = 0;
			clearFragment();
			setTabSelection(currentPage);
			states.clear();
			break;
		case R.id.re_chat:
			currentTabIndex = 1;
			currentPage = 1;
			clearFragment();
			setTabSelection(currentPage);
			states.clear();
			break;
		case R.id.re_find:
			clearFragment();
			currentTabIndex = 2;
			currentPage = 2;
			setTabSelection(currentPage);
			states.clear();
			break;
		case R.id.re_my:
			clearFragment();
			currentTabIndex = 3;
			currentPage = 3;
			setTabSelection(currentPage);
			states.clear();
			break;
		// 我
		case R.id.btn_class_manage:
			currentTabIndex = 3;
			currentPage = 100;// 课程管理
			setTabSelection(currentPage);
			break;
		case R.id.iv_class_manage_title_left:
			currentTabIndex = 3;
			currentPage = 3;
			setTabSelection(currentPage);
			break;
		case R.id.lv_content:
			currentTabIndex = 3;
			currentPage = 200;// 编辑课程
			setTabSelection(currentPage);
			break;
		case R.id.iv_class_manage_title_right:
			currentTabIndex = 3;
			currentPage = 303;// 发布课程
			setTabSelection(currentPage);
			break;
		case R.id.iv_issue_course_title_left:
			currentTabIndex = 3;
			currentPage = 100;
			setTabSelection(currentPage);
			break;
		// 首页
		case R.id.smallId1:// 私人教练
			currentTabIndex = 0;
			currentPage = 101;
			setTabSelection(currentPage);
			break;
		case R.id.smallId2:// 健身举了波
			currentTabIndex = 0;
			currentPage = 102;
			setTabSelection(currentPage);
			break;
		case R.id.smallId4:// 营养配餐
			currentTabIndex = 0;
			currentPage = 104;
			setTabSelection(currentPage);
			break;
		case R.id.searchId:
			currentTabIndex = 0;
			currentPage = 301;
			setTabSelection(currentPage);
			break;
		// Talk
		case R.id.chat_add:
			currentTabIndex = 1;
			currentPage = 201;
			setTabSelection(currentPage);
			break;
		case R.id.rl_error_item:
			currentTabIndex = 1;
			currentPage = 202;
			setTabSelection(currentPage);
			break;
		case R.id.pay_tv_commit:
			clearFragment();
			currentTabIndex = 3;
			currentPage = 3;
			setTabSelection(currentPage);
			states.clear();
			break;
		}
		setTabColor(currentTabIndex);
	}

	/**
	 * 清空Fragment
	 */
	private void clearFragment() {
		FragmentManager ft = getSupportFragmentManager();
		int count = ft.getBackStackEntryCount();
		for (int i = 0; i < count; i++) {
			ft.popBackStackImmediate();
//			Log.i("Fragment popped from stack: ", "just popped: " + i + ".");
		}
		Log.e("MainActivityPopdone:",
				ft.getFragments().size() + ";" + ft.getBackStackEntryCount());
	}

	public void setTabColor(int index) {
		for (int i = 0; i < mTabs.length; i++) {
			if (i == index) {
				// 把当前tab设为选中状态
				mTabs[i].setSelected(true);
				textviews[i].setTextColor(0xFFFF631B);
			} else {
				mTabs[i].setSelected(false);
				textviews[i].setTextColor(0xFF999999);

			}
		}
	}

	/**
	 * url加载
	 * 
	 * @param url
	 */

	public void setTabWebViewSelection(String url) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		FragmentWebView webviewFragment = new FragmentWebView(url);
		ft.add(R.id.fragment_container, webviewFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
		Log.e("MainActivity", getSupportFragmentManager()
				.getBackStackEntryCount() + "");
	}

	public String getPath() {
		double jd = MyApplication.getInstance().mLongitude;
		double wd = MyApplication.getInstance().mLatitude;
		if (jd == 0.0 && wd == 0.0) {
			jd = 123.43095;
			wd = 41.811159;
		}
		String path = "?memid=" + MyApplication.getInstance().getMemid()
				+ "&longitude=" + jd + "&latitude=" + wd;
		return path;
	}

	// private ChatAllHistoryFragment chatHistoryFragment;
	private FragmentHome homefragment;
	private FragmentTalk chatHistoryFragment;

	public void setTabSelection(int index) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		currentPage = index;
		switch (index) {
		case 0:

			FragmentHome.flag=true;
			homefragment = new FragmentHome();
			ft.add(R.id.fragment_container, homefragment);
			break;
		case 1:
			chatHistoryFragment = new FragmentTalk();
			// chatHistoryFragment = new ChatAllHistoryFragment();
			ft.add(R.id.fragment_container, chatHistoryFragment);
			break;
		case 2:
			// TODO
//			 FragmentFind findfragment = new FragmentFind();
			FragmentFinds findfragment = new FragmentFinds();
			ft.add(R.id.fragment_container, findfragment);
			break;
		case 3:
			//
            // TODO
			FragmentMe myfragment = new FragmentMe();
			// FragmentMy myfragment = new FragmentMy();
			ft.add(R.id.fragment_container, myfragment);
			break;
		case 100:
			FragmentClassManage classManagefragment = new FragmentClassManage();
			ft.add(R.id.fragment_container, classManagefragment);
			break;
		case 303:
			FragmentIssueCourse issueCourseFragment = new FragmentIssueCourse();
			ft.add(R.id.fragment_container, issueCourseFragment);
			break;
		case 101:
			// TODO
//			 FragmentWebView coachWebviewFragment = new FragmentWebView(// 0,
//			 // "教练列表",
//			 "file:///android_asset/coachList.html" + getPath());
//			 ft.add(R.id.fragment_container, coachWebviewFragment);

			FragmentCoachList coachList = new FragmentCoachList();
			ft.add(R.id.fragment_container, coachList);
			break;
		case 102:
			FragmentWebView clubWebviewFragment = new FragmentWebView(// 0,
																		// "俱乐部列表",
					"file:///android_asset/clubList.html" + getPath());
			ft.add(R.id.fragment_container, clubWebviewFragment);
			break;
		case 103:
			states.add(getSupportFragmentManager().getBackStackEntryCount() + 1);
			FragmentCourse courseFragment = new FragmentCourse();
			ft.add(R.id.fragment_container, courseFragment);
			break;
		case 104:
			FragmentWebView foodmerWebviewFragment = new FragmentWebView(// 0,
																			// "配餐列表",
					"file:///android_asset/foodmerList.html");
			ft.add(R.id.fragment_container, foodmerWebviewFragment);
			break;
		case 201:
			FragmentWebView findFriendWebviewFragment = new FragmentWebView(// 1,
																			// "查找好友",
					"file:///android_asset/findFriend.html");
			ft.add(R.id.fragment_container, findFriendWebviewFragment);
			break;
		case 202:
			FragmentWebView dynWebviewFragment = new FragmentWebView(// 1,
																		// "好友列表",
					"file:///android_asset/dynList.html");
			ft.add(R.id.fragment_container, dynWebviewFragment);
			break;
		case 301:
			FragmentSearch searchFragment = new FragmentSearch();
			ft.add(R.id.fragment_container, searchFragment);
			break;
		case 402:// 订单列表
			// TODO
//			 FragmentOrderList orderFragment = new FragmentOrderList();
			OrderListFragment orderFragment = new OrderListFragment();
			ft.add(R.id.fragment_container, orderFragment);
			break;
		case 403:// 个人主页
			states.add(getSupportFragmentManager().getBackStackEntryCount() + 1);
			FragmentMemberClub memberClubFragment = new FragmentMemberClub();
			ft.add(R.id.fragment_container, memberClubFragment);
			break;

		case 501:// 按摩项目列表
			FragmentMassage fragmentMassage = new FragmentMassage();
			ft.add(R.id.fragment_container, fragmentMassage);
			break;
		case 502:// 按摩详情
			FragmentMassageDetails fragmentMassageDetails = new FragmentMassageDetails();
			ft.add(R.id.fragment_container, fragmentMassageDetails);
			break;
		case 503:// 按摩订单
			FragmentMassageOrder fragmentMassageorder = new FragmentMassageOrder();
			ft.add(R.id.fragment_container, fragmentMassageorder);
			break;
		case 504:// 按摩师列表
			FragmentMassagePersonList fragmentMassagePersonList = new FragmentMassagePersonList();
			ft.add(R.id.fragment_container, fragmentMassagePersonList);
			break;
		case 505:// 支付页面
			FragmentPayAmount amount = new FragmentPayAmount();
			ft.add(R.id.fragment_container, amount);
			break;
		case 506:// 支付页面
			FragmentEvaluate evaluate = new FragmentEvaluate();
			ft.add(R.id.fragment_container, evaluate);
			break;
		case 601:// 热门动态
			FragmentHotDynamic dynamic = new FragmentHotDynamic();
			ft.add(R.id.fragment_container, dynamic);
			break;
		case 602:// 所有群组
			FragmentFriendGroup friendGroup = new FragmentFriendGroup();
			ft.add(R.id.fragment_container, friendGroup);
			break;
		case 603:// 我加入的群组
			FragmentMyGroup myGroup = new FragmentMyGroup();
			ft.add(R.id.fragment_container, myGroup);
			break;
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
		Log.e("MainActivity", getSupportFragmentManager()
				.getBackStackEntryCount() + "");
	}

	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{
			final EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			refreshUI();
			break;
		}

		case EventOfflineMessage: {
			refreshUI();
			break;
		}

		case EventConversationListChanged: {
			refreshUI();
			break;
		}

		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				updateUnreadLabel();
			}
		});
	}

	@Override
	public void back(View view) {
		super.back(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(
					connectionListener);
		}

		if (groupChangeListener != null) {
			EMGroupManager.getInstance().removeGroupChangeListener(
					groupChangeListener);
		}

		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			if (currentPage == 1) {
				chatHistoryFragment.refresh();
			}
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(
				Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 好友变化listener
	 */
	public class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					String st10 = getResources().getString(
							R.string.have_you_removed);
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast.makeText(
								MainActivity.this,
								ChatActivity.activityInstance
										.getToChatUsername() + st10,
								Toast.LENGTH_SHORT).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
					// 刷新ui
					// contactListFragment.refresh();
					// chatHistoryFragment.refresh();
				}
			});

		}

		@Override
		public void onContactInvited(String username, String reason) {

			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {

			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");
		}

	}

	/**
	 * 连接监听listener
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance()
					.isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance()
					.isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				new Thread() {
					@Override
					public void run() {
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
				if (!groupSynced) {
					asyncFetchGroupsFromServer();
				}

				if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
					asyncFetchBlackListFromServer();
				}
			}

		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} 
				}

			});
		}
	}

	/**
	 * MyGroupChangeListener
	 */
	public class MyGroupChangeListener implements EMGroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {

			boolean hasGroup = false;
			for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
				if (group.getGroupId().equals(groupId)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup)
				return;

			// 被邀请
			String st3 = getResources().getString(
					R.string.Invite_you_to_join_a_group_chat);
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + " " + st3));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 1)
						// chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(MainActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {

			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (CommonUtils.getTopActivity(MainActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {

			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {

			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {

			String st4 = getResources().getString(
					R.string.Agreed_to_your_group_chat_application);
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + " " + st4));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					// if (currentTabIndex == 0)
					// chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}
	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	private void showWarning(){
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
				SharedPreferencesUtils.clearData(MainActivity.this);
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
			}
			
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().getMemid().equals("")) {
			AlertDialog.Builder builder=new Builder(MainActivity.this);
			builder.setTitle("重要提示");
			builder.setMessage("登录已经过期，请重新登录！");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					showWarning();
				}
			});
			builder.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface arg0) {
					showWarning();
				}
			});
			builder.create();
			builder.show();
		}
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			// updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(true, null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return returnBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean returnBack() {
		FragmentManager ft = getSupportFragmentManager();
		int size = ft.getFragments() != null ? ft.getFragments().size() : 0;
		int backStackEntryCount = ft.getBackStackEntryCount();
		Log.e("MainActivity", size + ";" + backStackEntryCount);
		if (backStackEntryCount == 1 || size == 1) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				exitTime = System.currentTimeMillis();
				Toast.makeText(MainActivity.this, "再按一次退出羿健康",
						Toast.LENGTH_SHORT).show();
			} else {
				MyApplication.getInstance().mLocationClient.stop();
				finish();
				System.exit(0);
			}
			return true;
		} else {
			// 返回时，当前为隐藏底栏改为显示底栏
			int state = -1;
			if (states != null && states.size() > 0) {
				state = states.get(states.size() - 1);// 取最后一个
				if (backStackEntryCount == state) {
					hideTitleBar(0);
					states.remove(states.size() - 1);
				} else if (backStackEntryCount - 1 == state) {
					hideTitleBar(1);

				}
			}
			ft.popBackStack();
			return true;
		}
	}
	
	

}
