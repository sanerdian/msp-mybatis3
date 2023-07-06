package com.jnetdata.msp.metadata.tstable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tciteadd.model.TciteaddModel;
import com.jnetdata.msp.metadata.tpushtocategory.model.TpushtocategoryModel;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TpushtocategoryService;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;

@Controller
@ApiOperation(value = "添加推送")
@RequestMapping("/addpushcotegory/push")
public class TpushtocategoryController  extends BaseController<Long, TpushtocategoryModel> {

    @Resource
    private TpushtocategoryService tpushtocategoryService;

    @Resource
    private TableinfoMapper tableinfoMapper;

    @ApiOperation("添加操作")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult add(@RequestBody BaseVo<TpushtocategoryModel> vo) {
        TpushtocategoryModel entity = vo.getEntity();
        if(entity==null){
            return renderError();
        }else {
            Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
            entity.setTablename(id.getTablename());
            JsonResult<Pager<TpushtocategoryModel>> pagerJsonResult = doList(getService(), vo.getPager(), entity);
            if(pagerJsonResult.getObj()!=null){
                return doAdd(getService(), entity);
            }
            return renderError();
        }
    }
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<TpushtocategoryModel>> list(@RequestBody BaseVo<TpushtocategoryModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "删除操作", notes="删除操作")
    @GetMapping(value = "/dele/{id}")
    @ResponseBody
    public JsonResult<EntityId<Long>> dele(@PathVariable("id") Long id){

        boolean b = getService().deleteById(id);
        return b?renderSuccess("删除成功") : renderError("删除失败");

    }
    private TpushtocategoryService getService() {
        return this.tpushtocategoryService;
    }
}
