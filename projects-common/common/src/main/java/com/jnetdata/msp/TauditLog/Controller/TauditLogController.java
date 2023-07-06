package com.jnetdata.msp.TauditLog.Controller;

import com.jnetdata.msp.TauditLog.model.TauditLogModel;
import com.jnetdata.msp.TauditLog.service.TauditLogService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;

/**
 * 功能描述：
 */
@Controller
@RequestMapping("/log/taudit")
public class TauditLogController extends BaseController {
    @Resource
    private TauditLogService tauditLogService;

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<TauditLogModel>> list(@RequestBody BaseVo<TauditLogModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    private TauditLogService getService() {
        return tauditLogService;
    }
}
