package com.efithealth.app.fragment;

import image.Bimp;
import image.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.Photo.ChoosePhotoActivity2;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.PublishActiviy;
import com.efithealth.app.entity.ClubList;
import com.efithealth.app.entity.MpCourseSave;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.StringUtil;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.view.ScrollviewEdit;
import com.google.gson.Gson;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 发布课程
 */
public class FragmentIssueCourse extends BaseFragment {

	private ImageView iv_issue_course_title_left;
	private ImageView iv_course_picture;
	private EditText et_course_name;
	private EditText et_course_number;
	private EditText et_course_time;
	private EditText et_club;
	private EditText et_price;
	private EditText et_notice;
	private Button btn_issue_commit;
	private ImageView iv_club;
	private Application app;
	private RelativeLayout rl_club;
	private TextView tv_title_centre;

	public String head_imagePath = null, temp_imagePath;

	private MyAdapter myAdapter;


	private Gson gson;
	private ClubList clubList;
	private MpCourseSave mpCourseSave;

	private ListView listView;

	private Boolean btn_issue_commit_flag = false;// true 为发布了课程
	
	private TextView tv_title;
	
	private ScrollView sv_1;
	private ScrollviewEdit sv_feedback;

	@Override
	public void onDestroyView() {
		FragmentHome.flag=true;
		super.onDestroyView();
	}
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentHome.flag=false;
		gson = new Gson();
		View view = inflater.inflate(R.layout.fragment_issue_class, null);
		iv_course_picture = (ImageView) view.findViewById(R.id.iv_course_picture);
		et_course_name = (EditText) view.findViewById(R.id.et_course_name);
		et_course_number = (EditText) view.findViewById(R.id.et_course_number);
		et_course_time = (EditText) view.findViewById(R.id.et_course_time);

		et_club = (EditText) view.findViewById(R.id.et_club);
		rl_club = (RelativeLayout) view.findViewById(R.id.rl_club);
		tv_title_centre = (TextView) view.findViewById(R.id.tv_title_centre);

		et_price = (EditText) view.findViewById(R.id.et_price);
		et_notice = (EditText) view.findViewById(R.id.et_notice);
		
		sv_1 = (ScrollView) view.findViewById(R.id.sv_1);
		sv_feedback = (ScrollviewEdit) view.findViewById(R.id.sv_feedback);
		sv_feedback.setParent_scrollview(sv_1);
		
