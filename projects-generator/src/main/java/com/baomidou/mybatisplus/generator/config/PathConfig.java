package com.baomidou.mybatisplus.generator.config;

import lombok.Data;

import java.io.File;

@Data
public class PathConfig {
    private String srcPath;
    private String libPath;
    private String moduleDir;
    private String project;
    private String modelName;
    private String outputPath;
    private String outputApiPath;
    private String ctrlPath;
    private String pPath;
    private String oldJarPath;
    private String newJarPath;
    private String oldApiJarPath;
    private String newApiJarPath;
    private String jarVersion;

    // 父类包名
    private String packageSuperClass = "com.jnetdata.msp.core";
    // 实际项目包名
    private String packageParent = "com.jnetdata.msp";

    public PathConfig(String srcPath,String libPath,String entityPackage, String project){
        this.srcPath = srcPath;
        this.libPath = libPath;
        this.modelName = entityPackage;
        this.moduleDir = "projects-"+project;
        this.project = project;

        String jarPath = "-"+jarVersion+".jar";

        this.outputPath = srcPath+ File.separator +moduleDir+ File.separator + project + File.separator + "src"+ File.separator + "main"+ File.separator + "java";
        this.outputApiPath = srcPath+File.separator+moduleDir+ File.separator + project + "-api" + File.separator + "src"+ File.separator + "main"+ File.separator + "java";
        this.ctrlPath = srcPath+File.separator+moduleDir+ File.separator + project;
        this.pPath = srcPath+File.separator+moduleDir;
        this.oldJarPath = srcPath + File.separator + moduleDir + File.separator + project + File.separator + "target"+ File.separator + "jnetdata-msp-" + project + jarPath;
        this.newJarPath = libPath+ File.separator +"jnetdata-msp-"+project+jarPath;
        this.oldApiJarPath = srcPath+File.separator+moduleDir + File.separator + project + "-api" + File.separator + "target"+ File.separator + "jnetdata-msp-"+project+"-api"+jarPath;
        this.newApiJarPath = libPath+File.separator + "jnetdata-msp-"+project+"-api"+jarPath;
        this.packageParent = "com.jnetdata.msp."+project;
    }

}
