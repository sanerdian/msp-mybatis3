package com.jnetdata.msp.core.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/8/28
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人名称
     */
    @ExcelProperty("创建人")
    @ColumnWidth(15)
    @TableField(value="CRUSER", fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建人名称(系统会自动维护）")
    private String crUser;

    /**
     * 修改人名称
     */
    @ExcelProperty("操作人")
    @ColumnWidth(15)
    @TableField(value="MODIFY_USER", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="最后修改人名称(系统会自动维护）")
    private String modifyUser;

    /**
     * 创建人id
     */
    @TableField(value="CRNUMBER", fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建人id(系统会自动维护）")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("创建人id")
    private Long createBy;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(value="CRTIME", fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建时间搜索")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @TableField(exist = false)
    @ExcelIgnore
    private Date createTimeBt2;

    /**
     *  最后修改人id
     */
    @ApiModelProperty(value = "最后修改人id", hidden = true)
    @TableField(value="MODIFY_BY",fill = FieldFill.INSERT_UPDATE)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("最后修改人id")
    private Long modifyBy;

    /**
     * 最后修改时间
     */
    @ExcelProperty("操作时间")
    @ColumnWidth(22)
    @ApiModelProperty(value = "最后修改时间", hidden = true)
    @TableField(value="MODIFY_TIME",fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "创建时间搜索")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @TableField(exist = false)
    @ExcelIgnore
    private Date modifyTimeBt2;
//
//    @ApiModelProperty(value = "查询字段")
//    @TableField(exist = false)
//    @ExcelIgnore
//    private List<String> fields;

}
