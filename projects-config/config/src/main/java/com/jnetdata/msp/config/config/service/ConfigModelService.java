package com.jnetdata.msp.config.config.service;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.core.service.BaseService;

import java.util.List;

/**
 * Created by Admin on 2019/3/11.
 */
public interface ConfigModelService extends BaseService<ConfigModel>{
    //获取路径
    String getPath(String mark);

    /**
     * 获取上传路径
     * @param mark
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    String getUploadPath(String mark);

    //获取基本上传路径
    String getBaseUploadPath();

    /**
     * 获取前端路径
     * @return
     */
    String getFrontPath();

    /**
     * 不带分页查询
     * @param system
     * @author 于金龙
     * @date 2022/11/8
     * @return
     */
    List<ConfigModel> getSystem(ConfigModel system);

    //非标准接口
    String getUploadPath(String mark,String parentdz);
    String getBaseUploadPath(String parentdz);
}
