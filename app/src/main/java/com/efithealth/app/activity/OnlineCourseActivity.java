package com.efithealth.app.activity;

import image.Bimp;
import image.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.adapter.ImageWallAdapter;
import com.efithealth.app.entity.MpCourseSave;
import com.efithealth.app.fragment.FragmentOnLineClass;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.StringUtil;
import com.efithealth.app.utils.ToastCommom;
import com.google.gson.Gson;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OnlineCourseActivity extends Activity {

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	private TextView tv_title;
	private ImageView iv_course_picture;
	private Button btn_commit;
	private Button btn_cancel;
	private LinearLayout ll_1;
	private RelativeLayout rl_1;

	public String head_imagePath = null;

	private MpCourseSave mpCourseSave;

	private Gson gson;

	private int position;
	
	 private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertdialog_on_line_edit);
		

		gson = new Gson();

		ll_1 = (LinearLayout) findViewById(R.id.ll_1);
		ll_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		iv_course_picture = (ImageView) findViewById(R.id.iv_course_picture);

		tv_title.setText("修改图片");
		iv_course_picture.setVisibility(View.VISIBLE);

		Intent intent = this.getIntent(); // 获取已有的intent对象
		Bundle bundle = intent.getExtras(); // 获取intent里面的bundle对象
		position = bundle.getInt("position"); // 获取Bundle里面的字符串
		String str_img = MyApplication.getInstance().mCourseList.getCourseList().get(position).getImgpfile();
		if (!str_img.contains("http://")) {
			Bitmap bm = BitmapFactory.decodeFile(str_img);
			iv_course_picture.setImageBitmap(bm);
		}else{
			ImageLoader.getInstance().displayImage(
					MyApplication.getInstance().mCourseList.getCourseList().get(position).getImgpfile(),
					iv_course_picture,
					MyApplication.getInstance().initPicDisImgOption());
		}

		iv_course_picture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("debug", "iv_course_picture");
				new PopupWindows(OnlineCourseActivity.this, findViewById(R.id.ll_1), 0);
			}
		});

		Button btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("debug", "btn_commit");
				

				if (StringUtil.isEmpty(head_imagePath)) {
					ToastCommom.getInstance().ToastShow(OnlineCourseActivity.this, "请上传新的课程图片");
					return;
				}

				showProgressDialog();
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("pcourseid",
						MyApplication.getInstance().mCourseList.getCourseList().get(position).getPcourseid());// 课程编号
				map.put("imagefile", head_imagePath);// 图片修改

				LoadDataFromServer task = new LoadDataFromServer(OnlineCourseActivity.this, Constant.URL_UPDATECOURSE,
						map);
				task.getData(new DataCallBack() {

					@Override
					public void onDataCallBack(JSONObject data) {

						Log.i("djy", data.toString());
						closeProgressDialog();

						mpCourseSave = gson.fromJson(data.toString(), MpCourseSave.class);
						if ("1".equals(mpCourseSave.getMsgFlag())) {

							ToastCommom.getInstance().ToastShow(OnlineCourseActivity.this,
									mpCourseSave.getMsgContent());

							MyApplication.getInstance().newCourseFlag = true;// 改变发布课程标志
							
							FragmentOnLineClass.flag_img=true;
							MyApplication.getInstance().mCourseList
							.getCourseList().get(position).setImgpfile(head_imagePath);
//							MainActivity.instance.setTabSelection(100);
							FragmentOnLineClass.instance.popupWindow.dismiss();
							OnlineCourseActivity.this.finish();

						} else {
							ToastCommom.getInstance().ToastShow(OnlineCourseActivity.this,
									mpCourseSave.getMsgContent());
						}

					}
				});

			}
		});

		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("debug", "btn_cancel");

				Intent intent = new Intent();
				intent = intent.setClass(OnlineCourseActivity.this, FragmentOnLineClass.class);
				Bundle bundle = new Bundle();
				bundle.putString("result", "2");
				
				intent.putExtras(bundle);
				// OnlineCourseActivity.this.setResult(RESULT_OK, intent);
				// //RESULT_OK是返回状态码
				OnlineCourseActivity.this.finish(); // 会触发onDestroy();
			}
		});

	}
	
	

	@SuppressLint("SimpleDateFormat")
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
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

	// 获取相册图片的路径
	@SuppressWarnings("deprecation")
	private String getImgPath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);
		File file = new File(img_path);
		return file.getPath();
	}

	private String toImg(Intent data, String path, String imgNamePath,
			ImageView iv) {
		String newStr = "";
		Bitmap bm;
		try {
			if (data != null) {
				Bundle bundle = data.getExtras();
				bm = (Bitmap) bundle.get("data");
			} else {
				bm = Bimp.revitionImageSize(path);
			}
			newStr = imgNamePath.substring(imgNamePath.lastIndexOf("/") + 1,
					imgNamePath.lastIndexOf("."));
			FileUtils.saveBitmap(bm, "" + newStr);
			path = FileUtils.SDPATH + newStr + ".JPEG";
			int angle = readPictureDegree(path);
			if (angle != 0) {
				// 旋转图片 动作
				Matrix matrix = new Matrix();
				matrix.postRotate(angle);
				System.out.println("angle2=" + angle);
				// 创建新的图片
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), matrix, true);
			}
			FileUtils.saveBitmap(bm, "" + newStr);
			iv.setImageBitmap(bm);
			path = FileUtils.SDPATH + newStr + ".JPEG";
			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/*
	 * Description:调用其他activity 结束回调函数
	 * 
	 * @author：Xushd
	 * 
	 * @since:2016年2月29日下午7:33:52
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PHOTO_REQUEST_TAKEPHOTO:
				if (data != null) {
					head_imagePath = toImg(data, "",head_imagePath, iv_course_picture);
				}
				break;
			case PHOTO_REQUEST_GALLERY:
				if (data != null){
					String path = getImgPath(data.getData());
					head_imagePath = toImg(null, path,head_imagePath, iv_course_picture);
				}
				break;
			case PHOTO_REQUEST_CUT:
				ImageLoader.getInstance().displayImage("file:///mnt" + Constant.DIR_PATH + head_imagePath,
						iv_course_picture, MyApplication.getInstance().initPicDisImgOption());

				break;

			}
			super.onActivityResult(requestCode, resultCode, data);
		}

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	public void showProgressDialog() {

		dialog = new ProgressDialog(OnlineCourseActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存中...");
		dialog.show();

	}
	@SuppressLint("ClickableViewAccessibility")
	public class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent, final int flag) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

			// 照相
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (flag == 0) {
						head_imagePath = getNowTime() + ".png";
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// 指定调用相机拍照后照片的储存路径
//						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constant.DIR_PATH, head_imagePath)));
						startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
					}
					dismiss();
				}
			});

			// 相册
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (flag == 0) {
						getNowTime();
						head_imagePath = getNowTime() + ".png";
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
					}
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
	
	public void closeProgressDialog(){
		dialog.dismiss();
	}

}
