package com.efithealth.app.maxiaobu.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by 马小布 on 2016/8/1.
 */
public class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {
    public RefreshHeaderView(Context context) {
        super(context);
    }
    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onRefresh() {
        setText("正在拼命加载数据...");
    }
    @Override
    public void onPrepare() {
        setText("");
    }
    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                setText("释放刷新");
            } else {
                setText("下拉刷新");
            }
        } else {
            setText("下拉刷新");
        }
    }
    @Override
    public void onRelease() {
    }
    @Override
    public void onComplete() {
        setText("刷新成功");
    }
    @Override
    public void onReset() {
        setText("");
    }
}
