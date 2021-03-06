package com.example.backend.event.dto;

import com.example.backend.event.domain.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private Long eventId;
    private String name;

    private String imageUrl;

    public ProductResponseDto(Product product, Long eventId){
        this.eventId = eventId;
        this.name = product.getName();
        this.imageUrl = product.getImgUrl();
    }

}
