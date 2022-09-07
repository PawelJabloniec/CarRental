package com.example.carrental.repository;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.domain.RentalOffice.CarRentalOfficeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CarsRentalOfficeRepository extends JpaRepository<CarRentalOffice, String> {
    CarRentalOffice findCarRentalOfficeById(String id) throws CarRentalOfficeException;

}
