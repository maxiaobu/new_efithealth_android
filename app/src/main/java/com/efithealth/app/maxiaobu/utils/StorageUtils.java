package com.efithealth.app.maxiaobu.utils;

import android.util.Log;

import java.io.File;

/**
 * Created by 马小布 on 2016/6/30.
 */
public class StorageUtils {
    public static String TAG="StorageUtils";

    public static boolean fileIsExists(String path){
        try{
            File f=new File(path);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            Log.i(TAG, "fileIsExists: 异常");
            return false;
        }
        return true;
    }
}
