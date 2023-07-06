package com.jnetdata.msp.visual.template_type.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.template_type.model.VisualTemplateType;
import com.jnetdata.msp.visual.template_type.mapper.VisualTemplateTypeMapper;
import com.jnetdata.msp.visual.template_type.service.VisualTemplateTypeService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 组件类型 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
@Service
public class VisualTemplateTypeServiceImpl extends BaseServiceImpl<VisualTemplateTypeMapper, VisualTemplateType> implements VisualTemplateTypeService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<VisualTemplateType> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<VisualTemplateType> pw = createWrapperUtil(condition)
                .eq("title")
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
}
