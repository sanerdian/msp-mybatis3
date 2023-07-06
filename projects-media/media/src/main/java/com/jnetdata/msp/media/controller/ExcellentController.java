package com.jnetdata.msp.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.site.mapper.SiteMapper;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.ExcellentService;
import com.jnetdata.msp.media.service.UserImportService;
import com.jnetdata.msp.media.util.publicMethodUtil;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.tlujy.follow.mapper.FollowMapper;
import com.jnetdata.msp.tlujy.follow.model.Follow;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen020.service.Xinwen020Service;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import com.jnetdata.msp.tlujy.yjfk_photo.model.YjfkPhoto;
import com.jnetdata.msp.tlujy.yjfk_photo.service.YjfkPhotoService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/media/appexcellent")
@Api(description = "对app使用的一些代码进行优化的类")
public class ExcellentController extends BaseController<Long, JobApi> {

    @Autowired
    private ExcellentService service;
    private ExcellentService getService(){ return service; }

    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    private YjfkPhotoService yjfkPhotoService;
    @Autowired
    private ProgramaService programaService;
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private ProgramaMapper programaMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserImportService userImportService;
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private Xinwen020Service xinwen020Service;
    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @ApiOperation(value = "根据站点id获取栏目", notes="根据站点id获取栏目")
    @PostMapping("/getlm/{siteid}")
    @ResponseBody
    public JsonResult<Programa> getlm(@PathVariable("siteid") Long siteid) {

        Site site=siteMapper.selectById(siteid);

        if(site!=null){
            return JsonResult.success(getService().getlmbySiteid(siteid,"1"));
        }

        return JsonResult.success(getService().getlmbyparentid(siteid,"1"));
    }

