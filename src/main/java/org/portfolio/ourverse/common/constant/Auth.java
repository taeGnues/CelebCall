package org.portfolio.ourverse.common.constant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
    회원가입 및 로그인 시 사용하는 DTO
 */
public class Auth {
    @Data
    public static class SignUp{

        @NotBlank(message = "username을 입력해야합니다.")
        private String username;
        @NotBlank(message = "email을 입력해야합니다.")
        private String email;
        @NotBlank(message = "phone을 입력해야합니다.")
        private String phone;
        @NotBlank(message = "passowrd를 입력해야합니다.")
        private String password;
        @NotBlank(message = "name을 입력해야합니다.")
        private String name;
        @NotBlank(message = "nickname을 입력해야합니다.")
        private String nickname;
        private Authority role;
    }

    @Data
    public static class SignIn {

        @NotBlank(message = "username을 입력해야합니다.")
        private String username;
        @NotBlank(message = "password를 입력해야합니다.")
        private String password;
    }
}
