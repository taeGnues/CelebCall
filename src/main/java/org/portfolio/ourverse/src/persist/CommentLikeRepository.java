package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findFirstByCommentAndUser(Comment comment, User user);
}
