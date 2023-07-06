package com.jnetdata.msp.visual.modulemenu.service.impl;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulemenu.mapper.ModuleMenuMapper;
import com.jnetdata.msp.visual.modulemenu.model.ModuleMenu;
import com.jnetdata.msp.visual.modulemenu.service.ModuleMenuService;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ModuleMenuServiceImpl extends BaseServiceImpl<ModuleMenuMapper, ModuleMenu> implements ModuleMenuService {

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ModuleMenuMapper moduleMenuMapper;


    @Override
    public PropertyWrapper<ModuleMenu> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增菜单组件
     * @author chunping
     * @date 2022/12/12
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleMenu entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.MENU.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增菜单组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除菜单组件
     * @author chunping
     * @date 2022/12/12
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleTemplateService.deleteRelation(id, ModuleType.MENU.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除菜单组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 生成菜单组件相关js
     * @param relation
     * @author chunping
     * @date 2022/12/13
     */
    public String generateMenuJs(RelationModuleTemplate relation) {
        StringBuilder menuJs = new StringBuilder();
        try {
            //获取组件详细信息
            ModuleMenu moduleMenu = moduleMenuMapper.selectById(relation.getModuleId());
//            if (ObjectUtils.isEmpty(moduleMenu.getSiteId()) && StringUtils.isEmpty(moduleMenu.getMenuId())
//                    && StringUtils.isEmpty(moduleMenu.getDataTplId())
//                    && StringUtils.isEmpty(moduleMenu.getDataAreaId())) {
//                return "";
//            }
            if (moduleMenu.getChooseMenu().equals("column")) {
                menuJs.append("\nfunction getMetadataColumn(){")
                        .append("\n\tvar params = {")
                        .append("\n\t\t\"entity\": {")
                        .append("\n\t\t\tstatus: 0,")
                        .append("\n\t\t\tstop: 0,")
                        .append("\n\t\t\tparentId: 0,")
                        .append("\n\t\t\tsiteId:\"").append(moduleMenu.getSiteId()).append("\"")
                        .append("\n\t\t")
                        .append("\n\t\t},")
                        .append("\n\t\t\"pager\": {")
                        .append("\n\t\t\tcurrent: 1,")
                        .append("\n\t\t\tsize: 100,")
                        .append("\n\t\t\tsortProps: [")
                        .append("\n\t\t\t\t{key: \"parentId\", value: true},")
                        .append("\n\t\t\t\t{key: \"chnlOrder\", value: true}")
                        .append("\n\t\t\t]")
                        .append("\n\t\t}")
                        .append("\n\t};")
                        .append("\n\tajax(service_prefix.manage,\"/programa/list\", \"post\", JSON.stringify(params)).then(function(data){")
                        .append("\n\t\tif(data.success){")
                        .append("\n\t\tvar html = '';")
                        .append("\n\t\tvar dataArr = data.obj.records;")
                        .append("\n\tfor(var i = 0; i < dataArr.length; i++){")
                        .append("\n\thtml += '<li><a href=\"' + dataArr[i].listUrl + '\">' + dataArr[i].name + '</a></li>';")
                        .append("}")
                        .append("\n\t\t$('#")
                        .append(moduleMenu.getDataAreaId())
                        .append("').html(html);")
                        .append("\n\t}else{")
                        .append("\n\t\tconsole.log(data.msg);")
                        .append("\n\t}")
                        .append("\n});")
                        .append("\n}")
                        .append("\ngetMetadataColumn();");

            } else if (moduleMenu.getChooseMenu().equals("menu")) {
                menuJs.append("\nfunction getMenuColumn(){")
                        .append("\n\tvar organId = \"")
                        .append(moduleMenu.getOrganId())
                        .append("\";")
                        .append("\n\tvar id = \"")
                        .append(moduleMenu.getMenuId())
                        .append("\";")
                        .append("\n\tajax(service_prefix.member,\"/menu/tree?type=2\", \"get\", {}).then(function(data){")
                        .append("\n\t\tif(data.success){")
                        .append("\n\t\t\tvar html = '';")
                        .append("\n\t\t\tvar nodes = data.obj;")
                        .append("\n\t\t\tvar newNodesArr = [];")
                        .append("\n\t\t\tvar newNodesAllArr = [];")
                        .append("\n\t\t\tvar newNodesChildrenArr = [];")
                        .append("\n\t\t\tvar newNodesChildrenObj = {};")
                        .append("\n\t\t\tfor(var o of nodes){")
                        .append("\n\t\t\t\tif(o.parentId == 0 && o.id == organId){")
                        .append("\n\t\t\t\t\tnewNodesArr.push(o);")
                        .append("\n\t\t\t\t}else{")
                        .append("\n\t\t\t\t\tnewNodesChildrenArr.push(o);")
                        .append("\n\t\t\t\t}")
                        .append("\n\t\t\t}")
                        .append("\n\t\t\tfor(var i = 0; i < newNodesArr.length; i++){")
                        .append("\n\t\t\t\tvar newNodesChildrenListArr = [];")
                        .append("\n\t\t\t\tfor(var j = 0; j < newNodesChildrenArr.length; j++){")
                        .append("\n\t\t\t\t\tif(newNodesArr[i].id == newNodesChildrenArr[j].companyId){")
                        .append("\n\t\t\t\t\t\tnewNodesChildrenObj = {")
                        .append("\n\t\t\t\t\t\t\t\"parentId\": newNodesChildrenArr[j].parentId,")
                        .append("\n\t\t\t\t\t\t\t\"id\": newNodesChildrenArr[j].id,")
                        .append("\n\t\t\t\t\t\t\t\"isSite\": newNodesChildrenArr[j].isSite,")
                        .append("\n\t\t\t\t\t\t\t\"name\": newNodesChildrenArr[j].name,")
                        .append("\n\t\t\t\t\t\t\t\"children\": newNodesChildrenArr[j].children")
                        .append("\n\t\t\t\t\t\t}")
                        .append("\n\t\t\t\t\t\tnewNodesChildrenListArr.push(newNodesChildrenObj);")
                        .append("\n\t\t\t\t\t}")
                        .append("\n\t\t\t\t}")
                        .append("\n\t\t\t\tvar newNodesObj = {")
                        .append("\n\t\t\t\t\t\"parentId\": newNodesArr[i].parentId,")
                        .append("\n\t\t\t\t\t\"id\": newNodesArr[i].id,")
                        .append("\n\t\t\t\t\t\"isSite\": newNodesArr[i].isSite,")
                        .append("\n\t\t\t\t\t\"name\": newNodesArr[i].name,")
                        .append("\n\t\t\t\t\t\"children\": newNodesChildrenListArr")
                        .append("\n\t\t\t\t};")
                        .append("\n\t\t\t\tnewNodesAllArr.push(newNodesObj);")
                        .append("\n\t\t\t}")
                        .append("\n\t\t\tvar html = getMenuHtml(id, newNodesAllArr);")
                        .append("\n\t\t\t$('#")
                        .append(moduleMenu.getDataAreaId())
                        .append("').html(html);")
                        .append("\n\t\t\tvar layFilter = $(\"")
                        .append(moduleMenu.getDataAreaId())
                        .append("\").attr('lay-filter');")
                        .append("\n\t\t\telement.render('nav', layFilter);")
                        .append("\n\t\t}else{")
                        .append("\n\t\t\tconsole.log(data.msg);")
                        .append("\n\t\t}")
                        .append("\n\t});")
                        .append("\n}")
                        .append("\nfunction getMenuHtml(id, dataArr){")
                        .append("\n\tdataArr = dataArr[0].children;")
                        .append("\n\tvar html = '<ul class=\"layui-nav layui-nav-tree\">';")
                        .append("\n\tfor(var i = 0; i < dataArr.length; i++){")
                        .append("\n\t\tif(dataArr[i].id == id){")
                        .append("\n\t\t\tfor(var j = 0; j < dataArr[i].children.length; j++){")
                        .append("\n\t\t\t\thtml += '<li class=\"layui-nav-item\">' +")
                        .append("\n\t\t\t\t\t'<a href=\"javascript:;\">' + dataArr[i].children[j].name + '</a>';");
                        if("2".equals(moduleMenu.getChooseLevel())){
                            menuJs.append("\n\t\t\t\t if(dataArr[i].children[j].children){")
                                  .append("\n\t\t\t\t\thtml += '<ul class=\"layui-nav-child\">';")
                                  .append("\n\t\t\t\t\tfor(var k = 0; k < dataArr[i].children[j].children.length; k++){")
                                  .append("\n\t\t\t\t\t\thtml += '<li>' + '<a href=\"javascript:;\">' + ")
                                  .append("\n\t\t\t\t\t\t\tdataArr[i].children[j].children[k].name + '</a>';")
                                  .append("\n\t\t\t\t\t\thtml += '<ul class=\"layui-nav-child\">';");
                        }
                            if(Objects.equals(moduleMenu.getChooseLevel(),"3")){
                            menuJs.append("\n\t\t\t\t\t\tif(dataArr[i].children[j].children[k].children){")
                                  .append("\n\t\t\t\t\t\t\tconsole.log('a,',dataArr[i].children[j].children[k].children)")
                                  .append("\n\t\t\t\t\t\t\tfor(var m = 0; m < dataArr[i].children[j].children[k].children.length; m++){")
                                  .append("\n\t\t\t\t\t\t\t\thtml += '<li><a href=\"' + dataArr[i].children[j].children[k].children[m].url + '\">' + dataArr[i].children[j].children[k].children[m].name + '</a></li>';")
                                  .append("\n\t\t\t\t\t\t\t}")
                                  .append("\n\t\t\t\t\t\t}");
                            }
                            if(Objects.equals(moduleMenu.getChooseLevel(),"2")){
                                menuJs.append("\n\t\t\t\t\t\thtml += '</ul>';")
                                        .append("\n\t\t\t\t\t\thtml += '</li>';")
                                        .append("\n\t\t\t\t\t}")
                                        .append("\n\t\t\t\t\thtml += '</ul>';")
                                        .append("\n\t\t\t\t}");
                            }


                menuJs.append("\n\t\t\t\thtml +='</li>';")
                        .append("\n\t\t\t}")
                        .append("\n\t\t}")
                        .append("\n\t}")
                        .append("\n\treturn html;")
                        .append("\n}")
                        .append("\ngetMenuColumn();");
            }
        } catch (Exception e) {
            log.error("生成菜单组件相关js异常：{}", e.getMessage());
        }
        return menuJs.toString();
    }
}