package com.jnetdata.msp.metadata.viewtableinfo.controller;


import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.fieldinfo.service.FieldinfoService;
import com.jnetdata.msp.metadata.moduleinfo.controller.ModuleViewController;
import com.jnetdata.msp.metadata.moduleinfo.model.ModuleView;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleViewService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewfieldinfo.service.ViewFieldService;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;
import com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService;
import com.jnetdata.msp.metadata.viewtableinfo.model.ViewTable;
import com.jnetdata.msp.metadata.viewtableinfo.service.ViewTableService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/view")
@ApiModel(value = "ViewTableController", description = "视图管理")
public class ViewTableController extends BaseController<Long,ViewTable> {

    @Autowired
    private ViewTableService viewTableService;

    @Autowired
    private TableinfoService tableinfoService;

    @Autowired
    private MetaDataOperatorService metaDataOperatorService;

    @Autowired
    private ViewFieldService viewFieldService;

    @Autowired
    private ContentLogClient contentLogService;

    @Autowired
    private FieldinfoService fieldinfoService;

    @Autowired
    private ModuleViewService moduleViewService;

    @Autowired
    private ModuleViewController moduleViewController;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @PostMapping(value = "/add")
    @ResponseBody
    @ApiOperation("添加")
    public JsonResult<EntityId<Long>> add(@RequestBody ViewTable entity){
        List<String> stringList = viewTableService.existsView(entity.getViewname());
        JsonResult<EntityId<Long>> result=null;
        if (stringList.size()>0){
            result.renderError("视图已存在");
        }
        val user = SessionUser.getCurrentUser();
        entity.setCruser(user.getName());
        entity.setCrnumber(user.getId());
        entity.setCrtime(new Date());
        result=doAdd(getService(),entity);
        return result;
    }

    @ApiOperation(value = "根据id查询", notes="查看视图")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ViewTable> getById(@PathVariable("id") Long id){
        return doGetById(getService(),id);
    }

    @ApiOperation(value = "修改", notes="修改元数据")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult updata(@PathVariable("id")Long id,@RequestBody ViewTable viewTable){
       return  doUpdateById(getService(),id,viewTable);
    }

