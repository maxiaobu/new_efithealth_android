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
import com.efithealth.app.adapter.OrderClassAdapter;
import com.efithealth.app.javabean.CorderList;
import com.efithealth.app.javabean.ClassOrderListModel;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentClassOrder extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {
	private View v;
	private WaterDropListView order_lv;
	private OrderClassAdapter adapter;
	private List<CorderList> list_model = new ArrayList<CorderList>();
	private List<CorderList> list_adapter = new ArrayList<CorderList>();
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
		order_lv = (WaterDropListView) v.findViewById(R.id.order_lv);
		order_lv.setWaterDropListViewListener(this);
		order_lv.setPullLoadEnable(true, true);
		order_lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
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
						Log.i("orderclose", response);
						Gson gson = new Gson();
						ClassOrderListModel listModel = gson.fromJson(response,
								ClassOrderListModel.class);
						String msg = listModel.getMsgFlag();
						String errer = listModel.getMsgContent();
						list_model = listModel.getCorderList();
						if (list_model != null) {
							if (list_model.size() > 0) {
								ll_error.setVisibility(View.GONE);
								if (list_model.size() < 10) {
									flag_root = false;
									order_lv.setPullLoadEnable(false, true);
								} else {
									flag_root = true;
									order_lv.setPullLoadEnable(true, true);
								}
								if (msg.equals("1")) {

									switch (type) {
									case 1:
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 2) {
											order_lv.setPullLoadEnable(false,
													false);
										}
										adapter = new OrderClassAdapter(
												getActivity(), list_adapter);
										order_lv.setAdapter(adapter);
										break;

									case 2:
										list_adapter.clear();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 2) {
											order_lv.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										swipeLayout.setRefreshing(false);
										Toast.makeText(getActivity(), "刷新成功", 0)
												.show();
										break;

									case 3:
										order_lv.stopLoadMore();
										for (int i = 0; i < list_model.size(); i++) {
											list_adapter.add(list_model.get(i));
										}
										if (list_adapter.size() < 2) {
											order_lv.setPullLoadEnable(false,
													false);
										}
										adapter.notifyDataSetChanged();
										Toast.makeText(getActivity(), "数据加载成功",
												0).show();
										break;
									}
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
				map.put("listtype", "corderlist");
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
