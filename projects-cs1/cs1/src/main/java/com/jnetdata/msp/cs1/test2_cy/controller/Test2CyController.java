package com.jnetdata.msp.cs1.test2_cy.controller;
import com.jnetdata.msp.fenfa.model.Fenfa;
import com.jnetdata.msp.fenfa.service.FenfaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.ExportVo;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.thenicesys.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;
import java.util.List;
import java.util.ArrayList;
import org.thenicesys.data.api.EntityId;
import com.jnetdata.msp.vo.BaseVo;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.cs1.test2_cy.service.Test2CyService;
import com.jnetdata.msp.cs1.test2_cy.model.Test2Cy;

/**
 * <p>
 * 测试表cy 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2023-06-05
 */
@Controller
@RequestMapping("/cs1/test2cy")
public class Test2CyController extends BaseController<Long,Test2Cy> {

    final private Test2CyService test2CyService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    private FenfaService fenfaService;

    @Autowired
    public Test2CyController(Test2CyService test2CyService) {
        this.test2CyService = test2CyService;
    }

    

    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Test2Cy entity) {
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        syncAdd(entity.getColumnid(),entity.getId());
        return result;
    }

    public void syncAdd(Long columnid,Long fromid){
        Test2Cy toCopy = new Test2Cy();
        toCopy.setSingletempkate(0L);
        toCopy.setCopyfromid(fromid);

        Test2Cy toYinyong = new Test2Cy();
        toYinyong.setId(fromid);
        List<String> quotainfo = new ArrayList<>();

        List<Fenfa> fenfas = fenfaService.list(new PropertyWrapper<>(Fenfa.class)
            .like("fromids", columnid + "")
            .like("syncwhile", "0"));
        for (Fenfa fenfa : fenfas) {
            if(fenfa.getSynctype() == 0){
                toCopy.setColumnid(fenfa.getColumnid());
                getService().insert(toCopy);
            }else{
                quotainfo.add(fenfa.getColumnid() + ":" + fenfa.getSynctype());
            }
        }

        if(!quotainfo.isEmpty()){
            toYinyong.setQuotainfo(String.join(",",quotainfo));
            getService().updateById(toYinyong);
        }
    }

    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping("/batch")
    @ResponseBody
    public JsonResult addbatch( @RequestBody List<Test2Cy> list) {
        getService().insertBatch(list);
        return renderSuccess();
    }

    @ApiOperation(value = "查看", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Test2Cy> getById(@PathVariable("id") Long id) {
        return renderSuccess(getService().getById(id));
    }

    @ApiOperation(value = "发布")
    @GetMapping("/pub/{id}")
    @ResponseBody
    public JsonResult<Test2Cy> pub(@PathVariable("id") Long id) {
        pubData(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量发布")
    @PostMapping("/pub")
    @ResponseBody
    public JsonResult<Test2Cy> pubByIds(@RequestBody Long[] ids) {
        for (Long id : ids) {
            pubData(id);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "撤销发布")
    @GetMapping("/repub/{id}")
    @ResponseBody
    public JsonResult<Test2Cy> repub(@PathVariable("id") Long id) {
        repubData(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量撤销发布")
    @PostMapping("/repub")
    @ResponseBody
    public JsonResult<Test2Cy> repubByIds(@RequestBody Long[] ids) {
        for (Long id : ids) {
            repubData(id);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "审核通过")
    @GetMapping("/pass/{id}")
    @ResponseBody
    public JsonResult<Test2Cy> pass(@PathVariable("id") Long id,String content) {
        passData(id,content);
        return renderSuccess();
    }

    @ApiOperation(value = "批量审核通过")
    @PostMapping("/pass")
    @ResponseBody
    public JsonResult<Test2Cy> passByIds(@RequestBody Long[] ids,String content) {
        for (Long id : ids) {
            passData(id,content);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "审核不通过")
    @PostMapping("/notpass/{id}")
    @ResponseBody
    public JsonResult<Test2Cy> notpass(@PathVariable("id") Long id,String content) {
        notpassData(id,content);
        return renderSuccess();
    }

    @ApiOperation(value = "批量审核不通过")
    @PostMapping("/notpass")
    @ResponseBody
    public JsonResult<Test2Cy> notpassByIds(@RequestBody Long[] ids,String content) {
        for (Long id : ids) {
            notpassData(id,content);
        }
        return renderSuccess();
    }

    public void passData(Long id,String content){
        Test2Cy res = getService().getById(id);
        res.setStatus(10);
        res.setDoctitle(content);
        getService().updateById(res);
    }

    public void notpassData(Long id,String content){
        Test2Cy res = getService().getById(id);
        res.setStatus(20);
        res.setDoctitle(content);
        getService().updateById(res);
    }

    public void pubData(Long id){
        Test2Cy res = getService().getById(id);
        res.setStatus(21);
        res.setDocstatus(-1);
        res.setId(null);
        getService().insert(res);

        List<Fenfa> fenfas = fenfaService.list(new PropertyWrapper<>(Fenfa.class)
            .like("fromids", res.getColumnid()+"")
            .and(m -> m.like("syncwhile", "2").or().like("syncwhile", "3")));

        List<String> quotainfo = new ArrayList<>();
        List<Long> tocopycolumnId = new ArrayList<>();
        for (Fenfa fenfa : fenfas) {
            if(fenfa.getSynctype() == 0){
                tocopycolumnId.add(fenfa.getColumnid());
            }else if(fenfa.getSynctype() == 1 || fenfa.getSynctype() == 2){
                quotainfo.add(fenfa.getColumnid() + ":" +fenfa.getSynctype());
            }
        }
        if(quotainfo.size() > 0) res.setQuotainfo(String.join(",",quotainfo));

        res.setDocchannelid(res.getId());
        res.setId(id);
        res.setDocstatus(0);
        getService().updateById(res);

        if(!tocopycolumnId.isEmpty()){
            List<Test2Cy> toSyncEdit = getService().list(new PropertyWrapper<>(Test2Cy.class).eq("copyfromid", id).in("columnid", tocopycolumnId));
            if(!toSyncEdit.isEmpty()){
                for (Test2Cy sync : toSyncEdit) {
                    Long syncId = sync.getId();
                    Long columnId = sync.getColumnid();
                    BeanUtils.copyProperties(res,sync);
                    sync.setId(syncId);
                    sync.setColumnid(columnId);
                    sync.setCopyfromid(id);
                    getService().updateById(sync);
                    tocopycolumnId.remove(columnId);
                }
            }

            for (Long columnid : tocopycolumnId) {
                res.setCopyfromid(id);
                res.setColumnid(columnid);
                res.setId(null);
                getService().insert(res);
            }
        }
    }

    public void repubData(Long id){
        Test2Cy res = getService().getById(id);
        res.setStatus(4);
        res.setDocchannelid(null);
        getService().updateAllColumnById(res);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation(value="假删")
    public JsonResult<Void> deleteByIdGet(@PathVariable("id") Long id) {
        Test2Cy res = getService().getById(id);
        if(res.getDocchannelid() != null){
            //删除已发布的
            getService().deleteById(res.getDocchannelid());
        }
        Test2Cy entity = new Test2Cy();
        entity.setId(id);
        entity.setDocstatus(1);
        getService().updateById(entity);
        return renderSuccess();
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/{id}/jsdelete")
    @ResponseBody
    @ApiOperation(value="假删")
    public JsonResult<Void> jsdelete(@PathVariable("id") Long id) {
        Test2Cy res = getService().getById(id);
        if(res.getDocchannelid() != null){
            //删除已发布的
            getService().deleteById(res.getDocchannelid());
        }
        Test2Cy entity = new Test2Cy();
        entity.setId(id);
        entity.setDocstatus(1);
        getService().updateById(entity);
        return renderSuccess();
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value="假删")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        Test2Cy res = getService().getById(id);
        if(res.getDocchannelid() != null){
            //删除已发布的
            getService().deleteById(res.getDocchannelid());
        }
        Test2Cy entity = new Test2Cy();
        entity.setId(id);
        entity.setDocstatus(1);
        getService().updateById(entity);
        return renderSuccess();
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @GetMapping("/{ids}/batch")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> deleteBatchIdsGet(@PathVariable("ids") String ids) {
        String[] idss = ids.split(",");

        //删除已发布的
        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        List<Test2Cy> toDel = new ArrayList<>();
        for (String s : idss) {
            Test2Cy entity = new Test2Cy();
            entity.setId(Long.valueOf(s));
            entity.setDocstatus(1);
            toDel.add(entity);
        }
        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") String ids) {
        String[] idss = ids.split(",");

        //删除已发布的
        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        List<Test2Cy> toDel = new ArrayList<>();
        for (String s : idss) {
            Test2Cy entity = new Test2Cy();
            entity.setId(Long.valueOf(s));
            entity.setDocstatus(1);
            toDel.add(entity);
        }
        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @PostMapping("/delBatch")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> deleteBatchIds(@RequestBody Long[] ids) {
        //删除已发布的
        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(ids));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        List<Test2Cy> toDel = new ArrayList<>();
        for (Long s : ids) {
            Test2Cy entity = new Test2Cy();
            entity.setId(s);
            entity.setDocstatus(1);
            toDel.add(entity);
        }
        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @GetMapping("/{ids}/batchdel/{columnid}")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> batchdelIdsGet(@PathVariable("ids") String ids,@PathVariable("columnid") Long columnid) {
        String[] idss = ids.split(",");

        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Test2Cy> toDel = new ArrayList<>();

        //删除已发布的
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        //删除同步的
        List<Test2Cy> delSync = new ArrayList<>();
        for (Test2Cy entity : entitys) {
            if(!entity.getColumnid().equals(columnid)){
                delSync.add(entity);
            }else{
                entity.setDocstatus(1);
                toDel.add(entity);
            }
        }
        delSync(columnid,delSync);

        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
    * 执行批量删除操作
    * @param ids
    * @return
    */
    @PostMapping("delBatch/{columnid}")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> batchdelIdsGet(@RequestBody Long[] ids,@PathVariable("columnid") Long columnid) {

        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(ids));
        List<Test2Cy> toDel = new ArrayList<>();

        //删除已发布的
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        //删除同步的
        List<Test2Cy> delSync = new ArrayList<>();
        for (Test2Cy entity : entitys) {
            if(!entity.getColumnid().equals(columnid)){
                delSync.add(entity);
            }else{
                entity.setDocstatus(1);
                toDel.add(entity);
            }
        }
        delSync(columnid,delSync);

        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}/batchdel/{columnid}")
    @ResponseBody
    @ApiOperation(value="批量假删")
    public JsonResult<Void> batchdelIds(@PathVariable("ids") String ids,@PathVariable("columnid") Long columnid) {
        String[] idss = ids.split(",");

        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Test2Cy> toDel = new ArrayList<>();

        //删除已发布的
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        //删除同步的
        List<Test2Cy> delSync = new ArrayList<>();
        for (Test2Cy entity : entitys) {
            if(!entity.getColumnid().equals(columnid)){
                delSync.add(entity);
            }else{
                entity.setDocstatus(1);
                toDel.add(entity);
            }
        }
        delSync(columnid,delSync);

        getService().updateBatchById(toDel);
        return renderSuccess();
    }

    public void delSync(Long columnid,List<Test2Cy> entitys) {
        for (Test2Cy entity : entitys) {
            List<String> list = new ArrayList<>(Arrays.asList(entity.getQuotainfo().split(",")));
            list.remove(columnid + ":1");
            list.remove(columnid + ":2");
            entity.setQuotainfo(String.join(",",list));
            getService().updateById(entity);
        }
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @GetMapping("/real/{id}")
    @ResponseBody
    public JsonResult<Void> deleteByIdrealGet(@PathVariable("id") Long id) {

        Test2Cy res = getService().getById(id);
        if(res.getDocchannelid() != null){
            //删除已发布的
            getService().deleteById(res.getDocchannelid());
        }
        return doDeleteById(getService(), id);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @DeleteMapping("/real/{id}")
    @ResponseBody
    public JsonResult<Void> deleteByIdreal(@PathVariable("id") Long id) {

        Test2Cy res = getService().getById(id);
        if(res.getDocchannelid() != null){
            //删除已发布的
            getService().deleteById(res.getDocchannelid());
        }
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @GetMapping("/real/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIdsrealGet(@PathVariable("ids") String ids) {

        //删除已发布的
        String[] idss = ids.split(",");
        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @DeleteMapping("/real/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIdsreal(@PathVariable("ids") String ids) {

        //删除已发布的
        String[] idss = ids.split(",");
        List<Test2Cy> entitys = getService().listByIds(Arrays.asList(idss));
        List<Long> delIds = entitys.stream().filter(m -> m.getDocchannelid() != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        getService().deleteBatchIds(delIds);

        return doDeleteBatchIds(getService(), ids);
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateByIdPost(@PathVariable("id") Long id, @RequestBody Test2Cy entity) {
        List<Fenfa> fenfas = fenfaService.list(new PropertyWrapper<>(Fenfa.class)
            .like("fromids", entity.getColumnid() + "%")
            .eq("synctype", 0)
            .like("syncwhile", "1"));
        List<Long> targetColumnIds = fenfas.stream().map(m -> m.getColumnid()).collect(Collectors.toList());
        if(targetColumnIds.size() > 0){
            List<Test2Cy> syncs = getService().list(new PropertyWrapper<>(Test2Cy.class).in("columnid", targetColumnIds).eq("copyfromid", id));
            if(syncs.size() > 0) {
                for (Test2Cy sync : syncs) {
                    Long syncId = sync.getId();
                    Long columnId = sync.getColumnid();
                    BeanUtils.copyProperties(entity,sync);
                    sync.setId(syncId);
                    sync.setColumnid(columnId);
                    sync.setCopyfromid(id);
                }
                getService().updateBatchById(syncs);
            }
        }
        return doUpdateById(getService(), id, entity);
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
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Test2Cy entity) {
        List<Fenfa> fenfas = fenfaService.list(new PropertyWrapper<>(Fenfa.class)
            .like("fromids", entity.getColumnid() + "%")
            .eq("synctype", 0)
            .like("syncwhile", "1"));
        List<Long> targetColumnIds = fenfas.stream().map(m -> m.getColumnid()).collect(Collectors.toList());
        if(targetColumnIds.size() > 0){
            List<Test2Cy> syncs = getService().list(new PropertyWrapper<>(Test2Cy.class).in("columnid", targetColumnIds).eq("copyfromid", id));
            if(syncs.size() > 0) {
                for (Test2Cy sync : syncs) {
                    Long syncId = sync.getId();
                    Long columnId = sync.getColumnid();
                    BeanUtils.copyProperties(entity,sync);
                    sync.setId(syncId);
                    sync.setColumnid(columnId);
                    sync.setCopyfromid(id);
                }
                getService().updateBatchById(syncs);
            }
        }
        return doUpdateById(getService(), id, entity);
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PostMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdatePost(@RequestBody List<Test2Cy> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 属性选择性更新操作
     * @return
     */
    @ApiOperation(value = "批量选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/batchUpdate")
    @ResponseBody
    public JsonResult<Void> batchUpdate(@RequestBody List<Test2Cy> list) {
        getService().updateBatchById(list);
        return renderSuccess();
    }

    /**
     * 属性选择性批量更新操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "选择性批量更新操作（放入回收站）", notes="只更新entity中设置为非null的属性")
    @PostMapping("/updateDocStatus")
    @ResponseBody
    public JsonResult<Void> updateBatchById(@RequestBody Long[] ids, Integer docstatus) {
        List<Test2Cy> list = new ArrayList<>();
        for (Long id : ids) {
            Test2Cy entity = new Test2Cy();
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
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PostMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnByIdPost(@PathVariable("id") Long id, @RequestBody Test2Cy entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Test2Cy entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "查看(带关联表数据)", notes = "查看指定id的实体对象(带关联数据)")
    @GetMapping("/{id}/join")
    @ResponseBody
    public JsonResult<Test2Cy> getjoin(@PathVariable("id") Long id) {
        Test2Cy entity = getService().getEntityAndJoinsById(id);
        return renderSuccess(entity);
    }


    @ApiOperation(value = "查询列表(带关联表数据)", notes="根据vo指定条件进行查询(带关联数据)")
    @PostMapping(value = "/listing/join")
    @ResponseBody
    public JsonResult<Pager<Test2Cy>> listjoin(@RequestBody BaseVo<Test2Cy> vo) {
        JsonResult<Pager<Test2Cy>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        List<Test2Cy> list = pagerJsonResult.getObj().getRecords();
        getService().getListJoin(list,vo.getEntity());
        return pagerJsonResult;
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    public JsonResult importData(String columnid, MultipartFile file) throws Exception {
        //文件后缀不是.jpg
        if (!file.getOriginalFilename().contains(".xls") && !file.getOriginalFilename().contains(".xlsx")) {
            return renderError("请上传EXCEL文件");
        }

        InputStream in = file.getInputStream();
        List<List<String>> listob = ExcelUtil.importExcelStringInfo(in, file.getOriginalFilename());
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_test2_cy"));

        List<String> fieldNames = new ArrayList<>();
        Map<String, Fieldinfo> another2FieldMap = list.stream().collect(Collectors.toMap(Fieldinfo::getAnothername, v -> v, (v1, v2) -> v1));
        List<String> titles = listob.get(0);
        List<String> errTitles = new ArrayList<>();
        for (Object title : titles) {
            if (another2FieldMap.get(title) == null) {
                errTitles.add(String.valueOf(title));
            } else {
                fieldNames.add(another2FieldMap.get(title).getProName());
            }
        }
        if (errTitles.size() > 0) {
            return renderError("列“" + String.join("”“", errTitles) + "”不存在!");
        }

        listob.remove(0);

        List<Map<String,Object>> list1 = new ArrayList<>();
        listob.forEach(item -> {
            Map<String,Object> map = new HashMap<>();
            for(int i=0; i<fieldNames.size(); i++){
                map.put(fieldNames.get(i),item.get(i));
            }
            list1.add(map);
        });

        List<Test2Cy> list2 = new ArrayList<>();
        list1.forEach(item -> {
            Test2Cy obj = MapUtil.map2Obj(item, Test2Cy.class);
            obj.setColumnid(Long.valueOf(columnid));
            obj.setDocstatus(0);
            list2.add(obj);
        });

        getService().insertBatch(list2);

        return renderSuccess();
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询", hidden = true)
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Test2Cy>> list(@ModelAttribute Test2Cy entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<Test2Cy>> list(@RequestBody BaseVo<Test2Cy> vo) {
        if(vo.getEntity() == null) vo.setEntity(new Test2Cy());
            vo.getEntity().setDocstatus(0);
            JsonResult<Pager<Test2Cy>> result = doList(getService(), vo.getPager(), vo.getEntity());
            for (Test2Cy record : result.getObj().getRecords()) {
            if(vo.getEntity()!=null && vo.getEntity().getColumnid()!=null && !vo.getEntity().getColumnid().equals(record.getColumnid())){
                List<String> split = Arrays.asList(record.getQuotainfo().split(","));
                record.setSynctype(split.contains(vo.getEntity().getColumnid() + ":1")?1:split.contains(vo.getEntity().getColumnid() + ":2")?2:null);
            }
        }
            return result;
    }


    @ApiOperation(value = "查询已删除列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/delList")
    @ResponseBody
    public JsonResult<Pager<Test2Cy>> delListing(@RequestBody BaseVo<Test2Cy> vo) {
        if(vo.getEntity() == null) vo.setEntity(new Test2Cy());
                vo.getEntity().setDocstatus(1);
                return doList(getService(), vo.getPager(), vo.getEntity());
    }


    @ApiOperation(value = "查询已发布列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/pubList")
    @ResponseBody
    public JsonResult<Pager<Test2Cy>> pubListing(@RequestBody BaseVo<Test2Cy> vo) {
                if(vo.getEntity() == null) vo.setEntity(new Test2Cy());
        List<Test2Cy> list = getService().list(new PropertyWrapper<>(Test2Cy.class).eq("status", "21").eq("docstatus", 0).select("docchannelid"));
        List<Long> ids = list.stream().filter(m -> m != null).map(m -> m.getDocchannelid()).collect(Collectors.toList());
        vo.getEntity().setPubIds(ids);
        vo.getEntity().setDocstatus(-1);
        vo.getEntity().setStatus(21);
                return doList(getService(), vo.getPager(), vo.getEntity());
    }


    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出", hidden = true)
    @RequestMapping(value = "/export", method={RequestMethod.POST,RequestMethod.GET})
    public void export(Test2Cy entity, ExportVo vo, HttpServletResponse response) {
        List<Test2Cy> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", "jmeta_test2_cy").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),Test2Cy.class);
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export2")
    public void export2(@RequestBody ExportVo<Test2Cy> vo, HttpServletResponse response) {
        List<Test2Cy> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(vo.getEntity()));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_test2_cy").eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),Test2Cy.class);
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @RequestMapping(value = "/export3", method={RequestMethod.POST,RequestMethod.GET})
    public void export3(ExportVo<Test2Cy> vo, HttpServletResponse response) {
        List<Test2Cy> datalist;
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
            DownMetadataUtil.downMetadata(datalist,response,vo.getExt(),vo.getIncludeTitle(),Test2Cy.class,vo.getFields());
        }else{
            List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename","jmeta_test2_cy").eq("showList", 1));
            DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle(),Test2Cy.class);
        }
    }


    private Test2CyService getService() {
        return this.test2CyService;
    }

}
