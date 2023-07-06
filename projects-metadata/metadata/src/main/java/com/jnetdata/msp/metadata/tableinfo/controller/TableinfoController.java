package com.jnetdata.msp.metadata.tableinfo.controller;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.log.metadata.service.MetadataLogService;
import com.jnetdata.msp.metadata.Class.model.Class;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import com.jnetdata.msp.metadata.dict.model.Dict;
import com.jnetdata.msp.metadata.dict.service.DictService;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.util.PathUtil;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;
import com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import com.jnetdata.msp.metadata.vo.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.var;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata")
@ApiModel(value = "元数据管理(TableinfoController)", description = "元数据管理")
public class TableinfoController extends BaseController<Long, Tableinfo> {

    @Autowired
    private TableinfoService tableinfoService;
    @Autowired
    private FieldinfoService fieldinfoService;
    @Autowired
    private ContentLogService contentLogService;
    @Autowired
    private ClassService classService;
    @Autowired
    private DictService dictService;
    @Autowired
    private MetadataGroupService metadataGroupService;
    @Autowired
    private MetadataLogService metadataLogService;
    @Autowired
    private ViewTableService viewTableService;
    @Autowired
    private ModuleinfoService moduleinfoService;
    @Autowired
    private MetaDataOperatorService metaDataOperatorService;




