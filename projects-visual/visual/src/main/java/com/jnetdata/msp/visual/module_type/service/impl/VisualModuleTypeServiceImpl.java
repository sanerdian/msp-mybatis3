package com.jnetdata.msp.visual.module_type.service.impl;

import com.jnetdata.msp.visual.module_type.model.VisualModuleType;
import com.jnetdata.msp.visual.module_type.mapper.VisualModuleTypeMapper;
import com.jnetdata.msp.visual.module_type.service.VisualModuleTypeService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * <p>
 * 组件类型 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
@Slf4j
@Service
public class VisualModuleTypeServiceImpl extends BaseServiceImpl<VisualModuleTypeMapper, VisualModuleType> implements VisualModuleTypeService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<VisualModuleType> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<VisualModuleType> pw = createWrapperUtil(condition)
                .eq("title")
                .eq("parentId")
                .eq("parentTitle")
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public void add(VisualModuleType entity) {
        if(entity.getParentId() == null){
            entity.setParentId(0L);
        }else if(entity.getParentId() != 0L){
            VisualModuleType byId = super.getById(entity.getParentId());
            entity.setParentTitle(byId.getTitle());
        }
        super.insert(entity);
    }

    @Override
    public void addBatch(List<VisualModuleType> list) {
        for (VisualModuleType visualModuleType : list) {
            add(visualModuleType);
        }
    }

    /**
     * 获取所有组件类型id与code的映射
     */
    @Override
    public Map<Long, String> getTypeMap(){
        Map<Long, String> map = new HashMap<>();
        try {
            List<VisualModuleType> typeList = this.list();
            for(VisualModuleType type: typeList){
                if(!StringUtils.isEmpty(type.getTypeCode())){
                    map.put(type.getId(), type.getTypeCode());
                }
            }
        }catch (Exception e){
            log.error("获取所有组件类型id与code的映射异常：{}", e.getMessage());
        }
        return map;
    }
}
