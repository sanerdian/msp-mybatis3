package com.jnetdata.msp.metadata.vo;

import org.apache.commons.lang3.StringUtils;
import lombok.Data;

/**
 * <p>
 * 表字段信息
 * </p>
 *
 * @author YangHu
 * @since 2016-12-03
 */
@Data
public class TableField {

    private String field;
    private String type;
    private Integer matchType;

    public int getDbType(){
        return DataTypeConvert.getDbType(type);
    }

    public int getFieldType(){
        return DataTypeConvert.getFieldType(type);
    }

    public String getPropertyName() {
        // 快速检查
        if (StringUtils.isEmpty(field)) {
            // 没必要转换
            return "";
        }
        String tempName = field;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (StringUtils.isAllUpperCase(field) || StringUtils.isMixedCase(field)) {
            tempName = field.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String camels[] = tempName.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (StringUtils.isEmpty(camel)) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        }
        return result.toString();
    }

    public String capitalFirst(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }
}
