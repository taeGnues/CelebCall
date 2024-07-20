package org.portfolio.ourverse.src.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment-like")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/{commentId}")
    public ResponseEntity<String> postCommentLike(@PathVariable Long commentId) {

        Long id = commentLikeService.postCommentLike(commentId);

        return ResponseEntity.ok("댓글에 좋아요를 성공했습니다"+ id);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentLike(@PathVariable Long commentId) {

        commentLikeService.deleteCommentLike(commentId);

        return ResponseEntity.ok("댓글에 좋아요를 취소했습니다.");
    }

}
