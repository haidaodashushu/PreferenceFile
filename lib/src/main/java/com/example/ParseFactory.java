package com.example;

import org.xml.sax.Attributes;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * @author Administrator on 2016-04-14 11:07
 */
public class ParseFactory {


    /**
     * ���Խ���
     * @param qName
     * @param attrs
     * @return
     */
    public static StringBuilder parseAttributes(String qName, Attributes attrs) {
        StringBuilder sb = new StringBuilder();

        //����entry��item��ǩ
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
                //���"entry":[,
                AppendTools.appendWithQuotes(sb,"entry");
                AppendTools.appendObj(sb,":[");
                AppendTools.appendWithQuotes(sb,"true");
                AppendTools.appendObj(sb,",");
                AppendTools.appendWithQuotes(sb,false);
                AppendTools.appendObj(sb,"],");
                readAttributes(attrs,sb);
                break;
            case "entry":
                //���"entry":[,
                AppendTools.appendWithQuotes(sb,"entry");
                AppendTools.appendObj(sb,":[");
                AppendTools.appendN(sb, 1);
                return sb;
            case "item":
                return sb;
        }



        //���"type":"value",
        //��ӵĶ��Ż���endElement()������ɾ��
        AppendTools.appendJson(sb,"type",qName);
        return sb;
    }


    private static void readAttributes(Attributes attrs,StringBuilder sb) {
        //���û��keyֵ������Ե�ǰʱ����Ϊkeyֵ�����
        if (getAttrsIndex(attrs, "key")==-1) {
            //���"key":{
            StringBuilder sbKey = new StringBuilder();
            AppendTools.appendWithQuotes(sbKey,System.currentTimeMillis());
            AppendTools.appendObj(sbKey,":{");
            AppendTools.appendN(sbKey, 1);
            //Ҫ��keyֵ��ӵ���һ��
            sb.insert(0, sbKey.toString());
        }
        for (int i = 0; i < attrs.getLength(); i++) {
            switch (attrs.getQName(i)) {
                case "key":
                    //���"key":{
                    StringBuilder sbKey = new StringBuilder();
                    AppendTools.appendWithQuotes(sbKey,attrs.getValue(i));
                    AppendTools.appendObj(sbKey,":{");
                    AppendTools.appendN(sbKey, 1);
                    //Ҫ��keyֵ��ӵ���һ��
                    sb.insert(0, sbKey.toString());
                    break;
                case "title":
                    //���"title":"value",
                    AppendTools.appendJson(sb,"title",attrs.getValue(i));
                    break;
                case "summary":
                    AppendTools.appendJson(sb,"summary",attrs.getValue(i));
                    break;
                //��defaultValueΪ׼
                case "defaultValue":
                    //���"defaultValue":"value",
                    AppendTools.appendJson(sb,"defaultValue",attrs.getValue(i));
                    AppendTools.appendJson(sb,"value",attrs.getValue(i));
                    break;
                /*case "value":
                    //���"defaultValue":"value",
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
     * ������������
     * @param qName
     * @param attrs
     * @return "key="value";
     */
    public static LinkedList<String> parseVariable(String qName, Attributes attrs, int mode) {
        LinkedList<String> variableList = new LinkedList<>();
        StringBuilder variableSb = new StringBuilder();
        //�������ʽ�汾�������version������
        //����ǲ��԰汾�����������version,��Ĭ��Ϊ1
        if ("configure".equals(qName)&&mode ==1) {
            //���version
            int index = getAttrsIndex(attrs,"version");
            AppendTools.appendKey(variableSb,"public static final String version");
            if (index == -1) {
                AppendTools.appendValue(variableSb, "1");
            } else {
                AppendTools.appendValue(variableSb,attrs.getValue(index));
            }
            variableList.add(variableSb.toString());
            //���mode
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
            //����
            if (qName.equals("multiList")) {
                AppendTools.appendArray(variableSb, new String[]{attrs.getValue(index)});
            } else {
                AppendTools.appendValue(variableSb, attrs.getValue(index));
            }
        } else {
            //��ʽ
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
     * ���ظñ�ǩ�е�qName���±�
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
