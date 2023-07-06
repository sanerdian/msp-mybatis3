package com.jnetdata.msp.manage.column.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.core.util.StreamUtil;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.publish.service.PublishLogService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.column.vo.ProgramaVo;
import com.jnetdata.msp.manage.column.vo.SetLimitVo;
import com.jnetdata.msp.manage.column.vo.WordVo;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.service.CacheService;
import com.jnetdata.msp.manage.publish.core.service.PublishService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.vo.BaseEntityVo;
import com.jnetdata.msp.vo.BaseVo;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSUtil;
import inout.TextFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by WFJ on 2019/4/26.
 */

@Controller
@RequestMapping("/manage/programa")
@ApiModel(value = "栏目管理(ProgramaController)", description = "栏目管理")
@Api(tags = "栏目管理(ProgramaController")
public class ProgramaController extends BaseController<Long, Programa> {
    @Autowired
    private ProgramaService service;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ContentLogService contentLogService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    private PublishLogService publishLogService;
    @Autowired
    private TableinfoService tableinfoService;
    @Autowired
    private ModuleinfoService moduleinfoService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private PublishService publishService;

    @ApiOperation(value = "添加", notes = "添加栏目")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Programa entity) {
        int count = getService().count(new PropertyWrapper<>(Programa.class).eq("skuNumber", entity.getSkuNumber()).eq("status", 0));
        int count2 = siteService.count(new PropertyWrapper<>(Site.class).eq("code", entity.getSkuNumber()).eq("status", 0));
        if (count > 0 || count2 > 0) {
            return renderError("唯一标识已存在");
        }
        entity.setChnlDataPath(getChnlDataPath(entity));
        getService().updateSeq(entity);

        if(entity.getQuotaid() != null){
            Programa p = getService().getById(entity.getQuotaid());
            entity.setTableId(p.getTableId());
        }

        JsonResult<EntityId<Long>> entityIdJsonResult = doAdd(getService(), entity);
        //日志
        addLog("添加",entityIdJsonResult.isSuccess());
        //模板状态
        templateService.updateUse(new Long[]{entity.getListtpl(), entity.getEdittpl(), entity.getDetailtpl()});

        cacheService.initChannel();

        return entityIdJsonResult;
    }

    public void addLog(String type,Boolean isSuccess){
        contentLogService.addLog(type+"元数据栏目","站群管理","栏目管理",isSuccess);
    }

    private String getChnlDataPath(Programa entity){
        if(entity.getParentId() != null && entity.getParentId() != 0L){
            return getChnlDataPath(getService().getById(entity.getParentId())) + File.separator + entity.getSkuNumber();
        } else {
            return File.separator + siteService.getById(entity.getSiteId()).getCode() + File.separator + entity.getSkuNumber();
        }
    }

    @ApiOperation(value = "删除", notes = "删除指定id对应的栏目信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("栏目id") @PathVariable("id") Long id) {
        JsonResult<Void> voidJsonResult = doDeleteById(getService(), id);
        addLog("删除",voidJsonResult.isSuccess());

        cacheService.initChannel();
        return voidJsonResult;
    }

    @ApiOperation(value = "批量删除", notes = "根据多个id删除栏目信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids) {
        JsonResult<Void> voidJsonResult = doDeleteBatchIds(getService(), ids);
        addLog("删除",voidJsonResult.isSuccess());

        cacheService.initChannel();
        return voidJsonResult;
    }


    @ApiOperation(value = "修改", notes = "修改指定id对应的栏目信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("栏目id") @PathVariable("id") Long id, @RequestBody Programa entity) {
        Programa old = getService().getById(id);
        if(!SessionUser.getSubject().isPermitted("column:edit:"+id) && !old.getCreateBy().equals(SessionUser.getCurrentUser().getId())){
            return renderError("没有权限");
        }

        if(entity.getSkuNumber() != null){
            int count = getService().count(new PropertyWrapper<>(Programa.class).eq("skuNumber",entity.getSkuNumber()).ne("id",id).eq("status",0));
            int count2 = siteService.count(new PropertyWrapper<>(Site.class).eq("code",entity.getSkuNumber()).eq("status",0));
            if(count>0 || count2>0){
                return renderError("唯一标识已存在");
            }
        }

        if(entity.getChnlDataPath() == null){
            entity.setChnlDataPath("");
        }

        if (entity.getChnlOrder() != null){
            if (entity.getParentId() == null) entity.setParentId(old.getParentId());
            if (entity.getSiteId() == null) entity.setSiteId(old.getSiteId());
            getService().updateSeq(entity.getId(),entity.getChnlOrder());
        }

        if(entity.getQuotaid() != null){
            Programa p = getService().getById(entity.getQuotaid());
            entity.setTableId(p.getTableId());
        }

        JsonResult<Void> voidJsonResult = doUpdateById(getService(), id, entity);
        //日志
        addLog("修改",voidJsonResult.isSuccess());
        //模板状态
//        templateService.updateUse(new Long[]{entity.getListtpl(),entity.getEdittpl(),entity.getDetailtpl()});
        JsonResult<Void> voidJsonResult1 = doUpdateById(getService(), id, entity);
        cacheService.initChannel();
        return voidJsonResult1;
    }

    @ApiOperation(value = "排序", notes = "按照栏目id进行排序")
    @GetMapping(value = "/sort/{id}/{sort}")
    @ResponseBody
    public JsonResult<Void> sort(@ApiParam("栏目id") @PathVariable("id") Long id, @ApiParam("排序") @PathVariable("sort") int sort) {
        getService().updateSeq(id,sort);
        return renderSuccess();
    }


    @ApiOperation(value = "复制", notes="根据id复制栏目")
    @PutMapping(value = "/copySite{id}")
    @ResponseBody
    public JsonResult<Void> copyById(@ApiParam("栏目id") @PathVariable("id") Long id) {
        Programa programa=service.getById(id);
        programa.setId(null);
        service.insert(programa);
        //日志
        addLog("复制",true);

        cacheService.initChannel();
        return renderSuccess("复制成功");
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定栏目id对应的栏目信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Programa> getById(@ApiParam("栏目id") @PathVariable("id") Long id) {
        Programa obj = getService().getById(id);
        if (obj.getParentId() != null && obj.getParentId() != 0L) {
            Programa parent = getService().getById(obj.getParentId());
            if(parent!=null){
                obj.setParentName(parent.getName());
            }
        }
        if (obj.getSiteId() != null) {
            Site site = siteService.getById(obj.getSiteId());
            if (site != null) {
                obj.setSiteName(site.getName());
            }
        }
        if (obj.getListtpl() != null) {
            Template t = templateService.getById(obj.getListtpl());
            if (t != null) {
                obj.setListtplName(t.getTempname());
            }
        }
        if (obj.getDetailtpl() != null) {
            Template t = templateService.getById(obj.getDetailtpl());
            if (t != null) {
                obj.setDetailtplName(t.getTempname());
            }
        }
        if (obj.getEdittpl() != null) {
            Template t = templateService.getById(obj.getEdittpl());
            if (t != null) {
                obj.setEdittplName(t.getTempname());
            }
        }

        if(obj.getTableId() != null){
            Tableinfo tableinfo = tableinfoService.getById(obj.getTableId());
            if(tableinfo != null && tableinfo.getOwnerid() != null){
                Moduleinfo moduleinfo = moduleinfoService.getById(tableinfo.getOwnerid());
                if(moduleinfo != null){
                    String tablename = tableinfo.getTablename();
                    String[] strs = tablename.split("_");
                    String[] strsNew = new String[strs.length-1];
                    System.arraycopy(strs, 1, strsNew, 0, strsNew.length);
                    String modelName = moduleinfo.getEnglishname();
                    String metadataUri = "/"+modelName+"/"+String.join("",strsNew).toLowerCase();
                    obj.setMetadataUri(metadataUri);
                }
            }
        }

        return renderSuccess(obj);
    }

    @ApiOperation(value = "根据实体属性查询", notes = "根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Programa>> list(@RequestBody ProgramaVo vo) {
        if(vo.getEntity() == null) vo.setEntity( new Programa() );
        val user = SessionUser.getCurrentUser();
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            Long userId = user.getId();
            ids = permissionService.getUserPermissionIds(userId, "column:list:");

            List<Long> isChild = permissionService.getUserPermissionIds(userId, "column:child:");
            if(isChild != null && !isChild.isEmpty()){
                for (Long aLong : isChild) {
                    List<Programa> allByparent = getService().getAllByparent(aLong);
                    ids.addAll(allByparent.stream().map(m -> m.getId()).collect(Collectors.toList()));
                }
            }

            vo.getEntity().setConditionIds(ids);
        }
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "分类查询", notes = "分类查询")
    @PostMapping("/getColoumTree")
    @ResponseBody
    public JsonResult getSiteListe(@RequestBody Programa entity) {
        return JsonResult.renderSuccess(getService().getColumnList(entity));
    }

    @ApiOperation(value = "栏目逻辑删除状态修改", notes = "栏目逻辑删除修改")
    @GetMapping("/updateStatus")
    @ResponseBody
    public JsonResult updateStatus(@RequestParam("ids") String ids, @RequestParam("status") int status) {
        Long userid = SessionUser.getCurrentUser().getId();
        for (String id : ids.split(",")) {
            boolean isPermssion = SessionUser.getSubject().isPermitted("column:delete:" + id);
            if(!isPermssion) isPermssion = getService().getById(id).getCreateBy().equals(userid);
            if(!isPermssion) return renderError("没有权限");
        }
        JsonResult jsonResult = renderSuccess(getService().updateStatus(ids, status));
        cacheService.initChannel();
        return jsonResult;
    }


    @ApiOperation(value = "根据条件查询子节点", notes = "根据条件查询子节点")
    @GetMapping("/getByparentId")
    @ResponseBody
    public JsonResult getByParentId(@RequestParam("parentId") Long parentId, @RequestParam("type") int type) {
        return JsonResult.renderSuccess(getService().getByParentId(parentId, type));
    }

    @ApiOperation(value = "获取删除状态的数据", notes = "获取删除状态的数据")
    @PostMapping("/getDelList")
    @ResponseBody
    public JsonResult getDelList(@RequestBody ProgramaVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "清空栏目回收站", notes = "清空栏目回收站数据")
    @PostMapping("/delPrograma")
    @ResponseBody
    public JsonResult delPrograma() throws Exception {
        getService().delete(new PropertyWrapper<>(Programa.class).eq("status", 1));
        return JsonResult.renderSuccess();
    }

    @ApiOperation(value = "批量上传", notes = "通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel(MultipartFile file) throws Exception {
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "下载数据", notes = "根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {
        return JsonResult.renderSuccess(null);
    }

    /**
     * 获取栏目树
     * @param webclass
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "获取树")
    @PostMapping("/getTreeData")
    @ResponseBody
    public JsonResult getTreeData(String webclass) {
        val user = SessionUser.getCurrentUser();
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("column:view")) {
            ids = permissionService.getUserPermissionIds(user.getId(), "column:view:");
            List<Programa> list = getService().list(new PropertyWrapper<>(Programa.class).eq("status", 0).eq("createBy", user.getId()));
            if(!list.isEmpty()) {
                List<Long> ids1 = list.stream().map(m -> m.getId()).collect(Collectors.toList());
                ids.addAll(ids1);
            }
            if (ids.size() == 0) {
                ids.add(-1L);
            }
        }
        try {
            List<Site> sites = siteService.getTreeDataAsList(webclass);
            List<Long> siteIds = sites.stream().filter(m -> m.getCompanyId() != 0L).map(Site::getId).collect(Collectors.toList());
            if (siteIds == null || siteIds.size() == 0) {
                siteIds.add(0L);
            }

            List<Programa> programas = new ArrayList<>();
            List<Programa> programas3 = new ArrayList<>();

            if(!(ids.size() == 1 && ids.get(0) == -1L)){
                programas3 = getService().list(
                        new PropertyWrapper<>(Programa.class)
                                .in("siteId", siteIds)
//                            .in(ids != null && ids.size() > 0, "id", ids)
                                .eq("status", 0)
                                .orderBy(new ArrayList<>(Arrays.asList("chnlOrder")),true));
                Map<Long, List<Programa>> collect1 = programas3.stream().collect(Collectors.groupingBy(m -> m.getParentId()));
                Map<Long, Programa> collect2 = programas3.stream().collect(Collectors.toMap(m -> m.getId(),m->m));
                programas3.forEach(menu -> {
                    menu.setChildren1(collect1.containsKey(menu.getId()) ? collect1.get(menu.getId()) : new ArrayList<>());
                });
                if(ids.size() > 0){
                    List<Programa> listsss = new ArrayList<>();
                    ids.forEach(res -> {
                        if(collect2.containsKey(res)) listsss.add(collect2.get(res));
                        if(collect1.containsKey(res)) listsss.addAll(collect1.get(res));
                    });
                    programas = getChildrens(listsss);

                programas = programas.stream().filter(StreamUtil.distinctByKey(m -> m.getId())).collect(Collectors.toList());

                }else{
                    if(collect1.containsKey(0L)) programas = getChildrens(collect1.get(0L));
                }
            }




            List<Long> siteIds1 = new ArrayList<>();
            List<Long> companyIds1 = new ArrayList<>();
            for (Programa item : programas) {
                if (item.getParentId() == 0) {
                    siteIds1.add(item.getSiteId());
                    item.setParentId(item.getSiteId());
                }
                item.setIsSite(2);
            }

//            List<Programa> programas2 = sites.stream().filter(m -> siteIds1.contains(m.getId())).map(c -> {
            Stream<Site> siteStream = sites.stream();
//            if(!siteIds1.isEmpty()) {
//                siteStream = siteStream.filter(m -> siteIds1.contains(m.getId()));
//            }
            List<Programa> programas2 = siteStream.map(c -> {
                Programa p = new Programa();
                companyIds1.add(c.getCompanyId());
                p.setId(c.getId());
                p.setName(c.getName());
                p.setParentId(c.getCompanyId());
                p.setIsSite(c.getIsSite());
                p.setChnlOrder(0);
                p.setSkuNumber(c.getCode());
                return p;
            }).collect(Collectors.toList());

            programas.addAll(programas2);
            return renderSuccess(programas);
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("查询树失败");
        }
    }

    private List<Programa> getChildrens(List<Programa> programas){
        List<Programa> list = new ArrayList<>();
        list.addAll(programas);
        programas.forEach(res -> {
            if(res.getChildren1() != null) list.addAll(getChildrens(res.getChildren1()));
            res.setChildren1(null);
        });
        return list;
    }


    @ApiOperation(value = "异步获取树")
    @GetMapping("/asyncTree")
    @ResponseBody
    public List<Programa> asyncTree(Long userId,Long id) {
        if(userId == null){
            val user = SessionUser.getCurrentUser();
            userId = user.getId();
        }
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            List<Long> isChild = permissionService.getUserPermissionIds(userId, "column:child:");
            if(isChild == null || isChild.isEmpty()){
                ids = permissionService.getUserPermissionIds(userId, "column:list:");
                if (ids.size() == 0) {
                    ids.add(-1L);
                }
            }else{
                ids.addAll(isChild);
                for (Long aLong : isChild) {
                    List<Programa> allByparent = getService().getAllByparent(aLong);
                    ids.addAll(allByparent.stream().map(m -> m.getId()).collect(Collectors.toList()));
                }
            }
        }
        try {

            List<Programa> programas = new ArrayList<>();

            if(ids.size() == 1 && ids.get(0) == -1L){
                return programas;
            }

            List<Long> finalIds = ids;
            Long finalUserId = userId;
            programas = getService().list(
                    new PropertyWrapper<>(Programa.class)
                            .and(finalIds != null && finalIds.size() > 0,i -> i.in( "CHANNELID", finalIds).or().eq("CRNUMBER", finalUserId))
                            .eq("status", 0)
                            .and(i -> i.eq("SITEID",id)
                                    .eq("PARENTID",0)
                                    .or()
                                    .eq("PARENTID",id))
                            .orderBy(new ArrayList<>(Arrays.asList("chnlOrder")),true));
            List<Long> pids = programas.stream().map(m -> m.getId()).collect(Collectors.toList());
            List<Programa> list = getService().list(new PropertyWrapper<>(Programa.class).in("parentId", pids).eq("status", 0).groupBy(Arrays.asList("parentId")).select("parentId"));
            List<Long> idss = list.stream().map(m -> m.getParentId()).collect(Collectors.toList());
            programas.forEach(res -> {
                if(idss.contains(res.getId())){
                    res.setIsParent(true);
                }
            });
            return programas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @ApiOperation(value = "异步获取树")
    @PostMapping("/asyncTreeForSetLimit")
    @ResponseBody
    public List<Map<String, Object>> asyncTreeForSetLimit(@RequestBody SetLimitVo vo) {
        List<Programa> programas = asyncTree(SessionUser.getCurrentUser().getId(), vo.getId());
        List<Map<String, Object>> maps = MapUtil.toListMap(programas);
        if(vo.getChecks()!=null && vo.getChecks().contains("inherit")){
            for (Map<String, Object> map : maps) {
                map.put("checks",vo.getChecks());
            }
        }else{
            List<String> uap = permissionService.getUserPermissionStr(vo.getUserId(),"metadata:");
            Map<String, List<String>> collect = uap.stream().distinct().collect(Collectors.groupingBy(m -> m.split(":")[2],Collectors.mapping(m -> m.split(":")[1],Collectors.toList())));
            for (Map<String, Object> map : maps) {
                String mid = map.get("id").toString();
                if(collect.containsKey(mid)){
                    map.put("checks",collect.get(mid));
                }
            }
        }
        return maps;
    }

  /*  @ApiOperation(value = "异步获取树，并且表要相同")
    @GetMapping("/asyncTreetable")
    @ResponseBody
    public List<Programa> asyncTreetable(Long userId,Long id,Long tableid) {
        if(userId == null){
            val user = SessionUser.getCurrentUser();
            userId = user.getId();
        }
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            List<Long> isChild = permissionService.getUserPermissionIds(userId, "column:child:");
            if(isChild == null || isChild.isEmpty()){
                ids = permissionService.getUserPermissionIds(userId, "column:list:");
                if (ids.size() == 0) {
                    ids.add(-1L);
                }
            }else{
                ids.addAll(isChild);
                for (Long aLong : isChild) {
                    List<Programa> allByparent = getService().getAllByparent(aLong);
                    ids.addAll(allByparent.stream().map(m -> m.getId()).collect(Collectors.toList()));
                }
            }
        }
        try {

            List<Programa> programas = new ArrayList<>();

            if(ids.size() == 1 && ids.get(0) == -1L){
                return programas;
            }

            programas = getService().list(
                    new PropertyWrapper<>(Programa.class)
                            .in(ids != null && ids.size() > 0, "id", ids)
                            .eq("status", 0)
                            .and(i -> i.eq("SITEID",id)
                                    .eq("PARENTID",0)
                                    .or()
                                    .eq("PARENTID",id))
                            .orderBy(new ArrayList<>(Arrays.asList("chnlOrder")),true));
            List<Long> pids = programas.stream().map(m -> m.getId()).collect(Collectors.toList());
            List<Programa> list = getService().list(new PropertyWrapper<>(Programa.class).in("parentId", pids).eq("status", 0).groupBy(Arrays.asList("parentId")).select("parentId"));
            List<Long> idss = list.stream().map(m -> m.getParentId()).collect(Collectors.toList());
            List<Programa> programass = new ArrayList<>();
            programas.forEach(res -> {
                if(idss.contains(res.getId())){
                    res.setIsParent(true);
                }
                if(res.getTableId()==tableid){
                    programass.add(res);
                }
            });
            return programass;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
*/
   @ApiOperation(value = "栏目的引用", notes = "栏目的引用")
   @GetMapping("/citeadd")
    @ResponseBody
    public JsonResult<EntityId<Long>> citeadd(Long id,Long siteid) {
        Programa getselect = getService().getselect(id);
        Programa programa = new Programa();
        BeanUtils.copyProperties(getselect,programa);
        programa.setSiteId(siteid);
        JsonResult<EntityId<Long>> entityIdJsonResult = doAdd(getService(), programa);
        return entityIdJsonResult;
   }

    @ApiOperation(value = "根据父节点获取子树(无权限)")
    @GetMapping("/getTreeData1/{id}")
    @ResponseBody
    public JsonResult getTreeData1(@PathVariable("id") Long id) {
        return renderSuccess(getService().getTreeData(id));
    }

    @ApiOperation("导出数据")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response, Programa programa) {
        val user = SessionUser.getCurrentUser();
        List<Long> ids = new ArrayList<>();
        if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            Long userId = user.getId();
            ids = permissionService.getUserPermissionIds(userId, "column:list:");
            programa.setConditionIds(ids);
        }
       // List<Programa> list = getService().list(new PropertyWrapper<>(Programa.class).eq("PARENTID", programa.getParentId()).eq("status",0));
        List<Programa> records = getService().list(createCondition(programa));
        List<Programa> list = new ArrayList<>();
        List<Programa> selects = getSelect(records,list);

        //查询数据

        String sheetName = "栏目数据";

        String[] title = {"栏目名称", "栏目id", "上级栏目id"};

        String fileName = "栏目" + System.currentTimeMillis() + ".xls";

        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            //设置响应
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            service.upLoadExcel(os, sheetName, title, selects);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public List<Programa> getSelect(List<Programa> records,List<Programa> list) {
        records.forEach(s -> {
            list.add(s);
            Long id = s.getId();
            List<Programa> list1 = getService().list(new PropertyWrapper<>(Programa.class).eq("PARENTID", id).eq("status", 0));
            if (list1 != null) {
               getSelect(list1,list);
            }
        }
        );
        return list;
    }




    @ApiOperation("导入数据")
    @PostMapping("/import")
    @ResponseBody
    public JsonResult importExcel(@RequestParam(value = "file", required = true) MultipartFile file,Long columnId,Long siteId) {

        //文件后缀不是.jpg
        if (!file.getOriginalFilename().contains(".xls") && !file.getOriginalFilename().contains(".xlsx")) {
            return renderError("请上传EXCEL文件");
        }

        if(columnId == null || siteId == null) return renderError("参数错误");

        try {
            List<Programa> programaList = service.readExcel(file,columnId,siteId);

            List<String> skuNumbers = programaList.stream().map(m -> m.getSkuNumber()).collect(Collectors.toList());
            List<Programa> list = getService().list(new PropertyWrapper<>(Programa.class).in("skuNumber", skuNumbers).eq("status", 0).select("SKUNUMBER"));
            if(!list.isEmpty()) return renderError("唯一标识:'"+ list.stream().map(m -> m.getSkuNumber()).collect(Collectors.joining("','")) +"'已存在!");
            getService().insertBatch(programaList);

            return renderSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("导入失败");
        }

    }

    @ApiOperation("查询父栏目和子栏目")
    @PostMapping("/getColumn")
    @ResponseBody
    public JsonResult<List<Programa>> getColumn(Long id) {
        List<Programa> programaList = new ArrayList<>();
        Programa programa = service.getById(id);
        programaList = service.list(new PropertyWrapper<>(Programa.class).eq("parentId", id).eq("status", 0));
        programaList.add(programa);
        return renderSuccess(programaList);
    }


    @ApiOperation("多选栏目接口")
    @PostMapping("/updateColumn")
    @ResponseBody
    public JsonResult<List<Programa>> updateColumn(@RequestBody WordVo vo, Long id) {
        List<Programa> programaList = new ArrayList<>();
        List<Site> siteList = new ArrayList<>();
        List<Long> ids = vo.getIds();
        List<Long> siteIds = vo.getSiteIds();
        Integer tplType = vo.getTplType();
        for (int i = 0; i < ids.size(); i++) {
            Programa p = new Programa();
            p.setId(ids.get(i));
            if(tplType == 1){
                p.setListtpl(id);
            }else if(tplType == 2){
                p.setDetailtpl(id);
            }else if(tplType == 3){
                p.setEdittpl(id);
            }
            programaList.add(p);
        }
        if(!programaList.isEmpty()){
            service.updateBatchById(programaList);
        }
        for (Long siteId : siteIds) {
            Site site = new Site();
            site.setId(siteId);
            site.setHomeTemplateId(id+"");
            siteList.add(site);
        }
        if(!siteList.isEmpty()){
            siteService.updateBatchById(siteList);
        }
        return renderSuccess();
    }


    @ApiOperation("提取关键词")
    @PostMapping("/extractKeywords")
    @ResponseBody
    public List<String> extractKeywords(@RequestBody WordVo vo) {


        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            TextFilter.Client client = new TextFilter.Client(protocol);

            String result  = client.keywords(vo.getStr());
            System.out.println(result);

            System.out.println("==================1done=============================");
            String[] arr =  result.split(",") ;
//
            List<String> list = new ArrayList<>();

            for(String elt:arr) {
               list.add(elt) ;
            }


            return list  ;

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException x) {
            x.printStackTrace();
        }

        return null  ;

    }

    @ApiOperation("提取敏感词")
    @PostMapping("/extractmingganwords")
    @ResponseBody
    public List<String> extractmingganwords(@RequestBody WordVo vo) {


        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            TextFilter.Client client = new TextFilter.Client(protocol);



            String result3 = client.get_mingan(vo.getStr());
            System.out.println(result3);
            transport.close();
//            System.out.println("==================3done=============================");
            String[] arr =  result3.split(",") ;
            List<String> list = new ArrayList<>();

            for(String elt:arr) {
                list.add(elt) ;
            }


            return list  ;


        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException x) {
            x.printStackTrace();
        }

        return null  ;

    }
    @Autowired
    com.jnetdata.msp.zdff.publishdistribution.service.PublishdistriButionService publishdistriButionService;

    @PostMapping(value = "/createHtml")
    @ResponseBody
    @ApiOperation(value = "发布")
    public JsonResult createHtml(Long columnId, HttpServletRequest request) {
        Programa column = getService().getById(columnId);
        if(column == null)  return renderError("栏目不存在！");
        if(StringUtils.isEmpty(column.getChnlDataPath())){
            column.setChnlDataPath(getChnlDataPath(column));
        }
        String os = System.getProperty("os.name");
        if(!os.toLowerCase().startsWith("win")) column.setChnlDataPath(column.getChnlDataPath().replace("\\","/"));

        String dir_pub = configModelService.getPath("dir_pub");
        if(StringUtils.isBlank(dir_pub)) dir_pub = "pub";

        String per_pub = "/"+dir_pub+"/html";

        boolean staticPublic = false;

        List<Template> list = new ArrayList<>();
        String ip = IpUtil.getRequestIp(request);
        String filePath = per_pub+column.getChnlDataPath().replace("\\","/") + "/";
        if(column.getListtpl() != null) {
            Template template = templateService.getById(column.getListtpl());
            if(template != null && template.getTpltype() != null) {
                if( template.getTpltype() == 2){
                    list.add(template);
                }else {
                    staticPublic = true;
                    column.setChnlOutlineTemp(column.getListtpl());
                }
                column.setListUrl(filePath + template.getOutputfilename()+"."+template.getTempext());
                publishLogService.addLog(ip,filePath,template.getOutputfilename()+"."+template.getTempext(),column.getName());
            }
        }
        if(column.getEdittpl() != null) {
            Template template = templateService.getById(column.getEdittpl());
            if(template != null && template.getTpltype() != null && template.getTpltype() == 2) {
                list.add(template);
                column.setEditUrl(filePath + template.getOutputfilename()+"."+template.getTempext());
                publishLogService.addLog(ip, filePath,template.getOutputfilename()+"."+template.getTempext(),column.getName());
            }
        }
        if(column.getDetailtpl() != null) {
            Template template = templateService.getById(column.getDetailtpl());
            if(template != null && template.getTpltype() != null){
                if( template.getTpltype() == 2){
                    list.add(template);
                }else{
                    staticPublic = true;
                    column.setChnlDetailTemp(column.getDetailtpl());
                }
                column.setDetailUrl(filePath + template.getOutputfilename()+"."+template.getTempext());
                publishLogService.addLog(ip, filePath,template.getOutputfilename()+"."+template.getTempext(),column.getName());
            }
        }
        if(list.size() == 0 && !staticPublic) return renderError("栏目没有选择模板!");

        String path = configModelService.getFrontPath()+File.separator+dir_pub+File.separator+"html"+column.getChnlDataPath();
        for (Template template : list) {
//            Velocity.init();
//            VelocityContext context = new VelocityContext();
//            StringWriter stringWriter = new StringWriter();
            String tplHtml = template.getTemphtml();
            String repHtml = "<script>var columnId = '"+ column.getId() +"';</script>";
            tplHtml = tplHtml.replace("</html>",repHtml);
//            Velocity.evaluate(context, stringWriter, "mystring", tplHtml);
            File file = new File(path);
            file.mkdirs();
            String filepath = path + File.separator + template.getOutputfilename()+"."+template.getTempext();
            file = new File(filepath);
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(tplHtml.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            inputstreamtofile(inputStream, file);
            //新增站点分发
            List<com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution> entity = publishdistriButionService.findPublishdistriBution(column.getSiteId());
            if(entity.size()>0){
                com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSPublishUtil cmsPublishUtil = new com.jnetdata.msp.zdff.publishdistribution.service.impl.CMSPublishUtil();
                //E:\fastdevxp\web\pub\html\zkjw\ptcp\sjyzsfw
                ///pub/html/zkjw/ptcp/sjyzsfw/lby.html
                cmsPublishUtil.publishdistriBution(entity,filepath,filePath, CMSUtil.longFormat("0"),column.getSiteId());
            }
        }
        getService().updateById(column);

        if(staticPublic){
            cacheService.initChannel();
            PublishVO publishVO = new PublishVO();
            publishVO.setChnlId(columnId);
            publishVO.setSiteId(column.getSiteId());
            publishVO.setService("all");
//            publishVO.setChnlDocIdList(Arrays.asList(26L));
            publishService.channelPublish(publishVO);
        }
        return renderSuccess();
    }

    public static void inputstreamtofile(InputStream ins, File file){
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("获取导航路径")
    @GetMapping("/getNaviPath/{id}")
    @ResponseBody
    public JsonResult getNaviPath(@PathVariable("id") Long id){
        return getService().getNaviPath(id);
    }

    private ProgramaService getService() {
        return service;
}

}
