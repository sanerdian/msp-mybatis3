package com.jnetdata.msp.log.j_log.service;

import com.jnetdata.msp.log.j_log.model.JLog;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
/**
 * <p>
 * 日志 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-02-02
 */
public interface JLogService extends BaseService<JLog> {
        PropertyWrapper<JLog> makeSearchWrapper(Map<String, Object> condition);
        JLog getEntityAndJoinsById(Long id);
        void getListJoin(List<JLog> list, JLog vo);

        /*
        * 用户添加日志
        * @param request
        * @param handletitle
        * @param handletype
        * @param targetid
        * @param targettype
        * @param errMsg  渲染结果
        * @author hongshou
        * @date 2020/5/26
        * */
        void addLog(HttpServletRequest request,String handletitle,String handletype,Long targetid,String targettitle,String targettype,String errMsg);
        void addLog(HttpServletRequest request,String handletitle,String handletype,Long targetid,String targettitle,String targettype);
        /**
         * 添加用户登录日志
         * @param request
         * @param result
         * @author hongshou
         * @date 2020/5/26
         */
        void addLoginLog(HttpServletRequest request,String result);

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
        void addLogs(HttpServletRequest request, String handletitle, String handletype, String targetid, String[] targettitle, String targettype);
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
        void addLogs(HttpServletRequest request, String handletitle, String handletype, Long[] targetid, String[] targettitle, String targettype);
        /*
        * 添加日志
        * @param request
        * @param handletitle
        * @param targetid
        * @param targettitle
        * @param targettype
        * @param errMsg
        * @author hongshou
        * @date 2020/5/26
        * */
        void addLogs(HttpServletRequest request, String handletitle, String handletype, Long[] targetid, String[] targettitle, String targettype,String errMsg);
        void addLogoutLog(HttpServletRequest request, String result);

        /*
        * 机构管理日志方法
        * @author hongshou
        * @date 2020/5/26
        * */
        void jLog(HttpServletRequest request,String handletitle,String handletype, Object id, Object title);
        void jLog(HttpServletRequest request,String handletitle,String handletype, Object id, Object title, String errMsg);

    JsonResult<Pager<JLog>> selectList(PageVo1 pager, JLog entity);
}
