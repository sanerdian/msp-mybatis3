package com.jnetdata.msp.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.mapper.PushMapper;
import com.jnetdata.msp.media.service.PushService;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
import com.jnetdata.msp.media.vo.MsgVo;
import com.jnetdata.msp.media.vo.PushCondition;
import com.jnetdata.msp.media.vo.PushSettingVo;
import com.jnetdata.msp.media.vo.push.SimpleWorkerVo;
import com.jnetdata.msp.media.vo.push.TreeNodeVo;
import com.jnetdata.msp.media.vo.push.UserTagQueryVo;
import com.jnetdata.msp.message.msg.mapper.MsgMapper;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.message.msgConfig.mapper.SetMsgMapper;
import com.jnetdata.msp.tlujy.integral.model.Integral;
import com.jnetdata.msp.tlujy.investigate.mapper.InvestigateMapper;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate_user.mapper.InvestigateUserMapper;
import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_user.mapper.XinwenUserMapper;
import com.jnetdata.msp.tlujy.xinwen_user.model.XinwenUser;
import com.jnetdata.msp.vo.BaseVo;
import lombok.var;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.*;

@Service
public class PushServiceImpl implements PushService {
    @Autowired
    private Xinwen020Mapper xinwen020Mapper;
    @Autowired
    private ProgramaMapper programaMapper;
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private SetMsgMapper setMsgMapper;
    @Autowired
    private InvestigateMapper investigateMapper;
    @Autowired
    private InvestigateUserMapper investigateUserMapper;
    @Autowired
    private XinwenUserMapper xinwenUserMapper;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private UserImportService userImportService;

    @Override
    public void saveXinwenPush(PushSettingVo vo) {
        Xinwen020 xinwen020 = new Xinwen020();
        xinwen020.setId(vo.getId());

        if(vo.getUsers()!=null&&vo.getUsers().size()>0){
            xinwen020.setPushtouser(JSON.toJSONString(vo.getUsers()));
        }
//        if(vo.getGroups()!=null&&vo.getGroups().size()>0){
//            xinwen020.setPushtogroup(JSON.toJSONString(vo.getGroups()));
//
//            String dwbsm="";
//            String bmbm="";
//
//            for ( TreeNodeVo treeNodeVo  : vo.getGroups()) {
//                if(treeNodeVo.getType().equals(TreeNodeVo.TYPE_XSJG)){//下属机构
//                    bmbm+="!"+treeNodeVo.getId()+"!,";
//                }else{
//                    dwbsm+="!"+treeNodeVo.getId()+"!,";
//                }
//            }
//
//            xinwen020.setDwbsm(dwbsm);
//            xinwen020.setBmbm(bmbm);
//
//            for (PushCondition pushCondition : vo.getConditions()) {
//                if("xm".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setXm(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("sex".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setSex(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("age".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    //定义最大为150
//                    int max = 150;
//                    int min = 0;
//                    String age="";
//                    //介于期间
//                    if("between".equals(pushCondition.getOperator().toString())){
//
//                        for (int i = Integer.valueOf(pushCondition.getValue().toString()); i <= Integer.valueOf(pushCondition.getValue2().toString()); i++) {
//                            age+="!"+i+"!,";
//                        }
//
//                    }else {
//                        if("=".equals(pushCondition.getOperator().toString())){//等于
//                            min = Integer.valueOf(pushCondition.getValue().toString());
//                            max = Integer.valueOf(pushCondition.getValue().toString());
//                        }else if(">".equals(pushCondition.getOperator().toString())){//大于
//                            min = Integer.valueOf(pushCondition.getValue().toString())+1;
//                        }else if(">=".equals(pushCondition.getOperator().toString())){//大于等于
//                            min = Integer.valueOf(pushCondition.getValue().toString());
//                        }else if("<".equals(pushCondition.getOperator().toString())){//小于
//                            max = Integer.valueOf(pushCondition.getValue().toString())-1;
//                        }else if("<=".equals(pushCondition.getOperator().toString())){//小于等于
//                            max = Integer.valueOf(pushCondition.getValue().toString());
//                        }
//
//                        for (int i = min; i <= max; i++) {
//                            age+="!"+i+"!,";
//                        }
//                    }
//
////                    xinwen020.setAge(age);
//                    continue;
//                }
//
//                if("xwhcdhz".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setXwhcdhz(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("sxzy".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setSxzy(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("zzmm".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setZzmm(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("zwmc".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setZwmc(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("xjszwmc".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setXjszwmc(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("jsdj".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setJsdj(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("szbz".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setSzbz(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("bzzlx".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setBzzlx(pushCondition.getValue().toString());
//                    continue;
//                }
//
//                if("gz".equals(pushCondition.getField())&&pushCondition.getValue()!=null){
//                    xinwen020.setGz(pushCondition.getValue().toString());
//                    continue;
//                }
//            }
//
//        }
        if(vo.getConditions()!=null&&vo.getConditions().size()>0){
            xinwen020.setPushtocondition(JSON.toJSONString(vo.getConditions()));
        }

        if((vo.getUsers()!=null&&vo.getUsers().size()>0)||(vo.getConditions()!=null&&vo.getConditions().size()>0)||(vo.getGroups()!=null&&vo.getGroups().size()>0)){
            xinwen020.setPushtorange(vo.getRange());
            xinwen020.setTuijianzhe(getDwmc());
            xinwen020.setPushsql(getPushedWorkers(vo));
        }else{
            xinwen020.setPushtogroup("");
            xinwen020.setPushtouser("");
            xinwen020.setPushtocondition("");
            xinwen020.setPushtorange("");
            xinwen020.setTuijianzhe("");
            xinwen020.setPushsql("");
        }
        xinwen020Mapper.updateById(xinwen020);
        saveMsg(vo);
    }

