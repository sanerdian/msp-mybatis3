package com.jnetdata.msp.metadata.fieldinfo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.dict.model.Dict;
import com.jnetdata.msp.metadata.dict.service.DictService;
import com.jnetdata.msp.metadata.fieldinfo.mapper.FieldinfoMapper;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.vo.FieldinfoVo;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.vo.Dbddl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.IQueryWrapper;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class FieldinfoServiceImpl  extends BaseServiceImpl<FieldinfoMapper, Fieldinfo> implements FieldinfoService {

    @Value("${spring.datasource.jdbcDialect}")
    private String jdbcDialect;
    @Autowired
    private DictService dictService;
    @Autowired
    private MetadataGroupService metadataGroupService;

    @Override
    protected PropertyWrapper<Fieldinfo> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Fieldinfo> wrapper = createWrapperUtil(condition)
                .eq("tableid" )
                .like("fieldname" )
                .eq("dblength")
                .eq("dbtype")
                .eq("fieldtype")
                .like("crUser")
                .eq("groupid")
                .ge("createTime")
                .like("anothername")
                .eq("id")
                .eq("matchType")
                .eq("showList")
                .eq("showSearch")
                .eq("showDetail")
                .getWrapper();
        wrapper.le(condition.get("crtimeTo") != null,"createTime",condition.get("crtimeTo"));
        if(condition.get("searchName") != null){
            wrapper.getWrapper().and(i -> i.like("FIELDNAME",condition.get("searchName")+"").or().like("ANOTHERNAME",condition.get("searchName")+""));
        }
        return wrapper;
    }

    @Override
    public void deleteByTableinfoId(Long id) {
        delete(createWrapper().eq("tableid",id));
    }

    @Override
    public void updateTableGen(Long id) {
        baseMapper.updateTableGen(id);
    }

    @Override
    public List<Fieldinfo> list(Fieldinfo entity) {
        return list(createWrapper().eq("tableid",entity.getTableid()).eq("fieldname",entity.getFieldname()));
    }

    @Override
    public List<String> getTableField(String tablename) {
        return baseMapper.getTableField(tablename);
    }

    @Override
    public void createField(Fieldinfo entity) {
        baseMapper.createField(entity);
        if(Dbddl.getDbddl(jdbcDialect).getAddComment()) baseMapper.createComment(entity);
    }

    @Override
    public boolean updateField(Fieldinfo entity) {
        if(entity.getDbtype() == null) return super.updateById(entity);
        Dict dict = dictService.get("db_type_" + jdbcDialect,entity.getDbtype());
        entity.setDbTypeStr(dict.getCode());
        entity.setOldname(this.getById(entity.getId()).getFieldname());
        if (!entity.getOldname().equals(entity.getFieldname())) {
            this.updateFieldName(entity);
        }
        baseMapper.updateField(entity);
        if(Dbddl.getDbddl(jdbcDialect).getAddComment()) baseMapper.createComment(entity);
        // TODO postgresql修改默认值方发
        return super.updateById(entity);
    }

    @Override
    public void updateFieldName(Fieldinfo entity) {
        baseMapper.updateFieldName(entity);
    }

    @Override
    public void deleteField(Fieldinfo obj) {
        baseMapper.deleteField(obj);
    }

    @Override
    public void sort(Fieldinfo entity) {
        baseMapper.sort(entity);
    }

    @Override
    public List<Fieldinfo> getByIds(Long[] ids) {
        List<Fieldinfo> list = super.list(new PropertyWrapper<>(Fieldinfo.class).in("id", Arrays.asList(ids)));
        Map<Long, Fieldinfo> FieldMap = list.stream().collect(Collectors.toMap(m -> m.getId(), m -> m));
        List<Fieldinfo> newList = new ArrayList();
        for (Long id : ids) {
            newList.add(FieldMap.get(id));
        }
        return newList;
    }

    @Override
    public String addField(Fieldinfo entity) {
        List<Fieldinfo> list = this.list(entity);
        String result = null;
        if(list.size()>0){
            //TODO 再判断表里有没有字段
            result = "字段已存在";
        }else{
            if(entity.getDbtype() == null ){
                super.insert(entity);
            }else {
                List<String> fieldList = this.getTableField(entity.getTablename());
                if (fieldList != null && fieldList.contains(entity.getFieldname())) {
                    result = "字段已存在";
                } else {
                    Dict dict = dictService.get("db_type_"+jdbcDialect, entity.getDbtype());
                    if(dict!=null) entity.setDbTypeStr(dict.getCode());
                    this.createField(entity);
                    super.insert(entity);
                }
            }
        }
        if(entity.getId() == null) return result;
        Fieldinfo orderEntity = new Fieldinfo();
        orderEntity.setId(entity.getId());
        orderEntity.setFieldorder(entity.getId());
        super.updateById(orderEntity);
        return result;
    }

    @Override
    public Pager<Fieldinfo> getFields(PageVo1<Fieldinfo> pager, ConditionMap conditionMap) {
        List<MetadataGroup> list = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).ne("parentid", 0));
        List<MetadataGroup> parentList = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).eq("parentid", 0));
        Map<Long, MetadataGroup> maps = parentList.stream().collect(Collectors.toMap(m -> m.getId(), m -> m));
        for (MetadataGroup metadataGroup : list) {
            metadataGroup.setName(maps.get(metadataGroup.getParentid()).getName() + "." + metadataGroup.getName());
        }
        Map<Integer, String> db_type = dictService.list("db_type_"+jdbcDialect).stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Long, String> group_name = list.stream().collect(Collectors.toMap(MetadataGroup::getId, MetadataGroup::getName));

        Pager<Fieldinfo> result = this.search(pager.toPager(), conditionMap);
