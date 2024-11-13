package org.example;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyLog {

    @Before("execution(* org..*.*(..))")
    public void before() {
        System.out.println("before");
    }

    @After("execution(* org..*.*(..))")
    public void after(){
        System.out.println("after");
    }
}
