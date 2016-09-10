package com.efithealth.app.utils;

import com.efithealth.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastCommom {
	
	private static ToastCommom toastCommom;

	public static ToastCommom getInstance() {
		if (toastCommom == null) {
			toastCommom = new ToastCommom();
		}
		return toastCommom;
	}

	private Toast toast;

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param root
	 * @param tvString
	 */

	public void ToastShow(Context context, String tvString) {
		View layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
		TextView text = (TextView) layout.findViewById(R.id.text);

		text.setText(tvString);
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 500);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}
