package com.jnetdata.msp.message.msg.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.SmsClientConfiguration;
import com.baidubce.services.sms.model.SendMessageV3Request;
import com.baidubce.services.sms.model.SendMessageV3Response;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.message.msg.entity.MsgVoEntity;
import com.jnetdata.msp.message.msg.mapper.MsgMapper;
import com.jnetdata.msp.message.msg.mapper.MsgSelectMapper;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.message.msg.model.MsgModel;
import com.jnetdata.msp.message.msg.model.WxTemplateMsg;
import com.jnetdata.msp.message.msg.service.MsgService;
import com.jnetdata.msp.message.msg.utils.CheckUtil;
import com.jnetdata.msp.message.msg.utils.HttpClientUtils;
import com.jnetdata.msp.message.msg.vo.MsgVo;
import com.jnetdata.msp.message.msgConfig.service.SetMsgService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.model.RoleUser;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.message.phonemessage.service.PhoneMessageService;
import com.jnetdata.msp.message.readuser.model.ReaduserModel;
import com.jnetdata.msp.message.readuser.service.ReaduserService;
import com.jnetdata.msp.util.JpushClientUtil;
import com.jnetdata.msp.util.MailUtil;
import com.jnetdata.msp.util.SendSms;
import com.jnetdata.msp.util.Test;
import com.sun.mail.util.MailSSLSocketFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ListUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.store.message.sms.model.KindFlag;
import org.thenicesys.store.message.sms.model.PhoneMessageProperties;
import org.thenicesys.store.message.sms.service.PhoneMessageUtil;
import org.thenicesys.store.message.sms.service.SmsRequestInfo;
import org.thenicesys.store.message.sms.service.SmsResponse;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.thenicesys.store.message.sms.service.PhoneMessageUtil.*;

/**
 * 消息管理/发送消息
 */
@Controller
@RequestMapping("/message/msg")
@ApiModel(value = "MsgController", description = "消息管理")
public class MsgController extends BaseController<Long, Msg> {
//   log
    private static final Logger log = LoggerFactory.getLogger(PhoneMessageUtil.class);

    @Autowired
    private MsgService service;
    @Autowired
    private UserService userService;

    @Autowired
    private ContentLogClient contentLogService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private GrpUserService grpUserService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SetMsgService setMsgService;

    @Autowired
    private PhoneMessageService phoneMessageService;

    @Resource
    private MsgMapper msgMapper;