//        JsonResult<Pager<Fieldinfo>> result = doList(getService(),vo.getPager(),vo.getEntity());
        result.getRecords().forEach(res -> {
            res.setDbTypeStr(db_type.get(res.getDbtype()));
            res.setFieldTypeStr(field_type.get(res.getFieldtype()));
            res.setGroupname(group_name.get(res.getGroupid()));
        });
        return result;
    }

    @Override
    public Map<String, List<Fieldinfo>> getGroupFields(Long tableId) {
        List<MetadataGroup> list = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).ne("parentid", 0));
        Map<Integer, String> db_type = dictService.list("db_type_"+jdbcDialect).stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Long, String> group_name = list.stream().collect(Collectors.toMap(MetadataGroup::getId, MetadataGroup::getName));
        List<Pair<String,Boolean>> sp = new ArrayList<>();
        sp.add(new Pair("fieldorder",true));
        List<Fieldinfo> result = super.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid",tableId).orderBy(sp));
        result.forEach(res -> {
            res.setDbTypeStr(db_type.get(res.getDbtype()));
            res.setFieldTypeStr(field_type.get(res.getFieldtype()));
            res.setGroupname(group_name.get(res.getGroupid()));
        });
        Map<String, List<Fieldinfo>> collect = result.stream().filter(m -> m.getGroupname() != null).collect(Collectors.groupingBy(Fieldinfo::getGroupname));
        List<Fieldinfo> listnull = result.stream().filter(m -> m.getGroupname() == null).collect(Collectors.toList());
        if(listnull.size() > 0) collect.put("无分组",listnull);
        return collect;
    }

    @Override
    public List<Fieldinfo> lister(Long tableid) {
        QueryWrapper<Fieldinfo> fieldinfoQueryWrapper = new QueryWrapper<>();
        fieldinfoQueryWrapper.eq("tableid", tableid);
        List<Fieldinfo> fieldinfos = baseMapper.selectList(fieldinfoQueryWrapper);
        return fieldinfos;
    }

    @Override
    public List<String> selectErtable(String tablename1) {
        List<String> strings = baseMapper.selectEr(tablename1);
        return strings;
    }

    @Override
    public List<Map> getErtable(String tablename1) {
        List<Map> map = baseMapper.selectErMap(tablename1);
        return map;
    }

    @Override
    public List<String> excleEr(String tablename) {
        List<String> strings = baseMapper.excleEr(tablename);
        return strings;
    }

    @Override
    public String getMajorKey(String tablename) {
        String majorKey = baseMapper.getMajorKey(tablename);
        return majorKey;
    }

    @Override
    public List<String> getErPhone(String tablename) {
       List<String> s = baseMapper.selectErPhone(tablename);
        return s ;
    }

}


