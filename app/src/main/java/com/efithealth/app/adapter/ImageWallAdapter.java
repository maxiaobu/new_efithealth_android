package com.efithealth.app.adapter;

import java.util.List;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.ImageWall;
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

public class ImageWallAdapter extends ArrayAdapter {
	
	
	
	
	
	private Context m_Context;
	private int m_resource;
	private List<String> m_imglist;
	private String str_img;
	private ImageView add_img,del_img;
	
	
	@SuppressWarnings("unchecked")
	public ImageWallAdapter(Context context, int resource, List list) {
		super(context, resource, list);
		this.m_Context = context;
		this.m_imglist = list;
		this.m_resource = resource;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		str_img = m_imglist.get(position);
		if(str_img.equals("add")){		
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.personalinfo_list1, null);
			
			add_img = (ImageView) convertView.findViewById(R.id.test1);				
			ImageLoader.getInstance().displayImage("drawable://" + R.drawable.myinfomr,add_img,
    				MyApplication.getInstance().initPicDisImgOption());
			add_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0){					
					((ImageWall) m_Context).GridItemClick(m_resource);
					
				}
			});		
		}else{
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.personalinfo_list, null);
			add_img = (ImageView) convertView.findViewById(R.id.test);	
			if (!str_img.contains("http://")) {
				Bitmap bm = BitmapFactory.decodeFile(str_img);
				add_img.setImageBitmap(bm);
			} else {
				ImageLoader.getInstance().displayImage(str_img, add_img,
						MyApplication.getInstance().initPicDisImgOption());
			}
//			add_img.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0){					
//					((ImageWall) m_Context).showImgView(m_imglist.get(position),m_resource);
//				}
//			});		
			del_img = (ImageView) convertView.findViewById(R.id.del);
			del_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {					
					((ImageWall)m_Context).delImg(position,m_resource);
				}
			});
		}
		
		return convertView;
	}
	

}
