package com.jnetdata.msp.member.user.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.model.UserRelation;
import com.jnetdata.msp.member.user.service.UserRelationService;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: YUEHAO
 * @Date: 2019/12/3
 */
@Controller
@RequestMapping("/member/userRelation")
@ApiModel(value = "UserRelationController",description = "虚线上报人")
public class UserRelationController extends BaseController<Long,UserRelation>{
    private static final String BASE_URL = "/member/userRelation";

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    /**
     * 根据提供的多个id添加虚线上报人
     * @param ids
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的多个id添加虚线上报人")
    private JsonResult add(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids){
        String[] s = ids.split(",");
        List<Long> longList = new ArrayList();
        for(int i = 0;i<s.length;i++){
            longList.add(Long.parseLong(s[i]));
        }
        User user = userService.getById(SessionUser.getCurrentUser().getId());
        for(int i =0;i<longList.size();i++){
            if(user.getId().equals(longList.get(i))){
                return renderError("不能将自己设为虚线上报人！");
            }
            if(user.getLeaderId()!=null&&user.getLeaderId().equals(longList.get(i))){
                return renderError("不能将直属领导设为虚线上报人！");
            }
            UserRelation userRelation = new UserRelation();
            userRelation.setUserId(user.getId());
            userRelation.setVirLeaderId(longList.get(i));
            userRelationService.insert(userRelation);
        }
        return renderSuccess();
    }

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation(value = "列表")
    private JsonResult<List<User>> list(){
        val user = SessionUser.getCurrentUser();
        List<UserRelation> userRelationList = userRelationService.list(new PropertyWrapper<>(UserRelation.class).eq("userId",user.getId()));
        List<User> userList = new ArrayList<>();
        for(UserRelation item :userRelationList){
            User user1 = userService.getById(item.getVirLeaderId());
            if(user1!=null){
                user1.setLeaderId(item.getId());
                userList.add(user1);
            }
        }
        return renderSuccess(userList);
    }

    /**
     * 删除指定id对应的虚拟上报人
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "删除", notes="删除指定id对应的虚拟上报人")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    private JsonResult<Void> deleteLeader(@PathVariable("id") Long id){

        return doDeleteById(userRelationService,id);
    }
}
