package com.jnetdata.msp.metadata.moduleinfo.service;


import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.moduleinfo.model.Moduleinfo;
import com.jnetdata.msp.metadata.vo.BaseVo;
import org.thenicesys.data.api.Pager;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2019-08-20
 */
public interface ModuleinfoService extends BaseService<Moduleinfo> {
    void updateorder(Long id ,int sort);

    Integer getmaxorder();

    Pager<Moduleinfo> pageByPermission(BaseVo<Moduleinfo> vo);

    List<Long> allByMyPermission(String permissionType);

    List<Long> metdataByMyPermission();
}
