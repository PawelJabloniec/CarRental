package com.example.carrental.service.CarService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarException;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.RentalBranch.RentalBranch;
import com.example.carrental.domainDto.CarDto;
import com.example.carrental.repository.CarsRepository;
import com.example.carrental.repository.RentalBranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CarsServiceImpl implements CarsService {

    private final CarsRepository carsRepository;
    private final RentalBranchRepository rentalBranchRepository;

    public CarsServiceImpl(CarsRepository carsRepository, RentalBranchRepository rentalBranchRepository) {
        this.carsRepository = carsRepository;
        this.rentalBranchRepository = rentalBranchRepository;
    }

    @Override
    public Car createCar(CarDto carDto, Long rentalBranchId) {
        RentalBranch rentalBranchById = rentalBranchRepository.findRentalBranchById(rentalBranchId);
        Car car = Car.builder()
                .rentalBranchId(rentalBranchId)
                .mark(carDto.getMark())
                .model(carDto.getModel())
                .bodyType(carDto.getBodyType())
                .yearOfProduction(carDto.getYearOfProduction())
                .colour(carDto.getColour())
                .run(carDto.getRun())
                .carStatus(carDto.getCarStatus())
                .dayPrice(carDto.getDayPrice())
                .build();
        carsRepository.save(car);
        rentalBranchById.getCars().add(car);
        return car;
    }

    @Override
    public void updateCar(CarDto carDto, Long id) throws Exception{
        Car carToUpdate = getCarById(id);
        Car car = Car.builder()
                .id(carToUpdate.getId())
                .rentalBranchId(carDto.getRentalBranchId())
                .mark(carDto.getMark())
                .model(carDto.getModel())
                .bodyType(carDto.getBodyType())
                .yearOfProduction(carDto.getYearOfProduction())
                .colour(carDto.getColour())
                .run(carDto.getRun())
                .carStatus(carDto.getCarStatus())
                .dayPrice(carDto.getDayPrice())
                .build();
        carsRepository.save(car);
    }

    @Override
    public List<Car> getAllCars() {
        return carsRepository.findAll();
    }

    @Override
    public Car getCarById(Long id) throws CarException {
        return validCarId(id);
    }

    @Override
    public List<Car> filterCarsByCarStatus(CarStatus carStatus) {
        return carsRepository.findAll().stream()
                .filter(car -> car.getCarStatus().equals(carStatus))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByBodyType(String bodyType) {
        return carsRepository.findAll().stream()
                .filter(car -> car.getBodyType().equals(bodyType))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByDayPrice(BigDecimal from, BigDecimal to) {
        return carsRepository.findAll().stream()
                .filter(car -> isInRange(car.getDayPrice(), from, to))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCarById(Long id) throws CarException {
        Car carById = validCarId(id);
        carsRepository.delete(carById);
        log.info("Car with id" + id + " successfully deleted");
    }

    private boolean isInRange(BigDecimal price, BigDecimal from, BigDecimal to) {
        return price.compareTo(from)  > 0  && price.compareTo(to) < 0;
    }

    private Car validCarId(Long id) throws CarException {
       return Optional.of(carsRepository.findCarById(id)).orElseThrow(() -> new CarException("Couldn't find specific id: " + id));
    }
}
