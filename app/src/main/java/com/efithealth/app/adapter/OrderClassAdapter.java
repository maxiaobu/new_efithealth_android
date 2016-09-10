package com.efithealth.app.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.efithealth.app.javabean.CorderList;
import com.efithealth.app.javabean.CreateTime;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.annotation.SuppressLint;
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

public class OrderClassAdapter extends BaseAdapter {
	private Context context;
	private List<CorderList> list;

	public OrderClassAdapter(Context context, List<CorderList> list) {
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
					R.layout.fragment_order_class_item, null);
			holder.iv_head = (ImageView) v
					.findViewById(R.id.item_order_class_iv);
			holder.tv_name = (TextView) v
					.findViewById(R.id.item_order_class_tv_name);
			holder.tv_clubNme = (TextView) v
					.findViewById(R.id.item_order_class_tv_clubname);
			holder.tv_subscribe = (TextView) v
					.findViewById(R.id.item_order_class_tv_subscribe);
			holder.tv_className = (TextView) v
					.findViewById(R.id.item_order_class_tv_classname);
			holder.tv_classTime = (TextView) v
					.findViewById(R.id.item_order_class_tv_classtime);
			holder.tv_classAddress = (TextView) v
					.findViewById(R.id.item_order_class_tv_classaddress);
			holder.tv_classNum = (TextView) v
					.findViewById(R.id.item_order_class_tv_classnum);
			holder.tv_classMoney = (TextView) v
					.findViewById(R.id.item_order_class_tv_classmoney);
			holder.tv_classCommit = (TextView) v
					.findViewById(R.id.item_order_class_tv_commit);
			holder.tv_jx = (TextView) v
					.findViewById(R.id.item_order_class_tv_jx);
			holder.tv_del = (TextView) v
					.findViewById(R.id.item_order_class_tv_del);
			holder.ll = (LinearLayout) v.findViewById(R.id.item_order_class_ll);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final CorderList corderList = list.get(position);

