package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
public class BaseConfigList {
    public Integer num;//返回的最大条目数
    public String order;//排序字段
    public String ordertype="DESC";//排序方式
    public List<Pair<String, Boolean>> getOrderBy(){
        List<Pair<String, Boolean>> orderBys= new ArrayList<>();
        if(!ObjectUtils.isEmpty(order)){
            Pair<String, Boolean> orderBy= new Pair<>();
            orderBy.setKey(order);
            orderBy.setValue(ordertype.equalsIgnoreCase("asc"));
            orderBys.add(orderBy);
        }
        return orderBys;
    }
    public static String getPager(int current,int pageSize,int total,String urlFirst){
        int pageMax;
        if(total%pageSize>0){
            pageMax=total/pageSize+1;
        }else {
            pageMax=total/pageSize;
        }
        int start,end;//页码范围
        if(pageMax<=10){
            start=1;end=pageMax;
        }else if(current<6){
            start=1;end=10;
        }else if(pageMax-current>4){
            start=current-4;
            end=current+5;
        }else {
            start=pageMax-9;
            end=pageMax;
        }
//        System.out.println(current+"["+start+","+end+"]");
        String url;
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div class=\"page\">");
        buffer.append("<span class=\"page_total\">共").append(pageMax).append("页,").append(total).append("条数据</span>&nbsp&nbsp;");
        buffer.append(pageSize).append("条/页</span>&nbsp;&nbsp;");
        for(int index=start;index<=end;index++){
            url=getUrl(urlFirst,index);
            if(index!=current){
                buffer.append("<a href=\"").append(url).append("\" class=\"page_item\">").append(index).append("</a>");
            }else {
                buffer.append("<a href=\"").append(url).append("\" class=\"page_current\"><Strong>").append(index).append("</Strong></a>");
            }
        }
        buffer.append("</div>");
        return buffer.toString();
    }

    /**
     * 用第一页的地址生成指定页的页码假定第一页为/a/b/index.htm，则指定第N页为/a/b/index_N.htm
     * @param urlFirst 第一页的地址
     * @param index 指定页码
     * @return
     */
    private static String getUrl(String urlFirst,int index) {
        if(ObjectUtils.isEmpty(urlFirst)||!urlFirst.contains(".")){//文件名为空或不包含后缀，则直接返回空字符串
            return "";
        }else if(index<=1){//提供的就是第一页
            return urlFirst;
        }else {
            int position=urlFirst.lastIndexOf(".");//第一步已经将不含后缀的处理了
            return urlFirst.substring(0,position)+"_"+index+urlFirst.substring(position);
        }
    }

}
