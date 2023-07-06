package com.jnetdata.msp.manage.column.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * Created by WFJ on 2019/4/25.
 */


@Data
@TableName("CHANNELINFO")
@ApiModel(value = "栏目信息实体类",description = "栏目信息")
public class Programa extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "CHANNELID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("id")
    private Long id;

    @TableField(value = "SITEID")
    @ApiModelProperty(value = "站点id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("站点id")
    private Long siteId;

    @TableField(value = "menuid")
    @ApiModelProperty(value = "前台菜单id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long menuid;

    @TableField(value = "CHNLNAME")
    @ApiModelProperty(value = "栏目名称")
    @ExcelProperty("栏目名称")
    private String name;

    @TableField(value = "menuname")
    @ApiModelProperty(value = "前台菜单名称")
    private String menuname;

    @TableField(value = "LISTTPL")
    @ApiModelProperty(value = "列表模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("列表模板")
    private Long listtpl;

    @TableField(value = "DETAILTPL")
    @ApiModelProperty(value = "详情模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("详情模板")
    private Long detailtpl;

    @TableField(value = "EDITTPL")
    @ApiModelProperty(value = "编辑页模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("编辑页模板")
    private Long edittpl;

    @TableField(value = "CHNLDESC")
    @ApiModelProperty(value = "描述")
    @ExcelProperty("描述")
    private String chnlDesc;

    @TableField(value = "PARENTID")
    @ApiModelProperty(value = "父栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("父栏目id")
    private Long parentId;

    @TableField(value = "QUOTAID")
    @ApiModelProperty(value = "引用栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("引用栏目id")
    private Long quotaid;

    @TableField(value = "QUOTANAME")
    @ApiModelProperty(value = "引用栏目")
    @ExcelProperty("引用栏目")
    private String quotaname;

    @TableField(value = "CHNLORDER")
    @ApiModelProperty(value = "排序")
    @ExcelProperty("排序")
    private Integer chnlOrder;

    @TableField(value = "CHNLDATAPATH")
    @ApiModelProperty(value = "存放位置")
    @ExcelProperty("存放位置")
    private String chnlDataPath;

    @TableField(value = "STATUS")
    @ApiModelProperty(value = "栏目状态（逻辑删除）")
    @ExcelProperty("栏目状态")
    private Integer status;

    @TableField(value = "STOP")
    @ApiModelProperty(value = "是否停用")
    @ExcelProperty("是否停用")
    private Integer stop;

    @TableField(value = "CHNLOUTLINETEMP")
    @ApiModelProperty(value = "模板id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("模板id")
    private Long chnlOutlineTemp;

    @TableField(value = "CHNLDETAILTEMP")
    @ApiModelProperty(value = "细览模板")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("细览模板")
    private Long chnlDetailTemp;

    @TableField(value = "OPERUSER")
    @ApiModelProperty(value = "删除操作用户")
    @ExcelProperty("删除操作用户")
    private String operUser;

    @TableField(value = "OPERTIME")
    @ApiModelProperty(value = "删除时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("删除时间")
    private Date operTime;

    @TableField(value = "EDITPAGE")
    @ApiModelProperty(value = "自定义编辑页面(默认值 页面路径)")
    @ExcelProperty("自定义编辑页面")
    private String editPage;

    @TableField(value = "LISTPAGE")
    @ApiModelProperty(value = "自定义内容(文档)列表页面(默认值 页面路径)")
    @ExcelProperty("自定义内容")
    private String listPage;

    @TableField(value = "SHOWPAGE")
    @ApiModelProperty(value = "自定义内容查看页面")
    @ExcelProperty("自定义内容查看页面")
    private String showPage;

    @TableField(value = "STATICFILE")
    @ApiModelProperty(value = "生成静态文件(是 否  1,0)")
    @ExcelProperty("生成静态文件")
    private Integer staticFile;

    @TableField(value = "TENTTYPE")
    @ApiModelProperty(value = "内容类型")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("内容类型")
    private Long tentType;

    @TableField(value = "ELSES")
    @ApiModelProperty(value = "其他模板")
    @ExcelProperty("其他模板")
    private String elses;

    @TableField(value = "SAVERULES")
    @ApiModelProperty(value = "文章页存储规则")
    @ExcelProperty("文章页存储规则")
    private String saveRlues;

    @TableField(value = "SITENAME")
    @ApiModelProperty(value = "站点名")
    @ExcelProperty("站点名")
    private String siteName;

    @TableField(value = "SKUNUMBER")
    @ApiModelProperty(value = "唯一标识符")
    @ExcelProperty("唯一标识符")
    private String skuNumber;

    @TableField(value = "REMARK")
    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String remark;

    @TableField(value = "TABLEID")
    @ApiModelProperty(value = "元数据管理表Id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("元数据管理表Id")
    private Long tableId;

    @TableField(value = "METADATATYPE")
    @ApiModelProperty(value = "元数据类型")
    @ExcelProperty("元数据类型")
    private String metadataType;

    @TableField(value = "IMGCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("IMGCOLUMN")
    private Long imgColumn;

    @TableField(value = "FILECOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("FILECOLUMN")
    private Long fileColumn;

    @TableField(value = "VIDEOCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("VIDEOCOLUMN")
    private Long videoColumn;

    @TableField(value = "RADIOCOLUMN")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("RADIOCOLUMN")
    private Long audioColumn;

    @TableField(value = "LISTURL")
    @ExcelProperty("LISTURL")
    private String listUrl;

    @TableField(value = "EDITURL")
    @ExcelProperty("EDITURL")
    private String editUrl;

    @TableField(value = "DETAILURL")
    @ExcelProperty("DETAILURL")
    private String detailUrl;

    @TableField(value = "WORKFLOW")
    @ExcelProperty("工作流")
    private String workFlow;



    @TableField(exist = false)
    @ExcelIgnore
    private String parentName;
    @TableField(exist = false)
    @ExcelIgnore
    private String listtplName;
    @TableField(exist = false)
    @ExcelIgnore
    private String detailtplName;
    @TableField(exist = false)
    @ExcelIgnore
    private String edittplName;

    @ApiModelProperty(value = "接口地址")
    @TableField(exist = false)
    @ExcelIgnore
    private String metadataUri;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点集合")
    @ExcelIgnore
    private List<Programa> children1;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否为站点")
    @ExcelIgnore
    private Integer isSite=2;

    @TableField(exist = false)
    @ApiModelProperty(value = "树节点展开属性")
    @ExcelIgnore
    private boolean isParent;

    @TableField(exist = false)
    @ExcelIgnore
    private List<Long> conditionIds;

    public boolean getIsParent(){
        return isParent;
    }

    public void setIsParent(boolean isParent){
        this.isParent = isParent;
    }

    public String getIconSkin(){
        return isSite == 0?"columnTreeIcon1": isSite == 1? "columnTreeIcon2" : "columnTreeIcon3";
    }
}
