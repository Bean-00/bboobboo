package net.todo.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.todo.core.exception.CustomException;
import net.todo.core.exception.CustomExceptionCode;
import net.todo.core.exception.ErrorResponse;
import net.todo.core.security.authentication.CustomAuthenticationToken;
import net.todo.core.security.dto.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.InputStream;

import static net.todo.core.exception.CustomExceptionCode.FAILURE_UNAUTHORIZED;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        setAuthenticationSuccessHandler(getAuthenticationSuccessHandler());
        setAuthenticationFailureHandler(getAuthenticationFailureHandler());
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(authentication.getPrincipal()));

        };
    }

    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return (request, response, exception) -> {

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .errorCode(FAILURE_UNAUTHORIZED.getCode())
                    .errorClassName(exception.getClass().getName())
                    .errorMessage(FAILURE_UNAUTHORIZED.getMessage())
                    .httpStatusCode(FAILURE_UNAUTHORIZED.getHttpStatus().value())
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new Gson().toJson(errorResponse));
        };
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = request.getInputStream()) {

            User.LoginRequest loginRequest = objectMapper.readValue(inputStream, User.LoginRequest.class);
            User.Principal principal = User.Principal.builder()
                    .email(loginRequest.getEmail())
                    .build();

            CustomAuthenticationToken token = CustomAuthenticationToken.builder()
                    .credentials(loginRequest.getPassword())
                    .principal(principal)
                    .build();

            return this.getAuthenticationManager().authenticate(token);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.NOT_SUPPORTED_CONTENT_TYPE);
        }
    }
}
