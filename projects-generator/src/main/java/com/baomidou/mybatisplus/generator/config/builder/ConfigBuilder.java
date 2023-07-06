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
package com.baomidou.mybatisplus.generator.config.builder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.jnetdata.msp.util.generator.ConstVal;
import com.jnetdata.msp.util.generator.converts.EsTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.jnetdata.msp.util.generator.rules.DbType;
import com.jnetdata.msp.util.generator.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.QuerySQL;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.service.EsCommonService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>
 * 配置汇总 传递给文件生成工具
 * </p>
 *
 * @author YangHu, tangguo, hubin
 * @since 2016-08-30
 */
@Data
@Accessors(chain = true)
public class ConfigBuilder {
    private EsCommonService esCommonService;

    /**
     * 模板路径配置信息
     */
    private final TemplateConfig template;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private QuerySQL querySQL;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;

    /**
     * <p>
     * 在构造器中处理配置
     * </p>
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public ConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                         TemplateConfig template, GlobalConfig globalConfig, EsCommonService esCommonService) {

        this.esCommonService = esCommonService;

        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }

        // 模板配置
        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }
        this.template.setTtype(globalConfig.getTtype());

        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), this.globalConfig.getOutputApiDir(), new PackageConfig(),this.globalConfig.getOutputPomDir(),this.getGlobalConfig().getOutputPPomDir());
        } else {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), this.globalConfig.getOutputApiDir(), packageConfig,this.globalConfig.getOutputPomDir(),this.getGlobalConfig().getOutputPPomDir());
        }

        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);

        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }
        handlerStrategy(this.strategyConfig);
    }

    // ************************ 曝露方法 BEGIN*****************************

    /**
     * <p>
     * 模板路径配置信息
     * </p>
     *
     * @return 所以模板路径配置信息
     */
    public TemplateConfig getTemplate() {
        return template == null ? new TemplateConfig() : template;
    }

    // ****************************** 曝露方法 END**********************************


    /**
     * <p>
     * 处理包配置
     * </p>
     *
     * @param template  TemplateConfig
     * @param outputDir
     * @param config    PackageConfig
     */
    private void handlerPackage(TemplateConfig template, String outputDir, String outputApiDir, PackageConfig config, String ctrlDir, String parentPomDir) {
        packageInfo = new HashMap<>();
        packageInfo.put(ConstVal.MODULENAME, config.getModuleName());
        packageInfo.put(ConstVal.PACKAGENAME, config.getParent().substring((config.getParent().lastIndexOf(".msp.")+5), (config.getParent().lastIndexOf("."))));
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        packageInfo.put(ConstVal.SERIVCE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICEIMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));
        packageInfo.put(ConstVal.BAT, joinPackage(config.getParent(), ""));
        packageInfo.put("parent", config.getOnlyParent());

