package com.example.carrental.service.RentalBranchService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.RentalBranch.RentalBranch;
import com.example.carrental.domainDto.RentalBranchDto;

import java.util.List;

public interface RentalBranchService {

    RentalBranch getRentalBranchById(Long id) throws Exception;

    RentalBranch createRentalBranch(RentalBranchDto rentalBranchDto);

    List<RentalBranch> getAllRentalBranch();

    List<Car> getAllCarsFromBranch(Long id) throws Exception;

    void deleteRentalBranch(Long id) throws Exception;
}
