package net.todo.core.security.service;

import lombok.RequiredArgsConstructor;
import net.todo.core.security.dto.User;
import net.todo.core.security.repository.SecurityRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService{

    private final SecurityRepository securityRepository;

    @Override
    public Optional<User.Principal> getLoginUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getPrincipal() instanceof User.Principal)
                .map(authentication -> (User.Principal) authentication.getPrincipal());
    }

    @Override
    public User.UserAccount loadUserByUserName(String name) {
        return securityRepository.findUserByEmail(name);
    }
}
