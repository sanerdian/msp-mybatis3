package com.jnetdata.msp.util.property;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: ZKJW
 * @date: 2019/7/29
 */
public class PropertyUtil {


    /**
     * 清除属性值
     * @param list 实体列表
     * @param clearProperties 属性列表
     * @param <T> 泛型
     */
    @SneakyThrows
    public static  <T>  void clearProperties(List<T> list, List<String> clearProperties){
        for(T t : list){
            clearProperties.forEach(property->{
                try {
                    Field field = t.getClass().getDeclaredField(property);
                    field.setAccessible(true);
                    field.set(t,null);
                }catch (Exception e){

                }
            });
        }
    }

    /**
     * 拷贝属性值
     * @param list 实体列表
     * @param copyProperties 属性列表
     * @param <T> 泛型
     */
    @SneakyThrows
    public static  <T>  void copyProperties(List<T> list, List<String> copyProperties){
        if(list.size()==0){
            return;
        }
        Field[] declaredFields = list.get(0).getClass().getDeclaredFields();
        List<String> clearProperties = new ArrayList<>();
        Arrays.asList(declaredFields).forEach(field -> {
            if(!copyProperties.contains(field.getName())){
                clearProperties.add(field.getName());
            }
        });
        clearProperties(list,clearProperties);
    }
}
