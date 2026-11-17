package com.foodapp.controller;

import com.foodapp.dto.AuthRespDto;
import com.foodapp.dto.LoginReqDto;
import com.foodapp.dto.RegisterReqDto;
import com.foodapp.dto.UserRespDto;
import com.foodapp.service.AuthService;
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

    @PostMapping("/login")
    public AuthRespDto login(@Valid @RequestBody LoginReqDto request) {
        return authService.login(request);
    }
}