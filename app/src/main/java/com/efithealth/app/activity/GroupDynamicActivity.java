package com.efithealth.app.activity;

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
import com.efithealth.app.adapter.GroupDynamicAdapter;
import com.efithealth.app.adapter.HotDynamicAdapter;
import com.efithealth.app.javabean.HotDynamic;
import com.efithealth.app.javabean.HotDynamicModel;
import com.efithealth.app.javabean.MBDynamicList;
import com.efithealth.app.javabean.MBDynamicModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupDynamicActivity extends BaseActivity implements
		WaterDropListView.IWaterDropListViewListener {

	private ImageView iv_back;
	private SwipeRefreshLayout swipeLayout;
	private WaterDropListView hotLv;
	private GroupDynamicAdapter adapter;
	private int pageIndex = 1;
	private List<MBDynamicList> listModel = new ArrayList<MBDynamicList>();
	private List<MBDynamicList> listAdapter = new ArrayList<MBDynamicList>();
	private boolean flag_root = true;
	private ProgressDialog dialog;
	// private String groupid="";
	private LinearLayout ll_error;

	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.fragment_hot_dynamic);
		ShowLoadData();
		initView();
	}

	// 加载框
	private void ShowLoadData() {
		dialog = new ProgressDialog(GroupDynamicActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在加载数据……");
		dialog.show();

	}

	private void initView() {
		ll_error = (LinearLayout) findViewById(R.id.ll_error);
		iv_back = (ImageView) findViewById(R.id.back_hot_dynamic_1);
		hotLv = (WaterDropListView) findViewById(R.id.hot_hotdynamic_lv);
		hotLv.setWaterDropListViewListener(this);
		hotLv.setPullLoadEnable(true, true);
		hotLv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
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
						getData(2, Constant.URL_GROUP_DYN);
					}
				});
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		hotLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// file:///android_asset/dynReview.html?memid=M000003&dynid=D000225&from=hot
				SharedPreferencesUtils.setParam(GroupDynamicActivity.this,
						"dynId", listAdapter.get(position).getDynid());
				String page = "file:///android_asset/dynReview.html?memid="
						+ MyApplication.getInstance().getMemid() + "&dynid="
						+ listAdapter.get(position).getDynid() + "&from=hot";
				Intent intent = new Intent(GroupDynamicActivity.this,
						FindActivity.class);
				intent.putExtra("url", page);
				startActivity(intent);

			}
		});
		getData(1, Constant.URL_GROUP_DYN);
	}

	private void getData(final int type, String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(
				GroupDynamicActivity.this).getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("GroupDynamicActivity", response);
						if (type != 4) {
							Gson gson = new Gson();
							MBDynamicModel dynamicModel = gson.fromJson(
									response, MBDynamicModel.class);
							String msg = dynamicModel.getMsgFlag();
							String errer = dynamicModel.getMsgContent();
							listModel = dynamicModel.getmBDynamicList();
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
											Toast.makeText(
													GroupDynamicActivity.this,
													"暂时没有热门动态", 0).show();
										}
									} else {
										Toast.makeText(
												GroupDynamicActivity.this,
												errer, 0).show();
									}
								}else{
									ll_error.setVisibility(View.VISIBLE);
								}
							}else{
								ll_error.setVisibility(View.VISIBLE);
							}
						} else {
							try {
								JSONObject jsonObject = new JSONObject(response);
								String msg1 = jsonObject.getString("msgFlag");
								if (msg1.equals("1")) {
									JSONObject object = jsonObject
											.getJSONObject("bDynamic");
									String isPoint = object
											.getString("isPoint");
									String pointnum = object
											.getString("pointnum");
									String reviewnum = object
											.getString("reviewnum");
									String dynid = (String) SharedPreferencesUtils
											.getParam(
													GroupDynamicActivity.this,
													"dynId", "");
									for (int i = 0; i < listAdapter.size(); i++) {
										MBDynamicList dynamic = listAdapter
												.get(i);
										if (dynid.equals(dynamic.getDynid())) {
											dynamic.setPointnum(pointnum);
											dynamic.setReviewnum(reviewnum);
											dynamic.setIsPoint(isPoint);
										}
									}
									adapter.notifyDataSetChanged();
									SharedPreferencesUtils.deleteSharedData(
											GroupDynamicActivity.this, "dynId");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						dialog.cancel();
					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("pageIndex", pageIndex + "");
				map.put("groupid", getIntent().getStringExtra("groupid"));
				if (type == 4) {
					String dynid = (String) SharedPreferencesUtils.getParam(
							GroupDynamicActivity.this, "dynId", "");
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
			adapter = new GroupDynamicAdapter(GroupDynamicActivity.this,
					listAdapter);
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
			Toast.makeText(GroupDynamicActivity.this, "刷新成功", 0).show();
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
			Toast.makeText(GroupDynamicActivity.this, "数据加载成功", 0).show();
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
			getData(3, Constant.URL_GROUP_DYN);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}
