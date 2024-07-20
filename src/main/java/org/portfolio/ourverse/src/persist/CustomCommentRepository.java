package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.model.CommentOrderCondition;
import org.portfolio.ourverse.src.persist.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findAllByFeed_IdWithCriteria(Long id, int pageNo, int pageSize, CommentOrderCondition form);
}
