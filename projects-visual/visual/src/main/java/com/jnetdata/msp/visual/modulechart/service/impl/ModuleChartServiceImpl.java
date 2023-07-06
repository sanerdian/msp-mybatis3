package com.jnetdata.msp.visual.modulechart.service.impl;

import com.google.common.collect.Maps;
import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.Logic;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulechart.mapper.ModuleChartMapper;
import com.jnetdata.msp.visual.modulechart.model.ModuleChart;
import com.jnetdata.msp.visual.modulechart.service.ModuleChartService;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmodulefield.vo.ModuleRelation;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.field.FieldList;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;
import sun.security.provider.MD2;

import java.security.Key;
import java.util.*;

@Slf4j
@Service
public class ModuleChartServiceImpl extends BaseServiceImpl<ModuleChartMapper, ModuleChart> implements ModuleChartService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    /**
     * 新增echarts组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleChart entity) {
        try {
            if (ObjectUtils.isEmpty(entity.getVisualTemplateId())) {
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.CHART.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.CHART.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        } catch (Exception e) {
            log.error("新增echarts组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除echarts组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id) {
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.CHART.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.CHART.value());
            return JsonResult.success();
        } catch (Exception e) {
            log.error("删除echarts组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改echarts组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleChart entity) {
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.CHART.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.CHART.value());
            return JsonResult.success();
        } catch (Exception e) {
            log.error("修改echarts组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询echarts组件
     */
    @Override
    public JsonResult<ModuleChart> getEntity(Long id) {
        try {
            ModuleChart entity = this.getById(id);
            if (ObjectUtils.isEmpty(entity)) {
                return JsonResult.fail("对象不存在");
            }
            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.CHART.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        } catch (Exception e) {
            log.error("查询echarts组件异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 生成对应的js代码
     */
    @Override
    public String generateJs(RelationModuleTemplate relation) {
        StringBuilder builder = new StringBuilder();
        try {
            //获取组件详细信息
            ModuleChart moduleChart = this.getById(relation.getModuleId());

            //获取组件关联的元数据信息（接口名，字段列表）
            ModuleRelation moduleRelation = relationModuleFieldService.getModuleRelationEntity(relation);

            boolean pageFlag = Logic.NO.getCode().equals(moduleChart.getPageSetup()) ? false : true;
            int pageSize = ObjectUtils.isEmpty(moduleChart.getPageSize()) ? 10 : moduleChart.getPageSize();
            if(Objects.equals(moduleChart.getViewType(),"areastack") || Objects.equals(moduleChart.getViewType(),"stack")|| Objects.equals(moduleChart.getViewType(),"area")|| Objects.equals(moduleChart.getViewType(),"line")|| Objects.equals(moduleChart.getViewType(),"bar")) {
                builder.append("\n$(function () {")
                        .append("\n\t function getChartListData(){")
                        .append("\n\tvar tableFields = [");
                for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                    builder.append("\n\t\t{\"title\":\"")
                            .append(relationModuleField.getAnothername())
                            .append("\",\"field\":\"")
                            .append(relationModuleField.getFieldname())
                            .append("\"},");
                }
                builder.append("\n\t];")
                        .append("\n\t\tvar jsonData = {")
                        .append("\n\t\t\t\"entity\": {},")
                        .append("\n\t\t\t\"pager\": {")
                        .append("\n\t\t\t\t\"current\": 1,")
                        .append("\n\t\t\t\t\"size\": ")
                        .append(moduleChart.getPageSize())
                        .append("\n\t\t\t}")
                        .append("\n\t\t}")
                        .append("\n\tvar serviceId = '")
                        .append(moduleRelation.getServiceName())
                        .append("';")
                        .append("\n\tvar url")
                        .append(" = '/")
                        .append(moduleRelation.getEntityName())
                        .append("';")
                        .append("\n\tajax(serviceId")
                        .append(", url")
                        .append("+ '/listing', 'post', JSON.stringify(jsonData)).then(function (data) {")
                        .append("\n\t\t\tif (data.success) {");
                if (Objects.equals(moduleChart.getShowTable(), 1)) {
                    builder.append("\n\t\t\t\tsetTableData('#")
                            .append(moduleChart.getDataAreaId())
                            .append("TableView', data.obj, tableFields);");
                    if (Objects.equals(moduleChart.getShowChar(), 0)) {
                        builder.append("\n\t\t\t\t$('#")
                                .append(moduleChart.getDataAreaId())
                                .append("ChartView').remove();");
                    }
                }
                builder.append("\n\t\t\t\t$('#")
                        .append(moduleChart.getDataAreaId())
                        .append("TableView').remove();");
                if (Objects.equals(moduleChart.getShowChar(), 1)) {
                    builder.append("\n\t\t\t\tchartProcessData(data.obj.records);");
                }
                builder.append("\n\t\t\t}")
                        .append("\n\t\t})")
                        .append("\n\t}");
                if (Objects.equals(moduleChart.getShowChar(), 1)) {
                    builder.append("\n/*")
                            .append("\n* 图表数据处理")
                            .append("\n@param dataArr 元数据表的数据")
                            .append("\n*/")
                            .append("\nfunction chartProcessData(dataArr){");
                    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                        builder.append("\n\tvar ")
                                .append(relationModuleField.getFieldname())
                                .append("ValArr = [];");
                    }
                    builder.append("\n\tfor(var k = 0; k < dataArr.length; k++){");
                    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                        builder.append("\n\t\tvar ")
                                .append(relationModuleField.getFieldname())
                                .append("ValObj = dataArr[k].")
                                .append(relationModuleField.getFieldname())
                                .append(";")
                                .append("\n\t\t")
                                .append(relationModuleField.getFieldname())
                                .append("ValArr.push(")
                                .append(relationModuleField.getFieldname())
                                .append("ValObj);");
                    }
                    builder.append("\n\t}")
                            .append("\n\tvar chartDataArr = [");
                    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                        if (Objects.equals(relationModuleField.getAnothername(), "标题")) {
                            builder.append("\n\t\t{\"chName\":\"")
                                    .append(relationModuleField.getAnothername())
                                    .append("\",")
                                    .append("\n\t\t\"charData\":")
                                    .append(relationModuleField.getFieldname())
                                    .append("ValArr,")
                                    .append("\n\t\t\"charType\":\"\"")
                                    .append("\n\t},");
                        }
                        else{
                            builder.append("\n\t\t{\"chName\":\"")
                                    .append(relationModuleField.getAnothername())
                                    .append("\",")
                                    .append("\n\t\t\"charData\":")
                                    .append(relationModuleField.getFieldname())
                                    .append("ValArr,")
                                    .append("\n\t\t\"charType\":\"")
                                    .append(relationModuleField.getSeriesType())
                                    .append("\"")
                                    .append("\n\t},");
                        }
                    }
                    builder.append("\n\t];")
                            .append("\n\tvar filedsArr =  [");
                    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                        if (!Objects.equals(relationModuleField.getAnothername(),"标题")) {
                            builder.append("\"")
                                    .append(relationModuleField.getAnothername())
                                    .append("\",");
                        }
                    }
                    builder.append("];")
                            .append("\n\tvar chartSeriesObj = {};")
                            .append("\n\tvar chartSeriesData = [];")
                            .append("\n\tvar titleArr;")
                            .append("\n\tfor(var i=0; i<chartDataArr.length; i++){")
                            .append("\n\t\tif(chartDataArr[i].charType == \"\"){")
                            .append("\n\t\ttitleArr = chartDataArr[i].charData;")
                            .append("\n\t} else if(chartDataArr[i].charType == \"bar\"){")
                            .append("\n\t\tchartSeriesObj = {")
                            .append("\n\t\t\tname: chartDataArr[i].chName,")
                            .append("\n\t\t\ttype: \"bar\",")
                            .append("\n\t\t\tdata: chartDataArr[i].charData,")
                            .append("\n\t\t\tmarkPoint: {")
                            .append("\n\t\t\t\tdata: [")
                            .append("\n\t\t\t\t\t{ type: \"max\", name: \"Max\" },")
                            .append("\n\t\t\t\t\t{ type: \"min\", name: \"Min\" },")
                            .append("\n\t\t\t\t],")
                            .append("\n\t\t\t},")
                            .append("\n\t\t\tmarkLine: {")
                            .append("\n\t\t\t\tdata: [{ type: \"average\", name: \"Avg\" }],")
                            .append("\n\t\t\t}")
                            .append("\n\t\t}")
                            .append("\n\t\tchartSeriesData.push(chartSeriesObj);")
                            .append("\n\t} else if(chartDataArr[i].charType == \"line\"){")
                            .append("\n\t\tchartSeriesObj = {")
                            .append("\n\t\t\tname: chartDataArr[i].chName,")
                            .append("\n\t\t\ttype: \"line\",");
                    if (Objects.equals(moduleChart.getViewType(), "stack")) {
                        builder.append("\n\t\t\tstack: 'Total', ");
                    }
                    if (Objects.equals(moduleChart.getViewType(), "area")) {
                        builder.append("\n\t\t\tareaStyle: {},")
                                .append("\n\t\t\temphasis: {")
                                .append("\n\t\t\t\tfocus: 'series'")
                                .append("\b\t\t\t},");
                    }
                    if (Objects.equals(moduleChart.getViewType(), "areastack")) {
                        builder.append("\n\t\t\tstack: 'Total',")
                                .append("\n\t\t\tareaStyle: {},")
                                .append("\n\t\t\temphasis: {")
                                .append("\n\t\t\t\tfocus: 'series'")
                                .append("\b\t\t\t},");
                    }
                    builder.append("\n\t\t\tdata: chartDataArr[i].charData,")
                            .append("\n\t\t\ttooltip: {")
                            .append("\n\t\tvalueFormatter: function (value) {")
                            .append("\n\t\t\treturn value + \" %\";")
                            .append("\n\t\t},")
                            .append("\n\t}")
                            .append("\n\t}")
                            .append("\n\tchartSeriesData.push(chartSeriesObj);")
                            .append("\n\t}")
                            .append("\n}")
                            .append("\ncreateChart(filedsArr, titleArr, chartSeriesData);")
                            .append("\n}")
                            .append("\n")
                            .append("\n/*")
                            .append("\n* 创建图表")
                            .append("\n* @param legendFiledsArr 图例字段数据")
                            .append("\n* @param xAxisTitleArr x轴标题数据")
                            .append("\n* @param chartSeriesData 图表系列数据")
                            .append("\n*/")
                            .append("\nfunction createChart(legendFiledsArr, xAxisTitleArr, chartSeriesData) {")
                            .append("\n\tvar dom = document.getElementById(\"")
                            .append(moduleChart.getDataAreaId())
                            .append("ChartView\");")
                            .append("\n\tvar myChart = echarts.init(dom, null, {")
                            .append("\n\t\trenderer: \"canvas\",")
                            .append("\n\t\tuseDirtyRect: false,")
                            .append("\n\t});")
                            .append("\n\tvar option = {")
                            .append("\n\t\ttooltip: {")
                            .append("\n\t\t\ttrigger: \"axis\",")
                            .append("\n\t\t},")
                            .append("\n\t\tlegend: {")
                            .append("\n\t\t\tdata: legendFiledsArr,")
                            .append("\n\t\t\tbottom: 0,")
                            .append("\n\t\t\ttextStyle: {")
                            .append("\n\t\t\t\tfontSize: 11,")
                            .append("\n\t\t\t},")
                            .append("\n\t\t},")
                            .append("\n\t\t\tcolor: [");
                    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
                        if (!StringUtils.isEmpty(relationModuleField.getFieldname()) && !StringUtils.isEmpty(relationModuleField.getFieldColor())) {
                            builder.append("'")
                                    .append(relationModuleField.getFieldColor())
                                    .append("', ");
                        }
                    }
                    builder.append("],")
                            .append("\n\t\t\ttoolbox: {")
                            .append("\n\t\t\t\tshow: true,")
                            .append("\n\t\t\t\tfeature: {")
                            .append("\n\t\t\t\t\tsaveAsImage: { show: true, title: \"保存为图片\" },")
                            .append("\n\t\t\t\t},")
                            .append("\n\t\t\t\tright: \"10%\",")
                            .append("\n\t\t\t},")
                            .append("\n\t\t\tdataZoom: [")
                            .append("\n\t\t\t\t{")
                            .append("\n\t\t\t\t\ttype: \"inside\",")
                            .append("\n\t\t\t\t\tfilterMode: \"filter\",")
                            .append("\n\t\t\t\t\theight: 15,")
                            .append("\n\t\t\t\t},")
                            .append("\n\t\t\t\t{")
                            .append("\n\t\t\t\ttype: \"slider\",")
                            .append("\n\t\t\t\theight: 15,")
                            .append("\n\t\t\t\t},")
                            .append("\n\t\t\t],")
                            .append("\n\t\t\tcalculable: true,")
                            .append("\n\t\t\txAxis: [")
                            .append("\n\t\t{")
                            .append("\n\t\t\ttype: \"category\",");
                    if (Objects.equals(moduleChart.getViewType(), "line") || Objects.equals(moduleChart.getViewType(), "area") || Objects.equals(moduleChart.getViewType(), "stack") || Objects.equals(moduleChart.getViewType(), "areastack")) {
                        builder.append("boundaryGap: false,");
                    }
                    builder.append("\n\t\t\tdata: xAxisTitleArr,")
                            .append("\n\t\t},")
                            .append("\n\t],")
                            .append("\n\tyAxis: [")
                            .append("\n\t\t{")
                            .append("\n\t\t\ttype: \"value\",")
                            .append("\n\t\t\tname: \"(")
                            .append(moduleChart.getUnitYchart())
                            .append(")\",")
                            .append("\n\t\t},")
                            .append("\n\t\t{")
                            .append("\n\t\t\ttype: \"value\",")
                            .append("\n\t\t\tname: \"(%)\",")
                            .append("\n\t\t\taxisLabel: {")
                            .append("\n\t\t\t\tformatter: \"{value} %\",")
                            .append("\n\t\t\t\t},")
                            .append("\n\t\t\t},")
                            .append("\n\t\t],")
                            .append("\n\t\tseries: chartSeriesData")
                            .append("\n\t};")
                            .append("\n\tif (option && typeof option === \"object\") {")
                            .append("\n\t\tmyChart.setOption(option);")
                            .append("\n\t}")
                            .append("\n\twindow.addEventListener(\"resize\", myChart.resize);")
                            .append("\n\t}");

                }
                builder.append("\n\tgetChartListData();")
                        .append("\n})")
                        .append("\n");


            }else if(Objects.equals(moduleChart.getViewType(),"pie") || Objects.equals(moduleChart.getViewType(),"annular")){
                builder.append("\n$(function () { ")
                       .append(this.setCharTable(relation,moduleRelation,moduleChart))
                        .append(this.setDateChar(relation,moduleRelation,moduleChart))
                        .append(this.creatChar(relation, moduleRelation , moduleChart))
                        .append("\n")
                        .append(moduleChart.getDataAreaId())
                        .append("getChartPieListData();")
                        .append("\n})")
                        .append("\n");

            }
        } catch (Exception e) {
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }


    /**
     * 获取表格和图表数据
     * @return
     */
    private String setCharTable(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleChart moduleChart){
        StringBuffer builder = new StringBuffer();
        builder.append("\n/*")
                .append("\n* 获取表格和图表数据")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleChart.getDataAreaId())
                .append("getChartPieListData(){")
                .append("\n\tvar tableFields = [");
        for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
            builder.append("\n\t\t{\"title\":\"")
                    .append(relationModuleField.getAnothername())
                    .append("\",\"field\":\"")
                    .append(relationModuleField.getFieldname())
                    .append("\"},");
        }
            builder.append("\n\t];")
                    .append("\n\tvar jsonData = {")
                    .append("\n\t\t\"entity\": {},")
                    .append("\n\t\t\"pager\": {")
                    .append("\n\t\t\t\"current\": 1,")
                    .append("\n\t\t\t\"size\": ")
                    .append(moduleChart.getPageSize())
                    .append("\n\t\t}")
                    .append("\n\t}")
                    .append("\n\tvar serviceId = '")
                    .append(moduleRelation.getServiceName())
                    .append("';")
                    .append("\n\tvar url")
                    .append(" = '/")
                    .append(moduleRelation.getEntityName())
                    .append("';")
                    .append("\n\tajax(serviceId")
                    .append(", url")
                    .append("+ '/listing', 'post', JSON.stringify(jsonData)).then(function (data) {")
                    .append("\n\t\tif (data.success) {")
                    .append("\n\t\t\t$('#")
                    .append(moduleChart.getDataAreaId())
                    .append("TableView').html('');");
                    if(Objects.equals(moduleChart.getShowTable(),1)){
                        builder.append("\n\t\t\tsetTableData('#")
                                .append(moduleChart.getDataAreaId())
                                .append("TableView', data.obj, tableFields);");
                    }
                    if(Objects.equals(moduleChart.getShowChar(),1)){
                        builder.append("\n\t\t\t ")
                                .append(moduleChart.getDataAreaId())
                                .append("chartProcessData(data.obj.records);");
                        }
                    if(Objects.equals(moduleChart.getShowTable(),0)){
                        builder.append("\n\t\t\t$('#")
                                .append(moduleChart.getDataAreaId())
                                .append("TableView').remove();");
                    }if(Objects.equals(moduleChart.getShowChar(),0)){
                        builder.append("\n\t\t\t$('#")
                               .append(moduleChart.getDataAreaId())
                               .append("ChartView').remove();");
        }
                     builder.append("\n\t\t}")
                             .append("\n\t})")
                             .append("\n}");
            return builder.toString();
}



    /**
     * 图表数据处理
     * @return
     */
    private String setDateChar(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleChart moduleChart) {
        StringBuffer builder = new StringBuffer();
        builder.append("\n/*")
                .append("\n* 图表数据处理")
                .append("\n*/")
                .append("\nfunction ")
                .append(moduleChart.getDataAreaId())
                .append("chartProcessData(dataArr){")
                .append("\n\tvar filedsArr =  [];")
                .append("\n\tvar chartSeriesObj = {};")
                .append("\n\tvar chartSeriesData = [];")
                .append("\n\tfor(var i = 0; i < dataArr.length; i++){")
                .append("\n\t\tchartSeriesObj = {")
                .append("\n\t\t\tname: dataArr[i].title,");
        if(moduleRelation.getFieldList().size()>1){
            for(RelationModuleField relationModuleField : moduleRelation.getFieldList()){
                if(!Objects.equals(relationModuleField.getAnothername(),"标题")){
                    builder.append("\n\t\t\tvalue: dataArr[i].")
                           .append(relationModuleField.getFieldname());
                    break;
                }
            }
        }else {
            builder.append("\n\t\t\tvalue: dataArr[i].addNum");
        }
                builder.append("\n\t\t}")
                .append("\n\t\tchartSeriesData.push(chartSeriesObj);")
                .append("\n\t\tfiledsArr.push(dataArr[i].title);")
                .append("\n\t}")
                .append("\n\t")
                .append(moduleChart.getDataAreaId())
                .append("createChart(filedsArr,chartSeriesData);")
                .append("\n}");

            return builder.toString();
    }

    /**
     * 创建图表
     * @return
     */
