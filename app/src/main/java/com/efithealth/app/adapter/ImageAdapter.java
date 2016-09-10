package com.efithealth.app.adapter;

import java.util.List;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ImageWall;
import com.efithealth.app.activity.MyInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

@SuppressWarnings("rawtypes")
public class ImageAdapter extends ArrayAdapter {

	private Context m_Context;
	private List<String> m_imglist;
	private int con = 0;
	private ImageView add_img, del_img;
	private String str_img;
	private int resource;

	@SuppressWarnings("unchecked")
	public ImageAdapter(Context context, int resource, List<String> objects) {
		super(context, 0, objects);

		m_imglist = objects;
		m_Context = context;
		this.resource = resource;

	}

	@Override
	public int getCount() {

		return m_imglist.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		str_img = (String) m_imglist.get(position);

			convertView = LayoutInflater.from(m_Context).inflate(
					R.layout.personalinfo_list, null);
			add_img = (ImageView) convertView.findViewById(R.id.test);
			del_img = (ImageView) convertView.findViewById(R.id.del);

			if (!str_img.contains("http://")) {
				Bitmap bm = BitmapFactory.decodeFile(str_img);
				add_img.setImageBitmap(bm);
			} else {
				ImageLoader.getInstance().displayImage(str_img, add_img,
						MyApplication.getInstance().initPicDisImgOption());
			}

			del_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					((MyInfo) m_Context).delImg(position);
				}
			});
//			add_img.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					((MyInfo) m_Context).showImgView(m_imglist.get(position));
//				}
//			});

		return convertView;
	}

}
