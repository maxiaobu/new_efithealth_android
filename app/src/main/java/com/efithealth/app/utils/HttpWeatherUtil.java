package com.efithealth.app.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class HttpWeatherUtil {

	//百度天气接口url
	
	private static String httpUrl = "http://apis.baidu.com/apistore/weatherservice/recentweathers";	
	private static String httpUrl_citycodde = "http://apis.baidu.com/apistore/weatherservice/cityinfo";
	//百度天气接口key
	private static String key = "0e758c3c749705f41fd10fe01925bb92";
	public static String request(String cityname) {
		String httpArg = httpUrl;
		try {
			 httpArg +="?cityname=" + URLEncoder.encode(cityname, "UTF-8");
			 httpArg += "&cityid="+getCityCode(cityname);
		} catch (UnsupportedEncodingException e1) {			
			e1.printStackTrace();
			return "";
		}	  
		return requestHttpUrl( httpUrl + "?" + httpArg);
	}
	
	public static String getCityCode(String cityname){
		String httpUrl =  httpUrl_citycodde;
		try {
			httpUrl = httpUrl + "?cityname=" + URLEncoder.encode(cityname, "UTF-8");
		} catch (UnsupportedEncodingException e1) {			
			e1.printStackTrace();
			return "";
		}
		String result = requestHttpUrl(httpUrl);
		if (result!=null) {
			JSONObject jsonobject;
			try {
				jsonobject = new JSONObject(result);
				String citycode =  jsonobject.getJSONObject("retData").optString("cityCode");
				return citycode;
			} catch (JSONException e) {
				e.printStackTrace();
				return "";
			}
			
		}
		
		return "沈阳";
	   
	}
	public static String requestHttpUrl(String httpUrl){
		BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
		try {
		        URL url = new URL(httpUrl);
		        HttpURLConnection connection = (HttpURLConnection) url
		                .openConnection();
		        connection.setRequestMethod("GET");		       
		        connection.setRequestProperty("apikey",  key);
		        connection.connect();
		        InputStream is = connection.getInputStream();
		        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		        String strRead = null;
		        while ((strRead = reader.readLine()) != null) {
		            sbf.append(strRead);
		            sbf.append("\r\n");
		        }
		        reader.close();
		        result = sbf.toString();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		Log.i("-----------------------------------------", result+"");
		 return result;
	}
}
