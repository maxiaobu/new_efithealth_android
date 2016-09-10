package com.efithealth.app.Photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.efithealth.R;

import image.Bimp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * 发布课程相册显示
 */
public class ChoosePhotoActivity3 extends Activity {

	final String TAG = getClass().getSimpleName();
	private List<ImageModel> mImageList;// 相册图片
	private LayoutInflater mLayoutInflater;
	private GridView mGridView;
	private Adapter mAdapter;
	private ImageWork mImageWork;// 图片加载类
	private ImageView back;
	private Button bt;
	private Map<String, String> map =new LinkedHashMap<>();
	@SuppressLint({ "HandlerLeak", "ShowToast" })
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ChoosePhotoActivity3.this, "最多选择4张图片", 0)
						.show();
				break;
			case 1:
				if (count == 0) {
					bt.setText("完成");
				} else if (count > 0 && count < 4) {
					bt.setText("完成" + "(" + count + ")");
				} else {
					bt.setText("完成(4)");
				}
				break;
			default:
				break;
			}
		}
	};

	private int count = 0;
	private int selectTotal = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_photo);
		init();

		back = (ImageView) findViewById(R.id.photo_iv_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ChoosePhotoActivity3.this.finish();
			}
		});
		count = Bimp.drr.size() + selectTotal;
		if (count == 0) {
			bt.setText("完成");
		} else if (count > 0 && count < 4) {
			bt.setText("完成" + "(" + count + ")");
		} else {
			bt.setText("完成(4)");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ChoosePhotoActivity3.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	void init() {
		mImageWork = new ImageWork(this);
		mLayoutInflater = LayoutInflater.from(this);
		mImageList = Utils.getImages(this);
		mGridView = (GridView) findViewById(R.id.gv_main);
		mAdapter = new Adapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mAdapter.notifyDataSetChanged();
			}

		});
		bt = (Button) findViewById(R.id.bt1);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (map.size() != 0) {
					ArrayList<String> list = new ArrayList<String>();
					Collection<String> c = map.values();
					Iterator<String> it = c.iterator();
					for (; it.hasNext();) {
						list.add(it.next());
					}
					if (Bimp.act_bool) {
						Bimp.act_bool = false;

					}
					for (int i = 0; i < list.size(); i++) {
						if (Bimp.drr.size() < 9) {
							Bimp.drr.add(list.get(i));
						}
					}
					
					Bimp.act_bool = true;
				}
				finish();
			}

		});

		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					// Before Honeycomb pause image loading on scroll to help
					// with performance
					if (!Utils.hasHoneycomb()) {
						mImageWork.setPauseWork(true);
					}
				} else {
					mImageWork.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	private class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mImageList.size();
		}

		@Override
		public Object getItem(int i) {
			return mImageList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			final ViewHolder viewHolder;
			final ImageModel imageModel = (ImageModel) getItem(i);
			String path = imageModel.getPath();
			if (view == null) {
				viewHolder = new ViewHolder();
				view = mLayoutInflater.inflate(R.layout.imageview, null);
				viewHolder.imageView = (ImageView) view
						.findViewById(R.id.iv_imageView);
				viewHolder.selected = (ImageView) view
						.findViewById(R.id.isselected);
				viewHolder.text = (TextView) view
						.findViewById(R.id.item_image_grid_text);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.imageView.setTag(path);
			mImageWork.loadImage(path, viewHolder.imageView);
			if (imageModel.getisSelected()) {

				viewHolder.selected
						.setImageResource(R.drawable.photo_sel_photopickervc2x);
				viewHolder.text
						.setBackgroundResource(R.drawable.bgd_relatly_line);
			} else {
				viewHolder.selected.setImageResource(-1);
				viewHolder.text.setBackgroundColor(0x00000000);
			}
			viewHolder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String path = mImageList.get(i).getPath();
					Message message = mHandler.obtainMessage();
					if ((Bimp.drr.size() + selectTotal) < 4) {
						if (!imageModel.getisSelected()) {
							imageModel.setisSelected(true);
							viewHolder.selected
									.setImageResource(R.drawable.photo_sel_photopickervc2x);
							viewHolder.text
									.setBackgroundResource(R.drawable.bgd_relatly_line);
							selectTotal++;
							map.put(path, path);
						} else {
							imageModel.setisSelected(false);
							viewHolder.selected.setImageResource(-1);
							viewHolder.text.setBackgroundColor(0x00000000);
							selectTotal--;
							map.remove(path);
						}
						message.what = 1;
					} else if ((Bimp.drr.size() + selectTotal) >= 4) {
						if (imageModel.getisSelected()) {
							imageModel.setisSelected(false);// =
							viewHolder.selected.setImageResource(-1);
							selectTotal--;
							map.remove(path);
							message.what = 1;
						} else {
							message.what = 0;
						}
					}
					count = Bimp.drr.size() + selectTotal;
					message.sendToTarget();
				}

			});
			return view;
		}

		class ViewHolder {
			ImageView imageView;
			private ImageView selected;
			private TextView text;
		}
	}

}
