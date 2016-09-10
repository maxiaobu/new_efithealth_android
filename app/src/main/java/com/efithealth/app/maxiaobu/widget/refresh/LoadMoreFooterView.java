package com.efithealth.app.maxiaobu.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by 马小布 on 2016/8/1.
 */
public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }
    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onLoadMore() {
        setText("正在拼命加载数据...");
    }
    @Override
    public void onPrepare() {
        setText("");
    }
    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText("释放加载");
            } else {
                setText("上拉加载");
            }
        } else {
            setText("上拉加载");
        }
    }
    @Override
    public void onRelease() {
        setText("正在拼命加载数据...");
    }
    @Override
    public void onComplete() {
        setText("加载完成");
    }
    @Override
    public void onReset() {
        setText("");
    }
}