    @Override
    public void saveSurveyPush(PushSettingVo vo) {
        Investigate investigate = new Investigate();
        investigate.setId(vo.getId());
        if(vo.getUsers()!=null){
            investigate.setPushtouser(JSON.toJSONString(vo.getUsers()));
        }
        if(vo.getGroups()!=null){
            investigate.setPushtogroup(JSON.toJSONString(vo.getGroups()));
        }
        if(vo.getConditions()!=null){
            investigate.setPushtocondition(JSON.toJSONString(vo.getConditions()));
        }
        investigate.setPushtorange(vo.getRange());
        investigate.setTuijianzhe(getDwmc());
        investigate.setPushsql(getPushedWorkers(vo));
        investigateMapper.updateById(investigate);

    }
    private Lz_WorkerVo getCurrentWorker(){
        Object obj= SecurityUtils.getSubject().getSession().getAttribute("userinfo");

        if(obj!=null){
            Lz_WorkerVo user= (Lz_WorkerVo)obj ;
            return user;
        }else {
            return null;
        }
    }
    private String getDwmc(){
        Lz_WorkerVo worker = userImportService.getInfobyUserid(SessionUser.getCurrentUser().getId());
        return worker==null?"":worker.getDwmc();
    }
    private Long getCurrentUserId() {//todo 尝试获取用户id，失败则用默认值。测试时临时使用。正式功能时，非登陆用户应该抛异常，让其登陆
        try{
            var currentUser = SessionUser.getCurrentUser();
            return currentUser.getId();
        }catch (Exception e){
            return 0L;
        }
    }
    @Override
    public List<TreeNodeVo> loadDwRoot(String id) {
        List<TreeNodeVo> treeNodeVos = pushMapper.loadDwRoot(id);
        for(TreeNodeVo treeNodeVo : treeNodeVos){
            treeNodeVo.setType(TreeNodeVo.TYPE_DW);
        }
        return treeNodeVos;
    }

