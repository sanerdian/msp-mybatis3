package com.jnetdata.msp.resources.picture.controller;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.baidu.aip.speech.AipSpeech;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.model.Watermark;
import com.jnetdata.msp.resources.picture.service.PictureService;
import com.jnetdata.msp.resources.picture.service.WatermarkService;
import com.jnetdata.msp.resources.picture.vo.PictureVo;
import com.jnetdata.msp.resources.theclient.ProgramaClient;
import com.jnetdata.msp.resources.theclient.SiteClient;
import com.jnetdata.msp.resources.theclient.SystemClient;
import com.jnetdata.msp.util.FontUtil;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/resources/picture")
@ApiModel(value = "PictureController", description = "图片配置")
public class PictureController extends BaseController<Long, Picture> {
    @Autowired
    private PictureService service;

    @Autowired
    private WatermarkService watermarkService;

    @Autowired
    private SystemClient systemService;

    @Autowired
    private ProgramaClient programaService;

    @Autowired
    private SiteClient siteService;

    @Autowired
    private ConfigModelService configModelService;


    @ApiOperation(value = "获取本周新增资源数", notes = "获取本周新增资源数")
    @GetMapping(value = "/addThisWeek")
    @ResponseBody
    public JsonResult<Map> addThisWeek() {
        return renderSuccess(getService().addThisWeek());
    }

    @ApiOperation(value = "获取本月新增资源数", notes = "获取本月新增资源数")
    @GetMapping(value = "/addThisMonth")
    @ResponseBody
    public JsonResult<Map> addThisMonth() {
        return renderSuccess(getService().addThisMonth());
    }

    @ApiOperation(value = "获取本年新增资源数", notes = "获取本年新增资源数")
    @GetMapping(value = "/addThisYear")
    @ResponseBody
    public JsonResult<Map> addThisYear() {
        return renderSuccess(getService().addThisYear());
    }

    @ApiOperation(value = "添加图片信息", notes = "添加图片信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Picture entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes = "删除指定id对应的图片信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes = "根据多个id删除图片信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(), ids);
    }


