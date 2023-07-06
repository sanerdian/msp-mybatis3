package com.jnetdata.msp.docs.document.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * @author Administrator
 */
@Data
@TableName(value = "f_documents")
@ApiModel(value = "文件实体类",description = "文件信息")
public class Document extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField(value = "content_type")
    @ApiModelProperty(value = "文件类型")
    private String contentType;

    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    @TableField(value = "pathname")
    @ApiModelProperty(value = "上传文件保存路径")
    private String uploadPathName;

    @TableField(value = "url")
    @ApiModelProperty(value = "图片url全地址，比如http://124.193.187.98:17980/JMETADATA/goods/test.jpg")
    private String url;

    /**
     * 图片
     */
    public static final String CONTENTTYPE_PIC = "pic";
    /**
     * mp3
     */
    public static final String CONTENTTYPE_MP3 = "mp3";
    /**
     * 视频
     */
    public static final String CONTENTTYPE_VIDEO = "video";
    /**
     * VR视频
     */
    public static final String CONTENTTYPE_VRVIDEO = "vr_video";

    /**
     * pdf文件
     */
    public static final String CONTENTTYPE_PDF = "pdf";

    /**
     * doc文档
     */
    public static final String CONTENTTYPE_DOC="doc";
}
