package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.javabean.ClubList;
import com.efithealth.app.javabean.CreateTime;
import com.efithealth.app.javabean.MassageDetails;
import com.efithealth.app.javabean.MassageDetailsModel;
import com.efithealth.app.task.VolleySingleton;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMassageDetails extends BaseFragment {

	private View v;
	private ImageView iv_back;
	private Button btn_commit;
	private String massageid = "";
	private ImageView iv_head;
	private TextView tv_name, tv_appraise, tv_details, tv_money, tv_time,
			tv_phone, tv_gn, tv_jj;
	private CheckBox cb_read;
	public static Map<String, String> map=new HashMap<String, String>();
	public static List<ClubList> list=new ArrayList<ClubList>();
	public static String money="";

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_massage_datails, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		iv_back = (ImageView) v.findViewById(R.id.back_massage_datails_1);
		btn_commit = (Button) v.findViewById(R.id.massage_btn_commit);
		iv_head = (ImageView) v.findViewById(R.id.massage_datails_tv);
		tv_name = (TextView) v.findViewById(R.id.massage_details_tv_name);
		tv_appraise = (TextView) v
				.findViewById(R.id.massage_details_tv_appraise);
		tv_details = (TextView) v.findViewById(R.id.massage_details_tv_details);
		tv_money = (TextView) v.findViewById(R.id.massage_detials_tv_money);
		tv_time = (TextView) v.findViewById(R.id.massage_detials_tv_time);
		tv_phone = (TextView) v.findViewById(R.id.massage_detials_tv_phone);
		tv_gn = (TextView) v.findViewById(R.id.massage_detials_tv_gn);
		tv_jj = (TextView) v.findViewById(R.id.massage_detials_tv_jj);
		cb_read=(CheckBox)v.findViewById(R.id.checkbox_massage);
		
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (list.size()>0) {
					if (cb_read.isChecked()) {
						if (list.size()>0) {
							MainActivity.instance.setTabSelection(503);
						}else{
							Toast.makeText(getActivity(), "请先绑定俱乐部，才可购买", 0).show();
						}
					}else{
						Toast.makeText(getActivity(), "请确认(我已阅读和了解[禁忌说明])", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "您还没有加入俱乐部，不能订购此服务", 0).show();
				}

			}
		});
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();

			}
		});

		massageid = (String) SharedPreferencesUtils.getParam(getActivity(),
				"massageid", "");
		getDetailsData();

		super.onActivityCreated(savedInstanceState);
	}

	private void getDetailsData() {
		RequestQueue queue = VolleySingleton.getVolleySingleton(getActivity())
				.getRequestQueue();
		StringRequest request = new StringRequest(Method.POST,
				Constant.URL_MASSAGE_INFO, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("FragmentMassageDetails", response);
						setData(response);

					}
				}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("memid", MyApplication.getInstance().getMemid());
				map.put("massageid", massageid);
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
		CreateTime createTime = detailsModel.getCreatetime();
		list=massageDetails.getClublist();
		ImageLoader.getInstance().displayImage(detailsModel.getImgpfilename(),
				iv_head, MyApplication.getInstance().initHomeDisImgOption1());
		tv_name.setText(detailsModel.getMname());
		tv_appraise.setText(detailsModel.getStatus());
		tv_details.setText(detailsModel.getSubtitle());
		tv_money.setText(detailsModel.getPrice()+"元");
		tv_time.setText(detailsModel.getTimeconsuming()+"分钟");
		tv_phone.setText(detailsModel.getConnphone());
		tv_gn.setText(detailsModel.getIntro());
		tv_jj.setText(detailsModel.getDisclaimer());
		map.clear();
		if (list.size()>0) {
			map.put("massageid", detailsModel.getMassageid());
			map.put("massagename", detailsModel.getMname());
			map.put("clubid", list.get(0).getClubid());
			map.put("ordamt", detailsModel.getPrice());
			map.put("address", list.get(0).getAddress());
			map.put("clubname", list.get(0).getClubname());
		}
		money=detailsModel.getPrice();
	}

	@Override
	public void onDestroy() {
		SharedPreferencesUtils.deleteSharedData(getActivity(), "massageid");
		map.clear();
		list.clear();
		super.onDestroy();
	}

}
