/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config.rules;

/**
 * <p>
 * 表数据查询
 * </p>
 *
 * @author hubin, tangguo
 * @since 2016-04-25
 */
public enum QuerySQL {
    MYSQL("mysql", "show tables", "show table status"
            , "SELECT a.column_name FIELD,a.column_type TYPE,IFNULL(b.ANOTHERNAME,a.column_comment) COMMENT,column_key `KEY`,a.Extra,\n"
                    + "if(b.DBTYPE>0,b.MATCH_TYPE,b.DBTYPE) MATCH_TYPE,b.FIELDTYPE,c.TABLENAME JOINTABLE,d.FIELDNAME JOINFIELD\n"
                    + "from information_schema.`COLUMNS` a \n"
                    + "LEFT JOIN dbfieldinfo b ON UPPER(a.column_name) = UPPER(b.FIELDNAME) AND UPPER(a.TABLE_NAME) = UPPER(b.TABLENAME) \n"
                    + "LEFT JOIN tableinfo c on b.JOINTABLE = c.TABLEINFOID \n"
                    + "LEFT JOIN dbfieldinfo d on b.JOINFIELD = d.DBFIELDINFOID\n"
                    + "WHERE UPPER(a.TABLE_NAME)=UPPER('%s') AND a.TABLE_SCHEMA = (select database())"
//            ,"SELECT a.FIELDNAME, if(a.DBTYPE>0,a.MATCH_TYPE,a.DBTYPE) MATCH_TYPE, a.FIELDTYPE, b.TABLENAME JOINTABLE, c.FIELDNAME JOINFIELD  " +
//            "FROM DBFIELDINFO a  LEFT JOIN tableinfo b on a.JOINTABLE = b.TABLEINFOID LEFT JOIN dbfieldinfo c on a.JOINFIELD = c.DBFIELDINFOID " +
//            "WHERE a.TABLENAME = '%s'  AND a.MATCH_TYPE IS NOT NULL"
    ),

    ORACLE("oracle", "SELECT * FROM USER_TABLES", "SELECT TABLE_NAME NAME,DECODE(TABLE_TYPE,'VIEW','VIEW',COMMENTS) as \"COMMENT\" FROM USER_TAB_COMMENTS UNION ALL SELECT view_name NAME,'VIEW' as \"COMMENT\" from user_views",
            "SELECT A.COLUMN_NAME FIELD, CASE WHEN A.DATA_TYPE='NUMBER' THEN "
                    + "(CASE WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE "
                    + "WHEN NVL(A.DATA_SCALE, 0) > 0 THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')' "
                    + "ELSE A.DATA_TYPE||'('||A.DATA_PRECISION||')' END) "
                    + "ELSE A.DATA_TYPE END TYPE, B.COMMENTS \"COMMENT\",DECODE(C.POSITION, '1', 'PRI') KEY "
                    + ",'0' MATCH_TYPE,'0' FIELDTYPE,'0' JOINTABLE,'0' JOINFIELD\n"
                    + "FROM USER_TAB_COLUMNS A INNER JOIN USER_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME"
                    + " AND A.COLUMN_NAME = B.COLUMN_NAME LEFT JOIN USER_CONSTRAINTS D "
                    + "ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' "
                    + "LEFT JOIN USER_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME "
                    + "AND C.COLUMN_NAME=A.COLUMN_NAME WHERE A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID "
//            ,"SELECT a.FIELDNAME, DECODE(SIGN(a.DBTYPE),1,a.MATCH_TYPE,-1,a.DBTYPE,a.MATCH_TYPE) MATCH_TYPE, a.FIELDTYPE, b.TABLENAME JOINTABLE, c.FIELDNAME JOINFIELD  " +
//            "FROM DBFIELDINFO a  LEFT JOIN tableinfo b on a.JOINTABLE = b.TABLEINFOID LEFT JOIN dbfieldinfo c on a.JOINFIELD = c.DBFIELDINFOID " +
//            "WHERE a.TABLENAME = '%s'  AND a.MATCH_TYPE IS NOT NULL"
    ),

