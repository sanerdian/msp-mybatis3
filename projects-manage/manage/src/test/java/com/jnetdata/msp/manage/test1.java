//package com.jnetdata.msp.manage;
//
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.lib.Ref;
////import org.junit.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class test1 {
//
//
//        /*// 默认单元格内容为数字时格式
//        private static DecimalFormat df = new DecimalFormat("0");
//        // 默认单元格格式化日期字符串
//        private static SimpleDateFormat sdf = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss");
//        // 格式化数字
//        private static DecimalFormat nf = new DecimalFormat("0.00");
//
//        @Test
//        public static ArrayList<ArrayList<Object>> readExcel() {
//            File file = new File("");
//            if (file == null) {
//                return null;
//            }
//            if (file.getName().endsWith("xlsx")) {
//                // 处理ecxel2007
//                return readExcel2013(file);
//            } else {
//                // 处理ecxel2013
//                return readExcel2003(file);
//            }
//        }
//
//        *//*
//         * @return 将返回结果存储在ArrayList内，存储结构与二位数组类似
//         * lists.get(0).get(0)表示过去Excel中0行0列单元格
//         *//*
//        public static ArrayList<ArrayList<Object>> readExcel2003(File file) {
//            try {
//                ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
//                ArrayList<Object> colList;
//                HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
//                HSSFSheet sheet = wb.getSheetAt(0);
//                HSSFRow row;
//                HSSFCell cell;
//                Object value;
//                for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet
//                        .getPhysicalNumberOfRows(); i++) {
//                    row = sheet.getRow(i);
//                    colList = new ArrayList<Object>();
//                    if (row == null) {
//                        // 当读取行为空时
//                        if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
//                            rowList.add(colList);
//                        }
//                        continue;
//                    } else {
//                        rowCount++;
//                    }
//                    for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//                        cell = row.getCell(j);
//                        if (cell == null
//                                || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//                            // 当该单元格为空
//                            if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
//                                colList.add("");
//                            }
//                            continue;
//                        }
//                        switch (cell.getCellType()) {
//                            case XSSFCell.CELL_TYPE_STRING:
//
//                                value = cell.getStringCellValue();
//                                break;
//                            case XSSFCell.CELL_TYPE_NUMERIC:
//                                if ("@".equals(cell.getCellStyle()
//                                        .getDataFormatString())) {
//                                    value = df.format(cell.getNumericCellValue());
//                                } else if ("General".equals(cell.getCellStyle()
//                                        .getDataFormatString())) {
//                                    value = nf.format(cell.getNumericCellValue());
//                                } else {
//                                    value = sdf.format(HSSFDateUtil.getJavaDate(cell
//                                            .getNumericCellValue()));
//                                }
//                                break;
//                            case XSSFCell.CELL_TYPE_BOOLEAN:
//                                value = Boolean.valueOf(cell.getBooleanCellValue());
//                                break;
//                            case XSSFCell.CELL_TYPE_BLANK:
//                                value = "";
//                                break;
//                            default:
//                                value = cell.toString();
//                        }// end switch
//                        colList.add(value);
//                    }// end for j
//                    rowList.add(colList);
//                }// end for i
//
//                return rowList;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        public static ArrayList<ArrayList<Object>> readExcel2013(File file) {
//            try {
//                ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
//                ArrayList<Object> colList;
//                XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
//                XSSFSheet sheet = wb.getSheetAt(0);
//                XSSFRow row;
//                XSSFCell cell;
//                Object value;
//                for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet
//                        .getPhysicalNumberOfRows(); i++) {
//                    row = sheet.getRow(i);
//                    colList = new ArrayList<Object>();
//                    if (row == null) {
//                        // 当读取行为空时
//                        if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
//                            rowList.add(colList);
//                        }
//                        continue;
//                    } else {
//                        rowCount++;
//                    }
//                    for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//                        cell = row.getCell(j);
//                        if (cell == null
//                                || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//                            // 当该单元格为空
//                            if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
//                                colList.add("");
//                            }
//                            continue;
//                        }
//                        switch (cell.getCellType()) {
//                            case XSSFCell.CELL_TYPE_STRING:
//
//                                value = cell.getStringCellValue();
//                                break;
//                            case XSSFCell.CELL_TYPE_NUMERIC:
//                                if ("@".equals(cell.getCellStyle()
//                                        .getDataFormatString())) {
//                                    value = df.format(cell.getNumericCellValue());
//                                } else if ("General".equals(cell.getCellStyle()
//                                        .getDataFormatString())) {
//                                    value = nf.format(cell.getNumericCellValue());
//                                } else {
//                                    value = sdf.format(HSSFDateUtil.getJavaDate(cell
//                                            .getNumericCellValue()));
//                                }
//
//                                break;
//                            case XSSFCell.CELL_TYPE_BOOLEAN:
//
//                                value = Boolean.valueOf(cell.getBooleanCellValue());
//                                break;
//                            case XSSFCell.CELL_TYPE_BLANK:
//
//                                value = "";
//                                break;
//                            default:
//
//                                value = cell.toString();
//                        }// end switch
//                        colList.add(value);
//                    }// end for j
//                    rowList.add(colList);
//                }// end for i
//
//                return rowList;
//            } catch (Exception e) {
//                System.out.println("exception");
//                return null;
//            }
//        }
//*/
//
//    /*@Test
//    public void test7 () throws Exception {
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        Connection conn = DriverManager
//                .getConnection("jdbc:mysql://47.93.151.74:13306/fastdev?user=fastdev&password=Jnetdata.mysql.20190316&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&autoReconnect=true");
//        DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
//
//        ResultSet  rs = meta.getColumns(null, "%", "view_jmeta_ddd", "%");
//
//        while (rs.next())  {
//            // table catalog (may be null)
//            String tableCat = rs.getString("TABLE_CAT");
//            // table schema (may be null)
//            String tableSchemaName = rs.getString("TABLE_SCHEM");
//            // table name
//            String tableName_ = rs.getString("TABLE_NAME");
//            // column name
//            String columnName = rs.getString("COLUMN_NAME");
//
//            // SQL type from java.sql.Types
//            int dataType = rs.getInt("DATA_TYPE");
//
//            // Data source dependent type name, for a UDT the type name is
//            // fully qualified
//            String dataTypeName = rs.getString("TYPE_NAME");
//            System.out.println(columnName + "    " + dataTypeName);
//            // table schema (may be null)
//            int columnSize = rs.getInt("COLUMN_SIZE");
//            // the number of fractional digits. Null is returned for data
//            // types where DECIMAL_DIGITS is not applicable.
//            int decimalDigits = rs.getInt("DECIMAL_DIGITS");
//            // Radix (typically either 10 or 2)
//            int numPrecRadix = rs.getInt("NUM_PREC_RADIX");
//            // is NULL allowed.
//            int nullAble = rs.getInt("NULLABLE");
//            // comment describing column (may be null)
//            String remarks = rs.getString("REMARKS");
//            // default value for the column, which should be interpreted as
//            // a string when the value is enclosed in single quotes (may be
//            // null)
//            String columnDef = rs.getString("COLUMN_DEF");
//            //
//            int sqlDataType = rs.getInt("SQL_DATA_TYPE");
//            //
//            int sqlDatetimeSub = rs.getInt("SQL_DATETIME_SUB");
//            // for char types the maximum number of bytes in the column
//            int charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");
//            // index of column in table (starting at 1)
//            int ordinalPosition = rs.getInt("ORDINAL_POSITION");
//            // ISO rules are used to determine the nullability for a column.
//            // YES --- if the parameter can include NULLs;
//            // NO --- if the parameter cannot include NULLs
//            // empty string --- if the nullability for the parameter is
//            // unknown
//            String isNullAble = rs.getString("IS_NULLABLE");
//            // Indicates whether this column is auto incremented
//            // YES --- if the column is auto incremented
//            // NO --- if the column is not auto incremented
//            // empty string --- if it cannot be determined whether the
//            // column is auto incremented parameter is unknown
//            String isAutoincrement = rs.getString("IS_AUTOINCREMENT");
//            System.out.println(tableCat + "-" + tableSchemaName + "-" + tableName_ + "-" + columnName + "-"
//                    + dataType + "-" + dataTypeName + "-" + columnSize + "-" + decimalDigits + "-" + numPrecRadix
//                    + "-" + nullAble + "-" + remarks + "-" + columnDef + "-" + sqlDataType + "-" + sqlDatetimeSub
//                    + charOctetLength + "-" + ordinalPosition + "-" + isNullAble + "-" + isAutoincrement + "-");
//        }
//        conn.close();
//
//    }
//*/
//    @Test
//    public void test() {
//        String sql = "CREATE\n" +
//                "OR REPLACE VIEW VIEW_JMETA_DDD AS\n" +
//                "SELECT\n" +
//                "  `T1`.`PROJECT_TYPE1` AS `PROJECT_TYPE1`,\n" +
//                "  `T2`.`PROJECT_TYPE2` AS `PROJECT_TYPE2`,\n" +
//                "  `T3`.`PROJECT_TYPE3` AS `PROJECT_TYPE3`,\n" +
//                "  `T4`.`PROJECT_TYPE4` AS `PROJECT_TYPE4`,\n" +
//                "  `T5`.`PROJECT_TYPE5` AS `PROJECT_TYPE5`,\n" +
//                "  `T6`.`ROSTER_TYPE1` AS `ROSTER_TYPE1`,\n" +
//                "  `T7`.`ROSTER_TYPE2` AS `ROSTER_TYPE2`,\n" +
//                "  `T8`.`ROSTER_TYPE3` AS `ROSTER_TYPE3`,\n" +
//                "  `T9`.`ROSTER_TYPE4` AS `ROSTER_TYPE4`,\n" +
//                "  `T10`.`DECLARE_11` AS `DECLARE_11`,\n" +
//                "  `T11`.`DECLARE_12` AS `DECLARE_12`\n" +
//                "FROM\n" +
//                "  (\n" +
//                "    (\n" +
//                "      (\n" +
//                "        (\n" +
//                "          (\n" +
//                "            (\n" +
//                "              (\n" +
//                "                (\n" +
//                "                  (\n" +
//                "                    (\n" +
//                "                      (\n" +
//                "                        (\n" +
//                "                          SELECT\n" +
//                "                            COUNT(0) AS `PROJECT_TYPE1`\n" +
//                "                          FROM\n" +
//                "                            `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                          WHERE\n" +
//                "                            (\n" +
//                "                              `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`PROJECT_TYPE` = '1169783921265491969'\n" +
//                "                            )\n" +
//                "                        )\n" +
//                "                      ) `T1`\n" +
//                "                      JOIN (\n" +
//                "                        SELECT\n" +
//                "                          COUNT(0) AS `PROJECT_TYPE2`\n" +
//                "                        FROM\n" +
//                "                          `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                        WHERE\n" +
//                "                          (\n" +
//                "                            `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`PROJECT_TYPE` = '1169785980232548354'\n" +
//                "                          )\n" +
//                "                      ) `T2`\n" +
//                "                    )\n" +
//                "                    JOIN (\n" +
//                "                      SELECT\n" +
//                "                        COUNT(0) AS `PROJECT_TYPE3`\n" +
//                "                      FROM\n" +
//                "                        `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                      WHERE\n" +
//                "                        (\n" +
//                "                          `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`PROJECT_TYPE` = '1169786287465316353'\n" +
//                "                        )\n" +
//                "                    ) `T3`\n" +
//                "                  )\n" +
//                "                  JOIN (\n" +
//                "                    SELECT\n" +
//                "                      COUNT(0) AS `PROJECT_TYPE4`\n" +
//                "                    FROM\n" +
//                "                      `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                    WHERE\n" +
//                "                      (\n" +
//                "                        `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`PROJECT_TYPE` = '1169787667814957057'\n" +
//                "                      )\n" +
//                "                  ) `T4`\n" +
//                "                )\n" +
//                "                JOIN (\n" +
//                "                  SELECT\n" +
//                "                    COUNT(0) AS `PROJECT_TYPE5`\n" +
//                "                  FROM\n" +
//                "                    `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                  WHERE\n" +
//                "                    (\n" +
//                "                      `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`PROJECT_TYPE` = '1172087809003638786'\n" +
//                "                    )\n" +
//                "                ) `T5`\n" +
//                "              )\n" +
//                "              JOIN (\n" +
//                "                SELECT\n" +
//                "                  COUNT(0) AS `ROSTER_TYPE1`\n" +
//                "                FROM\n" +
//                "                  `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "                WHERE\n" +
//                "                  (\n" +
//                "                    `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`ROSTER_TYPE` = '1169790331487404033'\n" +
//                "                  )\n" +
//                "              ) `T6`\n" +
//                "            )\n" +
//                "            JOIN (\n" +
//                "              SELECT\n" +
//                "                COUNT(0) AS `ROSTER_TYPE2`\n" +
//                "              FROM\n" +
//                "                `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "              WHERE\n" +
//                "                (\n" +
//                "                  `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`ROSTER_TYPE` = '1169790517295071234'\n" +
//                "                )\n" +
//                "            ) `T7`\n" +
//                "          )\n" +
//                "          JOIN (\n" +
//                "            SELECT\n" +
//                "              COUNT(0) AS `ROSTER_TYPE3`\n" +
//                "            FROM\n" +
//                "              `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "            WHERE\n" +
//                "              (\n" +
//                "                `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`ROSTER_TYPE` = '1169790582197731329'\n" +
//                "              )\n" +
//                "          ) `T8`\n" +
//                "        )\n" +
//                "        JOIN (\n" +
//                "          SELECT\n" +
//                "            COUNT(0) AS `ROSTER_TYPE4`\n" +
//                "          FROM\n" +
//                "            `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "          WHERE\n" +
//                "            (\n" +
//                "              `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`ROSTER_TYPE` = '1169790331487404033'\n" +
//                "            )\n" +
//                "        ) `T9`\n" +
//                "      )\n" +
//                "      JOIN (\n" +
//                "        SELECT\n" +
//                "          COUNT(0) AS `DECLARE_11`\n" +
//                "        FROM\n" +
//                "          `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "        WHERE\n" +
//                "          (\n" +
//                "            `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`DECLARE_1` = '1169790639928131586'\n" +
//                "          )\n" +
//                "      ) `T10`\n" +
//                "    )\n" +
//                "    JOIN (\n" +
//                "      SELECT\n" +
//                "        COUNT(0) AS `DECLARE_12`\n" +
//                "      FROM\n" +
//                "        `FASTDEV`.`JMETA_CHINAENTRYPROJECT`\n" +
//                "      WHERE\n" +
//                "        (\n" +
//                "          `FASTDEV`.`JMETA_CHINAENTRYPROJECT`.`DECLARE_1` = '1169790685025288194'\n" +
//                "        )\n" +
//                "    ) `T11`\n" +
//                "  )";
//        String sql2 = sql.trim();
//        sql2 = sql2.toUpperCase();
//        System.out.println(sql2.substring((sql2.indexOf("SELECT\n")+7) , sql2.indexOf("FROM\n")));
//        sql2 = sql2.substring((sql2.indexOf("SELECT\n")+7) , sql2.indexOf("FROM\n")).replaceAll("`","");
//        System.out.println(sql2);
//        String[] s =  sql2.split(",");
//        List<String> strings = new ArrayList<String>();
//        for(int j = 0 ; j < s.length ; j++){
//            System.out.println(s[j]);
//            s[j] = s[j].substring(s[j].lastIndexOf(" AS ")+4 , s[j].length());
//            System.out.println(s[j]);
//        }
//    }
//
//    @Test
//    public void test2(){
//        String sql = "select `t`.`project_type` AS `project_type`,`t2`.`Roster_type` AS `Roster_type` from (select count(0) AS `project_type` from `fastdev`.`jmeta_chinaentryproject` where (`fastdev`.`jmeta_chinaentryproject`.`PROJECT_TYPE` like '%表演艺术%')) `t` join (select count(0) AS `Roster_type` from `fastdev`.`jmeta_chinaentryproject` where (`fastdev`.`jmeta_chinaentryproject`.`ROSTER_TYPE` like '%人类非物质文化遗产代表作名录%')) `t2`";
//        String sql2 = sql.trim();
//        sql2 = sql2.toUpperCase().substring(sql2.indexOf(" FROM ") , sql2.length());
//        sql2 = sql2;
//        String sql1 = "SELECT ";
//        sql1 += "PROJECT_TYPE";
//        sql = sql1 + sql2;
//        System.out.println(sql);
//    }
//
//    public static String localRepoPath = "D:/repo";
//    public static String localRepoGitConfig = "D:/repo/.git";
//    public static String remoteRepoURI = "git@gitlab.com:wilson/test.git";
//    public static String localCodeDir = "D:/platplat";
//
//    /**
//     * 新建一个分支并同步到远程仓库
//     * @param branchName
//     * @throws IOException
//     * @throws GitAPIException
//     */
//    public static String newBranch(String branchName){
//        String newBranchIndex = "refs/heads/"+branchName;
//        String gitPathURI = "";
//        Git git = null;
//        try {
//            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
//            List<Ref> refs = git.branchList().call();
//            for (Ref ref : refs) {
//                if (ref.getName().equals(newBranchIndex)) {
//                    System.out.println("Removing branch before");
//                    git.branchDelete().setBranchNames(branchName).setForce(true)
//                            .call();
//                    break;
//                }
//            }
//            //新建分支
//            Ref ref = git.branchCreate().setName(branchName).call();
//            //推送到远程
//            git.push().add(ref).call();
//            gitPathURI = remoteRepoURI + " " + "feature/" + branchName;
//        } catch (GitAPIException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return gitPathURI;
//    }
//
//    public static void commitFiles() throws IOException, GitAPIException {
//        String filePath = "";
//        Git git = Git.open( new File(localRepoGitConfig) );
//        //创建用户文件的过程
//        File myfile = new File(filePath);
//        myfile.createNewFile();
//        git.add().addFilepattern("pets").call();
//        //提交
//        git.commit().setMessage("Added pets").call();
//        //推送到远程
//        git.push().call();
//    }
//
//    public static boolean pullBranchToLocal(String cloneURL){
//        boolean resultFlag = false;
//        String[] splitURL = cloneURL.split(" ");
//        String branchName = splitURL[1];
//        String fileDir = localCodeDir+"/"+branchName;
//        //检查目标文件夹是否存在
//        File file = new File(fileDir);
//        if(file.exists()){
//            deleteFolder(file);
//        }
//        Git git;
//        try {
//            git = Git.open( new File(localRepoGitConfig) );
//            git.cloneRepository().setURI(cloneURL).setDirectory(file).call();
//            resultFlag = true;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (GitAPIException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return resultFlag;
//    }
//
//    public static void deleteFolder(File file){
//        if(file.isFile() || file.list().length==0){
//            file.delete();
//        }else{
//            File[] files = file.listFiles();
//            for(int i=0;i<files.length;i++){
//                deleteFolder(files[i]);
//                files[i].delete();
//            }
//        }
//    }
//
//    public static void setupRepo() throws GitAPIException{
//        //建立与远程仓库的联系，仅需要执行一次
//        Git git = Git.cloneRepository().setURI(remoteRepoURI).setDirectory(new File(localRepoPath)).call();
//    }
//
//
//
//
//}
