package com.facebook.user.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.facebook.user.exception.FacebookException;

@Component
@Aspect
public class LoggingAspect {
    public static final Log LOGGER =LogFactory.getLog(LoggingAspect.class);
    @AfterThrowing(pointcut = "execution(* com.facebook.user.service.*Impl.*(..))", throwing = "exception")
    public void logServiceException(FacebookException exception) {
        LOGGER.error(exception.getMessage(), exception);
    }
}