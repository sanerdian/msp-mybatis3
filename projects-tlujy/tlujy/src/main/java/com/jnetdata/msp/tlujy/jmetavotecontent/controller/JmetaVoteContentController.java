package com.jnetdata.msp.tlujy.jmetavotecontent.controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnetdata.msp.util.DownMetadataUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import com.jnetdata.msp.vo.ExportVo;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.stereotype.Controller;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.tlujy.jmetavotecontent.service.JmetaVoteContentService;
import com.jnetdata.msp.tlujy.jmetavotecontent.model.JmetaVoteContent;

/**
 * <p>
 * VIEW 前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-08-29
 */
@Controller
@RequestMapping("/tlujy/jmetavotecontent")
public class JmetaVoteContentController extends BaseController {

    final private JmetaVoteContentService jmetaVoteContentService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    public JmetaVoteContentController(JmetaVoteContentService jmetaVoteContentService) {
        this.jmetaVoteContentService = jmetaVoteContentService;
    }

    
    @ApiOperation(value = "查询列表(*已废除接口)", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<JmetaVoteContent>> list(@ModelAttribute JmetaVoteContent entity,@RequestBody PageVo1 pager) {
        return doList(getService(), pager, entity);
    }


    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/listing")
    @ResponseBody
    public JsonResult<Pager<JmetaVoteContent>> list(@RequestBody BaseVo<JmetaVoteContent> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "导出", notes="根据vo指定条件进行导出")
    @PostMapping(value = "/export")
    public void export(JmetaVoteContent entity, ExportVo vo, HttpServletResponse response) {
        List<JmetaVoteContent> datalist;
        if(vo.getArea() == 2){
            datalist = getService().list();
        }else{
            datalist = getService().list(createCondition(entity));
        }
        List<Fieldinfo> list = fieldinfoUtilService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", vo.getTableId()).eq("showList", 1));
        DownMetadataUtil.downMetadata(list,datalist,response,vo.getExt(),vo.getIncludeTitle());
    }

    private JmetaVoteContentService getService() {
        return this.jmetaVoteContentService;
    }

}

