package com.jnetdata.msp.metadata.fieldinfo.vo;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 功能描述：
 */
@Data
public class WordEntity {
    private String tableName;//表名
    private String majorkey;//主键
    private String foreignKey; //外键
    private WordVO wordVO;


}
