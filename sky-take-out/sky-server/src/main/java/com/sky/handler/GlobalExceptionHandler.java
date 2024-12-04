package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<Object> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] ss = message.split(" ");
            String name = ss[2];
            String msg = name+"已经存在";
            return Result.error(msg);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
    @ExceptionHandler(IOException.class)
    public Result<Object> ioExceptionHandler(IOException ex){
        log.error("IO异常：{}", ex.getMessage());
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result<Object> arithmeticExceptionHandler(ArithmeticException ex){
        log.error("算术异常：{}", ex.getMessage());
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
//    @ExceptionHandler(Exception.class)
//    public Result<Object> otherExceptionHandler(Exception ex){
//        log.error("其他捕获的异常：{}", ex.getMessage());
//        return Result.error(MessageConstant.UNKNOWN_ERROR);
//    }
}
