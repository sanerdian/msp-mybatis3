package com.jnetdata.msp.metadata.addreference.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import com.jnetdata.msp.metadata.addreference.service.AddReferenceService;
import com.jnetdata.msp.metadata.pushg.model.PushgModel;
import com.jnetdata.msp.metadata.pushg.service.PushgService;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/addreference/pushg")
@ApiModel(value = "添加元数据", description = "添加元数据")
@Slf4j
public class AddReferenceController  extends BaseController<Long ,AddReferenceModel> {

    @Resource
    private AddReferenceService addReferenceService;
    @Resource
    private TableinfoMapper tableinfoMapper;
    @PostMapping("/add")
    @ResponseBody
    public JsonResult add(@RequestBody BaseVo<AddReferenceModel> vo) {
        AddReferenceModel entity = vo.getEntity();
        //通过表的id去查找表名
        Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
     /*   entity.setTablename(id.getTablename());
        Tableinfo id= tableinfoService.selectadd(entity);*/
       /* SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
        entity.setCrtime(new Date());
        entity.setTablename(id.getTablename());
        boolean entityIdJsonResult= getService().addlist(entity);
        if(entityIdJsonResult==true){
            return JsonResult.success();
        }else {
            return JsonResult.fail("失败");
        }
    }
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<AddReferenceModel>> list(@RequestBody BaseVo<AddReferenceModel> vo) {
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
    private AddReferenceService getService() {
        return this.addReferenceService;
    }
}
