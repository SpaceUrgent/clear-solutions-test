package com.test.userapp.service;

import java.time.LocalDate;
import java.util.List;
import com.test.userapp.entity.User;

public interface UserService {
    User save(User user);
    User update(Long id, User user);
    boolean delete(Long id);
    List<User> searchByBirthDateRange(LocalDate from, LocalDate to);
}
