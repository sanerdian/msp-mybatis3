package com.jnetdata.msp.message.msgConfig.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.message.msg.service.MsgService;
import com.jnetdata.msp.message.msgConfig.model.Msg;
import com.jnetdata.msp.message.msgConfig.service.SetMsgService;
import com.jnetdata.msp.message.msgConfig.vo.MsgVo;
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
 * Created by Admin on 2019/3/11.
 */

@Controller
@RequestMapping("/message/config")
@ApiModel(value = "SetMsgController", description = "消息源配置")
public class SetMsgController extends BaseController<Long,Msg> {

    @Autowired
    private SetMsgService service;

    /**
     * 添加消息源信息
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "添加消息源信息", notes="添加消息源信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Msg entity) {
        return doAdd(getService(), entity);
    }

    @Autowired
    private MsgService msgService;

    /**
     * 删除指定id对应的消息源信息
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "删除", notes="删除指定id对应的消息源信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        msgService.deleteById(service.getById(id).getId());

        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除消息源信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {

        return doDeleteBatchIds(getService(),ids);
    }

    /**
     * 修改指定id对应的消息源信息(只需要填消息源中的id)
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "修改", notes="修改指定id对应的消息源信息(只需要填消息源中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                 @RequestBody Msg entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 查看指定消息源id对应的消息源信息
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据id查询", notes="查看指定消息源id对应的消息源信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Msg> getById(@ApiParam("消息源信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Msg>> list(@RequestBody MsgVo vo) {
        JsonResult<Pager<Msg>> page = doList(getService(), vo.getPager(), vo.getEntity());

        // TODO
/*
        List<Long> ids = page.getObj().getRecords().stream().map(Msg::getGroupId).collect(Collectors.toList());
        List<Groupinfo> groupinfos = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("id",ids));
        Map<Long,String> map = groupinfos.stream().collect(Collectors.toMap(Groupinfo::getId,Groupinfo::getName));
        page.getObj().getRecords().forEach(res->{
            res.setGroupName(map.get(res.getGroupId()));
        });
*/

        return page;
    }


    /**
     * 通过excel批量上传数据
     * @param file
     * @return
     * @throws Exception
     * @author hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }

    /**
     * 根据查询条件下载excel数据
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

    }

    private SetMsgService getService() {
        return service;
    }




}
