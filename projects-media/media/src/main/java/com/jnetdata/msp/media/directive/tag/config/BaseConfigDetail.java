package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

@Data
public class BaseConfigDetail {
    public String field;
    public Long id;
    public String name;
    public String url;
    public Integer maxLength;
    public String getLink(String url,String name){
        return "<a href=\""+url+"\" target=\"_blank\">"+fixMaxLength(name)+"</a>";
    }
    public String fixMaxLength(String fieldValue){
        if(fieldValue!=null&&maxLength!=null&&maxLength>3&&fieldValue.length()>maxLength){
            String subStr=fieldValue.substring(0,maxLength-3);
            int start=subStr.lastIndexOf("<");//最后一个标签的开始位置
            int end = subStr.lastIndexOf(">");//最后一个标签的结束位置
            if(start>end){//如果字符串截取破环了标签，则将该标签完全去掉
                subStr=subStr.substring(0,start);
            }
            return subStr+"...";
        }else {
            return fieldValue;
        }
    }
}
