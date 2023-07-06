package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.dict3.wordname.mapper.DictChangjingMapper;
import com.jnetdata.msp.dict3.wordname.model.*;
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
@RequestMapping("/dictchangjingci")
public class DictChangjingciController {



    @Autowired
    DictChangjingMapper dictChangjingMapper ;





    @ApiOperation(value = "列表", notes="列表")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictChangjing> vo){
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

        QueryWrapper<DictChangjing> wrapper=new QueryWrapper();

        DictChangjing entity_param =  vo.getEntity() ;

        if(entity_param.getChangjingci()!=null  && "".equals(entity_param.getChangjingci()))
            entity_param.setChangjingci(null);

        if(entity_param.getSource()!=null  && "".equals(entity_param.getSource()))
            entity_param.setSource(null);

        wrapper.setEntity(vo.getEntity());
        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;

        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;


        Page<DictChangjing> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);

        List<DictChangjing> dictWords= dictChangjingMapper.selectPage(page,wrapper).getRecords() ;
         int total  =  dictChangjingMapper.selectCount(wrapper) ;
         int pages =   total / page_size  -1;



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

        DictChangjing obj =    dictChangjingMapper.selectById(id) ;


        return  JsonResult.success(obj) ;
    }


    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictChangjing vo){
//

        val user = SessionUser.getCurrentUser();

        vo.setJmetachangjingid(BigInteger.valueOf(ID.getId()));
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());
        vo.setModifyTime(new Date());
        vo.setModifyUser( user.getName() );
    int result   =  dictChangjingMapper.insert(vo) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictChangjing vo){

        vo.setJmetachangjingid(BigInteger.valueOf(id));
        val user = SessionUser.getCurrentUser();
        vo.setModifyTime(new Date());
        vo.setModifyUser( user.getName() );

        vo.setJmetachangjingid(BigInteger.valueOf(id));
        dictChangjingMapper.updateById(vo);

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

        int result   =  dictChangjingMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//

//        int result   =  dictChangjingMapper.deleteById(id) ;


//        Wrapper<DictChangjing> var1 = new Wrapper<DictChangjing>() {
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
//        var1.eq("JMETACHANGJINGID",id) ;


        int result   =  dictChangjingMapper.deleteById(id);

        return JsonResult.success(result);
    }


}
