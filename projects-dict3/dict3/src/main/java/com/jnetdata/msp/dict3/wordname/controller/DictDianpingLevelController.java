package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.dict3.wordname.mapper.DictDianpinglevelMapper;
import com.jnetdata.msp.dict3.wordname.model.DictDianpinglevel;
import com.jnetdata.msp.dict3.wordname.vo.Params;
import com.jnetdata.msp.dict3.wordname.ID;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dictdianpinglevel")
public class DictDianpingLevelController {



    @Autowired
    DictDianpinglevelMapper dictDianpinglevelMapper;


    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictDianpinglevel> vo){
//


        QueryWrapper<DictDianpinglevel> wrapper=new QueryWrapper<>();
        wrapper.setEntity(vo.getEntity());


        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;




        Page<DictDianpinglevel> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);

        List<DictDianpinglevel> dictWords= dictDianpinglevelMapper.selectPage(page,wrapper).getRecords() ;
         int total  =  dictDianpinglevelMapper.selectCount(wrapper) ;
         int pages =   total / page_size +1 ;



        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);



        return JsonResult.success(pager);
    }


    @ApiOperation(value = "根据id查询", notes="查看")
    @GetMapping("/view/{id}")
    @ResponseBody
    @RequestMapping(value = {"/view/{id}"},method = RequestMethod.POST)
    public JsonResult view(@PathVariable("id") Long id){

        DictDianpinglevel obj =    dictDianpinglevelMapper.selectById(id) ;


        return  JsonResult.success(obj) ;
    }

    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictDianpinglevel vo){
//

        val user = SessionUser.getCurrentUser();

        vo.setMetaid(ID.getId());
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());
    int result   =  dictDianpinglevelMapper.insert(vo) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictDianpinglevel vo){

        vo.setMetaid(id);

        int result   =  dictDianpinglevelMapper.updateById(vo) ;

        return JsonResult.success(result);
    }


    @ApiOperation(value = "批量删", notes="批量删")
    @GetMapping("/batchDel")
    @ResponseBody
    @RequestMapping(value = {"/batchDel"},method = RequestMethod.POST)
    public JsonResult batchDel(@RequestBody Params vo){
//
       String[] arr = vo.getField1().split(",") ;
        List<Long> idList = new ArrayList<Long>();

        for (String string : arr) {
            Long integerobj  =  Long.parseLong(string);
            idList.add(integerobj ) ;
        }

        int result   =  dictDianpinglevelMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//





        int result = dictDianpinglevelMapper.deleteById(id);

        return JsonResult.success(result);
    }


}
