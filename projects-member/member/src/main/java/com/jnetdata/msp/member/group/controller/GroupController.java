package com.jnetdata.msp.member.group.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.group.vo.GroupVo;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.var;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/1.
 */
@Controller
@RequestMapping("/member/group")
@ApiModel(value = "组织管理(GroupController)", description = "组织管理")
public class GroupController extends BaseController<Long, Groupinfo> {

    @Autowired
    private GroupService service;
    @Autowired
    private ContentLogClient contentLogService;
    @Autowired
    private GrpUserService grpUserService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JLogService jLogService;

    @ApiOperation(value = "添加", notes="按提供的实体属性添加组织")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Groupinfo entity, HttpServletRequest request) {
        entity.setCreateTime(new Date());
        if(entity.getParentId()!=null && entity.getParentId()!=0){
            Groupinfo grpinfo = service.getById(entity.getParentId());
            Long newCompanyId = grpinfo.getCompanyId();
            entity.setCompanyId(newCompanyId);
        }
        JsonResult<EntityId<Long>> res = doAdd(getService(), entity);
        jLogService.addLog(request,"添加组织","添加",entity.getId(),entity.getName(),"组织");
        return res;
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的组织")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("组织id") @PathVariable("id") Long id,HttpServletRequest request) {
        List<Long> ids = del(id);
        jLogService.addLogs(request,"删除组织","删除",ids.toArray(new Long[ids.size()]),null,"组织");
        return renderSuccess();
    }

    private List<Long> del(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        Groupinfo obj = new Groupinfo();
        obj.setParentId(id);

        grpUserService.delete(new PropertyWrapper<>(GrpUser.class).eq("groupId",id));

        List<Groupinfo> list = getService().list(obj);
        doDeleteById(getService(),id);
        if(list!=null && list.size()>0){
            for(Groupinfo c : list){
                ids.addAll(del(c.getId()));

            }
        }
        return ids;
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除组织")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody Long[] ids,HttpServletRequest request) {
        jLogService.addLogs(request,"删除组织","删除",ids,null,"组织");
        boolean b = getService().deleteBatchIds(Arrays.asList(ids));
        return renderSuccess();
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的组织")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("组件id") @PathVariable("id") Long id,
                                       @RequestBody Groupinfo entity,HttpServletRequest request) {
        jLogService.addLog(request,"修改组织","修改",id,null,"组织");
        return doUpdateById(getService(), id, entity);
    }



    @ApiOperation(value = "修改", notes="修改指定id对应的组织")
    @PostMapping(value = "/editBatch")
    @ResponseBody
    public JsonResult<Void> editBatch(@RequestBody List<Groupinfo> list,HttpServletRequest request) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    @ApiOperation(value = "查看", notes="查看指定组件操作日志id对应的组织")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Groupinfo> getById(@ApiParam("组件操作日志id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @Autowired
    private CompanyInfoService companyInfoService;
    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Groupinfo>> list(@RequestBody GroupVo vo) {

        JsonResult<Pager<Groupinfo>> result = doList(getService(), vo.getPager(), vo.getEntity());
        List<Groupinfo> list = result.getObj().getRecords();
        list.forEach(e ->{
            if(companyInfoService.getById(e.getCompanyId())!=null){
                String name =companyInfoService.getById(e.getCompanyId()).getName();
                e.setCompanyName(name);
            }
        });

        return result;
    }

    @ApiOperation(value = "获取菜单", notes="获取菜单数据")
    @RequestMapping(value = "/tree",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public JsonResult<List<Groupinfo>> tree() {
        return renderSuccess(getService().tree());
    }

    @ApiOperation(value = "获取菜单", notes="获取菜单数据")
    @GetMapping("/tree/{id}")
    @ResponseBody
    public JsonResult<List<Groupinfo>> tree(@PathVariable("id") Long id) {
        return renderSuccess(getService().tree(id));
    }

    @ApiOperation(value = "获取菜单", notes="获取菜单数据")
    @PostMapping("/asyncTreeCompanys")
    @ResponseBody
    public JsonResult<List<Groupinfo>> asyncTreeCompanys() {
        List<Companyinfo> companyinfos = companyInfoService.treeByPermission();
        companyinfos = companyinfos.stream().filter(m -> m.getParentId().equals(0L)).collect(Collectors.toList());
        List<Long> ids = companyinfos.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Groupinfo> list = getService().list(new PropertyWrapper<>(Groupinfo.class).in("companyId", ids).groupBy(Arrays.asList("COMPANYID")).select("COMPANYID ID"));
        List<Long> idss = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Groupinfo> gs = companyinfos.stream().map(c -> {
            Groupinfo p = new Groupinfo();
            p.setId(c.getId());
            p.setName(c.getName());
            p.setParentId(c.getParentId());
            p.setTreeType(0);
            if (idss.contains(p.getId())) {
                p.setIsParent(true);
            }
            return p;
        }).collect(Collectors.toList());
        return renderSuccess(gs);
    }

    @ApiOperation(value = "异步获取树")
    @GetMapping("/asyncTree")
    @ResponseBody
    public List<Groupinfo> asyncTree(Long userId, Long id) {
        if(userId == null){
            val user = SessionUser.getCurrentUser();
            userId = user.getId();
        }
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("group:peer")) {
            ids = permissionService.getUserPermissionIds(userId, "group:peer:");
            if(ids.isEmpty()) return new ArrayList<>();
        }
        List<Long> finalIds = ids;
        Long finalUserId = userId;
        List<Groupinfo> groupinfos = getService().list(
                new PropertyWrapper<>(Groupinfo.class)
                        .and(!ids.isEmpty(), i -> i.in("GROUPID", finalIds).or().eq("CRNUMBER", finalUserId))
                        .and(i -> i.and(j -> j.eq("PARENTID", 0).or().isNull("PARENTID")).eq("companyId",id)
                                .or()
                                .eq("PARENTID", id))
                        );

        List<Companyinfo> cps = companyInfoService.list(new PropertyWrapper<>(Companyinfo.class).eq("parentId", id));
        List<Groupinfo> gs = new ArrayList<>();
        if(!cps.isEmpty()){
            List<Long> ccpids = cps.stream().map(m -> m.getId()).collect(Collectors.toList());
            List<Companyinfo> clist = companyInfoService.list(new PropertyWrapper<>(Companyinfo.class).in("parentId", ccpids).groupBy(Arrays.asList("PARENT_ID")).select("PARENT_ID parentId"));
            List<Long> cpidss = new ArrayList<>();
            if(!clist.isEmpty()){
                List<Long> cidss = clist.stream().map(m -> m.getParentId()).collect(Collectors.toList());
                List<Groupinfo> gplist = getService().list(new PropertyWrapper<>(Groupinfo.class).select("COMPANYID companyId").in("companyId", cidss).and(m -> m.eq("parentid",0).or().isNull("parentid")).groupBy(Arrays.asList("COMPANYID")));
                cpidss = gplist.stream().map(m -> m.getCompanyId()).collect(Collectors.toList());
            }

            List<Long> finalCpidss = cpidss;
            gs = cps.stream().map(c -> {
                Groupinfo p = new Groupinfo();
                p.setId(c.getId());
                p.setName(c.getName());
                p.setParentId(c.getParentId());
                p.setTreeType(0);
                if (finalCpidss.contains(p.getId())) {
                    p.setIsParent(true);
                }
                return p;
            }).collect(Collectors.toList());
        }


        List<Long> pids = groupinfos.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Groupinfo> list = getService().list(new PropertyWrapper<>(Groupinfo.class).in("parentId", pids).groupBy(Arrays.asList("parentId")).select("parentId"));
        List<Long> idss = list.stream().map(m -> m.getParentId()).collect(Collectors.toList());
        groupinfos.forEach(res -> {
            if (idss.contains(res.getId())) {
                res.setIsParent(true);
            }
        });

        if(!gs.isEmpty()) groupinfos.addAll(gs);
        return groupinfos;
    }

    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

    }

    @ApiOperation(value = "获取组织树形数据", notes="获取组织树形数据")
    @GetMapping("/getGroupTree")
    @ResponseBody
    public JsonResult<List<Groupinfo>> getGroupTree(){
        return renderSuccess(getService().getGroupTree());
    }

    private GroupService getService() {
        return service;
    }
}
