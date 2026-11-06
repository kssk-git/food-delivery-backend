package com.example.demo.service;

import com.example.demo.dto.AuthRespDto;
import com.example.demo.dto.LoginReqDto;
import com.example.demo.dto.RegisterReqDto;
import com.example.demo.dto.UserRespDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repository, UserMapper mapper,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public UserRespDto register(RegisterReqDto request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        Role role = request.getRole() == null ? Role.CUSTOMER : request.getRole();
        if (role == Role.ADMIN) {
            throw new BadRequestException("Cannot register as ADMIN");
        }

        String encoded = passwordEncoder.encode(request.getPassword());
        User saved = repository.save(mapper.mapToEntity(request, encoded, role));
        return mapper.mapToDto(saved);
    }


    public AuthRespDto login(LoginReqDto request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        AuthRespDto response = new AuthRespDto();
        response.setToken(jwtUtil.generateToken(user.getEmail(), user.getRole()));
        response.setUser(mapper.mapToDto(user));
        return response;
    }
}

