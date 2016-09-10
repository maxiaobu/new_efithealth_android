package com.efithealth.app.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.FindActivity;
import com.efithealth.app.javabean.CreateTime;
import com.efithealth.app.javabean.HotDynamic;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HotDynamicAdapter extends BaseAdapter {
	private Context context;
	private List<HotDynamic> list;
	// HotDynamic dynamic;
	// private int index = 0;
	List<String> listImg = new ArrayList<String>();

	public HotDynamicAdapter(Context context, List<HotDynamic> list) {
		super();
		this.context = context;
		this.list = list;
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
		// index = position;
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(
					R.layout.item_hot_dynamic_adapter, null);
			holder.iv_head = (ImageView) v
					.findViewById(R.id.item_hot_dynamic_avatar);
			holder.iv_zan = (ImageView) v
					.findViewById(R.id.item_hot_dynamic_iv);
			holder.ll_zan = (LinearLayout) v
					.findViewById(R.id.item_hot_dynamic_zan);
			holder.ll_comment = (LinearLayout) v
					.findViewById(R.id.item_hot_dynamic_comment);
			holder.tv_name = (TextView) v
					.findViewById(R.id.item_hot_dynamic_name);
			holder.tv_time = (TextView) v
					.findViewById(R.id.item_hot_dynamic_time);
			holder.tv_zan = (TextView) v
					.findViewById(R.id.item_hot_dynamic_tv_zan);
			holder.tv_comment = (TextView) v
					.findViewById(R.id.item_hot_dynamic_comment_tv);
			holder.gv = (GridView) v.findViewById(R.id.item_hot_dynamic_gv);
			holder.tv_message = (TextView) v
					.findViewById(R.id.item_hot_dynamic_message);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		final HotDynamic dynamic = list.get(position);
		CreateTime createtime = dynamic.getCreatetime();
		ImageLoader.getInstance().displayImage(dynamic.getImgsfilename(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		if (dynamic.getIsPoint().equals("1")) {
			// 已点赞
			holder.iv_zan.setImageResource(R.drawable.zan_red);
		} else {
			holder.iv_zan.setImageResource(R.drawable.zan);
		}
		holder.tv_name.setText(dynamic.getNickname());
		holder.tv_zan.setText(dynamic.getPointnum());
		holder.tv_comment.setText(dynamic.getReviewnum());
		holder.tv_message.setText(dynamic.getContent());
		// 拼接日期时间字符串
		String time = "";
		if (Integer.parseInt(getDate().split("-")[0]) > Integer
				.parseInt(dynamic.getCreatetimes().substring(2, 4))) { // 去年
			time = dynamic.getCreatetimes().substring(0, 9);
		} else if (Integer.parseInt(getDate().split("-")[1]) > Integer
				.parseInt(createtime.getMonth())) { // 今年今月之前
			time += ((Integer.parseInt(createtime.getMonth()) + 1) < 10 ? "0"
					+ (Integer.parseInt(createtime.getMonth()) + 1) : (Integer
					.parseInt(createtime.getMonth()) + 1))
					+ " - "
					+ (createtime.getDate().length() < 2 ? "0"
							+ createtime.getDate() : createtime.getDate());
		} else {// 今年今月
			int n = Integer.parseInt(getDate().split("-")[2])
					- Integer.parseInt(createtime.getDate());
			if (n == 0) {
				time += "今天";
			} else if (n == 1) {
				time += "昨天";
			} else if (n == 2) {
				time += "前天";
			} else {
				time += ((Integer.parseInt(createtime.getMonth()) + 1) < 10 ? "0"
						+ (Integer.parseInt(createtime.getMonth()) + 1)
						: (Integer.parseInt(createtime.getMonth()) + 1))
						+ " - "
						+ (createtime.getDate().length() < 2 ? "0"
								+ createtime.getDate() : createtime.getDate());
			}
		}
		time += "  "
				+ (createtime.getHours().length() < 2 ? "0"
						+ createtime.getHours() : createtime.getHours())
				+ ":"
				+ (createtime.getMinutes().length() < 2 ? "0"
						+ createtime.getMinutes() : createtime.getMinutes());
		holder.tv_time.setText(time);
		holder.gv.setAdapter(new Myadapter(dynamic));
		holder.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				listImg.clear();
				for (int i = 0; i < dynamic.getImgList().size(); i++) {
					listImg.add(dynamic.getImgList().get(i).getImgpfilename());
				}
				Intent intent = new Intent(context, ImagePagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("urls", (Serializable) listImg);
				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});

		holder.ll_zan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendData(dynamic.getDynid(), dynamic.getIsPoint(), dynamic);
			}
		});

		holder.ll_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// file:///android_asset/dynReview.html?memid=M000003&dynid=D000225&from=hot
				SharedPreferencesUtils.setParam(context, "dynId",
						dynamic.getDynid());
				String page = "file:///android_asset/dynReview.html?memid="
						+ MyApplication.getInstance().getMemid() + "&dynid="
						+ dynamic.getDynid() + "&from=hot";
				Intent intent = new Intent(context, FindActivity.class);
				intent.putExtra("url", page);
				context.startActivity(intent);

			}
		});
		return v;
	}

	private String getDate() {
		long times = System.currentTimeMillis();
		Date date = new Date(times);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		return dateFormat.format(date);
	}

	protected void sendData(final String dynid, final String ispoint,
			final HotDynamic dynamic) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(context)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_HOT_ZAN, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("djy", response);
						try {
							org.json.JSONObject object = new org.json.JSONObject(
									response);
							String msg = object.getString("msgFlag");
							if (msg.equals("1")) {
								String str = dynamic.getPointnum();
								int num = Integer.parseInt(str);
								if (ispoint.equals("1")) {
									Toast.makeText(context, "取消点赞成功", 0).show();
									dynamic.setIsPoint("0");
									num -= 1;
								} else {
									Toast.makeText(context, "点赞成功", 0).show();
									dynamic.setIsPoint("1");
									num += 1;
								}
								dynamic.setPointnum(num + "");
								notifyDataSetChanged();
							} else {
								Toast.makeText(context, "失败，请稍后重试", 0).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("pageIndex", "1");
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("dynid", dynid);
				return map;
			}
		};
		queue.add(request);
	}

	class ViewHolder {
		ImageView iv_head, iv_zan;
		LinearLayout ll_zan, ll_comment;
		TextView tv_name, tv_time, tv_zan, tv_comment, tv_message;
		GridView gv;
	}

	class Myadapter extends BaseAdapter {
		private HotDynamic dynamic;

		public Myadapter(HotDynamic dynamic) {
			this.dynamic = dynamic;
		}

		@Override
		public int getCount() {
			return dynamic.getImgList().size();
		}

		@Override
		public Object getItem(int arg0) {
			return dynamic.getImgList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			ViewHolder holder = null;

			if (v == null) {
				holder = new ViewHolder();
				v = LayoutInflater.from(context).inflate(
						R.layout.item_hot_dynamic_img, null);
				holder.iv = (ImageView) v.findViewById(R.id.item_gv_iv);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			ImageLoader.getInstance().displayImage(
					dynamic.getImgList().get(position).getImgsfilename(),
					holder.iv,
					MyApplication.getInstance().initPicDisImgOption());

			return v;
		}

		class ViewHolder {
			ImageView iv;
		}
	}

}
