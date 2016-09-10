/*package com.efithealth.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.efithealth.R;
import com.efithealth.app.activity.PersonalInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class PersonalInfoAdapter extends ArrayAdapter {
	List list;
	List list2 = new ArrayList();
	ImageView del, addId;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	ImageLoaderConfiguration config;
	String bit;
	public Context context1;

	public PersonalInfoAdapter(Context context, List objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		list = objects;
		context1 = context;
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.roominfo_add_btn_pressed)
				.showImageOnFail(R.drawable.roominfo_add_btn_pressed).resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(5)).build();
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		bit = (String) list.get(position);
		if (bit.equals("add")) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.personalinfo_list1, null);
			addId = (ImageView) convertView.findViewById(R.id.test1);
			ImageLoader.getInstance().displayImage(bit, addId, options);
			addId.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					// intent.addCategory(Intent.CATEGORY_OPENABLE);
					// intent.setType("image/*");
					// intent.putExtra("return-data", true);
					((PersonalInfo) context1).open(list);
				}
			});
		} else {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.personalinfo_list, null);
			addId = (ImageView) convertView.findViewById(R.id.test);
			del = (ImageView) convertView.findViewById(R.id.del);
			ImageLoader.getInstance().displayImage(bit, addId, options);
			del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					list.remove(position);
					if (list.size() < 4) {
						if (list.get(list.size() - 1).equals("aaa")) {

						} else {
							list2.add("aaa");
							list.addAll(list2);
						}
					}
					notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}
}
*/