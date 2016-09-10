package com.efithealth.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efithealth.R;
import com.efithealth.app.Constant;
import com.efithealth.app.MyApplication;
import com.efithealth.app.fragment.BaseFragment;
import com.efithealth.app.utils.HttpWeatherUtil;
import com.efithealth.app.utils.LoadDataFromServer;
import com.efithealth.app.utils.LoadDataFromServer.DataCallBack;
import com.efithealth.app.utils.DrawableUtil;
import com.efithealth.app.utils.LoadingDialog;
import com.efithealth.app.utils.SharedPreferencesUtils;
import com.efithealth.app.view.RefreshableView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页
 * 
 * @author star
 * 
 */
public class FragmentHome extends BaseFragment implements
		RefreshableView.RefreshListener {
	public static boolean flag = true;
	protected static final String TAG = "FragmentHome";
	public View view;
	private ImageView smallId1, smallId2, smallId3, smallId4, img_more1;
	private ArrayList<TextView> radioArray = new ArrayList<TextView>();
	private View today_view, tom_view, aftom_view;
	private ViewPager pager;
	// loading加载动画
	private LoadingDialog loading_dialog;
	private RelativeLayout rl;
	private RefreshableView mRefreshableView;
	private LinearLayout ttId, qrCode;
	private ImageView home1, home2, home3, home4, home5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null)
			view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	@SuppressLint("NewApi")
	private void changeColor(int n) {
		for (int i = 0; i < radioArray.size(); i++) {
			if (i == n) {
				radioArray.get(i).setTextColor(
						getResources().getColor(R.color.brown));
				radioArray.get(i)
						.setBackground(
								getResources().getDrawable(
										R.drawable.button_big_shape));
			} else {
				radioArray.get(i).setTextColor(
						getResources().getColor(R.color.gray));
				radioArray.get(i).setBackgroundColor(
						getResources().getColor(R.color.transparent));
				radioArray.get(i).setTextColor(Color.parseColor("#000000"));
			}
		}
	}

	// 实现下拉刷新RefreshListener 中方法
	public void onRefresh(RefreshableView view) {
		// 处理下拉刷新
		update_loacl_indexdata();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;

		mRefreshableView = (RefreshableView) view
				.findViewById(R.id.refresh_root);
		mRefreshableView.setRefreshListener(this);
		ttId = (LinearLayout) view.findViewById(R.id.ttId);
		// 给背景设置圆角图片
		BitmapDrawable bbb = new DrawableUtil().ImgToCorner(getActivity(),
				R.drawable.daoda, 15);// 依次传入activity,图片ID,角度
		ttId.setBackgroundDrawable(bbb);
		radioArray.add((TextView) view.findViewById(R.id.t1));
		radioArray.add((TextView) view.findViewById(R.id.t2));
		radioArray.add((TextView) view.findViewById(R.id.t3));

		radioArray.get(0).setOnClickListener(new OnViewClickListener());
		radioArray.get(1).setOnClickListener(new OnViewClickListener());
		radioArray.get(2).setOnClickListener(new OnViewClickListener());

		changeColor(0);

		smallId1 = (ImageView) view.findViewById(R.id.smallId1);
		smallId2 = (ImageView) view.findViewById(R.id.smallId2);
		smallId3 = (ImageView) view.findViewById(R.id.smallId3);
		smallId4 = (ImageView) view.findViewById(R.id.smallId4);
		img_more1 = (ImageView) view.findViewById(R.id.img_more1);
		rl = (RelativeLayout) view.findViewById(R.id.rl_top);

		home1 = (ImageView) view.findViewById(R.id.home_iv1);
		home2 = (ImageView) view.findViewById(R.id.home_iv2);
		home3 = (ImageView) view.findViewById(R.id.home_iv3);
		home4 = (ImageView) view.findViewById(R.id.home_iv4);
		home5 = (ImageView) view.findViewById(R.id.home_iv5);

		loading_dialog = new LoadingDialog(getActivity(), "正在努力加载数据...",
				R.anim.loading);
		loading_dialog.show();

		smallId1.setOnClickListener(new OnViewClickListener());
		smallId2.setOnClickListener(new OnViewClickListener());
		smallId3.setOnClickListener(new OnViewClickListener());
		smallId4.setOnClickListener(new OnViewClickListener());
		home1.setOnClickListener(new OnViewClickListener());
		home2.setOnClickListener(new OnViewClickListener());
		home3.setOnClickListener(new OnViewClickListener());
		home4.setOnClickListener(new OnViewClickListener());
		home5.setOnClickListener(new OnViewClickListener());

		RelativeLayout edit_search = (RelativeLayout) view
				.findViewById(R.id.searchId);
		edit_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("debug", "edit_search click");
				if (flag) {
					MainActivity.instance.setTabSelection(301);
				}
			}
		});
		qrCode = (LinearLayout) view.findViewById(R.id.qrCodeId);
		qrCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("debug", "qrCode click");
				if (flag) {
					startActivity(new Intent(getActivity(),
							QRCodeActivity.class).putExtra("yes", "0"));
				}

			}
		});
		initView();
	}

	public void initView() {
		Message msg = new Message();
		msg.arg1 = 1;
		m_handler.sendMessage(msg);
	}

	private class OnViewClickListener implements OnClickListener {

		public void onClick(View v) {
			if (flag) {
				switch (v.getId()) {
				case R.id.smallId1:
					MainActivity.instance.onTabClicked(view
							.findViewById(R.id.smallId1));
					break;
				case R.id.smallId2:
					MainActivity.instance.onTabClicked(view
							.findViewById(R.id.smallId2));
					break;
				case R.id.smallId3:// 全部课程
					MainActivity.instance.setTabSelection(103);
					break;
				case R.id.smallId4://营养配餐
					/*MainActivity.instance.onTabClicked(view
							.findViewById(R.id.smallId4));*/
					startActivity(new Intent(getActivity(),LunchListActivity.class));
					break;
				case R.id.t1:
					pager.setCurrentItem(0);
					changeColor(0);
					break;
				case R.id.t2:
					pager.setCurrentItem(1);
					changeColor(1);
					break;
				case R.id.t3:
					pager.setCurrentItem(2);
					changeColor(2);
					break;

				case R.id.home_iv1:
					MainActivity.instance.setTabSelection(501);
					break;
				case R.id.home_iv2:
				case R.id.home_iv3:
				case R.id.home_iv4:
				case R.id.home_iv5:
					Toast.makeText(getActivity(), "此功能正在开发中,敬请期待...", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		}

	}

	@SuppressLint("HandlerLeak")
	public Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				updateImgView();
				break;
			case 2:
				analyze_weather(msg.obj);
				break;
			default:
				break;
			}
		}

	};

	// 异步获取首页信息
	public void update_loacl_indexdata() {
		Map<String, String> map = new HashMap<String, String>();
		String jingdu = (String) SharedPreferencesUtils.getParam(getActivity(),
				"jingdu", "");
		String weidu = (String) SharedPreferencesUtils.getParam(getActivity(),
				"weidu", "");

		map.put("longitude", jingdu);
		map.put("latitude", weidu);
		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		LoadDataFromServer task = new LoadDataFromServer(getActivity(),
				Constant.URL_HOME, map);

		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				try {

					if (data.getString("msgFlag").equals("1")) {
						JSONArray small_imgArry = data
								.getJSONArray("pageHImageList");
						String samll_img_url_1 = small_imgArry.getJSONObject(0)
								.getString("imgpfile");
						String samll_img_url_2 = small_imgArry.getJSONObject(1)
								.getString("imgpfile");
						String samll_img_url_3 = small_imgArry.getJSONObject(2)
								.getString("imgpfile");
						String samll_img_url_4 = small_imgArry.getJSONObject(3)
								.getString("imgpfile");

						SharedPreferencesUtils.setParam(getActivity(),
								"samll_img_url_1", samll_img_url_1);
						SharedPreferencesUtils.setParam(getActivity(),
								"samll_img_url_2", samll_img_url_2);
						SharedPreferencesUtils.setParam(getActivity(),
								"samll_img_url_3", samll_img_url_3);
						SharedPreferencesUtils.setParam(getActivity(),
								"samll_img_url_4", samll_img_url_4);

						JSONArray big_imgArry = data
								.getJSONArray("pageVImageList");
						String big_img = "";
						for (int i = 0; i < big_imgArry.size(); i++) {
							big_img += big_imgArry.getJSONObject(i).getString(
									"imgpfile");
							if (i != big_imgArry.size() - 1) {
								big_img += ",";
							}
						}
						SharedPreferencesUtils.setParam(getActivity(),
								"big_img_url", big_img);
						// group.removeAllViews();
						updateImgView();
					}
				} catch (Exception e) {

				}

			}
		});

	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void updateImgView() {

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		Log.i("frag",
				SharedPreferencesUtils.getParam(getActivity(), "headpage", "0")
						+ "  home");
		if (SharedPreferencesUtils.getParam(getActivity(), "headpage", "0")
				.equals("2")) {
			if (views.size() != 0) {
				views.clear();
			}

			LayoutInflater mLi = LayoutInflater.from(getActivity());
			today_view = mLi.inflate(R.layout.lesson, null);
			tom_view = mLi.inflate(R.layout.lesson2, null);
			aftom_view = mLi.inflate(R.layout.lesson3, null);

			views.add(today_view);
			views.add(tom_view);
			views.add(aftom_view);
			getLessson1();
			getLessson2();
			getLessson3();

		} else {
			// 天气预报
			if (views.size() != 0) {
				views.clear();
			}
			// 将要分页显示的View装入数组中
			LayoutInflater mLi = LayoutInflater.from(getActivity());
			today_view = mLi.inflate(R.layout.weather_today, null);
			tom_view = mLi.inflate(R.layout.weather_tomorrow, null);
			aftom_view = mLi.inflate(R.layout.weather_aftertom, null);
			mLi.inflate(R.layout.refresh_top_item, null);
			views.add(today_view);
			views.add(tom_view);
			views.add(aftom_view);
			getWeather();

		}

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		pager = (ViewPager) view.findViewById(R.id.viewpager);
		pager.setAdapter(mPagerAdapter);

		pager.setOnPageChangeListener(new MyOnPageChangeListener());

		loading_dialog.dismiss();

	}

	/*
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			changeColor(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 获取当前城市天气预报
	 */
	private void getWeather() {
		new Thread() {
			public void run() {
				boolean flag = MyApplication.getInstance().isConnect(
						getActivity());
				if (flag) {

					String result = HttpWeatherUtil
							.request(SharedPreferencesUtils.getParam(
									getActivity(), "cityname",
									getString(R.string.localcity_default))
									.toString());
					try {
						org.json.JSONObject jsonobject = new org.json.JSONObject(
								result);
						Message msg = new Message();
						msg.obj = jsonobject;
						msg.arg1 = 2;// 天气预报
						m_handler.sendMessage(msg);
					} catch (org.json.JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 处理获取的天气数据
	 * 
	 * @param obj
	 */
	@SuppressLint("SimpleDateFormat")
	private void analyze_weather(Object obj) {

		// 取得系统日期:
		long time1 = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date d1 = new Date(time1);
		String time = "更新于" + format.format(d1);

		org.json.JSONObject weatherResult = (org.json.JSONObject) obj;
		try {
			if (weatherResult.getString("errNum").equals("0")) {

				org.json.JSONObject today_info = weatherResult.getJSONObject(
						"retData").getJSONObject("today");

				String temperature = today_info.getString("curTemp");
				String wind = today_info.getString("fengxiang");
				String weather_info = today_info.getString("type");

				org.json.JSONArray forecast = weatherResult.getJSONObject(
						"retData").getJSONArray("forecast");
				org.json.JSONObject tomorrow = forecast.getJSONObject(0);
				String temperature_2 = tomorrow.getString("lowtemp") + "~"
						+ tomorrow.getString("hightemp");
				String wind_2 = tomorrow.getString("fengxiang");
				String weather_info_2 = tomorrow.getString("type");

				org.json.JSONObject aftertomorrow = forecast.getJSONObject(1);
				String temperature_3 = aftertomorrow.getString("lowtemp") + "~"
						+ aftertomorrow.getString("hightemp");
				String wind_3 = tomorrow.getString("fengxiang");
				String weather_info_3 = aftertomorrow.getString("type");
				((TextView) today_view.findViewById(R.id.tv_weathertime1))
						.setText(time);
				((TextView) today_view.findViewById(R.id.tv_temperature))
						.setText(temperature);
				((TextView) today_view.findViewById(R.id.tv_weatherinfo))
						.setText(weather_info);
				((TextView) today_view.findViewById(R.id.tv_wind))
						.setText(wind);
				((ImageView) today_view.findViewById(R.id.iv_weather_img))
						.setImageResource(imageResoId(weather_info));

				((TextView) tom_view.findViewById(R.id.tv_2time2))
						.setText(time);
				((TextView) tom_view.findViewById(R.id.tv_temperature2))
						.setText(temperature_2);
				((TextView) tom_view.findViewById(R.id.tv_weatherinfo2))
						.setText(weather_info_2);
				((TextView) tom_view.findViewById(R.id.tv_wind2))
						.setText(wind_2);
				((ImageView) tom_view.findViewById(R.id.iv_weather_img2))
						.setImageResource(imageResoId(weather_info_2));

				((TextView) aftom_view.findViewById(R.id.tv_weathertime3))
						.setText(time);
				((TextView) aftom_view.findViewById(R.id.tv_temperature3))
						.setText(temperature_3);
				((TextView) aftom_view.findViewById(R.id.tv_weatherinfo3))
						.setText(weather_info_3);
				((TextView) aftom_view.findViewById(R.id.tv_wind3))
						.setText(wind_3);
				((ImageView) aftom_view.findViewById(R.id.iv_weather_img3))
						.setImageResource(imageResoId(weather_info_3));

			}
		} catch (org.json.JSONException e) {
			e.printStackTrace();
			Log.i(TAG, e.getMessage());
		}
		// 刷新结束，让刷新布局消失
		mRefreshableView.finishRefresh();
	}

	private int imageResoId(String weather) {
		int resoid = R.drawable.yintian;
		if (weather.indexOf("晴") != -1) {
			resoid = R.drawable.qing;
		} else if (weather.indexOf("多云") != -1) {
			resoid = R.drawable.duoyun;
		} else if (weather.indexOf("小雨") != -1) {
			resoid = R.drawable.xiaoyu;
		} else if (weather.indexOf("中雨") != -1) {
			resoid = R.drawable.zhongyu;
		} else if (weather.indexOf("大雨") != -1) {
			resoid = R.drawable.dayu;
		} else if (weather.indexOf("冰雹") != -1) {
			resoid = R.drawable.bingbao;
		} else if (weather.indexOf("阵雨") != -1) {
			resoid = R.drawable.leizhenyu;
		} else if (weather.indexOf("小雪") != -1) {
			resoid = R.drawable.xiaoxue;
		} else if (weather.indexOf("中雪") != -1) {
			resoid = R.drawable.zhongxue;
		} else if (weather.indexOf("大雪") != -1) {
			resoid = R.drawable.daxue;
		} else if (weather.indexOf("扬沙") != -1) {
			resoid = R.drawable.yangsha;
		} else if (weather.indexOf("沙尘") != -1) {
			resoid = R.drawable.shachenbao;
		} else if (weather.indexOf("雾") != -1) {
			resoid = R.drawable.wu;
		} else if (weather.indexOf("阴") != -1) {
			resoid = R.drawable.yintian;
		} else if (weather.indexOf("扬沙") != -1) {
			resoid = R.drawable.yangsha;
		} else if (weather.indexOf("雾霾") != -1) {
			resoid = R.drawable.wumai;
		} else if (weather.indexOf("雷阵雨") != -1) {
			resoid = R.drawable.leizhenyu;
		} else if (weather.indexOf("雷电") != -1) {
			resoid = R.drawable.leidian;
		} else if (weather.indexOf("浮尘") != -1) {
			resoid = R.drawable.fuchen;
		}
		return resoid;
	}

	public void getLessson1() {

		Map<String, String> map = new HashMap<String, String>();

		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		map.put("dayadd", "0"); // 传递参数

		LoadDataFromServer task = new LoadDataFromServer(getActivity(),
				Constant.URL_LESSON, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				Log.i("djy", data.getJSONArray("lessonList") + "");
				if (data != null) {
					if (data.getString("msgFlag").equals("1")) {
						UpdateLessonView("0", data.getJSONArray("lessonList"));
					}
				}
			}
		});

	}

	public void getLessson2() {

		Map<String, String> map = new HashMap<String, String>();

		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		map.put("dayadd", "1"); // 传递参数

		LoadDataFromServer task = new LoadDataFromServer(getActivity(),
				Constant.URL_LESSON, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data != null) {
					if (data.getString("msgFlag").equals("1")) {
						UpdateLessonView("1", data.getJSONArray("lessonList"));
					}
				}
			}
		});

	}

	public void getLessson3() {

		Map<String, String> map = new HashMap<String, String>();

		map.put("memid", MyApplication.getInstance().getMemid()); // 传递参数
		map.put("dayadd", "2"); // 传递参数

		LoadDataFromServer task = new LoadDataFromServer(getActivity(),
				Constant.URL_LESSON, map);
		task.getData(new DataCallBack() {
			@Override
			public void onDataCallBack(JSONObject data) {
				if (data != null) {
					if (data.getString("msgFlag").equals("1")) {
						UpdateLessonView("2", data.getJSONArray("lessonList"));
					}
				}
			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	public void UpdateLessonView(String day, JSONArray arry) {

		if (arry != null) {
			for (int i = 0; i < arry.size(); i++) {
				if (day.equals("0")) {
					try {

						String timeb = arry.getJSONObject(i)
								.getJSONObject("begintime").getString("time");

						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

						String begintime = sdf.format(new Date(Long
								.parseLong(timeb)));
						String timee = arry.getJSONObject(i)
								.getJSONObject("endtime").getString("time");
						String endtime = sdf.format(new Date(Long
								.parseLong(timee)));

						String coursename = arry.getJSONObject(i).getString(
								"coursename");
						if (i == 0) {
							((TextView) today_view.findViewById(R.id.tv_1))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 1) {
							((TextView) today_view.findViewById(R.id.tv_2))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 2) {
							((TextView) today_view.findViewById(R.id.tv_3))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 3) {
							((TextView) today_view.findViewById(R.id.tv_4))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 4) {
							((TextView) today_view.findViewById(R.id.tv_5))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 5) {
							((TextView) today_view.findViewById(R.id.tv_6))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
					} catch (Exception e) {
					}

				} else if (day.equals("1")) {
					try {
						String timeb = arry.getJSONObject(i)
								.getJSONObject("begintime").getString("time");
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						String begintime = sdf.format(Long.parseLong(timeb));
						String timee = arry.getJSONObject(i)
								.getJSONObject("endtime").getString("time");
						String endtime = sdf.format(Long.parseLong(timee));
						String coursename = arry.getJSONObject(i).getString(
								"coursename");
						if (i == 0) {
							((TextView) tom_view.findViewById(R.id.tv_1))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 1) {
							((TextView) tom_view.findViewById(R.id.tv_2))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 2) {
							((TextView) tom_view.findViewById(R.id.tv_3))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 3) {
							((TextView) tom_view.findViewById(R.id.tv_4))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 4) {
							((TextView) tom_view.findViewById(R.id.tv_5))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 5) {
							((TextView) tom_view.findViewById(R.id.tv_6))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
					} catch (Exception e) {
					}
				} else {
					try {
						String timeb = arry.getJSONObject(i)
								.getJSONObject("begintime").getString("time");
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						String begintime = sdf.format(Long.parseLong(timeb));
						String timee = arry.getJSONObject(i)
								.getJSONObject("endtime").getString("time");
						String endtime = sdf.format(Long.parseLong(timee));
						String coursename = arry.getJSONObject(i).getString(
								"coursename");
						if (i == 0) {
							((TextView) aftom_view.findViewById(R.id.tv_1))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 1) {
							((TextView) aftom_view.findViewById(R.id.tv_2))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 2) {
							((TextView) aftom_view.findViewById(R.id.tv_3))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 3) {
							((TextView) aftom_view.findViewById(R.id.tv_4))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 4) {
							((TextView) aftom_view.findViewById(R.id.tv_5))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
						if (i == 5) {
							((TextView) aftom_view.findViewById(R.id.tv_6))
									.setText("   " + begintime + " - "
											+ endtime + "   " + coursename);
						}
					} catch (Exception e) {
					}

				}
			}
		}
		// 刷新结束，让刷新布局消失
		mRefreshableView.finishRefresh();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			HideSoftInput(getView().getWindowToken());
		}
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
