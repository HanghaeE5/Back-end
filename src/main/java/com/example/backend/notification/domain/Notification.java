package com.example.backend.notification.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_ID")
    private NotificationContent content;

    @ManyToOne
    @JoinColumn(name = "url_ID")
    private RelatedURL url;
    @Embedded
    private Review review;

    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationCategory notificationCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    public void setContent(NotificationContent content) {
        this.content = content;
    }

    public void setUrl(RelatedURL url) {
        this.url = url;
    }

    @Builder
    public Notification(Member receiver, Review review, NotificationCategory notificationCategory, String content, String url, Boolean isRead) {
        this.receiver = receiver;
        this.notificationCategory = notificationCategory;
        this.content = new NotificationContent(content);
        this.url = new RelatedURL(url);
        this.isRead = isRead;
        this.review = review;
    }

    public String getContent() {
        return content.getContent();
    }

    public String getUrl() {
        return url.getUrl();
    }
}