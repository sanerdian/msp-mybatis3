package com.jnetdata.msp.log.j_log.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.log.j_log.model.JLog;
import com.jnetdata.msp.log.j_log.mapper.JLogMapper;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * <p>
 * 日志 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-02-02
 */
@Service
public class JLogServiceImpl extends BaseServiceImpl<JLogMapper, JLog> implements JLogService {

//    @Autowired


    private String andOr = "and";
    @Override
    public PropertyWrapper<JLog> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<JLog> pw = createWrapperUtil(condition)
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
                                                            .eq("status")
                                                            .eq("companyid")
                                                            .eq("websiteid")
                                                            .eq("columnid")
                                                            .eq("modifyBy")
                                                                                                        .eq("seqencing")
                                                            .eq("modifyTime")
                                                                                                                                                                                                .eq("handletype")
                                                            .eq("userid").like("crUser")
                                                                                                                                                                                                                                            .eq("targetid")
                                                                                                                                                    .ge("handledate")
                                                            .ge("targetdate")
                                                            .eq("result").ge("quitdate")
                                                                                                                                                                    .eq("createBy")
        .getWrapper();
                                                                                                                                                        pw.le(condition.get("handledateBT2")!=null && !condition.get("handledateBT2").equals(""),"handledate",condition.get("handledateBT2"));
                    pw.le(condition.get("targetdateBT2")!=null && !condition.get("targetdateBT2").equals(""),"targetdate",condition.get("targetdateBT2"));
                            andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"handletitle");
        andOr(condition,pw,"username");
        andOr(condition,pw,"truename");
        andOr(condition,pw,"mobile");
        andOr(condition,pw,"ipstr");
        andOr(condition,pw,"targettitle");
        andOr(condition,pw,"targettype");
        andOr(condition,pw,"objjson");
        andOr(condition,pw,"zhaiyao");
        andOr(condition,pw,"remarks");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public JLog getEntityAndJoinsById(Long id){
        JLog entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(JLog entity, JLog vo) {
        if(vo == null) vo = new JLog();
    }

    @Override
    public void getListJoin(List<JLog> list, JLog vo) {
        for (JLog entity : list) {
            getJoin(entity, vo);
        }
    }

    /*
     * 用户添加日志
     * @param request
     * @param handletitle
     * @param handletype
     * @param targetid
     * @param targettype
     * @author hongshou
     * @date 2020/5/26
     * @param errMsg  渲染结果
     * */
    @Override
    public void addLog(HttpServletRequest request, String handletitle, String handletype, Long targetid, String targettitle, String targettype,String result) {
        IUser<Long> user = SessionUser.getCurrentUserWithoutException();
        JLog jLog = new JLog();
        if(user != null){
            jLog.setUserid(user.getId());
            jLog.setUsername(user.getName());
            jLog.setTruename(user.getTrueName());
            jLog.setMobile(user.getMobile());
            if(handletype=="登出"){
                JLog jLog1 = baseMapper.selectOne(new QueryWrapper<JLog>().eq("HANDLETYPE", "登录").eq("USERID", user.getId()).eq("IPSTR", IpUtil.getRequestIp(request)).orderByDesc("HANDLEDATE").last("limit 1"));
                Date handledate = jLog1.getHandledate();
                jLog1.setQuitdate(new Date());
                jLog1.setModifyTime(new Date());
                this.updateById(jLog1);
                return ;
            }
        }
        jLog.setIpstr(IpUtil.getRequestIp(request));
        jLog.setHandletitle(handletitle);
        jLog.setHandletype(handletype);
        jLog.setTargetid(targetid);
        jLog.setTargettitle(targettitle);
        jLog.setTargettype(targettype);
        jLog.setHandledate(new Date());
        jLog.setTargetdate(new Date());
        jLog.setResult(result == null?"成功":"失败");
        jLog.setRemarks(result);
        super.insert(jLog);
    }

    @Override
    public void addLog(HttpServletRequest request, String handletitle, String handletype, Long targetid, String targettitle, String targettype) {
        addLog(request,handletitle,handletype,targetid,targettitle,targettype,null);
    }

   /*
     * 重置密码日志
     * @param request
     * @param handletitle
     * @param targetid
     * @param targettitle
     * @param targettype
     * @param errMsg
     * @author hongshou
    * @date 2020/5/26
     * */
    @Override
    public void addLogs(HttpServletRequest request, String handletitle, String handletype, Long[] targetid, String[] targettitle, String targettype,String result) {
        IUser<Long> user = SessionUser.getCurrentUserWithoutException();
        List<JLog> list = new ArrayList<>();
        for (int i=0;i< targetid.length ; i++) {
            JLog jLog = new JLog();
            if(user != null){
                jLog.setUserid(user.getId());
                jLog.setUsername(user.getName());
                jLog.setTruename(user.getTrueName());
                jLog.setMobile(user.getMobile());
            }
            jLog.setIpstr(IpUtil.getRequestIp(request));
            jLog.setHandletitle(handletitle);
            jLog.setHandletype(handletype);
            jLog.setTargetid(targetid[i]);
            jLog.setTargettitle(targettitle!=null?targettitle[i]:null);
            jLog.setTargettype(targettype);
            jLog.setHandledate(new Date());
            jLog.setTargetdate(new Date());
            jLog.setResult(result == null?"成功":"失败");
            jLog.setRemarks(result);
            list.add(jLog);
        }
        super.insertBatch(list);
    }

    /**
     * 添加用户登录日志
     * @param request
     * @param result
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public void addLoginLog(HttpServletRequest request,String result) {
        addLog(request,"用户登录","登录",null,null,null,result);
    }

    /**
     * 添加日志
     * @param request
     * @param handletitle
     * @param handletype
     * @param targetid
     * @param targettitle
     * @param targettype
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public void addLogs(HttpServletRequest request, String handletitle, String handletype, Long[] targetid, String[] targettitle, String targettype) {
        addLogs(request,handletitle,handletype,targetid,targettitle,targettype,null);
    }
    /**
     * 添加日志
     * @param request
     * @param handletitle
     * @param handletype
     * @param targetid
     * @param targettitle
     * @param targettype
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public void addLogs(HttpServletRequest request, String handletitle, String handletype, String targetid, String[] targettitle, String targettype) {
        Long[] ids = Arrays.stream(targetid.split("," )).map(m -> Long.valueOf(m)).collect(Collectors.toList()).toArray(new Long[]{});
        addLogs(request,handletitle,handletype,ids,targettitle,targettype,null);
    }

    @Override
    public void addLogoutLog(HttpServletRequest request,String result) {
        addLog(request,"用户登出","登出",null,null,null,result);
    }

    /*
    * 机构管理插入日志
    * @author hongshou
    * @date 2020/5/26
    * */
    @Override
    public void jLog(HttpServletRequest request, String handletitle, String handletype, Object id, Object title) {
        addLog(request,handletitle,handletype,id,title,null);
    }

    @Override
    public void jLog(HttpServletRequest request, String handletitle, String handletype, Object id, Object title, String errMsg) {
        addLog(request,handletitle,handletype,id,title,errMsg);
    }

    @Override
    public JsonResult<Pager<JLog>> selectList(PageVo1 pager, JLog entity) {
        if(entity==null){
            QueryWrapper<JLog> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.eq("HANDLETYPE","登录").or().eq("HANDLETYPE","登出");
            List<JLog> jLogs = baseMapper.selectList(objectQueryWrapper);
            Pager pager1 = pager.toPager();
            pager1.setRecords(jLogs);
            pager1.setPages(new Long(pager1.getPages()).intValue());
            pager1.setTotal(new Long(pager1.getTotal()).intValue());
            return JsonResult.success(pager);
        }else {
            QueryWrapper<JLog> queryWrapper = new QueryWrapper<>();
            if(entity.getHandletype()!=null&&entity.getHandletype()!=""){
                queryWrapper.like("HANDLETYPE",entity.getHandletype());
            }
            if(entity.getCreateTime()!=null&&!entity.getCreateTime().equals("")){
                queryWrapper.like("CRTIME",entity.getCreateTime());
            }if(entity.getCrUser()!=null&&!entity.getCrUser().equals("")){
                queryWrapper.like("CRUSER",entity.getCrUser());
            }

            queryWrapper.eq("HANDLETYPE","登录");

            List<JLog> jLogs = baseMapper.selectList(queryWrapper);
            Pager pager1 = pager.toPager();
            pager1.setRecords(jLogs);
            pager1.setPages(new Long(pager1.getPages()).intValue());
            pager1.setTotal(new Long(pager1.getTotal()).intValue());
            return JsonResult.success(pager);
        }
    }

    public void addLog(HttpServletRequest request, String handletitle, String type, Object id, Object title, String errMsg){
        if (id instanceof Long){
            this.addLog(request, type + handletitle, type , (Long) id, title==null?null:String.valueOf(title), handletitle,errMsg);
        }else if(id instanceof Long[]){
            this.addLogs(request, type + handletitle, type , (Long[]) id, title==null?null:(String[])title, handletitle,errMsg);
        }else if(id instanceof String){
            this.addLogs(request, type + handletitle, type , (String) id, null, handletitle);
        }

    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<JLog>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
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
