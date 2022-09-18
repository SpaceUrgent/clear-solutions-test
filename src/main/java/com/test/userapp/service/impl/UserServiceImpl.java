package com.test.userapp.service.impl;

import com.test.userapp.entity.User;
import com.test.userapp.repository.UserRepository;
import com.test.userapp.service.UserService;
import com.test.userapp.utils.PropertyCopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User updatedUser) throws IllegalAccessException {
        User userFromDb = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user with id" + id)
        );
        PropertyCopyHelper.copyNonNullProperties(updatedUser, userFromDb);
        return userRepository.save(userFromDb);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<User> searchByBirthDateRange(LocalDate from, LocalDate to) {
        return null;
    }
}
