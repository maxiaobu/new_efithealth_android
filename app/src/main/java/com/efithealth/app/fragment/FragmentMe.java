package com.efithealth.app.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ExitConfimActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.MyInfo;
import com.efithealth.app.activity.QRCodeActivity;
import com.efithealth.app.javabean.Member;
import com.efithealth.app.javabean.MemberMolder;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.color;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMe extends BaseFragment implements OnClickListener {
	private View v;
	private LinearLayout ll_shop, ll_order, ll_yy, ll_money, ll_showOrHide;
	private static TextView tv_name;
	private TextView tv_sao;
	private static TextView tv_shop;
	private static TextView tv_order;
	private static TextView tv_yy;
	private static TextView tv_money;
	private TextView tv_friend;
	private TextView tv_infomation;
	private TextView tv_vip;
	private TextView tv_play;
	private TextView tv_manager;
	private TextView tv_class;
	private TextView tv_club;
	private TextView tv_student;
	private TextView tv_time;
	private TextView tv_black;
	private TextView tv_top_set;
	private TextView tv_out;
	private TextView tv_approve;
	private static ImageView iv_head;
	private static Member member;
	private boolean flag_paly = false;
	private static int money = 0;
	private static FragmentActivity instance=null;

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			getData1();
		}
	};

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		instance=getActivity();
		v = inflater.inflate(R.layout.fragment_me, container, false);
		return v;
	}

	protected static void getData1() {
		RequestQueue queue = VolleySingleton.getVolleySingleton(instance)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, Constant.URL_ME,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						oneTime(response);

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				return map;
			}
		};
		queue.add(request);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		iv_head = (ImageView) v.findViewById(R.id.me_iv_head);
		ll_shop = (LinearLayout) v.findViewById(R.id.me_ll_shop);
		ll_order = (LinearLayout) v.findViewById(R.id.me_ll_order);
		ll_yy = (LinearLayout) v.findViewById(R.id.me_ll_yy);
		ll_money = (LinearLayout) v.findViewById(R.id.me_ll_money);
		ll_showOrHide = (LinearLayout) v.findViewById(R.id.me_show_or_hide);
		tv_name = (TextView) v.findViewById(R.id.me_tv_name);
		tv_sao = (TextView) v.findViewById(R.id.me_tv_sao);
		tv_shop = (TextView) v.findViewById(R.id.me_tv_shop);
		tv_order = (TextView) v.findViewById(R.id.me_tv_order);
		tv_yy = (TextView) v.findViewById(R.id.me_tv_yy);
		tv_money = (TextView) v.findViewById(R.id.me_tv_money);
		tv_friend = (TextView) v.findViewById(R.id.me_tv_friend);
		tv_infomation = (TextView) v.findViewById(R.id.me_tv_infomation);
		tv_vip = (TextView) v.findViewById(R.id.me_tv_vip);
		tv_play = (TextView) v.findViewById(R.id.me_tv_play);
		tv_manager = (TextView) v.findViewById(R.id.me_tv_manager);
		tv_class = (TextView) v.findViewById(R.id.me_tv_class);
		tv_club = (TextView) v.findViewById(R.id.me_tv_club);
		tv_student = (TextView) v.findViewById(R.id.me_tv_student);
		tv_time = (TextView) v.findViewById(R.id.me_tv_time);
		tv_black = (TextView) v.findViewById(R.id.me_tv_black);
		tv_top_set = (TextView) v.findViewById(R.id.me_tv_top_set);
		tv_out = (TextView) v.findViewById(R.id.me_tv_out);
		tv_approve = (TextView) v.findViewById(R.id.me_tv_approve);

		iv_head.setOnClickListener(this);
		tv_sao.setOnClickListener(this);
		ll_shop.setOnClickListener(this);
		ll_order.setOnClickListener(this);
		ll_yy.setOnClickListener(this);
		ll_money.setOnClickListener(this);
		tv_yy.setOnClickListener(this);
		tv_friend.setOnClickListener(this);
		tv_infomation.setOnClickListener(this);
		tv_vip.setOnClickListener(this);
		tv_manager.setOnClickListener(this);
		tv_class.setOnClickListener(this);
		tv_club.setOnClickListener(this);
		tv_student.setOnClickListener(this);
		tv_time.setOnClickListener(this);
		tv_black.setOnClickListener(this);
		tv_top_set.setOnClickListener(this);
		tv_out.setOnClickListener(this);
		tv_play.setOnClickListener(this);
		tv_approve.setOnClickListener(this);

		// 判断身份的
		String memrole = (String) SharedPreferencesUtils.getParam(
				getActivity(), "memrole", "mem");
		Log.i("djy", memrole);
		if (memrole.equals("coach")) {
			ll_showOrHide.setVisibility(View.VISIBLE);
			tv_approve.setVisibility(View.GONE);
			setShowOrHide(true);
		} else if (memrole.equals("mem")) {
			tv_approve.setVisibility(View.VISIBLE);
			ll_showOrHide.setVisibility(View.GONE);
			setShowOrHide(false);
		}

		getData(Constant.URL_ME, 1);
		getData(Constant.URL_SIGN, 2);

		super.onActivityCreated(savedInstanceState);
	}

	private void getData(String url, final int type) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("fragmentme", response);

						showData(type, response);

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				return map;
			}
		};
		queue.add(request);
	}
	
	private static void oneTime(String json){
		Gson gson = new Gson();
		MemberMolder molder = gson.fromJson(json, MemberMolder.class);
		String msg = molder.getMsgFlag();
		if (msg.equals("1")) {
			member = molder.getMember();
			ImageLoader.getInstance().displayImage(
					member.getImgsfilename(), iv_head,
					MyApplication.getInstance().initHeadDisImgOption());
			tv_name.setText(member.getNickname());
			String moneyS=molder.getYcoinnum();
			money=Integer.parseInt(moneyS);
			int qian=0;
			if (moneyS.length()>5&&moneyS.length()<=8) {
				qian = Integer.parseInt(moneyS.substring(0, moneyS.length()-3));
				tv_money.setText(qian*1.0f/10 + "万");
			}else if (moneyS.length()>8) {
				qian = Integer.parseInt(moneyS.substring(0, moneyS.length()-7));
				tv_money.setText(qian*1.0f/10 + "亿");
			}else{
				qian = Integer.parseInt(moneyS);
				tv_money.setText(qian+"");
			}
			tv_shop.setText(molder.getShopcount());
			tv_order.setText(molder.getOrdercount());
			tv_yy.setText(molder.getLessoncount());
		} else {
			Toast.makeText(instance, "加载失败，稍后重试", 0).show();
		}
	}

	private void showData(int key, String json) {
		switch (key) {
		case 1:
			oneTime(json);
			break;

		case 2:
			if (json.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(json);
					String msg2 = jsonObject.getString("msgFlag");
					int count = jsonObject.getInt("count");
					if (msg2.equals("1")) {
						if (count > 0) {
							tv_play.setText("已签到");
							tv_play.setTextColor(Color.parseColor("#8a8a8a"));
							flag_paly = true;
						} else {
							tv_play.setText("签到");
							tv_play.setTextColor(Color.parseColor("#4d4d4d"));
							flag_paly = false;
						}
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "加载失败，稍后重试", 0).show();
					e.printStackTrace();
				}
			}
			break;

		case 3:
			if (json.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(json);
					String msg2 = jsonObject.getString("msgFlag");
					int count = jsonObject.getInt("count");
					if (msg2.equals("1")) {
						if (count > 0) {
							tv_play.setText("已签到");
							tv_play.setTextColor(Color.parseColor("#8a8a8a"));
							money += 300;
							if (money>100000&&money<=99999999) {
								tv_money.setText((money/1000)*1.0f/10 + "万");
							}else if (money>=100000000) {
								tv_money.setText((money/10000000)*1.0f/10 + "亿");
							}else{
								tv_money.setText(money+"");
							}
							Toast.makeText(getActivity(), "签到成功", 0).show();
							flag_paly = true;
						} else {
							tv_play.setText("签到");
							tv_play.setTextColor(Color.parseColor("#4d4d4d"));
							Toast.makeText(getActivity(), "签到失败，稍后重试", 0)
									.show();
							flag_paly = false;
						}
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "加载失败，稍后重试", 0).show();
					e.printStackTrace();
				}
			}

			break;
		}
	}

	private void setShowOrHide(boolean isShow) {
		tv_approve.setEnabled(!isShow);
		tv_manager.setEnabled(isShow);
		tv_class.setEnabled(isShow);
		tv_club.setEnabled(isShow);
		tv_student.setEnabled(isShow);
		tv_time.setEnabled(isShow);
	}

	@Override
	public void onClick(View key) {
		String page = "";
		switch (key.getId()) {
		// 购物车
		case R.id.me_ll_shop:
			SharedPreferencesUtils.setParam(getActivity(), "me_change", "yes");
			page = "file:///android_asset/shopcart.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 订单
		case R.id.me_ll_order:
			SharedPreferencesUtils.setParam(getActivity(), "me_change", "yes");
			MainActivity.instance.setTabSelection(402);
			break;

		// 预约
		case R.id.me_ll_yy:
			SharedPreferencesUtils.setParam(getActivity(), "me_change", "yes");
			page = "file:///android_asset/bespeak.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 余额
		case R.id.me_ll_money:
			SharedPreferencesUtils.setParam(getActivity(), "me_change", "yes");
			page = "file:///android_asset/account.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 二维码
		case R.id.me_tv_sao:
			Intent intent = new Intent(getActivity(), QRCodeActivity.class);
			intent.putExtra("yes", "0");
			startActivity(intent);
			break;

		// 我的好友
		case R.id.me_tv_friend:
			page = "file:///android_asset/dynList.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 个人信息
		case R.id.me_tv_infomation:
			SharedPreferencesUtils.setParam(getActivity(), "me_change", "yes");
			Intent intent1 = new Intent(getActivity(), MyInfo.class);
			startActivity(intent1);
			break;

		// 我的会员卡
		case R.id.me_tv_vip:
			page = "file:///android_asset/memCard.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 签到
		case R.id.me_tv_play:
			if (flag_paly) {
				Toast.makeText(getActivity(), "今天已签到", 0).show();
			} else {
				getData(Constant.URL_SIGNIN, 3);
			}
			break;

		// 课程管理
		case R.id.me_tv_manager:
			MainActivity.instance.setTabSelection(100);
			break;

		// 我的教学预约
		case R.id.me_tv_class:
			page = "file:///android_asset/coachBespeak.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 俱乐部列表
		case R.id.me_tv_club:
			page = "file:///android_asset/bindClubList.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 我的学员
		case R.id.me_tv_student:
			page = "file:///android_asset/coachMember.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 档期管理
		case R.id.me_tv_time:
			page = "file:///android_asset/manager.html?coachid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 黑名单
		case R.id.me_tv_black:
			page = "file:///android_asset/blackerList.html?memid="
					+ member.getMemid();
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 顶部设置
		case R.id.me_tv_top_set:
			page = "file:///android_asset/pluginSetting.html?memid="
					+ member.getMemid() + "&page=personal";
			MainActivity.instance.setTabWebViewSelection(page);
			break;

		// 退出
		case R.id.me_tv_out:
			startActivity(new Intent(getActivity(), ExitConfimActivity.class));
			break;

		// 个人中心
		case R.id.me_iv_head:
			page = "?tarid=" + member.getMemid() + "&role="+member.getMemrole()
					+ "&memid=" + MyApplication.getInstance().getMemid();
			page += "&memphoto=" + member.getImgsfile() + "&memnickname="
					+ member.getNickname();
			if (page != null && page.length() > 0) {
				SharedPreferencesUtils.setParam(getActivity(), "page", page);
			}
			MainActivity.instance.setTabSelection(403);
			break;

		// 申请认证
		case R.id.me_tv_approve:
			page = "file:///android_asset/coachcertApply.html?memid="
					+ member.getMemid() + "&nickname=" + member.getNickname()
					+ "&imgsfile=" + member.getImgsfile();
			MainActivity.instance.setTabWebViewSelection(page);
			break;
		}

	}

}
