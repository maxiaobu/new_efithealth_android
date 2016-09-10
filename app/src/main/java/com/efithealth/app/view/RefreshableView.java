package com.efithealth.app.view;

//fragmenthome  下拉刷新
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.efithealth.R;

/**
 * 刷新控制view
 * 
 * @author Nono
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class RefreshableView extends LinearLayout {

    private static final String TAG = "LILITH";
    private Scroller scroller;
    private View refreshView;
    private ImageView refreshIndicatorView;
    private int refreshTargetTop = -200;
    private ProgressBar bar;
    private TextView downTextView;
    private TextView timeTextView;
    private LinearLayout reFreshTimeLayout;//显示上次刷新时间的layout
    private RefreshListener refreshListener;

    private int lastY;
    // 拉动标记
    private boolean isDragging = false;
//    Calendar LastRefreshTime;
    
    private Context mContext;
    public RefreshableView(Context context) {
        super(context);
        mContext = context;
        
    }
    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        
    }
    @SuppressWarnings("deprecation")
	private void init() {
    	
      
        //滑动对象，
//        LastRefreshTime=Calendar.getInstance();
        scroller = new Scroller(mContext);
        
        //刷新视图顶端的的view
         refreshView = LayoutInflater.from(mContext).inflate(R.layout.refresh_top_item, null);
        //指示器view
         refreshIndicatorView = (ImageView) refreshView.findViewById(R.id.indicator);
        //刷新bar
        bar = (ProgressBar) refreshView.findViewById(R.id.progress);
        //下拉显示text
         downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
         
        //下来显示时间
         timeTextView = (TextView) refreshView.findViewById(R.id.refresh_time);
         reFreshTimeLayout=(LinearLayout)refreshView.findViewById(R.id.refresh_time_layout);
         
         setTimeSet();
         
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, -refreshTargetTop);
        lp.topMargin = refreshTargetTop;
        lp.gravity = Gravity.CENTER;
        addView(refreshView, lp);
        downTextView.setText("下拉可刷新");
    }
    
    @SuppressLint("SimpleDateFormat")
	private void setTimeSet(){
    	long time=System.currentTimeMillis();  
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd  HH:mm");  
        Date d1=new Date(time);  
        String t1=format.format(d1);  

         timeTextView.setText(t1);
    }



    /**
     * 设置上次刷新时间
     * @param
     */
    private void setLastRefreshTimeText() {
        reFreshTimeLayout.setVisibility(View.VISIBLE);


    }


    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        int y= (int) event.getRawY();
        
        
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            //记录下y坐标
            lastY = y;
            break;

        case MotionEvent.ACTION_MOVE:
            Log.i(TAG, "ACTION_MOVE");
            //y移动坐标
            int m = y - lastY;
            if(((m < 6) && (m > -1)) || (!isDragging )){
                setLastRefreshTimeText();
                 doMovement(m);
            }
            //记录下此刻y坐标
            this.lastY = y;
            break;
            
        case MotionEvent.ACTION_UP:
            Log.i(TAG, "ACTION_UP");
            
            fling();
            
            break;
        }
        return true;
    }


    /**
     * up事件处理
     */
    private void fling() {
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        Log.i(TAG, "fling()" + lp.topMargin);
        if(lp.topMargin > 0){//拉到了触发可刷新事件
            refresh();  
        }else{
            returnInitState();
        }
    }
    

    
    private void returnInitState() {
         LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
         int i = lp.topMargin;
         scroller.startScroll(0, i, 0, refreshTargetTop);
         invalidate();
    }
    private void refresh() {
         LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
         int i = lp.topMargin;
         reFreshTimeLayout.setVisibility(View.GONE);
         refreshIndicatorView.setVisibility(View.GONE);
         refreshIndicatorView.clearAnimation();
         bar.setVisibility(View.VISIBLE);
         timeTextView.setVisibility(View.GONE);
         downTextView.setVisibility(View.GONE);
         scroller.startScroll(0, i, 0, 0-i);
         setTimeSet();
         invalidate();
         if(refreshListener !=null){
             refreshListener.onRefresh(this);
         }
    }
    
    /**
     * 
     */
    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            int i = this.scroller.getCurrY();
              LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
              int k = Math.max(i, refreshTargetTop);
              lp.topMargin = k;
              this.refreshView.setLayoutParams(lp);
              this.refreshView.invalidate();
              invalidate();
        }
    }
    /**
     * 下拉move事件处理
     * @param moveY
     */
    private void doMovement(int moveY) {
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        if(moveY>0){
            //获取view的上边距
            float f1 =lp.topMargin;
            float f2 = moveY * 0.4F;
            int i = (int)(f1+f2);
            //修改上边距
            lp.topMargin = i;
            //修改后刷新
            refreshView.setLayoutParams(lp);
            refreshView.invalidate();
            invalidate();
        }
        else 
        {
            float f1 =lp.topMargin;
            int i=(int)(f1+moveY*0.9F);
            Log.i("aa", String.valueOf(i));
            if(i>=refreshTargetTop)
            {
                lp.topMargin = i;
                //修改后刷新
                refreshView.setLayoutParams(lp);
                refreshView.invalidate();
                invalidate();
            }
            else 
            {
                
            }
        }
        
        timeTextView.setVisibility(View.VISIBLE);
        downTextView.setVisibility(View.VISIBLE);
        
        refreshIndicatorView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);

        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation animation=new RotateAnimation(0f,180f,Animation.RELATIVE_TO_SELF, 0.5f

                                                            ,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(5000);
        animation.setFillAfter(true);

        RotateAnimation animation1=new RotateAnimation(180f,360f,Animation.RELATIVE_TO_SELF, 0.5f

                ,Animation.RELATIVE_TO_SELF,0.5f);
        animation1.setDuration(5000);
        animation1.setFillAfter(true);

        if(lp.topMargin >  0){
            downTextView.setText("松开可刷新");
            animationSet.addAnimation(animation1);
            refreshIndicatorView.startAnimation(animationSet);
        }else{
            downTextView.setText("下拉可刷新");
            animationSet.addAnimation(animation);
            refreshIndicatorView.startAnimation(animationSet);
        }
            
    }

    public void setRefreshEnabled(boolean b) {
    }

    public void setRefreshListener(RefreshListener listener) {
        this.refreshListener = listener;
    }


    /**
     * 结束刷新事件
     */
    public void finishRefresh(){
        Log.i(TAG, "执行了=====finishRefresh");
         LayoutParams lp= (LayoutParams)this.refreshView.getLayoutParams();
            int i = lp.topMargin;
            refreshIndicatorView.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.VISIBLE);
            scroller.startScroll(0, i, 0, refreshTargetTop);
            invalidate();
