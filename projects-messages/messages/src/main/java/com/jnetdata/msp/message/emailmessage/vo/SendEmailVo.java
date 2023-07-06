package com.jnetdata.msp.message.emailmessage.vo;

import lombok.Data;

import java.io.File;

/**
 * 功能描述：
 */
@Data
public class SendEmailVo {

    //发送地址
    private String toMail;

    //标题
    private String title;


    //发送内容
    private String cont;

    //文件对象
    private File fileList;
}