		btn_issue_commit = (Button) view.findViewById(R.id.btn_issue_commit);
		btn_issue_commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!checkInput())
					return;
				showProgressDialog();
				if (MyApplication.getInstance().editCourseFlag) {// 从编程过来的,走下面的请求
					Log.d("debug", "编程课程");

					int i = MyApplication.getInstance().coursePosition;// 课程位置
																		// 上线课程在listview里的位置

					Map<String, String> map = new HashMap<String, String>();
					map.put("pcourseid", MyApplication.getInstance().mCourseList.getCourseList().get(i).getPcourseid());// 课程编号
					map.put("pcoursename", et_course_name.getText().toString());// 名称修改

					if (!StringUtil.isEmpty(path)) {
						Log.i("djy", "1             "+StringUtil.isEmpty(path));
						map.put("imagefile", path);// 图片修改
					}
					
					map.put("pcoursetimes", et_course_number.getText().toString());// 课程次数
					map.put("pcoursedays", et_course_time.getText().toString());// 课程天数
					map.put("pcourseprice", et_price.getText().toString());// 课程价格
					map.put("resinform", et_notice.getText().toString());
					map.put("linestatus", "1");// 1上线0:下线

					LoadDataFromServer task = new LoadDataFromServer(getActivity(), Constant.URL_UPDATECOURSE, map);
					task.getData(new DataCallBack() {

						@Override
						public void onDataCallBack(JSONObject data) {

							closeProgressDialog();
							Log.d("===", data.toString());

							mpCourseSave = gson.fromJson(data.toString(), MpCourseSave.class);
							if ("1".equals(mpCourseSave.getMsgFlag())) {

								btn_issue_commit_flag = true;
								MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
								ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
								Bimp.drr.clear();
								Bimp.bmp.clear();
								Bimp.max = 0;
								Bimp.act_bool = true;
								FileUtils.deleteDir();
								MainActivity.instance.returnBack();

							} else {
								ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
							}

						}
					});

				} else {// fa布课程

					Map<String, String> map = new HashMap<String, String>();
					map.put("coachid", (String) SharedPreferencesUtils.getParam(getActivity(), "memid", "")); // 会员id
																												// 教练编号
					map.put("pcoursename", et_course_name.getText().toString());// 课程名称
					map.put("pcoursecode", "");// 课程特征码
					map.put("imagefile", path);// 图片
					map.put("pcoursetimes", et_course_number.getText().toString());
					map.put("pcoursedays", et_course_time.getText().toString());
					map.put("pcourseprice", et_price.getText().toString());
					map.put("resinform", et_notice.getText().toString());

					for (int i = 0; i < clubList.getBindList().size(); i++) {
						if (et_club.getText().toString().equals(clubList.getBindList().get(i).getClubname())) {

							Log.d("clubid", "btn_issue_commit" + clubList.getBindList().get(i).getClubid());

							map.put("clubid", clubList.getBindList().get(i).getClubid());// 俱乐部编码
						}
					}

					LoadDataFromServer task = new LoadDataFromServer(getActivity(), Constant.URL_ISSUE_COURSE, map);
					task.getData(new DataCallBack() {

						@Override
						public void onDataCallBack(JSONObject data) {
							closeProgressDialog();
							Log.d("===", data.toString());

							mpCourseSave = gson.fromJson(data.toString(), MpCourseSave.class);
							if ("1".equals(mpCourseSave.getMsgFlag())) {

								btn_issue_commit_flag = true;
								MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
								ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
								Bimp.drr.clear();
								Bimp.bmp.clear();
								Bimp.max = 0;
								Bimp.act_bool = true;
								FileUtils.deleteDir();
								MainActivity.instance.returnBack();
							} else {
								ToastCommom.getInstance().ToastShow(getActivity(), mpCourseSave.getMsgContent());
							}

						}
					});
				}

			}
		});

		iv_issue_course_title_left = (ImageView) view.findViewById(R.id.iv_issue_course_title_left);
		iv_issue_course_title_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bimp.drr.clear();
				Bimp.bmp.clear();
				Bimp.max = 0;
				Bimp.act_bool = true;
				FileUtils.deleteDir();
				if (btn_issue_commit_flag) {
					MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
				}
				MainActivity.instance.returnBack();
			}
		});

		iv_course_picture.setOnClickListener(new HeadImgClickListener());

		iv_club = (ImageView) view.findViewById(R.id.iv_club);
		iv_club.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				showClubDialog();
			}
		});

		request();// 获得教练绑定的俱乐部信息

		if (MyApplication.getInstance().editCourseFlag) {// 编程课程
			et_club.setVisibility(View.GONE);
			rl_club.setVisibility(View.GONE);
			tv_title_centre.setText("编辑课程");

			int i = MyApplication.getInstance().coursePosition;// 课程位置
																// 上线课程在listview里的位置

			ImageLoader.getInstance().displayImage(
					MyApplication.getInstance().mCourseList.getCourseList().get(i).getImgpfile(), iv_course_picture,
					MyApplication.getInstance().initPicDisImgOption());

			et_course_name.setText(MyApplication.getInstance().mCourseList.getCourseList().get(i).getPcoursename());
			et_course_number.setText(MyApplication.getInstance().mCourseList.getCourseList().get(i).getPcoursetimes());
			et_course_time.setText(MyApplication.getInstance().mCourseList.getCourseList().get(i).getPcoursedays());
			et_price.setText(MyApplication.getInstance().mCourseList.getCourseList().get(i).getPcourseprice());
			et_notice.setText(MyApplication.getInstance().mCourseList.getCourseList().get(i).getResinform());

		} else {
			tv_title_centre.setText("发布课程");
		}

		return view;
	}

	public void showClubDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.club_dialog);
		dialog.show();
		
		tv_title = (TextView) dialog.findViewById(R.id.tv_title);
		tv_title.setText("合作俱乐部");
		listView = (ListView) dialog.findViewById(R.id.lv_club);

		listView.setAdapter(myAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Log.d("dubug=====", arg2 + "");
				et_club.setText(clubList.getBindList().get(arg2).getClubname());
				dialog.dismiss();

			}
		});
		
		
		

	}

	/**
	 * 获得教练绑定的俱乐部信息
	 */
	private void request() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("memid", (String) SharedPreferencesUtils.getParam(getActivity(), "memid", ""));

		LoadDataFromServer task_club_message = new LoadDataFromServer(getActivity(), Constant.URL_CLUB_MESSAGE, map);
		task_club_message.getData(new DataCallBack() {

			@Override
			public void onDataCallBack(JSONObject data) {
				Log.d("===获得教练绑定的俱乐部信息", data.toString());

				clubList = gson.fromJson(data.toString(), ClubList.class);

				if ("1".equals(clubList.getMsgFlag())) {
					Log.d("===教练绑定的俱乐部信息 ok", "ok");

					myAdapter = new MyAdapter(getActivity(), clubList);

				} else {
					Toast.makeText(getActivity(), "没有教练绑定信息", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	@SuppressLint("InflateParams")
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		private ClubList data;

		public MyAdapter(Context context, ClubList clubList) {
			layoutInflater = LayoutInflater.from(context);
			data = clubList;
		}

		@Override
		public int getCount() {
			return data.getBindList() == null ? 0 : data.getBindList().size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.getBindList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.item_issue_course, null);
				holder = new ViewHolder();
				holder.tv_club_name = (TextView) convertView.findViewById(R.id.tv_club_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_club_name.setText(data.getBindList().get(position).getClubname());

			return convertView;
		}

	}

	public final class ViewHolder {
		public TextView tv_club_name;

	}

	class HeadImgClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
//			showPhotoDialog(0);
			//TODO
			new PopupWindows(getActivity(),getView().findViewById(R.id.class_rl1));
		}

	}

	@SuppressLint("InflateParams")
	public void showPhotoDialog(final int flag) {
		final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog);
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
		tv_paizhao.setText("拍照");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {
				
					photo();
					dlg.cancel();
				

			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
		tv_xiangce.setText("相册");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						ChoosePhotoActivity2.class);
				startActivityForResult(intent,110);
				dlg.cancel();
			}
		});

	}
	public class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					if (event.getAction()==MotionEvent.ACTION_DOWN) {
						dismiss();
						return true;
					}
					return false;
				}
			});
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			 setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							ChoosePhotoActivity2.class);
					startActivityForResult(intent,110);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	@SuppressLint("SimpleDateFormat")
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
	}
	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
	

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String name = String.valueOf(System.currentTimeMillis()) + ".jpg";
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", name);
		path = file.getPath();
		Log.i("djy", "1             "+path);
