package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("select f from Feed f where f.id = :feedId")
    Optional<Feed> findByIdWithUsers(@Param("feedId") Long id);

    List<Feed> findAllByGroupName(GroupName groupName, Pageable pageable);
}
