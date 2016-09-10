package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.List;

import com.efithealth.R;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.MipcaActivityCapture;
import com.efithealth.app.menu.ActionItem;
import com.efithealth.app.menu.TitlePopup;
import com.efithealth.app.menu.TitlePopup.OnItemOnClickListener;
import com.efithealth.app.utils.ToastCommom;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentTalk extends BaseFragment {

	private static final int QECODE_RESULT = 1;
	private View v;
	private TextView talk_message, talk_group;
	private TitlePopup titlePopup;
	private ViewPager vp;
	private LinearLayout ll_title;
	private int selected_index = 0;
	private List<Fragment> listvp=new ArrayList<Fragment>();
	private FragmentTalkMessage fragmentTalkMessage=new FragmentTalkMessage();
	private FragmentTalkGroup fragmentTalkGroup=new FragmentTalkGroup();

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_talk, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		talk_message = (TextView) v.findViewById(R.id.talk_message);
		talk_group = (TextView) v.findViewById(R.id.talk_group);
		ll_title=(LinearLayout) v.findViewById(R.id.ll_title_talk);
		vp = (ViewPager) v.findViewById(R.id.talk_viewPager);

		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(getActivity(),
				R.string.menu_findfriend, R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(getActivity(),
				R.string.menu_addchat, R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(getActivity(),
				R.string.menu_qrcode, R.drawable.icon_menu_sao));

		ImageView addimg = (ImageView) getView().findViewById(R.id.chat_add);

		addimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				titlePopup.show(getView().findViewById(R.id.chat_add));
			}
		});
		talk_message.setOnClickListener(itemClickListener);
		talk_group.setOnClickListener(itemClickListener);

		listvp.add(fragmentTalkMessage);
		listvp.add(fragmentTalkGroup);
		vp.setAdapter(new MyAdapter(getChildFragmentManager()));
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
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
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		fragmentTalkMessage.refresh();
		fragmentTalkGroup.refresh();
	}
	
	class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return listvp.get(arg0);
		}

		@Override
		public int getCount() {
			return listvp.size();
		}
		
	}
	
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
					vp.setCurrentItem(selected_index);
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

	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 查找好友
				MainActivity.instance.onTabClicked(getView().findViewById(
						R.id.chat_add));
				break;
			case 1:// 发起聊天
				// MainActivity.instance.setFromIndex(2);
				MainActivity.instance.setTabSelection(202);
				break;
			case 2:// 扫一扫
				Intent intent = new Intent(getActivity(),
						MipcaActivityCapture.class);
				startActivityForResult(intent, QECODE_RESULT);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == QECODE_RESULT) {
			if (data == null) {
				ToastCommom.getInstance().ToastShow(getActivity(), "取消");
			} else {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("data");
				ToastCommom.getInstance().ToastShow(getActivity(), scanResult);
			}
		}
	}

}
