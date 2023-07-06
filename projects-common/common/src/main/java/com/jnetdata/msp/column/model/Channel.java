package com.jnetdata.msp.column.model;

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

import java.util.Date;
import java.util.List;

/**
 * Created by WFJ on 2019/4/25.
 */


@Data
@TableName("CHANNELINFO")
@ApiModel(description = "栏目信息表")
public class Channel extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "CHANNELID", hidden = true)
    @TableId(value = "CHANNELID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "SITEID")
    @ApiModelProperty(value = "站点id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long siteId;

    @TableField(value = "CHNLNAME")
    @ApiModelProperty(value = "栏目名称")
    private String name;

    @TableField(value = "LISTTPL")
    @ApiModelProperty(value = "列表模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long listtpl;

    @TableField(value = "DETAILTPL")
    @ApiModelProperty(value = "详情模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long detailtpl;

    @TableField(value = "EDITTPL")
    @ApiModelProperty(value = "编辑页模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long edittpl;

    @TableField(value = "CHNLDESC")
    @ApiModelProperty(value = "描述")
    private String chnlDesc;

    @TableField(value = "PARENTID")
    @ApiModelProperty(value = "父栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    @TableField(value = "QUOTAID")
    @ApiModelProperty(value = "引用栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long quotaid;

    @TableField(value = "QUOTANAME")
    @ApiModelProperty(value = "引用栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String quotaname;

    @TableField(value = "CHNLORDER")
    @ApiModelProperty(value = "排序")
    private Integer chnlOrder;

    @TableField(value = "CHNLDATAPATH")
    @ApiModelProperty(value = "存放位置")
    private String chnlDataPath;

    @TableField(value = "STATUS")
    @ApiModelProperty(value = "栏目状态（逻辑删除）")
    private Integer status;

    @TableField(value = "STOP")
    @ApiModelProperty(value = "是否停用")
    private Integer stop;

    @TableField(value = "CHNLOUTLINETEMP")
    @ApiModelProperty(value = "模板id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long chnlOutlineTemp;

    @TableField(value = "CHNLDETAILTEMP")
    @ApiModelProperty(value = "细览模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long chnlDetailTemp;

    @TableField(value = "OPERUSER")
    @ApiModelProperty(value = "删除操作用户")
    private String operUser;

    @TableField(value = "OPERTIME")
    @ApiModelProperty(value = "删除时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    @TableField(value = "EDITPAGE")
    @ApiModelProperty(value = "自定义编辑页面(默认值 页面路径)")
    private String editPage;

    @TableField(value = "LISTPAGE")
    @ApiModelProperty(value = "自定义内容(文档)列表页面(默认值 页面路径)")
    private String listPage;

    @TableField(value = "SHOWPAGE")
    @ApiModelProperty(value = "自定义内容查看页面")
    private String showPage;

    @TableField(value = "STATICFILE")
    @ApiModelProperty(value = "生成静态文件(是 否  1,0)")
    private Integer staticFile;

    @TableField(value = "TENTTYPE")
    @ApiModelProperty(value = "内容类型")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long tentType;

    @TableField(value = "ELSES")
    @ApiModelProperty(value = "其他模板")
    private String elses;

    @TableField(value = "SAVERULES")
    @ApiModelProperty(value = "文章页存储规则")
    private String saveRlues;

    @TableField(value = "SITENAME")
    @ApiModelProperty(value = "站点名")
    private String siteName;

    @TableField(value = "SKUNUMBER")
    @ApiModelProperty(value = "唯一标识符")
    private String skuNumber;

    @TableField(value = "REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value = "TABLEID")
    @ApiModelProperty(value = "元数据管理表Id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long tableId;

    @TableField(value = "METADATATYPE")
    @ApiModelProperty(value = "元数据类型")
    private String metadataType;

    @TableField(value = "IMGCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long imgColumn;

    @TableField(value = "FILECOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long fileColumn;

    @TableField(value = "VIDEOCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long videoColumn;

    @TableField(value = "RADIOCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long audioColumn;

    @TableField(value = "LISTURL")
    private String listUrl;

    @TableField(value = "EDITURL")
    private String editUrl;

    @TableField(value = "DETAILURL")
    private String detailUrl;

    @TableField(value = "WORKFLOW")
    private String workFlow;

    @TableField(exist = false)
    @ApiModelProperty(value = "子栏目列表")
    private List<Channel> childList;
}
