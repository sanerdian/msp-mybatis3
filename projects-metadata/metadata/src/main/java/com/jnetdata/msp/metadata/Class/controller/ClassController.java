package com.jnetdata.msp.metadata.Class.controller;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.j_log.service.JLogService;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.metadata.Class.ClassUtil.ClassExcelUtil;
import com.jnetdata.msp.metadata.Class.model.Class;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import com.jnetdata.msp.metadata.encoding.model.Encodingmodel;
import com.jnetdata.msp.metadata.encoding.service.EncodingService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/metadata/class")
@Api(value = "分类管理(ClassController)", description = "分类管理")
public class ClassController extends BaseController<Long, Class> {

    @Autowired
    private EncodingService encodingService;
    @Autowired
    private ClassService classService;

    @Autowired
    private ContentLogClient contentLogService;
    @Autowired
    private JLogService jLogService;
    @Autowired
    private PermissionService permissionService;

    private ClassService getService() {
        return classService;
    }

    @ApiOperation(value = "添加")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> addClass(@RequestBody Class entity, HttpServletRequest request) {
        val user = SessionUser.getCurrentUser();
        if(StringUtils.isNotEmpty(entity.getBmname())){
            sss(entity);
        }
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        jLogService.addLog(request,"添加分类法","添加",entity.getId(),entity.getClassName(),"分类法");
        return result;
    }

    private void sss(Class entity){
        Encodingmodel selectse = encodingService.selectse(entity.getBmname());
        String bmstyle = selectse.getBmstyle();
        int cumulative = selectse.getCumulative();
        int digit = selectse.getDigit();
        int number = selectse.getNummber();
        String prefix = selectse.getPrefix();
        Class selectlist = getService().selectlist(entity);
        String substring = bmstyle.substring(0, bmstyle.length() - 2);
        String yyyyMMdd = customFormatDate(substring);
        if(selectlist!=null&&cumulative==0){
            String methodid = selectlist.getMethodid();
            //截取后面的几位数字
            String substring2 = methodid.substring(methodid.length()-digit);//00000
            String code = getCode(methodid, substring2);
            entity.setMethodid(code);
        }else if(selectlist!=null&&cumulative==1){
            String yyyy = customFormatDate("yyyy");
            String moduleId = selectlist.getMethodid();
            String substringg= moduleId.substring(prefix.length()-1, prefix.length()+3);
            if(substringg==yyyy){
                String substring2 = moduleId.substring(moduleId.length()-digit);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }else {
                String str1 = custom(prefix, digit, number, yyyyMMdd);
                entity.setMethodid(str1);
            }
        }else if(selectlist!=null&&cumulative==2){
            String yyyy = customFormatDate("MM");
            String moduleId = selectlist.getMethodid();
            int mm = bmstyle.indexOf("M");
            String substringg = moduleId.substring(prefix.length()+mm, prefix.length()+mm+2);
            if(substringg==yyyy||substringg.equals(yyyy)){
                String substring2= selectlist.getMethodid().substring(moduleId.length()-digit);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }else {
                String str1 = custom(prefix, digit, number, yyyyMMdd);
                entity.setMethodid(str1);
            }
        }
        else if(selectlist!=null&&cumulative==3){
            String yyyy = customFormatDate("dd");
            String moduleId = selectlist.getMethodid();
            int mm = bmstyle.indexOf("d");
            String substringg = moduleId.substring(prefix.length()+mm, prefix.length()+mm+2);
            if(substringg==yyyy||substringg.equals(yyyy)){
                String substring2=null;
                substring2 = selectlist.getMethodid().substring(moduleId.length()-digit);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }else {
                String str1 = custom(prefix, digit, number, yyyyMMdd);
                entity.setMethodid(str1);
            }
        }else if(selectlist==null){
            String substringg=prefix+yyyyMMdd+String.format("%0"+digit+"d", number);
            entity.setMethodid(substringg);
        }
    }


