package com.jnetdata.msp.manage.tplresource.controller;
import com.jnetdata.msp.manage.site.service.SiteService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.vo.ExportVo;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.tplresource.service.JTemplateResourceService;
import com.jnetdata.msp.manage.tplresource.model.JTemplateResource;

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
@Api(tags = "模板资源(tplresourceController)")
public class JTemplateResourceController extends BaseController<Long,JTemplateResource> {

    final private JTemplateResourceService jTemplateResourceService;

    @Autowired
    private SiteService siteService;

    @Autowired
    public JTemplateResourceController(JTemplateResourceService jTemplateResourceService) {
        this.jTemplateResourceService = jTemplateResourceService;
    }


    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping("addCommon")
    @ResponseBody
    public JsonResult<EntityId<Long>> addCommon(@Validated @RequestBody JTemplateResource entity) {
        getService().setSite(entity.getType(),entity);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping("addCommonJs")
    @ResponseBody
    public JsonResult<EntityId<Long>> addCommonJs(@Validated @RequestBody JTemplateResource entity) {
        getService().setSite("js",entity);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("addCommonCss")
    @ResponseBody
    public JsonResult<EntityId<Long>> addCommonCss(@Validated @RequestBody JTemplateResource entity) {
        getService().setSite("css",entity);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("importCommon")
    @ResponseBody
    public JsonResult<EntityId<Long>> importCommonJs(Long siteId,String type, MultipartFile file) {
        JTemplateResource entity = getService().getEntity(type,siteId,file);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("importCommonJs")
    @ResponseBody
    public JsonResult<EntityId<Long>> importCommonJs(Long siteId, MultipartFile file) {
        JTemplateResource entity = getService().getJsEntity(siteId,file);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("importCommonCss")
    @ResponseBody
    public JsonResult<EntityId<Long>> importCommonCss(Long siteId, MultipartFile file) {
        JTemplateResource entity = getService().getCssEntity(siteId,file);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("importCommonImg")
    @ResponseBody
    public JsonResult<EntityId<Long>> importCommonImg(Long siteId, MultipartFile file) {
        JTemplateResource entity = getService().getImgEntity(siteId, file);
        getService().insert(entity);
        return renderSuccess(entity);
    }

    @PostMapping("editImg/{id}")
    @ResponseBody
    public JsonResult<EntityId<Long>> editImg(@PathVariable("id") Long rid, String title, MultipartFile file) {
        getService().editImg(rid,title,file);
        return renderSuccess();
    }

    @ApiOperation(value = "js列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/jsList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> jsList(@RequestBody BaseVo<JTemplateResource> vo) {
        return renderSuccess(getService().listing("js" , vo.getPager() ,createCondition(vo.getEntity())));
    }


    @ApiOperation(value = "css列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/cssList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> cssList(@RequestBody BaseVo<JTemplateResource> vo) {
        return renderSuccess(getService().listing("css" , vo.getPager() ,createCondition(vo.getEntity())));
    }


    @ApiOperation(value = "img列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/commonList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> commonList(@RequestBody BaseVo<JTemplateResource> vo) {
        return renderSuccess(getService().listing(vo.getEntity().getType() , vo.getPager() ,createCondition(vo.getEntity())));
    }


    @ApiOperation(value = "img列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/imgList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> imgList(@RequestBody BaseVo<JTemplateResource> vo) {
        return renderSuccess(getService().listing("img" , vo.getPager() ,createCondition(vo.getEntity())));
    }

    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<JTemplateResource> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "根据指定id删除")
    @GetMapping("/delete/{id}")
    @ResponseBody
    public JsonResult<Void> deleteByIdreal(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据指定多个id批量删除")
    @PostMapping("/delete/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIdsreal(@RequestBody String[] ids) {
        return doDeleteBatchIds(getService(), String.join(",",ids));
    }

    @ApiOperation(value = "发布")
    @GetMapping("/pub/{id}")
    @ResponseBody
    public JsonResult<JTemplateResource> pub(@PathVariable("id") Long id) {
        getService().pubFile(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量发布")
    @PostMapping("/pub")
    @ResponseBody
    public JsonResult<JTemplateResource> pubByIds(@RequestBody Long[] ids) {
        getService().pubFiles(ids);
        return renderSuccess();
    }

    @ApiOperation(value = "撤销发布", hidden = true)
    @GetMapping("/repub/{id}")
    @ResponseBody
    public JsonResult<JTemplateResource> repub(@PathVariable("id") Long id) {
        repubData(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量撤销发布", hidden = true)
    @PostMapping("/repub")
    @ResponseBody
    public JsonResult<JTemplateResource> repubByIds(@RequestBody Long[] ids) {
        for (Long id : ids) {
            repubData(id);
        }
        return renderSuccess();
    }

    public void passData(Long id,String content){
        JTemplateResource res = getService().getById(id);
        res.setStatus(10);
        getService().updateById(res);
    }

    public void notpassData(Long id,String content){
        JTemplateResource res = getService().getById(id);
        res.setStatus(20);
        getService().updateById(res);
    }


    public void repubData(Long id){
        JTemplateResource res = getService().getById(id);
        res.setStatus(4);
        getService().updateAllColumnById(res);
    }


    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody JTemplateResource entity) {
        entity.setStatus(0);
        return doUpdateById(getService(), id, entity);
    }
    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */

    @ApiOperation(value = "添加", hidden = true)
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody JTemplateResource entity) {
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        return result;
    }

    @ApiOperation(value = "批量添加", notes = "批量添加", hidden = true)
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<JTemplateResource> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "审核通过", hidden = true)
    @GetMapping("/pass/{id}")
    @ResponseBody
    public JsonResult<JTemplateResource> pass(@PathVariable("id") Long id,String content) {
        passData(id,content);
        return renderSuccess();
    }

    @ApiOperation(value = "批量审核通过", hidden = true)
    @PostMapping("/pass")
    @ResponseBody
    public JsonResult<JTemplateResource> passByIds(@RequestBody Long[] ids,String content) {
        for (Long id : ids) {
            passData(id,content);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "审核不通过", hidden = true)
    @PostMapping("/notpass/{id}")
    @ResponseBody
    public JsonResult<JTemplateResource> notpass(@PathVariable("id") Long id,String content) {
        notpassData(id,content);
        return renderSuccess();
    }

    @ApiOperation(value = "批量审核不通过", hidden = true)
    @PostMapping("/notpass")
    @ResponseBody
    public JsonResult<JTemplateResource> notpassByIds(@RequestBody Long[] ids,String content) {
        for (Long id : ids) {
            notpassData(id,content);
        }
        return renderSuccess();
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性", hidden = true)
    @PutMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdate(@RequestBody List<JTemplateResource> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 属性选择性批量更新操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "选择性批量更新操作（放入回收站）", notes="只更新entity中设置为非null的属性", hidden = true)
    @PostMapping("/updateDocStatus")
    @ResponseBody
    public JsonResult<Void> updateBatchById(@RequestBody Long[] ids, Integer docstatus) {
        List<JTemplateResource> list = new ArrayList<>();
        for (Long id : ids) {
            JTemplateResource entity = new JTemplateResource();
            entity.setId(id);
            entity.setDocstatus(docstatus);
            list.add(entity);
        }
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null", hidden = true)
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody JTemplateResource entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "导入", hidden = true)
    @PostMapping(value = "/import")
    public JsonResult importData(String columnid, MultipartFile file) throws Exception {
        //文件后缀不是.jpg
        if (!file.getOriginalFilename().contains(".xls") && !file.getOriginalFilename().contains(".xlsx")) {
            return renderError("请上传EXCEL文件");
        }

        InputStream in = file.getInputStream();
        List<List<String>> listob = ExcelUtil.importExcelStringInfo(in, file.getOriginalFilename());

        List<String> fieldNames = new ArrayList<>();

        listob.remove(0);

        List<Map<String,Object>> list1 = new ArrayList<>();
        listob.forEach(item -> {
            Map<String,Object> map = new HashMap<>();
            for(int i=0; i<fieldNames.size(); i++){
                map.put(fieldNames.get(i),item.get(i));
            }
            list1.add(map);
        });

        List<JTemplateResource> list2 = new ArrayList<>();
        list1.forEach(item -> {
            JTemplateResource obj = MapUtil.map2Obj(item, JTemplateResource.class);
            obj.setDocstatus(0);
            list2.add(obj);
        });

        getService().insertBatch(list2);

        return renderSuccess();
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> list(@ModelAttribute JTemplateResource entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> list(@RequestBody BaseVo<JTemplateResource> vo) {
        if(vo.getEntity() == null) vo.setEntity(new JTemplateResource());
            vo.getEntity().setDocstatus(0);
            JsonResult<Pager<JTemplateResource>> result = doList(getService(), vo.getPager(), vo.getEntity());
            return result;
    }


    @ApiOperation(value = "查询已删除列表", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/delList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> delListing(@RequestBody BaseVo<JTemplateResource> vo) {
        if(vo.getEntity() == null) vo.setEntity(new JTemplateResource());
                vo.getEntity().setDocstatus(1);
                return doList(getService(), vo.getPager(), vo.getEntity());
    }


    @ApiOperation(value = "查询已发布列表", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/pubList")
    @ResponseBody
    public JsonResult<Pager<JTemplateResource>> pubListing(@RequestBody BaseVo<JTemplateResource> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }


    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出", hidden = true)
    @RequestMapping(value = "/export", method={RequestMethod.POST,RequestMethod.GET})
    public void export(JTemplateResource entity, ExportVo vo, HttpServletResponse response) {
        List<JTemplateResource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
//        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),JTemplateResource.class);
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出", hidden = true)
    @PostMapping(value = "/export2")
    public void export2(@RequestBody ExportVo<JTemplateResource> vo, HttpServletResponse response) {
        List<JTemplateResource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(vo.getEntity()));
        }
//        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),JTemplateResource.class);
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出", hidden = true)
    @RequestMapping(value = "/export3", method={RequestMethod.POST,RequestMethod.GET})
    public void export3(ExportVo<JTemplateResource> vo, HttpServletResponse response) {
        List<JTemplateResource> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            if(vo.getPageSize() == null || vo.getPageStartNo() == null ){
                datalist = getService().list(createCondition(vo.getEntity()));
            }else{
                datalist = new ArrayList<>();
                for(int i = vo.getPageStartNo(); i <= vo.getPageEndNo(); i++){
                    Pager pager1 = new Pager();
                    pager1.setCurrent(i);
                    pager1.setSize(vo.getPageSize());
                    Pager pager = getService().list(pager1,getService().makeSearchWrapper(createCondition(vo.getEntity())));
                    datalist.addAll(pager.getRecords());
                }
            }
        }
        if(vo.getFields() != null){
            DownMetadataUtil.downMetadata(datalist,response,vo.getExt(),vo.getIncludeTitle(),JTemplateResource.class,vo.getFields());
        }else{
//            DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),JTemplateResource.class);
        }
    }


    private JTemplateResourceService getService() {
        return this.jTemplateResourceService;
    }

}

