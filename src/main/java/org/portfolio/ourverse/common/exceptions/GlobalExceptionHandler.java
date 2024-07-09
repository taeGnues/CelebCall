package org.portfolio.ourverse.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.portfolio.ourverse.common.constant.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResult> BaseExceptionHandle(BaseException e){
        log.info("{} ->", e.getExceptionCode().getMessage());
        var result = BaseResult.builder()
                .status(e.getExceptionCode().getHttpStatus())
                .message(e.getExceptionCode().getMessage())
                .build();

        return new ResponseEntity<>(result, e.getExceptionCode().getHttpStatus());
    }
    /*
    @Valid에서 오류가 발생했을 때.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResult> MethodARgumentNotValidExceptionHandle(MethodArgumentNotValidException e){
        var msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        var result = BaseResult.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(msg)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResult> ExceptionHandle(Exception e){
        log.info("{} : {}->", e.getClass(), e.getMessage());
        var result = BaseResult.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("알 수 없는 오류가 발생했습니다.")
                .build();

        return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
