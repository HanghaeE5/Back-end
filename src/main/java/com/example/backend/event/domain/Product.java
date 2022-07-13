package com.example.backend.event.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String percentage;

    @Column
    private int price;

    @Column
    private String imgUrl;

    @Builder
    public Product(String name, String percentage, int price, String imgUrl){
        this.name = name;
        this.percentage = percentage;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Product() {

    }
}
