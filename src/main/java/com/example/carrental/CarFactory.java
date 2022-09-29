package com.example.carrental;

import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domainDto.CarDto;
import com.example.carrental.domainDto.RentalBranchDto;
import com.example.carrental.domainDto.UserDto;
import com.example.carrental.service.CarService.CarsService;
import com.example.carrental.service.RentalBranchService.RentalBranchService;
import com.example.carrental.service.UserService.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CarFactory implements CommandLineRunner {

    private final CarsService carsService;
    private final UsersService usersService;
    private final RentalBranchService rentalBranchService;

    public CarFactory(CarsService carsService, UsersService usersService, RentalBranchService rentalBranchService) {
        this.carsService = carsService;
        this.usersService = usersService;
        this.rentalBranchService = rentalBranchService;
    }


    @Override
    public void run(String... args) throws Exception {
        providedExampleData();
    }

    private void providedExampleData() {
        createRentalBranches();
        createExampleCars();
        createExampleUsers();
    }

    private void createRentalBranches() {
        List<RentalBranchDto> listRentalBranch = new ArrayList<>(Arrays.asList(
                new RentalBranchDto(new ArrayList<>(),"Zakopane","Aleje 3-go Maja","34-500"),
                new RentalBranchDto(new ArrayList<>(),"Gdansk", "Aleje Wojska Polskiego", "80-366"),
                new RentalBranchDto(new ArrayList<>(),"Lodz", "Klonowa","90-003")
        ));
        for(int i =0; i<listRentalBranch.size(); i++) {
            rentalBranchService.createRentalBranch(listRentalBranch.get(i));
        }
    }

    private void createExampleUsers() {
        List<UserDto> listUserExample = new ArrayList<>(Arrays.asList(
                new UserDto("Piotr","Piotr123","Piotr","Larkowski",
                        "p.larkowski90@gmail.com","Zakopane",null,
                        "USER","false"),
                new UserDto("Pawel","Pawel123","Pawel","Jabloniec",
                        "pawjab90@gmail.com","Gdansk",null,
                        "USER","false"),
                new UserDto("Przemek","Przemek123","Przemek","Gesiewski",
                        "p.gesiewski@gmail.com","Lodz",null,
                        "USER","false")
        ));
        for(int i =0; i<listUserExample.size(); i++) {
            usersService.createUser(listUserExample.get(i));
        }
    }

    private void createExampleCars() {
        List<CarDto> listCarExample = new ArrayList<>(Arrays.asList(
                new CarDto(1L, "volvo", "XC90", "combi", 1990, "red", 19999, CarStatus.IN_REPAIR, BigDecimal.valueOf(250000L)),
                new CarDto(1L, "vw", "passat", "combi", 1995, "green", 15999, CarStatus.AVAILABLE, BigDecimal.valueOf(150000L)),
                new CarDto(1L, "ford", "mondeo", "combi", 2000, "blue", 100000, CarStatus.AVAILABLE, BigDecimal.valueOf(300000L)),
                new CarDto(1L,"fiat", "panda", "sedan", 2005, "red", 100000, CarStatus.BROKEN, BigDecimal.valueOf(150000L)),
                new CarDto(1L,"bmw", "3", "sedan", 1958, "black", 150000, CarStatus.AVAILABLE, BigDecimal.valueOf(350000L)),

                new CarDto(2L, "volvo", "XC90", "combi", 1990, "red", 19999, CarStatus.IN_REPAIR, BigDecimal.valueOf(250000L)),
                new CarDto(2L, "vw", "passat", "combi", 1995, "green", 15999, CarStatus.AVAILABLE, BigDecimal.valueOf(150000L)),
                new CarDto(2L, "ford", "mondeo", "combi", 2000, "blue", 100000, CarStatus.AVAILABLE, BigDecimal.valueOf(300000L)),
                new CarDto(2L,"fiat", "panda", "sedan", 2005, "red", 100000, CarStatus.BROKEN, BigDecimal.valueOf(150000L)),
                new CarDto(2L,"bmw", "3", "sedan", 1958, "black", 150000, CarStatus.AVAILABLE, BigDecimal.valueOf(350000L)),

                new CarDto(3L, "volvo", "XC90", "combi", 1990, "red", 19999, CarStatus.IN_REPAIR, BigDecimal.valueOf(250000L)),
                new CarDto(3L, "vw", "passat", "combi", 1995, "green", 15999, CarStatus.AVAILABLE, BigDecimal.valueOf(150000L)),
                new CarDto(3L, "ford", "mondeo", "combi", 2000, "blue", 100000, CarStatus.AVAILABLE, BigDecimal.valueOf(300000L)),
                new CarDto(3L,"fiat", "panda", "sedan", 2005, "red", 100000, CarStatus.BROKEN, BigDecimal.valueOf(150000L)),
                new CarDto(3L,"bmw", "3", "sedan", 1958, "black", 150000, CarStatus.AVAILABLE, BigDecimal.valueOf(350000L))
        ));
        for(int i =0; i<listCarExample.size(); i++) {
            carsService.createCar(listCarExample.get(i), listCarExample.get(i).getRentalBranchId());
        }
    }
}
