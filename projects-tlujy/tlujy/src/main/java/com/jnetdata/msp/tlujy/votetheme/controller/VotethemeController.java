package com.jnetdata.msp.tlujy.votetheme.controller;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.ExportVo;
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
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.tlujy.votetheme.service.VotethemeService;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-10-18
 */
@Controller
@RequestMapping("/tlujy/votetheme")
public class VotethemeController extends BaseController<Long,Votetheme> {

    final private VotethemeService votethemeService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public VotethemeController(VotethemeService votethemeService) {
        this.votethemeService = votethemeService;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Votetheme entity) {
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Votetheme entity) {
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
        List<Votetheme> list = new ArrayList<>();
        for (Long id : ids) {
        Votetheme entity = new Votetheme();
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Votetheme entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Votetheme> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }


    @ApiOperation(value = "查看(带关联表数据)", notes = "查看指定id的实体对象(带关联数据)")
    @GetMapping("/{id}/join")
    @ResponseBody
    public JsonResult<Votetheme> getjoin(@PathVariable("id") Long id) {
        Votetheme entity = getService().getEntityAndJoinsById(id);
        return renderSuccess(entity);
    }


    @ApiOperation(value = "查询列表(带关联表数据)", notes="根据vo指定条件进行查询(带关联数据)")
    @PostMapping(value = "/listing/join")
    @ResponseBody
    public JsonResult<Pager<Votetheme>> listjoin(@RequestBody BaseVo<Votetheme> vo) {
        JsonResult<Pager<Votetheme>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<Votetheme> list = pagerJsonResult.getObj().getRecords();
        getService().getListJoin(list,vo.getEntity());
        return pagerJsonResult;
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Votetheme>> list(@ModelAttribute Votetheme entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<Votetheme>> list(@RequestBody BaseVo<Votetheme> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export")
    public void export(Votetheme entity, ExportVo vo, HttpServletResponse response) {
        List<Votetheme> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    private VotethemeService getService() {
        return this.votethemeService;
    }

}

