package org.portfolio.ourverse.src.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.portfolio.ourverse.src.persist.entity.Feed;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDetailDTO {

    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int feedLikeCnt;
    private int commentCnt;

    public static FeedDetailDTO fromEntity(Feed feed) {
        return FeedDetailDTO.builder()
                .title(feed.getTitle())
                .content(feed.getContent())
                .username(feed.getUser().getUsername())
                .commentCnt(feed.getCommentCnt())
                .feedLikeCnt(feed.getFeedLikeCnt())
                .createdAt(feed.getCreatedAt())
                .modifiedAt(feed.getModifiedAt())
                .build();
    }
}
