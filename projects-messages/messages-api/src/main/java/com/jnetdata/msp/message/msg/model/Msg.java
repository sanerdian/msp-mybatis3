package com.jnetdata.msp.message.msg.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.member.user.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TF on 2019/3/14.
 */

@Data
@TableName("CUSTOMERNEWSLOG")
@ApiModel(value = "消息管理实体类",description = "消息管理")
public class Msg extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "NEWSID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "NEWSTYPE")
    @NotEmpty(message = "信息类型")
    @ApiModelProperty(value = "信息类型")
    private Long type;

    @TableField(value = "BUSINESSTYPE")
    @NotEmpty(message = "业务类型")
    @ApiModelProperty(value = "业务类型")
    private Long busType;

    @TableField(value = "NREWSTITLE")
    @NotEmpty(message = "标题内容")
    @ApiModelProperty(value = "标题内容")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("标题")
    @ColumnWidth(15)
    private String title;

    @TableField(value = "ADDRESSEE")
    @NotEmpty(message = "接收用户id")
    @ApiModelProperty(value = "接收用户id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long receptionId;

    @TableField(value = "SENDER")
    @NotEmpty(message = "发送用户id")
    @ApiModelProperty(value = "发送用户id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long sendID;

    @TableField(value = "SENDSTATE")
    @NotEmpty(message = "发送状态")
    @ApiModelProperty(value = "发送状态")
    private Long sendState;

    @TableField(value = "JUMPURL")
    @ApiModelProperty(value = "跳转的url地址")
    private String jumpUrl;

    @TableField(value="SENDTIME")
    @ApiModelProperty(value="发送时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("接收时间")
    @ColumnWidth(22)
    private Date sendTime;

    @TableField(value = "NEWSCONTEXT")
    @NotEmpty(message = "信息内容")
    @ApiModelProperty(value = "信息内容")
    private String content;

    @TableField(value = "status")
    @NotEmpty(message = "读取状态")
    @ApiModelProperty(value = "读取状态")
    private Integer status;

    @TableField(value = "ADDRESSTYPE")
    @NotEmpty(message = "接收者类型")
    @ApiModelProperty(value = "接收者类型")
    private Integer addressType;

    @TableField(exist = false)
    @ApiModelProperty(value = "收件人类型")
    private String addressTypeName;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户读取状态")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("用户")
    @ColumnWidth(15)
    private String userids;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色读取状态")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("角色")
    @ColumnWidth(15)
    private String roleids;

    @TableField(exist = false)
    @ApiModelProperty(value = "组织读取状态")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("组织")
    @ColumnWidth(15)
    private String grpids;

    @TableField(exist = false)
    @ApiModelProperty(value = "发送用户名")
    @HeadFontStyle(fontHeightInPoints =10)
    @ExcelProperty("发件人")
    @ColumnWidth(10)
    private String sendUserName;

    @TableField(exist = false)
    @ApiModelProperty(value = "接收用户名")
    private String receptUserName;

    @TableField(exist = false)
    @ApiModelProperty(value = "邮件接收人")
    private String receiveMail;

    @TableField(value = "CONFIGID")
    @NotEmpty(message = "消息源类型")
    @ApiModelProperty(value = "消息源类型")
    private Long configId;

    @TableField(exist = false)
    @ApiModelProperty(value = "消息源配置类型")
    @HeadFontStyle (fontHeightInPoints =10)
    @ExcelProperty("消息类型")
    @ColumnWidth(10)
    private String msgSign;

    @TableField(value = "ISDISPLAY")
    @NotEmpty(message = "已发是否显示")
    @ApiModelProperty(value = "已发是否显示")
    private Integer isDisplay;

    @TableField(value = "NAMELIST")
    @NotEmpty(message = "接收人列表")
    @ApiModelProperty(value = "接收人列表")
    private String nameList;

    @TableField(value="PARENTID")
    @NotEmpty(message = "parentId")
    @ApiModelProperty(value = "parentId")
    private Long parentId;

    @TableField(exist=false)
    @ApiModelProperty(value = "companyNames")
    private String companyNames;

    @TableField(value = "SEND_RANGE")
    @NotEmpty(message = "发送范围")
   /* @ApiModelProperty(value = "发送范围")
    @HeadFontStyle (fontHeightInPoints =10)
    @ExcelProperty("范围")*/
    @ColumnWidth(10)
    private String sendRange;
    //private String range;

    @TableField(exist=false)
    private List<User> list;

    @TableField(exist=false)
    @ApiModelProperty(value = "子节点id集合")
    private List<Long> chileIds;

    public List<String> getChiledStrIds(){
        List<String> list = new ArrayList<>();
        if(chileIds == null){
            return list;
        }
        for (Long chileId : chileIds) {
            list.add(chileId+"");
        }
        return list;
    }
}
