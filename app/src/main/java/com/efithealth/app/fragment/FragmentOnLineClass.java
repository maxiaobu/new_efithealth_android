package com.efithealth.app.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.OnlineCourseActivity;
import com.efithealth.app.entity.MCourseList;
import com.efithealth.app.entity.MpCourseSave;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.StringUtil;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.Utils;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
@SuppressLint("NewApi")
public class FragmentOnLineClass extends BaseFragment {

	public static FragmentOnLineClass instance = null;
	public static boolean flag_img = false;

	public String head_imagePath = null;

	private ListView lv_content;
	private MyAdapter myAdapter;

	public PopupWindow popupWindow;

	private LayoutInflater layoutInflater;

	private View popupWindow_view;

	private String popupWindowWidth_dp = "300";
	private String popupWindowHeight_dp = "32";

	private Gson gson;

	private MpCourseSave mpCourseSave;

	@Override
	public void onDestroyView() {
		FragmentHome.flag=true;
		super.onDestroyView();
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FragmentHome.flag=false;
		instance = this;
		gson = new Gson();

		View view = inflater.inflate(R.layout.fragment_on_line_class, null);

		lv_content = (ListView) view.findViewById(R.id.lv_content);

		Map<String, String> map = new HashMap<String, String>();
		map.put("coachid", (String) SharedPreferencesUtils.getParam(
				getActivity(), "memid", "")); // 会员id
		map.put("linestatus", "1");// 状态 0 历史 1 上线
		showProgressDialog1();
		LoadDataFromServer task = new LoadDataFromServer(getActivity(),
				Constant.URL_MCOURSELIST, map);
		task.getData(new DataCallBack() {

			@Override
			public void onDataCallBack(JSONObject data) {
				closeProgressDialog();
				Log.d("===", data.toString());

				MyApplication.getInstance().mCourseList = gson.fromJson(
						data.toString(), MCourseList.class);

				myAdapter = new MyAdapter(getActivity(), MyApplication
						.getInstance().mCourseList);
				lv_content.setAdapter(myAdapter);

			}
		});

		lv_content
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						Log.d("=====", i + "");
						MyApplication.getInstance().upDateIssueCourseFragment = true;// 更新发布课程fragment
						MyApplication.getInstance().editCourseFlag = true;// 编程课程
						MyApplication.getInstance().coursePosition = i;// 课程位置
						Intent intent = new Intent(getActivity(),
								IssueCourseActivity.class);
						startActivity(intent);
					}
				});

		return view;
	}

	@Override
	public void onResume() {
		if (flag_img) {
			myAdapter.notifyDataSetChanged();
			flag_img = false;
		}
		super.onResume();
	}

	/***
	 * 鑾峰彇PopupWindow瀹炰緥
	 */
	private void getPopupWindow(int position) {
		initPopupWindow(position);
	}

	/**
	 * 鍒涘缓PopupWindow
	 */
	protected void initPopupWindow(final int position) {

		// 鑾峰彇鑷畾涔夊竷灞�枃浠禷ctivity_popupwindow_left.xml鐨勮鍥�
		popupWindow_view = getActivity().getLayoutInflater().inflate(
				R.layout.on_line_course_edit_popupwindow_right, null);
		// 鍒涘缓PopupWindow瀹炰緥,200,LayoutParams.MATCH_PARENT鍒嗗埆鏄搴﹀拰楂樺害
		popupWindow = new PopupWindow(popupWindow_view, Utils.dip2px(
				getActivity(), Float.parseFloat(popupWindowWidth_dp)),
				Utils.dip2px(getActivity(),
						Float.parseFloat(popupWindowHeight_dp)), true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		// 璁剧疆鍔ㄧ敾鏁堟灉
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 鐐瑰嚮鍏朵粬鍦版柟娑堝け

		TextView tv_name = (TextView) popupWindow_view
				.findViewById(R.id.tv_name);
		tv_name.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("=====", "名称" + position);
				showEditDialog(1, position);

			}

		});
		TextView tv_picture = (TextView) popupWindow_view
				.findViewById(R.id.tv_picture);
		tv_picture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("=====", "图片" + position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), OnlineCourseActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
				// showEditDialog(2, position);
			}
		});
		TextView tv_combo = (TextView) popupWindow_view
				.findViewById(R.id.tv_combo);
		tv_combo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("=====", "套餐" + position);
				showEditDialog(3, position);
			}
		});
		TextView tv_downline = (TextView) popupWindow_view
				.findViewById(R.id.tv_downline);
		tv_downline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("=====", "下线"
						+ MyApplication.getInstance().mCourseList
								.getCourseList().get(position).getPcoursename());
				showEditDialog(4, position);

			}
		});

	}

	public void showEditDialog(final int index, final int position) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		dialog.setCanceledOnTouchOutside(true);// 单击外部关闭对话框
		dialog.setCancelable(true);
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog_on_line_edit);
		// 为确认按钮添加事件,执行退出应用操作
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
		final EditText et_course_name = (EditText) window
				.findViewById(R.id.et_course_name);
		RelativeLayout rl_course_number = (RelativeLayout) window
				.findViewById(R.id.rl_course_number);
		RelativeLayout rl_course_time = (RelativeLayout) window
				.findViewById(R.id.rl_course_time);
		final EditText et_course_number = (EditText) window
				.findViewById(R.id.et_course_number);
		final EditText et_course_time = (EditText) window
				.findViewById(R.id.et_course_time);
		final EditText et_price = (EditText) window.findViewById(R.id.et_price);
		Button btn_commit = (Button) window.findViewById(R.id.btn_commit);
		Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);

		switch (index) {
		case 1:// 名称
			tv_title.setText("修改名称");
			et_course_name.setVisibility(View.VISIBLE);
			et_course_name.setText(MyApplication.getInstance().mCourseList
					.getCourseList().get(position).getPcoursename());

			break;
		case 3:// 套餐
			tv_title.setText("修改套餐");
			rl_course_number.setVisibility(View.VISIBLE);
			rl_course_time.setVisibility(View.VISIBLE);
			et_price.setVisibility(View.VISIBLE);

			et_course_number.setText(MyApplication.getInstance().mCourseList
					.getCourseList().get(position).getPcoursetimes());
			et_course_time.setText(MyApplication.getInstance().mCourseList
					.getCourseList().get(position).getPcoursedays());
			et_price.setText(MyApplication.getInstance().mCourseList
					.getCourseList().get(position).getPcourseprice());

			break;
		case 4:// 下线
			tv_title.setText("课程下线");

			break;

		}

		final Map<String, String> map = new HashMap<String, String>();

		btn_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("deubg=====", "click commit");

				showProgressDialog();

				switch (index) {
				case 1:// 名称
					if (StringUtil.isEmpty(et_course_name.getText().toString())) {
						ToastCommom.getInstance().ToastShow(getActivity(),
								"课程名称不能为空");
						return;
					}
					map.clear();
					map.put("pcourseid",
							MyApplication.getInstance().mCourseList
									.getCourseList().get(position)
									.getPcourseid());// 课程编号
					map.put("pcoursename", et_course_name.getText().toString());
					break;
				case 3:// 套餐
					if (StringUtil.isEmpty(et_course_number.getText()
							.toString())) {
						ToastCommom.getInstance().ToastShow(getActivity(),
								"课程次数不能为空");
						return;
					}
					if (StringUtil.isEmpty(et_course_time.getText().toString())) {
						ToastCommom.getInstance().ToastShow(getActivity(),
								"课程时间不能为空");
						return;
					}
					if (StringUtil.isEmpty(et_price.getText().toString())) {
						ToastCommom.getInstance().ToastShow(getActivity(),
								"价格不能为空");
						return;
					}
					map.clear();
					map.put("pcourseid",
							MyApplication.getInstance().mCourseList
									.getCourseList().get(position)
									.getPcourseid());// 课程编号
					map.put("pcoursetimes", et_course_number.getText()
							.toString());// 课程次数
					map.put("pcoursedays", et_course_time.getText().toString());// 课程天数
					map.put("pcourseprice", et_price.getText().toString());// 课程价格
					break;
				case 4:// 下线
					map.clear();
					map.put("pcourseid",
							MyApplication.getInstance().mCourseList
									.getCourseList().get(position)
									.getPcourseid());// 课程编号
					map.put("linestatus", "0");// 1上线 0:下线
					break;
				}

				LoadDataFromServer task = new LoadDataFromServer(getActivity(),
						Constant.URL_UPDATECOURSE, map);
				task.getData(new DataCallBack() {

					@Override
					public void onDataCallBack(JSONObject data) {

						Log.d("===", data.toString());
						closeProgressDialog();

						mpCourseSave = gson.fromJson(data.toString(),
								MpCourseSave.class);
						if ("1".equals(mpCourseSave.getMsgFlag())) {

							MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志

							switch (index) {
							case 1:
								MyApplication.getInstance().mCourseList
										.getCourseList()
										.get(position)
										.setPcoursename(
												et_course_name.getText()
														.toString());
								break;
							case 3:
								MyApplication.getInstance().mCourseList
										.getCourseList()
										.get(position)
										.setPcoursedays(
												et_course_time.getText()
														.toString());
								MyApplication.getInstance().mCourseList
										.getCourseList()
										.get(position)
										.setPcoursetimes(
												et_course_number.getText()
														.toString());
								MyApplication.getInstance().mCourseList
										.getCourseList()
										.get(position)
										.setPcourseprice(
												et_price.getText().toString());
								break;
							case 4:
								MyApplication.getInstance().mHistoryCourseList
										.getCourseList()
										.add(MyApplication.getInstance().mCourseList
												.getCourseList().get(position));

								MyApplication.getInstance().mCourseList
										.getCourseList().remove(position);
								break;

							}
							myAdapter.notifyDataSetChanged();

							// MainActivity.instance.setTabSelection(100);
							ToastCommom.getInstance().ToastShow(getActivity(),
									mpCourseSave.getMsgContent());
							popupWindow.dismiss();
							dialog.cancel();

						} else {
							ToastCommom.getInstance().ToastShow(getActivity(),
									mpCourseSave.getMsgContent());
						}

					}
				});

			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("deubg=====", "click commit");
				dialog.cancel();
			}
		});

	}

	private class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		private MCourseList data;

		public MyAdapter(Context context, MCourseList data) {
			this.layoutInflater = LayoutInflater.from(context);
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.getCourseList() == null ? 0 : data.getCourseList()
					.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			String str_img = data.getCourseList().get(position).getImgpfile();
			final ViewHolder holder;
			// 观察convertView随ListView滚动情况
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.listview_item_onlingclass, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.tv_item_title_name = (TextView) convertView
						.findViewById(R.id.tv_item_title_name);
				holder.tv_class = (TextView) convertView
						.findViewById(R.id.tv_class);
				holder.tv_day = (TextView) convertView
						.findViewById(R.id.tv_day);
				holder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				holder.iv_item_edit = (ImageView) convertView
						.findViewById(R.id.iv_item_edit);
				holder.iv_introduce = (ImageView) convertView
						.findViewById(R.id.iv_introduce);
				holder.iv_into = (ImageView) convertView
						.findViewById(R.id.iv_into);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			holder.tv_item_title_name.setText(data.getCourseList()
					.get(position).getClubname());
			holder.tv_class.setText(data.getCourseList().get(position)
					.getPcoursename());
			holder.tv_day
					.setText(data.getCourseList().get(position)
							.getPcoursetimes()
							+ "次/"
							+ data.getCourseList().get(position)
									.getPcoursedays() + "天");
			holder.tv_money.setText(data.getCourseList().get(position)
					.getPcourseprice()
					+ "元");

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
					popupWindow.showAsDropDown(
							view,
							-(Utils.dip2px(getActivity(),
									Float.parseFloat(popupWindowWidth_dp))),
							-(view.getHeight() + (Utils.dip2px(getActivity(),
									Float.parseFloat(popupWindowHeight_dp)) - view
									.getHeight()) / 2));

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

	/** 存放控件 */
	public final class ViewHolder {
		public TextView tv_item_title_name, tv_class, tv_day, tv_money;
		public ImageView iv_item_edit, iv_introduce, iv_into;
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
			InputMethodManager manager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
