package net.todo.core.security.authentication;


import lombok.Getter;
import lombok.Setter;
import net.todo.core.security.dto.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class CustomAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private Map<String, User.LoginInfo> userDB = new ConcurrentHashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //여기서 토큰 발행 일어남 (인증 체크)
        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;

        if (userDB.containsKey(token.getName()) && userDB.get(token.getName()).getPassword().equals(token.getCredentials())) {
            return CustomAuthenticationToken.builder()
                    .principal(token.getPrincipal())
                    .credentials(token.getCredentials())
                    .details(token.getDetails())
                    .authenticated(true)
                    .build();
        }

        // 인증 실패 시엔 null return
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomAuthenticationToken.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userDB.put("rudalsOwner@naver.com", User.LoginInfo.builder()
                .email("rudalsOwner@naver.com")
                .password("0909")
                .roles(Set.of(new SimpleGrantedAuthority("USER")))
                .name("훙")
                .build());

        userDB.put("rudalsRealOwner@naver.com", User.LoginInfo.builder()
                .email("rudalsORealwner@naver.com")
                .password("0909")
                .roles(Set.of(new SimpleGrantedAuthority("ADMIN")))
                .name("훙훙")
                .build());
    }
}
