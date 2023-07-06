package com.jnetdata.msp.metadata.tstable.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import com.jnetdata.msp.metadata.addreference.service.AddReferenceService;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/tstable/push")
public class TstableController extends BaseController<Long, TstableModel> {
    @Resource
    private TstableService tstableService;

    @Resource
    private TableinfoMapper tableinfoMapper;

    @ApiOperation("添加操作")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult add(@RequestBody BaseVo<TstableModel> vo) {
        //  String sql = "select * from"+entity.getTablename()+"where id="+entity.getTableid();
        TstableModel entity = vo.getEntity();
        Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
        Tableinfo ids = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getPushtableid()));
        if (!id.getTablename().equals(ids.getTablename())) {
            return JsonResult.fail("请找相同的表格");
        }
        entity.setCrtime(new Date());
        entity.setTablename(id.getTablename());
        boolean entityIdJsonResult = getService().addlist(entity);
        if(entityIdJsonResult==true){
            return JsonResult.success();
        }else {
            return JsonResult.fail("失败");
        }
    }
    private TstableService getService() {
        return this.tstableService;
    }
}

