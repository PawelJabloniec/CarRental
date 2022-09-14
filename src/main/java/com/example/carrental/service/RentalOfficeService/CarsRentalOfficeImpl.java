package com.example.carrental.service.RentalOfficeService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarException;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.Income.Income;
import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.domain.RentalOffice.CarRentalOfficeException;
import com.example.carrental.domain.User.User;
import com.example.carrental.domain.User.UserException;
import com.example.carrental.domainDto.CarDto.CarDto;
import com.example.carrental.domainDto.RentalOffice.CarRentalOfficeDto;
import com.example.carrental.domainDto.RentalOffice.CarRentalOfficeList;
import com.example.carrental.domainDto.UserDto.UserDto;
import com.example.carrental.repository.CarsRentalOfficeRepository;
import com.example.carrental.repository.IncomeRepository;
import com.example.carrental.service.CarService.CarsService;
import com.example.carrental.service.IncomeService.IncomesService;
import com.example.carrental.service.UserService.UsersService;
import org.springframework.stereotype.Service;

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
    public void returnACar(Long userId, Long carId, Long carRentalOfficeId) throws Exception {
        changeCarStatusInCarAndUser(userId, carId, CarStatus.AVAILABLE);
        updateCarRentalOffice(carRentalOfficeId);

    }
//Transactional
    private boolean changeCarStatusInCarAndUser(Long userId, Long carId, CarStatus carStatus) throws Exception {
        User user = getUserById(userId);
        Car carToRent = getCarById(carId);
        if (user.getUserCarId() != null && user.getUserCarId().equals(carId)) {
            updateCarStatus(carToRent, carStatus);
            updateUserCarStatus(carId, carStatus, user);
            return true;
        } else if (user.getUserCarId() == null) {
            updateCarStatus(carToRent, carStatus);
            updateUserCarStatus(carId, carStatus, user);
            return true;
        }
        throw new CarRentalOfficeException("Value doesn't match, please write proper value!");
    }

    private void updateUserCarStatus(Long carId, CarStatus carStatus, User user) throws Exception {
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
        carsService.updateCar(new CarDto(carToRent.getRentalBranchId(), carToRent.getMark(), carToRent.getModel(), carToRent.getBodyType(),
                carToRent.getYearOfProduction(), carToRent.getColour(), carToRent.getRun(),
                carStatus, carToRent.getDayPrice()), carToRent.getId());
    }

    private boolean checkCarStatusMatchVariable(Car carToRent) {
        if (carToRent.getCarStatus() == CarStatus.AVAILABLE) {
//            carToRent.setCarStatus(CarStatus.RENTED);
            return true;
        } else if (carToRent.getCarStatus() == CarStatus.RENTED) {
//            carToRent.setCarStatus(CarStatus.AVAILABLE);
            return true;
        }
        return false;
    }

    private Car getCarById(Long carId) throws CarException {
        Car carToRent = Optional.of(carsService.getCarById(carId)).orElseThrow(() -> new CarException("No car with given id"));
        return carToRent;
    }

    private boolean haveUserCarRent(User user) {
        //jeżeli ma wynajęte carId = 3
        if (user.getUserCarId() != null) {
            new CarException("User has already rented car");
            return false;
        }
        return true;
    }

    private User getUserById(Long userId) {
        return Optional.of(usersService.getUserById(userId)).orElseThrow(() -> new UserException("No client with given ID"));
    }

}
