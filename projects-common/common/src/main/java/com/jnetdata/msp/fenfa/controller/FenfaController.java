package com.jnetdata.msp.fenfa.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.fenfa.model.Fenfa;
import com.jnetdata.msp.fenfa.service.FenfaService;
import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.BaseVo;
import com.jnetdata.msp.vo.ExportVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 分发 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2021-03-16
 */
@Controller
@RequestMapping("/manage/fenfa")
public class FenfaController extends BaseController<Long, Fenfa> {

    final private FenfaService fenfaService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public FenfaController(FenfaService fenfaService) {
        this.fenfaService = fenfaService;
    }


    /**
     * 执行新增操作
     * @param entity
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Fenfa entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<Fenfa> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    /**
     * 查看指定id的实体对象
     * @param id
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Fenfa> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }

    /**
     * 执行删除操作
     * @param id
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @DeleteMapping("/real/{id}")
    @ResponseBody
    @ApiOperation(value = "删除", notes= "根据指定id删除")
    public JsonResult<Void> deleteByIdreal(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @DeleteMapping("/real/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIdsreal(@PathVariable("ids") @ApiParam("多个id用逗号隔开")String ids) {
        //删除已发布的
        String[] idss = ids.split(",");
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Fenfa entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdate(@RequestBody List<Fenfa> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Fenfa entity) {
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
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_fenfa"));

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

        List<Fenfa> list2 = new ArrayList<>();
        list1.forEach(item -> {
            Fenfa obj = MapUtil.map2Obj(item, Fenfa.class);
            obj.setColumnid(Long.valueOf(columnid));
//            obj.setDocstatus(0);
            list2.add(obj);
        });

        getService().insertBatch(list2);

        return renderSuccess();
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Fenfa>> list(@ModelAttribute Fenfa entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }

    /**
     * 根据vo指定条件进行查询
     * @param vo
     * @author 王树斌
     * @date 2021/3/23
     * @return
     */
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<Fenfa>> list(@RequestBody BaseVo<Fenfa> vo) {
        if(vo.getEntity() == null) vo.setEntity(new Fenfa());
            return doList(getService(), vo.getPager(), vo.getEntity());
    }

    /**
     * 根据vo指定条件进行导出
     * @param entity
     * @param vo
     * @param response
     * @author 王树斌
     * @date 2021/3/23
     */
    @ApiOperation(value = "根据参数导出", notes="根据vo指定条件进行导出", hidden = true)
    @RequestMapping(value = "/export", method={RequestMethod.POST,RequestMethod.GET})
    public void export(Fenfa entity, ExportVo vo, HttpServletResponse response) {
        List<Fenfa> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_fenfa").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),Fenfa.class);
    }

    /**
     * 根据vo指定条件进行导出
     * @param vo
     * @param response
     * @author 王树斌
     * @date 2021/3/23
     */
    @ApiOperation(value = "根据json参数导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export2")
    public void export2(@RequestBody ExportVo<Fenfa> vo, HttpServletResponse response) {
        List<Fenfa> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(vo.getEntity()));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_fenfa").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),Fenfa.class);
    }


    private FenfaService getService() {
        return this.fenfaService;
    }

}

