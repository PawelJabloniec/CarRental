package com.example.carrental.domainDto;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.User.CarRentalUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CarRentalOfficeDto {

    private CarRentalUser user;

    private Car car;

    private LocalDateTime localDateTimeOfRent;

    private LocalDateTime getLocalDateTimeOfReturn;
}