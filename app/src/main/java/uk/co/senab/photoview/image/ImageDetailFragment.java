package uk.co.senab.photoview.image;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.efithealth.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ImageView image_small;
	private View all_view;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;
		Log.i("ImageDetailFragment", "--------" + mImageUrl + "------------");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		all_view = (View) v.findViewById(R.id.all_view);
		all_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		image_small = (ImageView) v.findViewById(R.id.image_small);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnViewTapListener(new OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// image_small.setImageResource(R.drawable.ic_launcher);
		if (!mImageUrl.contains("http://")) {
			Bitmap bm = BitmapFactory.decodeFile(mImageUrl);
			mImageView.setImageBitmap(bm);
		} else {

			DisplayImageOptions options = new DisplayImageOptions.Builder()//
					.cacheInMemory(true)//
					.cacheOnDisk(true)//
					.bitmapConfig(Config.RGB_565)//
					.build();
			ImageLoader.getInstance().displayImage(mImageUrl, image_small,
					options);
			ImageLoader.getInstance().displayImage(mImageUrl, mImageView,
					options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason failReason) {
							// TODO Auto-generated method stub
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "下载错误";
								break;
							case DECODING_ERROR:
								message = "图片无法显示";
								break;
							case NETWORK_DENIED:
								message = "网络有问题，无法下载";
								break;
							case OUT_OF_MEMORY:
								message = "图片太大无法显示";
								break;
							case UNKNOWN:
								message = "未知的错误";
								break;
							}
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							// TODO Auto-generated method stub
							progressBar.setVisibility(View.GONE);
							image_small.setVisibility(View.GONE);
							all_view.setVisibility(View.GONE);
							mAttacher.update();
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
		}

	}
}
