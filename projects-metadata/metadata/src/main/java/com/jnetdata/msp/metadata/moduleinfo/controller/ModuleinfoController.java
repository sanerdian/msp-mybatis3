package com.jnetdata.msp.metadata.moduleinfo.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.metadata.moduleinfo.model.ModuleView;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleViewService;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleinfoService;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import com.jnetdata.msp.metadata.viewlog.service.MetaDataOperatorService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import com.jnetdata.msp.swagger.model.Swaggerconfiguration;
import com.jnetdata.msp.swagger.service.SwaggerconfigurationService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.scene.control.Tab;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/moduleinfo")
@ApiModel(value = "模块管理(ModuleinfoController)" , description = "模块管理")
public class ModuleinfoController extends BaseController<Long , Moduleinfo> {

    @Autowired
    private ModuleinfoService moduleinfoService;
    @Autowired
    private TableinfoService tableinfoService;
    @Autowired
    private ModuleViewService moduleViewService;
    @Autowired
//    private ContentLogClient contentLogService;
    private ContentLogService contentLogService;

    @Autowired
    private MetaDataOperatorService metaDataOperatorService;

    @Autowired
    private SwaggerconfigurationService swaggerconfigurationService;


    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "获取模块列表")
    public JsonResult<Pager<Moduleinfo>> listByPermission(@RequestBody BaseVo<Moduleinfo> vo){
        return renderSuccess(getService().pageByPermission(vo));
    }

    @PostMapping(value = "/add")
    @ResponseBody
    @ApiOperation(value = "新建模块")
    public JsonResult<EntityId<Long>> add(@RequestBody Moduleinfo entity){
        List<Moduleinfo> list = getService().list(new PropertyWrapper<>(Moduleinfo.class).eq("englishname" , entity.getEnglishname()));
        JsonResult<EntityId<Long>> result = null;
        val user = SessionUser.getCurrentUser();

        if(list.size() > 0){
            result = renderError("模块已存在！");
        }else{
            Integer order = getService().getmaxorder();
            entity.setModuleorder((order==null?0:order) + 1);
            result = doAdd(getService(),entity);
        }
        String packageName = "com.jnetdata.msp." + entity.getEnglishname();
        String beanName = "create" + entity.getEnglishname().substring(0, 1).toUpperCase()+entity.getEnglishname().substring(1)
                + "Api";
        Swaggerconfiguration swaggerconfiguration = new Swaggerconfiguration();
        swaggerconfiguration.setBasepackagename(packageName);
        swaggerconfiguration.setGroupname(entity.getEnglishname());
        swaggerconfiguration.setBeanname(beanName);
        swaggerconfigurationService.insert(swaggerconfiguration);
        contentLogService.addLog("新建模块","元数据管理","元数据模块管理",true);
        return result;
    }

    @ApiOperation(value = "根据id查询", notes="查看模块")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Moduleinfo> getById(@ApiParam("模块id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "修改", notes="修改模块")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("模块id") @PathVariable("id") Long id,
                                       @RequestBody Moduleinfo entity) {
        int count = getService().count(new PropertyWrapper<>(Moduleinfo.class).eq("englishname",entity.getEnglishname()).ne("id",id));
        if(count > 0){
            contentLogService.addLog("修改模块","元数据管理","元数据模块管理",false);
            return renderError("模块已存在！");
        }else{
            contentLogService.addLog("修改模块","元数据管理","元数据模块管理",true);
            return doUpdateById(getService(), id, entity);
        }
    }

    @PostMapping(value = "/addTable/{id}")
    @ResponseBody
    @ApiOperation(value = "引入元数据表")
    public JsonResult introduceTable(@ApiParam("模块id") @PathVariable("id") Long id , @RequestBody Long[] ids){
        List<Tableinfo> list = new ArrayList<>();
        val user = SessionUser.getCurrentUser();
        for(Long tid : ids){
            Tableinfo t = new Tableinfo();
            t.setId(tid);
            t.setOwnerid(id);
            t.setIntroducetime(new Date());
            t.setIntroduceuser(user.getName());
            list.add(t);
        }
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("引入元数据表");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据模块管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        tableinfoService.updateBatchById(list);
        return renderSuccess();
    }

    @PostMapping(value = "/addEsTable/{id}")
    @ResponseBody
    @ApiOperation(value = "引入元数据表")
    public JsonResult addEsTable(@ApiParam("模块id") @PathVariable("id") Long id , @RequestBody String[] indexs){
        List<Tableinfo> list = new ArrayList<>();
        List<String> toAddIndexs = new ArrayList<>();
        for (String index : indexs) {
            toAddIndexs.add(index.toUpperCase(Locale.ROOT));
        }
        List<Tableinfo> tables = tableinfoService.list(new PropertyWrapper<>(Tableinfo.class).in("concat(dbsourceid,':',tablename)", toAddIndexs));
        Map<String, Long> collect = tables.stream().collect(Collectors.toMap(m -> m.getDbsourceid() + ":" + m.getTablename(), m -> m.getId()));
        for (String index : toAddIndexs) {
            String[] split = index.split(":");
            Tableinfo tableinfo = new Tableinfo();
            if(collect.containsKey(index)) tableinfo.setId(collect.get(index));
            tableinfo.setOwnerid(id);
            tableinfo.setTablename(split[1]);
            tableinfo.setTabletype("es");
            tableinfo.setDbsourceid(Long.valueOf(split[0]));
            list.add(tableinfo);
        }
        tableinfoService.insertOrUpdateBatch(list);
        return renderSuccess();
    }

    @PostMapping(value = "/removeTable")
    @ResponseBody
    @ApiOperation(value = "移除所选元数据表")
    public JsonResult removeTable(@RequestBody Long[] ids){
        List<Tableinfo> list = new ArrayList<>();
        for(Long id : ids){
            Tableinfo t = new Tableinfo();
            t.setId(id);
            t.setOwnerid(0L);
            list.add(t);
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("移除元数据表");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据模块管理");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        tableinfoService.updateBatchById(list);
        return renderSuccess();
    }

    @PostMapping(value = "delByIds")
    @ResponseBody
    @ApiOperation(value = "批量删除" , notes = "根据多个id删除对应模块数据")
    public JsonResult deleteBatchIds(@RequestBody Long[] ids){
        int count = 0;
        List<Long> list = Arrays.stream(ids).collect(Collectors.toList());
        List<Tableinfo> tlist = tableinfoService.list(new PropertyWrapper<>(Tableinfo.class).in("ownerid",list));
        List<Tableinfo> listt = new ArrayList<>();
        for(int i = 0 ; i < tlist.size() ; i++){
            Tableinfo t = new Tableinfo();
            t.setId(tlist.get(i).getId());
            t.setOwnerid(0L);
            listt.add(t);
        }
        //删除相关视图数据
        List<ModuleView> mlist = moduleViewService.list(new PropertyWrapper<>(ModuleView.class).in("moduleinfoid",list));
        for(int i = 0 ; i < mlist.size() ; i++){
            moduleViewService.deleteById(mlist.get(i).getId());
        }
        if(listt.size() > 0) {
            tableinfoService.updateBatchById(listt);
        }
        for(Long id : ids){
            JsonResult<Moduleinfo> m = getById(id);
            swaggerconfigurationService.delete(new PropertyWrapper<>(Swaggerconfiguration.class).eq("groupname" , m.getObj().getEnglishname()));
            JsonResult<Void> result = deleteAll(id);
            if(result.isSuccess()) {
                count++;
            }
        }
        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("新建模块");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("元数据模块管理");

        if(count > 0){
            contentLog.setResult("成功");
            contentLog.setCreateTime(new Date());
            contentLogService.insert(contentLog);
            return renderSuccess("成功删除"+count+"个模块");
        }else{
            contentLog.setResult("失败");
            contentLog.setCreateTime(new Date());
            contentLogService.insert(contentLog);
            return renderError("删除失败！");
        }
    }

    private JsonResult<Void> deleteAll(Long id){
        JsonResult<Moduleinfo> json = doGetById(getService(),id);
        JsonResult<Void> result = null;
        if(json.isSuccess()){
            Moduleinfo obj = json.getObj();
            String moduleinfoname = obj.getModulename();
            result = doDeleteById(getService(),id);
        }else{
            result = renderError("模块不存在！");
        }
        return result;
    }
    @ApiOperation(value = "排序", notes = "按照模块id进行排序")
    @GetMapping(value = "/sort/{id}/{sort}")
    @ResponseBody
    public JsonResult<Void> sort(@ApiParam("模块id") @PathVariable("id") Long id, @ApiParam("排序") @PathVariable("sort") int sort) {
        getService().updateorder(id,sort);
        return renderSuccess();
    }

    private ModuleinfoService getService(){
        return moduleinfoService;
    }

    private void addXml(File file,String m) {
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //②获取一个与磁盘文件关联的非空Document对象
            Document doc = newDocumentBuilder.parse(file);
            //③通过文档对象获得该文档对象的根节点
            Element root = doc.getDocumentElement();

            //创建一个新的person节点
            Element modules0 = doc.createElement("modules");
            //创建person的几个子节点
            Element module0 = doc.createElement("module");

            //给子节点设置值
            module0.setTextContent(m);
            //将子节点追加到person
            modules0.appendChild(module0);
            //将person追加到根节点
            root.appendChild(modules0);

            //注意：XML文件是被加载到内存中 修改也是在内存中 ==》因此需要将内存中的数据同步到磁盘中
            /*
             * static TransformerFactory newInstance():获取 TransformerFactory 的新实例。
             * abstract  Transformer newTransformer():创建执行从 Source 到 Result 的复制的新 Transformer。
             * abstract  void transform(Source xmlSource, Result outputTarget):将 XML Source 转换为 Result。
             */
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //DOMSource source = new DOMSource(doc);
            Source source = new DOMSource(doc);
            //StreamResult result = new StreamResult();
            Result result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
