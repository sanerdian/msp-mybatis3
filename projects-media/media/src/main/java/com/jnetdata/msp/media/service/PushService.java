package com.jnetdata.msp.media.service;

import com.jnetdata.msp.media.vo.PushSettingVo;
import com.jnetdata.msp.media.vo.push.SimpleWorkerVo;
import com.jnetdata.msp.media.vo.push.TreeNodeVo;
import com.jnetdata.msp.media.vo.push.UserTagQueryVo;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.vo.BaseVo;
import org.thenicesys.data.api.Pager;

import java.util.List;
import java.util.Map;

public interface PushService {
    void saveXinwenPush(PushSettingVo vo);

//    List<Groupinfo> groupTree4CurrentUser();

    void saveSurveyPush(PushSettingVo vo);

    List<TreeNodeVo> loadDwRoot(String id);
    List<TreeNodeVo> loadDwByParent(String pid);

    List<TreeNodeVo> loadDwByName(String pid, String name);

    Pager<SimpleWorkerVo> pageWorker(BaseVo<SimpleWorkerVo> vo);

    Map<String, List<String>> queryUserTags(UserTagQueryVo vo);

    public String getPushedWorkers(PushSettingVo vo);
}
