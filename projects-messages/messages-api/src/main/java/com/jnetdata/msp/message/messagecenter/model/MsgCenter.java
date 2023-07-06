package com.jnetdata.msp.message.messagecenter.model;

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

/**
 * @author ZKJW
 */
@Data
@TableName("t_message")
@ApiModel(description = "消息或者通知")
public class MsgCenter extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 待办
     */
    public final static String MSG_TYPE_TODO = "1";

    /**
     * 通知
     */
    public final static String MSG_TYPE_NOTICE = "2";

    /**
     * 消息来源类型：需求审核被拒绝
     */
    public final static String SOURCE_TYPE_REFUSE_REQUIREMENT_AUDIT= "refuseRequirementAudit";

    /**
     * 消息来源类型：寻求合作
     */
    public final static String SOURCE_TYPE_COO_INTENTION = "CooperationIntention";

    /**
     * 消息来源类型：推荐简历
     */
    public final static String SOURCE_TYPE_INTENTION_RESUME = "IntentionResume";

    /**
     * 消息来源类型：系统推荐合作伙伴
     */
    public final static String SOURCE_TYPE_RECOMMEND_COMPANY= "RecommendCompany";

    /**
     * 消息来源类型：系统推荐简历
     */
    public final static String SOURCE_TYPE_RECOMMEND_RESUME = "RecommendResume";

    /**
     * 消息来源类型：合作伙伴
     */
    public final static String SOURCE_TYPE_COMPANY = "company";

    /**
     * 消息来源类型：简历
     */
    public final static String SOURCE_TYPE_RESUME = "resume";

    /**
     * 消息来源类型：需求
     */
    public final static String SOURCE_TYPE_REQUIREMENT = "requirement";

    /**
     * 消息来源类型：面试邀请
     */
    public final static String SOURCE_TYPE_INTERVIEW="Interview";

    /**
     * 消息来源类型：意向单
     */
    public final static String SOURCE_TYPE_PO  ="po";

    /**
     * 消息来源类型：生成意向单
     */
    public final static String SOURCE_TYPE_GENERATE_PO = "generatePurchaseOrder";

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型(1: 待办，2: 通知)",example = "1")
    @TableField(value = "msg_type")
    private String msgType;

    /**
     * 消息来源类型（合作意向单: 推荐简历, 采购意向单: 议价)
     */
    @ApiModelProperty(value = "消息来源类型")
    @TableField(value = "source_type")
    private String sourceType;

    /**
     * 消息来源Id
     */
    @ApiModelProperty(value = "消息来源Id",example = "101")
    @TableField(value = "source_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long sourceId;

    /**
     * 消息来源人Id (操作人)
     */
    @ApiModelProperty(value = "消息来源账号Id",example = "101")
    @TableField(value = "from_userid")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long fromUserId;

    /**
     * 消息目标Id
     */
    @ApiModelProperty(value = "消息目标Id",example = "102")
    @TableField(value = "target_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long targetId;

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题", example = "北京地区招聘JAVA工程师3名")
    @TableField(value = "msg_title")
    private String msgTitle;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", example = "需求邀请您推送合适人员的简历")
    @TableField(value = "msg_content")
    private String msgContent;

    /**
     * 消息时间
     */
    @ApiModelProperty(value = "消息时间",example = "2019-11-25")
    @TableField(value = "mst_date")
    @JSONField(format = "yyyy-MM-dd  HH:mm")
    private Date mstDate;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态(0:未读)(1:已读)",example = "0")
    @TableField(value = "status")
    private Integer status;

}
