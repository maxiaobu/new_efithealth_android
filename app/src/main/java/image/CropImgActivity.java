package image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.efithealth.R;
import com.efithealth.app.activity.BaseActivity;

public class CropImgActivity extends BaseActivity implements OnClickListener {
	private CropImageView iv;
	private String name = "";
	private Button commit_btn, canle_btn;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_crop_img);
		iv = (CropImageView) findViewById(R.id.cropimage);
		commit_btn = (Button) findViewById(R.id.crop_cut_commit);
		canle_btn = (Button) findViewById(R.id.crop_cut_canle);
		commit_btn.setOnClickListener(this);
		canle_btn.setOnClickListener(this);
		initData();

	}

	
	@SuppressWarnings("deprecation")
	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			String path = intent.getStringExtra("path");
			name = intent.getStringExtra("name");
			String flag = intent.getStringExtra("flag");
			Drawable drawable = null;
			if (flag.equals("0")) {
				drawable=createScaledBitmap(Bimp.bmp.get(0));
			}else{
				drawable = new BitmapDrawable(Bimp.bmp.get(0));
			}
			iv.setDrawable(drawable, 200, 200);
		} else {
			Toast.makeText(this, "图片路径是错的,找不到图片", 0).show();
			finish();
		}

	}
	
	
	//拍照的照片不能全屏显示，需要做以下处理
	@SuppressWarnings("deprecation")
	private Drawable createScaledBitmap(Bitmap bitmap){  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int width = dm.widthPixels+1;  
        int height = dm.heightPixels+1;  
        int t_width;  
        int t_height;  
        if (bitmap.getWidth()>width || bitmap.getHeight()>height){  
            t_width = width;  
            t_height = bitmap.getHeight()*width/bitmap.getWidth();  
            if (t_height>height){  
                t_width = t_width*height/t_height;  
                t_height = height;  
            }  
        } else  
        if (bitmap.getWidth()<width && bitmap.getHeight()<height){  
            t_width = width;  
            t_height = bitmap.getHeight()*width/bitmap.getWidth();  
            if (t_height>height){  
                t_width = t_width*height/t_height;  
                t_height = height;  
            }  
        } else {  
            t_width = bitmap.getWidth();  
            t_height = bitmap.getHeight();  
        }  
        bitmap = Bitmap.createScaledBitmap(bitmap, t_width, t_height, true);
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }  

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.crop_cut_commit:
			// 调用该方法得到剪裁好的图片
			Bitmap mBitmap = iv.getCropImage();
			if (mBitmap != null) {
				String newStr = name.substring(name.lastIndexOf("/") + 1,
						name.lastIndexOf("."));
				FileUtils.saveBitmap(mBitmap, newStr);
				setResult(RESULT_OK, null);
			} else {
				Toast.makeText(this, "剪切失败", 0).show();
			}
			mBitmap.recycle();
			finish();
			break;
		case R.id.crop_cut_canle:
			finish();
			break;

		}

	}

	@Override
	protected void onDestroy() {
		if (Bimp.bmp.size() > 0) {
			Bimp.bmp.get(0).recycle();
		}
		super.onDestroy();
	}

}
