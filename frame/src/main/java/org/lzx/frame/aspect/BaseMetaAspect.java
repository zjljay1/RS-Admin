package org.lzx.frame.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BaseMetaAspect {

    @Pointcut("@annotation(org.lzx.common.annotation.BaseMetaInterface)")
    public void BaseMeta() {}

    public BaseMetaAspect() {
        log.info("CommonAspect init");
    }

    @Before("BaseMeta()")
    public void BaseMetaBefore(JoinPoint jp) {
        log.info("CommonAspect before");

    }
}
