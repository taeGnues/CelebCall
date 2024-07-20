package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.Comment;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    List<Comment> findAllByFeed(Feed feed);
    void deleteAllByFeed(Feed feed);
}

