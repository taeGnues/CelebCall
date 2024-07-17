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
public class FeedDTO {
    private String title;
    private String username;
    private LocalDateTime createdAt;
    private int feedLikeCnt;
    private int commentCnt;

    public static FeedDTO fromEntity(Feed feed) {
        return FeedDTO.builder()
                .title(feed.getTitle())
                .username(feed.getUser().getUsername())
                .createdAt(feed.getCreatedAt())
                .feedLikeCnt(feed.getFeedLikeCnt())
                .commentCnt(feed.getCommentCnt())
                .build();
    }
}
