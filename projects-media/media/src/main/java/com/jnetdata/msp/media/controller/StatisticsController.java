package com.jnetdata.msp.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.StatisticsService;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;

@Controller
@RequestMapping("/media/statistics")
@Api(description = "统计分析模块")
public class StatisticsController extends BaseController<Long, JobApi> {

    @Autowired
    private StatisticsService service;
    private StatisticsService getService(){ return service; }

    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @ApiOperation(value = "统计分析内容统计新闻", notes="统计分析内容统计新闻")
    @PostMapping("/xinwenlist")
    @ResponseBody
    public JsonResult xinwenlist(@RequestBody xinwenQueryVo vo) {

        Page<xinwenVo> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().xinwenlist(page,vo.getEntity()));
    }


    @ApiOperation(value = "部门统计图", notes="部门统计图")
    @PostMapping("/bmtjt")
    @ResponseBody
    public JsonResult bmtjt(@RequestBody TjtVo vo) {
        return JsonResult.success(getService().bmtjt(vo));
    }

    @ApiOperation(value = "部门统计图评论", notes="部门统计图评论")
    @PostMapping("/bmtjtpl")
    @ResponseBody
    public JsonResult bmtjtpl(@RequestBody TjtVo vo) {

        return JsonResult.success(getService().bmtjtpl(vo));
    }

    @ApiOperation(value = "年龄统计图", notes="年龄统计图")
    @PostMapping("/nltjt")
    @ResponseBody
    public JsonResult nltjt(@RequestBody TjtVo vo) {

        vo.setMin(0);
        vo.setMax(20);

        Map<String,Object> a=new HashMap<>();//0-20
        a.put("name","<20");
        a.put("value",getService().tjage(vo));

        vo.setMin(20);
        vo.setMax(30);
        Map<String,Object> b=new HashMap<>();//0-20
        b.put("name","20-30");
        b.put("value",getService().tjage(vo));


        vo.setMin(30);
        vo.setMax(40);
        Map<String,Object> c=new HashMap<>();//0-20
        c.put("name","30-40");
        c.put("value",getService().tjage(vo));

        vo.setMin(40);
        vo.setMax(50);
        Map<String,Object> d=new HashMap<>();//0-20
        d.put("name","40-50");
        d.put("value",getService().tjage(vo));

        vo.setMin(50);
        vo.setMax(60);
        Map<String,Object> e=new HashMap<>();//0-20
        e.put("name","50-60");
        e.put("value",getService().tjage(vo));

        vo.setMin(60);
        vo.setMax(2000);
        Map<String,Object> f=new HashMap<>();//0-20
        f.put("name",">60");
        f.put("value",getService().tjage(vo));

        List<Object> listname=asList("<20","20-30","30-40","40-50","50-60",">60");
        List<Map<String,Object>> listmap=new ArrayList<>();
        listmap.add(a);
        listmap.add(b);
        listmap.add(c);
        listmap.add(d);
        listmap.add(e);
        listmap.add(f);

        vo.setListname(listname);
        vo.setListmap(listmap);
        return JsonResult.success(vo);
    }

    @ApiOperation(value = "评论统计图", notes="评论统计图")
    @PostMapping("/pltjt")
    @ResponseBody
    public JsonResult pltjt(@RequestBody TjtVo vo) {

        return JsonResult.success(getService().pltjt(vo));
    }

    @ApiOperation(value = "点赞统计图", notes="点赞统计图")
    @PostMapping("/dztjt")
    @ResponseBody
    public JsonResult dztjt(@RequestBody TjtVo vo) {

        return JsonResult.success(getService().dztjt(vo));
    }


    @ApiOperation(value = "用户趋势", notes="用户趋势")
    @PostMapping("/yhqs")
    @ResponseBody
    public JsonResult yhqs(@RequestBody TjtVo vo) {

        Map<String,Object> map = getService().usertjCount(vo);//用户总数，新用户数，登陆次数
        vo.setMap(map);

        //一天的毫秒数
        int daytime=24*60*60*1000;

        Date startdate = vo.getStatrdate();

        int dateCount=(int)Math.ceil((vo.getEnddate().getTime()-vo.getStatrdate().getTime())/daytime);

        List<Object> listname=new ArrayList<>();
        List<Object> listvalue=new ArrayList<>();
        List<Object> listvalue2=new ArrayList<>();
        List<Map<String,Object>> listmap=new ArrayList<>();

        for (int i = 0; i <=dateCount; i++) {

            Date starttime=new Date(startdate.getTime()+(i*daytime));

            vo.setStatrdate(starttime);
            vo.setEnddate(new Date(startdate.getTime()+(i+1)*daytime));

            Map<String,Object> m = getService().usertjCount(vo);//用户总数，新用户数，登陆次数
            m.put("datatime",sf.format(starttime));

            listname.add(sf.format(starttime));
            listvalue.add(m.get("COUNTLOGIN"));
            listvalue2.add(m.get("COUNTNOLOGIN"));
            listmap.add(m);
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);
        vo.setListvalue2(listvalue2);
        vo.setListmap(listmap);

        return JsonResult.success(vo);
    }


    @ApiOperation(value = "活跃用户", notes="活跃用户")
    @PostMapping("/hyyh")
    @ResponseBody
    public JsonResult hyyh(@RequestBody TjtVo vo) {

        //一天的毫秒数
        long daytime=24*60*60*1000;

        Date startdate = vo.getStatrdate();

        int dateCount=(int)Math.ceil((vo.getEnddate().getTime()-vo.getStatrdate().getTime())/daytime);

        List<Object> listname=new ArrayList<>();
        List<Object> listvalue=new ArrayList<>();
        List<Object> listvalue2=new ArrayList<>();
        List<Map<String,Object>> listmap=new ArrayList<>();

        for (int i = 0; i <=dateCount; i++) {

            Date starttime = new Date(startdate.getTime()+(i*daytime));
            Date endtime = new Date(startdate.getTime()+(i+1)*daytime);

            Date starttimez = new Date(starttime.getTime()-7*daytime);
            Date endtimez = endtime;

            Date starttimey = new Date(starttime.getTime()-(30L*daytime));
            Date endtimey = endtime;

            vo.setStatrdate(starttime);
            vo.setEnddate(endtime);
            vo.setStatrdatez(starttimez);
            vo.setEnddatez(endtimez);
            vo.setStatrdatey(starttimey);
            vo.setEnddatey(endtimey);

            Map<String,Object> m = getService().usertjhyCount(vo);//日活跃用户，百分比，周活跃，百分比，月活跃，百分比，日不活跃
            m.put("datatime",sf.format(starttime));

            listname.add(sf.format(starttime));
            listvalue.add(m.get("RCOUNTLOGIN"));
            listvalue2.add(m.get("COUNTNOLOGIN"));
            listmap.add(m);
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);
        vo.setListvalue2(listvalue2);
        vo.setListmap(listmap);

        return JsonResult.success(vo);
    }

    @ApiOperation(value = "投票列表", notes="投票列表")
    @PostMapping("/votelist")
    @ResponseBody
    public JsonResult<voteVo> votelist(@RequestBody VoteQueryVo vo) {

        Page<Vote> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().votelist(page,vo.getEntity()));
    }

    @ApiOperation(value = "投票标题部门统计", notes="投票标题部门统计")
    @PostMapping("/votedepttj")
    @ResponseBody
    public JsonResult votedepttj(@RequestBody TjtVo vo) {

        List<Object> listname=new ArrayList<>();
        List<Object> listvalue=new ArrayList<>();

        List<Map<String,Object>> list = getService().votedepttj(vo);

        for (Map<String,Object> map : list) {
            listname.add(map.get("DWMC"));
            listvalue.add(map.get("COUNTS"));
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return JsonResult.success(vo);
    }

    @ApiOperation(value = "投票内容部门统计", notes="投票内容部门统计")
    @PostMapping("/voteContentdepttj")
    @ResponseBody
    public JsonResult voteContentdepttj(@RequestBody TjtVo vo) {

        List<Object> listname=new ArrayList<>();
        List<Object> listvalue=new ArrayList<>();
        List<Map<String,Object>> listMap=new ArrayList<>();

        List<Map<String,Object>> list = getService().voteContentdepttj(vo);

        for (Map<String,Object> map : list) {
            listname.add(map.get("DWMC"));
            listvalue.add(map.get("COUNTS"));

            Map m=new HashMap();
            m.put("name",map.get("DWMC"));
            m.put("value",map.get("COUNTS"));
            listMap.add(m);
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);
        vo.setListmap(listMap);

        return JsonResult.success(vo);
    }


    @ApiOperation(value = "投票内容数量统计", notes="投票内容数量统计")
    @PostMapping("/votecommontj")
    @ResponseBody
    public JsonResult votecommontj(@RequestBody TjtVo vo) {

        List<Object> listname=new ArrayList<>();
        List<Map<String,Object>> listmap=new ArrayList<>();

        List<Map<String,Object>> list = getService().votecommontj(vo);

        for (Map<String,Object> map : list) {
            listname.add(map.get("TITLE"));

            Map m=new HashMap();

            m.put("id",map.get("ID"));
            m.put("name",map.get("TITLE"));
            m.put("value",map.get("COUNTS"));
            listmap.add(m);
        }

        vo.setListname(listname);
        vo.setListmap(listmap);

        return JsonResult.success(vo);
    }

    @ApiOperation(value = "投票趋势统计图", notes="投票趋势统计图")
    @PostMapping("/voteqst")
    @ResponseBody
    public JsonResult voteqst(@RequestBody TjtVo vo) {

        //一天的毫秒数
        int daytime=24*60*60*1000;

        Date startdate = vo.getStatrdate();

        int dateCount=(int)Math.ceil((vo.getEnddate().getTime()-vo.getStatrdate().getTime())/daytime);

        List<Object> listname=new ArrayList<>();
        List<Object> listvalue=new ArrayList<>();

        for (int i = 0; i <=dateCount; i++) {

            Date starttime=new Date(startdate.getTime()+(i*daytime));

            vo.setStatrdate(starttime);
            vo.setEnddate(new Date(startdate.getTime()+(i+1)*daytime));

            Map<String,Object> m = getService().voteUserCount(vo);//用户总数，新用户数，登陆次数
            listname.add(sf.format(starttime));
            listvalue.add(m.get("COUNTS"));
        }

        vo.setListname(listname);
        vo.setListvalue(listvalue);

        return JsonResult.success(vo);
    }


    @ApiOperation(value = "根据投票内容id查询投票名称和投递数量", notes="根据投票内容id查询投票内容")
    @GetMapping("/getVoteContent/{votecontentid}")
    @ResponseBody
    public JsonResult getVoteContent(@PathVariable("votecontentid") Long votecontentid) {
        return JsonResult.success(getService().getVoteContent(votecontentid));
    }

}
