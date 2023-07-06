package com.jnetdata.msp.message.msg.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

@Data
public class MsgVoEntity {
    private Long id;
    private String s1;
    private String educationbackground;
    private String politicsstatus;
    private int ageForBirthday;
    private String name;
    private String cruser;
    private String title;
    private Integer size;
    private Integer current;
}
