package com.jnetdata.msp.media.vo;

import lombok.Data;

//用户提供的部门
@Data
public class Lz_Dwbsbmxx {

    //单位下属部门标识
    private String dwxxbmbs;

    //单位标识码
    private String dwbsm;

    //部门编码
    private String xsbmbm;

    //部门名称
    private String xsbmmc;

    //部门父编码(单位或部门)
    private String fbmbm;
    
}
