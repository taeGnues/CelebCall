package org.portfolio.ourverse.src.service;

import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.constant.Auth;
import org.portfolio.ourverse.common.exceptions.BaseException;
import org.portfolio.ourverse.common.exceptions.ExceptionCode;
import org.portfolio.ourverse.src.model.UserVO;
import org.portfolio.ourverse.src.persist.UserRepository;
import org.portfolio.ourverse.src.persist.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    회원 등록
     */
    @Transactional
    public void register(Auth.SignUp form){

        // 1. 이미 존재하는 사용자인지 확인.
        checkExistUser(form);

        // 2. User 객체 생성 후 저장.
        userRepository.save(
            User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .phone(form.getPhone())
                .password(passwordEncoder.encode(form.getPassword()))
                .name(form.getName())
                .nickname(form.getNickname())
                .role(form.getRole())
                .build()
        );
    }

    private void checkExistUser(Auth.SignUp form) {
        // 0. username(ID) unique한지 확인.
        if(userRepository.existsByUsername(form.getUsername())){
            throw new BaseException(ExceptionCode.ALREADY_EXISTS_USERNAME);
        }

        // 1. 전화번호 unique한지 확인.
        if(userRepository.existsByPhone(form.getPhone())){
            throw new BaseException(ExceptionCode.ALREADY_EXISTS_PHONE);
        }

        // 2. 이메일 unique한지 확인.
        if(userRepository.existsByEmail(form.getEmail())){
            throw new BaseException(ExceptionCode.ALREADY_EXISTS_EMAIL);
        }
    }

    /*
    ID,Password 인증
     */
    public User authentication(Auth.SignIn form) {

        // 1. user 찾기
        User user = userRepository.findByUsername(form.getUsername()).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_USERNAME)
        );

        // 2. 해당 user의 password와 맞는지 확인하기.
        if(!passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new BaseException(ExceptionCode.WRONG_SOMETHING);
        }

        return user;
    }

    /*
    현재 로그인한 유저 정보 가져오기.
     */
    public UserVO getCurrentUserVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(principal instanceof User){
                var username = ((User) principal).getUsername();
                var userId = ((User) principal).getId();
                return UserVO.builder()
                        .username(username)
                        .userId(userId)
                        .build();
            }
        }
        throw new BaseException(ExceptionCode.NOT_AUTHENTICATE);
    }

    /*
    username으로 정보 가져오기
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_EXISTS_USERNAME)
        );
    }
}
