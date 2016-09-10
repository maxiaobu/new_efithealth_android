package com.efithealth.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.entity.MCourseList;
import com.efithealth.app.entity.MpCourseSave;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.Utils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class FragmentHistoryClass extends BaseFragment {

	private ListView lv_content;
	private MyAdapter myAdapter;

	private PopupWindow popupWindow;

	private View popupWindow_view;

	private String popupWindowWidth_dp = "300";
	private String popupWindowHeight_dp = "30";

	private Gson gson;

	private MpCourseSave mpCourseSave;
	private List<String> list;
	
	@Override
	public void onDestroyView() {
		FragmentHome.flag=true;
		super.onDestroyView();
	}
	@SuppressLint("InflateParams")
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentHome.flag=false;
		list=new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add("ceshi"+i);
		}
		gson = new Gson();

		View view = inflater.inflate(R.layout.fragment_history_class, null);

		lv_content = (ListView) view.findViewById(R.id.lv_content);

		Map<String, String> map = new HashMap<String, String>();
		map.put("coachid", (String) SharedPreferencesUtils.getParam(getActivity(), "memid", "")); // 会员id
		map.put("linestatus", "0");// 状态 0 历史 1 上线
		showProgressDialog();
		LoadDataFromServer task = new LoadDataFromServer(getActivity(), Constant.URL_MCOURSELIST, map);
		task.getData(new DataCallBack() {

			@Override
			public void onDataCallBack(JSONObject data) {
				closeProgressDialog();
				Log.d("ha", data.toString());

				MyApplication.getInstance().mHistoryCourseList = gson.fromJson(data.toString(), MCourseList.class);

				myAdapter = new MyAdapter(getActivity(), MyApplication.getInstance().mHistoryCourseList);
				lv_content.setAdapter(myAdapter);

			}
		});

		lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Log.d("=====", i + "");
			}
		});

		return view;
	}

	/***
	 * 鑾峰彇PopupWindow瀹炰緥
	 */
	private void getPopupWindow(int position) {
		// if (null != popupWindow) {
		// popupWindow.dismiss();
		// return;
		// } else {
		initPopupWindow(position);
		// }

	}

	/**
	 * 鍒涘缓PopupWindow
	 */
	@SuppressLint("InflateParams")
	protected void initPopupWindow(final int position) {

		// layoutInflater.from(getActivity());
		// 鑾峰彇鑷畾涔夊竷灞�枃浠禷ctivity_popupwindow_left.xml鐨勮鍥�
		popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.history_course_edit_popupwindow_right,
				null);
		// 鍒涘缓PopupWindow瀹炰緥,200,LayoutParams.MATCH_PARENT鍒嗗埆鏄搴﹀拰楂樺害
		popupWindow = new PopupWindow(popupWindow_view,
				Utils.dip2px(getActivity(), Float.parseFloat(popupWindowWidth_dp)),
				Utils.dip2px(getActivity(), Float.parseFloat(popupWindowHeight_dp)), true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		// 璁剧疆鍔ㄧ敾鏁堟灉
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 鐐瑰嚮鍏朵粬鍦版柟娑堝け
		// popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (popupWindow != null && popupWindow.isShowing()) {
		// popupWindow.dismiss();
		// popupWindow = null;
		// }
		// return false;
		// }
		// });

		final Map<String, String> map = new HashMap<String, String>();

		TextView tv_on_line = (TextView) popupWindow_view.findViewById(R.id.tv_on_line);
		tv_on_line.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.d("=====", "上线" + position);

				map.clear();
				map.put("pcourseid",
						MyApplication.getInstance().mHistoryCourseList.getCourseList().get(position).getPcourseid());// 课程编号
				map.put("linestatus", "1");// 状态 0 历史 1 上线

				showProgressDialog();
				LoadDataFromServer taskonline = new LoadDataFromServer(getActivity(), Constant.URL_UPDATECOURSE, map);
				taskonline.getData(new DataCallBack() {

					@Override
					public void onDataCallBack(JSONObject data) {

						closeProgressDialog();
						Log.d("===", data.toString());

						mpCourseSave = gson.fromJson(data.toString(), MpCourseSave.class);
						if ("1".equals(mpCourseSave.getMsgFlag())) {

							MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
							MyApplication.getInstance().fragmentHistoryFlag = true;
							MyApplication.getInstance().mCourseList
							.getCourseList()
							.add(MyApplication.getInstance().mHistoryCourseList
									.getCourseList().get(position));
							MyApplication.getInstance().mHistoryCourseList.getCourseList().remove(position);
							myAdapter.notifyDataSetChanged();
							
//							MainActivity.instance.setTabSelection(100);
							ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
							popupWindow.dismiss();

						} else {
							ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
						}

					}
				});

			}
		});
		TextView tv_del = (TextView) popupWindow_view.findViewById(R.id.tv_del);
		tv_del.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("=====", "删除" + position);
				map.clear();
				map.put("pcourseid",
						MyApplication.getInstance().mHistoryCourseList.getCourseList().get(position).getPcourseid());// 课程id
				showProgressDialog();
				LoadDataFromServer taskdelete = new LoadDataFromServer(getActivity(), Constant.URL_DELETECOURSE, map);
				taskdelete.getData(new DataCallBack() {

					@Override
					public void onDataCallBack(JSONObject data) {
						closeProgressDialog();
						Log.d("===", data.toString());
						mpCourseSave = gson.fromJson(data.toString(), MpCourseSave.class);
						if ("1".equals(mpCourseSave.getMsgFlag())) {

							MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
							MyApplication.getInstance().fragmentHistoryFlag = true;
							MyApplication.getInstance().mHistoryCourseList.getCourseList().remove(position);
							myAdapter.notifyDataSetChanged();
							
							ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
							popupWindow.dismiss();

						} else {
							ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
						}
					}
				});
			}
		});


	}

	@SuppressLint("InflateParams")
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		private MCourseList data;

		public MyAdapter(Context context, MCourseList data) {
			this.layoutInflater = LayoutInflater.from(context);
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.getCourseList() == null ? 0 : data.getCourseList().size();
		}

		@Override
		public Object getItem(int i) {
			return data.getCourseList().get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			String str_img = data.getCourseList().get(position).getImgpfile();
			final ViewHolder holder;
			// 观察convertView随ListView滚动情况
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.listview_item_onlingclass, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.tv_item_title_name = (TextView) convertView.findViewById(R.id.tv_item_title_name);
				holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
				holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
				holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				holder.iv_item_edit = (ImageView) convertView.findViewById(R.id.iv_item_edit);
				holder.iv_introduce = (ImageView) convertView.findViewById(R.id.iv_introduce);
				holder.iv_into = (ImageView) convertView.findViewById(R.id.iv_into);
				holder.iv_into.setVisibility(View.GONE);

				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			// holder.tv_item_title_name.setText("羿健康健身俱乐部=1");
			holder.tv_item_title_name.setVisibility(View.GONE);
			holder.tv_class.setText(data.getCourseList().get(position).getPcoursename());
			holder.tv_day.setText(data.getCourseList().get(position).getPcoursetimes() + "次/"
					+ data.getCourseList().get(position).getPcoursedays() + "天");
			holder.tv_money.setText(data.getCourseList().get(position).getPcourseprice() + "元");

			if (!str_img.contains("http://")) {
				Bitmap bm = BitmapFactory.decodeFile(str_img);
				holder.iv_introduce.setImageBitmap(bm);
			} else {
				ImageLoader.getInstance().displayImage(str_img,
						holder.iv_introduce,
						MyApplication.getInstance().initPicDisImgOption());
			}

			holder.iv_item_edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d("=====", "click edit");
					getPopupWindow(position);
					popupWindow
							.showAsDropDown(view, -(Utils.dip2px(getActivity(), Float.parseFloat(popupWindowWidth_dp))),
									-(view.getHeight()
											+ (Utils.dip2px(getActivity(), Float.parseFloat(popupWindowHeight_dp))
													- view.getHeight()) / 2));

				}
			});
			holder.iv_into.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d("=====", "click into");
				}
			});

			return convertView;

		}
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(getView().getWindowToken());
		}
	}
	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/** 存放控件 */
	public final class ViewHolder {
		public TextView tv_item_title_name, tv_class, tv_day, tv_money;
		public ImageView iv_item_edit, iv_introduce, iv_into;
	}
}
