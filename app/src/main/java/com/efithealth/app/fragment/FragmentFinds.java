package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.efithealth.R;
import com.efithealth.app.activity.PublishActiviy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentFinds extends BaseFragment{
	
	private View v;
	private LinearLayout ll_title;
	private TextView txt_near, txt_friend, txt_hot;
	private ImageView img_publish;
	private ViewPager viewPager;
	private int selected_index = 0;
	private List<Fragment> list=new ArrayList<>();
	public static boolean flag=true;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v=inflater.inflate(R.layout.activity_find, container,false);
		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ll_title = (LinearLayout) v.findViewById(R.id.ll_title);
		txt_near = (TextView) v.findViewById(R.id.txt_near);
		txt_friend = (TextView) v.findViewById(R.id.txt_friend);
		txt_hot = (TextView) v.findViewById(R.id.txt_hot);
		img_publish = (ImageView) v.findViewById(R.id.img_publish);
		viewPager = (ViewPager) v.findViewById(R.id.viewPager);
		
		list.add(new FragmentNearbyPerson());
		list.add(new FragmentFriends());
		list.add(new FragmentPlaza());
		
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				selected_index = position;
				selectChange();
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		selectChange();
		
		txt_near.setOnClickListener(itemClickListener);
		txt_friend.setOnClickListener(itemClickListener);
		txt_hot.setOnClickListener(itemClickListener);
		img_publish.setOnClickListener(publishClickListener);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		
	}
	
	/**
	 * 发布动态
	 */
	OnClickListener publishClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (flag) {
				startActivity(new Intent(getActivity(), PublishActiviy.class));
			}
		}
	};

	/**
	 * 点击标题，更改选中内容
	 */
	private OnClickListener itemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView checkView = (TextView) v;
			int childCount = ll_title.getChildCount();
			for (int i = 0; i < childCount; i++) {
				TextView titleView = (TextView) ll_title.getChildAt(i);
				if (checkView.equals(titleView)) {
					selected_index = i;
					viewPager.setCurrentItem(selected_index);
					break;
				}
			}
			selectChange();
		}
	};

	/**
	 * 选中类别更改时，对应标题颜色更改
	 */
	@SuppressLint("NewApi")
	private void selectChange() {
		int childCount = ll_title.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TextView titleView = (TextView) ll_title.getChildAt(i);
			if (selected_index == i) {
				titleView.setTextColor(getResources().getColor(R.color.brown));
				titleView.setBackground(getResources().getDrawable(
						R.drawable.button_big_shape));
			} else {
				titleView.setTextColor(getResources().getColor(R.color.gray));
				titleView.setBackgroundColor(getResources().getColor(
						R.color.transparent));
			}
		}
	}

}
