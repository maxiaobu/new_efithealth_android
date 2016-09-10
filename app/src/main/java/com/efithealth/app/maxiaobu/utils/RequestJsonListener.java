package com.efithealth.app.maxiaobu.utils;

import com.android.volley.VolleyError;

/**
 *
 *  2015/12/17 0017 JSON 数据请求接口
 */
public interface RequestJsonListener<T> {
    /**
     * 成功
     *
     */
    public void requestSuccess(T result);

    /**
     * 错误
     */
    public void requestError(VolleyError e);
}
