package org.portfolio.ourverse.src.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.portfolio.ourverse.common.constant.GroupName;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final AuthService authService;

    // 1. 등록된 그룹 조회하기
    @GetMapping
    public ResponseEntity<List<String>> readGroupNames() {
        return ResponseEntity.ok(Arrays.stream(GroupName.class.getDeclaredFields())
                .filter(Field::isEnumConstant)
                .map(Field::getName).toList());
    }

    // 2. 그룹 가입하기
    @PatchMapping("/{groupname}")
    public ResponseEntity<String> updateGroupName(@PathVariable String groupname) {
        log.info("Updating group name {}", groupname);
        try{
            authService.updateGroupName(GroupName.valueOf(groupname));
        }catch (IllegalArgumentException e){
            throw new BaseException(ExceptionCode.WRONG_GROUPNAME);
        }
        return ResponseEntity.ok("그룹 가입에 성공하셨습니다.");
    }
}