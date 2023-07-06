package com.jnetdata.msp.metadata.subscribe.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;

import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import com.jnetdata.msp.metadata.subscribe.service.SubscribeService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/subscribe")
public class SubscribeController extends BaseController {
    @Autowired
    SubscribeService subscribeService;

    @ApiOperation(value = "查询", notes="")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Subscribeinfo>> list(@RequestBody BaseVo<Subscribeinfo> vo) {
        JsonResult jsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return jsonResult;
    }
    @Resource
    private ClassService classService;
    @ApiOperation(value = "根据条件查询关注的标签名", notes="")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<List<String>> listLabel(@RequestBody BaseVo<Subscribeinfo> vo) {
        Subscribeinfo entity = vo.getEntity();
        List<Subscribeinfo> classtreeid1 = getService().list(new PropertyWrapper<>(Subscribeinfo.class).eq("classtreeid", entity.getClasstreeid()).eq("userid",entity.getUserid()));
       if(classtreeid1==null||classtreeid1.size()==0){
           return renderError();
       }
        //根据所查到的标签id去查找相应的名称
        List<Long> collect = classtreeid1.stream().map(Subscribeinfo::getClassid).collect(Collectors.toList());
        List<String> strings = classService.selectLab(collect);
        return renderSuccess(strings);
    }

    /*@ApiOperation(value = "查询", notes="")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Subscribeinfo>> listt(@RequestBody BaseVo<Subscribeinfo> vo) {
        JsonResult jsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return jsonResult;
    }*/
    @ApiOperation(value = "添加删除操作", notes="")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult listt(@RequestBody List<Subscribeinfo> ids) {
        if(ids==null){
            return renderError();
        }
        Subscribeinfo subscribeinfo1 = ids.get(0);
        Long userid = subscribeinfo1.getUserid();
        //对没有查到的数据进行添加
        ids.forEach(s->{
            Subscribeinfo selectidone = getService().selectidone(s);
            if(selectidone==null){
                s.setSubscribetime(new Date());
                getService().insert(s);
            }
        });
        //根据用户查询表中所有的数据，然后进行比较对没有的用户进行删除
        List<Subscribeinfo> slist= getService().selectalluser(subscribeinfo1.getUserid(),subscribeinfo1.getClasstreeid());
        List<Long> collect = slist.stream().map(Subscribeinfo::getClassid).collect(Collectors.toList());
        List<Long> collect1 = ids.stream().map(Subscribeinfo::getClassid).collect(Collectors.toList());

        collect.removeAll(collect1);
        //删除
        if(collect!=null){
            collect.forEach(s->{
               int i= getService().selectclassid(s,userid,subscribeinfo1.getClasstreeid());
            });
        }
        return renderSuccess();
    }


    @ApiOperation(value = "添加订阅或取消订阅")
    @PostMapping("/{id}")
    @ResponseBody
    public JsonResult add(@PathVariable("id") Long classid) {
        val user = SessionUser.getCurrentUser();
        Long userid = user.getId();
        List<Subscribeinfo> list = getService().list(new PropertyWrapper(Subscribeinfo.class).eq("classid", classid).eq("userid", userid));
        if (list.size() > 0) {
            Subscribeinfo subscribe = list.get(0);
            if(subscribe.getType() ==1){
                subscribe.setUsername(SessionUser.getCurrentUser().getName());
                subscribe.setType(0);
                subscribe.setSubscribetime(new Date());
                subscribeService.updateById(subscribe);
                return  renderSuccess("取消订阅成功");
            }else {
                subscribe.setUsername(SessionUser.getCurrentUser().getName());
                subscribe.setType(1);
                subscribe.setSubscribetime(new Date());
                subscribeService.updateById(subscribe);
                return   renderSuccess("订阅成功");
            }
        }else{
            Subscribeinfo subscribeinfo = new Subscribeinfo();
            subscribeinfo.setClassid(classid);
            subscribeinfo.setUserid(userid);
            subscribeinfo.setSubscribetime(new Date());
            subscribeinfo.setUsername(SessionUser.getCurrentUser().getName());
            subscribeinfo.setType(1);
            subscribeService.insert(subscribeinfo);
            return renderSuccess("订阅成功");
        }

    }


    private SubscribeService getService() {
        return subscribeService;

    }

    @Autowired
    private UserService userService;
    @ApiOperation(value = "根据分类法树查询用户", notes="")
    @PostMapping(value = "/listuser")
    @ResponseBody
    public JsonResult<Pager<Subscribeinfo>> listUser(@RequestBody BaseVo<Subscribeinfo> vo) {
        Subscribeinfo entity = vo.getEntity();
        Long classid = entity.getClassid();
        Pager pager = vo.getPager().toPager();
        Page<Subscribeinfo> page1 = new Page<>(pager.getCurrent(),pager.getSize());
       Page<Subscribeinfo> selectPageall=getService().selectPageall(page1,classid);
        //根据用户的id进行查找用户
        selectPageall.getRecords().forEach(s->{
            if(s.getUserid()!=null) {
                Long userid = s.getUserid();
                User user = userService.selectUserone(userid);
                s.setUsername(user.getName());
            }
        });
        pager.setRecords(selectPageall.getRecords());
        pager.setPages((new Long(selectPageall.getPages())).intValue());
        pager.setTotal((new Long(selectPageall.getTotal())).intValue());
        return renderSuccess(pager);
    }

    @ApiOperation(value = "取消订阅操作", notes="取消订阅操作")
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


}
