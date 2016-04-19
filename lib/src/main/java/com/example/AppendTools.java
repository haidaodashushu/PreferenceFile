package com.example;

/**
 * @author Administrator on 2016-04-14 11:09
 */
public class AppendTools {
    public static void appendKey(StringBuilder variableSb,String key) {
        variableSb.append(key);
        variableSb.append("=");

    }
    public static void appendArray(StringBuilder variableSb,Object[] value) {
        appendObj(variableSb,"{");
        for (int i = 0; i < value.length; i++) {
            appendObj(variableSb,"\""+value[i]+"\"");
            if (i < value.length - 1) {
                appendObj(variableSb,",");
            }
        }
        appendObj(variableSb,"}");
        appendObj(variableSb,";");
    }
    public static void appendValue(StringBuilder variableSb,Object value) {

        appendObj(variableSb,"\""+value+"\"");
        appendObj(variableSb,";");
    }
    public static void appendJson(StringBuilder sb,String key,String value) {
        appendWithQuotes(sb,key);
        appendObj(sb,":");
        appendWithQuotes(sb,value);
        appendObj(sb,",");
        appendN(sb, 1);
    }
    /**
     * @param sb
     * @param value
     */
    public static void appendWithQuotes(StringBuilder sb,Object value) {
        sb.append("\\\"");
        sb.append(value);
        sb.append("\\\"");
    }
    public static  void appendObj(StringBuilder sb, Object value) {
        sb.append(value);
    }
    /**
     * 添加换行
     *
     * @param sb
     * @param count
     */
    public static void appendN(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
//                sb.append("\n");
        }
    }
}
