package com.jnetdata.msp.manage.site.controller;

import com.alibaba.excel.EasyExcel;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.publish.core.service.CacheService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.site.vo.SiteVo;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.util.DownMetadataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/26.
 */
@Controller
@RequestMapping("/manage/site")
@ApiModel(value = "站点管理(SiteController)", description = "站点管理")
@Api(tags = "站点管理(SiteController)")
public class SiteController extends BaseController<Long,Site> {
    @Autowired
    private SiteService service;

    @Autowired
    private ContentLogService contentLogService;
    @Autowired
    private ProgramaService programaService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private JLogService jLogService;
    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "添加", notes="站点信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Site entity, HttpServletRequest request) {
        int count = programaService.count(new PropertyWrapper<>(Programa.class).eq("skuNumber",entity.getCode()).eq("status",0));
        int count2 = getService().count(new PropertyWrapper<>(Site.class).eq("code",entity.getCode()).eq("status",0));
        if(count>0 || count2>0){
            jLogService.addLog(request,"添加站点","添加",entity.getId(),entity.getName(),"站点","唯一标识已存在");
            return renderError("唯一标识已存在");
        }
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        jLogService.addLog(request,"添加站点","添加",entity.getId(),entity.getName(),"站点",null);
        cacheService.initSite();
        return result;
    }

    @ApiOperation(value = "修改站点",notes="站点信息")
    @PostMapping({"/updateBatchById"})
    @ResponseBody
    public JsonResult updateBatchById(@RequestBody List<Site> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的站点信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("站点id") @PathVariable("id") Long id, HttpServletRequest request) {
        boolean isPermssion  = SessionUser.getSubject().isPermitted("site:delete:"+id);
        Site site = getService().getById(id);
        if(site.getCreateBy().equals(SessionUser.getCurrentUser().getId())) isPermssion = true;
        if(isPermssion) {
            jLogService.addLog(request,"删除站点","删除",id,site.getName(),"站点",null);
            return doDeleteById(getService(), id);
        }else{
            jLogService.addLog(request,"删除站点","删除",id,site.getName(),"站点","没有权限");
            return renderError("没有权限");
        }
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除站点信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids, HttpServletRequest request) {
        String[] idss = ids.split(",");
        List<Long> idsss = new ArrayList<>();
        for (String s : idss) {
            idsss.add(Long.valueOf(s));
        }
        jLogService.addLogs(request,"删除站点","删除",idsss.toArray(new Long[idsss.size()]),null,"站点",null);
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的站点信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("站点id") @PathVariable("id") Long id,@RequestBody Site entity, HttpServletRequest request) {

        if(entity.getCode() != null){
            int count = programaService.count(new PropertyWrapper<>(Programa.class).eq("skuNumber",entity.getCode()).eq("status",0));
            int count2 = getService().count(new PropertyWrapper<>(Site.class).eq("code",entity.getCode()).ne("id",id).eq("status",0));
            if(count>0 || count2>0){
                return renderError("唯一标识已存在");
            }
        }
        boolean isPermssion  = SessionUser.getSubject().isPermitted("site:edit:"+id);
        Site site = getService().getById(id);
        if(site.getCreateBy().equals(SessionUser.getCurrentUser().getId())) isPermssion = true;
        if(isPermssion){
            jLogService.addLog(request,"修改站点","修改",id,site.getName(),"站点",null);
            return doUpdateById(getService(), id, entity);
        }else{
            jLogService.addLog(request,"修改站点","修改",id,site.getName(),"站点","没有权限");
            return renderError("没有权限");
        }
    }

    @ApiOperation(value = "复制", notes="根据id复制站点")
    @PutMapping(value = "/copySite/{id}")
    @ResponseBody
    public JsonResult<Void> copyById(@ApiParam("站点id") @PathVariable("id") Long id, HttpServletRequest request) {

        Site site=service.getById(id);
        site.setCode(site.getCode()+new Date().getTime());
        site.setId(null);
        service.insert(site);
        jLogService.addLog(request,"复制站点","复制",id,site.getName(),"站点",null);
        return renderSuccess("复制成功");
    }



    @ApiOperation(value = "根据id查询", notes="查看指定站点id对应的站点信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Site> getById(@ApiParam("站点id") @PathVariable("id") Long id) {
        boolean isPermssion  = SessionUser.getSubject().isPermitted("site:view:"+id);
        Site site = getService().getById(id);
        if(site.getCreateBy().equals(SessionUser.getCurrentUser().getId())) isPermssion = true;
        if(isPermssion){
            String templateId = site.getHomeTemplateId();
            if(!StringUtils.isEmpty(templateId)){
                Template template = templateService.getById(templateId);
                site.setHomeTemplateId(template.getTempname());
            }
            return renderSuccess(site);
        }else{
            return renderError("没有权限");
        }
    }

    /**
     * 根据实体属性查询
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Site>> list(@RequestBody SiteVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    /**
     * 查询所有status为0的站点信息
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "查询所有status为0的站点信息", notes="查询所有status为0的站点信息")
    @PostMapping("/all")
    @ResponseBody
    public JsonResult<List<Site>> all() {
        return renderSuccess(getService().list(new PropertyWrapper<>(Site.class).eq("status",0)));
    }

    /**
     * 站点逻辑（假删）
     * @param ids
     * @param status
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "站点逻辑删除状态修改", notes="站点逻辑删除修改")
    @GetMapping("/updateStatus")
    @ResponseBody
    public JsonResult updateStatus(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids,@RequestParam("status")int status){
        return JsonResult.renderSuccess(getService().updateStatus(ids,status));
    }

    /**
     * 获取删除状态
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "获取删除状态信息", notes="获取删除状态信息")
    @PostMapping("/getDelList")
    @ResponseBody
    public JsonResult getDelList(@RequestBody    SiteVo vo){
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    /**
     * 通过Excel批量上传
     * @param file
     * @return
     * @author hongshou
     * @date 2020/5/26
     * @throws Exception
     */
    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
        return JsonResult.renderSuccess(null);
    }

    /**
     * 清空站点回收站数据
     * @return
     * @author hongshou
     * @date 2020/5/26
     * @throws Exception
     */
    @ApiOperation(value = "清空站点回收站", notes="清空站点回收站数据")
    @PostMapping("/del")
    @ResponseBody
    public JsonResult del() throws Exception{
        getService().delete(new PropertyWrapper<>(Site.class).eq("status",1));
        return JsonResult.renderSuccess();
    }

    /**
     * 根据查询条件下载Excel数据
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return JsonResult.renderSuccess(null);

    }

    /**
     * 导出数据
     * @param companyId
     * @param response
     * @author 王树彬
     * @date 2023/2/8
     */
    @ApiOperation("导出数据")
    @GetMapping("/export/{companyId}")
    public void exportExcel(@PathVariable Long companyId,HttpServletResponse response){
        List<Site> records = getService().list(new PropertyWrapper<>(Site.class).eq("status", 0).eq("companyId", companyId));
        DownMetadataUtil.downExcel(1,records,response,Site.class,Arrays.asList("id","companyId","code","name","siteDesc","dataPath","webUrl","webClass","status","siteOrder","homeTemplateId","detailTemplateId","sitestatus","moduleId","moduleName"));
    }

    /**
     * Excel导入数据
     * @param companyId
     * @param mFile
     * @author hongshou
     * @date 2020/5/26
     * @return
     * @throws IOException
     */
    @ApiOperation("Excel导入")
    @PostMapping("/import/{companyId}")
    @ResponseBody
    public JsonResult importExcel(@PathVariable Long companyId,@RequestParam(value = "file",required = true)MultipartFile mFile ) throws IOException {
        List<Site> list = EasyExcel.read(mFile.getInputStream()).sheet(0).head(Site.class).doReadSync();
        List<Site> SiteAll2 = service.list(new PropertyWrapper<>(Site.class).eq("status", 0));
        List<String> codes = SiteAll2.stream().map(m -> m.getCode()).collect(Collectors.toList());
        List<String> codes2 = new ArrayList<>();
        for (Site site : list) {
            String code = site.getCode();
            if(codes.contains(code) || codes2.contains(code)){
                code = code + new Date().getTime();
                codes2.add(code);
                site.setCode(code);
            }
        }
        getService().insertBatch(list);
        return renderSuccess();
    }

    /**
     * 获取站点目录权限
     * @param webclass
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation("获取站点目录数据(权限)")
    @GetMapping("/getSiteTree")
    @ResponseBody
    public JsonResult<List<Site>> getSiteTree(String webclass){
        List<Site> siteList=service.getTreeData(webclass);
        return renderSuccess(siteList);
    }

    /**
     * 获取全部站点目录数据
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation("获取全部站点目录数据")
    @GetMapping("/getAllSiteTree")
    @ResponseBody
    public JsonResult<List<Site>> getAllSiteTree(){
        try {
            List<Site> siteList=service.getAllTreeData();
            return renderSuccess(siteList);
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("查询失败");
        }

    }

    @ApiOperation("获取")
    @GetMapping("/getAllSite")
    @ResponseBody
    public JsonResult<List<Site>> getAllSite1(String webclass){
        List<Site> sites = service.getTreeDataAsList(webclass);
        return  renderSuccess(sites);
    }

    private SiteService getService() {
        return service;
    }

}
