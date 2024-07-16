package org.portfolio.ourverse.src.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.FeedDTO;
import org.portfolio.ourverse.src.model.FeedDetailDTO;
import org.portfolio.ourverse.src.model.FeedPostDetailDTO;
import org.portfolio.ourverse.src.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // 1. 그룹의 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<FeedDetailDTO> readFeedDetail(@PathVariable Long id) {
        FeedDetailDTO res = feedService.readFeedDetail(id);
        return ResponseEntity.ok(res);
    }

    // 2. 그룹의 게시글 작성
    @PostMapping
    public ResponseEntity<String> postFeed(@Valid @RequestBody FeedPostDetailDTO feedPostDetailDTO){

        Long id = feedService.postFeed(feedPostDetailDTO);

        return ResponseEntity.ok("게시글 저장에 성공했습니다. 게시글 ID : " + id.toString());
    }


    // 3. 그룹의 게시글 목록 조회 (최신순, 페이징처리)
    // 3-1. 그룹 게시글의 최신순 정렬
    @GetMapping("/criteria-recent")
    public ResponseEntity<List<FeedDTO>> readAllFeedsByRecent(String groupname, int pageNo, int pageSize){
        GroupName groupName;
        try{
             groupName = GroupName.valueOf(groupname);
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }

        return ResponseEntity.ok(feedService.readFeedAllByRecent(groupName, pageNo, pageSize));
    }

    // 3-2. 그룹 게시글의 좋아요 순 정렬
    @GetMapping("/criteria-like")
    public ResponseEntity<List<FeedDTO>> readAllFeedsByLikeCnt(String groupname, int pageNo, int pageSize){
        GroupName groupName;
        try{
            groupName = GroupName.valueOf(groupname);
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }

        return ResponseEntity.ok(feedService.readFeedAllByLikeCnt(groupName, pageNo, pageSize));
    }


    // 3-3. 그룹 게시글의 댓글 순 정렬
    @GetMapping("/criteria-comment")
    public ResponseEntity<List<FeedDTO>> readAllFeedsByCommentCnt(String groupname, int pageNo, int pageSize){
        GroupName groupName;
        try{
            groupName = GroupName.valueOf(groupname);
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }

        return ResponseEntity.ok(feedService.readFeedAllByCommentCnt(groupName, pageNo, pageSize));
    }


    // 5. 그룹의 게시글 삭제 기능

}