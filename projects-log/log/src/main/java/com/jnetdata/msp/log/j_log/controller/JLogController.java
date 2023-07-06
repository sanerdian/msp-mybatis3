package com.jnetdata.msp.log.j_log.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
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
import java.util.Arrays;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.log.j_log.model.JLog;

/**
 * <p>
 * 日志 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2021-02-02
 */
@Controller
@RequestMapping("/msplog/jlog")
@ApiModel(value = "JLogController", description = "Jlog日志")
@Api(tags = "Jlog日志(JLogController)")
public class JLogController extends BaseController<Long,JLog> {

    final private JLogService jLogService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public JLogController(JLogService jLogService) {
        this.jLogService = jLogService;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加操作", notes="根据提供的实体属性添加日志")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody JLog entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<JLog> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<JLog> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody JLog entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdate(@RequestBody List<JLog> list) {
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody JLog entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "查看(带关联表数据)", notes = "查看指定id的实体对象(带关联数据)")
    @GetMapping("/{id}/join")
    @ResponseBody
    public JsonResult<JLog> getjoin(@PathVariable("id") Long id) {
        JLog entity = getService().getEntityAndJoinsById(id);
        return renderSuccess(entity);
    }


    @ApiOperation(value = "查询列表(带关联表数据)", notes="根据vo指定条件进行查询(带关联数据)")
    @PostMapping(value = "/listing/join")
    @ResponseBody
    public JsonResult<Pager<JLog>> listjoin(@RequestBody BaseVo<JLog> vo) {
        JsonResult<Pager<JLog>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<JLog> list = pagerJsonResult.getObj().getRecords();
        getService().getListJoin(list,vo.getEntity());
        return pagerJsonResult;
    }

    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<JLog>> list(@ModelAttribute JLog entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<JLog>> list(@RequestBody BaseVo<JLog> vo) {
        if(vo.getEntity() == null) vo.setEntity(new JLog());
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "查询登录和退出列表", notes="查询登录和退出列表")
    @PostMapping(value = "/listingg")
    @ResponseBody
    public JsonResult<Pager<JLog>> listing(@RequestBody BaseVo<JLog> vo) {
        if(vo.getEntity() == null) {
            JLog jLog = new JLog();
            jLog.setHandletype("登录");
            vo.setEntity(jLog);
        }
        vo.getEntity().setHandletype("登录");
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "根据字段条件导出", notes="根据vo指定条件进行导出")
    @RequestMapping(value = "/export", method={RequestMethod.POST,RequestMethod.GET})
    public void export(JLog entity, ExportVo vo, HttpServletResponse response) {
        List<JLog> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_j_log").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    @ApiOperation(value = "根据json对象参数导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export2")
    public void export2(@RequestBody ExportVo<JLog> vo, HttpServletResponse response) {
        List<JLog> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(vo.getEntity()));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_j_log").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }


    private JLogService getService() {
        return this.jLogService;
    }

}

