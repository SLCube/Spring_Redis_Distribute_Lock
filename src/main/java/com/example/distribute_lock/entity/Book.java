package com.example.distribute_lock.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Builder
    private Book(final String name, final int price, final Stock stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void purchase(final long quantity) {
        stock.decrease(quantity);
    }
}
