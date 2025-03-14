package net.todo.core.security.service;

import lombok.RequiredArgsConstructor;
import net.todo.core.security.dto.User;
import net.todo.core.security.repository.SecurityRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService, UserDetailsService {

    private final SecurityRepository securityRepository;

    @Override
    public Optional<User.UserAccount> getLoginUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getPrincipal() instanceof User.UserAccount)
                .map(authentication -> (User.UserAccount) authentication.getPrincipal());
    }

    @Override
    public User.UserAccount loadUserByGoogleId(String googleId) {
        return securityRepository.findUserByGoogleId(googleId);
    }

    @Override
    public User.UserAccount loadUserByKakaoId(String kakaoId) {
        return securityRepository.findUserByKakaoId(kakaoId);
    }

    @Override
    public User.UserAccount joinUser(User.UserAccount user) {
        securityRepository.persistUser(user);
        return user;
    }

    @Override
    public User.UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        return securityRepository.findUserByUsername(username);
    }
}
