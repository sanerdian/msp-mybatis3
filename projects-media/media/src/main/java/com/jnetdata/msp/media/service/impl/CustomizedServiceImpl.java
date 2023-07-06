package com.jnetdata.msp.media.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.media.service.CustomizedService;
import com.jnetdata.msp.media.util.ReflactUtil;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
import com.jnetdata.msp.media.vo.XinwenCommentVo;
import com.jnetdata.msp.media.vo.XinwenDetailVo;
import com.jnetdata.msp.media.vo.XinwenSummaryVo;
import com.jnetdata.msp.media.vo.commentManage.CommentQueryParam;
import com.jnetdata.msp.media.vo.commentManage.CommentQueryVo;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_comment.mapper.XinwenCommentMapper;
import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_evaluate.mapper.XinwenEvaluateMapper;
import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.xinwen_read.mapper.XinwenReadMapper;
import com.jnetdata.msp.tlujy.xinwen_read.model.XinwenRead;
import com.jnetdata.msp.tlujy.xinwen_user.mapper.XinwenUserMapper;
import com.jnetdata.msp.tlujy.xinwen_user.model.XinwenUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomizedServiceImpl implements CustomizedService {
    @Autowired
    private Xinwen020Mapper xinwenMapper;
    @Autowired
    private XinwenReadMapper xinwenReadMapper;

    @Autowired
    private XinwenCommentMapper xinwenCommentMapper;
    @Autowired
    private XinwenEvaluateMapper xinwenEvaluateMapper;
//    @Autowired
//    private GrpUserMapper grpUserMapper;
    @Autowired
    private XinwenUserMapper xinwenUserMapper;
    private Long getUserid() {//todo 尝试获取用户id，失败则用默认值。测试时临时使用。正式功能时，非登陆用户应该抛异常，让其登陆
        try{
            IUser<Long> currentUser = SessionUser.getCurrentUser();
            return currentUser.getId();
        }catch (Exception e){
            return 0L;
        }

    }
    @Override
    public List<XinwenSummaryVo> xinwenLunbotu(Long siteid) {
        PropertyWrapper wrapper = new PropertyWrapper(Xinwen020.class);
        wrapper.eq("isrotation",1);//是轮播图
        wrapper.eq("siteid",siteid);//查询指定网站
        wrapper.eq("docstatus",0);//未被标记删除
        List<Xinwen020> xinwens = xinwenMapper.selectList(wrapper.getWrapper());
        List<XinwenSummaryVo> vos = new ArrayList<>();
        for(Xinwen020 xinwen:xinwens){
            XinwenSummaryVo vo=change2XinwenSummaryVo(xinwen);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<XinwenSummaryVo> xinwenByColumn(Long columnid) {
        PropertyWrapper wrapper = new PropertyWrapper(Xinwen020.class);
        wrapper.eq("columnid",columnid);//查询指定栏目
        wrapper.eq("docstatus",0);//未被标记删除
        List<Xinwen020> xinwens = xinwenMapper.selectList(wrapper.getWrapper());
        List<XinwenSummaryVo> vos = new ArrayList<>();
        for(Xinwen020 xinwen:xinwens){
            XinwenSummaryVo vo=change2XinwenSummaryVo(xinwen);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public XinwenDetailVo xinwenById(Long docid) {
        Xinwen020 xinwen = xinwenMapper.selectById(docid);
        XinwenDetailVo vo= (XinwenDetailVo) ReflactUtil.obj2obj(xinwen,XinwenDetailVo.class,true);
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("createBy",getUserid());
        wrapper.eq("idvalue",docid);
        Integer isEvaluated = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());//当前用户是否已评价过（1已点赞或踩，0未点赞或踩）
        vo.setIsEvaluated(isEvaluated!=null&&isEvaluated>0?1:0);

        wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("idvalue",docid);
        wrapper.eq("evaluateobject",1);
        wrapper.eq("evaluatetype",1);
        Integer agreeNum = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());//点赞的数量
        vo.setAgreeNum(agreeNum!=null?agreeNum:0);

        wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("idvalue",docid);
        wrapper.eq("evaluateobject",1);
        wrapper.eq("evaluatetype",-1);
        Integer disagreeNum=xinwenEvaluateMapper.selectCount(wrapper.getWrapper());////踩的数量
        vo.setDisagreeNum(disagreeNum!=null?disagreeNum:0);

        wrapper = new PropertyWrapper(XinwenComment.class);
        wrapper.eq("docid",docid);
        wrapper.andNew();
        wrapper.eq("commentstatus",1);
        wrapper.or();
        wrapper.eq("createBy",getUserid());
        List<String> orders = new ArrayList<>();
        orders.add("createTime");
        wrapper.orderBy(orders,false);
        List<XinwenComment> comments= xinwenCommentMapper.selectList(wrapper.getWrapper());//评论列表(审核通过的！或自己发表的评论！)
        List<XinwenCommentVo> commentVos = new ArrayList<>();
        for(XinwenComment comment:comments){
            XinwenCommentVo commentVo= (XinwenCommentVo) ReflactUtil.obj2obj(comment,XinwenCommentVo.class,true);
            wrapper = new PropertyWrapper(XinwenEvaluate.class);
            wrapper.eq("evaluateobject",2);
            wrapper.eq("createBy",getUserid());
            wrapper.eq("idvalue",comment.getId());
            Integer isCommentEvaluated = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());//当前用户是否已评价过（1已点赞或踩，0未点赞或踩）
            commentVo.setIsEvaluated(isCommentEvaluated!=null&&isCommentEvaluated>0?1:0);

            wrapper = new PropertyWrapper(XinwenEvaluate.class);
            wrapper.eq("evaluateobject",2);
            wrapper.eq("idvalue",comment.getId());
            wrapper.eq("evaluatetype",1);
            Integer commentAgreeNum = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());//点赞的数量
            commentVo.setAgreeNum(commentAgreeNum!=null?commentAgreeNum:0);

            wrapper = new PropertyWrapper(XinwenEvaluate.class);
            wrapper.eq("evaluateobject",2);
            wrapper.eq("idvalue",comment.getId());
            wrapper.eq("evaluatetype",-1);
            Integer commentDisagreeNum=xinwenEvaluateMapper.selectCount(wrapper.getWrapper());////踩的数量
            commentVo.setDisagreeNum(commentDisagreeNum!=null?commentDisagreeNum:0);
            commentVos.add(commentVo);
        }
        vo.setCommentvos(commentVos);
        return vo;
    }


    @Override
    public Long addXinwenEvaluate(Long docid, Integer type) {
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("docid",docid);
        wrapper.eq("createBy",getUserid());
        Integer count = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());
        if(count!=null&&count>0){
            throw new RuntimeException("评价失败！已对该新闻评价过（已对其点赞/踩过）");
        }else {
            XinwenEvaluate evaluate = new XinwenEvaluate();
            evaluate.setIdvalue(docid);
            evaluate.setEvaluateobject(1L);
            evaluate.setEvaluatetype(type==null?1:type.longValue());
            evaluate.setCreateBy(getUserid());
            xinwenEvaluateMapper.insert(evaluate);
            return evaluate.getId();
        }
    }

    @Override
    public int deleteXinwenEvaluate(Long docid) {
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("idvalue",docid);
        wrapper.eq("evaluateobject",1);
        wrapper.eq("createBy",getUserid());
        Integer count = xinwenEvaluateMapper.delete(wrapper.getWrapper());
        return count!=null?count:0;
    }

    @Override
    public Long addXinwenComment(XinwenComment xinwenComment) {
        xinwenComment.setCreateBy(getUserid());
        xinwenCommentMapper.insert(xinwenComment);
        return xinwenComment.getId();
    }

    @Override
    public int deleteXinwenComment(Long commentid) {
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("evaluateobject",2);
        wrapper.eq("idvalue",commentid);
        wrapper.eq("createBy",getUserid());
        xinwenEvaluateMapper.delete(wrapper.getWrapper());
        Integer count = xinwenCommentMapper.deleteById(commentid);
        return count!=null?count:0;
    }

    @Override
    public Long addCommentEvaluate(Long commentid, Integer type) {
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("evaluateobject",2);
        wrapper.eq("idvalue",commentid);
        wrapper.eq("createBy",getUserid());
        Integer count = xinwenEvaluateMapper.selectCount(wrapper.getWrapper());
        if(count!=null&&count>0){
            throw new RuntimeException("评价失败！已对该评论进行过评价（已对其点赞/踩过）");
        }else{
            XinwenEvaluate evaluate = new XinwenEvaluate();
            evaluate.setEvaluateobject(2L);
            evaluate.setIdvalue(commentid);
            evaluate.setEvaluatetype(type==null?1:type.longValue());
            evaluate.setCreateBy(getUserid());
            xinwenEvaluateMapper.insert(evaluate);
            return evaluate.getId();
        }
    }
    @Override
    public int deleteCommentEvaluate(Long commentid) {
        PropertyWrapper wrapper = new PropertyWrapper(XinwenEvaluate.class);
        wrapper.eq("evaluateobject",2);
        wrapper.eq("idvalue",commentid);
        wrapper.eq("createBy",getUserid());
        Integer count = xinwenEvaluateMapper.delete(wrapper.getWrapper());
        return count!=null?count:0;
    }

    @Override
    public List<XinwenSummaryVo> getPushXinwen(Long userid) {
        List<XinwenSummaryVo> vos = new ArrayList<>();

        Object  externalid=userid;
        if(externalid==null){
            Lz_WorkerVo worker = getCurrentWorker();
            if(worker!=null){{
                externalid=worker.getExternalid();
            }}
        }
        if(externalid!=null){
            PropertyWrapper wrapper= new PropertyWrapper(XinwenUser.class);
            wrapper.eq("userid", externalid);
            wrapper.eq("ispush",1);
            List<XinwenUser> xinwenUsers = xinwenUserMapper.selectList(wrapper.getWrapper());
            List<Long> ids=xinwenUsers.stream().map(item->item.getDocid()).collect(Collectors.toList());
            List<Xinwen020> xinwen020s=new ArrayList<>();
            if(ids!=null&&ids.size()>0){
                xinwen020s=xinwenMapper.selectList(new PropertyWrapper<>(Xinwen020.class).in("id",ids).eq("docstatus",0).getWrapper());
            }
            if(xinwen020s!=null){
                for(Xinwen020 xinwen020:xinwen020s){
                    xinwen020.setCreateBy(Long.valueOf(String.valueOf(externalid)));
                    XinwenSummaryVo xinwenSummaryVo = this.change2XinwenSummaryVo(xinwen020);
                    vos.add(xinwenSummaryVo);
                }
            }
        }

        return vos;
    }
