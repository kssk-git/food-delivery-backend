package com.example.demo.service;

import com.example.demo.dto.RegisterReqDto;
import com.example.demo.dto.UserRespDto;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository, UserMapper mapper,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRespDto register(RegisterReqDto request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        String encoded = passwordEncoder.encode(request.getPassword());
        User saved = repository.save(mapper.mapToEntity(request, encoded));
        return mapper.mapToDto(saved);
    }
}

