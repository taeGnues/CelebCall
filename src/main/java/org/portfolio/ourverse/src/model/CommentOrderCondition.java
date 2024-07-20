package org.portfolio.ourverse.src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentOrderCondition {
    private boolean isCommentlikeCnt;
    private boolean isCreatedAt;

}
