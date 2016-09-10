package com.efithealth.app.fragment;

import java.util.HashMap;
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
import com.efithealth.app.javabean.CreateTime;
import com.efithealth.app.javabean.MassageDetails;
import com.efithealth.app.javabean.MassageDetailsModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetDataCallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentEvaluate extends BaseFragment implements OnClickListener {

	private View v;
	private LinearLayout ll;
	private ImageView iv_head, iv_back;
	private ImageView iv1, iv2, iv3, iv4, iv5;
	private TextView tv_name, tv_xq, tv_time, tv_money, tv_sort, tv_commit;
	private String massageid = "", orderid = "";
	private int score = 5;
	private boolean flag = false;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_evaluate, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ll = (LinearLayout) v.findViewById(R.id.ll_evaluate);
		iv_back = (ImageView) v.findViewById(R.id.back_evaluate_1);

		iv_head = (ImageView) v.findViewById(R.id.item_evaluate_iv);
		tv_name = (TextView) v.findViewById(R.id.item_evaluate_tv_name);
		tv_xq = (TextView) v.findViewById(R.id.item_evaluate_tv_message);
		tv_time = (TextView) v.findViewById(R.id.item_evaluate_tv_time);
		tv_money = (TextView) v.findViewById(R.id.item_evaluate_tv_money);
		tv_sort = (TextView) v.findViewById(R.id.item_evaluate_tv_sort);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();
			}
		});
		tv_commit = (TextView) v.findViewById(R.id.tv_evaluate_commit);
		tv_commit.setOnClickListener(this);
		iv1 = (ImageView) v.findViewById(R.id.evaluate_iv1);
		iv1.setOnClickListener(this);
		iv2 = (ImageView) v.findViewById(R.id.evaluate_iv2);
		iv2.setOnClickListener(this);
		iv3 = (ImageView) v.findViewById(R.id.evaluate_iv3);
		iv3.setOnClickListener(this);
		iv4 = (ImageView) v.findViewById(R.id.evaluate_iv4);
		iv4.setOnClickListener(this);
		iv5 = (ImageView) v.findViewById(R.id.evaluate_iv5);
		iv5.setOnClickListener(this);

		massageid = (String) SharedPreferencesUtils.getParam(getActivity(),
				"massageid", "");
		orderid = (String) SharedPreferencesUtils.getParam(getActivity(),
				"orderid", "");
		getDetailsData(Constant.URL_MASSAGE_INFO);
		super.onActivityCreated(savedInstanceState);
	}

	// mevaluate.do?orderid=$orderid&massageid=$massageid&score=$score
	private void getDetailsData(String url) {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentMassageDetails", response);
						if (flag) {
							JSONObject object;
							try {
								object = new JSONObject(response);
								String msgflag = object.getString("msgFlag");
								if (msgflag.equals("1")) {
									flag=false;
									Toast.makeText(getActivity(), "评价成功", 0).show();
									MainActivity.instance.setTabSelection(3);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else{
							setData(response);
						}

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("massageid", massageid);
				if (flag) {
					map.put("orderid", orderid);
					map.put("score", score + "");
				} else {
					map.put("memid", MyApplication.getInstance().getMemid());
				}
				return map;
			}
		};
		queue.add(request);

	}

	private void setData(String jsonStr) {
		Gson gson = new Gson();
		MassageDetails massageDetails = gson.fromJson(jsonStr,
				MassageDetails.class);
		MassageDetailsModel detailsModel = massageDetails.getMassage();
		String msgFlag = massageDetails.getMsgFlag();
		if (msgFlag.equals("1")) {
			ImageLoader.getInstance().displayImage(
					detailsModel.getImgpfilename(), iv_head,
					MyApplication.getInstance().initPicDisImgOption());
			tv_name.setText(detailsModel.getMname());
			tv_sort.setText(detailsModel.getStatus());
			tv_xq.setText(detailsModel.getSubtitle());
			tv_money.setText(detailsModel.getPrice() + "元");
			tv_time.setText(detailsModel.getTimeconsuming() + "分钟");
		} else {
			Toast.makeText(getActivity(), "数据错误，请重试", 0).show();
		}
	}

	@Override
	public void onClick(View key) {
		switch (key.getId()) {
		case R.id.evaluate_iv1:
			score = 1;
			break;
		case R.id.evaluate_iv2:
			score = 2;
			break;
		case R.id.evaluate_iv3:
			score = 3;
			break;
		case R.id.evaluate_iv4:
			score = 4;
			break;
		case R.id.evaluate_iv5:
			score = 5;
			break;
		case R.id.tv_evaluate_commit:
			flag = true;
			getDetailsData(Constant.URL_EVALUATE);
			break;
		}
		setColorForstar();
	}

	private void setColorForstar() {
		for (int i = 0; i < ll.getChildCount(); i++) {
			if (i < score) {
				ll.getChildAt(i).setBackgroundResource(R.drawable.staronbig);
			} else {
				ll.getChildAt(i).setBackgroundResource(R.drawable.staroffbig);
			}
		}
	}

	@Override
	public void onDestroyView() {
		SharedPreferencesUtils.deleteSharedData(getActivity(), "massageid");
		SharedPreferencesUtils.deleteSharedData(getActivity(), "orderid");
		super.onDestroyView();
	}

}
