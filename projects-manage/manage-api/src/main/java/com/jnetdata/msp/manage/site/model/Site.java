package com.jnetdata.msp.manage.site.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.manage.column.model.Programa;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("WEBSITEINFO")
@ApiModel(value = "站点信息实体类",description = "站点信息")
public class Site extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "SITEID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("id")
    private Long id;

    @TableField(value = "COMPANYID")
    @NotEmpty(message = "所属企业id")
    @ApiModelProperty(value = "所属企业id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("所属企业id")
    private Long companyId;

    @TableField(value = "code")
    @NotEmpty(message = "唯一标识")
    @ApiModelProperty(value = "唯一标识")
    @ExcelProperty("唯一标识")
    private String code;

    @TableField(value = "SITENAME")
    @NotEmpty(message = "站点名称")
    @ApiModelProperty(value = "站点名称")
    @ExcelProperty("站点名称")
    private String name;

    @TableField(value = "SITEDESC")
    @NotEmpty(message = "备注")
    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String siteDesc;

    @TableField(value = "DATAPATH")
    @NotEmpty(message = "站点发布的目录")
    @ApiModelProperty(value = "站点发布的目录")
    @ExcelProperty("站点发布的目录")
    private String dataPath;

    @TableField(value = "WEBURL")
    @NotEmpty(message = "站点url")
    @ApiModelProperty(value = "站点url")
    @ExcelProperty("站点url")
    private String webUrl;

    @TableField(value = "webclass")
    @NotEmpty(message = "站点类型")
    @ApiModelProperty(value = "站点类型")
    @ExcelProperty("站点类型")
    private String webClass;

    @TableField(value = "STATUS")
    @NotEmpty(message = "创建状态")
    @ApiModelProperty(value = "创建状态")
    @ExcelProperty("创建状态")
    private Integer status;

    @TableField(value = "SITEORDER")
    @NotEmpty(message = "站点顺序")
    @ApiModelProperty(value = "站点顺序")
    @ExcelProperty("站点顺序")
    private Integer siteOrder;

    @TableField(value = "HOMETEMPLATEID")
    @NotEmpty(message = "首页模板id")
    @ApiModelProperty(value = "首页模板id")
    @ExcelProperty("首页模板id")
    private String homeTemplateId;

    @TableField(value = "DETAILTEMPLATEID")
    @NotEmpty(message = "细览模板id")
    @ApiModelProperty(value = "细览模板id")
    @ExcelProperty("细览模板id")
    private String detailTemplateId;

    @TableField(value = "SITESTATUS")
    @NotEmpty(message = "站点状态，0正常，1停用")
    @ApiModelProperty(value = "站点状态，0正常，1停用")
    @ExcelProperty("站点状态")
    private Integer sitestatus;

    @TableField(value = "MODULE_ID")
    @ApiModelProperty(value = "模块id")
    @ExcelProperty("模块id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long moduleId;

    @TableField(value = "MODULE_NAME")
    @ApiModelProperty(value = "模块名称")
    @ExcelProperty("模块名称")
    private String moduleName;

    @TableField(value = "LASTMODIFYTIME")
    @NotEmpty(message = "最后操作时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "最后操作时间")
    @ExcelProperty("最后操作时间")
    private Date lastModifyTime;

    @TableField(value = "SEOINFO")
    @NotEmpty(message = "seo描述")
    @ApiModelProperty(value = "seo描述")
    @ExcelProperty("seo描述")
    private String seoInfo;

    @TableField(value = "SEOTITLE")
    @NotEmpty(message = "seo标题")
    @ApiModelProperty(value = "seo标题")
    @ExcelProperty("seo标题")
    private String seoTitle;

    @TableField(value = "SEOKEYWORDS")
    @NotEmpty(message = "seo关键字")
    @ApiModelProperty(value = "seo关键字")
    @ExcelProperty("seo关键字")
    private String seoKeyWord;

    @TableField(value = "PLATFORM")
    @NotEmpty(message = "发布平台")
    @ApiModelProperty(value = "发布平台")
    @ExcelProperty("发布平台")
    private String platForm;

    @TableField(value = "YPUBLISH")
    @NotEmpty(message = "适合发布的类型")
    @ApiModelProperty(value = "适合发布的类型")
    @ExcelProperty("适合发布的类型")
    private String yPublish;

    @TableField(value = "CPYNAME")
    @NotEmpty(message = "企业信息表公司名称")
    @ApiModelProperty(value = "企业信息表公司名称")
    @ExcelProperty("企业信息表公司名称")
    private String cpyName;


    @TableField(exist = false)
    @NotEmpty(message = "子节点集合")
    @ApiModelProperty(value = "子节点集合")
    @ExcelIgnore
    private List<Programa> children1;
    @TableField(exist = false)
    @NotEmpty(message = "子节点集合")
    @ApiModelProperty(value = "子节点集合")
    @ExcelIgnore
    private List<Site> sites = new ArrayList<>();
    @TableField(exist = false)
    @NotEmpty(message = "是否为站点")
    @ApiModelProperty(value = "是否为站点")
    @ExcelIgnore
    private Integer isSite=1;
    @TableField(exist = false)
    @ApiModelProperty(value = "树节点展开属性")
    @ExcelIgnore
    private boolean isParent;
    @TableField(exist = false)
    @ApiModelProperty(value = "树节点展选择框是否显示")
    @ExcelIgnore
    private boolean nocheck = true;

    public boolean getIsParent(){
        return isParent;
    }

    public void setIsParent(boolean isParent){
        this.isParent = isParent;
    }

    public List<Site> getChildren(){
        return sites;
    }

    public String getIconSkin(){
        return isSite == 0?"columnTreeIcon1": isSite == 1? "columnTreeIcon2" : "columnTreeIcon3";
    }
}
