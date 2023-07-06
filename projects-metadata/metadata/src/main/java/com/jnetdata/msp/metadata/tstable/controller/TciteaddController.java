package com.jnetdata.msp.metadata.tstable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.Push.service.PushService;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tciteadd.model.TciteaddModel;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TciteaddService;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/tciteadd/push")
public class TciteaddController  extends BaseController<Long, TciteaddModel> {
   /* @Resource
    private TciteaddService tciteaddService;

    @Resource
    private TableinfoMapper tableinfoMapper;*/

    /*@Resource
    private PushService pushService;*/

   /* @ApiOperation("添加操作")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult add(@RequestBody BaseVo<TciteaddModel> vo) {
        //  String sql = "select * from"+entity.getTablename()+"where id="+entity.getTableid();
        TciteaddModel entity = vo.getEntity();
        Long pushid = entity.getPushid();
        Long nameid = entity.getNameid();
       Long idname= pushService.selectlist(pushid,nameid);


        Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
        Tableinfo ids = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", idname));
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
    private TciteaddService getService() {
        return this.tciteaddService;
    }*/
}
