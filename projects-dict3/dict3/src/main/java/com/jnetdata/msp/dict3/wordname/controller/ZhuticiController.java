package com.jnetdata.msp.dict3.wordname.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.dict3.wordname.mapper.*;
import com.jnetdata.msp.dict3.wordname.model.*;
import com.jnetdata.msp.dict3.wordname.vo.Params;
import com.jnetdata.msp.dict3.wordname.ID;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import com.jnetdata.msp.core.model.util.SessionUser;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/zhutici")
public class ZhuticiController {



    @Autowired
    DictWordMapper dictWordMapper ;

    @Autowired
    DictWordsrelationMapper dictWordsrelationMapper ;

    @Autowired
    DictShuxingciMapper dictShuxingciMapper ;

    @Autowired
    DictChangjingMapper dictChangjingMapper ;

    @Autowired
    DictStopwordMapper dictStopwordMapper ;

    @ApiOperation(value = "问好", notes="问个好")
    @GetMapping("/hello")
    @ResponseBody
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String hello(){
        return "index";
    }




    @ApiOperation(value = "列表", notes="近一天，近三天，近一周")
    @GetMapping("/list")
    @ResponseBody
    @RequestMapping(value = {"/list"},method = RequestMethod.POST)
    public JsonResult list(@RequestBody BaseVo<DictWord> vo){
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



        QueryWrapper<DictWord> wrapper=new QueryWrapper();

        DictWord entity_param =  vo.getEntity() ;

        if(entity_param.getWordname()!=null  && "".equals(entity_param.getWordname()))
            entity_param.setWordname(null);

        if(entity_param.getSource()!=null  && "".equals(entity_param.getSource()))
            entity_param.setSource(null);

        if(entity_param.getWordname() != null) {
            wrapper.like("WORDNAME",entity_param.getWordname());
            entity_param.setWordname(null);
        }



        wrapper.setEntity(vo.getEntity());


        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;

        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;


        Page<DictWord> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);
        OrderItem orderItem = new OrderItem("crtime",false);
        List<OrderItem> listOrder = new ArrayList<>();
        listOrder.add(orderItem);
        page.setOrders(listOrder);

        List<DictWord> dictWords= dictWordMapper.selectPage(page,wrapper).getRecords() ;



        int total  =  dictWordMapper.selectCount(wrapper) ;
        int pages =   total / page_size +1 ;

//        JsonResult jsonResult = new JsonResult() ;

//        jsonResult.set

        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);
//        jsonResult.setObj(pager);


        return JsonResult.success(pager);
    }

    @ApiOperation(value = "列表", notes="近一天，近三天，近一周")
    @GetMapping("/allList")
    @ResponseBody
    @RequestMapping(value = {"/allList"},method = RequestMethod.POST)
    public JsonResult allList(@RequestBody BaseVo<DictWord> vo){
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



        QueryWrapper<DictWord> wrapper=new QueryWrapper();

        DictWord entity_param =  vo.getEntity() ;

        if(entity_param.getWordname()!=null  && "".equals(entity_param.getWordname()))
            entity_param.setWordname(null);

        if(entity_param.getSource()!=null  && "".equals(entity_param.getSource()))
            entity_param.setSource(null);

        wrapper.setEntity(vo.getEntity());

        if (gt_ref_time != null && !"".equals(gt_ref_time))
            wrapper.gt("CRTIME",gt_ref_time) ;
        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;

//        Page<DictWord> page = new Page<>();
//        page.setSize(page_size);
//        page.setCurrent(current);
//        OrderItem orderItem = new OrderItem("crtime",false);
//        List<OrderItem> listOrder = new ArrayList<>();
//        listOrder.add(orderItem);
//        page.setOrders(listOrder);

        List<DictWord> dictWords= dictWordMapper.selectList(wrapper);

        int total  =  dictWords.size();
        int pages =   total / page_size +1 ;

//        JsonResult jsonResult = new JsonResult() ;

//        jsonResult.set

        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);
