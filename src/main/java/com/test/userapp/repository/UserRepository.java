package com.test.userapp.repository;

import java.time.LocalDate;
import java.util.List;
import com.test.userapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to);
}
