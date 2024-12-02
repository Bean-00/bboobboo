package net.todo.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예외 response")
public class ErrorResponse {

    @Schema(description = "Custom 예러 코드")
    private String errorCode;

    @Schema(description = "Custom 예러 메세지")
    private String errorMessage;

    @Schema(description = "Http Status")
    private int httpStatusCode;

    @Schema(description = "Error 클래스 명")
    private String errorClassName;
}
