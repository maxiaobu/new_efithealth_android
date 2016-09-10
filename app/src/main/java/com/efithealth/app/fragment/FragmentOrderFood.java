package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.adapter.OrderClassAdapter;
import com.efithealth.app.adapter.OrderFoodAdapter;
import com.efithealth.app.javabean.ClassOrderListModel;
import com.efithealth.app.javabean.FoodOrderListModel;
import com.efithealth.app.javabean.ForderList;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentOrderFood extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {

	private View v;
	private List<ForderList> list_model = new ArrayList<ForderList>();
	private List<ForderList> list_adapter = new ArrayList<ForderList>();
	private WaterDropListView lv_food;
	private OrderFoodAdapter adapter;
	private int pageIndex = 1;
	private boolean flag_root = true;
	private SwipeRefreshLayout swipeLayout;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_order, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		ll_error = (LinearLayout) v.findViewById(R.id.ll_error);
		lv_food = (WaterDropListView) v.findViewById(R.id.order_lv);
		lv_food.setWaterDropListViewListener(this);
		lv_food.setPullLoadEnable(true, true);
		lv_food.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		getData(1);
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
						getData(2);
					}
				});
		super.onActivityCreated(savedInstanceState);
	}

	private void getData(final int type) {
		RequestQueue mRequestQueue = VolleySingleton.getVolleySingleton(
				getActivity()).getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_CLASS_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Gson gson = new Gson();
						FoodOrderListModel listModel = gson.fromJson(response,
								FoodOrderListModel.class);
						String msg = listModel.getMsgFlag();
						String errer = listModel.getMsgContent();
						list_model = listModel.getForderList();
						if (list_model != null) {
							if (list_model.size() > 0) {
								ll_error.setVisibility(View.GONE);

								if (list_model.size() < 10) {
									flag_root = false;
									lv_food.setPullLoadEnable(false, true);
								} else {
									flag_root = true;
									lv_food.setPullLoadEnable(true, true);
								}
								if (msg.equals("1")) {

									switch (type) {
									case 1:
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 2) {
											lv_food.setPullLoadEnable(false,
													false);
										}
										adapter = new OrderFoodAdapter(
												getActivity(), list_adapter);
										lv_food.setAdapter(adapter);
										break;

									case 2:
										list_adapter.clear();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));

										}
										if (list_adapter.size() < 2) {
											lv_food.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										swipeLayout.setRefreshing(false);
										Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT)
												.show();
										break;

									case 3:
										lv_food.stopLoadMore();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 2) {
											lv_food.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										Toast.makeText(getActivity(), "数据加载成功",
												Toast.LENGTH_SHORT).show();
										break;
									}
								} else {
									Toast.makeText(getActivity(), errer, Toast.LENGTH_SHORT)
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
				map.put("pageIndex", pageIndex + "");
				map.put("listtype", "forderlist");
				return map;
			}
		};
		mRequestQueue.add(request);
	}

	@Override
	public void onLoadMore() {
		if (flag_root) {
			pageIndex += 1;
			getData(3);

		}

	}

}
