package com.jnetdata.msp.metadata.tnetwork.entity;

import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

@Data
public class UserVeo {
    /*private String birthDate;*/
   /* private String educationbackground;
    private Long id;
    private String sex;
    private String politicsstatus;
    private String truename;*/


    private PageVo1 pager;

    private UserVoEntity entity;

    private String ids;

    private int status;
}
