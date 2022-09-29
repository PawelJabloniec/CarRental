package com.example.carrental.controller;

import com.example.carrental.domain.RentalOffice.CarRentalOffice;
import com.example.carrental.service.RentalOfficeService.CarRentalOfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/carRentalOffice")
public class CarRentalOfficeContoller {

    private final CarRentalOfficeService carRentalOfficeService;

    public CarRentalOfficeContoller(CarRentalOfficeService carRentalOfficeService) {
        this.carRentalOfficeService = carRentalOfficeService;
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarRentalOffice> getAllCarRentalOffice(){
        return carRentalOfficeService.getAllCarRentalOffices();
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarRentalOffice getCarRentalOfficeById(@PathVariable @Valid Long id){
        return carRentalOfficeService.getCarRentalOfficeById(id);
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    @GetMapping(path = "/dateTime/{localDateTime}")
    @ResponseStatus(HttpStatus.OK)
    public List<CarRentalOffice> getCarRentalOfficeByDateTime(@PathVariable LocalDateTime localDateTime){
        return carRentalOfficeService.findCarRentalOfficeByLocalDateTimeOfRent(localDateTime);
    }
    @PutMapping(path = "/rentCar")
    public void userRentCar(@RequestParam(name="userId") Long userId, @RequestParam(name="carId") Long carId) throws Exception{
        carRentalOfficeService.rentACar(userId, carId);
    }
    @PutMapping(path ="/returnCar/{carRentalOfficeId}")
    public void userReturnCar(@PathVariable Long carRentalOfficeId) throws Exception{
        carRentalOfficeService.returnACar(carRentalOfficeId);
    }
}
