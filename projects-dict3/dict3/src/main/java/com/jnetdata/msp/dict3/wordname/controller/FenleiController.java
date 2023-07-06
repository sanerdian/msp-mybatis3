package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.dict3.wordname.ID;
import com.jnetdata.msp.dict3.wordname.mapper.DictFenleiMapper;
import com.jnetdata.msp.dict3.wordname.model.DictFenlei;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 21/08/09.
 */
public class FenleiController {
    @Autowired
    DictFenleiMapper dictFenleiMapper ;


    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictFenlei> vo){
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


        QueryWrapper<DictFenlei> wrapper=new QueryWrapper<>();
        wrapper.setEntity(vo.getEntity());
        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;

        int current = vo.getPager().getCurrent()  ;   int page_size = vo.getPager().getSize() ;


        Page<DictFenlei> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);

        List<DictFenlei> dictWords= dictFenleiMapper.selectPage(page,wrapper).getRecords() ;
        int total  =  dictFenleiMapper.selectCount(wrapper) ;
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

        DictFenlei obj =    dictFenleiMapper.selectById(id) ;


        return  JsonResult.success(obj) ;
    }


    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictFenlei vo){
//


        val user = SessionUser.getCurrentUser();

        vo.setFenfeiid(BigInteger.valueOf(ID.getId()));
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());
        int result   =  dictFenleiMapper.insert(vo) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictFenlei vo){

//        vo.setJmetastopwordid(id);
        val user = SessionUser.getCurrentUser();
        vo.setModifyTime(new Date());
        vo.setModifyUser( user.getName() );

        vo.setFenfeiid(BigInteger.valueOf(id));
        dictFenleiMapper.updateById(vo);

        return JsonResult.success();
    }


    @ApiOperation(value = "批量删除", notes="批量删除")
    @GetMapping("/batchDel")
    @ResponseBody
    @RequestMapping(value = {"/batchDel"},method = RequestMethod.POST)
    public JsonResult batchDel(@RequestBody BaseVo<DictFenlei> vo){
//
        String[] arr = vo.getEntity().getField1().split(",") ;
        List<Long> idList = new ArrayList<Long>();

        for (String string : arr) {
            Long integerobj  =  Long.parseLong(string);
            idList.add(integerobj ) ;
        }

        int result   =  dictFenleiMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//        int result   =  dictFenleiMapper.delete(var1) ;
        int result   =  dictFenleiMapper.deleteById(id);
        return JsonResult.success(result);
    }

}
