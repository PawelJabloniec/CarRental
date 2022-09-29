package com.example.carrental.service.CarService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.Car.CarStatus;
import com.example.carrental.domainDto.CarDto;
import com.example.carrental.repository.CarsRepository;
import com.example.carrental.repository.RentalBranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarsServiceImplTest {

    @Mock
    private CarsRepository carsRepository;
    @Mock
    private RentalBranchRepository rentalBranchRepository;

    @Test
    public void shouldUpdateACar() throws Exception {

        CarsServiceImpl carsService = new CarsServiceImpl(carsRepository, rentalBranchRepository);
        //GIVEN
        Long carId = 1L;
        Mockito.when(carsRepository.findCarById(carId)).thenReturn(new Car(carId, 1L, "Mark", "Model", "bodyType", 1990,
                "Red", 3, CarStatus.AVAILABLE, BigDecimal.valueOf(15)));
        CarDto carDto = new CarDto(carId, "Mark", "Model", "bodyType", 1990,
                "Green", 3, CarStatus.AVAILABLE, BigDecimal.valueOf(15));
        //WHEN
        carsService.updateCar(carDto,carId);
        //THEN
        verify(carsRepository,times(1)).save(Mockito.any());
    }
    @Test
    public void shouldDeleteACar() throws Exception {

        CarsServiceImpl carsService = new CarsServiceImpl(carsRepository, rentalBranchRepository);
        //GIVEN
        Long carId = 1L;
        Mockito.when(carsRepository.findCarById(carId)).thenReturn(new Car(carId, 1L, "Mark", "Model", "bodyType", 1990,
                "Red", 3, CarStatus.AVAILABLE, BigDecimal.valueOf(15)));
        //WHEN
        carsService.deleteCarById(carId);
        //THEN
        verify(carsRepository,times(1)).delete(Mockito.any());
    }
}