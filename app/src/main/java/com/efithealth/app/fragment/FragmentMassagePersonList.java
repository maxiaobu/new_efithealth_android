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
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.adapter.MassagePersonAdapter;
import com.efithealth.app.javabean.MassagePersonModel;
import com.efithealth.app.javabean.Masseurlist;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.view.refreshlistview.WaterDropListView;
import com.efithealth.app.view.refreshlistview.WaterDropListView.IWaterDropListViewListener;
import com.google.gson.Gson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMassagePersonList extends BaseFragment implements
		IWaterDropListViewListener {

	private View v;
	private ImageView iv_back;
	private WaterDropListView lv;
	private List<Masseurlist> list_model = new ArrayList<Masseurlist>();
	private List<Masseurlist> list_adapter = new ArrayList<Masseurlist>();
	private boolean flag_root = true;
	private MassagePersonAdapter adapter;
	private int pageIndex = 1;
	private TextView tv;
	public static Map<Integer, Masseurlist> map = new HashMap<>();
	public static int position_list = -1;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_massage_person_list, container,
				false);
		FragmentMassageOrder.list.clear();
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		lv = (WaterDropListView) v.findViewById(R.id.massage_person_lv);
		iv_back = (ImageView) v.findViewById(R.id.back_massage_person);
		tv = (TextView) v.findViewById(R.id.massage_person_commit);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (position_list != -1) {
					FragmentMassageOrder.list.add(map.get(position_list));
					FragmentMassageOrder.handler.sendEmptyMessage(0);
				}
				MainActivity.instance.returnBack();
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				adapter.notifyDataSetChanged();

			}
		});
		getPersonList(1);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		map.clear();
		position_list = -1;
		super.onDestroy();
	}

	private void getPersonList(final int type) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MASSAGE_PERSON, new Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("person", response);
						ShowData(type, response);
					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pageIndex", pageIndex + "");
				return map;
			}
		};
		queue.add(request);

	}

	@Override
	public void onLoadMore() {
		pageIndex += 1;
		getPersonList(3);

	}

	private void ShowData(int index, String jsonPath) {
		Gson gson = new Gson();
		MassagePersonModel model = gson.fromJson(jsonPath,
				MassagePersonModel.class);
		String msg = model.getMsgFlag();
		String errer = model.getMsgContent();
		list_model = model.getMasseurlist();
		if (list_model != null) {

			if (list_model.size() < 10) {
				flag_root = false;
				lv.setPullLoadEnable(false, true);
			} else {
				flag_root = true;
				lv.setPullLoadEnable(true, true);
			}

			if (msg.equals("1")) {
				switch (index) {
				case 1:
					for (int i = 0; i < list_model.size(); i++) {
						list_adapter.add(list_model.get(i));
					}
					if (list_adapter.size() < 6) {
						lv.setPullLoadEnable(false, false);
					}
					adapter = new MassagePersonAdapter(getActivity(),
							list_adapter);
					lv.setAdapter(adapter);
					break;

				case 2:
					list_adapter.clear();
					for (int i = 0; i < list_model.size(); i++) {
						list_adapter.add(list_model.get(i));

					}
					if (list_adapter.size() < 6) {
						lv.setPullLoadEnable(false, false);
					}
					adapter.notifyDataSetChanged();
					// swipeLayout.setRefreshing(false);
					Toast.makeText(getActivity(), "刷新成功", 0).show();
					break;
				case 3:
					lv.stopLoadMore();
					for (int i = 0; i < list_model.size(); i++) {
						list_adapter.add(list_model.get(i));
					}
					if (list_adapter.size() < 6) {
						lv.setPullLoadEnable(false, false);
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(getActivity(), "数据加载成功", 0).show();
					break;
				}
			} else {
				Toast.makeText(getActivity(), errer, 0).show();
			}
		}
	}

}
