package com.jnetdata.msp.exercise.computer.service.impl;

import com.jnetdata.msp.exercise.computer.model.Computer;
import com.jnetdata.msp.exercise.computer.mapper.ComputerMapper;
import com.jnetdata.msp.exercise.computer.service.ComputerService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * 练习 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2022-07-11
 */
@Service
public class ComputerServiceImpl extends BaseServiceImpl<ComputerMapper, Computer> implements ComputerService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Computer> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<Computer> pw = createWrapperUtil(condition)
                .eq("userPerson")
                .eq("startTime")
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        andOr(condition,pw,"computerCode");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public Computer getEntityAndJoinsById(Long id){
        Computer entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Computer entity, Computer vo) {
        if(vo == null) vo = new Computer();
    }

    @Override
    public void getListJoin(List<Computer> list, Computer vo) {
        for (Computer entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Computer>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
