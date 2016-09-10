package com.efithealth.app.activity;

import image.Bimp;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import image.FileUtils;
import image.PhotoActivity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kpswitch.util.KPSwitchConflictUtil;
import kpswitch.util.KeyboardUtil;
import kpswitch.widget.KPSwitchFSPanelLinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.task.SendMyPublishTask;
import com.efithealth.app.task.SendMyPublishTask.DataCallBack;
import com.efithealth.app.utils.ToastCommom;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * 发布动态
 */
@SuppressLint("InlinedApi")
public class PublishActiviy extends BaseActivity {

	public String temp_imgpath;
	public GridAdapter adapter;
	private EditText et_content;

	public GridView gridview;
	public String isclose = "0";
	public ImageView open, close;
	private ProgressDialog dialog;

	private KPSwitchFSPanelLinearLayout panel_root;
	private OnGlobalLayoutListener ogll;
	private int height;
	PopupWindows popupWindows;

	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.activity_publish);
		super.onCreate(arg0);
		InitView();
		init();
		panel_root = (KPSwitchFSPanelLinearLayout) findViewById(R.id.panel_root123);
		// 监听键盘的
		KeyboardUtil.attach(this, panel_root);
		KPSwitchConflictUtil.attach(panel_root, null, et_content,
				new KPSwitchConflictUtil.SwitchClickListener() {
					@Override
					public void onClickSwitch(boolean switchToPanel) {
						if (switchToPanel) {
							et_content.clearFocus();
						} else {
							et_content.requestFocus();
						}
					}
				});
		// 监听键盘的
		ogll = new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
				WindowManager wm = getWindowManager();
				height = wm.getDefaultDisplay().getHeight();

				int h = height - (rect.bottom - rect.top);
				boolean flag = h > height / 3;
				if (flag) {
					panel_root.setVisibility(View.VISIBLE);
				} else {
					panel_root.setVisibility(View.GONE);
				}

			}
		};
	}

	// 取消监听键盘是否异常（不及时取消，容易OOM）
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void hehe() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().getDecorView().getViewTreeObserver()
					.removeOnGlobalLayoutListener(ogll);
		} else {
			getWindow().getDecorView().getViewTreeObserver()
					.removeGlobalOnLayoutListener(ogll);
		}
	}

	public void init() {
		adapter = new GridAdapter(this);
		adapter.update();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(PublishActiviy.this,
						PhotoActivity.class);
				intent.putExtra("ID", position);
				startActivity(intent);
			}
		});
	}

	private void InitView() {

		et_content = (EditText) findViewById(R.id.et_content);
		et_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 开始监听键盘是否显示
				getWindow().getDecorView().getViewTreeObserver()
						.addOnGlobalLayoutListener(ogll);
			}
		});
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hehe();
				Bimp.drr.clear();
				Bimp.bmp.clear();
				Bimp.max = 0;
				Bimp.act_bool = true;
				FileUtils.deleteDir();
				PublishActiviy.this.finish();
			}
		});
		final LinearLayout insert = (LinearLayout) findViewById(R.id.insert);
		insert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (Bimp.drr.size() <= 8) {
					popupWindows = new PopupWindows(PublishActiviy.this,
							findViewById(R.id.rootView123));
					hehe();
				} else {
					ToastCommom.getInstance().ToastShow(PublishActiviy.this,
							"最大支持9张图片！");
				}

			}
		});
		final LinearLayout send = (LinearLayout) findViewById(R.id.tosend);
		send.setOnClickListener(new ButtonClick());

		open = (ImageView) findViewById(R.id.iv_switch_open_notification);
		close = (ImageView) findViewById(R.id.iv_switch_close_notification);
		open.setOnClickListener(new ButtonClick());
		close.setOnClickListener(new ButtonClick());
		gridview = (GridView) findViewById(R.id.gridview_publish);
	}


	@SuppressLint("ClickableViewAccessibility")
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
					Intent intent = new Intent(PublishActiviy.this,
							ChoosePhotoActivity.class);
					startActivity(intent);
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


	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String name = String.valueOf(System.currentTimeMillis()) + ".jpg";
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", name);
		path = file.getPath();
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bitmap bm;
				Bundle bundle = data.getExtras();
				bm=(Bitmap) bundle.get("data");
				String newStr = path.substring(
						path.lastIndexOf("/") + 1,
						path.lastIndexOf("."));
				FileUtils.saveBitmap(bm, "" + newStr);
				path=FileUtils.SDPATH+newStr + ".JPEG";
				Bimp.drr.add(path);

			}
			break;
		case 110:
			if (data != null) {
				if (data.getStringExtra("send1").equals("是")) {
					sendMyPublish();
				}

			}
			break;
		}
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public void delImg(int position) {

		Bimp.drr.remove(position);
		Bimp.bmp.remove(position);
		adapter.notifyDataSetChanged();
	}

	private class ButtonClick implements OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.iv_switch_open_notification:
				open.setVisibility(View.INVISIBLE);
				close.setVisibility(View.VISIBLE);
				isclose = "0";
				break;
			case R.id.iv_switch_close_notification:
				open.setVisibility(View.VISIBLE);
				close.setVisibility(View.INVISIBLE);
				isclose = "1";
				break;
			case R.id.tosend:
				startActivityForResult(
						new Intent(PublishActiviy.this,
								com.efithealth.app.activity.AlertDialog.class)
								.putExtra("titleIsCancel", true)
								.putExtra("msg", "确定发送？")
								.putExtra("cancel", true), 110);
				break;

			}
		}
	}

	@SuppressLint({ "HandlerLeak", "InflateParams" })
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return Bimp.bmp.size();
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater
						.inflate(R.layout.personalinfo_list, null);
				holder = new ViewHolder();
				holder.add_img = (ImageView) convertView
						.findViewById(R.id.test);
				holder.del_img = (ImageView) convertView.findViewById(R.id.del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (Bimp.bmp.size() > 0) {
				holder.add_img.setImageBitmap(Bimp.bmp.get(position));
				holder.del_img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						delImg(position);
					}
				});
			}

			return convertView;
		}

		public class ViewHolder {
			private ImageView add_img, del_img;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								Bitmap bm = Bimp.revitionImageSize(path);
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
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				hehe();
				Bimp.drr.clear();
				Bimp.bmp.clear();
				Bimp.max = 0;
				Bimp.act_bool = true;
				FileUtils.deleteDir();
				PublishActiviy.this.finish();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
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

	private List<String> getImgUrls() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring(
					Bimp.drr.get(i).lastIndexOf("/") + 1,
					Bimp.drr.get(i).lastIndexOf("."));
			list.add(FileUtils.SDPATH + Str + ".JPEG");
		}
		return list;
	}

	public void sendMyPublish() {

		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在发布中...");
		dialog.show();

		Map<String, String> map = new HashMap<String, String>();
		String content = et_content.getText().toString();
		map.put("memid", MyApplication.getInstance().getMemid());
		map.put("content", content);
		map.put("isprivate", isclose);
		map.put("istop", "1");

		SendMyPublishTask task = new SendMyPublishTask(getBaseContext(),
				Constant.URL_PUBLISH, getImgUrls(), map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				dialog.dismiss();
				if (data == null) {
					ToastCommom.getInstance().ToastShow(PublishActiviy.this,
							"服务器连接错误...");
					return;
				}
				int code = data.getInteger("msgFlag");
				if (code == 1) {
					ToastCommom.getInstance().ToastShow(PublishActiviy.this,
							data.getString("msgContent"));
					Bimp.drr.clear();
					Bimp.bmp.clear();
					Bimp.max = 0;
					Bimp.act_bool = true;
					FileUtils.deleteDir();
					finish();
				} else {
					ToastCommom.getInstance().ToastShow(PublishActiviy.this,
							"服务器端错误:" + data.getString("msgContent"));
				}
			}
		});

	}

}
