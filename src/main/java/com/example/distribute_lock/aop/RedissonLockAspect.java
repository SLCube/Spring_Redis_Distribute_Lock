package com.example.distribute_lock.aop;


import com.example.distribute_lock.aop.annotation.RedissonLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final TransactionAspect transactionAspect;

    @Around("@annotation(com.example.distribute_lock.aop.annotation.RedissonLock) && args(id, ..))")
    public Object redissonLock(ProceedingJoinPoint joinPoint, final Long id) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String className = joinPoint.getTarget().getClass().getSimpleName();

        String lockKey = String.format("%s : %s : %s", className, method.getName(), id.toString());
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean available = lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeunit());

            if (!available) {
                log.info("redisson getLock timeout");
                throw new IllegalArgumentException();
            }
            return transactionAspect.proceed(joinPoint);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
