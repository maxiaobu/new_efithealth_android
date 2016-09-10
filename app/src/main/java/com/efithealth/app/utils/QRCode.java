package com.efithealth.app.utils;

import java.util.Hashtable;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCode {
	Bitmap bitmap;
	public Bitmap createImage(List list) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();

            String text = "111111";

            Log.i("222", "生成的文本：" + text);
//            if (text == null || "".equals(text) || text.length() < 1) {
//                return;
//            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    50, 30);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, 50, 30, hints);
            int[] pixels = new int[50 * 30];
            for (int y = 0; y < 30; y++) {
                for (int x = 0; x < 50; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * 50 + x] = 0xff000000;
                    } else {
                        pixels[y * 50 + x] = 0xffffffff;
                    }

                }
            }

            bitmap = Bitmap.createBitmap(50, 30,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, 50, 0, 0, 50, 30);
//            qr_image.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
