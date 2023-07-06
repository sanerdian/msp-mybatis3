package com.jnetdata.msp.member.companyinfo.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.text.Format;
import java.util.Date;

@Data
@TableName("companyinfo")
@ApiModel(value="companyinfo对象", description="公司信息")
public class Companyinfo extends BaseEntity implements EntityId<Long> {

    @TableId(value = "companyid", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("id")
    private Long id;

    @TableField(value = "cpyname")
    @ApiModelProperty(value = "企业名称", example = "")
    @ExcelProperty("企业名称")
    private String name;

    @ApiModelProperty(value = "公司简介", example = "")
    @ExcelProperty("公司简介")
    private String cpydesc;

    @ApiModelProperty(value = "企业电话", example = "")
    @ExcelProperty("企业电话")
    private String cpytel;

    @ApiModelProperty(value = "注册时间", example = "2019-01-22")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regtime;

    @ApiModelProperty(value = "0 未开通  1 开通", example = "")
    @ExcelProperty("状态")
    private int status;

    @ApiModelProperty(value = "负责人", example = "")
    @ExcelProperty("负责人")
    private String principal;

    @ApiModelProperty(value = "有效期", example = "")
    @ExcelProperty("有效期")
    private int validity;

    @ApiModelProperty(value = "父机构id", example = "")
    @TableField("PARENT_ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "父机构", example = "")
    @ExcelProperty("父机构")
    @TableField("PARENT_NAME")
    private String parentName;

    @ApiModelProperty(value = "是 否 0、1 ", example = "")
    @ExcelProperty("ENABLE")
    private int enable;

    @ApiModelProperty(value = "购买站点数", example = "")
    @ExcelProperty("购买站点数")
    private int bsnum;

    @ApiModelProperty(value = "插件ID", example = "1,2,3")
    @ExcelProperty("插件ID")
    private String pqplug;

    @ApiModelProperty(value = "排序", example = "")
    @ExcelProperty("排序")
    private int cpyorder;

    @TableField(exist = false)
    @NotEmpty(message = "树节点展开属性")
    @ApiModelProperty(value = "树节点展开属性")
    @ExcelIgnore
    private boolean open;

    public String getIconSkin(){
        return "companyTreeIcon1";
    }

}
