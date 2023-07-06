package com.jnetdata.msp.message.msg.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.message.msg.entity.MsgVoEntity;
import com.jnetdata.msp.message.msg.model.Msg;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * Created by TF on 2019/3/14.
 */
public interface MsgService extends BaseService<Msg> {
    /**
     * 更新信息状态（未读，已读）
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean setRead(Long[] ids);
    /**
     * 下载信息数据到excel
     * @param os
     * @param msgList
     * @param title
     * @param sheetName
     * @date 2020/5/26
     * @return
     */
    void export(ServletOutputStream os,List<Msg> msgList, String[] title,String sheetName);
    /**
     * 获取所有id
     * @param userId
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean setAllRead(Long userid);

    /**
     * 微信公众号发送模板消息
     * @param msg
     * @param userList1
     * @param WxNewsId
     */
    void extracted(Msg msg, List<User> userList1, Long WxNewsId);


    List<Msg> getMsgDataList();
    /**
     * 获取用户消息
     * @author 纪凯静
     * @date 2023/9/23
     * @return
     */
    List<Msg> getMsgList();
    List<Msg> getMsgExe(MsgVoEntity msgVoEntity);
}
