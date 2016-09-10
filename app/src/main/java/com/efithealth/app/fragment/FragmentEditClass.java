package com.efithealth.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;

/**
 * Created by Administrator on 2016/3/14 0014.
 * 
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了 目前不用了
 * 目前不用了 目前不用了
 */
public class FragmentEditClass extends Fragment {

	private ImageView iv_issue_course_title_left;
	private ImageView iv_course_picture;
	private EditText et_course_name;
	private EditText et_course_number;
	private EditText et_course_time;
	private EditText et_club;
	private EditText et_price;
	private EditText et_notice;
	private Button btn_issue_commit;
	private RelativeLayout rl_club;
	private TextView tv_title_centre;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_issue_class, null);

		iv_course_picture = (ImageView) view.findViewById(R.id.iv_course_picture);
		et_course_name = (EditText) view.findViewById(R.id.et_course_name);
		et_course_number = (EditText) view.findViewById(R.id.et_course_number);
		et_course_time = (EditText) view.findViewById(R.id.et_course_time);
		et_club = (EditText) view.findViewById(R.id.et_club);
		et_club.setVisibility(View.GONE);
		et_price = (EditText) view.findViewById(R.id.et_price);
		et_notice = (EditText) view.findViewById(R.id.et_notice);
		rl_club = (RelativeLayout) view.findViewById(R.id.rl_club);
		rl_club.setVisibility(View.GONE);

		tv_title_centre = (TextView) view.findViewById(R.id.tv_title_centre);
		tv_title_centre.setText("編輯课程");

		btn_issue_commit = (Button) view.findViewById(R.id.btn_issue_commit);
		btn_issue_commit.setOnClickListener(new View.OnClickListener() {

			// @Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if (!checkInput())
					return;

				Map<String, String> map = new HashMap<String, String>();
				map.put("coachid", (String) SharedPreferencesUtils.getParam(getActivity(), "memid", "")); // 会员id
				map.put("linestatus", "1");// 状态 0 历史 1 上线
				LoadDataFromServer task = new LoadDataFromServer(getActivity(), Constant.URL_MCOURSELIST, map);
				task.getData(new DataCallBack() {

					@Override
					public void onDataCallBack(JSONObject data) {
						// TODO 自动生成的方法存根

						Log.d("===", data.toString());

					}
				});
			}
		});

		iv_issue_course_title_left = (ImageView) view.findViewById(R.id.iv_issue_course_title_left);
		iv_issue_course_title_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.instance.setTabSelection(100);
			}
		});

		request();// 请求网络数据

		return view;
	}

	private void request() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("coachid", (String) SharedPreferencesUtils.getParam(getActivity(), "memid", "")); // 会员id
		map.put("linestatus", "1");// 状态 0 历史 1 上线
		LoadDataFromServer task = new LoadDataFromServer(getActivity(), Constant.URL_MCOURSELIST, map);
		task.getData(new DataCallBack() {

			@Override
			public void onDataCallBack(JSONObject data) {
				// TODO 自动生成的方法存根

				Log.d("===", data.toString());

				// ImageLoader.getInstance().displayImage(uri,
				// iv_course_picture,
				// MyApplication.getInstance().initPicDisImgOption());
				et_course_name.setText("aaaaa");
				et_course_number.setText("bbbbb");
				et_course_time.setText("ccccc");
				et_club.setText("ddddd");
				et_price.setText("1900");
				et_notice.setText("fffff");

			}
		});

	}

	private Boolean checkInput() {

		if (StringUtil.isEmpty(et_course_name.getText().toString())) {
			Toast.makeText(getActivity(), "课程名称不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtil.isEmpty(et_course_number.getText().toString())) {
			Toast.makeText(getActivity(), "课程次数不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtil.isEmpty(et_course_time.getText().toString())) {
			Toast.makeText(getActivity(), "课程时间不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtil.isEmpty(et_price.getText().toString())) {
			Toast.makeText(getActivity(), "价格不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
