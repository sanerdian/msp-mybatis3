package com.jnetdata.msp.flowable.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.ProcessDefiMapper;
import com.jnetdata.msp.flowable.model.ProcessDefi;
import com.jnetdata.msp.flowable.service.ProcessDefiService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessDefiServiceImpl extends BaseServiceImpl<ProcessDefiMapper, ProcessDefi> implements ProcessDefiService {


    @Override
    protected PropertyWrapper<ProcessDefi> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增流程定义
     */
    @Override
    public void insertEntity(ModelRepresentation modelRepresentation){
        try {
            ProcessDefi entity = new ProcessDefi();
            BeanUtils.copyProperties(modelRepresentation, entity);
            entity.setProcessName(modelRepresentation.getName());
            entity.setProcessKey(modelRepresentation.getKey());
            entity.setProcessDesc(modelRepresentation.getDescription());
            this.insert(entity);
        }catch (Exception e){
            log.error("新增流程定义异常：{}", e.getMessage());
        }
    }

    /**
     * 更新流程定义
     */
    @Override
    public void updateEntity(String modelId, MultiValueMap<String, String> values){
        try {
            String loginUserId = this.getLoginUserid();
            Date now = new Date();

            ProcessDefi entity = new ProcessDefi();
            entity.setId(modelId);
            entity.setProcessName(values.getFirst("name"));
            entity.setProcessDesc(values.getFirst("description"));
            entity.setProcessKey(values.getFirst("key"));
            entity.setOrderNumber(Integer.parseInt(values.getFirst("orderNumber")));
            entity.setIconPath(values.getFirst("iconPath"));
            entity.setIconColor(values.getFirst("iconColor"));
            entity.setFormClassId(values.getFirst("formClassId"));
            entity.setFormId(values.getFirst("formId"));
            entity.setProcessClassId(values.getFirst("processClassId"));
            entity.setDelegateFlag(values.getFirst("delegateFlag"));
            entity.setAttachmentFlag(values.getFirst("attachmentFlag"));
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setCreateUserId(loginUserId);
            entity.setUpdateUserId(loginUserId);
            this.updateById(entity);
        }catch (Exception e){
            log.error("更新流程定义异常：{}", e.getMessage());
        }
    }

    /**
     * 获取当前登录用户id
     */
    private String getLoginUserid(){
        try {
            return SessionUser.getCurrentUser().getId().toString();
        }catch (Exception e){
            log.error("获取当前登录用户id异常：{}", e.getMessage());
        }
        return null;
    }

}
