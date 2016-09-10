package com.efithealth.app.adapter;

import java.util.List;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.javabean.CoachList;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoachListAdapter extends BaseAdapter {
	private Context context;
	private List<CoachList> list;
	private String sortType;

	public CoachListAdapter(Context context, List<CoachList> list,
			String sortType) {
		super();
		this.context = context;
		this.list = list;
		this.sortType = sortType;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_coach_list_item, null);
			holder.iv_head = (ImageView) convertView
					.findViewById(R.id.coach_item_iv);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.coach_item_name);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.coach_item_distance);
			holder.tv_signature = (TextView) convertView
					.findViewById(R.id.coach_item_signature);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CoachList coachList = list.get(position);
		holder.tv_name.setText(coachList.getNickname());
		holder.tv_signature.setText(coachList.getSignature());
		if (sortType == "evascore") {
			Log.d("djy", coachList.getMemid());
			float eva = Float.parseFloat(coachList.getEvascore());
			float f = Math.round(eva * 10) / 10;
			holder.tv_distance.setText(f <= 5 ? (f != 0.0 ? f + "☆" : "暂无评分")
					: "5.0 ☆");
		} else if (sortType == "coursetimes") {
			holder.tv_distance.setText(coachList.getCoursetimes());
		} else {
			holder.tv_distance.setText(coachList.getDistancestr());
		}
		ImageLoader.getInstance().displayImage(coachList.getImgsfilename(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		return convertView;
	}

	class ViewHolder {
		ImageView iv_head;
		TextView tv_name, tv_distance, tv_signature;
	}

}
