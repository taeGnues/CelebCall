package org.portfolio.ourverse.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    /*
        400 : Request, Response 오류
     */
    ALREADY_EXISTS_PHONE(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 email입니다."),
    ALREADY_EXISTS_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 username입니다."),
    ALREADY_EXISTS_FEEDLIKE(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),

    NOT_EXISTS_USERNAME(HttpStatus.BAD_REQUEST, "존재하지 않는 username입니다."),
    NOT_EXISTS_FEED(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물입니다."),
    NOT_EXISTS_FEEDLIKE(HttpStatus.BAD_REQUEST, "존재하지 않는 좋아요 정보입니다."),
    NOT_EXISTS_COMMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물입니다."),
    NOT_AUTHENTICATE(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    USER_NOT_GROUP(HttpStatus.UNAUTHORIZED, "가입된 GROUP이 없습니다."),

    WRONG_GROUPNAME(HttpStatus.BAD_REQUEST, "잘못된 GROUPNAME입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "password가 잘못됐습니다."),
    WRONG_ACCESS_AUTHORITY(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    WRONG_SOMETHING(HttpStatus.BAD_REQUEST, "잘못된 요청을 했습니다."),
    WRONG_TYPE_ROLE(HttpStatus.BAD_REQUEST, "잘못된 타입의 role를 입력했습니다."),

    /*
        500 : Server 오류
     */
    NOT_EXISTS_USER(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 사용자입니다.");

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;
}