    @ApiOperation(value = "获取投票主题列表", notes="获取投票主题列表")
    @PostMapping("/votethemelist")
    @ResponseBody
    public JsonResult<VoteThemeVo> votethemelist(@RequestBody VoteThemeQueryVo vo, HttpServletRequest request) {

        Page<Votetheme> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().votethemelist(page,vo.getEntity(),request));
    }

    @ApiOperation(value = "获取投票主题信息", notes="获取投票主题信息")
    @GetMapping("/getvotetheme/{id}")
    @ResponseBody
    public JsonResult<Votetheme> getvotetheme(@PathVariable("id")String id) {

        return JsonResult.success(getService().getvotetheme(id));
    }


    @ApiOperation(value = "app获取新闻", notes="获取新闻")
    @PostMapping("/xinwenlist")
    @ResponseBody
    public JsonResult xinwenlist(@RequestBody BaseVo<Xinwen020> vo, HttpServletRequest request) {
        PageVo1 pager1 = vo.getPager();
        Xinwen020 entity = vo.getEntity();
        Pager pager=new Pager(pager1.getCurrent(),pager1.getSize());
        List<Xinwen020> list=new ArrayList<>();
        PropertyWrapper wrapper=new PropertyWrapper(Xinwen020.class);
        wrapper.eq("status",21)
                .eq("docstatus",0)
                .eq("columnid",entity.getColumnid())
                .eq("toppingflag",1)
                .orderBy(Arrays.asList(new Pair("toppingorder",true)));
        List<Xinwen020> toppings=xinwen020Service.list(wrapper);
        if(pager1.getCurrent()!=null&&pager1.getCurrent()==1){
            list.addAll(toppings);
        }
        wrapper=new PropertyWrapper(Xinwen020.class);
        wrapper.eq("status",21)
                .eq("docstatus",0)
                .eq("columnid",entity.getColumnid())
                .ne("toppingflag",1);
        if(ObjectUtils.isEmpty(pager1.getSortProps())){
            wrapper.orderBy(Arrays.asList(new Pair("ptime",false)));
        }else {
              wrapper.orderBy(pager1.getSortProps());
        }
        Pager result = xinwen020Service.list(pager, wrapper);
        List<Xinwen020> records = result.getRecords();
        for(Xinwen020 xinwen020:records){
            list.add(xinwen020);
        }
        result.setRecords(list);
        wrapper=new PropertyWrapper(Xinwen020.class);
        wrapper.eq("status",21)
                .eq("docstatus",0)
                .eq("columnid",entity.getColumnid());
        int total=xinwen020Service.count(wrapper)-toppings.size();
        result.setTotal(total);
        return JsonResult.success(result);
    }

    @ApiOperation(value = "app获取新闻详情", notes="app获取新闻详情")
    @PostMapping("/getxinwen")
    @ResponseBody
    public JsonResult getxinwen(@RequestBody xinwenVo vo) {
        return JsonResult.success(getService().getxinwen(vo));
    }

    @ApiOperation(value = "app获取评论", notes="获取评论")
    @PostMapping("/commentlist")
    @ResponseBody
    public JsonResult<Pager<CommentVo>> commentlist(@RequestBody CommentQueryVo vo) {

        Page<XinwenComment> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().commentlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "app获取调查问卷", notes="app获取调查问卷")
    @GetMapping("/investigatelist")
    @ResponseBody
    public JsonResult<Investigate> investigatelist(HttpServletRequest request) {

        String userid = publicMethodUtil.getLoginWorker(request).getExternalid();

        return JsonResult.success(getService().Investigatelist(userid));
    }

    @ApiOperation(value = "app获取根据数量投的投票列表", notes="app获取根据数量投的投票列表")
    @PostMapping("/votelist")
    @ResponseBody
    public JsonResult<voteVo> votelist(@RequestBody VoteQueryVo vo) {

        Page<Vote> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().votelist(page,vo.getEntity()));
    }

    @ApiOperation(value = "app获取投票详细列表", notes="获取投票详细列表")
    @PostMapping("/voteContentlist")
    @ResponseBody
    public JsonResult<voteVo> voteContentlist(@RequestBody VoteQueryVo vo) throws ParseException {

        Page<Vote> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().voteContentlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "app获取谁给投票详情投过票", notes="app获取谁给投票详情投过票")
    @PostMapping("/voteUserlist")
    @ResponseBody
    public JsonResult<voteVo> voteUserlist(@RequestBody voteUserQueryVo vo) throws ParseException {

        Page<VoteUser> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().voteUserlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "app根据id查询投票信息", notes="app根据id查询投票信息")
    @PostMapping("/getVote")
    @ResponseBody
    public JsonResult<voteVo> getVote(@RequestBody voteVo entity){

        return JsonResult.success(getService().getVote(entity));
    }


    @ApiOperation(value = "app获取点赞数量", notes="获取点赞数量")
    @PostMapping("/evaluateCount")
    @ResponseBody
    public JsonResult evaluateCount(@RequestBody XinwenEvaluate vo, HttpServletRequest request) {
        return JsonResult.success(getService().evaluateCount(vo));
    }

    @ApiOperation(value = "app获取评论数量", notes="获取评论数量")
    @PostMapping("/commentCount")
    @ResponseBody
    public JsonResult pldjCount(@RequestBody XinwenComment vo, HttpServletRequest request) {
        return JsonResult.success(getService().commentCount(vo));
    }

    @ApiOperation(value = "添加意见反馈", notes="添加意见反馈")
    @PostMapping("/yjfkAdd/{uuid}")
    @ResponseBody
    public JsonResult yjfkAdd(@PathVariable("uuid") String uuid,@RequestBody Yjfk yjfk) {
        return JsonResult.success(getService().yjfkAdd(uuid,yjfk));
    }

    @ApiOperation(value = "app获取意见反馈历史", notes="获取意见反馈历史")
    @PostMapping("/yjfkhistory")
    @ResponseBody
    public JsonResult<Pager<Yjfk>> yjfkhistory(@RequestBody yjfkQueryVo vo) {

        Page<Yjfk> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().yjfkhistory(page,vo.getEntity()));
    }

    //意见反馈图片上传
    @ApiOperation(value = "意见反馈上传文件", notes="意见反馈上传文件")
    @PostMapping("/yjfkupload/{yjfkid}")
    @ResponseBody
    public JsonResult upload(@PathVariable("yjfkid") String yjfkid,HttpServletRequest request) throws IOException{

        System.out.println("进入后台");

        YjfkPhoto yjfkPhoto = new YjfkPhoto();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        MultipartFile file =null;
        for(Map.Entry<String,MultipartFile > set : fileMap.entrySet()) {
            file = set.getValue();

            // 1. 保存到服务器 得到path
            String path = configModelService.getUploadPath(ConfigModel.dir_pic);
            String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File f = new File(path, fileName);

            //file.transferTo(f);
            FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            yjfkPhoto.setYjfkid(yjfkid);
            yjfkPhoto.setPhoto("/webpic/" + fileName);
            yjfkPhotoService.insert(yjfkPhoto);
        }

        return  JsonResult.success(yjfkPhoto);
    }

    //意见反馈图片上传
    @ApiOperation(value = "删除意见反馈文件", notes="删除意见反馈文件")
    @GetMapping("/yjfkPhotoDelete/{yjfkphotoid}")
    @ResponseBody
    public JsonResult yjfkPhotoDelete(@PathVariable("yjfkphotoid") Long yjfkphotoid){

        String path = configModelService.getUploadPath(ConfigModel.dir_pic);

        //意见反馈图片
        YjfkPhoto yjfkPhoto=yjfkPhotoService.getById(yjfkphotoid);

        //删除文件
        File file=new File((path+"/"+yjfkPhoto.getPhoto()).replace("webpic","").replace("//","/"));

        if(!file.exists()){
            file.delete();
        }

        //删除意见反馈图片信息
        return  JsonResult.success(yjfkPhotoService.deleteById(yjfkphotoid));
    }

    @ApiOperation(value = "app开始投票", notes="开始投票")
    @PostMapping("/voteUserAdd")
    @ResponseBody
    public JsonResult pldjCount(@RequestBody voteUserVo entity) {
        return JsonResult.success(getService().voteUserAdd(entity));
    }

    @ApiOperation(value = "app删除评论", notes="app删除评论")
    @PostMapping("/deleteComment")
    @ResponseBody
    public JsonResult deleteComment(@RequestBody CommentVo commentVo) {

        if(commentVo.getDocid()==null&&commentVo.getCommentsid()==null){
            return JsonResult.success("请填写正确的值");
        }

        return JsonResult.success(getService().deleteComment(commentVo));
    }

    @ApiOperation(value = "app删除点赞", notes="app删除")
    @PostMapping("/deleteEvaluate")
    @ResponseBody
    public JsonResult deleteEvaluate(@RequestBody CommentVo commentVo) {

        if(commentVo.getDocid()==null&&commentVo.getCommentsid()==null){
            return JsonResult.success("请填写正确的值");
        }

        return JsonResult.success(getService().deleteEvaluate(commentVo));
    }

    @ApiOperation(value = "app删除收藏", notes="app删除")
    @PostMapping("/deleteCollect")
    @ResponseBody
    public JsonResult deleteCollect(@RequestBody CommentVo commentVo) {

        if(commentVo.getDocid()==null){
            return JsonResult.success("请填写正确的值");
        }

        return JsonResult.success(getService().deleteCollect(commentVo));
    }


    @ApiOperation(value = "查询关注部门一级", notes="查询关注部门一级")
    @PostMapping("/getbmone")
    @ResponseBody
    public JsonResult getbmone(@RequestBody CaidanVo vo, HttpServletRequest request) {

        String externalid = publicMethodUtil.getLoginWorker(request).getExternalid();

        List<CaidanVo> cdlist=new ArrayList<>();

        //是站点
        PropertyWrapper pw=new PropertyWrapper<>(Site.class);
        pw.eq("status",0);
        pw.isNull("lmorcd");
        pw.eq("companyId",vo.getId());
        if(!StringUtils.isEmpty(vo.getName())){
            pw.like("name","%"+vo.getName()+"%");
        }

        List<Site> list = siteMapper.selectList(pw.getWrapper().orderBy(true,true,"crtime"));


        for (Site s : list) {
            CaidanVo c = new CaidanVo();
            c.setId(s.getId());
            c.setName(s.getName());
            c.setOpenflag("1");

            int count=followMapper.selectCount(new PropertyWrapper<>(Follow.class).eq("bmid",s.getId()).eq("ptuserid",externalid).getWrapper());
            if(count>0){
                c.setFollowtype("1");
            }

            cdlist.add(c);
        }

        return JsonResult.success(cdlist);
    }


    @ApiOperation(value = "查询关注部门其他级", notes="查询关注部门其他级")
    @PostMapping("/getbmqt")
    @ResponseBody
    public JsonResult getbmqt(@RequestBody CaidanVo vo, HttpServletRequest request) {

        String externalid = publicMethodUtil.getLoginWorker(request).getExternalid();

        List<CaidanVo> cdlist=new ArrayList<>();

        //是栏目
        PropertyWrapper pw = new PropertyWrapper<>(Programa.class);
        pw.eq("status",0);
        pw.isNull("lmorcd");
        if(!StringUtils.isEmpty(vo.getName())){
            pw.like("name","%"+vo.getName()+"%");
        }
        pw.eq("siteId",vo.getId());
        pw.or();
        pw.eq("parentId",vo.getId());

        List<Programa> list = programaMapper.selectList(pw.getWrapper().orderBy(true,true,"crtime"));

        for (Programa s : list) {
            CaidanVo c = new CaidanVo();
            c.setId(s.getId());
            c.setName(s.getName());
//            c.setOpenflag(s.getOpenflag());

            int count=followMapper.selectCount(new PropertyWrapper<>(Follow.class).eq("bmid",s.getId()).eq("ptuserid",externalid).getWrapper());
            if(count>0){
                c.setFollowtype("1");
            }

            cdlist.add(c);
        }

        return JsonResult.success(cdlist);
    }

    @ApiOperation(value = "关注部门", notes="关注部门")
    @GetMapping("/follow/{id}")
    @ResponseBody
    public JsonResult follow(@PathVariable String id,HttpServletRequest request) {

        String externalid = publicMethodUtil.getLoginWorker(request).getExternalid();

        int count = followMapper.selectCount(new PropertyWrapper<>(Follow.class).eq("bmid",id).eq("ptuserid",externalid).getWrapper());
        //大于0证明已经关注,要删除关注
        if(count > 0){

            List<Follow> list = followMapper.selectList(new PropertyWrapper<>(Follow.class).eq("bmid",id).eq("ptuserid",externalid).getWrapper());

            //先把所有的放到最后然后删除
            for (Follow l: list) {
                Follow f = new Follow();
                f.setId(l.getId());

                getService().updateFollowOrder(f,request);
            }

            followMapper.delete(new PropertyWrapper<>(Follow.class).eq("bmid",id).eq("ptuserid",externalid).getWrapper());
        }else{//没有就添加
            Follow follow = new Follow();
            follow.setBmid(id);
            follow.setPtuserid(externalid);

            Site site = siteMapper.selectById(id);
            if(site != null){
                follow.setBmname(site.getName());
            }else{
                Programa programa = programaMapper.selectById(id);
                follow.setBmname(programa.getName());
            }

            followMapper.insert(follow);

            //给新的排序

            Follow f = new Follow();
            f.setId(follow.getId());

            getService().updateFollowOrder(f,request);

        }

        return JsonResult.success(true);
    }


    @ApiOperation(value = "获取自己关注的站点", notes="获取自己关注的站点")
    @GetMapping("/followbyUser")
    @ResponseBody
    public JsonResult followbyUser(HttpServletRequest request) {

        String externalid = publicMethodUtil.getLoginWorker(request).getExternalid();

        List<Follow> list = followMapper.selectList(new PropertyWrapper<>(Follow.class)
                .eq("ptuserid",externalid).getWrapper()
                .apply("((select count(1) from websiteinfo c where c.siteid=bmid and c.status='0' and c.lmorcd is null)>0 or (select count(1) from channelinfo l where l.channelid=bmid and l.status='0' and l.lmorcd is null)>0)")
                .orderBy(true,true,"ordernum"));

        return JsonResult.success(list);
    }

    @ApiOperation(value = "提交已经选择的投票", notes="提交已经选择的投票")
    @PostMapping("/votetj")
    @ResponseBody
    public JsonResult votetj(@RequestBody List<Long> list,HttpServletRequest request) {

        String userid = publicMethodUtil.getLoginWorker(request).getExternalid();
        String xm = publicMethodUtil.getLoginWorker(request).getXm();

        getService().votetj(list,userid,xm);

        return JsonResult.success(list);
    }

    @ApiOperation(value = "获取当前登录人所在的栏目/站点", notes="获取当前登录人所在的栏目/站点")
    @GetMapping("/userZd")
    @ResponseBody
    public JsonResult userZd(HttpServletRequest request) {

        String dwbsm = publicMethodUtil.getLoginWorker(request).getDwbsm();

        TreeVo treeVo = service.userZd(dwbsm);

        if(treeVo==null||treeVo.getId()==null||treeVo.getId()==""){
            treeVo.setId("1293913392133804033");
            treeVo.setName("国铁集团测试");
        }

        List<String> strings = service.bmParentList(dwbsm);

        treeVo.setDwlist(strings);

        return JsonResult.success(treeVo);
    }



    /**
     * 根据关注id，进行排序
     * @param followArr
     * @return
     */
    @ApiOperation(value = "根据关注id，进行排序", notes="根据关注id，进行排序")
    @PostMapping("/updateFollowOrder")
    @ResponseBody
    public JsonResult<Void> updateRecordOrder(@RequestBody Follow[] followArr,HttpServletRequest request) {
        getService().updateFollowOrderArr(followArr,request);
        return JsonResult.success();
    }


}
