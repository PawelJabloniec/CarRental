package com.example.carrental.domain.RentalBranch;

import com.example.carrental.domain.Car.Car;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RentalBranch {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "rentalBranchId", cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<>();

    private String city;

    private String street;

    private String postCode;



}
