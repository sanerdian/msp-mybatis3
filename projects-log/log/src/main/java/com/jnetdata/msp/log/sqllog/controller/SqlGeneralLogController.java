package com.jnetdata.msp.log.sqllog.controller;

import com.jnetdata.msp.log.sqllog.model.SqlGeneralLog;
import com.jnetdata.msp.log.sqllog.service.SqlGeneralLogService;
import com.jnetdata.msp.log.sqllog.vo.SqlGeneralLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

/**
 * Created by TF on 2019/3/26.
 */

@Controller
@RequestMapping("/log/sqlgeneralLog")
@ApiModel(value = "SqlGeneralLogController", description = "sql日志")
@Api(tags = "sql日志(SqlGeneralLogController)")
public class SqlGeneralLogController {

    private static final String BASE_URL = "/log/sqlgeneral";

    @Autowired
    private SqlGeneralLogService service;

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<SqlGeneralLog>> list(@RequestBody SqlGeneralLogVo vo) {
        Pager<SqlGeneralLog> pager = service.search(createPager(vo.getPager()), ConditionMap.of(vo.getEntity()));
        return JsonResult.renderSuccess(pager);
    }

    private Pager<SqlGeneralLog> createPager(PageVo1 pageVo) {
        Pager<SqlGeneralLog> pager = new Pager<>();
        BeanUtils.copyProperties(pageVo, pager);
        return pager;
    }
}
