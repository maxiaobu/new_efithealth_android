package com.efithealth.app.maxiaobu.utils;


import android.content.Context;

/**
 *  2015/12/17 封装请求
 */
public class IRequest {
    /**
     * 返回String get
     *
     * @param context
     * @param url
     * @param l
     */
    public static void get(Context context, String url, RequestListener l) {
        RequestManager.get(url, context, l);
    }

    /**
     * 返回String 带进度条 get
     *
     * @param context
     * @param url
     * @param progressTitle
     * @param l
     */
    public static void get(Context context, String url, String progressTitle,
                           RequestListener l) {
        RequestManager.get(url, context, progressTitle, l);
    }


    /**
     * 返回String post
     *
     * @param context
     * @param url
     * @param params
     * @param l
     */
    public static void post(Context context, String url, RequestParams params,
                            RequestListener l) {
        RequestManager.post(url, context, params, l);
    }

    /**
     * noProgress
     *
     * @param context
     * @param url
     * @param params
     * @param l
     */
    public static void postNoProgress(Context context, String url, RequestParams params,
                                      RequestListener l) {
        RequestManager.postNoProgress(url, context, params, l);
    }

    /**
     * 上传图片
     *
     * @param context
     * @param url
     * @param params
     * @param l
     */
    public static void postImg(Context context, String url, RequestParams params,
                               RequestListener l) {
        RequestManager.postImg(url, context, params, l);
    }

    /**
     * 返回对象 post
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param l
     */
    public static <T> void post(Context context, String url, Class<T> classOfT,
                                RequestParams params, RequestJsonListener<T> l) {
        RequestManager.post(url, context, classOfT, params, null, false, l);
    }


    /**
     * 返回String 带进度条 post
     *
     * @param context
     * @param url
     * @param params
     * @param progressTitle
     * @param l
     */
    public static void post(Context context, String url, RequestParams params,
                            String progressTitle, RequestListener l) {
        RequestManager.post(url, context, params, progressTitle, l);
    }

    /**
     * 返回对象 带进度条 post
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param l
     */
    public static <T> void post(Context context, String url, Class<T> classOfT,
                                RequestParams params, String progressTitle, RequestJsonListener<T> l) {
        RequestManager.post(url, context, classOfT, params, progressTitle,
                true, l);

    }

    /**
     * 返回对象 带进度条 post 可选择显示进度 适合带分页
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param progressTitle
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param l
     */
    public static <T> void post(Context context, String url, Class<T> classOfT,
                                RequestParams params, String progressTitle, boolean LoadingShow,
                                RequestJsonListener<T> l) {
        RequestManager.post(url, context, classOfT, params, progressTitle,
                true, l);
    }
}
