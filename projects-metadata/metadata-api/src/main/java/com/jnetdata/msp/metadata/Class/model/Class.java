package com.jnetdata.msp.metadata.Class.model;

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
import java.util.List;

@Data
@TableName("class")
@ApiModel(value = "分类管理实体类",description = "分类管理")
public class Class extends BaseEntity implements EntityId<Long> {
    @TableId(value = "classid",type = IdType.ID_WORKER )
    @ApiModelProperty(value = "主键id",hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "分类法名称",example = "")
    @TableField(value = "classname")
    @ExcelProperty("分类法名称")
    private String className;

    @ApiModelProperty(value = "分类法描述",example = "")
    @TableField(value = "CLASSDESC")
    @ExcelProperty("分类法描述")
    private String classDesc;

    @ApiModelProperty(value = "",example = "")
    @ExcelProperty("CODE")
    private String classCode;

    @ApiModelProperty(value = "父节点id",example = "")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty(value = "图标url",example = "")
    @TableField("ICON_URL")
    @ExcelProperty("图标url")
    private String iconUrl;

    @ApiModelProperty(value = "图标路径",example = "")
    @TableField("ICON_PATH")
    @ExcelProperty("图标路径")
    private String iconPath;

    @ApiModelProperty(value = "所属模块名称",example = "")
    @TableField("MODULE_NAME")
    @ExcelProperty("所属模块名称")
    private String moduleName;

    @ApiModelProperty(value = "所属模块id",example = "")
    @TableField("MODULE_ID")
    @ExcelProperty("所属模块id")
    private Long moduleId;

    @ApiModelProperty(value = "编码规则",example = "")
    @TableField("BMNAME")
    @ExcelProperty("编码规则")
    private String bmname;

    @ApiModelProperty(value = "匹配标签词组",example = "")
    @TableField("MATCHG")
    @ExcelProperty("匹配标签词组")
    private String matchg;

    @ApiModelProperty(value = "分类法编码id",example = "")
    @TableField("METHODID")
    @ExcelProperty("分类法编码id")
    private String methodid;

    @ExcelProperty("排序")
    private Integer seqencing;

    @TableField(exist = false)
    @NotEmpty(message = "子节点集合")
    @ApiModelProperty(value = "子节点集合")
    @ExcelIgnore
    private List<Class> children;

    @TableField(exist = false)
    @ApiModelProperty(value = "树节点展开属性")
    @ExcelIgnore
    private boolean isParent;

    public boolean getIsParent(){
        return isParent;
    }

    public void setIsParent(boolean isParent){
        this.isParent = isParent;
    }

    public String getIconSkin(){
        return parentId == null || parentId.equals(0L)?"classTreeIcon1":"classTreeIcon2";
    }

}
