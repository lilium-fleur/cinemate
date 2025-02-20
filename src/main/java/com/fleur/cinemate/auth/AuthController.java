package com.fleur.cinemate.auth;

import com.fleur.cinemate.auth.dto.AuthDto;
import com.fleur.cinemate.auth.dto.LoginDto;
import com.fleur.cinemate.auth.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(
            @RequestBody @Valid RegisterDto registerDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.register(registerDto, request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(
            @RequestBody @Valid LoginDto loginDto,
            HttpServletResponse response,
            HttpServletRequest request){
        return ResponseEntity.ok(authService.login(loginDto, request, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDto> refresh(
            @CookieValue("__tar") String refreshToken,
            @CookieValue("__paf") String fingerprint){
        return ResponseEntity.ok(authService.refresh(refreshToken, fingerprint));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("__tar") String refreshToken,
            @CookieValue("__paf") String fingerprint,
            HttpServletResponse response){
        authService.logout(refreshToken, fingerprint, response);
        return ResponseEntity.noContent().build();
    }

}
