package net.todo.core.security.authentication;

import jakarta.annotation.PostConstruct;
import net.todo.core.security.dto.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private Map<String, User.UserAccount> userDB = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        userDB.put("popo@naver.com", User.UserAccount.builder()
                .id(2)
                .email("popo@naver.com")
                .password("1234")
                .role(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                .name("포포")
                .build());

        userDB.put("io@naver.com", User.UserAccount.builder()
                .id(2)
                .email("io@naver.com")
                .password("1234")
                .role(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .name("아이오")
                .build());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //실질적인 인증 로직 => 인증되었으면 토큰 발행해주고 아니면 null, exception 리턴

        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;

        if (userDB.containsKey(token.getName())
                && userDB.get(token.getName()).getPassword().equals(token.getCredentials())) {
            User.UserAccount account = userDB.get(token.getName());

            if (account.getPassword().equals(token.getCredentials())) {
                //인증 성공
                return CustomAuthenticationToken.builder()
                        .principal(User.Principal.builder()
                                .id(account.getId())
                                .email(account.getEmail())
                                .name(account.getName())
                                .role(account.getRole())
                                .build()
                        )
                        .credentials(null)
                        .details(token.getDetails())
                        .authenticated(true)
                        .build();
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class == authentication;
    }


}
