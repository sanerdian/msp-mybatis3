package com.jnetdata.msp.generator.controller;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.jnetdata.msp.util.generator.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.jnetdata.msp.util.generator.rules.DbColumnType;
import com.jnetdata.msp.util.generator.rules.DbType;
import com.jnetdata.msp.util.generator.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
//import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.restfulweb.controller.BaseRestfulController;
import org.thenicesys.web.JsonResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/generator")
@ApiModel(value = "GeneratorController", description = "generator")
@Api(tags = "生成(GeneratorController)")
public class GeneratorController extends BaseRestfulController {

    // 父类包名
    private String packageSuperClass = "com.jnetdata.msp.core";
    // 实际项目包名
    private String packageParent = "com.jnetdata.msp";
    // 项目模块名
    private String moduleName = "generated";
    // 需要产生代码的数据库表
    private String[] includeTables;
    // 输出目录
    private String outputDir;

    // 此处可以修改为您的表前缀
    private String[] tablePrefixes;
    // 实体类公共字段
    private String[] superEntityColumns = new String[] {"CRUSER", "CRNUMBER","CRTIME", "modify_by", "modify_time"};

    @PostMapping(value = "/createCode")
    @ResponseBody
    @ApiOperation(value = "生成应用")
    public JsonResult createCode(String tableNames, String prefixes) {
//        if(StringUtils.isEmpty(tableNames)||StringUtils.isEmpty(prefixes)){
//            return renderError("参数错误!");
//        }
        if(StringUtils.isEmpty(prefixes)){
            prefixes = "";
        }
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) { //windows
            this.outputDir = "E:\\kuaisukaifapingtai\\web\\projects-generated\\src\\main\\java";
        }else{
            this.outputDir = "/data/proj1/src/projects-generated/src/main/java";
        }

        this.includeTables = tableNames.split(",");
        this.tablePrefixes = prefixes.split(",");
        AutoGenerator generator = createAutoGenerator();
        List<TableInfo> tableInfos = generator.getConfig().getTableInfoList();
        tableInfos.forEach(res -> {
            res.setTType(0);
        });
        generator.execute();

