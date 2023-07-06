package com.jnetdata.msp.util.reflect;

//import org.junit.Test;

public class ObjectCascadeVisitTest {


//    @Test
    public void test_1(){

        DemoVisitOrder order = createOrder();
        ObjectCascadeVisit.visit(order, cls -> cls==String.class, (obj) ->  {
            System.out.println("obj = " + obj);
        });
    }

    private DemoVisitOrder createOrder() {
        DemoVisitOrder order = new DemoVisitOrder();
        order.setId(1L);
        order.setOrderNo("orderNo_1");
        return order;
    }

}
