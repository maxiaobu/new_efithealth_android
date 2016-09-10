package com.efithealth.app.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


/*
 * 绘制圆角图片
 */
public class DrawableUtil {
	 public Bitmap toRoundCorner(Bitmap bitmap, int pixels) { 
		  Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
		  Canvas canvas = new Canvas(output); 
		  final Paint paint = new Paint(); 
		  final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		  final RectF rectF = new RectF(rect); 
		  final float roundPx = pixels; 
		  paint.setAntiAlias(true); 
		  canvas.drawARGB(0, 0, 0, 0); 
		  canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		  canvas.drawBitmap(bitmap, rect, rect, paint); 
		  return output; 
		  } 
		 public BitmapDrawable ImgToCorner(Context c,int id,int jd){
		  Drawable drawable = c.getResources().getDrawable(id); 
		  BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable; 
		  Bitmap bitmap = bitmapDrawable.getBitmap();
		  return new BitmapDrawable(c.getResources(),toRoundCorner(bitmap, jd)); 
		 }
}