private String creatChar(RelationModuleTemplate relation,ModuleRelation moduleRelation,ModuleChart moduleChart) {
    StringBuffer builder = new StringBuffer();
    builder.append("\n/*")
            .append("\n* 创建图表")
            .append("\n*/")
            .append("\nfunction ")
            .append(moduleChart.getDataAreaId())
            .append("createChart(legendFiledsArr, chartSeriesData) {")
            .append("\n\tvar dom = document.getElementById(\"")
            .append(moduleChart.getDataAreaId())
            .append("ChartView\");")
            .append("\n\tvar myChart = echarts.init(dom, null, {")
            .append("\n\t\trenderer: \"canvas\",")
            .append("\n\t\tuseDirtyRect: false,")
            .append("\n\t});")
            .append("\n\tvar option = {")
            .append("\n\t\ttooltip: {")
            .append("\n\t\t\ttrigger: \"item\",")
            .append("\n\t\t},")
            .append("\n\t\tlegend: {")
            .append("\n\t\t\tbottom: 0")
            .append("\n\t\t},")
            .append("\n\t\tcolor: [");
    for (RelationModuleField relationModuleField : moduleRelation.getFieldList()) {
        if (!StringUtils.isEmpty(relationModuleField.getFieldname()) && !StringUtils.isEmpty(relationModuleField.getFieldColor())) {
            builder.append("'")
                    .append(relationModuleField.getFieldColor())
                    .append("', ");
        }
    }
    builder.append("],")
            .append("\n\t\ttoolbox: {")
            .append("\n\t\t\tshow: true,")
            .append("\n\t\t\tfeature: {")
            .append("\n\t\t\t\tsaveAsImage: { show: true, title: \"保存为图片\" },")
            .append("\n\t\t\t},")
            .append("\n\t\t\tright: \"10%\",")
            .append("\n\t\t},")
            .append("\n\t\tseries: [")
            .append("\n\t\t\t{")
            .append("\n\t\t\t\ttype: 'pie',");
    if(Objects.equals(moduleChart.getViewType(),"pie")){
        builder.append("\n\t\t\t\tradius: '50%',");
    }else if(Objects.equals(moduleChart.getViewType(),"annular")){
        builder.append("\n\t\t\t\tradius: ['40%', '70%'],");
    }
            builder.append("\n\t\t\t\tdata: chartSeriesData,")
            .append("\n\t\t\t\temphasis: {")
            .append("\n\t\t\t\titemStyle: {")
            .append("\n\t\t\t\t\tshadowBlur: 10,")
            .append("\n\t\t\t\t\tshadowOffsetX: 0,")
            .append("\n\t\t\t\t\tshadowColor: 'rgba(0, 0, 0, 0.5)'")
            .append("\n\t\t\t\t}")
            .append("\n\t\t\t}")
            .append("\n\t\t}")
            .append("\n\t]")
            .append("\n};")
            .append("\nif (option && typeof option === \"object\") {")
            .append("\n\tmyChart.setOption(option);")
            .append("\n}")
            .append("\nwindow.addEventListener(\"resize\", myChart.resize);")
            .append("\n}");
    return builder.toString();
    }

}

