package net.todo.core.security.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.todo.core.exception.CustomException;
import net.todo.core.security.dto.JwtDTO;
import net.todo.core.security.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static net.todo.core.exception.CustomExceptionCode.JWT_NOT_VALID;
import static net.todo.core.security.service.JwtService.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
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
        String accessToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.isEmpty(accessToken) || !accessToken.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(BEARER.length());
        JwtDTO atkJwtDTO = jwtService.verifyToken(accessToken);
        boolean isRefreshAtk = false;

        if (!atkJwtDTO.isVaildToken() && Objects.nonNull(request.getCookies())) {
            String refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("rtk"))
                    .map(Cookie::getValue)
                    .findAny().orElse(null);
            JwtDTO rtkJwtDTO = jwtService.verifyToken(refreshToken);
            if (rtkJwtDTO.isVaildToken()) {
                isRefreshAtk = true;
                atkJwtDTO.setVaildToken(true);
                atkJwtDTO.setSubject(rtkJwtDTO.getSubject());
            }

        }

        if (atkJwtDTO.isVaildToken()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(atkJwtDTO.getSubject());
            if (Objects.nonNull(userDetails)) { //스프링 시큐리티가 동작하기 위해 있는 부분 JWT와 상관 X
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                if (isRefreshAtk) {
                    response.setHeader("atk", BEARER + jwtService.createAccessToken(authentication));
                }
                chain.doFilter(request, response);
                return;
            }
        }

        log.error(atkJwtDTO.getErrorMessage());
        throw new CustomException(JWT_NOT_VALID);
    }
}
