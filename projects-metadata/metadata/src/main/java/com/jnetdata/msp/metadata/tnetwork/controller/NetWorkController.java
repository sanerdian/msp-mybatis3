package com.jnetdata.msp.metadata.tnetwork.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.member.group.service.GrpUserService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.metadata.tnetwork.entity.*;
import com.jnetdata.msp.metadata.tnetwork.mapper.ExcellenttMapper;
import com.jnetdata.msp.metadata.tnetwork.mapper.NetWorkMapper;
import com.jnetdata.msp.metadata.tnetwork.model.NetWorkModel;
import com.jnetdata.msp.metadata.tnetwork.service.NetWorkService;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/tlu/pushgroup")
@Api(tags = "精准推送")
public class NetWorkController extends BaseController<Long,NetWorkModel> {

    @Resource
    private NetWorkService netWorkService;
    @Resource
    private TableinfoMapper tableinfoMapper;

    @ApiOperation(value = "查询列表", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult list(@RequestBody NetWorkVo vo) {
        val user = SessionUser.getCurrentUser();
        Date date = new Date();
        String name1 = user.getName();
        List<GroupEntiy> groupEntiy = vo.getGroupEntiy();
        Paersonage paersonage = vo.getPaersonage();
        List<UserEntity> userEntity = vo.getUserEntity();
        int age = paersonage.getAge();
        int age1 =paersonage.getAge1();
        String condition = paersonage.getCondition();
        String name = paersonage.getName();
        String education = paersonage.getEducation();
        String politicsstatus = paersonage.getPoliticsstatus();
        Pager<NetWorkModel> netWorkModelPager = new Pager<>(1,15);
        NetWorkModel netWorkModel1 = new NetWorkModel();
        netWorkModel1.setXwid(vo.getXwid());
        netWorkModel1.setNamee(name);
        netWorkModel1.setAge(age);
        netWorkModel1.setEducation(education);
        netWorkModel1.setPoliticsstatus(politicsstatus);
        netWorkModel1.setConditionn(condition);
        if(groupEntiy!=null) {
            groupEntiy.forEach(s -> {
                netWorkModel1.setOrganizationid(s.getOrganizationid());
                JsonResult<Pager<NetWorkModel>> pagerJsonResult = doList(getService(), netWorkModelPager, netWorkModel1);
                if(pagerJsonResult.getObj().getRecords().isEmpty()) {
                    NetWorkModel netWorkModel = new NetWorkModel();
                    netWorkModel.setOrganizationid(s.getOrganizationid());
                    netWorkModel.setOrganizationname(s.getOrganizationname());
                    netWorkModel.setXwid(vo.getXwid());
                    netWorkModel.setTablename(vo.getTablename());
                    netWorkModel.setTreeid(vo.getTreeid());
                    netWorkModel.setTreename(vo.getTreename());
                    netWorkModel.setConditionn(condition);
                    netWorkModel.setAge(age);
                    netWorkModel.setCrtime(date);
                    netWorkModel.setAgeone(age1);
                    netWorkModel.setNamee(name);
                    netWorkModel.setCruser(name1);
                    netWorkModel.setEducation(education);
                    netWorkModel.setPoliticsstatus(politicsstatus);
                    getService().insertba(netWorkModel);
                }
            });
        }
        if(!userEntity.isEmpty()) {
            userEntity.forEach(s -> {
                List<NetWorkModel> list = getService().list(new PropertyWrapper<>(NetWorkModel.class).eq("xwid", vo.getXwid()).eq("userid", s.getUserid()));
                if(list==null) {
                   NetWorkModel netWorkModel = new NetWorkModel();
                   netWorkModel.setXwid(vo.getXwid());
                   netWorkModel.setTreeid(vo.getTreeid());
                   netWorkModel.setTreename(vo.getTreename());
                   netWorkModel.setTablename(vo.getTablename());
                   netWorkModel.setUserid(s.getUserid());
                   netWorkModel.setUsername(s.getUsername());
                    netWorkModel.setCruser(name1);
                    netWorkModel.setCrtime(date);
                   getService().insertba(netWorkModel);
               }
            });
        }
        return renderSuccess();
        /*return doList(getService(), vo.getPager(), vo.getEntity());*/
    }

    @Resource
    private GrpUserService grpUserService;

    @Resource
    private NetWorkMapper netWorkMapper;
    @Resource
    private ExcellenttMapper excellenttMapper;
    @Autowired
    private UserService userService;
    //根据用户信息查找用户推荐
    @ApiOperation(value = "查看新闻", notes = "")
    @PostMapping("/selectxinwen")
    @ResponseBody
    public JsonResult<Pager<Map>> selectxiwen(@RequestBody UserVeo userVeo) {
        UserVoEntity entity = userVeo.getEntity();
        Long id = entity.getId();
        User user = userService.selectUserone(id);
        Set<String> set1=new HashSet<>();
        String birthDate = user.getBirthDate();
        String tablename = entity.getTablename();
        //先把有用户的查出来
        List<NetWorkModel> pushname= getService().list(new PropertyWrapper<>(NetWorkModel.class).eq("userid",entity.getId()).eq("tablename",tablename));
        pushname.forEach(s->{
            set1.add(String.valueOf(s.getXwid()));
        });
        int ageForBirthday = getAgeForBirthday(birthDate);//把出生日期转换成年龄
        String educationbackground = user.getEducationbackground();//学历
        String politicsstatus = user.getPoliticsstatus();//政治面貌
        String name = user.getTrueName();
        //然后去查询其他条件后得到精准推送
        List<Long> longs = grpUserService.FindOrganization(id);
        longs.forEach(s->{
            List<NetWorkModel> netWorkModels = netWorkMapper.selectOrganization(s, ageForBirthday, educationbackground, politicsstatus, name,tablename);
            netWorkModels.forEach(m->
            {
                set1.add(String.valueOf(m.getXwid()));
            });
        });
       /* List<Object>  bingList = new ArrayList<>(objectss);
        //去重
        bingList.addAll(objects);
        bingList.removeAll(objects);
*/
     /*   List<List<Map>> hell=new ArrayList<>();
        for (int a=0;a<bingList.size();a++){
            HashMap o = (HashMap)bingList.get(a);
            Long o1 = (Long)o.get("id");
            String o2 = (String) o.get("tablename");
            hell.add(excellenttMapper.selectall(o2, o1));
        }*/
        Pager pager = userVeo.getPager().toPager();
        String set2 = String.join(",", set1);
        Page<Map> page = new Page<>(pager.getCurrent(),pager.getSize());
        List<Map> selectall = excellenttMapper.selectall(tablename, set2);
        page.setRecords(selectall);
        pager.setRecords(page.getRecords());
        pager.setPages((new Long(page.getPages())).intValue());
        pager.setTotal((new Long(page.getTotal())).intValue());
        return renderSuccess(pager);
    }
    //根据出生年月转换成年龄
    public static int getAgeForBirthday(String birthday){
        //定义一个日期格式yyyy-MM-dd，将String转为Date
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simp.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //判断该生日是否在当前日期之前,设置一个初始值，表示输入日期错误
        if(date.after(new Date())){
            return -1;
        }
        //获取当前日历对象中的年、月、日
        Calendar nowc = Calendar.getInstance();
        int nowYear = nowc.get(Calendar.YEAR);
        int nowMonth = nowc.get(Calendar.MONTH);
        int nowDay = nowc.get(Calendar.DAY_OF_MONTH);
        //将Date转为Calendar日历对象,获取生日的年、月、日
        nowc.setTime(date);
        //通过年月日计算该对象的年纪
        //先通过Year计算初步年龄
        int year = nowYear-nowc.get(Calendar.YEAR);

        //通过Month和Day判断是否过生日

        if(nowc.get(Calendar.MONTH)>nowMonth){
            return year-1;
        }
        if (nowc.get(Calendar.DAY_OF_MONTH)>nowDay){
            return year-1;
        }
        return year;
    }
    private NetWorkService getService() {
        return netWorkService;
    }


   /* @ApiOperation(value = "查看推送通知查询",notes = "查看推送通知查询")
    @PostMapping("/list2")
    @ResponseBody
    public JsonResult<Pager<NetWorkModel>> list2(@RequestBody BaseVo<NetWorkModel> vo){
        JsonResult<Pager<NetWorkModel>> pagerJsonResult = doList(getService(), vo.getPager(), vo.getEntity());
        return pagerJsonResult;
    }*/
}
