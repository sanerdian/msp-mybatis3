package com.jnetdata.msp.manage.publish.core.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 *
 */
@Data
@Builder
public class MonitorVO extends BaseEntity implements EntityId<Long> {

    @ApiModelProperty(value = "MonitorID", hidden = true)
    @TableId(value = "Monitor", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public enum MONITORSTATUS {
        wait, doing, error, finish
    }

    /**
     * 监控名称
     */
    private String monitorName;
    /**
     * 发布类型
     */
    private int publicationType;
    /**
     * 发布状态
     * {@link MONITORSTATUS}
     */
    private int monitorStatus;
    /**
     * 发布人
     */
    private String postedBy;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 总用时间
     */
    private long totalTime;
    /**
     * 站点编号
     */
    private long siteId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 页面地址
     */
    private String pageAddress;
    /**
     * 提交时间
     */
    private Date submitTime;
    /**
     * 发布页面
     */
    private String postPage;
    /**
     * 详细描述
     */
    private String description;
    /**
     * 间隔时间
     */
    private String repeatInterval;
    /**
     * 间隔时间字符串
     */
    private String jtime;
}
