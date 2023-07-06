package com.jnetdata.msp.flowable.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.flowable.model.FormClass;
import org.apache.ibatis.annotations.Select;

public interface FormClassMapper extends BaseMapper<FormClass> {

    @Select("select max(order_number) from flow_form_class")
    int getOrderNumber();
}
