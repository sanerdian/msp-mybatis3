package com.jnetdata.msp.tlujy.jmetavotecontent.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.List;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author zyj
 * @since 2020-08-29
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("view_jmeta_vote_content")
@ApiModel(value="JmetaVoteContent对象", description="VIEW")
public class JmetaVoteContent  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "投票id")
    @TableField("VOTEID")
    private String voteid;
    @ApiModelProperty(value = "标题")
    @TableField("TITLE")
    private String title;
    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;
    @ApiModelProperty(value = "图片")
    @TableField("PHOTO")
    private String photo;
    @TableField("PHOTOS")
    private String photos;


    @TableField(exist = false)
    private String andOr;




}
