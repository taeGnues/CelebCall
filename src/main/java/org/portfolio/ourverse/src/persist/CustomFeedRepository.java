package org.portfolio.ourverse.src.persist;

import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.src.model.FeedOrderCondition;
import org.portfolio.ourverse.src.persist.entity.Feed;

import java.util.List;

public interface CustomFeedRepository {
    List<Feed> findAllByGroupNameWithCriteria(GroupName groupName, int pageNo, int pageSize, FeedOrderCondition form);
}