//		Uri imageUri = Uri.fromFile(file);
//		 openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		switch (requestCode) {
		case TAKE_PICTURE:
			if ( resultCode == -1) {
				
				String newStr="";
				try {
					Bitmap bm;
					Bundle bundle = data.getExtras();
					bm=(Bitmap) bundle.get("data");
					newStr = path.substring(
							path.lastIndexOf("/") + 1,
							path.lastIndexOf("."));
					FileUtils.saveBitmap(bm, "" + newStr);
					path=FileUtils.SDPATH+newStr + ".JPEG";
					Bimp.drr.add(path);
//					bm = Bimp.revitionImageSize(path);
					int angle = readPictureDegree(path);
					if (angle != 0) {
						// 旋转图片 动作
						Matrix matrix = new Matrix();
						matrix.postRotate(angle);
						System.out.println("angle2=" + angle);
						// 创建新的图片
						bm = Bitmap.createBitmap(bm, 0, 0,
								bm.getWidth(), bm.getHeight(),
								matrix, true);
					}
					newStr = path.substring(
							path.lastIndexOf("/") + 1,
							path.lastIndexOf("."));
					FileUtils.saveBitmap(bm, "" + newStr);
					path=FileUtils.SDPATH+newStr + ".JPEG";
					iv_course_picture.setImageBitmap(bm);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			break;
		case 110:
				try {
					
					int n=Bimp.drr.size()-1;
					if (n<0) {
							break;				
					}
					path = Bimp.drr.get(n);
					Bitmap bm;
					bm = Bimp.revitionImageSize(path);
					int angle = readPictureDegree(path);
					if (angle != 0) {
						// 旋转图片 动作
						Matrix matrix = new Matrix();
						matrix.postRotate(angle);
						System.out.println("angle2=" + angle);
						// 创建新的图片
						bm = Bitmap.createBitmap(bm, 0, 0,
								bm.getWidth(), bm.getHeight(),
								matrix, true);
					}
					String newStr = path.substring(
							path.lastIndexOf("/") + 1,
							path.lastIndexOf("."));
					FileUtils.saveBitmap(bm, "" + newStr);
					iv_course_picture.setImageBitmap(bm);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			break;
		}
		
	}
	
	public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	private Boolean checkInput() {

		if (!MyApplication.getInstance().editCourseFlag) {// 编程课程
			if (StringUtil.isEmpty(path)) {
				ToastCommom.getInstance().ToastShow(getActivity(), "课程图片不能为空");
				return false;
			}
		}

		if (StringUtil.isEmpty(et_course_name.getText().toString())) {
			ToastCommom.getInstance().ToastShow(getActivity(), "课程名称不能为空");
			return false;
		}
		if (StringUtil.isEmpty(et_course_number.getText().toString())) {
			ToastCommom.getInstance().ToastShow(getActivity(), "课程次数不能为空");
			return false;
		}
		if (StringUtil.isEmpty(et_course_time.getText().toString())) {
			ToastCommom.getInstance().ToastShow(getActivity(), "课程时间不能为空");
			return false;
		}

		if (!MyApplication.getInstance().editCourseFlag) {// 编程课程

			if (StringUtil.isEmpty(et_club.getText().toString())) {
				ToastCommom.getInstance().ToastShow(getActivity(), "合作俱乐部不能为空");
				return false;
			}

		}

		if (StringUtil.isEmpty(et_price.getText().toString())) {
			ToastCommom.getInstance().ToastShow(getActivity(), "价格不能为空");
			return false;
		}

		return true;
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

}
