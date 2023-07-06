package com.jnetdata.msp.metadata.encoding.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.metadata.encoding.model.Encodingmodel;
import com.jnetdata.msp.metadata.encoding.service.EncodingService;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import com.jnetdata.msp.metadata.subscribe.service.SubscribeService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



@Controller
@RequestMapping("/metadata/encoding")
public class EncodingController extends BaseController {

    @Resource
    private EncodingService encodingService;

    @ApiOperation(value = "查询", notes="")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Encodingmodel>> listt(@RequestBody BaseVo<Encodingmodel> vo) {
        JsonResult jsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return jsonResult;
    }
    @ApiOperation(value = "新建", notes="新建")
    @PostMapping(value = "/addlist")
    @ResponseBody
    public JsonResult<EntityId<Long>> selectlist(@RequestBody Encodingmodel vo) {
     Boolean boold=encodingService.selectlist(vo);
     return renderSuccess();
       /* JsonResult jsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return jsonResult;*/
    }
    @ApiOperation(value = "修改操作", notes="修改操作")
    @PostMapping(value = "/uplist")
    @ResponseBody
    public JsonResult<EntityId<Long>> uplist(@RequestBody Encodingmodel vo) {
        Boolean boold=encodingService.uplist(vo);
        return renderSuccess();
    }
    @ApiOperation(value = "删除操作", notes="删除操作")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult<EntityId<Long>> delectList(@RequestBody Long[] ids, HttpServletRequest request){
        List<Encodingmodel> grpusers = encodingService.list(new PropertyWrapper<>(Encodingmodel.class).in("Id" , Arrays.asList(ids)));
        List<Long> groupIds = grpusers.stream().map(m -> m.getId()).distinct().collect(Collectors.toList());
      /*  for (Long groupId : groupIds) {
            if(!SessionUser.getSubject().isPermitted("group:user:"+groupId)){
                return renderError("没有删除组织下用户权限");
            }
        }*/
        encodingService.delete(new PropertyWrapper<>(Encodingmodel.class).in("Id", Arrays.asList(ids)));
       /* // TODO 删除权限
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            User user1 = new User();
            user1.setId(id);
            user1.setStatus(-1);
            users.add(user1);
        }
        boolean b = getService().updateBatchById(users);
        jLogService.addLogs(request,"删除用户","删除",ids,null,"用户",null);*/
        return renderSuccess();

    }


    private EncodingService getService() {
        return encodingService;

    }
}
