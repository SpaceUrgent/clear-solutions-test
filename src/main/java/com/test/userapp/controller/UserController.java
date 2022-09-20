package com.test.userapp.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import com.test.userapp.dto.request.UserCreateRequestDtoWrapper;
import com.test.userapp.dto.request.UserPatchRequestDtoWrapper;
import com.test.userapp.dto.response.UserResponseDto;
import com.test.userapp.dto.response.UserResponseDtoWrapper;
import com.test.userapp.entity.User;
import com.test.userapp.exception.InvalidDataRangeInputs;
import com.test.userapp.service.UserService;
import com.test.userapp.service.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserResponseDtoWrapper> register(@RequestBody @Valid UserCreateRequestDtoWrapper userDtoWrapper)
            throws URISyntaxException {
        User user = userService.save(userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.created(new URI("/users/" + user.getId()))
                .body(new UserResponseDtoWrapper(List.of(userMapper.toDto(user))));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserResponseDtoWrapper> patch(@PathVariable Long id,
                                       @RequestBody @Valid UserPatchRequestDtoWrapper userDtoWrapper) {
        User user = userService.update(id, userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.ok().body(new UserResponseDtoWrapper(List.of(userMapper.toDto(user))));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponseDtoWrapper> update(@PathVariable Long id,
                                       @RequestBody @Valid UserCreateRequestDtoWrapper userDtoWrapper) {
        User user = userService.update(id, userMapper.fromDto(userDtoWrapper.getData()));
        return ResponseEntity.ok().body(new UserResponseDtoWrapper(List.of(userMapper.toDto(user))));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDtoWrapper> findWithinBirthDateRange(@RequestParam("from") LocalDate from,
                                                               @RequestParam("to") LocalDate to) {
        if (from.isAfter(to)) {
            throw new InvalidDataRangeInputs("Wrong search date range: " + from + " can't be after " + to);
        }
        List<User> users = userService.searchByBirthDateRange(from, to);
        List<UserResponseDto> userResponseDtos = users.stream()
                .map(user -> userMapper.toDto(user))
                .toList();
        return ResponseEntity.ok(new UserResponseDtoWrapper(userResponseDtos));
    }
}
