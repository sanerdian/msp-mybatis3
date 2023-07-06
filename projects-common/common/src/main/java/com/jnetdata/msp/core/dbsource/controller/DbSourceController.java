package com.jnetdata.msp.core.dbsource.controller;
import com.jnetdata.msp.core.service.EsCommonService;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.ExportVo;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import javax.servlet.http.HttpServletResponse;
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

import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.dbsource.service.DbSourceService;
import com.jnetdata.msp.core.dbsource.model.DbSource;

/**
 * <p>
 * dbSource 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2022-12-06
 */
@Controller
@RequestMapping("/metadata/dbSource")
public class DbSourceController extends BaseController<Long,DbSource> {

    final private DbSourceService dbSourceService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;
    @Autowired
    private EsCommonService esCommonService;

    @Autowired
    public DbSourceController(DbSourceService dbSourceService) {
        this.dbSourceService = dbSourceService;
    }

    /**
     * 执行新增操作
     * @param entity
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody DbSource entity) {
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        esCommonService.flashClient(entity);
        return result;
    }

    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<DbSource> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    /**
     * 查看指定id的实体对象
     * @param id
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "查看", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<DbSource> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }

    /**
     * 执行删除操作
     * @param id
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public JsonResult<Void> deleteByIdrealGet(@PathVariable("id") Long id) {
        esCommonService.delClient(id);
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @GetMapping("/delete/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIdsrealGet(@PathVariable("ids") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateByIdPost(@PathVariable("id") Long id, @RequestBody DbSource entity) {
        entity.setId(id);
        esCommonService.flashClient(entity);
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdatePost(@RequestBody List<DbSource> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }


    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PostMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnByIdPost(@PathVariable("id") Long id, @RequestBody DbSource entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    public JsonResult importData(String columnid, MultipartFile file) throws Exception {
        //文件后缀不是.jpg
        if (!file.getOriginalFilename().contains(".xls") && !file.getOriginalFilename().contains(".xlsx")) {
            return renderError("请上传EXCEL文件");
        }

        InputStream in = file.getInputStream();
        List<List<String>> listob = ExcelUtil.importExcelStringInfo(in, file.getOriginalFilename());
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_dbSource"));

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

        List<DbSource> list2 = new ArrayList<>();
        list1.forEach(item -> {
            DbSource obj = MapUtil.map2Obj(item, DbSource.class);
            list2.add(obj);
        });

        getService().insertBatch(list2);

        return renderSuccess();
    }

    /**
     * 根据vo指定条件进行查询
     * @param entity
     * @param pager
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<DbSource>> list(@ModelAttribute DbSource entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }

    /**
     * 根据vo指定条件进行查询
     * @param vo
     * @author 王树彬
     * @date 2022/12/16
     * @return
     */
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<DbSource>> list(@RequestBody BaseVo<DbSource> vo) {
        if(vo.getEntity() == null) vo.setEntity(new DbSource());
        JsonResult<Pager<DbSource>> result = doList(getService(), vo.getPager(), vo.getEntity());
        return result;
    }

    @ApiOperation(value = "查询所有", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/all")
    @ResponseBody
    public JsonResult<List<DbSource>> all() {
        List<DbSource> list = getService().list();
        return renderSuccess(list);
    }

    /**
     * 根据vo指定条件进行查询
     * @param entity
     * @param vo
     * @param response
     * @author 王树彬
     * @date 2022/12/16
     */
    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出", hidden = true)
    @RequestMapping(value = "/export", method={RequestMethod.POST,RequestMethod.GET})
    public void export(DbSource entity, ExportVo vo, HttpServletResponse response) {
        List<DbSource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_dbSource").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),DbSource.class);
    }

    /**
     * 根据vo指定条件进行导出
     * @param vo
     * @param response
     * @author 王树彬
     * @date 2022/12/16
     */
    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export2")
    public void export2(@RequestBody ExportVo<DbSource> vo, HttpServletResponse response) {
        List<DbSource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(vo.getEntity()));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_dbSource").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),DbSource.class);
    }

    /**
     * 根据vo指定条件进行导出
     * @author 王树彬
     * @date 2022/12/16
     * @param vo
     * @param response
     */
    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @RequestMapping(value = "/export3", method={RequestMethod.POST,RequestMethod.GET})
    public void export3(ExportVo<DbSource> vo, HttpServletResponse response) {
        List<DbSource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            if(vo.getPageSize() == null || vo.getPageStartNo() == null ){
                datalist = getService().list(createCondition(vo.getEntity()));
            }else{
                datalist = new ArrayList<>();
                for(int i = vo.getPageStartNo(); i <= vo.getPageEndNo(); i++){
                    Pager pager1 = new Pager();
                    pager1.setCurrent(i);
                    pager1.setSize(vo.getPageSize());
                    Pager pager = getService().list(pager1,getService().makeSearchWrapper(createCondition(vo.getEntity())));
                    datalist.addAll(pager.getRecords());
                }
            }
        }
        if(vo.getFields() != null){
            DownMetadataUtil.downMetadata(datalist,response,vo.getExt(),vo.getIncludeTitle(),DbSource.class,vo.getFields());
        }else{
            List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_dbSource").eq("showList", 1));
            DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),DbSource.class);
        }
    }


    private DbSourceService getService() {
        return this.dbSourceService;
    }

}

