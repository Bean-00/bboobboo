package net.todo.core.security.dto;

import lombok.*;
import net.todo.core.security.constant.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static net.todo.core.security.constant.UserRole.SYS_ADMIN;

public class User {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserResponse {
        private int id;
        private String email;
        private String name;
        private Set<String> role;
        private boolean isSysAdmin;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {

        private String email;
        private String password;
        private boolean rememberMe;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserAccount implements UserDetails {
        private int id;
        private String email;
        private String name;
        private String googleId;
        private String kakaoId;
        private Set<GrantedAuthority> role;
        private String password;

        public void setRole(int roleId) {
            this.role = Set.of(new SimpleGrantedAuthority(UserRole.getUserRoleById(roleId).toString()));
        }

        public int getRoleId() {
            String roleStr = role.stream().collect(Collectors.toList()).get(0).getAuthority();
            return UserRole.getUserRoleByStr(roleStr).getId();
        }

        public UserResponse toResponse() {
            Set<String> authoritySet = role.stream()
                    .map(r -> r.getAuthority())
                    .collect(Collectors.toSet());

            return UserResponse.builder()
                    .id(id)
                    .email(email)
                    .name(name)
                    .role(authoritySet)
                    .isSysAdmin(authoritySet.stream().anyMatch(a->a.equals(SYS_ADMIN.toString())))
                    .build();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return role;
        }

        @Override
        public String getUsername() {
            return email;
        }
    }


}
