package com.jnetdata.msp.visual.moduletable.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.util.ObjectUtil;
import com.jnetdata.msp.visual.enums.Logic;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.moduletable.mapper.ModuleTableMapper;
import com.jnetdata.msp.visual.moduletable.model.ModuleTable;
import com.jnetdata.msp.visual.moduletable.service.ModuleTableService;
import com.jnetdata.msp.visual.relationmodulefield.mapper.RelationModuleFieldMapper;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Append;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

import static cn.hutool.core.util.ArrayUtil.isEmpty;

@Slf4j
@Service
public class ModuleTableServiceImpl extends BaseServiceImpl<ModuleTableMapper, ModuleTable> implements ModuleTableService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Override
    public PropertyWrapper<ModuleTable> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增表格组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleTable entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.TABLE.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.TABLE.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增表格组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除表格组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.TABLE.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.TABLE.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除表格组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改表格组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleTable entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.TABLE.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.TABLE.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改表格组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询表格组件
     */
    @Override
    public JsonResult<ModuleTable> getEntity(Long id){
        try {
            ModuleTable entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.TABLE.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询表格组件异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 生成对应的js代码
     */
    @Override
    public String generateJs(RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        try {
            //获取组件详细信息
            ModuleTable moduleTable = this.getById(relation.getModuleId());

            //获取组件关联的元数据信息（接口名，字段列表）
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);

            //获取分页信息
            boolean pageFlag = Logic.NO.getCode().equals(moduleTable.getPageSetup()) ? false : true;
            int pageSize = ObjectUtils.isEmpty(moduleTable.getPageSize()) ? 10 : moduleTable.getPageSize();

            if(ObjectUtils.isEmpty(moduleTable.getSetformType())){
                builder.append(this.getListJs(moduleRelation,pageFlag, pageSize,relation,moduleTable));
                //添加模块名和实体名
                builder.append("\nvar serviceId")
                        .append(relation.getOrder())
                        .append(" = \"").append(moduleRelation.getServiceName()).append("\";")
                        .append("\nvar url")
                        .append(relation.getOrder())
                        .append(" = \"/").append(moduleRelation.getEntityName()).append("\";");
                //设置表格字段js
                 builder.append(this.setTableField(moduleRelation,relation,moduleTable));
                if(moduleTable.getViewSetup().equals("1")){
                    builder.append("\n\t, {fixed: 'right', title:'操作', toolbar: '#")
                            .append(moduleTable.getDataAreaId())
                            .append("tableOperateBar'}");
                }
                builder.append("\n];")
                        .append("\n$(function(){")
                        .append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("View\").html(\"\");")
                        .append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("Page\").html(\"\");")
                        .append(this.getDefaultEntity(moduleRelation.getFieldList()))
                        .append("\n\tsetListData").append(relation.getOrder())
                        .append("(\"")
                        .append(moduleRelation.getServiceName())
                        .append("\",url")
                        .append(relation.getOrder())
                        .append(", curr, defaultPageSize, defaultEntity);")
                        .append("\n\tgetSearch(\"")
                        .append(moduleRelation.getServiceName())
                        .append("\", url")
                        .append(relation.getOrder())
                        .append(", curr, defaultPageSize);")
                        .append("\n})");

            }
            if(Objects.equals(moduleTable.getSetformType(),"link")){
                //获取表格数据js
                builder.append(this.getListJs(moduleRelation,pageFlag, pageSize,relation,moduleTable));
                //添加模块名和实体名
                builder.append("\nvar serviceId")
                        .append(relation.getOrder())
                        .append(" = \"").append(moduleRelation.getServiceName()).append("\";")
                        .append("\nvar url")
                        .append(relation.getOrder())
                        .append(" = \"/").append(moduleRelation.getEntityName()).append("\";");

                //设置表格字段js
                builder.append(this.setTableField(moduleRelation,relation,moduleTable));

                String[] stes = moduleTable.getSetView().split(",");
                if(moduleTable.getViewSetup().equals("1")){
                    builder.append("\n\t, {fixed: 'right', title:'操作', toolbar: '#")
                            .append(moduleTable.getDataAreaId())
                            .append("tableOperateBar'}");
                }
                builder.append("\n];")
                        .append("\n$(function(){")
                        .append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("View\").html(\"\");")
                        .append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("Page\").html(\"\");")
                        .append(this.getDefaultEntity(moduleRelation.getFieldList()))
                        .append("\n\tsetListData").append(relation.getOrder())
                        .append("(\"")
                        .append(moduleRelation.getServiceName())
                        .append("\",url")
                        .append(relation.getOrder())
                        .append(", curr, defaultPageSize, defaultEntity);")
                        .append("\n\tgetSearch(\"")
                        .append(moduleRelation.getServiceName())
                        .append("\", url")
                        .append(relation.getOrder())
                        .append(", curr, defaultPageSize);")
                        .append("\n})");
                //删除静态数据，引用加载数据的函数
                for(String r: stes){
                    if(r.equals("3")){
                        builder.append(this.setTabledele(moduleTable,relation));
                    }
                }
                //添加导出设置
                builder.append(this.export(relation ,moduleRelation ,moduleTable));
                //操作显示设置，是否显示操作，0-否，1-是
                builder.append("\n\tvar htmlTpl = ");
                int a  = 0;
                for (String r : stes) {
                    if (r.equals("1")) {
                        builder.append("\n\t'<a class=\"layui-btn layui-btn-xs\" href=\"")
                                .append(moduleTable.getFormAdress())
                                .append("?id='+'{{ d.id }}'+'\" target=\"_blank\">查看</a>'");
                    } else if (r.equals("2")) {
                        builder.append("\n\t'<a class=\"layui-btn layui-btn-xs layui-btn-normal\" href=\"")
                                .append(moduleTable.getFormAdress())
                                .append("?id='+'{{ d.id }}'+'\" target=\"_blank\">编辑</a>'");
                    } else if (r.equals("3")) {
                        builder.append("\n\t'<button class=\"layui-btn layui-btn-xs layui-btn-danger\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("DelClick({{d.id}})\">删除</button>';");
                    }
                    if (r.equals("4") || r.equals("5")) {
                        a++;
                    }
                    if (a < stes.length-1 && !r.equals("4") && !r.equals("5") && !Objects.equals(moduleTable.getSetView(),"1,5") && !Objects.equals(moduleTable.getSetView(),"1,4") && !Objects.equals(moduleTable.getSetView(),"2,5") && !Objects.equals(moduleTable.getSetView(),"2,4")&& !Objects.equals(moduleTable.getSetView(),"1,4,5")&& !Objects.equals(moduleTable.getSetView(),"2,4,5")) {
                        builder.append("+");
                        a++;
                    }
                }
                builder.append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("tableOperateBar\").html(htmlTpl);");
                boolean views = true;
                boolean expers = true;
                boolean del = true;
                int b = 1;
                builder.append("\n\t var htmlButton = ");
                for(String r : stes) {
                    if (r.equals("4")) {
                        views = false;
                        builder.append("'<a class=\"layui-btn\" href=\"")
                                .append(moduleTable.getFormAdress())
                                .append("\" target=\"_blank\">新建</a>'");
                        b++;
                    }
                    if(r.equals("5") && b == 2){
                        builder.append("+");
                    }
                    if(r.equals("5")){
                        expers = false;
                        builder.append("'<button class=\"layui-btn layui-btn-primary layui-border\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("ExportFile()\">导出</button>'");
                        b++;
                    }
                    if(r.equals("6") && b == 2 || b==3){
                        builder.append("+");
                    }
                    if(r.equals("6")){
                        del = false;
                        builder.append("''<button class=\"layui-btn layui-btn-primary layui-border\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("BatchDel()\">批量彻底删除</button>'");
                        b++;
                    }

                }
                if(b!=1){
                    builder.append(";");
                }
                builder.append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append(" .visual_table  .visual_button\").html(htmlButton);");
                if(views && expers && del){
                    builder.append("\n$(\"#")
                            .append(moduleTable.getDataAreaId())
                            .append(" .visual_table .visual_button\").remove();");
                }
                builder.append(this.batchdel1(relation,moduleRelation,moduleTable));
            }

            if(Objects.equals(moduleTable.getSetformType(),"layer")){
                String[] stes = moduleTable.getSetView().split(",");
                 builder.append(this.getListJs2(moduleRelation,pageFlag, pageSize,relation,moduleTable));
                 builder.append("\nvar serviceId")
                        .append(relation.getOrder())
                        .append(" = \"").append(moduleRelation.getServiceName()).append("\";")
                        .append("\nvar url")
                        .append(relation.getOrder())
                        .append(" = \"/").append(moduleRelation.getEntityName()).append("\";");
                builder.append(this.tabletitle(relation,moduleRelation,moduleTable));
                 builder.append("\n$(function(){")
                        .append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append("View\").html(\"\");")
                         .append("\n})");
                builder.append(this.setaddField(moduleTable,moduleRelation,relation));
                //builder.append(this.delFormClick(moduleRelation,relation));
                builder.append(this.setOpenLayer(relation,moduleRelation,moduleTable));
                builder.append(this.doDeleteByIds(relation,moduleRelation,moduleTable));
                builder.append(this.batchdel1(relation,moduleRelation,moduleTable));//批量真删除
                builder.append(this.operateBar(moduleRelation,moduleTable));
                builder.append("\n$(function(){")
                        .append("\n\toperateBar('")
                        .append(moduleTable.getDataAreaId())
                        .append("tableFilter');")
                        .append("\n\t$('#")
                        .append(moduleTable.getDataAreaId())
                        .append("FormLayerTemplate').html(replaceTemplate())")
                        .append("\n\tsetListData")
                        .append(relation.getOrder())
                        .append("(serviceId")
                        .append(relation.getOrder())
                        .append(", url")
                        .append(relation.getOrder())
                        .append(", curr, defaultPageSize, {});")
                        .append("\n});");
                builder.append(this.detailjs(relation,moduleRelation,moduleTable));
                //添加导出设置
                builder.append(this.export(relation ,moduleRelation ,moduleTable));
                builder.append(this.newstring(stes,relation,moduleRelation,moduleTable));
                builder.append("\n\t var htmlButton = ");
                int d =1 ;
                for(String r : stes) {
                    if (r.equals("4")) {
                                builder.append("'<button class=\"layui-btn\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("formAdd()\">新建</button>'");
                                d++;
                    }
                    if(r.equals("5") && d == 2 ||d ==3){
                        builder.append("+\n\t");
                    }
                    if(r.equals("5")){
                        builder.append("'<button class=\"layui-btn layui-btn-primary layui-border\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("ExportFile()\">导出</button>'");
                                d++;
                    }
                    if(r.equals("6") && d == 2 || d==3){
                        builder.append("+\n\t");
                    }
                    if(r.equals("6")){
                        builder.append("''<button class=\"layui-btn layui-btn-primary layui-border\" onclick=\"")
                                .append(moduleTable.getDataAreaId())
                                .append("BatchDel()\">批量彻底删除</button>'");
                        d++;
                    }
                }
                if(d!=1){
                    builder.append(";");
                }
                builder.append("\n\t$(\"#")
                        .append(moduleTable.getDataAreaId())
                        .append(" .visual_table  .visual_button\").html(htmlButton);");
                if(d == 1){
                    builder.append("\n$(\"#")
                            .append(moduleTable.getDataAreaId())
                            .append(" .visual_table .visual_button\").remove();");
                }
            }


        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

    /**
     * 获取表格数据js
     * @param pageFlag 是否显示分页
     * @param pageSize 每页显示数据条数
     */
    private String getListJs(ModuleRelation moduleRelation,boolean pageFlag, int pageSize,RelationModuleTemplate relation,ModuleTable moduleTable){
        pageSize = pageSize <= 0 ? 10 : pageSize;

        StringBuilder builder = new StringBuilder();
        builder.append("\n//每页显示数据条数，当前页")
                .append("\nvar defaultPageSize = ")
                .append(pageSize)
                .append(";")
                .append("\nvar curr = 1;")
                .append("\n//列表查询")
                .append("\nfunction setListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize,entity){")
                .append("\n\tvar jsonData = {")
                .append("\n\t\t\"pager\": {")
                .append("\n\t\t  \"current\": curr,")
                .append("\n\t\t  \"size\": defaultPageSize")
                .append("\n\t\t},")
                .append("\n\t\t\"entity\":entity")
                .append("\n\t};")
                .append("\n\tajax(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append("+'/listing','post',JSON.stringify(jsonData)).then(function (data){")
                .append("\n\t\tif(data.success){\t")
                .append("\n\t\t\tsetTableData('#")
                .append(moduleTable.getDataAreaId())
                .append("View',data.obj,columns")
                .append(relation.getOrder())
                .append(");");
        if(pageFlag){
            builder.append("\n\t\t\tpage(data.obj.total, curr, defaultPageSize, setListData")
                    .append(relation.getOrder())
                    .append(", '")
                    .append(moduleTable.getDataAreaId()).append("Page', entity, serviceId")
                    .append(relation.getOrder())
                    .append(", url")
                    .append(relation.getOrder())
                    .append(");");
        }else {
            builder.append("\n\t\t\t$('#")
                    .append(moduleTable.getDataAreaId())
                    .append(" .visual_page').remove();");
        }
        builder.append("\n\t\t}else{")
                .append("\n\t\t\tconsole.log(data.msg);")
                .append("\n\t\t}")
                .append("\n\t})")
                .append("\n}")
                .append("\n//条件搜索")
                .append("\nfunction getSearch(")
                .append("serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize){")
                .append("\n\tform.on(\"submit(LAY-back-search)\",function(data){")
                .append("\n\t\tsetListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize,data.field)")
                .append("\n\t\treturn false;")
                .append("\n\t})")
                .append("\n}")
                .append("\nsetRender();");
        return builder.toString();
    }



    /**
     * 获取表格数据js
     * @param pageFlag 是否显示分页
     * @param pageSize 每页显示数据条数
     */
    private String getListJs2(ModuleRelation moduleRelation,boolean pageFlag, int pageSize,RelationModuleTemplate relation,ModuleTable moduleTable){
        pageSize = pageSize <= 0 ? 10 : pageSize;

        StringBuilder builder = new StringBuilder();
        builder.append("\n//每页显示数据条数，当前页")
                .append("\nvar defaultPageSize = ")
                .append(pageSize)
                .append(";")
                .append("\nvar curr = 1;")
                .append("\n//列表查询")
                .append("\nfunction setListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize,entity){")
                .append("\n\tvar jsonData = {")
                .append("\n\t\t\"pager\": {")
                .append("\n\t\t  \"current\": curr,")
                .append("\n\t\t  \"size\": defaultPageSize")
                .append("\n\t\t},")
                .append("\n\t\t\"entity\":entity")
                .append("\n\t};")
                .append("\n\tajax(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append("+'/listing','post',JSON.stringify(jsonData)).then(function (data){")
                .append("\n\t\tif(data.success){\t")
                .append("\n\t\t\tsetTableData('#")
                .append(moduleTable.getDataAreaId())
                .append("View',data.obj,columns")
                .append(relation.getOrder())
                .append(");");
        if(pageFlag){
            builder.append("\n\t\t\tpage(data.obj.total, curr, defaultPageSize, setListData")
                    .append(relation.getOrder())
                    .append(", '")
                    .append(moduleTable.getDataAreaId()).append("Page', entity, serviceId")
                    .append(relation.getOrder())
                    .append(", url")
                    .append(relation.getOrder())
                    .append(");");
        }else {
            builder.append("\n\t\t\t$('#")
                    .append(moduleTable.getDataAreaId())
                    .append(" .visual_page').remove();");
        }
        builder.append("\n\t\t}else{")
                .append("\n\t\t\tconsole.log(data.msg);")
                .append("\n\t\t}")
                .append("\n\t})")
                .append("\n}")
                .append("\n//条件搜索")
                .append("\nfunction getSearch(")
                .append("serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize){")
                .append("\n\tform.on(\"submit(LAY-back-search)\",function(data){")
                .append("\n\t\tsetListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(",curr,defaultPageSize,data.field)")
                .append("\n\t\treturn false;")
                .append("\n\t})")
                .append("\n}")
                .append("\nsetRender();");
        return builder.toString();
    }
    private String setTabledele(ModuleTable moduleTable,RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        builder.append("\n//删除")
                .append("\nfunction ").append(moduleTable.getDataAreaId())
                .append("DelClick(id){")
                .append("\n\tlayer.confirm(")
                .append("\n\t\t'确认删除?',")
                .append("\n\t\t{ icon: 3, title: '提示' },")
                .append("\n\t\tfunction (index) {")
                .append("\n\t\t\t layer.close(index);")
                .append("\n\t\t\tajax(serviceId").append(relation.getOrder())
                .append(",url")
                .append(relation.getOrder())
                .append(" + \"/real/\" + id, \"delete\").then(function (data) {")
                .append("\n\t\t\t\tvar msg = data.msg;")
                .append("\n\t\t\t\tif (data.success) {")
                .append("\n\t\t\t\t\tmsg = '删除成功';")
                .append("\n\t\t\t\t\tshowWinTips(msg,null,1);")
                .append("\n\t\t\t\t\tsetListData")
                .append(relation.getOrder())
                .append("(\"exercise\", \"/computer\", 1, 10, {});")
                .append("\n\t\t\t\t} else {")
                .append("\n\t\t\t\t\tlayer.msg(data.msg);")
                .append("\n\t\t\t\t}")
                .append("\n\t\t\t});")
                .append("\n\t\t}")
                .append("\n\t);")
                .append("\n}");
        return builder.toString();
    }

    /**
     * 表头
     * @param moduleTable
     * @param relation
     * @return
     */
    private String detailjs(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\nfunction replaceTemplate() {")
                .append("\n\t var data = [");
        for(RelationModuleField field: moduleRelation.getFieldList()) {
            builder.append("\n\t{\"name\" : \"")
                    .append(field.getAnothername())
                    .append("\", \"code\": \"")
                    .append(field.getFieldname())
                    .append("\"},");

        }
        builder.append("\n\t];")
                .append("\n\tvar replaceHtml = '<form class=\"layui-form\" lay-filter=\"formFilter\">';")
                .append("\n\tfor(var i=0; i<data.length; i++){")
                .append("\n\t\treplaceHtml += '\\n\\t<div class=\"layui-form-item\">';")
                .append("\n\t\treplaceHtml += '\\n\\t\\t<label class=\"layui-form-label\">';")
                .append("\n\t\treplaceHtml += data[i].name;")
                .append("\n\t\treplaceHtml += '</label>';")
                .append("\n\t\treplaceHtml += '\\n\\t\\t<div class=\"layui-input-block\">';")
                .append("\n\t\treplaceHtml += '\\n\\t\\t<input type=\"text\" name=\"';")
                .append("\n\t\treplaceHtml += data[i].code;")
                .append("\n\t\treplaceHtml += '\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"请输入\" class=\"layui-input\">';")
                .append("\n\t\treplaceHtml += '\\n\\t\\t</div>';")
                .append("\n\t\treplaceHtml += '\\n\\t</div>';")
                .append("\n\t}")
                .append("\n\t\treplaceHtml += '\\n\\t<div class=\"layui-form-item\">'")
                .append("\n\t\treplaceHtml += '\\n\\t<div class=\"layui-input-block\">';")
                .append("\n\t\treplaceHtml += '\\n\\t<button type=\"submit\" class=\"layui-btn\" lay-submit lay-filter=\"formSubmit\">立即提交</button>';")
                .append("\n\t\treplaceHtml += '\\n\\t</div>';")
                .append("\n\t\treplaceHtml += '\\n\\t</div>';")
                .append("\n\treplaceHtml += '</form>';")
                .append("\n\treturn replaceHtml;")
                .append("\n}");

        return builder.toString();
    }

    /**
     * 批量真删除方法调用
     * @param relation
     * @param moduleRelation
     * @param moduleTable
     * @return
     */
    private String batchdel1(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
    StringBuilder builder = new StringBuilder();
    builder.append("\n//批量真删除方法调用")
            .append("\nfunction ")
            .append(moduleTable.getDataAreaId())
            .append("BatchDel(){")
            .append("\n\tvar data = layui.table.checkStatus('tableData').data")
            .append("\n\t\tconsole.log(data)")
            .append("\n\t\tdoDeleteByIds(data)")
            .append("\n}")
            .append("\n");
    return builder.toString();
}
    /**批量假删除方法调用
     * @param relation
     * @param moduleRelation
     * @param moduleTable
     * @return
     */
    private String fackdel(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n//批量假删除方法调用")
                .append("\nfunction ")
                .append(moduleTable.getDataAreaId())
                .append("BatchFackDel(){")
                .append("\n\tvar data = layui.table.checkStatus('tableData').data")
                .append("\n\tconsole.log(data)")
                .append("\n\tdoFalseDeleteByIds(data)")
                .append("\n}")
                .append("\n");
        return builder.toString();
    }


    /**批量假删除
     * @param relation
     * @param moduleRelation
     * @param moduleTable
     * @return
     */
    private String batchfackdel(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n//批量假删除")
                .append("\nfunction doFalseDeleteByIds(data) {")
                .append("\n\tvar ids = [];")
                .append("\n\tfor(var item of data){")
                .append("\n\t\tids.push(item.id);")
                .append("\n\t}")
                .append("\n\tids = ids.join(',');")
                .append("\n\tlayer.alert(\"是否确定删除,该操作不可撤回\", function () {")
                .append("\n\t\tajax(").append("serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append("+  '/' + ids + \"/batch\", \"delete\").then(res => {")
                .append("\n\t\t\tres.msg = \"删除失败,\" + res.msg;")
                .append("\n\t\t\tif (res.success) {")
                .append("\n\t\t\t\tsetListData(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(", curr, defaultPageSize, {});")
                .append("\n\t\t\t\tlayer.closeAll();")
                .append("\n\t\t\t} else {")
                .append("\n\t\t\t\tlayer.alert(res.msg);")
                .append("\n\t\t\t}")
                .append("\n\t\t})")
                .append("\n\t});")
                .append("\n}")
                .append("\n");
        return builder.toString();
    }
    /**
     * 表头
     * @param moduleTable
     * @param relation
     * @return
     */
    private String tabletitle(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n//表头")
                .append("\nvar columns")
                .append(relation.getOrder())
                .append(" = [");
        if(Objects.equals(moduleTable.getSetCheck(),"1")){
             builder.append("\n\t{type: 'checkbox', fixed: 'left'},");
        }
        if(Objects.equals(moduleTable.getSetNum(),"1")){
            builder.append("\n\t {type: 'numbers', title: '序号', fixed: 'left'}");
        }

        //添加字段信息
        for(RelationModuleField field: moduleRelation.getFieldList()) {
            builder.append("\n\t, {field: '")
                    .append(field.getFieldname())
                    .append("', title: '")
                    .append(field.getAnothername())
                    .append("'}");
            //添加对应的列宽
            if (!StringUtils.isEmpty(field.getShowWidth())) {
                builder.append("', width: '")
                        .append(field.getShowWidth());
            }
        }
        if(moduleTable.getViewSetup().equals("1")){
            builder.append("\n\t, {fixed: 'right', title:'操作', toolbar: '#")
                    .append(moduleTable.getDataAreaId())
                    .append("tableOperateBar'}");
        }
        builder.append("\n];");
        return builder.toString();
    }
    /**
     * 设置表格字段js
     */
    private String setTableField(ModuleRelation moduleRelation,RelationModuleTemplate relation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n//设置表格字段")
                .append("\nvar columns")
                .append(relation.getOrder())
                .append(" = [");
        if(Objects.equals(moduleTable.getSetCheck(),"1")){
             builder.append("\n\t{type: 'checkbox', fixed: 'left'}");
        }
        if(Objects.equals(moduleTable.getSetNum(),"1")){
             builder.append("\n\t, {type: 'numbers', title: '序号', fixed: 'left'}");
        }


        //字段序号，第一个字段会保存数据id
        int serialNumber = 1;
        //添加字段信息
        for(RelationModuleField field: moduleRelation.getFieldList()){
            builder.append("\n\t, {field: '")
                    .append(field.getFieldname())
                    .append("', title: '")
                    .append(field.getAnothername());
            //添加对应的列宽
            if(!StringUtils.isEmpty(field.getShowWidth())){
                builder.append("', width: '")
                        .append(field.getShowWidth());
            }
            //添加对应的事件和样式
            if(StringUtils.isEmpty(field.getEventType()) && !Logic.YES.getCode().equals(field.getStyleFlag())){
                if(serialNumber == 1){
                    builder.append("', templet: function (data) {")
                            .append("\n\t\t\treturn \"<span data-id='\" + data.id + \"'>\" + data.")
                            .append(field.getFieldname()).append(" + \"</span>\"")
                            .append("\n\t}}");
                }else{
                    builder.append("'}");
                }
            }else{
                builder.append("', templet: function (data) {")
                        .append("\n\t\t\treturn \"<span ");
                if(serialNumber == 1){
                    builder.append("data-id='\" + data.id + \"' ");
                }

                //添加样式
                if(Logic.YES.getCode().equals(field.getStyleFlag())){
                    builder.append("class='").append(field.getFieldname()).append("_style").append("' ");
                }
                //添加事件
                if(!StringUtils.isEmpty(field.getEventType())){
                    builder.append("onclick='")
                            .append(field.getFieldname()).append(field.getEventType())
                            .append("(\\\"\" + data.id + \"\\\")' ");
                }
                builder.append(">\" + data.")
                        .append(field.getFieldname()).append(" + \"</span>\";")
                        .append("\n\t\t}")
                        .append("\n\t}");
            }
            serialNumber++;
        }
        return builder.toString();
    }

    /**
     * 新建弹框样式
     * @param moduleRelation
     * @param relation
     * @return
     */
    private String setaddField(ModuleTable moduleTable,ModuleRelation moduleRelation,RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        builder.append("\n/*")
                .append("\n* 新建")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleTable.getDataAreaId())
                .append("formAdd(){")
                .append("\n\t")
                .append(moduleTable.getDataAreaId())
                .append("setOpenLayer({});")
                .append("\n\tform.on('submit(formSubmit)', function(data){")
                .append("\n\t\tajax(serviceId")
                .append(relation.getOrder())
                .append(", ")
                .append("url")
                .append(relation.getOrder())
                .append(", \"post\", JSON.stringify(data.field)).then(function(data){")
                .append("\n\t\tif(data.success){")
                .append("\n\t\t\tlayer.msg('新建成功');")
                .append("\n\t\t\tsetTimeout(function(){")
                .append("\n\t\t\t\tlayer.closeAll();")
                .append("\n\t\t\t\tsetListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(", curr, defaultPageSize, {});")
                .append("\n\t\t\t},2000);")
                .append("\n\t\t}else{")
                .append("\n\t\t\tconsole.log(data.msg);")
                .append("\n\t\t}")
                .append("\n\t});")
                .append("\n\treturn false;")
                .append("\n});")
                .append("\n}");
        return builder.toString();
    }

/**
 * 删除弹框样式
 * @param moduleRelation
 * @param relation
 * @return
 */
private String delFormClick(ModuleRelation moduleRelation,RelationModuleTemplate relation){
    StringBuffer builder = new StringBuffer();
    builder.append("\n/*")
            .append("\n* 删除")
            .append("\n*/")
            .append("\nfunction delClick(id){")
            .append("\n\tlayer.confirm(")
            .append("\n\t\t'确认删除?',")
            .append("\n\t\t{ icon: 3, title: '提示' },")
            .append("\n\t\tfunction (index) {")
            .append("\n\t\t\tlayer.close(index);")
            .append("\n\t\t\tajax(\"exercise\", \"/computer/real/\" + id, \"delete\").then(function (data) {")
            .append("\n\t\t\t\tvar msg = data.msg;")
            .append("\n\t\t\t\tif (data.success) {")
            .append("\n\t\t\t\t\tmsg = '删除成功';")
            .append("\n\t\t\t\t\tshowWinTips(msg,null,1);")
            .append("\n\t\t\t\t\tsetListData(\"exercise\", \"/computer\", 1, 10, {});")
            .append("\n\t\t\t\t} else {")
            .append("\n\t\t\t\t\tlayer.msg(data.msg);")
            .append("\n\t\t\t\t}")
            .append("\n\t\t\t});")
            .append("\n\t\t}")
            .append("\n\t);")
            .append("\n}");

    return builder.toString();
}

    /**
     * 设置弹出层数据（新增，查看，修改）
     * @param moduleRelation
     * @return
     */
    private String setOpenLayer(RelationModuleTemplate relation, ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuffer builder = new StringBuffer();
        builder.append("\n/*")
                .append("\n* 设置弹出层数据（新增，查看，修改）")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleTable.getDataAreaId())
                .append("setOpenLayer(data) {")
                .append("\n\tif(data.editType == 1){")
                .append("\n\t\tvar editType = \"修改\";")
                .append("\n\t}else if(data.editType == 2){")
                .append("\n\t\tvar editType = \"查看\";")
                .append("\n\t}else{")
                .append("\n\t\tvar editType = \"新建\";")
                .append("\n\t}")
                .append("\n\tlayui.layer.open({")
                .append("\n\t\ttype: 1,")
                .append("\n\t\ttitle: editType,")
                .append("\n\t\tarea: ['50%','50%'],")
                .append("\n\t\tcontent: '<div id=\"")
                .append(moduleTable.getDataAreaId())
                .append("TableLayerSetView\" class=\"layer_form_box\"></div>',")
                .append("\n\t});")
                .append("\n\tgetData(data, \"#")
                .append(moduleTable.getDataAreaId())
                .append("FormLayerTemplate\", \"#")
                .append(moduleTable.getDataAreaId())
                .append("TableLayerSetView\");")
                .append("\n\tform.val('formFilter', data);")
                .append("\n\tvar id = data.id;")
                .append("\n\tif(id){")
                .append("\n\t\tform.on('submit(formSubmit)', function(data){")
                .append("\n\t\t\tajax(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(" + '/' + id, \"put\", JSON.stringify(data.field)).then(function(data){")
                .append("\n\t\t\t\tif(data.success){")
                .append("\n\t\t\t\t\tlayer.msg('编辑成功');")
                .append("\n\t\t\t\t\tsetTimeout(function(){")
                .append("\n\t\t\t\t\t\tlayer.closeAll();")
                .append("\n\t\t\t\t\t\tsetListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(", curr, defaultPageSize, {});")
                .append("\n\t\t\t\t\t},2000);")
                .append("\n\t\t\t\t}else{")
                .append("\n\t\t\t\t\tconsole.log(data.msg);")
                .append("\n\t\t\t\t}")
                .append("\n\t\t\t});")
                .append("\n\t\t\treturn false;")
                .append("\n\t\t});")
                .append("\n\t}")
                .append("\n}");
        return builder.toString();
    }


    /**
     * 导出
     * @param relation
     * @param moduleRelation
     * @param moduleTable
     * @return
     */
    private String export(RelationModuleTemplate relation, ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n/*")
                .append("\n* 导出")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleTable.getDataAreaId())
                .append("ExportFile(){")
                .append("\n\tlayer.open({")
                .append("\n\t\ttype: 1,")
                .append("\n\t\ttitle:'导出',")
                .append("\n\t\tarea: ['540px', '200px'],")
                .append("\n\t\tcontent: '<div class=\"layer_form_box\" id=\"")
                .append(moduleTable.getDataAreaId())
                .append("ExportLayerView\"></div>',")
                .append("\n\t\tsuccess: function(layero, index){")
                .append("\n\t\t\tvar data = {};")
                .append("\n\t\tgetData(data, '#")
                .append(moduleTable.getDataAreaId())
                .append("ExportTemplate', '#")
                .append(moduleTable.getDataAreaId())
                .append("ExportLayerView'); ")
                .append("\n\t\t\tform.render();")
                .append("\n\t\t\t$('#")
                .append(moduleTable.getDataAreaId())
                .append("ExportLayerView form').attr('action', getAjaxUrl(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(" + '/export3'));")
                .append("\n\t\t}")
                .append("\n\t})")
                .append("\n}");
            return  builder.toString();
    }
    /**
     * 设置弹出层数据（新增，查看，修改）
     * @param moduleRelation
     * @return
     */
    private String doDeleteByIds(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n/*")
                .append("\n* 批量删除")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleTable.getDataAreaId())
                .append("doDeleteByIds(data) {")
                .append("\n\tvar ids = [];")
                .append("\n\tfor(var item of data){")
                .append("\n\t\tids.push(item.id);")
                .append("\n\t}")
                .append("\n\tids = ids.join(',');")
                .append("\n\tlayer.alert(\"是否确定删除,该操作不可撤回\", function () {")
                .append("\n\t\tajax(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(" + \"/real/\" + ids + \"/batch\", \"delete\").then(res => {")
                .append("\n\t\t\tres.msg = \"删除失败,\" + res.msg;")
                .append("\n\t\t\tif (res.success) {")
                .append("\n\t\t\t\tsetListData")
                .append(relation.getOrder())
                .append("(serviceId")
                .append(relation.getOrder())
                .append(", url")
                .append(relation.getOrder())
                .append(", curr, defaultPageSize, {});")
                .append("\n\t\t\t\tlayer.closeAll();")
                .append("\n\t\t\t} else {")
                .append("\n\t\t\t\tlayer.alert(res.msg);")
                .append("\n\t\t\t}")
                .append("\n\t\t})")
                .append("\n\t});")
                .append("\n}");

        return builder.toString();
    }

    private String newstring(String[] stes, RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tvar htmlTpl = ");
        int c  = 0;
        for (String r : stes) {
            if (r.equals("1")) {
                builder.append("\n\t'<a class=\"layui-btn layui-btn-xs\" lay-event=")
                        .append("\"detail\">查看</a>'");
            } else if (r.equals("2")) {
                builder.append("\n\t'<button class=\"layui-btn layui-btn-xs layui-btn-normal\" lay-event=")
                        .append("\"edit\">编辑</button>'");
            } else if (r.equals("3")) {
                builder.append("\n\t'<button class=\"layui-btn layui-btn-xs layui-btn-danger\" lay-event=")
                        .append("\"del\">删除</button>';");
            }
            if (r.equals("4")||r.equals("5")) {
                c++;
            }
            if (c < stes.length-1 && !r.equals("4") && !r.equals("5") && !Objects.equals(moduleTable.getSetView(),"1,5") && !Objects.equals(moduleTable.getSetView(),"1,4") && !Objects.equals(moduleTable.getSetView(),"2,5") && !Objects.equals(moduleTable.getSetView(),"2,4")&& !Objects.equals(moduleTable.getSetView(),"1,4,5")&& !Objects.equals(moduleTable.getSetView(),"2,4,5")) {
                builder.append("+");
                c++;
            }
        }
        builder.append("\n\t$(\"#")
                .append(moduleTable.getDataAreaId())
                .append("tableOperateBar\").html(htmlTpl);");
            return builder.toString();
    }
    /**
     * 设置弹出层数据（新增，查看，修改）
     * @param moduleRelation
     * @return
     */
    private String operateBar(ModuleRelation moduleRelation,ModuleTable moduleTable){
        StringBuilder builder = new StringBuilder();
        builder.append("\n/*")
                .append("\n* 表格操作功能")
                .append("\n*/")
                .append("\nfunction operateBar(filter){")
                .append("\n\tlayui.table.on('tool('+ filter +')', function (obj) {")
                .append("\n\t\tvar data = obj.data;")
                .append("\n\t\tvar layEvent = obj.event;")
                .append("\n\t\tif (layEvent === 'detail') {")
                .append("\n\t\t\tdata.editType = 2;")
                .append("\n\t\t\t")
                .append(moduleTable.getDataAreaId())
                .append("setOpenLayer(data);")
                .append("\n\t\t} else if (layEvent === 'del') {")
                .append("\n\t\t\tlayer.alert(\"确定删除:[\" + data.computerCode + \"]\", function () {")
                .append("\n\t\t\t\t")
                .append(moduleTable.getDataAreaId())
                .append("doDeleteByIds([data]);")
                .append("\n\t\t\t})")
                .append("\n\t\t} else if (layEvent === 'edit') {")
                .append("\n\t\t\tdata.editType = 1;")
                .append("\n\t\t\t")
                .append(moduleTable.getDataAreaId())
                .append("setOpenLayer(data);")
                .append("\n\t\t}")
                .append("\n\t});")
                .append("\n}");

        return builder.toString();
    }
    /**
     * 字段信息集合
     * @param fieldList
     */
    private String getDefaultEntity(List<RelationModuleField> fieldList){
        StringBuilder builder = new StringBuilder();
        builder.append("\n\t//默认传参")
                .append("\n\tvar defaultEntity = {");
        for(RelationModuleField field: fieldList){
            if(Logic.YES.getCode().equals(field.getIsPassGinseng())){
                builder.append("'")
                        .append(field.getFieldname())
                        .append("':'")
                        .append(field.getFieldValue())
                        .append("', ");
            }
        }
        builder.append("};");
        return builder.toString();
    }
}