//    @Override
//    public List<XinwenSummaryVo> getPushXinwen(Long userid) {
//        if(userid==null){
//            userid = getUserid();
//        }
//        PropertyWrapper<GrpUser> wrapper1 = new PropertyWrapper<>(GrpUser.class);
//        wrapper1.eq("USERID", userid);
//        List<GrpUser> grpUsers = grpUserMapper.selectList(wrapper1.getWrapper());//从用户-部门交互表中获取用户的部门信息！
//        PropertyWrapper wrapper = new PropertyWrapper(Xinwen020.class);
//        wrapper.eq("status",21);
//        wrapper.eq("docstatus",0);
//        wrapper.andNew();
//        wrapper.like("pushtouser",":"+userid+",");
//        if(grpUsers.size()>0){
//            for(GrpUser grpUser:grpUsers){
//                wrapper.or();
//                wrapper.like("pushtogroup",":"+grpUser.getGroupId()+",");
//            }
//        }
//        List<Xinwen020> list = xinwenMapper.selectList(wrapper.getWrapper());
//        List<XinwenSummaryVo> vos = new ArrayList<>();
//        if(list!=null){
//            for(Xinwen020 xinwen020:list){
//                XinwenSummaryVo xinwenSummaryVo = this.change2XinwenSummaryVo(xinwen020);
//                vos.add(xinwenSummaryVo);
//            }
//        }
//
//        return vos;
//    }

    @Override
    public Pager<XinwenComment> pageXinwenComment(CommentQueryVo queryVo) {
        Pager<XinwenComment> result= new Pager<>();
        PropertyWrapper wrapper;
        CommentQueryParam entity = queryVo.getEntity();
        wrapper=new PropertyWrapper(Xinwen020.class);
        wrapper.eq(!ObjectUtils.isEmpty(entity.getSiteid()),"siteid",entity.getSiteid());
        wrapper.like(!ObjectUtils.isEmpty(entity.getDoctitle()),"title",entity.getDoctitle());
        List<Xinwen020> xinwen020s = xinwenMapper.selectList(wrapper.getWrapper());
        Map<Long,String> xinwenMap=new HashMap<>();
        for(Xinwen020 xinwen020:xinwen020s){
            xinwenMap.put(xinwen020.getId(),xinwen020.getTitle());
        }
        if(xinwenMap.size()>0){
            wrapper = new PropertyWrapper(XinwenComment.class);
            wrapper.in("docid",xinwenMap.keySet());
            if(entity.getCreateTime()!=null&&entity.getCreateTime().size()==2){
                wrapper.between("createTime",entity.getCreateTime().get(0),entity.getCreateTime().get(1));
            }
            if(!ObjectUtils.isEmpty(entity.getId())){
                try{
                    Long id=Long.parseLong(entity.getId());
                    wrapper.eq(!ObjectUtils.isEmpty(entity.getId()),"id",id);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new RuntimeException("评论编号只能是数字！");
                }
            }

            List sortProps = queryVo.getPager().getSortProps();
            if(sortProps!=null&&sortProps.size()>0){
                wrapper.orderBy(sortProps);
            }

            Integer total=xinwenCommentMapper.selectCount(wrapper.getWrapper());
            if(total==null||total==0){
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
            }else {
                Page page = new Page(queryVo.getPager().getCurrent(),queryVo.getPager().getSize());
                List<XinwenComment> xinwenComments=xinwenCommentMapper.selectPage((IPage<XinwenComment>) page,wrapper.getWrapper()).getRecords();


                for(XinwenComment xinwenComment:xinwenComments){
                    xinwenComment.setDoctitle(xinwenMap.get(xinwenComment.getDocid()));
                }
                result.setTotal(total);
                result.setRecords(xinwenComments);
            }

        }else {
            result.setTotal(0);
            result.setRecords(new ArrayList<>());
        }

        return result;
    }

    private XinwenSummaryVo change2XinwenSummaryVo(Xinwen020 xinwen){
        XinwenSummaryVo vo= (XinwenSummaryVo) ReflactUtil.obj2obj(xinwen,XinwenSummaryVo.class,false);
        PropertyWrapper wrapperRead= new PropertyWrapper(XinwenRead.class);
        wrapperRead.eq("createBy",xinwen.getCreateBy());
        wrapperRead.eq("docid",xinwen.getId());
        Integer isRead = xinwenReadMapper.selectCount(wrapperRead.getWrapper());
        vo.setIsRead(isRead!=null&&isRead>0?1:0);

        PropertyWrapper wrapperComment = new PropertyWrapper(XinwenComment.class);
        wrapperComment.eq("docid",xinwen.getId());
        Integer commentNum = xinwenCommentMapper.selectCount(wrapperComment.getWrapper());
        vo.setDianpingshu(String.valueOf(commentNum!=null?commentNum:0));
        return vo;
    }
    private Lz_WorkerVo getCurrentWorker(){
        Object obj= SecurityUtils.getSubject().getSession().getAttribute("userinfo");

        if(obj!=null){
            Lz_WorkerVo user= (Lz_WorkerVo)obj ;
            return user;
        }else {
            return null;
        }
    }

}
