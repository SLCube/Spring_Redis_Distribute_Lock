package com.example.distribute_lock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private long remain;

    public Stock(final long remain) {
        this.remain = remain;
    }

    public void decrease(final long quantity) {
        if (remain - quantity < 0) {
            throw new IllegalArgumentException();
        }

        this.remain -= quantity;
    }
}
