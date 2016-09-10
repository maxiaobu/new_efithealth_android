package com.efithealth.app.utils;

import com.efithealth.R;
import com.efithealth.app.Photo.ChoosePhotoActivity;
import com.efithealth.app.activity.ChatActivity;
import com.efithealth.app.activity.ExitConfimActivity;
import com.efithealth.app.activity.FindActivity;
import com.efithealth.app.activity.MainActivity;
import com.efithealth.app.activity.MemberCluMenubActivity;
import com.efithealth.app.activity.PublishActiviy;
import com.efithealth.app.activity.QRCodeActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BaseJsToAndroid {
	private Context context;

	public BaseJsToAndroid(Context context) {
		this.context = context;
	}

	/**
	 * 任何一页控制
	 * 
	 * @param fromPage
	 * @param toPage
	 * @param bottom
	 *            底栏
	 */
	@JavascriptInterface
	public void popNewWindow(String page) {
		Log.i("page", page);
		int endIndex=page.indexOf("?");
		String h5=page.substring(0, endIndex);
		if (page.indexOf("file:///android_asset/") < 0)
			page = "file:///android_asset/" + page;
		if (MainActivity.instance.currentPage == 601||h5.equals("dynReview.html")) {
			Intent intent = new Intent(context, FindActivity.class);
			intent.putExtra("url", page);
			context.startActivity(intent);
		} else {
			//TODO
			if (h5.equals("pay.html")) {
				MainActivity.instance.setTabSelection(505);
				SharedPreferencesUtils.setParam(context, "paypage", page);
			}else{
				MainActivity.instance.setTabWebViewSelection(page);
			}
		}
	}

	/**
	 * ?tarid=M000021&role=coach&tabpage=course&memid=M000003&memphoto=http://
	 * efithealthresource
	 * .oss-cn-beijing.aliyuncs.com/image/bmember/M000021_1461571239735_s
	 * .jpg&memnickname=羿健康男教练
	 * 
	 * 个人信息 "?tarid=" +memid +"&role="+memrole+"&memid="+memid
	 * 
	 * @param tarid
	 * @param memrole
	 * @param memid
	 */
	@JavascriptInterface
	public void memberIndex(String page) {
		if (page != null && page.length() > 0) {
			SharedPreferencesUtils.setParam(context, "page", page);
			Log.i("djy", page);
		}
//			Intent intent=new Intent(context,MemberCluMenubActivity.class);
//			intent.putExtra("url", page);
//			context.startActivity(intent);
		MainActivity.instance.setTabSelection(403);
	}

	/**
	 * 获得当前登录memid
	 * 
	 * @return
	 */
	@JavascriptInterface
	public String getmemid() {
		String memid = (String) SharedPreferencesUtils.getParam(context,
				"memid", "");
		return memid;
	}

	/**
	 * HTML 页面提示
	 * 
	 * @param content
	 */
	@JavascriptInterface
	public void alertInfo(String content) {
		ToastCommom.getInstance().ToastShow(context, content);
	}

	/**
	 * 提示信息
	 * 
	 * @param type
	 *            提示类型:success成功，error错误，warn警告, message(无文本)
	 * @param message
	 */
	@JavascriptInterface
	public void alertInfo(String type, String content) {
		ToastCommom.getInstance().ToastShow(context, content);
	}

	/**
	 * 聊天
	 * 
	 * @param memid
	 * @param nickname
	 * @param imgpath
	 */
	@JavascriptInterface
	public void toChat(String memid, String nickname, String imgpath) {
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("userId", memid.toLowerCase());
		intent.putExtra("nickname", nickname);
		intent.putExtra("headimg", imgpath);
		context.startActivity(intent);
	}

	/**
	 * 打电话
	 * 
	 * @param phoneNumber
	 *            电话号码
	 */
	@SuppressWarnings("deprecation")
	@JavascriptInterface
	public void callUp(final String phoneNumber) {
		MyAlertDiaLog builder = new MyAlertDiaLog(context, phoneNumber);
		builder.setMessage("您确定呼叫 " + phoneNumber + " 这个电话号码?\n");
		builder.setButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});

		builder.setButton2("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + phoneNumber));
				// 启动
				context.startActivity(phoneIntent);

			}
		});
		builder.show();

	}

	/**
	 * 退出当前登录
	 * 
	 * @param str
	 */
	@JavascriptInterface
	public void logout() {
		context.startActivity(new Intent(context, ExitConfimActivity.class));
	}

	/**
	 * 生成二维码
	 * 
	 * @param memid
	 */
	@JavascriptInterface
	public void getMyQrCode() {
		Intent intent = new Intent();
		intent.setClass(context, QRCodeActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 课程管理
	 * 
	 * @param memid
	 */
	@JavascriptInterface
	public void courseManager(String memid) {
		MainActivity.instance.setTabSelection(100);
	}

	/**
	 * 订单类型
	 */
	@JavascriptInterface
	public void getOrderList() {
		MainActivity.instance.setTabSelection(402);

	}
}
