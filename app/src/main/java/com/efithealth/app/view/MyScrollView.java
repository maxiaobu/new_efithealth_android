package com.efithealth.app.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class MyScrollView extends ScrollView {


    private OnScrollListener onScrollListener;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;


    private int centre;

    public MyScrollView(Context context) {
        this(context, null);


    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public int getCentre() {
        return centre;
    }

    public void setCentre(int centre) {
        this.centre = centre;
    }

    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {

        this.onScrollListener = onScrollListener;
    }


    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }

        }

        ;

    };

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float downY = 0;
        float upY = 0;

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                upY = ev.getY();

                if (!(this.getMaxScrollAmount() <= this.getScrollY())) {
                    Log.d("gzq", "this.getScrollY()" + this.getScrollY());
                    Log.d("gzq", "" + centre);
                    if (this.getScrollY() < centre) {

                            this.scrollTo(0, 0);
                            Log.d("gzq", "0, 0=" + centre);

                    } else if (this.getScrollY() >= centre) {

                           this.scrollTo(0, centre*2);
                           Log.d("gzq", "0, centre*2=" + centre);


                    }

                }


                break;
        }


        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_UP:
//                handler.sendMessageDelayed(handler.obtainMessage(), 5);
//                break;
//        }
        return super.onTouchEvent(ev);
    }


    /**
     * 滚动的回调接口
     *
     * @author xiaanming
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         *
         * @param scrollY 、
         */
        public void onScroll(int scrollY);
    }
}
