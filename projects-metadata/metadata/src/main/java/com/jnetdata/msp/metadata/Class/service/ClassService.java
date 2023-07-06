package com.jnetdata.msp.metadata.Class.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.Class.model.Class;

import java.util.Collection;
import java.util.List;

public interface ClassService extends BaseService<Class> {
    List<Class> list(Class obj);

    Class selectlist(Class obj );

    /**
     * 查询分类和子分类
     * @param id
     * @return
     */
    List<Class> selectAdd(Long id);

    List<Class> selectAdds(List<Long> ids);

    List<String> selectLab(List<Long> collect);

    String treeList(Long id);

    List<Class> getAllForModuld(Long id);
}
