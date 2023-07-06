package com.jnetdata.msp.generator.controller.component.service;


import com.jnetdata.msp.generator.controller.component.model.Component;
import com.jnetdata.msp.core.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2019-08-20
 */
public interface ComponentService extends BaseService<Component> {
    List<Component> listByModuleId(Long id, String osname);
}

