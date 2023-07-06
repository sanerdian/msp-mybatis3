package com.jnetdata.msp.metadata.Push.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.Class.model.Class;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import com.jnetdata.msp.metadata.Push.mapper.PushSelectMapper;
import com.jnetdata.msp.metadata.Push.service.PushService;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.pic.model.MetadataPic;
import com.jnetdata.msp.metadata.push.model.PushModel;
import com.jnetdata.msp.metadata.subscribe.service.SubscribeService;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tnetwork.entity.UserVeo;
import com.jnetdata.msp.metadata.tnetwork.entity.UserVoEntity;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/push/pic")
public class PushController extends BaseController<Long ,PushModel> {

    @Resource
    private PushService pushService;

    @Resource
    private ClassService classService;

    @Resource
    private TableinfoMapper tableinfoMapper;

    @ApiOperation(value = "引入操作", notes="引入操作")
    @PostMapping(value = "addlist")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody  BaseVo<PushModel> vo) {
        PushModel entity = vo.getEntity();
       PushModel pushmode= getService().selectpush(entity);
       if(pushmode!=null){
           return renderError();
       }
        Tableinfo id = tableinfoMapper.selectOne(new QueryWrapper<Tableinfo>().eq("tableinfoid", entity.getTableid()));
        List<Class> classList= classService.selectAdd(entity.getClassid());
        classList.forEach(s->{
            PushModel pushModel = new PushModel();
            pushModel. setTableid(entity.getTableid());
            pushModel.setTablename(id.getTablename());
            pushModel.setPushid(entity.getId());
            pushModel.setClassname(s.getClassName());
            pushModel.setClassid(s.getId());
            pushModel.setName(entity.getName());
            pushModel.setStatus(1);
            pushModel.setCruser(entity.getCruser());
            pushModel.setCrtime(entity.getCrtime());
            pushService.insert(pushModel);
        });
        return renderSuccess("引用成功");
    }

    @ApiOperation(value = "引入操作", notes="引入操作")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<PushModel>> selectlist(@RequestBody BaseVo<PushModel> vo){
        JsonResult<Pager<PushModel>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return pagerJsonResult;
    }

    @ApiOperation(value = "引入操作删除操作", notes="引入操作删除操作")
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
    @Resource
    private PushSelectMapper pushSelectMapper;

    @ApiOperation(value = "用户根据id查找推送到分类法的数据")
    @PostMapping(value = "/selectclass")
    @ResponseBody
    public JsonResult<List<Map>> selectlist(@RequestBody  UserVoEntity entity){
        String selectonetable = pushSelectMapper.selectonetable(entity.getClassid());
        List<Map> selectall = pushSelectMapper.selectalll(selectonetable, entity.getId(),entity.getClassid());
        /*JsonResult<Pager<PushModel>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return pagerJsonResult;*/
        return renderSuccess(selectall);
    }


    private PushService getService() {
        return pushService;
    }


}
