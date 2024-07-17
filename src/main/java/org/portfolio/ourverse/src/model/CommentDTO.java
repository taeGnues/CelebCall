package org.portfolio.ourverse.src.model;

import lombok.Builder;
import lombok.Data;
import org.portfolio.ourverse.src.persist.entity.Comment;

@Data
@Builder
public class CommentDTO {
    private String content;
    private String nickname;

    public static CommentDTO from(Comment m) {
        return CommentDTO.builder()
                .content(m.getContent())
                .nickname(m.getUser().getNickname())
                .build();
    }
}
