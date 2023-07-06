package com.jnetdata.msp.metadata.es.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.es.EsFieldInfo;
import com.jnetdata.msp.core.service.EsCommonService;
import com.jnetdata.msp.core.dbsource.model.DbSource;
import com.jnetdata.msp.core.dbsource.service.DbSourceService;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.*;

@Controller
@RequestMapping("/metadata/es")
@ApiModel(value = "es元数据管理(TableinfoController)", description = "es元数据管理")
public class EsTableinfoController extends BaseController<Long, Tableinfo> {

    @Autowired
    EsCommonService service;
    @Autowired
    DbSourceService dbSourceService;
    @Autowired
    FieldinfoService fieldinfoService;
    @Autowired
    TableinfoService tableinfoService;

    @PostMapping(value = "/create")
    @ResponseBody
    @ApiOperation(value = "创建索引")
    public JsonResult create(@RequestBody EsTableinfo entity) {
        service.createIndices(entity.getDbSourceId(),entity.getIndex());
        return renderSuccess();
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    @ApiOperation(value = "删除索引")
    public JsonResult delete(@RequestBody EsTableinfo entity) {
        service.deleteIndices(entity.getDbSourceId(),entity.getIndex());
        return renderSuccess();
    }

    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "根据实体属性查询")
    public JsonResult getList(@RequestBody BaseVo<EsTableinfo> vo) {
        Set<String> allIndices = service.getAllIndices(vo.getEntity().getDbSourceId());
        Pager pager = vo.getPager().toPager();
        pager.setRecords(new ArrayList(allIndices));
        return renderSuccess(pager);
    }

    @GetMapping(value = "/all")
    @ResponseBody
    @ApiOperation(value = "查询所有")
    public JsonResult<List<EsTableinfo>> all() {
        List<EsTableinfo> esTableinfos = new ArrayList<>();
        List<DbSource> list = dbSourceService.allByType("ES");
        for (DbSource dbSource : list) {
            Set<String> allIndices = service.getAllIndices(dbSource);
            for (String allIndex : allIndices) {
                esTableinfos.add(new EsTableinfo(dbSource.getId(),allIndex));
            }
        }
        return renderSuccess(esTableinfos);
    }

    @GetMapping(value = "/tree")
    @ResponseBody
    @ApiOperation(value = "tree")
    public JsonResult<List<DbSource>> tree() {
        List<DbSource> list = dbSourceService.allByType("ES");
        for (DbSource dbSource : list) {
            try{
                Set<String> allIndices = service.getAllIndices(dbSource);
                List<EsTableinfo> list1 = new ArrayList<>();
                for (String allIndex : allIndices) {
                    list1.add(new EsTableinfo(dbSource.getId(),allIndex));
                }
                dbSource.setList(list1);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return renderSuccess(list);
    }

    @PostMapping(value = "/fieldinfo/list")
    @ResponseBody
    @ApiOperation(value = "元数据字段列表")
    public JsonResult fieldinfoList(@RequestBody BaseVo<EsTableinfo> vo) {
        List<Map<String, Object>> fields = service.getFields(vo.getEntity().getDbSourceId(),vo.getEntity().getIndex());
        //TODO 修改es字段匹配方式
//        List<Fieldinfo> fieldinfos = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableName", vo.getEntity().getIndex()));
//        Map<String, Fieldinfo> collect = fieldinfos.stream().collect(Collectors.toMap(m -> m.getFieldname(), m -> m));
//        for (Map<String, Object> field : fields) {
//
//        }
        Pager pager = vo.getPager().toPager();
        pager.setRecords(fields);

        return renderSuccess(pager);
    }

    @PostMapping(value = "/addField")
    @ResponseBody
    @ApiOperation(value = "addField")
    public JsonResult addField(@RequestBody EsFieldInfo entity) {
       service.addField(entity);
       return renderSuccess();
    }

    @GetMapping(value = "/saveToDb")
    @ResponseBody
    @ApiOperation(value = "saveToDb")
    public JsonResult saveToDb() {
        List<EsTableinfo> esTableinfos = new ArrayList<>();
        List<DbSource> list = dbSourceService.allByType("ES");
        for (DbSource dbSource : list) {
            Set<String> allIndices = service.getAllIndices(dbSource);
            for (String allIndex : allIndices) {
                EsTableinfo esTableinfo = new EsTableinfo(dbSource.getId(), allIndex);
                esTableinfo.setFieldInfo(service.getEsFields(dbSource.getId(),allIndex));
                esTableinfos.add(esTableinfo);
            }
        }
        tableinfoService.saveEsToDb(esTableinfos);
        return renderSuccess();
    }
}
