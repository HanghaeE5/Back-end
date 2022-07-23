package com.example.backend.notification.domain;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "related_url")
public class RelatedURL {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column
    @NotNull
    private String  url;
    public RelatedURL(String url) {
        this.url = url;
    }
    public RelatedURL() {
    }
}