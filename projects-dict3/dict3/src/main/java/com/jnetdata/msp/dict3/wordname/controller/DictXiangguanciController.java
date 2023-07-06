package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.dict3.wordname.mapper.DictWordsrelationMapper;
import com.jnetdata.msp.dict3.wordname.model.DictWord;
import com.jnetdata.msp.dict3.wordname.model.DictWordsrelation;
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
@RequestMapping("/dictxiangguanci")
public class DictXiangguanciController {



    @Autowired
    DictWordsrelationMapper dictWordsrelationMapper ;





    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictWordsrelation> vo){
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


        QueryWrapper<DictWordsrelation> wrapper=new QueryWrapper();
        wrapper.setEntity(vo.getEntity());

        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;

        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;


        Page<DictWordsrelation> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);

        List<DictWordsrelation> dictWords= dictWordsrelationMapper.selectPage(page,wrapper).getRecords() ;
         int total  =  dictWordsrelationMapper.selectCount(wrapper) ;
         int pages =   total / page_size +1;



        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);



        return JsonResult.success(pager);
    }
    @ApiOperation(value = "查看最新的日期", notes="查看")
    @ResponseBody
    @RequestMapping(value = {"/searchLastestDate"},method = RequestMethod.POST)
    public JsonResult searchLastestDate(){
        QueryWrapper<DictWordsrelation> wrapper=new QueryWrapper();
        wrapper.groupBy("CRTIME").select("max(CRTIME) as CRTIME");
        wrapper.orderByDesc("CRTIME");
//        wrapper.l
        List<DictWordsrelation> programas  = dictWordsrelationMapper.selectList(wrapper);
//        programaService.list(new PropertyWrapper<>(Programa.class).eq("status", 0).isNotNull("siteId").groupBy(Arrays.asList("siteId")).select("siteId"));
//        DictFenlei obj =    dictFenleiMapper.selectById(id) ;
        return  JsonResult.success(programas.get(0)) ;
    }
    @ApiOperation(value = "根据id查询", notes="查看")
    @GetMapping("/view/{id}")
    @ResponseBody
    @RequestMapping(value = {"/view/{id}"},method = RequestMethod.POST)
    public JsonResult view(@PathVariable("id") Long id){


//        DictWordsrelation dictWordsrelation = new DictWordsrelation();
//        dictWordsrelation.setJmetawordrelationid(id);


        DictWordsrelation obj =    dictWordsrelationMapper.selectById(id);


        return  JsonResult.success(obj) ;
    }


    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictWordsrelation vo){
//

        val user = SessionUser.getCurrentUser();

        vo.setJmetawordrelationid(BigInteger.valueOf(ID.getId()));
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());
        vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );

    int result   =  dictWordsrelationMapper.insert(vo) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictWordsrelation vo){
        val user = SessionUser.getCurrentUser();
        vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );
        vo.setJmetawordrelationid(BigInteger.valueOf(id));
        dictWordsrelationMapper.updateById(vo);

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

        int result   =  dictWordsrelationMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//

//        Wrapper<DictWordsrelation> var1 = new Wrapper<DictWordsrelation>() {
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
//
//        var1.eq("JMETAWORDRELATIONID" ,id) ;

//        int result   =  dictWordsrelationMapper.delete(var1);
        int result   =  dictWordsrelationMapper.deleteById(id);

        return JsonResult.success(result);
    }


}
