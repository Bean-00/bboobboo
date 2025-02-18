package net.todo.core.security.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.todo.core.exception.CustomException;
import net.todo.core.security.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;

import static net.todo.core.exception.CustomExceptionCode.JWT_NOT_VALID;
import static net.todo.core.security.service.JwtService.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && !token.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        token = token.substring(BEARER.length());
        String userName = jwtService.verifyToken(token);
        if (StringUtils.isNotEmpty(userName)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (Objects.nonNull(userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
                return;
            }
        }
        throw new CustomException(JWT_NOT_VALID);
    }
}
