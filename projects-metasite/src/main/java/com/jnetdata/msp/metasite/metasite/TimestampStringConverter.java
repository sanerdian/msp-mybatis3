package com.jnetdata.msp.metasite.metasite;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampStringConverter implements Converter<Timestamp> {
 
    @Override
    public Class<?> supportJavaTypeKey() {
        return Timestamp.class;
    }
 
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Timestamp value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        CellData cellData = new CellData();
        String cellValue;
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            cellValue = DateUtils.format(new Date(value.getTime()));
        } else {
            cellValue = DateUtils.format(new Date(value.getTime()), contentProperty.getDateTimeFormatProperty().getFormat());
        }
        cellData.setType(CellDataTypeEnum.STRING);
        cellData.setStringValue(cellValue);
        cellData.setData(cellValue);
        return cellData;
    }
}
