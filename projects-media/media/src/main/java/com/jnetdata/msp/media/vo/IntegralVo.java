package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 积分管理vo
 */
@Data
public class IntegralVo {

    //id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    //用户名
    private String username;

    //部门名称
    private String dwbsm;

    //单位名称
    private String dwmc;

    //积分数量
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long counts;

    @ApiModelProperty(value="创建人名称(系统会自动维护）")
    private String crUser;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long createBy;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
