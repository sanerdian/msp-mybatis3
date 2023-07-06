package com.jnetdata.msp.metadata.fieldinfo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class ExcleTableVo {
    /*@ExcelProperty(value = "中文名称")*/
    @ColumnWidth(25)
    private String tableChinaName;
  /*  @ExcelProperty(value = "英文名称")*/
  @ColumnWidth(20)
    private String tableName;
    /*@ExcelProperty(value = "字段类型")*/
    @ColumnWidth(15)
    private String fieldType;
   /* @ExcelProperty(value = "库中类型")*/
   @ColumnWidth(15)
    private String libraryType;
    @ColumnWidth(15)
    /*@ExcelProperty(value = "库中长度")*/
    private String libraryLength;

}
