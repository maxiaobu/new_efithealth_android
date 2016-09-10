package com.efithealth.app.adapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;

import com.baidu.location.LLSInterface;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.fragment.FragmentMassageOrder;
import com.efithealth.app.fragment.FragmentMassagePersonList;
import com.efithealth.app.javabean.Masseurlist;
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

public class MassagePersonAdapter extends BaseAdapter {

	private Context context;
	private List<Masseurlist> list;

	public MassagePersonAdapter(Context context, List<Masseurlist> list) {
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

	ViewHolder holder = null;
	
//	int lasePosition = -1;

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		// ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(
					R.layout.item_massage_person, null);
			holder.iv_head = (ImageView) v
					.findViewById(R.id.item_massage_person_iv_head);
			holder.iv_sex = (ImageView) v
					.findViewById(R.id.item_massage_person_iv_sex);
			holder.iv_tag = (ImageView) v
					.findViewById(R.id.item_massage_person_iv_tag);
			holder.tv_name = (TextView) v
					.findViewById(R.id.item_massage_person_name);
			holder.tv_details = (TextView) v
					.findViewById(R.id.item_massage_person_tv_details);
			holder.ll_root = (LinearLayout) v
					.findViewById(R.id.Item_massage_person_root);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final Masseurlist masseur = list.get(position);
		ImageLoader.getInstance().displayImage(Constant.URL_RESOURCE+masseur.getImgsfile(),
				holder.iv_head,
				MyApplication.getInstance().initHeadDisImgOption());
		if (masseur.getGender().equals("1")) {
			holder.iv_sex.setImageResource(R.drawable.ee_24);
		} else {
			holder.iv_sex.setImageResource(R.drawable.ee_25);
		}

		if (masseur.getFlag_mpa()) {
			holder.iv_tag
					.setImageResource(R.drawable.selectimg);
		} else {
			holder.iv_tag.setImageResource(-1);
		}
		holder.tv_name.setText(masseur.getMname());
		holder.tv_details.setText(masseur.getSummary());
		holder.ll_root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i("djy", "adapter");
				if (FragmentMassagePersonList.position_list > -1 && FragmentMassagePersonList.position_list != position) {
					list.get(FragmentMassagePersonList.position_list).setFlag_mpa(false);
					FragmentMassagePersonList.map.remove(FragmentMassagePersonList.position_list);
				}
				if (!masseur.getFlag_mpa()) {
					masseur.setFlag_mpa(true);
					holder.iv_tag
					.setImageResource(R.drawable.selectimg);
					FragmentMassagePersonList.map.put(position, list.get(position));

				} else {
					masseur.setFlag_mpa(false);
					FragmentMassagePersonList.map.remove(position);
					holder.iv_tag.setImageResource(-1);
				}
				FragmentMassagePersonList.position_list = position;
				notifyDataSetChanged();
			}
		});
		return v;
	}

	class ViewHolder {
		ImageView iv_head, iv_sex, iv_tag;
		TextView tv_name, tv_details;
		LinearLayout ll_root;
	}

}
