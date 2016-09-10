/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.efithealth.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.EMCallBack;
import com.easemob.chat.EMGroupManager;
import com.efithealth.R;
import com.efithealth.app.activity.FragmentHome;
import com.efithealth.app.activity.LoginActivity;
import com.efithealth.app.domain.User;
import com.efithealth.app.entity.MCourseList;
import com.efithealth.app.utils.CircleDisplayer;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.applib.controller.HXSDKHelper;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.utils.ToastCommom;
import com.efithealth.app.utils.UserUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.WindowManager;

public class MyApplication extends MultiDexApplication {

	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	public LocationClient mLocationClient;
	public BDLocationListener myListener = new MyLocationListener();
	public double mLatitude;//纬度
	public double mLongitude;//经度
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public Boolean newCourseFlag = false;// true 为发布课程 清空 MainActivity
	// 对应的fragment=null 移除从新添加

	public Boolean upDateIssueCourseFragment = false;// 从课程管理页面里单击ListView Item
	// 过来的

	public Boolean editCourseFlag = false; // true 编程课程 false 发布课程

	public Boolean fragmentHistoryFlag = false;// true 课程管理页中显示历史课程页

	public int coursePosition = -1;// 课程位置

	public MCourseList mCourseList;
	public MCourseList mHistoryCourseList;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;
		hxSDKHelper.onInit(applicationContext);
		initImage();
		mLocationClient = new LocationClient(instance); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
		mLocationClient.start();

	}
	
	

	public static MyApplication getInstance() {
		return instance;
	}
	

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param username
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM, final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(isGCM, emCallBack);
	}
	
	public boolean isConnect(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;

	}

	public void initImage() {

		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
				// 设置图片下载期间显示的图
				.showImageForEmptyUri(R.drawable.ic_empty)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error)
				// 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图
				.build(); // 创建配置过得DisplayImageOption对象

		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(8)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // 缓存的文件数量
				.discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout
																										// (5
																										// s),
																										// readTimeout
																										// (30
																										// s)超时时间
				/*.writeDebugLogs() // Remove for release app
*/				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions initHomeDisImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty_index)
				// 设置图片下载期间显示的图
				.showImageForEmptyUri(R.drawable.empty_index)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.empty_index)
				// 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(15)) // 设置成圆角图
				.build(); // 创建配置过得DisplayImageOption对象

		return options;
	}
	public DisplayImageOptions initHomeDisImgOption1() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty_index)
				// 设置图片下载期间显示的图
				.showImageForEmptyUri(R.drawable.empty_index)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.empty_index)
				// 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(50)) // 设置成圆角图
				.build(); // 创建配置过得DisplayImageOption对象

		return options;
	}

	public DisplayImageOptions initHeadDisImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_avatar)
				// 设置图片下载期间显示的图
				.showImageForEmptyUri(R.drawable.default_avatar)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_avatar)
				// 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new CircleDisplayer()) // 设置成圆角图
				.build(); // 创建配置过得DisplayImageOption对象

		return options;
	}

	public DisplayImageOptions initPicDisImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty_photo)
				// 设置图片下载期间显示的图
				.showImageForEmptyUri(R.drawable.empty_photo)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.empty_photo)
				// 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(5)) // 设置成圆角图
				.build(); // 创建配置过得DisplayImageOption对象

		return options;
	}

	public String getMemid() {
		return (String) SharedPreferencesUtils.getParam(this, "memid", "");
	}

	public String getNickname() {
		return (String) SharedPreferencesUtils.getParam(this, "nickname", "");
	}

	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public Map<String, String> getMyInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nickname", (String) SharedPreferencesUtils.getParam(instance, "nickname", ""));
		map.put("brithday", (String) SharedPreferencesUtils.getParam(instance, "brithday", ""));
		map.put("sex", (String) SharedPreferencesUtils.getParam(instance, "sex", ""));
		map.put("mysign", (String) SharedPreferencesUtils.getParam(instance, "mysign", ""));
		map.put("addressname", (String) SharedPreferencesUtils.getParam(instance, "addressname", ""));
		map.put("addresphone", (String) SharedPreferencesUtils.getParam(instance, "addresphone", ""));
		map.put("address", (String) SharedPreferencesUtils.getParam(instance, "address", ""));
		map.put("headImgUrl", (String) SharedPreferencesUtils.getParam(instance, "headImgUrl", ""));
		map.put("memrole", (String) SharedPreferencesUtils.getParam(instance, "memrole", "mem"));

		return map;

	}

	/**
	 * 同步服务器个人信息
	 */
	public void update_local_myinfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("memid", getMemid()); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(this, Constant.URL_MYINFO, map);
		task.getData(new DataCallBack() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onDataCallBack(com.alibaba.fastjson.JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(getBaseContext(), "同步服务器失败！");
				} else {
					if (data.getString("msgFlag").equals("1")) {
						com.alibaba.fastjson.JSONObject memberobj = data.getJSONObject("member");
						String nickname = memberobj.getString("nickname");
						String brithday = memberobj.getString("birth").toString();
						String sex = memberobj.getString("gender");
						String mysign = memberobj.getString("signature");
						String addressname = memberobj.getString("recname");
						String addresphone = memberobj.getString("recphone");
						String address = memberobj.getString("recaddress");
						String memrole = memberobj.getString("memrole");
						String headpage =memberobj.getString("headpage");
						String headImgUrl = Constant.URL_RESOURCE + memberobj.getString("imgpfile");
						SharedPreferencesUtils.setParam(getBaseContext(), "nickname", nickname);
						SharedPreferencesUtils.setParam(getBaseContext(), "brithday", brithday);
						SharedPreferencesUtils.setParam(getBaseContext(), "sex", sex);
						SharedPreferencesUtils.setParam(getBaseContext(), "mysign", mysign);
						SharedPreferencesUtils.setParam(getBaseContext(), "addressname", addressname);
						SharedPreferencesUtils.setParam(getBaseContext(), "addresphone", addresphone);
						SharedPreferencesUtils.setParam(getBaseContext(), "address", address);
						SharedPreferencesUtils.setParam(getBaseContext(), "headImgUrl", headImgUrl);
						SharedPreferencesUtils.setParam(getBaseContext(), "memrole", memrole);
						SharedPreferencesUtils.setParam(getBaseContext(), "headpage", headpage);

						UserUtils.updateCurrentUserNick(nickname);
						UserUtils.updateCurrentUserAvatar(headImgUrl);
						JSONArray imglist_top = data.getJSONArray("istopfile");
						String topImglist = "";
						String topidlist = "";
						for (int i = 0; i < imglist_top.size(); i++) {
							topImglist += imglist_top.getJSONObject(i).getString("imgpfilename");
							topidlist += imglist_top.getJSONObject(i).getString("medid");
							if (i != imglist_top.size() - 1) {
								topImglist += ",";
								topidlist += ",";
							}
						}
						String moreImglist = "";
						String moreidlist = "";
						JSONArray imglist_more = data.getJSONArray("imagefile");
						for (int i = 0; i < imglist_more.size(); i++) {
							moreImglist += imglist_more.getJSONObject(i).getString("imgpfilename");
							moreidlist += imglist_more.getJSONObject(i).getString("medid");
							if (i != imglist_more.size() - 1) {
								moreImglist += ",";
								moreidlist += ",";
							}
						}

						SharedPreferencesUtils.setParam(getBaseContext(), "topImgList", topImglist);
						SharedPreferencesUtils.setParam(getBaseContext(), "moreImgList", moreImglist);
						SharedPreferencesUtils.setParam(getBaseContext(), "topidlist", topidlist);
						SharedPreferencesUtils.setParam(getBaseContext(), "moreidlist", moreidlist);
						Log.e("MyApplication", "照片墙信息更新成功");

						Log.e("MyApplication", "个人信息更新成功");

					} else {
						ToastCommom.getInstance().ToastShow(getBaseContext(), "服务器连接不稳定！");
					}
				}

			}
		});

	}

	/**
	 * 同步服务器照片墙
	 */
	public void update_loacl_imgWall() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(this, Constant.URL_IMGWALL, map);
		task.getData(new com.efithealth.app.utils.LoadDataFromServer.DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data == null) {
					ToastCommom.getInstance().ToastShow(getBaseContext(), "同步服务器失败！");
				} else {
					if (data.getString("msgFlag").equals("1")) {
						JSONArray imglist_top = data.getJSONArray("istopfile");
						String topImglist = "";
						String topidlist = "";
						for (int i = 0; i < imglist_top.size(); i++) {
							topImglist += imglist_top.getJSONObject(i).getString("imgpfilename");
							topidlist += imglist_top.getJSONObject(i).getString("medid");
							if (i != imglist_top.size() - 1) {
								topImglist += ",";
								topidlist += ",";
							}
						}
						String moreImglist = "";
						String moreidlist = "";
						JSONArray imglist_more = data.getJSONArray("imagefile");
						for (int i = 0; i < imglist_more.size(); i++) {
							moreImglist += imglist_more.getJSONObject(i).getString("imgpfilename");
							moreidlist += imglist_more.getJSONObject(i).getString("medid");
							if (i != imglist_more.size() - 1) {
								moreImglist += ",";
								moreidlist += ",";
							}
						}

						SharedPreferencesUtils.setParam(getBaseContext(), "topImgList", topImglist);
						SharedPreferencesUtils.setParam(getBaseContext(), "moreImgList", moreImglist);
						SharedPreferencesUtils.setParam(getBaseContext(), "topidlist", topidlist);
						SharedPreferencesUtils.setParam(getBaseContext(), "moreidlist", moreidlist);
						
						Log.e("MyApplication", "照片墙信息更新成功");
					} else {
						ToastCommom.getInstance().ToastShow(getBaseContext(), "服务器连接不稳定！");
					}
				}
			}
		});

	}

	public Map<String, String> getImgWallInfo() {
		Map<String, String> map = new HashMap<>();
		map.put("topImgList", (String) SharedPreferencesUtils.getParam(getBaseContext(), "topImgList", ""));
		map.put("topidList", (String) SharedPreferencesUtils.getParam(getBaseContext(), "topidlist", ""));
		map.put("moreImgList", (String) SharedPreferencesUtils.getParam(getBaseContext(), "moreImgList", ""));
		map.put("moreidList", (String) SharedPreferencesUtils.getParam(getBaseContext(), "moreidlist", ""));
		return map;
	}

	// 异步更新好友信息
	public void update_loacl_friend() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(this, Constant.URL_FRIEND, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				

				if (data == null) {
					ToastCommom.getInstance().ToastShow(getBaseContext(), "同步服务器失败！");
				} else {
					try {
						if (data.getString("msgFlag").equals("1")) {
							Log.i("djy", data.toJSONString());
							JSONArray jsonfriends = data.getJSONArray("friendslist");
							
					
							
							Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
							
							for (int i = 0; i < jsonfriends.size(); i++) {
								String f_memeid = jsonfriends.getJSONObject(i).getString("memid").toLowerCase();
								String f_nickname = jsonfriends.getJSONObject(i).getString("nickname");
								String f_headimg = jsonfriends.getJSONObject(i).getString("imgsfile");
								User f_user = UserUtils.getUserInfo(f_memeid);
								f_user.setAvatar(f_headimg);
								f_user.setNick(f_nickname);
								localUsers.put(f_memeid, f_user);			
							

							}
							UserUtils.updateUserInfo(localUsers);
							Log.e("MyApplication", "好友信息更新成功");
						} else {
							ToastCommom.getInstance().ToastShow(getBaseContext(), "服务器连接不稳定！");
						}
					} catch (Exception e) {
					
					}
					
				}

			}
		});
	}

	// 异步获取首页信息
	public void update_loacl_indexdata() {
		Map<String, String> map = new HashMap<String, String>();
		String jingdu = (String) SharedPreferencesUtils.getParam(instance, "jingdu", "");
		String weidu = (String) SharedPreferencesUtils.getParam(instance, "weidu", "");

		map.put("longitude", jingdu);
		map.put("latitude", weidu);
		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(this, Constant.URL_HOME, map);

		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				try {

					if (data.getString("msgFlag").equals("1")) {
						JSONArray small_imgArry = data.getJSONArray("pageHImageList");
						String samll_img_url_1 = small_imgArry.getJSONObject(0).getString("imgpfile");
						String samll_img_url_2 = small_imgArry.getJSONObject(1).getString("imgpfile");
						String samll_img_url_3 = small_imgArry.getJSONObject(2).getString("imgpfile");
						String samll_img_url_4 = small_imgArry.getJSONObject(3).getString("imgpfile");
						
						SharedPreferencesUtils.setParam(instance, "samll_img_url_1", samll_img_url_1);
						SharedPreferencesUtils.setParam(instance, "samll_img_url_2", samll_img_url_2);
						SharedPreferencesUtils.setParam(instance, "samll_img_url_3", samll_img_url_3);
						SharedPreferencesUtils.setParam(instance, "samll_img_url_4", samll_img_url_4);
						
						JSONArray big_imgArry = data.getJSONArray("pageVImageList");
						String big_img = "";
						for (int i = 0; i < big_imgArry.size(); i++) {
							big_img += big_imgArry.getJSONObject(i).getString("imgpfile");
							if (i != big_imgArry.size() - 1) {
								big_img += ",";
							}
						}
						SharedPreferencesUtils.setParam(instance, "big_img_url", big_img);
						
					} else {

					}
				} catch (Exception e) {

				}

			}
		});

	}

	//// 百度MAP///////////////////////////////////////////////////////////////////////
	// 设置百度MAP 定位相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setAddrType("all");
		option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到地址
//		option.setOpenGps(true);// 打开GPS
		
		//默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.setIgnoreKillProcess(false);
		option.setScanSpan(60000);// 多长时间进行一次请求       option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
		mLocationClient.setLocOption(option);// 使用设置    
	}

	/**
	 * Description:百度MAP 定位成功回调接口方法
	 *
	 * @author Xushd
	 * @since 2016年2月20日上午11:14:36
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			SharedPreferencesUtils.setParam(instance, "jingdu", location.getLongitude() + "");
			SharedPreferencesUtils.setParam(instance, "weidu", location.getLatitude() + "");
			SharedPreferencesUtils.setParam(instance, "city", location.getCity());
			
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			String local_city = location.getCity();
			Log.i("myapp","beyond");
//			mLocationClient.stop();
			if (local_city != null) {
				String cityname = local_city.substring(0, local_city.length() - 1).toString();
				SharedPreferencesUtils.setParam(instance, "cityname", cityname);
			} 
			else {
				SharedPreferencesUtils.setParam(instance, "cityname",getString(R.string.localcity_default));				
			}
			

		}

		public void onReceivePoi(BDLocation location) {

		}
	}

	
	
	
	
}
