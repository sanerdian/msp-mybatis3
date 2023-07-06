package com.jnetdata.msp.visual.moduletextlist.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

@Data
@TableName("j_module_textlist")
@ApiModel(value="ModuleTextlist", description="文字列表组件表")
public class ModuleTextlist extends ModuleObject implements EntityId<Long>  {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "list_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "分页设置，是否显示分页，0-否，1-是")
    @TableField("page_setup")
    private String pageSetup;

    @ApiModelProperty(value = "每页显示数据条数")
    @TableField("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;

    @ApiModelProperty(value = "栏目名设置，是否显示栏目名，0-否，1-是")
    @TableField("channel_setup")
    private String channelSetup;

    @ApiModelProperty(value = "关联的栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField("channel_id")
    private Long channelId;

    @ApiModelProperty(value = "分页区域id")
    @TableField("page_area_id")
    private String pageAreaId;
}
