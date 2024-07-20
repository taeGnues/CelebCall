package org.portfolio.ourverse.src.service;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.CommentDTO;
import org.portfolio.ourverse.src.model.CommentOrderCondition;
import org.portfolio.ourverse.src.model.CommentPostDTO;
import org.portfolio.ourverse.src.model.UserVO;
import org.portfolio.ourverse.src.persist.CommentLikeRepository;
import org.portfolio.ourverse.src.persist.CommentRepository;
import org.portfolio.ourverse.src.persist.FeedRepository;
import org.portfolio.ourverse.src.persist.UserRepository;
import org.portfolio.ourverse.src.persist.entity.Comment;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    /*
       현재 게시물 정보와 현재 유저 정보를 바탕으로 댓글 저장
     */
    @Transactional
    public Long postComment(Long feedId, CommentPostDTO form) {
        // 0. 현재 유저 확인
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 유저 정보 가져오기.
        User user = userRepository.findById(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_USER)
        );

        // 2. feedId로 feed 찾기.
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_FEED)
        );

        // 3. 현재 유저의 GROUP과 현재 게시글의 GROUP이 동일한지 확인.
        if(!user.getGroupname().equals(feed.getGroupName())){
            throw new BaseException(ExceptionCode.WRONG_ACCESS_AUTHORITY);
        }

        // 4. comment 생성 및 저장.
        Comment comment = Comment.of(form, feed, user);
        commentRepository.save(comment);
        feed.plusCommentCnt();

        return comment.getId();
    }

    /*
        현재 게시글 댓글 조회하기. (페이징 + 좋아요 순, 최신 순 기준으로 동적 정렬해서 리턴)
     */
    public List<CommentDTO> getComments(Long feedId, int pageNo, int pageSize, CommentOrderCondition commentOrderCondition) {
        return commentRepository.findAllByFeed_IdWithCriteria(feedId, pageNo, pageSize, commentOrderCondition)
                .stream().map(CommentDTO::from).toList();
    }

    /*
       현재 댓글 삭제하기.
     */
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = checkAuthorityAndGetComment(commentId);
        comment.getFeed().minusCommentCnt();

        // 해당 댓글에 등록된 좋아요 모두 삭제하기.
        commentLikeRepository.deleteAllByComment(comment);
        commentRepository.delete(comment);
    }

    /*
        댓글 수정하기
     */
    @Transactional
    public void updateComment(Long commentId, CommentPostDTO form) {
        Comment comment = checkAuthorityAndGetComment(commentId);

        comment.updateContent(form.getContent());
    }

    // 권한 확인 후 댓글 가져오기
    private Comment checkAuthorityAndGetComment(Long commentId) {
        UserVO userVO = authService.getCurrentUserVO();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_COMMENT));
        if(!comment.getUser().getId().equals(userVO.getUserId())){
            throw new BaseException(ExceptionCode.WRONG_ACCESS_AUTHORITY);
        }
        return comment;
    }
}
