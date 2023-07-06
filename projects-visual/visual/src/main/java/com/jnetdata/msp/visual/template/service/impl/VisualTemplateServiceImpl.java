package com.jnetdata.msp.visual.template.service.impl;

import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.template.mapper.TemplateMapper;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.visual.enums.ModuleType;
import com.jnetdata.msp.visual.modulecarousel.service.ModuleCarouselService;
import com.jnetdata.msp.visual.modulechart.service.ModuleChartService;
import com.jnetdata.msp.visual.modulecustombutton.service.ModuleCustombuttonService;
import com.jnetdata.msp.visual.moduledetail.service.ModuleDetailService;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.visual.moduleform.service.ModuleFormService;
import com.jnetdata.msp.visual.moduleformelement.mapper.ModuleFormelementMapper;
import com.jnetdata.msp.visual.moduleformelement.model.ModuleFormelement;
import com.jnetdata.msp.visual.moduleformelement.service.ModuleFormelementService;
import com.jnetdata.msp.visual.modulelogin.service.ModuleLoginService;
import com.jnetdata.msp.visual.modulemenu.mapper.ModuleMenuMapper;
import com.jnetdata.msp.visual.modulemenu.model.ModuleMenu;
import com.jnetdata.msp.visual.modulemenu.service.ModuleMenuService;
import com.jnetdata.msp.visual.modulenavigation.mapper.ModuleNavigationMapper;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import com.jnetdata.msp.visual.modulesearch.mapper.ModuleSearchMapper;
import com.jnetdata.msp.visual.modulesearch.service.ModuleSearchService;
import com.jnetdata.msp.visual.moduletable.service.ModuleTableService;
import com.jnetdata.msp.visual.moduletextlist.service.ModuleTextlistService;
import com.jnetdata.msp.visual.relationmoduletemplate.mapper.RelationModuleTemplateMapper;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import com.jnetdata.msp.visual.template.mapper.VisualTemplateMapper;
import com.jnetdata.msp.visual.template.model.VisualTemplate;
import com.jnetdata.msp.visual.template.service.VisualTemplateService;
import com.jnetdata.msp.visual.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
@Slf4j
@Service
public class VisualTemplateServiceImpl extends BaseServiceImpl<VisualTemplateMapper, VisualTemplate> implements VisualTemplateService {

    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private ProgramaMapper programaMapper;

    @Autowired
    private ConfigModelService configModelService;

    @Resource
    private ModuleMenuMapper moduleMenuMapper;

    @Resource
    private ModuleSearchMapper moduleSearchMapper;

    @Resource
    private ModuleNavigationMapper moduleNavigationMapper;

    @Resource
    private RelationModuleTemplateMapper relationModuleTemplateMapper;

    @Autowired
    private ModuleDetailService moduleDetailService;

    @Autowired
    private ModuleTextlistService moduleTextlistService;

    @Autowired
    private ModuleCarouselService moduleCarouselService;

    @Autowired
    private ModuleTableService moduleTableService;

    @Autowired
    private ModuleLoginService moduleLoginService;

    @Autowired
    private ModuleCustombuttonService moduleCustombuttonService;

    @Autowired
    private ModuleFormService moduleFormService;

    @Autowired
    private ModuleFormelementService moduleFormelementService;

    @Resource
    private ModuleFormelementMapper moduleFormelementMapper;

    @Autowired
    private ModuleSearchService moduleSearchService;

    @Autowired
    private ModuleChartService moduleChartService;

    @Autowired
    private ModuleMenuService moduleMenuService;