    DM("dm", "SELECT * FROM USER_TABLES", "SELECT TABLE_NAME NAME,DECODE(TABLE_TYPE,'VIEW','VIEW',COMMENTS) COMMENT FROM USER_TAB_COMMENTS",
            "SELECT A.COLUMN_NAME FIELD, CASE WHEN A.DATA_TYPE='NUMBER' THEN "
                    + "(CASE WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE "
                    + "WHEN NVL(A.DATA_SCALE, 0) > 0 THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')' "
                    + "ELSE A.DATA_TYPE||'('||A.DATA_PRECISION||')' END) "
                    + "ELSE A.DATA_TYPE END TYPE, B.COMMENTS COMMENT,DECODE(C.POSITION, '1', 'PRI') KEY "
                    + ",'0' MATCH_TYPE,'0' FIELDTYPE,'0' JOINTABLE,'0' JOINFIELD\n"
                    + "FROM USER_TAB_COLUMNS A INNER JOIN USER_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME"
                    + " AND A.COLUMN_NAME = B.COLUMN_NAME LEFT JOIN USER_CONSTRAINTS D "
                    + "ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' "
                    + "LEFT JOIN USER_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME "
                    + "AND C.COLUMN_NAME=A.COLUMN_NAME WHERE A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID "
//            ,"SELECT a.FIELDNAME, DECODE(SIGN(a.DBTYPE),1,a.MATCH_TYPE,-1,a.DBTYPE,a.MATCH_TYPE) MATCH_TYPE, a.FIELDTYPE, b.TABLENAME JOINTABLE, c.FIELDNAME JOINFIELD  " +
//            "FROM DBFIELDINFO a  LEFT JOIN tableinfo b on a.JOINTABLE = b.TABLEINFOID LEFT JOIN dbfieldinfo c on a.JOINFIELD = c.DBFIELDINFOID " +
//            "WHERE a.TABLENAME = '%s'  AND a.MATCH_TYPE IS NOT NULL"
    ),

    KINGBASE("kingbase", "SELECT * FROM USER_TABLES", "select * from (SELECT TABLE_NAME NAME,DECODE(TABLE_TYPE,'VIEW','VIEW',COMMENTS) as COMMENT FROM USER_TAB_COMMENTS UNION ALL SELECT view_name NAME,'VIEW' as COMMENT from user_views) t WHERE t.name in ('%s')",
            "SELECT\n"
                    + "    A.COLUMN_NAME FIELD,\n"
                    + "    CASE WHEN A.DATA_TYPE='NUMERIC'\n"
                    + "        THEN\n"
                    + "            (CASE WHEN A.DATA_PRECISION IS NULL\n"
                    + "                    THEN A.DATA_TYPE\n"
                    + "                WHEN NVL(A.DATA_SCALE, 0) > 0\n"
                    + "                    THEN 'NUMBER'||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')'\n"
                    + "                ELSE 'NUMBER'||'('||A.DATA_PRECISION||')'\n"
                    + "            END)\n"
                    + "    ELSE A.DATA_TYPE\n"
                    + "    END TYPE\n"
                    + ", B.COMMENTS AS COMMENT\n"
                    + ",DECODE(D.CONSTRAINT_TYPE,'P','PRI') KEY\n"
                    + ",D.CONSTRAINT_TYPE\n"
                    + ",C.POSITION\n"
                    + ",F.MATCH_TYPE,F.FIELDTYPE,'0' JOINTABLE,'0' JOINFIELD\n"
                    + "FROM ALL_TAB_COLUMNS A\n"
                    + "INNER JOIN USER_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME\n"
                    + "LEFT JOIN USER_CONS_COLUMNS C ON C.COLUMN_NAME=A.COLUMN_NAME AND C.TABLE_NAME = A.TABLE_NAME\n"
                    + "LEFT JOIN USER_CONSTRAINTS D ON upper(C.CONSTRAINT_NAME) = D.CONSTRAINT_NAME AND D.TABLE_NAME = C.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P'\n"
                    + "LEFT JOIN dbfieldinfo F ON UPPER(a.COLUMN_NAME) = UPPER(F.FIELDNAME) AND UPPER(a.TABLE_NAME) = UPPER(F.TABLENAME)\n"
                    + "WHERE A.TABLE_NAME = '%s'\n"
                    + "ORDER BY A.COLUMN_ID"
//            ,"SELECT a.FIELDNAME, DECODE(SIGN(a.DBTYPE),1,a.MATCH_TYPE,-1,a.DBTYPE,a.MATCH_TYPE) MATCH_TYPE, a.FIELDTYPE, b.TABLENAME JOINTABLE, c.FIELDNAME JOINFIELD  " +
//            "FROM DBFIELDINFO a  LEFT JOIN tableinfo b on a.JOINTABLE = b.TABLEINFOID LEFT JOIN dbfieldinfo c on a.JOINFIELD = c.DBFIELDINFOID " +
//            "WHERE a.TABLENAME = '%s'  AND a.MATCH_TYPE IS NOT NULL"
            ),


