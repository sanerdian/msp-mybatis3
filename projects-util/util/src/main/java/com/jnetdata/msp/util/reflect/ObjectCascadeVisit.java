package com.jnetdata.msp.util.reflect;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ObjectCascadeVisit {

    public static void visit(Object obj, Predicate<Class> predicate, Consumer visitor) {

        // 原始类型
        Class cls = obj.getClass();
        if (cls.isPrimitive()) {
            if (predicate.test(cls)) {
                visitor.accept(obj);
                return;
            }
        }

        // 数组
        if (cls.isArray()) {
            Object[] items = (Object[])obj;
            Arrays.stream(items).forEach( item ->  visit(item, predicate, visitor) );
            return;
        }

        // List类型
        if (obj instanceof List) {
            List list = (List)obj;
            list.forEach(item ->  visit(item, predicate, visitor));
            return;
        }

        // Map类型
        if (obj instanceof Map) {
            Map map = (Map)obj;
            map.forEach((k,v) -> visit(v, predicate, visitor));
            return;
        }

        // 其他类型(Fields)
        Field[] fields = cls.getDeclaredFields();
        for(Field field :  fields) {
            Object item = ReflectionUtils.getField(field, obj);
            visit(item, predicate, visitor);
        }

    }


}
