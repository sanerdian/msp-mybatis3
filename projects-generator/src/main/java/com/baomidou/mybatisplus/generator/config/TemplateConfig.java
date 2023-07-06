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
package com.baomidou.mybatisplus.generator.config;

import com.jnetdata.msp.util.generator.ConstVal;
import lombok.Data;

/**
 * <p>
 * 模板路径配置项
 * </p>
 *
 * @author tzg hubin
 * @since 2017-06-17
 */
@Data
public class TemplateConfig {

    private int ttype;

    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;

    private String esEntity = ConstVal.TEMPLATE_ES_ENTITY_JAVA;

    private String service = ConstVal.TEMPLATE_SERVICE;

    private String serviceImpl = ConstVal.TEMPLATE_SERVICEIMPL;

    private String esservice = ConstVal.TEMPLATE_ES_SERVICE;

    private String esserviceImpl = ConstVal.TEMPLATE_ES_SERVICEIMPL;

    private String mapper = ConstVal.TEMPLATE_MAPPER;

    private String xml = ConstVal.TEMPLATE_XML;

    private String controller = ConstVal.TEMPLATE_CONTROLLER;

    private String escontroller = ConstVal.TEMPLATE_ES_CONTROLLER;

    private String bat = ConstVal.TEMPLATE_BAT;

    private String pom = ConstVal.TEMPLATE_POM;

    private String apipom = ConstVal.TEMPLATE_API_POM;

    private String parentpom = ConstVal.TEMPLATE_PARENT_POM;

    public String getEntity(boolean kotlin) {
        return ttype == 2 ? esEntity : kotlin ? ConstVal.TEMPLATE_ENTITY_KT : entity;
    }

    public TemplateConfig setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public String getService() {
        return ttype == 2 ? esservice : service;
    }

    public TemplateConfig setService(String service) {
        this.service = service;
        return this;
    }

    public String getServiceImpl() {
        return ttype == 2 ? esserviceImpl : serviceImpl;
    }

    public TemplateConfig setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public String getMapper() {
        return mapper;
    }

    public TemplateConfig setMapper(String mapper) {
        this.mapper = mapper;
        return this;
    }

    public String getXml() {
        return xml;
    }

    public TemplateConfig setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public String getController() {
        return  ttype == 2 ? escontroller : controller;
    }

    public TemplateConfig setController(String controller) {
        this.controller = controller;
        return this;
    }

    public String getBat() {
        return bat;
    }

    public TemplateConfig setBat(String bat) {
        this.bat = bat;
        return this;
    }

    public String getPom() {
        return pom;
    }

    public TemplateConfig setPom(String pom) {
        this.pom = pom;
        return this;
    }

    public String getApipom() {
        return apipom;
    }

    public TemplateConfig setApipom(String apipom) {
        this.apipom = pom;
        return this;
    }

    public String getParentpom() {
        return parentpom;
    }

    public TemplateConfig setParentpom(String parentpom) {
        this.parentpom = parentpom;
        return this;
    }
}
