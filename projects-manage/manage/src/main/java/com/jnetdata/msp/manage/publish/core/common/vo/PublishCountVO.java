package com.jnetdata.msp.manage.publish.core.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/9/12 18:25
 */
@ApiModel(value = "统计发布记录")
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Component
public class PublishCountVO {

    private LongAdder channelAllNum = new LongAdder();

    private LongAdder channelFinishNum = new LongAdder();

    private LongAdder channelErrorNum = new LongAdder();

    private LongAdder metadataAllNum = new LongAdder();

    private LongAdder metadataFinishNum = new LongAdder();

    private LongAdder metadataErrorNum = new LongAdder();
}
