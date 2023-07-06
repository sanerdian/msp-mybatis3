package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.media.service.SurveyService;
import com.jnetdata.msp.media.vo.survey.*;
import com.jnetdata.msp.tlujy.answer_user.model.AnswerUser;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/media/survey")
@Api(description = "问卷调查定制后台")
public class SurveyController {
    @Autowired
    SurveyService surveyService;

    @ApiOperation(value = "保存调查问卷", notes="同时保存调查问卷的题目及选择题的备选答案")
    @PostMapping("/save")
    public JsonResult save(@RequestBody InvestigateVo vo){
        try{
            Long id=surveyService.save(vo);
            return JsonResult.success(id);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "获取调查问卷信息", notes="同时获取调查问卷的题目及选择题的备选答案，以及当前用户对该问卷的答案")
    @GetMapping("/{id}")
    public JsonResult getById(@PathVariable Long id){
        try{
            InvestigateVo vo = surveyService.getById(id);
            return JsonResult.success(vo);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "物理删除问卷调查", notes="同时删除调查问卷的题目及选择题的备选答案")
    @DeleteMapping("/deletesOnDisk/{ids}")
    public JsonResult deletesOnDisk(@PathVariable List<Long> ids){
        try{
            surveyService.deletesOnDisk(ids);
            return JsonResult.success("调查问卷已物理删除！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "标记删除问卷调查", notes="仅将问卷调查的docstatus的取值设置为1")
    @DeleteMapping("/deletesByRemark/{ids}")
    public JsonResult deleteByRemark(@PathVariable List<Long> ids){
        try{
            surveyService.deletesByRemark(ids);
            return JsonResult.success("调查问卷已标记删除！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "批量更新问卷调查", notes="")
    @PutMapping("/updates")
    public JsonResult updates(@RequestBody List<Investigate> investigates){
        try{
            surveyService.updates(investigates);
            return JsonResult.success("调查问卷已更新成功！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "获取当前用户对问卷的答案", notes="不是已答题目的标准答案，而是当前用户对该问卷的题目的答复")
    @GetMapping("/getAnswers/{id}")
    public JsonResult getAnswers(@PathVariable Long id){
        try{
            Map<String,List> vo = surveyService.getAnswers(id);
            return JsonResult.success(vo);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "保存用户对问卷的答案（草稿）", notes="body是Map，其中key为题目id，value为数组（为答案）")
    @PostMapping("/saveAnswers/{id}")
    public JsonResult saveAnswers(@PathVariable Long id,@RequestBody Map<Long,List> answerMap,Long secondCost){
        try{
            surveyService.saveAnswers(id,answerMap,secondCost);
            return JsonResult.success("问卷答案（草稿）保存成功！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "提交用户对问卷的答案（最终答案）", notes="body是Map，其中key为题目id，value为数组（为答案）")
    @PostMapping("/commitAnswers/{id}")
    public JsonResult commitAnswers(@PathVariable Long id,@RequestBody Map<Long,List> answerMap,Long secondCost){
        try{
            if(!ObjectUtils.isEmpty(answerMap)){
                surveyService.saveAnswers(id,answerMap,secondCost);
            }
            //surveyService.commitAnswers(id);
            return JsonResult.success("问卷答案保存成功！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation(value = "提交答案", notes="")
    @PostMapping("/commitDaan/{id}")
    public JsonResult commitDaan(@PathVariable Long id){
        try{
            surveyService.commitAnswers(id);
            return JsonResult.success("问卷提交成功！");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation(value = "获取当前用户的问卷列表", notes="要么该问卷是全员可见，要么当前用户属于被推送目标。返回结果中answerStatus：0未开始答题、1已答部分题目、2已打完全部题目")
    @PostMapping("/getSurvey4CurrentUser")
    public JsonResult getSurvey4CurrentUser(@RequestBody Investigate template){
        try{
            List<InvestigateCurrentVo> vos=surveyService.getSurvey4CurrentUser(template);
            return JsonResult.success(vos);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询问卷列表，同时统计各问卷的发布量及回收量")
    @PostMapping("/pageSurvey")
    public JsonResult pageSurvey(@RequestBody BaseVo<InvestigateQueryVo> vo){
        try{
            Pager<InvestigateWithCount> pager= surveyService.pageSurvey(vo);
            return JsonResult.success(pager);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "按部门统计问卷的回收情况", notes="需要参数问卷的id")
    @GetMapping("/countRecoverByGroup/{investigateId}")
    public JsonResult countRecoverByGroup(@PathVariable Long investigateId){
        try{
            List<Map> counts=surveyService.countRecoverByGroup(investigateId);
            return JsonResult.success(counts);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "分页查询回收/未回收名单",notes="需要参数问卷id(investigateid)、机构id（groupid）、回收状态（recoverstatus，0未回收1已回收null全部）")
    @PostMapping("/pageInvestigateUser")
    public JsonResult pageInvestigateUser(@RequestBody BaseVo<InvestigateUser> vo){
        try{
            Pager<InvestigateUser> users=surveyService.pageInvestigateUser(vo);
            return JsonResult.success(users);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "对当前问卷的结果进行统计",notes="需要参数问卷的id；返回结果是Map，其中key为answerId,value为该备选答案选中的次数")
    @GetMapping("/countByAnswer/{investigateId}")
    public JsonResult countByAnswer(@PathVariable Long investigateId){
        try{
            Map counts=surveyService.countByAnswer(investigateId);
            return JsonResult.success(counts);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation(value = "导出答题情况",notes="可以导出指定问卷/题目/人员（createBy）的答题情况")
    @PostMapping("/exportAnswer")
    public void exportAnswer(@RequestBody AnswerUser vo, HttpServletResponse response){
        surveyService.exportAnswer(vo,response);
    }
    @ApiOperation(value = "导出问卷",notes="可以导出指定问卷")
    @GetMapping("/exportInvestigate/{investigateid}")
    public void exportInvestigate(@PathVariable Long investigateid, HttpServletResponse response) {
        surveyService.exportInvestigate(investigateid,response);
    }
    @ApiOperation(value = "测试导出",notes="")
    @GetMapping("/exportTest")
    public void exportTest( HttpServletResponse response){
//        InvestigateVo vo = surveyService.getById(investigateId);
//        List datalist= vo.getTopics();
        List datalist=new ArrayList();
        Map data=new HashMap();
        data.put("TOPICID","题目1这是一个非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的题目");
        datalist.add(data);
        Map data1=new HashMap();
        data1.put("answerchoose","这个是一个普通的选项");
        datalist.add(data1);
        List<Pair<String,String>> list = new ArrayList<>();
        list.add(new Pair<>("题目id","TOPICID"));
        list.add(new Pair<>("选中的答案","answerchoose"));
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
        for (Pair fieldinfo : list) {
            excel.add(new ExcelBean(fieldinfo.getKey().toString(), fieldinfo.getValue().toString(), 0));
        }
        map.put(0, excel);
        String sheetName = "datas";  //sheet名
        String excelName = sheetName + ".xlsx";
        try {
            XSSFWorkbook workbook = ExcelUtil.createExcelFile(datalist, map, sheetName);
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=data.xlsx");
            response.setContentType("application/octet-stream");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
