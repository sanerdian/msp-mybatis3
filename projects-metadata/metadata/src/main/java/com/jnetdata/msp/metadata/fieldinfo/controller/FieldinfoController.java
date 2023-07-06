package com.jnetdata.msp.metadata.fieldinfo.controller;


import cn.hutool.http.HttpRequest;
import java.net.HttpURLConnection;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;
import com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService;
import freemarker.template.Configuration;
import freemarker.template.Template;




import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedLong;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.IpUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.metadata.service.MetadataLogService;
import com.jnetdata.msp.metadata.dict.model.Dict;
import com.jnetdata.msp.metadata.dict.service.DictService;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.model.VerifyTypeEnum;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.fieldinfo.untiy.CellColorSheetWriteHandler;
import com.jnetdata.msp.metadata.fieldinfo.untiy.ExcelFillCellMergePrevCol;
import com.jnetdata.msp.metadata.fieldinfo.vo.*;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/metadata/fieldinfo")
@ApiModel(value = "元数据字段管理(FieldinfoController)", description = "元数据字段管理")
public class FieldinfoController extends BaseController<Long, Fieldinfo> {

    @Autowired
    private MetaDataOperatorService metaDataOperatorService;
    @Autowired
    private FieldinfoService fieldinfoService;
    @Autowired
    private DictService dictService;
    @Autowired
    private MetadataGroupService metadataGroupService;
    @Autowired
    private ContentLogService contentLogService;
    @Autowired
    private TableinfoService tableinfoService;
    @Autowired
    private MetadataLogService metadataLogService;

    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "根据实体属性查询字段列表")
    public JsonResult<Pager<Fieldinfo>> getList(@RequestBody FieldinfoVo vo) {
        return renderSuccess(getService().getFields(vo.getPager(),createCondition(vo.getEntity())));
    }

    @GetMapping(value = "/all")
    @ResponseBody
    @ApiOperation(value = "分组字段列表")
    public JsonResult<Map<String, List<Fieldinfo>>> all(Long tableId) {
        return renderSuccess(getService().getGroupFields(tableId));
    }

    @GetMapping(value = "/listall")
    @ResponseBody
    @ApiOperation(value = "查询所有字段列表")
    public JsonResult<List<Fieldinfo>> listall(Long tableid) {
        List<Pair<String,Boolean>> sp = new ArrayList<>();
        sp.add(new Pair("fieldorder",true));
        List<Fieldinfo> result = getService().list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid",tableid).orderBy(sp));
        return renderSuccess(result);
    }

    @ApiOperation(value = "添加字段")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Fieldinfo entity) {
        entity.setCreateTime(new Date());
        String result = fieldinfoService.addField(entity);
        if(result != null) return renderError(result);
        contentLogService.addLog("新建字段","元数据管理","元数据表设计",true);
        return renderSuccess();
    }

    @ApiOperation(value = "更新生成java代码应用信息")
    @PostMapping("updateState")
    @ResponseBody
    public JsonResult<Void> updateState(@RequestBody Long id){
        Tableinfo t = new Tableinfo();
        t.setId(id);
        t.setGeneratetime(new Date());
        tableinfoService.updateById(t);
        Fieldinfo fieldinfos = new Fieldinfo();
        fieldinfos.setGeneratetime(new Date());
        fieldinfos.setGeneratestate(1L);
        boolean b = getService().update(fieldinfos,new PropertyWrapper<>(Fieldinfo.class).eq("tableid",id));
        contentLogService.addLog("生成java代码信息","元数据管理","元数据表设计",b);
       /* MetaDataOperator metaDataOperator = new MetaDataOperator();
        Fieldinfo byId = getService().getById(id);
        val user = SessionUser.getCurrentUser();
        metaDataOperator.setUserid(user.getId());
        metaDataOperator.setUsername(user.getName());
        metaDataOperator.setHandletype("修改视图");
        metaDataOperator.setTitle(byId.getAnothername());
        metaDataOperatorService.insert(metaDataOperator);*/

        if(b){
            return renderSuccess("生成成功");
        }else {
            return renderError("生成失败");
        }
    }

    @ApiOperation(value = "删除java代码应用信息")
    @PostMapping("deleteState")
    @ResponseBody
    public JsonResult<Void> deleteState(@RequestBody Long id){
        fieldinfoService.updateTableGen(id);

        Fieldinfo fieldinfos = new Fieldinfo();
        fieldinfos.setGeneratetime(null);
        fieldinfos.setGeneratestate(0L);
        boolean b = getService().update(fieldinfos,new PropertyWrapper<>(Fieldinfo.class).eq("tableid",id));
        contentLogService.addLog("删除java应用代码信息","元数据管理","元数据表设计",b);
        if(b){
            return renderSuccess("删除成功");
        }else {
            return renderError("删除失败");
        }
    }

    @ApiOperation(value = "删除", notes="删除字段")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("内容操作日志id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除字段")
    @PostMapping(value = "/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody String[] ids,HttpServletRequest request) {
        for(String id : ids ){
            Fieldinfo obj = getService().getById(id);
            getService().deleteField(obj);
            metadataLogService.addlogd(MapUtil.toMap(obj),IpUtil.getRequestIp(request));

            contentLogService.addLog("删除字段","元数据管理","元数据表设计",true);
        }
        return doDeleteBatchIds(getService(), Arrays.asList(ids));
    }

    @ApiOperation(value = "修改", notes="修改字段")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("字段id") @PathVariable("id") Long id,@RequestBody Fieldinfo entity,HttpServletRequest request) {
        entity.setId(id);
        Fieldinfo byId = getService().getById(id);
        boolean b = getService().updateField(entity);
        metadataLogService.addFieldLog(MapUtil.toMap(byId),MapUtil.toMapNotNull(entity), IpUtil.getRequestIp(request),id);
        contentLogService.addLog("修改字段","元数据管理","元数据表设计",b);
        return b ? renderSuccess("更新成功") : renderError("更新失败");
    }

    @ApiOperation(value = "排序")
    @PutMapping(value = "/sort")
    @ResponseBody
    public JsonResult<Void> sort(@RequestBody Fieldinfo entity) {
        getService().sort(entity);
        getService().updateById(entity);
        return renderSuccess();
    }

    @ApiOperation(value = "根据id查询", notes="查看字段")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Fieldinfo> getById(@ApiParam("字段id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "查看多条字段", notes="根据多个id查看字段")
    @PostMapping(value = "/listByIds")
    @ResponseBody
    public JsonResult<List<Fieldinfo>> getById(@RequestBody Long[] ids) {
        return renderSuccess(getService().getByIds(ids));
    }


    @ApiOperation(value = "查看校验类型列表", notes="查询校验类型列表")
    @GetMapping(value = "/verifyTypeList")
    @ResponseBody
    public JsonResult verifyTypeList() {
        VerifyTypeEnum[] values = VerifyTypeEnum.values();
        List<Map<String,Object>> list = new ArrayList<>();
        for (VerifyTypeEnum value : values) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",value.getValue());
            map.put("name",value.getName());
            map.put("hasLength",value.getHasLength());
            list.add(map);
        }
        return renderSuccess(list);
    }
   /* @Resource
    private TableinfoService tableinfoService;*/

    @PostMapping(value = "/listerr")
    @ResponseBody
    @ApiOperation(value = "根据实体属性查询er图")
    public JsonResult<ErTableEntity> selectLister1(@RequestBody FieldinfoVo vo) {
        String tablename1=vo.getEntity().getTablename();
        //把list集合转换为set集合,是为了去重
        Set<String>set2 = new HashSet<>();
        ArrayList<String> strings2 = new ArrayList<>();
        strings2.add(tablename1.toLowerCase());
        set2.addAll(strings2);
        Set<TableEntiy> tableEntiys = new HashSet<>();
        ErTableEntity erTableEntity = new ErTableEntity();
        List<TableNameVo> tableNameVos=new ArrayList<>();
        for (int a=0;a<strings2.size();a++){
            String s = strings2.get(a).toUpperCase();
            System.out.printf("s"+s);
            List<String> strings = fieldinfoService.selectErtable(s);
            if(!strings.isEmpty()){
                List<String> strings1= strings.stream().map(String::toLowerCase).collect(Collectors.toList());
                strings1.forEach(str -> {
                    if (set2.add(str)) {
                        set2.add(str);
                        strings2.add(str);
                    }
                });
            }
        }
        //此处循环查询外键
        for (int a=0;a<strings2.size();a++){
            String s2 = strings2.get(a);
            List<Fieldinfo> tablename2 = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", s2.toUpperCase()));
            if(!tablename2.isEmpty()) {
                TableNameVo tableNameVo = new TableNameVo();
                String tablename3 = tablename2.get(0).getTablename();
                Tableinfo tablename4 = tableinfoService.get(new PropertyWrapper<>(Tableinfo.class).eq("tablename", tablename3.toUpperCase()));
                tableNameVo.setTablename(tablename3.toLowerCase());
                tableNameVo.setTableChinaName(tablename4.getAnothername());
                ArrayList<FormNameVo> stringss = new ArrayList<>();
                //遍历获取字段名
                tablename2.forEach(s1 -> {
                    FormNameVo formNameVo = new FormNameVo();
                    formNameVo.setFormName(s1.getFieldname());
                    formNameVo.setFormChinaName(s1.getAnothername());
                    stringss.add(formNameVo);
                });
                tableNameVo.setTablelist(stringss);
                tableNameVos.add(tableNameVo);
            }
            if(strings2.size()!=1) {
                List<Map> tablenameq = fieldinfoService.getErtable(strings2.get(a));
                tablenameq.forEach(s -> {
                    TableEntiy tableEntiy = new TableEntiy();
                    tableEntiy.setTname(String.valueOf(s.get("tname")));
                    tableEntiy.setTnamee(String.valueOf(s.get("tnamee")));
                    tableEntiys.add(tableEntiy);
                });
            }
        }
        ArrayList<TableEntiy> tableEntiys1 = new ArrayList<>();
        tableEntiys1.addAll(tableEntiys);
        erTableEntity.setTableEntiy(tableEntiys1);
        erTableEntity.setTableNameVo(tableNameVos);
        return renderSuccess(erTableEntity);
    }
    @PostMapping(value = "/getter")
    @ResponseBody
    @ApiOperation(value = "根据实体属性查询er图")
    public JsonResult<Set<TableEntiy>> getLister(@RequestBody FieldinfoVo vo) {
        Long tableid = vo.getEntity().getTableid();
        List<Fieldinfo> lister = getService().lister(tableid);
        if(lister.isEmpty()){
            return null;
        }
        // 去数据库查找表
        String tablename1 = tableinfoService.getById(tableid).getTablename();
        Set<TableEntiy> tableEntiys = new HashSet<>();
        /*//查找表关联的外键
        List<Map> tablename= fieldinfoService.getErtable(tablename1);*/
        Set<String>set2 = new HashSet<>();
        //查找表关联的外键
        List<String> tablename=fieldinfoService.selectErtable(tablename1);
        tablename.add(tablename1.toLowerCase());
        set2.addAll(tablename);
        ArrayList<String> strings2 = new ArrayList<>();
        strings2.addAll(set2);
        //此处循环查询外键
        for (int a=0;a<strings2.size();a++){
            List<String> strings = fieldinfoService.selectErtable(strings2.get(a));
            if(!strings.isEmpty()){
                List<String> strings1= strings.stream().map(String::toLowerCase).collect(Collectors.toList());
                strings1.forEach(str -> {
                    if (set2.add(str)) {
                        set2.add(str);
                        strings2.add(str);
                    }
                });
            }
        }
        set2.forEach(s1->{
            List<Map> tablenameq= fieldinfoService.getErtable(s1);
            tablenameq.forEach(s->{
                TableEntiy tableEntiy = new TableEntiy();
                tableEntiy.setTname(String.valueOf(s.get("tname")));
                tableEntiy.setTnamee(String.valueOf(s.get("tnamee")));
                tableEntiys.add(tableEntiy);
            });

        });
        return renderSuccess(tableEntiys);
    }

    @Value("${spring.datasource.jdbcDialect}")
    private String jdbcDialect;


    @GetMapping(value = "/downloadexclee/{tablenamee}")
    @ResponseBody
    @ApiOperation(value = "导出excle图")
    public void  exportt(@PathVariable("tablenamee") String tablenamee,HttpServletResponse response) {
        Map<Integer, String> db_type = dictService.list("db_type_"+jdbcDialect).stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));

        //设置样式
        HashMap<Integer, List<Integer>> map = new HashMap<>();

        List<ExcleTableVo> result = new ArrayList<>();
        if(!tablenamee.isEmpty()) {
            //设置合并单元格
            Set<String>set2 = new HashSet<>();
            set2.add(tablenamee.toLowerCase());
            ArrayList<String> strings2 = new ArrayList<>();
            strings2.addAll(set2);
            ExcelFillCellMergePrevCol mergePrevCol = new ExcelFillCellMergePrevCol();

            for (int a=0;a<strings2.size();a++){
                List<String> strings = fieldinfoService.selectErtable(strings2.get(a).toUpperCase());
                if(!strings.isEmpty()){
                    List<String> strings1= strings.stream().map(String::toLowerCase).collect(Collectors.toList());
                    strings1.forEach(str -> {
                        if (set2.add(str)) {
                            set2.add(str);
                            strings2.add(str);
                        }
                    });
                }
            }
            for (int a=0;a<strings2.size();a++){
                List<Fieldinfo> tablename2 = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", strings2.get(a).toUpperCase()));
                if(!tablename2.isEmpty()){
                    ExcleTableVo excleTableVo1 = new ExcleTableVo();
                    //添加表名的值
                    excleTableVo1.setTableChinaName("表名");
                    excleTableVo1.setTableName(tablename2.get(0).getTablename());
                    mergePrevCol.add(result.size(),1,3);
                    map.put(result.size(),Arrays.asList(0));
                    result.add(excleTableVo1);
                    ExcleTableVo excleTableVo2 = new ExcleTableVo();
                   String majorKey= getService().getMajorKey(tablename2.get(0).getTablename().toUpperCase());
                    ExcleTableVo excleTableVo4 = new ExcleTableVo();
                    //添加表名的值
                    excleTableVo4.setTableChinaName("主键");
                    excleTableVo4.setTableName(majorKey);
                    mergePrevCol.add(result.size(),1,3);
                    map.put(result.size(),Arrays.asList(0));
                    result.add(excleTableVo4);

                    List<String> name = getService().excleEr(tablename2.get(0).getTablename().toUpperCase());
                   //拼接外键的值
                    String foreiqukey = "";
                    for (int d = 0; d < name.size(); d++) {
                        foreiqukey += name.get(d)+" ";
                    }
                    excleTableVo2.setTableChinaName("外键");
                    excleTableVo2.setTableName(foreiqukey);
                    mergePrevCol.add(result.size(),1,3);
                    map.put(result.size(),Arrays.asList(0));
                    result.add(excleTableVo2);
                    //添加表头
                    ExcleTableVo excleTableVo3 = new ExcleTableVo();
                    excleTableVo3.setTableChinaName("中文描述");
                    excleTableVo3.setTableName("英文描述");
                    excleTableVo3.setFieldType("字段类型");
                    excleTableVo3.setLibraryType("库中类型");
                    excleTableVo3.setLibraryLength("库中长度");
                    map.put(result.size(),Arrays.asList(0,1,2,3,4));
                    result.add(excleTableVo3);
                    result.addAll(getall());
                    //写入数据
                    tablename2.forEach(s -> {
                        ExcleTableVo excleTableVo = new ExcleTableVo();
                        excleTableVo.setTableName(s.getFieldname());
                        excleTableVo.setTableChinaName(s.getAnothername());
                        excleTableVo.setLibraryLength(s.getDblength());
                        excleTableVo.setFieldType(field_type.get(s.getFieldtype()));
                        excleTableVo.setLibraryType(db_type.get(s.getDbtype()));
                        result.add(excleTableVo);
                    });
                    if (a!=strings2.size()-1) {
                        ExcleTableVo excleTableVo5 = new ExcleTableVo();
                        excleTableVo5.setTableName("");
                        mergePrevCol.add(result.size(), 0, 4);
                        result.add(excleTableVo5);
                    }

                }
            }
            //指定单元格样式
            CellColorSheetWriteHandler writeHandler = new CellColorSheetWriteHandler(map, IndexedColors.RED.getIndex());
            List<String> stringss = Arrays.asList("tableChinaName","tableName", "fieldType", "libraryType","libraryLength");
            DownEasyExcel(response,result,ExcleTableVo.class,stringss,tablenamee,mergePrevCol,writeHandler);

        }
    }



    public void DownEasyExcel(HttpServletResponse response, List  data, Class clas, List<String> fieldNames, String excel,ExcelFillCellMergePrevCol mergePrevCol,CellColorSheetWriteHandler writeHandler){
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(excel, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //下载
            EasyExcel.write(response.getOutputStream(), clas).needHead(false)
                    .registerWriteHandler(mergePrevCol).registerWriteHandler(writeHandler).includeColumnFiledNames(fieldNames).sheet(excel).doWrite(data);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* @PostMapping(value = "/downloadword")
    @ResponseBody
    @ApiOperation(value = "导出word图")
    public void  wordDown(@RequestBody ReqWordVo vo,HttpServletResponse response) throws IOException{
        String tablenamee = vo.getTablenamee();*/
   @GetMapping(value = "/downloadword/{tablenamee}")
   @ResponseBody
   @ApiOperation(value = "导出word图")
   public void  wordDown(@PathVariable("tablenamee") String tablenamee,HttpServletResponse response) throws IOException{

       Map<Integer, String> db_type = dictService.list("db_type_"+jdbcDialect).stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        //组装表格列表数据
        List<Map<String,Object>> tList=new ArrayList<Map<String,Object>>();
        if(!tablenamee.isEmpty()) {
            //设置合并单元格
            Set<String>set2 = new HashSet<>();
            set2.add(tablenamee.toLowerCase());
            ArrayList<String> strings2 = new ArrayList<>();
            strings2.addAll(set2);
            List<Map> params = Lists.newArrayList();
            for (int a=0;a<strings2.size();a++){
                List<String> strings = getService().selectErtable(strings2.get(a).toUpperCase());
                if(!strings.isEmpty()){
                    List<String> strings1= strings.stream().map(String::toLowerCase).collect(Collectors.toList());
                    strings1.forEach(str -> {
                        if (set2.add(str)) {
                            set2.add(str);
                            strings2.add(str);
                        }
                    });
                }
            }
            for (int a=0;a<strings2.size();a++){
                List<Fieldinfo> tablename2 = getService().list(new PropertyWrapper<>(Fieldinfo.class).eq("tablename", strings2.get(a).toUpperCase()));
                if(!tablename2.isEmpty()){
                    Map<String, Object> mapList = new HashMap<>();
                    //准备导出数据
                    List<Map<String, Object>> listUsers = new ArrayList<>();

                    Map<String,Object> detailMap = new HashMap<String, Object>();
                    detailMap.put("sub_type",tablename2.get(a).getTablename());
                    listUsers.addAll(getalle());
                    //写入数据
                    tablename2.forEach(s -> {
                        Map<String, Object> maptable = new HashMap<>();
                        maptable.put("tableChinaname",s.getAnothername());
                        maptable.put("tableEnglishName",s.getFieldname());
                        maptable.put("fieldType",field_type.get(s.getFieldtype()));
                        maptable.put("libraryType",db_type.get(s.getDbtype()));
                        maptable.put("libraryLength",s.getDblength());
                        listUsers.add(maptable);
                    });
                    //添加到返回集合中
                    mapList.put("tableName", tablename2.get(0).getTablename());
                    mapList.put("title",tablename2.get(0).getTablename());
                    String majorKey= getService().getMajorKey(tablename2.get(0).getTablename().toUpperCase());
                    List<String> name = getService().getErPhone(tablename2.get(0).getTablename().toUpperCase());
                    //拼接外键的值
                    String foreiqukey = "";
                    for (int d = 0; d < name.size(); d++) {
                        foreiqukey += name.get(d)+" ";
                    }
                    Map<String,Object> tMap = new HashMap<String, Object>();
                    tMap.put("title","1");
                    tMap.put("foreignKey",foreiqukey);
                    tMap.put("majorkey", majorKey);
                    Tableinfo tablename4 = tableinfoService.get(new PropertyWrapper<>(Tableinfo.class).eq("tablename", tablename2.get(a).getTablename().toUpperCase()));
                    tMap.put("tablename",tablename2.get(a).getTablename());
                    tMap.put("sub_type",tablename4.getAnothername()+"("+tablename2.get(a).getTablename()+")");
                    tMap.put("detailList",listUsers);
                    tList.add(tMap);
                }
            }
        }
        //获取模板地址
        InputStream is = FieldinfoController.class.getClassLoader().getResourceAsStream("template/ertu.docx");
       String canonicalName = is.getClass().getCanonicalName();
       System.out.println(canonicalName);
       HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
        Configure config = Configure.newBuilder()
                .bind("detailList", policy).build();

        XWPFTemplate template = XWPFTemplate.compile(FieldinfoController.class.getClassLoader().getResourceAsStream("cm/tabletwo.docx"), config).render(
                new HashMap<String, Object>() {{
                    put("typeProducts",tList);
                    put("order_number", tablenamee);
                }}
        );
        //=================生成文件保存在本地D盘某目录下=================
        String temDir="./"; ;//生成临时文件存放地址
        // 生成的word格式
        String formatSuffix = ".docx";



        // 拼接后的文件名
        String fileName = tablenamee + formatSuffix;//文件名  带后缀

        FileOutputStream fos = new FileOutputStream(temDir+fileName);
        template.write(fos);
        //=================生成word到设置浏览默认下载地址=================
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        OutputStream out = response.getOutputStream();
        template.write(out);
        out.flush();
        out.close();
        template.close();

    }
    @GetMapping(value = "/downWordDocument/{tableid}")
    @ResponseBody
    @ApiOperation(value = "导出接口文档")
    public void  wordDownDocument(@PathVariable("tableid") Long tableid,HttpServletResponse response) throws IOException{
        FieldinfoVo formNameVo = new  FieldinfoVo();
        Fieldinfo fieldinfo = new Fieldinfo();
        fieldinfo.setTableid(tableid);
        formNameVo.setEntity(fieldinfo);
        PageVo1 pageVo1 = new PageVo1();
        pageVo1.setSize(100);
        pageVo1.setCurrent(1);
        formNameVo.setPager(pageVo1);
        Pager fields = getService().getFields(formNameVo.getPager(), createCondition(formNameVo.getEntity()));
        List<Fieldinfo> records = fields.getRecords();
        Map<String, Object> mapList = new HashMap<>();
        //准备导出数据
        List<Map<String, Object>> listUsers = new ArrayList<>();
        List<Map<String, Object>> listUserss = new ArrayList<>();
        List<Map<String, Object>> listUsersss = new ArrayList<>();
        List<Map<String,Object>> tList=new ArrayList<Map<String,Object>>();
        StringBuilder sb = new StringBuilder();
        StringBuilder fanhui = new StringBuilder();
        StringBuilder daochu=new StringBuilder();
        daochu.append("{\n" +
                "  \"area\": 0,\n" +
                "  \"entity\": {");
        fanhui.append("{\n" +
                "\t\"msg\": \"\",\n" +
                "\t\"obj\": {\n" +
                "\t\t\"current\": 0,\n" +
                "\t\t\"pages\": 0,\n" +
                "\t\t\"records\": [\n" +
                "\t\t\t{\n");
        sb.append("{\n entity{\n");
        String tablename = records.get(0).getTablename();
        int i1 = tablename.indexOf("_");
        String substring1 = tablename.substring(i1+1);
        int i2 = substring1.indexOf("_");
        String substring2 = substring1.substring(0, i2);
        String substring3 = substring1.substring(i2+1);
        String s2 = (substring2 + substring3).toLowerCase();
        records.forEach(s->{
            Map<String, Object> maptable = new HashMap<>();
            maptable.put("anothername",s.getAnothername());
            if(s.getFieldname()!=null){
                maptable.put("fieldname",s.getFieldname());
            }else {
                maptable.put("fieldname", s.getAnothername());
            }
            String fanhuiz = fanhuiz(s.getFieldTypeStr());
            String fanhuiz1 = fanhuiz(s.getDbTypeStr());

            maptable.put("fieldTypeStr",fanhuiz(s.getDbTypeStr()));
            if(s.getFieldTypeStr()=="自定义"){
                sb.append(s.getFieldname()+":").append(0+"\n");
                fanhui.append(s.getFieldname()+":").append(0+"\n");
                daochu.append(s.getFieldname()+":").append(0+"\n");
            }else {
                sb.append(s.getFieldname()+":").append("\"\""+"\n");
                fanhui.append("\t"+s.getFieldname()+":").append("\"\""+"\n");
                daochu.append("\t"+s.getFieldname()+":").append("\"\""+"\n");
            }
            listUsers.add(maptable);
        });
        sb.append(" }\n \"pager\": {\n" +
                "    \"current\": 1,\n" +
                "    \"size\": 10,\n" +
                "    \"sortProps\": [\n" +
                "      {\n" +
                "        \"key\": {},\n" +
                "        \"value\": {}\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n}");
        fanhui.append("\t}\n" +
                "\t\t],\n" +
                "\t\t\"size\": 0,\n" +
                "\t\t\"sortProps\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"key\": \"\",\n" +
                "\t\t\t\t\"value\": true\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"total\": 0\n" +
                "\t},\n" +
                "\t\"status\": 200,\n" +
                "\t\"success\": true,\n" +
                "\t\"timestamp\": 0\n" +
                "}");
        daochu.append(" \"ext\": \"string\",\n" +
                "  \"fields\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"includeTitle\": 0,\n" +
                "  \"pageEndNo\": 0,\n" +
                "  \"pageSize\": 0,\n" +
                "  \"pageStartNo\": 0,\n" +
                "  \"tableId\": 0\n" +
                "}");
       /* String body = HttpUtil.createGet("http://10.131.3.231:30929/v2/api-docs?group=all").execute().body();*/
        String body = HttpUtil.createGet("http://localhost:8082/v2/api-docs?group=all").execute().body();
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject path = jsonObject.getJSONObject("paths");
       Map<String,List<Object>> map=new HashMap<>();
        path.forEach((key,value) -> {
            JSONObject jsonObject1 = (JSONObject)value;
            /*if(key.contains("/tonghang/jmetaflysites/")){*/
            if(key.contains("/"+s2+"/")){
                ((JSONObject) value).forEach((key3,value3)->{
                    JSONObject jsonObject2 = (JSONObject)value3;

                    if (key3=="post") {
                        Map<String, Object> tMap = new HashMap<String, Object>();
                        tMap.put("requestAddress", key);
                        tMap.put("Interfacedescription",jsonObject2.get("summary"));
                        Object post = jsonObject2.get("parameters");
                        JSONArray postt=(JSONArray) post;
                        postt.forEach(s->{
                            JSONObject jsonObjects =(JSONObject) s;
                            Object name1 = jsonObjects.get("name");
                            String s1 = name1.toString();
                            if(!s1.equals("vo")){
                                int i = s1.indexOf(".");
                                Map<String, Object> maptablee = new HashMap<>();
                                Object description= jsonObjects.get("description");
                                if(description!=null){
                                    String description2= description.toString();
                                    maptablee.put("anothername",description2);
                                }
                                Object type= jsonObjects.get("type");
                                String type2= type.toString();
                                if(i!=-1){
                                    String substring = s1.substring(7);
                                    maptablee.put("fieldname",substring);
                                    maptablee.put("fieldTypeStr",type2);
                                    listUserss.add(maptablee);
                                }
                            }else {
                                Object schema = jsonObjects.get("schema");
                                System.out.println(schema);
                                if(!schema.equals(null)){
                                    JSONObject schemam=(JSONObject)schema;
                                    Object ref = schemam.get("$ref");
                                    String s3 = ref.toString();
                                   if(s3.contains("#/definitions/ExportVo")){
                                       tMap.put("detailList", listUsers);
                                       tMap.put("requestExample", daochu);
                                       /*tMap.put("returnExample", fanhui);*/
                                   }else {
                                       tMap.put("detailList", listUsers);
                                       tMap.put("detailLists", listUsers);
                                       tMap.put("requestExample", sb);
                                       tMap.put("returnExample", fanhui);
                                   }
                                }
                            }


                        });
                        if(postt.size()!=1){
                            tMap.put("detailList", listUsers);
                        }/*else {
                            tMap.put("detailList", listUsers);
                            tMap.put("detailLists", listUsers);
                            tMap.put("requestExample", sb);
                            tMap.put("returnExample", fanhui);
                        }*/

                            tMap.put("wayToRequest", "post");
                        tList.add(tMap);
                    } else {
                        Map<String, Object> tMap = new HashMap<String, Object>();
                        tMap.put("requestAddress", key);
                        JSONObject get = jsonObject1.getJSONObject("get");
                          tMap.put("Interfacedescription",jsonObject2.get("summary"));
                        Object post = jsonObject2.get("parameters");
                        JSONArray postt=(JSONArray) post;
                       /* postt.forEach(s->{
                            JSONObject jsonObjects =(JSONObject) s;
                            Object name1 = jsonObjects.get("name");
                            String s1 = name1.toString();
                            if(!s1.equals("vo")){
                                int i = s1.indexOf(".");
                                Map<String, Object> maptablee = new HashMap<>();
                                Object description= jsonObjects.get("description");
                                if(description!=null){
                                    String description2= description.toString();
                                    maptablee.put("anothername",description2);
                                }
                                Object type= jsonObjects.get("type");
                                String type2= type.toString();
                                if(i!=-1){
                                    String substring = s1.substring(7);
                                    maptablee.put("fieldname",substring);
                                    maptablee.put("fieldTypeStr",type2);
                                    listUsersss.add(maptablee);
                                }
                                }});*/
                        /*get.forEach((key2, value2) -> {*/
                            tMap.put("wayToRequest", "get");
                            tMap.put("detailList", listUsers);
                            tMap.put("detailLists", "");
                             tList.add(tMap);
                        /*});*/
                    };
                });
                /*ArrayList<Object> objects = new ArrayList<>();
                objects.add(value);
                map.put(key,objects);*/
             }
            });
        InputStream fis = FieldinfoController.class.getClassLoader().getResourceAsStream("template/documentt.docx");
        try{
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            directory.getCanonicalPath(); //获取标准路径。该方法需要放置在try/catch块中，或声明抛出异常
        }catch (Exception e){

        }

       /* FileInputStream fis = new FileInputStream(new File("D:\\work\\javawork\\msp\\projects-metadata\\metadata\\src\\main\\resources\\template\\documentt.docx"));
     */   HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
        Configure config = Configure.newBuilder()
                .bind("detailList", policy).bind("detailLists", policy).build();

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("typeProducts",tList);
        stringObjectHashMap.put("order_number", tableid);
        XWPFTemplate template = XWPFTemplate.compile(fis, config)
                .render(stringObjectHashMap);
        //=================生成文件保存在本地D盘某目录下=================
        String temDir="./"; ;//生成临时文件存放地址
        // 生成的word格式
        String formatSuffix = ".docx";
        // 拼接后的文件名
        String fileName = s2 + formatSuffix;//文件名  带后缀
        FileOutputStream fos = new FileOutputStream(temDir+fileName);
        template.write(fos);
        //=================生成word到设置浏览默认下载地址=================
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        OutputStream out = response.getOutputStream();
        template.write(out);
        out.flush();
        out.close();
        template.close();
    }


    //这里是把数据写死了，只要创建表自动会添加所有数据
    public List<ExcleTableVo> getall(){
        List<ExcleTableVo> result = new ArrayList<>();
        ExcleTableVo maptable1 = new ExcleTableVo();maptable1.setTableChinaName("主键id");maptable1.setTableName("ID");maptable1.setFieldType("自定义");maptable1.setLibraryType( "整数");maptable1.setLibraryLength("20"); result.add(maptable1);
        ExcleTableVo maptable2 = new ExcleTableVo();maptable2.setTableChinaName("创建人名称");maptable2.setTableName("CRUSER");maptable2.setFieldType("普通文本");maptable2.setLibraryType("字符串");maptable2.setLibraryLength("255");result.add(maptable2);
        ExcleTableVo maptable3 = new ExcleTableVo();maptable3.setTableChinaName("创建人id");maptable3.setTableName("CRNUMBER");maptable3.setFieldType("自定义");maptable3.setLibraryType("整数");maptable3.setLibraryLength("20");result.add(maptable3);
        ExcleTableVo maptable4 = new ExcleTableVo();maptable4.setTableChinaName("创建时间");maptable4.setTableName("CRTIME");maptable4.setFieldType("日期");maptable4.setLibraryType("时间");maptable4.setLibraryLength("0");result.add(maptable4);
        ExcleTableVo maptable5 = new ExcleTableVo();maptable5.setTableChinaName( "最后修改人名称");maptable5.setTableName( "MODIFY_USER");maptable5.setFieldType("普通文本");maptable5.setLibraryType("字符串");maptable5.setLibraryLength( "255");result.add(maptable5);
        ExcleTableVo maptable6 = new ExcleTableVo();maptable6.setTableChinaName( "最后修改人id");maptable6.setTableName( "MODIFY_BY");maptable6.setFieldType("自定义");maptable6.setLibraryType( "整数");maptable6.setLibraryLength("20");result.add(maptable6);
        ExcleTableVo maptable7 = new ExcleTableVo();maptable7.setTableChinaName( "最后修改时间");maptable7.setTableName("MODIFY_TIME");maptable7.setFieldType("日期");maptable7.setLibraryType("时间");maptable7.setLibraryLength("0");result.add(maptable7);
        ExcleTableVo maptable8 = new ExcleTableVo();maptable8.setTableChinaName("文档id");maptable8.setTableName("DOCCHANNELID");maptable8.setFieldType("自定义");maptable8.setLibraryType( "整数");maptable8.setLibraryLength("20");result.add(maptable8);
        ExcleTableVo maptable9 = new ExcleTableVo();maptable9.setTableChinaName("删除状态");maptable9.setTableName("DOCSTATUS");maptable9.setFieldType("自定义");maptable9.setLibraryType( "整数");maptable9.setLibraryLength("2");result.add(maptable9);
        ExcleTableVo maptable10 = new ExcleTableVo();maptable10.setTableChinaName("站点id");maptable10.setTableName("SITEID");maptable10.setFieldType("自定义");maptable10.setLibraryType( "整数");maptable10.setLibraryLength("20");result.add(maptable10);
        ExcleTableVo maptable11 = new ExcleTableVo();maptable11.setTableChinaName("发布时间");maptable11.setTableName("DOCPUBTIME");maptable11.setFieldType("日期");maptable11.setLibraryType("时间");maptable11.setLibraryLength("0");result.add(maptable11);
        ExcleTableVo maptable12 = new ExcleTableVo();maptable12.setTableChinaName("发布状态");maptable12.setTableName("STATUS");maptable12.setFieldType("自定义");maptable12.setLibraryType("整数");maptable12.setLibraryLength("6");result.add(maptable12);
        ExcleTableVo maptable13 = new ExcleTableVo();maptable13.setTableChinaName("机构id");maptable13.setTableName("COMPANYID");maptable13.setFieldType("自定义");maptable13.setLibraryType("整数");maptable13.setLibraryLength("20");result.add(maptable13);
        ExcleTableVo maptable14 = new ExcleTableVo();maptable14.setTableChinaName("站点id");maptable14.setTableName("WEBSITEID");maptable14.setFieldType("自定义");maptable14.setLibraryType("整数");maptable14.setLibraryLength("20");result.add(maptable14);
        ExcleTableVo maptable15 = new ExcleTableVo();maptable15.setTableChinaName("栏目id");maptable15.setTableName("COLUMNID");maptable15.setFieldType("自定义");maptable15.setLibraryType("整数");maptable15.setLibraryLength( "20");result.add(maptable15);
        ExcleTableVo maptable16 = new ExcleTableVo();maptable16.setTableChinaName("排序");maptable16.setTableName("SEQENCING");maptable16.setFieldType("自定义");maptable16.setLibraryType("整数");maptable16.setLibraryLength( "10");result.add(maptable16);
        ExcleTableVo maptable17 = new ExcleTableVo();maptable17.setTableChinaName("工作流id");maptable17.setTableName("FLOW_ID");maptable17.setFieldType("普通文本");maptable17.setLibraryType("字符串");maptable17.setLibraryLength( "255");result.add(maptable17);
        ExcleTableVo maptable18 = new ExcleTableVo();maptable18.setTableChinaName("工作流用户");maptable18.setTableName("FLOW_USER");maptable18.setFieldType("普通文本");maptable18.setLibraryType("字符串");maptable18.setLibraryLength( "255");result.add(maptable18);
        ExcleTableVo maptable19 = new ExcleTableVo();maptable19.setTableChinaName("引用信息");maptable19.setTableName("QUOTAINFO");maptable19.setFieldType("多行文本");maptable19.setLibraryType("字符串");maptable19.setLibraryLength( "255");result.add(maptable19);
        ExcleTableVo maptable20 = new ExcleTableVo();maptable20.setTableChinaName("复制文档id");maptable20.setTableName("COPYFROMID");maptable20.setFieldType("自定义");maptable20.setLibraryType("整数");maptable20.setLibraryLength( "20");result.add(maptable20);
        //以下是暂时无用的数据
        ExcleTableVo maptable21 = new ExcleTableVo();maptable21.setTableChinaName("操作人");maptable21.setTableName("OPERUSER");maptable21.setFieldType("多行文本");maptable21.setLibraryType( "字符串");maptable21.setLibraryLength("255");result.add(maptable21);
        ExcleTableVo maptable22 = new ExcleTableVo();maptable22.setTableChinaName("操作时间");maptable22.setTableName("OPERTIME");maptable22.setFieldType("日期");maptable22.setLibraryType("时间");maptable22.setLibraryLength("0");result.add(maptable22);
        ExcleTableVo maptable23 = new ExcleTableVo();maptable23.setTableChinaName("文档标题");maptable23.setTableName("DOCTITLE");maptable23.setFieldType("多行文本");maptable23.setLibraryType("字符串");maptable23.setLibraryLength("255");result.add(maptable23);
        ExcleTableVo maptable24 = new ExcleTableVo();maptable24.setTableChinaName("文档时间");maptable24.setTableName("DOCRELTIME");maptable24.setFieldType("日期");maptable24.setLibraryType("时间");maptable24.setLibraryLength("0");result.add(maptable24);
        ExcleTableVo maptable25 = new ExcleTableVo();maptable25.setTableChinaName("文档发布url");maptable25.setTableName("DOCPUBURL");maptable25.setFieldType("多行文本");maptable25.setLibraryType("字符串");maptable25.setLibraryLength("255");result.add(maptable25);
        ExcleTableVo maptable26 = new ExcleTableVo();maptable26.setTableChinaName("文档链接");maptable26.setTableName("LINKURL");maptable26.setFieldType("多行文本");maptable26.setLibraryType("字符串");maptable26.setLibraryLength("255");result.add(maptable26);
        ExcleTableVo maptable27 = new ExcleTableVo();maptable27.setTableChinaName("分类法id");maptable27.setTableName("CLASSINFOID");maptable27.setFieldType("自定义");maptable27.setLibraryType("整数");maptable27.setLibraryLength("20");result.add(maptable27);
        ExcleTableVo maptable28 = new ExcleTableVo();maptable28.setTableChinaName("SINGLETEMPKATE");maptable28.setTableName("SINGLETEMPKATE");maptable28.setFieldType("自定义");maptable28.setLibraryType("整数");maptable28.setLibraryLength("20");result.add(maptable28);
        ExcleTableVo maptable29 = new ExcleTableVo();maptable29.setTableChinaName("DOCVALID");maptable29.setTableName("DOCVALID");maptable29.setFieldType("日期");maptable29.setLibraryType("时间");maptable29.setLibraryLength("0");result.add(maptable29);
        return result;
    }
    /**
     * 将Map中的非空的value拼接为json字符串
     * @param paramMap
     * @return
     */
    public String paramToString(Map<String, Object> paramMap) {
        String json;
        StringBuffer stringBuffer = new StringBuffer();
        //json字符串的第一个位置应该是 {
        stringBuffer.append("{ \"entity\":\"{\"");
        //去除Map中的所有Key值，放入Set集合中
        Set<String> paramKey = paramMap.keySet();
        //遍历出每一个key值，然后取出Map中的对应value做非空判断，若非空就进行拼接到stringBuffer中
        for (String param : paramKey) {
            if (paramMap.get(param) != null) {
                stringBuffer.append("\"" + param + "\":\"" + paramMap.get(param).toString().trim() + "\",");
            }
        }
        //若stringBuffer的长度大于2，则表示Map中有非空value并拼接到stringBuffer，那么就要去掉stringBuffer最后位置的逗号，然后再拼接上}即可；
        //若stringBuffer的长度小于2，则表示Map中没有非空value拼接到stringBuffer,那么只需给stringBuffer再拼接上}即可
        if (stringBuffer.length() > 2) {
            String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
            json = substring + "}";
        } else {
            json = stringBuffer.toString() + "\"pager\",\" {\\n\" +\n" +
                    "                \"    \\\"current\\\": 1,\\n\" +\n" +
                    "                \"    \\\"size\\\": 10,\\n\" +\n" +
                    "                \"    \\\"sortProps\\\": [\\n\" +\n" +
                    "                \"      {\\n\" +\n" +
                    "                \"        \\\"key\\\": \\\"\\\",\\n\" +\n" +
                    "                \"        \\\"value\\\": true\\n\" +\n" +
                    "                \"      }\\n\" +\n" +
                    "                \"    ]\\n\" +\n" +
                    "                \"  }\"}";
        }
        return json;
    }
    public String fanhuiz(String string){
        if(string.equals("字符串")){
            return "String";
        }else if (string.equals("整数")){
            return "int";
        }else if(string.equals("时间")){
            return "date";
        }else if(string.equals("小数")){
          return "float";
        }else if(string.equals("普通文本")){
            return "String";
        } else{
            return string;
        }

    }


    //这里是把数据写死了，只要创建表自动会添加所有数据
    public List<Map<String, Object>> getalle(){
        List<Map<String, Object>> listUsers = new ArrayList<>();
        Map<String, Object> maptable1 = new HashMap<>();maptable1.put("tableChinaname", "主键id");maptable1.put("tableEnglishName", "ID");maptable1.put("fieldType","自定义");maptable1.put("libraryType", "整数");maptable1.put("libraryLength", "20"); listUsers.add(maptable1);
        Map<String, Object> maptable2 = new HashMap<>();maptable2.put("tableChinaname", "创建人名称");maptable2.put("tableEnglishName", "CRUSER");maptable2.put("fieldType","普通文本");maptable2.put("libraryType", "字符串");maptable2.put("libraryLength", "255");listUsers.add(maptable2);
        Map<String, Object> maptable3 = new HashMap<>();maptable3.put("tableChinaname", "创建人id");maptable3.put("tableEnglishName", "CRNUMBER");maptable3.put("fieldType","自定义");maptable3.put("libraryType", "整数");maptable3.put("libraryLength", "20");listUsers.add(maptable3);
        Map<String, Object> maptable4 = new HashMap<>();maptable4.put("tableChinaname", "创建时间");maptable4.put("tableEnglishName", "CRTIME");maptable4.put("fieldType","日期");maptable4.put("libraryType", "时间");maptable4.put("libraryLength", "0");listUsers.add(maptable4);
        Map<String, Object> maptable5 = new HashMap<>();maptable5.put("tableChinaname", "最后修改人名称");maptable5.put("tableEnglishName", "MODIFY_USER");maptable5.put("fieldType","普通文本");maptable5.put("libraryType", "字符串");maptable5.put("libraryLength", "255");listUsers.add(maptable5);
        Map<String, Object> maptable6 = new HashMap<>();maptable6.put("tableChinaname", "最后修改人id");maptable6.put("tableEnglishName", "MODIFY_BY");maptable6.put("fieldType","自定义");maptable6.put("libraryType", "整数");maptable6.put("libraryLength", "20");listUsers.add(maptable6);
        Map<String, Object> maptable7 = new HashMap<>();maptable7.put("tableChinaname", "最后修改时间");maptable7.put("tableEnglishName", "MODIFY_TIME");maptable7.put("fieldType","日期");maptable7.put("libraryType", "时间");maptable7.put("libraryLength", "0");listUsers.add(maptable7);
        Map<String, Object> maptable8 = new HashMap<>();maptable8.put("tableChinaname", "文档id");maptable8.put("tableEnglishName", "DOCCHANNELID");maptable8.put("fieldType","自定义");maptable8.put("libraryType", "整数");maptable8.put("libraryLength", "20");listUsers.add(maptable8);
        Map<String, Object> maptable9 = new HashMap<>();maptable9.put("tableChinaname", "删除状态");maptable9.put("tableEnglishName", "DOCSTATUS");maptable9.put("fieldType","自定义");maptable9.put("libraryType", "整数");maptable9.put("libraryLength", "2");listUsers.add(maptable9);
        Map<String, Object> maptable10 = new HashMap<>();maptable10.put("tableChinaname", "站点id");maptable10.put("tableEnglishName", "SITEID");maptable10.put("fieldType","自定义");maptable10.put("libraryType", "整数");maptable10.put("libraryLength", "20");listUsers.add(maptable10);
        Map<String, Object> maptable11 = new HashMap<>();maptable11.put("tableChinaname", "发布时间");maptable11.put("tableEnglishName", "DOCPUBTIME");maptable11.put("fieldType","日期");maptable11.put("libraryType", "时间");maptable11.put("libraryLength", "0");listUsers.add(maptable11);
        Map<String, Object> maptable12 = new HashMap<>();maptable12.put("tableChinaname", "发布状态");maptable12.put("tableEnglishName", "STATUS");maptable12.put("fieldType","自定义");maptable12.put("libraryType", "整数");maptable12.put("libraryLength", "6");listUsers.add(maptable12);
        Map<String, Object> maptable13 = new HashMap<>();maptable13.put("tableChinaname", "机构id");maptable13.put("tableEnglishName", "COMPANYID");maptable13.put("fieldType","自定义");maptable13.put("libraryType", "整数");maptable13.put("libraryLength", "20");listUsers.add(maptable13);
        Map<String, Object> maptable14 = new HashMap<>();maptable14.put("tableChinaname", "站点id");maptable14.put("tableEnglishName", "WEBSITEID");maptable14.put("fieldType","自定义");maptable14.put("libraryType", "整数");maptable14.put("libraryLength", "20");listUsers.add(maptable14);
        Map<String, Object> maptable15 = new HashMap<>();maptable15.put("tableChinaname", "栏目id");maptable15.put("tableEnglishName", "COLUMNID");maptable15.put("fieldType","自定义");maptable15.put("libraryType", "整数");maptable15.put("libraryLength", "20");listUsers.add(maptable15);
        Map<String, Object> maptable16 = new HashMap<>();maptable16.put("tableChinaname", "排序");maptable16.put("tableEnglishName", "SEQENCING");maptable16.put("fieldType","自定义");maptable16.put("libraryType", "整数");maptable16.put("libraryLength", "10");listUsers.add(maptable16);
        Map<String, Object> maptable17 = new HashMap<>();maptable17.put("tableChinaname", "工作流id");maptable17.put("tableEnglishName", "FLOW_ID");maptable17.put("fieldType","普通文本");maptable17.put("libraryType", "字符串");maptable17.put("libraryLength", "255");listUsers.add(maptable17);
        Map<String, Object> maptable18 = new HashMap<>();maptable18.put("tableChinaname", "工作流用户");maptable18.put("tableEnglishName", "FLOW_USER");maptable18.put("fieldType","普通文本");maptable18.put("libraryType", "字符串");maptable18.put("libraryLength", "255");listUsers.add(maptable18);
        Map<String, Object> maptable19 = new HashMap<>();maptable19.put("tableChinaname", "引用信息");maptable19.put("tableEnglishName","QUOTAINFO");maptable19.put("fieldType","多行文本");maptable19.put("libraryType", "字符串");maptable19.put("libraryLength", "255");listUsers.add(maptable19);
        Map<String, Object> maptable20 = new HashMap<>();maptable20.put("tableChinaname", "复制文档id");maptable20.put("tableEnglishName", "COPYFROMID");maptable20.put("fieldType","自定义");maptable20.put("libraryType", "整数");maptable20.put("libraryLength", "20");listUsers.add(maptable20);
        //以下是暂时无用的数据
        Map<String, Object> maptable21 = new HashMap<>();maptable21.put("tableChinaname", "操作人");maptable21.put("tableEnglishName","OPERUSER");maptable21.put("fieldType","多行文本");maptable21.put("libraryType", "字符串");maptable21.put("libraryLength", "255");listUsers.add(maptable21);
        Map<String, Object> maptable22 = new HashMap<>();maptable22.put("tableChinaname", "操作时间");maptable22.put("tableEnglishName", "OPERTIME");maptable22.put("fieldType","日期");maptable22.put("libraryType", "时间");maptable22.put("libraryLength", "0");listUsers.add(maptable22);
        Map<String, Object> maptable23 = new HashMap<>();maptable23.put("tableChinaname","文档标题");maptable23.put("tableEnglishName","DOCTITLE");maptable23.put("fieldType","多行文本");maptable23.put("libraryType", "字符串");maptable23.put("libraryLength", "255");listUsers.add(maptable23);
        Map<String, Object> maptable24 = new HashMap<>();maptable24.put("tableChinaname", "文档时间");maptable24.put("tableEnglishName", "DOCRELTIME");maptable24.put("fieldType","日期");maptable24.put("libraryType", "时间");maptable24.put("libraryLength", "0");listUsers.add(maptable24);
        Map<String, Object> maptable25 = new HashMap<>();maptable25.put("tableChinaname","文档发布url");maptable25.put("tableEnglishName","DOCPUBURL");maptable25.put("fieldType","多行文本");maptable25.put("libraryType", "字符串");maptable25.put("libraryLength", "255");listUsers.add(maptable25);
        Map<String, Object> maptable26 = new HashMap<>();maptable26.put("tableChinaname","文档链接");maptable26.put("tableEnglishName","LINKURL");maptable26.put("fieldType","多行文本");maptable26.put("libraryType", "字符串");maptable26.put("libraryLength", "255");listUsers.add(maptable26);
        Map<String, Object> maptable27 = new HashMap<>();maptable27.put("tableChinaname", "分类法id");maptable27.put("tableEnglishName", "CLASSINFOID");maptable27.put("fieldType","自定义");maptable27.put("libraryType", "整数");maptable27.put("libraryLength", "20");listUsers.add(maptable27);
        Map<String, Object> maptable28 = new HashMap<>();maptable28.put("tableChinaname", "SINGLETEMPKATE");maptable28.put("tableEnglishName", "SINGLETEMPKATE");maptable28.put("fieldType","自定义");maptable28.put("libraryType", "整数");maptable28.put("libraryLength", "20");listUsers.add(maptable28);
        Map<String, Object> maptable29 = new HashMap<>();maptable29.put("tableChinaname", "DOCVALID");maptable29.put("tableEnglishName", "DOCVALID");maptable29.put("fieldType","日期");maptable29.put("libraryType", "时间");maptable29.put("libraryLength", "0");listUsers.add(maptable29);
        return listUsers;
    }

    private FieldinfoService getService() {
        return fieldinfoService;
    }
}
