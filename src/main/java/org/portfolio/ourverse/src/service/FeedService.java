package org.portfolio.ourverse.src.service;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.*;
import org.portfolio.ourverse.src.persist.*;
import org.portfolio.ourverse.src.persist.entity.Comment;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FeedLikeRepository feedLikeRepository;

    /*
    게시판에 글 작성.
     */
    @Transactional
    public Long postFeed(FeedPostDetailDTO feedPostDetailDTO) {
        // 1. 현재 로그인한 유저 정보 확인.
        UserVO userVO = authService.getCurrentUserVO();
        User user = userRepository.findById(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_USER)
        );

        // 1-1. 현재 유저가 가입된 group이 없다면, 오류 발생.
        if (ObjectUtils.isEmpty(user.getGroupname())) {
            throw new BaseException(ExceptionCode.NOT_EXISTS_USERNAME);
        }

        // 2. FeedDetailDTO를 Feed Entity로 변환 후 저장.
        Feed feed = Feed.of(feedPostDetailDTO, user);
        feedRepository.save(feed);

        return feed.getId();
    }

    /*
    게시판 글 상세조회
     */
    public FeedDetailDTO readFeedDetail(Long id) {
        // 1. 현재 사용자 정보 가져오기.
        UserVO userVO = authService.getCurrentUserVO();
        User user = userRepository.findById(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_USER)
        );

        // 1-1. 현재 유저가 가입된 group이 없다면, 오류 발생.
        if (ObjectUtils.isEmpty(user.getGroupname())) {
            throw new BaseException(ExceptionCode.USER_NOT_GROUP);
        }

        // 2. 현재 게시글이 어떤 group에 속한 게시글인지 확인하기. -> 만약 다르다면, 권한이 없다고 알림.
        Feed feed = feedRepository.findByIdWithUsers(id).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_FEED)
        );

        if(feed.getGroupName().equals(user.getGroupname())) {
            return FeedDetailDTO.fromEntity(feed);
        }

        throw new BaseException(ExceptionCode.WRONG_ACCESS_AUTHORITY);
    }
    /*
    게시판 목록 조회 (동적 정렬, 최신순-좋아요순-댓글수순)
     */
    public List<FeedDTO> readFeedAllByCrtieria(GroupName groupName, int pageNo, int pageSize, FeedOrderCondition feedOrderCondition) {
        return feedRepository.findAllByGroupNameWithCriteria(groupName, pageNo, pageSize, feedOrderCondition)
                .stream().map(FeedDTO::fromEntity).toList();
    }

    /*
    게시판 글 삭제
     */
    @Transactional
    public void deleteFeed(Long id) {
        UserVO userVO = authService.getCurrentUserVO();
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_FEED));

        if (!userVO.getUserId().equals(feed.getUser().getId())){
            throw new BaseException(ExceptionCode.WRONG_ACCESS_AUTHORITY);
        }

        // 0. 게시글에 해당하는 좋아요 삭제
        feedLikeRepository.deleteAllByFeed(feed);

        // 1. 게시글의 댓글 찾기
        List<Comment> comments = commentRepository.findAllByFeed(feed);

        // 2. 게시글의 각 댓글에 해당하는 좋아요 찾아서 삭제.
        for (Comment comment : comments) {
            commentLikeRepository.deleteAllByComment(comment);
        }

        // 3. 게시글의 댓글 삭제
        commentRepository.deleteAllByFeed(feed);

        feedRepository.delete(feed);
    }

    /*
    게시글 수정
     */
    @Transactional
    public void updateFeed(Long id, FeedPostDetailDTO feedPostDetailDTO) {
        UserVO userVO = authService.getCurrentUserVO();
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_FEED));

        if (!userVO.getUserId().equals(feed.getUser().getId())){
            throw new BaseException(ExceptionCode.WRONG_ACCESS_AUTHORITY);
        }

        feed.updateFeed(feedPostDetailDTO);
    }
}
