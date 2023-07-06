package com.jnetdata.msp.member.menu.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.util.StreamUtil;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.member.menu.model.Menu;
import com.jnetdata.msp.member.menu.service.MenuService;
import com.jnetdata.msp.member.menu.service.impl.MenuServiceImpl;
import com.jnetdata.msp.member.menu.vo.MenuVo;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import com.jnetdata.msp.core.model.util.SessionUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by TF on 2019/3/19.
 */
@Controller
@RequestMapping("/member/menu")
@ApiModel(value = "菜单管理(MenuController)", description = "菜单管理")
public class MenuController extends BaseController<Long,Menu> {

    @Autowired
    private MenuService service;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private ContentLogClient contentLogService;

    @ApiOperation(value = "添加", notes="菜单信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Menu entity) {
        val user = SessionUser.getCurrentUser();
        val contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("新建菜单");
        contentLog.setContentType("菜单管理");
        contentLog.setContent("新建菜单");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);

        getService().updataSeq(entity);
        entity.setSeq(getService().getListMenu(entity.getParentId(), entity.getType()) + 1);
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的菜单信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("菜单id") @PathVariable("id") Long id) {
        List<Menu> list = getService().list();
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除菜单信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam ("ids") @ApiParam("多个id用逗号隔开")String ids) {
        val user = SessionUser.getCurrentUser();
        val contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("删除菜单");
        contentLog.setContentType("菜单管理");
        contentLog.setContent("删除菜单");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doDeleteBatchIds(getService(),ids);
    }



    @ApiOperation(value = "修改", notes="修改指定id对应的菜单信息")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("菜单id") @PathVariable("id") Long id,
                                       @RequestBody Menu entity) {
        //查询原来的排序
        Menu old = doGetById(getService(),id).getObj();
        if(!old.getSeq().equals(entity.getSeq())){
            getService().updataSeq(entity);
        }
        val user = SessionUser.getCurrentUser();
        val contentLog = new ContentLog();
        contentLog.setCrUser(user.getName());
        contentLog.setHandleType("修改菜单");
        contentLog.setContentType("菜单管理");
        contentLog.setContent("修改菜单");
        contentLog.setResult("成功");
        contentLog.setCreateTime(new Date());
        contentLogService.insert(contentLog);
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @GetMapping("/getMenu2")
    @ResponseBody
    public JsonResult<Menu> findMenu2(@ApiParam("前台展示") @RequestParam("type") int type,@ApiParam("companyId") @RequestParam("companyId") Long companyId){
        return JsonResult.renderSuccess(getService().getMenu(type,companyId));
    }

    @ApiOperation(value = "根据id查询", notes="查看指定菜单id对应的菜单信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Menu> getById(@ApiParam("菜单id") @PathVariable("id") Long id) {
        JsonResult<Menu> json = doGetById(getService(), id);
        if(json.getObj().getParentId()!=null && json.getObj().getParentId()!=0){
            JsonResult<Menu> parent = doGetById(getService(),json.getObj().getParentId());
            if(parent!=null){
                json.getObj().setParentTitle(parent.getObj().getName());
            }
        }
        return json;
    }

    @ApiOperation(value = "获取前缀", notes="根据查询条件下载excel数据")
    @PostMapping("/getPerfix")
    @ResponseBody
    public JsonResult getPerfix(HttpServletRequest request) {
        Map perfixs = getService().getPerfix();
        return renderSuccess(perfixs);
    }


    @ApiOperation(value = "获取菜单", notes="获取菜单数据")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<Menu>> list(@RequestBody MenuVo vo) {
        /*List<Companyinfo> companyinfos = companyInfoService.list();
        List<Menu> list = getService().tree(vo.getEntity().getType());
        list.forEach(e->{
            if(e.getParentId() == 0L){
                e.setParentId(e.getCompanyId());
            }
        });
        companyinfos.forEach(e->{
            Menu menu = new Menu();
            menu.setParentId(0L);
            menu.setName(e.getName());
            menu.setId(e.getId());
            list.add(menu);
        });*/
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    @ApiOperation(value = "获取树对象")
    @GetMapping("/tree")
    @ResponseBody
    public JsonResult<List<Menu>> tree(@ApiParam("前台展示") @RequestParam("type") int type) {
        List<Companyinfo> companyinfos = companyInfoService.list();
        List<Long> companyIds = companyinfos.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<String> sorts = new ArrayList<>();
        sorts.add("seq");

        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        List<Menu> menus = new ArrayList<>();
        if (SessionUser.getSubject().isPermitted("menu:view")) {
            for (Long cid : companyIds) {
                menus.addAll(getService().getMenu(type, cid));
            }
        }else {
            List<Long> ids = permissionService.getUserPermissionIds(userId,"menu:view:");//获取用户权限值
            for (Long cid: companyIds) {
                menus.addAll(getService().getMenu(ids, type, cid));
            }
        }
        menus.forEach(e->{
            if(e.getParentId() == 0L){
                e.setParentId(e.getCompanyId());
            }
        });
        companyinfos.forEach(e->{
            Menu menu = new Menu();
            menu.setParentId(0L);
            menu.setName(e.getName());
            menu.setId(e.getId());
            menus.add(menu);
        });
        List<Menu> childs = new ArrayList<>();
//        for (Menu menu : menus) {
//            if(menu.getChildren() != null)
//            childs.addAll(menu.getChildren());
//        }
        menus.addAll(childs);
        return renderSuccess(menus);
    }

    @ApiOperation(value = "查询")
    @GetMapping("/getMenu")
    @ResponseBody
    public JsonResult<Menu> findMenu(@ApiParam("前台展示") @RequestParam("type") int type,
                                     @ApiParam("companyId") @RequestParam("companyId") Long companyId){

        val user = SessionUser.getCurrentUser();
        Assert.notNull(user, "当前用户不能为null");

        if (SessionUser.getSubject().isPermitted("menu:view")) {
            List<Menu> menus = getService().getMenu(type, companyId);
            return JsonResult.success(menus);
        }

        Long userId = user.getId();
        List<Long> ids = permissionService.getUserPermissionIds(userId,"menu:view:");
        List<Menu> menus = getService().getMenu(ids, type, companyId);
        return JsonResult.success(menus);
    }

    @ApiOperation(value = "根据角色id查询", notes="根据vo查询")
    @GetMapping("/getMenuByRole")
    @ResponseBody
    public JsonResult<Menu> getMenuByRole(@ApiParam("前台展示") @RequestParam("type") int type,
                                     @ApiParam("companyId") @RequestParam("companyId") Long companyId,Long roleId){

        List<Long> ids = permissionService.getRolePermissionIds(roleId,"menu:view:");
        List<Menu> menus = getService().getMenu(ids, type, companyId);
        return JsonResult.success(menus);
    }

    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
//        getService().uploadData(file);
        return JsonResult.renderSuccess(null);
    }


    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {
        return JsonResult.renderSuccess(null);
    }
    private MenuService getService() {
        return service;
    }

}
