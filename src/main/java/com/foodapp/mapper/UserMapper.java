package com.foodapp.mapper;

import com.foodapp.dto.RegisterReqDto;
import com.foodapp.dto.UserRespDto;
import com.foodapp.entity.Role;
import com.foodapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToEntity(RegisterReqDto request, String encodedPassword, Role role) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
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
