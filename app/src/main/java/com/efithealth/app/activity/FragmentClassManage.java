package com.efithealth.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.fragment.FragmentHistoryClass;
import com.efithealth.app.fragment.FragmentOnLineClass;
import com.efithealth.app.fragment.IssueCourseActivity;

import image.Bimp;
import image.FileUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class FragmentClassManage extends BaseFragment implements
		View.OnClickListener {

	public static FragmentClassManage instance = null;
	public static boolean flag = false;
	private TextView tv_title_centre;
	private ImageView iv_class_manage_title_left;
	private ImageView iv_class_manage_title_right;

	private RadioButton rb_on_line_class;
	private RadioButton rb_history_class;

	private FragmentOnLineClass fragmentOnLineClass;
	private FragmentHistoryClass fragmentHistoryClass;

	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		instance = this;
		view = inflater.inflate(R.layout.fragment_class_manage, container,
				false);
		return view;

	}
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(view.getWindowToken());
		}
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("djy", "onActivityCreated");

		iv_class_manage_title_left = (ImageView) getView().findViewById(
				R.id.iv_class_manage_title_left);
		iv_class_manage_title_left
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						MainActivity.instance.returnBack();
					}
				});

		tv_title_centre = (TextView) getView().findViewById(
				R.id.tv_title_centre);

		iv_class_manage_title_right = (ImageView) getView().findViewById(
				R.id.iv_class_manage_title_right);
		iv_class_manage_title_right
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						MyApplication.getInstance().upDateIssueCourseFragment = true;// 更新发布课程fragment
						MyApplication.getInstance().editCourseFlag = false;// 发布课程
						Intent intent = new Intent(getActivity(),
								IssueCourseActivity.class);
						startActivity(intent);
					}
				});

		rb_on_line_class = (RadioButton) getView().findViewById(
				R.id.rb_on_line_class);
		rb_on_line_class.setOnClickListener(this);
		rb_history_class = (RadioButton) getView().findViewById(
				R.id.rb_history_class);
		rb_history_class.setOnClickListener(this);

		getFragmentManager();

		InitViewPager();

		// setTabSelection(0);

	}

	@Override
	public void onResume() {
		if (flag) {
			InitViewPager();
			flag = false;
		}
		super.onResume();
	}

	/*
	 * ��ʼ��ViewPager
	 */
	MyFragmentPagerAdapter adapter;

	public void InitViewPager() {
		mPager = (ViewPager) getView().findViewById(R.id.viewpager_cm);
		fragmentList = new ArrayList<Fragment>();

		fragmentOnLineClass = new FragmentOnLineClass();
		fragmentHistoryClass = new FragmentHistoryClass();

		fragmentList.add(fragmentOnLineClass);
		fragmentList.add(fragmentHistoryClass);

		// ��ViewPager����������
		adapter = new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentList);
		mPager.setAdapter(adapter);

		if (MyApplication.getInstance().fragmentHistoryFlag) {
			mPager.setCurrentItem(1);// ���õ�ǰ��ʾ��ǩҳΪ��һҳ
			rb_history_class.setChecked(true);
			MyApplication.getInstance().fragmentHistoryFlag = false;
		} else {
			mPager.setCurrentItem(0);// ���õ�ǰ��ʾ��ǩҳΪ��һҳ
		}

		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// ҳ��仯ʱ�ļ�����

	}

	public class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {
		// private int one = offset *2 +bmpW;//��������ҳ���ƫ����

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				rb_on_line_class.setChecked(true);
				break;
			case 1:
				rb_history_class.setChecked(true);
				break;

			}
			int i = arg0 + 1;
		}
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> list) {
			super(fm);
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

	}
	@Override
	public void onDestroy() {
		Bimp.drr.clear();
		Bimp.bmp.clear();
		Bimp.max = 0;
		Bimp.act_bool = true;
		FileUtils.deleteDir();
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.rb_on_line_class:
			// setTabSelection(0);
			mPager.setCurrentItem(0);
			break;
		case R.id.rb_history_class:
			// setTabSelection(1);
			mPager.setCurrentItem(1);
			break;
		}
		adapter.notifyDataSetChanged();
		
	}


}
