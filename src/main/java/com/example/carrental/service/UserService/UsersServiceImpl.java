package com.example.carrental.service.UserService;

import com.example.carrental.domain.User.CarRentalUser;
import com.example.carrental.domain.User.UserException;
import com.example.carrental.domainDto.UserDto.UserDto;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.security.SecurityConfig;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CarRentalUser createUser(UserDto userDto) {

        CarRentalUser user = CarRentalUser.builder()
                .userLogin(userDto.getUserLogin())
                .userPassword(userDto.getUserPassword())
                .userName(userDto.getUserName())
                .userLastName(userDto.getUserLastName())
                .userEMail(userDto.getUserEMail())
                .userAddress(userDto.getUserAddress())
                .userCarId(userDto.getUserCarId())
                .role(userDto.getRole())
                .status(userDto.getStatus())
                .build();

        SecurityConfig.addUserSecurity(user);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUser(UserDto userDto, Long id) throws Exception {
        CarRentalUser userToUpdate = getUserById(id);

        userRepository.save(new CarRentalUser(userToUpdate.getId(), userDto.getUserLogin(), userDto.getUserPassword(),
                userDto.getUserName(), userDto.getUserLastName(), userDto.getUserEMail(), userDto.getUserAddress(),
                userDto.getUserCarId(), userDto.getRole(), userDto.getStatus()));
    }

    @Override
    public List<CarRentalUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public CarRentalUser getUserById(Long id)   {
        Long userLongId = Optional.of(id)
                .orElseThrow(() -> new UserException("No Client found in DB"));
        return userRepository.findUserById(userLongId);
    }

    @Override
    public CarRentalUser findUserByUserLogin(String login) throws UserException{
        return Optional.of(login)
                .map(this::getUserByLogin)
                .orElseThrow(() -> new UserException("User not found!"));
    }

    @Override
    public CarRentalUser findUserByUserEmail(String email) throws UserException{
        return Optional.of(email)
                .map(this::getUserByEmail)
                .orElseThrow(() -> new UserException("Not found User with given E-mail"));
    }

    @Override
    public void deleteUserById(Long id) {
        CarRentalUser userById = getUserById(id);
        userRepository.delete(userById);
    }

    private CarRentalUser getUserByLogin(String login) throws UserException {
        return userRepository.findAll().stream()
                .filter(user -> user.getUserLogin().equals(login))
                .findFirst().orElseThrow(() -> new UserException("Not found User with given login"));
    }

    private CarRentalUser getUserByEmail(String email) throws UserException {
        return userRepository.findAll().stream()
                .filter(user -> user.getUserEMail().equals(email))
                .findFirst().orElseThrow(() -> new UserException("Not found User with given E-mail"));
    }
}
