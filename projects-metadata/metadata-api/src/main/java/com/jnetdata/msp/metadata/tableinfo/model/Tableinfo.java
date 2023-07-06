package com.jnetdata.msp.metadata.tableinfo.model;

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
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@TableName("tableinfo")
@ApiModel(value = "元数据管理实体类",description = "元数据管理")
public class Tableinfo extends BaseEntity implements EntityId<Long> {

    @TableId(value = "tableinfoid", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "元数据名称/表名", example = "jmeta_xxx")
    @ExcelProperty("元数据名称表名")
    private String tablename;

    @ApiModelProperty(value = "中文名称", example = "xxx")
    @ExcelProperty("中文名称")
    private String anothername;

    @ApiModelProperty(value = "元数据类型({table:表,view:视图,tview:物化视图})", example = "xxx")
    @ExcelProperty("元数据类型")
    private String tabletype;

    @ApiModelProperty(value = "描述说明", example = "xxx")
    @ExcelProperty("描述说明")
    private String tabledesc;

    @ApiModelProperty(value = "所属模块类型", example = "1")
    @ExcelProperty("所属模块类型")
    private int ownertype;

    @ApiModelProperty(value = "所属模块编号", example = "1163701433499713537")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("所属模块编号")
    private Long ownerid;

    @ApiModelProperty(value = "可视化组件id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("可视化组件id")
    private Long visualid;

    @ApiModelProperty(value = "可视化组件类型id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("可视化组件类型id")
    private Long visualTypeid;

    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @TableField(exist = false)
    @ExcelIgnore
    private Date crtimeTo;

    @ApiModelProperty(value = "该元数据表的主键名", example = "xxxid")
    @ExcelProperty("该元数据表的主键名")
    private String pk;

    @ApiModelProperty(value = "视图sql", example = "xxxid")
    @ExcelProperty("视图sql")
    private String viewsql;

    //TODO 添加字段
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("数据源ID")
    private Long dbsourceid;




    @ApiModelProperty(value = "列表页jsp", example = "xxx.jsp")
    @ExcelIgnore
    private String listjsp;

    @ApiModelProperty(value = "页面路径", example = "1")
    @ExcelIgnore
    private String folderpath;

    @ApiModelProperty(value = "新增jsp", example = "xxx.jsp")
    @ExcelIgnore
    private String addjsp;

    @ApiModelProperty(value = "修改jsp", example = "xxx.jsp")
    @ExcelIgnore
    private String editjsp;

    @ApiModelProperty(value = "显示jsp", example = "xxx.jsp")
    @ExcelIgnore
    private String showjsp;

    @ApiModelProperty(value = "模板文件名", example = "xxx.xlsx")
    @ExcelIgnore
    private String modelfilename;

    @ApiModelProperty(value = "模板文件路径", example = "xxx.xlsx")
    @ExcelIgnore
    private String modelfilepath;

    @ApiModelProperty(value = "生成代码时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private Date generatetime;

    @ApiModelProperty(value = "引入元数据模块时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private Date introducetime;

    @ApiModelProperty(value = "创建引入时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @TableField(exist = false)
    @ExcelIgnore
    private Date introducetimeto;

    @ApiModelProperty(value = "引入元数据表的用户名称")
    @ExcelIgnore
    private String introduceuser;

    @ApiModelProperty(value = "生成代码的用户名称")
    @ExcelIgnore
    private String generateuser;

    @TableField(exist = false)
    @NotEmpty(message = "视图名称")
    @ApiModelProperty(value = "视图名称")
    @ExcelIgnore
    private String viewname;
    @TableField(exist = false)
    @ExcelIgnore
    private String count;
    @TableField(exist = false)
    @ExcelIgnore
    private String oldname;
    @ApiModelProperty(value = "搜索中文或英文名称")
    @TableField(exist = false)
    @ExcelIgnore
    private String searchName;

    public String getTablename() {
        if (StringUtils.isEmpty(tablename)) {
            return tablename;
        } else {
            return tablename.toUpperCase();
        }
    }

    public String getPk() {
        if (StringUtils.isEmpty(pk)) {
            return "ID";
        } else {
            return pk.toUpperCase();
        }
    }

}