		ImageLoader.getInstance().displayImage(corderList.getCa_imgsfilename(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		holder.tv_name.setText(corderList.getCa_nickname());
		holder.tv_clubNme.setText(corderList.getClubname());
		if (corderList.getPaystatus().equals("0")) {
			holder.tv_classCommit.setVisibility(View.GONE);
			holder.tv_classCommit.setEnabled(false);
			holder.ll.setVisibility(View.VISIBLE);
			holder.tv_jx.setEnabled(true);
			holder.tv_del.setEnabled(true);
			holder.tv_subscribe.setText("待付款");
			holder.tv_jx.setText("继续支付");
			holder.tv_del.setText("删除");
		} else if (corderList.getOrdstatus().equals("1")) {
			holder.tv_classCommit.setVisibility(View.GONE);
			holder.tv_classCommit.setEnabled(false);
			holder.ll.setVisibility(View.VISIBLE);
			holder.tv_jx.setEnabled(true);
			holder.tv_del.setEnabled(true);
			holder.tv_subscribe.setText("已完成");
			holder.tv_jx.setText("再来一单");
			holder.tv_del.setText("删除");
		} else if (corderList.getBespeaknum() == corderList.getCoursenum()) {
			holder.tv_classCommit.setVisibility(View.VISIBLE);
			holder.tv_classCommit.setEnabled(true);
			holder.ll.setVisibility(View.GONE);
			holder.tv_jx.setEnabled(false);
			holder.tv_del.setEnabled(false);
			holder.tv_subscribe.setText("待预约");
			holder.tv_classCommit.setText("现在预约");
		} else if (Integer.parseInt(corderList.getBespeaknum()) > Integer
				.parseInt(corderList.getCoursenum())) {
			holder.tv_classCommit.setVisibility(View.VISIBLE);
			holder.tv_classCommit.setEnabled(true);
			holder.ll.setVisibility(View.GONE);
			holder.tv_jx.setEnabled(false);
			holder.tv_del.setEnabled(false);
			holder.tv_subscribe.setText("已预约");
			holder.tv_classCommit.setText("查看预约");
		}
		holder.tv_className.setText(corderList.getCoursename());
		if (corderList.getPaystatus().equals("0")) {
			holder.tv_classTime.setText("");
			holder.tv_classNum.setText(corderList.getCoursedays() + "天/"
					+ corderList.getOrdcoursetimes() + "次");
		} else if (corderList.getOrdstatus().equals("1")) {
			holder.tv_classTime.setText("截止到"
					+ formatdate(corderList.getOrdenddate().getTime()));
			holder.tv_classNum.setText(corderList.getCoursedays() + "天/"
					+ corderList.getOrdcoursetimes() + "次");
		} else {
			holder.tv_classTime.setText("截止到"
					+ formatdate(corderList.getOrdenddate().getTime()));
			holder.tv_classNum.setText("剩余" + corderList.getOrdcoursetimes()
					+ "次");
		}
		holder.tv_classAddress.setText(corderList.getAddress());
		if (corderList.getPaystatus().equals("2")) {
			holder.tv_classMoney.setText("凭会员卡免费预约");
		} else {
			holder.tv_classMoney.setText("共计：" + corderList.getOrdamt() + "元");
		}
		holder.tv_className.setText(corderList.getCoursename());
		holder.tv_classCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String page="";
				if (corderList.getBespeaknum() == corderList.getCoursenum()) {
					// 现在预约
					page = "file:///android_asset/reservation.html?coachid=" + corderList.getCoachid() + "&nickname=" + corderList.getCa_nickname() + "&clubname=" + corderList.getClubname() + "&address=" + corderList.getAddress();
					page += "&coursename=" +corderList.getCoursename() + "&enddate=" + formatdate(corderList.getOrdenddate().getTime()) + "&times=" + corderList.getOrdcoursetimes()+ "&orderid=" + corderList.getOrdno();
					page +="&imgsfile="+corderList.getCa_imgsfilename();
					MainActivity.instance.setTabWebViewSelection(page);
				} else if (Integer.parseInt(corderList.getBespeaknum()) > Integer
						.parseInt(corderList.getCoursenum())) {
					// 查看预约
				}

			}
		});

		holder.tv_jx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (corderList.getPaystatus().equals("0")) {
					// 继续支付
					getData(Constant.URL_CLASS_PAY, 0, corderList, position);
				} else if (corderList.getOrdstatus().equals("1")) {
					// 再来一单
					getData(Constant.URL_CLASS_PAY_AGAIN, 1, corderList,
							position);

				}

			}
		});

		holder.tv_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getData(Constant.URL_CLASS_DEL, 2, corderList, position);

			}
		});

		return v;
	}

	@SuppressLint("SimpleDateFormat")
	private String formatdate(String time) {
		long time1 = Long.parseLong(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time1);
		return format.format(date);
	}

	class ViewHolder {
		ImageView iv_head;
		TextView tv_name, tv_clubNme, tv_subscribe, tv_className, tv_classTime,
				tv_classAddress, tv_classNum, tv_classMoney, tv_classCommit,
				tv_jx, tv_del;
		LinearLayout ll;

	}

	private void getData(String url, final int type,
			final CorderList corderList, final int position) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(context)
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("djy", response);
						try {
							JSONObject object = new JSONObject(response);
							String msgflag = object.getString("msgFlag");
							String page="";
							switch (type) {
							case 0:
								if (msgflag.equals("1")) {
									page = "pay.html?ordno="
											+ corderList.getOrdno()
											+ "&ordamt="
											+ corderList.getOrdamt()
											+ "&memid=" + MyApplication.getInstance().getMemid();
									SharedPreferencesUtils.setParam(context, "paypage", page);
									MainActivity.instance.setTabSelection(505);
								} else {
									Toast.makeText(context, "失败！请重试", 0).show();
								}
								break;

							case 1:
								if (msgflag.equals("1")) {
									//TODO
									String bCorder=object.getString("bCorder");
									String bMember=object.getString("bMember");
									JSONObject object2=new JSONObject(bMember);
									String mobphone=object2.getString("mobphone");
									JSONObject object3=new JSONObject(bCorder);
									String courseid=object3.getString("courseid");
									if ( corderList.getOrdtype().equals("gclub") || corderList.getOrdtype().equals( "gcoach")) { //团操
										page = "file:///android_asset/gcourse.html?gcourseid=" + courseid;
										
									} else { //私教
										page = "file:///android_asset/pcourse.html?pcourseid=" + courseid + "&coachname=" +
												corderList.getCoachname() + "&mobphone=" + mobphone + 
												"&memid=" + MyApplication.getInstance().getMemid();
										
									}
									MainActivity.instance.setTabWebViewSelection(page);
								} else {
									Toast.makeText(context, "失败！请重试", 0).show();
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
							Toast.makeText(context, "数据解析失败！请重试", 0).show();
							e.printStackTrace();
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				switch (type) {
				case 0:
					map.put("ordno", corderList.getOrdno());
					break;

				case 1:
					map.put("ordno", corderList.getOrdno());
					break;
				case 2:
					String str[]=corderList.getOrdno().split("-");
					map.put("ordno", str[1]+"-"+str[2]);
					map.put("listtype", "corderlist");
					Log.i("orderclass",str[1]+"-"+str[2]);
					break;
				}
				return map;
			}
		};
		queue.add(request);
	}
}