   /* @ApiOperation(value = "添加")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Class entity, HttpServletRequest request) {
        val user = SessionUser.getCurrentUser();
        entity.setCruser(user.getName());
        entity.setCrtime(new Date());
        Encodingmodel selectse = encodingService.selectse(entity.getBmname());
        String bmstyle = selectse.getBmstyle();
        int cumulative = selectse.getCumulative();
        int digit = selectse.getDigit();
        int number = selectse.getNumber();
        Class selectlist = getService().selectlist(entity);
        String yyyyMMdd = customFormatDate("yyyyMMdd");
        if(selectlist!=null&&cumulative==0){
            String substring2=null;
            substring2 = selectlist.getMethodid().substring(0, bmstyle.length() - 2);
            String code = getCode(selectlist.getMethodid(), substring2);
            entity.setMethodid(code);
        }else if(selectlist==null){
            String substring= null;
            if(bmstyle.length()>10) {
                 substring = bmstyle.substring(0, bmstyle.length() - 10);
            };
            if(substring!=null){
                String str2=substring+yyyyMMdd+String.format("%0"+digit+"d", number);
                entity.setMethodid(str2);
            }else {
                String str2=yyyyMMdd+String.format("%0"+digit+"d", number);
                entity.setMethodid(str2);
            }
        }else if(selectlist!=null&&cumulative==1){
            String yyyy = customFormatDate("yyyy");
            String moduleId = selectlist.getMethodid();
            String substring = moduleId.substring(bmstyle.length() - 11, bmstyle.length() - 7);
            if(substring==yyyy){
                String substring2=null;
                     substring2 = selectlist.getMethodid().substring(0, bmstyle.length() - 2);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }
            String str1 = custom(bmstyle,digit,number,yyyyMMdd);
            entity.setMethodid(str1);
        }
        else if(selectlist!=null&&cumulative==2){
            String yyyy = customFormatDate("MM");
            String moduleId = selectlist.getMethodid();
            String substring = moduleId.substring(bmstyle.length() - 6, bmstyle.length() - 5);
            if(substring==yyyy){
                String substring2=null;
                    substring2 = selectlist.getMethodid().substring(0, bmstyle.length() - 2);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }
            String str1 = custom(bmstyle,digit,number,yyyyMMdd);
            entity.setMethodid(str1);
        }
        else if(selectlist!=null&&cumulative==3){
            String yyyy = customFormatDate("dd");
            String moduleId = selectlist.getMethodid();
            String substring = moduleId.substring(bmstyle.length() - 5, bmstyle.length() - 4);
            if(substring==yyyy){
                String substring2=null;
                substring2 = selectlist.getMethodid().substring(0, bmstyle.length() - 2);
                String code = getCode(moduleId, substring2);
                entity.setMethodid(code);
            }
            String str1 = custom(bmstyle,digit,number,yyyyMMdd);
            entity.setMethodid(str1);
        }

        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        jLogService.addLog(request,"添加分类法","添加",entity.getId(),entity.getClassName(),"分类法");
        return result;
    }*/

    public static String getCode(String originalValue, String identifier){
        if (!originalValue.contains(identifier)) {
            throw new RuntimeException("original value does not contains identifier");
        }
        String num = identifier;
        int n = num.length();
        if ("".equals(num)) {
            num = "0";
        }
        int nums = Integer.parseInt(num) + 1;
        String newNum = String.valueOf(nums);
        n = Math.min(n, newNum.length());
        return originalValue.subSequence(0, originalValue.length() - n) + newNum;

    }
    public static String custom(String bmstyle,int digit, int number,String yyyyMMdd ){
        String substring2=bmstyle+yyyyMMdd+String.format("%0"+digit+"d", number);
        return substring2;

    }

