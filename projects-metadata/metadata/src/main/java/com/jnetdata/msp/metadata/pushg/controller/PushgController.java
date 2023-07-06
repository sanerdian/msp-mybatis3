package com.jnetdata.msp.metadata.pushg.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.metadata.push.model.PushModel;
import com.jnetdata.msp.metadata.pushg.model.PushgModel;
import com.jnetdata.msp.metadata.pushg.service.PushgService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tlujy/pushg")
@ApiModel(value = "群组管理", description = "群组管理")
public class PushgController  extends BaseController<Long , PushgModel> {

    @Resource
    private PushgService pushpService;

    @PostMapping("/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody PushgModel entity) {
        return doAdd(getService(), entity);
    }
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<PushgModel>> list(@RequestBody BaseVo<PushgModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }
    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
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
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") String ids) {
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody PushgModel entity) {
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
        List<PushgModel> list = new ArrayList<>();
        for (Long id : ids) {
            PushgModel entity = new PushgModel();
            entity.setId(id);
            //entity.setDocstatus(docstatus);
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody PushgModel entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "查看", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<PushgModel> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }


  /*  @ApiOperation(value = "查看(带关联表数据)", notes = "查看指定id的实体对象(带关联数据)")
    @GetMapping("/{id}/join")
    @ResponseBody
    public JsonResult<PushgModel> getjoin(@PathVariable("id") Long id) {
        PushgModel entity = getService().getEntityAndJoinsById(id);
        return renderSuccess(entity);
    }*/
    //根据用户查询新闻


    private PushgService getService() {
        return this.pushpService;
    }
}
