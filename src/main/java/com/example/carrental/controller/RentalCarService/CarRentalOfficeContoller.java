package com.example.carrental.controller.RentalCarService;

import com.example.carrental.repository.CarsRentalOfficeRepository;
import com.example.carrental.service.CarService.CarsService;
import com.example.carrental.service.RentalOfficeService.CarRentalOfficeService;
import com.example.carrental.service.UserService.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path="/carRentalOffice")
public class CarRentalOfficeContoller {

    private final UsersService usersService;

    private final CarsService carsService;

    private final CarRentalOfficeService carRentalOfficeService;

    public CarRentalOfficeContoller(UsersService usersService, CarsService carsService, CarsRentalOfficeRepository carsRentalOfficeRepository, CarRentalOfficeService carRentalOfficeService) {
        this.usersService = usersService;
        this.carsService = carsService;
        this.carRentalOfficeService = carRentalOfficeService;
    }

    @PutMapping(path = "/rentCar")
    public void userRentCar(@RequestParam String userId, @RequestParam String carId) throws Exception{
        carRentalOfficeService.rentACar(userId, carId);
    }

    @PutMapping(path ="/returnCar")
    public void userReturnCar(@RequestParam String userId, @RequestParam String carId) throws Exception{
        carRentalOfficeService.returnACar(userId,carId);
    }
}
