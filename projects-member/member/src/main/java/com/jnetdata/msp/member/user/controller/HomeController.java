package com.jnetdata.msp.member.user.controller;
//import com.baomidou.mybatisplus.toolkit.StringUtils;
//import com.jnetdata.msp.config.systemmsg.service.AppInfoMsgService;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.google.common.collect.ImmutableMap;
import com.jnetdata.msp.constants.VerifyCodeKey;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.log.userlogin.model.UserLoginLog;
import com.jnetdata.msp.log.userlogin.service.UserLoginLogService;
import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.member.business.service.BusinessService;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.shiro.impl.UserNamePasswordTelphoneToken;
//import com.jnetdata.msp.member.theclient.SqlLogClient;
//import com.jnetdata.msp.member.theclient.UserMenuLogClient;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.member.user.util.ImgCodeUtil;
import com.jnetdata.msp.member.user.util.LoginType;
import com.jnetdata.msp.member.user.util.QRLoginUtil;
import com.jnetdata.msp.member.user.vo.*;
import com.jnetdata.msp.core.model.util.PasswordHelper;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.model.util.Weixinlogin;
import com.jnetdata.msp.verifycodesaver.model.TimeConstant;
import com.jnetdata.msp.verifycodesaver.service.VerifyCodeSaverService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by TF on 2019/3/28.
 * 用户登录
 * @author hongshou
 * @date 2020/5/26
 */
@Controller
@RequestMapping("/member/home")
@ApiModel(value = "用户登陆/退出(HomeController)", description = "用户登陆/退出")
@Slf4j
public class HomeController extends BaseController {
    @Value("${user-login-lock-times}")
    private int locktimes;

    @Autowired
    UserService userService;

    @Autowired
    private UserLoginLogService loginLogService;

    @Autowired
    private JLogService jLogService;

    @Autowired
    private VerifyCodeSaverService verifyCodeSaverService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private GrpUserService grpUserService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AbstractSessionDAO sessionDAO;

    @Autowired
    private WxProperties wxProperties;

    //concurrent包的线程安全Set
    public static ConcurrentHashMap<String, Long> qrLoginUserIds = new ConcurrentHashMap<>();

    @GetMapping("/getToken")
    @ResponseBody
    public JsonResult getToken(HttpServletRequest request) {
        return renderSuccess(ImmutableMap.of("mspToken",request.getSession().getId()));
    }

