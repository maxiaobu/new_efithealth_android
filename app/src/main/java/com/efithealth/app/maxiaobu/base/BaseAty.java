package com.efithealth.app.maxiaobu.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.DemoHXSDKHelper;
import com.efithealth.app.MyApplication;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.LoginActivity;
import com.efithealth.app.activity.LunchListActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.maxiaobu.utils.RequestManager;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by 马小布 on 2016/7/29.
 *
 * 全局变量application & context
 * 设置过渡动画
 * 清空网络请求线程
 *
 */
public class BaseAty extends AppCompatActivity {
    public Context myApplication;
    public AppCompatActivity mActivity;

    /**
     * 账号在别处登录
     */
    public boolean isConflict = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.applicationContext;
        mActivity = this;
//        setStatusColor();//沉浸式布局
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (MyApplication.getInstance().getMemid().equals("")) {
            android.app.AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
            builder.setTitle("重要提示");
            builder.setMessage("登录已经过期，请重新登录！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    showWarning();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    showWarning();
                }
            });
            builder.create();
            builder.show();
        }
    }

    /**
     * 设置返回toolbar
     *
     * @param toolbarCommon
     * @param title
     * @param titleString
     */
    public void setCommonBackToolBar(Toolbar toolbarCommon, TextView title, String titleString) {
        toolbarCommon.setTitle("");
        setSupportActionBar(toolbarCommon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(titleString);
    }

    /**
     * 重新显示登陆页面
     */
    public void showWarning(){
        MyApplication.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onError(int arg0, String arg1) {


            }

            @Override
            public void onProgress(int arg0, String arg1) {


            }

            @Override
            public void onSuccess() {
                FragmentHome.flag=true;
                // 重新显示登陆页面
                MainActivity.instance.finish();
                SharedPreferencesUtils.clearData(mActivity);
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }

        });
    }
}
