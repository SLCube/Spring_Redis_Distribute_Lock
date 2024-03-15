package com.example.distribute_lock.service;

import com.example.distribute_lock.aop.annotation.RedissonLock;
import com.example.distribute_lock.entity.Book;
import com.example.distribute_lock.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @RedissonLock(value = "#bookId")
    public void purchase(final Long id, final long quantity) {
        Book book = bookRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        book.purchase(quantity);
    }
}
