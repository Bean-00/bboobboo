package net.todo.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CustomExceptionCode {
    TODO_CONTENT_NOT_NULL("T001", HttpStatus.BAD_REQUEST, "Content 값은 Null일 수 없습니다."),
    NOT_SUPPORTED_CONTENT_TYPE("S001", HttpStatus.BAD_REQUEST, "지원하지 않는 Content type입니다."),
    FAILURE_AUTHENTICATION("S002", HttpStatus.BAD_REQUEST, "인증에 실패했습니다."),
    USER_UNAUTHORIZED("S004", HttpStatus.UNAUTHORIZED, "로그인 후 사용가능합니다."),
    USER_FORBIDDEN("S005", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    FILE_NOT_FOUND("F001", HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),
    JWT_NOT_VALID("S006", HttpStatus.UNAUTHORIZED, "JWT가 올바르지 않거나 만료되었습니다.");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
