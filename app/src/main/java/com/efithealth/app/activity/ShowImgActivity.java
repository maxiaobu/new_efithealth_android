package com.efithealth.app.activity;

import com.efithealth.R;
import com.efithealth.app.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ShowImgActivity extends Activity {

	public ImageView imgview;
	public String img_path;
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimg);
		imgview = (ImageView) findViewById(R.id.image);	
		img_path = getIntent().getExtras().getString("imgpath");
		
		showImg();
	}

	public void click(View view){
		switch (view.getId()) {
		case R.id.image:
			finish();
			break;

		default:
			break;
		}
	}
	public void showImg(){
		if(!img_path.contains("http://")){
			Bitmap bm=BitmapFactory.decodeFile(img_path);
			imgview.setImageBitmap(bm);
		}else{
			ImageLoader.getInstance().displayImage(img_path, imgview, MyApplication.getInstance().initPicDisImgOption());
		}				
	}
	
}
