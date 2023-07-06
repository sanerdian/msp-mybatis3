package com.jnetdata.msp.cs1.esunstructured.controller;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import com.jnetdata.msp.cs1.esunstructured.service.EsunstructuredService;
import com.jnetdata.msp.cs1.esunstructured.model.Esunstructured;

@Controller
@RequestMapping("/cs1/esunstructured")
public class EsunstructuredController extends BaseController {

    @Autowired
    EsunstructuredService esunstructuredService;

    @ApiOperation(value = "查看", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Esunstructured> getById(@PathVariable("id") String id) {
        return renderSuccess(getService().getById(id));
    }

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<String>> add(@Validated @RequestBody Esunstructured entity) {
        return renderSuccess(new EntityIdVo(getService().insert(entity)));
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
    public JsonResult<Void> updateById(@PathVariable("id") String id, @RequestBody Esunstructured entity) {
        getService().updateById(id,entity);
        return renderSuccess();
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value="删除")
    public JsonResult deleteById(@PathVariable("id") String id) {
        //TODO 删除不成功
        return renderSuccess(getService().delete(id));
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation(value="删除")
    public JsonResult delete(@PathVariable("id") String id) {
        //TODO 删除不成功
        return renderSuccess(getService().delete(id));
    }

    /**
     * 执行删除操作
     * @param ids
     * @return
     */
    @PostMapping("/delBatch")
    @ResponseBody
    @ApiOperation(value="删除")
    public JsonResult delBatch(@RequestBody String[] ids) {
        getService().deleteBatch(ids);
        return renderSuccess();
    }

    @PostMapping("/listing")
    @ResponseBody
    public JsonResult search(@RequestBody BaseVo<Esunstructured> vo) {
        Pager<Esunstructured> pager = getService().list(vo.getPager(), vo.getEntity());
        return renderSuccess(pager);
    }

    private EsunstructuredService getService() {
        return this.esunstructuredService;
    }

}