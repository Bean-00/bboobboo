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

        private int id;
        private String email;
        private String name;
        private Set<GrantedAuthority> role;
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
    public static class UserAccount {
        private int id;
        private String email;
        private String name;
        private Set<GrantedAuthority> role;
        private String password;
    }

}
