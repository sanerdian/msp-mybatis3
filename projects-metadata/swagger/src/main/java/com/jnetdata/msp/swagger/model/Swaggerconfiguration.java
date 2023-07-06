package com.jnetdata.msp.swagger.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2019-09-10
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("swaggerconfiguration")
@ApiModel(value="Swaggerconfiguration对象", description="")
public class Swaggerconfiguration {


    @TableField("BASEPACKAGENAME")
    private String basepackagename;

    @TableField("GROUPNAME")
    private String groupname;

    @TableField("BEANNAME")
    private String beanname;


}
