package com.jnetdata.msp.docs.document.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.docs.document.exception.InvalidateDocumentException;
import com.jnetdata.msp.docs.document.model.Document;
import com.jnetdata.msp.docs.document.model.DocumentUploadProperties;
import com.jnetdata.msp.docs.document.service.DocumentService;
import com.jnetdata.msp.docs.document.vo.PicUploadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("/docs/document")
@Api(value = "DocumentController", description = "文档")
@Slf4j
@EnableConfigurationProperties(DocumentUploadProperties.class)
public class DocumentController extends BaseController<Long, Document> {

    @Autowired
    private DocumentUploadProperties documentUploadProperties;

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "文件下载",notes = "根据id下载文件")
    @GetMapping("/downloadFile")
    @ResponseBody
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,@RequestParam Long id){
        Document document = getService().getById(id);
        if(ObjectUtils.isEmpty(document)){
            throw new InvalidateDocumentException("id错误");
        }
        String[] strs = document.getName().split("\\.");
        String targetFileName = document.getId()+"."+strs[strs.length-1];
        doDownload(document.getUploadPathName(),targetFileName,request,response);
    }

    /**
     *  文件下载
     * @param downloadFilePath 下载文件路径
     * @param targetFilename 下载后文件名
     * @param request 请求
     * @param response 响应
     */
    @SneakyThrows
    public void doDownload(String downloadFilePath, String targetFilename, HttpServletRequest request, HttpServletResponse response) {

        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" +  targetFilename );
        //设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        //文件输入流
        FileInputStream in = new FileInputStream(new File(downloadFilePath));
        //获取响应输出流
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        FileCopyUtils.copy(in,out);
    }

    @ApiOperation(value = "图片显示",notes = "使用方法：src=/docs/document/image/id=1130357999342919682")
    @GetMapping("/image")
    public void downloaPicture(HttpServletResponse response, @RequestParam Long id) {
        Document document = getService().getById(id);
        if (Objects.isNull(document) || !Document.CONTENTTYPE_PIC.equals(document.getContentType())) {
            throw new InvalidateDocumentException("图片地址错误");
        }
        //写给浏览器
        response.setContentType("image/*");
        //浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        try {
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(document.getUploadPathName());
            FileCopyUtils.copy(in, out);
        }catch(FileNotFoundException e1) {
            throw new InvalidateDocumentException("文件["+document.getUploadPathName()+"]不存在");
        }catch (IOException e2) {
            throw new InvalidateDocumentException("打开响应输出流异常");
        }
    }

    @ApiOperation("根据id获取文件名称")
    @GetMapping("/getFileNameById")
    @ResponseBody
    public JsonResult getFileNameById(@RequestParam Long id){
        if(ObjectUtils.isEmpty(id)){
            throw new InvalidateDocumentException("id错误");
        }
        Document document = getService().getById(id);
        if(ObjectUtils.isEmpty(document)){
            throw new InvalidateDocumentException("id错误");
        }
        return JsonResult.renderSuccess((Object) document.getName());
    }

    @ApiOperation("根据ids获取文件属性,包括id,文件名称,文件描述,文件上传时间")
    @GetMapping("/getFileAttributes")
    @ResponseBody
    public JsonResult getFileAttributes(@ApiParam(value = "以逗号分隔的id")@RequestParam(value = "ids") String ids){
        return JsonResult.renderSuccess(getService().getFileAttributes(ids));
    }

    @ApiOperation("头像文档上传")
    @PostMapping("/iconPicUpload")
    @ResponseBody
    public JsonResult<Document> iconPicUpload(@Validated @RequestBody PicUploadVo vo) {
        FileUploadHelper helper = DocumentUploadHelper.fromPicUpload(vo);
        FileUploadHelper[] helpers = new FileUploadHelper[]{helper};
        List<Document> docs = doDocumentUpload(helpers,
                Document.CONTENTTYPE_PIC, vo.getName(), "", "icon");
        return JsonResult.renderSuccess(docs.get(0));
    }


    @ApiOperation("头像上传")
    @PostMapping("/iconUpload")
    @ResponseBody
    public JsonResult<Document> iconUpload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("name") String name,
                                           @RequestParam("description") String description) {
        MultipartFile[] files = new MultipartFile[]{file};
        List<Document> list = doFileUpload(files, "pic", name, description, "icon");
        return JsonResult.renderSuccess(list.get(0));
    }

    @ApiOperation("文档上传")
    @PostMapping("/docUpload")
    @ResponseBody
    public JsonResult<List<Document>> docUpload(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("contentType") String contentType,
                                                @RequestParam("name") String name,
                                                @RequestParam("description") String description,
                                                @RequestParam("path") String path) {

        return JsonResult.renderSuccess(doFileUpload(files, contentType, name, description, path));
    }

    @SneakyThrows
    private List<Document> doFileUpload(MultipartFile[] files,
                                        String contentType, String name, String description, String path){

        FileUploadHelper[] helpers = DocumentUploadHelper.fromMultipartFile(files);
        return doDocumentUpload(helpers, contentType, name, description, path);
    }

    @SneakyThrows
    private List<Document> doDocumentUpload(FileUploadHelper[] files,
                                            String contentType, String name, String description, String path){

        String relativePath = contentType+"/"+path;
        File dir = new File(getUploadPath(relativePath));
        if(!dir.exists()){
            dir.mkdirs();
        }

        List<Document> docs = new ArrayList<>();
        for(int i=0; i<files.length; i++) {
            FileUploadHelper file = files[i];
            if (file.isEmpty()) {
                continue;
            }

            //文件将保存的路径(当前规则 时间戳加文件名)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = (dir + "/" + fileName).replaceAll("\\\\", "/");

            // 转存文件
            file.saveTo(filePath);
            log.debug("上传成功，文件大小：" + file.getSize());

            Document doc = new Document();
            doc.setName(name+"_"+(i+1));
            doc.setContentType(contentType);
            doc.setDescription(description);
            doc.setUrl(getMappingUrl(relativePath)+"/"+fileName);
            doc.setUploadPathName(filePath);
            docs.add(doc);
        }

        documentService.insertBatch(docs);
        return docs;
    }

    private String getUploadPath(String relativePath) {
        return documentUploadProperties.getUploadRoot()+relativePath;
    }
    private String getMappingUrl(String relativePath) {
        return documentUploadProperties.getRootMappingUrl() + relativePath;
    }


    private DocumentService getService() {
        return documentService;
    }



}
