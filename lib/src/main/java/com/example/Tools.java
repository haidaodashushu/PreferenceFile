package com.example;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Administrator on 2016-04-11 15:09
 */
public class Tools {
    public static String packageStr = "";
    /**debug模式为1，release模式为0*/
    public static int mode = 1;
    public static void main(String[] args) {
        String xmlPath = "D:\\workspace_zk\\PreferenceFile\\app\\src\\main\\assets\\config.cfg";
        String javaPath = "D:\\workspace_zk\\PreferenceFile\\app\\src\\main\\java\\com\\example\\wangzhengkui\\preferencefile\\preference\\entity\\Constants.java";
        packageStr = "package com.example.wangzhengkui.preferencefile.preference.entity";
        config2Json(xmlPath, javaPath);
    }

    public static void config2Json(String path, String javaPath) {
        StringBuilder jsonSb = new StringBuilder();
        LinkedList<String> variableList = new LinkedList<>();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            InputStream in = new FileInputStream(path);
            MyXmlHandler handler = new MyXmlHandler(jsonSb, variableList, javaPath);
            parser.parse(in, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class MyXmlHandler extends DefaultHandler {
        StringBuilder jsonSb;
        LinkedList<String> lineList;

        String javaPath;
        String currentElement = "";

        public MyXmlHandler(StringBuilder jsonSb, LinkedList<String> variableList, String javaPath) {
            this.jsonSb = jsonSb;
            this.lineList = variableList;
            this.javaPath = javaPath;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            if (mode == 1) {
                jsonSb.append("{");
                AppendTools.appendN(jsonSb, 1);
            }
        }

        /**
         * 文档解析结束
         *
         * @throws SAXException
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            if (mode == 1) {
                //删除末尾的逗号
                deleteComma(jsonSb);
                jsonSb.append("}");
            }
            System.out.println(jsonSb.toString());
            //生成类
            constructClass(javaPath, lineList,jsonSb,mode);
        }

        /**
         * 开始解析元素标签
         *
         * @param uri
         * @param localName
         * @param qName
         * @param attributes
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            currentElement = qName;

            if ("configure".equals(qName)) {
                int index = ParseFactory.getAttrsIndex(attributes, "mode");
                if (index == -1) {
                    mode = 1;
                } else {
                    try {
                        mode = Integer.parseInt(attributes.getValue(index));
                    } catch (Exception e) {
                        mode = 1;
                    }
                }
            }

            if (mode == 1) {
                //解析json部分
                jsonSb.append(ParseFactory.parseAttributes(qName, attributes));
            }
            //解析变量部分
            lineList.addAll(ParseFactory.parseVariable(qName,attributes,mode));

        }

        /**
         * 元素标签解析结束
         *
         * @param uri
         * @param localName
         * @param qName
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (mode == 1) {
                if ("entry".equals(qName)) {
                    //先元素末尾删除逗号
                    deleteComma(jsonSb);
                    //表示每个array对象的结束，添加的逗号会在文档解析完成之后删除末尾的逗号
                    jsonSb.append("],");
                    AppendTools.appendN(jsonSb, 1);
                } else if (!"item".equals(qName)&&!"configure".equals(qName)) {
                    //先元素末尾删除逗号
                    deleteComma(jsonSb);
                    //表示每个json对象的结束，添加的逗号会在文档解析完成之后删除末尾的逗号
                    jsonSb.append("},");
                    AppendTools.appendN(jsonSb, 1);
                }
                currentElement = "";
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if ("item".equals(currentElement)) {
                AppendTools.appendWithQuotes(jsonSb, new String(ch, start, length));
                jsonSb.append(",");
            }
        }

        /**
         * 删除sb中的末尾的逗号，如果末尾不是逗号，则不删除
         *
         * @param sb
         */
        public void deleteComma(StringBuilder sb) {
            int index = sb.lastIndexOf(",");
            if (index != sb.length() - 1) {
                String end = sb.substring(index + 1, sb.length());
                //如果是换行
                if (end.matches("\\*\n")) {
                    sb.replace(index, index + 1, "");
                }
            } else if (index == sb.length() - 1) {
                sb.deleteCharAt(sb.length() - 1);
            }

        }
    }

    public static String getClassName(String path) {
        try {
            String className = path.substring(path.lastIndexOf("\\") + 1);
            className = className.split("\\.")[0];
            return className;
        } catch (Exception e) {
//            System.out.println("输入path参数错误，请输入文件路径，而不是文件夹路径");
            System.err.println("input \"path\" params error , please input file path, not directory path");
        }
        return null;
    }

    /**
     * 构造类结构
     * @param path
     * @param lineList 所有的变量声明
     * @param mode
     */
    public static void constructClass(String path,LinkedList<String> lineList,StringBuilder jsonSb,int mode) {
        //构造变量
        StringBuilder variableSb = new StringBuilder();
        //添加jsonData变量
            lineList.addFirst("public static final String jsonDate = "+"\""+jsonSb.toString()+"\";");

        /*for (int i = 0; i < lineList.size(); i++) {
            if (mode == 1) {
                lineList.set(i,"public static String " + lineList.get(i)+"\n");
                //测试包
            } else {
                //正式包
                lineList.set(i,"public static final String " + lineList.get(i)+"\n");
            }
        }*/


        //给所有的变量定义添加制表符和换行符
        for (int i = 0; i < lineList.size(); i++) {
            lineList.set(i,"\t\t"+lineList.get(i)+"\n");
        }

        File file = new File(path);
        if (file.isDirectory()) {
//            System.out.println("输入path参数错误，请输入文件路径，而不是文件夹路径");
            System.err.println("input \"path\" params error , please input file path, not directory path");
            return;
        }
        String className = getClassName(path);
        if (className == null||"".equals(className)) {
            return;
        }
        lineList.addFirst(packageStr + ";\n\n");
        lineList.add(1,"public class " + className + "{\n");


        lineList.addLast("\n}");

        //写入类
        write(path, lineList);
    }

    /**
     * 将value的值写入文件
     * @param path
     * @param value 存放类的代码
     */
    public static void write(String path, LinkedList<String> value) {
        File file = new File(path);
        System.out.println(value);
        OutputStream os = null;
        BufferedWriter bw = null;
        try {
            os = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            for (int i = 0; i < value.size(); i++) {
                bw.write(value.get(i));
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e1) {
                }
            }
        }
    }


}