//        jsonResult.setObj(pager);


        return JsonResult.success(pager);
    }

    @ApiOperation(value = "过滤相关词已经筛选过的列表", notes="近一天，近三天，近一周")
    @GetMapping("/filter/list")
    @ResponseBody
    @RequestMapping(value = {"/filter/list"},method = RequestMethod.POST)
    public JsonResult filterList(@RequestBody BaseVo<DictWord> vo){
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



        QueryWrapper<DictWord> wrapper=new QueryWrapper();

        DictWord entity_param =  vo.getEntity() ;

//        if(entity_param.getWordname()!=null  && "".equals(entity_param.getWordname()))
//            entity_param.setWordname(null);
//
//        if(entity_param.getSource()!=null  && "".equals(entity_param.getSource()))
//            entity_param.setSource(null);
//
//
//
//        wrapper.setEntity(vo.getEntity());
//
//        if (gt_ref_time != null && !"".equals(gt_ref_time))
//            wrapper.gt("CRTIME",gt_ref_time) ;
        wrapper.setEntity(entity_param);
        QueryWrapper<DictWordsrelation> wrapper1=new QueryWrapper();
        List<DictWordsrelation> programas  = dictWordsrelationMapper.selectList(wrapper1);
        if(programas.size()>0) {
            List<Long> wordIds = programas.stream().filter(m -> m.getWordid() != 0L).map(DictWordsrelation::getWordid).collect(Collectors.toList());
            wrapper.notIn("JMETASEARWORDID", wordIds);
        }
        int current = vo.getPager().getCurrent() ;   int page_size = vo.getPager().getSize() ;

        Page<DictWord> page = new Page<>();
        page.setSize(page_size);
        page.setCurrent(current);
        OrderItem orderItem = new OrderItem("crtime",false);
        List<OrderItem> listOrder = new ArrayList<>();
        listOrder.add(orderItem);
        page.setOrders(listOrder);
       // List<DictWord> dictWords= dictWordMapper.selectPage(page,wrapper).getRecords() ;
        List<DictWord> dictWords= dictWordMapper.selectList(wrapper) ;
        int total  =  dictWords.size() ;
        int pages =   total / page_size +1 ;

//        JsonResult jsonResult = new JsonResult() ;

//        jsonResult.set

        Pager pager = new Pager() ;
        pager.setSize(page_size);
        pager.setCurrent(current);
        pager.setTotal(total);
        pager.setPages(pages);
        pager.setRecords(dictWords);
//        jsonResult.setObj(pager);


        return JsonResult.success(pager);
    }



    @ApiOperation(value = "根据id查询", notes="查看")
    @GetMapping("/view/{id}")
    @ResponseBody
    @RequestMapping(value = {"/view/{id}"},method = RequestMethod.POST)
    public JsonResult view(@PathVariable("id") Long id){

        DictWord obj =    dictWordMapper.selectById(id) ;


        return  JsonResult.success(obj) ;
    }
    @ApiOperation(value = "查看最新的日期", notes="查看")
//    @GetMapping("/searchLastestDate")
    @ResponseBody
//    @GetMapping("/searchLastestDate")
    @RequestMapping(value = {"/searchLastestDate"},method = RequestMethod.POST)
    public JsonResult searchLastestDate(){
        QueryWrapper<DictWord> wrapper=new QueryWrapper();
        wrapper.groupBy("CRTIME").select("max(CRTIME) as CRTIME");
        wrapper.orderByDesc("CRTIME");
//        wrapper.l
        List<DictWord> programas  = dictWordMapper.selectList(wrapper);
//        programaService.list(new PropertyWrapper<>(Programa.class).eq("status", 0).isNotNull("siteId").groupBy(Arrays.asList("siteId")).select("siteId"));
//        DictFenlei obj =    dictFenleiMapper.selectById(id) ;
        return  JsonResult.success(programas.get(0)) ;
    }

    @ApiOperation(value = "增加", notes="增加")
    @GetMapping("/add")
    @ResponseBody
    @RequestMapping(value = {"/add"},method = RequestMethod.POST)
    public JsonResult add(@RequestBody DictWord vo){
        val user = SessionUser.getCurrentUser();
//
        vo.setJmetasearwordid(BigInteger.valueOf(ID.getId()));
        vo.setCruser(user.getName());
        vo.setCrtime(new Date());

        vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );
        Map<String,Object> word  = new HashMap<>();
        word.put("WORDNAME",vo.getWordname());
        List<DictWord> checkWord = dictWordMapper.selectByMap(word);
        int result =0;
        if(checkWord.size() <=0) {
             result = dictWordMapper.insert(vo);
        }
        return JsonResult.success(result);
    }

    @ApiOperation(value = "修改", notes="修改")
    @GetMapping("/modify/{id}")
    @ResponseBody
    @RequestMapping(value = {"/modify/{id}"},method = RequestMethod.POST)
    public JsonResult modify(@PathVariable("id") Long id,@RequestBody DictWord vo){

//        val user = SessionUser.getCurrentUser();
//        System.out.println(1);
//        vo.setJmetasearwordid(id);

//        OPERUSER OPERTIME

        val user = SessionUser.getCurrentUser();
      vo.setOpertime(new Date());
        vo.setOperuser( user.getName() );

        vo.setJmetasearwordid(BigInteger.valueOf(id));

        int i = dictWordMapper.updateById(vo);

        return JsonResult.success();
    }

    @ApiOperation(value = "批量修改", notes="批量修改")