    @Override
    public PropertyWrapper<VisualTemplate> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .like("cssCode")
                .like("freeHtmlCode")
                .like("exteName")
                .like("htmlCode")
                .like("img")
                .eq("indClass")
                .like("jsCode")
                .eq("netType")
                .eq("outFileName")
                .like("proDes")
                .eq("screenType")
                .eq("status")
                .like("title")
                .eq("createBy")
                .eq("type")
                .eq("templateType")
                .eq("businessType")
                .eq("mjType")
                .getWrapper();
    }

    /**
     * 生成可视化模板对应的js代码和css代码
     * @param entity
     * @author chunping
     * @date 2022/11/18
     */
    @Override
    public void generateJsAndCss(VisualTemplate entity){
        //配置文件路径
        String dir = this.getPubPath(entity.getId());
        if(StringUtils.isEmpty(dir))return;
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }

        //组件自定义js和css生成文件
        this.generateCustom(entity, dir, "js");
        this.generateCustom(entity, dir, "css");

        //生成可视化模板对应的js代码
        this.generateJs(entity, dir);
        //生成可视化模板对应的css代码
        this.generateCss(entity, dir);
    }

    /**
     * 生成可视化模板对应的js代码
     */
    private void generateJs(VisualTemplate entity, String dir){
        try {
            //如果文件存在，删除文件
            String fileName = dir + File.separator + entity.getOutFileName() + ".js";
            File file = new File(fileName);
            if(file.exists()){file.delete();}
            //获取组件列表
            List<RelationModuleTemplate> moduleList = this.getModuleList(entity.getId());
            if(CollectionUtils.isEmpty(moduleList)){ return; }

            //是否第一个文字列表
            boolean isFist = true;
            //文字列表，用于生成对应js函数后缀
            int order = 1;

            //获取各个组件的js代码
            StringBuilder builder = new StringBuilder();
            for(RelationModuleTemplate relation: moduleList){
                if(ModuleType.TABLE.value().equals(relation.getModuleType())){
                    relation.setFirst(isFist);
                    relation.setOrder(order);
                    builder.append(moduleTableService.generateJs(relation));
                    isFist = false;
                    order++;
                } else if (ModuleType.CHART.value().equals(relation.getModuleType())) {
                    relation.setFirst(isFist);
                    relation.setOrder(order);
                    builder.append(moduleChartService.generateJs(relation));
                    isFist = false;
                    order++;
                } else if(ModuleType.MENU.value().equals(relation.getModuleType())){
                    builder.append(moduleMenuService.generateMenuJs(relation));
                }else if(ModuleType.DETAIL.value().equals(relation.getModuleType())){
                    builder.append(moduleDetailService.generateJs(relation));
                }else if(ModuleType.TEXTLIST.value().equals(relation.getModuleType())){
                    relation.setFirst(isFist);
                    relation.setOrder(order);
                    builder.append(moduleTextlistService.generateJs(relation));
                    isFist = false;
                    order++;
                }else if(ModuleType.CAROUSEL.value().equals(relation.getModuleType())){
                    relation.setFirst(isFist);
                    relation.setOrder(order);
                    builder.append(moduleCarouselService.generateJs(relation));
                    isFist = false;
                    order++;
                }else if(ModuleType.LOGIN.value().equals(relation.getModuleType())){
                    builder.append(moduleLoginService.generateJs(relation));
                }else if(ModuleType.CUSTOMBUTTON.value().equals(relation.getModuleType())){
                    builder.append(moduleCustombuttonService.generateJs(relation));
                }else if(ModuleType.FORM.value().equals(relation.getModuleType())){
                    builder.append(moduleFormService.generateJs(relation));
                }else if(ModuleType.SEARCH.value().equals(relation.getModuleType())){
                    relation.setFirst(isFist);
                    relation.setOrder(order);
                    builder.append(moduleSearchService.generateJs(relation));
                    isFist = false;
                    order++;
                }
            }

            //生成文件
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileUtil.inputstreamtofile(inputStream, file);
        }catch (Exception e){
            log.error("生成可视化模板对应的js代码异常：{}", e.getMessage());
        }
    }

    /**
     * 生成可视化模板对应的css代码
     */
    private void generateCss(VisualTemplate entity, String dir){
        try {
            //如果文件存在，删除文件
            String fileName = dir + File.separator + entity.getOutFileName() + ".css";
            File file = new File(fileName);
            if(file.exists()){file.delete();}

            //获取组件列表
            List<RelationModuleTemplate> moduleList = this.getModuleList(entity.getId());
            if(CollectionUtils.isEmpty(moduleList)){ return; }

            //获取各个组件的css代码
            StringBuilder builder = new StringBuilder();
            builder.append(".canEdit{border:none;}\n");
            for(RelationModuleTemplate relation: moduleList){
                ModuleObject module = null;
                if(ModuleType.TABLE.value().equals(relation.getModuleType())){
                    module = moduleTableService.getById(relation.getModuleId());
                }else if(ModuleType.MENU.value().equals(relation.getModuleType())){
                    module = moduleMenuMapper.selectById(relation.getModuleId());
                }else if(ModuleType.SEARCH.value().equals(relation.getModuleType())){
                    module = moduleSearchMapper.selectById(relation.getModuleId());
                }else if(ModuleType.DETAIL.value().equals(relation.getModuleType())){
                    module = moduleDetailService.getById(relation.getModuleId());
                }else if(ModuleType.NAVIGATION.value().equals(relation.getModuleType())){
                    module = moduleNavigationMapper.selectById(relation.getModuleId());
                }else if(ModuleType.LOGIN.value().equals(relation.getModuleType())){
                    builder.append(moduleLoginService.generateCss(relation));
                }else if(ModuleType.FORM.value().equals(relation.getModuleType())){
                    builder.append(moduleFormService.generateCss(relation));
                }
                builder.append(this.getCssCode(module));
            }
            //生成文件
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileUtil.inputstreamtofile(inputStream, file);
        }catch (Exception e){
            log.error("生成可视化模板对应的css代码异常：{}", e.getMessage());
        }
    }

    /**
     * 生成自定义文件
     * @param entity
     * @param dir
     * @param fileType
     * @author chunping
     * @date 2023/3/24
     */
    private void generateCustom(VisualTemplate entity, String dir, String fileType){
        try {
            //如果文件存在，删除文件
            String fileName = dir + File.separator + entity.getOutFileName() + "Custom." + fileType;
            File file = new File(fileName);
            if(file.exists()){file.delete();}

            //文件内容
            String fileContent = "js".equals(fileType) ? entity.getJsCode() : entity.getCssCode();
            fileContent = fileContent == null ? "" : fileContent;

            //生成文件
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileUtil.inputstreamtofile(inputStream, file);
        }catch (Exception e){
            log.error("组件自定义js和css生成文件异常：{}", e.getMessage());
        }
    }

    /**
     * 根据组件属性组织css代码
     * @param module
     * @author chunping
     * @date 2023/1/4
     */
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

    /**
     * 获取文件发布路径
     * @param visualTemplateId
     * @author chunping
     * @date 2022/12/27
     */
    private String getPubPath(Long visualTemplateId){
        try {
            //获取可视化模板对应的模板
            List<Template> templateList = templateMapper.selectList(new PropertyWrapper<>(Template.class).eq("visualTemplateId", visualTemplateId).getWrapper());
            if(CollectionUtils.isEmpty(templateList) || templateList.size() > 1){
                log.info("没有对应的模板，或对应多个模板");
                return null;
            }
            Template template = templateList.get(0);

            //获取模板对应的栏目信息
            List<Programa> programaList = programaMapper.selectList(new PropertyWrapper<>(Programa.class)
                    .eq("listtpl", template.getId())
                    .or().eq("detailtpl", template.getId()).getWrapper());
            if(CollectionUtils.isEmpty(programaList) || programaList.size() > 1){
                log.info("没有对应的栏目，或对应多个栏目");
                return null;
            }

            //发布页面根目录
            String dir_pub = configModelService.getPath("dir_pub");
            if(StringUtils.isEmpty(dir_pub)){
                dir_pub = "pub";
            }

            //本地路径配置
            //return "D:\\xiangmu\\msp\\run\\target\\fastdevapp" + File.separator+dir_pub + File.separator + "html" + programaList.get(0).getChnlDataPath();
            return configModelService.getFrontPath() + File.separator+dir_pub + File.separator + "html" + programaList.get(0).getChnlDataPath();
        }catch (Exception e){
            log.error("获取文件发布路径异常：{}", e.getMessage());
        }
        return null;

    }


    /**
     * 预览
     * @param id
     * @author chunping
     * @date 2022/11/18
     */
    @Override
    public JsonResult<String> preview(Long id){
        try {
            return this.getPublishUrl(id);
        }catch (Exception e){
            log.error("预览异常：{}，产品id：{}", e.getMessage(), id);
            return JsonResult.fail();
        }
    }

    /**
     * 获取发布页面地址
     * @param visualTemplateId 可视化模板id
     */
    private JsonResult<String> getPublishUrl(Long visualTemplateId){
        //获取可视化产品对应的模板
        List<Template> templateList = templateMapper.selectList(new PropertyWrapper<>(Template.class)
                .eq("visualTemplateId", visualTemplateId).getWrapper());
        if(CollectionUtils.isEmpty(templateList) || templateList.size() > 1){
            return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "没有对应的模板，或对应多个模板");
        }
        Template template = templateList.get(0);

        //获取模板对应的栏目信息
        List<Programa> programaList = programaMapper.selectList(new PropertyWrapper<>(Programa.class)
                .eq("listtpl", template.getId())
                .or().eq("detailtpl", template.getId())
                .or().eq("edittpl", template.getId()).getWrapper());
        if(CollectionUtils.isEmpty(programaList) || programaList.size() > 1){
            return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "没有对应的栏目，或对应多个栏目");
        }
        Programa programa = programaList.get(0);

        //返回发布地址（如果未发布，发布前段页面）
        if(StringUtils.isEmpty(programa.getListUrl())){
            return JsonResult.success(this.createHtml(programa, template.getId()));
        }else{
            return  JsonResult.success(programa.getListUrl());
        }
    }

    /**
     * 获取表单页面发布地址
     * @param formId 表单组件id
     * @author tang.chunping
     * @date 2023/06/09
     */
    @Override
    public JsonResult<String> getFormUrl(Long formId){
        try {
            ModuleForm moduleForm = moduleFormService.getById(formId);
            return this.getPublishUrl(moduleForm.getVisualTemplateId());
        }catch (Exception e){
            log.error("获取表单页面发布地址：{}，表单组件id：{}", e.getMessage(), formId);
            return JsonResult.fail();
        }
    }

    /**
     * 发布前端页面
     * @param column 栏目信息
     * @param tempId 模板id
     * @author chunping
     * @date 2022/12/18
     * @return
     */
    private String createHtml(Programa column, Long tempId){
        try {
            //设置栏目存放位置
            String os = System.getProperty("os.name");
            if(!os.toLowerCase().startsWith("win")){
                column.setChnlDataPath(column.getChnlDataPath().replace("\\","/"));
            }

            //发布页面根目录
            String dir_pub = configModelService.getPath("dir_pub");
            if(StringUtils.isEmpty(dir_pub)){
                dir_pub = "pub";
            }
            String per_pub = "/"+dir_pub+"/html";

            //获取前段页面路径
            String filePath = per_pub + column.getChnlDataPath().replace("\\","/") + "/";
            Template template = templateMapper.selectById(tempId);
            if(template != null && template.getTpltype() != null) {
                if( template.getTpltype() != 2){
                    column.setChnlOutlineTemp(tempId);
                }
                column.setListUrl(filePath + template.getOutputfilename()+"."+template.getTempext());
            }

            //生成文件
            //本地路径配置
            //String path = "D:\\xiangmu\\msp\\run\\target\\fastdevapp"+File.separator+dir_pub+File.separator+"html"+column.getChnlDataPath();
            String path = configModelService.getFrontPath()+File.separator+dir_pub+File.separator+"html"+column.getChnlDataPath();
            String tplHtml = template.getTemphtml();
            String repHtml = "<script>var columnId = '"+ column.getId() +"';</script>";
            tplHtml = tplHtml.replace("</html>",repHtml);
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }

            String filepath = path + File.separator + template.getOutputfilename()+"."+template.getTempext();
            file = new File(filepath);
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(tplHtml.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileUtil.inputstreamtofile(inputStream, file);

            programaMapper.updateById(column);

            return column.getListUrl();
        }catch (Exception e){
            log.error("发布前端页面异常：{}, 栏目id：{}", e.getMessage(), column.getId());
            return null;
        }
    }

    /**
     * 获取可视化模板关联的组件列表
     * @param visualTemplateId
     * @author chunping
     * @date 2020/11/18
     */
    private List<RelationModuleTemplate> getModuleList(Long visualTemplateId){
        try {
            return relationModuleTemplateMapper.selectList( new PropertyWrapper<>(RelationModuleTemplate.class)
                    .eq("visualTemplateId", visualTemplateId).getWrapper());
        }catch (Exception e){
            log.error("获取可视化模板关联的组件列表异常:{}, visualTemplateId:{}", e.getMessage(), visualTemplateId);
            return null;
        }
    }

    /**
     * 同步到模板并发布
     * @param template
     * @author chunping
     * @date 2022/12/06
     */
    @Override
    public JsonResult<String> syncAndPublish(Template template){
        try {
            if(StringUtils.isEmpty(template.getVisualTemplateId())){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "可视化模板ID(visualTemplateId)不能为空！");
            }

            //获取可视化模板对应的模板信息
            List<Template> templateList = templateMapper.selectList(new PropertyWrapper<>(Template.class)
                    .eq("visualTemplateId", template.getVisualTemplateId()).getWrapper());
            if(CollectionUtils.isEmpty(templateList) || templateList.size() > 1){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "没有对应的模板，或对应多个模板");
            }

            //更新模板
            template.setId(templateList.get(0).getId());
            templateMapper.updateById(template);

            //获取模板对应的栏目信息
            List<Programa> programaList = programaMapper.selectList(new PropertyWrapper<>(Programa.class)
                    .eq("listtpl", template.getId())
                    .or().eq("detailtpl", template.getId()).getWrapper());
            if(CollectionUtils.isEmpty(programaList) || programaList.size() > 1){
                return JsonResult.fail(HttpStatus.BAD_REQUEST.value() + "", "没有对应的栏目，或对应多个栏目");
            }

            //发布前端页面
            this.createHtml(programaList.get(0), template.getId());
            return JsonResult.success();
        }catch (Exception e){
            log.error("同步到模板并发布异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }
}
