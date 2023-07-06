package com.jnetdata.msp.metadata.precise.entity;

import com.alibaba.fastjson.JSON;
import com.jnetdata.msp.metadata.precise.vo.PushCondition;
import com.jnetdata.msp.metadata.precise.vo.SimpleWorkerVo;
import com.jnetdata.msp.metadata.precise.vo.TreeNodeVo;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PushSettingVo {

    public static final String CONTAINSONDEPT_INCLUDE="含子部门";
    public static final String CONTAINSONDEPT_EXCLUDE="不含子部门";

    Long id;//新闻id
    String range;
    String containsondept;
    List<TreeNodeVo> groups=new ArrayList<>();//推送目标部门
    List<PushCondition> conditions=new ArrayList<>();//推送至分组的一组复合条件
    List<SimpleWorkerVo> users=new ArrayList<>();//推送目标用户
    Date pushExpireTime;
    String type;
  /*  MsgVo msg;//消息的配置信息*/


    public PushgroupModel toXinwen(){
        PushgroupModel item = new PushgroupModel();
        item.setXwid(id);
        item.setContainsondept(containsondept);
        item.setPushtorange(range);
        item.setPushtouser(users2Str());
        item.setPushtogroup(groups2Str());
        item.setPushtocondition(conditions2Str());
        item.setPushtotype(type);
        item.setCrtime(pushExpireTime);
        return item;
    }
    private String users2Str() {
        List<SimpleWorkerVo> _users = users!=null?users:new ArrayList<>();
        return JSON.toJSONString(_users);
    }
    private String groups2Str(){
        List<TreeNodeVo> _groups = groups!=null?groups:new ArrayList<>();
        return JSON.toJSONString(_groups);
    }
    private String conditions2Str(){
        List<PushCondition> _conditions = conditions!=null?conditions:new ArrayList<>();
        return JSON.toJSONString(_conditions);
    }


}
