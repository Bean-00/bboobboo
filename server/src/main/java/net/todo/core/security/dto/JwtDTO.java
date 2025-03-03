package net.todo.core.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private boolean isVaildToken;
    private String subject;
    private String errorMessage;
}
