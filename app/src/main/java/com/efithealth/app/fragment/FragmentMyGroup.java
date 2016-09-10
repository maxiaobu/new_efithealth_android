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
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ChatActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.adapter.GroupAdapter;
import com.efithealth.app.fragment.FragmentFriendGroup.MyAdapter;
import com.efithealth.app.fragment.FragmentFriendGroup.MyAdapter.ViewHolder;
import com.efithealth.app.javabean.PublicGroup;
import com.efithealth.app.javabean.PublicGroupModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.applib.controller.HXSDKHelper;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentMyGroup extends BaseFragment {
	public static final String TAG = "FragmentMyGroup";
	private View v;
	private ListView groupLv;
	protected List<EMGroup> grouplist;
	// private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	private SyncListener syncListener;
	// private View progressBar;
	private List<PublicGroup> listModel = new ArrayList<>();
	private SwipeRefreshLayout swipeRefreshLayout;
	private MyAdapter adapter;
	private String imid = "";
	private LinearLayout ll_error;
	Handler handler = new Handler();

	class SyncListener implements HXSDKHelper.HXSyncListener {
		@Override
		public void onSyncSucess(final boolean success) {
			EMLog.d(TAG, "onSyncGroupsFinish success:" + success);
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					swipeRefreshLayout.setRefreshing(false);
					if (success) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								refresh();
								// progressBar.setVisibility(View.GONE);
							}
						}, 1000);
					} else {
						if (!getActivity().isFinishing()) {
							String s1 = getResources()
									.getString(
											R.string.Failed_to_get_group_chat_information);
							Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG)
									.show();
							// progressBar.setVisibility(View.GONE);
						}
					}
				}
			});
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_my_group, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		groupLv = (ListView) v.findViewById(R.id.my_group_list);
		ll_error = (LinearLayout) v.findViewById(R.id.ll_error);
		ImageView back = (ImageView) v.findViewById(R.id.back_my_group);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();
			}
		});
		swipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorScheme(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				MainActivity.asyncFetchGroupsFromServer();
			}
		});
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// grouplist = EMGroupManager.getInstance().getAllGroups();
		// groupAdapter = new GroupAdapter(getActivity(), 1, grouplist);
		// groupLv.setAdapter(groupAdapter);
		groupLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 进入群聊
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				// it is group chat
				intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				intent.putExtra("groupId", listModel.get(position).getImid());
				intent.putExtra("managerid", listModel.get(position)
						.getManagerid());
				intent.putExtra("nameg", listModel.get(position).getGname());
				intent.putExtra("groupid", listModel.get(position).getGroupid());
				startActivityForResult(intent, 0);
			}

		});
		groupLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getActivity().getCurrentFocus()
										.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		// progressBar = (View)v.findViewById(R.id.progress_bar);
		//
		syncListener = new SyncListener();
		HXSDKHelper.getInstance().addSyncGroupListener(syncListener);
		//
		// if (!HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
		// progressBar.setVisibility(View.VISIBLE);
		// } else {
		// progressBar.setVisibility(View.GONE);
		// }
		getData(1);
		refresh();
		super.onActivityCreated(savedInstanceState);
	}

	private void getData(final int type) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MY_GROUP_LIST, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentMyGroup", response);
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
										groupLv.setAdapter(adapter);
									} else {
										adapter.notifyDataSetChanged();
									}
								} else {
									ll_error.setVisibility(View.VISIBLE);
								}
							} else {
								ll_error.setVisibility(View.VISIBLE);
							}
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

	@Override
	public void onResume() {
		super.onResume();
		grouplist = EMGroupManager.getInstance().getAllGroups();
		// groupAdapter = new GroupAdapter(getActivity(), 1, grouplist);
		// groupLv.setAdapter(groupAdapter);
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		if (syncListener != null) {
			HXSDKHelper.getInstance().removeSyncGroupListener(syncListener);
			syncListener = null;
		}
		super.onDestroy();
	}

	public void refresh() {
		if (groupLv != null && adapter != null) {
			grouplist = EMGroupManager.getInstance().getAllGroups();
			// groupAdapter = new GroupAdapter(getActivity(), 1,
			// grouplist);
			// groupLv.setAdapter(groupAdapter);
			adapter.notifyDataSetChanged();
		}
	}

}
