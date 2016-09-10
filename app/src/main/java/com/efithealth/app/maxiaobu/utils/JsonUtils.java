package com.efithealth.app.maxiaobu.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonUtils {
    private static Gson gson = new Gson();

    public static <T> T object(String json, Class<T> classOfT) {

        return gson.fromJson(json, classOfT);
    }

    public static <T> List<T> getList(String jsonString, Class<T> cls) {
        JsonElement root = new JsonParser().parse(jsonString);
        List<T> data = new ArrayList<>();
        JsonArray rootArray = root.getAsJsonArray();
        for (JsonElement json : rootArray) {
            try {
                data.add(gson.fromJson(json, cls));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = new JSONArray();
            Iterator<String> keyIter = jsonObject.keys();
            String key = null;
            Object value;
            Map<String, Object> valueMap = new HashMap<>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e("TAG", e.toString());
        }
        return null;
    }

    /**
     * Json 转成 List<Map<>>
     *
     * @param jsonStr
     * @return
     */
    public static List<Map<String, Object>> getlistForJson(String jsonStr) {

        List<Map<String, Object>> list = null;

        JSONObject jsonObj = null;

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            list = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObj = (JSONObject) jsonArray.get(i);
                list.add(getMapForJson(jsonObj.toString()));
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }

}
