package com.jnetdata.msp.util.reflect;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单
 */
@Data
public class DemoVisitOrder {

    private Long id;
    private String orderNo;
    private String buyer;
    private Long amount;
    private Date crTime;

    private Map<String,Object> map;

    private List<DemoVisitOrderItem> items;

}
