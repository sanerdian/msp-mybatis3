package com.jnetdata.msp.manage.template.controller;

import com.alibaba.excel.EasyExcel;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.log.template.model.TemplateLog;
import com.jnetdata.msp.log.template.service.TemplateLogService;
import com.jnetdata.msp.manage.publish.core.service.CacheService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import com.jnetdata.msp.manage.template.vo.TemplateVo;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.util.DownMetadataUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.poi.ss.SpreadsheetVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/manage/template")
@ApiModel(value = "模板管理(TemplateController)", description = "模板管理")
public class TemplateController extends BaseController<Long,Template> {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private TemplateLogService templateLogService;
    @Autowired
    private CacheService cacheService;

    private TemplateService getService(){
        return  templateService;
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Template>> list(@RequestBody TemplateVo vo) {
        PropertyWrapper propertyWrapper = getService().makeSearchWrapper(createCondition(vo.getEntity()));
        propertyWrapper.select("TEMPID id","TEMPNAME tempname","TEMPDESC tempdesc","SITEID siteid","TPLTYPE tpltype","TEMPTYPE temptype","MODIFY_USER modifyUser","CRUSER crUser","CRTIME createTime","MODIFY_TIME modifyTime");
        Pager list = getService().list(vo.getPager().toPager(), propertyWrapper);
        return renderSuccess(list);
    }

    @ApiOperation(value = "添加", notes="添加菜单信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Template entity,HttpServletRequest request) {
        val user = SessionUser.getCurrentUser();
        int count = getService().count(new PropertyWrapper<>(Template.class).eq("tempname", entity.getTempname()).eq("siteId",entity.getSiteid()));
        if(count > 0 ){
            return renderError("模板已经存在");
        }
        entity.setModifyUser(user.getName());
        entity.setModifyTime(new Date());
        Site byId = siteService.getById(entity.getSiteid());
        String name = byId.getName();

        templateLogService.addLog("添加",entity.getTempname(),entity.getTemptype(), IpUtil.getRequestIp(request),name);
        JsonResult<EntityId<Long>> entityIdJsonResult = doAdd(getService(), entity);
        cacheService.initTemplate();
        return entityIdJsonResult;
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的菜单信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("菜单id") @PathVariable("id") Long id,HttpServletRequest request) {
        Template entity = getService().getById(id);
        Site byId = siteService.getById(entity.getSiteid());
        String name = byId.getName();
        templateLogService.addLog("删除",entity.getTempname(),entity.getTemptype(),IpUtil.getRequestIp(request),name);
        JsonResult<Void> voidJsonResult = doDeleteById(getService(), id);
        cacheService.initTemplate();
        return voidJsonResult;
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除菜单信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") @ApiParam("多个id用逗号隔开")String ids, HttpServletRequest request) {
        val user = SessionUser.getCurrentUser();
        String ipAddress = IpUtil.getRequestIp(request);
        List<Template> templates = getService().listByIds(Arrays.asList(ids.split(",")));
        List<TemplateLog> logs = new ArrayList<>();
        for (Template template : templates) {
            int temptype = template.getTemptype();
            TemplateLog log = new TemplateLog();
            log.setCrUser(user.getName());
            log.setCreateBy(user.getId());
            log.setAddress(ipAddress);
            log.setTemplateName(template.getTempname());
            log.setTemplateType(temptype==1?"概览模板":temptype==2?"概览模板":temptype==3?"概览模板":"其他");
            log.setHandleType("删除模板");
            log.setHandleResult(0L);
            Site byId = siteService.getById(template.getSiteid());
            String name = byId.getName();
            log.setColumnName(name);
            logs.add(log);
        }
        templateLogService.insertBatch(logs);
        JsonResult<Void> voidJsonResult = doDeleteBatchIds(getService(), ids);
        cacheService.initTemplate();
        return voidJsonResult;
    }

    @ApiOperation(value = "修改", notes="修改指定id对应的菜单信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("菜单id") @PathVariable("id") Long id, @RequestBody Template entity,HttpServletRequest request) {
        Template entityr = getService().getById(id);
        Site byId = siteService.getById(entityr.getSiteid());
        String name = byId.getName();
        templateLogService.addLog("修改",entity.getTempname(),entity.getTemptype(),IpUtil.getRequestIp(request),name);
        entity.setLastmodifiedtime(new Date());
        JsonResult<Void> voidJsonResult = doUpdateById(getService(), id, entity);
        cacheService.initTemplate();
        return voidJsonResult;
    }

    @ApiOperation(value = "根据id查询", notes="查看指定菜单id对应的菜单信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Template> getById(@ApiParam("菜单id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }


    @ApiOperation(value = "导出")
    @GetMapping(value = "/export/{siteId}")
    @ResponseBody
    public void export(@PathVariable("siteId") Long siteId, HttpServletResponse response) {
        List<Template> list = templateService.list(new PropertyWrapper<>(Template.class).eq("siteid",siteId));
        List<String> strings = Arrays.asList("id", "siteid", "columnid", "tempname", "tempdesc", "tempext", "temptype", "tpltype", "outputfilename", "lastmodifieduser", "lastmodifiedtime", "changehistory", "temphtml", "pagesize", "datanum", "startposition", "rightcontent", "themecssname", "themecsscontent", "basestylecssname", "basestylecsscontent", "basestylejsname", "basestylejscontent", "basecontactcssname", "basecontactcsscontent", "basecontactjsname", "basecontactjscontent", "portaltype", "status", "thumbnail" );
        resetCellMaxTextLength();
        DownMetadataUtil.downExcel(1, list, response, Template.class, strings);
    }

    @ApiOperation(value = "导入")
    @PostMapping(value = "/import/{siteId}")
    @ResponseBody
    public JsonResult import1(@PathVariable("siteId") Long siteId, @RequestParam(value = "file", required = true) MultipartFile file, HttpServletResponse response) {
        try {
            List<Template> list = EasyExcel.read(file.getInputStream()).sheet(0).head(Template.class).doReadSync();
            for (Template template : list) {
                template.setSiteid(siteId);
            }
            getService().insertBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return renderSuccess();
    }

    public static void resetCellMaxTextLength() {
        SpreadsheetVersion excel2007 = SpreadsheetVersion.EXCEL2007;
        if (Integer.MAX_VALUE != excel2007.getMaxTextLength()) {
            Field field;
            try {
                // SpreadsheetVersion.EXCEL2007的_maxTextLength变量
                field = excel2007.getClass().getDeclaredField("_maxTextLength");
                // 关闭反射机制的安全检查，可以提高性能
                field.setAccessible(true);
                // 重新设置这个变量属性值
                field.set(excel2007,Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
