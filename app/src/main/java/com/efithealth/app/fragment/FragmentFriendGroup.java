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
import com.efithealth.app.activity.GroupSimpleDetailActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.PublicGroupsSeachActivity;
import com.efithealth.app.javabean.PublicGroup;
import com.efithealth.app.javabean.PublicGroupModel;
import com.efithealth.app.task.VolleySingleton;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentFriendGroup extends BaseFragment {
	private View v;
	private ImageView iv_back;
	private SwipeRefreshLayout swipeLayout;
	private List<PublicGroup> listModel = new ArrayList<>();
	private Button searchBtn;
	private ListView lv;
	private MyAdapter adapter;
	public static boolean flag = false;
	private LinearLayout ll_error;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FragmentFinds.flag = false;
		v = inflater.inflate(R.layout.fragment_friend_group, container, false);
		return v;
	}

	@Override
	public void onResume() {
		if (flag) {
			getData(2);
			flag = false;
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		FragmentFinds.flag = true;
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ll_error = (LinearLayout) v.findViewById(R.id.ll_error);
		iv_back = (ImageView) v.findViewById(R.id.back_friend_group);
		lv = (ListView) v.findViewById(R.id.friend_group_lv);
		searchBtn = (Button) v.findViewById(R.id.btn_search_group);
		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// startActivity(new Intent(getActivity(),
				// PublicGroupsSeachActivity.class));
			}
		});
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();

			}
		});
		// 设置刷新时动画的颜色，可以设置4个
		swipeLayout.setColorScheme(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		swipeLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						getData(2);
					}
				});
		swipeLayout.setRefreshing(false);
		getData(1);
		// 设置item点击事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						GroupSimpleDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("groupid", listModel.get(position)
						.getGroupid());
				bundle.putString("managerid", listModel.get(position)
						.getManagerid());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (lv.getCount() != 0) {
						int lasPos = view.getLastVisiblePosition();
						if (lasPos == lv.getCount() - 1) {

						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	private void getData(final int type) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_GROUP_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentFriendGroup", response);

						Gson gson = new Gson();
						PublicGroupModel groupModel = gson.fromJson(response,
								PublicGroupModel.class);
						String msg = groupModel.getMsgFlag();
						if (msg.equals("1")) {
							listModel = groupModel.getGrouplist();
							if (listModel != null) {
								if (listModel.size() > 0) {
									ll_error.setVisibility(View.GONE);

									if (type == 1) {
										adapter = new MyAdapter();
										lv.setAdapter(adapter);
									} else {
										adapter.notifyDataSetChanged();
									}
								} else {
									ll_error.setVisibility(View.VISIBLE);
								}
							} else {
								ll_error.setVisibility(View.VISIBLE);
							}
							swipeLayout.setRefreshing(false);
						}

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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listModel.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listModel.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int positiion, View v, ViewGroup arg2) {
			ViewHolder holder = null;
			if (v == null) {
				holder = new ViewHolder();
				v = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_public_group_adapter, null);
				holder.head = (ImageView) v
						.findViewById(R.id.item_public_group_head);
				holder.name = (TextView) v
						.findViewById(R.id.item_public_group_name);
				holder.sort = (TextView) v
						.findViewById(R.id.item_public_group_sort);
				holder.distance = (TextView) v
						.findViewById(R.id.item_public_group_distance);
				holder.details = (TextView) v
						.findViewById(R.id.item_public_group_details);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			PublicGroup group = listModel.get(positiion);
			ImageLoader.getInstance().displayImage(
					Constant.URL_RESOURCE + group.getImgsfile(), holder.head,
					MyApplication.getInstance().initHeadDisImgOption());
			holder.name.setText(group.getGname());
			if (!group.getGsortname().equals("")) {
				holder.sort.setVisibility(View.VISIBLE);
				holder.sort.setText(group.getGsortname());
			} else {
				holder.sort.setVisibility(View.GONE);
			}
			holder.details.setText(group.getSummary());
			holder.distance.setText(group.getDistancestr());
			return v;
		}

		class ViewHolder {
			ImageView head;
			TextView name, sort, distance, details;
		}

	}

}
