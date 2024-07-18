package org.portfolio.ourverse.src.persist;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.src.model.FeedOrderCondition;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.portfolio.ourverse.src.persist.entity.QFeed.feed;

@Repository
public class CustomFeedRepositoryImpl extends QuerydslRepositorySupport implements CustomFeedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomFeedRepositoryImpl(EntityManager entityManager) {
        super(Feed.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Feed> findAllByGroupNameWithCriteria(GroupName groupName, int pageNo, int pageSize, FeedOrderCondition form) {

        return jpaQueryFactory.selectFrom(feed)
                .where(feed.groupName.eq(groupName))
                .orderBy(getOrderSpecifiers(form))
                .limit(pageSize)
                .offset(pageNo)
                .stream().toList();
    }

    private OrderSpecifier[] getOrderSpecifiers(FeedOrderCondition form) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if(form.isCreatedAt()){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, feed.createdAt));
        }

        if(form.isFeedlikeCnt()){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, feed.feedLikeCnt));
        }

        if(form.isCommentCnt()){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, feed.commentCnt));
        }


        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
