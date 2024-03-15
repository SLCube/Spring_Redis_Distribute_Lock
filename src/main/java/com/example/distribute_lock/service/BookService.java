package com.example.distribute_lock.service;

import com.example.distribute_lock.entity.Book;
import com.example.distribute_lock.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final RedissonClient redissonClient;

    public void purchase(final Long bookId, final long quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(IllegalArgumentException::new);
        book.purchase(quantity);
    }
}
