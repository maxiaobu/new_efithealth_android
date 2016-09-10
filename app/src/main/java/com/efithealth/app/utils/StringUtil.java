package com.efithealth.app.utils;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class StringUtil {

     public static boolean isEmpty(String s) {
        if (s == null || s.length() <= 0 || "".equals(s)) {
            return true;
        }
        return false;
    }
}
