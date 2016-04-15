package com.example.wangzhengkui.preferencefile.manager;

import com.example.wangzhengkui.preferencefile.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureListEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Administrator on 2016-04-12 10:40
 */
public class DataParseTools {
    public static ConfigureEntity parseData(String key, JSONObject jsonObject) {
        ConfigureEntity entity = null;
        //读取第一屏的数据
        if (key == null) {
            ConfigureScreenEntity screenEntity = new ConfigureScreenEntity();
            Iterator<String> keys = jsonObject.keys();
            LinkedList<ConfigureEntity> linkedList = new LinkedList<>();
            //读取第一屏screen的key;
            while (keys.hasNext()) {
                key = keys.next();
                screenEntity.setKey(key);
            }

            try {
                //读取第一屏所对应的json
                jsonObject = jsonObject.getJSONObject(key);
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
        }

        return entity;
    }

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
            case ConfigureType.LIST:
                entity = new ConfigureListEntity();
                JSONArray array = null;
                try {
                    array = jsonObject.getJSONArray(ConfigureManager.ENTRY);
                    CharSequence[] entrys = new CharSequence[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        entrys[i] = (CharSequence)array.get(i);
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
        entity.setValue(jsonObject.opt(ConfigureManager.DEFAULT_VALUE));

        return entity;
    }
}
