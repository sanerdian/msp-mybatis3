package com.jnetdata.msp.member.groupbusiness.controller;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.vo.ExportVo;
import javax.servlet.http.HttpServletResponse;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;

import java.util.List;

import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.groupbusiness.service.GroupBusinessService;
import com.jnetdata.msp.member.groupbusiness.model.GroupBusiness;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Controller
@RequestMapping("/member/groupbusiness")
@ApiModel(value = "组织企业管理(GroupController)", description = "组织企业管理")
public class GroupBusinessController extends BaseController<Long,GroupBusiness> {

    final private GroupBusinessService groupBusinessService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public GroupBusinessController(GroupBusinessService groupBusinessService) {
        this.groupBusinessService = groupBusinessService;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加实体对象")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody GroupBusiness entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "指定id删除", notes= "根据指定id删除实体对象")
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
    @ApiOperation(value = "批量删除", notes= "指定多个id批量删除")
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") @ApiParam("多个id用逗号隔开") String ids) {
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody GroupBusiness entity) {
        return doUpdateById(getService(), id, entity);
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
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody GroupBusiness entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<GroupBusiness> getById(@PathVariable("id") Long id) {
        GroupBusiness entity = getService().getById(id);
        return renderSuccess(entity);
    }
    

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<GroupBusiness>> list(@RequestBody BaseVo<GroupBusiness> vo) {
            return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "导出组织企业列表")
    @PostMapping(value = "/export")
    public void export(GroupBusiness entity, ExportVo vo, HttpServletResponse response) {
        List<GroupBusiness> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    private GroupBusinessService getService() {
        return this.groupBusinessService;
    }

}

