package com.jnetdata.msp.visual.module.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.module.mapper.VisualModuleMapper;
import com.jnetdata.msp.visual.module.model.VisualModule;
import com.jnetdata.msp.visual.module.service.VisualModuleService;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
@Service
public class VisualModuleServiceImpl extends BaseServiceImpl<VisualModuleMapper, VisualModule> implements VisualModuleService {
    @Override
    public PropertyWrapper<VisualModule> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                        .eq("id")
                        .eq("status")
                        .like("cssCode")
                        .like("exteName")
                        .like("htmlCode")
                        .like("img")
                        .eq("indClass")
                        .like("jsCode")
                        .eq("netType")
                        .eq("outFileName")
                        .like("proDes")
                        .eq("screenType")
                        .eq("status")
                        .like("title")
                        .eq("type")
                        .eq("moduleType")
                        .eq("vmType")
                        .eq("vcType")
                        .eq("frame")
                    .eq("createBy")
        .getWrapper().ne(condition.get("pid") != null && (Long)condition.get("pid") == -1,"pid",0)
                .eq(condition.get("pid") != null && (Long)condition.get("pid") != -1 , "pid",condition.get("pid"));
    }

    @Override
    public List<VisualModule> getTree(ConditionMap entity) {
        List<VisualModule> list = search(entity);

        //按一级菜单分组
        Map<Long, List<VisualModule>> map = list.stream().collect(Collectors.groupingBy(VisualModule::getPid));

        list.forEach(VisualModule -> {
            VisualModule.setChildren(map.containsKey(VisualModule.getId()) ? map.get(VisualModule.getId()) : new ArrayList<>());
        });

        return map.get(0L);
    }
}
