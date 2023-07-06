package com.jnetdata.msp.util.reflect;

import lombok.Data;

import java.util.List;

/**
 * 订单明细
 */
@Data
public class DemoVisitOrderItem {
    private Long id;
    private Long orderId;
    private String productName;
    private String productDescription;
    private Long price;
    private Long qty;

    /**
     * 发货
     */
    private List<DemoVisitShipment> shippments;
}
