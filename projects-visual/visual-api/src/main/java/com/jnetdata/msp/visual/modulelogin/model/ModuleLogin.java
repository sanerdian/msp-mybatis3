package com.jnetdata.msp.visual.modulelogin.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

@Data
@TableName("j_module_login")
@ApiModel(value="登录组件实体类", description="登录组件")
public class ModuleLogin extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "login_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "背景图片")
    @TableField("background_image")
    private String backgroundImage;

    @ApiModelProperty(value = "登录位置")
    @TableField("login_position")
    private String loginPosition;

    @ApiModelProperty(value = "边距值")
    @TableField("login_margin")
    private String loginMargin;

    @ApiModelProperty(value = "组件宽度")
    @TableField("login_width")
    private Integer loginWidth;


    @ApiModelProperty(value = "距离上边距高度")
    @TableField("login_height")
    private Integer loginHeight;

    @ApiModelProperty(value = "登录类型")
    @TableField("login_type")
    private String loginType;

    @ApiModelProperty(value = "第三方登录")
    @TableField("third_login")
    private String thirdLogin;

    @ApiModelProperty(value = "验证方式")
    @TableField("verify_mode")
    private String verifyMode;

    @ApiModelProperty(value = "登录跳转")//站内1 站外2
    @TableField("login_skip")
    private String loginSkip;

    @ApiModelProperty(value = "关联的栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField("channel_id")
    private Long channelId;

    @ApiModelProperty(value = "站内链接地址")
    @TableField("inner_url")
    private String innerUrl;

    @ApiModelProperty(value = "站外链接地址")
    @TableField("outer_url")
    private String outerUrl;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;
}
