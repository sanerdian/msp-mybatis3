package com.jnetdata.msp.media.vo;

import com.jnetdata.msp.media.vo.push.SimpleWorkerVo;
import com.jnetdata.msp.media.vo.push.TreeNodeVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PushSettingVo {
    Long id;//新闻id
    String range;
    List<TreeNodeVo> groups=new ArrayList<>();//推送目标部门
    List<PushCondition> conditions=new ArrayList<>();//推送至分组的一组复合条件
    List<SimpleWorkerVo> users=new ArrayList<>();//推送目标用户
    MsgVo msg;//消息的配置信息
}
