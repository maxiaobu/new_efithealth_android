package com.efithealth.app.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.RevampAddress;
import com.efithealth.app.javabean.MassageOrderId;
import com.efithealth.app.javabean.MassageOrderTime;
import com.efithealth.app.javabean.Masseurlist;
import com.efithealth.app.task.VolleySingleton;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMassageOrder extends BaseFragment implements
		OnClickListener {

	private View v;
	private ImageView iv_back, iv_auto, iv_info,iv_address;
	private RelativeLayout rl_auto, rl_list;
	private Button btn_commit;
	private TextView tv_nickName;
	private TextView tv_phone, tv_address, tv_clubname;
	private String isplateassign = 1 + "";
	private String reservetime = "";
	private List<TextView> list_tv = new ArrayList<TextView>();
	private GridView gv;
	private MyAdapter adapter_time;
	private TextView tv_time_j, tv_time_m, tv_time_h;
	private int position_list = -1;
	private Map<Integer, MassageOrderTime> map_time = new HashMap<Integer, MassageOrderTime>();

	private static TextView tv_list;
	private static int flag_time = 0;
	public static String masseurid = "";
	public static String datomworks = "";
	public static String todworks = "";
	public static String tomworks = "";
	public static String orderId = "";
	private static List<MassageOrderTime> list_time = new ArrayList<>();
	public static List<Masseurlist> list = new ArrayList<Masseurlist>();

	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list.size() > 0) {
				Masseurlist masseurlist = list.get(0);
				tv_list.setText(masseurlist.getMname());
				masseurid = masseurlist.getMasseurid();
				datomworks = masseurlist.getDatomworks();
				todworks = masseurlist.getTodworks();
				tomworks = masseurlist.getTomworks();
				upListData(flag_time);
			} else {
				showToast();
			}
		};
	};

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_massage_order, container, false);
		return v;
	}

	private static void showToast() {
		Toast.makeText(MainActivity.instance, "选择失败，请重新选择", 0).show();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		initData1(todworks);
		reservetime = getnowTime(1);
		tv_time_j = (TextView) v.findViewById(R.id.tv_time_j);
		tv_time_m = (TextView) v.findViewById(R.id.tv_time_m);
		tv_time_h = (TextView) v.findViewById(R.id.tv_time_h);
		list_tv.add(tv_time_j);
		list_tv.add(tv_time_m);
		list_tv.add(tv_time_h);

		gv = (GridView) v.findViewById(R.id.massage_order_gv);
		iv_back = (ImageView) v.findViewById(R.id.back_massage_order);
		rl_auto = (RelativeLayout) v.findViewById(R.id.massage_order_ll_auto);
		rl_list = (RelativeLayout) v.findViewById(R.id.massage_order_rl_list);
		iv_auto = (ImageView) v.findViewById(R.id.massage_order_iv_auto);
		iv_info = (ImageView) v.findViewById(R.id.massage_order_iv_myinfo);
		tv_list = (TextView) v.findViewById(R.id.massage_order_tv__list_name);
		tv_nickName = (TextView) v.findViewById(R.id.massage_order_tv_nickname);
		tv_phone = (TextView) v.findViewById(R.id.massage_order_tv_phone);
		btn_commit = (Button) v.findViewById(R.id.massage_btn_commit);
		tv_address = (TextView) v.findViewById(R.id.massage_order_tv_address);
		tv_clubname = (TextView) v.findViewById(R.id.massage_order_tv_clubname);
		iv_address=(ImageView) v.findViewById(R.id.massage_order_iv_address);
		tv_nickName.setText(MyApplication.getInstance().getMyInfo()
				.get("nickname"));
		tv_phone.setText(MyApplication.getInstance().getMyInfo()
				.get("addresphone"));
		
		if (tv_address.getLineCount() > 3) {
			tv_address.setLines(3);
			tv_address.setEllipsize(TruncateAt.END);
		}

		if (FragmentMassageDetails.map.get("address") != null) {
			tv_address.setText(FragmentMassageDetails.map.get("address")
					+ FragmentMassageDetails.map.get("clubname"));
			tv_clubname.setText(FragmentMassageDetails.map.get("clubname"));
			iv_address.setImageResource(R.drawable.selectimg);
		}else{
			iv_address.setImageResource(-1);
			tv_address.setText("您还没有加入俱乐部");
			tv_clubname.setText("");
		}

		tv_time_j.setOnClickListener(this);
		tv_time_m.setOnClickListener(this);
		tv_time_h.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_info.setOnClickListener(this);
		rl_auto.setOnClickListener(this);
		rl_list.setOnClickListener(this);
		btn_commit.setOnClickListener(this);

		changeColor(flag_time);
		adapter_time = new MyAdapter();
		gv.setAdapter(adapter_time);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 11) {
			if (data != null) {
				String name = data.getStringExtra("nick");
				String phone = data.getStringExtra("phone");
				tv_nickName.setText(name);
				tv_phone.setText(phone);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendMassageData(final int key, String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentMassageOrder", response);
						Gson gson = new Gson();
						if (key == 2) {
							MassageOrderId massageOrderId = gson.fromJson(
									response, MassageOrderId.class);
							if (massageOrderId.getMsgFlag().equals("1")) {
								orderId = massageOrderId.getOrderid();
								MainActivity.instance.setTabSelection(505);
							} else {
								Toast.makeText(getActivity(), "订单确认失败，请重试", 0)
										.show();
							}
						} else {

						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				if (key == 2) {
					map.put("massageid",
							FragmentMassageDetails.map.get("massageid"));
					map.put("massagename",
							FragmentMassageDetails.map.get("massagename"));
					map.put("clubid", FragmentMassageDetails.map.get("clubid"));
					map.put("ordamt", FragmentMassageDetails.map.get("ordamt"));
					map.put("memid", MyApplication.getInstance().getMemid());
					map.put("reservetime",
							reservetime + map_time.get(position_list).getTime()
									+ "");
					if (isplateassign.equals("0")) {
						map.put("masseurid", masseurid);
					}
					map.put("isplateassign", isplateassign);
				} else {

				}
				return map;
			}
		};
		queue.add(request);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back_massage_order:// 返回键
			MainActivity.instance.returnBack();
			break;

		case R.id.massage_order_iv_myinfo:// 修改收货信息
			Intent intent = new Intent(getActivity(), RevampAddress.class);
			startActivityForResult(intent, 11);
			break;
		case R.id.massage_order_ll_auto:// 自动分配按摩师
			if (isplateassign.equals("1")) {
				Toast.makeText(getActivity(), "您以选择自动分配按摩师，无需再按", 0).show();
			} else {
				isplateassign = 1 + "";
				tv_list.setText("");
				iv_auto.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.massage_order_rl_list:// 进入选择按摩师列表
			FragmentMassagePersonList.position_list = -1;
			iv_auto.setVisibility(View.GONE);
			isplateassign = 0 + "";
			MainActivity.instance.setTabSelection(504);
			break;
		case R.id.massage_btn_commit:
			if (isplateassign.equals("0") && tv_list.getText().equals("")) {
				Toast.makeText(getActivity(), "请选择按摩师或者选择自动分配", 0).show();
			} else if (map_time.size() < 1) {
				Toast.makeText(getActivity(), "请选择按摩师时间", 0).show();
			} else {
				sendMassageData(2, Constant.URL_MASSAGE_ORDER);
			}
			break;
		case R.id.tv_time_j:
			flag_time = 0;
			reservetime = getnowTime(1);
			initData1(todworks);
			changeColor(flag_time);
			map_time.clear();
			adapter_time.notifyDataSetChanged();
			break;
		case R.id.tv_time_m:
			flag_time = 1;
			reservetime = Integer.parseInt(getnowTime(1)) + 10 + "";
			initData23(tomworks);
			changeColor(flag_time);
			map_time.clear();
			adapter_time.notifyDataSetChanged();
			break;
		case R.id.tv_time_h:
			flag_time = 2;
			reservetime = Integer.parseInt(getnowTime(1)) + 20 + "";
			initData23(datomworks);
			changeColor(flag_time);
			map_time.clear();
			adapter_time.notifyDataSetChanged();
			break;
		}

	}

	private void changeColor(int n) {
		for (int i = 0; i < list_tv.size(); i++) {
			if (i == n) {
				list_tv.get(n).setTextColor(
						getResources().getColor(R.color.brown));
			} else {
				list_tv.get(i).setTextColor(
						getResources().getColor(R.color.gray));
			}
		}
	}

	private static void upListData(int key) {
		switch (key) {
		case 0:
			initData1(todworks);
			break;

		case 1:
			initData23(tomworks);
			break;
		case 2:
			initData23(datomworks);
			break;
		}
	}

	private static void initData1(String str) {
		list_time.clear();
		if (str.equals("")) {
			int n = Integer.parseInt(getnowTime(0)) + 2;
			for (int i = 10; i <= 21; i++) {
				MassageOrderTime massageOrderTime = new MassageOrderTime();
				if (i <= n) {
					massageOrderTime.setFlage_ischecked(1);
				}
				massageOrderTime.setTime(i + "");
				massageOrderTime.setSelected(false);
				list_time.add(massageOrderTime);
			}
		} else {
			// "\\|"
			String[] time = str.split("\\|");
			for (int i = 0; i < time.length; i++) {
				time[i] = time[i].substring(time[i].length() - 2,
						time[i].length());
			}
			for (int i = 10; i <= 21; i++) {
				MassageOrderTime massageOrderTime = new MassageOrderTime();
				if (i <= Integer.parseInt(getnowTime(0))) {
					massageOrderTime.setFlage_ischecked(1);
				}
				for (int j = 0; j < time.length; j++) {
					if (time[j].equals(i + "")) {
						massageOrderTime.setFlage_ischecked(1);
					}
				}
				massageOrderTime.setTime(i + "");
				massageOrderTime.setSelected(false);
				list_time.add(massageOrderTime);
			}

		}
	}

	private static void initData23(String str) {
		list_time.clear();

		if (str.equals("")) {
			for (int i = 10; i <= 21; i++) {
				MassageOrderTime massageOrderTime = new MassageOrderTime();
				massageOrderTime.setFlage_ischecked(0);
				massageOrderTime.setTime(i + "");
				massageOrderTime.setSelected(false);
				list_time.add(massageOrderTime);
			}
		} else {
			String time[] = str.split("\\|");
			for (int i = 0; i < time.length; i++) {
				time[i] = time[i].substring(time[i].length() - 2,
						time[i].length());
			}
			for (int i = 10; i <= 21; i++) {
				MassageOrderTime massageOrderTime = new MassageOrderTime();
				for (int j = 0; j < time.length; j++) {
					if (time[j].equals(i + "")) {
						massageOrderTime.setFlage_ischecked(1);
					}
				}
				massageOrderTime.setTime(i + "");
				massageOrderTime.setSelected(false);
				list_time.add(massageOrderTime);
			}
		}
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list_time.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_time.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(final int position, View v, ViewGroup arg2) {
			v = LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_massage_order_time, null);
			final TextView tv = (TextView) v
					.findViewById(R.id.fragment_massage_order_time_tv);
			tv.setText(list_time.get(position).getTime() + ":00");
			if (list_time.get(position).getFlage_ischecked() != 0) {
				tv.setBackgroundResource(R.drawable.massage_order_gv_back_1);
			} else {
				if (list_time.get(position).isSelected()) {
					tv.setBackgroundResource(R.drawable.massage_order_gv_back_2);
				} else {
					tv.setBackgroundResource(R.drawable.coach_back_pop);
				}
			}
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (list_time.get(position).getFlage_ischecked() != 0) {
						Toast.makeText(getActivity(), "灰色区域时间不可选，请重新选择时间！！！", 0)
								.show();
					} else {
						if (!list_time.get(position).isSelected()) {
							if (position_list > -1 && position_list != position) {
								list_time.get(position_list).setSelected(false);
								map_time.remove(position_list);
							}
							list_time.get(position).setSelected(true);
							// tv.setBackgroundResource(R.drawable.massage_order_gv_back_2);
							map_time.put(position, list_time.get(position));
						} else {
							Toast.makeText(getActivity(), "已选择了这个时间", 0).show();
						}
						position_list = position;
						notifyDataSetChanged();
					}

				}
			});
			return v;
		}

	}

	@SuppressLint("SimpleDateFormat")
	private static String getnowTime(int n) {
		long times = System.currentTimeMillis();
		Date date = new Date(times);
		SimpleDateFormat format = null;
		if (n == 0) {
			format = new SimpleDateFormat("HH");
		} else {
			format = new SimpleDateFormat("yyyyMMdd");
		}
		String time = format.format(date);
		return time;
	}

}