    @ApiOperation(value = "根据id查询", notes = "查看元数据")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Tableinfo> getById(@ApiParam("元数据id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }


    @ApiOperation(value = "根据id查询", notes = "查看元数据")
    @GetMapping(value = "/tableInfo/{id}")
    @ResponseBody
    public JsonResult<Map> tableInfo(@ApiParam("元数据id") @PathVariable("id") Long id) {
        Tableinfo tableinfo = getService().getById(id);
        Map<String, Object> map = MapUtil.toMap(tableinfo);
        if(tableinfo.getOwnerid() != null){
            Moduleinfo moduleinfo = moduleinfoService.getById(tableinfo.getOwnerid());
            String url = PathUtil.getUrl(moduleinfo.getEnglishname(), tableinfo.getTablename());
            map.put("url",url);
        }
        map.put("fieldinfos",fieldinfoService.getGroupFields(id));
        return renderSuccess(map);
    }

    /**
     * 修改元数据
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "修改", notes = "修改元数据")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("元数据id") @PathVariable("id") Long id, @RequestBody Tableinfo entity) {
        Tableinfo old = getService().getById(id);
        entity.setOldname(old.getTablename());
        if (!entity.getOldname().equals(entity.getTablename())) {
            getService().updateTablename(entity);
            List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", id));
            list.forEach(e -> {
                e.setTablename(entity.getTablename());
            });
            fieldinfoService.updateBatchById(list);
        }
        JsonResult<Void> result = doUpdateById(getService(), id, entity);
        String type = old.getTabletype().equals("table")?"表":old.getTabletype().equals("table")?"视图":"物化视图";
        contentLogService.addLog("修改元数据"+type,"元数据管理","元数据"+type+"设计",result.isSuccess());
        return result;
    }
    /**
     * 获取元数据列表
     * @param vo
     * @author 王树彬
     * @date 2020/6/30
     * @return
     */
    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "元数据列表")
    public JsonResult<Pager<Tableinfo>> getList(@RequestBody BaseVo<Tableinfo> vo) {
        return renderSuccess(getService().pageByPermission(vo));
    }

    /**
     * 获取视图列表
     * @param vo
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @PostMapping(value = "/view/list")
    @ResponseBody
    @ApiOperation(value = "视图列表")
    public JsonResult<Pager<Tableinfo>> viewlist(@RequestBody BaseVo<Tableinfo> vo){
        if(vo.getEntity() == null) vo.setEntity(new Tableinfo());
        vo.getEntity().setTabletype("view");
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @PostMapping(value = "/module/es")
    @ResponseBody
    @ApiOperation(value = "es列表")
    public JsonResult<Pager<Tableinfo>> eslist(@RequestBody BaseVo<Tableinfo> vo){
        if(vo.getEntity() == null) vo.setEntity(new Tableinfo());
        vo.getEntity().setTabletype("es");
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    /**
     * 物化视图列表
     * @param vo
     * @author 王树彬
     * @date 2020/6/30
     * @return
     */
    @PostMapping(value = "/tview/list")
    @ResponseBody
    @ApiOperation(value = "物化视图列表")
    public JsonResult<Pager<Tableinfo>> tviewlist(@RequestBody BaseVo<Tableinfo> vo){
        if(vo.getEntity() == null) vo.setEntity(new Tableinfo());
        vo.getEntity().setTabletype("tview");
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    /**
     * 获取所有视图列表
     * @author 王树彬
     * @date 2020/6/30
     * @return
     */
    @PostMapping(value = "/view/all")
    @ResponseBody
    @ApiOperation(value = "获取所有视图列表")
    public JsonResult<List<Tableinfo>> getViewTable(){
        return renderSuccess(getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tabletype","view")));
    }

    @PostMapping(value = "/tableInfo/list")
    @ResponseBody
    @ApiOperation(value = "表或视图列表")
    public JsonResult<Pager<Tableinfo>> list(@RequestBody BaseVo<Tableinfo> vo){
        return doList(getService(),vo.getPager(),vo.getEntity());
    }

    @PostMapping(value = "/es/all")
    @ResponseBody
    @ApiOperation(value = "获取es列表")
    public JsonResult<List<Tableinfo>> getEsTable(){
        return renderSuccess(getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tabletype","es")));
    }

    /**
     * 获取所有物化视图列表
     * @author 王树彬
     * @date 2020/6/30
     * @return
     */
    @PostMapping(value = "/tview/all")
    @ResponseBody
    @ApiOperation(value = "获取所有物化视图列表")
    public JsonResult<List<Tableinfo>> getTViewTable(){
        return renderSuccess(getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tabletype","tview")));
    }

    /**
     * 查看视图sql
     * @param id
     * @author 王树彬
     * @date 2020//6/24
     * @return
     */
    @ApiOperation(value = "查看视图sql", notes="查看视图")
    @PostMapping(value = "/view/viewSql")
    @ResponseBody
    public JsonResult<Tableinfo> getSql(@RequestBody Long id){
        Tableinfo obj = getService().getById(id);
        if (StringUtils.isEmpty(obj.getViewsql())) {
            obj.setViewsql(viewTableService.viewSql(obj.getTablename()));
            tableinfoService.updateById(obj);
        }
        return renderSuccess(obj);
    }

    /**
     * 得到选择字段视图
     * @param id
     * @param ids
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @ApiOperation(value = "查看选择字段视图" , notes = "得到选择字段视图")
    @PostMapping(value = "/selectViewSql")
    @ResponseBody
    public JsonResult<Tableinfo> getSelectViewSql(Long id ,  String[] ids){
        Tableinfo obj = getService().getById(id);
//        String sql = viewTableService.viewSql(obj.getTablename());
//        String sql2 = sql.trim();
//        sql2 = sql2.toUpperCase();
//        sql2 = sql2.substring(sql2.indexOf(" FROM ") , sql2.length());
//        String a = String.join(",",ids);
//        List<Fieldinfo> fieldInfos = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).in("id",String.join(",",ids)));
//        String sql1 = "SELECT ";
//        String[] s = new String[]{};
//        s = fieldInfos.stream().map(Fieldinfo::getFieldname).collect(Collectors.toList()).toArray(s);
//        for (int i = 0; i < s.length; i++) {
//            s[i] = s[i]+" AS "+s[i];
//        }
//        sql1 += String.join("," , s);
//        sql = sql1 + sql2;
//        obj.setSql(sql);
        return renderSuccess(obj);
    }

    @ApiOperation(value = "修改sql语句", notes="修改视图的sql语句")
    @PostMapping(value = "/getViewSql")
    @ResponseBody
    public JsonResult<Void> getViewSql(Long id ,String viewname ,String sql){
        val user = SessionUser.getCurrentUser();
//        viewTableService.dropView(viewname);  //为了 sql server适配  添的
        //对sql语句进行修改
        boolean b = viewTableService.performSql(sql);
        List<TableField> viewFields = tableinfoService.getViewFields(viewname);
        Tableinfo tableinfo = getService().getById(id);
        tableinfo.setTablename(viewname);
        tableinfo.setViewsql(viewTableService.viewSql(viewname));

        List<Fieldinfo> olds = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", id));
        Map<String, Fieldinfo> oldMap = olds.stream().collect(Collectors.toMap(Fieldinfo::getFieldname, m -> m));

        List<Fieldinfo> fields = new ArrayList<>();
        for (TableField viewField : viewFields) {
            if(oldMap.containsKey(viewField.getField())) {
                fields.add(oldMap.get(viewField.getField()));
                continue;
            }
            Fieldinfo fieldinfo = new Fieldinfo();
            fieldinfo.setTablename(viewname);
            fieldinfo.setTableid(id);
            fieldinfo.setDbtype(viewField.getDbType());
            fieldinfo.setFieldname(viewField.getField());
            fieldinfo.setShowList(1);
            fieldinfo.setShowSearch(1);
            fieldinfo.setShowDetail(1);
            fieldinfo.setFieldtype(viewField.getFieldType());
            fieldinfo.setMatchType(8);
            fields.add(fieldinfo);
        }

        tableinfoService.updateById(tableinfo);
        fieldinfoService.deleteByTableinfoId(id);
        fieldinfoService.insertBatch(fields);//插入文件路径
        contentLogService.addLog("修改sql视图","元数据管理","修改sql视图",b);
       /* MetaDataOperator metaDataOperator = new MetaDataOperator();
        metaDataOperator.setUserid(user.getId());
        metaDataOperator.setUsername(user.getName());
        metaDataOperator.setHandletype("修改SQL");
        metaDataOperator.setTitle(tableinfo.getAnothername());
        metaDataOperatorService.insert(metaDataOperator);*/
        metaDataOperatorService.AddLog("修改SQL",tableinfo.getAnothername());
        return renderSuccess();
    }

    /**
     * 创建视图
     * @param id
     * @param viewname
     * @param ids
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @ApiOperation("创建视图")
    @PostMapping("/create")
    @ResponseBody
    public JsonResult create(@ModelAttribute("id") Long id , @ModelAttribute("viewname") String viewname , @RequestBody Long[] ids){
        List<Fieldinfo> fieldInfos = new ArrayList<>();
        try {
            Tableinfo tableinfo = getService().getById(id);
            tableinfo.setViewname(viewname);
            tableinfo.setCreateTime(new Date());
            List<String> stringList = viewTableService.existsView(viewname);
            if(stringList.size()>0){
                return renderError("该视图已存在");
            }
            fieldInfos = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).in("id",Arrays.asList(ids)));

            getService().createView(tableinfo,fieldInfos);//视图创建成功
            val user = SessionUser.getCurrentUser();
            /*MetaDataOperator metaDataOperator = new MetaDataOperator();
            metaDataOperator.setUserid(user.getId());
            metaDataOperator.setUsername(user.getName());
            metaDataOperator.setHandletype("创建视图");
            metaDataOperator.setTitle(tableinfo.getAnothername());
            metaDataOperatorService.insert(metaDataOperator);*/
            metaDataOperatorService.AddLog("创建视图",tableinfo.getAnothername());
            return renderSuccess("视图创建成功");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return renderError("视图创建失败");
    }

    @PostMapping(value = "/count")
    @ResponseBody
    @ApiOperation(value = "元数据变化")
    public JsonResult<List> getCountNews() {
        return renderSuccess(getService().countNews());
    }

    /**
     * 本周元数据新增数
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @GetMapping(value = "/addThisWeek")
    @ApiOperation(value = "本周元数据新增数", notes = "本周新增")
    @ResponseBody
    public JsonResult<Map> addThisWeek() {
        return renderSuccess(getService().addThisWeek());
    }

    /**
     * 本月元数据增加
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @GetMapping(value = "/addThisMonth")
    @ApiOperation(value = "本月元数据新增数", notes = "本月新增")
    @ResponseBody
    public JsonResult<Map> addThisMonth() {
        return renderSuccess(getService().addThisMonth());
    }

    /**
     * 本年元数据增加数
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @GetMapping(value = "/addThisYear")
    @ApiOperation(value = "本年元数据新增数", notes = "本年新增")
    @ResponseBody
    public JsonResult<Map> addThisYear() {
        return renderSuccess(getService().addThisYear());
    }

    /**
     * 获取选择模块的元数据表
     * @param vo
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @PostMapping(value = "/listOwn")
    @ResponseBody
    @ApiOperation(value = "获得选择模块的元数据表")
    public JsonResult<Pager<Tableinfo>> getListOwn(@RequestBody BaseVo<Tableinfo> vo) {
        JsonResult<Pager<Tableinfo>> tlist = doList(getService(), vo.getPager(), vo.getEntity());
        List<Tableinfo> table = tlist.getObj().getRecords();
        table.forEach(res -> {
            if (res.getOwnerid() != vo.getEntity().getOwnerid()) {
                table.remove(res);
            }
        });
        return tlist;
    }

    /**
     * 获得所有源数据表
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @RequestMapping(value = "/all",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "获得所有元数据表")
    public JsonResult<List<Tableinfo>> all() {
        return renderSuccess(getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tabletype","table")));
    }

    /**
     * 获得未选择模块的源数据表
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @PostMapping(value = "/listNoChoose")
    @ResponseBody
    @ApiOperation(value = "获得未选择模块的元数据表")
    public JsonResult<List<Tableinfo>> getListNoChoose() {
        List<Tableinfo> list = getService().list(new PropertyWrapper<>(Tableinfo.class).eq("ownerid", 0));
        return renderSuccess(list);
    }

    /**
     * 添加元数据
     * @param entity
     * @author 王树彬
     * @date 2020/6/24
     * @return
     */
    @ApiOperation(value = "添加元数据")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Tableinfo entity) {
        List<Tableinfo> list = tableinfoService.findByTableName(entity.getTablename());
        JsonResult<EntityId<Long>> result = null;
        val user = SessionUser.getCurrentUser();
        if(list.size()>0){
            result =  renderError("元数据已存在");
        }else{
            List<String> tablenames = tableinfoService.existsTable(entity.getTablename());
            if (tablenames.size() > 0) {
                result = renderError("表已存在");
            } else {
                entity.setCrUser(user.getName());
                entity.setCreateBy(user.getId());
                entity.setCreateTime(new Date());
                tableinfoService.createTable(entity);
                result = doAdd(getService(), entity);
            }
        }
        contentLogService.addLog("新建元数据表","元数据管理","元数据表设计",result.isSuccess());
        return result;
    }

    /**
     * 删除指定id的元数据
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除指定id元数据")
    @PostMapping(value = "/del/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("元数据id") @PathVariable("id") Long id) {
        return renderSuccess();
    }

    /**
     * 根据多个id删除元数据
     * @param ids
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "批量删除", notes = "根据多个id删除元数据")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestBody Long[] ids, HttpServletRequest request) {
        for (Long id : ids) {
            deleteAll(id);
            contentLogService.addLog("删除元数据表","元数据管理","元数据表设计",true);
        }
        return renderSuccess();
    }

    /**
     * 删除全部
     * @param id
     * @author hongshou
     * @date 2020/5/26
     */
    private void deleteAll(Long id) {
        Tableinfo table = getService().getById(id);
        JsonResult<Void> result = null;
        if (table != null) {
            String tablename = table.getTablename();
            String type = table.getTabletype();
            //删除表
            List<String> tablenames = tableinfoService.existsTable(tablename);
            if (tablenames.size() > 0) {
                if(type.equals("table")){
                    tableinfoService.dropTable(tablename);
                }else if(type.equals("view")){
                    viewTableService.dropView(tablename);
                }

            }
            //删除元数据
            boolean b = getService().deleteById(id);
            //删除字段
            fieldinfoService.deleteByTableinfoId(id);
        }
    }

    /**
     * 查询相应tablename对应信息
     * @param tablename
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "查询相应tablename对应信息", notes = "查询相应tablename对应信息")
    @PostMapping(value = "/searchList")
    @ResponseBody
    public JsonResult<List<Tableinfo>> getSearchList(@RequestBody String tablename) {
        List<Tableinfo> list = getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tablename", tablename.toUpperCase()));
        return renderSuccess(list);
    }

    /**
     * 异常捕获
     * @param str
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }

    public boolean isNumeric2(char[] str) {
        //2  看它是否能转化为一个数
        try {
            double db=Double.parseDouble(String.valueOf(str));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过Excel表导入表数据
     * @param id
     * @param columnid
     * @param file
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "导入表数据", notes = "导入表数据")
    @PostMapping(value = "/import")
    @ResponseBody
    public JsonResult<List<List<Object>>> importData(@ApiParam("元数据id") @ModelAttribute("id") String id, @ApiParam("栏目id") @ModelAttribute("columnid") String columnid, MultipartFile file, HttpServletRequest request){

        //文件后缀不是.jpg
        if(!file.getOriginalFilename().contains(".xls")&&!file.getOriginalFilename().contains(".xlsx")){
            return renderError("请上传EXCEL文件");
        }

        InputStream in = null;
        int index = 0;
        try {
            in = file.getInputStream();
            List<List<String>> listob = ExcelUtil.importExcelStringInfo(in, file.getOriginalFilename());
            List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", id));

            List<Long> classIds = list.stream().filter(f -> f.getFieldtype() == 13).map(Fieldinfo::getClassid).collect(Collectors.toList());
            Map<Long,Map<String,Long>> classIdMap = new HashMap<>();
            if(classIds!=null && classIds.size()>0){
                List<Class> classes = classService.list(new PropertyWrapper<>(Class.class).in("parentId", classIds));
                Map<Long, List<Class>> classGroup = classes.stream().collect(Collectors.groupingBy(Class::getParentId));
                for(Map.Entry<Long, List<Class>> entry : classGroup.entrySet()){
                    List<Class> item = entry.getValue();
                    classIdMap.put(entry.getKey(),item.stream().collect(Collectors.toMap( Class::getClassName,Class::getId,(v1, v2) -> v1)));
                }
            }

            String fieldStr = "";
            List<String> fieldNames = new ArrayList<>();
            Map<String, Fieldinfo> another2FieldMap = list.stream().collect(Collectors.toMap(Fieldinfo::getAnothername, v -> v, (v1, v2) -> v1));
            List<String> titles = listob.get(0);
            List<String> errTitles = new ArrayList<>();
            for (Object title : titles) {
                if(another2FieldMap.get(title) == null){
                    errTitles.add(String.valueOf(title));
                }else{
                    fieldNames.add(another2FieldMap.get(title).getFieldname());
                }
            }
            if(errTitles.size()>0){
                return renderError("列“"+String.join("”“", errTitles)+"”不存在!");
            }
            fieldStr = String.join(",", fieldNames);
            fieldStr += ",COLUMNID";
            fieldStr += ",CRTIME";
            for (int i=1;i<listob.size();i++) {
                List<String> o = listob.get(i);
                for (int j = 0; j < titles.size(); j++) {
                    int dbType = another2FieldMap.get(titles.get(j)).getDbtype();
                    if (dbType == 1) {
                        if(!isNumeric(o.get(j))){
                            o.set(j, "0");
                        }
                    }else if (dbType == 3) {
                        if(!isNumeric2(o.get(j).toCharArray())){
                            o.set(j, "0");
                        }
                    }else{
                        if (another2FieldMap.get(titles.get(j)).getFieldtype().equals(13)) {
                            if(StringUtils.isNotEmpty(String.valueOf(o.get(j)))){
                                String[] arr = String.valueOf(o.get(j)).split("；");
                                String cnameid = "";
                                List<String> cname = new ArrayList<>();
                                for (String s1 : arr) {
                                    Long classid = classIdMap.get(another2FieldMap.get(titles.get(j)).getClassid()).get(s1);
                                    if (classid != null) {
                                        cname.add(String.valueOf(classid));
                                    }else{
                                        return renderError("列“"+titles.get(j)+"”中“"+s1+"”不存在!");
                                    }
                                }
                                String[] c1 = new String[]{};
                                c1 = cname.stream().collect(Collectors.toList()).toArray(c1);
                                cnameid = String.join(",", c1);
                                if (cname.size() > 0) {
                                    o.set(j, cnameid);
                                }
                            }
                        }
                    }
                    o.set(j,StringUtils.isEmpty(o.get(j)) ? null : "'"+o.get(j).replace("'","\\'")+"'");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                o.add("'"+columnid+"'");
                o.add("'"+sdf.format(new Date())+"'");
            }

            String tableName = list.get(0).getTablename();

            List<Map<String,String>> params = new ArrayList<>();

            for(int i=1; i<listob.size();i++ ){
                Map<String,String> map = new HashMap<>();
                String sql = "insert into "+tableName+" ("+fieldStr+") values ("+String.join(",",listob.get(i))+")";
                map.put("sql",sql);
                params.add(map);
            }
            try {
//                getService().insertInfo(list.get(0).getTablename(), fieldStr, listob.get(i));
                index = getService().insertInfoBatch(params);
            } catch (Exception e) {
                e.printStackTrace();
                return renderError("您选择的文本内容第" + (Integer.parseInt(e.getMessage())+1) + "行有问题，请重新选择或联系管理员！\n错误信息:"+e.getCause().getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return renderSuccess("导入"+index+"条数据！");
    }

    @ApiOperation(value = "导入表数据", notes = "导入表数据")
    @PostMapping(value = "/import2")
    @ResponseBody
    public JsonResult<List<List<Object>>> importData2(@ApiParam("元数据id") @ModelAttribute("id") String id, @ApiParam("元数据id") @ModelAttribute("columnid") String columnid, MultipartFile file, HttpServletRequest request) throws Exception {
        //MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
        //MultipartFile file = multipart.getFile("upfile");
        InputStream in = file.getInputStream();
        List<List<Object>> listob = ExcelUtil.importExcelInfo(in, file.getOriginalFilename());
        List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class)
                .eq("tableid", id));
        String fieldnames = "";
        String[] s = new String[]{};
        s = list.stream().map(Fieldinfo::getFieldname).collect(Collectors.toList()).toArray(s);
        fieldnames = String.join(",", s);
        fieldnames += ",COLUMNID";
        fieldnames += ",CRTIME";
        int index = 0;
        for (int i = 0; i < listob.size(); i++) {
            //System.out.println(listob.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getDbtype().equals(1)) {
                    //if (listob.get(i).get(j) == "" || listob.get(i).get(j) == " " || listob.get(i).get(j) == "_") {
                    if (listob.get(i).get(j).equals("-")) {
                        listob.get(i).set(j, 0);
                    }
                    if (listob.get(i).get(j).equals("") || listob.get(i).get(j).equals(" ")) {
                        listob.get(i).set(j, "");
                    }
                }
                if (list.get(j).getDbtype().equals(3)) {
                    //if (listob.get(i).get(j) == "" || listob.get(i).get(j) == " " || listob.get(i).get(j) == "_") {
                    if (listob.get(i).get(j).equals("-")) {
                        listob.get(i).set(j, 0);
                    }
                    if (listob.get(i).get(j).equals("") || listob.get(i).get(j).equals(" ")) {
                        listob.get(i).set(j, "");
                    }
                }
                if (list.get(j).getFieldtype().equals(13)) {
                    String[] arr = String.valueOf(listob.get(i).get(j)).split("；");
                    String cnameid = "";
                    List<String> cname = new ArrayList<>();
                    for (String s1 : arr) {
                        List<Class> c = classService.list(new PropertyWrapper<>(Class.class)
                                .eq("className", s1)
                                .eq("parentId", list.get(j).getClassid()));
                        if (c.size() > 0) {
                            cname.add(String.valueOf(c.get(0).getId()));
                        }
                    }
                    String[] c1 = new String[]{};
                    c1 = cname.stream().collect(Collectors.toList()).toArray(c1);
                    cnameid = String.join(",", c1);
                    if (cname.size() > 0) {
                        listob.get(i).set(j, cnameid);
                    }
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            listob.get(i).add(columnid);
            listob.get(i).add(sdf.format(new Date()));
            /*Long id2 = IdWorker.getId();
            listob.get(i).add(id2);*/
            index = i;
            try {
//                getService().insertInfo(list.get(0).getTablename(), fieldnames, listob.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return renderError("您选择的文本内容第" + (i + 2) + "行有问题，请重新选择或联系管理员！");
            }
        }
        in.close();
        return renderSuccess("导入" + index + "条数据！");
    }

