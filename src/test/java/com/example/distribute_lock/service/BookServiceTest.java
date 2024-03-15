package com.example.distribute_lock.service;

import com.example.distribute_lock.entity.Book;
import com.example.distribute_lock.entity.Stock;
import com.example.distribute_lock.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void 동시성을_고려하지_않은_간단한_도서구매_요구사항_테스트() {

        Book book = Book.builder()
                .name("Effective Java")
                .price(10000)
                .stock(new Stock(100))
                .build();

        Long bookId = bookRepository.save(book).getId();

        for (int i = 0; i < 100; i++) {
            bookService.purchase(bookId, 1);
        }

        Book foundBook = bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);

        assertThat(foundBook.getStock().getRemain()).isZero();
    }

    @Test
    void 동시성을_고려한_도서구매_요구사항_테스트() throws InterruptedException {

        Book book = Book.builder()
                .name("effective Java")
                .price(10000)
                .stock(new Stock(100))
                .build();

        Long bookId = bookRepository.save(book).getId();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                bookService.purchase(bookId, 1);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        Book foundBook = bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);

        assertThat(foundBook.getStock().getRemain()).isZero();
    }
}
