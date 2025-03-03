package net.todo.core.security.constant;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum UserRole {
    SYS_ADMIN(0, "SYS_ADMIN"),
    ADMIN(1, "ADMIN"),
    USER(4, "USER");

    private final int id;
    private final String str;

    @Override
    public String toString() {
        return str;
    }

    public int getId() {
        return id;
    }

    public static UserRole getUserRoleById(int id) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.id == id)
                .findAny()
                .orElse(USER);
    }

    public static UserRole getUserRoleByStr(String str) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.str.equals(str))
                .findAny()
                .orElse(USER);
    }
}
