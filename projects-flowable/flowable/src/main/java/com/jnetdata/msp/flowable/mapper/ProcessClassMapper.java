package com.jnetdata.msp.flowable.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.flowable.model.ProcessClass;
import org.apache.ibatis.annotations.Select;

public interface ProcessClassMapper extends BaseMapper<ProcessClass> {

    @Select("select max(order_number) from flow_process_class")
    int getOrderNumber();
}
