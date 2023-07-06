package com.jnetdata.msp.metadata.tstable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TreferenceService;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/trtable/push")
public class TreferenceController extends BaseController<Long, TreferenceModel> {
    @Resource
    private TreferenceService treferenceService;

    @Resource
    private TableinfoMapper tableinfoMapper;

    @ApiOperation("添加操作")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult add(@RequestBody BaseVo<TreferenceModel> vo) {
        TreferenceModel entity = vo.getEntity();
        Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
        Tableinfo ids = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getPushtableid()));
        if (!id.getTablename().equals(ids.getTablename())) {
            return JsonResult.renderError("请找相同的表格");
        }
        entity.setCrtime(new Date());
        entity.setTablename(id.getTablename());
        boolean entityIdJsonResult = getService().addlist(entity);
        if(entityIdJsonResult==true){
            return JsonResult.success();
        }else {
            return JsonResult.renderError("失败");
        }
    }
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<TreferenceModel>> list(@RequestBody BaseVo<TreferenceModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "删除操作", notes="删除操作")
    @GetMapping(value = "/dele/{id}")
    @ResponseBody
    public JsonResult<EntityId<Long>> dele(@PathVariable("id") Long id){
        boolean b = getService().deleteById(id);
        if(b==true){
            return renderSuccess("成功");
        }else {
            return renderError("失败");
        }
    }

    private TreferenceService getService() {
        return this.treferenceService;
    }

}
