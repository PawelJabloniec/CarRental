package com.example.carrental.service.RentalOfficeService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarException;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.Income.Income;
import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.domain.RentalOffice.CarRentalOfficeException;
import com.example.carrental.domain.User.CarRentalUser;
import com.example.carrental.domain.User.UserException;
import com.example.carrental.domainDto.CarDto;
import com.example.carrental.domainDto.UserDto;
import com.example.carrental.repository.CarsRentalOfficeRepository;
import com.example.carrental.repository.IncomeRepository;
import com.example.carrental.service.CarService.CarsService;
import com.example.carrental.service.IncomeService.IncomesService;
import com.example.carrental.service.UserService.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarsRentalOfficeImpl implements CarRentalOfficeService {

    private final CarsRentalOfficeRepository carsRentalOfficeRepository;

    private final CarsService carsService;

    private final UsersService usersService;

    private final IncomesService incomeService;

    private final IncomeRepository incomeRepository;

    public CarsRentalOfficeImpl(CarsRentalOfficeRepository carsRentalOfficeRepository, CarsService carsService, UsersService usersService, IncomesService incomeService, IncomeRepository incomeRepository) {
        this.carsRentalOfficeRepository = carsRentalOfficeRepository;
        this.carsService = carsService;
        this.usersService = usersService;
        this.incomeService = incomeService;
        this.incomeRepository = incomeRepository;
    }

    @Override
    public CarRentalOffice getCarRentalOfficeById(Long id) throws CarRentalOfficeException {
        return Optional.of(carsRentalOfficeRepository.findById(id))
                .orElseThrow(() -> new CarRentalOfficeException("Rental not found"));
    }

    @Override
    public List<CarRentalOffice> getAllCarRentalOffices() {
        return findAllDto();
    }

    @Override
    public List<CarRentalOffice> findAllDto() {
        return carsRentalOfficeRepository.findAll();
    }

    @Override
    public List<CarRentalOffice> findCarRentalOfficeByLocalDateTimeOfRent(LocalDateTime dateTime) throws CarRentalOfficeException {
        return carsRentalOfficeRepository.findAll()
                .stream()
                .filter(car -> car.getLocalDateTimeOfRent().equals(dateTime))
                .collect(Collectors.toList());
    }

    @Override
    public void rentACar(Long userId, Long carId) throws Exception {
        changeCarStatusInCarAndUser(userId, carId, CarStatus.RENTED);
        createCarRentalOffice(userId, carId);
    }

    @Override
    public void returnACar(Long carRentalOfficeId) throws Exception {
        changeCarStatusInCarAndUser(getCarRentalOfficeById(carRentalOfficeId).getUser().getId(),
                getCarRentalOfficeById(carRentalOfficeId).getCar().getId(), CarStatus.AVAILABLE);
        updateCarRentalOffice(carRentalOfficeId);
    }

    private boolean changeCarStatusInCarAndUser(Long userId, Long carId, CarStatus carStatus) throws Exception {
        CarRentalUser user = getUserById(userId);
        Car carToRent = getCarById(carId);
        if(carToRent.getCarStatus() == CarStatus.AVAILABLE || carToRent.getCarStatus() == CarStatus.RENTED) {
            if (user.getUserCarId() == null || user.getUserCarId().equals(carId)) {
                updateCarStatus(carToRent, carStatus);
                updateUserCarStatus(carId, carStatus, user);
                return true;
            }
            throw new CarRentalOfficeException("Value doesn't match, please write proper value!");
        }
        throw new CarException("Car can't be be return or rent");
    }

    private void updateUserCarStatus(Long carId, CarStatus carStatus, CarRentalUser user) throws Exception {
        if (carStatus.equals(CarStatus.AVAILABLE)) {
            user.setUserCarId(null);
        } else {
            user.setUserCarId(carId);
        }
        usersService.updateUser(new UserDto(user.getUserLogin(), user.getUserPassword(), user.getUserName(),
                user.getUserLastName(), user.getUserEMail(), user.getUserAddress(), user.getUserCarId(), user.getRole(), user.getStatus()), user.getId());
    }

    private CarRentalOffice createCarRentalOffice(Long userId, Long carId) throws CarException {
        CarRentalOffice newCarRentalOffice = CarRentalOffice.builder()
                .user(usersService.getUserById(userId))
                .car(carsService.getCarById(carId))
                .localDateTimeOfRent(LocalDateTime.now())
                .getLocalDateTimeOfReturn(null)
                .build();
        carsRentalOfficeRepository.save(newCarRentalOffice);

        Income income = incomeService.createIncome(carsService.getCarById(carId).getDayPrice());
        incomeRepository.save(income);

        return newCarRentalOffice;
    }

    private void updateCarRentalOffice(Long carRentalOfficeId) {
        CarRentalOffice rentalOfficeToUpdate = Optional.of(carsRentalOfficeRepository.findById(carRentalOfficeId))
                .orElseThrow(() -> new CarRentalOfficeException("Couldn't find rent"));
        CarRentalOffice updatedCarRentalOffice = new CarRentalOffice(carRentalOfficeId, rentalOfficeToUpdate.getUser(),
                rentalOfficeToUpdate.getCar(), rentalOfficeToUpdate.getLocalDateTimeOfRent(),
                LocalDateTime.now());
        carsRentalOfficeRepository.save(updatedCarRentalOffice);
    }

    private void updateCarStatus(Car carToRent, CarStatus carStatus) throws Exception {
        carsService.updateCar(CarDto.builder()
                .rentalBranchId(carToRent.getRentalBranchId())
                .mark(carToRent.getMark())
                .model(carToRent.getModel())
                .bodyType(carToRent.getBodyType())
                .yearOfProduction(carToRent.getYearOfProduction())
                .colour(carToRent.getColour())
                .run(carToRent.getRun())
                .carStatus(carStatus)
                .dayPrice(carToRent.getDayPrice())
                .build(), carToRent.getId());
    }

    private Car getCarById(Long carId) throws CarException {
        Car carToRent = Optional.of(carsService.getCarById(carId)).orElseThrow(() -> new CarException("No car with given id"));
        return carToRent;
    }

    private CarRentalUser getUserById(Long userId) {
        return Optional.of(usersService.getUserById(userId)).orElseThrow(() -> new UserException("No client with given ID"));
    }

}
