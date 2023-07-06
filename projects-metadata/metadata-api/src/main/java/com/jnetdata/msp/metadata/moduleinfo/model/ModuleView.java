package com.jnetdata.msp.metadata.moduleinfo.model;

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
@TableName("module_viewinfo")
@ApiModel(value = "模块视图管理实体类",description = "模块视图管理")
public class ModuleView implements EntityId<Long> {

    @TableId(value = "moduleid" , type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id" , hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "模板id", example = "XXX")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long moduleinfoid;

    @ApiModelProperty(value = "视图id", example = "XXX")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long viewid;

    @ApiModelProperty(value = "视图英文名称" , example = "XXX")
    private String modulename;

    @ApiModelProperty(value = "视图中文名称" , example = "XXX")
    private String moduleviewname;

    @ApiModelProperty(value = "创建时间" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date crtime;

}