    @ApiOperation(value = "用户名，密码登录")
    @PostMapping("/backAjaxLogin")
    @ResponseBody
    public JsonResult<Map> backLogin(@RequestBody LoginVo vo, HttpServletRequest request) {
        checkVerifyCode(vo.getCode(), generateValidateSessionKeyByRequestSessionId(request.getSession().getId()));
        String result = doAccountLogin(vo.getName(),LoginType.USERNAME_PHONE.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        return renderSuccess(userService.getLoginUser());
    }

    @ApiOperation(value = "个人用户,用户名(手机号)，密码登录")
    @PostMapping("/itUserLogin")
    @ResponseBody
    public JsonResult<Map> itUserLogin(@RequestBody LoginVo vo, HttpServletRequest request) {
        String result = doAccountLogin(vo.getName(),LoginType.IT_USERNAME_PHONE.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        return renderSuccess(userService.getLoginUser());
    }

    @ApiOperation(value = "系统（后台）用户,用户名(手机号)，密码登录")
    @PostMapping("/sysUserLogin")
    @ResponseBody
    public JsonResult<Map> sysUserLogin(@RequestBody LoginVo vo, HttpServletRequest request) {
        String result = doAccountLogin(vo.getName(),LoginType.SYS_USERNAME_PHONE.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        return renderSuccess(userService.getLoginUser());
    }

    @ApiOperation(value = "企业用户,用户名(手机号)，密码登录")
    @PostMapping("/qyUserLogin")
    @ResponseBody
    public JsonResult<Map> qyUserLogin(@RequestBody LoginVo vo, HttpServletRequest request) {
        String result = doAccountLogin(vo.getName(),LoginType.PASS_USERNAME_PHONE.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        Map<String, Object> map = userService.getLoginUser();
        User user = (User)map.get("user");
        Long businessId = user.getBusinessId();
        String businessStatus = "0";
        Boolean isMain = false;
        String businessName = "";
        String businessSHReasion = "";
        if(businessId!=null) {
            Business bu = businessService.getById(businessId);
            if(bu != null){
                if(bu.getStatus() != null) businessStatus = bu.getStatus();
                if(bu.getName() != null) businessName = bu.getName();
                if(bu.getDoctitle()!=null) businessSHReasion = bu.getDoctitle();
                isMain = user.getId().equals(bu.getUserid());
            }
        }
        map.put("businessName",businessName);
        map.put("businessStatus",businessStatus);
        map.put("businessSHReasion",businessSHReasion);
        map.put("isMain",isMain);
        return renderSuccess(map);
    }

    @ApiOperation(value = "邮箱，密码登录")
    @PostMapping("/emailAjaxLogin")
    @ResponseBody
    public JsonResult<Map> emailAjaxLogin(@RequestBody LoginVo vo, HttpServletRequest request) {
//        checkVerifyCode(vo.getCode(), generateValidateSessionKeyByRequestSessionId(request.getSession().getId()));
        String result = doAccountLogin(vo.getName(), LoginType.EMAIL.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        return renderSuccess(userService.getLoginUser());
    }

    @ApiOperation(value = "用户名，密码登录")
    @PostMapping("/wxbackAjaxLogin")
    @ResponseBody
    public JsonResult<Map> wxbackLogin(@RequestBody LoginVo vo,HttpServletRequest request) {
        String result = doAccountLogin(vo.getName(),LoginType.USERNAME_PHONE.getValue(),vo.getPassWord(),true, request);
        if(result!=null) return renderError(result);
        Map<String, Object> loginUser = userService.getLoginUser(request);
        return renderSuccess(loginUser);
    }

    /**
     * 第三方登录
     * @param vo
     * @param request
     * @author 王树彬
     * @date 2020/7/8
     * @return
     */
    @ApiOperation("第三方登录")
    @PostMapping(value = "/thirdPartyLogin")
    @ResponseBody
    public JsonResult<Map> thirdPartyLogin(@RequestBody ThirdPartyLoginVo vo,HttpServletRequest request) {
        User user = userService.getByOpenId(vo.getType(), vo.getOpenId());
        if (Objects.isNull(user)) {
            user = userService.addUser1(vo.toUser());
        }
        String result = doAccountLogin(vo.getOpenId(),LoginType.valueOf(vo.getType()).getValue(),user.getPassWord(),true, request);
        if(result != null) return renderError(result);
        Map<String, Object> map = userService.getLoginUser(request);
        return renderSuccess(map);
    }

    /**
     * 手机号，验证码登陆
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation("手机号，密码，验证码注册")
    @PostMapping(value = "/registByPhone")
    @ResponseBody
    public JsonResult<Map> registByPhone(@RequestBody RegistVo vo) {
        User olduser = userService.getByCellPhone(vo.getMobile());
        if(!Objects.isNull(olduser)){
            throw new AuthenticationException("该手机号已注册!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getMobile()));

        User user = userService.addUser1(vo.toUser());
        Map<String, Object> map = MapUtil.toMap(user);
        map.put("userId",user.getId());
        return renderSuccess(map);
    }


    /**
     * 手机号，验证码登陆
     * @param vo
     * @author 开普云
     * @date 2020/10/18
     * @return
     */
    @ApiOperation("邮箱，密码，验证码注册")
    @PostMapping(value = "/registByEmail")
    @ResponseBody
    public JsonResult<Map> registByEmail(@RequestBody RegistVo vo, HttpServletRequest request) {
        User olduser = userService.getByEmail(vo.getEmail());
        if(!Objects.isNull(olduser)){
            throw new AuthenticationException("该邮箱号已注册!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getEmail()));

        User user = userService.addUser1(vo.toUser());
        Map<String, Object> map = MapUtil.toMap(user);
        map.put("userId",user.getId());

        return renderSuccess(map);
    }

    @ApiOperation("用户名密码注册")
    @PostMapping(value = "/registByName")
    @ResponseBody
    public JsonResult<User> registByName(@RequestBody User entity) {
        return renderSuccess(userService.addUser1(entity));
    }

    /**
     * 用户管理-个人组织管理新建用户改为一个接口
     * @param entity
     * @param request
     * @param organId
     * @author zike
     * @date 2022/11/15
     * @return
     */
    @ApiOperation("用户名密码注册并加入对应组织")
    @PostMapping(value = "/registByNameJoinGroup/{groupId1}")
    @ResponseBody
    public JsonResult registByNameJoinGroup(@RequestBody User entity,HttpServletRequest request,@PathVariable("groupId1") String organId ) {
        AddUserVo vo = new AddUserVo();
        vo.setEntity(entity);
        vo.setGroupIds(new Long[]{Long.valueOf(organId)});
        User user = grpUserService.addUser(vo);
        return renderSuccess(user);

    }

    @ApiOperation("注册企业用户")
    @PostMapping(value = "/registBusinessUser")
    @ResponseBody
    public JsonResult registBusinessUser(@RequestBody BusinessUserVo vo) {
        Boolean b = userService.addUser(vo.getUser());
        vo.getBusiness().setUserid(vo.getUser().getBusinessId());
        businessService.add(vo.getBusiness());
        User u = new User();
        u.setId(vo.getUser().getId());
        u.setBusinessId(vo.getBusiness().getId());
        userService.updateById(u);
        return renderSuccess();
    }

    @ApiOperation("忘记密码,手机号验证修改密码")
    @PostMapping(value = "/forgetPassword")
    @ResponseBody
    public JsonResult<String> forgetPassword(@RequestBody RegistVo vo,HttpServletRequest request){
        User user = null;
        if("0".equals(vo.getSign())){
            user = userService.get(new PropertyWrapper<>(User.class).eq("mobile",vo.getMobile()).gt("status",-1).eq("sign",0));
        } else{
            user = userService.get(new PropertyWrapper<>(User.class).eq("mobile",vo.getMobile()).gt("status",-1));
        }
        if(user==null){
            throw new AuthenticationException("该手机号不存在!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getMobile()));
        user.setPassWord(vo.getPassword());
        String mdpasswd = PasswordHelper.doEncryptedPassword(vo.getPassword());
        user.setMdPassWord(mdpasswd);
        userService.updateById(user);
        return renderSuccess("修改成功");
    }

    /**
     * 邮箱验证修改密码
     * @param vo
     * @param request
     * @author 刘红乐
     * @date 2020/11/12
     * @return
     */
    @ApiOperation("忘记密码,邮箱验证修改密码")
    @PostMapping(value = "/forgetPasswordByEmail")
    @ResponseBody
    public JsonResult<String> forgetPasswordByEmail(@RequestBody RegistVo vo,HttpServletRequest request){
        User user = userService.getByEmail(vo.getEmail());
        if(user==null){
            throw new AuthenticationException("该邮箱不存在!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getEmail()));
        user.setPassWord(vo.getPassword());
        String mdpasswd = PasswordHelper.doEncryptedPassword(vo.getPassword());
        user.setMdPassWord(mdpasswd);
        userService.updateById(user);
        return renderSuccess("修改成功");
    }

    @ApiOperation("手机号，验证码登陆")
    @PostMapping(value = "/loginByPhone")
    @ResponseBody
    public JsonResult loginByPhone(@RequestBody MobileSmsLoginVo vo, HttpServletRequest request) {
        val user = userService.getByCellPhone(vo.getMobile());
        if(Objects.isNull(user)){
            throw new AuthenticationException("该手机号未注册!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getMobile()));
        String result = doAccountLogin(user.getName(), LoginType.USERNAME_PHONE.getValue(),user.getPassWord(),false,request);
        if(result!=null) return renderError(result);
        Map map = userService.getLoginUser(request);
        return renderSuccess(map);
    }

    @ApiOperation("手机号，验证码登陆")
    @PostMapping({"/loginByPhonePassword"})
    @ResponseBody
    public JsonResult loginByPhonePassword(@RequestBody LoginVo vo, HttpServletRequest request) {
        User user = this.userService.getByCellPhone(vo.getName());
        if (Objects.isNull(user)) {
            throw new AuthenticationException("该手机号未注册!");
        } else {
            this.checkVerifyCode(vo.getCode(), this.generateValidateSessionKeyByCellPhone(vo.getName()));
            String result = this.doAccountLogin(user.getName(), LoginType.USERNAME_PHONE.getValue(), vo.getPassWord(), false, request);
            if (result != null) {
                return this.renderError(result);
            } else {
                Map map = this.userService.getLoginUser(request);
                return this.renderSuccess(map);
            }
        }
    }

    @ApiOperation("手机号绑定")
    @PostMapping(value = "/bindPhone")
    @ResponseBody
    public JsonResult<User> bindPhone(@RequestBody MobileSmsLoginVo vo){
        User olduser = userService.getByCellPhone(vo.getMobile());
        if(!Objects.isNull(olduser)){
            throw new AuthenticationException("该手机号已注册!");
        }
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getMobile()));
        IUser<Long> iuser = SessionUser.getCurrentUserWithoutException();
        if(iuser == null) return renderError("登录失效!");
        User user = userService.getById(iuser.getId());
        user.setMobile(vo.getMobile());
        userService.updateById(user);
        return renderSuccess();
    }

    @ApiOperation(value = "getAppKey", hidden = true)
    @GetMapping(value = "/getAppKey/{key}")
    @ResponseBody
    public JsonResult<User> getAppKey(@PathVariable("key") String key){
        String md5 = PasswordHelper.doEncryptedPassword(key + "cpuSerial");
        return renderSuccess(md5);
    }

    @ApiOperation("手机号更换")
    @PostMapping(value = "/changePhone/{id}")
    @ResponseBody
    public JsonResult<User> changePhone(@RequestBody MobileSmsLoginVo vo, @PathVariable("id")Long id){
        checkVerifyCode(vo.getVerifyCode(), generateValidateSessionKeyByCellPhone(vo.getMobile()));
        User old = userService.getByCellPhone(vo.getMobile());
        if(old != null) return renderError("手机号已注册！");
        User user = new User();
        user.setId(id);
        user.setMobile(vo.getMobile());
        userService.updateById(user);
        return renderSuccess();
    }

    @ApiOperation("获取密码")
    @GetMapping(value = "/getCode")
    @ResponseBody
    public JsonResult getCode(HttpServletRequest request) {
        String v = (String) verifyCodeSaverService.get(SecurityUtils.getSubject().getSession(), generateValidateSessionKeyByRequestSessionId(request.getSession().getId()));
        Map<String,String> result = new HashMap<>();
        result.put("code",v);
        return JsonResult.success(result);
    }

    @ApiOperation("获取sessionid")
    @PostMapping(value = "/getSessionId")
    @ResponseBody
    public JsonResult<String> getSessionId() {
        Session session = SecurityUtils.getSubject().getSession();
        return renderSuccess(session.getId().toString());
    }

    @ApiOperation("微信小程序登陆")
    @PostMapping(value = "/loginByWeixin")
    @ResponseBody
    public JsonResult<Map> loginByWeixin(@RequestParam String code,HttpServletRequest request) {
        Map map = Weixinlogin.getWxUserOpenid(code,wxProperties.getAppid(),wxProperties.getSecret());
        map.put("uuid",request.getSession().getId());
        return renderSuccess(map);
    }

    /**
     * 验证码校验
     * @param verifyCode
     * @param sessionKey
     * @author hongshou
     * @date 2020/5/26
     */
    private void checkVerifyCode(String verifyCode, String sessionKey) {

        if (null == verifyCode || "".equals(verifyCode.trim())) {
            throw new AuthenticationException("验证码不能为空");
        }

        Session session = SecurityUtils.getSubject().getSession();
        String v = (String) verifyCodeSaverService.get(session, sessionKey);
        if (!v.equalsIgnoreCase(verifyCode)) {
            throw new AuthenticationException("验证码错误!");
        }
    }

    private String generateValidateSessionKeyByCellPhone(String phonenumber) {
        return VerifyCodeKey.generateValidateSessionKeyByCellPhone(phonenumber);
    }
    private String generateValidateSessionKeyByRequestSessionId(String requestSessionId) {
        return VerifyCodeKey.generateValidateSessionKeyByRequestSessionId(requestSessionId);
    }

    @ApiOperation(value = "用户退出")
    @PostMapping("/ajaxLeave")
    @ResponseBody
    public JsonResult userLeave(HttpServletRequest request){
        try{
            doLogoutLog(request);
        }catch (Exception e){
            throw e;
        }finally {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return renderSuccess("用户退出成功");
        }
    }

    private void doLogoutLog(HttpServletRequest request) {
        User user = new User();
        user.setId(SessionUser.getCurrentUser().getId());
        user.setLogoutTime(new Date());
        userService.updateById(user);

        Long logId = (Long)request.getSession().getAttribute("loginLogId");
        if(logId!=null) {
            UserLoginLog loginLog = new UserLoginLog();
            loginLog.setId(logId);
            loginLog.setLogoutTime(new Date());
            loginLogService.updateById(loginLog);
        }
        jLogService.addLogoutLog(request,null);
    }

    @ApiOperation(value = "获取图像编码")
    @GetMapping("/imgCode")
    public void imgCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ImgCodeUtil icu = new ImgCodeUtil();
        String code = icu.generatorCode();

        Session session= SecurityUtils.getSubject().getSession();
        String key = VerifyCodeKey.generateValidateSessionKeyByRequestSessionId(req.getSession().getId());
        verifyCodeSaverService.set(session, key, code, 5 * TimeConstant.MINUTE, 2);

        icu.imgCode(code,req,resp);
    }
    @ApiOperation(value = "获取登录二维码")
    @GetMapping("/getLoginQR")
    @ResponseBody
    public JsonResult<Map<String, String>> getLoginQR(HttpServletRequest request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String uuid = UUID.randomUUID().toString();
//        String url = request.getScheme()+"://"+ request.getServerName()+":"+request.getServerPort() + "/member/home/scanQR/"+uuid;
        String url = "/member/home/scanQR/"+uuid;
        QrCodeUtil.generate(url,360,360,"png",outputStream);
        Base64.Encoder encoder = Base64.getEncoder();
        String img = "data:image/jpeg;base64," + encoder.encodeToString(outputStream.toByteArray());
        Map<String,String> map = new HashMap<>();
        map.put("img",img);
        map.put("qrid",uuid);
        return renderSuccess(map);
    }
    @ApiOperation(value = "获取登录二维码id")
    @GetMapping("/getLoginQRId")
    @ResponseBody
    public JsonResult<Map<String, String>> getLoginQRId(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString();
        Map<String,String> map = new HashMap<>();
        map.put("qrid",uuid);
        return renderSuccess(map);
    }

    /**
     * 获取登录二维码
     * @param request
     * @param uuid
     * @author 王树彬
     * @date 2020/7/1
     * @return
     */
    @GetMapping("/getLoginQRById/{qrid}")
    @ResponseBody
    public JsonResult<Map<String, String>> getLoginQRById(HttpServletRequest request,@PathVariable("qrid") String uuid) {
        qrLoginUserIds.put(uuid,0L);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        String url = request.getScheme()+"://"+ request.getServerName()+":"+request.getServerPort() + "/member/home/scanQR/"+uuid;
        String url = "/member/home/scanQR/"+uuid;
        QrCodeUtil.generate(url,360,360,"png",outputStream);
        Base64.Encoder encoder = Base64.getEncoder();
        String img = "data:image/jpeg;base64," + encoder.encodeToString(outputStream.toByteArray());
        Map<String,String> map = new HashMap<>();
        map.put("img",img);
        map.put("qrid",uuid);
        return renderSuccess(map);
    }

    /**
     * 扫描二维码
     * @param qrid
     * @author 王树彬
     * @date 2020/7/1
     * @return
     */
    @ApiOperation(value = "扫描二维码")
    @GetMapping("/scanQR/{qrid}")
    @ResponseBody
    public JsonResult<Map<String, String>> scanQR(@PathVariable("qrid") String qrid) {
        IUser<Long> currentUser = SessionUser.getCurrentUser();
        qrLoginUserIds.put(qrid,currentUser.getId());
        QRLoginUtil.sendInfo(qrid,"islogin");
        return renderSuccess();
    }
    @ApiOperation(value = "扫码登录")
    @GetMapping("/loginByQR1/{qrid}")
    @ResponseBody
    public JsonResult<Map<String, Object>> loginByQR1(@PathVariable("qrid") String qrid,HttpServletRequest request) {
        if(!qrLoginUserIds.containsKey(qrid))  return renderError("id失效");
        Long userid = qrLoginUserIds.get(qrid);
        if(userid == 0L) return renderError("未扫码");
        User user = userService.getById(userid);
        if(user == null) return renderError("用户无效");
        qrLoginUserIds.remove(qrid);

        String zu = "";
        String bumen = "";
        List<GrpUser> grpUsers = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("userId", userid).select("GROUPID"));
        Groupinfo groupinfo = groupService.getById(grpUsers.get(0).getGroupId());
        if(groupinfo.getParentId() == 0L){
            bumen = groupinfo.getName();
        }else {
            zu = groupinfo.getName();
            Groupinfo parent = groupService.getById(groupinfo.getParentId());
            bumen = parent.getName();
        }

        Map<String,Object> map = new HashMap<>();
        map.put("id",userid+"");
        map.put("name",user.getName());
        map.put("truename",user.getTrueName());
        map.put("zu",zu);
        map.put("bumen",bumen);

        if(qrLoginUserIds.size() > 1000) qrLoginUserIds.clear();
        if(QRLoginUtil.sessionPools.size()>1000) {
            QRLoginUtil.sessionPools.clear();
        }

        return renderSuccess(map);
    }

    /**
     * 扫码登录
     * @param qrid
     * @param request
     * @author 王树彬
     * @date 2020/7/1
     * @return
     */
    @ApiOperation(value = "扫码登录")
    @GetMapping("/loginByQR/{qrid}")
    @ResponseBody
    public JsonResult<Map<String, Object>> loginByQR(@PathVariable("qrid") String qrid,HttpServletRequest request) {
        if(qrLoginUserIds.size() > 1000) qrLoginUserIds.clear();
        if(QRLoginUtil.sessionPools.size()>1000) {
            javax.websocket.Session session = QRLoginUtil.sessionPools.get(qrid);
            QRLoginUtil.sessionPools.clear();
            QRLoginUtil.sessionPools.put(qrid,session);
        }
        Long userid = qrLoginUserIds.get(qrid);
        if(userid == null) return renderError("id失效");
        User user = userService.getById(userid);
        if(user == null) return renderError("用户无效");
        String result = doAccountLogin(user.getName(),LoginType.USERNAME.getValue(),user.getPassWord(),true,request);
        if(result != null) return renderError(result);
        qrLoginUserIds.remove(qrid);
        QRLoginUtil.sessionPools.remove(qrid);
        Map<String,Object> map = new HashMap<>();
        map.put("id",userid+"");
        map.put("name",user.getName());
        map.put("truename",user.getTrueName());
        return renderSuccess(map);
    }

    /**
     * 账号登陆
     * @param username 用户名
     * @param password 密码
     * @param rememberMe 是否记住
     * @author 王树彬
     * @date 2020/7/1
     */
    private String doAccountLogin(String username, int loginType, String password,Boolean rememberMe,HttpServletRequest request){
        try{
            UserNamePasswordTelphoneToken token = new UserNamePasswordTelphoneToken(username,loginType, password,true);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            if (false && subject.isAuthenticated()) {  //需要限制同时登陆  把false去掉
                Collection<Session> activeSessions = sessionDAO.getActiveSessions();
                long count = activeSessions.stream().filter(m -> m.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null).map(session -> {
                    User user = (User) ((SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).getPrimaryPrincipal();
                    return user.getName();
                }).filter(m -> m.equals(username)).count();
                if(count > 1){
                    addLoginLog(request,"账号已经登录");
                    subject.logout();
                    return "账号已经登录";
                }
            }
            addLoginLog(request,null);
            return null;
        } catch ( UnknownAccountException uae ) {
            addLoginLog(request,"用户名不存在");
            return "用户名不存在";
        } catch ( IncorrectCredentialsException ice ) {
            addLoginLog(request,"密码错误");
            if(addLockTimes(username)) return "密码错误,错误次数超过"+locktimes+"次,用户锁定";
            return "密码错误";
        } catch ( LockedAccountException lae ) {
            addLoginLog(request,"用户被锁定，不能登录");
            if(lae.getMessage() != null) return lae.getMessage();
            return "用户被锁定，不能登录";
        } catch ( AuthenticationException ae ) {
            ae.printStackTrace();
            addLoginLog(request,"系统错误");
            return "系统错误";
        } catch (Exception e) {
            addLoginLog(request,"账号/密码错误！");
            e.printStackTrace();
            return "账号/密码错误！";
        }
    }

    /**
     * 添加锁定时间
     * @param username
     * @author unknown
     * @date 2020/7/23
     * @return
     */
    private boolean addLockTimes(String username){
        if(locktimes < 0) return false;
        User user = userService.getByName(username);
        if (null == user) {
            user = userService.getByCellPhone(username);
        }
        if(null == user) return false;
        user.setLocktimes(user.getLocktimes()+1);
        if(user.getLocktimes() >= locktimes) user.setStopState(2);
        userService.updateById(user);
        return user.getLocktimes() >= locktimes;
    }

    /**
     * 添加登录日志
     * @param request
     * @param result
     * @author 王树彬
     * @date 2021/2/3
     */
    private void addLoginLog(HttpServletRequest request,String result) {
        IUser<Long> user1 = SessionUser.getCurrentUserWithoutException();
        if(user1 != null){
            String ipAddress = IpUtil.getRequestIp(request);
            User u = userService.getById(SessionUser.getCurrentUser().getId());
            User user = new User();
            user.setId(user1.getId());
            user.setLoginIp(ipAddress);
            user.setLoginTime(new Date());
            user.setTimes((u.getTimes()==null?0:u.getTimes()) + 1);
            user.setLocktimes(0);
            userService.updateById(user);
            loginLogService.addLoginlog(request,user);
        }
        jLogService.addLoginLog(request,result);
    }
}
