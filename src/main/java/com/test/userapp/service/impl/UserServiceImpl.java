package com.test.userapp.service.impl;

import com.test.userapp.entity.User;
import com.test.userapp.repository.UserRepository;
import com.test.userapp.service.UserService;
import com.test.userapp.utils.PropertyCopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User update(Long id, User updatedUser) {
        User userFromDb = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user with id" + id)
        );
        try {
            PropertyCopyHelper.copyNonNullProperties(updatedUser, userFromDb);
        } catch (Exception e) {
                throw new RuntimeException("Error occurred during updating properties ", e);
        }
        return userRepository.save(userFromDb);
    }

    @Override
    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Can't delete. No user with id " + id);
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public List<User> searchByBirthDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new RuntimeException("Wrong search date range: " + from + " can't be after " + to);
        }
        return userRepository.findAllByBirthDateBetween(from, to);
    }
}
