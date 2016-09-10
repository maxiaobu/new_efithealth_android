package com.efithealth.app.activity;

import image.Bimp;
import image.CropImgActivity;
import image.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.ImageMessageBody;
import com.easemob.util.EMLog;
import com.efithealth.R;
import com.efithealth.R.integer;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.Photo.ChoosePhotoActivity5;
import com.efithealth.app.adapter.ImageAdapter;
import com.efithealth.app.adapter.ImageWallAdapter;
import com.efithealth.app.fragment.FragmentMe;
import com.efithealth.app.task.SendMyInfoTask;
import com.efithealth.app.task.SendMyInfoTask.DataCallBack;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 修改个人信息
 */
public class MyInfo extends BaseActivity {

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private static final int PHOTO_REQUEST_TAKEPHOTO_IMGLIST = 4; // 拍照
	private static final int PHOTO_REQUEST_GALLERY_IMGLIST = 5;// 从相册中选择
	private static final int IMGWALL_FINISH = 7;

	private static int istest = 0;// 0选择图片 1跳转到照片墙
	public String head_imagePath, temp_imagePath;
	private ImageView head_image, myinfo_iv;
	private LinearLayout add_img_personifno, myinfo_mem_ll;
	public GridView gridview;
	public ImageAdapter imgadapter;
	private TextView birthday, sex, changehead;
	private List<String> lists = new ArrayList<String>();
	private List<String> listid = new ArrayList<String>();
	private ProgressDialog dialog;
	private PopupWindows popupWindows;

	EditText kname, K_notes, K_nick, K_phone, K_address;

	private String brithday_temp;

