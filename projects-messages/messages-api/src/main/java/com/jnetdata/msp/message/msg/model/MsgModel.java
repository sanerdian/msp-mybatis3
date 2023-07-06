package com.jnetdata.msp.message.msg.model;

import lombok.Data;

/**
 * @ClassName MsgModel
 * @Description 模板消息内容实体类
 * @Author chenyan
 * @Date 2023/6/13 9:51
 */
@Data
public class MsgModel {

    private String keyword1;

    private String keyword2;

    private String keyword3;

    private String keyword4;

    private String keyword5;

    private String jumpUrl;

    private String openId;

    private String accessKeyId;

    private String secretAccessKey;

    private String templateId;
}
