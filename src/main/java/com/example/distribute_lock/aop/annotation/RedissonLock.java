package com.example.distribute_lock.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    String value();
    long waitTime() default 5L;

    long leaseTime() default 3L;

    TimeUnit timeunit() default TimeUnit.SECONDS;
}
