package com.efithealth.app.adapter;

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
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.fragment.FragmentMassageDetails;
import com.efithealth.app.fragment.FragmentMassageOrder;
import com.efithealth.app.javabean.MassageOrderList;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MassageOrderListAdapter extends BaseAdapter {

	private Context context;
	private List<MassageOrderList> list;
	private String str = "";

	public MassageOrderListAdapter(Context context, List<MassageOrderList> list) {
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
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(
					R.layout.item_massage_order_list, null);
			holder.iv_head = (ImageView) v
					.findViewById(R.id.order_massage_title);
			holder.ll = (LinearLayout) v
					.findViewById(R.id.order_massage_item_ll);
			holder.tv_flag = (TextView) v.findViewById(R.id.order_massage_fh);
			holder.tv_massageName = (TextView) v
					.findViewById(R.id.order_massage_name_tv);
			holder.tv_money = (TextView) v
					.findViewById(R.id.order_massage_money_tv);
			holder.tv_jx = (TextView) v
					.findViewById(R.id.item_order_massage_tv_jx);
			holder.tv_del = (TextView) v
					.findViewById(R.id.item_order_massage_tv_del);
			holder.tv_commit = (TextView) v
					.findViewById(R.id.item_order_massage_tv_commit);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final MassageOrderList orderList = list.get(position);
		ImageLoader.getInstance().displayImage(orderList.getImgsfilename(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		holder.tv_massageName.setText(orderList.getMassagename());
		holder.tv_money.setText(orderList.getOrdamt() + "元");
		/*
		 * ordstatus=1 “已完成”“再来一单” ordstatus=0 &&paystatus=0 “待支付” “继续支付”“删除”
		 * ordstatus=0 &&paystatus=1 &&evastatus=0 “待评价”“评价
		 */

		if (orderList.getOrdstatus().equals("1")) {
			// “已完成”“再来一单”
			holder.ll.setVisibility(View.GONE);
			holder.tv_commit.setVisibility(View.VISIBLE);
			holder.tv_commit.setEnabled(true);
			holder.tv_jx.setEnabled(false);
			holder.tv_del.setEnabled(false);

			holder.tv_flag.setText("已完成");
			holder.tv_commit.setText("再来一单");
		} else if (orderList.getOrdstatus().equals("0")
				&& orderList.getPaystatus().equals("0")) {
			// “待支付” “继续支付”“删除”
			holder.ll.setVisibility(View.VISIBLE);
			holder.tv_commit.setVisibility(View.GONE);
			holder.tv_commit.setEnabled(false);
			holder.tv_jx.setEnabled(true);
			holder.tv_del.setEnabled(true);

			holder.tv_flag.setText("待支付");
			holder.tv_jx.setText("继续支付");
			holder.tv_del.setText("删除");
		} else if (orderList.getOrdstatus().equals("0")
				&& orderList.getPaystatus().equals("1")
				&& orderList.getEvastatus().equals("0")) {
			// “待评价”“评价
			holder.ll.setVisibility(View.GONE);
			holder.tv_commit.setVisibility(View.VISIBLE);
			holder.tv_commit.setEnabled(true);
			holder.tv_jx.setEnabled(false);
			holder.tv_del.setEnabled(false);

			holder.tv_flag.setText("待评价");
			holder.tv_commit.setText("评价");
		}

		str = holder.tv_commit.getText().toString();
		holder.tv_jx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FragmentMassageDetails.money = orderList.getOrdamt();
				FragmentMassageOrder.orderId = orderList.getOrderid();
				MainActivity.instance.setTabSelection(505);
			}
		});
		holder.tv_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendData(orderList, position);
			}
		});
		holder.tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (orderList.getOrdstatus().equals("1")) {
				// // “再来一单”
				// Log.i("djy", "再来一单");
				// FragmentMassageDetails.map.clear();
				// FragmentMassageDetails.map.put("massageid",
				// orderList.getMassageid());
				// FragmentMassageDetails.map.put("massagename",
				// orderList.getMname());
				// FragmentMassageDetails.map.put("clubid",
				// orderList.getClubid());
				// FragmentMassageDetails.map.put("ordamt",
				// orderList.getOrdamt());
				// FragmentMassageDetails.money=orderList.getOrdamt();
				// MainActivity.instance.setTabSelection(503);
				// } else if (orderList.getOrdstatus().equals("0")
				// && orderList.getPaystatus().equals("1")
				// && orderList.getEvastatus().equals("0")) {
				// // “评价
				// Log.i("djy", "评价");
				// SharedPreferencesUtils.setParam(context, "massageid",
				// orderList.getMassageid());
				// SharedPreferencesUtils.setParam(context, "orderid",
				// orderList.getOrderid());
				// MainActivity.instance.setTabSelection(506);
				// }
				if (str.equals("再来一单")) {
					FragmentMassageDetails.map.clear();
					FragmentMassageDetails.map.put("massageid",
							orderList.getMassageid());
					FragmentMassageDetails.map.put("massagename",
							orderList.getMname());
					FragmentMassageDetails.map.put("clubid",
							orderList.getClubid());
					FragmentMassageDetails.map.put("ordamt",
							orderList.getOrdamt());
					FragmentMassageDetails.money = orderList.getOrdamt();
					MainActivity.instance.setTabSelection(503);
				} else {
					SharedPreferencesUtils.setParam(context, "massageid",
							orderList.getMassageid());
					SharedPreferencesUtils.setParam(context, "orderid",
							orderList.getOrderid());
					MainActivity.instance.setTabSelection(506);
				}

			}
		});

		return v;
	}

	class ViewHolder {
		LinearLayout ll;
		TextView tv_flag, tv_massageName, tv_money, tv_jx, tv_del, tv_commit;
		ImageView iv_head;
	}

	private void sendData(final MassageOrderList orderList, final int position) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(context)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MASSAGE_ORDER_DEL, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject object = new JSONObject(response);
							String msgflag = object.getString("msgFlag");
							if (msgflag.equals("1")) {
								list.remove(position);
								notifyDataSetChanged();
								Toast.makeText(context, "删除成功", 0).show();

							} else {
								Toast.makeText(context, "删除失败！请重试", 0).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context, "数据错误！请重试", 0).show();
							e.printStackTrace();
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", orderList.getOrderid());
				return map;
			}
		};
		queue.add(request);
	}

}
