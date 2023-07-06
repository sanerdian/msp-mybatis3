package com.jnetdata.msp.member.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.member.user.vo.AddUserVo;
import com.jnetdata.msp.member.user.vo.RegistVo;
import com.jnetdata.msp.member.user.vo.ThirdPartyLoginVo;
import com.jnetdata.msp.member.user.vo.UserVo;
import lombok.Synchronized;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by TF on 2019/3/25.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    ContentLogService contentLogService;

    @Value("${spring.datasource.jdbcDialect}")
    String jdbcDialect;

    @Override
    protected PropertyWrapper<User> makeSearchWrapper(Map<String, Object> condition) {
        List<Long> ids = null;
        if(condition.get("ids") != null) ids = (List<Long>)condition.get("ids");
        if(ids != null && ids.size() == 0) ids.add(0L);
        return createWrapperUtil(condition)
                .like("name")
                .like("trueName")
                .like("email")
                .like("leaderId")
                .eq("sex")
                .getWrapper().ge("status",0)
                .in(ids!=null,"id",ids)
                .eq(condition.get("sign")!=null ,"sign",condition.get("sign"));
    }


    /**
     * 更新状态（开通和注销）
     * @param ids
     * @param type
     * @author hongshou
     * @date 2020/5/26
     * @return
     */

    @Override
    public boolean updateStatus(String ids, int type) {
        String[] str = ids.split(",");

        List<User> list = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            User user = new User();
            user.setId(Long.valueOf(str[i]));
            //开通
            if(type == 1){
                user.setStatus(1);
            }else{
                //注销
                user.setStatus(-1);
            }
            list.add(user);
        }
        return super.updateBatchById(list);
    }

    /**
     * 根据用户名称获取用户
     * @param name 用户名称
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public User getByName(String name) {
        return super.get(new PropertyWrapper<User>(User.class).eq("name",name).gt("status",-1));
    }

    /**
     * 根据电话号获取用户
     * @param phone 电话号
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public User getByCellPhone(String phone) {
        return super.get(new PropertyWrapper<User>(User.class).eq("MOBILE",phone).gt("status",-1));
    }

    /*
     * 添加系统用户
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * return boolean类型
     * */
    @Override
    public boolean addUser(User user) {
        addUser1(user);
        return true;
    }


    @Override
    public User addUser1(User user) {
        String name = user.getName();
        User byId = this.getById(user.getId());
        //判断用户名手机号邮箱是否存在
        User olduser = this.getByName(name);
        if(!Objects.isNull(olduser)){
            if(!Objects.isNull(byId)&&byId.getId()!=olduser.getId()&&byId.getName()!=olduser.getName()) {
                throw new AuthenticationException("该用户名已存在!");
            }
        }
        User olduser1 = this.getByCellPhone(user.getMobile());
        if(!Objects.isNull(olduser1)){
            if(!Objects.isNull(byId)&&byId.getId()!=olduser.getId()&&byId.getMobile()!=olduser1.getMobile()) {
                throw new AuthenticationException("该手机号已存在!");
            }
        }
        User olduser2 = this.getByEmail(user.getEmail());
        if(!Objects.isNull(olduser2)){
            if(!Objects.isNull(byId)&&byId.getId()!=olduser2.getId()&&byId.getEmail()!=olduser2.getEmail()) {
                throw new AuthenticationException("该邮箱已存在!");
            }
        }
        String mdpasswd = PasswordHelper.doEncryptedPassword(user.getPassWord());
        user.setMdPassWord(mdpasswd);
        user.setRegTime(new Date());
        boolean insert = this.insert(user);
        contentLogService.addLog("新建用户","用户管理","新建用户",insert);
        user.setPassWord(null);
        user.setMdPassWord(null);
        return user;
    }

    /**
     * 根据注册id获取用户
     * @param vo
     * @author 王树斌
     * @date 2020/5/26
     * @return
     */
    @Override
    public User registerUsingOpenId(ThirdPartyLoginVo vo) {
        User user = new User();
        user.setSex(vo.getSex());
        user.setHeadUrl(vo.getUrlPic());
        user.setNickName(vo.getPersonName());
        String password = new Date().getTime() + "";
        user.setPassWord(password);
        user.setMdPassWord(PasswordHelper.doEncryptedPassword(password));
        user.setSign(vo.getSign()==null?3:vo.getSign());
        user.setRegTime(new Date());
        String type = vo.getType();
        user.setName(type+new Date().getTime());
        if(type.equals("WX")){
            user.setOpenidWx(vo.getOpenId());
            user.setWxname(vo.getPersonName());
        }else if(type.equals("WB")){
            user.setOpenidWb(vo.getOpenId());
            user.setWbname(vo.getPersonName());
        }else{
            user.setOpenidQq(vo.getOpenId());
            user.setQqname(vo.getPersonName());
        }
        insert(user);
        return user;
    }

    @Override
    @Synchronized
    public Integer getYQM(Long id) {
        Integer yqm = baseMapper.getYQM();
        if(yqm == null || yqm == 0 || yqm < 1000) yqm = 1000;
        yqm++;
        User user = new User();
        user.setId(id);
        user.setInvitationCode(yqm.toString());
        super.updateById(user);
        return yqm;
    }

    /**
     * 用户分页
     * @param pager
     * @param user
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public Pager<User> userPage(Pager<User> pager, User user) {
//        user.setCreateBy(SessionUser.getCurrentUser().getId());
        Page<User> page = new Page<>(pager.getCurrent(),pager.getSize());
        page = baseMapper.userPage(page, user);
        pager.setRecords(page.getRecords());
        pager.setPages((new Long(page.getPages())).intValue());
        pager.setTotal((new Long(page.getTotal())).intValue());
        return pager;
    }

    @Override
    public Map<Long, String> listGrpUser(ArrayList<Long> longs) {
        Map<Long,String> ids = new HashMap<>();
        longs.forEach(s->{

        });
        return null;
    }


    /**
     * 获取用户日志
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public Map<String,Object> getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        Map<String,Object> map = new HashMap<>();
        if(ObjectUtils.isEmpty(user)){
            map.put("status",false);
            return map;
        }

        User tempUser = super.getById(user.getId());
        tempUser.setPassWord("");

        List<Role> roleList = roleUserService.getRolesByUserId(user.getId());

        map.put("status",true);
        map.put("role",roleList);
        map.put("user",tempUser);
        map.put("userid",tempUser.getId().toString());

        return map;
    }

    /**
     *
     * @param nowPassWd 老密码
     * @param newPassWD 新密码
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public String ediPassWD(String nowPassWd, String newPassWD) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        User tempUser = super.getById(user.getId());
        if(!tempUser.getMdPassWord().equals(PasswordHelper.doEncryptedPassword(nowPassWd))){
            return "原密码不正确！";
        }else{
            tempUser.setPassWord(newPassWD);
            tempUser.setMdPassWord(PasswordHelper.doEncryptedPassword(newPassWD));

            boolean result = super.updateById(tempUser);
            if(result){
                return "密码更新成功";
            }else{
                return "密码更新失败";
            }
        }

    }

    /**
     * 获取当前登录用户实体
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public User getUser() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        return user;
    }

    /**
     * 注册用户上传头像
     * @param file
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public String importHead(MultipartFile file) {
        File targetFile=null;
        String url="";
        String fileName=file.getResource().getFilename();//原文件名
        if (fileName!=null&&fileName!=""){

            fileName =  UUID.randomUUID()+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
//            ResourceBundle resource = ResourceBundle.getBundle("config");
//            String path = resource.getString("upload_path");
            String path = configModelService.getUploadPath(ConfigModel.dir_headimg);

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File f = new File(path, fileName);

            //图片出入文件夹
            try {
                //file.transferTo(f);
                FileUtils.copyInputStreamToFile(file.getInputStream(),f);
                String basePath = configModelService.getBaseUploadPath();
//        String fileUrl=path;//存储路径
                String fileUrl = path.replace(basePath,"");
                fileUrl = fileUrl.replace("\\","/");
                url="/files"+fileUrl+"/"+fileName;
                return url;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 修改密码
     * @param nowPassWD
     * @param newPassWD
     * @param user
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public String updatePassword(String nowPassWD, String newPassWD, User user) {
        if(!user.getMdPassWord().equals(PasswordHelper.doEncryptedPassword(nowPassWD))){
            return "原密码错误";
        }else{
            user.setPassWord(newPassWD);
            user.setMdPassWord(PasswordHelper.doEncryptedPassword(newPassWD));
            boolean result = super.updateById(user);
            if(result){
                return null;
            }else{
                return "密码更新失败";
            }
        }


    }

    /*
    * 查询本周新增系统用户数
    * return
    * @author hongshou
    * @date 2020/5/26
    * */
    @Override
    public  Map<String,Object> addThisWeek(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfWeek(now).toJdkDate();
        Date endDate = DateUtil.endOfWeek(now).toJdkDate();
        return getSysUserCount(startDate,endDate);
    }

    /*
    * 查询本周新增互联网用户数
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    @Override
    public Map<String,Object> addItThisWeek(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfWeek(now).toJdkDate();
        Date endDate = DateUtil.endOfWeek(now).toJdkDate();
        return getItUserCount(startDate,endDate);
    }

    /*
    * 本月新增系统用户
    * @author hongshou
    * @date2020/5/26
    * return
    */
    @Override
    public Map<String,Object> addThisMonth(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfMonth(now).toJdkDate();
        Date endDate = DateUtil.endOfMonth(now).toJdkDate();
        return getSysUserCount(startDate,endDate);
    }

    /*
    * 本月新增it用户
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    @Override
    public Map<String,Object> addItThisMonth(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfMonth(now).toJdkDate();
        Date endDate = DateUtil.endOfMonth(now).toJdkDate();
        return getItUserCount(startDate,endDate);
    }
    /*
    * 本年新增系统用户
    * return
    * @author hongshou
    * @date 2020/5/26
    * */
    @Override
    public Map<String,Object> addThisYear(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfYear(now).toJdkDate();
        Date endDate = DateUtil.endOfYear(now).toJdkDate();
        return getSysUserCount(startDate,endDate);
    }
    /*
    * 本年新增it用户
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    @Override
    public Map<String,Object> addItThisYear(){
        Date now = new Date();
        Date startDate = DateUtil.beginOfYear(now).toJdkDate();
        Date endDate = DateUtil.endOfYear(now).toJdkDate();
        return getItUserCount(startDate,endDate);
    }

    public Map<String,Object> getSysUserCount(Date startDate,Date endDate){
        return getCount(startDate,endDate,0);
    }

    public Map<String,Object> getItUserCount(Date startDate,Date endDate){
        return getCount(startDate,endDate,3);
    }

    public Map<String,Object> getCount(Date startDate,Date endDate,int sign){
        PropertyWrapper<User> pw = new PropertyWrapper<>(User.class).ge("REGTIME", startDate).le("REGTIME", endDate);
        if(sign == 0) pw.eq("sign",0);
        else pw.in("sign","3,4,5");
        int count = super.count(pw);;
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        return map;
    }

    /*
    * 查询上级
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    @Override
    public List<User> getParentIds(Long userId){
        List<User> list = new ArrayList<>();
        getParents(list,userId);
        return list;
    }

    public void getParents(List<User> list,Long id){
        if(id == null) return;
        User user = super.getById(id);
        if(user == null) return;
        User u = new User();
        u.setId(user.getId());
        u.setName(user.getName());
        list.add(u);
        getParents(list,user.getLeaderId());
    }

    @Override
    public Pager<User> userList(UserVo vo) {
        Pager pager = new Pager();
        pager.setSize(vo.getPager().getSize());
        pager.setCurrent(vo.getPager().getCurrent());
        Pager page = super.search(pager, ConditionMap.of(vo.getEntity()));
        return null;
    }

    /**
     * 根据查询条件下载excel数据
     * @param response
     * @param itUsers
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public void exportItUser(HttpServletResponse response, List<User> itUsers) {
        String excelName = "互联网用户";
        doExport(excelName,response,itUsers);
    }

    @Override
    public String importItUser(List<List<String>> lists) {
        try {
            List<User> users = getItUsersFromExcel(lists);
            String userNames = addUsers(users);
            return userNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String importItUserNew(List<User> userList) {
        try {
            String userNames = addUsers(userList);
            return userNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加用户
     * @param users
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public String addUsers(List<User> users) {
        long countd = users.stream().map(User::getName).distinct().count();
        if(countd < users.size()) return "用户名重复";

        String phoneRegix = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        String eMailRegix = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        String passWordRegix = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![a-z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,16}$";
        Map<String,List<String>> map = new HashMap<>();
        for (User user : users) {
            if(StringUtils.isEmpty(user.getName())){
                return "用户名不能为空";
            }
            if(StringUtils.isEmpty(user.getMobile())){
                return "手机号不能为空";
            }
            if(StringUtils.isEmpty(user.getPassWord())){
                return "密码不能为空";
            }
            if(StringUtils.isEmpty(user.getEmail())){
                return "邮箱不能为空";
            }
            if(!user.getMobile().matches(phoneRegix)){
                return "手机号["+user.getMobile()+"]格式不正确";
            }
            if(!user.getEmail().matches(eMailRegix)){
                return "邮箱["+user.getEmail()+"]格式不正确";
            }
            if(!user.getPassWord().matches(passWordRegix)){
                return "密码["+user.getPassWord()+"]格式不正确, 密码必须8到16位，包括数字、小写字母、大写字母、特殊符号中至少3类";
            }
            user.setSign(3);
            user.setMdPassWord(PasswordHelper.doEncryptedPassword(user.getPassWord()));
            user.setPassWord(null);
            user.setRegTime(new Date());
        }
        List<String> userNames = users.stream().map(m -> m.getName()).collect(Collectors.toList());
        List<String> phones = users.stream().map(m -> m.getMobile()).collect(Collectors.toList());
        int count = super.count(new PropertyWrapper<>(User.class).in("name", userNames).eq("status", 0));
        int count1 = super.count(new PropertyWrapper<>(User.class).in("mobile", phones).eq("status", 0));
        if(count>0){
            List<User> list = super.list(new PropertyWrapper<>(User.class).in("name", userNames).eq("status", 0));
            List<String> names = list.stream().map(m -> m.getName()).collect(Collectors.toList());
            return "用户名["+String.join(",",names)+"]已存在";
        }
        if(count1>0){
            List<User> list = super.list(new PropertyWrapper<>(User.class).in("mobile", phones).eq("status", 0));
            List<String> phones1 = list.stream().map(m -> m.getMobile()).collect(Collectors.toList());
            return "手机号["+String.join(",",phones1)+"]已存在";
        }
        super.insertBatch(users);
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return super.get(new PropertyWrapper<>(User.class).eq("EMAIL",email).gt("status",-1));
    }

    @Override
    public User getByOpenId(String type, String openId) {
        return super.get(new PropertyWrapper<>(User.class).eq("OPENID_"+type, openId));
    }

    public List<User> getItUsersFromExcel(List<List<String>> lists){
        List<User> userList = new ArrayList<>();
        for(int i=1 ; i<lists.size(); i++){
            List<String> list = lists.get(i);
            if(!StringUtils.isEmpty(list.get(0))){
                User user = new User();
                user.setName(list.get(0));
                user.setPassWord(list.get(1));
                user.setMobile(list.get(2));
                user.setEmail(list.get(3));
                userList.add(user);
            }
        }
        return userList;
    }

    public void doExport(String excelName,HttpServletResponse response,List<User> itUsers){
        XSSFWorkbook workbook = getExcel(excelName,itUsers);
        ExcelUtil.doExport(excelName,response,workbook);
    }

    public XSSFWorkbook getExcel(String name, List<User> itUsers){
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
//        excel.add(new ExcelBean("id", "id", 0));
        excel.add(new ExcelBean("用户名", "name", 0));
//        excel.add(new ExcelBean("姓名", "trueName", 0));
        excel.add(new ExcelBean("手机", "mobile", 0));
        excel.add(new ExcelBean("邮箱", "email", 0));
        excel.add(new ExcelBean("组织", "groupName", 0));
        map.put(0, excel);
        try {
            return ExcelUtil.createExcelFile(itUsers, map, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public User selectUserone(Long id){
        User user = baseMapper.selectById(id);
        return user;
    }

    @Override
    public Map<String, Object> getLoginUser(HttpServletRequest request) {
        Map<String, Object> loginUser = getLoginUser();
        loginUser.put("mspToken",request.getSession().getId());
        loginUser.put("userid",SessionUser.getCurrentUser().getId().toString());
        loginUser.put("userId",SessionUser.getCurrentUser().getId().toString());
        return loginUser;
    }

    @Override
    public Pager<User> list1(UserVo vo) {
//        List<String> fields = getAnnotation(User.class);
//
//        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
//                .selectAll(User.class)
//                .leftJoin(RoleUser.class,RoleUser::getUserId,User::getId)
//                .leftJoin(Role.class,Role::getId,RoleUser::getRoleId)
//                .leftJoin(GrpUser.class,GrpUser::getUserId,User::getId)
//                .leftJoin(Groupinfo.class,Groupinfo::getId,GrpUser::getGroupId)
//                .like(vo.getEntity().getName()!=null,User::getName,vo.getEntity().getName())
//                .like(vo.getEntity().getTrueName()!=null,User::getTrueName,vo.getEntity().getTrueName())
//                .like(vo.getEntity().getEmail()!=null,User::getEmail,vo.getEntity().getEmail())
//                .like(vo.getEntity().getLeaderId()!=null,User::getLeaderId,vo.getEntity().getLeaderId())
//                .eq(vo.getEntity().getSex()!=null,User::getSex,vo.getEntity().getSex())
//                .ge(User::getStatus,0)
//                .in(vo.getEntity().getIds()!=null,User::getId,vo.getEntity().getIds())
//                .eq(vo.getEntity().getSign()!=null ,User::getSign,vo.getEntity().getSign())
//                .in(vo.getEntity().getGroupId()!=null,Groupinfo::getId,vo.getEntity().getGroupId())
//                .in(vo.getEntity().getRoleId()!=null,Role::getId,vo.getEntity().getRoleId());
//
//        if(jdbcDialect.equals("mysql")){
//            wrapper.select("group_concat(DISTINCT t2.ROLENAME) roleName")
//                    .select("group_concat(DISTINCT t4.GROUPNAME) groupName")
//            .groupBy(User::getId);
//        }else {
//            wrapper.select("wm_concat(DISTINCT to_char(t2.ROLENAME)) roleName")
//                    .select("wm_concat(DISTINCT to_char(t4.GROUPNAME)) groupName")
//                    .groupByStr(fields);
//        }
//
//        wrapper.orderByDesc(User::getCreateTime);
//
//        Page<User> page = baseMapper.selectJoinPage(new Page<>(vo.getPager().getCurrent(), vo.getPager().getSize()), User.class, wrapper);
//        Pager<User> userPager = new Pager<>();
//        userPager.setRecords(page.getRecords());
//        userPager.setTotal((int)page.getTotal());
//        userPager.setSize((int)page.getSize());
//        userPager.setCurrent((int)page.getCurrent());
//        userPager.setPages((int)page.getPages());
//        return userPager;
        return null;
    }

    @Override
    public void updateUser(Long id, User user) {
        String name = user.getName();
        User olduser = this.getByName(name);
        if(!Objects.isNull(olduser) && !olduser.getId().equals(id)){
            throw new AuthenticationException("该用户名已存在!");
        }

        if(!StringUtils.isEmpty(user.getMobile())){
            olduser = this.getByCellPhone(user.getMobile());
            if(!Objects.isNull(olduser) && !olduser.getId().equals(id)){
                throw new AuthenticationException("该手机号已存在!");
            }
        }

        if(!StringUtils.isEmpty(user.getEmail())){
            olduser = this.getByEmail(user.getEmail());
            if(!Objects.isNull(olduser) && !olduser.getId().equals(id)){
                throw new AuthenticationException("该邮箱已存在!");
            }
        }
        user.setId(id);
        this.updateById(user);
        contentLogService.addLog("修改用户","用户管理","修改用户",true);
    }

    private List<String> getAnnotation(Class<?> clazz){
        // 获取类的所有属性
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        Arrays.stream(fields).filter(m -> m.isAnnotationPresent(TableId.class)).forEach(field -> {
            TableId myField = field.getAnnotation(TableId.class);
            fieldNames.add("t."+myField.value());
        });
        Arrays.stream(fields).filter( m-> m.isAnnotationPresent(TableField.class)).forEach(field -> {
            TableField myField = field.getAnnotation(TableField.class);
            if(myField.exist()){
                if(myField.value() != null){
                    fieldNames.add("t."+myField.value());
                }else {
                    fieldNames.add("t."+field.getName());
                }
            }
        });
        clazz = clazz.getSuperclass();
        fields = clazz.getDeclaredFields();
        Arrays.stream(fields).filter( m-> m.isAnnotationPresent(TableField.class)).forEach(field -> {
            TableField myField = field.getAnnotation(TableField.class);
            if(myField.exist()){
                if(myField.value() != null){
                    fieldNames.add("t."+myField.value());
                }else {
                    fieldNames.add("t."+field.getName());
                }
            }
        });
        return fieldNames;
    }

}
