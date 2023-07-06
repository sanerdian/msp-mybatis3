package com.jnetdata.msp.tlujy.xinwen020.controller;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.ExportVo;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;
import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

    import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.tlujy.xinwen020.service.Xinwen020Service;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-12-11
 */
@Controller
@RequestMapping("/tlujy/xinwen020")
public class Xinwen020Controller extends BaseController<Long,Xinwen020> {

    final private Xinwen020Service xinwen020Service;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public Xinwen020Controller(Xinwen020Service xinwen020Service) {
        this.xinwen020Service = xinwen020Service;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Xinwen020 entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "删除", notes= "根据指定id删除")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Xinwen020 entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性批量更新操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "选择性批量更新操作（放入回收站）", notes="只更新entity中设置为非null的属性")
    @PostMapping("/updateDocStatus")
    @ResponseBody
    public JsonResult<Void> updateBatchById(@RequestBody Long[] ids, Integer docstatus) {
        List<Xinwen020> list = new ArrayList<>();
        for (Long id : ids) {
        Xinwen020 entity = new Xinwen020();
        entity.setId(id);
        entity.setDocstatus(docstatus);
        list.add(entity);
        }
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Xinwen020 entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Xinwen020> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }


    @ApiOperation(value = "查看(带关联表数据)", notes = "查看指定id的实体对象(带关联数据)")
    @GetMapping("/{id}/join")
    @ResponseBody
    public JsonResult<Xinwen020> getjoin(@PathVariable("id") Long id) {
        Xinwen020 entity = getService().getEntityAndJoinsById(id);
        return renderSuccess(entity);
    }


    @ApiOperation(value = "查询列表(带关联表数据)", notes="根据vo指定条件进行查询(带关联数据)")
    @PostMapping(value = "/listing/join")
    @ResponseBody
    public JsonResult<Pager<Xinwen020>> listjoin(@RequestBody BaseVo<Xinwen020> vo) {
        JsonResult<Pager<Xinwen020>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<Xinwen020> list = pagerJsonResult.getObj().getRecords();
        getService().getListJoin(list,vo.getEntity());
        return pagerJsonResult;
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Xinwen020>> list(@ModelAttribute Xinwen020 entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<Xinwen020>> list(@RequestBody BaseVo<Xinwen020> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export")
    public void export(Xinwen020 entity, ExportVo vo, HttpServletResponse response) {
        List<Xinwen020> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    @ResponseBody
    public JsonResult importData(String columnid, MultipartFile file) throws Exception {
        //文件后缀不是.jpg
        if (!file.getOriginalFilename().contains(".xls") && !file.getOriginalFilename().contains(".xlsx")) {
            return renderError("请上传EXCEL文件");
        }

        InputStream in = file.getInputStream();
        List<List<String>> listob = ExcelUtil.importExcelStringInfo(in, file.getOriginalFilename());
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "JMETA_XINWEN020"));

        List<String> fieldNames = new ArrayList<>();
        Map<String, Fieldinfo> another2FieldMap = list.stream().collect(Collectors.toMap(Fieldinfo::getAnothername, v -> v, (v1, v2) -> v1));
        List<String> titles = listob.get(0);
        List<String> errTitles = new ArrayList<>();
        for (Object title : titles) {
            if (another2FieldMap.get(title) == null) {
                errTitles.add(String.valueOf(title));
            } else {
                fieldNames.add(another2FieldMap.get(title).getProName());
            }
        }
        if (errTitles.size() > 0) {
            return renderError("列“" + String.join("”“", errTitles) + "”不存在!");
        }

        listob.remove(0);

        List<Map<String,Object>> list1 = new ArrayList<>();
            listob.forEach(item -> {
                Map<String,Object> map = new HashMap<>();
                for(int i=0; i<fieldNames.size(); i++){
                    map.put(fieldNames.get(i),item.get(i));
                }
            list1.add(map);
        });

        List<Xinwen020> list2 = new ArrayList<>();
        list1.forEach(item -> {
            Xinwen020 obj = MapUtil.map2Obj(item, Xinwen020.class);
            obj.setColumnid(Long.valueOf(columnid));
            obj.setDocstatus(0);
            list2.add(obj);
        });

        getService().insertBatch(list2);

        return renderSuccess();
    }

    private Xinwen020Service getService() {
        return this.xinwen020Service;
    }

}

