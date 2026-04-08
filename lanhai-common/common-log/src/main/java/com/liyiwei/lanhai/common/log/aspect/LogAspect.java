package com.liyiwei.lanhai.common.log.aspect;

import com.liyiwei.lanhai.common.log.annotation.Log;
import com.liyiwei.lanhai.common.log.service.AsyncOperLogService;
import com.liyiwei.lanhai.common.log.utils.LogUtil;
import com.liyiwei.lanhai.model.entity.system.SysOperLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncOperLogService operLogService;


    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) {


        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);


        Object proceed = null;
        try {
            proceed = joinPoint.proceed();



            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,0,null);
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,1,e.getMessage());

            throw new RuntimeException();
        }


        operLogService.saveSysOperLog(sysOperLog);
        return proceed;
    }
}


