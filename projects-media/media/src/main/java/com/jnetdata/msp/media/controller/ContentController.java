package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.service.ContentService;
import com.jnetdata.msp.media.util.publicMethodUtil;
import com.jnetdata.msp.media.vo.OptionsVo;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen020.service.Xinwen020Service;
import com.jnetdata.msp.tlujy.xinwen_read.mapper.XinwenReadMapper;
import com.jnetdata.msp.tlujy.xinwen_read.model.XinwenRead;
import com.jnetdata.msp.tlujy.xinwen_record.mapper.XinwenRecordMapper;
import com.jnetdata.msp.tlujy.xinwen_record.model.XinwenRecord;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/media/content/")
@Api(description = "前台使用cdn的接口")
public class ContentController {

    @Autowired
    private ContentService service;
    private ContentService getService(){ return service; }

    @Autowired
    private Xinwen020Service xinwen020Service;
    @Autowired
    private XinwenRecordMapper xinwenRecordMapper;

    @ApiOperation(value = "获取置顶新闻列表", notes="获取置顶新闻列表")
    @GetMapping("/listXiwentopping/{lmid}")
    @ResponseBody
    public JsonResult<Xinwen020> listXiwentopping(@PathVariable("lmid") Long lmid) {

        //执行设置置顶值
        List<OptionsVo> list = service.listXiwentopping(lmid);

        return JsonResult.success(list);
    }

    @ApiOperation(value = "修改新闻置顶新闻", notes="修改新闻置顶新闻")
    @PostMapping("/updateXiwentopping")
    @ResponseBody
    public JsonResult<Programa> updateXiwentopping(@RequestBody Xinwen020 xinwen020) {

        //执行设置置顶值
        service.updateXiwentopping(xinwen020);

        return JsonResult.success();
    }


    @ApiOperation(value = "获取置顶新闻列表", notes="获取置顶新闻列表")
    @GetMapping("/listXiwenrotation/{lmid}")
    @ResponseBody
    public JsonResult<Xinwen020> listXiwenrotation(@PathVariable("lmid") Long lmid) {

        //执行设置轮播图
        List<OptionsVo> list = service.listXiwenrotation(lmid);

        return JsonResult.success(list);
    }

    @ApiOperation(value = "修改新闻置顶新闻", notes="修改新闻置顶新闻")
    @PostMapping("/updateXiwenrotation")
    @ResponseBody
    public JsonResult<Programa> updateXiwenrotation(@RequestBody Xinwen020 xinwen020) {

        //执行设置轮播图
        service.updateXiwenrotation(xinwen020);

        return JsonResult.success();
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/updatexinwen/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Xinwen020 entity) {

        User user = publicMethodUtil.getUser();

        if (ObjectUtils.isEmpty(id)) {
            throw new NullPointerException("数据错误，请刷新页面重试");
        }

        Xinwen020 xinwen020 = xinwen020Service.getById(id);

        //对修改进行记录

        if(!StringUtils.isEmpty(entity.getTitle())&&!StringUtils.isEmpty(entity.getZhengwen())&&(!xinwen020.getTitle().equals(entity.getTitle())||!xinwen020.getZhengwen().equals(entity.getZhengwen()))){

            //给记录表添加
            XinwenRecord xinwenRecord = new XinwenRecord();
            xinwenRecord.setCreateBy(user.getId());
            xinwenRecord.setCreateTime(new Date());
            xinwenRecord.setCrUser(user.getName());
            xinwenRecord.setXinwenid(id+"");
            xinwenRecord.setTitleold(xinwen020.getTitle());
            xinwenRecord.setTitlenew(entity.getTitle());
            xinwenRecord.setHtmlold(xinwen020.getZhengwen());
            xinwenRecord.setHtmlnew(entity.getZhengwen());

            xinwenRecordMapper.insert(xinwenRecord);
        }

        return xinwen020Service.updateById(entity) ? JsonResult.success("更新成功") : JsonResult.fail("更新失败");
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

        User user = publicMethodUtil.getUser();

        List<Xinwen020> list = new ArrayList<>();
        for (Long id : ids) {
            Xinwen020 entity = new Xinwen020();
            entity.setId(id);
            entity.setDocstatus(docstatus);
            entity.setModifyTime(new Date());
            entity.setModifyBy(user.getId());
            entity.setModifyUser(user.getName());
            list.add(entity);
        }
        xinwen020Service.updateBatchById(list);
        return JsonResult.renderSuccess();
    }


}
