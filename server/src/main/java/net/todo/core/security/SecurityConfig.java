package net.todo.core.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.todo.core.exception.CustomExceptionHandler;
import net.todo.core.security.authentication.CustomAuthenticationProvider;
import net.todo.core.security.filter.CustomUsernamePasswordAuthenticationFilter;
import net.todo.core.security.service.CustomRememberMeService;
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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static net.todo.core.exception.CustomExceptionCode.*;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final List<String> allowedRequestUrlList = List.of(
            "/api/security/login",
            "/api/security/login-user"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                           SecurityContextRepository securityContextRepository,
                                           RememberMeServices rememberMeServices) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(allowedRequestUrlList.toArray(String[]::new))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .addFilterAt(new CustomUsernamePasswordAuthenticationFilter(authenticationManager,
                        securityContextRepository, rememberMeServices),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler(getAccessDeniedHandler())
                        .authenticationEntryPoint(getAuthenticationEntryPoint()))
                .rememberMe(r -> r.rememberMeServices(rememberMeServices))
                .logout(logout -> logout.logoutUrl("/api/security/logout")
                        .logoutSuccessHandler(getLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));

        return http.build();
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
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));

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

//    @Bean
//    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, SecurityService securityService) {
//        return new CustomAuthenticationProvider(passwordEncoder, securityService);
//    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        return new CustomRememberMeService("uniqueAndSecretKey", userDetailsService, persistentTokenRepository);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        try {
            tokenRepository.removeUserTokens("test");
        } catch (Exception e) {
            tokenRepository.setCreateTableOnStartup(true);
        }

        return tokenRepository;

    }
}