//            LastRefreshTime=Calendar.getInstance();
    }

    
    /*该方法一般和ontouchEvent 一起用
     * (non-Javadoc)
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        int y= (int) e.getRawY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            lastY = y;
            break;

        case MotionEvent.ACTION_MOVE:
            //y移动坐标
            int m = y - lastY;

            //记录下此刻y坐标
            this.lastY = y;
             if(m > 6 &&  canScroll()){
                 return true;
             }
            break;
        case MotionEvent.ACTION_UP:
            
            break;
            
    case MotionEvent.ACTION_CANCEL:
            
            break;
        }
        return false;
    }
    private boolean canScroll() {
        View childView;
        if(getChildCount()>1){
            childView = this.getChildAt(1);
            if(childView instanceof ListView){
                int top =((ListView)childView).getChildAt(0).getTop(); 
                int pad =((ListView)childView).getListPaddingTop(); 
                if((Math.abs(top-pad)) < 3&&
                        ((ListView) childView).getFirstVisiblePosition() == 0){
                    return true;
                }else{
                    return false;
                }
            }else if(childView instanceof ScrollView){
                if(((ScrollView)childView).getScrollY() == 0){
                    return true;
                }else{
                    return false;
                }
            }
            
        }
        return false;
    }
    /**
     * 刷新监听接口
     * @author Nono
     *
     */
    public interface RefreshListener{
        public void onRefresh(RefreshableView view);
    }


}
