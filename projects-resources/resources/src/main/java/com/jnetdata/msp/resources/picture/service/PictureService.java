package com.jnetdata.msp.resources.picture.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.resources.picture.model.Picture;
import com.jnetdata.msp.resources.picture.vo.PictureVo;
import org.thenicesys.data.api.ConditionContainer;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.web.vo.PageVo1;

import java.util.Map;

public interface PictureService extends BaseService<Picture> {

    /*
    * 获取本周新增资源数
    * */
    Map<String,Object> addThisWeek();

    /*
    * 获取本月新增资源数
    * */
    Map<String,Object> addThisMonth();

/*
* 获取本年新增资源数
* */
    Map<String,Object> addThisYear();

    Pager<Picture> resList(PageVo1 pager, Map<String, Object> condition);
}


