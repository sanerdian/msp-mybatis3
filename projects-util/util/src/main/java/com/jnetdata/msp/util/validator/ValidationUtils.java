package com.jnetdata.msp.util.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @author Administrator
 */
public class ValidationUtils {

    private static Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 验证对象
     * @param obj 对象
     * @param <T> 泛型
     * @return 验证结果
     */
    public static <T> Set<ConstraintViolation<T>> validateEntity(T obj){
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        return set;
    }

    /**
     * 验证对象指定的属性
     * @param obj 对象
     * @param propertyName 属性名
     * @param <T> 泛型
     * @return 验证结果
     */
    public static <T> Set<ConstraintViolation<T>> validateProperty(T obj, String propertyName){
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj,propertyName,Default.class);
        return set;
    }


}
