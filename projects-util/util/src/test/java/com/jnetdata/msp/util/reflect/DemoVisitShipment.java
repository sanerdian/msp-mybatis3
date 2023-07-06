package com.jnetdata.msp.util.reflect;

import lombok.Data;

import java.util.Date;

/**
 * 发货
 */
@Data
public class DemoVisitShipment {
    private Long id;
    /**
     * 发货人
     */
    private String shipper;
    /**
     * 发货数量
     */
    private Long qty;
    /**
     * 发货时间
     */
    private Date shipTime;
}
