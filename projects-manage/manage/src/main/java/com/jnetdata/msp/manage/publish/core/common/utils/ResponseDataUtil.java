package com.jnetdata.msp.manage.publish.core.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Response data util.
 */
public class ResponseDataUtil {

    /**
     * Build binding result list.
     *
     * @param errorList the error list
     * @return the list
     */
    public static String buildBindingResult(List<ObjectError> errorList) {
        final List<String> list = new ArrayList<>();
        for (final ObjectError objectError : errorList) {
            list.add(objectError.getDefaultMessage());
        }
        return StringUtils.join(list, ",");
    }
}
