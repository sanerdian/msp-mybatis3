package com.jnetdata.msp.metadata.tableinfo.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.jnetdata.msp.core.es.EsFieldInfo;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.es.controller.EsTableinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import com.jnetdata.msp.util.generator.converts.OracleTypeConvert;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.metadata.vo.Dbddl;
import com.jnetdata.msp.metadata.vo.TableField;
import com.jnetdata.msp.util.generator.rules.DbColumnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TableinfoServiceImpl extends BaseServiceImpl<TableinfoMapper, Tableinfo> implements TableinfoService {

    @Value("${spring.datasource.jdbcDialect}")
    private String jdbcDialect;

    @Autowired
    private ViewTableService viewTableService;

    @Autowired
    private FieldinfoService fieldinfoService;

    @Autowired
    private ModuleinfoService moduleinfoService;

    @Override
    protected PropertyWrapper<Tableinfo> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Tableinfo> e = createWrapperUtil(condition).eq("ownerid").ge("introducetime").like("anothername").like("tablename").like("introduceuser").eq("tabletype").getWrapper();
        e.le(condition.get("introducetimeto")!=null,"introducetime",condition.get("introducetimeto"));
        if(condition.get("searchName") != null){
            e.getWrapper().and(i -> i.like("TABLENAME",condition.get("searchName")+"").or().like("ANOTHERNAME",condition.get("searchName")+""));
        }
        e.le(condition.get("crtimeTo") != null, "crtimeTo", condition.get("crtimeTo"));
        return e;
    }

    @Override
    public void createTable(Tableinfo entity) {
        baseMapper.createTable(entity);
        Dbddl dbddl = Dbddl.getDbddl(jdbcDialect);
        if (dbddl.getAddSeq()) {
            try {
                baseMapper.dropSeq(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            baseMapper.createSeq(entity);
            baseMapper.createTri(entity);
        }
        if (dbddl.getAddComment()) {
            baseMapper.createComment(entity);
        }
    }

    @Override
    public List<Tableinfo> findByTableName(String tablename) {
        return list(createWrapper().eq("tablename", tablename));
    }

    @Override
    public List<String> existsTable(String tablename) {
        return baseMapper.existsTable(tablename);
    }

    @Override
    public void dropTable(String tablename) {
        baseMapper.dropTable(tablename);
    }

    @Override
    public void updateTablename(Tableinfo entity) {
        baseMapper.updateTablename(entity);
    }

    /*
    * 元数据变化
    * return
    * */
    @Override
    public List<Map<String, Object>> countNews() {
        DateTime now = new DateTime();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            DateTime time = now.offsetNew(DateField.DAY_OF_MONTH, -i);
            int count = super.count(new PropertyWrapper<>(Tableinfo.class).le("CRTIME", time.toJdkDate()));
            Map<String, Object> map = new HashMap<>();
            map.put("date", DateUtil.formatDate(time));
            map.put("count", count);
            list.add(map);
        }
        return list;
    }

    /*
    * 本周元数据新增数
    * return
    * */
    @Override
    public Map<String, Object> addThisWeek() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfWeek(now).toJdkDate();
        Date endDate = DateUtil.endOfWeek(now).toJdkDate();
        int count = super.count(new PropertyWrapper<>(Tableinfo.class).ge("CRTIME", startDate).le("CRTIME", endDate));
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return map;
    }

    /*
    * 查询本月元数据新增数
    * return
    * */
    @Override
    public Map<String, Object> addThisMonth() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfMonth(now).toJdkDate();
        Date endDate = DateUtil.endOfMonth(now).toJdkDate();
        int count = super.count(new PropertyWrapper<>(Tableinfo.class).ge("CRTIME", startDate).le("CRTIME", endDate));
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return map;
    }

    /*
    * 本年元数据新增数
    * return
    * */
    @Override
    public Map<String, Object> addThisYear() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfYear(now).toJdkDate();
        Date endDate = DateUtil.endOfYear(now).toJdkDate();
        int count = super.count(new PropertyWrapper<>(Tableinfo.class).ge("CRTIME", startDate).le("CRTIME", endDate));
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return map;
    }

    @Override
    public void toTop(String tablename, Long dataId) {
        baseMapper.toTop(tablename, dataId);
    }

    @Override
    public void reSetTop(String tablename, Long dataId) {
        baseMapper.reSetTop(tablename, dataId);
    }

    @Override
    public void clearRecycle(String tablename) {
        baseMapper.clearRecycle(tablename);
    }

    @Override
    public void insertInfo(String tablename, String fieldnames, List<Object> list) {
        baseMapper.insertInfo(tablename, fieldnames, list);
    }

    @Override
    public List<Map<String, Object>> selectMess(String fieldnames, String tablename, Long columnId) {
        return baseMapper.selectMess(fieldnames, tablename, columnId);
    }

    @Override
    public List<Map<String, Object>> selectMess2(String fieldnames, String id) {
        return baseMapper.selectMess2(fieldnames, id);
    }

    @Override
    public void insertTable(String fieldnames, List<Object> list) {
        baseMapper.insertTable(fieldnames, list);
    }

    @Override
    public int insertInfoBatch(List<Map<String, String>> params) {
        return batchInsert("insertInfoBatch", (List) params, 500);
    }

    @Override
    public void statusPass(int status, String[] ids, String tableName, String reason) {
        baseMapper.statusPass(status, String.join(",", ids), tableName, reason);
    }

    @Override
    public void createView(Tableinfo tableinfo, List<Fieldinfo> fieldInfos) {
        Tableinfo viewTable = new Tableinfo();
        List<String> fieldNames = new ArrayList<>();
        viewTable.setAnothername(tableinfo.getAnothername());
        viewTable.setTablename(tableinfo.getViewname());
        for (Fieldinfo fieldinfo : fieldInfos) {
            fieldNames.add(fieldinfo.getFieldname());
        }

        baseMapper.createView(tableinfo, fieldNames);//创建视图

        tableinfo.setId(null);
        tableinfo.setTablename(tableinfo.getViewname());
        tableinfo.setTabletype("view");
        super.insert(tableinfo);//视图表添加信息

        for (Fieldinfo fieldinfo : fieldInfos) {//添加视图字段信息
            fieldinfo.setId(null);
            fieldinfo.setTableid(tableinfo.getId());
            fieldinfo.setTablename(tableinfo.getTablename());
            fieldinfoService.insert(fieldinfo);
        }
    }

    @Override
    public List<TableField> getViewFields(String viewname) {
        return baseMapper.getViewFields(viewname);
    }

    @Override
    public List<Tableinfo> getDbTables(String name) {
        return baseMapper.getDbTables(name);
    }

    @Override
    public void addDbTableFields(Tableinfo tableinfo, Long groupId) {
        OracleTypeConvert oracleTypeConvert = new OracleTypeConvert();
        List<Fieldinfo> dbTableFields = baseMapper.getDbTableFields(tableinfo.getTablename());
        for (Fieldinfo dbTableField : dbTableFields) {
            dbTableField.setTableid(tableinfo.getId());
            dbTableField.setTablename(tableinfo.getTablename());
            dbTableField.setMatchType(1);
            dbTableField.setGroupid(groupId);

            DbColumnType dbColumnType = oracleTypeConvert.processTypeConvert(dbTableField.getDbTypeStr());
            dbTableField.setDbtype(dbColumnType.getDbType());
            dbTableField.setFieldtype(dbColumnType.getFieldType());
        }

        fieldinfoService.insertBatch(dbTableFields);
    }

    @Override
    public Map<String, Object> getOnePublish(long metaDataId, long tableId, PublishObj publishObj) {
        Map<String, Object> map = baseMapper.getTabName(tableId);
        return baseMapper.getOnePublish(metaDataId, String.valueOf(map.get("TABLENAME")), Long.valueOf(String.valueOf(map.get("PK"))));
    }

    @Override
    public List<Map<String, Object>> getListPublish(String metadataIds, long tableId, PublishObj publishObj) {
        Map<String, Object> map = baseMapper.getTabName(tableId);
        return baseMapper.getListPublish(metadataIds, String.valueOf(map.get("TABLENAME")), Long.valueOf(String.valueOf(map.get("PK"))));
    }

    @Override
    public List<Map<String, Object>> getListPublish(long channelId, long tableId, PublishObj publishObj) {
        String tableName;
        if (tableId > 0) {
            Map<String, Object> map = baseMapper.getTabName(tableId);
            tableName = String.valueOf(map.get("TABLENAME"));
            return baseMapper.getListPublish_channelId(channelId, tableName);
        }else{
            tableName= PublishConstants.PUBLISH_METADATA_TABLE_DOCUMENTINFO;
            //TODO xuning   因documentinfo跟元数据表字段不兼容,故跳过documentinfo表
            return new ArrayList<Map<String, Object>>();
        }
    }

    @Override
    public List<Map<String, Object>> getPubList(long channelId, long tableId, PublishObj publishObj) {
        String tableName;
        if (tableId > 0) {
            Map<String, Object> map = baseMapper.getTabName(tableId);
            tableName = String.valueOf(map.get("TABLENAME"));
            if(publishObj.getNum()==null){
                publishObj.setNum(baseMapper.getPubCount(channelId, tableName, null));
            }
            return baseMapper.getPubList(channelId, tableName, null, publishObj.getFrom(), publishObj.getNum(), publishObj.getOrder());
        }else{
            tableName= PublishConstants.PUBLISH_METADATA_TABLE_DOCUMENTINFO;
            //TODO xuning   因documentinfo跟元数据表字段不兼容,故跳过documentinfo表
            return new ArrayList<Map<String, Object>>();
        }
    }

    @Override
    public List<Map<String, Object>> getPubList(String chnlIds, long tableId, PublishObj publishObj) {
        String tableName;
        if (tableId > 0) {
            Map<String, Object> map = baseMapper.getTabName(tableId);
            tableName = String.valueOf(map.get("TABLENAME"));
            if(publishObj.getNum()==null){
                publishObj.setNum(baseMapper.getPubCount(null, tableName, chnlIds));
            }
            return baseMapper.getPubList(null, tableName, chnlIds, publishObj.getFrom(), publishObj.getNum(), publishObj.getOrder());
        }else{
            tableName= PublishConstants.PUBLISH_METADATA_TABLE_DOCUMENTINFO;
            //TODO xuning   因documentinfo跟元数据表字段不兼容,故跳过documentinfo表
            return new ArrayList<Map<String, Object>>();
        }
    }

    @Override
    public Tableinfo getTableinfo(Long dbSourceId,String name) {
        List<Tableinfo> list = super.list(new PropertyWrapper<>(Tableinfo.class).eq("dbsourceid", dbSourceId).eq("tablename", name.toUpperCase(Locale.ROOT)));
        if(list.isEmpty()) return null;
        return list.get(0);
    }

    @Override
    public void saveEsToDb(List<EsTableinfo> esTableinfos) {
        List<Tableinfo> tableinfoList = new ArrayList<>();
        List<Tableinfo> list = super.list(new PropertyWrapper<>(Tableinfo.class).eq("tabletype", "es"));
        Map<String, Long> collect1 = list.stream().collect(Collectors.toMap(m -> m.getDbsourceid() + ":" + m.getTablename(), m -> m.getId()));
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());


        for (EsTableinfo esTableinfo : esTableinfos) {
            Tableinfo tableinfo = new Tableinfo();
            String key = esTableinfo.getDbSourceId() + ":" + esTableinfo.getIndex().toUpperCase(Locale.ROOT);
            Map<String, Long> collect = new HashMap<>();
            if(collect1.containsKey(key)){
                tableinfo.setId(collect1.get(key));
                List<Fieldinfo> list1 = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", tableinfo.getId()));
                collect = list1.stream().collect(Collectors.toMap(m -> m.getFieldname(), m -> m.getId()));
            }
            tableinfo.setTablename(esTableinfo.getIndex());
            tableinfo.setTabletype("es");
            tableinfo.setAnothername(esTableinfo.getIndex());
            tableinfo.setDbsourceid(esTableinfo.getDbSourceId());
            tableinfoList.add(tableinfo);
            super.insertOrUpdate(tableinfo);

            List<Fieldinfo> fieldinfoList = new ArrayList<>();
            for (EsFieldInfo esFieldInfo : esTableinfo.getFieldInfo()) {
                Fieldinfo fieldinfo = new Fieldinfo();
                fieldinfo.setTableid(tableinfo.getId());
                fieldinfo.setTablename(tableinfo.getTablename());
                fieldinfo.setFieldname(esFieldInfo.getKey());
                fieldinfo.setAnothername(esFieldInfo.getKey());
                fieldinfo.setFieldtype(1);
                fieldinfoList.add(fieldinfo);

                if(!collect.isEmpty() && collect.containsKey(fieldinfo.getFieldname())){
                    fieldinfo.setId(collect.get(fieldinfo.getFieldname()));
                }
            }
            fieldinfoService.insertOrUpdateBatch(fieldinfoList);
        }
    }

    @Override
    public List<Map<String, Object>> getAllFromTable(String tablename) {
        return baseMapper.getAllFromTable(tablename);
    }

    @Override
    public void insertByListMap(List<Map<String, Object>> datas,String tableName,List<String> titles) {
        baseMapper.insertByListMap(datas,tableName,titles);
    }

    @Override
    public Pager<Tableinfo> pageByPermission(BaseVo<Tableinfo> vo) {
        if(vo.getEntity() == null) vo.setEntity(new Tableinfo());
        vo.getEntity().setTabletype("table");
        PropertyWrapper<Tableinfo> pw = makeSearchWrapper((Map)ConditionMap.of(vo.getEntity()));
        if(!SessionUser.getSubject().isPermitted("metadataModule:view")){
            List<Long> ids = moduleinfoService.allByMyPermission("view");
            pw.and(m -> m.in(!ids.isEmpty(), "ownerid",ids).or(!ids.isEmpty()).eq("CRNUMBER", SessionUser.getCurrentUser().getId()));
        }
        Pager list = list(vo.getPager().toPager(), pw);
        return list;
    }

}


