package com.jnetdata.msp.metadata.moduleinfo.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.moduleinfo.model.ModuleView;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleViewService;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/moduleview")
@ApiModel(value = "模块视图管理(ModuleViewController)" , description = "模块视图管理")
public class ModuleViewController extends BaseController<Long, ModuleView> {

    @Autowired
    private ViewTableService viewTableService;
    @Autowired
    private ModuleViewService moduleViewService;

    @PostMapping(value = "/addView/{id}")
    @ResponseBody
    @ApiOperation(value = "引入所选元数据视图")
    public JsonResult<Void> introduceView(@ApiParam("模块id") @PathVariable("id") Long id, @RequestBody Long[] ids){
        List<ModuleView> list = new ArrayList<>();
        for(Long vid : ids){
            ViewTable vt = viewTableService.getById(vid);
            List<ModuleView> list1 = moduleViewService.list(new PropertyWrapper<>(ModuleView.class).eq("moduleinfoid", id).eq("viewid", vid));
            if(list1.size() > 0){
                return renderError("存在已引入的视图！");
            }else {
                ModuleView mv = new ModuleView();
                mv.setModuleinfoid(id);
                mv.setViewid(vt.getId());
                mv.setModulename(vt.getViewname());
                mv.setModuleviewname(vt.getTableviewname());
                mv.setCrtime(vt.getCrtime());
                doAdd(moduleViewService, mv);
            }
        }
        return renderSuccess();
    }

    @PostMapping(value = "/removeView")
    @ResponseBody
    @ApiOperation(value = "移除所选元数据视图")
    public JsonResult removeView(@RequestBody Long[] ids){
        List<Long> list = Arrays.stream(ids).collect(Collectors.toList());
        List<String> strings = new ArrayList<String>();
        list.forEach(e->{
            strings.add(String.valueOf(e));
        });
        doDeleteBatchIds(moduleViewService, strings);
        return renderSuccess();
    }

    public JsonResult removeView2(Long id){
        doDeleteById(moduleViewService,id);
        return renderSuccess();
    }

    @PostMapping(value = "/listModuleView")
    @ResponseBody
    @ApiOperation(value = "查询对应模块元数据视图")
    public JsonResult<Pager<ModuleView>> getModuleView(@RequestBody BaseVo<ModuleView> vo){
        JsonResult<Pager<ModuleView>> tlist = doList(moduleViewService,vo.getPager(),vo.getEntity());
        /*List<ModuleView> table = tlist.getObj().getRecords();
        table.forEach(res->{
            if(res.getModuleinfoid() != vo.getEntity().getModuleinfoid()){
                table.remove(res);
            }
        });*/
        return tlist;
    }
    @ApiOperation(value = "查询相应view对应信息", notes ="查询相应view对应信息")
    @PostMapping(value = "/searchList")
    @ResponseBody
    public JsonResult<List<ModuleView>> getSearchList(@RequestBody String viewname) {
        List<ModuleView> list = moduleViewService.list(new PropertyWrapper<>(ModuleView.class).eq("modulename",viewname));
        return renderSuccess(list);
    }

}
