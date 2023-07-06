package com.jnetdata.msp.media.util;

import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
import com.jnetdata.msp.member.group.mapper.GroupMapper;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component
public class publicMethodUtil {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupMapper groupMapper;

    public static publicMethodUtil pm;

    @PostConstruct
    public void init() {
        pm = this;
    }

    /**
     * 获取当前用户所在的部门名称
     * @return
     */
    public static String getGroupNameForCurrentUser(){
        String name="";
        Long userid=getUserid();
        User user=pm.userMapper.selectById(userid);
        if(user!=null){
            Long groupId=user.getGroupId();
            Groupinfo groupinfo =pm.groupMapper.selectById(groupId);
            if(groupinfo!=null){
                name=groupinfo.getName();
            }
        }
        return name;
    }

    public static Long getUserid() {//todo 尝试获取用户id，失败则用默认值。测试时临时使用。正式功能时，非登陆用户应该抛异常，让其登陆
        try{
            var currentUser = SessionUser.getCurrentUser();
            return currentUser.getId();
        }catch (Exception e){
            return 0L;
        }
    }

    public static User getUser() {//todo获取用户
         try{
             IUser<Long> currentUser = SessionUser.getCurrentUser();
             return pm.userMapper.selectById(currentUser.getId());
         }catch (Exception e){
             return null;
         }
    }

    /**
     * 获取前台登陆的用户
     * @param request
     * @return
     */
    public static Lz_WorkerVo getLoginWorker(HttpServletRequest request){
        Object obj=request.getSession().getAttribute("userinfo");

        Lz_WorkerVo user=null;

        if(obj!=null){
            user= (Lz_WorkerVo)obj ;
        }
        return user;
    }

}
