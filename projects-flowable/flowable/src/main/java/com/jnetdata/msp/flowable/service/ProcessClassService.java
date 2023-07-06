package com.jnetdata.msp.flowable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.flowable.model.ProcessClass;
import org.springframework.web.bind.annotation.PathVariable;
import org.thenicesys.web.JsonResult;

import java.util.List;

public interface ProcessClassService extends BaseService<ProcessClass> {

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
     * 设置分类下的流程信息
     */
    void setProcessList(List<ProcessClass> classList);

}
