package com.efithealth.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.efithealth.app.utils.CircleDisplayer;
import com.efithealth.app.utils.QRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeActivity extends BaseActivity {
	ImageView image, qrback, title_img, down_img;
	QRCode qrcode;
	Bitmap bitmap;
	private String memid, str;
	DisplayImageOptions options;

	private int width = 600;
	private int height = 600;
	private String groupid = "", clubid = "";
	private boolean flag = true;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle arg0) {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(arg0);
		setContentView(R.layout.activity_qrcode);
		image = (ImageView) findViewById(R.id.qr);
		qrback = (ImageView) findViewById(R.id.back);
		title_img = (ImageView) findViewById(R.id.title_img);
		down_img = (ImageView) findViewById(R.id.down_img);
		
		qrback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		TextView username = (TextView) findViewById(R.id.username);
		
		String str=getIntent().getStringExtra("yes");
		if (str.equals("1")) {
			flag = false;
			groupid = getIntent().getStringExtra("groupid");
			clubid = getIntent().getStringExtra("clubid");
			String img = getIntent().getStringExtra("img");
			String name = getIntent().getStringExtra("name");
			ImageLoader.getInstance().displayImage(img, title_img,
					MyApplication.getInstance().initHeadDisImgOption());
			username.setText(name);
		} else {
			flag = true;
			Map<String, String> map = MyApplication.getInstance().getMyInfo();
			String head_imagePath = map.get("headImgUrl");
			ImageLoader.getInstance().displayImage(head_imagePath, title_img,
					MyApplication.getInstance().initHeadDisImgOption());
			username.setText(map.get("nickname"));
		}
		createImage();
	}

	public void createImage() {
		try {
			String text = "";
			// 需要引入core包
			QRCodeWriter writer = new QRCodeWriter();
			if (flag) {
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				str = formatter.format(curDate);
				memid = MyApplication.getInstance().getMemid();
				
				text = "{\"memid\":\"" + memid + "\",\"curDate\":\"" + str
						+ "\"}";
				Log.i("222", "生成的文本：" + text);
			} else {
				text = "{\"groupid\":\"" + groupid + "\",\"clubid\":\"" + clubid
						+ "\"}";
				Log.i("333", "生成的文本：" + text);
			}

			if (text == null || "".equals(text) || text.length() < 1) {
				return;
			}
			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
					width, height);
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}

				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			bitmap = Bitmap
					.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			// 显示到一个ImageView上面
			image.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}

	}

	public void qrcodedown(View v) {
		BitmapDrawable bmpDrawable = (BitmapDrawable) image.getDrawable();
		Bitmap bmp = bmpDrawable.getBitmap();
		if (bmp != null) {
			try {
				ContentResolver cr = getContentResolver();
				String url = MediaStore.Images.Media.insertImage(cr, bmp,
						String.valueOf(System.currentTimeMillis()), "");
				Toast.makeText(this, "已保存到系统相册！", Toast.LENGTH_SHORT).show();

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse("file://"
								+ Environment.getExternalStorageDirectory())));

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "我的二维码保存失败！", Toast.LENGTH_SHORT).show();
		}

	}
}
