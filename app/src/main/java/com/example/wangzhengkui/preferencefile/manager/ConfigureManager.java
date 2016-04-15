package com.example.wangzhengkui.preferencefile.manager;


import android.text.TextUtils;

import com.example.wangzhengkui.preferencefile.cache.PreferencesCacheManager;
import com.example.wangzhengkui.preferencefile.cache.PreferencesCacheManager.EditorImpl;
import com.example.wangzhengkui.preferencefile.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.entity.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

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
    public static final String VERSION = "version";
    public static final String EMPTY = "  ";
    public static final String RELEASE = "0";

    private static JSONObject dataJson;

    public static void init() {
        String oldVersion = readCacheValue(VERSION);
        if (EMPTY.equals(oldVersion)) {
            writeCacheValue(VERSION, Constants.version);
        } else if (Constants.version.compareTo(oldVersion) > 0) {
            //配置版本号高于原来的版本号 清除缓存
            clearCache();
            writeCacheValue(VERSION, Constants.version);
        }
    }

    /***
     * 将json字符串解析存到缓存
     */
    public static String parseData(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        HashMap<String, String> maps = DataParseTools.parseJson();
        writeCacheValueNotExist(maps);
        return maps.get(key);
    }

    /***
     * 保存还没有记录的key value，如果已经存在 则不保存
     */
    private static void writeCacheValueNotExist(HashMap<String, String> hashMap) {
        if (hashMap == null || hashMap.isEmpty()){
            return;
        }
        PreferencesCacheManager mgr = PreferencesCacheManager.getInstance();
        EditorImpl editor = mgr.edit();
        Set<Entry<String, String>> sets = hashMap.entrySet();
        for(Entry<String, String> set : sets) {
            String key = set.getKey();
            String value = set.getValue();
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value) || isCacheValue(key)) {
                continue;
            }
            editor.putString(key, value);
        }
        editor.commit();
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {

        if (TextUtils.isEmpty(key)) {
            return null;
        }
        //realse
        if (TextUtils.equals(Constants.mode, RELEASE)) {
            return key;
        }
        //debug
        String cacheValue = readCacheValue(key);
        //有缓存值 直接返回
        if (!EMPTY.equals(cacheValue)) {
            return cacheValue;
        }else {   //否则 返回默认值
           return parseData(key);
        }
    }

    /**
     * 从缓存中读取value
     * @param key
     * @return
     */
    public static String readCacheValue(String key) {
        if (TextUtils.isEmpty(key)) {
            return EMPTY;
        }
        PreferencesCacheManager mgr = PreferencesCacheManager.getInstance();
        return mgr.getString(key, EMPTY);
    }

    /**
     * 清除所有缓存
     */
    private static void clearCache() {
        PreferencesCacheManager mgr = PreferencesCacheManager.getInstance();
        EditorImpl editor = mgr.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 缓存是否有记录该Key相对于的value值
     * @param key
     * @return
     */
    public static boolean isCacheValue(String key) {
        return EMPTY.equals(readCacheValue(key)) ? false : true;
    }

    /**
     * 写缓存
     * @param key
     */
    public static void writeCacheValue(String key, String value) {
        PreferencesCacheManager mgr = PreferencesCacheManager.getInstance();
        EditorImpl editor = mgr.edit();
        editor.putString(key, value);
        editor.commit();
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
     * 保存hashmap记录的key value值到缓存
     * @param hashMap
     */
    public static void writeCacheValue(HashMap<String, String> hashMap) {
        if (hashMap == null || hashMap.isEmpty()){
            return;
        }
        PreferencesCacheManager mgr = PreferencesCacheManager.getInstance();
        EditorImpl editor = mgr.edit();
        Set<Entry<String, String>> sets = hashMap.entrySet();
        for(Entry<String, String> set : sets) {
            String key = set.getKey();
            String value = set.getValue();
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                continue;
            }
            editor.putString(key, value);
        }
        editor.commit();
    }
    /**
     * 返回key所对应的屏幕信息
     * @param key
     */
    public static ConfigureEntity getScreen(String key) {
        return DataParseTools.getScreen(key);
    }


    /**
     * 重置缓存
     * @param entity
     * @param restoreChild 是否同时重置子页
     */
    public static void restore(ConfigureEntity entity,boolean restoreChild) {
        if (entity == null) {
            return;
        }
        Object ot = entity.getValue();
        if (ot != null){
            String key = entity.getKey();
            String defaultValue = (String) entity.getValue();
            if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(defaultValue)) {
                writeCacheValue(key, defaultValue);
            }
        }
        if (entity instanceof ConfigureScreenEntity) {
            LinkedList<ConfigureEntity> lists =  ((ConfigureScreenEntity)entity).getItemList();
            if (lists == null || lists.isEmpty()){
                return;
            }
            for (int i = 0; i < lists.size(); i++) {
                ConfigureEntity ce = lists.get(i);
                if(ce == null)continue;
                String ckey = ce.getKey();
                if (restoreChild && ce instanceof ConfigureScreenEntity) {
                    restore(getScreen(ckey),restoreChild);
                } else {
                    String cdefaultValue = (String) entity.getValue();
                    Object cot = ce.getValue();
                    if (cot == null){
                        continue;
                    }
                    if(!TextUtils.isEmpty(ckey) && !TextUtils.isEmpty(cdefaultValue)) {
                        writeCacheValue(ckey, cdefaultValue);
                    }
                }
            }
        }
    }
}
