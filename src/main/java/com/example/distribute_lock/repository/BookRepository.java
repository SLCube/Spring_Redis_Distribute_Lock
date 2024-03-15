package com.example.distribute_lock.repository;

import com.example.distribute_lock.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
