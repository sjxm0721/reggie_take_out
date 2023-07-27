package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


//对统一的报错进行拦截处理

@RestControllerAdvice(annotations = {RestController.class, Controller.class}) //配置拦截路径（拦截加了RestController注解和Controller注解的请求方法）
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//针对SQLIN....这个报错的拦截方法
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)//针对一般报错的拦截方法
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }

}
