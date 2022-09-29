package com.example.carrental.controller;

import com.example.carrental.domain.RentalBranch.RentalBranch;
import com.example.carrental.domainDto.RentalBranchDto;
import com.example.carrental.service.RentalBranchService.RentalBranchService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rentalBranch")
public class RentalBranchController {

    private final RentalBranchService rentalBranchService;

    public RentalBranchController(RentalBranchService rentalBranchService) {
        this.rentalBranchService = rentalBranchService;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalBranch createRentalBranch(@RequestBody RentalBranchDto rentalBranchDto) {
        return rentalBranchService.createRentalBranch(rentalBranchDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RentalBranch> getAllRentalBranch() {
        return rentalBranchService.getAllRentalBranch();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RentalBranch getRentalBranchById(@PathVariable Long id) throws Exception {
        return rentalBranchService.getRentalBranchById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRentalBranch(@PathVariable Long id) throws Exception {
        rentalBranchService.deleteRentalBranch(id);
    }


}
