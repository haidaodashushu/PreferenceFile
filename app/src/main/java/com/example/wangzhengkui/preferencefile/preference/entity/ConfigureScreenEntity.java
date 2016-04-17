package com.example.wangzhengkui.preferencefile.preference.entity;

import java.util.LinkedList;

/**
 * @author Administrator on 2016-04-12 10:10
 */
public class ConfigureScreenEntity extends ConfigureEntity {
    LinkedList<ConfigureEntity> itemList;

    public LinkedList<ConfigureEntity> getItemList() {
        return itemList;
    }

    public void setItemList(LinkedList<ConfigureEntity> itemList) {
        this.itemList = itemList;
    }
}