    @Override
    public List<TreeNodeVo> loadDwByParent(String pid) {
        List<TreeNodeVo> treeNodeVos =new ArrayList<>();
        List<TreeNodeVo> dws =  pushMapper.listDwByFdwbsm(pid);
        for(TreeNodeVo vo:dws){
            vo.setType(TreeNodeVo.TYPE_DW);
            treeNodeVos.add(vo);
        }
        List<TreeNodeVo> jgs = pushMapper.listJgByDwbsm(pid);
        for(TreeNodeVo vo:jgs){
            vo.setType(TreeNodeVo.TYPE_XSJG);
            treeNodeVos.add(vo);
        }
        return treeNodeVos;
    }

    @Override
    public List<TreeNodeVo> loadDwByName(String pid, String name) {
        List<TreeNodeVo> treeNodeVos =new ArrayList<>();
        List<TreeNodeVo> dws = pushMapper.listDwByDwmc(pid,name);
        for(TreeNodeVo vo:dws){
            vo.setType(TreeNodeVo.TYPE_DW);
            treeNodeVos.add(vo);
        }
        List<TreeNodeVo> jgs =pushMapper.listJgByBmmc(pid,name);
        for(TreeNodeVo vo:jgs){
            vo.setType(TreeNodeVo.TYPE_XSJG);
            treeNodeVos.add(vo);
        }
        return treeNodeVos;
    }

    @Override
    public Pager<SimpleWorkerVo> pageWorker(BaseVo<SimpleWorkerVo> vo) {
        Pager<SimpleWorkerVo> result = new Pager<>();
        int total=pushMapper.countWorker(vo.getEntity());
        result.setTotal(total);
        int current=vo.getPager().getCurrent();
        int size=vo.getPager().getSize();
        int start=(current-1)*size+1;
        int end=current*size;
        List<SimpleWorkerVo> records=pushMapper.listWorker(vo.getEntity(),start,end);
        result.setRecords(records);
        return result;
    }

    @Override
    public Map<String, List<String>> queryUserTags(UserTagQueryVo vo) {
        Map<String, List<String>> result = new HashMap<>();
        List<TreeNodeVo> bms = vo.getBms();
        List<String> bmbms=new ArrayList<>();
        List<String> dwbsms=new ArrayList<>();
        for(TreeNodeVo node:bms){
            if(node.getType().equals(TreeNodeVo.TYPE_XSJG)){
                bmbms.add(node.getId());
            }else {
                dwbsms.add(node.getId());
            }
        }
        List<String> tagNames = vo.getTagNames();
        for(String tagName:tagNames){
            List<String> list=pushMapper.distinctWorkerField(tagName,toInStr(bmbms),toInStr(dwbsms));
            result.put(tagName,list);
        }
        return result;
    }
    //todo 暂时没考虑数字
    private String toInStr(Collection ids) {
        if(ObjectUtils.isEmpty(ids)){
            return null;
        }else {
            StringBuffer buffer=new StringBuffer();
            for(Object id:ids){
                buffer.append(",'").append(id).append("'");
            }
            if(buffer.length()>0){
                return buffer.substring(1);
            }else {
                return null;
            }
        }
    }

