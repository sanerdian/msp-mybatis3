package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.dict3.wordname.mapper.DictStopwordMapper;
import com.jnetdata.msp.dict3.wordname.model.DictStopword;

import com.jnetdata.msp.dict3.wordname.vo.Params;
import com.jnetdata.msp.dict3.wordname.ID;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dictstopword")
public class DictStopwordController {




    @Autowired
    DictStopwordMapper dictStopwordMapper ;


    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictStopword> vo){
//
        String     dtype = vo.getEntity().getCruser() ;
        vo.getEntity().setCruser(null);

        String gt_ref_time = null ;


        if(dtype==null  || "".equals(dtype) ) dtype="" ;


        switch (dtype)
        {
            case "近一天":

            {
                gt_ref_time = MFunction.extracted(-1) ;
            }

            break;



            case  "近三天":

            {
                gt_ref_time = MFunction.extracted(-3) ;
            }

            break;


            case  "近一周":

            {
                gt_ref_time = MFunction.extracted(-7) ;
            }

            break;



            default:

                System.out.println("未知等级");

        }


        QueryWrapper<DictStopword> wrapper=new QueryWrapper<>();
        wrapper.setEntity(vo.getEntity());
        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;

        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;


        Page<DictStopword> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);

        List<DictStopword> dictWords= dictStopwordMapper.selectPage(page,wrapper).getRecords() ;
         int total  =  dictStopwordMapper.selectCount(wrapper) ;
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

        DictStopword obj =    dictStopwordMapper.selectById(id) ;


        return  JsonResult.success(obj) ;
    }


    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictStopword vo){
//


        val user = SessionUser.getCurrentUser();

        vo.setJmetastopwordid(BigInteger.valueOf(ID.getId()));
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());
        vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );
    int result   =  dictStopwordMapper.insert(vo) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictStopword vo){

//        vo.setJmetastopwordid(id);
        val user = SessionUser.getCurrentUser();
        vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );

        vo.setJmetastopwordid(BigInteger.valueOf(id));
        dictStopwordMapper.updateById(vo);

        return JsonResult.success();
    }


    @ApiOperation(value = "批量删除", notes="批量删除")
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

        int result   =  dictStopwordMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//

//        Wrapper<DictStopword> var1 = new Wrapper<DictStopword>() {
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
//        var1.eq("JMETASTOPWORDID",id) ;


//        int result   =  dictStopwordMapper.delete(var1) ;
        int result   =  dictStopwordMapper.deleteById(id);
        return JsonResult.success(result);
    }


}
