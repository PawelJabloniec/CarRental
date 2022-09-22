package com.example.carrental.service.CarService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarException;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domainDto.CarDto.CarDto;

import java.math.BigDecimal;
import java.util.List;

public interface CarsService {

    Car createCar(CarDto carDto, Long rentalBranchId);

    void updateCar(CarDto carDto, Long id) throws Exception;

    List<Car> getAllCars();

    Car getCarById(Long id) throws CarException;

    void deleteCarById(Long id) throws CarException;

    List<Car> filterCarsByDayPrice(BigDecimal from, BigDecimal to);

    List<Car> filterCarsByBodyType(String bodyType);

    List<Car> filterCarsByCarStatus(CarStatus carStatus);

}
