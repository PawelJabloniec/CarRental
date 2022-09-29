package com.example.carrental.domainDto;

import com.example.carrental.domain.Car.Car;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RentalBranchDto {

    private List<Car> cars = new ArrayList<>();

    private String city;

    private String street;

    private String postCode;
}
