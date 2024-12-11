package net.todo.core.security.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class User {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Principal {

        private String email;
        private String name;
        private Set<GrantedAuthority> roles;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginInfo {
        private String email;
        private String name;
        private Set<GrantedAuthority> roles;
        private String password;
    }

}