    /**
     * 导出数据格式为Excel表格
     * @param id
     * @param columnId
     * @param request
     * @param response
     * @author hongshou
     * @date 2020/5/26
     * @throws Exception
     */
    @ApiOperation(value = "导出表数据", notes = "导出表数据")
    @GetMapping(value = "/export")
    @ResponseBody
    public void exportData(@ApiParam("元数据表id tableid") @ModelAttribute("id") String id,@ApiParam("元栏目id") @ModelAttribute("columnId") Long columnId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();

        XSSFWorkbook xssfWorkbook = null;
        //设置标题栏
        List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class).eq("tableid", id).eq("showList",1));
        String fieldnames = "";
        String[] s = new String[]{};
        s = list.stream().map(Fieldinfo::getFieldname).collect(Collectors.toList()).toArray(s);
        fieldnames = String.join(",", s);
        List<Map<String, Object>> datas = getService().selectMess(fieldnames, list.get(0).getTablename(),columnId);
        List<Long> classIds = list.stream().filter(f -> f.getFieldtype() == 13).map(Fieldinfo::getClassid).collect(Collectors.toList());
        List<String> classFields = list.stream().filter(f -> f.getFieldtype() == 13).map(Fieldinfo::getFieldname).collect(Collectors.toList());
        if(classIds!=null && classIds.size()>0){
            List<Class> classes = classService.list(new PropertyWrapper<>(Class.class).in("parentId", classIds));
            Map<Long, String> classNames = classes.stream().collect(Collectors.toMap(Class::getId, Class::getClassName));
            for (Map<String, Object> item : datas) {
                for (String iitem : classFields) {
                    String cids = String.valueOf(item.get(iitem));
                    if(StringUtils.isNotEmpty(cids)){
                        String[] cidarr = cids.split(",");
                        for (int i=0;i<cidarr.length;i++) {
                            try {
                                cidarr[i] = classNames.get(Long.parseLong(cidarr[i]));
                            }catch (Exception e){
                                cidarr[i] = "";
                            }
                        }
                        item.put(iitem,String.join(",",cidarr));
                    }
                }
            }
        }
        for (Fieldinfo fieldinfo : list) {
            excel.add(new ExcelBean(fieldinfo.getAnothername(), fieldinfo.getFieldname(), 0));
        }
        map.put(0, excel);
        String sheetName = doGetById(getService(), id).getObj().getAnothername();  //sheet名
        String excelName = sheetName + ".xlsx";
        XSSFWorkbook workbook = ExcelUtil.createExcelFile(datas, map, sheetName);
        response.reset();
        response.setHeader("content-disposition", "attachement;fileName=" + (new String(excelName.getBytes(), "ISO-8859-1")));
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 导入元数据表
     * @param id
     * @param file
     * @param request
     * @author hongshou
     * @date 2020/5/26
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导入元数据表", notes = "导入元数据表")
    @PostMapping(value = "/importTable")
    @ResponseBody
    public JsonResult<List<List<Object>>> importTable(@ApiParam("元数据id") @ModelAttribute("id") String id, MultipartFile file, HttpServletRequest request) throws Exception {

        System.out.println(file + "======================================");
        System.out.println(file.getSize() + "======================================");

        int index = 0;
        InputStream in = file.getInputStream();
        List<List<Object>> listob = ExcelUtil.importExcelInfo(in, file.getOriginalFilename());
        JsonResult<Tableinfo> result = doGetById(getService(), id);
        Map<String, Integer> db_type = dictService.list("db_type").stream().collect(Collectors.toMap(Dict::getName, Dict::getNo));
        Map<String, Integer> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getName, Dict::getNo));
        List<MetadataGroup> list = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).ne("parentid", 0));
        for (int i = 0; i < list.size(); i++) {
            String g = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).eq("id", list.get(i).getParentid())).get(0).getName();
            g = g + "." + list.get(i).getName();
            list.get(i).setName(g);
        }
        Map<String, Long> group_name = list.stream().collect(Collectors.toMap(MetadataGroup::getName, MetadataGroup::getId));
        for (int i = 0; i < listob.size(); i++) {
            index = i + 1;
            if (field_type.get(listob.get(i).get(2)) != null) {
                listob.get(i).set(2, field_type.get(listob.get(i).get(2)));
                if (listob.get(i).get(2).equals(13)) {
                    if (listob.get(i).get(4) != null) {
                        listob.get(i).set(3, 2);
                        List<Class> cl = classService.list(new PropertyWrapper<>(Class.class).eq("className", listob.get(i).get(4)));
                        if (cl.size() == 1) {
                            listob.get(i).set(4, cl.get(0).getId());
                            listob.get(i).add(cl.get(0).getParentId());
                        } else {
                            renderError("分类法存在异常（可能存在相同名称分类法）！");
                        }
                    } else {
                        renderError("导入的数据存在异常！");
                    }
                } else if (listob.get(i).get(2).equals(4)) {
                    listob.get(i).set(3, 4);
                    listob.get(i).add("");
                } else if (listob.get(i).get(2).equals(5)) {
                    listob.get(i).set(3, db_type.get(listob.get(i).get(3)));
                    listob.get(i).add("");
                } else if (listob.get(i).get(2).equals(18)) {
                    listob.get(i).set(3, 5);
                    listob.get(i).add("");
                } else {
                    listob.get(i).set(3, 2);
                    listob.get(i).add("");
                }
            } else {
                renderError("导入的数据存在异常！");
            }
            if (listob.get(i).get(7) != null) {
                if (group_name.get(listob.get(i).get(7)) != null) {
                    listob.get(i).set(7, group_name.get(listob.get(i).get(7)));
                } else {
                    renderError("分组不存在！");
                }
            } else {
                renderError("导入的数据(分组)存在异常！");
            }
            Fieldinfo entity = new Fieldinfo();
            entity.setTableid(result.getObj().getId());
            entity.setTablename(result.getObj().getTablename());
            entity.setAnothername(listob.get(i).get(0).toString());
            entity.setFieldname(listob.get(i).get(1).toString());
            entity.setFieldtype(parseInteger(listob.get(i).get(2).toString()));
            entity.setDbtype(parseInteger(listob.get(i).get(3).toString()));
            entity.setClassid(parseLong(listob.get(i).get(4).toString()));
            entity.setRadorchk(parseInteger(listob.get(i).get(5).toString()));
            entity.setDblength(listob.get(i).get(6).toString());
            entity.setGroupid(parseLong(listob.get(i).get(7).toString()));
            entity.setHiddenfield(parseInteger(listob.get(i).get(8).toString()));
            entity.setShowDetail(parseInteger(listob.get(i).get(9).toString()));
            entity.setShowList(parseInteger(listob.get(i).get(10).toString()));
            entity.setShowSearch(parseInteger(listob.get(i).get(11).toString()));
            entity.setDefaultvalue(listob.get(i).get(12).toString());
            entity.setNotbenull(parseInteger(listob.get(i).get(13).toString()));
            entity.setClassparentid(parseLong(listob.get(i).get(14).toString()));
            List<Fieldinfo> list2 = fieldinfoService.list(entity);
            if (list2.size() > 0) {
                renderError("字段已存在");
            } else {
                List<String> fieldList = fieldinfoService.getTableField(entity.getTablename());
                System.out.println(entity.getTablename());
                if (fieldList != null && fieldList.contains(entity.getFieldname())) {
                    renderError("字段已存在");
                } else {
                    Dict dict = dictService.get("db_type", entity.getDbtype());
                    entity.setDbTypeStr(dict.getCode());
                    fieldinfoService.createField(entity);
                    fieldinfoService.insert(entity);
                }
            }
            /*try {
                getService().insertInfo("dbfieldinfo", fieldnames, listob.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return renderError("您选择的文本内容第"+ (i+2) +"行有问题，请重新选择或联系管理员！");
            }*/
        }

        in.close();
        return renderSuccess("导入" + index + "条数据！");
    }

    public Long parseLong(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return Long.parseLong(str);
        } else {
            return null;
        }
    }

    public Integer parseInteger(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return Integer.parseInt(str);
        } else {
            return null;
        }
    }

    @ApiOperation(value = "导出元数据表", notes = "导出元数据表")
    @GetMapping(value = "/exportTable")
    @ResponseBody
    public void exportTable(@ApiParam("元数据id") @ModelAttribute("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
        map.put(0, excelList());
        String fieldnames = "ANOTHERNAME,FIELDNAME,FIELDTYPE,DBTYPE,CLASSID,RADORCHK," +
                "DBLENGTH,DBLENGTH,GROUPID,HIDDENFIELD,SHOW_DETAIL,SHOW_LIST,SHOW_SEARCH,DEFAULTVALUE,NOTBENULL";
        List<Map<String, Object>> datas = getService().selectMess2(fieldnames, id);
        Map<Integer, String> db_type = dictService.list("db_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        Map<Integer, String> field_type = dictService.list("field_type").stream().collect(Collectors.toMap(Dict::getNo, Dict::getName));
        List<MetadataGroup> list = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).ne("parentid", 0));
        for (int i = 0; i < list.size(); i++) {
            String g = metadataGroupService.list(new PropertyWrapper<>(MetadataGroup.class).eq("id", list.get(i).getParentid())).get(0).getName();
            g = g + "." + list.get(i).getName();
            list.get(i).setName(g);
        }
        Map<Long, String> group_name = list.stream().collect(Collectors.toMap(MetadataGroup::getId, MetadataGroup::getName));
        datas.forEach(e -> {
            if (e.get("DBTYPE") != null) {
                e.put("DBTYPE", db_type.get(Integer.parseInt(e.get("DBTYPE").toString())));
            }
            if (e.get("FIELDTYPE") != null) {
                e.put("FIELDTYPE", field_type.get(Integer.parseInt(e.get("FIELDTYPE").toString())));
            }
            if (e.get("GROUPID") != null) {
                e.put("GROUPID", group_name.get(Long.parseLong(e.get("GROUPID").toString())));
            }
            if (e.get("CLASSID") != null) {
                e.put("CLASSID", classService.list(new PropertyWrapper<>(Class.class).eq("id", e.get("CLASSID"))).get(0).getClassName());
            }
        });
      /*  String sheetName = doGetById(getService(), id).getObj().getAnothername();  //sheet名
        String excelName = sheetName + ".xlsx";
        XSSFWorkbook workbook = ExcelUtil.createExcelFile(datas, map, sheetName);
        response.reset();
        response.setHeader("content-disposition", "attachement;fileName=" + (new String(excelName.getBytes(), "ISO-8859-1")));
        workbook.write(response.getOutputStream());
        workbook.close();*/
        String sheetName = doGetById(getService(), id).getObj().getAnothername();
        // 这里注意会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        XSSFWorkbook workbook = ExcelUtil.createExcelFile(datas, map, sheetName);
        workbook.write(response.getOutputStream());
        workbook.close();
        ExcelUtil.createExcelFile(datas, map, sheetName).write(response.getOutputStream());

    }

    public static List<ExcelBean>  excelList(){
        List<ExcelBean> excel = new ArrayList<>();
        excel.add(new ExcelBean("中文名称", "ANOTHERNAME", 0));
        excel.add(new ExcelBean("英文名称", "FIELDNAME", 0));
        excel.add(new ExcelBean("字段类型", "FIELDTYPE", 0));
        excel.add(new ExcelBean("库中类型", "DBTYPE", 0));
        excel.add(new ExcelBean("分类法id", "CLASSID", 0));
        // excel.add(new ExcelBean("选择类型（单选多选）", "RADORCHK", 0));
        excel.add(new ExcelBean("库中长度", "DBLENGTH", 0));
        excel.add(new ExcelBean("选择分组", "GROUPID", 0));
        // excel.add(new ExcelBean("是否启用", "HIDDENFIELD", 0));
        excel.add(new ExcelBean("详情展示", "SHOW_DETAIL", 0));
        excel.add(new ExcelBean("列表展示", "SHOW_LIST", 0));
        excel.add(new ExcelBean("搜索展示", "SHOW_SEARCH", 0));
        excel.add(new ExcelBean("字段默认值", "DEFAULTVALUE", 0));
        excel.add(new ExcelBean("是否为空", "NOTBENULL", 0));
        return excel;
    }

    @ApiOperation(value = "置顶", notes = "置顶")
    @GetMapping(value = "/toTop")
    @ResponseBody
    public JsonResult toTop(Long tableId, Long dataId) {
        Tableinfo tableinfo = getService().getById(tableId);
        getService().toTop(tableinfo.getTablename(), dataId);
        return renderSuccess();
    }

    /**
     * 取消置顶
     * @param tableId
     * @param dataId
     * @author unkonwn
     * @date 2020/9/27
     * @return
     */
    @ApiOperation(value = "取消置顶", notes = "取消置顶")
    @GetMapping(value = "/reSetTop")
    @ResponseBody
    public JsonResult reSetTop(Long tableId, Long dataId) {
        Tableinfo tableinfo = getService().getById(tableId);
        getService().reSetTop(tableinfo.getTablename(), dataId);
        return renderSuccess();
    }

    /**
     * 清空回收站
     * @param tableId
     * @author unkonwn
     * @date 2020/9/27
     * @return
     */
    @ApiOperation(value = "清空回收站", notes = "清空回收站")
    @GetMapping(value = "/clearRecycle")
    @ResponseBody
    public JsonResult clearRecycle(Long tableId) {
        Tableinfo tableinfo = getService().getById(tableId);
        getService().clearRecycle(tableinfo.getTablename());
        return renderSuccess();
    }

    /**
     * 审核
     * @param status
     * @param ids
     * @param tableName
     * @param reason
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @ApiOperation(value = "审核", notes = "")
    @PostMapping(value = "/statusPass")
    @ResponseBody
    public JsonResult statusPass(int status,@RequestBody String[] ids,String tableName,String reason) {
        getService().statusPass(status,ids,tableName,reason);
        return renderSuccess();
    }


    @PostMapping(value = "/getDbTables")
    @ResponseBody
    public JsonResult getDbTables(String name) {
        List<Tableinfo> dbTables = getService().getDbTables(name);
        return renderSuccess(dbTables);
    }

    /**
     * 检查数据库表
     * @param tableinfos
     * @param moduleId
     * @param groupId
     * @param ttype
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @PostMapping(value = "/checkDbTables")
    @ResponseBody
    public JsonResult checkDbTables(@RequestBody List<Tableinfo> tableinfos, Long moduleId, Long groupId, int ttype) {
        for (Tableinfo tableinfo : tableinfos) {
            tableinfo.setOwnertype(0);
            tableinfo.setOwnerid(moduleId);
            tableinfo.setTabletype("table");
            tableinfo.setPk("ID");
        }
        tableinfoService.insertBatch(tableinfos);

        List<String> failTables = new ArrayList<>();

        for (Tableinfo tableinfo : tableinfos) {
            try {
                getService().addDbTableFields(tableinfo,groupId);
            }catch (Exception e){
                failTables.add(tableinfo.getTablename());
            }
        }

        return renderSuccess(failTables);
    }

    /**
     * 表生成代码生成人，生成时间
     * @param tablename
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @PostMapping (value = "/crtGeneTime")
    @ResponseBody
    @ApiOperation(value = "表生成代码生成人，生成时间")
    public JsonResult crtGeneTime(@RequestBody String tablename) {
        var user = SessionUser.getCurrentUser();
        List<Tableinfo> list = getService().list(new PropertyWrapper<>(Tableinfo.class).eq("tablename", tablename));
        Tableinfo tableinfo = list.get(0);
        tableinfo.setGeneratetime(new Date());
        tableinfo.setGenerateuser(user.getName());
        getService().updateById(tableinfo);
        metaDataOperatorService.AddLog("生成java代码",tableinfo.getAnothername());

        return renderSuccess();
    }

    private TableinfoService getService() {
        return tableinfoService;
    }
}
