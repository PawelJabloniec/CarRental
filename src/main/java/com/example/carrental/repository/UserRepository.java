package com.example.carrental.repository;

import com.example.carrental.domain.User.User;
import com.example.carrental.domain.User.UserException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUserLogin(String login) throws UserException;
}
