package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.src.persist.entity.Feed;
import org.portfolio.ourverse.src.persist.entity.FeedLike;
import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    Optional<FeedLike> findFirstByFeedAndUser(Feed feed, User user);
}
