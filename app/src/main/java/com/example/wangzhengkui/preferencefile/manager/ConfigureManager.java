package com.example.wangzhengkui.preferencefile.manager;

import android.content.Context;
import android.util.SparseArray;

import com.example.wangzhengkui.preferencefile.KeyValue;
import com.example.wangzhengkui.preferencefile.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.entity.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Administrator on 2016-04-06 14:42
 */
public class ConfigureManager {
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String TYPE = "type";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String ENTRY = "entry";
    public static final String DEFAULT_VALUE = "defaultValue";


    private static JSONObject dataJson;

    public static void init() {
        //根据版本号，是否删除缓存
    }
    /**
     *
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        //release
//        return key;
        //debug
        if (isCacheValue(key)) {
            return readCacheValue(key);
        } else {
            return "defalutValue";
        }
    }

    /**
     * 从缓存中读取value
     * @param key
     * @return
     */
    public static Object readCacheValue(String key) {

        return null;
    }
    public static boolean isCacheValue(String key) {

        return false;
    }

    /**
     * 写缓存
     * @param key
     */
    public static void writeCacheValue(String key,String value) {

    }


    /**
     * 返回第一屏幕信息
     */
    public static ConfigureScreenEntity getFirstScreen() {
        try {
            JSONObject object = new JSONObject(Constants.jsonDate);
            return (ConfigureScreenEntity) DataParseTools.parseData(null,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回key所对应的屏幕信息
     * @param key
     */
    public static void getScreen(String key) {

        /*for (int i = 0; i < 10; i++) {
            if (!isCacheValue(key)) {
                value = defalutValue;
                writeCacheValue(key, value);
            } else {
                value = readCacheValue(key);
            }
        }*/
    }


    /**
     * 更新缓存和
     * @param key
     * @param value
     */
    public static void update(String key, String value) {
//        "apn"=value;
//        Constants.apn = "cmwap";

    }


    /**
     * 重置缓存
     * @param entity
     * @param restoreChild 是否重置子页
     */
    public static void restore(ConfigureEntity entity,boolean restoreChild) {

    }
}