    @ApiOperation(value = "修改", notes = "修改指定id对应的图片信息(只需要填图片中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                       @RequestBody Picture entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "修改", notes = "修改指定id对应的图片信息(只需要填图片中的id)")
    @PutMapping(value = "/edit/{id}")
    @ResponseBody
    public JsonResult<Picture> updateById1(@ApiParam("用户id") @PathVariable("id") Long id,
                                           @RequestBody Picture entity) {
        doUpdateById(getService(), id, entity);
        Picture p = service.getById(id);
        return renderSuccess(p);
    }

    @ApiOperation(value = "批量修改", notes="修改指定id对应的图片信息(只需要填图片中的id)")
    @PostMapping(value = "/updateByBatchId")
    @ResponseBody
    public JsonResult<Void> updateByBatchId(@RequestBody List<Picture> list) {
        service.updateBatchById(list);
        return renderSuccess();
    }

    @ApiOperation(value = "元数据文件关联", notes="修改指定id对应的图片信息(只需要填图片中的id)")
    @PostMapping(value = "/addToDoc")
    @ResponseBody
    public JsonResult<Void> addToDoc(@RequestBody List<Long> ids,String tableId,String dataId) {
        List<Picture> pictures = new ArrayList<>();
        for (Long id : ids) {
            Picture picture = new Picture();
            picture.setId(id);
            picture.setDataId(dataId);
            picture.setTableid(tableId);
            pictures.add(picture);
        }
        service.updateBatchById(pictures);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询", notes="查看指定图片id对应的图片信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Picture> getById(@ApiParam("图片信息id") @PathVariable("id") Long id) {

        return doGetById(getService(), id);
    }

    @ApiOperation(value = "查看对应id照相机信息", notes = "根据id查看照相机信息")
    @PostMapping("/getInfo")
    @ResponseBody
    public JsonResult<List<Map>> getInfo(@RequestParam("ids") String ids) throws Exception {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        List<Map> list = new ArrayList<>();

        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            File jpegFile = new File(picture.getPath());
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
            Directory dr = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            Map map = new HashMap();
            if (dr != null) {
                for (Tag tag : dr.getTags()) {
                    map.put(tag.getTagName(), tag.getDescription());
                    /*System.out.println(tag.getTagName() + ", " + tag.getDescription());*/
                }
            } else if (dr == null) {
                map.put(null, null);
            }
            list.add(map);
        }
        return renderSuccess(list);
    }

    @ApiOperation(value = "查看对应id图片详细信息", notes = "根据id查看图片详细信息")
    @PostMapping("/getPicInfo")
    @ResponseBody
    public JsonResult<List<Map>> getPicInfo(@RequestParam("ids") String ids) throws Exception {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        List<Map> list = new ArrayList<>();

        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            File jpegFile = new File(picture.getPath());//获取文件路径
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);//读取文件获取信息
            Directory dr = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Map map = new HashMap();
            if (dr != null) {
                for (Tag tag : dr.getTags()) {

                    map.put(tag.getTagName(), tag.getDescription());
                }
                list.add(map);
            } else if (dr == null) {
                map.put(null, null);
            }

        }
        return renderSuccess(list);
    }

    @ApiOperation(value = "根据实体属性查询", notes = "根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Picture>> list(@RequestBody PictureVo vo) {
        for (int i = 0; i < 10; i++) {
            System.out.println("i = " + i);
        }
        System.out.println(vo);
        return doList(getService(), vo.getPager(), vo.getPicture());
    }

    @ApiOperation(value = "非遗资源查询", notes = "非遗资源查询")
    @PostMapping("/resList")
    @ResponseBody
    public JsonResult<Pager<Picture>> resList(@RequestBody PictureVo vo) {
//        JsonResult<Pager<Picture>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getPicture());

        Pager<Picture> page = getService().resList(vo.getPager(), createCondition(vo.getPicture()));

//        List<Picture> picturePager = pagerJsonResult.getObj().getRecords();
//        for(Picture picture:picturePager){
//            ProResRelation proResRelation = proResRelationService.get(new PropertyWrapper<>(ProResRelation.class).eq("resid", picture.getId()));
//            if(proResRelation!=null){
//                Representativeinheritor representativeinheritor = representativeinheritorService.getById(proResRelation.getProid());
//                if(representativeinheritor!=null){
//                    Representativeitems representativeitems = representativeitemsService.getById(representativeinheritor.getItemno());
//                    if(representativeitems!=null){
//                        String inhertorname = representativeinheritor.getName();
//                        String proname = representativeitems.getProjectname();
//                        if(inhertorname!=null){
//                            picture.setInheritname(inhertorname);
//                        }if(proname!=null){
//                            picture.setRelationname(proname);
//                        }
//                    }
//                }
//            }
//        }
//        return pagerJsonResult;
        return renderSuccess(page);
    }

    @ApiOperation(value = "图片其他分辨率查询", notes = "图片其他分辨率查询")
    @PostMapping("/picotherreso")
    @ResponseBody
    public JsonResult<Pager<Picture>> picotherreso(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("reso", "1024*768,1600*1200,2048*1536").eq("sign", 0));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "图片其他比例查询", notes = "图片其他比例查询")
    @PostMapping("/picotherscale")
    @ResponseBody
    public JsonResult<Pager<Picture>> picother(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("scale", "16:9,4:3").eq("sign", 0));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "视频其他分辨率", notes = "视频其他分辨率查询")
    @PostMapping("/videootherreso")
    @ResponseBody
    public JsonResult<Pager<Picture>> videootherreso(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("height", "1080,720,480,360").eq("sign", 1));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "视频其他格式", notes = "视频其他格式查询")
    @PostMapping("/videootherform")
    @ResponseBody
    public JsonResult<Pager<Picture>> videootherform(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("form", "avi,mp4,dvr,vcd,mov,vob,dvd").eq("sign", 1));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "视频其他比例", notes = "视频其他比例查询")
    @PostMapping("/videootherscale")
    @ResponseBody
    public JsonResult<Pager<Picture>> videootherscale(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("scale", "16:9,4:3").eq("sign", 1));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "音频其他格式", notes = "音频其他格式")
    @PostMapping("/radiootherform")
    @ResponseBody
    public JsonResult<Pager<Picture>> radiootherform(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("form", "mp3,mwa,wav,asf,aac").eq("sign", 3));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "文件其他格式", notes = "文件其他格式")
    @PostMapping("/fileotherform")
    @ResponseBody
    public JsonResult<Pager<Picture>> fileotherform(@RequestBody PictureVo vo) {

        Pager<Picture> picturePager = service.list(new Pager<Picture>(vo.getPager().getCurrent(), vo.getPager().getSize()), new PropertyWrapper(Picture.class).notIn("form", "txt,doc,docx,ppt,xls,xlsx").eq("sign", 2));

        return renderSuccess(picturePager);
    }

    @ApiOperation(value = "修改统一信息", notes = "修改统一信息")
    @PostMapping("/editAll")
    @ResponseBody

    public JsonResult<Void> editAll(@RequestBody Picture entity, @RequestParam("ids") String ids, @RequestBody String form) throws Exception {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            String path = picture.getPath();
            File f = new File(path);
            f.canRead();

            BufferedImage src = ImageIO.read(f);
            String formatPath = picture.getPath();
            String formatName = formatPath.substring(0, formatPath.lastIndexOf(".")) + "." + form;
            ImageIO.write(src, formatName, new File(formatName));

        }
        return renderSuccess();
    }

    @ApiOperation(value = "改变审核状态", notes = "改变审核状态")
    @PostMapping("/changeStatus")
    @ResponseBody
    public JsonResult<Void> changeStatus(@RequestParam("ids") String ids, @RequestParam int status) {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList();
        List<Picture> pictureList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            picture.setStatus(status);
            updateById(idList.get(j), picture);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "添加统一信息", notes = "添加统一信息")
    @PostMapping("/addAll")
    @ResponseBody
    public JsonResult<Void> addAll(@RequestBody Picture entity, @RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList();
        List<Picture> pictureList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            picture.setCrUser(entity.getCrUser());
            picture.setContentType(entity.getContentType());
            picture.setCopyright(entity.getCopyright());
            picture.setResc(entity.getResc());
            picture.setFdesc(entity.getFdesc());
            service.updateById(picture);
        }
        return renderSuccess();
    }

    @ApiOperation(value = "是否收藏", notes = "是否收藏")
    @PostMapping("/changeCollect")
    @ResponseBody
    private JsonResult<Void> changeCollect(@RequestParam Long id) {
        Picture picture = service.getById(id);
        int sign = picture.getCollection();
        if (sign == 1) {
            picture.setCollection(0);
        } else if (sign == 0) {
            picture.setCollection(1);
        }
        return updateById(id, picture);
    }

    @ApiOperation(value = "文件转PDF预览", notes = "文件转PDF预览")
    @PostMapping(value = "/transToPDF")
    @ResponseBody
    public JsonResult<List<Map>> transToPDF(@RequestParam("ids") String ids) throws Exception {
        String[] s = ids.split(",");
        List<Long> list = new ArrayList<>();
        List<Map> list1 = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            list.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < list.size(); j++) {
            Picture picture = service.getById(list.get(j));
            String path = picture.getPath();
            String type = "." + picture.getForm();
            Long id = picture.getId();
            String toFilePath = configModelService.getUploadPath(ConfigModel.dir_file);
            File file = null;
            FileInputStream fileInputStream = null;
            file = new File(path);
            fileInputStream = new FileInputStream(file);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timesuffix = sdf.format(date);
            String docFileName = null;
            String htmFileName = null;
            if (".doc".equals(type)) {
                docFileName = "doc_" + timesuffix + ".doc";
                htmFileName = "doc_" + timesuffix + ".pdf";
            } else if (".docx".equals(type)) {
                docFileName = "docx_" + timesuffix + ".docx";
                htmFileName = "docx_" + timesuffix + ".pdf";
            } else if (".xls".equals(type)) {
                docFileName = "xls_" + timesuffix + ".xls";
                htmFileName = "xls_" + timesuffix + ".pdf";
            } else if (".ppt".equals(type)) {
                docFileName = "ppt_" + timesuffix + ".ppt";
                htmFileName = "ppt_" + timesuffix + ".pdf";
            } else if (".pptx".equals(type)) {
                docFileName = "pptx_" + timesuffix + ".pptx";
                htmFileName = "pptx_" + timesuffix + ".pdf";
            } else if (".xlsx".equals(type)) {
                docFileName = "xlsx_" + timesuffix + ".xlsx";
                htmFileName = "xlsx_" + timesuffix + ".pdf";
            } else {
                java.lang.System.out.println("Trans fail");
                return null;
            }
            File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
            File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
            if (htmlOutputFile.exists()) {
                htmlOutputFile.delete();
                htmlOutputFile.createNewFile();
            }
            if (docInputFile.exists()) {
                docInputFile.delete();
                docInputFile.createNewFile();
            }
            /*** 由fromFileInputStream构建输入文件  */
            try {
                OutputStream os = new FileOutputStream(docInputFile);
                int bytesRead = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                fileInputStream.close();
            } catch (IOException e) {
            }
            // 连接服务
            Process process = null;
            OpenOfficeConnection connection = null;
            String command = "soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard &";
            try {
                if (File.separator.equals("\\")) {
                    process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
                } else {
                    process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
                }
                /*创建OpenOffice连接*/
                connection = new SocketOpenOfficeConnection(8100);
            //OpenOfficeConnection connection = new SocketOpenOfficeConnection("47.93.151.74",8203);
            //try {
                /*连接*/
                connection.connect();
            //} catch (ConnectException e) {
            //    java.lang.System.err.println("error:Please check if OpenOffice service is started");
            //}
            // convert 转换
                DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
                converter.convert(docInputFile, htmlOutputFile);
            } catch (ConnectException e) {
                java.lang.System.err.println("error:Please check if OpenOffice service is started");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (process != null) {
                    process.destroy();
                }
            }
            // 转换完之后删除word文件
            docInputFile.delete();
            Picture picture1 = new Picture();
            String newpath = toFilePath + File.separator + htmFileName;
            picture1.setPath(toFilePath+"/"+htmFileName);
            picture1.setResParentId(id);
            picture1.setName(picture.getName());
            picture1.setPdfSign(0);
            picture1.setForm("pdf");

            String basedir = configModelService.getBaseUploadPath();
            newpath = newpath.replace(basedir,"");
            if(File.separator.equals("\\")){
                newpath = newpath.replace("\\","/");
            }
            String url="/files" + newpath;

//            picture1.setUrl(url);
//            service.insert(picture1);
            picture.setPdfSign(200);
            picture.setPdfUrl(url);
            service.updateById(picture);
            Map map = new HashMap();
            map.put("url", url);
            list1.add(map);
        }
        return renderSuccess(list1);
    }

    @ApiOperation(value = "pdf加水印", notes = "PDF加水印")
    @PostMapping(value = "/pdfAddWatermark")
    @ResponseBody
    public JsonResult<Void> pdfAddWatermark(@RequestParam("ids") String ids, @RequestParam Long id) throws Exception {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        Watermark watermark = watermarkService.getById(id);
        String waterMarkName = watermark.getContent();
        String font = watermark.getInputFont();
        float alpha = watermark.getAlpha() / 100f;
        int red = watermark.getRed();
        int green = watermark.getGreen();
        int blue = watermark.getBlue();
        int size = watermark.getFontSize();
        int location = watermark.getLocation();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));

            String fileName = picture.getPath();
            String toFilePath = fileName.substring(0, fileName.lastIndexOf("/") + 1) + UUID.randomUUID() + picture.getName() + "." + picture.getForm();
            Picture picture1 = new Picture();
            picture1.setResParentId(idList.get(j));

            picture1.setPath(toFilePath);
            picture1.setName(picture.getName());
            picture1.setForm("pdf");
            String url = "/files" + "/" + toFilePath.substring(toFilePath.lastIndexOf("/", fileName.lastIndexOf("/") - 1) + 1);
            picture1.setUrl(picture.getUrl());

            service.insert(picture1);
            Picture picture2 = service.getById(picture.getResParentId());
            picture2.setPdfSign(100);
            picture2.setPdfUrl(url);
            service.updateById(picture2);
            picture.setUrl(url);
            if (picture.getPdfSign() == 1) {
                picture.setPdfSign(0);
            } else {
                picture.setPdfSign(1);
            }
            service.updateById(picture);
            int num = 0;
            PdfReader reader = new PdfReader(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(toFilePath)));

            PdfStamper stamper = new PdfStamper(reader, bos);
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            BaseFont base = null;
            if (font.equals("黑体")) {
                base = BaseFont.createFont("fonts/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } else if (font.equals("宋体")) {
                base = BaseFont.createFont("fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } else if (font.equals("仿宋")) {
                base = BaseFont.createFont("fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } else if (font.equals("楷体")) {
                base = BaseFont.createFont("fonts/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } else if (font.equals("默认")) {
                base = BaseFont.createFont("fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }

            /*BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.EMBEDDED);*/
            //BaseFont base = BaseFont.createFont("/data/tmis/uploads/file/font/simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                content = stamper.getOverContent(i);// 在内容上方加水印
                // content = stamper.getUnderContent(i);// 在内容下方加水印
                gs.setFillOpacity(alpha);
                content.setGState(gs);
                pageRect = stamper.getReader().getPageSizeWithRotation(i);
                float x;
                float y;
                /*开始写入文本*/
                content.beginText();
                content.setRGBColorFill(red, green, blue);
                content.setFontAndSize(base, size);
                float contentlength = content.getEffectiveStringWidth(waterMarkName, false);
                /*content.setTextMatrix(100, 250);*/
                if (location == 1) {
                    x = 0;
                    y = pageRect.getHeight() - size;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 2) {
                    x = pageRect.getWidth() / 2 - contentlength / 2;
                    y = pageRect.getHeight() - size;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 3) {
                    x = pageRect.getWidth() - contentlength;
                    y = pageRect.getHeight() - size;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 4) {
                    x = 0;
                    y = pageRect.getHeight() / 2;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 5) {
                    x = pageRect.getWidth() / 2 - contentlength / 2;
                    y = pageRect.getHeight() / 2;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 6) {
                    x = pageRect.getWidth() - contentlength;
                    y = pageRect.getHeight() / 2;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 7) {
                    x = 0;
                    y = 5;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 8) {
                    x = pageRect.getWidth() / 2 - contentlength / 2;
                    y = 5;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                } else if (location == 9) {
                    x = pageRect.getWidth() - contentlength;
                    y = 5;
                    content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, 0);
                }

            }
            stamper.close();

        }
        return renderSuccess();
    }

    @ApiOperation(value = "添加文字水印", notes = "添加文字水印")
    @PostMapping(value = "/addWaterMark")
    @ResponseBody
    public JsonResult<String> addWaterMark(@RequestParam("ids") String ids, @RequestParam Long markid) throws IOException {
        Watermark watermark = watermarkService.getById(markid);
        List<String> resId = new ArrayList<>();
        String residString;
        String inputfont = watermark.getInputFont();
        int red = watermark.getRed();
        int green = watermark.getGreen();
        int blue = watermark.getBlue();
        int size = watermark.getFontSize();
        int alpha = watermark.getAlpha();
        int location = watermark.getLocation();

        String waterMarkContent = watermark.getContent();
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            Long l = Long.parseLong(s[i]);
            Picture picture = service.getById(l);
            Long addId = null;
            if (picture.getResParentId() != 0) {
                addId = picture.getResParentId();
                picture.setIsNew(0);
                service.updateById(picture);
            } else {
                addId = Long.parseLong(s[i]);
            }
            idList.add(addId);
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            String path = picture.getPath();
            File f = new File(path);
            Image srcImg = ImageIO.read(f);
            /*获取图片的宽*/
            int srcImgWidth = srcImg.getWidth(null);
            /*获取图片的高*/
            int srcImgHeight = srcImg.getHeight(null);

            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            // Font font = new Font("Courier New", Font.PLAIN, 12);
//            Font font = new Font(inputfont, Font.BOLD,size);
            Font font = FontUtil.getFont();

            Color markContentColor = new Color(red, green, blue, alpha);
            g.setColor(markContentColor); // 根据图片的背景设置水印颜色
            g.setFont(font);
            if (location == 1) {
                int x = 10;
                int y = 25;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 2) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int x = srcImgWidth - srcImgWidth / 2 - x1 / 2;
                int y = 25;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 3) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int x = srcImgWidth - x1 - 10;
                int y = 25;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 4) {
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = 10;
                int y = srcImgHeight - srcImgHeight / 2 + y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 5) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = srcImgWidth - srcImgWidth / 2 - x1 / 2;
                int y = srcImgHeight - srcImgHeight / 2 + y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 6) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = srcImgWidth - x1 - 10;
                int y = srcImgHeight - srcImgHeight / 2 + y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 7) {
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = 10;
                int y = srcImgHeight - y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 8) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = srcImgWidth - srcImgWidth / 2 - x1 / 2;
                int y = srcImgHeight - y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            } else if (location == 9) {
                int x1 = g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
                int y1 = g.getFontMetrics(g.getFont()).getHeight();
                int x = srcImgWidth - x1 - 10;
                int y = srcImgHeight - y1 / 2;
                g.drawString(waterMarkContent, x, y);
                g.dispose();
            }

            // 输出图片
            String name = UUID.randomUUID() + "." + picture.getForm();
            String newPath = path.substring(0, path.lastIndexOf(File.separator) + 1) + name;
            FileOutputStream outImgStream = new FileOutputStream(newPath);
            ImageIO.write(bufImg, picture.getForm(), outImgStream);
            Picture wmkPicture = new Picture();
            wmkPicture.setPath(newPath);
            //为0不在列表中显示
            picture.setIsNew(0);

            wmkPicture.setColumnId(picture.getColumnId());
            wmkPicture.setIsNew(1);
            wmkPicture.setName(picture.getName());
            wmkPicture.setResc("默认来源");
            wmkPicture.setForm(picture.getForm());
            wmkPicture.setDatetime(new Date());
            wmkPicture.setReso(picture.getReso());
            wmkPicture.setSign(0);
            wmkPicture.setScale(picture.getScale());
            wmkPicture.setColumnId(picture.getColumnId());
            wmkPicture.setCopyright("默认版权");
            wmkPicture.setFsize(picture.getFsize());
            wmkPicture.setOtherCp("其他");
            wmkPicture.setResParentId(idList.get(j));

            java.lang.System.out.println("=============================:" + newPath);

            String newurl = newPath.substring(newPath.lastIndexOf(File.separator, path.lastIndexOf(File.separator) - 1) + 1);

            String url = "/files" + "/" + newurl.replace("\\", "/");

            java.lang.System.out.println("=============================:" + url);

