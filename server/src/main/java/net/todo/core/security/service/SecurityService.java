package net.todo.core.security.service;

import net.todo.core.security.dto.User;

import java.util.Optional;

public interface SecurityService {
    Optional<User.Principal> getLoginUser();
}
