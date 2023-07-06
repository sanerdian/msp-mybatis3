package com.jnetdata.msp.media.util;

//import com.baomidou.mybatisplus.annotations.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Object与Map互相转换！
 * 此处引入几种特殊情况：
 * a、Object也有可能是Map；
 * b、Map的key与Object的field的名称可能大小写不同！
 * c、Object的field的名称与数据库中的对应的表中的字段不一致（考虑注解TableField）（未实现，有时间再实现）
 */
public class ReflactUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //1、object转换成map。加入补充：本身object已经是map时，只把key转成字符串
    public static Map<String, Object> obj2Map(Object obj, boolean keyLowerCase){
        Map<String, Object> map = new HashMap<String, Object>();
        if(obj==null){
            return map;
        }

        if(obj instanceof Map){
            Map map_=(Map) obj;
            Iterator iterator = map_.keySet().iterator();
            while(iterator.hasNext()){
                Object key_ = iterator.next();
                String key = keyLowerCase?String.valueOf(key_).toLowerCase():String.valueOf(key_);
                Object value=map_.get(key_);
                map.put(key,value);
            }
        }else {
            Field[] fields = getAllField(obj.getClass());
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    String key=keyLowerCase?field.getName().toLowerCase():field.getName();
                    map.put(key, field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Object转map失败！");
                }
            }
        }
        return map;
    }

    //2、map转换为object

    public static Object map2Obj(Map map, Class<?> clz,boolean ignoreCharCase){
        if(Map.class.isAssignableFrom(clz)){//如果目标是map，则直接返回
            return map;
        }
        Object obj = null;
        Map<String,Object>  valueMap=new HashMap<>();
        if(ignoreCharCase){
            for(Object key:map.keySet()){
                valueMap.put(String.valueOf(key).toLowerCase(),map.get(key));//忽略大小写时Map中的key都已转为小写
            }
        }else {
            for(Object key:map.keySet()){
                valueMap.put(String.valueOf(key),map.get(key));
            }
        }
        try {
            obj = clz.newInstance();
            Field[] declaredFields = getAllField(obj.getClass());
            for (Field field : declaredFields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                Object value;
                if(ignoreCharCase){
                    value=valueMap.get(field.getName().toLowerCase());//忽略大小写时Map中的key都已转为小写
                }else {
                    value=valueMap.get(field.getName());
                }
                if(value!=null){
                    field.set(obj, value);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("map转Object失败！");
        }
        return obj;
    }

    /**
     * 用Map作为中间过渡，将对象转换为新的对象
     * @param object
     * @param clz
     * @param ignoreCharCase
     * @return
     */
    public static Object obj2obj(Object object, Class<?> clz,boolean ignoreCharCase){
        Map<String, Object> map = obj2Map(object, ignoreCharCase);
        Object result = map2Obj(map, clz, ignoreCharCase);
        return result;
    }
    /**
     * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
     * @param clazz
     * @return Field数组
     */
    public static Field[] getAllField(Class<?> clazz) {
        ArrayList<Field> fieldList = new ArrayList<Field>();
        Field[] dFields = clazz.getDeclaredFields();
        if (null != dFields && dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            Field[] superFields = getAllField(superClass);
            if (null != superFields && superFields.length > 0) {
                for(Field field:superFields){
                    if(!isContain(fieldList, field)){
                        fieldList.add(field);
                    }
                }
            }
        }
        Field[] result=new Field[fieldList.size()];
        fieldList.toArray(result);
        return result;
    }

    /**检测Field List中是否已经包含了目标field
     * @param fieldList
     * @param field 带检测field
     * @return
     */
    public static boolean isContain(ArrayList<Field> fieldList, Field field){
        for(Field temp:fieldList){
            if(temp.getName().equals(field.getName())){
                return true;
            }
        }
        return false;
    }
    public static String getFieldStringValue(Object object,String fieldName){
        Object value=getFieldValue(object,fieldName);
        String result="";
        if(object!=null){
            if(value instanceof Date){
                Date date= (Date) value;
                result=dateFormat.format(date);
            }else {
                result =value.toString();
            }
        }
        return result;
    }
    public static Object getFieldValue(Object object, String fieldName){
        if(object==null){
            return null;
        }
        else if(object instanceof Map){
            Map map = (Map) object;
            return map.get(fieldName);
        }else {
            Object value=null;
            Field[] allField = getAllField(object.getClass());
            for(Field field:allField){
                if(field.getName().equalsIgnoreCase(fieldName)){
                    try{
                        field.setAccessible(true);
                        value=field.get(object);
                    }catch (IllegalAccessException e){
                        e.printStackTrace();//无需特别处理！异常=取值失败！则值为null
                    }
                }
            }
            return value;
        }
    }

    /**
     * 比较object与clz的所有属性，如果object的某个属性在clz中也有，则取出object的该属性及其取值并放到结果Map中
     * @param object
     * @param clz
     * @param ignoreCharCase
     * @return
     */
    public static Map<String,Object> getFieldsAndValues(Object object,Class<?> clz,boolean ignoreCharCase){
        Map<String, Object> map = obj2Map(object, ignoreCharCase);
        Field[] allField = getAllField(clz);
        Map<String,Object> result= new HashMap<>();
        for(Field field:allField){
            String key=ignoreCharCase?field.getName().toLowerCase():field.getName();
            if(map.containsKey(key)){
                Object value=map.get(key);
                if(value!=null){
                    result.put(key,value);
                }
            }
//            if(field.isAnnotationPresent(TableField.class)){
//                TableField tableField = field.getAnnotation(TableField.class);
//                key=ignoreCharCase?tableField.value().toLowerCase():tableField.value();
//                if(map.containsKey(key)){
//                    Object value=map.get(key);
//                    if(value!=null){
//                        result.put(key,value);
//                    }
//                }
//            }

        }
        return result;
    }
}

