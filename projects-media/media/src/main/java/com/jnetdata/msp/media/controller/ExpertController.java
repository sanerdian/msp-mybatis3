package com.jnetdata.msp.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.site.mapper.SiteMapper;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.ExcellentService;
import com.jnetdata.msp.media.service.ExpertService;
import com.jnetdata.msp.media.vo.*;
import com.jnetdata.msp.member.companyinfo.mapper.CompanyInfoMapper;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.member.companyinfo.service.CompanyInfoService;
import com.jnetdata.msp.member.limit.model.Permission;
import com.jnetdata.msp.member.limit.service.PermissionService;
import com.jnetdata.msp.tlujy.integral.model.Integral;
import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote.service.VoteService;
import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
import com.jnetdata.msp.tlujy.vote_content.service.VoteContentService;
import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
import com.jnetdata.msp.tlujy.votetheme.service.VotethemeService;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/media/expert")
@Api(description = "后台互动中心接口")
public class ExpertController extends BaseController<Long, JobApi> {

    @Autowired
    private ExpertService service;
    private ExpertService getService(){ return service; }

    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteContentService voteContentService;
    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    private VotethemeService votethemeService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ProgramaMapper programaMapper;
    @Autowired
    private ProgramaService programaService;
    @Autowired
    private ExcellentService excellentService;
    @Autowired
    private CompanyInfoService companyInfoService;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private PermissionService permissionService;