	SQL_SERVER("sql_server",
			"select cast(name as varchar(500)) as NAME from sysObjects where (xtype='U' or xtype='v') order by name",
			"select " +
					"cast(so.name as varchar(500)) as NAME, " +
                    "CASE\n" +
                    "WHEN so.type= 'v' THEN\n" +
                    "'view' ELSE CAST ( sep.value AS VARCHAR ( 500 ) ) \n" +
                    "END COMMENT  " +
					"from sysobjects so " +
					"left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0 " +
					"where (xtype='U' or xtype='v')",
			"SELECT  cast(a.NAME AS VARCHAR(500)) AS TABLE_NAME,cast(b.NAME AS VARCHAR(500)) AS FIELD, "
					+ "cast(c.VALUE AS VARCHAR(500)) AS COMMENT,cast(sys.types.NAME AS VARCHAR (500)) AS TYPE,"
					+ "(" + " SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END"
                    + ",'0' MATCH_TYPE,'0' FIELDTYPE,'0' JOINTABLE,'0' JOINFIELD\n"
					+ " FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes "
					+ " WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (A.NAME) AND sysobjects.xtype = 'PK'"
					+ " AND sysobjects.parent_obj = syscolumns.id " + " AND sysindexes.id = syscolumns.id "
					+ " AND sysobjects.NAME = sysindexes.NAME AND sysindexkeys.id = syscolumns.id "
					+ " AND sysindexkeys.indid = sysindexes.indid "
					+ " AND syscolumns.colid = sysindexkeys.colid AND syscolumns.NAME = B.NAME) as 'KEY',"
					+ "  b.is_identity isIdentity "
					+ " FROM ( select name,object_id from sys.tables UNION all select name,object_id from sys.views ) a "
					+ " INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id "
					+ " LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id   "
					+ " LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id "
					+ " WHERE a.NAME = '%s' and sys.types.NAME !='sysname' "
//            ,"SELECT\n" +
//                    "\ta.FIELDNAME,\n" +
//                    "case \n" +
//                    "    when a.DBTYPE> 0 then a.MATCH_TYPE\n" +
//                    "    else a.DBTYPE\n" +
//                    "end\t\n" +
//                    "\t MATCH_TYPE,\n" +
//                    "\ta.FIELDTYPE,\n" +
//                    "\tb.TABLENAME JOINTABLE,\n" +
//                    "\tc.FIELDNAME JOINFIELD \n" +
//                    "FROM\n" +
//                    "\tDBFIELDINFO a\n" +
//                    "\tLEFT JOIN tableinfo b ON a.JOINTABLE = b.TABLEINFOID\n" +
//                    "\tLEFT JOIN dbfieldinfo c ON a.JOINFIELD = c.DBFIELDINFOID \n" +
//                    "WHERE\n" +
//                    "\ta.TABLENAME = '%s' \n" +
//                    "\tAND a.MATCH_TYPE IS NOT NULL"
                    ),

    POSTGRE_SQL("postgresql", "select tablename NAME from pg_tables where schemaname='%s' ORDER BY tablename",
            "SELECT A.tablename NAME, obj_description(relfilenode, 'pg_class') AS COMMENT FROM pg_tables A, pg_class B WHERE A.schemaname='public' AND A.tablename = B.relname UNION ALL SELECT a.viewname as NAME,'view' as COMMENT FROM pg_views a LEFT JOIN pg_class b on a.viewname = b.relname WHERE a.schemaname ='public'",
            "SELECT A.attname AS name, format_type(A.atttypid, A.atttypmod) AS type,col_description(A.attrelid, A.attnum) AS COMMENT, (CASE C.contype WHEN 'p' THEN 'PRI' ELSE '' END) AS KEY " +
                    "FROM pg_attribute A LEFT JOIN pg_constraint C ON A.attnum = C.conkey[1] AND A.attrelid = C.conrelid "
                    + ",'0' MATCH_TYPE,'0' FIELDTYPE,'0' JOINTABLE,'0' JOINFIELD\n" +
                    "WHERE  A.attrelid = lower('%s.%s')::regclass AND A.attnum > 0 AND NOT A.attisdropped ORDER  BY A.attnum"
//            ,"SELECT A.FIELDNAME,CASE WHEN A.DBTYPE > 0 THEN A.MATCH_TYPE ELSE A.DBTYPE END MATCH_TYPE, A.FIELDTYPE, B.TABLENAME JOINTABLE, C.FIELDNAME JOINFIELD FROM DBFIELDINFO A LEFT JOIN tableinfo b ON A.JOINTABLE = b.TABLEINFOID LEFT JOIN dbfieldinfo C ON A.JOINFIELD = C.DBFIELDINFOID WHERE A.TABLENAME = '%s' AND A.MATCH_TYPE IS NOT NULL"
            );

    private final String dbType;
    private final String tablesSql;
    private final String tableCommentsSql;
    private final String tableFieldsSql;

    QuerySQL(final String dbType, final String tablesSql, final String tableCommentsSql, final String tableFieldsSql) {
        this.dbType = dbType;
        this.tablesSql = tablesSql;
        this.tableCommentsSql = tableCommentsSql;
        this.tableFieldsSql = tableFieldsSql;
    }

    public String getDbType() {
        return dbType;
    }

    public String getTablesSql() {
        return tablesSql;
    }

    public String getTableCommentsSql() {
        return tableCommentsSql;
    }

    public String getTableFieldsSql() {
        return tableFieldsSql;
    }

}
