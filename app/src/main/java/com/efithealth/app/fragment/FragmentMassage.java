package com.efithealth.app.fragment;

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
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.adapter.MassageListAdapter;
import com.efithealth.app.javabean.MassageList;
import com.efithealth.app.javabean.MassageListModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMassage extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {
	private View view;
	private PopMassageLeft popMassageLeft;
	private PopMassageRight popMassageRight;
	private LinearLayout ll_left, ll_right;
	private RelativeLayout rl_title;
	private SwipeRefreshLayout swipeLayout;
	private ImageView iv_back, iv_left, iv_right;
	private TextView tv_left, tv_right;
	private WaterDropListView lv_massage;
	private boolean flag_root = true, flag_click_left = true,
			flag_click_right = true, list_flag_t = true, list_flag_b = true,
			list_r_flag_t = true, list_r_flag_b = true;
	private ProgressDialog dialog;
	private RelativeLayout tv1, tv2, tv4, tv5;
	private String pseqdesc = "", pfildesc = "";
	// 请求页码
	private int pageIndex = 1, n1 = 2, n2 = 2, left_n = 0, right_n = 0;
	private List<MassageList> list_adapter = new ArrayList<MassageList>();
	private List<MassageList> list_model = new ArrayList<MassageList>();
	private MassageListAdapter adapter;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_massage, container, false);
		ShowLoadData();
		FragmentHome.flag = false;
		return view;
	}

	@Override
	public void onDestroy() {
		FragmentHome.flag = true;
		if (left_n != 0) {
			popMassageLeft.dismiss();
		}
		if (right_n != 0) {
			popMassageRight.dismiss();
		}
		huanYuan();
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		ll_error = (LinearLayout) view.findViewById(R.id.ll_error);
		lv_massage = (WaterDropListView) view.findViewById(R.id.massage_lv);
		ll_left = (LinearLayout) view.findViewById(R.id.ll_left_massage);
		ll_right = (LinearLayout) view.findViewById(R.id.ll_right_massage);
		rl_title = (RelativeLayout) view.findViewById(R.id.rl_massage_title);
		iv_back = (ImageView) view.findViewById(R.id.back_massage_1);
		iv_left = (ImageView) view.findViewById(R.id.iv_left_massage);
		iv_right = (ImageView) view.findViewById(R.id.iv_right_massage);
		tv_left = (TextView) view.findViewById(R.id.tv_left_massage);
		tv_right = (TextView) view.findViewById(R.id.tv_right_massage);

		// 设置刷新时动画的颜色，可以设置4个
		swipeLayout.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						// 下拉刷新页面
						pageIndex = 1;
						getMassageData(2);
					}
				});

		rl_title.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (left_n != 0) {
						popMassageLeft.dismiss();
						huanYuan();
						return true;
					}
					if (right_n != 0) {
						popMassageRight.dismiss();
						huanYuan();
						return true;
					}
				}
				return false;
			}
		});

		ll_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flag_click_right = true;
				if (flag_click_left) {
					if (right_n != 0) {
						popMassageRight.dismiss();
						right_n = 0;
					}
					popMassageLeft = new PopMassageLeft(getActivity(), view
							.findViewById(R.id.coach_massage_root));
					iv_left.setImageResource(R.drawable.top_all);
					iv_right.setImageResource(R.drawable.bottom_all);
					left_n = 1;
					flag_click_left = false;
				} else {
					iv_left.setImageResource(R.drawable.bottom_all);
					iv_right.setImageResource(R.drawable.bottom_all);
					flag_click_left = true;
					left_n = 0;
					right_n = 0;
					popMassageLeft.dismiss();
				}
			}
		});

		ll_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flag_click_left = true;
				if (flag_click_right) {
					if (left_n != 0) {
						popMassageLeft.dismiss();
						left_n = 0;
					}
					popMassageRight = new PopMassageRight(getActivity(), view
							.findViewById(R.id.coach_massage_root));
					iv_right.setImageResource(R.drawable.top_all);
					iv_left.setImageResource(R.drawable.bottom_all);
					right_n = 1;
					flag_click_right = false;
				} else {
					iv_left.setImageResource(R.drawable.bottom_all);
					iv_right.setImageResource(R.drawable.bottom_all);
					flag_click_right = true;
					right_n = 0;
					left_n = 0;
					popMassageRight.dismiss();
				}
			}
		});

		lv_massage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (left_n != 0) {
						popMassageLeft.dismiss();
						huanYuan();
						return true;
					}
					if (right_n != 0) {
						popMassageRight.dismiss();
						huanYuan();
						return true;
					}
				}
				return false;
			}
		});

		lv_massage.setWaterDropListViewListener(this);
		lv_massage.setPullLoadEnable(true, true);
		lv_massage.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		lv_massage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String massageid = list_adapter.get(position).getMassageid();
				SharedPreferencesUtils.setParam(getActivity(), "massageid",
						massageid);
				MainActivity.instance.setTabSelection(502);
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				MainActivity.instance.returnBack();
			}
		});

		getMassageData(1);
		super.onActivityCreated(savedInstanceState);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		if (flag_root) {
			pageIndex += 1;
			getMassageData(3);
		}

	}

	private void getMassageData(final int type) {
		RequestQueue mRequestQueue = VolleySingleton.getVolleySingleton(
				getActivity()).getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MASSAGE_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("djy", response);
						dialog.dismiss();
						Gson gson = new Gson();
						MassageListModel massageListModel = gson.fromJson(
								response, MassageListModel.class);
						String msg = massageListModel.getMsgFlag();
						String errer = massageListModel.getMsgContent();
						list_model = massageListModel.getMassageList();
						if (list_model != null) {
							if (list_model.size() > 0) {
								ll_error.setVisibility(View.GONE);

								if (list_model.size() < 10) {
									flag_root = false;
									lv_massage.setPullLoadEnable(false, true);

								} else {
									flag_root = true;
									lv_massage.setPullLoadEnable(true, true);
								}

								if (msg.equals("1")) {
									ShowData(type);
								} else {
									Toast.makeText(getActivity(), errer, 0)
											.show();
								}
							} else {
								ll_error.setVisibility(View.VISIBLE);
							}

						} else {
							ll_error.setVisibility(View.VISIBLE);
						}

					}

				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("pageIndex", pageIndex + "");
				map.put("pseqdesc", pseqdesc);
				map.put("pfildesc", pfildesc);
				return map;
			}
		};
		mRequestQueue.add(request);
	}

	private void ShowData(int index) {
		switch (index) {
		case 1:
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 5) {
				lv_massage.setPullLoadEnable(false, false);
			}
			adapter = new MassageListAdapter(getActivity(), list_adapter);
			lv_massage.setAdapter(adapter);
			break;
		case 2:
			list_adapter.clear();
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 5) {
				lv_massage.setPullLoadEnable(false, false);
			}
			adapter.notifyDataSetChanged();
			swipeLayout.setRefreshing(false);
			Toast.makeText(getActivity(), "刷新成功", 0).show();
			break;
		case 3:
			lv_massage.stopLoadMore();
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 5) {
				lv_massage.setPullLoadEnable(false, false);

			}
			adapter.notifyDataSetChanged();
			Toast.makeText(getActivity(), "数据加载成功", 0).show();
			break;
		case 4:

			list_adapter.clear();
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 5) {
				lv_massage.setPullLoadEnable(false, false);

			}
			adapter = new MassageListAdapter(getActivity(), list_adapter);
			lv_massage.setAdapter(adapter);
			Toast.makeText(getActivity(), "刷新成功", 0).show();
			break;

		}
	}

	// 加载框
	private void ShowLoadData() {
		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在加载数据……");
		dialog.show();

	}

	private void loadDataMassageList() {
		pageIndex = 1;
		ShowLoadData();
		getMassageData(4);
		left_n = 0;
		right_n = 0;
	}

	private void huanYuan() {
		flag_click_right = true;
		flag_click_left = true;
		left_n = 0;
		right_n = 0;
		iv_left.setImageResource(R.drawable.bottom_all);
		iv_right.setImageResource(R.drawable.bottom_all);
	}

	public class PopMassageLeft extends PopupWindow implements OnClickListener {

		@SuppressWarnings("deprecation")
		public PopMassageLeft(Context mContext, View parent) {

			View view = View.inflate(mContext,
					R.layout.massage_list_popuwindow_left, null);
			tv1 = (RelativeLayout) view.findViewById(R.id.massage_left_tv1);
			tv2 = (RelativeLayout) view.findViewById(R.id.massage_left_tv2);

			tv1.setOnClickListener(this);
			tv2.setOnClickListener(this);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setContentView(view);
			showAtLocation(parent, Gravity.TOP, 0, rl_title.getHeight()
					+ ll_left.getHeight());
			setDataRank(n1);
			update();

		}

		// 读取筛选数据
		private void setDataRank(int position) {
			switch (position) {
			case 0:
				pseqdesc = "price";
				tv1.setBackgroundColor(Color.parseColor("#777777"));
				tv2.setBackgroundColor(Color.parseColor("#00000000"));

				break;
			case 1:
				tv2.setBackgroundColor(Color.parseColor("#777777"));
				tv1.setBackgroundColor(Color.parseColor("#00000000"));
				pseqdesc = "evascore";
				break;
			case 2:
				tv2.setBackgroundColor(Color.parseColor("#00000000"));
				tv1.setBackgroundColor(Color.parseColor("#00000000"));
				pseqdesc = "";
				break;
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.massage_left_tv1:
				if (list_flag_t) {
					setDataRank(0);
					list_flag_t = false;
					n1 = 0;
				} else {
					setDataRank(2);
					list_flag_t = true;
					n1 = 2;
				}

				list_flag_b = true;
				break;
			case R.id.massage_left_tv2:
				if (list_flag_b) {
					setDataRank(1);
					list_flag_b = false;
					n1 = 1;
				} else {
					setDataRank(2);
					list_flag_b = true;
					n1 = 2;
				}

				list_flag_t = true;
				break;
			}
			loadDataMassageList();
			huanYuan();
			dismiss();

		}

	}

	public class PopMassageRight extends PopupWindow implements OnClickListener {

		@SuppressWarnings("deprecation")
		public PopMassageRight(Context mContext, View parent) {

			View view = View.inflate(mContext,
					R.layout.massage_list_popuwindow_right, null);
			tv4 = (RelativeLayout) view.findViewById(R.id.massage_right_tv1);
			tv5 = (RelativeLayout) view.findViewById(R.id.massage_right_tv2);

			tv4.setOnClickListener(this);
			tv5.setOnClickListener(this);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setContentView(view);
			showAtLocation(parent, Gravity.TOP, 0, rl_title.getHeight()
					+ ll_left.getHeight());
			setDataSex(n2);
			update();

		}

		private void setDataSex(int position) {
			switch (position) {
			case 0:
				tv4.setBackgroundColor(Color.parseColor("#777777"));
				tv5.setBackgroundColor(Color.parseColor("#00000000"));
				pfildesc = "60";
				break;
			case 1:
				tv5.setBackgroundColor(Color.parseColor("#777777"));
				tv4.setBackgroundColor(Color.parseColor("#00000000"));
				pfildesc = "120";
				break;
			case 2:
				tv5.setBackgroundColor(Color.parseColor("#00000000"));
				tv4.setBackgroundColor(Color.parseColor("#00000000"));
				pfildesc = "";
				break;

			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.massage_right_tv1:
				if (list_r_flag_t) {
					setDataSex(0);
					list_r_flag_t = false;
					n2 = 0;
				} else {
					setDataSex(2);
					list_r_flag_t = true;
					n2 = 2;
				}

				list_r_flag_b = true;
				break;
			case R.id.massage_right_tv2:
				if (list_r_flag_b) {
					setDataSex(1);
					list_r_flag_b = false;
					n2 = 1;
				} else {
					setDataSex(2);
					list_r_flag_b = true;
					n2 = 2;
				}

				list_r_flag_t = true;
				break;
			}
			loadDataMassageList();
			huanYuan();
			dismiss();

		}

	}
}
