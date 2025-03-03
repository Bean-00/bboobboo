package net.todo.core.security.service;

import net.todo.core.security.dto.User;

import java.util.Map;
import java.util.Optional;

public interface SecurityService {
    Optional<User.UserAccount> getLoginUser();

    User.UserAccount loadUserByGoogleId(String googleId);

    User.UserAccount joinUser(User.UserAccount build);
}
