package com.example.backend.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Board {

    @Id
    private int id;

}
