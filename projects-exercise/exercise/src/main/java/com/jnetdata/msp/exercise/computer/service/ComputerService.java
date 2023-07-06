package com.jnetdata.msp.exercise.computer.service;

import com.jnetdata.msp.exercise.computer.model.Computer;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 练习 服务类
 * </p>
 *
 * @author zyj
 * @since 2022-07-11
 */
public interface ComputerService extends BaseService<Computer> {
        PropertyWrapper<Computer> makeSearchWrapper(Map<String, Object> condition);
        Computer getEntityAndJoinsById(Long id);
        void getListJoin(List<Computer> list, Computer vo);
}
