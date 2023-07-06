package com.jnetdata.msp.resources.picture.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.resources.picture.model.ProResRelation;
import com.jnetdata.msp.resources.picture.service.ProResRelationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: YUEHAO
 * @Date: 2019/11/29
 * 资源与申报项目关联接口
 */
@Controller
@RequestMapping("/resources/prorescrelation")
public class ProResController extends BaseController<Long,ProResRelation>{
    private static final String BASE_URL ="/resources/prorescrelation";

    @Autowired
    ProResRelationService resRelationService;

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加")
    public JsonResult<ProResRelation> add(@RequestParam Long id, @RequestParam("ids")String ids){
        String[]  s = ids.split(",");
        List<Long > longList = new ArrayList<>();
        for(int i = 0;i<s.length;i++){
           longList.add(Long.parseLong(s[i]));
        }
        for(int i = 0 ;i<longList.size();i++){
            ProResRelation proResRelation = new ProResRelation();
            proResRelation.setProid(id);
            proResRelation.setResid(longList.get(i));
            resRelationService.insert(proResRelation);
        }
        return renderSuccess();
    }


}
