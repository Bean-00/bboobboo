package net.todo.core.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.todo.core.exception.CustomExceptionHandler;
import net.todo.core.security.authentication.CustomAuthenticationProvider;
import net.todo.core.security.filter.CustomUsernamePasswordAuthenticationFilter;
import net.todo.core.security.filter.JwtAuthenticationFilter;
import net.todo.core.security.filter.JwtVerifyFilter;
import net.todo.core.security.oath2.CustomOauth2SuccessHandler;
import net.todo.core.security.service.CustomRememberMeService;
import net.todo.core.security.service.JwtService;
import net.todo.core.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static net.todo.core.exception.CustomExceptionCode.*;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private final List<String> allowedRequestUrlList = List.of(
            "/api/security/login",
            "/api/security/login-user",
            "/api/oauth2/**",
            "/error",
            "/api/oauth2/google"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, JwtVerifyFilter jwtVerifyFilter, CustomOauth2SuccessHandler customOauth2SuccessHandler) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(allowedRequestUrlList.toArray(String[]::new))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin.disable())
                .oauth2Login(oath2 -> oath2.authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig.baseUri("/api/oauth2/authorization")
                        ).redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/api/oauth2/*/callback"))
                        .successHandler(customOauth2SuccessHandler))
                .addFilterAt(jwtVerifyFilter, BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.accessDeniedHandler(getAccessDeniedHandler())
                        .authenticationEntryPoint(getAuthenticationEntryPoint()))
                .logout(logout -> logout.logoutUrl("/api/security/logout").logoutSuccessHandler(getLogoutSuccessHandler()).deleteCookies("rtk"));

        return http.build();
    }

    @Bean
    public CustomOauth2SuccessHandler customOauth2SuccessHandler(SecurityService securityService, JwtService jwtService) {
        return new CustomOauth2SuccessHandler(securityService, jwtService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        return new JwtAuthenticationFilter(authenticationManager, jwtService);
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return (((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        }));
    }

    private CorsConfigurationSource configurationSource() {

        return request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();

            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://todo.net", "https://todoo.o-r.kr"));
            corsConfiguration.setExposedHeaders(List.of("atk"));

            return corsConfiguration;
        };

    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        // 인증 X API 접근 (401 authorization)
        return (request, response, authentication) -> {
            CustomExceptionHandler.writeSecurityExceptionResponse(response, USER_UNAUTHORIZED);
        };
    }

    private AccessDeniedHandler getAccessDeniedHandler() {
        // 인증 O 인가 X API 접근 (403 forbidden)
        return (request, response, authentication) -> {
            CustomExceptionHandler.writeSecurityExceptionResponse(response, USER_FORBIDDEN);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(daoAuthenticationProvider));
    }

    @Bean
    public JwtVerifyFilter jwtVerifyFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        return new JwtVerifyFilter(authenticationManager, userDetailsService, jwtService);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
