package com.jnetdata.msp.visual.relationmodulefield.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.fieldinfo.mapper.FieldinfoMapper;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.moduleinfo.mapper.ModuleinfoMapper;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.util.ObjectUtil;
import com.jnetdata.msp.visual.enums.Logic;
import com.jnetdata.msp.visual.relationmodulefield.mapper.RelationModuleFieldMapper;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.FieldinfoVo;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RelationModuleFieldServiceImpl extends BaseServiceImpl<RelationModuleFieldMapper, RelationModuleField> implements RelationModuleFieldService {


    @Resource
    private TableinfoMapper tableinfoMapper;

    @Resource
    private FieldinfoMapper fieldinfoMapper;

    @Resource
    private ModuleinfoMapper moduleinfoMapper;

    @Override
    public PropertyWrapper<RelationModuleField> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 保存组件与字段的关联信息
     * @param list 字段信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    @Override
    public void saveRelation(List<RelationModuleField> list, Long moduleId, String moduleType){
        try {
            if(CollectionUtils.isEmpty(list)){return;}
            if(CollectionUtils.isEmpty(list)){
                log.error("list为空");
            }
            if(ObjectUtils.isEmpty(moduleId)){
                log.error("moduleId为空");
            }
            if(StringUtils.isEmpty(moduleType)){
                log.error("moduleType为空");
            }
            int fieldOrder = 1;//字段排序
            for(RelationModuleField relation: list){
                if(ObjectUtils.isEmpty(relation) || ObjectUtil.isEmpty(relation.getFieldId())){ continue;}
                relation.setModuleId(moduleId);
                relation.setModuleType(moduleType);
                relation.setFieldOrder(fieldOrder);
                relation.setCreateTime(new Date());
                this.insert(relation);
                fieldOrder++;
            }
        }catch (Exception e){
            log.error("保存组件与字段的关联信息异常：{}", e.getMessage());
        }
    }

    /**
     * 删除组件与字段的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    @Override
    public void deleteRelation(Long moduleId, String moduleType){
        try {
            if(ObjectUtils.isEmpty(moduleId)){
                log.error("moduleId为空");
            }
            if(StringUtils.isEmpty(moduleType)){
                log.error("moduleType为空");
            }
            List<RelationModuleField> list = this.list(new PropertyWrapper<>(RelationModuleField.class)
                    .eq("moduleId", moduleId).eq("moduleType", moduleType).getWrapper());
            for(RelationModuleField relation: list){
                this.deleteById(relation.getId());
            }
        }catch (Exception e){
            log.error("删除表格组件与字段的关联信息异常：{}", e.getMessage());
        }
    }

    /**
     * 查询组件与字段的关联信息
     * @param moduleId 组件id
     * @param moduleType 组件类型code
     */
    @Override
    public List<RelationModuleField> getRelation(Long moduleId, String moduleType){
        try {
            //获取组件与字段关联信息
            List<RelationModuleField> fieldList = this.list(new PropertyWrapper<>(RelationModuleField.class)
                    .eq("moduleType", moduleType)
                    .eq("moduleId", moduleId).getWrapper());
            if(CollectionUtils.isEmpty(fieldList)){ return null; }

            //排序
            Collections.sort(fieldList);

            //获取字段中文名和英文名
            for(RelationModuleField field: fieldList){
                Fieldinfo fieldinfo = fieldinfoMapper.selectById(field.getFieldId());
                field.setFieldname(Fieldinfo.fieldToProperty(fieldinfo.getFieldname()));
                field.setAnothername(fieldinfo.getAnothername());
            }

            return fieldList;
        }catch (Exception e){
            log.error("获取组件关联的字段信息异常：{}, 组件类型：{}, 组件id：{}", e.getMessage(), moduleType, moduleId);
            return null;
        }
    }

    /**
     * 获取组件关联的元数据信息（字段信息、模块名、实体名）
     */
    @Override
    public ModuleRelation getModuleRelationEntity(RelationModuleTemplate relation){
        try {
            ModuleRelation moduleRelation = new ModuleRelation();
            //获取字段信息
            List<RelationModuleField> fieldList = this.getRelation(relation.getModuleId(), relation.getModuleType());
            moduleRelation.setFieldList(fieldList);

            //获取组件关联的微服务名和实体名
            if(!CollectionUtils.isEmpty(fieldList)){
                String fieldId = fieldList.get(0).getFieldId();
                String[] names = this.getNames(fieldId, null);
                moduleRelation.setServiceName(names[0]);
                moduleRelation.setEntityName(names[1]);
            }else if(!StringUtils.isEmpty(relation.getDbTableId())){
                String[] names = this.getNames(null, relation.getDbTableId());
                moduleRelation.setServiceName(names[0]);
                moduleRelation.setEntityName(names[1]);
            }
            return moduleRelation;
        }catch (Exception e){
            log.error("获取组件关联的元数据信息（字段信息、模块名、实体名）异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取组件关联的微服务名和实体名
     */
    private String[] getNames(String fieldId, String tableId){
        try {
            if(StringUtils.isEmpty(tableId)){
                //字段信息
                Fieldinfo fieldinfo = fieldinfoMapper.selectById(fieldId);
                tableId = fieldinfo.getTableid().toString();
            }
            //表信息
            Tableinfo tableinfo = tableinfoMapper.selectById(tableId);
            //模块信息
            Moduleinfo moduleinfo = moduleinfoMapper.selectById(tableinfo.getOwnerid());
            //微服务名称
            String serviceName = moduleinfo.getEnglishname();
            //根据数据库表名，获取元数据实体名称
            String[] strs = tableinfo.getTablename().split("_");
            String[] strsNew = new String[strs.length-1];
            System.arraycopy(strs, 1, strsNew, 0, strsNew.length);
            String entityName = String.join("",strsNew).toLowerCase();
            return new String[]{serviceName, entityName};
        }catch (Exception e){
            log.error("获取组件关联的微服务名和实体名异常:{}, 字段id：{}", e.getMessage(), fieldId);
            return null;
        }
    }

    /**
     * 获取字段列表
     */
    @Override
    public JsonResult<List<FieldinfoVo>> list(FieldinfoVo entity){
        List<FieldinfoVo> fieldList = new ArrayList<>();
        try {
            if(StringUtils.isEmpty(entity.getDbTableId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "没有对应的模板，或对应多个模板");
            }
            //获取所有的字段信息
            List<Fieldinfo> list = fieldinfoMapper.selectList(new PropertyWrapper<>(Fieldinfo.class)
                    .eq("tableid", entity.getDbTableId()).getWrapper());
            for(Fieldinfo fieldinfo: list){
                FieldinfoVo vo = new FieldinfoVo();
                BeanUtils.copyProperties(fieldinfo, vo);
                fieldList.add(vo);
            }

            /**
             * 如果组件已经关联字段：
             * 1.获取已经关联的字段id并排序
             * 2.按顺序提取已经关联的字段信息
             * 3.添加没有关联的字段信息
             */
            if(!ObjectUtils.isEmpty(entity.getModuleId()) && !StringUtils.isEmpty(entity.getModuleType())){
                //获取组件与字段的关联信息
                List<RelationModuleField> relationList = this.list(new PropertyWrapper<>(RelationModuleField.class)
                        .eq("moduleType", entity.getModuleType())
                        .eq("moduleId", entity.getModuleId()).getWrapper());
                if(CollectionUtils.isEmpty(relationList)){
                    return JsonResult.success(fieldList);
                }
                //排序
                Collections.sort(relationList);
                //提取字段id，获取字段id与关联关系的映射
                List<Long> idList = new ArrayList<>();
                Map<Long, RelationModuleField> relationMap = new HashMap<>();
                for(RelationModuleField relation: relationList){
                    idList.add(Long.parseLong(relation.getFieldId()));
                    relationMap.put(Long.parseLong(relation.getFieldId()), relation);
                }

                //获取字段id与字段信息的映射
                Map<Long, FieldinfoVo> map = new HashMap<>();
                for(FieldinfoVo vo: fieldList){
                    map.put(vo.getId(), vo);
                }

                //按顺序提取已经关联的字段信息
                List<FieldinfoVo> fieldList2 = new ArrayList<>();
                for(Long id: idList){
                    if(map.containsKey(id)){
                        FieldinfoVo vo = map.get(id);
                        //设置是否已选
                        vo.setSelectFlag(Logic.YES.getCode());
                        //获取关联事件
                        RelationModuleField relation = relationMap.get(id);
                        vo.setEventType(relation.getEventType());
                        vo.setStyleFlag(relation.getStyleFlag());
                        vo.setShowWidth(relation.getShowWidth());
                        fieldList2.add(vo);
                    }
                }

                //添加没有关联的字段信息
                for(Map.Entry<Long, FieldinfoVo> mapEntry: map.entrySet()){
                    if(!idList.contains(mapEntry.getKey())){
                        FieldinfoVo vo = mapEntry.getValue();
                        vo.setSelectFlag(Logic.NO.getCode());
                        fieldList2.add(vo);
                    }
                }
                return JsonResult.success(fieldList2);
            }
        }catch (Exception e){
            return JsonResult.fail();
        }
        return JsonResult.success(fieldList);
    }
}