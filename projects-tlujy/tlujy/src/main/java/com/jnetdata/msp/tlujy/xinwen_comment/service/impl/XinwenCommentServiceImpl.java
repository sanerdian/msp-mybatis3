package com.jnetdata.msp.tlujy.xinwen_comment.service.impl;

import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
import com.jnetdata.msp.tlujy.xinwen_comment.mapper.XinwenCommentMapper;
import com.jnetdata.msp.tlujy.xinwen_comment.service.XinwenCommentService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-01-04
 */
@Service
public class XinwenCommentServiceImpl extends BaseServiceImpl<XinwenCommentMapper, XinwenComment> implements XinwenCommentService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<XinwenComment> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null){
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<XinwenComment> pw = createWrapperUtil(condition)
                                        .eq("id")
                                                            .eq("docchannelid")
                                                            .eq("docstatus")
                                                            .eq("singletempkate")
                                                            .eq("siteid")
                                                            .eq("docvalid")
                                                            .eq("docpubtime")
                                                                                                        .eq("opertime")
                                                                                                        .eq("docreltime")
                                                                                                                                                    .eq("classinfoid")
                                                                                                        .eq("companyid")
                                                            .eq("websiteid")
                                                            .eq("columnid")
                                                            .eq("modifyBy")
                                                                                                        .eq("seqencing")
                                                            .eq("modifyTime")
                                                                                                                                                    .eq("docid")
                                                            .eq("commentstatus")
                                                            .eq("statusdate")
                                                            .eq("commentsid")
                                                            .eq("dfdid")
                                                            .eq("usercomment")
                                                            .eq("anonymousflag")
                                .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"status");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        return pw;
    }

    @Override
    public XinwenComment getEntityAndJoinsById(Long id){
        XinwenComment entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(XinwenComment entity, XinwenComment vo) {
        if(vo == null) {
            vo = new XinwenComment();
        }
    }

    @Override
    public void getListJoin(List<XinwenComment> list, XinwenComment vo) {
        for (XinwenComment entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            pw.andNew().where(andOr.equals("and")?"1=1":"1=2",null);
            for(String str:condition.get(proName).toString().split(" " )){
                if(StringUtils.isNotEmpty(str)){
                    if(andOr.equals("or" )){
                        pw.or().like(proName,str);
                    }else{
                        pw.and().like(proName,str);
                    }
                }
            }
        }
    }
}
