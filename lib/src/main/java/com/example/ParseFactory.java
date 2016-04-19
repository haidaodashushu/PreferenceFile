package com.example;

import org.xml.sax.Attributes;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * @author Administrator on 2016-04-14 11:07
 */
public class ParseFactory {


    /**
     * 属性解析
     * @param qName
     * @param attrs
     * @return
     */
    public static StringBuilder parseAttributes(String qName, Attributes attrs) {
        StringBuilder sb = new StringBuilder();

        //处理entry和item标签
        switch (qName) {
            case "configure":
//                return parseConfigureAttributes(qName,attrs);
                return sb;
            case "screen":
            case "category":
            case "multiList":
            case "list":
            case "editor":
                readAttributes(attrs,sb);
                break;
            case "switch":
                //添加"entry":[,
                AppendTools.appendWithQuotes(sb,"entry");
                AppendTools.appendObj(sb,":[");
                AppendTools.appendWithQuotes(sb,"true");
                AppendTools.appendObj(sb,",");
                AppendTools.appendWithQuotes(sb,false);
                AppendTools.appendObj(sb,"],");
                readAttributes(attrs,sb);
                break;
            case "entry":
                //添加"entry":[,
                AppendTools.appendWithQuotes(sb,"entry");
                AppendTools.appendObj(sb,":[");
                AppendTools.appendN(sb, 1);
                return sb;
            case "item":
                return sb;
        }



        //添加"type":"value",
        //添加的逗号会在endElement()方法中删除
        AppendTools.appendJson(sb,"type",qName);
        return sb;
    }


    private static void readAttributes(Attributes attrs,StringBuilder sb) {
        //如果没有key值，则会以当前时间作为key值，添加
        if (getAttrsIndex(attrs, "key")==-1) {
            //添加"key":{
            StringBuilder sbKey = new StringBuilder();
            AppendTools.appendWithQuotes(sbKey,System.currentTimeMillis());
            AppendTools.appendObj(sbKey,":{");
            AppendTools.appendN(sbKey, 1);
            //要将key值添加到第一项
            sb.insert(0, sbKey.toString());
        }
        for (int i = 0; i < attrs.getLength(); i++) {
            switch (attrs.getQName(i)) {
                case "key":
                    //添加"key":{
                    StringBuilder sbKey = new StringBuilder();
                    AppendTools.appendWithQuotes(sbKey,attrs.getValue(i));
                    AppendTools.appendObj(sbKey,":{");
                    AppendTools.appendN(sbKey, 1);
                    //要将key值添加到第一项
                    sb.insert(0, sbKey.toString());
                    break;
                case "title":
                    //添加"title":"value",
                    AppendTools.appendJson(sb,"title",attrs.getValue(i));
                    break;
                case "summary":
                    AppendTools.appendJson(sb,"summary",attrs.getValue(i));
                    break;
                //以defaultValue为准
                case "defaultValue":
                    //添加"defaultValue":"value",
                    AppendTools.appendJson(sb,"defaultValue",attrs.getValue(i));
                    AppendTools.appendJson(sb,"value",attrs.getValue(i));
                    break;
                /*case "value":
                    //添加"defaultValue":"value",
                    AppendTools.appendJson(sb,"value",attrs.getValue(i));
                    break;*/
                case "version":
                    break;
                case "mode":
                    break;
            }
        }
    }
    /**
     * 解析变量部分
     * @param qName
     * @param attrs
     * @return "key="value";
     */
    public static LinkedList<String> parseVariable(String qName, Attributes attrs, int mode) {
        LinkedList<String> variableList = new LinkedList<>();
        StringBuilder variableSb = new StringBuilder();
        //如果是正式版本，则不添加version变量，
        //如果是测试版本，如果不存在version,则默认为1
        if ("configure".equals(qName)&&mode ==1) {
            //添加version
            int index = getAttrsIndex(attrs,"version");
            AppendTools.appendKey(variableSb,"public static final String version");
            if (index == -1) {
                AppendTools.appendValue(variableSb, "1");
            } else {
                AppendTools.appendValue(variableSb,attrs.getValue(index));
            }
            variableList.add(variableSb.toString());
            //添加mode
            variableSb.delete(0,variableSb.length());
            AppendTools.appendKey(variableSb,"public static final String mode");
            AppendTools.appendValue(variableSb, mode);
            variableList.add(variableSb.toString());
        }

        variableSb.delete(0,variableSb.length());
        int index = getAttrsIndex(attrs,"key");
        if (index == -1) {
            return variableList;
        }
        String key = attrs.getValue(index);
        if (qName.equals("multiList")) {
            if (mode == 1) {
                key = "public static String[] " + key;
            } else {
                key = "public static final String[] " + key;
            }
        } else {
            if (mode == 1) {
                key = "public static String " + key;
            } else {
                key = "public static final String " + key;
            }
        }
        AppendTools.appendKey(variableSb,key);
        if (mode == 1) {
            //测试
            if (qName.equals("multiList")) {
                AppendTools.appendArray(variableSb, new String[]{attrs.getValue(index)});
            } else {
                AppendTools.appendValue(variableSb, attrs.getValue(index));
            }
        } else {
            //正式
            index = getAttrsIndex(attrs,"defaultValue");
            if (index == -1) {
                AppendTools.appendValue(variableSb,"");
            } else {
                if (qName.equals("multiList")) {
                    String arrays = attrs.getValue(index);
                    String[] spilt = arrays.split(",");
                    AppendTools.appendArray(variableSb, spilt);
                } else {
                    AppendTools.appendValue(variableSb,attrs.getValue(index));
                }
            }
        }

        variableList.add(variableSb.toString());
        return variableList;
    }

    /**
     * 返回该标签中的qName的下标
     * @param attrs
     * @param qName
     * @return
     */
    public static int getAttrsIndex(Attributes attrs,String qName) {
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(qName)) {
                return i;
            }
        }
        return -1;
    }

}
