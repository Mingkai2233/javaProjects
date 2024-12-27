package com.hmdp.exception;

import com.hmdp.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {
    @ExceptionHandler(PhoneNumInvalid.class)
    public Result handlePhoneNumInvalid(PhoneNumInvalid e) {
        log.error(e.toString(), e);
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(UnknownError.class)
    public Result handleUnknownError(UnknownError e) {
        log.error(e.toString(), e);
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(CommonException.class)
    public Result handleCommonException(CommonException e) {
        log.error(e.toString(), e);
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.fail("服务器异常");
    }
}
