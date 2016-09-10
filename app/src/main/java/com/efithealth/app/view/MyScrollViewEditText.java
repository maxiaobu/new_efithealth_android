package com.efithealth.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollViewEditText extends ScrollView {

	public MyScrollViewEditText(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	
	
	
	public MyScrollViewEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}




	public MyScrollViewEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}




	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO 自动生成的方法存根
	  //super.onInterceptTouchEvent(ev);
		return false;
	}

}