    /**
     * 添加消息
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "添加", notes = "添加消息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Msg entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 获取所有消息
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "获取所有消息", notes = "获取所有消息")
    @GetMapping("/remind")
    @ResponseBody
    public JsonResult<List<Msg>> remind() {
        List<Msg> list = getService().list();
        return renderSuccess(list);
    }

    /**
     * 删除指定id对应的消息信息
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除指定id对应的消息信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("消息id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 根据多个id批量删除
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "批量删除", notes = "根据多个id删除消息信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        //获取用户信息
        val user = SessionUser.getCurrentUser();
        //添加操作日志
        ContentLog contentLog = new ContentLog();
        contentLog.setCreateTime(new Date());
        contentLog.setResult("成功");
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除消息");
        contentLog.setContentType("消息管理");
        contentLog.setContent("已发消息");
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 批量假删
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "批量假删除", notes = "单方删除，不影响对方显示")
    @GetMapping("/unrealdelByIds")
    @ResponseBody
    public JsonResult unrealdelete(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        //多个id分割用逗号隔开
        String[] s = ids.split(",");
        List<Long> longList = new ArrayList<>();
        for(int i = 0;i<s.length;i++){
          longList.add(Long.parseLong(s[i]));
        }
        //假删除
        for(int i = 0;i<longList.size();i++){
          Msg msg = service.getById(longList.get(i));
          msg.setIsDisplay(-1);
          service.updateById(msg);
        }
        //操作日志
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCreateTime(new Date());
        contentLog.setResult("成功");
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除消息");
        contentLog.setContentType("消息管理");
        contentLog.setContent("已发消息");
        contentLogService.insert(contentLog);
      return renderSuccess("删除成功");
    }

    @Resource
    private ReaduserService readuserService;

    /**
     * 单方删除，不影响对方显示
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "批量假删除", notes = "单方删除，不影响对方显示")
    @GetMapping("/unrealdelByIds1")
    @ResponseBody
    public JsonResult unrealdelete1(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        //将ids通过逗号隔开
        String[] s = ids.split(",");
        val user = SessionUser.getCurrentUser();
        Long id = user.getId();
        List<Long> longList = new ArrayList<>();
        for(int i = 0;i<s.length;i++){
            longList.add(Long.parseLong(s[i]));
        }
        //遍历id，通过id进行批量假删除
        for(int i = 0;i<longList.size();i++){
            Msg msg = service.getById(longList.get(i));
            if(msg==null){
               /* List<Map> selectread = msgSelectMapper.selectread(id, longList.get(i));*/
                ReaduserModel readuserModel = readuserService.selectUser(id, longList.get(i));
                if(readuserModel!=null){
                    readuserService.upUser(readuserModel);
                }else {
                    readuserService.insetUser(id,  longList.get(i));
                }
            }if(msg!=null){
                //删除
                msg.setIsDisplay(-1);
                service.updateById(msg);
            }
        }
        //操作日志
        ContentLog contentLog = new ContentLog();
        contentLog.setCreateTime(new Date());
        contentLog.setResult("成功");
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除消息");
        contentLog.setContentType("消息管理");
        contentLog.setContent("消息通知");
        contentLogService.insert(contentLog);
        return renderSuccess("删除成功");
    }

    /**
     * 修改指定id对应的消息信息
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改指定id对应的消息信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("消息id") @PathVariable("id") Long id,
                                       @RequestBody Msg entity) {
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 设置消息为已读
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "设置已读", notes = "设置已读")
    @PostMapping(value = "/setRead")
    @ResponseBody
    public JsonResult setRead(@RequestBody Long[] ids) {
        val user = SessionUser.getCurrentUser();
        if(ids == null || ids.length <= 0){
            return renderError("请选择消息!");
        }
        //遍历用户id，设置状态为已读
        for(Long a:ids){
            Msg byId = getService().getById(a);
            if(byId!=null){
                byId.setStatus(1);
                getService().updateById(byId);
            }else {
                ReaduserModel readuserModel = readuserService.selectUser(user.getId(), a);
                if(readuserModel==null){
                    readuserService.insetUser1(user.getId(), a);
                }
            }
        }
        return renderSuccess();
    }

    ///**
    // * 功能描述： 签名校验
    // *
    // * @param
    // * @author jiaoqianjin
    // * Date: 2020/8/19 9:57
    // */
    //@GetMapping("/addSend")
    //@ApiOperation(value = "签名校验", notes = "签名校验；\n author：陈闫")
    //public void login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
    //    request.setCharacterEncoding("UTF-8");
    //    String signature = request.getParameter("signature");
    //    String timestamp = request.getParameter("timestamp");
    //    String nonce = request.getParameter("nonce");
    //    System.out.println("signature");
    //    System.out.println("timestamp");
    //    System.out.println("nonce");
    //
    //    String echostr = request.getParameter("echostr");
    //    PrintWriter out = null;
    //    try {
    //        out = response.getWriter();
    //        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
    //            out.write(echostr);
    //        }
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        out.close();
    //    }
    //}


    /**
     * 获取消息推送内容
     * @param WxMsg
     * @return
     */
    @ApiOperation(value = "获取消息推送内容")
    @PostMapping("/findWxMsg")
    @ResponseBody
    public JsonResult<Msg> findWxMsg(@RequestBody Msg WxMsg){
        Msg msg = service.get(new PropertyWrapper<>(Msg.class).eq("newsId", WxMsg.getType()).select("NEWSCONTEXT as content", "NREWSTITLE as title", "CRUSER", "jumpUrl"));
        String content = msg.getContent().substring(3,msg.getContent().length()-4);

        msg.setContent(content);

        return JsonResult.success(msg);
    }

    /**
     * 新建批量发送消息
     * @param msg
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "新建批量发送消息")
    @PostMapping("/addSends")
    @ResponseBody
    public JsonResult<Void> addSendByIds(@RequestBody Msg msg, HttpServletRequest request) throws IOException {

        List<User> userList1 = new ArrayList<>();

        //根据Msg中的userids、roleids、grpids三个字段来判断，是否发送给用户/角色/组织
        String grpids = msg.getGrpids();
        String roleids = msg.getRoleids();
        String userids = msg.getUserids();
        if (StringUtils.isNotEmpty(grpids)){//发送给组织下的用户
                String[] split = grpids.split(",");
                List<String> asList = Arrays.asList(split);
                List<GrpUser> lists = grpUserService.list(new PropertyWrapper<>(GrpUser.class).in("groupId", asList).select("userid"));
                List<Long> longList = lists.stream().map(m -> m.getUserId()).collect(Collectors.toList());
                if (!longList.isEmpty()){
                    List<User> users = userService.list(new PropertyWrapper<>(User.class).in("USERID", longList).select("openid_wx as openidWx"));
                    List<User> users1 = users.stream().filter(u -> u != null).collect(Collectors.toList());
                    userList1.addAll(users1);
                }
        }

        if (StringUtils.isNotEmpty(roleids)){
            String[] split = roleids.split(",");
            List<String> asList = Arrays.asList(split);
            List<RoleUser> lists = roleUserService.list(new PropertyWrapper<>(RoleUser.class).in("roleId", asList).select("userid"));
            List<Long> longList = lists.stream().map(m -> m.getUserId()).collect(Collectors.toList());
            if (!longList.isEmpty()){
                List<User> users = userService.list(new PropertyWrapper<>(User.class).in("USERID", longList).select("openid_wx as openidWx"));
                List<User> users2 = users.stream().filter(u -> u != null).collect(Collectors.toList());
                userList1.addAll(users2);
            }
        }

        if (StringUtils.isNotEmpty(userids)){
                String[] userIds = userids.split(",");
                List<String> adList = Arrays.asList(userIds);
                List<User> lists = userService.list(new PropertyWrapper<>(User.class).in("userid", adList).select("openid_wx as openidWx"));
                List<User> users3 = lists.stream().filter(u -> u != null).collect(Collectors.toList());
                userList1.addAll(users3);
        }

       String  ReceiveMail=null;
       String  Recmobie=null;
        //获取要发送的用户
        if(StringUtils.isNotEmpty(msg.getUserids())){
            //当发送一个用户时
            if(msg.getUserids().contains(",")){
                /*String[] split = msg.getUserids().split(",");
                for (String user : split) {
                    userService.getById(user).getEmail();*/
            } else {
                ReceiveMail=userService.getById(msg.getUserids()).getEmail();
                Recmobie=userService.getById(msg.getUserids()).getMobile();
            }
        }
        //根据configid获取消息源信息
        com.jnetdata.msp.message.msgConfig.model.Msg msgconf = setMsgService.getById(msg.getConfigId());
        if(msgconf.getMsgType().equals("邮件")){
            /*获取前端传过来的标题和内容 smtp  账号密码*/
            String title = msg.getTitle();
            //<p>测试cy-2</p> 截取第四个到倒数第五个字符串
            String content = msg.getContent().substring(3,msg.getContent().length()-4);
            String myEmailSMTPHost=msgconf.getMail_smtp();
            try {
                //使用工具类
                MailUtil.sendActiveMail(ReceiveMail, new Date().getTime() + "",msgconf.getMail_account(),msgconf.getMail_password(),title,content,myEmailSMTPHost,msgconf.getIfSLL(),msgconf.getMail_port());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //确定短信类型
        if(msgconf.getMsgType().equals("短信")){
            if(msgconf.getThreepartType().equals("助通")){
                /*传参参数实例对象*/
                PhoneMessageProperties phoneMessageProperties=new PhoneMessageProperties();
                /*把值setdao这个对象中调佣sendmsg方法*/
                phoneMessageProperties.setLoginName(msgconf.getLoginName());
                phoneMessageProperties.setPassWord(msgconf.getPassword());
                phoneMessageProperties.setCorpId(msgconf.getCorpId());
                String content = msg.getContent().substring(3,msg.getContent().length()-4);
                content+="【中科聚网】";
                phoneMessageProperties.setModelMessage(content);
                phoneMessageProperties.setUnitName("【中科聚网】");
                phoneMessageProperties.setMd5Url("http://api.cosms.cn/sms/getMD5str/");
                //0-GB、1-URLENCODE、2-UTF8、3-UCS2
                phoneMessageProperties.setMsgFormat("2");
//            短信紧急程度     1：即时	2：一般      3：群发
                phoneMessageProperties.setMtLevel("1");
                phoneMessageProperties.setMtUrl("http://api.mix2.zthysms.com/v2/sendSms");
                SMSsendMsg(phoneMessageProperties,Recmobie,phoneMessageProperties.getModelMessage(),KindFlag.REGISTER); //发送短信
            }else {
                String ACCESS_KEY_ID = msgconf.getAccessKeyId();
                String SECRET_ACCESS_KEY = msgconf.getSecretAccessKey();
                String ENDPOINT = "http://smsv3.bj.baidubce.com";

                SmsClientConfiguration config = new SmsClientConfiguration();
                config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
                config.setEndpoint(ENDPOINT);
                SmsClient smsClient = new SmsClient(config);
                SendMessageV3Request request1 = new SendMessageV3Request();
                request1.setMobile(Recmobie);
                request1.setSignatureId(msgconf.getSignendpointatureId());
                request1.setTemplate(msgconf.getTemplateId());
                Map<String, String> contentVar = new HashMap<>();
                contentVar.put("code", "23456");
                contentVar.put("minute", "1");
                request1.setContentVar(contentVar);
                //发送短信
                SendMessageV3Response response = smsClient.sendMessage(request1);
                // 解析请求响应 response.isSuccess()为true 表示成功
                if (response != null && response.isSuccess()) {
//                    System.out.println("发送成功！");
                } else {
//                    System.out.println("发送失败！");
                }
            }

        }
        if(msgconf.getMsgType().equals("APP消息")){
            List<String> registrationIds = new ArrayList<String>();
            registrationIds.add("160a3797c8e3e4b9bc6");
            int length = msg.getContent().length();
            JpushClientUtil.sendToRegistrationIds(registrationIds,"聚网办公", msg.getTitle(), msg.getContent().substring(3,length-4), "扩展字段");
        }
        //获取用户信息
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        //操作日志
        ContentLog contentLog = new ContentLog();
        contentLog.setContent("已发消息");
        contentLog.setHandleType("新建消息");
        contentLog.setContentType("消息管理");
        contentLog.setCrUser(user.getName());
        contentLog.setCreateTime(new Date());
        List list = new ArrayList();
        List<Long> useridList = new ArrayList();
        List<String> mailList = new ArrayList();
        try {
            //parentMsg显示在发送消息列表，isDisplay=1
            Msg parentMsg = new Msg();
            parentMsg.setSendTime(new Date());
            parentMsg.setParentId(0L);
            parentMsg.setContent(msg.getContent());
            parentMsg.setConfigId(msg.getConfigId());
            parentMsg.setTitle(msg.getTitle());
            parentMsg.setSendID(userId);
            parentMsg.setReceptionId(null);
            parentMsg.setIsDisplay(1);
            parentMsg.setSendRange(msg.getSendRange());
            doAdd(service, parentMsg);

            Long WxNewsId=null;
            //用户读取状态不能为空
            if (!msg.getUserids().isEmpty()) {
                for (String id : msg.getUserids().split(",")) {
                    msg.setId(null);
                    msg.setSendID(userId);
                    msg.setReceptionId(Long.valueOf(id));
                    msg.setSendTime(new Date());
                    msg.setAddressType(2);
                    msg.setIsDisplay(3);

                    msg.setParentId(parentMsg.getId());

                    parentMsg.setAddressType(2);/*
                    service.insert(m)*/
                    doUpdateById(service, parentMsg.getId(), parentMsg);
                    doAdd(service, msg);
                    WxNewsId = msg.getId();
                    User user1 = userService.getById(id);
                    if (user1 != null) {
                        useridList.add(Long.parseLong(id));
                        if(user1.getEmail()!=null){
                            mailList.add(user1.getEmail());
                        }
                    }
                }
            }
            //发送的角色id不能为空
            if (msg.getRoleids()!=null && !msg.getRoleids().isEmpty()) {
                for (String id : msg.getRoleids().split(",")) {
                    msg.setId(null);
                    msg.setSendID(userId);
                    msg.setReceptionId(Long.valueOf(id));
                    msg.setSendTime(new Date());
                    msg.setAddressType(1);
                    msg.setIsDisplay(1);
                    msg.setParentId(parentMsg.getId());

                    parentMsg.setAddressType(1);
                    doUpdateById(service, parentMsg.getId(), parentMsg);
                    doAdd(service, msg);
                    list.add(id);
                    WxNewsId = msg.getId();
                }
                for (int i = 0; i < list.size(); i++) {
                    List<RoleUser> roleUserList = roleUserService.list(new PropertyWrapper<>(RoleUser.class).eq("roleId", list.get(i)));
                    for (int j = 0; j < roleUserList.size(); j++) {
                        Long usersid = roleUserService.getById(roleUserList.get(j)).getUserId();
                        User user1 = userService.getById(usersid);
                        if (user1 != null) {
                            useridList.add(usersid);
                            if(user1.getEmail()!=null){
                                mailList.add(user1.getEmail());
                            }
                        }
                    }
                }
            }
            if (msg.getGrpids()!=null && !msg.getGrpids().isEmpty()) {
                for (String id : msg.getGrpids().split(",")) {
                    msg.setId(null);
                    msg.setSendID(userId);
                    msg.setReceptionId(Long.valueOf(id));
                    msg.setSendTime(new Date());
                    msg.setAddressType(0);
                    msg.setIsDisplay(1);
                    msg.setParentId(parentMsg.getId());
                    parentMsg.setAddressType(0);
                    doUpdateById(service, parentMsg.getId(), parentMsg);
                    doAdd(service, msg);
                    list.add(id);
                    WxNewsId = msg.getId();
                }
                for (int i = 0; i < list.size(); i++) {

                    List<GrpUser> grpUserList = grpUserService.list(new PropertyWrapper<>(GrpUser.class).eq("groupId", list.get(i)));
                    for (int j = 0; j < grpUserList.size(); j++) {
                        Long usersid = grpUserService.getById(grpUserList.get(j)).getUserId();
                        User user1 = userService.getById(usersid);
                        if (user1 != null) {
                            useridList.add(usersid);
                            if(user1.getEmail()!=null){
                                mailList.add(user1.getEmail());
                            }
                        }
                    }
                }
            }
            if(useridList.size()==0){
                List<User> userList= userService.list();
                for(int i=0;i<userList.size();i++){
                    useridList.add(userList.get(i).getId());
                    mailList.add(userList.get(i).getEmail());
                }
            }
            HashSet<Long> hashSet = new HashSet(useridList);

            useridList.clear();
            useridList.addAll(hashSet);
            List<Msg> list1 = new ArrayList<>();
            useridList.forEach(res -> {
                Msg msg1 = new Msg();
                msg1.setId(null);
                msg1.setSendID(userId);
                msg1.setReceptionId(res);
                msg1.setSendTime(new Date());
                msg1.setAddressType(2);
                msg1.setIsDisplay(0);
                msg1.setParentId(parentMsg.getId());
                list1.add(msg1);
            });
            getService().insertBatch(list1);
//            try {
////                MailUtil.send(mailList, msg.getTitle(), msg.getContent(), true);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            if(msgconf.getMsgType().equals("微信")){
                getService().extracted(msg, userList1, WxNewsId);
            }
            contentLog.setResult("成功");
            contentLogService.insert(contentLog);
            return renderSuccess();
            //捕获空指针异常，如果报空直接返回发送消息失败
        } catch (NumberFormatException e) {
            e.printStackTrace();
            contentLog.setResult("失败");
            contentLogService.insert(contentLog);
            return renderError("发送消息失败");
        }
    }

    //@Value("${base.param.appId}")
    //private String appId;
    //
    //@Value("${base.param.appSecret}")
    //private String appSecret;

    /**
     * 跟新所有为已读
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "全部已读", notes = "更新所有数据全部已读")
    @PostMapping(value = "/setAllRead")
    @ResponseBody
    public JsonResult setAllRead() {
        Long userid = SessionUser.getCurrentUser().getId();
        getService().setAllRead(userid);
        User byId = userService.getById(userid);//获取id
        String educationbackground = byId.getEducationbackground();//获取教育背景
        String trueName = byId.getTrueName();//获取名字，
        String politicsstatus = byId.getPoliticsstatus();//获取政治面貌
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        List<Long> longs = grpUserService.FindOrganization(userid);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());//集合类型转换
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }
        List<Long> selectalluser = msgMapper.selectalluser(userid, seid, educationbackground, politicsstatus, ageForBirthday, trueName);
        selectalluser.forEach(s->{
            ReaduserModel readuserModel = readuserService.selectUser(userid, s);
            if(readuserModel==null) {
                readuserService.insetUser1(userid, s);
            }
        });
        return renderSuccess();
    }

    /**
     * 查看指定id对应的消息
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "查看指定消息id对应消息信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Msg> getById(@ApiParam("消息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @Resource
    private MsgSelectMapper msgSelectMapper;

    /**
     * 消息通知查询
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "消息通知查询",notes = "消息通知查询")
    @PostMapping("/list1")
    @ResponseBody
    public JsonResult<Pager<Msg>> list1(@RequestBody MsgVo vo){
        //获取用户
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        vo.getEntity().setReceptionId(userId);
        //vo.getEntity().setIsDisplay(0);
        //查询
        JsonResult<Pager<Msg>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<Msg> msgList = pagerJsonResult.getObj().getRecords();
        //初始化
        Msg msg2 = null;
        //遍历查找标题并添加标题addSends
        for(Msg msg : msgList){
            @NotEmpty(message = "parentId") Long parentId = msg.getParentId();
            msg2 = service.getById(msg.getParentId());
            Long id = msg.getSendID();
            try{
                //通过id获取name
                String name = userService.getById(id).getName();
                msg.setSendUserName(name);
            }catch (Exception e){
//                e.printStackTrace();
            }
            Msg msg1 = service.getById(msg.getParentId());
           if(msg1!=null){
               msg.setTitle(msg1.getTitle());
               msg.setContent(msg1.getContent());
           }
        }
        Pager<Msg> obj = pagerJsonResult.getObj();
        return pagerJsonResult;
    }
    @ApiOperation(value = "消息通知查询",notes = "消息通知查询")
    @PostMapping("/list3")
    @ResponseBody
    public Msg list3(@RequestBody MsgVo vo){
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        /*msgMapper.selectuser(userId,)*/
        return null;

    }
    //根据出生年月转换成年龄
    public static int getAgeForBirthday(String birthday){
        //定义一个日期格式yyyy-MM-dd，将String转为Date
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simp.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //判断该生日是否在当前日期之前,设置一个初始值，表示输入日期错误
        if(date.after(new Date())){
            return -1;
        }
        //获取当前日历对象中的年、月、日
        Calendar nowc = Calendar.getInstance();
        int nowYear = nowc.get(Calendar.YEAR);
        int nowMonth = nowc.get(Calendar.MONTH);
        int nowDay = nowc.get(Calendar.DAY_OF_MONTH);
        //将Date转为Calendar日历对象,获取生日的年、月、日
        nowc.setTime(date);
        //通过年月日计算该对象的年纪
        //先通过Year计算初步年龄
        int year = nowYear-nowc.get(Calendar.YEAR);

        //通过Month和Day判断是否过生日

        if(nowc.get(Calendar.MONTH)>nowMonth){
            return year-1;
        }
        if (nowc.get(Calendar.DAY_OF_MONTH)>nowDay){
            return year-1;
        }
        return year;
    }


    /**
     * 微信村公告消息通知查询
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */

    @ApiOperation(value = "微信村公告消息通知查询",notes = "消息通知查询")
    @PostMapping("/wxlist")
    @ResponseBody
    public JsonResult<Pager<Msg>> wxlist1(@RequestBody MsgVo vo){

     /*   User user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        vo.getEntity().setReceptionId(userId);*/
        //vo.getEntity().setIsDisplay(0);

        JsonResult<Pager<Msg>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());

        List<Msg> msgList = pagerJsonResult.getObj().getRecords();
        Msg msg2 = null;
        //遍历查找并添加标题
        for(Msg msg : msgList){
            msg2 = service.getById(msg.getParentId());
            Long id = msg.getSendID();
            String name = userService.getById(id).getName();

         /*       msg.setContent(msg2.getContent());
                msg.setTitle(msg2.getTitle());*/
            msg.setSendUserName(name);
                /*msgList1.add(msg2);
                System.out.println(msg1);*/
            Msg msg1 = service.getById(msg.getParentId());
            if(msg1!=null){
                msg.setTitle(msg1.getTitle());
                msg.setContent(msg1.getContent());
            }
        }
        return pagerJsonResult;
    }

  /*  @ApiOperation(value = "消息通知查询不分页",notes = "消息通知查询")
    @PostMapping("/list2")
    @ResponseBody*/
  /*  public JsonResult<List<Msg>> list2(){
        //获取用户
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        //根据条件查询
        List<Msg> msgList = service.list(new PropertyWrapper<>(Msg.class).eq("isDisplay",0).eq("receptionId",userId));
       //初始化
        Msg msg2 = null;
        for(Msg msg : msgList){
            //查询
            msg2 = service.getById(msg.getParentId());
            Long id = msg.getSendID();
            String name = userService.getById(id).getName();

         *//*       msg.setContent(msg2.getContent());
                msg.setTitle(msg2.getTitle());*//*
            msg.setSendUserName(name);
                *//*msgList1.add(msg2);
                System.out.println(msg1);*//*
            //查询
            Msg msg1 = service.getById(msg.getParentId());
            if(msg1!=null){
                msg.setTitle(msg1.getTitle());
                msg.setContent(msg1.getContent());
            }
        }
        return renderSuccess(msgList);
    }*/

    /**
     * 根据vo查询
     * @param vo
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据实体属性查询", notes = "根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Msg>> list(@RequestBody MsgVo vo, HttpServletRequest request) {
        Msg msg = vo.getEntity();
        JsonResult<Pager<Msg>> page = renderSuccess(vo.getPager().toPager());
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        Long parentId = msg.getParentId();

        if (msg.getSendID() == null && msg.getReceptionId() != null && msg.getReceptionId() == -1) {//通知消息
            List<Msg> msgList = service.list(new PropertyWrapper<>(Msg.class).eq("receptionId",userId));
            List<Msg> msgList1 = new ArrayList<>();
            Msg msg2 = null;
            for(int i = 0;i<msgList.size();i++){
                msg2 = service.getById(msgList.get(i).getParentId());
                msgList1.add(msg2);
            }

            /*msg.setReceptionId(userId);
            page = doList(getService(), vo.getPager(), msg);
            List<Long> ids = page.getObj().getRecords().stream().map(Msg::getSendID).collect(Collectors.toList());
            List<Groupinfo> groupinfos = groupService.list(new PropertyWrapper<>(Groupinfo.class).in("id", ids));
            List<Role> roles = roleService.list(new PropertyWrapper<>(Role.class).in("id", ids));
            List<User> users = userService.list(new PropertyWrapper<>(User.class).in("id", ids));
            Map<Long, String> map = users.stream().collect(Collectors.toMap(User::getId, User::getName));
            Map<Long, String> map1 = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
            Map<Long, String> map2 = groupinfos.stream().collect(Collectors.toMap(Groupinfo::getId, Groupinfo::getName));
            page.getObj().getRecords().forEach(res -> {
                Long cfid = res.getConfigId();
                String s = null;
                com.jnetdata.msp.manage.msgConfig.model.Msg msg1 = setMsgService.getById(cfid);
                if (msg1 != null)
                    s = msg1.getName();
                res.setMsgSign(s);
                if (res.getAddressType() == 2) {
                    res.setSendUserName(map.get(res.getSendID()));
                } else if (res.getAddressType() == 1) {
                    res.setSendUserName(map1.get(res.getSendID()));
                } else if (res.getAddressType() == 0) {
                    res.setSendUserName(map2.get(res.getSendID()));
                }
            });*/
        } else if (msg.getReceptionId() == null && msg.getSendID()!=null && msg.getSendID() == -1) {//发送消息
            msg.setSendID(userId);
            PageVo1 pager = vo.getPager();
            if(pager.getSize() == null){
                pager.setSize(15);
            }
            page = doList(getService(), pager, msg);


            //查询消息类型
            List<Long> configIds = page.getObj().getRecords().stream().map(Msg::getConfigId).collect(Collectors.toList());
            if(configIds.size()>0){
                List<com.jnetdata.msp.message.msgConfig.model.Msg> configs = setMsgService.listByIds(configIds);
                if(configs!=null){
                    Map<Long, String> configNames = configs.stream().collect(Collectors.toMap(com.jnetdata.msp.message.msgConfig.model.Msg::getId, com.jnetdata.msp.message.msgConfig.model.Msg::getName));
                    //查询子用户集合
                    List<Long> parentIds = page.getObj().getRecords().stream().map(Msg::getId).collect(Collectors.toList());
                    List<Msg> msgUserList = getService().list(new PropertyWrapper<>(Msg.class).eq("sendID",userId).eq("addressType", 2).in("parentId",parentIds));

                    List<User> users = userService.list(new PropertyWrapper<>(User.class));

                    List<RoleUser> roleUser = roleUserService.list();
                    Map<Long, List<Long>> userRoleIds = roleUser.stream().collect(Collectors.groupingBy(RoleUser::getUserId, Collectors.mapping(RoleUser::getRoleId, Collectors.toList())));
                    List<Long> roleUserIds = roleUser.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
                    List<Role> roles = roleService.listByIds(roleUserIds);
                    Map<Long, String> roleNames = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
                    Map<Long, String> userRoleNames = new HashMap<>();
                    for (Map.Entry<Long, List<Long>> m : userRoleIds.entrySet()) {
                        List<String> nameList = new ArrayList<>();
                        m.getValue().forEach(res -> {
                            if(StringUtils.isNotEmpty(roleNames.get(res))) {
                                nameList.add(roleNames.get(res));
                            }
                        });
                        userRoleNames.put(m.getKey(),String.join(",",nameList));
                    }
                    //查询子用户集合
                    List<GrpUser> grpUsers = grpUserService.list();
                    Map<Long, List<Long>> userGrpIds = grpUsers.stream().collect(Collectors.groupingBy(GrpUser::getUserId, Collectors.mapping(GrpUser::getGroupId, Collectors.toList())));
                    List<Long> grpUserIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
                    List<Groupinfo> groups = groupService.listByIds(grpUserIds);
                    Map<Long, String> groupNames = groups.stream().collect(Collectors.toMap(Groupinfo::getId, Groupinfo::getName));
                    Map<Long, String> userGrpNames = new HashMap<>();
                    for (Map.Entry<Long, List<Long>> m : userGrpIds.entrySet()) {
                        List<String> nameList = new ArrayList<>();
                        m.getValue().forEach(res -> {
                            if(StringUtils.isNotEmpty(groupNames.get(res))){
                                nameList.add(groupNames.get(res));
                            }
                        });
                        userGrpNames.put(m.getKey(),String.join(",",nameList));
                    }

                    //遍历添加
                    users.forEach(res -> {
                        res.setRoleName(userRoleNames.get(res.getId()));
                        res.setGroupName(userGrpNames.get(res.getId()));
                    });

                    Map<Long, List<Long>> userIdMap = msgUserList.stream().collect(Collectors.groupingBy(Msg::getParentId, Collectors.mapping(Msg::getReceptionId, Collectors.toList())));
                    Map<Long, User> usermap = Maps.uniqueIndex(users, User::getId);

                    page.getObj().getRecords().forEach(res -> {
                        res.setMsgSign(configNames.get(res.getConfigId()));

                        List<User> resultUser = new ArrayList<>();

                        List<Long> ids = userIdMap.get(res.getId());
                        if(ids!=null){
                            ids.forEach(id -> {
//                                User user1 = userService.getById(id);
//                                if(user1!=null){
                                    resultUser.add(usermap.get(id));
//                                }
                            });
                            res.setList(resultUser);
                        }else{
                            res.setList(users);
                        }
                    });
                }
            }
        }
        return page;
    }

    @ApiOperation(value = "根据实体属性查询(分页)", notes = "根据vo查询")
    @PostMapping("/sendList")
    @ResponseBody
    public JsonResult<Pager<Msg>> sendList(@RequestBody MsgVo vo) {
        Msg msg = vo.getEntity();
        JsonResult<Pager<Msg>> page = renderError();

        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();

        msg.setSendID(userId);
        msg.setIsDisplay(1);
        msg.setParentId(0L);
        PageVo1 pager = vo.getPager();
        page = doList(getService(), pager, msg);
        List<Msg> msgs = page.getObj().getRecords();
        if(msgs ==null || msgs.isEmpty()){
            return page;
        }
        //查询消息类型
        List<Long> configIds = msgs.stream().map(Msg::getConfigId).collect(Collectors.toList());
        Map<Long, String> configNames = new HashMap<>();
        if(configIds.size()>0){
            List<com.jnetdata.msp.message.msgConfig.model.Msg> configs = setMsgService.listByIds(configIds);
            if(configs!=null){
//                configNames = configs.stream().collect(Collectors.toMap(com.jnetdata.msp.message.msgConfig.model.Msg::getId, com.jnetdata.msp.message.msgConfig.model.Msg::getName));
                configNames = configs.stream().collect(Collectors.toMap(m->m.getId(),m->m.getName()));
            }
        }

        //查询子用户集合
        List<Long> parentIds = msgs.stream().map(Msg::getId).collect(Collectors.toList());
        List<Msg> msgUserList = getService().list(new PropertyWrapper<>(Msg.class).eq("sendID",userId).eq("addressType", 2).in("parentId",parentIds));
//
//        List<User> users = userService.list(new PropertyWrapper<>(User.class));
//
//        List<RoleUser> roleUser = roleUserService.list();
//        Map<Long, List<Long>> userRoleIds = roleUser.stream().collect(Collectors.groupingBy(RoleUser::getUserId, Collectors.mapping(RoleUser::getRoleId, Collectors.toList())));
//        List<Long> roleUserIds = roleUser.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
//        List<Role> roles = roleService.listByIds(roleUserIds);
//        Map<Long, String> roleNames = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
//        Map<Long, String> userRoleNames = new HashMap<>();
//        for (Map.Entry<Long, List<Long>> m : userRoleIds.entrySet()) {
//            List<String> nameList = new ArrayList<>();
//            m.getValue().forEach(res -> {
//                if(StringUtils.isNotEmpty(roleNames.get(res))) {
//                    nameList.add(roleNames.get(res));
//                }
//            });
//            userRoleNames.put(m.getKey(),String.join(",",nameList));
//        }
//
//        List<GrpUser> grpUsers = grpUserService.list();
//        Map<Long, List<Long>> userGrpIds = grpUsers.stream().collect(Collectors.groupingBy(GrpUser::getUserId, Collectors.mapping(GrpUser::getGroupId, Collectors.toList())));
//        List<Long> grpUserIds = grpUsers.stream().map(GrpUser::getGroupId).collect(Collectors.toList());
//        List<Groupinfo> groups = groupService.listByIds(grpUserIds);
//        Map<Long, String> groupNames = groups.stream().collect(Collectors.toMap(Groupinfo::getId, Groupinfo::getName));
//        Map<Long, String> userGrpNames = new HashMap<>();
//        for (Map.Entry<Long, List<Long>> m : userGrpIds.entrySet()) {
//            List<String> nameList = new ArrayList<>();
//            m.getValue().forEach(res -> {
//                if(StringUtils.isNotEmpty(groupNames.get(res))){
//                    nameList.add(groupNames.get(res));
//                }
//            });
//            userGrpNames.put(m.getKey(),String.join(",",nameList));
//        }
//
//        users.forEach(res -> {
//            res.setRoleName(userRoleNames.get(res.getId()));
//            res.setGroupName(userGrpNames.get(res.getId()));
//        });
//
        Map<Long, List<Long>> userIdMap = msgUserList.stream().collect(Collectors.groupingBy(Msg::getParentId, Collectors.mapping(Msg::getReceptionId, Collectors.toList())));
//        Map<Long, User> usermap = Maps.uniqueIndex(users, User::getId);

        for (Msg msg1 : msgs) {
            msg1.setMsgSign(configNames.get(msg1.getConfigId()));
            msg1.setChileIds(userIdMap.get(msg1.getId()));
        }

//            List<User> resultUser = new ArrayList<>();

//            List<Long> ids = userIdMap.get(res.getId());
//            if(ids!=null){
//                ids.forEach(id -> {
//                    resultUser.add(usermap.get(id));
//                });
//                res.setList(resultUser);
//            }else{
//                res.setList(users);
//            }
//        });

        return page;
    }

    /**
     * 通过Excel批量上传数据
     * @param file
     * @return
     * @author hongshou
     * @date 2020/5/26
     * @throws Exception
     */
    @ApiOperation(value = "批量上传", notes = "通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel(MultipartFile file) throws Exception {
//        getService().uploadData(file);
        return renderSuccess(null);
    }

