package com.jnetdata.msp.metadata.vo;

public enum DbType {

    MYSQL_LONG("mysql","整数","BIGINT",1)
    ,MYSQL_STRING("mysql","字符串","VARCHAR",2)
    ,MYSQL_DOUBLE("mysql","小数","DOUBLE",3)
    ,MYSQL_DATETIME("mysql","时间","DATETIME",4)
    ,MYSQL_LONGTEXT("mysql","大文本","LONGTEXT",5)

    ,ORACLE_LONG("oracle","整数","NUMBER",1)
    ,ORACLE_STRING("oracle","字符串","VARCHAR",2)
    ,ORACLE_DOUBLE("oracle","小数","NUMBER",3)
    ,ORACLE_DATETIME("oracle","时间","DATE",4)
    ,ORACLE_LONGTEXT("oracle","大文本","NCLOB",5)

    ,DM_LONG("dm","整数","NUMBER",1)
    ,DM_STRING("dm","字符串","VARCHAR",2)
    ,DM_DOUBLE("dm","小数","NUMBER",3)
    ,DM_DATETIME("dm","时间","DATE",4)
    ,DM_LONGTEXT("dm","大文本","NCLOB",5);

    String name;
    String type;
    int no;
    String dbtype;

    DbType(String dbtype, String name, String type, int no) {
        this.dbtype = dbtype;
        this.name = name;
        this.type = type;
        this.no = no;
    }

    public static String getType(String dbtype,int no){
        for (DbType c : DbType.values()) {
            if (dbtype.equals(c.dbtype) && c.no == no) {
                return c.type;
            }
        }
        return null;
    }
}
