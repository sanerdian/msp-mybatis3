package com.jnetdata.msp.resources.file.Controller;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.FastDFSClient;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.resources.file.service.FileService;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.util.Base64ImgUtil;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.TextWatermark;
import com.spire.doc.documents.WatermarkLayout;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.web.JsonResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/resources/files")
@ApiModel(value = "文件配置(FileController)", description = "文件配置")
public class FileController extends BaseController<Long, Picture> {
    @Autowired
    private FileService service;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    private FastDFSClient fdfsClient;

    @Autowired
    private ProgramaService programaService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private ConfigModelService configModelService;

    @Autowired
    private ContentLogService contentLogService;

    //文件上传
    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    @ResponseBody
    public Picture fileupload(@RequestPart MultipartFile file, HttpServletRequest request, @RequestParam("id") Long id)throws IOException {
        String path = configModelService.getUploadPath(ConfigModel.dir_file);

        String fileName =  UUID.randomUUID()+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
//        val user = SessionUser.getCurrentUser();
//        String username = user.getName();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(path,fileName);

        System.out.println("地址为："+path+fileName);
       // file.transferTo(f);

        //上传
        FileUtils.copyInputStreamToFile(file.getInputStream(),f);
        //2 .保存一条数据到file表
        Picture resultPicture = new Picture();
        resultPicture.setPath(f.getPath());
        File pictureFile = new File(resultPicture.getPath());

        int dot = pictureFile.getName().lastIndexOf('.');
        if ((dot > -1) && (dot < (pictureFile.getName().length() - 1))) {
            String pictureForm = pictureFile.getName().substring(dot + 1);
            resultPicture.setForm(pictureForm);
            if(!pictureForm.equals("doc")&&!pictureForm.equals("xlsx")&&!pictureForm.equals("txt")&&!pictureForm.equals("ppt")&&!pictureForm.equals("docx")){
                resultPicture.setOtherForm("其他");
            }
        }

        BigDecimal bd = new BigDecimal((double)pictureFile.length()/1024);
        double size = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        resultPicture.setFsize(size);
        resultPicture.setSign(2);
        Programa programa = programaService.getById(id);
        if(programa.getParentId()!=0){
            resultPicture.setParentId(programa.getParentId());
        }
        if(programa.getSiteId()!=null){
            resultPicture.setSiteId(programa.getSiteId());
            Site site = siteService.getById(programa.getSiteId());
            resultPicture.setCompanyId(site.getCompanyId());
        }

        Date date =new Date();
        String basedir = configModelService.getBaseUploadPath();
        String fileUrl = path.replace(basedir,"");
        if(File.separator.equals("\\")){
            fileUrl = fileUrl.replace("\\","/");
        }
        String url="/files"+fileUrl+"/"+fileName;

        resultPicture.setUrl(url);
        resultPicture.setDatetime(date);
        resultPicture.setColumnId(id);
        resultPicture.setOtherCp("其他");
        resultPicture.setResc("默认来源");
        resultPicture.setCopyright("默认版权");
//        resultPicture.setCrUser(username);
        resultPicture.setCreateTime(new Date());
        resultPicture.setCollection(0);
        resultPicture.setResParentId((long)0);
        resultPicture.setName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        resultPicture.setStatus(0);
        resultPicture.setPdfSign(0);

        getService().insert(resultPicture);
        return resultPicture;
    }

    @PostMapping("/downWord")
    @ResponseBody
    public void downWord(HttpServletRequest request, HttpServletResponse response, String content,String fileName) {
        try {
            //新建Document对象
            Document document = new Document();
            //添加section
            Section sec = document.addSection();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //添加段落并写入HTML文本
            sec.addParagraph().appendHTML(content);

            TextWatermark txtWatermark = new TextWatermark();
            //设置文本水印格式
            txtWatermark.setText("涉密资料  严控范围");
            txtWatermark.setFontSize(40);
            txtWatermark.setColor(Color.red);
            txtWatermark.setLayout(WatermarkLayout.Diagonal);
            //将文本水印添加到示例文档
            sec.getDocument().setWatermark(txtWatermark);

            document.saveToStream(os, FileFormat.Docx);

            InputStream input = new ByteArrayInputStream(os.toByteArray());

            contentLogService.addLog("文档下载","文档下载","下载文档:"+fileName,true);

            //输出文件
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");//导出word格式
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, "UTF-8") + ".docx");

