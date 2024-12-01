package org.example.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public String arithmeticExecptionHandler() {
        return "ArithmeticException!!!! ";
    }
}
