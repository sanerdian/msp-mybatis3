package com.jnetdata.msp.member.businessuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.member.businessuser.model.BusinessUser;
import com.jnetdata.msp.member.businessuser.mapper.BusinessUserMapper;
import com.jnetdata.msp.member.businessuser.service.BusinessUserService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.group.model.GroupModel;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Service
public class BusinessUserServiceImpl extends BaseServiceImpl<BusinessUserMapper, BusinessUser> implements BusinessUserService {
    private String andOr = "and";

    @Autowired
    private UserService userService;

    @Override
    public PropertyWrapper<BusinessUser> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<BusinessUser> pw = createWrapperUtil(condition)
                        .eq("id")
                                .eq("docchannelid")
                                .eq("docstatus")
                                .eq("singletempkate")
                                .eq("siteid")
                                .eq("docvalid")
                                .eq("docpubtime")
                                                    .eq("opertime")
                                                    .eq("docreltime")
                                                                        .eq("classinfoid")
                                                    .eq("companyid")
                                .eq("websiteid")
                                .eq("columnid")
                                                    .eq("seqencing")
                                                                        .eq("userid")
                                .eq("groupid")
                    .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"status");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        return pw;
    }

    @Override
    public boolean addUserGroup(String userIds, String organId) {
        String[] str = userIds.split(",");
        List<BusinessUser> list = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            BusinessUser businessUser = new BusinessUser();
            businessUser.setUserid(Long.valueOf(str[i]));
            businessUser.setGroupid(Long.valueOf(organId));
            list.add(businessUser);
        }
        return super.insertBatch(list);
    }

    @Override
    public Pager<User> getUser(BaseVo<BusinessUser> vo) {
        List<BusinessUser> list = super.list(new PropertyWrapper<>(BusinessUser.class).eq("groupid",vo.getEntity().getGroupid()));
        Pager<User> userList = new Pager<>();
        if(list.size()>0){
            List userIds = list.stream().map(BusinessUser -> String.valueOf(BusinessUser.getUserid())).collect(Collectors.toList());
            userList =userService.list(vo.getPager().toPager(),new PropertyWrapper<>(User.class).in("USERID",userIds).gt("status",-1));
        }
        return userList;
    }

    /**
     * 机构，用户数据关联
     * @param pager
     * @param userList
     * @return
     */
    private void setModelData(Pager<BusinessUser> pager, List<User> userList){

        Map<Long,User> map1 = new HashMap<>();
        userList.forEach(res->{
            map1.put(res.getId(),res);
        });

        List<GroupModel> list = new ArrayList<>();
        pager.getRecords().forEach(res->{
            res.setUser(map1.get(res.getUserid()));
        });
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<BusinessUser>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" " )){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
