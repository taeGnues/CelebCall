package org.portfolio.ourverse.src.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.constant.Auth;
import org.portfolio.ourverse.common.security.JwtTokenProvider;
import org.portfolio.ourverse.src.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody Auth.SignUp form) {
        authService.register(form);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    /*
    로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@Valid @RequestBody Auth.SignIn form){
        // 1. 인증하기
        var user = authService.authentication(form);

        // 2. token 발급하기.
        var token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId());

        return ResponseEntity.ok(token);
    }

}
