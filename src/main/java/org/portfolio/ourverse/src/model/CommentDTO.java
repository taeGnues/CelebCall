package org.portfolio.ourverse.src.model;

import lombok.Builder;
import lombok.Data;
import org.portfolio.ourverse.src.persist.entity.Comment;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDTO {
    private String content;
    private String nickname;
    private int commentLikeCnt;
    private LocalDateTime createdAt;

    public static CommentDTO from(Comment m) {
        return CommentDTO.builder()
                .content(m.getContent())
                .nickname(m.getUser().getNickname())
                .commentLikeCnt(m.getCommentLikeCnt())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