    @Override
    public String getPushedWorkers(PushSettingVo vo){
       /* List<SimpleWorkerVo> workerVos=new ArrayList<>();
        workerVos.addAll(vo.getUsers());//添加单独选择的人员*/
        if(vo.getGroups()!=null&&vo.getGroups().size()>0){
            //用推送的部门及标签获取推送的人员
            Set<String> bmbms=new HashSet<>();
            Set<String> dwbsms=new HashSet<>();
            for(TreeNodeVo group:vo.getGroups()){
                if(group.getType().equals(TreeNodeVo.TYPE_DW)){
                    dwbsms.add(group.getId());
                }else {
                    bmbms.add(group.getId());
                }
            }
            StringBuffer buffer = new StringBuffer();
            if(dwbsms.size()>0){
                buffer.append(" and dwbsm in (").append(toInStr(dwbsms)).append(")");
            }
            if(bmbms.size()>0){
                buffer.append(" and bmbm in (").append(toInStr(bmbms)).append(")");
            }
            if(!ObjectUtils.isEmpty(vo.getConditions())){
                for(PushCondition condition:vo.getConditions()){
                    String field=condition.getField();
                    String operator=condition.getOperator();
                    if(field.equals("sex")){
                        int sex="男".equals(condition.getValue())?1:0;//单号是男，双号是女
                        buffer.append(" and mod(substr(sfzh,15,1),2)=").append(sex).append(" ");
                    }else if(field.equals("age")){
                        //todo 简化计算，用年龄计算得到的日期只计算到年
                        buffer.append(" and substr(sfzh,4,4) ").append(operator);
                        if(operator.equals("between")){
                            buffer.append(getBirthYear(condition.getValue())).append(" and ").append(getBirthYear(condition.getValue2()));
                        }else {
                            buffer.append(getBirthYear(condition.getValue()));
                        }
                    }else {
                        buffer.append(" and ").append(field).append(" ").append(operator).append(" ");
                        if(operator.equals("like")){
                            buffer.append("'%").append(condition.getValue()).append("%'");
                        }else if(operator.equals("in")){
                            List list= (List) condition.getValue();
                            buffer.append("(").append(toInStr(list)).append(")");
                        }else if(operator.equals("between")){
                            buffer.append(condition.getValue()).append(" and ").append(condition.getValue2());
                        }else {
                            buffer.append(condition.getValue());
                        }
                    }
                }
            }

            return buffer.toString();
            //List<SimpleWorkerVo> users=pushMapper.queryWorker(buffer.toString());
            //workerVos.addAll(users);
        }
        return "";
    }
    /**得到生日的年份*/
    private int getBirthYear(Object age){
        if(ObjectUtils.isEmpty(age)||!(age instanceof Number)){
            return -1;
        }
        int num=((Number) age).intValue();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR,-1*num);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
    /*private void saveXinwenUser(PushSettingVo vo){
        //首先判断是否已为该新闻保存过推送名单，如果保存过则先删除旧的名单
        PropertyWrapper wrapper =new PropertyWrapper(XinwenUser.class);
        Long docid=vo.getId();
        wrapper.eq("docid",docid);
        Integer count = xinwenUserMapper.selectCount(wrapper.getWrapper());
        if(count!=null&&count>0){
            xinwenUserMapper.delete(wrapper.getWrapper());
        }
        //保存推送名单
        List<SimpleWorkerVo> users=getPushedWorkers(vo);
        for(SimpleWorkerVo user:users){
            XinwenUser xinwenUser = new XinwenUser();
            xinwenUser.setIspush(1);
            xinwenUser.setDocid(docid);
            xinwenUser.setUserid(user.getExternalid());
            xinwenUser.setUsername(user.getXm());
            xinwenUser.setGroupid(user.getDwbsm());
            xinwenUser.setGroupname(user.getDwmc());
            xinwenUserMapper.insert(xinwenUser);
        }
    }*/
    /**
     * 利用推送信息，获取当前问卷的推送名单，并将名单保存到：JMETA_INVESTIGATE_USER
     * @param vo
     */
    /*private void saveInvestigateUser(PushSettingVo vo) {
        //首先判断是否已为该问卷保存过推送名单，如果保存过则先删除旧的名单
        PropertyWrapper wrapper= new PropertyWrapper(InvestigateUser.class);
        Long investigateid = vo.getId();
        wrapper.eq("investigateid", investigateid);
        Integer count = investigateUserMapper.selectCount(wrapper.getWrapper());
        if(count!=null&&count>0){
            investigateUserMapper.delete(wrapper.getWrapper());
        }
        List<SimpleWorkerVo> users=getPushedWorkers(vo);
        for(SimpleWorkerVo user:users){
            InvestigateUser investigateUser = new InvestigateUser();
            investigateUser.setIspush(1);
            investigateUser.setRecoverstatus(0);//回收状态：未回收！
            investigateUser.setInvestigateid(investigateid);
            investigateUser.setUserid(user.getExternalid());
            investigateUser.setUsername(user.getXm());
            investigateUser.setGroupid(user.getDwbsm());
            investigateUser.setGroupname(user.getDwmc());
            investigateUserMapper.insert(investigateUser);
        }
    }*/
//
//    @Override
//    public List<Groupinfo> groupTree4CurrentUser() {
//        return userComponent.groupTree4CurrentUser();
//    }
//
    private void saveMsg(PushSettingVo vo) {
        Long configId;
        String content;
        String title;
        MsgVo template = vo.getMsg();
        if(template!=null){
            configId=template.getConfigId();
            content=template.getContent();
            title=template.getTitle();
        }else {
            configId=getConfigId();

            content=(getDwmc())+"向您推送了新闻："+getXinwenLink(vo.getId());
            title="推送新闻";
        }
        Msg parentMsg = new Msg();
        parentMsg.setParentId(0L);
        parentMsg.setConfigId(configId);
        parentMsg.setContent(content);
        parentMsg.setTitle(title);
        parentMsg.setSendID(getCurrentUserId());
        parentMsg.setSendTime(new Date());
        parentMsg.setReceptionId(null);
        parentMsg.setIsDisplay(1);// todo 该值如何设置（MsgController.addSendByIds中此处值为0） //数据库中备注：是否显示，0不显示，1显示（组织角色群发已发不显示）-1为假删除
        parentMsg.setSendRange(vo.getRange());

        msgMapper.insert(parentMsg);
        if(vo.getUsers()!=null&&vo.getUsers().size()>0){
            parentMsg.setAddressType(2);
            msgMapper.updateById(parentMsg);
            for(SimpleWorkerVo user:vo.getUsers()){
                Msg msg= new Msg();
                msg.setId(null);
                msg.setParentId(parentMsg.getId());
                msg.setConfigId(configId);
                msg.setContent(content);
                msg.setTitle(title);
                msg.setSendID(getCurrentUserId());
                msg.setSendTime(new Date());
//                msg.setReceptionId(user.getId());
                msg.setAddressType(2);//0组织，1角色，2用户
                msg.setIsDisplay(0);// todo 该值如何设置（MsgController.addSendByIds中此处值为3） //数据库中备注：是否显示，0不显示，1显示（组织角色群发已发不显示）-1为假删除
                msgMapper.insert(msg);
            }
        }
        if(vo.getGroups()!=null&&vo.getGroups().size()>0){//todo 目前先不考虑组织的进一步过滤，只考虑组织
            List<TreeNodeVo> groups = vo.getGroups();
            for(TreeNodeVo group:groups){
                Msg msg= new Msg();
                msg.setId(null);
                msg.setParentId(parentMsg.getId());
                msg.setConfigId(configId);
                msg.setContent(content);
                msg.setTitle(title);
                msg.setSendID(getCurrentUserId());
                msg.setSendTime(new Date());
//                msg.setReceptionId(group.getId());
                msg.setAddressType(0);//0组织，1角色，2用户
                msg.setIsDisplay(0);// todo 该值如何设置（MsgController.addSendByIds中此处值为1） //数据库中备注：是否显示，0不显示，1显示（组织角色群发已发不显示）-1为假删除
                msgMapper.insert(msg);
            }
        }
    }

    private Long getConfigId(){
        Long configId=null;
        HashMap map = new HashMap<>();
        map.put("ctype",9);
        map.put("ckey","系统消息");
        List msgs = setMsgMapper.selectByMap(map);
        if(msgs==null||msgs.size()<1){//尝试获取默认的消息类型失败，则提示之
            throw new RuntimeException("请设置消息类型！");
        }else {
            com.jnetdata.msp.message.msgConfig.model.Msg msg = (com.jnetdata.msp.message.msgConfig.model.Msg) msgs.get(0);
            configId= msg.getId();
        }
        return configId;
    }

    private String getXinwenLink(Long docid){
        Xinwen020 xinwen020 = xinwen020Mapper.selectById(docid);
        Programa programa = programaMapper.selectById(xinwen020.getColumnid());
        return "<a target='blank' href='"+programa.getDetailUrl()+"'>"+xinwen020.getTitle()+"</a>";
    }

}
