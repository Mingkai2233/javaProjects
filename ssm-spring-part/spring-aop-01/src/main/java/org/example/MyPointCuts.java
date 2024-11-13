package org.example;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class MyPointCuts {
    @Pointcut("execution(* org..*.*(..))")
    public void myPointCut1(){
    }
}
