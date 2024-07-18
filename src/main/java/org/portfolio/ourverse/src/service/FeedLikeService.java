package org.portfolio.ourverse.src.service;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.UserVO;
import org.portfolio.ourverse.src.persist.FeedLikeRepository;
import org.portfolio.ourverse.src.persist.FeedRepository;
import org.portfolio.ourverse.src.persist.UserRepository;
import org.portfolio.ourverse.src.persist.entity.Feed;
import org.portfolio.ourverse.src.persist.entity.FeedLike;
import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final AuthService authService;
    private final FeedLikeRepository feedLikeRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;


    @Transactional
    public Long postFeedLike(Long id) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 현재 유저 확인.
        User user = userRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_USER));

        // 2. 현재 보는 게시글 확인.
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_FEED));

        // 3. 이미 좋아요를 한 상태인지 확인.
        if(feedLikeRepository.findFirstByFeedAndUser(feed, user).isPresent()){
            throw new BaseException(ExceptionCode.ALREADY_EXISTS_FEEDLIKE);
        }

        // 4. 좋아요 등록.
        FeedLike feedLike = FeedLike.builder()
                .user(user)
                .feed(feed)
                .build();

        // 5. 해당 feed의 좋아요 수 늘려주기.
        feed.plusFeedLikeCnt();

        feedLikeRepository.save(feedLike);

        return feedLike.getId();
    }

    @Transactional
    public void deleteFeedLike(Long id) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 현재 유저 확인.
        User user = userRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_USER));

        // 2. 현재 보는 게시글 확인.
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_FEED));

        // 3. 이미 좋아요를 한 상태인지 확인.
        FeedLike feedLike = feedLikeRepository.findFirstByFeedAndUser(feed, user)
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_EXISTS_FEEDLIKE));

        // 4. 해당 feed 좋아요 수 줄이기.
        feed.minusFeedLikeCnt();

        feedLikeRepository.delete(feedLike);
    }
}
