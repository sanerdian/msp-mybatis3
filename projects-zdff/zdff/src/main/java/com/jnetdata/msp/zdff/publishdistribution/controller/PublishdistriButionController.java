package com.jnetdata.msp.zdff.publishdistribution.controller;


import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.vo.BaseVo;
import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import com.jnetdata.msp.zdff.publishdistribution.model.ftpentity;
import com.jnetdata.msp.zdff.publishdistribution.service.CMSUtil;
import com.jnetdata.msp.zdff.publishdistribution.service.PublishdistriButionService;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSPublishUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Api(description = "站点分发相关接口文档")
@RequestMapping("/zdff/publishdistriBution")
public class PublishdistriButionController extends BaseController<Long,PublishdistriBution> {
    @Autowired
    PublishdistriButionService publishdistriButionService;

//    @Autowired
//    ftpentity pentity;

    public static String getLoginUserName(HttpServletRequest request){
//        Object obj=request.getSession().getAttribute("loginUserName");
        var obj = SessionUser.getCurrentUser();
        if(obj!=null){
            return obj.getName();
//            return obj.toString();
        }
        return "system";
    }

    /**
     * 获取用户Ip
     * @author wangxuetao
     * @param request
     * @return String
     */
    public static String logUserIp(HttpServletRequest request){
        // 获取客户机IP
        String logUserIp = request.getHeader("x-forwarded-for");
        if (logUserIp == null || logUserIp.length() == 0
                || logUserIp.equalsIgnoreCase("unknown")) {
            logUserIp = request.getHeader("Proxy-Client-IP");
        }
        if (logUserIp == null || logUserIp.length() == 0
                || logUserIp.equalsIgnoreCase("unknown")) {
            logUserIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (logUserIp == null || logUserIp.length() == 0
                || logUserIp.equalsIgnoreCase("unknown")) {
            logUserIp = request.getRemoteAddr();
        }
        if(logUserIp.equals("0:0:0:0:0:0:0:1")){
            logUserIp = "127.0.0.1";
        }
        return logUserIp;
    }
    /**
     * 添加站点分发
     *
     * @param entity
     * @return
     * @author mahualong
     */
    @ApiOperation(value = "添加站点分发", notes="添加站点分发")
    @PostMapping("/insertPublishdistri")
    @ResponseBody
    public JsonResult insertPublishdistriBution(HttpServletRequest request, @Validated @RequestBody PublishdistriBution entity) {
        boolean b = false;
        String crUser = getLoginUserName(request);
        entity.setCrTime(new Date());
        try {
            if (entity.getTargetType() == 1) {//sftp检验
                boolean connectServer = sftpsynchronization.connectServer(entity.getTargetServer(), entity.getTargetPort(), entity.getLoginUser(), entity.getLoginPwd());
                if (connectServer) {
                    sftpsynchronization.closeChannel();
                    entity.setCrUser(crUser);
                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "系统连接失败"));
                    return renderError("系统连接失败");
                }
            } else if (entity.getTargetType() == 2) {//系统文件
                String ip = logUserIp(request);
                String dataPath = entity.getDataPath();
                File file = new File(dataPath);
                if (!file.isDirectory() || (dataPath.indexOf('/') == 0)) {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "指定的路径不是一个有效的目录"));
                    return renderError("指定的路径不是一个有效的目录");
                }
                entity.setCrUser(crUser);
                entity.setTargetServer(ip);
            } else if (entity.getTargetType() == 3) {//ftp校验
                /*创建ftp的实体对象*/
                ftpentity pentity = new ftpentity();
                pentity.setIpAddr(entity.getTargetServer());
                pentity.setPort(entity.getTargetPort());
                pentity.setUserName(entity.getLoginUser());
                pentity.setPwd(entity.getLoginPwd());
                boolean connectFtp = ftpsynchronization.connectFtp(pentity);
                if (connectFtp) {
                    ftpsynchronization.closeFtp();
                    entity.setCrUser(crUser);
                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "系统连接失败"));
                    return renderError("系统连接失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        b = publishdistriButionService.insert(entity);
        if(b){
            return renderSuccess("添加成功");
        }else {
            return renderError("添加失败");
        }
    }


    /**
     * 修改站点分发
     *
     * @param entity
     * @return
     * @author mahualong
     */
    @ApiOperation(value = "修改站点分发", notes="修改站点分发")
    @PostMapping("/updatePublishdistri")
    @ResponseBody
    public JsonResult<Void> updatePublishdistri(HttpServletRequest request, @Validated @RequestBody PublishdistriBution entity) {
        boolean b = false;
        PublishdistriBution publishdistriBution=publishdistriButionService.getById(entity.getId());
        publishdistriBution.setTargetType(entity.getTargetType());
//        publishdistriBution.setTargetTypeDesc(entity.getTargetTypeDesc());
        publishdistriBution.setStatus(entity.getStatus());
        publishdistriBution.setTargetServer(null);
        publishdistriBution.setTargetPort(null);
        publishdistriBution.setLoginUser(null);
        publishdistriBution.setLoginPwd(null);
        publishdistriBution.setDataPath(null);

//      String crUser = WebUtil.getLoginUserName(request);
        String crUser = getLoginUserName(request);
        entity.setOperTime(new Date());
        entity.setOperUser(crUser);
        try {
            if (entity.getTargetType() == 1) {//sftp检验
                boolean connectServer = sftpsynchronization.connectServer(entity.getTargetServer(), entity.getTargetPort(), entity.getLoginUser(), entity.getLoginPwd());
                if (connectServer) {
                    sftpsynchronization.closeChannel();

                    publishdistriBution.setTargetServer(entity.getTargetServer());
                    publishdistriBution.setTargetPort(entity.getTargetPort());
                    publishdistriBution.setLoginUser(entity.getLoginUser());
                    publishdistriBution.setLoginPwd(entity.getLoginPwd());
                    publishdistriBution.setDataPath(entity.getDataPath());

                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "修改失败，系统连接失败"));
                    return renderError("修改失败，系统连接失败");
                }
            } else if (entity.getTargetType() == 2) {//系统文件
                String ip = logUserIp(request);
                String dataPath = entity.getDataPath();
                File file = new File(dataPath);
                if (!file.isDirectory() || (dataPath.indexOf('/') == 0)) {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "修改失败，指定的路径不是一个有效的目录"));
                    return renderError("修改失败，指定的路径不是一个有效的目录");
                }
                publishdistriBution.setDataPath(entity.getDataPath());

            } else if (entity.getTargetType() == 3) {//ftp校验
                ftpentity pentity = new ftpentity();
                pentity.setIpAddr(entity.getTargetServer());
                pentity.setPort(entity.getTargetPort());
                pentity.setUserName(entity.getLoginUser());
                pentity.setPwd(entity.getLoginPwd());
                boolean connectFtp = ftpsynchronization.connectFtp(pentity);
                if (connectFtp) {
                    ftpsynchronization.closeFtp();

                    publishdistriBution.setTargetServer(entity.getTargetServer());
                    publishdistriBution.setTargetPort(entity.getTargetPort());
                    publishdistriBution.setLoginUser(entity.getLoginUser());
                    publishdistriBution.setLoginPwd(entity.getLoginPwd());
                    publishdistriBution.setDataPath(entity.getDataPath());

                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "修改失败，系统连接失败"));
                    return renderError("修改失败，系统连接失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//      return buildResponseEntity(publishdistriButionService.updateById(publishdistriBution));
        b = publishdistriButionService.updateById(entity);
        if(b){
            return renderSuccess("修改成功");
        }else {
            return renderError("修改失败");
        }
    }



    /**
     * 测试站点分发
     * @return
     * @author mahualong
     */
    @ApiOperation(value = "查看站点分发", notes="查看站点分发")
    @RequestMapping("/checkPublishdistri/{id}")
    @ResponseBody
    public JsonResult<Void> checkPublishdistri(@PathVariable Long id, HttpServletRequest request) {
//        String crUser = WebUtil.getLoginUserName(request);
        String crUser = getLoginUserName(request);
//        PublishdistriBution entity=publishdistriButionService.selectById(id);
        PublishdistriBution entity=publishdistriButionService.getById(id);
        try {
            if (entity.getTargetType() == 1) {//sftp检验
                boolean connectServer = sftpsynchronization.connectServer(entity.getTargetServer(), entity.getTargetPort(), entity.getLoginUser(), entity.getLoginPwd());
                if (connectServer) {
                    sftpsynchronization.closeChannel();
                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "系统连接失败"));
                    return renderError("系统连接失败");
                }
            } else if (entity.getTargetType() == 2) {//系统文件
                String dataPath = entity.getDataPath();
                File file = new File(dataPath);
                if (!file.isDirectory() || (dataPath.indexOf('/') == 0)) {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "指定的路径不是一个有效的目录"));
                    return renderError("修改失败，指定的路径不是一个有效的目录");
                }
            } else if (entity.getTargetType() == 3) {//ftp校验
                ftpentity pentity = new ftpentity();
                pentity.setIpAddr(entity.getTargetServer());
                pentity.setPort(entity.getTargetPort());
                pentity.setUserName(entity.getLoginUser());
                pentity.setPwd(entity.getLoginPwd());
                boolean connectFtp = ftpsynchronization.connectFtp(pentity);
                if (connectFtp) {
                    ftpsynchronization.closeFtp();
                } else {
//                    return buildResponseEntity(new StorageResult(StatusCodes.OK, false, "系统连接失败"));
                    return renderError("系统连接失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return buildResponseEntity(new StorageResult(StatusCodes.OK, true, "测试成功！！可以连接"));
        return renderSuccess("测试成功！！可以连接");

    }




    /**
     * 传入主键删除和栏目状态进行栏目删除操作,更新栏目的status字段值来改变栏目状态-1普通删除，-2从回收站删除
     *
     * @return ResponseEntity
     * @author liyingnan
     */
    // @ApiOperation(value = "传入id和status选择分发站点是否启用", notes = "传入id和status选择分发站点是否启用")
    // @ApiImplicitParams({
    // 		@ApiImplicitParam(name = "id", value = "编码后的主键id", required = true, paramType ="path",dataType = "Long"),
    // 		@ApiImplicitParam(name = "status", value = "是否启用【1启用0禁用】", required = false, paramType ="path",dataType = "Integer")
    // })
    @ApiOperation(value = "传入主键删除和栏目状态进行栏目删除操作,更新栏目的status字段值来改变栏目状态-1普通删除，-2从回收站删除", notes="传入主键删除和栏目状态进行栏目删除操作,更新栏目的status字段值来改变栏目状态-1普通删除，-2从回收站删除")
    @RequestMapping(value = "/editStatus/{id}/{status}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> editStatus(HttpServletRequest request, HttpServletResponse response,@PathVariable Long id,@PathVariable Integer status) {
//        String parameter = request.getParameter("status");
//        Integer status = CMSUtil.intFormat(request.getParameter("status"));
//        Long id = CMSUtil.longFormat(request.getParameter("id"));
        if (status == null) {
//            Result s = new Result(StatusCodes.ACCEPTED, false, "传入参数错误");
//            return buildResponseEntity(s);
            return renderError("传入参数错误");
        }
        PublishdistriBution entity = new PublishdistriBution();
        entity.setId(id);
        entity.setStatus(status);
        //
//        return buildResponseEntity(publishdistriButionService.editStatus(entity));
        boolean b = publishdistriButionService.updateById(entity);
        if(b){
            return renderSuccess("修改成功");
        }else {
            return renderError("修改失败");
        }
    }
    @ApiOperation(value = "删除站点分发", notes="删除站点分发")
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable Long id, HttpServletResponse response) {
        if (id == null) {
//            Result s = new Result(StatusCodes.ACCEPTED, false, "参数错误");
//            return buildResponseEntity(s);
            return renderError("传入参数错误");
        }
        boolean b = publishdistriButionService.deleteById(id);
        if(b){
            return renderSuccess("删除成功");
        }else {
            return renderError("删除失败");
        }
//        return buildResponseEntity(publishdistriButionService.deleteById(id));
    }

    /*@RequestMapping(value = "/deletes/{ids}", method = RequestMethod.POST)
    public JsonResult<EntityId<Long>> deleteByIds(@PathVariable String ids, HttpServletResponse response) {
        if (ids == null) {
            return renderError("传入参数错误");
        }
        boolean b = publishdistriButionService.deleteBatchIds(ids);
        if(b){
            return renderSuccess("删除成功");
        }else {
            return renderError("删除失败");
        }
//      return buildResponseEntity(publishdistriButionService.deleteByIds(ids.split(",")));
    }
*/
    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "执行批量删除操作站点分发", notes="执行批量删除操作站点分发")
    @DeleteMapping("/deletes/{ids}")
    @ResponseBody
    public JsonResult<Void> deleteBatchIdsreal(@PathVariable("ids") String ids) {
        //删除已发布的
        String[] idss = ids.split(",");
        List<PublishdistriBution> entitys = publishdistriButionService.listByIds(Arrays.asList(idss));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        publishdistriButionService.deleteBatchIds(delIds);
        return doDeleteBatchIds(publishdistriButionService, ids);
    }

    /*@Override
    public ResponseEntity<Object> deleteBySelection(@ModelAttribute PublishdistriBution entity, HttpServletResponse response) {
        return null;
    }*/
    @ApiOperation(value = "更新站点分发", notes="更新站点分发")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Void> updateById(@ModelAttribute PublishdistriBution entity, HttpServletResponse response) {
        if (entity.getSiteid() == null || entity.getStatus() == null || entity.getDataPath() == null || entity.getTargetType() == null) {
//            return buildResponseEntity(new StorageResult(StatusCodes.BAD_REQUEST, false, "字段填写不完整"));
            return renderError("字段填写不完整");
        } else {
//            return buildResponseEntity(publishdistriButionService.insert(entity));
            boolean b = publishdistriButionService.insert(entity);
            if(b){
                return renderSuccess("添加成功");
            }else {
                return renderError("添加失败");
            }
        }
    }

   /* @Override
    public ResponseEntity<Object> selectAll(String body, HttpServletResponse response) {
        return null;
    }*/
   @ApiOperation(value = "通过id站点分发", notes="通过id站点分发")
   @RequestMapping(value = "/selectPublishdistri/{id}", method = RequestMethod.POST)
   @ResponseBody
    public JsonResult<Object> selectById(@PathVariable Long id, HttpServletResponse response) {
        PublishdistriBution entity=publishdistriButionService.getById(id);
//        return ResponseEntity.ok(entity);
        return renderSuccess(entity);
    }


    /*@Override
    public ResponseEntity<Object> selectListBySelection(@ModelAttribute PublishdistriBution entity, HttpServletResponse response) {
        return null;
    }*/

   /* @RequestMapping(value = "/selectPageBySelection", method = RequestMethod.POST)
    public ResponseEntity<Object> selectPageBySelection(@RequestParam(value = "pageNo", required = true) Integer pageNo, @RequestParam(value = "pageSize", required = true) Integer pageSize, @ModelAttribute PublishdistriBution entity, HttpServletResponse response) {
        return renderSuccess(publishdistriButionService.selectPublishdistriButionBySelection(pageNo, pageSize, entity));
    }*/
    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/selectPageBySelection")
    @ResponseBody
    public JsonResult<Pager<PublishdistriBution>> list(@RequestBody BaseVo<PublishdistriBution> vo) {
        if(vo.getEntity() == null) vo.setEntity(new PublishdistriBution());
        vo.getEntity().setDocstatus(0);
        JsonResult<Pager<PublishdistriBution>> result = doList(publishdistriButionService, vo.getPager(), vo.getEntity());
        for (PublishdistriBution record : result.getObj().getRecords()) {
            record.setIds(CMSUtil.stringFormat(record.getId()));
//            record.setCrTime();
            if(vo.getEntity()!=null && vo.getEntity().getColumnid()!=null && !vo.getEntity().getColumnid().equals(record.getColumnid())){
                List<String> split = Arrays.asList(record.getQuotainfo().split(","));
                record.setSynctype(split.contains(vo.getEntity().getColumnid() + ":1")?1:split.contains(vo.getEntity().getColumnid() + ":2")?2:null);
            }
        }
        return result;
    }

    @ApiOperation(value = "站点分发按站点统计", notes="站点分发按站点统计")
    @GetMapping("/findPublishdistriBution/{siteId}")
    @ResponseBody
    public JsonResult<Object> findPublishdistriBution(@PathVariable Long siteId, HttpServletResponse response){
        List<PublishdistriBution> entity = publishdistriButionService.findPublishdistriBution(siteId);
        if(entity.size()>0){
            CMSPublishUtil cmsPublishUtil = new CMSPublishUtil();
            cmsPublishUtil.publishdistriBution(entity,"","",CMSUtil.longFormat("0"),siteId);
        }
        return renderSuccess(entity);
    }
}
