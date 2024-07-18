package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}

