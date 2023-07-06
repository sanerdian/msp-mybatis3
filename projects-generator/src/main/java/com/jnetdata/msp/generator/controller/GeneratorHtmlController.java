package com.jnetdata.msp.generator.controller;

import com.jnetdata.msp.config.config.controller.ConfigController;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.generator.vo.CreateHtmlVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.restfulweb.controller.BaseRestfulController;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/generator/html")
@ApiModel(value = "GeneratorHtmlController", description = "generator")
@Api(tags = "生成html(GeneratorHtmlController)")
public class GeneratorHtmlController extends BaseRestfulController {

    @Autowired
    private ConfigModelService configModelService;

    @PostMapping(value = "/createHtml")
    @ResponseBody
    @ApiOperation(value = "生成应用")
    public JsonResult createCode(HttpServletRequest request,String template, String path, String fileName) throws IOException {
        path = GeneratorHtmlController.class.getResource("/").getPath().replace("/WEB-INF/classes/","") + "/pub" + path;

        Velocity.init();

        VelocityContext context = new VelocityContext();

        ResourceBundle resource = ResourceBundle.getBundle("config");
        String default_html = resource.getString("default_html");
        template += default_html;

        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "mystring", template);

        File file = new File(path);
        file.mkdirs();
        file = new File(path + "/"+fileName+".html");
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        inputstreamtofile(inputStream, file);
        return renderSuccess();
    }

    @PostMapping(value = "/createHtml1")
    @ResponseBody
    @ApiOperation(value = "发布")
    public JsonResult createHtml1(@RequestBody CreateHtmlVo vo) {
       /*lsy

       path = GeneratorHtmlController.class.getResource("/").getPath().replace("/WEB-INF/classes/","") + "/pub" + path;*/

        String path = configModelService.getFrontPath()+File.separator+"pub"+File.separator+"html"+vo.getPath();

        Velocity.init();

        VelocityContext context = new VelocityContext();

        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "mystring", vo.getTemplate());

        File file = new File(path);
        file.mkdirs();
        file = new File(path + File.separator + vo.getFileName()+"."+vo.getTempext());
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        inputstreamtofile(inputStream, file);
        Map<String,String> map = new HashMap<>();
        map.put("path","/pub/"+"html"+vo.getPath() + "/" + vo.getFileName()+"."+vo.getTempext());
        return renderSuccess(map);
    }

    @PostMapping(value = "/delCodes")
    @ResponseBody
    @ApiOperation(value = "删除应用")
    public JsonResult delCodes(String tableNames, String prefixes) {

        return renderSuccess();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(GeneratorHtmlController.class.getResource("/"));
//        test2();
    }

    public static void test1(){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        // 载入（获取）模板对象
        Template t = ve.getTemplate("helloTpl.vm");
        VelocityContext ctx = new VelocityContext();
        // 域对象加入参数值
        ctx.put("name", "李智龙");
        ctx.put("date", (new Date()).toString());
        // list集合
        List temp = new ArrayList();
        temp.add("1");
        temp.add("2");
        ctx.put("list", temp);

        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

        System.out.println(sw.toString());
    }



    public static void test2() throws UnsupportedEncodingException {
        Velocity.init();

        VelocityContext context = new VelocityContext();

        context.put("name", "Velocity");
        context.put("project", "Jakarta");
        context.put("now", new Date());

        /* lets make our own string to render */

        String str = "We are using $project $name to render this. $now";
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "mystring", str);

        File file = new File("detail.html");
        InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("UTF-8"));
        inputstreamtofile(inputStream, file);
        System.out.println(" string : " + stringWriter);
    }

    public static void inputstreamtofile(InputStream ins, File file){
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
