package org.portfolio.ourverse.src.web;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.src.model.CommentDTO;
import org.portfolio.ourverse.src.model.CommentPostDTO;
import org.portfolio.ourverse.src.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // 0. 특정 게시물에 댓글 등록하기
    @PostMapping("/{feedId}")
    public ResponseEntity<String> postComment(
            @PathVariable Long feedId,
            @RequestBody CommentPostDTO form) {

        var res = commentService.postComment(feedId, form);

        return ResponseEntity.ok("댓글 등록에 성공했습니다. 댓글ID : " + res);
    }

    // 1. 특정 게시물 댓글 가져오기 - 최신순
    @GetMapping("/{feedId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long feedId, @RequestParam int pageNo, @RequestParam int pageSize) {

        var res = commentService.getComments(feedId, pageNo, pageSize);

        return ResponseEntity.ok(res);
    }

    // 2. 댓글 삭제하기
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.ok("댓글이 삭제됐습니다.");
    }

    // 3. 댓글 수정하기
    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentPostDTO form) {
        commentService.updateComment(commentId, form);

        return ResponseEntity.ok("댓글이 수정됐습니다.");
    }



    // 4. 댓글 조회 (좋아요순)


}