//    @GetMapping("/batchModify")
    @ResponseBody
    @RequestMapping(value = {"/batchModify"},method = RequestMethod.POST)
    public JsonResult batchModify(@RequestBody DictWord vo){

//        val user = SessionUser.getCurrentUser();
//        System.out.println(1);
//        vo.setJmetasearwordid(id);

//        OPERUSER OPERTIME

        //主题词批量修改
        val user = SessionUser.getCurrentUser();
        QueryWrapper<DictWord> wrapper=new QueryWrapper();
        wrapper.orderByDesc("CRTIME");
        List<DictWord> listWords = dictWordMapper.selectList(wrapper);
        listWords.forEach(i->{
            i.setFenlei(vo.getFenlei());
            i.setOpertime(new Date());
            i.setOperuser( user.getName() );
            dictWordMapper.updateById(i);
        });


        //场景词批量修改
        QueryWrapper<DictChangjing> wrapper1=new QueryWrapper();
        wrapper1.orderByDesc("CRTIME");
        List<DictChangjing> listWords1 = dictChangjingMapper.selectList(wrapper1);
        listWords1.forEach(i->{
            i.setFenlei(vo.getFenlei());
            i.setModifyTime(new Date());
            i.setModifyUser( user.getName() );
            dictChangjingMapper.updateById(i);
        });

        //停用词批量修改
        QueryWrapper<DictStopword> wrapper2=new QueryWrapper();
        wrapper2.orderByDesc("CRTIME");
        List<DictStopword> listWords2 = dictStopwordMapper.selectList(wrapper2);
        listWords2.forEach(i->{
            i.setFenlei(vo.getFenlei());
            i.setOpertime(new Date());
            i.setOperuser( user.getName() );
            dictStopwordMapper.updateById(i);
        });

        //属性词批量修改
        QueryWrapper<DictShuxingci> wrapper3=new QueryWrapper();
        wrapper3.orderByDesc("CRTIME");
        List<DictShuxingci> listWords3 = dictShuxingciMapper.selectList(wrapper3);
        listWords3.forEach(i->{
            i.setFenlei(vo.getFenlei());
            i.setOpertime(new Date());
            i.setOperuser( user.getName() );
            dictShuxingciMapper.updateById(i);
        });

        //形容词批量修改
        QueryWrapper<DictWordsrelation> wrapper4=new QueryWrapper();
        wrapper4.orderByDesc("CRTIME");
        List<DictWordsrelation> listWords4 = dictWordsrelationMapper.selectList(wrapper4);
        listWords4.forEach(i->{
            i.setFenlei(vo.getFenlei());
            i.setOpertime(new Date());
            i.setOperuser( user.getName() );
            dictWordsrelationMapper.updateById(i);
        });
        return JsonResult.success("主题词、场景词、停运词、属性词、形容词模块已更新！");
    }

    @ApiOperation(value = "批量删", notes="批量删")
    @GetMapping("/batchDel")
    @ResponseBody
    @RequestMapping(value = {"/batchDel"},method = RequestMethod.POST)
    public JsonResult batchDel(@RequestBody Params vo){
       //接收前端复选框的id,格式如：a,b,c,d
        String[] arr = vo.getField1().split(",") ;
        List<Long> idList = new ArrayList<Long>();
        for (String string : arr) {
            Long integerobj  =  Long.parseLong(string);
            idList.add(integerobj ) ;
        }
        int result   =  dictWordMapper.deleteBatchIds(idList) ;

        return JsonResult.success(result);
    }

    @ApiOperation(value = "删除", notes="根据指定id删除")
    @GetMapping("/delbyid/{id}")
    @ResponseBody
    @RequestMapping(value = {"/delbyid/{id}"},method = RequestMethod.POST)
    public JsonResult delbyid(@PathVariable("id") Long id){
//

////        int result   =  dictWordMapper.deleteById(id) ;
//
//
//
//        Wrapper<DictWord> var1 = new Wrapper<DictWord>() {
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
//        var1.eq("jmetasearwordid",id) ;


        int result   =  dictWordMapper.deleteById(id);


        return JsonResult.success(result);
    }


}
