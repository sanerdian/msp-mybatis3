package com.jnetdata.msp.metasite.metasite;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.util.PathUtil;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.zdff.publishdistribution.service.impl.StringUtil;
import io.swagger.annotations.ApiModel;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.store.download.DownloadUtil;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metasite")
@ApiModel(value = "MetasiteController")
public class MetasiteController extends BaseController {

    @Autowired
    TableinfoService tableinfoService;
    @Autowired
    ViewTableService viewTableService;
    @Autowired
    FieldinfoService fieldinfoService;
    @Autowired
    SiteService siteService;
    @Autowired
    CompanyInfoService companyInfoService;
    @Autowired
    ProgramaService programaService;
    @Autowired
    ModuleinfoService moduleinfoService;
    @Autowired
    MetadataGroupService metadataGroupService;
    @Autowired
    TemplateService templateService;
    @Autowired
    ClassService classService;

    public void downMetasite() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, ParseException, InvocationTargetException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelUtil.createFont(workbook); //字体样式
        createTableinfoSheet(workbook);
    }

    public void createTableinfoSheet(XSSFWorkbook workbook) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, ParseException, IllegalAccessException {
        XSSFSheet sheet = workbook.createSheet("tableinfo");
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
        excel.add(new ExcelBean("tablename","tablename"));
        map.put(0, excel);

        List<Tableinfo> list = tableinfoService.list();

        ExcelUtil.createTableHeader(sheet, map); //创建标题（头）
        ExcelUtil.createTableRows(sheet, map, list); //创建内容


    }

    @GetMapping("getPatth")
    @ResponseBody
    public JsonResult getPatth() {
        return renderSuccess(PathUtil.getLibPath());
    }

    @GetMapping("exportJar1/{siteId}")
    public void exportJar(@PathVariable("siteId") Long id,HttpServletResponse response){
        Site site = siteService.getById(id);
        if(site == null){
            throw new RuntimeException("站点数据异常");
        }
        if(site.getModuleId()!=null) {
            Moduleinfo moduleinfo = moduleinfoService.getById(site.getModuleId());
            String libJarPath = PathUtil.getLibJarPath(moduleinfo.getEnglishname(),1);
            doDownload(libJarPath,libJarPath.substring(libJarPath.lastIndexOf(File.separator)+1),response);
        }
    }

    @GetMapping("exportJar2/{siteId}")
    public void exportJar2(@PathVariable("siteId") Long id,HttpServletResponse response){
        Site site = siteService.getById(id);
        if(site == null){
            throw new RuntimeException("站点数据异常");
        }
        if(site.getModuleId()!=null) {
            Moduleinfo moduleinfo = moduleinfoService.getById(site.getModuleId());
            String libJarPath = PathUtil.getLibJarPath(moduleinfo.getEnglishname(),2);
            doDownload(libJarPath,libJarPath.substring(libJarPath.lastIndexOf(File.separator)+1),response);
        }
    }

    @SneakyThrows
    public void doDownload(String downloadFilePath, String targetFilename, HttpServletResponse response) {
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" +  targetFilename );
        //设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        //文件输入流
        FileInputStream in = new FileInputStream(downloadFilePath);
        //获取响应输出流
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        FileCopyUtils.copy(in,out);
    }

    @PostMapping("importJar")
    @ResponseBody
    public JsonResult importJar(@RequestParam(value = "file",required = true) MultipartFile mFile ) throws IOException {
        String libPath = PathUtil.getLibPath();
        libPath = libPath + File.separator + mFile.getOriginalFilename();
        FileUtil.writeFromStream(mFile.getInputStream(),libPath);
        return renderSuccess();
    }

    @GetMapping("export/{siteId}")
    public void export(@PathVariable("siteId") Long id,HttpServletResponse response) {
        Site site = siteService.getById(id);
        if(site == null){
            throw new RuntimeException("站点数据异常");
        }
        Companyinfo companyinfo = companyInfoService.getById(site.getCompanyId());
        if(companyinfo == null){
            throw new RuntimeException("站点所属机构数据异常");
        }
        List<Programa> programaList = programaService.list(new PropertyWrapper<>(Programa.class).eq("siteId" , id));
        List<Template> templateList = templateService.list(new PropertyWrapper<>(Template.class).eq("siteid" , id));
        ExcelWriter excelWriter = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("datas", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            excelWriter =  EasyExcel.write(response.getOutputStream()).registerConverter(new LongStringConverter()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("机构")
                    .head(Companyinfo.class)
                    .build();
            excelWriter.write(Arrays.asList(companyinfo), writeSheet);

            writeSheet = EasyExcel.writerSheet("站点")
                    .head(Site.class)
                    .build();
            excelWriter.write(Arrays.asList(site), writeSheet);

            writeSheet = EasyExcel.writerSheet("栏目")
                    .head(Programa.class)
                    .build();
            excelWriter.write(programaList, writeSheet);

            writeSheet = EasyExcel.writerSheet("风格模板")
                    .head(Template.class)
                    .build();
            excelWriter.write(templateList, writeSheet);

            if(site.getModuleId()!=null){
                Moduleinfo moduleinfo = moduleinfoService.getById(site.getModuleId());
                //元数据表
                List<Tableinfo> tableinfoList = tableinfoService.list(new PropertyWrapper<>(Tableinfo.class).eq("ownerid",moduleinfo.getId()));
                List<Long> tableidList = tableinfoList.stream().map(m -> m.getId()).collect(Collectors.toList());
                //元数据字段
                List<Fieldinfo> fieldinfoList = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).in("tableid",tableidList));
//                Map<Long, List<String>> fieldinfoMap = fieldinfoList.stream().collect(Collectors.groupingBy(m -> m.getTableid(), Collectors.mapping(Fieldinfo::getFieldname, Collectors.toList())));
                //元数据分组
                List<Long> groupIds = fieldinfoList.stream().map(m -> m.getGroupid()).collect(Collectors.toList());
                List<MetadataGroup> groupList = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).in("id" , groupIds));
                List<Long> groupPids = groupList.stream().distinct().map(m -> m.getParentid()).collect(Collectors.toList());
                List<MetadataGroup> groupPList = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).in("id" , groupPids));
                groupPList.addAll(groupList);
                //元数据分类法
                List<com.jnetdata.msp.metadata.Class.model.Class> classList = classService.getAllForModuld(moduleinfo.getId());

                writeSheet = EasyExcel.writerSheet("元数据模块")
                        .head(Moduleinfo.class)
                        .build();
                excelWriter.write(Arrays.asList(moduleinfo), writeSheet);

                writeSheet = EasyExcel.writerSheet("元数据表")
                        .head(Tableinfo.class)
                        .build();
                excelWriter.write(tableinfoList, writeSheet);

                writeSheet = EasyExcel.writerSheet("元数据字段")
                        .head(Fieldinfo.class)
                        .build();
                excelWriter.write(fieldinfoList, writeSheet);

                writeSheet = EasyExcel.writerSheet("元数据分组")
                        .head(MetadataGroup.class)
                        .build();
                excelWriter.write(groupPList, writeSheet);

                writeSheet = EasyExcel.writerSheet("元数据分类法")
                        .head(com.jnetdata.msp.metadata.Class.model.Class.class)
                        .build();
                excelWriter.write(classList, writeSheet);


                if(!tableinfoList.isEmpty()){
                    //从ApplicationContext中取出已创建好的的对象
                    //不可直接反射创建serviceimpi对象，因为反射创建出来的对象无法实例化dao接口
                    ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
                    //反射创建serviceimpi实体对象，和实体类
                    for (Tableinfo tableinfo : tableinfoList) {
                        if("table".equals(tableinfo.getTabletype())){
                            String entityName = PathUtil.getEntityName(tableinfo.getTablename());
                            String entityPackage = PathUtil.getEntityPackage(tableinfo.getTablename());

                            Class<?> serviceImpl = Class.forName("com.jnetdata.msp."+moduleinfo.getEnglishname()+"."+entityPackage+".service.impl."+entityName+"ServiceImpl");
                            Class<?> entity = Class.forName("com.jnetdata.msp."+moduleinfo.getEnglishname()+"."+entityPackage+".model." + entityName);
                            //反射设置方法参数。
                            Method method = serviceImpl.getMethod("list");
                            //在ApplicationContext中根据class取出已实例化的bean
                            List datas = (List) method.invoke(applicationContext.getBean(serviceImpl));
                            writeSheet = EasyExcel.writerSheet(tableinfo.getTablename())
                                    .head(entity)
                                    .build();
                            excelWriter.write(datas, writeSheet);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @PostMapping("import")
    @ResponseBody
    public JsonResult importData(@RequestParam(value = "file",required = true) MultipartFile mFile ) throws IOException {
        List<Companyinfo> companyinfoList = EasyExcel.read(mFile.getInputStream()).sheet("机构" ).head(Companyinfo.class).doReadSync();
        List<Site> siteList = EasyExcel.read(mFile.getInputStream()).sheet("站点" ).head(Site.class).doReadSync();
        List<Programa> programaList = EasyExcel.read(mFile.getInputStream()).sheet("栏目" ).head(Programa.class).doReadSync();
        List<Moduleinfo> moduleinfoList = EasyExcel.read(mFile.getInputStream()).sheet("元数据模块" ).head(Moduleinfo.class).doReadSync();
        List<Tableinfo> tableinfoList = EasyExcel.read(mFile.getInputStream()).sheet("元数据表" ).head(Tableinfo.class).doReadSync();
        List<Fieldinfo> fieldinfoList = EasyExcel.read(mFile.getInputStream()).sheet("元数据字段" ).head(Fieldinfo.class).doReadSync();
        List<MetadataGroup> metadataGroupList = EasyExcel.read(mFile.getInputStream()).sheet("元数据分组" ).head(MetadataGroup.class).doReadSync();
        List<Template> templateList = EasyExcel.read(mFile.getInputStream()).sheet("风格模板" ).head(Template.class).doReadSync();
        List<com.jnetdata.msp.metadata.Class.model.Class> classList = EasyExcel.read(mFile.getInputStream()).sheet("元数据分类法" ).head(com.jnetdata.msp.metadata.Class.model.Class.class).doReadSync();

        if(companyinfoList!=null) companyInfoService.insertOrUpdateBatch(companyinfoList);
        if(siteList!=null) siteService.insertOrUpdateBatch(siteList);
        if(programaList!=null) programaService.insertOrUpdateBatch(programaList);
        if(moduleinfoList!=null) moduleinfoService.insertOrUpdateBatch(moduleinfoList);
        if(tableinfoList!=null){
            tableinfoService.insertOrUpdateBatch(tableinfoList);
            Map<String, List<Tableinfo>> collect = tableinfoList.stream().collect(Collectors.groupingBy(m -> m.getTabletype()));
            for (Tableinfo table : collect.get("table")) {
                tableinfoService.createTable(table);
            }

            if(fieldinfoList!=null){
                for (Fieldinfo fieldinfo : fieldinfoList) {
                    if(!fieldinfo.getTablename().startsWith("VIEW_"))
                        fieldinfoService.addField(fieldinfo);
                }
            }

            for (Tableinfo view : collect.get("view")) {
                if(StringUtil.isNotEmpty(view.getViewsql()))
                viewTableService.createView(view);
            }
        }
        if(metadataGroupList!=null) metadataGroupService.insertOrUpdateBatch(metadataGroupList);
        if(templateList!=null) templateService.insertOrUpdateBatch(templateList);
        if(classList!=null) classService.insertOrUpdateBatch(classList);


        List<ReadSheet> readSheets = EasyExcel.read(mFile.getInputStream()).build().excelExecutor().sheetList();
        readSheets = readSheets.subList(9, readSheets.size());
        for (ReadSheet readSheet : readSheets) {
            try {
                String sheetName = readSheet.getSheetName();
                //从ApplicationContext中取出已创建好的的对象
                //不可直接反射创建serviceimpi对象，因为反射创建出来的对象无法实例化dao接口
                ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
                String entityName = PathUtil.getEntityName(sheetName);
                String entityPackage = PathUtil.getEntityPackage(sheetName);
                String modulePackage = moduleinfoList.get(0).getEnglishname();
                //反射创建serviceimpi实体对象，和实体类
                Class<?> serviceImpl = Class.forName("com.jnetdata.msp."+modulePackage+"."+entityPackage+".service.impl."+entityName+"ServiceImpl");
                Class<?> entity = Class.forName("com.jnetdata.msp."+modulePackage+"."+entityPackage+".model." + entityName);
                //反射设置方法参数。
                Method method = serviceImpl.getMethod("insertOrUpdateBatch",List.class);
                //在ApplicationContext中根据class取出已实例化的bean
                List objects = EasyExcel.read(mFile.getInputStream()).head(entity).sheet(readSheet.getSheetNo()).doReadSync();
                if(objects != null && !objects.isEmpty()){
                    method.invoke(applicationContext.getBean(serviceImpl),objects);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return renderSuccess();
    }

}
