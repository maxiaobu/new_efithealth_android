package com.efithealth.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class MyScrollViewWeb extends ScrollView {

	public MyScrollViewWeb(Context context) {
		this(context, null);

	}

	public MyScrollViewWeb(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public MyScrollViewWeb(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public interface OnScrollChangedListener {
		public void onScrollChanged(int x, int y, int oldX, int oldY);
	}

	private OnScrollChangedListener onScrollChangedListener;

	public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
		this.onScrollChangedListener = onScrollChangedListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
		super.onScrollChanged(x, y, oldX, oldY);
		if (onScrollChangedListener != null) {
			onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
		}
	}
}
