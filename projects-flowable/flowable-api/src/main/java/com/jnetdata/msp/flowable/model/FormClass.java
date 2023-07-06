package com.jnetdata.msp.flowable.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_flow_form_class")
@ApiModel(value="表单分类表")
public class FormClass implements EntityId<Integer> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id，主键")
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    @TableField("class_name")
    private String className;

    @ApiModelProperty(value = "序号")
    @TableField("order_number")
    private Integer orderNumber;

    @ApiModelProperty(value = "图标路径")
    @TableField("icon_path")
    private String iconPath;

    @ApiModelProperty(value = "图标颜色")
    @TableField("icon_color")
    private String iconColor;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "0-未删除，1-已删除")
    @TableField("delete_flag")
    private String deleteFlag;
}
