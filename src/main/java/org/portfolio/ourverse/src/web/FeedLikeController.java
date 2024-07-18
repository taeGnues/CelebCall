package org.portfolio.ourverse.src.web;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.src.service.FeedLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed-like")
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    @PostMapping("/{id}")
    public ResponseEntity<String> postFeedLike(@PathVariable Long id) {
        Long feedLikeId = feedLikeService.postFeedLike(id);
        return ResponseEntity.ok("좋아요에 성공했습니다 : " + feedLikeId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedLike(@PathVariable Long id) {
        feedLikeService.deleteFeedLike(id);
        return ResponseEntity.ok("좋아요를 취소했습니다.");
    }

}
