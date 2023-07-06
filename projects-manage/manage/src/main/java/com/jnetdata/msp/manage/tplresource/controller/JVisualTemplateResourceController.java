package com.jnetdata.msp.manage.tplresource.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.tplresource.model.JTemplateResource;
import com.jnetdata.msp.manage.tplresource.service.JTemplateResourceService;
import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.vo.BaseVo;
import com.jnetdata.msp.vo.ExportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模板资源(js,css,img) 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2022-07-12
 */
@Controller
@RequestMapping("/manage/tplresource")
@Api(tags = "可视化模板资源(VisualtplresourceController)")
public class JVisualTemplateResourceController extends BaseController<Long,JTemplateResource> {

    @Autowired
    JTemplateResourceService jTemplateResourceService;

    @Autowired
    private SiteService siteService;

    @PostMapping("importVisualModuleImg")
    @ResponseBody
    public JsonResult<EntityId<Long>> importVisualModuleImg( @RequestParam("file") MultipartFile file, Long moduleId) {
        return renderSuccess(getService().addVisualModuleImg(moduleId,file));
    }

    @PostMapping("importVisualModuleJs")
    @ResponseBody
    public JsonResult<EntityId<Long>> importVisualModuleJs(Long moduleId, MultipartFile file) {
        return renderSuccess(getService().addVisualModuleJs(moduleId,file));
    }

    @PostMapping("importVisualModuleCss")
    @ResponseBody
    public JsonResult<EntityId<Long>> importVisualModuleCss(Long moduleId, MultipartFile file) {
        return renderSuccess(getService().addVisualModuleCss(moduleId,file));
    }


    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作可视化资源", notes="只更新entity中设置为非null的属性")
    @PostMapping("/editVisualModule/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody JTemplateResource entity) {
        getService().updateById4Visual(id,entity);
        return renderSuccess();
    }

    @PostMapping("visualModuleImgList")
    @ResponseBody
    public JsonResult<List<JTemplateResource>> visualModuleImgList(Long moduleId) {
        return renderSuccess(getService().listingForVisualModule("img",moduleId));
    }

    private JTemplateResourceService getService() {
        return this.jTemplateResourceService;
    }

}

