package com.example.demo.service;

import com.example.demo.dto.RegisterReqDto;
import com.example.demo.dto.UserRespDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserRespDto register(@Valid @RequestBody RegisterReqDto request) {
        return authService.register(request);
    }
}