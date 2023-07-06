package com.jnetdata.msp.config.config.service.impl;

import com.jnetdata.msp.config.config.mapper.ConfigModelMapper;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2019/3/11.
 */
@Service
public class ConfigModelServiceImpl extends BaseServiceImpl<ConfigModelMapper, ConfigModel> implements ConfigModelService {
    @Override
    protected PropertyWrapper<ConfigModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("mark")
                .eq("ctype")
                .getWrapper();
    }


    /*
    * 获取路径
    * @param mark
    * return
    * */
    @Override
    public String getPath(String mark) {
        String dir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark",mark)).getValue();
        return dir;
    }

    /*
    * 获取上传路径
    * @param
    * return
    * */
    @Override
    public String getUploadPath(String mark) {
        String basedir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark","dir_upload_base")).getValue();
        String dir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark",mark)).getValue();
        return basedir + File.separator + dir;
    }

    /**
     * 获取基础上传路径
     * @return
     */
    @Override
    public String getBaseUploadPath() {
        String basedir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark","dir_upload_base")).getValue();
        return basedir;
    }

    /**
     * 获取前端路径
     * @return
     */
    @Override
    public String getFrontPath() {
        return super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark","dir_front")).getValue();
    }

    /**
     * 不带分页查询
     * @param system
     * @return
     */
    @Override
    public List<ConfigModel> getSystem(ConfigModel system) {
        return super.list(new PropertyWrapper<>(ConfigModel.class).eq("ctype",system.getCtype()));
    }


    //非标准接口
    @Override
    public String getUploadPath(String mark,String parentdz) {
        String basedir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark",parentdz)).getValue();
        String dir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark",mark)).getValue();
        return basedir + File.separator + dir;
    }
    @Override
    public String getBaseUploadPath(String parentdz) {
        String basedir = super.get(new PropertyWrapper<>(ConfigModel.class).eq("mark",parentdz)).getValue();
        return basedir;
    }
}
