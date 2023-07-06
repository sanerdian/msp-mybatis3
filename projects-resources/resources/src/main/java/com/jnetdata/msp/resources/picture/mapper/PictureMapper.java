package com.jnetdata.msp.resources.picture.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.resources.picture.model.Picture;

import java.util.Map;

public interface PictureMapper extends BaseMapper<Picture> {
    /*获取本周新增资源数*/
    Map<String,Object> addThisWeek();
    /*获取本月新增资源数*/
    Map<String,Object> addThisMonth();
    /*获取本年新增资源数*/
    Map<String,Object> addThisYear();

}
