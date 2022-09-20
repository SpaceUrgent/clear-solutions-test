package com.test.userapp.service;


import com.test.userapp.entity.User;
import com.test.userapp.exception.DataProcessingException;
import com.test.userapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class UserService_Test {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void update_nonExistingId_throwsException()
            throws IllegalAccessException {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> userService.update(100L, new User()));
        assertEquals("Can't find user with id 100", exception.getMessage());
    }

    @Test
    public void delete_nonExistingId_throwsException()
            throws IllegalAccessException {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(false);

        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> userService.delete(1L));
        assertEquals("Can't delete. No user with id 1", exception.getMessage());
    }
}
