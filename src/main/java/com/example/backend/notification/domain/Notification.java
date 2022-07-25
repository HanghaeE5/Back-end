package com.example.backend.notification.domain;

import com.example.backend.user.domain.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @NotNull
    private NotificationContent content;

    @ManyToOne
    @JoinColumn(name = "url_ID")
    private RelatedURL url;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    @NotNull
    private Long receiver;

    @Builder
    public Notification(Long receiver, String content, String url, Boolean isRead) {
        this.receiver = receiver;
        this.content = new NotificationContent(content);
        this.url = new RelatedURL(url);
        this.isRead = isRead;
    }

    public void setContent(NotificationContent content) {
        this.content = content;
    }

    public String getContent() {
        return content.getContent();
    }
}