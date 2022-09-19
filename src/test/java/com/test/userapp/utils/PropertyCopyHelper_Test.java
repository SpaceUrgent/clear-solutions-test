package com.test.userapp.utils;

import com.test.userapp.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyCopyHelper_Test {

    @Test
    public void copyNonNullProperties_ok() {
        List<User> usersTo = List.of(
                new User(1L, "email@domain.com", "Frodo", "Baggings",
                        LocalDate.of(1990, 10, 10), "", ""),
                new User(2L, "grey@domain.com", "Gendalf", "Grey",
                        LocalDate.of(1900, 10, 10), "", ""),
                new User(3L, "user@domain.com", "Name", "Surname",
                        LocalDate.of(2000, 10, 10), "", "")
        );
        List<User> usersFrom = List.of(
                new User(null, "frodo@domain.com", null, null,
                        null, null, null),
                new User(null, "white@domain.com", null, "White",
                        null, null, null),
                new User(3L, "bob@domain.com", "Bob", "Bob",
                        LocalDate.of(2001, 10, 10), "address", "phone")
        );
        List<User> expected = List.of(
                new User(1L, "frodo@domain.com", "Frodo", "Baggings",
                        LocalDate.of(1990, 10, 10), "", ""),
                new User(2L, "white@domain.com", "Gendalf", "White",
                        LocalDate.of(1900, 10, 10), "", ""),
                new User(3L, "bob@domain.com", "Bob", "Bob",
                        LocalDate.of(2001, 10, 10), "address", "phone")
        );
        for (int i = 0; i < usersFrom.size(); i++) {
            PropertyCopyHelper.copyNonNullProperties(usersFrom.get(i), usersTo.get(i));
            assertEquals(expected.get(i), usersTo.get(i));
        }

    }
}
