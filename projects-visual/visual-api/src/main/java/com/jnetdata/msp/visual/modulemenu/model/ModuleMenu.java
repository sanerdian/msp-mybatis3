package com.jnetdata.msp.visual.modulemenu.model;

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
@TableName("j_module_menu")
@ApiModel(value="菜单组件实体类", description="菜单组件")
public class ModuleMenu extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "menu_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "栏目id")
    @TableField("site_id")
    private Long siteId;

    @ApiModelProperty(value = "机构id")
    @TableField("organ_id")
    private Long organId;

    @ApiModelProperty(value = "菜单id")
    @TableField("cmenu_id")
    private Long menuId;

    @ApiModelProperty(value = "数据展示区域id")
    @TableField("data_area_id")
    private String dataAreaId;

    @ApiModelProperty(value = "数据模板id")
    @TableField("data_tpl_id")
    private String dataTplId;

    @ApiModelProperty(value = "选择栏目")
    @TableField("choose_menu")
    private String chooseMenu;

    @ApiModelProperty(value = "栏目级别")
    @TableField("choose_level")
    private String chooseLevel;


}
