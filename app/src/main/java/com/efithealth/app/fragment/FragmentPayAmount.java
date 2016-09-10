package com.efithealth.app.fragment;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.simcpux.Constants;

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
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPayAmount extends BaseFragment implements OnClickListener {
	private View v;
	private String orderId = "";
	private ImageView iv_back, iv_flag_yb, iv_flag_wx;
	private RelativeLayout rl_yb, rl_wx;
	private TextView tv_commit, pay_money1, pay_money2, pay_details;
	private long moneyContent = 0;
//	private float moneyNum = 0;
	private float ordamt = 0;
	private int payType=0,payIndex=0;
	private boolean type_wx=true,type_yb=false;
	private String type_flag="";
	private String urlType="";
	private String flag="";

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_pay_amount, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		String page=(String) SharedPreferencesUtils.getParam(getActivity(), "paypage", "");
		if (page.equals("")) {
			orderId = FragmentMassageOrder.orderId;
			ordamt = Float.parseFloat(FragmentMassageDetails.money);
		}else{
			String names[]=page.split("&");
			int beginIndex=names[0].indexOf("=")+1;
			flag=names[0].substring(beginIndex);
			String types[]=flag.split("-");
			type_flag=types[0];
			orderId=types[0]+types[1]+types[2];
			int beginIndex1=names[1].indexOf("=")+1;
			ordamt =Float.parseFloat(names[1].substring(beginIndex1));
		}
		iv_back = (ImageView) v.findViewById(R.id.back_pay_order);
		rl_yb = (RelativeLayout) v.findViewById(R.id.pay_rl_yb);
		rl_wx = (RelativeLayout) v.findViewById(R.id.pay_rl_weixin);
		tv_commit = (TextView) v.findViewById(R.id.pay_tv_commit);
		pay_money1 = (TextView) v.findViewById(R.id.pay_tv_money1);
		pay_money2 = (TextView) v.findViewById(R.id.pay_tv_money2);
		pay_details = (TextView) v.findViewById(R.id.pay_tv_details);
		iv_flag_yb = (ImageView) v.findViewById(R.id.pay_iv_flag_yb);
		iv_flag_wx = (ImageView) v.findViewById(R.id.pay_iv_flag_wx);

		iv_back.setOnClickListener(this);
		rl_yb.setOnClickListener(this);
		rl_wx.setOnClickListener(this);
		tv_commit.setOnClickListener(this);

		pay_money1.setText(ordamt + "元");
		pay_money2.setText("支付金额:" + ordamt + "元");
		getData();
		super.onActivityCreated(savedInstanceState);
	}

	

	private void getData() {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_PAY, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentPayAmount", response);
						try {
							JSONObject object = new JSONObject(response);
							if (object.getString("msgFlag").equals("1")) {
								long m1 = object.getLong("ycoinnum");
								long m2 = object.getLong("ycoincashnum");
								moneyContent = m1 + m2;
								if (ordamt * 100 < moneyContent) {
									payType=0;
									iv_flag_yb.setVisibility(View.VISIBLE);
									iv_flag_wx.setVisibility(View.INVISIBLE);
									pay_details.setText("本次可抵现" + ordamt
											+ "元,抵现后余额为"
											+ (moneyContent - ordamt * 100));
								} else {
									payType=1;
									rl_wx.setEnabled(false);
									iv_flag_yb.setVisibility(View.VISIBLE);
									iv_flag_wx.setVisibility(View.VISIBLE);
									pay_details.setText("本次可抵现" + (moneyContent/100f)
											+ "元,抵现后余额为0");
									ordamt = ordamt - moneyContent / 100f;
								}

							} else {
								Toast.makeText(getActivity(), "网络错误，请重试", 0)
										.show();
								MainActivity.instance.returnBack();
							}
						} catch (JSONException e) {
							Toast.makeText(getActivity(), "网络错误，请重试", 0).show();
							MainActivity.instance.returnBack();
							e.printStackTrace();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_pay_order:
			MainActivity.instance.returnBack();
			break;

		case R.id.pay_rl_yb:
			if (payType==0) {
				payIndex=0;
				iv_flag_yb.setVisibility(View.VISIBLE);
				iv_flag_wx.setVisibility(View.INVISIBLE);
			}else{
				if (type_yb) {
					iv_flag_yb.setVisibility(View.VISIBLE);
					type_yb=false;
					type_wx=false;
				}else{
					iv_flag_yb.setVisibility(View.INVISIBLE);
					type_yb=true;
					type_wx=true;
				}
			}
			break;
		case R.id.pay_rl_weixin:
			if (payType==0) {
				payIndex=1;
				iv_flag_yb.setVisibility(View.INVISIBLE);
				iv_flag_wx.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.pay_tv_commit:
			if (type_flag.equals("CO")) {
				urlType=Constant.URL_CLASS_MONEY;
			}else if (type_flag.equals("FO")) {
				urlType=Constant.URL_FOOD_MONEY;
			}else{
				urlType=Constant.URL_MASSAGE_MONEY;
			}
			if (payIndex==0) {
				sendData();
			}else{
				pay();
			}
			break;
		}

	}

	private void sendData() {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				urlType, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentPayAmount", response);
						try {
							JSONObject object = new JSONObject(response);
							if (object.getString("msgFlag").equals("1")) {
								Toast.makeText(getActivity(), "支付成功", 0).show();
								clearData();
								MainActivity.instance.onTabClicked(tv_commit);
								MainActivity.instance.setTabColor(3);
								
							} else {
								Toast.makeText(getActivity(), "支付失败，请重试", 0)
										.show();
							}
						} catch (JSONException e) {
							Toast.makeText(getActivity(), "数据解析错误，请重试", 0)
									.show();
							e.printStackTrace();
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				String str=orderId.substring(0, 2);
				if (str.equals("MO")) {
					map.put("orderid", orderId);
				}else{
					map.put("ordno", flag);
				}
				map.put("orderamt", ordamt * 100 + "");
				Log.i("djy", flag+"        "+ordamt+"---"+urlType);
				return map;
			}
		};
		queue.add(request);
	}

	// 微信支付
	public void pay() {
		// 第一步：与服务器连接获取信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("ordno", orderId);
		map.put("orderamt", (type_wx?ordamt:(ordamt - moneyContent / 100f))+"");
		LoadDataFromServer task_club_message = new LoadDataFromServer(
				getActivity(), Constant.URL_WEIXIN_PAY, map);
		task_club_message.getData(new DataCallBack() {

			@Override
			public void onDataCallBack(com.alibaba.fastjson.JSONObject json) {
				Log.d("===获取支付信息", json.toString());
				if (!"".equals(json.toString()) && json.toString().length() > 0) {
					IWXAPI api = WXAPIFactory.createWXAPI(getActivity(),
							"wxe774923041503e14");// wxb4ba3c02aa476ea1
					api.registerApp(Constants.APP_ID);
					PayReq req = new PayReq();
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					boolean isReq = api.sendReq(req);
					Log.e("aa", "" + isReq);
					if (isReq) {
						clearData();
						MainActivity.instance.onTabClicked(tv_commit);
						MainActivity.instance.setTabColor(3);
					}

				} else {
					Toast.makeText(getActivity(), "没有支付信息", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
	}
	
	
	private void clearData(){
		//所有静态变量全部还原
		FragmentMassageOrder.orderId = "";
		FragmentMassageOrder.list.clear();
		FragmentMassageOrder.masseurid="";
		FragmentMassageOrder.datomworks="";
		FragmentMassageOrder.todworks="";
		FragmentMassageOrder.tomworks="";
		FragmentMassageDetails.map.clear();
		FragmentMassageDetails.money="";
		FragmentMassagePersonList.map.clear();
		FragmentMassagePersonList.position_list=-1;
		SharedPreferencesUtils.deleteSharedData(getActivity(), "paypage");
		
	}
	
	@Override
	public void onDestroyView() {
		clearData();
		super.onDestroyView();
	}

}