    @ApiOperation(value = "sql语句", notes="执行视图的sql语句")
    @PostMapping(value = "/getViewSql")
    @ResponseBody
    public JsonResult<Void> getViewSql(Long id ,String viewname ,String sql){
        //对sql语句进行修改
        System.out.println(sql);
        String sql2 = sql.trim();
        sql2 = sql2.toUpperCase();
        sql2 = sql2.substring((sql2.indexOf("SELECT\n")+7) , sql2.indexOf("FROM\n")).replaceAll("`","").replaceAll("\n", "");
        String[] s =  sql2.split(",");
        List<String> strings = new ArrayList<String>();
        List<String> strings2 = new ArrayList<String>();
        for(int j = 0 ; j < s.length ; j++){
            if (s[j].indexOf(" AS ") != -1) {
                s[j] = s[j].substring((s[j].lastIndexOf(" AS ")+4), s[j].length());
            } else {
                if (s[j].indexOf(".") != -1) {
                    s[j] = s[j].substring(s[j].lastIndexOf(".") + 1, s[j].length());
                }
            }
            List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class)
                    .eq("fieldname",s[j])
                    .eq("tableid",getService().getById(id).getTableinfoid()));
            if (list.size() <= 0){
                strings2.add(s[j]);
            }
            for(int i = 0 ; i < list.size() ; i++) {
                strings.add(String.valueOf(list.get(i).getId()));
            }
        }
        MetaDataOperator metaDataOperator = new MetaDataOperator();
        String[] s3 = strings.toArray(new String[strings.size()]);
        String[] s4 = strings2.toArray(new String[strings2.size()]);
        List<Fieldinfo> fieldInfos = new ArrayList<>();
        String nameTitle;
        if (s3.length > 0) {
            try {
                Tableinfo tableinfo = tableinfoService.getById(getService().getById(id).getTableinfoid());
                tableinfo.setViewname(viewname);
                nameTitle=tableinfo.getAnothername();
                fieldInfos = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class)
                        .in("id", String.join(",", s3)));
                viewFieldService.delete(new PropertyWrapper<>(ViewField.class).eq("viewid",id));
                getService().deleteById(id);
                viewTableService.createView(tableinfo, fieldInfos);
                boolean b = getService().performSql(sql);
                id = getService().list(new PropertyWrapper<>(ViewTable.class).eq("viewname" , viewname)).get(0).getTableinfoid();
                getViewStructure(viewname , s4 , id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            viewFieldService.delete(new PropertyWrapper<>(ViewField.class).eq("viewid",id));
            boolean b = getService().performSql(sql);
            getViewStructure(viewname, s4, getService().getById(id).getTableinfoid());
        }
        /*boolean b = getService().performSql(sql);
        if(b == true){
            result = renderSuccess("视图创建成功");
        }else {
            result = renderError("视图创建失败");
        }*/
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改sql视图");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("修改sql视图");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        metaDataOperator.setUserid(user.getId());
        metaDataOperator.setUsername(user.getName());
        metaDataOperator.setHandletype("修改sql语句");
        String address;
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            address="127.0.0.1";
        }
        metaDataOperator.setIpaddress(address);
        metaDataOperatorService.insert(metaDataOperator);
        return renderSuccess();
    }

    @ApiOperation(value = "删除", notes="删除指定id视图数据")
    @PostMapping(value = "/del/{id}")
    @ResponseBody
    public JsonResult<Void> delById(@PathVariable("id") Long id){
        delAll(id);
        return renderSuccess();
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除视图数据")
    @PostMapping("/delByIds")
    @ResponseBody
    public JsonResult<Void> delByIds(@RequestBody Long[] ids){
        for (Long id : ids) {
            delAll(id);
        }
        return renderSuccess("删除成功");
    }

    private void delAll(Long id){
        Tableinfo viewTable = tableinfoService.getById(id);
        if (viewTable != null){
            String viewname = viewTable.getTablename();
            List<String> stringList = viewTableService.existsView(viewname);
            if (stringList.size()>0){
                viewTableService.dropView(viewname);
            }
            tableinfoService.deleteById(id);//删除视图信息
            fieldinfoService.deleteByTableinfoId(id);//删除视图字段
           /* val user = SessionUser.getCurrentUser();
            MetaDataOperator metaDataOperator = new MetaDataOperator();
            metaDataOperator.setUserid(user.getId());
            metaDataOperator.setUsername(user.getName());
            metaDataOperator.setHandletype("删除视图");
            metaDataOperator.setTitle(viewTable.getAnothername());
            metaDataOperatorService.insert(metaDataOperator);*/
            metaDataOperatorService.AddLog("删除视图",viewTable.getAnothername());

        }
    }

    private void getViewStructure(String viewname , String[] s4 , Long tableid) {
        try {
            Class.forName(driverClassName).newInstance();
            Connection conn = DriverManager
                    .getConnection(url,username,password);
            DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
            ResultSet rs = meta.getColumns(null, "%", viewname, "%");
            while (rs.next()) {
                    String tableCat = rs.getString("TABLE_CAT");
                    String tableSchemaName = rs.getString("TABLE_SCHEM");
                    String tableName_ = rs.getString("TABLE_NAME");
                    String columnName = rs.getString("COLUMN_NAME");
                    int dataType = rs.getInt("DATA_TYPE");
                    String dataTypeName = rs.getString("TYPE_NAME");
                    System.out.println(columnName + "    " + dataTypeName);
                    String columnSize = rs.getString("COLUMN_SIZE");
                    int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                    int numPrecRadix = rs.getInt("NUM_PREC_RADIX");
                    int nullAble = rs.getInt("NULLABLE");
                    String remarks = rs.getString("REMARKS");
                    String columnDef = rs.getString("COLUMN_DEF");
                    String sqlDataType = rs.getString("SQL_DATA_TYPE");
                    int sqlDatetimeSub = rs.getInt("SQL_DATETIME_SUB");
                    int charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    String isNullAble = rs.getString("IS_NULLABLE");
                    String isAutoincrement = rs.getString("IS_AUTOINCREMENT");
                    String[] s = new String[]{};
                    List<Fieldinfo> list = fieldinfoService.list(new PropertyWrapper<>(Fieldinfo.class)
                            .eq("fieldname" , columnName)
                            .eq("tableid" , tableid));
                    if (list.size() <= 0){
                        ViewField viewField = new ViewField();
                        viewField.setViewid(viewTableService.list(new PropertyWrapper<>(ViewTable.class).eq("viewname" , viewname)).get(0).getId());
                        viewField.setViewname(viewname);
                        viewField.setFieldname(columnName);
                        viewField.setAnothername(columnName);
                        if (dataTypeName.equals("LONGTEXT")) {
                            viewField.setFieldtype(5);
                        } else if (dataTypeName.equals("DATETIME")) {
                            viewField.setFieldtype(4);
                        } else if (dataTypeName.equals("VARCHAR")) {
                            viewField.setFieldtype(2);
                        } else {
                            viewField.setFieldtype(1);
                        }
                        viewField.setDblength(columnSize);
                        viewField.setDefaultvalue(sqlDataType);
                        viewField.setNotnull(1);
                        viewField.setRadorchk(0);
                        viewField.setNotedit(0);
                        viewField.setHiddenfield(0);
                        viewField.setDbtype(1);
                        viewField.setCrtime(new Date());
                        viewFieldService.insert(viewField);
                    }
            }
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ViewTableService getService(){
        return viewTableService;
    }
}
