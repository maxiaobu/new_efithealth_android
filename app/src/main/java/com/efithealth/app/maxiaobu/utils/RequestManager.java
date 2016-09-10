package com.efithealth.app.maxiaobu.utils;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.efithealth.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 *  2015/12/17 0017  网络请求管理类
 */
public class RequestManager {
    public static final String TAG="RequestManager";

    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApplication.applicationContext);

    private RequestManager() {

    }

    /**
     * 返回String
     *
     * @param url      连接
     * @param tag      上下文
     * @param listener 回调
     */
    public static void get(String url, Object tag, RequestListener listener) {
        try {
            ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                    url, null, responseListener(listener, false),
                    responseError(listener, false));
            addRequest(request, tag);
        } catch (Exception e) {
            Toast.makeText(MyApplication.applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 返回String 带进度条
     *
     * @param url           连接
     * @param tag           上下文
     * @param progressTitle 进度条文字
     * @param listener      回调
     */
    public static void get(String url, Object tag, String progressTitle,
                           RequestListener listener) {

        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, true),
                responseError(listener, true));
        //设置超时时间重连次数
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }

    /**
     * 返回对象
     *
     * @param url      连接
     * @param tag      上下文
     * @param classOfT 类对象
     * @param listener 回调
     */
    public static <T> void get(String url, Object tag, Class<T> classOfT,
                               RequestJsonListener<T> listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, classOfT, false),
                responseError(listener, false));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }


    /**
     * 上传图片
     *
     * @param url      接口
     * @param tag      上下文
     * @param params   post需要传的参数
     * @param listener 回调
     */
    public static void postImg(String url, Object tag, RequestParams params,
                               RequestListener listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, false),
                responseError(listener, false));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }

    public static void post(String url, Object tag, RequestParams params,
                            RequestListener listener) {

        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, true),
                responseError(listener, true));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }

    /**
     * 不带加载progress
     *
     * @param url
     * @param tag
     * @param params
     * @param listener
     */
    public static void postNoProgress(String url, Object tag, RequestParams params,
                                      RequestListener listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, false),
                responseError(listener, false));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }

    /**
     * 返回String 带进度条
     *
     * @param url           接口
     * @param tag           上下文
     * @param params        post需要传的参数
     * @param progressTitle 进度条文字
     * @param listener      回调
     */
    public static void post(String url, Object tag, RequestParams params,
                            String progressTitle, RequestListener listener) {

        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, true),
                responseError(listener, true));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }

    /**
     * 返回对象 带进度条
     *
     * @param url           接口
     * @param tag           上下文
     * @param classOfT      类对象
     * @param params        post需要传的参数
     * @param progressTitle 进度条文字
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param listener      回调
     */
    public static <T> void post(String url, Object tag, Class<T> classOfT,
                                RequestParams params, String progressTitle, boolean LoadingShow,
                                RequestJsonListener<T> listener) {

        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params,
                responseListener(listener, classOfT, LoadingShow),
                responseError(listener, LoadingShow));
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, tag);
    }


    /**
     * 成功消息监听 返回对象
     *
     * @param l
     * @return
     */
    protected static <T> Response.Listener<byte[]> responseListener(
            final RequestJsonListener<T> l, final Class<T> classOfT,
            final boolean flag) {
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] arg0) {
                String data = null;
                String data_request = null;
                try {
                    data = new String(arg0, "UTF-8");
                    JSONObject a = new JSONObject(data);

                    data_request = a.getString("data");
                    //request error
                    if (Integer.parseInt(a.get("code").toString()) == 400) {
                        Toast.makeText(MyApplication.applicationContext, a.get("msg").toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        l.requestSuccess(JsonUtils.object(data_request, classOfT));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 对象返回错误监听
     *
     * @param l    回调
     * @param flag flag true 带进度条 flase不带进度条
     * @return
     */
    protected static <T> Response.ErrorListener responseError(
            final RequestJsonListener<T> l, final boolean flag) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                if (NetworkUtils.isAvailable(MyApplication.applicationContext)) {
                    Toast.makeText(MyApplication.applicationContext, "请检查网络连接", Toast.LENGTH_LONG);
                }
                l.requestError(e);
            }
        };
    }


    protected static Response.Listener<byte[]> responseListener(
            final RequestListener l, final boolean flag) {
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] arg0) {
                String data = null;
                String data_request = null;
                try {
                    data = new String(arg0, "UTF-8");
//                    Log.i("data", data);
                    JSONObject a = new JSONObject(data);

                    data_request = a.getString("msgContent");
                    int code = Integer.parseInt(a.get("msgFlag").toString());
                    Log.i(TAG, "onResponse: "+code);
                    if (1==code){
                        l.requestSuccess(data);
                    }else {
                        l.resultFail(data_request);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }



    /**
     * String 返回错误监听
     *
     * @param l    String 接口
     * @param flag true 带进度条 flase不带进度条
     * @return
     */
    protected static Response.ErrorListener responseError(
            final RequestListener l, final boolean flag) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                if (!NetworkUtils.isAvailable(MyApplication.applicationContext)) {
                    Toast.makeText(MyApplication.applicationContext, "请检查网络连接", Toast.LENGTH_LONG).show();
                }
                l.requestError(e);
            }
        };
    }

    /**
     * @param request 请求
     * @param tag     上下文
     */
    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    /**
     * 当主页面调用协议 在结束该页面调用此方法
     *
     * @param tag
     */
    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    /**
     * Volley网络状态监听
     */
    public static boolean handleVolleyError(VolleyError error, boolean flag) {
        //如果进度条是显示状态，则关闭进度条

        if (error == null) {
            Toast.makeText(MyApplication.applicationContext, "亲,您的网络不给力", Toast.LENGTH_LONG).show();
            return true;
        } else if (error.networkResponse == null) {
            Toast.makeText(MyApplication.applicationContext, "亲,您的网络不给力", Toast.LENGTH_LONG).show();
            return true;
        } else if (error.networkResponse.statusCode == 500) {
            Toast.makeText(MyApplication.applicationContext, "亲,您的网络不给力", Toast.LENGTH_LONG).show();
            return true;
        } else if (error.networkResponse.statusCode == 404) {
            return true;
        } else if (error.networkResponse.statusCode == 401) {
            Toast.makeText(MyApplication.applicationContext, "亲,您的网络不给力", Toast.LENGTH_LONG).show();
            return true;
        } else if (error.networkResponse.statusCode == 406) {
            return true;
        } else if (error.networkResponse.statusCode == 500) {
            Toast.makeText(MyApplication.applicationContext, "亲,您的网络不给力", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
