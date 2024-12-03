package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointCut1(){}

    @Before("pointCut1()")
    public void autoFill(JoinPoint jp) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("Auto filling...");
        // 先提取据库操作的类型
        MethodSignature signature = (MethodSignature)jp.getSignature();
        AutoFill autoFillAnnotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType type = autoFillAnnotation.value();
        //然后获得参数
        Object[] args = jp.getArgs();
        Object entity = args[0];
        //根据操作类型对参数进行填充
        LocalDateTime now = LocalDateTime.now();
        Long curId = BaseContext.getCurrentId();
        if (type == OperationType.INSERT) {
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, curId);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, curId);
        }
        else if (type == OperationType.UPDATE){
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, curId);

        }

    }
}
