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
import com.efithealth.app.adapter.MassageOrderListAdapter;
import com.efithealth.app.javabean.MassageOrderList;
import com.efithealth.app.javabean.MassageOrderModel;
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
import android.widget.Toast;

public class FragmentMassageOrderList extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {
	private View v;
	private SwipeRefreshLayout swipeLayout;
	private WaterDropListView massage_lv;
	private MassageOrderListAdapter adapter;
	private int pageIndex = 1;
	private List<MassageOrderList> list_model = new ArrayList<>();
	private List<MassageOrderList> list_adapter = new ArrayList<>();
	private boolean flag_root = true;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_massage_order_list, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		ll_error = (LinearLayout) v.findViewById(R.id.ll_error);
		massage_lv = (WaterDropListView) v.findViewById(R.id.order_massage_lv);
		massage_lv.setWaterDropListViewListener(this);
		massage_lv.setPullLoadEnable(true, true);
		massage_lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
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
		getData(1);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onLoadMore() {
		if (flag_root) {
			pageIndex += 1;
			getData(3);
		}

	}

	private void getData(final int type) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MASSAGE_ORDER_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Gson gson = new Gson();
						Log.i("FragmentMassageOrderList", response);
						MassageOrderModel massageOrderModel = gson.fromJson(
								response, MassageOrderModel.class);
						String msg = massageOrderModel.getMsgFlag();
						String errer = massageOrderModel.getMsgContent();
						list_model = massageOrderModel.getMassageorderList();
						if (list_model != null) {
							if (list_model.size() > 0) {
								ll_error.setVisibility(View.GONE);

								if (list_model.size() < 10) {
									flag_root = false;
									massage_lv.setPullLoadEnable(false, true);
								} else {
									flag_root = true;
									massage_lv.setPullLoadEnable(true, true);
								}

								if (msg.equals("1")) {
									showDataForListView(type);
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

				return map;
			}
		};
		queue.add(request);
	}

	private void showDataForListView(int key) {
		switch (key) {
		case 1:
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 2) {
				massage_lv.setPullLoadEnable(false, false);
			}
			adapter = new MassageOrderListAdapter(getActivity(), list_adapter);
			massage_lv.setAdapter(adapter);
			break;

		case 2:
			list_adapter.clear();
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 2) {
				massage_lv.setPullLoadEnable(false, false);
			}
			adapter.notifyDataSetChanged();
			swipeLayout.setRefreshing(false);
			Toast.makeText(getActivity(), "刷新成功", 0).show();
			break;

		case 3:
			massage_lv.stopLoadMore();
			for (int i = 0; i < list_model.size(); i++) {
				list_adapter.add(list_model.get(i));
			}
			if (list_adapter.size() < 2) {
				massage_lv.setPullLoadEnable(false, false);
			}
			adapter.notifyDataSetChanged();
			Toast.makeText(getActivity(), "数据加载成功", 0).show();
		}
	}

}
