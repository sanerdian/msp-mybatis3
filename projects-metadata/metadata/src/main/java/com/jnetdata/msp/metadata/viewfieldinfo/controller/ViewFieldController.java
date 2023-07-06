package com.jnetdata.msp.metadata.viewfieldinfo.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.dict.model.Dict;
import com.jnetdata.msp.metadata.dict.service.DictService;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewfieldinfo.service.ViewFieldService;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/viewfield")
@ApiModel(value = "视图字段管理(ViewFieldController)", description = "视图字段管理")
public class ViewFieldController extends BaseController<Long, ViewField> {

    @Autowired
    private ViewFieldService viewFieldService;

    @Autowired
    private DictService dictService;

    @Autowired
    private MetadataGroupService metadataGroupService;

    @Autowired
    private ContentLogClient contentLogService;

    @Autowired
    private ViewTableService viewTableService;

    @ApiOperation("视图字段列表")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ViewField>> getList(@RequestBody BaseVo<ViewField> vo){
        List<MetadataGroup> list = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).ne("parentid", 0));
        for(int i = 0 ; i < list.size() ; i++){
            String g = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).eq("id", list.get(i).getParentid())).get(0).getName();
            g = g+"."+list.get(i).getName();
            list.get(i).setName(g);
        }
        Map<Integer, String> db_type = dictService.list("db_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo,Dict::getName));
        Map<Long, String> group_name = list.stream().collect(Collectors.toMap(MetadataGroup::getId, MetadataGroup::getName));
        JsonResult<Pager<ViewField>> result = doList(getService() , vo.getPager() , vo.getEntity());
        result.getObj().getRecords().forEach(res->{
            res.setDbTypeStr(db_type.get(res.getDbtype()));
            res.setFieldTypeStr(field_type.get(res.getFieldtype()));
            res.setGroupname(group_name.get(res.getGroupid()));
        });
        return result;
    }

    @ApiOperation(value = "根据Id查询")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ViewField> getById(@PathVariable(value = "id")Long id){
        return doGetById( getService(),id);
    }

    @ApiOperation(value = "添加")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody ViewField entity){
        val user = SessionUser.getCurrentUser();
        entity.setCruser(user.getName());
        entity.setCrnumber(user.getId());
        entity.setCrtime(new Date());
        return  doAdd(getService(),entity);
    }

    @ApiOperation(value = "根据ID修改")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable(value = "id") Long id,@RequestBody ViewField viewField){
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改元数据视图");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据视图管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doUpdateById(getService(),id,viewField);
    }

    @ApiOperation(value = "更新java应用代码生成信息")
    @PostMapping(value = "/updateViewState")
    @ResponseBody
    public JsonResult<Void> updateViewState(@RequestBody Long id){
        ViewTable vt = new ViewTable();
        vt.setId(id);
        vt.setGeneratetime(new Date());
        viewTableService.updateById(vt);
        Long[] ids = getService().getVFId(id);
        JsonResult<Void> result = null;
        for(Long vid : ids){
            ViewField viewField = new ViewField();
            viewField.setId(vid);
            viewField.setGeneratetime(new Date());
            viewField.setGeneratestate(1L);
            result = doUpdateById(getService(), vid, viewField);
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("生成java应用代码");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据视图管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return result;
    }

    @ApiOperation(value = "删除java应用代码生成信息")
    @PostMapping(value = "/deleteViewState")
    @ResponseBody
    public JsonResult<Void> deleteViewState(@RequestBody Long id){
        getService().updateViewGen(id);
        Long[] ids = getService().getVFId(id);
        JsonResult<Void> result = null;
        for(Long vid : ids){
            ViewField viewField = getService().getById(vid);
            viewField.setGeneratetime(null);
            viewField.setGeneratestate(0L);
            boolean b = getService().updateAllColumnById(viewField);
            if(b){
                result = renderSuccess("生成成功");
            }else {
                result = renderError("生成失败");
            }
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除java应用代码");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据视图管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return result;
    }

    @ApiOperation(value = "根据Id删除")
    @DeleteMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable(value = "id")Long id){
        deleteById(id);
        return renderSuccess();
    }


    @ApiOperation(value = "批量删除")
    @PostMapping(value = "/delByIds")
    @ResponseBody
    public JsonResult<Void> deleteByIds(@RequestBody Long[] ids){
        int count=0;
        for (Long id : ids) {
            JsonResult<Void> result = doDeleteById(getService(),id);
            if (result.isSuccess()){
                count++;
            }
        }
        if(count>0){
            return renderSuccess("删除成功");
        }else{
            return renderError("删除失败");
        }
    }

    private ViewFieldService getService(){
        return viewFieldService;
    }
}
