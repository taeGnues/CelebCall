package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByFeed_Id(Long id, Pageable pageable);
}