    private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @ApiOperation(value = "获取投票主题列表", notes="获取投票主题列表")
    @PostMapping("/votethemelist")
    @ResponseBody
    public JsonResult<VoteThemeVo> votethemelist(@RequestBody VoteThemeQueryVo vo) {

        Page<Votetheme> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().votethemelist(page,vo.getEntity()));
    }

    @ApiOperation(value = "批量删除投票主题", notes="批量删除投票主题")
    @DeleteMapping("/votethemedeletes/{ids}")
    public JsonResult<VoteThemeVo> votethemedeletes(@PathVariable("ids") String ids) {

        getService().votethemedeletes(ids);

        return JsonResult.success();
    }

    @ApiOperation(value = "获取投票列表", notes="获取投票列表")
    @PostMapping("/votelist")
    @ResponseBody
    public JsonResult<voteVo> votelist(@RequestBody VoteQueryVo vo) {

        Page<Vote> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().votelist(page,vo.getEntity()));
    }

    @ApiOperation(value = "获取投票列表", notes="获取投票列表")
    @GetMapping("/getvote/{id}")
    @ResponseBody
    public JsonResult<VoteFromVo> getvote(@PathVariable("id") Long id) {

        return JsonResult.success(getService().getvote(id));
    }



    @ApiOperation(value = "批量删除投票", notes="批量删除投票")
    @DeleteMapping("/votedeletes/{ids}")
    public JsonResult<voteVo> votedeletes(@PathVariable("ids") String votedeletes) {

        getService().votedeletes(votedeletes);

        return JsonResult.success();
    }

    @ApiOperation(value = "获取投票详细列表", notes="获取投票详细列表")
    @PostMapping("/voteContentlist")
    @ResponseBody
    public JsonResult<voteVo> voteContentlist(@RequestBody VoteQueryVo vo) {

        Page<Vote> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().voteContentlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "获取投票详细投递用户列表", notes="获取投票详细投递用户列表")
    @PostMapping("/voteUserlist")
    @ResponseBody
    public JsonResult<voteVo> voteUserlist(@RequestBody voteUserQueryVo vo) {

        Page<VoteUser> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().voteUserlist(page,vo.getEntity()));
    }

    /**
     * 执行投票新增/修改操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加/修改投票标题详情", notes="添加/修改投票标题详情")
    @PostMapping("/voteFrom")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody VoteFromVo entity) {
        return JsonResult.success(getService().voteFrom(entity));
    }


    @ApiOperation("对投票信息进行推送")
    @PostMapping("/pushvote")
    @ResponseBody
    public JsonResult pushvote(@RequestBody PushSettingVo vo){
        try {

            getService().votePush(vo);

            return JsonResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e.getClass().getName()+":"+e.getMessage());
        }
    }


    @ApiOperation(value = "获取新闻列表", notes="获取新闻列表")
    @PostMapping("/xinwenlist")
    @ResponseBody
    public JsonResult<CommentCenterVo> xinwenlist(@RequestBody commentCenterQueryVo vo) {

        Page<Xinwen020> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().xinwenlist(page,vo.getEntity()));
    }

    @ApiOperation(value = "获取评论列表", notes="获取评论列表")
    @PostMapping("/commentlist")
    @ResponseBody
    public JsonResult<CommentVo> commentlist(@RequestBody CommentQueryVo vo) {

        Page<XinwenComment> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().listcommentVo(page,vo.getEntity()));
    }

    @ApiOperation(value = "获取点赞列表", notes="获取点赞列表")
    @PostMapping("/evaluatelist")
    @ResponseBody
    public JsonResult<CommentVo> evaluatelist(@RequestBody CommentQueryVo vo) {

        Page<XinwenEvaluate> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().listEvaluateVo(page,vo.getEntity()));
    }


    @ApiOperation(value = "获取意见反馈", notes="获取评论中心列表")
    @PostMapping("/yjfklist")
    @ResponseBody
    public JsonResult<yjfkVo> commentCenter(@RequestBody yjfkQueryVo vo) {

        Page<Yjfk> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().listyjfkVo(page,vo.getEntity()));
    }


    @ApiOperation(value = "获取积分管理列表", notes="获取积分管理列表")
    @PostMapping("/IntegralList")
    @ResponseBody
    public JsonResult IntegralList(@RequestBody IntegralQueryVo vo) {

        Page<Integral> page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());

        return JsonResult.success(getService().listIntegralVo(page,vo.getEntity()));
    }

    @ApiOperation(value = "积分增加", notes="积分增加")
    @GetMapping("/IntegralAdd/{counts}")
    @ResponseBody
    public JsonResult IntegralAdd(@PathVariable Long counts) {
        return JsonResult.success(getService().IntegralAdd(counts));
    }


    @ApiOperation(value = "评论中心导出", notes="评论中心导出")
    @GetMapping("/xinwenExport")
    @ResponseBody
    public void commentExport(HttpServletResponse response) {

        Page<Xinwen020> page = new Page(1,1000000000);

        List<CommentCenterVo> list=getService().xinwenlist(page,new CommentCenterVo()).getRecords();


        String fileName = "新闻信息" + System.currentTimeMillis() + ".xls";
        String sheetName = "新闻信息";
        String[] title = {"稿件标题", "所属栏目","发布人", "发布时间", "总评论数","总点赞数"};

        ServletOutputStream os = null;

        List<List<String>> listAll=new ArrayList<>();

        for (CommentCenterVo comvo : list) {

            List<String> l=new ArrayList<>();
            l.add(comvo.getTitle());
            l.add(comvo.getDocchannelName());
            l.add(comvo.getCruser());
            l.add(sf.format(comvo.getCrtime()));
            l.add(String.valueOf(comvo.getCommentCount()));
            l.add(String.valueOf(comvo.getDjcounts()));
            listAll.add(l);
        }

        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, listAll, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "积分列表导出", notes="积分列表导出")
    @GetMapping("/IntegralExport")
    @ResponseBody
    public void IntegralExport(HttpServletResponse response) {

        Page<Integral> page = new Page(1,1000000000);

        List<IntegralVo> list=getService().listIntegralVo(page,new IntegralVo()).getRecords();


        String fileName = "积分信息" + System.currentTimeMillis() + ".xls";
        String sheetName = "积分信息";
        String[] title = {"姓名", "所属部门","所属单位", "拥有积分"};

        ServletOutputStream os = null;

        List<List<String>> listAll=new ArrayList<>();

        for (IntegralVo comvo : list) {

            List<String> l=new ArrayList<>();
            l.add(comvo.getUsername());
            l.add(comvo.getDwbsm());
            l.add(comvo.getDwmc());
            l.add(String.valueOf(comvo.getCounts()));
            listAll.add(l);
        }

        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, listAll, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "评论信息导出", notes="评论信息导出")
    @GetMapping("/commentExport/{docid}")
    @ResponseBody
    public void commentExport(@PathVariable Long docid,HttpServletResponse response) {

        Page<XinwenComment> page = new Page(1,1000000000);

        List<CommentVo> list=getService().listcommentVo(page,new CommentVo(docid)).getRecords();


        String fileName = "评论信息" + System.currentTimeMillis() + ".xls";
        String sheetName = "评论信息";
        String[] title = {"新闻标题", "评论内容","评论用户", "评论时间", "用户性别","所属组织","评论状态"};

        ServletOutputStream os = null;

        List<List<String>> listAll=new ArrayList<>();

        for (CommentVo comvo : list) {

            List<String> l=new ArrayList<>();
            l.add(comvo.getXinwentitle());
            l.add(comvo.getUsercomment());
            l.add(comvo.getCrUser());
            l.add(sf.format(comvo.getCreateTime()));
            l.add(String.valueOf(comvo.getSex()));
            l.add(String.valueOf(comvo.getOrganize()));
            l.add(String.valueOf(comvo.getCommentstatusStr()));
            listAll.add(l);
        }

        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, listAll, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "点赞信息导出", notes="点赞信息导出")
    @GetMapping("/evaluateExport/{docid}")
    @ResponseBody
    public void evaluateExport(@PathVariable Long docid,HttpServletResponse response) {

        Page<XinwenEvaluate> page = new Page(1,1000000000);

        List<CommentVo> list=getService().listEvaluateVo(page,new CommentVo(docid)).getRecords();


        String fileName = "点赞信息" + System.currentTimeMillis() + ".xls";
        String sheetName = "点赞信息";
        String[] title = {"新闻标题", "点赞用户","点赞时间", "用户性别", "所属组织"};

        ServletOutputStream os = null;

        List<List<String>> listAll=new ArrayList<>();

        for (CommentVo comvo : list) {

            List<String> l=new ArrayList<>();
            l.add(comvo.getXinwentitle());
            l.add(comvo.getCrUser());
            l.add(sf.format(comvo.getCreateTime()));
            l.add(String.valueOf(comvo.getSex()));
            l.add(String.valueOf(comvo.getOrganize()));
            listAll.add(l);
        }

        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, listAll, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @ApiOperation(value = "投票用户导出", notes="投票用户导出")
    @GetMapping("/voteUserExport/{voteid}")
    @ResponseBody
    public void voteUserExport(@PathVariable("voteid") Long voteid,HttpServletResponse response) {

        Page<VoteUser> page = new Page(1,1000000000);

        voteUserVo voteuser=new voteUserVo();
        voteuser.setVoteid(voteid);

        List<voteUserVo> list=getService().voteUserlist(page,voteuser).getRecords();

        String fileName = "投票用户信息" + System.currentTimeMillis() + ".xls";
        String sheetName = "投票用户信息";
        String[] title = {"投票标题", "投票内容","投票用户","投票时间", "用户性别", "所属组织"};

        ServletOutputStream os = null;

        List<List<String>> listAll=new ArrayList<>();

        for (voteUserVo comvo : list) {

            List<String> l=new ArrayList<>();
            l.add(comvo.getVotetitle());
            l.add(comvo.getVotecontenttitle());
            l.add(comvo.getCrUser());
            l.add(sf.format(comvo.getCreateTime()));
            l.add(String.valueOf(comvo.getSex()));
            l.add(String.valueOf(comvo.getOrganize()));
            listAll.add(l);
        }

        try {
            os = response.getOutputStream();
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            //2 下载数据
            service.export(os, listAll, title, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //投票上传文件
    @ApiOperation(value = "投票上传文件", notes="投票上传文件")
    @PostMapping("/voteupload/{voteid}")
    @ResponseBody
    public JsonResult voteupload(MultipartFile file, @PathVariable("voteid") String voteid) throws IOException{

        Vote vote=voteService.getById(Long.valueOf(voteid));

        // 1. 保存到服务器 得到path
        String path = configModelService.getUploadPath(ConfigModel.dir_pic);
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

        //之前有地址需要删除文件
        if(!StringUtils.isEmpty(vote.getPhoto())){
            //删除文件
            File delfile=new File((path+"/"+vote.getPhoto()).replace("webpic","").replace("//","/"));

            if(!delfile.exists()){
                delfile.delete();
            }
        }

        //上传新的文件
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(path, fileName);

        //file.transferTo(f);
        FileUtils.copyInputStreamToFile(file.getInputStream(),f);
        vote.setPhoto("/webpic/"+fileName);

        voteService.updateAllColumnById(vote);

        return  JsonResult.success("/webpic/"+fileName);
    }

    //投票上传文件
    @ApiOperation(value = "投票内容上传文件", notes="投票内容上传文件")
    @PostMapping("/votecontentupload/{votecontentid}/{photo}")
    @ResponseBody
    public JsonResult votecontentupload(MultipartFile file, @PathVariable("votecontentid") String votecontentid,@PathVariable("photo") String photo) throws IOException{

        if(!StringUtils.isEmpty(photo)){
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
        }

        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        // 1. 保存到服务器 得到path
        String path = configModelService.getUploadPath(ConfigModel.dir_pic);

        //上传新的文件
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //如果id为空，证明是添加的时候
        if (StringUtils.isEmpty(votecontentid)||"undefined".equals(votecontentid)){

            //如果地址为空,新增一个
            if(StringUtils.isEmpty(photo)||"undefined".equals(photo)){
                File f = new File(path, fileName);
                //file.transferTo(f);
                FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            }else{//地址不为空先删除之前的，后新增一个
                //删除文件
                File delfile=new File((path+"/"+photo).replace("webpic","").replace("//","/"));

                if(!delfile.exists()){
                    delfile.delete();
                }

                File f = new File(path, fileName);

                //file.transferTo(f);
                FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            }
        }else{
            VoteContent vote=voteContentService.getById(Long.valueOf(votecontentid));

            //之前有地址需要删除文件
            if(!StringUtils.isEmpty(vote.getPhoto())){
                //删除文件
                File delfile=new File((path+"/"+vote.getPhoto()).replace("webpic","").replace("//","/"));

                if(!delfile.exists()){
                    delfile.delete();
                }
            }

            File f = new File(path, fileName);

            //file.transferTo(f);
            FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            vote.setPhoto("/webpic/"+fileName);

            voteContentService.updateAllColumnById(vote);
        }


        return  JsonResult.success("/webpic/"+fileName);
    }


    //投票上传文件
    @ApiOperation(value = "投票内容上传文件", notes="投票内容上传文件")
    @PostMapping("/votethemeupload/{votethemeid}/{photo}")
    @ResponseBody
    public JsonResult votethemeupload(MultipartFile file, @PathVariable("votethemeid") String votethemeid,@PathVariable("photo") String photo) throws IOException{

        if(!StringUtils.isEmpty(photo)){
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
            photo=photo.replaceAll("@@@@","/");
        }

        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        // 1. 保存到服务器 得到path
        String path = configModelService.getUploadPath(ConfigModel.dir_pic);

        //上传新的文件
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //如果id为空，证明是添加的时候
        if (StringUtils.isEmpty(votethemeid)||"undefined".equals(votethemeid)){

            //如果地址为空,新增一个
            if(StringUtils.isEmpty(photo)||"undefined".equals(photo)){
                File f = new File(path, fileName);
                //file.transferTo(f);
                FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            }else{//地址不为空先删除之前的，后新增一个
                //删除文件
                File delfile=new File((path+"/"+photo).replace("webpic","").replace("//","/"));

                if(!delfile.exists()){
                    delfile.delete();
                }

                File f = new File(path, fileName);

                //file.transferTo(f);
                FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            }
        }else{
            Votetheme vote=votethemeService.getById(Long.valueOf(votethemeid));

            //之前有地址需要删除文件
            if(!StringUtils.isEmpty(vote.getPhoto())){
                //删除文件
                File delfile=new File((path+"/"+vote.getPhoto()).replace("webpic","").replace("//","/"));

                if(!delfile.exists()){
                    delfile.delete();
                }
            }

            File f = new File(path, fileName);

            //file.transferTo(f);
            FileUtils.copyInputStreamToFile(file.getInputStream(),f);
            vote.setPhoto("/webpic/"+fileName);

            votethemeService.updateAllColumnById(vote);
        }


        return  JsonResult.success("/webpic/"+fileName);
    }

    @ApiOperation("获取站点菜单或者栏目菜单")
    @GetMapping("/getSitecdorChannelcd/{id}")
    @ResponseBody
    public JsonResult getSitecdorChannelcd(@PathVariable("id") Long id){

        Long userid = SessionUser.getCurrentUser().getId();
        String username = SessionUser.getCurrentUser().getName();

        //根据id查询公司
        Companyinfo companyinfo=companyInfoService.getById(id);

        List<CaidanVo> cdlist=new ArrayList<>();

        //是站点菜单
        if(companyinfo != null){
            PropertyWrapper pw=new PropertyWrapper<>(Site.class);
            pw.eq("status",0);
            pw.eq("lmorcd","1");
            pw.eq("companyId",id);
            pw.orderBy(Arrays.asList("siteOrder"),true);

            List<Site> list = siteMapper.selectList(pw.getWrapper());

            for (Site s : list) {
                CaidanVo c = new CaidanVo();
                c.setId(s.getId());
                c.setName(s.getName());
                c.setParentId(s.getCompanyId());

                //不是我admin权限
                if(!"admin".equals(username)){
                    Permission permission = new Permission();
                    permission.setOwnerId(Long.valueOf(userid));
                    permission.setPermission("site:view:"+c.getId());

                    int count = excellentService.listPermissionCount(permission);

                    if(count==0&&!s.getCreateBy().equals(userid)){
                        continue;
                    }
                }

                cdlist.add(c);
            }

        }else{//是栏目菜单
            PropertyWrapper pw = new PropertyWrapper<>(Programa.class);
            pw.eq("status",0);
            pw.eq("lmorcd","1");
            pw.eq("siteId",id);
            pw.or();
            pw.eq("parentId",id);
            pw.orderBy(Arrays.asList("chnlOrder"),true);

            List<Programa> list = programaMapper.selectList(pw.getWrapper());

            for (Programa s : list) {
                CaidanVo c = new CaidanVo();
                c.setId(s.getId());
                c.setName(s.getName());
                //栏目下
                if(s.getParentId()!=null&&s.getParentId()!=0){
                    c.setParentId(s.getParentId());
                }else{
                    c.setParentId(s.getSiteId());
                }

                if(!"admin".equals(username)){
                    Permission permission = new Permission();
                    permission.setOwnerId(Long.valueOf(userid));
                    permission.setPermission("column:view:"+c.getId());

                    int count = excellentService.listPermissionCount(permission);

                    if(count==0&&!s.getCreateBy().equals(userid)){
                        continue;
                    }
                }

                cdlist.add(c);
            }
        }

        return JsonResult.success(cdlist);
    }




    @ApiOperation("获取站点目录数据(权限)")
    @GetMapping("/getSiteTree")
    @ResponseBody
    public JsonResult<List<SiteVo>> getSiteTree(HttpServletRequest request){

        Long userid = SessionUser.getCurrentUser().getId();
        String username =  SessionUser.getCurrentUser().getName();

        List<SiteVo> siteVos=new ArrayList<>();

        //所有机构
        List<Companyinfo> clist = companyInfoMapper.selectList(new PropertyWrapper<>(Companyinfo.class).getWrapper());

        clist.stream().map(m -> m.getId()).collect(Collectors.toList());

        for (Companyinfo c : clist) {
            SiteVo siteVo = new SiteVo();

            siteVo.setName(c.getName());
            siteVo.setId(c.getId());
            siteVo.setParentid(0L);

            if(!"admin".equals(username)){
                Permission permission = new Permission();
                permission.setOwnerId(Long.valueOf(userid));
                permission.setPermission("company:view:"+c.getId());

                int count = excellentService.listPermissionCount(permission);

                if(count==0&&c.getCreateBy().longValue()!=userid){
                    continue;
                }
            }

            siteVos.add(siteVo);
        }


        //所有站点
        List<Site> slist = siteMapper.selectList(new PropertyWrapper<>(Site.class)
                .eq("status",0)
                .isNull("lmorcd")
                .orderBy(Arrays.asList("siteOrder"),true)
                .getWrapper());

        for (Site s : slist) {
            SiteVo siteVo = new SiteVo();

            siteVo.setName(s.getName());
            siteVo.setId(s.getId());
            siteVo.setParentid(s.getCompanyId());

            if(!"admin".equals(username)){
                Permission permission = new Permission();
                permission.setOwnerId(Long.valueOf(userid));
                permission.setPermission("site:view:"+s.getId());

                int count = excellentService.listPermissionCount(permission);

                if(count==0&&s.getCreateBy().longValue()!=userid){
                    continue;
                }
            }

            siteVos.add(siteVo);
        }

        //所有栏目
        List<Programa> plist = programaMapper.selectList(new PropertyWrapper<>(Programa.class)
                .eq("status",0)
                .isNull("lmorcd")
                .orderBy(Arrays.asList("chnlOrder"),true)
                .getWrapper());

        for (Programa p : plist) {
            SiteVo siteVo = new SiteVo();

            siteVo.setName(p.getName());
            siteVo.setId(p.getId());
            siteVo.setParentid((p.getParentId()!=null&&p.getParentId()!=0L)?p.getParentId():p.getSiteId());

            if(!"admin".equals(username)){
                Permission permission = new Permission();
                permission.setOwnerId(Long.valueOf(userid));
                permission.setPermission("column:view:"+p.getId());

                int count = excellentService.listPermissionCount(permission);

                if(count==0&&p.getCreateBy().longValue()!=userid){
                    continue;
                }
            }

            siteVos.add(siteVo);
        }

        List<SiteVo> siteVone=new ArrayList<>();

        for (int i = 0; i < siteVos.size(); i++) {
            SiteVo siteVo = siteVos.get(i);

            //根据当前的父id和所有的id进行对比
            List<SiteVo> ids = siteVos.stream().filter(m -> ((m.getId()!=null&&siteVo.getParentid()!=null) && (m.getId().longValue()==siteVo.getParentid().longValue()))).collect(Collectors.toList());

            //如果父id，在id中都没有，证明自己是一级
            if(ids.size()==0){
                siteVone.add(siteVo);
            }
        }

        for (int i = 0; i < siteVone.size(); i++) {
            SiteVo siteVo = siteVone.get(i);

            List<SiteVo> ids = siteVos.stream().filter(m ->(m.getParentid()!=null&&siteVo.getId()!=null) &&( m.getParentid().longValue()==siteVo.getId().longValue())).collect(Collectors.toList());

            addList(ids,siteVos);

            siteVo.setSites(ids);

        }


        return JsonResult.success(siteVone);
    }

    private void addList(List<SiteVo> sites, List<SiteVo> siteVos){

        for (int i = 0; i < sites.size(); i++) {
            SiteVo siteVo = sites.get(i);

            List<SiteVo> ids = siteVos.stream().filter(m -> (m.getParentid()!=null&&siteVo.getId()!=null) && (m.getParentid().longValue()==siteVo.getId().longValue())).collect(Collectors.toList());

            addList(ids,siteVos);

            siteVo.setSites(ids);

        }

    }



    private void siteChannelInfo(SiteVo siteVo, Long userid, String username){

        //栏目id
        List<Programa> programas=excellentService.getlmbySiteid(siteVo.getId(),"");

        List<SiteVo> siteVos=new ArrayList<>();

        if(programas!=null){
            for (Programa p : programas) {

                if(!"admin".equals(username)){
                    Permission permission = new Permission();
                    permission.setOwnerId(Long.valueOf(userid));
                    permission.setPermission("column:view:"+p.getId());

                    int count = excellentService.listPermissionCount(permission);

                    if(count==0&&p.getCreateBy().longValue()!=userid){
                        continue;
                    }
                }

                SiteVo vo=new SiteVo();
                vo.setId(p.getId());
                vo.setName(p.getName());

                channelChannelInfo(vo,userid,username);

                siteVos.add(vo);
            }
        }
        siteVo.setSites(siteVos);
    }

    private void channelChannelInfo(SiteVo siteVo, Long userid, String username){
        //栏目id
        List<Programa> programas=excellentService.getlmbyparentid(siteVo.getId(),"");

        List<SiteVo> siteVos=new ArrayList<>();

        if(programas!=null){
            for (Programa p : programas) {

                if(!"admin".equals(username)){
                    Permission permission = new Permission();
                    permission.setOwnerId(userid);
                    permission.setPermission("column:view:"+p.getId());

                    int count = excellentService.listPermissionCount(permission);

                    if(count==0&&p.getCreateBy().longValue()!=userid){
                        continue;
                    }
                }

                SiteVo vo=new SiteVo();
                vo.setId(p.getId());
                vo.setName(p.getName());

                channelChannelInfo(vo,userid,username);

                siteVos.add(vo);
            }
        }
        siteVo.setSites(siteVos);
    }



}
