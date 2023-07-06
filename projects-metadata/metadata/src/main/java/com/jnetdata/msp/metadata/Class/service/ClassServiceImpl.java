package com.jnetdata.msp.metadata.Class.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.Class.mapper.ClassMapper;
import com.jnetdata.msp.metadata.Class.model.Class;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ClassServiceImpl extends BaseServiceImpl<ClassMapper,Class> implements ClassService {
    @Resource
    private ClassMapper classMapper;
    @Override
    protected PropertyWrapper<Class> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("classDesc")
                .like("className")
                .in("className")
                .eq("className")
                .eq("parentId")
                .eq("id")
                .getWrapper();
    }

    @Override
    public List<Class> list(Class obj) {
        return list(createWrapper().eq("parentId",obj.getParentId()));
    }

    @Override
    public Class selectlist(Class obj) {
        QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("bmname",obj.getBmname()).orderByDesc("crtime").last("limit 1");
        Class aClass = baseMapper.selectOne(classQueryWrapper);
        return aClass;
    }

    @Override
    public List<Class> selectAdd(Long id) {
        Class aClass = getById(id);
        ArrayList<Class> classes = new ArrayList<>();
        classes.add(aClass);
        List<Class> classes1 = getList(aClass.getId(), classes);
        return classes1;
    }

    @Override
    public List<Class> selectAdds(List<Long> ids) {
        List<Class> list = new ArrayList<>();
        for (Long id : ids) {
            list.addAll(selectAdd(id));
        }
        return list;
    }


    public List<Class> getList(Long id,ArrayList<Class> classes){
        QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("PARENTID",id);
        List<Class> classes1 = baseMapper.selectList(classQueryWrapper);
        if(classes1!=null){
            classes1.forEach(s->{classes.add(s);getList(s.getId(),classes);});
        }
        return classes;
    }

    public List<String> selectLab(List<Long> collect){
        List<Class> classes =baseMapper.selectBatchIds(collect);
        List<String> collect1 = classes.stream().map(Class::getClassName).collect(Collectors.toList());
        return collect1;
    }

    @Override
    public String treeList(Long id) {
        return null;
    }

    @Override
    public List<Class> getAllForModuld(Long id) {
        List<Class> list = super.list(new PropertyWrapper<>(Class.class).eq("moduleId", id));
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        return selectAdds(ids);
    }
}