            /*获取响应输出流对象*/
            ServletOutputStream ostream = response.getOutputStream();
            int len =-1;
            byte []by = new byte[1024];
            while((len = input.read(by))!=-1) {
                ostream.write(by,0,len);
            }
            ostream.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("/ue")
    @ResponseBody
    public Object importHead(@RequestPart(value = "upfile",required = true)MultipartFile file,HttpServletRequest request) throws IOException {
        Map<String,Object> result = new HashMap<>();
        String url;
        result = getService().upload(file);
        if(result.get("url") != null){
//            original: "1 (1).jpg"
//            size: "121978"
//            state: "SUCCESS"
//            title: "1602730386588090205.jpg"
//            type: ".jpg"
//            url: "/ueditor/jsp/upload/image/20201015/1602730386588090205.jpg"
            result.put("url",getUrlPerfix(request) + result.get("url"));
            result.put("state","SUCCESS");
            return result;
        }else{
            result.put("state","");
            return result;
        }
    }


    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/word")
    @ResponseBody
    public String ueWord(@RequestPart MultipartFile file, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        result = getService().upload(file);
        if(result.get("url") != null){
            return getUrlPerfix(request) + result.get("url");
        }else{
            return "";
        }
    }
    @ApiOperation(value = "解析pdf（pdf文件不会保存到服务器上上）")
    @PostMapping("/ue/pdf")
    @ResponseBody
    public JsonResult uePdf(@RequestPart MultipartFile file, HttpServletRequest request,HttpServletResponse response) throws IOException {
        String html=getService().pdf2html(file.getInputStream());
        org.jsoup.nodes.Document root = Jsoup.parse(html);
        Elements pages = root.getElementsByClass("page");
        Elements elemets=new Elements();
        for(Element page:pages){
            for(Element element:page.children()){

                element.attr("style","");
                if(element.tagName().equalsIgnoreCase("img")){
                    String src=element.attr("src");
                    if(src.indexOf("base64,")>-1){
                        Map result=getService().saveBase64Image(src);
                        element.attr("src",getUrlPerfix(request)+result.get("url"));
                        element.attr("width","80%");
                    }
                    Element p=new Element("p");
                    p.attr("style","text-align:center;");
                    p.appendChild(element);
                    elemets.add(p);
                }else {
                    elemets.add(element);
                }
            }
        }
        return JsonResult.success(elemets.toString());
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/catch")
    @ResponseBody
    public Object importHeadCatch(@RequestParam("source[]") String[] source, HttpServletRequest request){
        List<Map<String,Object>> list = new ArrayList<>();
        String success = "FIELD";
        for (String s : source) {
            Map<String,Object> url = downFile(request, s);
            url.put("state",url.size() == 0?"FIELD":"SUCCESS");
            list.add(url);
            if(url.size() > 0) success = "SUCCESS";
        }
        Map<String,Object> result = new HashMap<>();
        result.put("state",success);
        result.put("list",list);
        return result;
    }


    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/catchByBase64")
    @ResponseBody
    public Object catchByBase64(@RequestBody String[] source, HttpServletRequest request){
        List<String> list = new ArrayList<>();
        for (String s : source) {
            String path = configModelService.getUploadPath(ConfigModel.dir_thumbnail);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath =  path+File.separator + UUID.randomUUID()+".png";
            Base64ImgUtil.Base64ToImage(s.replace("data:image/jpeg;base64,",""),filePath);
            String basedir = configModelService.getBaseUploadPath();
            String fileUrl = filePath.replace(basedir,"");
            if(File.separator.equals("\\")){
                fileUrl = fileUrl.replace("\\","/");
            }
            String url="/files"+fileUrl;
            list.add(url);
        }
        return renderSuccess(list);
    }
    @ApiOperation(value = "上传文件",notes = "用url下载其对应的远程服务器上的文件，下载后转换成本服务器上的url地址反馈")
    @GetMapping("/ue/catchByUrl")
    @ResponseBody
    public Map catchByUrl(HttpServletRequest request,String url){
        if(url.startsWith(getUrlPerfix(request))){
            Map map=new HashMap();
            map.put("url",url);
            map.put("msg","本地文件，无需上传");
            return map;
        }
        return service.upload(url,false);
    }
    public Map<String,Object> downFile(HttpServletRequest request, String res){
        String path = configModelService.getUploadPath(ConfigModel.dir_headimg);
        String basePath = configModelService.getBaseUploadPath();
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(res)) return map;
        if(res.startsWith(getUrlPerfix(request))) return map;
        InputStream inputStream = HttpUtil.createGet(res).execute().bodyStream();
        String s = savePic(request, inputStream,path,basePath);
        map.put("url",s);
        map.put("title",s.substring(s.indexOf(".")));
        map.put("size",1);
        map.put("source",res);
        return map;
    }

    public List<String> getImgSrc(String htmlStr) {

        if (htmlStr == null) {
            return null;
        }

        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return new ArrayList<>(new HashSet<>(pics));
    }

    private String getUrlPerfix(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }


