package com.jnetdata.msp.metadata.precise.vo;


import lombok.Data;

@Data
public class PreciseVo {

    private Long id;

    private Long userid;

    private String name;

    private String sex;

    private int age;

    private String number;//年龄范围

    //职务
    private String office;

    //1为单位属性 0为人员属性 3为个人属性
    private int picname;

    //部门名称
    private String department;

    private Long departmentid;

    private int isdisplay; //1包含子部门，0不包含子部门

}
