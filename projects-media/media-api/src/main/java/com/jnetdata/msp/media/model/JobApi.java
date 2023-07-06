package com.jnetdata.msp.media.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import java.util.Date;

@Data
@TableName("job_api")
@ApiModel(description = "定时API任务")
public class JobApi extends BaseEntity implements EntityId<Long> {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = com.baomidou.mybatisplus.enums.IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "type")
    @ApiModelProperty(value = "类别：1新闻发布、2新闻撤稿、")
    private Integer type;

    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField(value = "siteid")
    @ApiModelProperty(value = "网站id")
    private Long siteid;

    @TableField(value = "columnid")
    @ApiModelProperty(value = "栏目id")
    private Long columnid;

    @TableField(value = "docid")
    @ApiModelProperty(value = "融媒体id")
    private Long docid;

    @TableField(value = "doctitle")
    @ApiModelProperty(value = "融媒体标题")
    private String doctitle;

    @TableField(value = "jobtime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "定时API任务执行时间")
    private Date jobtime;

//    @TableField(exist = false)
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date jobtimeBT2;

    @TableField(value = "requrl")
    @ApiModelProperty(value = "定时API任务的请求链接（已拼接了query等）")
    private String requrl;

    @TableField(value = "reqmethod")
    @ApiModelProperty(value = "定时API任务的请求Method（get/put/post等）")
    private String reqmethod;

    @TableField(value = "reqbody")
    @ApiModelProperty(value = "定时API任务的请求参数体")
    private String reqbody;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态：0未执行1请求成功2请求失败3执行异常")
    private Integer status;

    @TableField(value = "result")
    @ApiModelProperty(value = "定时任务执行结果")
    private String result;
}
