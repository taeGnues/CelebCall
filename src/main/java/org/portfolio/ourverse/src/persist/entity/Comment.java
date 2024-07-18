package org.portfolio.ourverse.src.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.portfolio.ourverse.common.entity.BaseEntity;
import org.portfolio.ourverse.src.model.CommentPostDTO;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private boolean isHidden;
    private int commentLikeCnt;

    public static Comment of(CommentPostDTO commentPostDTO, Feed feed, User user){
        return Comment.builder()
                .feed(feed)
                .user(user)
                .content(commentPostDTO.getContent())
                .isHidden(false)
                .commentLikeCnt(0)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void plusCommentLikeCnt() {
        this.commentLikeCnt++;
    }

    public void minusCommentLikeCnt() {
        this.commentLikeCnt--;
    }
}
