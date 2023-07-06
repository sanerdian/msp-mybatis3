package com.jnetdata.msp.member.user.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
//import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.FastDFSClient;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.member.user.util.ThirdPartyType;
import com.jnetdata.msp.member.user.vo.*;
import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.util.DownMetadataUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.var;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by TF on 2019/3/25.
 */

@Controller
@RequestMapping("/member/user")
@ApiModel(value = "用户管理(UserController)", description = "用户管理")
public class UserController extends BaseController<Long,User> {

    @Autowired
    private UserService service;

    @Autowired
    private GroupService groupService;

    @Resource
    private UserMapper userMapper;

    /*@Autowired
    private UserMapper userMapper;*/

    @Autowired
    private GrpUserService grpUserService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private ContentLogService contentLogService;


    @Autowired
    private RoleService roleService;

    @Autowired
    private GrpUserService getGrpUserService;

    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    private FastDFSClient fdfsClient;

    @Autowired
    private JLogService jLogService;

    @Resource
    private MemorySessionDAO sessionDAO;

    @ApiOperation(value = "添加", notes="按提供的实体属性添加用户")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody User entity) {
        boolean b = getService().addUser(entity);
        if(b) return renderSuccess();
        return renderError();
    }

    @ApiOperation(value = "添加", notes="用户信息")
    @PostMapping("/addUser")
    @ResponseBody
    public JsonResult<EntityId<Long>> addUser(@RequestBody AddUserVo vo) {
        User user = grpUserService.addUser(vo);
        return renderSuccess(user);
    }

    @ApiOperation(value = "添加系统用户", notes="用户信息")
    @PostMapping(value = "/addSysUser")
    @ResponseBody
    public JsonResult<EntityId<Long>> addSysUser(@RequestBody User entity) {
        entity.setSign(0);
        boolean b = getService().addUser(entity);
        if(b) return renderSuccess();
        return renderError();
    }

    @ApiOperation(value = "添加个人用户(互联网用户)", notes="用户信息")
    @PostMapping(value = "/addItUser")
    @ResponseBody
    public JsonResult<EntityId<Long>> addItUser(@RequestBody User entity) {
        entity.setSign(3);
        boolean b = getService().addUser(entity);
        if(b) return renderSuccess();
        return renderError();
    }

    @ApiOperation(value = "删除", notes="删除id对应用户")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        List<GrpUser> groupByUser = grpUserService.getGroupByUser(id);
        for (GrpUser grpUser : groupByUser) {
            if(!SessionUser.getSubject().isPermitted("group:user:"+grpUser.getGroupId())){
                return renderError("没有删除组织下用户权限");
            }
        }
        grpUserService.delete(new PropertyWrapper<>(GrpUser.class).eq("userId", id));
        User user = new User();
        user.setId(id);
        user.setStatus(-1);//假删
        getService().updateById(user);//假删
        return renderSuccess();
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除用户")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody Long[] ids,HttpServletRequest request) {
        List<GrpUser> grpusers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in("userId" , Arrays.asList(ids)));
        List<Long> groupIds = grpusers.stream().map(m -> m.getGroupId()).distinct().collect(Collectors.toList());
        for (Long groupId : groupIds) {
            if(!SessionUser.getSubject().isPermitted("group:user:"+groupId)){
                return renderError("没有删除组织下用户权限");
            }
        }
        grpUserService.delete(new PropertyWrapper<>(GrpUser.class).in("userId", Arrays.asList(ids)));
        // TODO 删除权限
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            User user1 = new User();
            user1.setId(id);
            user1.setStatus(-1);  //假删
            users.add(user1); //假删
        }
        boolean b = getService().updateBatchById(users);
        jLogService.addLogs(request,"删除用户","删除",ids,null,"用户",null);
        return renderSuccess();
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的用户信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id, @RequestBody User entity) {
        getService().updateUser(id, entity);
        return renderSuccess();
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的用户信息")
    @PostMapping(value = "/updateUser/{id}")
    @ResponseBody
    public JsonResult<Void> updateUser(@ApiParam("用户id") @PathVariable("id") Long id, @RequestBody AddUserVo vo) {
        grpUserService.updateUser(id, vo);
        return renderSuccess();
    }

    /**
     * 查看指定用户id对应的用户信息
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "查看", notes="查看指定用户id对应的用户信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<User> getById(@ApiParam("用户id") @PathVariable("id") Long id) {
        User user = service.getById(id);
        List<RoleUser> roleUser = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("userId",id));
        List<String> rolename = new ArrayList();
        List<String> grpname = new ArrayList();
        List<String> cpyname = new ArrayList();
        String rolestr = null;
        String grpstr =null;
        String cpystr = null;
        if(roleUser!=null){
            for(int i = 0;i<roleUser.size();i++){
                Role role = roleService.getById(roleUser.get(i).getRoleId());
                if(role!=null){
                    rolename.add(role.getName());
                }
            }
            rolestr = String.join(",",rolename);
        }
        List<GrpUser> grpUsers = getGrpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId",id));
        if(grpUsers!=null){
            for(int i = 0;i<grpUsers.size();i++){
                Groupinfo groupinfo = groupService.getById(grpUsers.get(i).getGroupId());
                if(groupinfo!=null){
                    grpname.add(groupinfo.getName());
                    Companyinfo companyinfo = companyInfoService.get(new PropertyWrapper<>(Companyinfo.class).eq("id",groupinfo.getCompanyId()));
                    if(companyinfo!=null){
                        cpyname.add(companyinfo.getName());
                    }
                }
            }
            cpystr = String.join(",",cpyname);
            grpstr = String.join(",",grpname);
        }
        if(!StringUtils.isEmpty(rolestr)){
            user.setRoleName(rolestr);
        }
        if(!StringUtils.isEmpty(grpstr)){
            user.setGroupName(grpstr);
        }
        if(!StringUtils.isEmpty(cpystr)){
            user.setCompanyName(cpystr);
        }
        return renderSuccess(user);
    }

    /**
     *获取全部组的id
     * @param ids
     * @author 王树彬
     * @date 2022/7/25
     * @return
     */
    private List<Long> getAllGroupIds(List<Long> ids){
        List<Groupinfo> gs = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("parentId", ids));
        if(!gs.isEmpty()){
            List<Long> pids = gs.stream().map(m -> m.getId()).collect(Collectors.toList());
            getAllGroupIds(pids);
            ids.addAll(pids);
        }
        return ids;
    }

    /**
     * 根据vo指定条件进行查询
     * @param vo
     * @author 王树彬
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<User>> list(@RequestBody UserVo vo) {
        List<Long> ids = new ArrayList<>();

//        boolean isAdmin = SessionUser.getCurrentUser().getId().equals(1L);
        boolean isAdmin = true;

        List<GrpUser> groupByUser = grpUserService.getGroupByUser(SessionUser.getCurrentUser().getId());
        List<Long> myGroupIds = groupByUser.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
        if(!myGroupIds.isEmpty()) {
            getAllGroupIds(myGroupIds);
        }
        //null是用户页面列表  !=null是消息用户列表
        if(vo.getEntity().getIds() == null){
            if(vo.getEntity().getGroupId() != null){
                if(isAdmin || myGroupIds.contains(vo.getEntity().getGroupId())){
                    List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("groupId", vo.getEntity().getGroupId()));
                    ids.addAll(grpUsers.stream().map(GrpUser::getUserId).collect(Collectors.toList()));
                }
            }else if(!isAdmin && !myGroupIds.isEmpty()){
                List<GrpUser> grpusers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in("groupId", myGroupIds));
                List<Long> userids = grpusers.stream().map(m -> m.getUserId()).collect(Collectors.toList());
                ids.addAll(userids);
            }

            if(!StringUtils.isEmpty(vo.getEntity().getRoleId())){
                if(isAdmin){
                    List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("roleId", Long.valueOf(vo.getEntity().getRoleId())).in(!ids.isEmpty(),"userId",ids));
                    ids = (roleUsers.stream().map(RoleUser::getUserId).collect(Collectors.toList()));
                }else if(!ids.isEmpty()){
                    List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("roleId", Long.valueOf(vo.getEntity().getRoleId())).in("userId",ids));
                    ids = (roleUsers.stream().map(RoleUser::getUserId).collect(Collectors.toList()));
                }
            }
        }

        if(!isAdmin || !ids.isEmpty() ){
            vo.getEntity().setIds(ids);
        }
        User entity = vo.getEntity();
        if(entity.getIds()==null&&entity.getGroupId()!=null){
            Pager<User> userPager = new Pager<>();
            return renderSuccess(userPager);
        }
        Pager<User> page = doList(getService(),vo.getPager(),vo.getEntity()).getObj();

        List<User> users = page.getRecords();
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).in(userIds.size()>0,"userId", userIds));
        Map<Long, List<RoleUser>> userRoleMap = roleUsers.stream().collect(Collectors.groupingBy(RoleUser::getUserId));
        List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.list(new PropertyWrapper<>(Role.class).in(roleIds.size()>0,"id", roleIds));
        Map<Long, String> roleNameMap = roles.stream().collect(Collectors.toMap(Role::getId,Role::getName));
        for (User item : users) {
            List<RoleUser> roleUsers1 = userRoleMap.get(item.getId());
            if(roleUsers1 !=null && roleUsers1.size()>0){
                List<String> names = new ArrayList<>();
                for (RoleUser iitem : roleUsers1) {
                    if(roleNameMap.get(iitem.getRoleId())!=null) {
                        names.add(roleNameMap.get(iitem.getRoleId()));
                    }
                }
                String[] arr = new String[names.size()];
                names.toArray(arr);
                item.setRoleName(String.join(",",arr));
            }
        }
        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in(userIds.size()>0,"userId", userIds));
        if(grpUsers.size()>0){
            Map<Long, List<GrpUser>> userGrpMap = grpUsers.stream().collect(Collectors.groupingBy(GrpUser::getUserId));
            List<Long> groupIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
            List<Groupinfo> groups = groupService.list(new PropertyWrapper<>(Groupinfo.class).in(groupIds.size()>0,"id", groupIds));
            Map<Long, String> grpNameMap = groups.stream().collect(Collectors.toMap(Groupinfo::getId,Groupinfo::getName));
            for (User item : users) {
                List<GrpUser> grpUsers1 = userGrpMap.get(item.getId());
                if(grpUsers1 !=null && grpUsers1.size()>0) {
                    List<String> names = new ArrayList<>();
                    List<String> gids = new ArrayList<>();
                    for (GrpUser iitem : grpUsers1) {
                        if(grpNameMap.get(iitem.getGroupId())!=null){
                            names.add(grpNameMap.get(iitem.getGroupId()));
                            gids.add(iitem.getGroupId()+"");
                        }
                    }
                    String[] arr = new String[names.size()];
                    names.toArray(arr);
                    item.setGroupName(String.join(",", arr));
                    item.setBirthAddress(String.join(",", gids));
                }
            }
        }

        if(!Objects.isNull(vo.getEntity().getRoleId()) && vo.getEntity().getSign() == null){
            List<RoleUser> list = roleUserService.list(new PropertyWrapper<>(RoleUser.class).in(userIds.size()>0,"userId", userIds).eq("roleId", Long.valueOf(vo.getEntity().getRoleId())));
            Map<Long, Long> roleUserIds = list.stream().collect(Collectors.toMap(RoleUser::getUserId, RoleUser::getId));
            Map<Long, Integer> isLeaders = list.stream().collect(Collectors.toMap(RoleUser::getUserId, RoleUser::getIsLeader));
            users.forEach(res->{
                res.setRoleUserId(roleUserIds.get(res.getId()));
                res.setIsLeader(isLeaders.get(res.getId()));
            });
        }
        if(vo.getEntity().getGroupId() != null && vo.getEntity().getSign() == null){
            List<GrpUser> list = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in(userIds.size()>0,"userId", userIds).eq("groupId", vo.getEntity().getGroupId()));
            Map<Long, Long> grpUserIds = list.stream().collect(Collectors.toMap(GrpUser::getUserId, GrpUser::getId));
            Map<Long, Integer> isLeaders = list.stream().collect(Collectors.toMap(GrpUser::getUserId, GrpUser::getIsLeader));
            users.forEach(res->{
                res.setGrpUserId(grpUserIds.get(res.getId()));
                res.setIsLeader(isLeaders.get(res.getId()));
            });
        }
        return renderSuccess(page);
    }



    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list1")
    @ResponseBody
    public JsonResult<Pager<User>> list1(@RequestBody UserVo vo) {
        Pager<User> userPager = getService().list1(vo);
        return renderSuccess(userPager);
    }



    @ApiOperation(value = "根据vo指定条件进行查询",notes = "查询当前用户所属组织id")
    @PostMapping("/getUserGrpid")
    @ResponseBody
    private JsonResult<Long> getUserGrpid(){
        var user = SessionUser.getCurrentUser();
        GrpUser grpUser = grpUserService.get(new PropertyWrapper<>(GrpUser.class).eq("userId",user.getId()));
        Long grpid = null;
        if(grpUser!=null){
            grpid = grpUser.getGroupId();
            Groupinfo groupinfo = groupService.getById(grpid);
        }
        return renderSuccess(grpid);
    }


    @ApiOperation(value = "查询当前用户所属组织id",notes = "查询当前用户所属组织id")
    @PostMapping("/wxgetUserGrpid")
    @ResponseBody
    private JsonResult<Long> wxgetUserGrpid(@RequestParam String userId){

        GrpUser grpUser = grpUserService.get(new PropertyWrapper<>(GrpUser.class).eq("userId",userId));
        Long grpid = null;
        if(grpUser!=null){
            grpid = grpUser.getGroupId();
            Groupinfo groupinfo = groupService.getById(grpid);
        }
        if(grpid!=null){
            return renderSuccess(grpid.toString());
        }else {
            return renderSuccess();
        }

    }

    /**
     * 查询当前用户所属组织名称
     * @param userId
     * @author 王树彬
     * @date 2020/7/10
     * @return
     */
    @ApiOperation(value = "查询当前用户所属组织名称",notes = "查询当前用户所属组织名称")
    @PostMapping("/getUserGroup")
    @ResponseBody
    public JsonResult getUserGroup(Long userId){
        //根据用户id获取用户所在组织id
        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId", userId));
        List<Groupinfo> groups = new ArrayList<>();
        if(grpUsers.size()>0){
            List<Long> groupIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
            groups = groupService.list(new PropertyWrapper<>(Groupinfo.class).in(groupIds.size()>0,"id", groupIds));
            Map<Long, String> grpNameMap = groups.stream().collect(Collectors.toMap(Groupinfo::getId,Groupinfo::getName));
        }
        return renderSuccess(groups);
    }

    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/list2")
    @ResponseBody
    public JsonResult<Pager<User>> list2(@RequestBody UserVo vo) {
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/listing")
    @ResponseBody
    public JsonResult<Pager<User>> listing(@RequestBody UserVo vo) {
        Pager<User> page = getService().userPage(vo.getPager().toPager(), vo.getEntity());
        return renderSuccess(page);
    }

    @ApiOperation(value = "申请入职",notes = "申请入职")
    @PostMapping("/applyIn")
    @ResponseBody
    public JsonResult applyin(@RequestParam Long id){
        User user = service.getById(id);
        user.setIsEntry(100);
        service.updateById(user);
        return renderSuccess(user.getIsEntry());
    }

    @ApiOperation(value = "申请离职",notes = "申请离职")
    @PostMapping("/applyout")
    @ResponseBody
    public JsonResult applyout(@RequestParam Long id){
        User user = service.getById(id);
        user.setIsEntry(-100);
        service.updateById(user);
        return renderSuccess(user.getIsEntry());
    }

    @ApiOperation(value = "获取当前登录用户", notes="获取当前登录用户")
    @GetMapping("/getLoginUser")
    @ResponseBody
    public JsonResult getLoginUser(){
        Map<String, Object> loginUser = getService().getLoginUser();
        try{
            Long userid = Long.valueOf((String) loginUser.get("userid"));
            List<GrpUser> groupByUser = grpUserService.getGroupByUser(userid);
            loginUser.put("group",groupByUser);
            return renderSuccess(loginUser);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return renderError();
        }
    }

    @ApiOperation(value = "获取当前登录用户")
    @PostMapping("/getLogin")
    @ResponseBody
    public JsonResult getLogin(HttpServletRequest request) {
        Serializable id = SessionUser.getCurrentUser().getId();
        if(id != null){
            User user = getService().getById(id);
            List<GrpUser> groupByUser = grpUserService.getGroupByUser(user.getId());
            if(groupByUser.isEmpty()) return renderSuccess(user);
            List<Groupinfo> groupinfos = groupService.listByIds(groupByUser.stream().map(m -> m.getGroupId()).collect(Collectors.toList()));
            Long parentId = groupinfos.get(0).getParentId();
            if(parentId != null && parentId != 0L){
                user.setGroupName(groupinfos.get(0).getName());
                user.setCompanyName(groupService.getById(parentId).getName());
            }else {
                user.setGroupName(groupinfos.get(0).getName());
            }
            return renderSuccess(user);
        }else{
            return renderError();
        }
    }

    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
        return renderSuccess(null);
    }

    @ApiOperation(value = "注销和开通", notes="注销和开通")
    @GetMapping("/updateStatus")
    @ResponseBody
    public JsonResult updataStatus(@RequestParam("ids") String ids,@RequestParam("type") int type){

        return renderSuccess(getService().updateStatus(ids,type));
    }

    @ApiOperation(value = "修改密码", notes="修改密码")
    @GetMapping("/ediPassWD")
    @ResponseBody
    public JsonResult ediPassWD(@RequestParam("nowPassWD")String nowPassWD,@RequestParam("newPassWD") String newPassWD,HttpServletRequest request){
        Serializable userId = SessionUser.getCurrentUser().getId();
        User user = getService().getById(userId);
        String msg = service.updatePassword(nowPassWD, newPassWD, user);
        if(msg == null) return renderSuccess();
        else return renderError(msg);
    }

    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response)throws Exception {
        List<Long> ids = new ArrayList<>();
        List<User> users= service.list(new PropertyWrapper<>(User.class).eq("sign", 3).eq("status", 0));
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).in(userIds.size()>0,"userId", userIds));
        Map<Long, List<RoleUser>> userRoleMap = roleUsers.stream().collect(Collectors.groupingBy(RoleUser::getUserId));
        List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.list(new PropertyWrapper<>(Role.class).in(roleIds.size()>0,"id", roleIds));
        Map<Long, String> roleNameMap = roles.stream().collect(Collectors.toMap(Role::getId,Role::getName));
        //添加角色
        for (User item : users) {
            List<RoleUser> roleUsers1 = userRoleMap.get(item.getId());
            if(roleUsers1 !=null && roleUsers1.size()>0){
                List<String> names = new ArrayList<>();
                for (RoleUser iitem : roleUsers1) {
                    if(roleNameMap.get(iitem.getRoleId())!=null) {
                        names.add(roleNameMap.get(iitem.getRoleId()));
                    }
                }
                String[] arr = new String[names.size()];
                names.toArray(arr);
                item.setRoleName(String.join(",",arr));
            }
        }

        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in(userIds.size()>0,"userId", userIds));

        //添加组织
        if(grpUsers.size()>0){
            Map<Long, List<GrpUser>> userGrpMap = grpUsers.stream().collect(Collectors.groupingBy(GrpUser::getUserId));
            List<Long> groupIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
            List<Groupinfo> groups = groupService.list(new PropertyWrapper<>(Groupinfo.class).in(groupIds.size()>0,"id", groupIds));
            Map<Long, String> grpNameMap = groups.stream().collect(Collectors.toMap(Groupinfo::getId,Groupinfo::getName));
            for (User item : users) {
                List<GrpUser> grpUsers1 = userGrpMap.get(item.getId());
                if(grpUsers1 !=null && grpUsers1.size()>0) {
                    List<String> names = new ArrayList<>();

                    for (GrpUser iitem : grpUsers1) {
                        if(grpNameMap.get(iitem.getGroupId())!=null){
                            names.add(grpNameMap.get(iitem.getGroupId()));
                        }
                    }
                    String[] arr = new String[names.size()];
                    names.toArray(arr);
                    item.setGroupName(String.join(",", arr));
                }
            }
        }
        List<String> strings = Arrays.asList("name","trueName","status","roleName","groupName","companyName");

        DownMetadataUtil downMetadataUtil = new DownMetadataUtil();
        downMetadataUtil.downExcel(1,users,response,User.class,strings);
       /* // 这里注意 使用swag;ger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        //下载
        EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(getMsgDataList);*/

    }

    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @GetMapping("/exportItUser")
    @ResponseBody
    public JsonResult exportItUser(HttpServletResponse response) {
        List<User> itUsers = getService().list(new PropertyWrapper<>(User.class).eq("sign", 3).eq("status",0));
        List<Long> userIds = itUsers.stream().map(User::getId).collect(Collectors.toList());
        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in(userIds.size()>0,"userId", userIds));
        if(grpUsers.size()>0){
            Map<Long, List<GrpUser>> userGrpMap = grpUsers.stream().collect(Collectors.groupingBy(GrpUser::getUserId));
            List<Long> groupIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
            List<Groupinfo> groups = groupService.list(new PropertyWrapper<>(Groupinfo.class).in(groupIds.size()>0,"id", groupIds));
            Map<Long, String> grpNameMap = groups.stream().collect(Collectors.toMap(Groupinfo::getId,Groupinfo::getName));
            for (User item : itUsers) {
                List<GrpUser> grpUsers1 = userGrpMap.get(item.getId());
                if(grpUsers1 !=null && grpUsers1.size()>0) {
                    List<String> names = new ArrayList<>();
                    for (GrpUser iitem : grpUsers1) {
                        if(grpNameMap.get(iitem.getGroupId())!=null){
                            names.add(grpNameMap.get(iitem.getGroupId()));
                        }
                    }
                    String[] arr = new String[names.size()];
                    names.toArray(arr);
                    item.setGroupName(String.join(",", arr));
                }
            }
        }
        getService().exportItUser(response,itUsers);
        return renderSuccess();
    }

    @ApiOperation(value = "导入数据互联网用户")
    @PostMapping("/importItUser")
    @ResponseBody
    public JsonResult importItUser(@RequestParam(value = "file", required = true) MultipartFile file,HttpServletResponse response) {
        try {
            List<User> userList = EasyExcel.read(file.getInputStream()).sheet(0).head(User.class).doReadSync();
            String strings = getService().importItUserNew(userList);
            if(strings==null){
                grpUserService.importGrpUserNew(userList);
                return renderSuccess();
            }else{
                return renderError(strings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传头像
     * @param file
     * @author 开普云
     * @date 2020/11/27
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传头像")
    @PostMapping("/importHead")
    @ResponseBody
    public JsonResult importHead(MultipartFile file) throws IOException {
        Map<String,Object> result = new HashMap<>();
        String url;
        if(fdfsWebServer.getWebServerUrl() != null){
            url = fdfsClient.uploadFile(file);
        }else {
            url = service.importHead(file);
        }
        if(url != null){
            result.put("url",url);
            return renderSuccess(result);
        }else{
            return renderError("上传失败");
        }
    }

    /**
     * 查看文件后缀
     * @param fileName
     * @author 王树彬
     * @date 2022/6/6
     * @return
     */
    public boolean checkFileExt(String fileName) {
        String[] allows = new String[]{"png", "jpg", "jpeg", "gif", "pdf", "mp4","apk"};
        List<String> strings = Arrays.asList(allows);
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return strings.contains(fileExtension);
    }

    /**
     * 获取当前IP
     * @param request
     * @author hongshou
     * @date 2020/7/20
     * @return
     */
    @ApiOperation(value = "获取当前ip")
    @PostMapping("/getCurrentIp")
    @ResponseBody
    public JsonResult<String> getCurrentIp(HttpServletRequest request){
        return renderSuccess(IpUtil.getRequestIp(request));
    }

    /**
     * 获取基本信息
     * @author hongshou
     * @date 2020/5/26
     * @param request
     * @return
     */
    @ApiOperation(value = "获取基本信息")
    @PostMapping("/getBaseInfo")
    @ResponseBody
    public JsonResult<User> getBaseInfo(HttpServletRequest request){
        val user = SessionUser.getCurrentUser();
        if(user == null){
            return renderError("请先登录");
        }
        return doGetById(getService(), user.getId());
    }

    /**
     * 修改登陆用户id对应的用户信息
     * @param user
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @ApiOperation(value = "设置基本信息", notes="修改登陆用户id对应的用户信息")
    @PutMapping("/updateBaseInfo")
    @ResponseBody
    public JsonResult<Void> updateBaseInfo(@RequestBody User user) {
        return doUpdateById(getService(), user.getId(), user);
    }

    @ApiOperation(value = "查询本周新增系统用户数",notes = "查询本周新增系统用户数")
    @GetMapping("/addThisWeek")
    @ResponseBody
    public JsonResult<Map> addThisWeek(){
        return renderSuccess(getService().addThisWeek());
    }

    @ApiOperation(value="查询本周新增互联网用户数",notes = "查询本周新增互联网用户数")
    @GetMapping("/addItThisWeek")
    @ResponseBody
    public JsonResult<Map> addItThisWeek(){
        return renderSuccess(getService().addItThisWeek());
    }

    @ApiOperation(value = "本月新增系统用户",notes ="本月新增系统用户")
    @GetMapping("/addThisMonth")
    @ResponseBody
    public JsonResult<Map> addThisMonth(){
        return renderSuccess(getService().addThisMonth());
    }

    @ApiOperation(value = "本月新增it用户",notes = "本月新增it用户")
    @GetMapping("/addItThisMonth")
    @ResponseBody
    public JsonResult<Map> addItThisMonth(){
        return renderSuccess(getService().addItThisMonth());
    }

    @ApiOperation(value = "本年新增系统用户",notes = "本年新增系统用户")
    @GetMapping("/addThisYear")
    @ResponseBody
    public JsonResult<Map> addThisYear(){
        return renderSuccess(getService().addThisYear());
    }

    @ApiOperation(value = "本年新增it用户",notes = "本年新增it用户")
    @GetMapping("/addItThisYear")
    @ResponseBody
    public JsonResult<Map> addItThisYear(){
        return renderSuccess(getService().addItThisYear());
    }

    @ApiOperation(value = "添加下级")
    @PostMapping("/setLowerLv")
    @ResponseBody
    public JsonResult setLowerLv(@RequestParam("ids") String ids){
        val user = SessionUser.getCurrentUser();
        List<User> userList = service.getParentIds(user.getId());
       //遍历用户列表获得id插入paentids集合
        List<Long> parentids = new ArrayList<>();
        for(int i = 0;i<userList.size();i++){
            parentids.add(userList.get(i).getId());
        }
        String[] s = ids.split(",");
        List<Long> longList = new ArrayList<>();
        for(int i = 0; i <s.length;i++){
            longList.add(Long.parseLong(s[i]));
        }
        for(int i = 0; i<longList.size();i++){
            for(int j = 0;j<parentids.size();j++){
                if(longList.get(i).equals(parentids.get(j))){
                    return renderError("不能将自己或自己的上级设为下属！");
                }
            }
            User lowUser = service.getById(longList.get(i));
            lowUser.setLeaderId(user.getId());
            service.updateById(lowUser);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "查询上级")
    @PostMapping("/getParentIds")
    @ResponseBody
    public JsonResult<List<User>> getParentIds(){
        val user = SessionUser.getCurrentUser();
        return renderSuccess(service.getParentIds(user.getId()));
    }

    @ApiOperation(value = "查询下属")
    @PostMapping("/getLowers")
    @ResponseBody
    public JsonResult<List<User>> getLowers(@RequestParam("leaderId") String leaderId){
        val user = SessionUser.getCurrentUser();
        return renderSuccess(getService().list(new PropertyWrapper<>(User.class).eq("leaderId",user.getId())));
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/resetPassword")
    @ResponseBody
    public JsonResult resetPassword(@RequestBody RestPassVo vo,HttpServletRequest request){
        String password = vo.getPassword();
        Long[] ids = vo.getIds();
        if(StringUtils.isEmpty(vo.getPassword())){
            return  renderError("新密码不能为空!");
        }
        if(ids == null || ids.length <=0){
            return  renderError("请选择用户!");
        }
        List<User> userList = new ArrayList<>();
        for (Long id : ids) {
            User user = new User();
            user.setId(id);
            user.setPassWord(password);
            user.setMdPassWord(PasswordHelper.doEncryptedPassword(vo.getPassword()));
            userList.add(user);
        }
        if(!getService().updateBatchById(userList)){
            renderError("重置失败,请联系管理员!");
        }
        jLogService.addLogs(request,"重置密码","重置密码",ids,null,"用户",null);
        return renderSuccess();
    }

    @ApiOperation(value = "根据用户名查询")
    @GetMapping("/getUserByName")
    @ResponseBody
    public JsonResult getUserByName(String userName){
        return renderSuccess(getService().getByName(userName));
    }

    @ApiOperation("验证用户密码")
    @PostMapping(value = "/verificationPassword")
    @ResponseBody
    public JsonResult<String> verificationPassword(@RequestParam String name, String password, HttpServletRequest request){

        JsonResult jsonResult=new JsonResult();
        User user = getService().get(new PropertyWrapper<>(User.class).eq("name",name).eq("password",password));
        if(user!=null){
            jsonResult.setMsg("成功");
            jsonResult.setObj(user);
            jsonResult.setSuccess(true);
        } else {
            jsonResult.setMsg("用户名或密码错误！");
            jsonResult.setSuccess(false);
        }
        return renderSuccess(jsonResult.toString());
    }

    @ApiOperation("第三方账号绑定")
    @PostMapping(value = "/bindThirdParty")
    @ResponseBody
    public JsonResult<User> bindThirdParty(@RequestBody ThirdPartyLoginVo vo){
        User user = getService().getByOpenId(vo.getType(), vo.getOpenId());
        String type = vo.getType().toUpperCase();
        if(user!=null) return renderError(ThirdPartyType.valueOf(type).getValue() +"账号已经被绑定");
        Long userid = SessionUser.getCurrentUser().getId();
        if(userid==null) return renderError("登录状态失效,请先登录!");
        user = getService().getById(userid);
        if(user.getHeadUrl() == null) user.setHeadUrl(vo.getUrlPic());
        if(type.equals("QQ")){
            user.setQqname(vo.getPersonName());
            user.setOpenidQq(vo.getOpenId());
        }else if(type.equals("WX")){
            user.setWxname(vo.getPersonName());
            user.setOpenidWx(vo.getOpenId());
        }else if(type.equals("WB")){
            user.setWbname(vo.getPersonName());
            user.setOpenidWb(vo.getOpenId());
        }else{
            user.setApplename(vo.getPersonName());
            user.setOpenidApple(vo.getOpenId());
        }
        getService().updateById(user);
        user.setPassWord(null);
        user.setMdPassWord(null);
        return renderSuccess(user);
    }

    @ApiOperation(value = "企业添加子账户")
    @PostMapping(value = "/addQyUser")
    @ResponseBody
    public JsonResult<Void> addQyUser(@RequestBody QyChildVo vo) {
        User user = getService().getByCellPhone(vo.getPhone());
        if(user == null) return renderError("手机号未注册,请先注册");
//        user.setTrueName(vo.getName());
        user.setJob(vo.getJob());
        user.setBusinessId(vo.getQyid());
        user.setSign(21);
        getService().updateById(user);
        return renderSuccess();
    }

    @ApiOperation(value = "获取组织")
    @GetMapping(value = "/getGroup/{userid}")
    @ResponseBody
    public JsonResult getGroup(@PathVariable("userid")Long id) {
        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId", id));
        List<Long> ids = grpUsers.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
        List<Groupinfo> groupinfos = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("id", ids));
        List<String> names = groupinfos.stream().map(m -> m.getName()).collect(Collectors.toList());
        Map<String,List<String>> map = new HashMap<>();
        map.put("group",names);
        return renderSuccess(map);
    }

    @ApiOperation(value = "停用用户")
    @PostMapping(value = "/stopUser")
    @ResponseBody
    public JsonResult<Void> stopUser(@RequestBody Long[] ids,HttpServletRequest request) {
        List<User> list = new ArrayList<>();
        for (Long id : ids) {
            User user = new User();
            user.setId(id);
            user.setStopState(1);
            list.add(user);
        }
        getService().updateBatchById(list);
        jLogService.addLogs(request,"停用用户","停用",ids,null,"用户",null);
        return renderSuccess();
    }
    @ApiOperation(value = "恢复用户")
    @PostMapping(value = "/restoreUser")
    @ResponseBody
    public JsonResult<Void> restoreUser(@RequestBody Long[] ids,HttpServletRequest request) {
        List<User> list = new ArrayList<>();
        for (Long id : ids) {
            User user = new User();
            user.setId(id);
            user.setStopState(0);
            user.setLocktimes(0);
            list.add(user);
        }
        getService().updateBatchById(list);
        jLogService.addLogs(request,"恢复用户","恢复",ids,null,"用户",null);
        return renderSuccess();
    }

    @GetMapping(value = "/getActive")
    @ResponseBody
    public JsonResult getActive() {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        List<User> collect = activeSessions.stream().filter(m -> m.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null).map(session -> {
            return (User)((SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).getPrimaryPrincipal();
        }).collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(User::getId)) ),ArrayList::new));
        return renderSuccess(collect);
    }

    /*
    * 计算年龄
    * @param age
    * return
    * */
    public static String validatorAge(int age){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String stratData = df.format(date).substring(4, 9);
        Integer middle = Integer.valueOf(df.format(date).substring(0, 4));
        middle = middle - age;
        String endData = middle + stratData;
        String overDate = df.format(endData);
        return overDate;
    }


    public List<User> selectget(Long id, String sex, String office, int age, String number) {
        UserVo vo=new UserVo();
        User user = new User();
        user.setGroupId(id);
        vo.setEntity(user);

        List<Long> ids = new ArrayList<>();

        boolean isAdmin = SessionUser.getCurrentUser().getId().equals(1L);

        List<GrpUser> groupByUser = grpUserService.getGroupByUser(SessionUser.getCurrentUser().getId());
        List<Long> myGroupIds = groupByUser.stream().map(m -> m.getGroupId()).collect(Collectors.toList());
        if(!myGroupIds.isEmpty()) {
            getAllGroupIds(myGroupIds);
        }
        //null是用户页面列表  !=null是消息用户列表
        if(vo.getEntity().getIds() == null){
            if(vo.getEntity().getGroupId() != null){
                if(isAdmin || myGroupIds.contains(vo.getEntity().getGroupId())){
                    List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("groupId", vo.getEntity().getGroupId()));
                    ids.addAll(grpUsers.stream().map(GrpUser::getUserId).collect(Collectors.toList()));
                }
            }else if(!isAdmin && !myGroupIds.isEmpty()){
                List<GrpUser> grpusers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in("groupId", myGroupIds));
                List<Long> userids = grpusers.stream().map(m -> m.getUserId()).collect(Collectors.toList());
                ids.addAll(userids);
            }

            if(!StringUtils.isEmpty(vo.getEntity().getRoleId())){
                if(isAdmin){
                    List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("roleId", Long.valueOf(vo.getEntity().getRoleId())).in(!ids.isEmpty(),"userId",ids));
                    ids = (roleUsers.stream().map(RoleUser::getUserId).collect(Collectors.toList()));
                }else if(!ids.isEmpty()){
                    List<RoleUser> roleUsers = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("roleId", Long.valueOf(vo.getEntity().getRoleId())).in("userId",ids));
                    ids = (roleUsers.stream().map(RoleUser::getUserId).collect(Collectors.toList()));
                }
            }
        }

        if(!isAdmin || !ids.isEmpty() ){
            vo.getEntity().setIds(ids);
        }
        List<Long> ids1 = vo.getEntity().getIds();
        String s1 =validatorAge(age);
        List<User> users=new ArrayList<>();
        ids.forEach(s->{
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id",s).eq("sex",sex).eq("DUTIES",office).le("BIRTHDATE",s1);
            User user1 = userMapper.selectOne(userQueryWrapper);
            users.add(user1);
        });
        /*return renderSuccess(page);*/
        return users;
    }



    private UserService getService() {
        return service;
    }


}
