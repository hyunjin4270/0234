package com.example.index.fantastic_app.auth.controller;

import com.example.index.fantastic_app.auth.dto.LoginRequest;
import com.example.index.fantastic_app.auth.dto.LoginResponse;
import com.example.index.fantastic_app.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        req.username(), req.password());

        authManager.authenticate(authToken);  // 비밀번호 검증 (BCrypt 매칭)

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(req.username());

        String jwt = jwtProvider.generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority());

        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }
}
