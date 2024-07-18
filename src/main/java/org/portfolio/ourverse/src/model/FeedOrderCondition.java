package org.portfolio.ourverse.src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedOrderCondition {
    private boolean isCommentCnt;
    private boolean isFeedlikeCnt;
    private boolean isCreatedAt;
}
