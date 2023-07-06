package com.jnetdata.msp.util.validator;


import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

/**
 * @author: ZKJW
 * @date: 2019/6/17
 */
public class ValidateUtil {

    /**
     * 验证列表
     * @param list 数据列表
     * @param <T>  泛型：验证的类型
     */
    public static <T> void doValidateList(List<T> list) {
        for (T t : list) {
            doValidateObject(t);
        }
    }

    /**
     * 验证对象
     * @param object 对象
     * @param <T>  泛型：验证的类型
     */
    public static <T> void doValidateObject(T object) {
        Set<ConstraintViolation<T>> set = ValidationUtils.validateEntity(object);
        doProcessValidateResult(set);
    }

    /**
     * 处理验证结果
     * @param set 验证结果集
     * @param <T> 泛型：验证的类型
     */
    private static <T> void doProcessValidateResult(Set<ConstraintViolation<T>> set) {
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (ConstraintViolation<T> cv : set) {
            throw new RuntimeException(cv.getMessage());
        }
    }

}
