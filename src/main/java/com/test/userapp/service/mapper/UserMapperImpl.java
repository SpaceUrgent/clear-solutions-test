package com.test.userapp.service.mapper;

import com.test.userapp.dto.request.UserCreateRequestDto;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User fromDto(UserCreateRequestDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());
        return user;
    }

    @Override
    public User fromDto(UserPatchRequestDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    @Override
    public UserCreateRequestDto toDto(User user) {
        return null;
    }
}
