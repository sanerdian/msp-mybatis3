package com.jnetdata.msp.generator.controller;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Map;

public class MyVelocityTemplateEngine extends VelocityTemplateEngine {

    /**
     * 可以重写加入自定义的变量
     * @param tableInfo
     * @return
     */
    @Override
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        return super.getObjectMap(tableInfo);
    }

}
