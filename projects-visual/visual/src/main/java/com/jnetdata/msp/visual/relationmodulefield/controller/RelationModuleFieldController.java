package com.jnetdata.msp.visual.relationmodulefield.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.visual.modulesearch.model.ModuleSearch;
import com.jnetdata.msp.visual.moduletable.service.ModuleTableService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.FieldinfoVo;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import java.util.List;

@RestController
@RequestMapping("/visual/relationmodulefield")
@ApiModel(value = "RelationModuleFieldController", description = "组件与字段关联")
public class RelationModuleFieldController extends BaseController<Long, RelationModuleField> {

    @Autowired
    private RelationModuleFieldService relationModuleTemplateService;

    @ApiOperation(value = "获取字段列表")
    @PostMapping("/list")
    public JsonResult<List<FieldinfoVo>> list(@RequestBody FieldinfoVo entity) {
        return getService().list(entity);
    }

    private RelationModuleFieldService getService() {
        return this.relationModuleTemplateService;
    }

}

