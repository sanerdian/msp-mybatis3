package com.jnetdata.msp.visual.template.controller;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.template.service.TemplateLogService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.util.Base64ImgUtil;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import com.jnetdata.msp.visual.template.model.VisualTemplate;
import com.jnetdata.msp.visual.template.service.VisualTemplateService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
@Controller
@RequestMapping("/visual/template")
public class VisualTemplateController extends BaseController<Long, VisualTemplate> {

    final private VisualTemplateService visualTemplateService;


    @Resource
    private TemplateLogService templateLogService;
    @Resource
    private ElementService elementService;
    @Autowired
    private ConfigModelService configModelService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Autowired
    public VisualTemplateController(VisualTemplateService visualTemplateService) {
        this.visualTemplateService = visualTemplateService;
    }

    /**
     * 执行新增操作
     * @param entity
     * @return
     * @author hongshou
     * @date 2020/5/26
     */
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加", notes="根据提供的实体属性添加")
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody VisualTemplate entity)  {
        if(entity.getImg() != null && entity.getImg().startsWith("data:image/png;base64,")){
            String path = configModelService.getUploadPath(ConfigModel.dir_thumbnail);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =  path+File.separator + UUID.randomUUID()+".png";
            Base64ImgUtil.Base64ToImage(entity.getImg().replace("data:image/png;base64,",""),filePath);
            String basedir = configModelService.getBaseUploadPath();
            String fileUrl = filePath.replace(basedir,"");
            if(File.separator.equals("\\")){
                fileUrl = fileUrl.replace("\\","/");
            }
            String url="/files"+fileUrl;
            entity.setImg(url);
        }

        //生成可视化模板对应的js代码和css代码
        getService().generateJsAndCss(entity);
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        templateLogService.addLog("添加",entity.getTitle(),entity.getType(),address,"");

        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "删除", notes= "根据指定id删除")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        VisualTemplate byId = getService().getById(id);
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        templateLogService.addLog("删除",byId.getTitle(),byId.getType(),address,"");
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes="根据指定的多个id进行批量删除")
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody VisualTemplate entity)  {
        if(entity.getImg() != null && entity.getImg().startsWith("data:image/png;base64,")){
            String path = configModelService.getUploadPath(ConfigModel.dir_thumbnail);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =  path+File.separator + UUID.randomUUID()+".png";
            Base64ImgUtil.Base64ToImage(entity.getImg().replace("data:image/png;base64,",""),filePath);
            String basedir = configModelService.getBaseUploadPath();
            String fileUrl = filePath.replace(basedir,"");
            if(File.separator.equals("\\")){
                fileUrl = fileUrl.replace("\\","/");
            }
            String url="/files"+fileUrl;
            entity.setImg(url);
        }
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        //生成可视化模板对应的js代码和css代码
        getService().generateJsAndCss(entity);
        templateLogService.addLog("更新保存",entity.getTitle(),entity.getType(),address,"");
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody VisualTemplate entity) {
        if(entity.getImg() != null && entity.getImg().startsWith("data:image/png;base64,")){
            String path = configModelService.getUploadPath(ConfigModel.dir_thumbnail);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =  path+File.separator + UUID.randomUUID()+".png";
            Base64ImgUtil.Base64ToImage(entity.getImg().replace("data:image/png;base64,",""),filePath);
            String basedir = configModelService.getBaseUploadPath();
            String fileUrl = filePath.replace(basedir,"");
            if(File.separator.equals("\\")){
                fileUrl = fileUrl.replace("\\","/");
            }
            String url="/files"+fileUrl;
            entity.setImg(url);
        }

        //生成可视化模板对应的js代码和css代码
        getService().generateJsAndCss(entity);
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        templateLogService.addLog("修改保存",entity.getTitle(),entity.getType(),address,entity.getMjType());

        return doUpdateAllColumnById(getService(), id, entity);
    }

    /**
     * 查看指定id的实体对象
     * @param id
     * @author chunping
     * @date 2022/11/29
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<VisualTemplate> getById(@PathVariable("id") Long id) {
        JsonResult<VisualTemplate> result = doGetById(getService(), id);
        result.getObj().setModuleList(relationModuleTemplateService.getModuleList(id));
        return result;
    }
    /*
    * 根据vo指定条件进行查询
    * @author Hongshou
    * @data 2020/5/16
    * */
    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<VisualTemplate>> list(@RequestBody BaseVo<VisualTemplate> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "预览")
    @PostMapping(value = "/preview/{id}")
    @ResponseBody
    public JsonResult<String> preview(@PathVariable("id") Long id) {
        return getService().preview(id);
    }

    @ApiOperation(value = "获取表单页面发布地址")
    @PostMapping(value = "/getFormUrl/{formId}")
    @ResponseBody
    public JsonResult<String> getFormUrl(@PathVariable("formId") Long formId) {
        return getService().getFormUrl(formId);
    }

    /**
     * 同步模板并发布
     * @author chunping
     * @date 2022/12/6
     * @param entity
     * @return
     */
    @ApiOperation(value = "同步到模板并发布")
    @PostMapping(value = "syncAndPublish")
    @ResponseBody
    public JsonResult<String> syncAndPublish(@RequestBody Template entity) {
        return getService().syncAndPublish(entity);
    }

    private VisualTemplateService getService() {
        return this.visualTemplateService;
    }

}

