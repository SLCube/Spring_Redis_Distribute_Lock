package com.example.distribute_lock.service.facade;

import com.example.distribute_lock.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookLockFacade {

    private final BookService bookService;
    private final RedissonClient redissonClient;

    public void purchase(final Long bookId, final int quantity) {
        RLock lock = redissonClient.getLock(String.format("purchase:book:%d", bookId));

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("redisson getLock timeout");
                return;
            }

            bookService.purchase(bookId, quantity);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
