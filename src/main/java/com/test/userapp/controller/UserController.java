package com.test.userapp.controller;

import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.dto.request.UserPatchRequestDto;
import com.test.userapp.entity.User;
import com.test.userapp.service.UserService;
import com.test.userapp.service.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserMapper userMapper;
    private UserService userService;

    @Autowired
    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserCreateRequestDtoWrapper userDtoWrapper) {
        User user = userService.save(userMapper.fromDto(userDtoWrapper.getData()));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<User> update(@PathVariable Long id,
                                       @RequestBody @Valid UserPatchRequestDto userPatchRequestDto)
            throws IllegalAccessException {

        User user = userService.update(id, userMapper.fromDto(userPatchRequestDto));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
