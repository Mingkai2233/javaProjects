package org.example;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.StringJoiner;

@Aspect
@Component
public class MyLog {

    @Before("org.example.MyPointCuts.myPointCut1()")
    public void before(JoinPoint jp) {
        String className = jp.getTarget().getClass().getSimpleName();
        String modifier = Modifier.toString(jp.getSignature().getModifiers());
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("Before");
        joiner.add(className);
        joiner.add(modifier);
        joiner.add(methodName);
        for (Object arg : args) {
            joiner.add(String.valueOf(arg));
        }
        System.out.println(joiner.toString());
    }

    @After("org.example.MyPointCuts.myPointCut1()")
    public void after(){
        System.out.println("after");
    }
}
