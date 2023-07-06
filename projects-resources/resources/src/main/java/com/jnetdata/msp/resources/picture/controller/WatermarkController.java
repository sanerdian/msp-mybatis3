package com.jnetdata.msp.resources.picture.controller;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.resources.picture.model.Watermark;
import com.jnetdata.msp.resources.picture.service.WatermarkService;
import com.jnetdata.msp.resources.picture.vo.WatermarkVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/26.
 */
@Controller
@RequestMapping("/resources/watermark")
@ApiModel(value = "WatermarkController", description = "水印")
public class WatermarkController extends BaseController<Long,Watermark>{

    @Autowired
    private WatermarkService service;
    @Autowired
    private ConfigModelService configModelService;

    private WatermarkService getService (){
        return service;
    }
    @ApiOperation(value = "添加水印信息", notes="添加水印信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Watermark entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的文件水印信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {

        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除文件水印信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的水印信息(只需要填文件中的id)")
    @PostMapping(value = "/edit")
    @ResponseBody
    public JsonResult<Void> updateById(
                                       @RequestBody Watermark entity,@RequestParam Long id) {

        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定文件id对应的水印信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Watermark> getById(@ApiParam("文件信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }



    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Watermark>> list(@RequestBody WatermarkVo vo) {

        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @ApiOperation(value = "添加压缩设置",notes = "添加压缩设置")
    @PostMapping("/addCompress")
    @ResponseBody
    public JsonResult<Void> addCompress(@RequestParam String name,@RequestParam Integer w,@RequestParam Integer h){
        Watermark watermark = new Watermark();
        watermark.setCpsW(w);
        watermark.setCpsH(h);
        watermark.setIsCompress(1);
        watermark.setName(name);
        service.insert(watermark);
        return renderSuccess();
    }

    @ApiOperation(value = "图片格式设置",notes = "图片设置")
    @PostMapping("/picSet")
    @ResponseBody
    public JsonResult<Void> picSet(@RequestParam String form){
        Watermark watermark = service.getById(1190169972001751041L);
        watermark.setSupForm(form);
        service.updateById(watermark);
        return renderSuccess();
    }
    @ApiOperation(value = "文件格式设置",notes = "文件设置")
    @PostMapping("/wordSet")
    @ResponseBody
    public JsonResult<Void> wordSet(@RequestParam String form){
        Watermark watermark = service.getById(1190170403385917441L);
        watermark.setSupForm(form);
        service.updateById(watermark);
        return renderSuccess();
    }
    @ApiOperation(value = "视频格式设置",notes = "视频设置")
    @PostMapping("/videoSet")
    @ResponseBody
    public JsonResult<Void> videoSet(@RequestParam String form){
        Watermark watermark = service.getById(1190170214461882370L);
        watermark.setSupForm(form);
        service.updateById(watermark);
        return renderSuccess();
    }
    @ApiOperation(value = "音频格式设置",notes = "音频设置")
    @PostMapping("/mp3Set")
    @ResponseBody
    public JsonResult<Void> mp3Set(@RequestParam String form){
        Watermark watermark = service.getById(1190170471144898562L);
        watermark.setSupForm(form);
        service.updateById(watermark);
        return renderSuccess();
    }
    @ApiOperation(value = "添加版权设置",notes = "添加版权设置")
    @PostMapping("/addCopyright")
    @ResponseBody
    public JsonResult<Void> addCopyright(@RequestParam String copyright){
        Watermark watermark =new Watermark();
        watermark.setIsCompress(2);
        watermark.setCopyRight(copyright);
        service.insert(watermark);
        return renderSuccess();
    }

    @ApiOperation(value = "上传")
    @PostMapping("/upload")
    @ResponseBody
    public JsonResult upload(@RequestPart MultipartFile file, HttpServletRequest request)throws IOException {
        // 1. 保存到服务器 得到path
        String path = configModelService.getUploadPath(ConfigModel.dir_watermaker);
       /* String path = "D:\\";*/
        /*String path = request.getSession().getServletContext().getRealPath("path");*/
        String fileName =  file.getOriginalFilename();

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(path,fileName);
        //file.transferTo(f);
        FileUtils.copyInputStreamToFile(file.getInputStream(), f);
        String path1 = f.getAbsolutePath();

        String basedir = configModelService.getBaseUploadPath();
        String fileUrl = path.replace(basedir,"");
        if(File.separator.equals("\\")){
            fileUrl = fileUrl.replace("\\","/");
        }
        String url="/files"+fileUrl+"/"+fileName;

        //2 .保存一条数据到file表
        /*Watermark resultPicture = new Watermark();
        resultPicture.setWmkPath(dir.getPath());
        resultPicture.setWmkSign("1");*/
        //JsonResult<Picture> result= doGetById(getService(), p.getId());
        //Picture resultPicture = service.getById(p.getId());
/*
        File pictureFile = new File(resultPicture.getPath());
        FileChannel fc = null;
        if (pictureFile.exists() && pictureFile.isFile()) {
            try {
                FileInputStream fs = new FileInputStream(pictureFile);
                fc = fs.getChannel();
                // System.out.println(fc.size() + "-----fc.size()");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(pictureFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        String reso = width +"*"+height;
        //约分求比例
        if(width>height){
            int temp = height;
            height = width;
            width = temp;
        }
        */
       /* getService().insert(resultPicture);*/
        Map<String,String> map = new HashMap<>();
        map.put("url",url);
        map.put("path",path1);
        return renderSuccess(map);
    }

}
