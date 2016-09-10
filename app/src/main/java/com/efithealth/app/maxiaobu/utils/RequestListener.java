package com.efithealth.app.maxiaobu.utils;

import com.android.volley.VolleyError;

/**
 * Created by 马小布 on 2015/12/17.
 * 请求监听
 */
public interface RequestListener {

    /**
     * 成功
     */
    public void requestSuccess(String json);

    /**
     * 错误
     */
    public void requestError(VolleyError e);

    /**
     * 数据返回出错
     *
     * @param json
     */
    public void resultFail(String json);

    /**
     * json解析失败
     *
     * @param json
     */
//    public void resultiJSONException(String json);


}
