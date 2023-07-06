package com.jnetdata.msp.resources.audio.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.resources.audio.model.Audio;
import com.jnetdata.msp.resources.audio.service.AudioService;
import com.jnetdata.msp.resources.audio.vo.AudioVo;
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
@RequestMapping("/config/audio")
@ApiModel(value = "音频配置(AudioController)", description = "音频配置")
public class AudioController extends BaseController<Long,Audio> {
    private static final String BASE_URL = "/config/audio";

    @Autowired
    private AudioService service;


    @ApiOperation(value = "添加音频信息", notes="添加音频信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Audio entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的音频信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除音频信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的音频信息(只需要填音频中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                       @RequestBody Audio entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定音频id对应的音频信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Audio> getById(@ApiParam("音频信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Audio>> list(@RequestBody AudioVo vo) {
        return doList(getService(), vo.getPager(), vo.getAudio());
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

    private AudioService getService() {
        return service;
    }




}