        // 生成路径信息
        pathInfo = new HashMap<>();
        if (StringUtils.isNotEmpty(template.getEntity(getGlobalConfig().isKotlin()))) {
            pathInfo.put(ConstVal.ENTITY_PATH, joinPath(outputApiDir, packageInfo.get(ConstVal.ENTITY)));
        }
        if (StringUtils.isNotEmpty(template.getMapper())) {
            pathInfo.put(ConstVal.MAPPER_PATH, joinPath(outputDir, packageInfo.get(ConstVal.MAPPER)));
        }
        if (StringUtils.isNotEmpty(template.getXml())) {
            pathInfo.put(ConstVal.XML_PATH, joinPath(outputDir, packageInfo.get(ConstVal.XML)));
        }
        if (StringUtils.isNotEmpty(template.getService())) {
            pathInfo.put(ConstVal.SERIVCE_PATH, joinPath(outputDir, packageInfo.get(ConstVal.SERIVCE)));
        }
        if (StringUtils.isNotEmpty(template.getServiceImpl())) {
            pathInfo.put(ConstVal.SERVICEIMPL_PATH, joinPath(outputDir, packageInfo.get(ConstVal.SERVICEIMPL)));
        }
        if (StringUtils.isNotEmpty(template.getController())) {
            pathInfo.put(ConstVal.CONTROLLER_PATH, joinPath(outputDir, packageInfo.get(ConstVal.CONTROLLER)));
        }
        if(StringUtils.isNotEmpty(template.getBat())) {
            //"E:\\IdeaProjects\\web\\projects-generator\\src\\main\\resources"
            pathInfo.put(ConstVal.BAT_PATH, joinPath(outputDir, packageInfo.get(ConstVal.BAT)));
        }
        if(StringUtils.isNotEmpty(template.getParentpom())) {
            pathInfo.put(ConstVal.PARENT_POM_PATH, parentPomDir);
        }
        if(StringUtils.isNotEmpty(template.getPom())) {
            pathInfo.put(ConstVal.POM_PATH, ctrlDir);
        }
        if(StringUtils.isNotEmpty(template.getApipom())) {
            pathInfo.put(ConstVal.APIPOM_PATH, ctrlDir+"-api");
        }
    }

    /**
     * <p>
     * 处理数据源配置
     * </p>
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
        querySQL = getQuerySQL(config.getDbType());
    }

    /**
     * <p>
     * 处理数据库表 加载数据库表、列、注释相关数据集
     * </p>
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }

    /**
     * <p>
     * 处理superClassName,IdClassType,IdStrategy配置
     * </p>
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
            superServiceClass = ConstVal.SUPERD_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
            superServiceImplClass = ConstVal.SUPERD_SERVICEIMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPERD_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        superControllerClass = config.getSuperControllerClass();
    }

    /**
     * <p>
     * 处理表对应的类名称
     * </P>
     *
     * @param tableList 表名称
     * @param strategy  命名策略
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        String[] fieldPrefix = config.getFieldPrefix();
        for (TableInfo tableInfo : tableList) {
            tableInfo.setEntityName(strategyConfig, NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix)));
            tableInfo.setMapperName(StringUtils.isNotEmpty(globalConfig.getMapperName()) ?
                    String.format(globalConfig.getMapperName(), tableInfo.getEntityName()) : tableInfo.getEntityName() + ConstVal.MAPPER);

            tableInfo.setXmlName( StringUtils.isNotEmpty(globalConfig.getXmlName()) ?
                    String.format(globalConfig.getXmlName(), tableInfo.getEntityName()) : tableInfo.getEntityName() + ConstVal.MAPPER);

            tableInfo.setServiceName(StringUtils.isNotEmpty(globalConfig.getServiceName()) ?
                    String.format(globalConfig.getServiceName(), tableInfo.getEntityName()) : "I" + tableInfo.getEntityName() + ConstVal.SERIVCE);

            tableInfo.setServiceImplName(StringUtils.isNotEmpty(globalConfig.getServiceImplName()) ?
                    String.format(globalConfig.getServiceImplName(), tableInfo.getEntityName()) :tableInfo.getEntityName() + ConstVal.SERVICEIMPL);

            tableInfo.setControllerName(StringUtils.isNotEmpty(globalConfig.getControllerName()) ?
                    String.format(globalConfig.getControllerName(), tableInfo.getEntityName()) : tableInfo.getEntityName() + ConstVal.CONTROLLER);

            tableInfo.setPomName(StringUtils.isNotEmpty(globalConfig.getPomName()) ?
                    String.format(globalConfig.getPomName(), tableInfo.getEntityName()) : tableInfo.getEntityName() + ConstVal.CONTROLLER);

            //强制开启字段注解
            checkTableIdTableFieldAnnotation(config, tableInfo, fieldPrefix);
        }
        return tableList;
    }

    /**
     * <p>
     * 检查是否有
     * {@link com.baomidou.mybatisplus.annotation.TableId}
     * {@link com.baomidou.mybatisplus.annotation.TableField}
     * 注解
     * </p>
     *
     * @param config
     * @param tableInfo
     * @param fieldPrefix
     */
    private void checkTableIdTableFieldAnnotation(StrategyConfig config, TableInfo tableInfo, String[] fieldPrefix) {
        boolean importTableFieldAnnotaion = false;
        boolean importTableIdAnnotaion = false;
        if (config.isEntityTableFieldAnnotationEnable()) {
            for (TableField tf : tableInfo.getFields()) {
                tf.setConvert(true);
                importTableFieldAnnotaion = true;
                importTableIdAnnotaion = true;
            }
        } else if (fieldPrefix != null && fieldPrefix.length != 0) {
            for (TableField tf : tableInfo.getFields()) {
                if (NamingStrategy.isPrefixContained(tf.getName(), fieldPrefix)) {
                    if (tf.isKeyFlag()) {
                        importTableIdAnnotaion = true;
                    }
                    tf.setConvert(true);
                    importTableFieldAnnotaion = true;
                }
            }
        }
        if (importTableFieldAnnotaion) {
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
        }
        if (importTableIdAnnotaion) {
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
        }
        if (globalConfig.getIdType() != null) {
            if (!importTableIdAnnotaion) {
                tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
            }
            tableInfo.getImportPackages().add(IdType.class.getCanonicalName());
        }
    }

    /**
     * <p>
     * 获取所有的数据库表信息
     * </p>
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        //需要反向生成或排除的表信息
        List<TableInfo> tableList = new ArrayList<>();

        if(globalConfig.getTtype() == 2){
            Set<String> allIndices = esCommonService.getAllIndices(globalConfig.getDbSourceId());
            for (String index : allIndices) {
                if(Arrays.asList(config.getInclude()).contains(index.toLowerCase())){
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setName(index);
                    tableInfo.setTType(2);
                    tableInfo.setDbSourceId(globalConfig.getDbSourceId());
                    this.convertTableFields(tableInfo, config.getColumnNaming());
                    tableList.add(tableInfo);
                }
            }
        }else {
            //不存在的表名
            PreparedStatement preparedStatement = null;
            try {
                String tableCommentsSql = querySQL.getTableCommentsSql();
                if (QuerySQL.POSTGRE_SQL == querySQL) {
                    tableCommentsSql = String.format(tableCommentsSql, dataSourceConfig.getSchemaname());
                } else { //oracle数据库表太多，出现最大游标错误
                    String join = String.join("','", config.getInclude()).toUpperCase(Locale.ROOT);
                    tableCommentsSql = String.format(tableCommentsSql,join);
                }

                preparedStatement = connection.prepareStatement(tableCommentsSql);
                ResultSet results = preparedStatement.executeQuery();
                while (results.next()) {
                    String tableName = results.getString("NAME");
                    if (StringUtils.isNotEmpty(tableName)) {
                        if (Arrays.asList(config.getInclude()).contains(tableName.toLowerCase())) {
                            String tableComment = results.getString("COMMENT");
                            TableInfo tableInfo = new TableInfo();
                            tableInfo.setName(tableName);
                            tableInfo.setComment(tableComment);
                            tableInfo.setTType(globalConfig.getTtype());
                            tableList.add(tableInfo);
                        }
                    } else {
                        System.err.println("当前数据库为空！！！");
                    }
                }

                /**
                 * 性能优化，只处理需执行表字段 github issues/219
                 */
                for (TableInfo ti : tableList) {
                    this.convertTableFields(ti, config.getColumnNaming());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // 释放资源
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return processTable(tableList, config.getNaming(), config);
    }


    /**
     * <p>
     * 判断主键是否为identity，目前仅对mysql进行检查
     * </p>
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException
     */
    private boolean isKeyIdentity(ResultSet results) throws SQLException {
        if (QuerySQL.MYSQL == this.querySQL) {
            String extra = results.getString("Extra");
            if ("auto_increment".equals(extra)) {
                return true;
            }
        } else if (QuerySQL.SQL_SERVER == this.querySQL) {
            int isIdentity = results.getInt("isIdentity");
            return 1 == isIdentity;
        }
        return false;
    }

    /**
     * <p>
     * 将字段信息与表信息关联
     * </p>
     *
     * @param tableInfo 表信息
     * @param strategy  命名策略
     * @return
     */
    private TableInfo convertTableFields(TableInfo tableInfo, NamingStrategy strategy) {
        List<TableField> fieldList = new ArrayList<>();
        if(globalConfig.getTtype() == 2){
            List<Map<String, Object>> fields = esCommonService.getFields(tableInfo.getDbSourceId(),tableInfo.getName());
            for (Map<String, Object> map : fields) {
                TableField field = new TableField();
                field.setKeyFlag(false);
                // 处理其它信息
                field.setName(map.get("key") + "");
                field.setType(map.get("type") + "");
                field.setPropertyName(strategyConfig, processName(field.getName().toLowerCase(), strategy));
                field.setColumnType(new EsTypeConvert().processTypeConvert(field.getType()));
                fieldList.add(field);
            }
            tableInfo.setFields(fieldList);
            return tableInfo;
        }

        boolean haveId = false;
        List<TableField> commonFieldList = new ArrayList<>();
        try {
            String tableFieldsSql = querySQL.getTableFieldsSql();
            if (QuerySQL.POSTGRE_SQL == querySQL) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaname(), tableInfo.getName());
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableInfo.getName());
            }
            PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                TableField field = new TableField();
                String key = results.getString("KEY");
                // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                boolean isId = StringUtils.isNotEmpty(key) && key.toUpperCase().equals("PRI");
                // 处理ID
                if (isId && !haveId) {
                    field.setKeyFlag(true);
                    if (isKeyIdentity(results)) {
                        field.setKeyIdentityFlag(true);
                    }
                    haveId = true;
                } else {
                    field.setKeyFlag(false);
                }
                // 处理其它信息
                field.setName(results.getString("FIELD"));
                field.setType(results.getString("TYPE"));
                field.setPropertyName(strategyConfig, processName(field.getName(), strategy));
                field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(field.getType()));
                field.setComment(results.getString("COMMENT"));
                field.setMatchType(results.getInt("MATCH_TYPE"));
                field.setFieldType(results.getInt("FIELDTYPE"));
                if (tableInfo.getTType() == 0 && strategyConfig.includeSuperEntityColumns(field.getName())) {
                    // 跳过公共字段
                    commonFieldList.add(field);
                    continue;
                }
                // 填充逻辑判断
                List<TableFill> tableFillList = this.getStrategyConfig().getTableFillList();
                if (null != tableFillList) {
                    for (TableFill tableFill : tableFillList) {
                        if (tableFill.getFieldName().equals(field.getName())) {
                            field.setFill(tableFill.getFieldFill().name());
                            break;
                        }
                    }
                }
                fieldList.add(field);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }

    /**
     * <p>
     * 连接路径字符串
     * </p>
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * <p>
     * 连接父子包名
     * </p>
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + "." + subPackage;
    }

    /**
     * <p>
     * 处理字段名称
     * </p>
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, this.strategyConfig.getFieldPrefix());
    }

    /**
     * <p>
     * 处理表/字段名称
     * </p>
     *
     * @param name
     * @param strategy
     * @param prefix
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length >= 1) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        if(propertyName.endsWith(".keyword")) propertyName = propertyName.replace(".keyword","2keyword");
        return propertyName;
    }

    /**
     * <p>
     * 获取当前的SQL类型
     * </p>
     *
     * @return DB类型
     */
    private QuerySQL getQuerySQL(DbType dbType) {
        for (QuerySQL qs : QuerySQL.values()) {
            if (qs.getDbType().equals(dbType.getValue())) {
                return qs;
            }
        }
        return QuerySQL.MYSQL;
    }

}
