package com.jnetdata.msp.flowable.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public interface FlowMetadataMapper extends BaseMapper<FlowMetadata> {

    /**
     * 更新元数据信息
     * 部分数据为空时置空
     */
    void updateMetadata(Map map);

    /**
     * 更新元数据信息
     * 选择性更新，不会置空
     */
    void updateOnly(Map map);

    /**
     * 添加通知消息
     */
    void addMsg(Map map);

    /**
     * 获取元数据公共信息
     */
    MetaPub singleMetaPub(Map map);

    /**
     * 删除草稿箱数据
     */
    void deleteDraftbox(Map map);

    /**
     * 获取流水号最大值
     */
    Integer getMaxSerialNumber();
}
