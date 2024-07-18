package org.portfolio.ourverse.src.web;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.UserVO;
import org.portfolio.ourverse.src.persist.CommentLikeRepository;
import org.portfolio.ourverse.src.persist.CommentRepository;
import org.portfolio.ourverse.src.persist.UserRepository;
import org.portfolio.ourverse.src.persist.entity.*;
import org.portfolio.ourverse.src.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final AuthService authService;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public Long postCommentLike(Long commentId) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 현재 유저 확인.
        User user = userRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_USER));

        // 2. 현재 보는 댓글 확인.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_COMMENT));

        // 3. 이미 좋아요를 한 상태인지 확인.
        if(commentLikeRepository.findFirstByCommentAndUser(comment, user).isPresent()){
            throw new BaseException(ExceptionCode.ALREADY_EXISTS_COMMENTLIKE);
        }

        // 4. 좋아요 등록.
        CommentLike commentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();

        // 5. 해당 feed의 좋아요 수 늘려주기.
        comment.plusCommentLikeCnt();

        commentLikeRepository.save(commentLike);

        return commentLike.getId();

    }

    @Transactional
    public void deleteCommentLike(Long commentId) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 현재 유저 확인.
        User user = userRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_USER));

        // 2. 현재 보는 댓글 확인.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_COMMENT));

        // 3. 이미 좋아요를 한 상태인지 확인.
        CommentLike commentLike = commentLikeRepository.findFirstByCommentAndUser(comment, user).orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_COMMENTLIKE));

        // 4. 좋아요 삭제
        commentLikeRepository.delete(commentLike);

        // 5. 해당 feed의 좋아요 수 줄이기.
        comment.minusCommentLikeCnt();
    }
}
