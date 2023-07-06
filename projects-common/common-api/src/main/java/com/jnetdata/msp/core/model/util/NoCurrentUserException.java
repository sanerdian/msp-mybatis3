package com.jnetdata.msp.core.model.util;


import lombok.Data;

/**
 *
 * @author 轻舞飞扬
 * @date 2019/2/27
 */
@Data
public class NoCurrentUserException extends RuntimeException{
    /**
     * 状态码
     */
    private String status;
    /**
     * 异常信息
     */
    private String message;

    public NoCurrentUserException(){
        this.status="511";
        this.message="登陆超时,请重新登陆";
    };
}
