package com.jnetdata.msp.log.content.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.content.vo.ContentLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

/**
 * Created by TF on 2019/3/13.
 */

@Controller
@RequestMapping("/log/content")
@ApiModel(value = "ContentLogController", description = "内容操作日志")
@Api(tags = "内容操作日志(ContentLogController)")
public class ContentLogController extends BaseController<Long,ContentLog> {

    private static final String BASE_URL = "/log/content";
    @Autowired
    private ContentLogService service;


    @ApiOperation(value = "添加信息", notes="内容操作日志信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody ContentLog entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的内容操作日志信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("内容操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除内容操作日志信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(),ids);
    }

    /**
     * 修改指定id对应的内容操作日志信息
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "修改", notes="修改指定id对应的内容操作日志信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("内容操作日志id") @PathVariable("id") Long id,
                                       @RequestBody ContentLog entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定内容操作日志id对应的内容操作日志信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ContentLog> getById(@ApiParam("内容操作日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ContentLog>> list(@RequestBody ContentLogVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }


/*
* 通过Excel批量上传
* */
    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

    }
    /**
     *
     * @return
     */
    @GetMapping("/index")
    public String toList() {
        return pageFilePath("/list");
    }


    private String pageFilePath(String path) {
        return super.webPath(BASE_URL, path);
    }

    private ContentLogService getService() {
        return service;
    }

}
