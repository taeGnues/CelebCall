package org.portfolio.ourverse.src.persist;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.portfolio.ourverse.src.model.CommentOrderCondition;
import org.portfolio.ourverse.src.persist.entity.Comment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.portfolio.ourverse.src.persist.entity.QComment.comment;

@Repository
public class CustomCommentRepositoryImpl extends QuerydslRepositorySupport implements CustomCommentRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCommentRepositoryImpl(EntityManager entityManager) {
        super(Comment.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Comment> findAllByFeed_IdWithCriteria(Long id, int pageNo, int pageSize, CommentOrderCondition form) {

        return jpaQueryFactory.selectFrom(comment)
                .where(comment.feed.id.eq(id))
                .orderBy(getOrderSpecifiers(form))
                .limit(pageSize)
                .offset(pageNo)
                .stream().toList();
    }

    private OrderSpecifier[] getOrderSpecifiers(CommentOrderCondition form) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if(form.isCreatedAt()){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, comment.createdAt));
        }

        if(form.isCommentlikeCnt()){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, comment.commentLikeCnt));
        }


        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
