package com.test.userapp.service;

import com.test.userapp.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User save(User user);
    User update(Long id, User user) throws IllegalAccessException;
    boolean delete(Long id);
    List<User> searchByBirthDateRange(LocalDate from, LocalDate to);
}
