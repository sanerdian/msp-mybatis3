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
package com.baomidou.mybatisplus.generator.engine;

import com.jnetdata.msp.util.generator.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * Velocity 模板引擎实现文件输出
 * </p>
 *
 * @author hubin
 * @since 2018-01-10
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {

    private static final String DOT_VM = ".vm";
    private VelocityEngine velocityEngine;

    @Override
    public VelocityTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOADPATH_KEY, ConstVal.VM_LOADPATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", "true");
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isEmpty(templatePath)) {
            return;
        }
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
        template.merge(new VelocityContext(objectMap), writer);
        writer.close();
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        StringBuilder fp = new StringBuilder();
        fp.append(filePath).append(DOT_VM);
        return fp.toString();
    }

    protected static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    protected static String toLowerCase(String s){
        return s.toLowerCase();
    }

    /**
     * 可以重写加入自定义的变量
     * @param tableInfo
     * @return
     */
    @Override
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String,Object> objectMap = super.getObjectMap(tableInfo);
        /// 第一个字母小写 的 实体类简名(比如：saleCustomer)
        objectMap.put("lowerCaseFirstOneEntityName", toLowerCaseFirstOne(tableInfo.getEntityName())  );
        // 全小写 实体类简名 (比如：salecustomer)
        objectMap.put("lowerCaseEntityName", toLowerCase(tableInfo.getEntityName())  );
        // 第一个字母小写 的 服务接口简名(比如：saleCustomerService)
        objectMap.put("lowerCaseFirstOneServiceName", toLowerCaseFirstOne(tableInfo.getServiceName())  );
        // 第一个字母小写的 控制类简名（比如：saleCustomerController)
        objectMap.put("lowerCaseFirstOneControllerName", toLowerCaseFirstOne(tableInfo.getControllerName())  );
        // 启用 swagger2
        objectMap.put("swagger2", true);
        // id 字段标注idType的值
        objectMap.put("idType", "ID_WORKER");

        if(tableInfo.getTType() == 0 ){
            // 主键field

            objectMap.put("keyField", findKeyField(tableInfo));
        }

        return objectMap;
    }

    protected TableField findKeyField(TableInfo tableInfo) {
        for(TableField fld : tableInfo.getFields()) {
            if (fld.isKeyFlag()) {
                return fld;
            }
        }
        throw new RuntimeException("没有找到主键字段");
    }

    @Override
    public ConfigBuilder getConfigBuilder() {
        ConfigBuilder configBuilder = super.getConfigBuilder();
        Map<String, String> pathInfo = configBuilder.getPathInfo();
        pathInfo.put(ConstVal.XML_PATH, pathInfo.get(ConstVal.MAPPER_PATH));
        return configBuilder;
    }

}
