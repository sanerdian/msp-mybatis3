package com.jnetdata.msp.metadata.viewtableinfo.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("view_tableinfo")
@ApiModel(value = "视图管理实体类",description = "视图管理")
public class ViewTable implements EntityId<Long> {

    @TableId(value = "VIEWID", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id",hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "元数据表id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long tableinfoid;

    @ApiModelProperty(value = "数据库表名", example = "JMETA_XXX")
    private String tablename;

    @ApiModelProperty(value = "视图值")
    private String viewname;

    @ApiModelProperty(value = "显示名称")
    private String tableviewname;

    @ApiModelProperty(value = "该视图表的主键名", example = "xxxid")
    private String pk;

    @ApiModelProperty(value = "视图描述说明")
    private String viewdesc;

    @ApiModelProperty(value = "生成代码时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date generatetime;

    @ApiModelProperty(value = "对象创建者")
    private String cruser;

    @ApiModelProperty(value = "对象创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date crtime;

    @ApiModelProperty(value = "创建人")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long crnumber;

    @ApiModelProperty(value = "模板文件名")
    private String modelfilename;

    @ApiModelProperty(value = "模板文件路径")
    private String modelfilepath;

    @TableField(exist = false)
    private String sql;

}
