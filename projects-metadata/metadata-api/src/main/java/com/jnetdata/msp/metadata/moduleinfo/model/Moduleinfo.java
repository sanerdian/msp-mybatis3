package com.jnetdata.msp.metadata.moduleinfo.model;

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

import java.util.Date;

@Data
@TableName("moduleinfo")
@ApiModel(value = "模块管理实体类",description = "模块管理")
public class Moduleinfo extends BaseEntity implements EntityId<Long> {

    @TableId(value = "moduleinfoid" , type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id" , hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("id")
    private Long id;

    @ApiModelProperty(value = "英文名称")
    @ExcelProperty("英文名称")
    private String englishname;

    @ApiModelProperty(value = "模块名称")
    @ExcelProperty("模块名称")
    private String modulename;

    @ApiModelProperty(value = "模块描述" )
    @ExcelProperty("模块描述")
    private String moduledesc;

    @ApiModelProperty(value = "排序" )
    @ExcelProperty("排序")
    private int moduleorder;

    @ApiModelProperty(value = "搜索中文或英文名称")
    @TableField(exist = false)
    @ExcelIgnore
    private String searchName;

    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @TableField(exist = false)
    @ExcelIgnore
    private Date crtimeTo;


}