/*    @PostMapping("/sendEmail")
    @ResponseBody
    public JsonResult sendEmail(){
        MailUtil.send(CollUtil.newArrayList("wudishulan@163.com"), "测试", "邮件来自Hutool测试", false);
        return renderSuccess();
    }*/


    @ApiOperation(value = "下载数据", notes = "根据查询条件下载excel数据")
    @RequestMapping("/downloadExcel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        /*   //1 获取数据
         *//*  Msg msg = new Msg();
        msg.setParentId(0L);*//*
        List<Msg> records = service.list(new PropertyWrapper<>(Msg.class).eq("parentId",0L));
        records.forEach(res -> {
            User user = userService.getById(res.getReceptionId());
            if(user!=null){
                res.setSendUserName(userService.getById(res.getSendID()).getName());
                res.setReceptUserName(userService.getById(res.getReceptionId()).getName());
            }
        });

        String fileName = "消息" + System.currentTimeMillis() + ".xls";
        String sheetName = "消息表";
        String[] title = {"标题", "内容","收件人姓名", "发件人姓名", "发送时间"};

        ServletOutputStream os = null;
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setContent("已发消息");
        contentLog.setHandleType("导出");
        contentLog.setContentType("消息");
        contentLog.setCrUser(user.getName());
        contentLog.setCreateTime(new Date());
        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, records, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       /* // swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("发送消息表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        //下载
        EasyExcel.write(response.getOutputStream(), Msg.class).includeColumnFiledNames(Arrays.asList("title", "msgSign", "sendRange", "sendTime","crUser","createTime","modifyUser","modifyTime"))
                .sheet("发送消息表").doWrite(service.getMsgDataList());*/
        List<String> strings = Arrays.asList("title", "msgSign","sendTime","crUser","createTime","modifyUser","modifyTime","roleids","grpids","userids");
        DownEasyExcel(response,service.getMsgDataList(),Msg.class,strings,"发送消息表");
    }

    @ApiOperation(value = "下载数据", notes = "根据查询条件下载excel数据")
    @RequestMapping("/downExcel")
    public void downExcel(HttpServletResponse response) throws Exception {
       /* //获取用户
        MsgVoEntity msgVoEntity = new MsgVoEntity();
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        User byId = userService.getById(userId);
        msgVoEntity.setId(userId);
        msgVoEntity.setEducationbackground(byId.getEducationbackground());//获取教育背景
        msgVoEntity.setName(byId.getName());//获取年龄，
        msgVoEntity.setPoliticsstatus(byId.getPoliticsstatus());
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        List<Long> longs = grpUserService.FindOrganization(userId);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }*/

        MsgVoEntity msgVoEntity = new MsgVoEntity();
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        User byId = userService.getById(userId);
        msgVoEntity.setId(userId);
        msgVoEntity.setEducationbackground(byId.getEducationbackground());//获取教育背景
        msgVoEntity.setName(byId.getTrueName());
        msgVoEntity.setPoliticsstatus(byId.getPoliticsstatus());
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        List<Long> longs = grpUserService.FindOrganization(userId);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        msgVoEntity.setS1(seid);
        List<Msg> inform = msgMapper.getInform(msgVoEntity);
        for(int a=0;a<inform.size();a++) {
            String content = inform.get(a).getContent();
            String title1 = inform.get(a).getTitle();

            // 获取标题字段名称，然后去根据字段名称和表查询标题

            String theTitle = msgMapper.getTheTitleTield(content);
            if(!theTitle.isEmpty()){
                String theTitle1 = msgMapper.getTheTitle(content, theTitle, title1);
                inform.get(a).setTitle(theTitle1);
            }
        }
        List<Msg> informm = msgMapper.getInformm(msgVoEntity);
        inform.addAll(informm);
        List<String> stringss = Arrays.asList("title", "sendUserName", "sendTime", "crUser","createTime", "modifyUser", "modifyTime");
        DownEasyExcel(response,inform,Msg.class,stringss,"消息通知表");
    }

    /*
     * 以Excel表格形式导出消息
     * @author 纪凯静
     * @date 2022/9/23
     * */
    public void DownEasyExcel(HttpServletResponse response, List  data, Class clas, List<String> fieldNames, String excel){
       try {
           response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
           response.setCharacterEncoding("utf-8");
           // 设置字符集格式防止乱码，这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
           String fileName = URLEncoder.encode(excel, "UTF-8").replaceAll("\\+", "%20");
           response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
           //下载
           EasyExcel.write(response.getOutputStream(), clas).includeColumnFiledNames(fieldNames).sheet(excel).doWrite(data);
       }catch (IOException e) {
           e.printStackTrace();
       }
    }

    private MsgService getService() {
        return service;
    }

    /**
     * 发送手消息
     * @param config
     * @param mobs
     * @param msg
     * @param kFlag1
     * @author zike
     * @date 2022/12/9
     */
    public static Boolean SMSsendMsg(PhoneMessageProperties config, String mobs, String msg, String kFlag1) {
        SmsRequestInfo smsRequestInfo = new SmsRequestInfo();
        String tKye = String.valueOf(getTimeStamp());
        smsRequestInfo.setUsername(config.getLoginName());
        String pwd = SecureUtil.md5(SecureUtil.md5(config.getPassWord()) + tKye);
        smsRequestInfo.setPassword(pwd);
        smsRequestInfo.setTKey(tKye);
        smsRequestInfo.setMobile(mobs);
        smsRequestInfo.setContent(msg);
        String result = sendSms(config.getMtUrl(), smsRequestInfo);
        SmsResponse smsResponse = (SmsResponse)fromJson(result, SmsResponse.class);
        if (!Objects.isNull(smsResponse) && smsResponse.getCode() == 200) {
            log.info("smsResponse = {}", result);
            return true;
        } else {
            log.error("smsResponse = {}", result);
            return false;
        }
    }

   /* @ApiOperation(value = "根据实体属性查询", notes = "根据vo查询")
    @PostMapping("/list2")
    @ResponseBody
    public JsonResult<Pager<Msg>> list2(@RequestBody MsgVo vo) {
        Pager pager = vo.getPager().toPager();
        MsgVoEntity msgVoEntity = new MsgVoEntity();
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        User byId = userService.getById(userId);
        msgVoEntity.setCruser(vo.getEntity().getCrUser());
        msgVoEntity.setTitle(vo.getEntity().getTitle());
        msgVoEntity.setId(userId);
        msgVoEntity.setEducationbackground(byId.getEducationbackground());//获取教育背景
        msgVoEntity.setName(byId.getTrueName());
        msgVoEntity.setPoliticsstatus(byId.getPoliticsstatus());
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        List<Long> longs = grpUserService.FindOrganization(userId);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        msgVoEntity.setS1(seid);
        Page<Msg> page = new Page<>(pager.getCurrent(),pager.getSize());
        page =msgMapper.Userpagename(page,msgVoEntity);
        pager.setRecords(page.getRecords());
        pager.setPages((new Long(page.getPages())).intValue());
        pager.setTotal((new Long(page.getTotal())).intValue());
        return renderSuccess(pager);
    }*/

    @ApiOperation(value = "根据实体属性查询", notes = "根据vo查询")
    @PostMapping("/list2")
    @ResponseBody
    public JsonResult<Pager<Msg>> list6(@RequestBody MsgVo vo) {
        Pager pager = vo.getPager().toPager();
        MsgVoEntity msgVoEntity = new MsgVoEntity();
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        User byId = userService.getById(userId);
        msgVoEntity.setCruser(vo.getEntity().getCrUser());
        msgVoEntity.setTitle(vo.getEntity().getTitle());
        msgVoEntity.setId(userId);
        msgVoEntity.setEducationbackground(byId.getEducationbackground());//获取教育背景
        msgVoEntity.setName(byId.getName());
        msgVoEntity.setPoliticsstatus(byId.getPoliticsstatus());
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        List<Long> longs = grpUserService.FindOrganization(userId);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        msgVoEntity.setS1(seid);
        Page<Msg> page = new Page<>(pager.getCurrent(),pager.getSize());

       /* page =msgMapper.Userpagename(page,msgVoEntity);*/
        List<Msg> inform =new ArrayList<>();
        if(msgVoEntity.getS1()!=null&&!msgVoEntity.getS1().isEmpty()) {
            inform = msgMapper.getInform(msgVoEntity);
            for (int a = 0; a < inform.size(); a++) {
                String content = inform.get(a).getContent();
                String title1 = inform.get(a).getTitle();
                // 获取标题字段名称，然后去根据字段名称和表查询标题
                String theTitle = msgMapper.getTheTitleTield(content);
                if (theTitle != null) {
                    String theTitle1 = msgMapper.getTheTitle(content, theTitle, title1);
                    inform.get(a).setTitle(theTitle1);
                }
            }
        }
        List<Msg> informm = msgMapper.getInformm(msgVoEntity);
        inform.addAll(informm);
        if(vo.getEntity().getTitle()!=null||vo.getEntity().getSendUserName()!=null) {
            inform = inform.stream().filter(Msg -> (Msg.getTitle() != null && Msg.getTitle().indexOf(vo.getEntity().getTitle()) > -1)&&(Msg.getCrUser()!=null&&Msg.getCrUser().indexOf(vo.getEntity().getCrUser()) > -1)).collect(Collectors.toList());
        }
        pager.setRecords(inform);
        pager.setPages((new Long(page.getPages())).intValue());
        pager.setTotal((new Long(page.getTotal())).intValue());
        return renderSuccess(pager);
    }

    @ApiOperation(value = "查询有无消息通知", notes = "查询有无消息通知")
    @PostMapping("/listcount")
    @ResponseBody
    public JsonResult<List<Msg>>selectcount(){
        MsgVoEntity msgVoEntity = new MsgVoEntity();
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        User byId = userService.getById(userId);
        msgVoEntity.setId(userId);
        msgVoEntity.setEducationbackground(byId.getEducationbackground());//获取教育背景
        msgVoEntity.setName(byId.getTrueName());
        msgVoEntity.setPoliticsstatus(byId.getPoliticsstatus());
        String birthDate = byId.getBirthDate();//获取出生年月日
        int ageForBirthday=0;
        if(birthDate!=null) {
            ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        List<Long> longs = grpUserService.FindOrganization(userId);
        List<String> strings = longs.stream().map(x -> x + ",").collect(Collectors.toList());
        Long[] L = longs.stream().toArray(Long[]::new);
        String seid="";
        //转换为string类型
        for(int a=0;a<L.length;a++){
            seid+=L[a];
            if(a!=L.length-1){
                seid+=",";
            }
        }
        msgVoEntity.setAgeForBirthday(ageForBirthday);
        msgVoEntity.setS1(seid);
        List<Msg> selectcount = msgMapper.selectcount(msgVoEntity);
        return renderSuccess(selectcount);
    }

}
