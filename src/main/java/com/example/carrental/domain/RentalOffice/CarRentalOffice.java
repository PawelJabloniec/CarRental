package com.example.carrental.domain.RentalOffice;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CarRentalOffice {

    @Id
    @Column(nullable = false)
    private String id;

    private String userId;

    private String carId;

    private LocalDateTime localDateTimeOfRent;

    private LocalDateTime localDateTimeOfRetun;

}
