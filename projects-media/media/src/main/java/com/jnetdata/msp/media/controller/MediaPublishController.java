package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.media.service.MediaPublishService;
import com.jnetdata.msp.media.directive.BasePublishService;
import com.jnetdata.msp.media.directive.TemplateConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thenicesys.web.JsonResult;

import java.io.IOException;

@RestController
@RequestMapping("/media/publish")
@Api(description = "新闻的发布及撤销发布")
public class MediaPublishController extends BasePublishService {
    @Autowired
    private MediaPublishService mediaPublishService;
    @ApiOperation("生成新闻的详情，并发布")
    @GetMapping("/publishXinwen/{docid}")
    public JsonResult publishDetail(@PathVariable Long docid){
        try{
            String url=mediaPublishService.publishDetail(docid);
            return JsonResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation("生成栏目的列表页，并发布")
    @GetMapping("/publishColumnList/{columnid}")
    public JsonResult publishList(@PathVariable Long columnid){
        try{
            String url=mediaPublishService.publishList(columnid, true, null);
            return JsonResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    public JsonResult publishHome(@PathVariable Long siteid){
        try{
            String url=mediaPublishService.publishHome(siteid);
            return JsonResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation("生成该栏目的列表页及所有新闻的详情页，并发布")
    @GetMapping("/publishColumnAll/{columnid}")
    public JsonResult publishColumn(@PathVariable Long columnid){
        try{
            String url=mediaPublishService.publishColumn(columnid);
            return JsonResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }
    @ApiOperation("撤销对指定新闻的发布")
    @GetMapping("/revokeXinwen/{docid}")
    public JsonResult revokeDetail(@PathVariable Long docid){
        try{
            mediaPublishService.revokeDetail(docid);
            return JsonResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation("生成新闻的预览页面")
    @GetMapping("/previewXinwen/{docid}")
    public JsonResult previewDetail(@PathVariable Long docid){
        try{
            String url=mediaPublishService.previewDetail(docid);
            return JsonResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }

    @ApiOperation("tag测试")
    @GetMapping("/test")
    public void test(){

        TemplateConfig config = new TemplateConfig();
//        config.context.put(PublishConstant.CMS_MODEL, 2);
        config.setTargetPath("D:\\a.html");
        config.setTempalteHtml("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<meta charset=\"UTF-8\">\n" +
                "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\n" +
                "\t<title>湖泊水污染治理与生态修复技术国家工程实验室</title>\n" +
                "\t<link rel=\"stylesheet\" href=\"/pub/hupo/images/reset.css\" />\n" +
                "\t<link rel=\"stylesheet\" href=\"/pub/hupo/images/common.css\" />\n" +
                "\t<link rel=\"stylesheet\" href=\"/pub/hupo/images/list.css\" />\n" +
                "\t<link rel=\"shortcut icon\" href=\"/pub/hupo/logo-icon.ico\">\n" +
                "</head>\n" +
                "<body>\n" +
                "#jnet_channels(\"{owner:'self',num:'5'}\") \n" +
                "                <li><a  href=\"#jnet_channel(\"{owner:'self',target:'_self',field:'_recurl'}\") #end\">#jnet_channel(\"{owner:'self',target:'_self',field:'chnlname'}\") #end<i></i><b></b></a>\n" +
                "#end" +
                "#jnet_channel(\"{owner:'self',id:'1'}\") " +
                "#end"+
                "</body>\n" +
                "</html>");
        try {
            mergeTemplate(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
