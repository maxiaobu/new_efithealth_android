package com.efithealth.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.javabean.CorderList;
import com.efithealth.app.javabean.ForderList;
import com.efithealth.app.javabean.MbFordermerList;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OrderFoodAdapter extends BaseAdapter {

	private Context context;
	private List<ForderList> list;
	private List<MbFordermerList> mbList=new ArrayList<>();
	private MyAdapter adapter;

	public OrderFoodAdapter(Context context, List<ForderList> list) {
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
		Viewholder holder = null;
		if (v == null) {
			holder = new Viewholder();
			v = LayoutInflater.from(context).inflate(
					R.layout.fragment_corder_food_item, null);
			holder.iv_head = (ImageView) v.findViewById(R.id.corder_food_title);
			holder.tv_name = (TextView) v.findViewById(R.id.corder_food_name);
			holder.tv_fh = (TextView) v.findViewById(R.id.corder_food_fh);
			holder.tv_money = (TextView) v.findViewById(R.id.corder_food_money);
			holder.tv_jx = (TextView) v
					.findViewById(R.id.item_order_food_tv_jx);
			holder.tv_del = (TextView) v
					.findViewById(R.id.item_order_food_tv_del);
			holder.tv_commit = (TextView) v
					.findViewById(R.id.item_order_food_tv_commit);
			holder.lv = (ListView) v.findViewById(R.id.corder_food_xq_lv);
			holder.ll=(LinearLayout) v.findViewById(R.id.order_food_item_ll);
			v.setTag(holder);
		} else {
			holder = (Viewholder) v.getTag();
		}
		final ForderList forderList = list.get(position);
		mbList = forderList.getMbfordermerlist();
		adapter = new MyAdapter();
		holder.lv.setAdapter(adapter);
		holder.iv_head.setImageResource(R.drawable.foodmerplat);
		holder.tv_name.setText(forderList.getClubname());
		holder.tv_money.setText("共计:" + forderList.getOrdamt() + "元");
		if (forderList.getPaystatus().equals("0")) {
			holder.ll.setVisibility(View.VISIBLE);
			holder.tv_commit.setVisibility(View.GONE);
			holder.tv_commit.setEnabled(false);
			holder.tv_del.setEnabled(true);
			holder.tv_jx.setEnabled(true);
			holder.tv_fh.setText("待付款");
			holder.tv_jx.setText("继续支付");
		} else if (forderList.getOrdstatus().equals("1")) {
			holder.ll.setVisibility(View.VISIBLE);
			holder.tv_commit.setVisibility(View.GONE);
			holder.tv_commit.setEnabled(false);
			holder.tv_del.setEnabled(true);
			holder.tv_jx.setEnabled(true);
			holder.tv_fh.setText("已完成");
			holder.tv_jx.setText("再来一单");
		} else if (forderList.getSendstatus().equals("0")) {
			holder.tv_commit.setVisibility(View.VISIBLE);
			holder.ll.setVisibility(View.GONE);
			holder.tv_commit.setEnabled(true);
			holder.tv_del.setEnabled(false);
			holder.tv_jx.setEnabled(false);
			holder.tv_fh.setText("待发货");
			holder.tv_commit.setText("再来一单");
		}
		holder.tv_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getData(Constant.URL_CLASS_DEL, 2, forderList, position);

			}
		});
		holder.tv_jx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (forderList.getPaystatus().equals("0")) {
					//继续支付
					getData(Constant.URL_FOOD_PAY_AGAIN, 0, forderList, position);
				} else if (forderList.getOrdstatus().equals("1")) {
					//再来一单
					getData(Constant.URL_FOOD_PAY_AGAIN, 1, forderList, position);
				} 

			}
		});
		holder.tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (forderList.getSendstatus().equals("0")) {
					getData(Constant.URL_FOOD_PAY_AGAIN, 1, forderList, position);
				}

			}
		});

		

		return v;
	}
	
	private void getData(String url,final int type,
			final ForderList forderList, final int position){
		RequestQueue queue = VolleySingleton.getVolleySingleton(context)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.i("djy", "food    "+response);
				JSONObject object;
				try {
					object = new JSONObject(response);
					String msgflag = object.getString("msgFlag");
					String page="";
					switch (type) {
					case 0:
						if (msgflag.equals("1")) {
							page = "pay.html?ordno=" + forderList.getOrdno() + "&ordamt=" + forderList.getOrdamt() + "&memid=" + MyApplication.getInstance().getMemid();
							SharedPreferencesUtils.setParam(context, "paypage", page);
							MainActivity.instance.setTabSelection(505);
						}
						break;

					case 1:
						//file:///android_asset/
						if (msgflag.equals("1")) {
							String merInfo="";
							for (int i = 0; i < mbList.size(); i++) {
								merInfo += mbList.get(position).getMerid() + "|";
								merInfo += mbList.get(position).getMername() + "|";
								merInfo += mbList.get(position).getPkeyListStr() + "|";
								merInfo += mbList.get(position).getBuynum() + "|";
								merInfo += mbList.get(position).getOrdno() + ",";
							}
							page = "file:///android_asset/foodmerBuy.html?memid=" + MyApplication.getInstance().getMemid() + "&merInfo=" + merInfo + "&ordamt=" + forderList.getOrdamt();
							MainActivity.instance.setTabWebViewSelection(page);
						}
						break;
					case 2:
						if (msgflag.equals("1")) {
							list.remove(position);
							notifyDataSetChanged();
							Toast.makeText(context, "删除成功", 0).show();
						} else {
							Toast.makeText(context, "删除失败！请重试", 0)
							.show();
						}
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map=new HashMap<>();
				String str[]=forderList.getOrdno().split("-");
				switch (type) {
				case 0:
					map.put("ordno", str[1]+str[2]);
					break;

				case 1:
					map.put("ordno", str[1]+str[2]);
					break;
				case 2:
					map.put("ordno", str[1]+str[2]);
					map.put("listtype", "forderlist");
					break;
				}
				return map;
			}
		};
		
		queue.add(request);
	}

	class Viewholder {
		ImageView iv_head;
		TextView tv_name, tv_fh, tv_money, tv_jx, tv_del, tv_commit;
		ListView lv;
		LinearLayout ll;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mbList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mbList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			v = LayoutInflater.from(context).inflate(
					R.layout.item_simpleadapter_food, null);
			TextView tv1 = (TextView) v.findViewById(R.id.item_simple_tv1);
			TextView tv2 = (TextView) v.findViewById(R.id.item_simple_tv2);
			tv1.setText(mbList.get(position).getMername());
			tv2.setText("×" + mbList.get(position).getBuynum());
			return v;
		}

	}

}
