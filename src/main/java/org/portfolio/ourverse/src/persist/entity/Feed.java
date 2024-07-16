package org.portfolio.ourverse.src.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.common.entity.BaseEntity;
import org.portfolio.ourverse.src.model.FeedPostDetailDTO;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Feed extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GroupName groupName;
    private String title;
    private String content;
    private boolean isHidden;
    private int feedLikeCnt;
    private int commentCnt;

    public static Feed of(FeedPostDetailDTO feedPostDetailDTO, User user) {
        return Feed.builder()
                .title(feedPostDetailDTO.getTitle())
                .content(feedPostDetailDTO.getContent())
                .user(user)
                .groupName(user.getGroupname())
                .isHidden(false)
                .feedLikeCnt(0)
                .commentCnt(0)
                .build();
    }
}
