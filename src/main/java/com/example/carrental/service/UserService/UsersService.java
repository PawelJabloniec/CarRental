package com.example.carrental.service.UserService;

import com.example.carrental.domain.User.CarRentalUser;
import com.example.carrental.domain.User.UserException;
import com.example.carrental.domainDto.UserDto.UserDto;

import java.util.List;

public interface UsersService {

    CarRentalUser createUser(UserDto userDto);

    void updateUser(UserDto userDto, Long id) throws Exception;

    List<CarRentalUser> getAllUsers();

    CarRentalUser getUserById(Long id);

    CarRentalUser findUserByUserEmail(String email) throws UserException;

    CarRentalUser findUserByUserLogin(String login) throws UserException;

    void deleteUserById(Long id) throws Exception;

}
