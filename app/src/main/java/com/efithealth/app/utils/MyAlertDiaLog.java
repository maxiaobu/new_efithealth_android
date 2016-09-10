package com.efithealth.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

public class MyAlertDiaLog extends AlertDialog{
	
	protected MyAlertDiaLog(Context context,String num) {
		super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		cancel();
		return true;
	}

}
