package com.example.carrental.service.RentalBranchService;

import com.example.carrental.domain.Car.Car;
import com.example.carrental.domain.RentalBranch.RentalBranch;
import com.example.carrental.domainDto.RentalBranchDto;
import com.example.carrental.repository.CarsRepository;
import com.example.carrental.repository.RentalBranchRepository;
import com.example.carrental.domain.RentalBranch.RentalBranchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RentalBranchServiceImpl implements RentalBranchService{

    private final RentalBranchRepository rentalBranchRepository;
    private final CarsRepository carsRepository;

    public RentalBranchServiceImpl(RentalBranchRepository rentalBranchRepository, CarsRepository carsRepository) {
        this.rentalBranchRepository = rentalBranchRepository;
        this.carsRepository = carsRepository;
    }

    @Override
    public RentalBranch getRentalBranchById(Long id) throws Exception{
        return validId(id);
    }

    @Override
    public RentalBranch createRentalBranch(RentalBranchDto rentalBranchDto) {
        RentalBranch newRentalBranch = RentalBranch.builder()
                .cars(new ArrayList<>())
                .city(rentalBranchDto.getCity())
                .street(rentalBranchDto.getStreet())
                .postCode(rentalBranchDto.getPostCode())
                .build();

        return rentalBranchRepository.save(newRentalBranch);
    }

    @Override
    public List<RentalBranch> getAllRentalBranch() {
        return rentalBranchRepository.findAll();
    }

    @Override
    public List<Car> getAllCarsFromBranch(Long id) {
        return rentalBranchRepository.findRentalBranchById(id).getCars();
    }

    @Override
    public void deleteRentalBranch(Long id) throws Exception{
        validId(id);
        rentalBranchRepository.deleteById(id);
    }

    private RentalBranch validId(Long id) throws Exception{
        return Optional.of(rentalBranchRepository.findRentalBranchById(id)).orElseThrow(() -> new RentalBranchException("No Branch with given ID"));
    }
}
