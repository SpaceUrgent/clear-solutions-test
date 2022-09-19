package com.test.userapp.controller;

import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.dto.request.UserPatchRequestDtoWrapper;
import com.test.userapp.dto.response.UserResponseDto;
import com.test.userapp.dto.response.UserResponseDtoWrapper;
import com.test.userapp.entity.User;
import com.test.userapp.service.UserService;
import com.test.userapp.service.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity register(@RequestBody @Valid UserCreateRequestDtoWrapper userDtoWrapper)
            throws URISyntaxException {
        User user = userService.save(userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.created(new URI("/users/" + user.getId()))
                .build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity patch(@PathVariable Long id,
                                       @RequestBody @Valid UserPatchRequestDtoWrapper userDtoWrapper)
            throws IllegalAccessException {
        userService.update(id, userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> update(@PathVariable Long id,
                                       @RequestBody @Valid UserCreateRequestDtoWrapper userDtoWrapper)
            throws IllegalAccessException {
        User user = userService.update(id, userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDtoWrapper> findWithinBirthDateRange(@RequestParam("from") LocalDate from,
                                                               @RequestParam("to") LocalDate to) {
        List<User> users = userService.searchByBirthDateRange(from, to);
        List<UserResponseDto> userResponseDtos = users.stream()
                .map(user -> userMapper.toDto(user))
                .toList();
        return ResponseEntity.ok(new UserResponseDtoWrapper(userResponseDtos));
    }
}
