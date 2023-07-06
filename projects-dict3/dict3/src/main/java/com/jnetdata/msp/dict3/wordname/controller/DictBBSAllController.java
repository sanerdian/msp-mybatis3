package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.dict3.wordname.mapper.DictBbsAllMapper;
import com.jnetdata.msp.dict3.wordname.mapper.DictWordsrelationMapper;
import com.jnetdata.msp.dict3.wordname.model.DictBbsAll;
import com.jnetdata.msp.dict3.wordname.model.DictWordsrelation;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;

@RestController
@RequestMapping("/dictbbsall")
public class DictBBSAllController {

    @Autowired
    DictBbsAllMapper dictBbsAllMapper;

    @ApiOperation(value = "问好", notes="问个好")
    @GetMapping("/hey")
    @ResponseBody
    @RequestMapping(value = {"/hey"},method = RequestMethod.GET)
    public String hey(){
        return "index";
    }


    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictBbsAll> vo){
//


        QueryWrapper<DictBbsAll> wrapper=new QueryWrapper();
        wrapper.setEntity(vo.getEntity());


        int current = vo.getPager().getCurrent();
        int page_size = vo.getPager().getSize() ;

        Page<DictBbsAll> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);


        List<DictBbsAll> dictWords= dictBbsAllMapper.selectPage(page,wrapper).getRecords() ;

        int total  =  dictBbsAllMapper.selectCount(wrapper) ;

        int pages =   total / page_size +1;



        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);

        return JsonResult.success(pager);
    }
}
