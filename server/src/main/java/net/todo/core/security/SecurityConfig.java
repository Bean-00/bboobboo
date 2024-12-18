package net.todo.core.security;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import net.todo.core.exception.CustomException;
import net.todo.core.exception.CustomExceptionHandler;
import net.todo.core.exception.ErrorResponse;
import net.todo.core.security.authentication.CustomAuthenticationProvider;
import net.todo.core.security.filter.CustomUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.nio.charset.StandardCharsets;

import static net.todo.core.exception.CustomExceptionCode.*;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/security/login")
                .permitAll()
                .anyRequest()
                .authenticated())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new CustomUsernamePasswordAuthenticationFilter(authenticationManager, securityContextRepository), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(getAccessDeniedHandler())
                .authenticationEntryPoint(getAuthenticationEntryPoint()));

        return http.build();
    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        // 인증 X API 접근 (403 forbidden)
        return (request, response, authentication) -> {
            CustomExceptionHandler.writeSecurityExceptionResponse(response, USER_FORBIDDEN);
        };
    }

    private AccessDeniedHandler getAccessDeniedHandler() {
        // 인증 O 인가 X API 접근 (401 authorization)
        return (request, response, authentication) -> {
           CustomExceptionHandler.writeSecurityExceptionResponse(response, USER_UNAUTHORIZED);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}
