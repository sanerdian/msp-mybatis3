package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.FormClass;
import com.jnetdata.msp.flowable.model.ProcessClass;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import org.thenicesys.web.JsonResult;

import java.util.List;

public interface FormClassService extends BaseService<FormClass> {

    /**
     * 获取新对象序号
     */
    int getOrderNumber();

    /**
     * 上移
     */
    JsonResult<Void> moveUp(Integer id);

    /**
     * 下移
     */
    JsonResult<Void> moveDown(Integer id);

    /**
     * 设置分类下的表单信息
     */
    void setProcessList(List<FormClass> classList);
}