	private String dimgStr = "";
	private String me_flag = "";

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		FragmentMy.flag_my = false;
		me_flag = (String) SharedPreferencesUtils.getParam(this, "me_change",
				"");
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.activity_personalinfo);
		super.onCreate(arg0);
		InitView();
	}

	private void InitView() {

		head_image = (ImageView) findViewById(R.id.touxiang);
		gridview = (GridView) findViewById(R.id.gridview_new);
		changehead = (TextView) findViewById(R.id.changehead);
		myinfo_iv = (ImageView) findViewById(R.id.myinfo_iv);
		add_img_personifno = (LinearLayout) findViewById(R.id.add_img_personifno);
		myinfo_mem_ll = (LinearLayout) findViewById(R.id.myinfo_mem_ll);
		imgadapter = new ImageAdapter(MyInfo.this, 0, lists);
		gridview.setAdapter(imgadapter);
		head_image.setOnClickListener(new HeadImgClickListener());
		changehead.setOnClickListener(new HeadImgClickListener());
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(MyInfo.this,
						ImagePagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("urls", (Serializable) lists);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		birthday = (TextView) findViewById(R.id.birthday);

		final Calendar c = Calendar.getInstance();
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dialog = new DatePickerDialog(MyInfo.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								c.set(year, monthOfYear, dayOfMonth);
								birthday.setText(DateFormat.format(
										"yyyy/MM/dd", c));
								brithday_temp = DateFormat.format("yyyy/MM/dd",
										c).toString();
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}

		});
		sex = (TextView) findViewById(R.id.sex);

		sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showSexDialog();
			}
		});

		Map<String, String> map = MyApplication.getInstance().getMyInfo();
		head_imagePath = map.get("headImgUrl");

		ImageLoader.getInstance().displayImage(head_imagePath, head_image,
				MyApplication.getInstance().initHeadDisImgOption());

		kname = (EditText) findViewById(R.id.K_name);
		kname.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int num = 7;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				System.out.println("s=" + s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionStart = kname.getSelectionStart();
				selectionEnd = kname.getSelectionEnd();
				// System.out.println("start="+selectionStart+",end="+selectionEnd);
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					kname.setText(s);
					kname.setSelection(tempSelection);// 设置光标在最后
					Toast.makeText(MyInfo.this, "最多输入7个汉字（标点或字母）", 0).show();
				}
			}
		});
		kname.setText(map.get("nickname"));
		birthday.setText(map.get("brithday"));
		brithday_temp = map.get("brithday");
		sex.setText((map.get("sex").equals("1") ? "男" : "女"));
		K_notes = (EditText) findViewById(R.id.K_notes);
		K_notes.setText(map.get("mysign"));

		K_nick = (EditText) findViewById(R.id.K_nick);
		K_nick.setText(map.get("addressname"));
		K_phone = (EditText) findViewById(R.id.K_phone);
		K_phone.setText(map.get("addresphone"));
		K_address = (EditText) findViewById(R.id.K_address);

		K_address.setText(map.get("address"));

		String memrole = map.get("memrole");
		if (memrole.equals("mem")) {
			istest = 0;
			add_img_personifno.setVisibility(View.GONE);
			add_img_personifno.setEnabled(false);
			myinfo_mem_ll.setVisibility(View.VISIBLE);
			myinfo_mem_ll.setEnabled(true);
		} else {
			istest = 1;
			add_img_personifno.setVisibility(View.VISIBLE);
			add_img_personifno.setEnabled(true);
			myinfo_mem_ll.setVisibility(View.GONE);
			myinfo_mem_ll.setEnabled(false);
		}

		// 普通会员照片墙走这个方法
		myinfo_mem_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GridItemClick();
			}
		});

		// 教练照片墙走这个方法
		add_img_personifno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(
						new Intent(MyInfo.this, ImageWall.class),
						IMGWALL_FINISH);
			}
		});
		setTopImg();

	}

	public void setTopImg() {
		lists.clear();
		listid.clear();
		Map<String, String> map_top = MyApplication.getInstance()
				.getImgWallInfo();
		String topImgList = map_top.get("topImgList");
		String topidList = map_top.get("topidList");

		if (!topImgList.equals("")) {
			String[] imglist = topImgList.split(",");
			String[] idlist = topidList.split(",");
			for (int i = 0; i < imglist.length; i++) {
				lists.add(imglist[i]);
				listid.add(idlist[i]);
				Bimp.drrTop.add(imglist[i]);
			}
			changeImg();
			imgadapter.notifyDataSetChanged();
		}

	}

	public void GridItemClick() {
		if (lists.size() < 5) {
			popupWindows = new PopupWindows(MyInfo.this,
					findViewById(R.id.myinfo_root), 1);
		}

	}

	class HeadImgClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.changehead:
				popupWindows = new PopupWindows(MyInfo.this,
						findViewById(R.id.myinfo_root), 0);
				break;
			case R.id.touxiang:
				showImgViewMy(head_imagePath);
				break;
			default:
				break;
			}

		}

	}

	@SuppressLint("SimpleDateFormat")
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
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

	/*
	 * Description:调用其他activity 结束回调函数
	 * 
	 * @author：Xushd
	 * 
	 * @since:2016年2月29日下午7:33:52
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String path = "";
			String newStr = "";
			Bitmap bm;
			Bundle bundle;
			switch (requestCode) {
			// 头像照相回调
			case PHOTO_REQUEST_TAKEPHOTO:
				if (data != null) {
					bundle = data.getExtras();
					bm = (Bitmap) bundle.get("data");
					// newStr = head_imagePath.substring(
					// head_imagePath.lastIndexOf("/") + 1,
					// head_imagePath.lastIndexOf("."));
					// path = FileUtils.SDPATH + newStr + ".JPEG";
					// bm = BitmapFactory.decodeFile(path);
					Bimp.bmp.clear();
					Bimp.bmp.add(bm);
					Intent intent1 = new Intent(MyInfo.this,
							CropImgActivity.class);
					intent1.putExtra("path", path);
					intent1.putExtra("name", head_imagePath);
					intent1.putExtra("flag", "0");
					startActivityForResult(intent1, PHOTO_REQUEST_CUT);
				} else {
					Toast.makeText(MyInfo.this, "照片无法保存！！！", 0).show();
				}
				break;
			// 头像相册回调
			case PHOTO_REQUEST_GALLERY:
				if (data != null) {
					try {
						path = getImgPath(data.getData());
						bm = BitmapFactory.decodeFile(path);
						Bimp.bmp.clear();
						Bimp.bmp.add(bm);
						FileUtils.saveBitmap(bm, "" + newStr);
						newStr = head_imagePath.substring(
								head_imagePath.lastIndexOf("/") + 1,
								head_imagePath.lastIndexOf("."));
						path = FileUtils.SDPATH + newStr + ".JPEG";
						Intent intent = new Intent(MyInfo.this,
								CropImgActivity.class);
						intent.putExtra("path", path);
						intent.putExtra("name", head_imagePath);
						intent.putExtra("flag", "1");
						startActivityForResult(intent, PHOTO_REQUEST_CUT);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MyInfo.this, "图片路径错误！！！", 0).show();
				}
				break;
			// 照片墙照相回调
			case PHOTO_REQUEST_TAKEPHOTO_IMGLIST:
				if (data != null) {
					bundle = data.getExtras();
					bm = (Bitmap) bundle.get("data");
					FileUtils.saveBitmap(bm, "" + newStr);
					newStr = temp_imagePath.substring(
							temp_imagePath.lastIndexOf("/") + 1,
							temp_imagePath.lastIndexOf("."));
					FileUtils.saveBitmap(bm, "" + newStr);
					path = FileUtils.SDPATH + newStr + ".JPEG";

					try {
						bm = Bimp.revitionImageSize(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
					path = FileUtils.SDPATH + newStr + ".JPEG";
					lists.add(path);
					changeImg();
					imgadapter.notifyDataSetChanged();
					temp_imagePath = "";
				} else {
					Toast.makeText(MyInfo.this, "照片无法保存！！！", 0).show();
				}
				break;

			// 照片墙相册回调
			case PHOTO_REQUEST_GALLERY_IMGLIST:
				lists.clear();
				try {
					for (int i = 0; i < Bimp.drrTop.size(); i++) {
						path = Bimp.drrTop.get(i);
						if (!path.contains("http://")) {
							bm = Bimp.revitionImageSize(path);
							newStr = path.substring(path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							lists.add(FileUtils.SDPATH + newStr + ".JPEG");
						} else {
							lists.add(path);
						}
					}
					changeImg();
					imgadapter.notifyDataSetChanged();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// if (data != null) {
				// path = getImgPath(data.getData());
				// try {
				// bm = Bimp.revitionImageSize(path);
				//
				// int angle = readPictureDegree(path);
				// if (angle != 0) {
				// // 旋转图片 动作
				// Matrix matrix = new Matrix();
				// matrix.postRotate(angle);
				// System.out.println("angle2=" + angle);
				// // 创建新的图片
				// bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				// bm.getHeight(), matrix, true);
				// }
				// newStr = temp_imagePath.substring(
				// temp_imagePath.lastIndexOf("/") + 1,
				// temp_imagePath.lastIndexOf("."));
				// FileUtils.saveBitmap(bm, "" + newStr);
				// path = FileUtils.SDPATH + newStr + ".JPEG";
				// lists.add(path);
				// changeImg();
				// imgadapter.notifyDataSetChanged();
				// temp_imagePath = "";
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// } else {
				// Toast.makeText(MyInfo.this, "图片路径错误！！！", 0).show();
				// }
				break;

			// 头像剪切回调
			case PHOTO_REQUEST_CUT:
				newStr = head_imagePath.substring(
						head_imagePath.lastIndexOf("/") + 1,
						head_imagePath.lastIndexOf("."));
				path = FileUtils.SDPATH + newStr + ".JPEG";
				try {
					bm = Bimp.revitionImageSize(path);

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
					path = FileUtils.SDPATH + newStr + ".JPEG";

					bm = BitmapFactory.decodeFile(path);
					head_image.setImageBitmap(bm);
					head_imagePath = path;
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case IMGWALL_FINISH:
				setTopImg();
				break;
			}

			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	private void changeImg() {
		if (istest == 0) {
			if (lists.size() < 4) {
				myinfo_mem_ll.setVisibility(View.VISIBLE);
				myinfo_mem_ll.setEnabled(true);
			} else {
				myinfo_mem_ll.setVisibility(View.GONE);
				myinfo_mem_ll.setEnabled(false);
			}

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

	public void showImgView(Object object) {

		showImgViewMy((String) object);

	}

	public void delImg(int position) {
		if (lists.get(position).contains("http://")) {
			lists.remove(position);
			Bimp.drrTop.remove(position);
			dimgStr += listid.get(position) + ",";
			listid.remove(position);
		} else {
			lists.remove(position);
			Bimp.drrTop.remove(position);
		}
		changeImg();
		imgadapter.notifyDataSetChanged();

	}

	/**
	 * 性别选择弹出框
	 */
	public void showSexDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alertdialog);
		LinearLayout ll_title = (LinearLayout) window
				.findViewById(R.id.ll_title);
		ll_title.setVisibility(View.VISIBLE);
		TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
		tv_title.setText("性别");
		// 为确认按钮添加事件,执行退出应用操作
		TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
		tv_paizhao.setText("男");
		tv_paizhao.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SdCardPath")
			public void onClick(View v) {
				sex.setText("男");

				dlg.cancel();
			}
		});
		TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
		tv_xiangce.setText("女");
		tv_xiangce.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sex.setText("女");
				dlg.cancel();
			}
		});
	}

	public void btn_myinfo_click(View v) {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存中...");
		dialog.show();
		Map<String, String> content = new HashMap<String, String>();
		content.put("memid", MyApplication.getInstance().getMemid());
		content.put("nickname", kname.getText().toString());
		content.put("signature", K_notes.getText().toString());
		content.put("recaddress", K_address.getText().toString());
		content.put("recname", K_nick.getText().toString());
		content.put("recphone", K_phone.getText().toString());
		content.put("birthday", brithday_temp);
		content.put("gender", sex.getText().toString().equals("男") ? "1" : "0");
		// content.put("gender", "男");
		if (!dimgStr.equals("")) {
			content.put("dimg",
					(String) dimgStr.subSequence(0, dimgStr.length() - 1));
		} else {
			content.put("dimg", "");
		}

		SendMyInfoTask task = new SendMyInfoTask(MyInfo.this,
				Constant.URL_MYINFO_UPDATE, lists, head_imagePath, content);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				dialog.dismiss();
				if (data == null) {
					ToastCommom.getInstance().ToastShow(MyInfo.this,
							"服务器连接错误...");
					return;
				}

				String code = data.getString("msgFlag");// ["1","1"]
				Log.i("String code1", code);
				if (code.equals("[\"1\",\"1\"]")) {
					Log.i("flag", code.equals("[\"1\",\"1\"]") + "");
					ToastCommom.getInstance().ToastShow(MyInfo.this, "修改成功");
					MyApplication.getInstance().update_local_myinfo();
					MyInfo.this.setResult(11);
					FragmentMy.flag_my = true;
					FileUtils.deleteDir();
					if (me_flag.equals("yes")) {
						FragmentMe.handler.sendEmptyMessage(0);
					}
					SharedPreferencesUtils.deleteSharedData(MyInfo.this,
							"me_change");
					finish();
				} else {
					ToastCommom.getInstance().ToastShow(MyInfo.this,
							"服务器端错误:" + data.getString("msgContent"));
				}

			}
		});

	}

	public void showImgViewMy(String imgpath) {
		Intent intent = new Intent(MyInfo.this, ShowImgActivity.class);
		intent.putExtra("imgpath", imgpath);
		startActivity(intent);
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
					if (isSDCard()) {
						if (flag == 0) {
							head_imagePath = getNowTime() + ".JPEG";
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							// .fromFile(new File(FileUtils.SDPATH,
							// head_imagePath)));
							startActivityForResult(intent,
									PHOTO_REQUEST_TAKEPHOTO);
						} else {
							temp_imagePath = getNowTime() + ".JPEG";
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,
									PHOTO_REQUEST_TAKEPHOTO_IMGLIST);
						}

					} else {
						Toast.makeText(MyInfo.this, "SD卡不可用，无法完成此操作", 0).show();
					}

					dismiss();
				}
			});

			// 相册
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isSDCard()) {
						if (flag == 0) {
							getNowTime();
							head_imagePath = getNowTime() + ".JPEG";
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							intent.setDataAndType(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							startActivityForResult(intent,
									PHOTO_REQUEST_GALLERY);
						} else {
								Intent intent = new Intent(MyInfo.this,
										ChoosePhotoActivity5.class);
								startActivityForResult(intent,
										PHOTO_REQUEST_GALLERY_IMGLIST);
							// getNowTime();
							// temp_imagePath = getNowTime() + ".JPEG";
							// Intent intent = new Intent(Intent.ACTION_PICK,
							// null);
							// intent.setDataAndType(
							// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							// "image/*");
							// startActivityForResult(intent,
							// PHOTO_REQUEST_GALLERY_IMGLIST);
						}
					} else {
						Toast.makeText(MyInfo.this, "SD卡不可用", 0).show();
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

	private boolean isSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	private long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.drrTop.clear();
		Bimp.max = 0;
		Bimp.act_bool = true;
		FileUtils.deleteDir();
		SharedPreferencesUtils.deleteSharedData(MyInfo.this, "me_change");
		super.onDestroy();
	}

}
