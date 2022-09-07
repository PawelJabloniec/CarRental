package com.example.carrental.service.RentalOfficeService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarException;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.domain.RentalOffice.CarRentalOfficeException;
import com.example.carrental.domain.User.User;
import com.example.carrental.domain.User.UserException;
import com.example.carrental.domainDto.CarDto.CarDto;
import com.example.carrental.domainDto.UserDto.UserDto;
import com.example.carrental.repository.CarsRentalOfficeRepository;
import com.example.carrental.service.CarService.CarsService;
import com.example.carrental.service.UserService.UsersService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CarsRentalOfficeImpl implements CarRentalOfficeService {

    private final CarsRentalOfficeRepository carsRentalOfficeRepository;

    private final CarsService carsService;

    private final UsersService usersService;

    public CarsRentalOfficeImpl(CarsRentalOfficeRepository carsRentalOfficeRepository, CarsService carsService, UsersService usersService) {
        this.carsRentalOfficeRepository = carsRentalOfficeRepository;
        this.carsService = carsService;
        this.usersService = usersService;
    }

    private CarRentalOffice createCarRentalOffice(String userId, String carId) {
        System.out.println("Creating a rent");
        CarRentalOffice rent = new CarRentalOffice(UUID.randomUUID().toString(), userId, carId, LocalDateTime.now(), null);
        carsRentalOfficeRepository.save(rent);
        return rent;
    }
    private void updateCarRentalOffice(String carRentalOfficeId){
        System.out.println("Updating car rental office");
        CarRentalOffice rentalOfficeToUpdate = carsRentalOfficeRepository.findCarRentalOfficeById(carRentalOfficeId);
        CarRentalOffice updatedCarRentalOffice = new CarRentalOffice(carRentalOfficeId, rentalOfficeToUpdate.getCarId(),
                rentalOfficeToUpdate.getUserId(),rentalOfficeToUpdate.getLocalDateTimeOfRent(),
                rentalOfficeToUpdate.getLocalDateTimeOfRetun());
        carsRentalOfficeRepository.save(updatedCarRentalOffice);
    }

    @Override
    public CarRentalOffice getCarRentalOfficeById(String id) throws CarRentalOfficeException{
        return carsRentalOfficeRepository.findById(id)
                .orElseThrow(()-> new CarRentalOfficeException("Rental not found"));
    }

    @Override
    public List<CarRentalOffice> getAllCarRentalOffices() {
        return carsRentalOfficeRepository.findAll();
    }
    @Override
    public List<CarRentalOffice> getCarRentalOfficeByDateTime(LocalDateTime dateTime) {
        return carsRentalOfficeRepository.findAll()
                .stream()
                .filter(car -> car.getLocalDateTimeOfRent().equals(dateTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean rentACar(String userId, String carId) throws Exception{
        if(changeCarStatusByUser(userId, carId, CarStatus.RENTED)){
            createCarRentalOffice(userId, carId);
            return true;
        }
        return false;
    }

    @Override
    public void returnACar(String userId, String carId) throws Exception {
        if(changeCarStatusByUser(userId, carId, CarStatus.AVAILABLE)){
            updateCarRentalOffice(carId);
        }
    }

    @Override
    public List<Car> filterCarsByCarStatus(CarStatus carStatus) {
        System.out.println("FILTER_BY_STATUS cars");
        return carsService.getAllCars().stream()
                .filter(car -> car.getCarStatus().equals(carStatus))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByBodyType(String bodyType) {
        System.out.println("FILTER_BY_BODY_TYPE cars");
        return carsService.getAllCars().stream()
                .filter(car -> car.getBodyType().equals(bodyType))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByDayPrice(BigDecimal from, BigDecimal to) {
        System.out.println("FILTER_BY_PRICE cars");
        return carsService.getAllCars().stream()
                .filter(car -> isInRange(car.getDayPrice(), from, to))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByYearOfProduction(int yearOfProduction) {
        return Optional.of(yearOfProduction)
                .map(m -> carsService.getAllCars().stream()
                        .filter(car -> car.getYearOfProduction() == yearOfProduction))
                .orElse(carsService.getAllCars().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> filterCarsByMark(String mark) {
        return Optional.of(mark)
                .map (getAllCarsByMark (mark))
                .orElse(carsService.getAllCars());
    }

    @Override
    public User findUserByUserLogin(String login) throws UserException{
        return Optional.of(login)
                .map(this::getUserByLogin)
                .orElseThrow(() -> new UserException("User not found!"));
    }

    private User getUserByLogin(String login) throws UserException {
        return usersService.getAllUsers().stream()
                .filter(user -> user.getUserLogin().equals(login))
                .findFirst().orElseThrow(() -> new UserException("Not found User with given login"));
    }

    @Override
    public User findUserByEmail(String email) throws UserException{
        return Optional.of(email)
                .map(this::getUserByEmail)
                .orElseThrow(() -> new UserException("Not found User with given E-mail"));
    }

    private User getUserByEmail(String email) throws UserException {
        return usersService.getAllUsers().stream()
                .filter(user -> user.getUserEMail().equals(email))
                .findFirst().orElseThrow(() -> new UserException("Not found User with given E-mail"));
    }

    private boolean changeCarStatusByUser(String userId, String carId, CarStatus carStatus) throws Exception{
        User user = getUserById(userId);
        if(haveUserCarRent(user)) {
            Car carToRent = getCarById(carId);
            if (checkCarStatusMatchVariable(carToRent)) {
                updateCarStatus(carId, carToRent);
                updateUserCarRentStatus(carId, carStatus, user);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    private void updateUserCarRentStatus(String carId, CarStatus carStatus, User user) throws Exception {
        if (carStatus.equals(CarStatus.AVAILABLE)) {
            user.setUserCarId(carId);
        } else {
            user.setUserCarId("");
        }
        usersService.updateUser(new UserDto(user.getUserLogin(), user.getUserPassword(), user.getUserName()
                , user.getUserLastName(), user.getUserEMail(), user.getUserAddress(), user.getUserCarId(), user.getRole(), user.getStatus()), user.getUserId());
    }

    private void updateCarStatus(String carId, Car carToRent) throws Exception {
        carsService.updateCar(new CarDto(carToRent.getMark(), carToRent.getModel(), carToRent.getBodyType(),
                carToRent.getYearOfProduction(), carToRent.getColour(), carToRent.getRun(),
                carToRent.getCarStatus(), carToRent.getDayPrice()), carId);
    }

    private boolean checkCarStatusMatchVariable(Car carToRent) {
        if(carToRent.getCarStatus() == CarStatus.AVAILABLE){
            carToRent.setCarStatus(CarStatus.RENTED);
            return true;
        }
        return false;
    }

    private Car getCarById(String carId) throws CarException {
        Car carToRent = Optional.of(carsService.getCarById(carId)).orElseThrow(() -> new CarException("No car with given id"));
        return carToRent;
    }

    private boolean haveUserCarRent(User user) {
        if(user.getUserCarId() != null) {
            new CarException("User has already rented car");
            return false;
        }
        return true;
    }

    private User getUserById(String userId) {
        User user = usersService.getUserById(userId).orElseThrow(() -> new UserException("No client with given ID"));
        return user;
    }

    private boolean isInRange(BigDecimal price, BigDecimal from, BigDecimal to) {
        return price.compareTo(from)  > 0  && price.compareTo(to) < 0;
    }

    private Function<String, List<Car>> getAllCarsByMark(String mark) {
        return m -> carsService.getAllCars().stream()
                .filter(car -> car.getMark().equals(mark))
                .collect(Collectors.toList());
    }
}
