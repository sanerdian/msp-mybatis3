package com.jnetdata.msp.resources.video.Controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.resources.video.model.Video;
import com.jnetdata.msp.resources.video.service.VideoService;
import com.jnetdata.msp.resources.video.vo.VideoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

@Controller
@RequestMapping("/resources/video")
@ApiModel(value = "VideoController", description = "视频配置")
public class VideoController extends BaseController<Long,Video> {
    @Autowired
    private VideoService service;

    /*添加视频信息*/
    @ApiOperation(value = "添加视频信息", notes="添加视频信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Video entity) {
        return doAdd(getService(), entity);
    }

    /*删除指定id对应的视频信息*/
    @ApiOperation(value = "删除", notes="删除指定id对应的视频信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /*根据多个id删除视频信息*/
    @ApiOperation(value = "批量删除", notes="根据多个id删除视频信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    /*修改指定id对应的视频信息*/
    @ApiOperation(value = "修改", notes="修改指定id对应的视频信息(只需要填视频中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                       @RequestBody Video entity) {
        return doUpdateById(getService(), id, entity);
    }

    /*查看指定视频id对应的视频信息*/
    @ApiOperation(value = "根据id查询", notes="查看指定视频id对应的视频信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Video> getById(@ApiParam("视频信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    /*根据实体属性查询*/
    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Video>> list(@RequestBody VideoVo vo) {
        return doList(getService(), vo.getPager(), vo.getVideo());
    }

    private VideoService getService() {
        return service;
    }




}
