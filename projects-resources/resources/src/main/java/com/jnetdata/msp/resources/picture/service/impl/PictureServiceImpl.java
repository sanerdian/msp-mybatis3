package com.jnetdata.msp.resources.picture.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.resources.picture.mapper.PictureMapper;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.service.PictureService;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.vo.PageVo1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PictureServiceImpl extends BaseServiceImpl<PictureMapper, Picture> implements PictureService {
    @Override
    protected PropertyWrapper<Picture> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Picture> pp = createWrapperUtil(condition)
                .eq("id")
                .eq("picSign")
                .like("name")
                .eq("height")
                .eq("status")
                .eq("form")
                .eq("sign")
                .eq("isNew")
                .like("crUser")
                .eq("style")
                .eq("copyright")
                .eq("columnId")
                .eq("length")
                .eq("otherForm")
                .eq("otherCp")
                .eq("parentId")
                .eq("siteId")
                .eq("companyId")
                .eq("status")
                .eq("collection")
                .eq("resParentId")
                .eq("resoType")
                .eq("reso")
                .eq("resc")
                .eq("scale")
                .eq("pdfSign")
                .eq("tableid")
                .eq("dataId").getWrapper().ne("fsize", 0);
        if(condition.get("keyword")!=null){
            String[] keywords = ((String)condition.get("keyword")).split(";");
            for (String keyword : keywords) {
                pp.like("keyword",keyword);
            }
        }
        if(condition.get("startTime")!=null){
            pp.ge("datetime",condition.get("startTime"));
        }
        if(condition.get("endTime")!=null){
            pp.le("datetime",condition.get("endTime"));
        }


        if(condition.get("sign") == null){
            pp.and(i -> i.eq("SIGN",0).eq("ISNEW",1).or().ne("SIGN",0));
        }else if((int)condition.get("sign") == 0){
            pp.eq("isNew",1);
        }

        if(condition.get("processing")!=null){
            pp.gt("processing",0);
        }
        return pp;
    }

    @Override
    public Pager<Picture> resList(PageVo1 pager, Map<String, Object> condition) {
        Pager<Picture> page1 = new Pager<>(pager.getCurrent(), pager.getSize());
        PropertyWrapper<Picture> e = createWrapperUtil(condition)
                .eq("id")
                .eq("picSign")
                .like("name")
                .eq("height")
                .eq("form")
                .eq("sign")
                .eq("isNew")
                .like("crUser")
                .eq("style")
                .eq("status")
                .eq("copyright")
                .eq("columnId")
                .eq("length")
                .eq("otherForm")
                .eq("otherCp")
                .eq("parentId")
                .eq("siteId")
                .eq("companyId")
                .eq("status")
                .eq("collection")
                .eq("resParentId")
                .eq("resoType")
                .eq("reso")
                .eq("resc")
                .eq("scale")
                .eq("pdfSign")
                .like("dataId").getWrapper().ne("fsize", 0);

        if(condition.get("keyword")!=null){
            String[] keywords = ((String)condition.get("keyword")).split(";");
            for (String keyword : keywords) {
                e.like("keyword",keyword);
            }
        }

        if(condition.get("sign") == null){
            e.and(i-> i.eq("SIGN",0).eq("ISNEW",1).or().ne("SIGN",0));
        }else if((int)condition.get("sign") == 0){
            e.eq("isNew",1);
        }
        e.orderBy(pager.getSortProps());
        return list(page1,e);
    }

    /*
    * 获取本周新增资源数
    * return
    * */
    @Override
    public Map<String, Object> addThisWeek() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfWeek(now).toJdkDate();
        Date endDate = DateUtil.endOfWeek(now).toJdkDate();
        return getCount(startDate,endDate);
    }
    /*
    * 获取本月新增资源数
    * return
    * */
    @Override
    public Map<String, Object> addThisMonth() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfMonth(now).toJdkDate();
        Date endDate = DateUtil.endOfMonth(now).toJdkDate();
        return getCount(startDate,endDate);
    }

    /*
    * 获取本年新增资源数
    * return
    * */
    @Override
    public Map<String, Object> addThisYear() {
        Date now = new Date();
        Date startDate = DateUtil.beginOfYear(now).toJdkDate();
        Date endDate = DateUtil.endOfYear(now).toJdkDate();
        return getCount(startDate,endDate);
    }

    public Map<String,Object> getCount(Date startDate,Date endDate){
        int count = super.count(new PropertyWrapper<>(Picture.class).ge("DATETIME", startDate).le("DATETIME", endDate));;
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        return map;
    }
}