    public String savePic(HttpServletRequest request, InputStream inStream, String path, String basePath) {
        String url = "";
        String title = "";
        Map<String, Object> map = new HashMap<>();

        title = UUID.randomUUID() + ".jpg";

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File imageFile = new File(path, title);

        //图片出入文件夹
        try {
            byte[] data = readInputStream(inStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();


            String fileUrl = path.replace(basePath, "");
            fileUrl = fileUrl.replace("\\", "/");
            url = getUrlPerfix(request) + "/files" + fileUrl + "/" + title;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();

    }

    private FileService getService() {
        return service;
    }


    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/{cdntype}")
    @ResponseBody
    public Object importHead(@PathVariable("cdntype") String cdntype,@RequestPart(value = "upfile",required = true)MultipartFile file,HttpServletRequest request) throws IOException {
        Map<String,Object> result = new HashMap<>();
        String url;
        result = getService().upload(file,"1".equals(cdntype)?true:false);
        if(result.get("url") != null){
//            original: "1 (1).jpg"
//            size: "121978"
//            state: "SUCCESS"
//            title: "1602730386588090205.jpg"
//            type: ".jpg"
//            url: "/ueditor/jsp/upload/image/20201015/1602730386588090205.jpg"
            result.put("url",getUrlPerfix(request) + result.get("url"));
            result.put("state","SUCCESS");
        }else{
            result.put("state","");
        }
        if(result.get("cutPicUrl") != null){
            result.put("cutPicUrl",getUrlPerfix(request) + result.get("cutPicUrl"));
        }
        return result;
    }


    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/word/{cdntype}")
    @ResponseBody
    public String ueWord(@PathVariable("cdntype") String cdntype,@RequestPart MultipartFile file, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        result = getService().upload(file,"1".equals(cdntype)?true:false);
        if(result.get("url") != null){
            return getUrlPerfix(request) + result.get("url");
        }else{
            return "";
        }
    }


    @ApiOperation(value = "上传文件")
    @PostMapping("/ue/catch/{cdntype}")
    @ResponseBody
    public Object importHeadCatch(@PathVariable("cdntype") String cdntype,@RequestParam("source[]") String[] source, HttpServletRequest request){

        boolean bool = "1".equals(cdntype)?true:false;

        List<Map<String,Object>> list = new ArrayList<>();
        String success = "FIELD";
        for (String s : source) {
            Map<String,Object> url = downFile(request, s,bool);
            url.put("state",url.size() == 0?"FIELD":"SUCCESS");
            list.add(url);
            if(url.size() > 0) {
                success = "SUCCESS";
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("state",success);
        result.put("list",list);
        return result;
    }
    @ApiOperation(value = "视频按播放时间抽取截图",notes = "根据可选参数确定截图策略。counts为截图张数，second不指定时为随机时刻截图，second指定而inteval未指定时为在播放时刻为该秒内的截图，second与inteval都指定时为按指定的播放时刻的等差序列截图")
    @GetMapping("/ue/grabberImage")
    @ResponseBody
    public JsonResult<List<String>> grabberImage(
            @RequestParam @ApiParam(value = "视频地址，必须是通过本系统上传的") String url,
            @RequestParam(required = false) @ApiParam(value = "截取图片张数，默认10，最大值100") Integer counts,
            @RequestParam(required = false) @ApiParam(value = "第一张截图对应播放时刻，单位秒。当该值不设置时为随机播放时刻取截图") Integer second,
            @RequestParam(required = false) @ApiParam(value = "需设置second。间隔多少播放时长截取一张图片，单位秒。" +
                    "\n例1：second=0,inteval is null,counts=5 为取播放时间为[0,1)之间内的5张随机图片；" +
                    "\n例2：second=10,inteval=5,counts=6 在播放时刻为10、15、20、25、30、35时各截取一张图片") Integer inteval,
            HttpServletRequest request){
        if(counts==null||counts<=0||counts>100){
            counts=10;
        }
        List<String> result=new ArrayList<>();
        List<String> imageUrls = getService().grabberImage(url,counts, second, inteval);
        if(imageUrls!=null){
            String prefix=getUrlPerfix(request);
            for(int index=0;index<imageUrls.size();index++){
                result.add(prefix+imageUrls.get(index));
            }
        }
        return JsonResult.success(result);
    }
    public Map<String,Object> downFile(HttpServletRequest request, String res,boolean bool){
        String path = configModelService.getUploadPath(ConfigModel.dir_headimg);
        String basePath = configModelService.getBaseUploadPath(bool?"dir_upload_base_cdn":"dir_upload_base");
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(res)) {
            return map;
        }
        if(res.startsWith(getUrlPerfix(request))) {
            return map;
        }
        InputStream inputStream = HttpUtil.createGet(res).execute().bodyStream();
        String s = savePic(request, inputStream,path,basePath);
        map.put("url",s);
        map.put("title",s.substring(s.indexOf(".")));
        map.put("size",1);
        map.put("source",res);
        return map;
    }
}
