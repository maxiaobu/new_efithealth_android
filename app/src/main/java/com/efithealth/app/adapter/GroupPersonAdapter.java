package com.efithealth.app.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.javabean.GroupPerson;
import com.efithealth.app.javabean.Memberlist;
import com.efithealth.app.task.VolleySingleton;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupPersonAdapter extends BaseAdapter {

	private Context context;
	private List<Memberlist> list;
	private String groupid;
	private String flag;

	public GroupPersonAdapter(Context context, List<Memberlist> list,
			String groupid,String flag) {
		super();
		this.context = context;
		this.list = list;
		this.groupid = groupid;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(
					R.layout.item_group_person_adapter, null);
			holder.iv_head = (ImageView) v
					.findViewById(R.id.item_person_g_head);
			holder.tv_name = (TextView) v.findViewById(R.id.item_person_g_name);
			holder.tv_details = (TextView) v
					.findViewById(R.id.item_person_g_details);
			holder.tv_yes = (TextView) v.findViewById(R.id.item_person_g_yes);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Memberlist memberlist = list.get(position);
		if (flag.equals("1")) {
			holder.tv_yes.setEnabled(false);
			holder.tv_yes.setVisibility(View.GONE);
		}else if (flag.equals("2")) {
			holder.tv_yes.setEnabled(true);
			holder.tv_yes.setVisibility(View.VISIBLE);
			holder.tv_yes.setText("通过");
		}else{
			holder.tv_yes.setEnabled(true);
			holder.tv_yes.setVisibility(View.VISIBLE);
			holder.tv_yes.setText("移除");
		}
		ImageLoader.getInstance().displayImage(Constant.URL_RESOURCE+memberlist.getImgsfile(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		holder.tv_name.setText(memberlist.getNickname());
		holder.tv_details.setText(memberlist.getSignature());

		holder.tv_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (flag.equals("2")) {
					sendData(1,position,Constant.URL_GROUP_YES);
				}else if (flag.equals("0")) {
					sendData(2,position,Constant.URL_GROUP_DEL);
				}
			}
		});

		return v;
	}

	private void sendData(int type,final int position,String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(context)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				url, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("djy", "GroupDynamicActivity" + response);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							String msg = jsonObject.getString("msgFlag");
							
							if (msg.equals("1")) {
								if (flag.equals("2")) {
									Toast.makeText(context, "对方已通过申请", 0).show();
								}else{
									Toast.makeText(context, "已删除此人", 0).show();
								}
								list.remove(position);
								notifyDataSetChanged();
							} else {
								Toast.makeText(context, "失败，请重试", 0).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("groupid", groupid);
				map.put("tarid", list.get(position).getMemid());
				if (flag.equals("2")) {
					map.put("memid",MyApplication.getInstance().getMemid() );
				}
				return map;
			}
		};
		queue.add(request);
	}

	class ViewHolder {
		ImageView iv_head;
		TextView tv_name, tv_details, tv_yes;
	}

}
