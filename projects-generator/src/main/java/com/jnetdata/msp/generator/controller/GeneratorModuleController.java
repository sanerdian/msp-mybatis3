package com.jnetdata.msp.generator.controller;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.jnetdata.msp.core.dbsource.model.DbSource;
import com.jnetdata.msp.core.dbsource.service.DbSourceService;
import com.jnetdata.msp.metadata.es.controller.EsTableinfo;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.util.PathUtil;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;

import com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService;
import com.jnetdata.msp.util.generator.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.service.EsCommonService;
import com.jnetdata.msp.generator.controller.component.model.Component;
import com.jnetdata.msp.generator.controller.component.service.ComponentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.restfulweb.controller.BaseRestfulController;
import org.thenicesys.web.JsonResult;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/generator/module")
@ApiModel(value = "GeneratorModuleController", description = "")
@Api(tags = "生成模块(GeneratorModuleController)")
public class GeneratorModuleController extends BaseRestfulController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.jdbcDialect}")
    private String dbtype;

    @Value("${spring.datasource.driverClassName}")
    private String driverName;

    // 需要产生代码的数据库表
    private String[] includeTables;

    // 输出目录

    private PathConfig pathConfig;

    private static String LOCAL_REPO_PATH;
    private static String LOCAL_REPOGIT_CONFIG;
    private static String REMOTE_REPO_URI;
    private static String INIT_LOCAL_CODE_DIR;
    private static String LOCAL_CODE_CT_SQL_DIR;
    private static String BRANCH_NAME;
    private static String GIT_USERNAME;
    private static String GIT_PASSWORD;

    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);



    @Autowired
    private MetaDataOperatorService metaDataOperatorService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private EsCommonService esCommonService;

    @Autowired
    private DbSourceService dbSourceService;

    @Autowired
    private TableinfoService tableinfoService;

    @Autowired
    private ModuleinfoService moduleinfoService;

    @Autowired
    private MetaDataOperatorService dataOperatorService;

    // 此处可以修改为您的表前缀
    private String[] tablePrefixes;
    // 实体类公共字段
    private String[] superEntityColumns = new String[] {"CRUSER", "CRNUMBER","CRTIME", "MODIFY_BY", "MODIFY_TIME","MODIFY_USER"};

    @PostMapping(value = "/createCode")
    @ResponseBody
    @ApiOperation(value = "生成模块应用")
    public JsonResult createCode(String tableNames, String prefixes, String moduleNames, Long id, int ttype) {
        if(StringUtils.isEmpty(prefixes)){
            prefixes = "";
        }
        getAllPath(tableNames, prefixes, moduleNames, id);
        AutoGenerator generator = createAutoGenerator(ttype);
        generator.execute();
        return renderSuccess();
    }

    @PostMapping(value = "/createEsCode")
    @ResponseBody
    @ApiOperation(value = "生成模块应用")
    public JsonResult createEsCode(@RequestBody EsTableinfo esTableinfo) {
        getEsAllPath(esTableinfo);
        AutoGenerator generator = createAutoGenerator(2,esTableinfo.getDbSourceId());
        generator.execute();

        return renderSuccess();
    }

    private void createJavaCode(String tableNames, String prefixes, String moduleNames, Long id, int ttype){
        if(StringUtils.isEmpty(prefixes)){
            prefixes = "";
        }
        getAllPath(tableNames, prefixes, moduleNames, id);
        AutoGenerator generator = createAutoGenerator(ttype);
        generator.execute();
    }

    private void createJavaCodes(String[] tableNames, String moduleNames, Long id, int ttype){
        for (String tableName : tableNames) {
            String prefixes = "";
            if(tableName.indexOf("_")>0){
                prefixes = tableName.substring(0,tableName.indexOf("_")-1);
            }
            getAllPath(tableName, prefixes, moduleNames, id);
            AutoGenerator generator = createAutoGenerator(ttype);
            generator.execute();
        }
    }


    @PostMapping(value = "/createCodes")
    @ResponseBody
    public JsonResult createCodes(String[] tableNames, String moduleNames, Long id, int ttype) {
        createJavaCodes(tableNames,moduleNames, id, ttype);
        return renderSuccess();
    }

    @PostMapping(value = "/delCodes")
    @ResponseBody
    @ApiOperation(value = "删除应用")
    public JsonResult delCodes(String tableNames, String prefixes, String moduleNames, Long id, int ttype) {
        if(StringUtils.isEmpty(tableNames)||StringUtils.isEmpty(prefixes)){
            return renderError("参数错误!");
        }
        getAllPath(tableNames, prefixes, moduleNames, id);
        ConfigBuilder config = createAutoGenerator(ttype).getConfig();
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
    public JsonResult mvnPackage() {
        dataOperatorService.addLog("部署应用","部署应用");
        String os = System.getProperty("os.name");
        String result = "";
        metaDataOperatorService.AddLog("部署应用","元数据管理部署应用");
        if(os.toLowerCase().startsWith("win")){ //windows
            result = mvnWin();
        }else{
            result = mvnLinux();
        }
        if(result == null){
            return renderSuccess();
        }else{
            return renderError(result);
        }
    }

    public String mvnWin(){
        String path = pathConfig.getSrcPath()+"\\mpackage.bat";
        logger.info("==========================生成打包程序: "+path+" ...==========================");
        writeFile(path);
        logger.info("==========================生成打包程序完成...==========================");
        Runtime run = Runtime.getRuntime();
        try {
            logger.info("==========================运行打包程序: cmd.exe /Q /C "+path+" ...==========================");
            Process process = run.exec("cmd.exe /Q /C " + path);
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            process.waitFor();
            int i = process.exitValue();
            in.close();
            reader.close();
            process.destroy();
            logger.info("==========================打包完成 ...==========================");
            logger.info("==========================打包返回值:"+i+" :"+ (i==0?"成功":"失败") +" ...==========================");
            if(i!=0){
                return "打包失败!请检查字段是否使用了关键词!";
            }
            Path old = Paths.get(pathConfig.getOldJarPath());
            Path newf = Paths.get(pathConfig.getNewJarPath());
            Path oldApi = Paths.get(pathConfig.getOldApiJarPath());
            Path newApi = Paths.get(pathConfig.getNewApiJarPath());
            if(Files.exists(old) && Files.exists(oldApi)){
                logger.info("==========================移动jar包 " +old.toFile().getAbsolutePath() + "to:" + newf.toFile().getAbsolutePath() + "==========================");
                if(moveJarFile(old,newf)){
                    logger.info("==========================移动jar包 " +oldApi.toFile().getAbsolutePath() + "to:" + newApi.toFile().getAbsolutePath() + "==========================");
                    if(moveJarFile(oldApi,newApi)){
                        logger.info("==========================移动jar包完成,重启程序 ...==========================");
                        return null;
                    }else{
                        return "打包失败!无法移动jar包!";
                    }
                }else{
                    return "打包失败!无法移动jar包!";
                }
            }else{
                return "打包失败!找不jar包!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public boolean moveJarFile(Path old,Path newName) throws InterruptedException {
        return repeatMoveJarFile(old,newName,1);
    }

    public boolean repeatMoveJarFile(Path old,Path newName,int i) throws InterruptedException {
        logger.info("移动文件第"+(i)+"次");
        try {
            Files.move(old, newName, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            i++;
            if(i>100) return false;
            Thread.sleep(1000);
            return repeatMoveJarFile(old,newName,i);
        }
    }

    public String mvnLinux(){
        String result = null;
        String path = pathConfig.getLibPath()+"/package.sh";
        logger.info("==========================生成打包程序: "+path+" ...==========================");
        writeFilelinux(path);
        logger.info("==========================生成打包程序完成...==========================");
        Runtime run = Runtime.getRuntime();
        try {
            logger.info("==========================运行打包程序: "+path+" ...==========================");
            Process process = run.exec(path);
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                logger.info(line);
                result += line;
            }
            process.waitFor();
            int i = process.exitValue();
            in.close();
            reader.close();
            process.destroy();
            logger.info("==========================打包完成 ...:"+i+"==========================");
            //gitPush();
            if(i != 0){
                return "发布失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }



    public AutoGenerator createAutoGenerator(int ttype) {
        return createAutoGenerator(ttype,null);
    }

    public AutoGenerator createAutoGenerator(int ttype,Long dbSourceId) {

        final String author = "zyj";
        AutoGenerator mpg = new AutoGenerator();

        mpg.setEsCommonService(esCommonService);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(pathConfig.getOutputPath());
        gc.setOutputPomDir(pathConfig.getCtrlPath());
        gc.setOutputPPomDir(pathConfig.getPPath());
        gc.setOutputApiDir(pathConfig.getOutputApiPath());
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
        gc.setBatName("mpackage");
        gc.setPomName("pom");

        gc.setTtype(ttype);
        gc.setDbSourceId(dbSourceId);

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
        return injectionConfig;
    }

    protected DataSourceConfig createDataSourceConfig() {

        return createOracleDataSourceConfig();
    }

    private DataSourceConfig createOracleDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dataSource = new DataSourceConfig();

        dataSource.setDriverName(driverName);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    protected PackageConfig createPackageConfig() {

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(pathConfig.getPackageParent());
        packageConfig.setModuleName(pathConfig.getModelName());
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
        strategy.setSuperEntityClass(pathConfig.getPackageSuperClass() + ".model.BaseEntity");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(superEntityColumns);
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(pathConfig.getPackageSuperClass() + ".mapper.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass(pathConfig.getPackageSuperClass() + ".service.BaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(pathConfig.getPackageSuperClass()  + ".service.impl.BaseServiceImpl");
        // 自定义 controller 父类
        strategy.setSuperControllerClass(pathConfig.getPackageSuperClass() + ".controller.BaseController");
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

    private static void insertNewWord(long skip, String str, String fileName) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
            if (skip < 0) {
                return;
            }
            byte[] b = str.getBytes();
            randomAccessFile.setLength(randomAccessFile.length() + b.length);
            //把后面的内容往后面挪
            for (long i = randomAccessFile.length() - 1; i > b.length + skip - 1; i--) {
                randomAccessFile.seek(i - b.length);
                byte temp = randomAccessFile.readByte();
                randomAccessFile.seek(i);
                randomAccessFile.writeByte(temp);
            }
            randomAccessFile.seek(skip);
            randomAccessFile.write(b);
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void getAllPath(String tableNames, String prefixes, String project, Long id){
        String entityPackage = PathUtil.getEntityPackage(tableNames);
        String os = System.getProperty("os.name");
        String osname = os.toLowerCase().startsWith("win")?"win":"linux";
        List<Component> list = componentService.list(new PropertyWrapper<>(Component.class)
                .eq("moduleinfoid",id).eq("osname", osname));
        pathConfig = new PathConfig(list.get(0).getComponentcpath(),list.get(0).getComponentdpath(),entityPackage,project);

        this.includeTables = tableNames.toLowerCase().split(",");
        this.tablePrefixes = prefixes.split(",");
    }

    protected void getEsAllPath(EsTableinfo esTableinfo){
        Long dbSourceId = esTableinfo.getDbSourceId();
        String tableName = esTableinfo.getIndex();
        String entityPackage = PathUtil.getEntityPackage(tableName);
        Tableinfo tableinfo = tableinfoService.getTableinfo(dbSourceId, tableName);
        if(tableinfo == null) new RuntimeException("未选择模块");
        Moduleinfo moduleinfo = moduleinfoService.getById(tableinfo.getOwnerid());
        if(moduleinfo == null) new RuntimeException("未选择模块");

        String modelName = moduleinfo.getEnglishname();
        String os = System.getProperty("os.name");
        String osname = os.toLowerCase().startsWith("win")?"win":"linux";
        List<Component> list = componentService.listByModuleId(moduleinfo.getId(),osname);
        pathConfig = new PathConfig(list.get(0).getComponentcpath(),list.get(0).getComponentdpath(),entityPackage,modelName);
        this.includeTables = tableName.toLowerCase().split(",");
        this.tablePrefixes = "".split(",");
    }


    protected void writeFile(String filepath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filepath));
            String text = pathConfig.getSrcPath().split(":")[0]+":\n"+"cd "+pathConfig.getSrcPath()+File.separator+pathConfig.getModuleDir()+"\n"+"mvn package -Dmaven.test.skip=true";
            bw.write(text);
            bw.flush();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void writeFilelinux(String filepath) {
        String jarPath = "-1.0.0.jar";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filepath));
            String text = "#!/bin/bash\n"+"cd "+pathConfig.getSrcPath()+"/"+pathConfig.getModuleDir()+"\n"+"mvn package -Dmaven.test.skip=true\n"+
                    "mv "+pathConfig.getSrcPath()+"/"+pathConfig.getModuleDir()+"/"+pathConfig.getProject()+"/target/jnetdata-msp-"+pathConfig.getProject()+jarPath+" "+
                    pathConfig.getLibPath()+"/jnetdata-msp-"+pathConfig.getProject()+jarPath+"\n"+
                    "mv "+pathConfig.getSrcPath()+"/"+pathConfig.getModuleDir()+"/"+pathConfig.getProject()+"-api"+"/target/jnetdata-msp-"+pathConfig.getProject()+"-api"+jarPath+" "+
                    pathConfig.getLibPath()+"/jnetdata-msp-"+pathConfig.getProject()+"-api"+jarPath+"\n";
            bw.write(text);
            bw.flush();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void gitPush() {
        String os = System.getProperty("os.name");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        if(os.toLowerCase().startsWith("win")) { //windows
            String gitshpath = pathConfig.getSrcPath()+"\\projects-generator\\src\\main\\resources\\gitpush.sh";
            String result = "";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec("sh " + gitshpath + " " + pathConfig.getSrcPath() + " " + time);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            String gitshpath = pathConfig.getLibPath()+"/gitpushlinux.sh";
            String result = "";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                //Process process = run.exec("sh " + gitshpath + " " + linuxpath + " " + time);
                Process process = run.exec(gitshpath + " " + pathConfig.getSrcPath() + " " + time);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void gitConfiguration(String name, String email, String gitpath) {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) { //windows
            String gitshpath = gitpath+"\\projects-generator\\src\\main\\resources\\gitconfig.sh";
            String result = "";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec("sh " + gitshpath + " " + pathConfig.getSrcPath() + " " + name + " " + email);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            String gitshpath = gitpath+"/gitconfig.sh";
            String result = "";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec(gitshpath + " " + pathConfig.getSrcPath() +" " + name + " " + email);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
