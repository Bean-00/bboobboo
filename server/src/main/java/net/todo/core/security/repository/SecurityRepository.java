package net.todo.core.security.repository;

import net.todo.core.security.dto.User;

public interface SecurityRepository {
    User.UserAccount findUserByEmail(String name);
}
