package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.BookingRsDto;
import com.javaacademy.flat_rent.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@Tag(name = "Бронирования", description = "Операции с бронированиями")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создание бронирования",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания бронирования"))
    @ApiResponse(responseCode = "409", description = "Дата бронирования пересекается с действующей бронью")
    public void create(@Valid @RequestBody BookingRqDto dto) {
        bookingService.save(dto);
    }

    @GetMapping("/by-email")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получение бронирования по email")
    public Page<BookingRsDto> getAllBookingsByEmail(
            @Parameter(description = "email брони")
            @RequestParam @Email String email,
            @Parameter(
                    description = "Параметры пагинации по умолчанию: size = 20, sort = startDate, direction = desc")
            @PageableDefault(size = 20,
                    sort = "startDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
        return bookingService.getAllBookingsByEmail(email, pageable);
    }
}
