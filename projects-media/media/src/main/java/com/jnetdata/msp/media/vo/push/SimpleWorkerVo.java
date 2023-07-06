package com.jnetdata.msp.media.vo.push;

import lombok.Data;

@Data
public class SimpleWorkerVo {
    String gzzh;//工作证号
    String sfzh;//身份证号
    String xm;//姓名
    String ljmc;//路局名称
    String dwbsm;//单位标识码
    String dwmc;//单位名称
    String bmbm;//部门编码
    String xsbmmc;//部门名称（一个单位下有很多个下属部门）

    String xwhcdhz;//学历
    String sxzy;//所学专业
    String zylb;//专业类别
    String zzmm;//政治面貌
    String zwmc;//职务
    String xjszwmc;//职称
    String jsdj;//技术等级
    String szbz;//班组
    String bzzlx;//班组长类型
    String gz;//工种

    String birthday;
    String sex;
    String age;

    String externalid;
}
