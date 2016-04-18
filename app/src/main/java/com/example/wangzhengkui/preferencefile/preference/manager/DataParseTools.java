package com.example.wangzhengkui.preferencefile.preference.manager;

import android.text.TextUtils;

import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureListEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureType;
import com.example.wangzhengkui.preferencefile.preference.entity.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Administrator on 2016-04-12 10:40
 */
public class DataParseTools {
    /***
     * 解析json  得到当前screen实体
     * @param key
     * @param jsonObject
     * @return
     */
    public static ConfigureEntity parseData(String key, JSONObject jsonObject) {
        ConfigureEntity entity = null;
        //读取第一屏的数据
        Iterator<String> keys = jsonObject.keys();
        ConfigureScreenEntity screenEntity = new ConfigureScreenEntity();
        LinkedList<ConfigureEntity> linkedList = new LinkedList<>();
        try {
            if (key == null) {
                //读取第一屏screen的key;
                while (keys.hasNext()) {
                    key = keys.next();
                    screenEntity.setKey(key);
                }
                //读取第一屏所对应的json
                jsonObject = jsonObject.getJSONObject(key);
            }
            keys = jsonObject.keys();
            while (keys.hasNext()) {
                //每个item中，type是必须且肯定存在
                String childKey = keys.next();
                if ("type".equals(childKey)) {
                    screenEntity.setType(jsonObject.optString(childKey));
                } else if ("value".equals(childKey)) {
                    screenEntity.setValue(jsonObject.optString(childKey));
                } else if ("summary".equals(childKey)) {
                    screenEntity.setSummary(jsonObject.optString(childKey));
                } else if ("title".equals(childKey)) {
                    screenEntity.setTitle(jsonObject.optString(childKey));
                } else {
                    JSONObject object = jsonObject.getJSONObject(childKey);
                    ConfigureEntity entity1 = parseEntity(childKey, object);
                    linkedList.add(entity1);
                }
            }
            screenEntity.setItemList(linkedList);
            return screenEntity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /***
     * 解析json 得到相对应的item实体
     * @param key
     * @param jsonObject
     * @return
     */
    public static ConfigureEntity parseEntity(String key, JSONObject jsonObject) {
        String type = jsonObject.optString(ConfigureManager.TYPE);
        ConfigureEntity entity = null;
        switch (type) {
            case ConfigureType.SCREEN:
                entity = new ConfigureScreenEntity();

                break;
            case ConfigureType.CATEGORY:
                entity = new ConfigureEntity();
                break;
            case ConfigureType.MULTILIST:
            case ConfigureType.LIST:
                entity = new ConfigureListEntity();
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray(ConfigureManager.ENTRY);
                    CharSequence[] entrys = new CharSequence[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        entrys[i] = (CharSequence) array.get(i);
                    }
                    ((ConfigureListEntity) entity).setEntry(entrys);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ConfigureType.SWITCH:
                entity = new ConfigureListEntity();
                break;
            case ConfigureType.EDITOR:
                entity = new ConfigureEntity();
                break;
            default:
                entity = new ConfigureEntity();
                break;
        }
        entity.setKey(key);
        entity.setTitle(jsonObject.optString(ConfigureManager.TITLE));
        entity.setType(type);
        entity.setSummary(jsonObject.optString(ConfigureManager.SUMMARY));
        entity.setDefaultValue(jsonObject.opt(ConfigureManager.DEFAULT_VALUE));
        String cacheValue = ConfigureManager.getValue(key);
        if(TextUtils.equals(ConfigureManager.EMPTY, cacheValue)) {
            cacheValue = (String) jsonObject.opt(ConfigureManager.DEFAULT_VALUE);
        }
        entity.setValue(cacheValue);
        return entity;
    }

    /***
     * 解析json 得到所有的key value值
     * @return
     */
    public static HashMap<String, String> parseJson() {
        HashMap<String, String> hashMap = new HashMap<>();
        JSONObject object = null;
        try {
            object = new JSONObject(Constants.jsonDate);
            parseScreenData(null, object, hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    /***
     * 解析screen项 保存该screen所有的key value值
     * @param key
     * @param jsonObject
     */
    private static void parseScreenData(String key, JSONObject jsonObject, HashMap<String, String> hashMap) {
        ConfigureEntity entity = null;
        //读取第一屏的数据
        Iterator<String> keys = jsonObject.keys();
        try {
            if (key == null) {
                //读取第一屏screen的key;
                while (keys.hasNext()) {
                    key = keys.next();
                }
                jsonObject = jsonObject.getJSONObject(key);
            }
            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String childKey = keys.next();
                if (isItem(childKey)) {
                    JSONObject object = jsonObject.getJSONObject(childKey);
                    parseItemEntity(childKey, object, hashMap);
                }else if ("value".equals(childKey)) {
                    String value = jsonObject.optString(childKey);
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        hashMap.put(key, value);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /***
     * key是否属于item
     * @param key
     * @return
     */
    private  static boolean isItem(String key) {
        if ("type".equals(key)) {
           return false;
        } else if ("value".equals(key)) {
            return false;
        } else if ("summary".equals(key)) {
            return false;
        } else if ("title".equals(key)) {
            return false;
        } else {
            return true;
        }
    }

    /***
     * 解析item项 获取key value值
     * @param key
     * @param jsonObject
     */
    private static void parseItemEntity(String key, JSONObject jsonObject, HashMap<String, String> hashMap) {
        String type = jsonObject.optString(ConfigureManager.TYPE);
        switch (type) {
            case ConfigureType.SCREEN:
                parseScreenData(key, jsonObject, hashMap);
                break;
        }
        String value = null;
        Object dot = jsonObject.opt(ConfigureManager.DEFAULT_VALUE);
        Object vot = jsonObject.opt(ConfigureManager.VALUE);
        if (dot != null) {
            value = dot.toString();
        }else if (vot != null){
            value = vot.toString();

        }
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            hashMap.put(key, value);
        }
    }


    /***
     * 获取对应selectedKey的screen实体
     * @param key
     * @return
     */
    public static ConfigureEntity getScreen(String key) {
        try {
            JSONObject object = new JSONObject(Constants.jsonDate);
            if (key == null) {
                return parseData(null, object);
            } else {
                return  parseScreenData(null, key, object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     *
     * @param key  当前screen的key值
     * @param selectedKey 选择的screen的key值
     * @param jsonObject  当前screen的jsonObject
     * @return 对应selectedKey的screen实体
     */
    private static ConfigureEntity parseScreenData(String key,String selectedKey, JSONObject jsonObject) {
        ConfigureEntity entity = null;
        //读取第一屏的数据
        Iterator<String> keys = jsonObject.keys();
        try {
            if (key == null) {
                //读取第一屏screen的key;
                while (keys.hasNext()) {
                    key = keys.next();
                }
                jsonObject = jsonObject.getJSONObject(key);
            }
            keys = jsonObject.keys();
            boolean hasFinded = TextUtils.equals(key, selectedKey);
            LinkedList<ConfigureEntity> linkedList = new LinkedList<>();
            if (hasFinded) {
                entity = new ConfigureScreenEntity();
                entity.setKey(key);
            }
            while (keys.hasNext()) {
                String childKey = keys.next();
                String optString = jsonObject.optString(childKey);
                if(hasFinded) {
                   if (isItem(childKey)) {
                       JSONObject object = jsonObject.getJSONObject(childKey);
                       ConfigureEntity entity1 = parseEntity(childKey, object);
                       linkedList.add(entity1);
                   } else {
                       setEntity(entity, childKey, optString);
                   }
                } else {
                    if (isItem(childKey)) {
                        JSONObject object = jsonObject.getJSONObject(childKey);
                        String type = object.optString(ConfigureManager.TYPE);
                        if (TextUtils.equals(ConfigureType.SCREEN, type)) {
                           entity = parseScreenData(childKey, selectedKey, object);
                            if (entity != null) {
                                return entity;
                            }
                        }
                    }
                }
            }
            if (entity != null) {
                ((ConfigureScreenEntity)entity).setItemList(linkedList);
            }
            return entity;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 设置实体
     * @param entity
     * @param key
     * @param optString
     */
    private static void setEntity(ConfigureEntity entity,String key, String optString) {
        if ("type".equals(key)) {
            entity.setType(optString);
        } else if ("value".equals(key)) {
            entity.setValue(optString);
        } else if ("summary".equals(key)) {
            entity.setSummary(optString);
        } else if ("title".equals(key)) {
            entity.setTitle(optString);
        }
    }
}
