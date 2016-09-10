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
import com.efithealth.app.Constant;
import com.efithealth.app.task.SendMyInfoTask.DataCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

/** 
 * Description: 发布动态 任务
 * @author Xushd
 * @since 2016年3月3日下午4:49:05
 */
public class SendMyPublishTask {

	private List<String> image_list;
	private Map<String,String> map;
	private String url;
	
	public SendMyPublishTask(Context context,String url,List<String> img_list,Map<String,String> content){
		this.image_list = img_list;
		this.url = url;
		this.map = content;
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
				int num = image_list.size();
				for(int i=0;i<num;i++){
					String imgUrl = image_list.get(i);					
					File file = new File(imgUrl);
					if(file.exists()){
						entity.addPart("imagefile", new FileBody(file));
						Log.e("imageStr_ok---->>>>>>.", "ffffff");
					}else{
						Log.e("imageStr_ok---->>>>>>.", "ggggggg");
					}									
				}				
				try {
					entity.addPart("memid",	new StringBody(map.get("memid"), Charset.forName("UTF-8")));
					entity.addPart("content",	new StringBody(map.get("content"), Charset.forName("UTF-8")));
					entity.addPart("isprivate",	new StringBody(map.get("isprivate"), Charset.forName("UTF-8")));
					entity.addPart("istop",	new StringBody(map.get("istop"), Charset.forName("UTF-8")));
				
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
