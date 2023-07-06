package com.jnetdata.msp.metadata.fieldinfo.vo;

import lombok.Data;

/**
 * 功能描述：
 */
@Data
public class WordVO {
    private String tableChinaname;//中文描述
    private String tableEnglishName;//英文描述
    /*@ExcelProperty(value = "字段类型")*/
    private String fieldType;
    /* @ExcelProperty(value = "库中类型")*/
    private String libraryType;
    /*@ExcelProperty(value = "库中长度")*/
    private String libraryLength;
}
