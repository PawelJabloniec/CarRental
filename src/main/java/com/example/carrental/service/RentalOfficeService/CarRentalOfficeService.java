package com.example.carrental.service.RentalOfficeService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.domain.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarRentalOfficeService {

    CarRentalOffice getCarRentalOfficeById(String id);

    List<CarRentalOffice> getAllCarRentalOffices();

    List<CarRentalOffice> getCarRentalOfficeByDateTime (LocalDateTime dateTime);

    boolean rentACar(String userId, String carId) throws Exception;

    void returnACar(String userId, String carId) throws Exception;

    List<Car> filterCarsByCarStatus(CarStatus carStatus);

    List<Car> filterCarsByBodyType(String bodyType);

    List<Car> filterCarsByDayPrice(BigDecimal from, BigDecimal to);

    List<Car> filterCarsByYearOfProduction(int yearOfProduction);

    List<Car> filterCarsByMark(String mark);

    User findUserByUserLogin(String login) throws Exception;

    User findUserByEmail(String email);





}
