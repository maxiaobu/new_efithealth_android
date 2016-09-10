package com.efithealth.app.adapter;

import java.util.List;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.javabean.MassageList;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MassageListAdapter extends BaseAdapter{

	private Context context;
	private List<MassageList> list;
	
	
	public MassageListAdapter(Context context, List<MassageList> list) {
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
	public View getView(int position, View v, ViewGroup arg2) {
		ViewHolder holder=null;
		if (v==null) {
			holder=new ViewHolder();
			v=LayoutInflater.from(context).inflate(R.layout.item_massage_fragment, null);
			holder.iv_head=(ImageView) v.findViewById(R.id.item_massage_iv);
			holder.tv_name=(TextView) v.findViewById(R.id.item_massage_tv_name);
			holder.tv_message=(TextView) v.findViewById(R.id.item_massage_tv_message);
			holder.tv_time=(TextView) v.findViewById(R.id.item_massage_tv_time);
			holder.tv_money=(TextView) v.findViewById(R.id.item_massage_tv_money);
			holder.tv_sort=(TextView) v.findViewById(R.id.item_massage_tv_sort);
			v.setTag(holder);
		}else{
			holder=(ViewHolder) v.getTag();
		}
		
		MassageList massageList=list.get(position);
		ImageLoader.getInstance().displayImage(massageList.getImgsfilename(), holder.iv_head,
				MyApplication.getInstance().initPicDisImgOption());
		holder.tv_name.setText(massageList.getMname());
		holder.tv_message.setText(massageList.getSubtitle());
		holder.tv_time.setText(massageList.getTimeconsuming()+"分钟");
		holder.tv_money.setText(massageList.getPrice()+"元");
		holder.tv_sort.setText(massageList.getEvascore());
		
		return v;
	}
	
	class ViewHolder{
		ImageView iv_head;
		TextView tv_name,tv_message,tv_time;
		TextView tv_money,tv_sort;
	}

}