    public static String customFormatDate(String dateFormat) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        return str;
    }

    @ApiOperation(value = "添加父分类")
    @PostMapping(value = "/addclass")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Class entity, HttpServletRequest request) {
        JsonResult<EntityId<Long>> result = doAdd(getService(), entity);
        jLogService.addLog(request,"添加分类法","添加",entity.getId(),entity.getClassName(),"分类法");
        return result;
    }

    @ApiOperation(value = "根据Id查询")
    @GetMapping(value = "/{classId}")
    @ResponseBody
    public JsonResult<Class> getClassById(@PathVariable(value = "classId") Long classId) {
        return doGetById(getService(), classId);
    }

    @ApiOperation(value = "列表查询（分页）")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Class>> getList(@RequestBody BaseVo<Class> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "根据父ID查询列表")
    @PostMapping(value = "/all")
    @ResponseBody
    public JsonResult<List<Class>> all(@RequestBody Class vo) {
        return renderSuccess(getService().list(new PropertyWrapper<>(Class.class).eq("parentId", vo.getParentId())));
    }

    @ApiOperation(value = "查询分类法名称")
    @GetMapping(value = "/classNames")
    @ResponseBody
    public JsonResult<String> classNames(String ids) {
        List<String> collect = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            List<Class> list = getService().list(new PropertyWrapper<>(Class.class).in("id", ids));
            collect = list.stream().map(Class::getClassName).collect(Collectors.toList());
        }
        return renderSuccess(String.join(",", collect.toArray(new String[collect.size()])));
    }

    @ApiOperation(value = "根据多个id查询")
    @GetMapping(value = "/listByIds")
    @ResponseBody
    public JsonResult<Map<String, String>> listByIds(String ids) {
//        List<Class> list = getService().listByIds(Arrays.asList(ids.split(",")));
        List<Class> list = getService().list(new PropertyWrapper<>(Class.class).in("id", ids));
//        Map<Long, String> collect = list.stream().collect(Collectors.toMap(Class::getId, Class::getClassName));
        Map<String, String> collect = list.stream().collect(HashMap::new, (m, v) -> m.put(v.getId() + "", v.getClassName()), HashMap::putAll);
        return renderSuccess(collect);
    }

    @ApiOperation(value = "根据多个id查询分类法名称")
    @PostMapping(value = "/classByIds")
    @ResponseBody
    public JsonResult<Map<String, String>> classByIds(@RequestBody Long[] ids) {
//        List<Class> list = getService().listByIds(Arrays.asList(ids.split(",")));
        List<Class> list = getService().list(new PropertyWrapper<>(Class.class).in("id", Arrays.asList(ids)));
//        Map<Long, String> collect = list.stream().collect(Collectors.toMap(Class::getId, Class::getClassName));
        Map<String, String> collect = list.stream().collect(HashMap::new, (m, v) -> m.put(v.getId() + "", v.getClassName()), HashMap::putAll);
        return renderSuccess(collect);
    }

    @ApiOperation(value = "查询对应父节点的子树")
    @PostMapping(value = "/tree")
    @ResponseBody
    public JsonResult<List<Class>> tree(@RequestBody Class c) {
        /*JsonResult<Pager<Class>> pclass = doList(getService(),vo.getPager(),vo.getEntity());
        List<Class> list = pclass.getObj().getRecords();*/
        List<String> orderList = new ArrayList<>();
        orderList.add("seq");
        List<Class> list = getService().list();
        //获取一级菜单
        Map<Long, List<Class>> map = list.stream().collect(Collectors.groupingBy(Class::getParentId));

        list.forEach(menu -> {
            menu.setChildren(map.containsKey(menu.getId()) ? map.get(menu.getId()) : new ArrayList<>());
        });
        return renderSuccess(map.get(c.getParentId()));
    }

    @ApiOperation(value = "查询对应父节点的子树(数组形式)")
    @GetMapping(value = "/treeList/{id}")
    @ResponseBody
    public JsonResult<List<Class>> treeList(@PathVariable("id") Long id) {
        return renderSuccess(getService().selectAdd(id));
    }

    @ApiOperation(value = "查询对应父节点的子树(数组形式)")
    @PostMapping(value = "/treeList")
    @ResponseBody
    public JsonResult<List<Class>> treeList(@RequestBody List<Long> ids) {
        return renderSuccess(getService().selectAdds(ids));
    }

    @ApiOperation(value = "查询对应父节点的子树(带权限)")
    @PostMapping(value = "/tree1")
    @ResponseBody
    public JsonResult<List<Class>> tree1(@RequestBody Class c) {
        List<String> orderList = new ArrayList<>();
        orderList.add("seq");
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        List<Long> classIds = permissionService.getUserPermissionIds(userId, "class:view");
        if(classIds.isEmpty()) return renderSuccess(new ArrayList<>());
        List<Class> list = getService().list(new PropertyWrapper<>(Class.class).in("id",classIds));
        List<Long> pids = list.stream().map(m -> m.getParentId()).distinct().collect(Collectors.toList());
        List<Class> list1 = getService().list(new PropertyWrapper<>(Class.class).in("id", pids).eq("parentId", c.getParentId()));
        list.addAll(list1);
        //获取一级菜单
        Map<Long, List<Class>> map = list.stream().collect(Collectors.groupingBy(Class::getParentId));

        list.forEach(menu -> {
            menu.setChildren(map.containsKey(menu.getId()) ? map.get(menu.getId()) : new ArrayList<>());
        });
        return renderSuccess(map.get(c.getParentId()));
    }

    @ApiOperation(value = "查询对应父节点的子树(异步)")
    @GetMapping(value = "/asyncTree")
    @ResponseBody
    public List<Class> asyncTree(Long id) {
        List<String> orderList = new ArrayList<>();
        orderList.add("seq");
        List<Class> list = getService().list(new PropertyWrapper<>(Class.class).eq("parentId", id));
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Class> list1 = getService().list(new PropertyWrapper<>(Class.class).in("parentId", ids).groupBy(Arrays.asList("parentId")).select("parentId"));
        List<Long> ids1 = list1.stream().map(m -> m.getParentId()).collect(Collectors.toList());
        list.forEach(res -> {
            if(ids1.contains(res.getId())) res.setIsParent(true);
        });
        return list;
    }


    @ApiOperation(value = "根据ID修改")
    @PutMapping(value = "/{classId}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable(value = "classId") Long classId, @RequestBody Class entity,HttpServletRequest request) {
        jLogService.addLog(request,"修改分类法","修改",entity.getId(),entity.getClassName(),"分类法");
        return doUpdateById(getService(), classId, entity);

    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    @ResponseBody
    public JsonResult batchUpdate(@RequestBody List<Class> classes) {
        boolean b = classService.updateBatchById(classes);
        return renderSuccess();
    }

    @ApiOperation(value = "根据Id删除")
    @DeleteMapping("/{classId}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable(value = "classId") Long classId,HttpServletRequest request) {
        List<Long> ids = del(classId);
        jLogService.addLogs(request,"删除分类法","删除",ids.toArray(new Long[ids.size()]),null,"分类法");
        return renderSuccess();
    }

    private List<Long> del(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        Class obj = new Class();
        obj.setParentId(id);
        List<Class> list = getService().list(obj);
        doDeleteById(getService(), id);
        if (list != null && list.size() > 0) {
            for (Class c : list) {
                ids.addAll(del(c.getId()));
            }
        }
        return ids;
    }

    @ApiOperation(value = "批量删除")
    @PostMapping(value = "/delByIds")
    @ResponseBody
    public JsonResult deleteByIds(@RequestBody Long[] ids,HttpServletRequest request) {
        List<Long> delids = new ArrayList<>();
        for (Long id : ids) {
            delids.addAll(del(id));
        }
        jLogService.addLogs(request,"删除分类法","删除",delids.toArray(new Long[delids.size()]),null,"分类法");
        return renderSuccess();
    }

    @ApiOperation(value = "导出")
    @RequestMapping(value = "/exportList")
    public void DbToExcel(PageVo1 page, HttpServletResponse response) {
        //1.查询数据
        JsonResult<Pager<Class>> pagerJsonResult = doList(getService(), page, new Class());
        List<Class> records = pagerJsonResult.getObj().getRecords();

        //2.存入Excel表格
        //标题
        String[] title = {"ClassName", "ClassDesc", "ClassCode", "ParentId", "CreTime", "CreUser", "ModifyTime", "ModifyUser"};

        String fileName = "class数据" + System.currentTimeMillis() + ".xls";

        //Sheet名
        String sheetName = "Class表";

        ServletOutputStream os = null;
        try {

            //获取相应流
            os = response.getOutputStream();

            //设置响应
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            // response.setContentType("application/vnd.ms-excel");
            ClassExcelUtil.doExport(os, records, sheetName, title, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        val user = SessionUser.getCurrentUser();
        val contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("导出分类法");
        contentLog.setContentType("元数据管理");
        contentLog.setContent("分类法");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
    }

    @ApiOperation(value = "导入数据")
    @PostMapping(value = "/import")
    @ResponseBody
    public JsonResult ExcelToDB(@RequestParam(value = "file", required = true) MultipartFile mFile) {
        try {
            List<Class> classes = ClassExcelUtil.importExcel(mFile);
            for (int i = 0; i < classes.size(); i++) {
                doAdd(getService(), classes.get(i));
            }
            return renderSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("导入失败");
        }
    }

    @ApiOperation("导入分类法的分类")
    @PostMapping("/importCategory")
    @ResponseBody
    public JsonResult importFenLei(@RequestParam(value = "file") MultipartFile mFile, String id) {
        if (id.equals("-1")) {
            return renderError("请选择分类法");
        } else {
            try {
                List<Class> classList = ClassExcelUtil.getExcCategory(Long.valueOf(id), mFile);
                for (Class c : classList) {
                    doAdd(getService(), c);
                }
                return renderSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return renderError("导入分类失败:" + e.getMessage());
            }
        }
    }

}

