package com.jnetdata.msp.metadata.group.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import com.jnetdata.msp.metadata.vo.BaseVo2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/group")
@ApiModel(value = "元数据分组管理(MetadataGroupController)", description = "元数据分组管理")
public class MetadataGroupController extends BaseController<Long, MetadataGroup> {

    @Autowired
    private MetadataGroupService metadataGroupService;
    @Autowired
    private ContentLogClient contentLogService;

    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "根据实体属性获取分组列表")
    public JsonResult<Pager<MetadataGroup>> getList(@RequestBody BaseVo2<MetadataGroup> vo) {
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @PostMapping(value = "/all")
    @ResponseBody
    @ApiOperation(value = "获取所有分组列表")
    public JsonResult<List<MetadataGroup>> all() {
        List<MetadataGroup> list = getService().list(new PropertyWrapper<>(MetadataGroup.class));
        Map<Long, MetadataGroup> idmap = list.stream().collect(Collectors.toMap(m -> m.getId(), m -> m));
        List<MetadataGroup> collect = list.stream().filter(m -> m.getParentid() != 0).collect(Collectors.toList());
        collect.forEach(item -> {
            String g = idmap.get(item.getParentid()).getName();
            g = g+"."+item.getName();
            item.setName(g);
        });
        return renderSuccess(collect);
    }

    @ApiOperation(value = "添加分组")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody MetadataGroup entity) {
        entity.setCreateTime(new Date());
        val user = SessionUser.getCurrentUser();
        entity.setCrUser(user.getName());
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("添加分组");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("分组管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除分组")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("内容操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除分组")
    @PostMapping(value = "/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody Long[] ids) {
        List<Long> list = Arrays.stream(ids).collect(Collectors.toList());
        List<String> strings = new ArrayList<String>();
        list.forEach(e->{
            strings.add(String.valueOf(e));
        });
        String sid = String.join(",",strings);
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除分组");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("分组管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(),sid);
    }

    @ApiOperation(value = "修改", notes="修改分组")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("分组id") @PathVariable("id") Long id,@RequestBody MetadataGroup entity) {
        List<MetadataGroup> list = getService().list(new PropertyWrapper<>(MetadataGroup.class).eq("name" , entity.getName()).notIn("id",String.valueOf(entity.getId())));
        JsonResult<Void> result = null;
        if(list.size() > 0){
            result = renderError("分组已存在！");
        }else{
            result = doUpdateById(getService(), id, entity);
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改分组");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("分组管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return result;
    }

    @ApiOperation(value = "根据id查询", notes="查看分组")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<MetadataGroup> getById(@ApiParam("分组id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    private MetadataGroupService getService() {
        return metadataGroupService;
    }
}
