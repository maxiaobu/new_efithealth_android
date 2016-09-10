package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.efithealth.R;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.utils.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class OrderListFragment extends BaseFragment {
	private View v;
	private ViewPager vp;
	private ImageView iv_back;
	private TextView txt_course, txt_forder,tv_massage;
	private MyPageAdapter adapter;
	private List<Fragment> list = new ArrayList<Fragment>();
	private String me_flag="";

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		me_flag=(String) SharedPreferencesUtils.getParam(getActivity(), "me_change", "");
		v = inflater.inflate(R.layout.fragment_order_list, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		vp = (ViewPager) v.findViewById(R.id.viewPager);
		iv_back = (ImageView) v.findViewById(R.id.order_img_back);
		txt_course = (TextView) v.findViewById(R.id.txt_course_order);
		txt_forder = (TextView) v.findViewById(R.id.txt_forder_order);
		tv_massage=(TextView) v.findViewById(R.id.txt_massage_order);
		txt_course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				page0();
				vp.setCurrentItem(0);
				
			}
		});
		txt_forder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				page1();
				vp.setCurrentItem(1);

			}
		});
		tv_massage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				vp.setCurrentItem(2);
				
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.returnBack();
			}
		});
		list.add(new FragmentClassOrder());
		list.add(new FragmentOrderFood());
		list.add(new FragmentMassageOrderList());
		adapter = new MyPageAdapter(getChildFragmentManager());
		vp.setOffscreenPageLimit(2);
		vp.setAdapter(adapter);
		vp.setCurrentItem(1);
		page1();
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pageIndex) {
				switch (pageIndex) {
				case 0:
					page0();
					break;

				case 1:
					page1();
					break;
				case 2:
					page2();
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int pageIndex) {

			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}
	
	private void page0(){
		txt_course.setTextColor(getResources().getColor(R.color.brown));
		txt_course.setBackground(getResources().getDrawable(
				R.drawable.button_big_shape));
		txt_forder.setTextColor(getResources().getColor(R.color.gray));
		txt_forder.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		tv_massage.setTextColor(getResources().getColor(R.color.gray));
		tv_massage.setBackgroundColor(getResources().getColor(
				R.color.transparent));
	}
	
	private void page1(){
		txt_forder.setTextColor(getResources().getColor(R.color.brown));
		txt_forder.setBackground(getResources().getDrawable(
				R.drawable.button_big_shape));
		txt_course.setTextColor(getResources().getColor(R.color.gray));
		txt_course.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		tv_massage.setTextColor(getResources().getColor(R.color.gray));
		tv_massage.setBackgroundColor(getResources().getColor(
				R.color.transparent));
	}
	private void page2(){
		tv_massage.setTextColor(getResources().getColor(R.color.brown));
		tv_massage.setBackground(getResources().getDrawable(
				R.drawable.button_big_shape));
		txt_course.setTextColor(getResources().getColor(R.color.gray));
		txt_course.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		txt_forder.setTextColor(getResources().getColor(R.color.gray));
		txt_forder.setBackgroundColor(getResources().getColor(
				R.color.transparent));
	}
	
	@Override
	public void onDestroy() {
		if (me_flag.equals("yes")) {
			FragmentMe.handler.sendEmptyMessage(0);
		}
		SharedPreferencesUtils.deleteSharedData(getActivity(), "me_change");
		super.onDestroy();
	}

}
