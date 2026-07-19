package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.service.ApartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apartment")
@RequiredArgsConstructor
@Tag(name = "Апартаменты", description = "Операции с апартаментами")
public class ApartmentController {
    private final ApartmentService apartmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создание апартаментов",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания апартаментов"))
    public ApartmentRsDto create(@Valid @RequestBody ApartmentRqDto dto) {
        return apartmentService.save(dto);
    }
}
