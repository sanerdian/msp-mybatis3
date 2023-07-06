package com.jnetdata.msp.visual.modulelogin.service.impl;

import com.alibaba.druid.util.jdbc.ConnectionBase;
import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.enums.login.LoginType;
import com.jnetdata.msp.visual.modulelogin.mapper.ModuleLoginMapper;
import com.jnetdata.msp.visual.modulelogin.model.ModuleLogin;
import com.jnetdata.msp.visual.modulelogin.service.ModuleLoginService;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import com.jnetdata.msp.visual.relationmodulefield.service.RelationModuleFieldService;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.relationmoduletemplate.service.RelationModuleTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ModuleLoginServiceImpl extends BaseServiceImpl<ModuleLoginMapper, ModuleLogin> implements ModuleLoginService {

    @Autowired
    private RelationModuleFieldService relationModuleFieldService;

    @Autowired
    private RelationModuleTemplateService relationModuleTemplateService;

    @Resource
    private ProgramaMapper programaMapper;

    @Override
    public PropertyWrapper<ModuleLogin> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .getWrapper();
    }

    /**
     * 新增登录组件
     */
    @Override
    public JsonResult<EntityId<Long>> insertEntity(ModuleLogin entity){
        try {
            if(ObjectUtils.isEmpty(entity.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板id（visualTemplateId）不能为空");
            }
            entity.setCreateTime(new Date());
            this.insert(entity);
            relationModuleFieldService.saveRelation(entity.getFieldList(), entity.getId(), ModuleType.LOGIN.value());
            relationModuleTemplateService.saveRelation(entity.getId(), ModuleType.LOGIN.value(), entity.getVisualTemplateId());
            return JsonResult.success(new EntityIdVo(entity.getId()));
        }catch (Exception e){
            log.error("新增登录组件异常：{}", e.getMessage());
            return JsonResult.fail("新增失败");
        }
    }

    /**
     * 删除登录组件
     */
    @Override
    public JsonResult<Void> deleteEntity(Long id){
        try {
            this.deleteById(id);
            relationModuleFieldService.deleteRelation(id, ModuleType.LOGIN.value());
            relationModuleTemplateService.deleteRelation(id, ModuleType.LOGIN.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("删除登录组件异常：{}", e.getMessage());
            return JsonResult.fail("删除失败");
        }
    }

    /**
     * 修改登录组件
     */
    @Override
    public JsonResult<Void> updateEntity(Long id, ModuleLogin entity){
        try {
            entity.setId(id);
            entity.setUpdateTime(new Date());
            this.updateById(entity);
            relationModuleFieldService.deleteRelation(id, ModuleType.LOGIN.value());
            relationModuleFieldService.saveRelation(entity.getFieldList(), id, ModuleType.LOGIN.value());
            return JsonResult.success();
        }catch (Exception e){
            log.error("修改登录组件异常：{}", e.getMessage());
            return JsonResult.fail("更新失败");
        }
    }

    /**
     * 查询登录组件
     */
    @Override
    public JsonResult<ModuleLogin> getEntity(Long id){
        try {
            ModuleLogin entity = this.getById(id);
            if(ObjectUtils.isEmpty(entity)){
                return JsonResult.fail("对象不存在");
            }

            List<RelationModuleField> list = relationModuleFieldService.getRelation(id, ModuleType.LOGIN.value());
            entity.setFieldList(list);

            return JsonResult.success(entity);
        }catch (Exception e){
            log.error("查询登录组件异常：{}", e.getMessage());
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
            ModuleLogin moduleLogin = this.getById(relation.getModuleId());

            if(!StringUtils.isEmpty(moduleLogin.getStyleName())
                    && !StringUtils.isEmpty(moduleLogin.getLoginType())
                    && !StringUtils.isEmpty(moduleLogin.getStyleName())){


                //从数据库获取登录类型
                String[] typeArray = moduleLogin.getLoginType().split(",");
                List<String> typeList = new ArrayList<>();
                for (int i = 0; i < typeArray.length; i++) {
                    if(!StringUtils.isEmpty(typeArray[i])){
                        typeList.add(typeArray[i]);
                    }
                }

                //判断配置了哪些登录类型
                boolean accountFlag = false;//是否选择账号登录
                boolean phoneFlag = false;//是否选择手机号登录
                boolean appFlag = false;//是否选择扫码登录
                if(typeList.contains(LoginType.ACCOUNT.getCode())){
                    accountFlag = true;
                }
                if(typeList.contains(LoginType.PHONE.getCode())){
                    phoneFlag = true;
                }
                if(typeList.contains(LoginType.APP.getCode())){
                    appFlag = true;
                }
                //根据配置隐藏或显示对应的标签
                if(accountFlag&&phoneFlag&&appFlag){
                    return builder.toString();
                }else {
                    //只显示用户登录
                    if(accountFlag==true && appFlag == false && phoneFlag == false){
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_phone,")
                                .append("#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_phone').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_code,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_code').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').addClass('layui-show');")
                                .append("\n")
                                .append("\nfunction loginGotoLink(){")
                                .append("\n\twindow.location.href =\"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl())
                                    .append("\";")
                                    .append("\n}");
                        }else {
                            builder.append(moduleLogin.getOuterUrl())
                                    .append("\";")
                                    .append("\n}");
                        }

                    }
                    if(accountFlag==false && appFlag == false && phoneFlag == true){
                        //只显示手机号登陆
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user,")
                                .append("#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_code,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_code').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_phone').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_phone').addClass('layui-show');")
                                .append("\n")
                                .append("\nfunction loginGotoLink(){")
                                .append("\n\twindow.location.href =\"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl())
                                    .append("\";")
                                    .append("\n}");
                        }else {
                            builder.append(moduleLogin.getOuterUrl())
                                    .append("\";")
                                    .append("\n}");
                        }

                    }
                    if(accountFlag==false && appFlag == true && phoneFlag == false){
                        //只显示APP登陆
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user,")
                                .append("#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_phone,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_phone').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_code').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_code').addClass('layui-show');")
                                .append("\n")
                                //扫码登陆跳转方法
                                .append("\nfunction dologin() {")
                                .append("\n\tajax(service_prefix.member, \"/home/loginByQR/\" + qrid, \"get\", {}).then(function (data) {")
                                .append("\n\t\tif (data.success) {")
                                .append("\n\t\t\tlocalStorage.setItem(\"state\", true);")
                                .append("\n\t\t\tstate = true;")
                                .append("\n\t\t\tdocument.cookie = \"username=\" + data.obj.truename")
                                .append("\n\t\t\tdocument.cookie = \"userid=\" + data.obj.id")
                                .append("\n\t\t\twindow.location.href = \"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                             builder.append(moduleLogin.getInnerUrl());
                        }else {
                            builder.append(moduleLogin.getOuterUrl());
                        }
                                builder.append("\";")
                                .append("\n\t\t} else {")
                                .append("\n\t\t\tconsole.log(res.msg)")
                                .append("\n\t\t}")
                                .append("\n\t})")
                                .append("\n}");
                    }
                    if(accountFlag==true && appFlag == false && phoneFlag == true){
                        //账号和手机号登录
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_code,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_code').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').addClass('layui-show');")
                                .append("\n")
                                .append("\nfunction loginGotoLink(){")
                                .append("\n\twindow.location.href =\"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl())
                                    .append("\";")
                                    .append("\n}");
                        }else {
                            builder.append(moduleLogin.getOuterUrl())
                                    .append("\";")
                                    .append("\n}");
                        }
                    }
                    if(accountFlag==true && appFlag == true && phoneFlag == false){
                        //账号和扫码登录
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_phone,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_phone').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').addClass('layui-show');")
                                .append("\n")
                                .append("\nfunction loginGotoLink(){")
                                .append("\n\twindow.location.href =\"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl())
                                    .append("\";")
                                    .append("\n}");
                        }else {
                            builder.append(moduleLogin.getOuterUrl())
                                    .append("\";")
                                    .append("\n}");
                        }

                        //扫码登陆跳转方法
                                builder.append("\nfunction dologin() {")
                                .append("\n\tajax(service_prefix.member, \"/home/loginByQR/\" + qrid, \"get\", {}).then(function (data) {")
                                .append("\n\t\tif (data.success) {")
                                .append("\n\t\t\tlocalStorage.setItem(\"state\", true);")
                                .append("\n\t\t\tstate = true;")
                                .append("\n\t\t\tdocument.cookie = \"username=\" + data.obj.truename")
                                .append("\n\t\t\tdocument.cookie = \"userid=\" + data.obj.id")
                                .append("\n\t\t\twindow.location.href = \"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl());
                        }else {
                            builder.append(moduleLogin.getOuterUrl());
                        }
                                builder.append("\";")
                                .append("\n\t\t} else {")
                                .append("\n\t\t\tconsole.log(res.msg)")
                                .append("\n\t\t}")
                                .append("\n\t})")
                                .append("\n}");
                    }
                    if(accountFlag==false && appFlag == true && phoneFlag == true){
                        //手机号和扫码登录
                        builder.append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_user,#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_userName').remove();")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_title_phone').addClass('layui-this');")
                                .append("\n$('#")
                                .append(moduleLogin.getDataAreaId())
                                .append(" .visual_login_phone').addClass('layui-show');")
                                .append("\n")
                                .append("\nfunction loginGotoLink(){")
                                .append("\n\twindow.location.href =\"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl())
                                    .append("\";")
                                    .append("\n}");
                        }else {
                            builder.append(moduleLogin.getOuterUrl())
                                    .append("\";")
                                    .append("\n}");
                        }
                        //扫码登陆跳转方法
                               builder.append("\nfunction dologin() {")
                                .append("\n\tajax(service_prefix.member, \"/home/loginByQR/\" + qrid, \"get\", {}).then(function (data) {")
                                .append("\n\t\tif (data.success) {")
                                .append("\n\t\t\tlocalStorage.setItem(\"state\", true);")
                                .append("\n\t\t\tstate = true;")
                                .append("\n\t\t\tdocument.cookie = \"username=\" + data.obj.truename")
                                .append("\n\t\t\tdocument.cookie = \"userid=\" + data.obj.id")
                                .append("\n\t\t\twindow.location.href = \"");
                        if(Objects.equals(moduleLogin.getLoginSkip(),"1")){
                            builder.append(moduleLogin.getInnerUrl());
                        }else {
                            builder.append(moduleLogin.getOuterUrl());
                        }
                               builder.append("\";")
                                .append("\n\t\t} else {")
                                .append("\n\t\t\tconsole.log(res.msg)")
                                .append("\n\t\t}")
                                .append("\n\t})")
                                .append("\n}");
                    }
                }
            }
            return builder.toString();
        }catch (Exception e){
            log.error("生成对应的js代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

    /**
     * 登录组件生成CSS代码
     * @param relation
     * @return
     */
    @Override
    public String generateCss(RelationModuleTemplate relation){
        StringBuilder builder = new StringBuilder();
        try {
            //获取组件详细信息
            ModuleLogin moduleLogin = this.getById(relation.getModuleId());
            builder.append("body{background:url(").append(moduleLogin.getBackgroundImage()).append(") no-repeat;background-size:cover;}");
                if (moduleLogin.getLoginPosition().isEmpty()) {
                    //拼接CSS样式代码
                    builder.append(".")
                            .append(moduleLogin.getDataAreaId())
                            .append(".visual_login{position:absolute;top:200px;right:200px;}");
                } else {
                    //拼接CSS样式代码
                    if (moduleLogin.getLoginPosition().equals("center")) {
                        builder.append("\n#")
                                .append(moduleLogin.getDataAreaId()).append(" .visual_login{")
                                .append("width:").append(moduleLogin.getLoginWidth())
                                .append("px;position:absolute;top:")
                                .append(moduleLogin.getLoginHeight()).append("px;")
                                .append("left:50%").append(";margin:0 0 0 -")
                                .append(moduleLogin.getLoginWidth()/2)
                                .append("px;}");
                    } else {
                        builder.append("\n#")
                                .append(moduleLogin.getDataAreaId()).append(" .visual_login{")
                                .append("width:")
                                .append(moduleLogin.getLoginWidth())
                                .append("px;position:absolute;top:")
                                .append(moduleLogin.getLoginHeight()).append("px;")
                                .append(moduleLogin.getLoginPosition())
                                .append(":")
                                .append(moduleLogin.getLoginMargin())
                                .append("px;}");
                    }
                }
            ModuleObject module = null;
            module = baseMapper.selectById(relation.getModuleId());
            builder.append(this.getCssCode(module));
            return builder.toString();
        }catch (Exception e){
            log.error("生成对应的css代码异常：{}", e.getMessage());
        }
        return builder.toString();
    }

    private String getCssCode(ModuleObject module){
        if(ObjectUtils.isEmpty(module) || StringUtils.isEmpty(module.getStyleName())){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(".").append(module.getStyleName()).append(",\n")
                .append(".").append(module.getStyleName()).append(" a,\n")
                .append(".").append(module.getStyleName()).append(" span,\n")
                .append(".").append(module.getStyleName()).append(" th,\n")
                .append(".").append(module.getStyleName()).append(" td,\n")
                .append(".").append(module.getStyleName()).append(" select,\n")
                .append(".").append(module.getStyleName()).append(" input,\n")
                .append(".").append(module.getStyleName()).append(" button,\n")
                .append(".").append(module.getStyleName()).append(" ul li a,\n")
                .append(".").append(module.getStyleName()).append(" .layui-laypage .layui-laypage-skip{\n");
        if(!StringUtils.isEmpty(module.getFontFamily())){
            builder.append("\tfont-family:\"").append(module.getFontFamily()).append("\";\n");
        }
        if(!StringUtils.isEmpty(module.getFontSize())){
            builder.append("\tfont-size:").append(module.getFontSize()).append("px;\n");
        }
        if(!StringUtils.isEmpty(module.getFontColor())){
            builder.append("\tcolor:").append(module.getFontColor()).append(";\n");
        }
        builder.append("}\n");
        return builder.toString();
    }


}