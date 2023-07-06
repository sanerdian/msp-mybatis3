package com.jnetdata.msp.metadata.vo;

/**
 * <p>
 * MYSQL 数据库字段类型转换
 * </p>
 *
 * @author hubin
 * @date 2017-01-20
 */
public class DataTypeConvert {

    public static int getDbType(String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("text")) {
            return 2;
        } else if (t.contains("bigint")) {
            return 1;
        } else if (t.contains("int")) {
            return 1;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return 4;
        } else if (t.contains("text")) {
            return 2;
        } else if (t.contains("bit")) {
            return 2;
        } else if (t.contains("decimal")) {
            return 2;
        } else if (t.contains("clob")) {
            return 5;
        } else if (t.contains("blob")) {
            return 5;
        } else if (t.contains("binary")) {
            return 5;
        } else if (t.contains("float")) {
            return 3;
        } else if (t.contains("double")) {
            return 3;
        } else if (t.contains("json") || t.contains("enum")) {
            return 2;
        }
        return 2;
    }

    public static int getFieldType(String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("text")) {
            return 1;
        } else if (t.contains("bigint")) {
            return 1;
        } else if (t.contains("int")) {
            return 1;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return 4;
        } else if (t.contains("text")) {
            return 1;
        } else if (t.contains("bit")) {
            return 1;
        } else if (t.contains("decimal")) {
            return 1;
        } else if (t.contains("clob")) {
            return 3;
        } else if (t.contains("blob")) {
            return 3;
        } else if (t.contains("binary")) {
            return 1;
        } else if (t.contains("float")) {
            return 1;
        } else if (t.contains("double")) {
            return 1;
        } else if (t.contains("json") || t.contains("enum")) {
            return 1;
        }
        return 1;
    }
}
