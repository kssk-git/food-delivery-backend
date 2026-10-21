package com.example.demo.mapper;

import com.example.demo.dto.RegisterReqDto;
import com.example.demo.dto.UserRespDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToEntity(RegisterReqDto request, String encodedPassword) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(Role.CUSTOMER);
        return user;
    }

    public UserRespDto mapToDto(User user) {
        UserRespDto response = new UserRespDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