//            String url=newPath.substring(newPath.lastIndexOf("\\")+1);
            wmkPicture.setUrl(url);
            service.updateById(picture);
            service.insert(wmkPicture);
            resId.add(wmkPicture.getId().toString());
            outImgStream.flush();
            outImgStream.close();
        }
        residString = String.join(",", resId);
        return renderSuccess(residString);
    }


    @ApiOperation(value = "添加图片水印", notes = "添加图片水印")
    @PostMapping("/addPictWaterMark")
    @ResponseBody
    private JsonResult<Void> addPicWaterMark(@RequestParam("ids") String ids, @RequestParam Long markid) {
        OutputStream os = null;
        Watermark watermark = watermarkService.getById(markid);
        int location = watermark.getLocation();
        float alpha = watermark.getPicalpha() / 10;
        int x = 0;
        int y = 0;
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        for (String id : ids.split(",")) {
            Long l = Long.parseLong(id);
            Picture picture = service.getById(l);
            Long addId = null;
            if (picture.getResParentId() != 0) {
                addId = picture.getResParentId();
                picture.setIsNew(0);
                pictureList.add(picture);
            } else {
                addId = Long.parseLong(id);
            }
            idList.add(addId);
        }
        for (int j = 0; j < idList.size(); j++) {
            try {
                Picture picture = service.getById(idList.get(j));
                String path = picture.getPath();
                String srcImgPath = picture.getPath();
                Image srcImg = ImageIO.read(new File(srcImgPath));
                int sw = srcImg.getWidth(null);
                int sh = srcImg.getHeight(null);

                BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);

                // 得到画笔对象
                // Graphics g= buffImg.getGraphics();
                Graphics2D g = buffImg.createGraphics();

                // 设置对线段的锯齿状边缘处理
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
                        0, null);

                /*if (null != degree) {
                    // 设置水印旋转
                    g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
                }*/

                // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
                String ss = watermark.getWmkPath();
                ImageIcon imgIcon = new ImageIcon(ss);
                int iw = imgIcon.getIconWidth();
                int ih = imgIcon.getIconHeight();
                if (iw >= sw && ih >= sh) {
                    return renderError("水印图片过大，请重新上传较小图片");
                }
                // 得到Image对象。
                Image img = imgIcon.getImage();

                // 透明度
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

                // 表示水印图片的位置


                if (location == 1) {
                    x = 5;
                    y = 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 2) {
                    x = sw / 2 - iw / 2;
                    y = 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 3) {
                    x = sw - iw - 5;
                    y = 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 4) {
                    x = 5;
                    y = sh / 2 - ih / 2;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 5) {
                    x = sw / 2 - iw / 2;
                    y = sh / 2 - ih / 2;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 6) {
                    x = sw - iw - 5;
                    y = sh / 2 - ih / 2;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 7) {
                    x = 5;
                    y = sh - ih - 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 8) {
                    x = sw / 2 - iw / 2;
                    y = sh - ih - 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                } else if (location == 9) {
                    x = sw - iw - 5;
                    y = sh - ih - 5;
                    g.drawImage(img, x, y, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g.dispose();
                }
                String newPath = path.substring(0, path.lastIndexOf(File.separator) + 1) + UUID.randomUUID() + "." + picture.getForm();
                Picture resPicture = new Picture();
                String newurl = newPath.substring(newPath.lastIndexOf(File.separator, path.lastIndexOf(File.separator) - 1) + 1);
                String url = "/files" + "/" + newurl.replace("\\", "/");

                resPicture.setUrl(url);
                resPicture.setPath(newPath);
                resPicture.setName(picture.getName());
                resPicture.setColumnId(picture.getColumnId());
                resPicture.setIsNew(1);
                resPicture.setForm(picture.getForm());
                resPicture.setDatetime(new Date());
                resPicture.setSign(0);
                resPicture.setResParentId(idList.get(j));
                picture.setIsNew(0);
                os = new FileOutputStream(newPath);

                // 生成图片
                ImageIO.write(buffImg, "JPG", os);

                int data = buffImg.getData().getDataBuffer().getSize();
                File file = new File(newPath);
                BigDecimal bd = new BigDecimal((double) file.length() / 1024);
                double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                resPicture.setFsize(size);

                service.insert(resPicture);
                service.updateById(picture);
                service.updateBatchById(pictureList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return renderSuccess();
    }

  /*  @ApiOperation(value = "格式设置",notes = "格式设置")
    @PostMapping("/formString")
    @ResponseBody
    public JsonResult<String> formString (){
        return renderSuccess(watermark.getSupForm());
    }

    @ApiOperation(value = "大小设置",notes = "大小设置")
    @PostMapping("/supSize")
    @ResponseBody
    public JsonResult<Double> getSupSize(){
        val user = SessionUser.getCurrentUser();
        Watermark watermark = watermarkService.getById(user.getWmkId());
        return renderSuccess(watermark.getUpsize());
    }*/

    @ApiOperation(value = "图片裁剪", notes = "图片裁剪")
    @PostMapping("/picCut")
    @ResponseBody
    public JsonResult<String> picCut() {
        BufferedImage image = new BufferedImage(3, 4, BufferedImage.TYPE_INT_RGB);
        return JsonResult.renderSuccess();
    }


    @ApiOperation(value = "图片压缩", notes = "图片压缩")
    @PostMapping("/picCompress")
    @ResponseBody
    public JsonResult<String> picCompress(@RequestParam("ids") String ids, @RequestParam int w, @RequestParam int h) {
        String newPath1 = null;
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List<Picture> pictureList = new ArrayList<>();
        List<String> resList = new ArrayList<>();
        String resString;
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            try {
                Picture picture = service.getById(idList.get(j));
                String path = picture.getPath();
                String url = picture.getUrl();
                File f = new File(path);
                Image srcImg = ImageIO.read(f);
                int imageWidth = srcImg.getWidth(null);
                int imageHeight = srcImg.getHeight(null);
                if (imageWidth >= imageHeight) {
                    w = w;
                    h = (int) Math.round((imageHeight * w * 1.0 / imageWidth));
                } else {
                    h = h;
                    w = (int) Math.round((imageWidth * h * 1.0 / imageHeight));
                }

                //构建图片对象
                BufferedImage _image = new BufferedImage(w, h,
                        BufferedImage.TYPE_INT_RGB);
                //绘制缩小后的图
                _image.getGraphics().drawImage(srcImg, 0, 0, w, h, null);
                //输出到文件流
                //Picture cpsPicture = new Picture();
                //String newPath = path.substring(0, path.lastIndexOf("/") + 1) + UUID.randomUUID() + picture.getName() + "." + picture.getForm();
                //String url = "/files" + "/" + newPath.substring(newPath.lastIndexOf("/", path.lastIndexOf("/") - 1) + 1);
                //cpsPicture.setPath(newPath);
                //newPath1 = newPath;
                String imgName = UUID.randomUUID() + picture.getName() + "." + picture.getForm();
                String newPath = path.substring(0, path.lastIndexOf(File.separator) + 1) + imgName;
                String newUrl = url.substring(0, url.lastIndexOf("/") + 1) + imgName;
                picture.setCpsPath(newUrl);
                resList.add(newUrl);
                //cpsPicture.setName(picture.getName());
                //cpsPicture.setForm(picture.getForm());
                //cpsPicture.setDatetime(picture.getDatetime());
                //cpsPicture.setOtherCp("其他");
                //cpsPicture.setCopyright("默认版权");
                //cpsPicture.setColumnId(picture.getColumnId());
                //cpsPicture.setResc("默认来源");
                //cpsPicture.setUrl(url);
                //cpsPicture.setSign(0);
                //cpsPicture.setResParentId(idList.get(j));
                //cpsPicture.setIsNew(1);
                //picture.setIsNew(0);
                FileOutputStream out = new FileOutputStream(newPath);

                ImageIO.write(_image, "jpeg", out);
                //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                //encoder.encode(_image);
                //service.insert(cpsPicture);
                service.updateById(picture);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resString = String.join(",", resList);
        return renderSuccess(resString);

    }

    private PictureService getService() {
        return service;
    }

    @ApiOperation(value = "下载")
    @GetMapping("/downLoad")
    public void downLoad(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Picture picture = getService().getById(id);
        String filename = picture.getName() + "." + picture.getForm();
        String path = picture.getPath();

        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        //转码，免得文件名中文乱码

        //设置文件下载头

        response.setHeader("Content-Disposition", "attachment;filename=" + (new String(filename.getBytes(), "ISO-8859-1")));

        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = bis.read(bytes)) != -1) {
            out.write(bytes, 0, len);
            /* out.write(len);*/
            out.flush();
        }
        out.close();
    }

    @ApiOperation(value = "上传")
    @PostMapping("/uploadother")
    @ResponseBody
    public Picture uploadother(@RequestPart MultipartFile file, HttpServletRequest request, @RequestParam("id") Long id) throws IOException {
        // 1. 保存到服务器 得到path
//        val user = SessionUser.getCurrentUser();
//        String userName = user.getName();

        String path = systemService.getById(1167395202814476290L).getValue();
        //String path = "D:\\upload";
        /*String path = request.getSession().getServletContext().getRealPath("path");*/
        String fileName = UUID.randomUUID() + file.getOriginalFilename();

        File dir = new File(path, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //file.transferTo(dir);
        FileUtils.copyInputStreamToFile(file.getInputStream(),dir);
        //2 .保存一条数据到file表
        Picture resultPicture = new Picture();
        resultPicture.setPath(dir.getPath());
       /* String url="/files"+path.substring(path.lastIndexOf("/"))+"/"+fileName;
        resultPicture.setUrl(url);*/
        //JsonResult<Picture> result= doGetById(getService(), p.getId());
        //Picture resultPicture = service.getById(p.getId());

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

        int dot = pictureFile.getName().lastIndexOf('.');
        if ((dot > -1) && (dot < (pictureFile.getName().length() - 1))) {
            String pictureForm = pictureFile.getName().substring(dot + 1);
            resultPicture.setForm(pictureForm);

            BigDecimal bd = new BigDecimal((double) pictureFile.length() / 1024);
            double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultPicture.setFsize(size);
            resultPicture.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            Date date = new Date();
            resultPicture.setDatetime(date);
            Programa programa = programaService.getById(id);
            if (programa.getParentId() != 0) {
                resultPicture.setParentId(programa.getParentId());
            }
            if (programa.getSiteId() != null) {
                resultPicture.setSiteId(programa.getSiteId());
                Site site = siteService.getById(programa.getSiteId());
                resultPicture.setCompanyId(site.getCompanyId());
            }
            resultPicture.setColumnId(id);
            resultPicture.setResParentId(0L);
            resultPicture.setIsNew(1);
//            resultPicture.setCrUser(userName);
            resultPicture.setOtherCp("其他");

            resultPicture.setCopyright("默认版权");
          /*  if(!copyRight.equals("原创")&&!copyRight.equals("经授权")){
                resultPicture.setOtherCp("其他");
            }*/
            resultPicture.setStatus(0);
            resultPicture.setCreateTime(new Date());
            resultPicture.setCollection(0);
        }

        getService().insert(resultPicture);
        return resultPicture;
    }

    //图片上传
    @ApiOperation(value = "图片上传")
    @PostMapping("/upload")
    @ResponseBody
    public Picture upload(@RequestPart MultipartFile file, HttpServletRequest request, @RequestParam("id") Long id, @RequestParam(value = "w",required = false,defaultValue = "0") int w, @RequestParam(value = "h",required = false,defaultValue = "0") int h, @RequestParam(value = "keyword",required = false) String keyword, @RequestParam(value = "customKeyword",required = false) String customKeyword) throws IOException, JpegProcessingException {
        // 1. 保存到服务器 得到path
//        val user = SessionUser.getCurrentUser();
//        String userName = user.getName()
        String userName = "admin";

        //获取路径
        String path = configModelService.getUploadPath(ConfigModel.dir_pic);
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

        //创建一个File文件，并判断路径是否存在，如果不存在就创建
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path, fileName);
        System.out.println("地址为："+path+fileName);
        //file.transferTo(f);

        //图片的上传
        FileUtils.copyInputStreamToFile(file.getInputStream(),f);

        //2 .保存一条数据到file表
        Picture resultPicture = new Picture();
        resultPicture.setPath(f.getPath());
        String basePath = configModelService.getBaseUploadPath();

        String fileUrl = path.replace(basePath, "");
        fileUrl = fileUrl.replace("\\", "/");
        String url = "/files" + fileUrl + "/" + fileName;
        resultPicture.setUrl(url);

        File pictureFile = new File(resultPicture.getPath());
        //FileChannel fc = null;
        //if (pictureFile.exists() && pictureFile.isFile()) {
        //    try {
        //        FileInputStream fs = new FileInputStream(pictureFile);
        //        fc = fs.getChannel();
        //    } catch (FileNotFoundException e) {
        //        e.printStackTrace();
        //    }
        //}

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(pictureFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        resultPicture.setWidth(width);
        resultPicture.setHeight(height);
        String reso = width + "*" + height;
        if (height > width) {
            resultPicture.setPicSign(1);//竖图
        }
        //约分求比例
        if (width > height) {
            resultPicture.setPicSign(0);//横图
            int temp = height;
            height = width;
            width = temp;
        }

        for (int i = width; i >= 1; i--) {
            if (width % i == 0 && height % i == 0) {
                int yueshu = i;
                String scale = height / yueshu + ":" + width / yueshu;
                resultPicture.setScale(scale);
                break;
            }
        }


        int dot = pictureFile.getName().lastIndexOf('.');
        extracted(file, id, keyword, customKeyword, userName, resultPicture, pictureFile, reso, dot);

        getService().insert(resultPicture);
        JsonResult<String> j = picCompress(Long.toString(resultPicture.getId()), w, h);
        System.out.println(j.getMsg());
        return resultPicture;
    }

    private void extracted(MultipartFile file, Long id, String keyword, String customKeyword, String userName, Picture resultPicture, File pictureFile, String reso, int dot) throws JpegProcessingException, IOException {
        if ((dot > -1) && (dot < (pictureFile.getName().length() - 1))) {
            String pictureForm = pictureFile.getName().substring(dot + 1);


            //获取相机信息
            Map<String, String> map = new HashMap<>();
            map.put("Make", "生产者");
            map.put("Model", "型号");
            map.put("Orientation", "方向");
            map.put("Software", "软件");
            map.put("Date/Time", "时间");
            map.put("YCbCr Positioning", "色相定位");
            map.put("Exposure Time", "曝光时间");
            map.put("F-Number", "光圈系数");
            map.put("ISO Speed Ratings", "感光度");
            map.put("Exif Version", "版本");
            map.put("Exposure Bias Value", "曝光补偿");
            map.put("Max Aperture Value", "最大光圈");
            map.put("Metering Mode", "测光方式");
            map.put("Light source", "光源");
            map.put("Flash", "是否使用闪光灯");
            map.put("Focal Length", "焦距");
            map.put("FlashPix Version", "版本");
            map.put("Color Space", "色域");
            map.put("Exif Image Width", "图像宽度");
            map.put("Exif Image Height", "图像高度");
            map.put("GPS Latitude", "纬度");
            map.put("GPS Longitude", "经度");

            Map<String, String> infoMap = new HashMap<>();

            if (pictureForm.equals("jpg") || pictureForm.equals("JPG")) {
                Metadata metadata = JpegMetadataReader.readMetadata(pictureFile);
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag);
                        if (map.containsKey(tag.getTagName()) && !StringUtils.isEmpty(tag.getDescription())) {
                            infoMap.put(map.get(tag.getTagName()), tag.getDescription());
                        }
                    }
                }
            }

            //添加参数
            resultPicture.setTakeTime(infoMap.get("时间"));
            resultPicture.setLatitude(infoMap.get("纬度"));
            resultPicture.setLongitude(infoMap.get("经度"));
            if (infoMap.containsKey("时间")) infoMap.remove("时间");
            if (infoMap.containsKey("纬度")) infoMap.remove("纬度");
            if (infoMap.containsKey("经度")) infoMap.remove("经度");

            JSONObject jsonObject = new JSONObject(infoMap);
            resultPicture.setImginfo(jsonObject.toString());
            resultPicture.setForm(pictureForm);

            BigDecimal bd = new BigDecimal((double) pictureFile.length() / 1024);
            double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultPicture.setFsize(size);
            resultPicture.setReso(reso);
            resultPicture.setSign(0);
            resultPicture.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            Date date = new Date();
            resultPicture.setDatetime(date);
            Programa programa = programaService.getById(id);
            if (programa.getParentId() != 0) {
                resultPicture.setParentId(programa.getParentId());
            }
            if (programa.getSiteId() != null) {
                resultPicture.setSiteId(programa.getSiteId());
                Site site = siteService.getById(programa.getSiteId());
                resultPicture.setCompanyId(site.getCompanyId());
            }
            resultPicture.setColumnId(id);
            resultPicture.setResParentId(0L);
            resultPicture.setIsNew(1);
            resultPicture.setCrUser(userName);
            resultPicture.setOtherCp("其他");
            resultPicture.setResc("默认来源");
            resultPicture.setCopyright("默认版权");
          /*  if(!copyRight.equals("原创")&&!copyRight.equals("经授权")){
                resultPicture.setOtherCp("其他");
            }*/
            resultPicture.setStatus(0);
            resultPicture.setCreateTime(new Date());
            resultPicture.setCollection(0);
            resultPicture.setKeyword(keyword);
            resultPicture.setCustomKeyword(customKeyword);
        }
    }

    //视频上传
    @ApiOperation(value = "视频上传")
    @PostMapping("/videoupload")
    @ResponseBody
    public JsonResult<Picture> vedioupload(@RequestPart MultipartFile file, HttpServletRequest request, @RequestParam("id") Long id) throws Exception {
        // 1. 保存到服务器 得到path
        String path = configModelService.getUploadPath(ConfigModel.dir_video);
        System.out.println("----------"+path);
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(path, fileName);
        //file.transferTo(f);
        FileUtils.copyInputStreamToFile(file.getInputStream(),f);
        //2 .保存一条数据到file表
        Picture resultPicture = new Picture();
        resultPicture.setPath(f.getPath());

        File pictureFile = new File(resultPicture.getPath());

//        Encoder encoder = new Encoder();
        String hour = null;
        String min = null;
        String sec = null;
        Integer height = null;
        Integer width = null;
        String codeStyle = null;
        Integer sampleRate = null;
        Integer bitRate = null;
        String audioNum = null;
        int dot = pictureFile.getName().lastIndexOf('.');
        if ((dot > -1) && (dot < (pictureFile.getName().length() - 1))) {
            String pictureForm = pictureFile.getName().substring(dot + 1);
            if (!pictureForm.equals("mp4") && !pictureForm.equals("avi") && !pictureForm.equals("mpg") && !pictureForm.equals("mkv")) {
                resultPicture.setOtherForm("其他");
            }
            resultPicture.setForm(pictureForm);

            BigDecimal bd = new BigDecimal((double) pictureFile.length() / 1024);
            double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            resultPicture.setFsize(size);
            Programa programa = programaService.getById(id);
            if (programa.getParentId() != 0) {
                resultPicture.setParentId(programa.getParentId());
            }
            if (programa.getSiteId() != null) {
                resultPicture.setSiteId(programa.getSiteId());
               Site site = siteService.getById(programa.getSiteId());
                resultPicture.setCompanyId(site.getCompanyId());
            }
            Date date = new Date();
            resultPicture.setDatetime(date);
            resultPicture.setSign(1);
//            String url="/files/"+path.substring(path.lastIndexOf(File.separator))+"/"+fileName;
            String basePath = configModelService.getBaseUploadPath();
            String fileUrl = path.replace(basePath, "");
            fileUrl = fileUrl.replace("\\", "/");
            String url = "/files" + fileUrl + "/" + fileName;
            resultPicture.setUrl(url);
            resultPicture.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            resultPicture.setColumnId(id);
            resultPicture.setCopyright("默认版权");
            resultPicture.setResc("默认来源");
            resultPicture.setOtherCp("其他");
            resultPicture.setResParentId((long) 0);
            resultPicture.setCreateTime(new Date());
            resultPicture.setCollection(0);
            resultPicture.setIsNew(1);
//            resultPicture.setCrUser(user.getName());
            resultPicture.setStatus(0);
        }
        String filename = f.getPath();
        ArrayList<String> commend = new ArrayList<>();
        commend.add("ffmpeg");
        commend.add("-i");
        commend.add(filename);
        commend.add("-hide_banner");
        StringBuilder result = new StringBuilder();
        String duration = null;
        String video = null;
        String audio = null;
        try { // 获得视频信息
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(commend);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            process.waitFor();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append('\n');
                if (line.contains("Duration")) {
                    duration = line;
                } else if (line.contains("Video")) {
                    video = line;
                } else if (line.contains("Audio")) {
                    audio = line;
                }
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result + "\nduration = " + duration + "\nvideo = " + video + "\naudio = " + audio);

        if (result.length() != 0) {
            String durationRegex ="Duration: (\\d{2}):(\\d{2}):(\\d{2}).*bitrate: (\\d*) kb/s";
            String videoRegex = "Video: (.*?) .*, (^0|[1-9][0-9]*)x(\\d+)";
            String audioRegex = "Audio:.*, (\\d*) Hz, (.*?),";
            Matcher durationMatcher = Pattern.compile(durationRegex).matcher(duration);
            Matcher videoMatcher = Pattern.compile(videoRegex).matcher(video);
            Matcher audioMatcher = Pattern.compile(audioRegex).matcher(audio);
            if (durationMatcher.find()) {
                hour = durationMatcher.group(1);
                min = durationMatcher.group(2);
                sec = durationMatcher.group(3);
                bitRate = Integer.parseInt(durationMatcher.group(4));
            }
            if (videoMatcher.find()) {
                codeStyle = videoMatcher.group(1);
                width = Integer.parseInt(videoMatcher.group(2));
                height = Integer.parseInt(videoMatcher.group(3));
            }
            if (audioMatcher.find()) {
                sampleRate = Integer.parseInt(audioMatcher.group(1));
                audioNum = audioMatcher.group(1);
                if (!(audioNum.equals("stereo")||audioNum.equals("mono"))) {
                    audioNum = "未知";
                }
            }
            resultPicture.setReso(width + "*" + height);
            resultPicture.setHeight(height);
            resultPicture.setWidth(width);
            String res = hour + ":" + min + ":" + sec;
            System.out.println(res);
            resultPicture.setLength(res);
            resultPicture.setWidth(width);
            resultPicture.setHeight(height);
            resultPicture.setBitRate(bitRate);
            resultPicture.setCodestyle(codeStyle);
            resultPicture.setSampleRate(sampleRate);
            resultPicture.setAudioNum(audioNum);

            String time = "00:00:05";
            String newpath = cutVideoImg(f.getPath(), time);
            resultPicture.setCover(newpath);
        }

//        IContainer container = IContainer.make();
//        int result = container.open(filename, IContainer.Type.READ, null);
//        if (result != null) {
////            throw new RuntimeException("Failed to open media file");
//            int numStreams = container.getNumStreams();
//            long duration = container.getDuration();
//            long secondDuration = duration / 1000000;
//            for (int i = 0; i < numStreams; i++) {
//                IStream stream = container.getStream(i);
//                IStreamCoder coder = stream.getStreamCoder();
//                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//                    resultPicture.setReso(coder.getWidth() + "*" + coder.getHeight());
//                    resultPicture.setHeight(coder.getHeight());
//                    resultPicture.setWidth(coder.getWidth());
//                    if (secondDuration / 60 < 10) {
//                        min = "0" + secondDuration / 60;
//                    } else {
//                        min = String.valueOf(secondDuration / 60);
//                    }
//                    if (secondDuration % 60 < 10) {
//                        sec = "0" + secondDuration % 60;
//                    } else {
//                        sec = String.valueOf(secondDuration % 60);
//                    }
//                    String res = min + ":" + sec;
//                    resultPicture.setLength(res);
//                }
//            }
//            container.close();
//            String time = "00:00:05";
//            String newpath = cutVideoImg(f.getPath(), time);
//            resultPicture.setCover(newpath);
//        }
        getService().insert(resultPicture);
        System.out.println(resultPicture);
        return renderSuccess(resultPicture);
    }

    @ApiOperation(value = "视频截取图片", notes = "视频截取图片")
    @PostMapping("/cutPicture")
    @ResponseBody
    private JsonResult<String> cutPicture(@RequestParam Long id, @RequestParam String time) {
        Picture picture = getService().getById(id);
        Long newid = picture.getNewvideoID();
        String resPath = null;
        if (newid != null) {
            resPath = getService().getById(newid).getPath();
        } else if (newid == null) {
            resPath = picture.getPath();
        }
        String newpath = cutVideoImg(resPath, time);
        Picture p = new Picture();
        p.setId(id);
        p.setCutPicUrl(newpath);
        getService().updateById(p);
        return renderSuccess(newpath);
    }

    public String cutVideoImg(String resPath, String time) {
        String path = configModelService.getUploadPath(ConfigModel.dir_video);
        UUID uuid = UUID.randomUUID();
        String newpath1 = path + "/cutPicture/" + uuid + ".jpg";
        File dir = new File(path + "/cutPicture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String basePath = configModelService.getBaseUploadPath();
        String fileUrl = path.replace(basePath, "");
        fileUrl = fileUrl.replace("\\", "/");
        String newpath = "/files" + fileUrl + "/cutPicture/" + uuid + ".jpg";
        String command = "ffmpeg -i" + " " + resPath + " " + "-ss " + time + " -t 1 -r 1 -q:v 2 -f image2 " + newpath1;
        String[] commend = command.split(" ");
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            java.lang.System.err.println("Create runtime false!");
        }
        try {
            java.lang.System.out.println(commend);
//            pro = runTime.exec(new String[]{"/bin/sh", "-c", command});
            pro = runTime.exec(commend);
//            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
////            output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
//            String line;
//            while ((line = input.readLine()) != null) {
//                java.lang.System.out.println("line: " + line);
//                returnString = returnString + line + "\n";
//            }

            InputStream erro = pro.getErrorStream();
            byte[] a = new byte[1024];
            int j = 0;
            while ((j = erro.read(a)) > -1) {
                returnString += new String(a);
                java.lang.System.out.println(new String(a));
            }
            java.lang.System.out.println("returnString:" + returnString);
            pro.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
        } //finally {
//            pro.destroy();
//        }
        return newpath;
    }

    @ApiOperation(value = "转码管理视频加水印", notes = "转码管理视频加水印")
    @PostMapping("/videoWatermarkThread")
    @ResponseBody
    public JsonResult<Void> videoWatermarkThread(@RequestParam("ids") String ids, @RequestParam Long id) {
        Thread thread = new VideoWatermarkThread(ids, id, watermarkService, systemService, service);
        //thread.start();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(thread, 120, TimeUnit.SECONDS);
        return renderSuccess();
    }


    @ApiOperation(value = "视频立即加水印", notes = "视频立即加水印")
    @PostMapping("/videoWatermark")
    @ResponseBody
    public JsonResult<Void> videoWatermark(@RequestParam("ids") String ids, @RequestParam Long id) {
        String[] s = ids.split(",");
        Watermark watermark = watermarkService.getById(id);
        int location = watermark.getLocation();
        String wmkpath = watermark.getWmkPath();
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            longList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < longList.size(); j++) {
            Picture picture = service.getById(longList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            Picture picture1 = new Picture();
            String path = configModelService.getUploadPath(ConfigModel.dir_video);
            UUID uuid = UUID.randomUUID();
            String newpath1 = path + "/" + uuid + ".mp4";
            String url = "/files/" + systemService.getById(1167404682386190338L).getValue() + "/" + uuid + ".mp4";
            picture1.setPath(newpath1);
            picture1.setSign(1);
            picture1.setColumnId(picture.getColumnId());
            picture1.setName(picture.getName());
            picture1.setForm("mp4");
            picture1.setUrl(url);
            picture1.setDatetime(new Date());
            picture1.setStatus(1);
            picture1.setIsNew(1);
            picture1.setResParentId(picture.getResParentId());
            picture1.setLength(picture.getLength());
            picture1.setWidth(picture.getWidth());
            picture1.setHeight(picture.getHeight());
            picture1.setBitRate(picture.getBitRate());
            picture1.setSampleRate(picture.getSampleRate());
            picture1.setAudioNum(picture.getAudioNum());
            picture1.setCollection(picture.getCollection());
            //service.insert(picture1);
            picture.setNewvideoID(picture1.getId());
            String newpath = "/files" /*+ path.substring(path.lastIndexOf("/"))*/ + "/" + uuid + ".mp4";
            String[] command = null;
            //if (location == 1) {
            //    command = "ffmpeg -i" + " " + picture.getPath() + " " + "-i " + wmkpath + " -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=0:y=0[out]\" -map \"[out]\" -movflags faststart " + newpath1;
            //} else if (location == 3) {
            //    command = "ffmpeg -i" + " " + picture.getPath() + " " + "-i " + wmkpath + " -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=W-w:y=0[out]\" -map \"[out]\" -movflags faststart " + newpath1;
            //} else if (location == 7) {
            //    command = "ffmpeg -i" + " " + picture.getPath() + " " + "-i " + wmkpath + " -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=0:y=H-h[out]\" -map \"[out]\" -movflags faststart " + newpath1;
            //} else if (location == 5) {
            //    command = "ffmpeg -i" + " " + picture.getPath() + " " + "-i " + wmkpath + " -filter_complex \"[1:v]colorkey=white:0.6:1.0[ckout];[0:v][ckout]overlay=x=W-w:y=H-h[out]\" -map \"[out]\" -movflags faststart " + newpath1;
            //}
            switch (location) {
                case 1:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=0:0", newpath1};
                    break;
                case 2:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w)/2:y=0", newpath1};
                    break;
                case 3:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w):y=0", newpath1};
                    break;
                case 4:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=0:y=(main_h-overlay_h)/2", newpath1};
                    break;
                case 5:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2", newpath1};
                    break;
                case 6:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w):y=(main_h-overlay_h)/2", newpath1};
                    break;
                case 7:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=0:y=(main_h-overlay_h)", newpath1};
                    break;
                case 8:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)", newpath1};
                    break;
                case 9:command = new String[]{"ffmpeg", "-i", picture.getPath(), "-i", wmkpath, "-filter_complex", "overlay=x=(main_w-overlay_w):y=(main_h-overlay_h)", newpath1};
                    break;
            }
            //"ffmpeg -i picture.getPath() -i wmkpath -filter_complex \"overlay=10:10\" newpath1";
            //String[] commend = command.split(" ");
            //Runtime runtime = null;
            //String returnString = "";
            //Process pro = null;
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println(String.join(" ", command));
                //pro = runTime.exec(new String[]{"sh", "-c", command});
                Process pro = runTime.exec(command);
                StringBuilder result = new StringBuilder();
                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                //String line;
                //while ((line = input.readLine()) != null) {
                //    java.lang.System.out.println("line: " + line);
                //    returnString = returnString + line + "\n";
                //}
                //java.lang.System.out.println("返回值:" + returnString);
                new Thread(() -> {
                    try {
                        String line1 = null;
                        while ((line1 = input.readLine()) != null) {
                            if (line1 != null){}
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally{
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                new Thread(() -> {
                    try {
                        String line2 = null ;
                        while ((line2 = error.readLine()) !=  null ) {
                            if (line2 != null){result.append(line2).append("\n");}
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally{
                        try {
                            System.out.println("\nresult = " + result + "\n");
                            error.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                pro.waitFor();
                //PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                //String line;
                //while ((line = input.readLine()) != null) {
                //    java.lang.System.out.println("line: " + line);
                //    returnString = returnString + line + "\n";
                //}

                //java.lang.System.out.println("返回值:" + returnString);
                //output.close();
                pro.destroy();
                picture.setVideoUrl(newpath);
                picture.setProcessing(2);
                picture.setIsNew(0);
                service.updateById(picture);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
            String coverpath = cutVideoImg(picture1.getPath(), "00:00:05");
            picture1.setCover(coverpath);
            File pictureFile = new File(picture1.getPath());
            System.out.println("\n"+picture1.getPath());
            BigDecimal bd = new BigDecimal((double) pictureFile.length() / 1024);
            System.out.println("\n"+bd);
            double size = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            picture1.setFsize(size);
            service.insert(picture1);
        }

        return renderSuccess();
    }

    /* @ApiOperation(value = "视频分辨率720p",notes = "视频分辨率720p")
     @PostMapping("/video720p")
     @ResponseBody*/
    public JsonResult<String> video720p(/*@RequestParam*/ Long id) {

        String path = systemService.getById(1167404682386190338L).getValue();
        Picture picture = service.getById(id);
        String newpath = path + "/" + picture.getName() + "720" + "." + picture.getForm();
        Picture picture720 = new Picture();
        picture720.setPath(newpath);
        picture720.setResParentId(id);
        picture720.setResoType(720);
        String url = "/files" + path.substring(path.lastIndexOf("/")) + "/" + picture.getName() + "720" + "." + picture.getForm();
        picture720.setUrl(url);
        picture720.setName(picture.getName());
        service.insert(picture720);
        String[] command = {"ffmpeg", "-i", picture.getPath(), "-vf", "scale=1280:720", newpath, "-hide_banner"};

        Runtime runtime = null;
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            java.lang.System.err.println("Create runtime false!");
        }
        try {
            java.lang.System.out.println("start trans 720");
            java.lang.System.out.println(command);
            pro = runTime.exec(command);

            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                java.lang.System.out.println("line: " + line);
                returnString = returnString + line + "\n";
            }

            java.lang.System.out.println("返回值:" + returnString);
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return renderSuccess(returnString);
    }

    /*@ApiOperation(value = "视频分辨率480p",notes = "视频分辨率480p")
    @PostMapping("/video480p")
    @ResponseBody*/
    public JsonResult<String> video480p(/*@RequestParam*/ Long id) {
        String path = systemService.getById(1167404682386190338L).getValue();
        Picture picture = service.getById(id);
        String newpath = path + "/" + picture.getName() + "480" + "." + picture.getForm();
        Picture picture480 = new Picture();
        picture480.setResParentId(id);
        picture480.setPath(newpath);
        picture480.setResoType(480);
        picture480.setName(picture.getName());
        String url = "/files" + path/*.substring(path.lastIndexOf("/"))*/ + "/" + picture.getName() + "480" + "." + picture.getForm();
        picture480.setUrl(url);
        service.insert(picture480);
        String[] command = {"ffmpeg", "-i", picture.getPath(), "-vf", "scale=640:480,setdar=4:3", newpath, " -hide_banner"};
        Runtime runtime = null;
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            java.lang.System.err.println("Create runtime false!");
        }
        try {
            java.lang.System.out.println("start trans480");
            java.lang.System.out.println(command);
//            pro = runTime.exec(new String[]{"sh", "-c", command});
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                java.lang.System.out.println("line: " + line);
                returnString = returnString + line + "\n";
            }
            java.lang.System.out.println("返回值:" + returnString);
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return renderSuccess(returnString);
    }

    /*    @ApiOperation(value = "视频分辨率360p",notes = "视频分辨率360p")
        @PostMapping("/video360p")
        @ResponseBody*/
    public JsonResult<String> video360p(/*@RequestParam*/ Long id) {
        String path = systemService.getById(1167404682386190338L).getValue();
        Picture picture = service.getById(id);
        String newpath = path + "/" + picture.getName() + "360" + "." + picture.getForm();
        Picture picture360 = new Picture();
        picture360.setPath(newpath);
        picture360.setResParentId(id);
        picture360.setResoType(360);
        picture360.setName(picture.getName());
        String url = "/files" + path/*.substring(path.lastIndexOf("/"))*/ + "/" + picture.getName() + "360" + "." + picture.getForm();
        picture360.setUrl(url);
        service.insert(picture360);
        String[] command = {"ffmpeg", "-i", picture.getPath(), "-vf", "scale=480:360,setdar=4:3", newpath, "-hide_banner"};
        Runtime runtime = null;
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            java.lang.System.err.println("Create runtime false!");
        }
        try {
            java.lang.System.out.println("start trans360");
            java.lang.System.out.println(command);
//            pro = runTime.exec(new String[]{"sh", "-c", command});
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                java.lang.System.out.println("line: " + line);
                returnString = returnString + line + "\n";
            }
            java.lang.System.out.println("返回值:" + returnString);
            /*关闭流、释放资源*/
            input.close();
            /*关闭流、释放资源*/
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return renderSuccess(returnString);
    }

    @ApiOperation(value = "视频处理压缩", notes = "视频处理压缩")
    @PostMapping("/viewCompress")
    @ResponseBody
    private JsonResult<String> viewCompress(@RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            String filename = picture.getPath();
            Integer width = picture.getWidth();
            if (width == 1920) {
                video720p(picture.getId());
                video480p(picture.getId());
                video360p(picture.getId());
            } else if (width == 1280) {
                video480p(picture.getId());
                video360p(picture.getId());
            } else if (width == 720) {
                video360p(picture.getId());
            }
            picture.setProcessing(2);
            service.updateById(picture);
//            IContainer container = IContainer.make();
//            int result = container.open(filename, IContainer.Type.READ, null);
//            if (result < 0) {
//                throw new RuntimeException("Failed to open media file");
//            }
//            int numStreams = container.getNumStreams();
//            long duration = container.getDuration();
//            long secondDuration = duration / 1000000;
//            for (int i = 0; i < numStreams; i++) {
//                IStream stream = container.getStream(i);
//                IStreamCoder coder = stream.getStreamCoder();
//                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//                    if (coder.getWidth() == 1920) {
//                        video720p(picture.getId());
//                        video480p(picture.getId());
//                        video360p(picture.getId());
//                    } else if (coder.getWidth() == 1280) {
//                        video480p(picture.getId());
//                        video360p(picture.getId());
//                    } else if (coder.getWidth() == 720) {
//                        video360p(picture.getId());
//                    }
//                    picture.setProcessing(2);
//                    service.updateById(picture);
//                }
//            }
        }
        return renderSuccess();
    }


    @ApiOperation(value = "视频信息", notes = "视频信息")
    @PostMapping("/getVideoinfo")
    @ResponseBody
    public JsonResult<List<List>> getVideoinfo(@RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        List infolist = new ArrayList();
        List reslist = new ArrayList();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            infolist.add(picture.getBitRate());
            infolist.add(picture.getSampleRate());
            infolist.add(picture.getAudioNum());
            infolist.add(picture.getCodestyle());
            // picturepath = C:\\upload\\webvideo\\d379f24a-de39-480e-874c-1a8005b0e9b9.mp4

//            IContainer container = IContainer.make();

            // we attempt to open up the container
//            int result = container.open(picture.getPath(), IContainer.Type.READ, null);
//            ArrayList<String> commend = new ArrayList<>();
//            commend.add("ffmpeg");
//            commend.add("-i");
//            commend.add(picture.getPath());
//            commend.add("-hide_banner");
//            StringBuilder result = new StringBuilder();
//            try { // 获得视频信息
//                ProcessBuilder processBuilder = new ProcessBuilder();
//                processBuilder.command(commend);
//                processBuilder.redirectErrorStream(true);
//                Process process = processBuilder.start();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line = null;
//                process.waitFor();
//                while ((line = bufferedReader.readLine()) != null) {
//                    result.append(line).append('\n');
//                }
//                process.destroy();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("result = "+result);
//            // check if the operation was successful
//            if (result.length() == 0) {
//                throw new RuntimeException("Failed to open media file");
//            }
//            Pattern pattern = Pattern.compile("bitrate: (\\d*) kb/s.*Video: (.*?) .*?, (\\d*) Hz, ([a-z]*),");
//            Matcher matcher = pattern.matcher(result);
//            if (matcher.find()) {
//                Long bitRate = Long.parseLong(matcher.group(1));
//                String code = matcher.group(2);
//                String sampleRate = matcher.group(3);
//                String audioNum = matcher.group(4);
//                infolist.add(bitRate);
//                infolist.add(sampleRate);
//                infolist.add(audioNum);
//                infolist.add(code);
//            }
            // query how many streams the call to open found
//            int numStreams = container.getNumStreams();

            // query for the total duration
//            long duration = container.getDuration();

            // query for the file size
//            long fileSize = container.getFileSize();

            // query for the bit rate
//            long bitRate = container.getBitRate();
//            for (int i = 0; i < numStreams; i++) {
//                infolist.add(bitRate);
                // find the stream object
//                IStream stream = container.getStream(i);

                // get the pre-configured decoder that can decode this stream;
//                IStreamCoder coder = stream.getStreamCoder();


//                infolist.add(coder.getSampleRate());
//                infolist.add(coder.getChannels());
//                infolist.add(coder.getCodecID());

//            }
        }
        reslist.add(infolist);
        return renderSuccess(reslist);
    }

    @ApiOperation(value = "视频立即转码h264", notes = "视频立即转码h264")
    @PostMapping("/transH264")
    @ResponseBody
    public JsonResult<Void> transH264(@RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            idList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < idList.size(); j++) {
            Picture picture = service.getById(idList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            String path = systemService.getById(1167404682386190338L).getValue();
            UUID uuid = UUID.randomUUID();
            String newpath = path + "/" + uuid + picture.getName() + "H264" + "." + picture.getForm();
            Picture picture1 = new Picture();
            picture1.setPath(newpath);
            picture1.setResParentId(idList.get(j));
            picture1.setResParentId(idList.get(j));

            String url = "/files" + path.substring(path.lastIndexOf("/")) + "/" + uuid + picture.getName() + "H264" + "." + picture.getForm();
            picture1.setUrl(url);
            picture.setH264Url(url);
            String[] command = {"ffmpeg", "-i", picture.getPath(), "-vcodec", "h264", newpath, "-hide_banner"};
            Runtime runtime = null;
            String returnString = "";
            Process pro = null;
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println("start transH264------process");
                java.lang.System.out.println(String.join(" ", command));
                pro = runTime.exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    java.lang.System.out.println("line: " + line);
                    returnString = returnString + line + "\n";
                }
                java.lang.System.out.println("返回值:" + returnString);
                /*关闭流*/
                input.close();
                /*关闭流*/
                output.close();
                pro.destroy();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            picture.setProcessing(2);
            service.updateById(picture);
            service.insert(picture1);
        }
        return renderSuccess("成功");
    }

    @ApiOperation(value = "转码管理视频转264", notes = "转码管理视频转H264")
    @PostMapping("/transH264Thread")
    @ResponseBody
    public JsonResult<Void> transH264Thread(@RequestParam("ids") String ids) {
        Thread thread = new VideoH264Thread(ids, configModelService.getUploadPath(ConfigModel.dir_video), configModelService.getBaseUploadPath(), service);
//        thread.start();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(thread, 0, TimeUnit.SECONDS);
        return renderSuccess();
    }

    //音频上传
    @ApiOperation(value = "音频上传")
    @PostMapping("/radioupload")
    @ResponseBody
    public Picture radioupload(@RequestPart MultipartFile file, HttpServletRequest request, @RequestParam("id") Long id) throws IOException {
        // 1. 保存到服务器 得到path

        String path = configModelService.getUploadPath(ConfigModel.dir_audio);

        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
//        val user = SessionUser.getCurrentUser();
//        String username = user.getName();
//        String fileName =  UUID.randomUUID()+file.getOriginalFilename();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(path, fileName);
        //file.transferTo(f);
        FileUtils.copyInputStreamToFile(file.getInputStream(),f);
        //2 .保存一条数据到file表
        Picture resultPicture = new Picture();
        resultPicture.setPath(f.getPath());
        //getService().insert(p);

        //JsonResult<Picture> result= doGetById(getService(), p.getId());

        File pictureFile = new File(resultPicture.getPath());
//        Encoder encoder = new Encoder();
        /*MultimediaInfo m = encoder.getInfo(pictureFile);*/
        /* long ls = m.getDuration()/1000;*/
        String min = null;
        String sec = null;
        /*if(ls/60<10){
             min = "0"+ls/60;
        }else{
            min = String.valueOf(ls/60);
        }
        if(ls%60<10){
             sec = "0"+ls%60;
        }else{
            sec = String.valueOf(ls%60);
        }*/
     /*   String res = min+":"+sec;
        resultPicture.setLength(res);*/
        //System.out.println("此视频时长为:"+ls/60000+"分"+(ls/1000-ls/60000*60)+"秒！");

        int dot = pictureFile.getName().lastIndexOf('.');
        if ((dot > -1) && (dot < (pictureFile.getName().length() - 1))) {
            String pictureForm = pictureFile.getName().substring(dot + 1);
            resultPicture.setForm(pictureForm);
            if (!pictureForm.equals("mp3") && !pictureForm.equals("wav") && !pictureForm.equals("wma") && !pictureForm.equals("asf")) {
                resultPicture.setOtherForm("其他");
            }
//            String url="/files"+path.substring(path.lastIndexOf("/"))+"/"+fileName;

            String basedir = configModelService.getBaseUploadPath();
            String fileUrl = path.replace(basedir, "");
            if (File.separator.equals("\\")) {
                fileUrl = fileUrl.replace("\\", "/");
            }
            String url = "/files" + fileUrl + "/" + fileName;

            resultPicture.setUrl(url);
            resultPicture.setFsize((double) pictureFile.length() / 1024);
            resultPicture.setSign(3);
//            resultPicture.setCrUser(username);
            //resultPicture.setReso(reso);
            Programa programa = programaService.getById(id);
            if (programa.getParentId() != 0) {
                resultPicture.setParentId(programa.getParentId());
            }
            if (programa.getSiteId() != null) {
                resultPicture.setSiteId(programa.getSiteId());
                Site site = siteService.getById(programa.getSiteId());
                resultPicture.setCompanyId(site.getCompanyId());
            }
            Date date = new Date();
            resultPicture.setDatetime(date);
            resultPicture.setColumnId(id);
            resultPicture.setCopyright("默认版权");
            resultPicture.setResc("默认来源");
            resultPicture.setOtherCp("其他");
           /* if(!copyRight.equals("原创")&&!copyRight.equals("经授权")){
                resultPicture.setOtherCp("其他");
            }*/
            resultPicture.setCreateTime(new Date());
            resultPicture.setCollection(0);
            resultPicture.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            resultPicture.setStatus(0);
        }
        getService().insert(resultPicture);
        return resultPicture;
    }

    @ApiOperation(value = "音频立即转码MP3", notes = "音频立即转码MP3")
    @PostMapping("/transMp3")
    @ResponseBody
    public JsonResult<String> transMp3(@RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            longList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < longList.size(); j++) {
            Picture picture = service.getById(longList.get(j));
            picture.setProcessing(1);
            service.updateById(picture);
            String path = systemService.getById(1167404882748092418L).getValue();
            UUID uuid = UUID.randomUUID();
            String newpath1 = path + "/" + uuid + ".mp3";
            String newpath = "/files" + path.substring(path.lastIndexOf("/")) + "/" + uuid + ".mp3";

            String command = null;
            command = "ffmpeg -i" + " " + picture.getPath() + " " + "-f mp3 -acodec libmp3lame -y " + newpath1;
            Runtime runtime = null;
            String returnString = "";
            Process pro = null;
            Runtime runTime = Runtime.getRuntime();
            if (runTime == null) {
                java.lang.System.err.println("Create runtime false!");
            }
            try {
                java.lang.System.out.println(command);
                pro = runTime.exec(new String[]{"sh", "-c", command});

                BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    java.lang.System.out.println("line: " + line);
                    returnString = returnString + line + "\n";
                }

                java.lang.System.out.println("返回值:" + returnString);
                input.close();
                output.close();
                pro.destroy();
                picture.setProcessing(2);
                picture.setMp3url(newpath);
                service.updateById(picture);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        return renderSuccess();
    }

    @ApiOperation(value = "转码管理转MP3", notes = "转码管理转MP3")
    @PostMapping("/transMp3Thread")
    @ResponseBody
    public JsonResult<Void> transMp3Thread(@RequestParam("ids") String ids) {

        Thread thread = new MP3Thread(ids, configModelService.getUploadPath(ConfigModel.dir_audio), configModelService.getBaseUploadPath(), service);
        /*thread.start();*/

      /*  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 4, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());*/
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(thread, 0, TimeUnit.SECONDS);

        return renderSuccess();
    }

    @ApiOperation(value = "获取音频信息", notes = "获取音频信息")
    @PostMapping("/getMp3Info")
    @ResponseBody
    public JsonResult<List<List>> getMp3Info(@RequestParam("ids") String ids) {
        String[] s = ids.split(",");
        List<Long> longList = new ArrayList<>();
        List infolist = new ArrayList();
        List reslist = new ArrayList();
        MP3File mp3File = null;
        for (int i = 0; i < s.length; i++) {
            longList.add(Long.parseLong(s[i]));
        }
        for (int j = 0; j < longList.size(); j++) {
            Picture picture = service.getById(longList.get(j));
            String path = picture.getPath();
            try {
                mp3File = new MP3File(path);
                MP3AudioHeader header = mp3File.getMP3AudioHeader();
                /*AbstractID3v2Tag id3v2tag=  mp3File.getID3v2Tag();
                String songName=new String(id3v2tag.frameMap.get("TIT2").toString().getBytes("ISO-8859-1"),"GB2312");
                String singer=new String(id3v2tag.frameMap.get("TPE1").toString().getBytes("ISO-8859-1"),"GB2312");
                String author=new String(id3v2tag.frameMap.get("TALB").toString().getBytes("ISO-8859-1"),"GB2312");*/
                infolist.add(header.getBitRate());//比特率
                infolist.add(header.getTrackLength());//音轨长度
                infolist.add(header.getChannels());//声道
                infolist.add(header.getSampleRate());//采样率
            } catch (Exception e) {
                e.printStackTrace();
            }
            reslist.add(infolist);

        }
        return renderSuccess(reslist);
    }

    public static final String APP_ID = "";
    public static final String API_KEY = "";
    public static final String SECRET_KEY = "";

    @ApiOperation(value = "mp3转words")
    @PostMapping("/mp3towords")
    @ResponseBody
    public JsonResult<Void> test(/*@RequestParam String path,@RequestParam  String path2*/) throws Exception {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        String path = "";
        String path2 = "D:\\test2.pcm";
        convertMP32Pcm(path, path2);
        JSONObject asrRes = client.asr(path2, "pcm", 16000, null);
        return JsonResult.renderSuccess(asrRes);
    }

    public boolean convertMP32Pcm(String mp3filepath, String pcmfilepath) {
        try {
            //获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            //将音频转化为  pcm的格式保存下来
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;
        AudioFormat targetFormat = null;
        try {
            AudioInputStream in = null;
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3);
            AudioFormat baseFormat = in.getFormat();
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }
}
