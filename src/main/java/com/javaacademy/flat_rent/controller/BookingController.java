package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.BookingRsDto;
import com.javaacademy.flat_rent.service.BookingService;
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
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody BookingRqDto dto) {
        bookingService.save(dto);
    }

    @GetMapping("/by-email")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    public Page<BookingRsDto> getAllBookingsByEmail(@RequestParam @Email String email,
                                                    @PageableDefault(size = 20,
                                                                     sort = "startDate",
                                                                     direction = Sort.Direction.DESC)
                                                    Pageable pageable) {
        return bookingService.getAllBookingsByEmail(email, pageable);
    }
}
