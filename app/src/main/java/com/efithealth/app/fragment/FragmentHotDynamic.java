package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.efithealth.app.activity.FindActivity;
import com.efithealth.app.activity.FragmentFind;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.adapter.HotDynamicAdapter;
import com.efithealth.app.javabean.HotDynamic;
import com.efithealth.app.javabean.HotDynamicModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentHotDynamic extends BaseFragment implements
		WaterDropListView.IWaterDropListViewListener {
	private View v;
	private ImageView iv_back;
	private SwipeRefreshLayout swipeLayout;
	private WaterDropListView hotLv;
	private HotDynamicAdapter adapter;
	private int pageIndex = 1;
	private List<HotDynamic> listModel = new ArrayList<HotDynamic>();
	private List<HotDynamic> listAdapter = new ArrayList<HotDynamic>();
	private boolean flag_root = true;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentFinds.flag = false;
		v = inflater.inflate(R.layout.fragment_hot_dynamic, container, false);
		return v;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		iv_back = (ImageView) v.findViewById(R.id.back_hot_dynamic_1);
		ll_error = (LinearLayout) v.findViewById(R.id.ll_error);
		hotLv = (WaterDropListView) v.findViewById(R.id.hot_hotdynamic_lv);
		hotLv.setWaterDropListViewListener(this);
		hotLv.setPullLoadEnable(true, true);
		hotLv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
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
						getData(2, Constant.URL_HOT_DYNAMIC);
					}
				});
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();

			}
		});
		hotLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// file:///android_asset/dynReview.html?memid=M000003&dynid=D000225&from=hot
				SharedPreferencesUtils.setParam(getActivity(), "dynId",
						listAdapter.get(position).getDynid());
				String page = "file:///android_asset/dynReview.html?memid="
						+ MyApplication.getInstance().getMemid() + "&dynid="
						+ listAdapter.get(position).getDynid() + "&from=hot";
				Intent intent = new Intent(getActivity(), FindActivity.class);
				intent.putExtra("url", page);
				startActivity(intent);

			}
		});
		getData(1, Constant.URL_HOT_DYNAMIC);
		super.onActivityCreated(savedInstanceState);
	}

	private void getData(final int type, String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentHotDynamic", response);
						if (type != 4) {
							Gson gson = new Gson();
							HotDynamicModel dynamicModel = gson.fromJson(
									response, HotDynamicModel.class);
							String msg = dynamicModel.getMsgFlag();
							String errer = dynamicModel.getMsgContent();
							listModel = dynamicModel.getHotDynamicsList();
							if (listModel != null) {
								if (listModel.size() > 0) {
									ll_error.setVisibility(View.GONE);

									if (msg.equals("1")) {
										if (listModel.size() < 10) {
											flag_root = false;
											hotLv.setPullLoadEnable(false, true);

										} else {
											flag_root = true;
											hotLv.setPullLoadEnable(true, true);
										}
										if (listModel.size() > 0) {
											ShowData(type);
										} else {
											Toast.makeText(getActivity(),
													"暂时没有热门动态", 0).show();
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
						} else {
							try {
								JSONObject jsonObject = new JSONObject(response);
								String msg = jsonObject.getString("msgFlag");
								if (msg.equals("1")) {
									JSONObject object = jsonObject
											.getJSONObject("bDynamic");
									String isPoint = object
											.getString("isPoint");
									String pointnum = object
											.getString("pointnum");
									String reviewnum = object
											.getString("reviewnum");
									String dynid = (String) SharedPreferencesUtils
											.getParam(getActivity(), "dynId",
													"");
									for (int i = 0; i < listAdapter.size(); i++) {
										HotDynamic dynamic = listAdapter.get(i);
										if (dynid.equals(dynamic.getDynid())) {
											dynamic.setPointnum(pointnum);
											dynamic.setReviewnum(reviewnum);
											dynamic.setIsPoint(isPoint);
										}
									}
									adapter.notifyDataSetChanged();
									SharedPreferencesUtils.deleteSharedData(
											getActivity(), "dynId");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("pageIndex", pageIndex + "");
				if (type == 4) {
					String dynid = (String) SharedPreferencesUtils.getParam(
							getActivity(), "dynId", "");
					map.put("dynid", dynid);
				}
				return map;
			}
		};
		queue.add(request);
	}

	protected void ShowData(int type) {
		switch (type) {
		case 1:
			for (int i = 0; i < listModel.size(); i++) {
				listAdapter.add(listModel.get(i));
			}
			if (listAdapter.size() < 4) {
				hotLv.setPullLoadEnable(false, false);
			}
			adapter = new HotDynamicAdapter(getActivity(), listAdapter);
			hotLv.setAdapter(adapter);
			break;

		case 2:
			listAdapter.clear();
			for (int i = 0; i < listModel.size(); i++) {
				listAdapter.add(listModel.get(i));
			}
			if (listAdapter.size() < 4) {
				hotLv.setPullLoadEnable(false, false);
			}
			adapter.notifyDataSetChanged();
			swipeLayout.setRefreshing(false);
			Toast.makeText(getActivity(), "刷新成功", 0).show();
			break;

		case 3:
			hotLv.stopLoadMore();
			for (int i = 0; i < listModel.size(); i++) {
				listAdapter.add(listModel.get(i));
			}
			if (listAdapter.size() < 4) {
				hotLv.setPullLoadEnable(false, false);

			}
			adapter.notifyDataSetChanged();
			Toast.makeText(getActivity(), "数据加载成功", 0).show();
			break;
		}

	}

	@Override
	public void onResume() {
		if (FragmentFind.flag_find) {
			getData(4, Constant.URL_HOT_DYNAMIN_RE);
			FragmentFind.flag_find = false;
		}
		super.onResume();
	}

	@Override
	public void onLoadMore() {
		if (flag_root) {
			pageIndex += 1;
			getData(3, Constant.URL_HOT_DYNAMIC);
		}
	}

	@Override
	public void onDestroy() {
		FragmentFinds.flag = true;
		super.onDestroy();
	}

}
