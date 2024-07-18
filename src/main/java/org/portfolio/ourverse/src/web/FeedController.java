package org.portfolio.ourverse.src.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.FeedDTO;
import org.portfolio.ourverse.src.model.FeedDetailDTO;
import org.portfolio.ourverse.src.model.FeedOrderCondition;
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


    // 3. 그룹의 게시글 목록 조회 (최신순-좋아요순-댓글수순 동적 정렬 & 페이징처리)
    @GetMapping
    public ResponseEntity<List<FeedDTO>> readAllFeedsByRecent(@RequestParam String groupname,
                                                              @RequestParam int pageNo,
                                                              @RequestParam int pageSize,
                                                              @RequestBody FeedOrderCondition feedOrderCondition){
        GroupName groupName;
        try{
             groupName = GroupName.valueOf(groupname);
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }

        return ResponseEntity.ok(feedService.readFeedAllByCrtieria(groupName, pageNo, pageSize, feedOrderCondition));
    }



    // 4. 그룹의 게시글 삭제 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeed(@PathVariable Long id) {
        feedService.deleteFeed(id);
        return ResponseEntity.ok("성공적으로 삭제했습니다.");
    }

    // 5. 그룹의 게시글 수정 기능
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateFeed(@PathVariable Long id, @Valid @RequestBody FeedPostDetailDTO feedPostDetailDTO) {
        feedService.updateFeed(id, feedPostDetailDTO);
        return ResponseEntity.ok("성공적으로 수정했습니다.");
    }

}
