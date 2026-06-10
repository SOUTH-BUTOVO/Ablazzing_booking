package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apartment")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApartmentRsDto create(@Valid @RequestBody ApartmentRqDto dto) {
        return apartmentService.save(dto);
    }
}
