package com.efithealth.app.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

/** 
 * Description: 照片墙 信息保存 任务
 * @author Xushd
 * @since 2016年3月2日上午9:32:38
 */
public class SendImgWallTask {

	private List<String> imglist_top;
	private List<String> imglist_more;
	private Map<String,String> map;
	private String url;
	public SendImgWallTask(Context context, String url, List<String> list_top,
			List<String> list_more,Map<String,String> map) {

		this.url = url;
		this.imglist_top = list_top;
		this.imglist_more = list_more;
		this.map = map;	
	}
	
	
	public void getData(final DataCallBack dataCallBack) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 111 && dataCallBack != null) {
					JSONObject jsonObject = (JSONObject) msg.obj;
					dataCallBack.onDataCallBack(jsonObject);
				} else {
					dataCallBack.onDataCallBack(null);
					Log.e("APIerrorCode:", String.valueOf(msg.what));

				}
			}
		};
		
		
		new Thread() {
			@SuppressLint("SdCardPath")
			public void run() {
				HttpClient client = new DefaultHttpClient();
				MultipartEntity entity = new MultipartEntity();
				int num_top = imglist_top.size();
				int num_more = imglist_more.size();
				
				for (int i = 0; i < num_top; i++) {
					String imageUrl = imglist_top.get(i);
					if(imageUrl.equals("add"))continue;	
					if(imageUrl.contains("http://"))continue;
					File file = new File(imageUrl);
					if(file.exists()){
						entity.addPart("istopfile", new FileBody(file));
						Log.e("imageStr_ok---->>>>>>.", "ffffff");
					}else{
						Log.e("imageStr_ok---->>>>>>.", "ggggggg");
					}									
				}
				for (int i = 0; i < num_more; i++) {
					String imageUrl = imglist_more.get(i);
					if(imageUrl.equals("add"))continue;
					if(imageUrl.contains("http://"))continue;
					File file = new File(imageUrl);
					if(file.exists()){
						entity.addPart("imagefile", new FileBody(file));
						Log.e("imageStr_ok---->>>>>>.", "ffffff");
					}else{
						Log.e("imageStr_ok---->>>>>>.", "ggggggg");
					}									
				}
				try {
					entity.addPart("memid",	new StringBody(map.get("memid"), Charset.forName("UTF-8")));
					entity.addPart("dimg",	new StringBody(map.get("dimg"), Charset.forName("UTF-8")));				
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
				
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 30000);				
				HttpPost post = new HttpPost(url);
				post.setEntity(entity);
				StringBuilder builder = new StringBuilder();
				try {
					HttpResponse response = client.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent(), Charset.forName("UTF-8")));
						for (String s = reader.readLine(); s != null; s = reader
								.readLine()) {
							builder.append(s);
						}
						String builder_BOM = jsonTokener(builder.toString());
						System.out.println("返回数据是------->>>>>>>>"
								+ builder.toString());
						try {
							JSONObject jsonObject = new JSONObject();
							jsonObject = JSONObject.parseObject(builder_BOM);
							Message msg = handler.obtainMessage();
							msg.what = 111;
							msg.obj = jsonObject;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							Message msg = handler.obtainMessage();
							msg.what = 222;
							msg.obj = null;
							handler.sendMessage(msg);
						}
					}
					
				} catch (ClientProtocolException e) {
					Message msg = handler.obtainMessage();
					msg.what = 444;
					msg.obj = null;
					handler.sendMessage(msg);

				} catch (IOException e) {
					Message msg = handler.obtainMessage();
					msg.what = 555;
					msg.obj = null;
					handler.sendMessage(msg);
				}
				
			}
		}.start();
	}
	private String jsonTokener(String in) {

		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}
	/**
	 * 网路访问调接口
	 * 
	 */
	public interface DataCallBack {
		void onDataCallBack(JSONObject data);
	}
	
	
}
