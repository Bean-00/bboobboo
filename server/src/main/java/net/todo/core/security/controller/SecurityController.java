package net.todo.core.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    @GetMapping("/login-user")
    public ResponseEntity<Authentication> getLoginUser (HttpServletRequest request) {
        return ResponseEntity.ok(SecurityContextHolder
                .getContext()
                .getAuthentication());
    }
}