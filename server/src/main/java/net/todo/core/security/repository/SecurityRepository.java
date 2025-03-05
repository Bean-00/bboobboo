package net.todo.core.security.repository;

import net.todo.core.security.dto.User;

public interface SecurityRepository {
    User.UserAccount findUserByUsername(String name);

    User.UserAccount findUserByGoogleId(String googleId);

    void persistUser(User.UserAccount user);

    User.UserAccount findUserByKakaoId(String kakaoId);
}