        return renderSuccess();
    }

    @PostMapping(value = "/delCodes")
    @ResponseBody
    @ApiOperation(value = "删除应用")
    public JsonResult delCodes(String tableNames, String prefixes) {
        if(StringUtils.isEmpty(tableNames)||StringUtils.isEmpty(prefixes)){
            return renderError("参数错误!");
        }
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) { //windows
            this.outputDir = "E:\\IdeaProjects\\web\\projects-generated\\src\\main\\java";
        }else{
            this.outputDir = "/data/proj1/src/projects-generated/src/main/java";
        }

        this.includeTables = tableNames.split(",");
        this.tablePrefixes = prefixes.split(",");
        ConfigBuilder config = createAutoGenerator().getConfig();
        List<TableInfo> tableInfoList = config.getTableInfoList();
        Map<String, String> pathInfo = config.getPathInfo();
        tableInfoList.forEach(res -> {
            File entityFile = new File(pathInfo.get("entity_path")+File.separator+res.getEntityName()+".java");
            File serviceFile = new File(pathInfo.get("serivce_path")+File.separator+res.getServiceName()+".java");
            File serviceImplFile = new File(pathInfo.get("serviceimpl_path")+File.separator+res.getServiceImplName()+".java");
            File controllerFile = new File(pathInfo.get("controller_path")+File.separator+res.getControllerName()+".java");
            File mapperFile = new File(pathInfo.get("mapper_path")+File.separator+res.getMapperName()+".java");
            File xmlFile = new File(pathInfo.get("mapper_path")+File.separator+res.getXmlName()+".xml");
            if(entityFile.exists()){
                entityFile.delete();
            }
            if(serviceFile.exists()){
                serviceFile.delete();
            }
            if(serviceImplFile.exists()){
                serviceImplFile.delete();
            }
            if(controllerFile.exists()){
                controllerFile.delete();
            }
            if(mapperFile.exists()){
                mapperFile.delete();
            }
            if(xmlFile.exists()){
                xmlFile.delete();
            }
        });

        mvnPackage();

        return renderSuccess();
    }

    @PostMapping(value = "/mvnPackage")
    @ResponseBody
    @ApiOperation(value = "mvnPackage")
    public JsonResult mvnPackage(){
//        Process p = Runtime.getRuntime().exec("ping 127.0.0.1");
        String os = System.getProperty("os.name");
        String result = "";
        boolean success = true;
        if(os.toLowerCase().startsWith("win")){ //windows
            String path = "E:\\IdeaProjects\\web\\projects-generator\\src\\main\\resources\\package.bat";
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec("cmd.exe /Q /C " + path);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();

                File oldName = new File("E:\\IdeaProjects\\web\\projects-generated\\target\\jnetdata-msp-generated-1.0-SNAPSHOT.jar");
                File newName = new File("E:\\IdeaProjects\\web\\projects-web\\target\\jnetdata-msp-web-1.0-SNAPSHOT\\WEB-INF\\lib\\jnetdata-msp-generated-1.0-SNAPSHOT.jar");
                if(oldName.exists()){
                    if(newName.exists()){
                        newName.delete();
                    }
                    success = oldName.renameTo(newName);
                    System.out.println(success);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return renderError(e.getMessage());
            }
        }else{
            String path = "/data/package.sh";
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec(path);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                return renderError(e.getMessage());
            }
        }
        if(success){
            return renderSuccess(result);
        }else{
            return renderError("发布失败");
        }
    }



    public AutoGenerator createAutoGenerator() {

        final String author = "zyj";

        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor(author);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        gc.setVoName("%sVo");


        mpg.setGlobalConfig(gc);

        mpg.setDataSource(createDataSourceConfig() );
        mpg.setStrategy( createStrategyConfig() );
        mpg.setPackageInfo( createPackageConfig() );

        // 执行生成
        mpg.setTemplateEngine( createTemplateEngine() );

        mpg.setInjectionConfig(createInjectionConfig());

        return mpg;

    }

    protected InjectionConfig createInjectionConfig() {
        InjectionConfig injectionConfig = new InjectionConfig();
        List<FileOutConfig> list = new ArrayList<>();
        list.add(FileOutConfig.create("/templates/test.java.vm", tableInfo -> {
            String entityName = tableInfo.getEntityName();
            String html = "/simple/"+moduleName+"/"+entityName.toLowerCase()+"/test.html";
            return html;
        }));
        injectionConfig.setFileOutConfigList(list);
        return injectionConfig;
    }

    protected DataSourceConfig createDataSourceConfig() {

        return createOracleDataSourceConfig();
    }

    private DataSourceConfig createOracleDataSourceConfig() {

        // 数据源配置
        DataSourceConfig dataSource = new DataSourceConfig();
        ResourceBundle resource = ResourceBundle.getBundle("config");
        String dbtype = resource.getString("jdbc_dialect");
//        if(dbtype.equals("mysql")) {
//            dataSource.setDbType(DbType.MYSQL);
//            dataSource.setTypeConvert(new MySqlTypeConvert(){
//                // 自定义数据库表字段类型转换【可选】
//                @Override
//                public DbColumnType processTypeConvert(String fieldType) {
//                    System.out.println("转换类型：" + fieldType);
//                    // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                    return super.processTypeConvert(fieldType);
//                }
//            });
//        }else{
//            dataSource.setDbType(DbType.ORACLE);
//            dataSource.setTypeConvert(new OracleTypeConvert() {
//                // 自定义数据库表字段类型转换【可选】
//                @Override
//                public DbColumnType processTypeConvert(String fieldType) {
//                    System.out.println("转换类型：" + fieldType);
//                    // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                    return super.processTypeConvert(fieldType);
//                }
//            });
//        }

        String driverName = resource.getString("jdbc_driver");
        String userName = resource.getString("jdbc_username");
        String password = resource.getString("jdbc_password");
        String url = resource.getString("jdbc_url");

        dataSource.setDriverName(driverName);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }
    private DataSourceConfig createMySqlDataSourceConfig() {

        // 数据源配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDbType(DbType.MYSQL);
        dataSource.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });

        dataSource.setDriverName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("11111111");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/db1?characterEncoding=utf8");

        return dataSource;
    }


    protected PackageConfig createPackageConfig() {

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(packageParent);
        packageConfig.setModuleName(moduleName);
        packageConfig.setEntity("model"); // 实体类包名
        packageConfig.setController("controller"); // 这里是控制器包名，默认 web

        return packageConfig;
    }

    protected StrategyConfig createStrategyConfig() {

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(tablePrefixes);
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(includeTables); // 需要生成的表
        //strategy.setExclude(new String[]{}); // 排除生成的表
        // 自定义实体父类
        strategy.setSuperEntityClass(packageSuperClass + ".model.BaseEntity");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(superEntityColumns);
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(packageSuperClass + ".mapper.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass(packageSuperClass + ".service.BaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(packageSuperClass  + ".service.impl.BaseServiceImpl");
        // 自定义 controller 父类
        strategy.setSuperControllerClass(packageSuperClass + ".controller.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        strategy.setEntityColumnConstant(false);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(false);
        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
        strategy.setEntityLombokModel(true);
        // Boolean类型字段是否移除is前缀处理
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.setRestControllerStyle(false);
        // strategy.setControllerMappingHyphenStyle(true)
        return strategy;
    }

    protected AbstractTemplateEngine createTemplateEngine() {
        return new MyVelocityTemplateEngine();
    }

}
