package com.jnetdata.msp.member.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.vo.AddUserVo;
import com.jnetdata.msp.member.user.vo.RegistVo;
import com.jnetdata.msp.member.user.vo.ThirdPartyLoginVo;
import com.jnetdata.msp.member.user.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/25.
 */
public interface UserService extends BaseService<User> {


    boolean updateStatus(String ids,int type);

    /*
    * 根据用户名查询
    * @param name 用户名称
    * @author hongshou
     * @date 2020/5/26
    * */
    User getByName(String name);

    /**
     * 查询手机号
     * @param phone
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    User getByCellPhone(String phone);

    /*
    * 添加系统用户
    * @param entity
    * */
    boolean addUser(User user);

    User addUser1(User user);

    /**
     * 获取用户日志
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    Map<String,Object> getLoginUser();

    String ediPassWD(String nowPassWd,String newPassWD);

    /**
     * 查询用户
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    User getUser();

    /**
     * 上传头像
     * @param file
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    String importHead(MultipartFile file);

    String updatePassword(String nowPassWD,String newPassWD,User user);
/*
* 查询本周新增系统用户数
* @author hongshou
* @date 2023/5/26
* */
    Map<String,Object> addThisWeek();
    /*
    * 查询本周新增互联网用户数
    * @author hongshou
    * @date 2020/5/26
    * */

    Map<String,Object> addItThisWeek();

    /*
    * 本月新增系统用户
    * @author hongshou
    * @date 2020/5/26
    * */
    Map<String,Object> addThisMonth();

    /*
    * 本月新增it用户
    * @author hongshou
    * @date 2020/5/26
    * */
    Map<String,Object> addItThisMonth();

    /*
    * 本年新增系统用户
    * @author hongshou
    * @date 2020/5/26
    * */
    Map<String,Object> addThisYear();

    /*
    *@author hongshou
    * @date 2020/5/26
    * 本年新增it用户
    * */
    Map<String,Object> addItThisYear();

    /*
    * @author hongshou
    * @date 2020/5/26
    * 查询上级
    * */
    List<User> getParentIds(Long userId);

    Pager<User> userList(UserVo vo);

    /**
     * 根据查询条件下载excel数据
     * @param response
     * @param itUsers
     * @author hongshou
     * @date 2020/5/26
     */
    void exportItUser(HttpServletResponse response, List<User> itUsers);

    String importItUser(List<List<String>> lists);

    String importItUserNew(List<User> userList);

    /**
     * 添加用户
     * @param users
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    String addUsers(List<User> users);

    /**
     * 获取邮箱
     * @param principal
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    User getByEmail(String principal);

    User getByOpenId(String type, String openId);
    /**
     * 根据注册id获取用户
     * @param vo
     * @author 王树斌
     * @date 2020/5/26
     * @return
     */
    User registerUsingOpenId(ThirdPartyLoginVo vo);

    Integer getYQM(Long id);

    /**
     * 用户分页
     * @param page
     * @param user
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    Pager<User> userPage(Pager<User> page,User user);

    Map<Long, String> listGrpUser(ArrayList<Long> longs);
    User selectUserone(Long id);

    /**
     * 获取登录用户
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    Map<String, Object> getLoginUser(HttpServletRequest request);

    Pager<User> list1(UserVo vo);

    void updateUser(Long id, User entity);

//    List<User> selectget(Long id, String sex, String office, int age, String number);
}
