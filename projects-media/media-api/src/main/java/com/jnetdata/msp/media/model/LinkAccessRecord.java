package com.jnetdata.msp.media.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("jmeta_link_access_record")
@ApiModel(description = "链接访问记录")
public class LinkAccessRecord {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = com.baomidou.mybatisplus.enums.IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "link_info")
    @ApiModelProperty(value = "链接信息")
    private String linkInfo;

    @TableField(value = "link_type")
    @ApiModelProperty(value = "链接类型：1首页链接,2服务链接，3首页图片链接，4首页附件链接，5首页外链，6非首页图片")
    private Integer linkType;

    @TableField(value = "access_result")
    @ApiModelProperty(value = "访问结果：0失败，1成功")
    private Integer accessResult;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}