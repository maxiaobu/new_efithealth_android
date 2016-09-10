package com.efithealth.app.activity;

import image.Bimp;
import image.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.image.ImagePagerActivity;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.Photo.ChoosePhotoActivity3;
import com.efithealth.app.Photo.ChoosePhotoActivity4;
import com.efithealth.app.Photo.ChoosePhotoActivity5;
import com.efithealth.app.adapter.ImageWallAdapter;
import com.efithealth.app.javabean.HotDynamic.ImgList;
import com.efithealth.app.task.SendImgWallTask;
import com.efithealth.app.task.SendImgWallTask.DataCallBack;
import com.efithealth.app.utils.ToastCommom;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ImageWall extends BaseActivity {

	private static final int PHOTO_REQUEST_TAKEPHOTO_TOP = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY_TOP = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_TAKEPHOTO_MORE = 4; // 拍照
	private static final int PHOTO_REQUEST_GALLERY_MORE = 5;// 从相册中选择
	public GridView gridview_top, gridview_more;
	private List<String> lists_top = new ArrayList<String>();
	private List<String> lists_more = new ArrayList<String>();
	private List<String> lists_top_send = new ArrayList<String>();
	private List<String> lists_more_send = new ArrayList<String>();
	private List<String> listsid_top = new ArrayList<String>();
	private List<String> listsid_more = new ArrayList<String>();

	private List<String> lists_top1 = new ArrayList<String>();
	private List<String> lists_more1 = new ArrayList<String>();
	public ImageWallAdapter imgadapter_top, imgadapter_more;
	public String imgpath_top, imgpath_more;
	private ProgressDialog dialog;
	@SuppressWarnings("unused")
	private PopupWindows popupWindows;
	private String dimgstr = "";
	private int preTop=0,preMore=0;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_imagewall);
		super.onCreate(arg0);
		Bimp.drrTop.clear();
		InitView();
	}

	private void InitView() {

		dialog = new ProgressDialog(ImageWall.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});

		gridview_top = (GridView) findViewById(R.id.gridview_top);
		gridview_more = (GridView) findViewById(R.id.gridview_more);

		ImageView back = (ImageView) findViewById(R.id.back_imgw);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				delBimp();
				finish();

			}
		});

		gridview_top.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				lists_top1.clear();
				Intent intent = new Intent(ImageWall.this,
						ImagePagerActivity.class);
				for (int i = 0; i < lists_top.size(); i++) {
					if (!lists_top.get(i).equals("add")) {
						lists_top1.add(lists_top.get(i));
					}
				}
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("urls", (Serializable) lists_top1);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		gridview_more.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				lists_more1.clear();
				for (int i = 0; i < lists_more.size(); i++) {
					if (!lists_more.get(i).equals("add")) {
						lists_more1.add(lists_more.get(i));
					}
				}
				Intent intent = new Intent(ImageWall.this,
						ImagePagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("urls", (Serializable) lists_more1);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		getImgWallData();

	}

	public void getImgWallData() {
		dialog.setMessage("照片墙信息读取中...");
		dialog.show();
		Map<String, String> map = MyApplication.getInstance().getImgWallInfo();
		String topImgList = map.get("topImgList");
		String moreImgList = map.get("moreImgList");
		String topidlist = map.get("topidList");
		String moreidlist = map.get("moreidList");
		if (topImgList == "") {
			lists_top.add("add");
			preTop=Bimp.drrTop.size();
			imgadapter_top = new ImageWallAdapter(ImageWall.this, 0, lists_top);// 0置顶照片
			gridview_top.setAdapter(imgadapter_top);
		} else {
			String[] imglist = topImgList.split(",");
			String[] idlist = topidlist.split(",");
			for (int i = 0; i < imglist.length; i++) {
				lists_top.add(imglist[i]);
				listsid_top.add(idlist[i]);
				Bimp.drrTop.add(imglist[i]);
			}
			if (imglist.length < 4) {
				lists_top.add("add");
			}
			preTop=Bimp.drrTop.size();
			imgadapter_top = new ImageWallAdapter(ImageWall.this, 0, lists_top);// 0置顶照片
			gridview_top.setAdapter(imgadapter_top);

		}
		if (moreImgList == "") {
			lists_more.add("add");
			preMore=Bimp.drr.size();
			imgadapter_more = new ImageWallAdapter(ImageWall.this, 1,
					lists_more);// 0置顶照片
			gridview_more.setAdapter(imgadapter_more);
		} else {
			String[] imglist = moreImgList.split(",");
			String[] idlist = moreidlist.split(",");
			for (int i = 0; i < imglist.length; i++) {
				lists_more.add(imglist[i]);
				listsid_more.add(idlist[i]);
				Bimp.drr.add(imglist[i]);
			}
			if (imglist.length < 12) {
				lists_more.add("add");
			}
			preMore=Bimp.drr.size();
			imgadapter_more = new ImageWallAdapter(ImageWall.this, 1,
					lists_more);// 0置顶照片
			gridview_more.setAdapter(imgadapter_more);

		}
		dialog.dismiss();
	}

	public void GridItemClick(int resource) {
		if (resource == 0) {
			if (lists_top.size() < 5) {
				popupWindows = new PopupWindows(ImageWall.this,
						findViewById(R.id.imagewall_root), resource);
			}
		} else {
			if (lists_more.size() < 13) {
				popupWindows = new PopupWindows(ImageWall.this,
						findViewById(R.id.imagewall_root), resource);
			}
		}
	}

	public void showImgView(String path, int resource) {
		Intent intent = new Intent(ImageWall.this, ShowImgActivity.class);
		intent.putExtra("imgpath", path);
		startActivity(intent);

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

//	// 获取相册图片的路径
//	@SuppressWarnings("deprecation")
//	private String getImgPath(Uri uri) {
//		String[] proj = { MediaStore.Images.Media.DATA };
//		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//		int actual_image_column_index = actualimagecursor
//				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		actualimagecursor.moveToFirst();
//		String img_path = actualimagecursor
//				.getString(actual_image_column_index);
//		File file = new File(img_path);
//		return file.getPath();
//	}

	private void toImg(Intent data, String path, String imgNamePath,
			List<String> list, ImageWallAdapter adapter, int index) {
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
			FileUtils.saveBitmap(bm, newStr);
			path = FileUtils.SDPATH + newStr + ".JPEG";
			Bimp.drr.add(path);
			list.remove(list.size() - 1);// 移除add图片
			list.add(path);
			if (index == 1) {
				if (list.size() < 4) {
					list.add("add");
				}
			} else {
				if (list.size() < 12) {
					list.add("add");
				}
			}
			adapter.notifyDataSetChanged();
			saveImg();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			// 顶部
			case PHOTO_REQUEST_TAKEPHOTO_TOP:
				if (data != null) {
					toImg(data, "", imgpath_top, lists_top, imgadapter_top, 1);
				}
				break;
			case PHOTO_REQUEST_GALLERY_TOP:
				if (Bimp.drr.size() > 0) {
					revitionImage(4, Bimp.drrTop, lists_top, imgadapter_top,lists_top_send);
					
					saveImg();
				}
				// if (data != null) {
				// String path = getImgPath(data.getData());
				// toImg(null, path, imgpath_top, lists_top, imgadapter_top, 1);
				// }
				break;

			// 尾部
			case PHOTO_REQUEST_TAKEPHOTO_MORE:
				if (data != null) {
					toImg(data, "", imgpath_more, lists_more, imgadapter_more,
							2);
				}
				break;
			case PHOTO_REQUEST_GALLERY_MORE:
				if (Bimp.drr.size() > 0) {
					revitionImage(12, Bimp.drr, lists_more, imgadapter_more,lists_more_send);
					saveImg();
				}
				// if (data != null) {
				//
				// // String path = getImgPath(data.getData());
				// // toImg(null, path, imgpath_more, lists_more,
				// // imgadapter_more, 2);
				// }
				break;
			}
			for (int i = 0; i < lists_top_send.size(); i++) {
				Log.i("djy",lists_top_send.get(i));
			}
			Log.i("djy",preTop+"");
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	public void delImg(int position, int m_resource) {
		if (m_resource == 0) {
			String url = lists_top.get(position);

			if (url.contains("http://")) {
				dimgstr += listsid_top.get(position) + ",";
				listsid_top.remove(position);
			}
			lists_top.remove(position);
			Bimp.drrTop.remove(position);
			preTop=Bimp.drrTop.size();
			if (lists_top.size() == 3) {
				if (!lists_top.get(2).equals("add")) {
					lists_top.add("add");
				}
			}
			imgadapter_top.notifyDataSetChanged();
			saveImg();
		} else {
			String url = lists_more.get(position);
			if (url.contains("http://")) {
				dimgstr += listsid_more.get(position) + ",";
				listsid_more.remove(position);
			}

			lists_more.remove(position);
			Bimp.drr.remove(position);
			preMore=Bimp.drr.size();
			if (lists_more.size() == 11) {
				if (!lists_more.get(10).equals("add")) {
					lists_more.add("add");
				}
			}
			imgadapter_more.notifyDataSetChanged();
			saveImg();
		}
	}

	private void saveImg() {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存中...");
		dialog.show();

		Map<String, String> map = new HashMap<>();
		map.put("memid", MyApplication.getInstance().getMemid());
		if (!dimgstr.equals("")) {
			map.put("dimg",
					(String) dimgstr.subSequence(0, dimgstr.length() - 1));
		} else {
			map.put("dimg", "");
		}

		SendImgWallTask task = new SendImgWallTask(ImageWall.this,
				Constant.URL_IMGWALL_UPLOAD, lists_top_send, lists_more_send, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				dialog.dismiss();
				if (data == null) {
					ToastCommom.getInstance().ToastShow(ImageWall.this,
							"服务器连接错误...");
					return;
				}
				int code = data.getInteger("msgFlag");
				if (code == 1) {
					ToastCommom.getInstance().ToastShow(ImageWall.this,
							data.getString("msgContent"));
					MyApplication.getInstance().update_loacl_imgWall();
					// FileUtils.deleteDir();
				} else {
					ToastCommom.getInstance().ToastShow(ImageWall.this,
							"服务器端错误:" + data.getString("msgContent"));
				}

			}
		});
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
					if (flag == 0) {// TOP
						if (Bimp.drrTop.size() < 4) {
							imgpath_top = getNowTime() + ".png";
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,
									PHOTO_REQUEST_TAKEPHOTO_TOP);
						} else {
							Toast.makeText(ImageWall.this, "已有4张图片，不能再添加了", 0)
									.show();
						}
					} else {// MORE
						if (Bimp.drr.size() < 12) {
							imgpath_more = getNowTime() + ".png";
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent,
									PHOTO_REQUEST_TAKEPHOTO_MORE);
						} else {
							Toast.makeText(ImageWall.this, "已有12张图片，不能再添加了", 0)
									.show();
						}
					}
					dismiss();
				}
			});

			// 相册
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (flag == 0) {// TOP
						if (Bimp.drrTop.size() < 4) {
							Intent intent = new Intent(ImageWall.this,
									ChoosePhotoActivity5.class);
							startActivityForResult(intent,
									PHOTO_REQUEST_GALLERY_TOP);
						} else {
							Toast.makeText(ImageWall.this, "已有4张图片，不能再添加了", 0)
									.show();
						}
						// getNowTime();
						// imgpath_top = getNowTime() + ".png";
						// Intent intent = new Intent(Intent.ACTION_PICK, null);
						// intent.setDataAndType(
						// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						// "image/*");
						// startActivityForResult(intent,
						// PHOTO_REQUEST_GALLERY_TOP);
					} else {
						if (Bimp.drr.size() < 12) {
							Intent intent = new Intent(ImageWall.this,
									ChoosePhotoActivity4.class);
							startActivityForResult(intent,
									PHOTO_REQUEST_GALLERY_MORE);
							// getNowTime();
							// imgpath_more = getNowTime() + ".png";
							// Intent intent = new Intent(Intent.ACTION_PICK,
							// null);
							// intent.setDataAndType(
							// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							// "image/*");
							// startActivityForResult(intent,
							// PHOTO_REQUEST_GALLERY_MORE);
						} else {
							Toast.makeText(ImageWall.this, "已有12张图片，不能再添加了", 0)
									.show();
						}
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

	// 压缩图片
	private void revitionImage(int type, List<String> listImg,
			List<String> listTorM, ImageWallAdapter adapter,List<String> listSend) {
		listTorM.clear();
		listSend.clear();
		try {
			for (int i = 0; i < listImg.size(); i++) {
				String path = listImg.get(i);
				if (!path.contains("http://")) {
					Bitmap bm = Bimp.revitionImageSize(path);
					String newStr = path.substring(path.lastIndexOf("/") + 1,
							path.lastIndexOf("."));
					FileUtils.saveBitmap(bm, "" + newStr);
					listTorM.add(FileUtils.SDPATH + newStr + ".JPEG");
					if (i>(type==4?preTop:preMore)-1) {
						listSend.add(FileUtils.SDPATH + newStr + ".JPEG");
					}
				} else {
					listTorM.add(path);
				}
			}
			if (type==4) {
				preTop=listTorM.size();
			}else{
				preMore=listTorM.size();
			}
			
			if (listImg.size() < type) {
				listTorM.add("add");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	private void delBimp() {
		Bimp.drr.clear();
		Bimp.bmp.clear();
		Bimp.max = 0;
		Bimp.act_bool = true;
		FileUtils.deleteDir();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		delBimp();
		finish();

	}

	@Override
	protected void onDestroy() {
		delBimp();
		super.onDestroy();
	}
}
