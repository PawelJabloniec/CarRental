package com.example.carrental.service.UserService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.User.User;
import com.example.carrental.domainDto.UserDto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UsersService {

    User createUser(UserDto userDto);

    User updateUser(UserDto userDto, String id) throws Exception;

    Collection<User> getAllUsers();

    //TODO Zwraa ilosc wypozyczonych aut

    Optional<User> getUserById(String id);

    void deleteUserById(String id) throws Exception;

    void rentCar(String userId, String carId) throws Exception;

    void returnCar(String userId, String carId) throws Exception;

}
