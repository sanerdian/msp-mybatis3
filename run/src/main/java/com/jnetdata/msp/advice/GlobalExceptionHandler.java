package com.jnetdata.msp.advice;

import com.jnetdata.msp.core.model.util.NoCurrentUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.stream.Collectors;

/**
 * Controller异常全局处理
 *
 * @author Administrator
 * @date 2018/9/17
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonResult handleRuntimeException(RuntimeException e){
        if(e instanceof NoCurrentUserException){
            log.warn(subStackTrace(e,3,null));
        }else if(e instanceof NullPointerException||e instanceof IndexOutOfBoundsException){
            log.warn(subStackTrace(e,1,null));
        }else if(e instanceof NumberFormatException){
            log.warn(subStackTrace(e,null,".jnetdata."));
        }else{
            log.warn(e.getMessage(),e);
        }
        e.printStackTrace();
        return JsonResult.renderError(e.getMessage());
    }
    private String subStackTrace(Exception e,Integer totalLine,String containStr){
        StringBuilder builder=new StringBuilder();
        builder.append(e.getMessage());
        builder.append("\n").append(e.getClass().getName()).append(":").append(e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();
        if(!ObjectUtils.isEmpty(elements)){
            for(int index=0;index<elements.length;index++){
                if(totalLine!=null&&index>=totalLine){
                    break;
                }
                String element=elements[index].toString();
                builder.append("\n\tat ").append(element);
                if(!ObjectUtils.isEmpty(containStr)&&element.contains(containStr)){
                    break;
                }
            }
        }
        return builder.toString();
    }
    /**
     * 处理所有不可知的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handleException(Exception e){
        log.error(e.getMessage(), e);
        e.printStackTrace();
        if (e.getMessage().contains("select")||e.getMessage().contains("from")) {
            return JsonResult.renderError("系统错误，请上日志中查看！");
        }
        return JsonResult.renderError(e.getMessage());
    }

}
