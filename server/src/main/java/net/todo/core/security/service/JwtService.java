package net.todo.core.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.todo.core.security.dto.JwtDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

    public static final String BEARER = "Bearer ";

    @Value("${jwt.atk-expired-time}")
    private long atkExpiredTime;

    @Value("${jwt.rtk-expired-time}")
    private long rtkExpiredTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String createAccessToken(Authentication authentication) {
       return createToken(authentication.getName(), atkExpiredTime);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication.getName(), rtkExpiredTime);
    }

    public String createAccessToken(String subject) {
        return createToken(subject, atkExpiredTime);
    }

    public String createRefreshToken(String subject) {
        return createToken(subject, rtkExpiredTime);
    }

    public JwtDTO verifyToken(String token) {
        try {
            String subject =  Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return JwtDTO.builder()
                    .isVaildToken(true)
                    .subject(subject)
                    .build();
        } catch (Exception e) {
            return JwtDTO.builder()
                    .isVaildToken(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    public long getRtkExpiredTime() {
        return rtkExpiredTime;
    }

    private String createToken(String subject, long expiredTime) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("exp", Instant.now().getEpochSecond() + expiredTime)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), algorithm)
                .compact();
    }
}
