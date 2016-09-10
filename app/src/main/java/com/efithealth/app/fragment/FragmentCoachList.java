package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.PublishActiviy;
import com.efithealth.app.adapter.CoachListAdapter;
import com.efithealth.app.javabean.CoachList;
import com.efithealth.app.javabean.CoachListModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentCoachList extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {
	private View view;
	private WaterDropListView coach_lv;
	private ImageView coach_iv;
	private CoachListAdapter adapter;
	private List<CoachList> list_adapter = new ArrayList<CoachList>();
	private List<CoachList> list_model = new ArrayList<>();
	private LinearLayout ll_left, ll_right;
	private ImageView iv_left, iv_right;
	private boolean flag_root = true, flag_click_left = true,
			flag_click_right = true;
	private ProgressDialog dialog;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6;

	// 请求页码
	private int pageIndex = 1, n1 = 0, n2 = 0, left_n = 0, right_n = 0;
	// 排序方式
	private String sortType = "distance";
	// 性别筛选条件
	private String gender = "all";

	private PopupWindows1 popupWindows_left;
	private PopupWindows2 popupWindows_right;
	private RelativeLayout rl;
	private SwipeRefreshLayout swipeLayout;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentHome.flag = false;
		view = inflater.inflate(R.layout.fragment_coach_list, container, false);
		ShowLoadData();

		return view;
	}

	@Override
	public void onDestroyView() {
		FragmentHome.flag = true;
		if (left_n != 0) {
			popupWindows_left.dismiss();
		}
		if (right_n != 0) {
			popupWindows_right.dismiss();
		}
		huanYuan();
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ll_error = (LinearLayout) view.findViewById(R.id.ll_error);
		coach_lv = (WaterDropListView) view.findViewById(R.id.coach_lv);
		coach_iv = (ImageView) view.findViewById(R.id.back_coach_1);
		rl = (RelativeLayout) view.findViewById(R.id.rl_coach_title);

		ll_left = (LinearLayout) view.findViewById(R.id.ll_left_cpach);
		ll_right = (LinearLayout) view.findViewById(R.id.ll_right_cpach);
		iv_left = (ImageView) view.findViewById(R.id.iv_left_cpach);
		iv_right = (ImageView) view.findViewById(R.id.iv_right_cpach);

		rl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (left_n != 0) {
						popupWindows_left.dismiss();
						huanYuan();
						return true;
					}
					if (right_n != 0) {
						popupWindows_right.dismiss();
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
						popupWindows_right.dismiss();
						right_n = 0;
					}
					popupWindows_left = new PopupWindows1(getActivity(), view
							.findViewById(R.id.coach_list_root));
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
					popupWindows_left.dismiss();
				}
			}
		});
		ll_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flag_click_left = true;
				if (flag_click_right) {
					if (left_n != 0) {
						popupWindows_left.dismiss();
						left_n = 0;
					}
					popupWindows_right = new PopupWindows2(getActivity(), view
							.findViewById(R.id.coach_list_root));
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
					popupWindows_right.dismiss();
				}

			}
		});

		coach_lv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (left_n != 0) {
						popupWindows_left.dismiss();
						huanYuan();
						return true;
					}
					if (right_n != 0) {
						popupWindows_right.dismiss();
						huanYuan();
						return true;
					}
				}
				return false;
			}
		});

		coach_lv.setWaterDropListViewListener(this);
		coach_lv.setPullLoadEnable(true, true);
		coach_lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		coach_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				MainActivity.instance.returnBack();
			}
		});
		getListData(1, pageIndex + "");
		coach_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String memid = list_adapter.get(position).getMemid();
				String imgsfile = list_adapter.get(position).getImgsfile();
				String nickname = list_adapter.get(position).getNickname();
				String page = "?tarid=" + memid + "&role=coach"
						+ "&tabpage=course&memid="
						+ MyApplication.getInstance().getMemid();
				page += "&memphoto=" + imgsfile + "&memnickname=" + nickname;
				if (page != null && page.length() > 0) {
					SharedPreferencesUtils
							.setParam(getActivity(), "page", page);
				}
				MainActivity.instance.setTabSelection(403);

			}
		});
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
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
						getListData(2, pageIndex + "");
					}
				});

		super.onActivityCreated(savedInstanceState);
	}

	private void getListData(final int index, final String pageIndex) {

		RequestQueue mRequestQueue = VolleySingleton.getVolleySingleton(
				getActivity()).getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_COACH_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("fragmentcoachlist", response);
						dialog.dismiss();
						list_model.clear();
						Gson gson = new Gson();

						CoachListModel coachListModel = gson.fromJson(response,
								CoachListModel.class);
						String msg = coachListModel.getMsgFlag();
						String errer = coachListModel.getMsgContent();
						list_model = coachListModel.getCoachList();
						if (list_model != null) {
							if (list_model.size() > 0) {
								ll_error.setVisibility(View.GONE);

								if (list_model.size() < 10) {
									flag_root = false;
									coach_lv.setPullLoadEnable(false, true);
								} else {
									flag_root = true;
									coach_lv.setPullLoadEnable(true, true);
								}

								if (msg.equals("1")) {
									switch (index) {
									// 第一次进入请求数据
									case 1:
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 6) {
											coach_lv.setPullLoadEnable(false,
													false);
										}
										adapter = new CoachListAdapter(
												getActivity(), list_adapter,
												sortType);
										coach_lv.setAdapter(adapter);
										break;

									// 下拉刷新
									case 2:
										list_adapter.clear();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));

										}
										if (list_adapter.size() < 6) {
											coach_lv.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										swipeLayout.setRefreshing(false);
										Toast.makeText(getActivity(), "刷新成功", 0)
												.show();
										break;

									// 加载更多
									case 3:
										coach_lv.stopLoadMore();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 6) {
											coach_lv.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										Toast.makeText(getActivity(), "数据加载成功",
												0).show();
										break;

									// 满足筛选条件
									case 4:
										list_adapter.clear();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 6) {
											coach_lv.setPullLoadEnable(false,
													false);
										}
										adapter = new CoachListAdapter(
												getActivity(), list_adapter,
												sortType);
										coach_lv.setAdapter(adapter);
										Toast.makeText(getActivity(), "刷新成功", 0)
												.show();
										break;
									}

								} else {
									Toast.makeText(getActivity(), errer, 0)
											.show();
								}
							} else {
								ll_error.setVisibility(View.VISIBLE);
							}
						}else {
							ll_error.setVisibility(View.VISIBLE);
						}
					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("pageIndex", pageIndex);
				map.put("latitude", MyApplication.getInstance().mLatitude + "");
				map.put("longitude", MyApplication.getInstance().mLongitude
						+ "");
				map.put("sorttytpe", sortType);
				map.put("gender", gender);
				return map;
			}
		};
		mRequestQueue.add(request);
	}

	// // 下拉刷新(勿删，可能以后会用)
	// @Override
	// public void onRefresh() {
	//
	// }

	// 加载更多
	@Override
	public void onLoadMore() {
		if (flag_root) {
			pageIndex += 1;
			getListData(3, pageIndex + "");
		}
	}

	private void loadDataCoachList() {
		pageIndex = 1;
		getListData(4, pageIndex + "");
		ShowLoadData();
		left_n = 0;
		right_n = 0;
	}

	// 加载框
	private void ShowLoadData() {
		dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在加载数据……");
		dialog.show();

	}

	@SuppressLint("ClickableViewAccessibility")
	public class PopupWindows1 extends PopupWindow implements OnClickListener {

		@SuppressWarnings("deprecation")
		public PopupWindows1(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.coach_list_popuwindow,
					null);
			tv1 = (TextView) view.findViewById(R.id.coach_rang_tv1);
			tv2 = (TextView) view.findViewById(R.id.coach_rang_tv2);
			tv3 = (TextView) view.findViewById(R.id.coach_rang_tv3);

			tv1.setOnClickListener(this);
			tv2.setOnClickListener(this);
			tv3.setOnClickListener(this);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setContentView(view);
			showAtLocation(parent, Gravity.TOP, 0,
					rl.getHeight() + ll_left.getHeight());
			setDataRank(n1);
			update();

		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.coach_rang_tv1:
				setDataRank(0);
				n1 = 0;
				break;
			case R.id.coach_rang_tv2:
				setDataRank(1);
				n1 = 1;
				break;
			case R.id.coach_rang_tv3:
				setDataRank(2);
				n1 = 2;
				break;
			}
			loadDataCoachList();
			huanYuan();
			dismiss();
		}

	}

	// 读取筛选数据
	private void setDataRank(int position) {
		switch (position) {
		case 0:
			tv1.setBackgroundColor(Color.parseColor("#777777"));
			tv2.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv3.setBackgroundColor(Color.parseColor("#F4F2F1"));
			sortType = "distance";

			break;
		case 1:
			tv2.setBackgroundColor(Color.parseColor("#777777"));
			tv1.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv3.setBackgroundColor(Color.parseColor("#F4F2F1"));
			sortType = "evascore";
			break;
		case 2:
			tv3.setBackgroundColor(Color.parseColor("#777777"));
			tv2.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv1.setBackgroundColor(Color.parseColor("#F4F2F1"));
			sortType = "coursetimes";
			break;

		}
	}

	@SuppressLint("ClickableViewAccessibility")
	public class PopupWindows2 extends PopupWindow implements OnClickListener {

		@SuppressWarnings("deprecation")
		public PopupWindows2(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.coach_list_popuwindow2,
					null);
			tv4 = (TextView) view.findViewById(R.id.coach_sex_tv1);
			tv5 = (TextView) view.findViewById(R.id.coach_sex_tv2);
			tv6 = (TextView) view.findViewById(R.id.coach_sex_tv3);

			tv4.setOnClickListener(this);
			tv5.setOnClickListener(this);
			tv6.setOnClickListener(this);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setContentView(view);
			showAtLocation(parent, Gravity.TOP, 0,
					rl.getHeight() + ll_left.getHeight());
			setDataSex(n2);
			update();

		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.coach_sex_tv1:
				setDataSex(0);
				n2 = 0;
				break;
			case R.id.coach_sex_tv2:
				setDataSex(1);
				n2 = 1;
				break;
			case R.id.coach_sex_tv3:
				setDataSex(2);
				n2 = 2;
				break;
			}
			loadDataCoachList();
			huanYuan();
			dismiss();
		}

	}

	private void setDataSex(int position) {
		switch (position) {
		case 0:
			tv4.setBackgroundColor(Color.parseColor("#777777"));
			tv5.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv6.setBackgroundColor(Color.parseColor("#F4F2F1"));
			gender = "all";
			break;
		case 1:
			tv5.setBackgroundColor(Color.parseColor("#777777"));
			tv4.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv6.setBackgroundColor(Color.parseColor("#F4F2F1"));
			gender = "1";
			break;
		case 2:
			tv6.setBackgroundColor(Color.parseColor("#777777"));
			tv5.setBackgroundColor(Color.parseColor("#F4F2F1"));
			tv4.setBackgroundColor(Color.parseColor("#F4F2F1"));
			gender = "0";
			break;

		}
	}

	private void huanYuan() {
		flag_click_right = true;
		flag_click_left = true;
		left_n = 0;
		right_n = 0;
		iv_left.setImageResource(R.drawable.bottom_all);
		iv_right.setImageResource(R.drawable.bottom_all);
	}

}
