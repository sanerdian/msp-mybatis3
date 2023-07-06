package com.jnetdata.msp.media.vo;

import lombok.Data;

//用户提供的单位
@Data
public class Lz_Dwjbxx {

    //单位标识码
    private String dwbsm;

    //单位编码(劳资)
    private String dwbm;

    //父单位标识码
    private String fdwbsm;

    //单位名称
    private String dwmc;

    //单位简称
    private String dwjc;

}
