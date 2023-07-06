package com.jnetdata.msp.core.dbsource.model;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * <p>
 * dbSource
 * </p>
 *
 * @author zyj
 * @since 2022-12-06
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("j_dbsource")
@ApiModel(value = "DbSource对象", description = "dbSource")
public class DbSource extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;
    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @TableField("STATUS")
    private Integer status;
    @TableField("SEQENCING")
    private Integer seqencing;
    @ExcelProperty(value = "名称")
    @ApiModelProperty(value = "名称")
    @TableField("NAME")
    private String name;
    @ExcelProperty(value = "ip")
    @ApiModelProperty(value = "ip")
    @TableField("IP")
    private String ip;
    @ExcelProperty(value = "端口")
    @ApiModelProperty(value = "端口")
    @TableField("PORT")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer port;
    @ExcelProperty(value = "账号")
    @ApiModelProperty(value = "账号")
    @TableField("ACOUNT")
    private String acount;
    @ExcelProperty(value = "密码")
    @ApiModelProperty(value = "密码")
    @TableField("PW")
    private String pw;

    //TODO 添加字段
    @ExcelProperty(value = "类型")
    @ApiModelProperty(value = "类型")
    @TableField("TYPE")
    private String type;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Object list;

    public String getIndex(){
        return this.getName();
    }

}
