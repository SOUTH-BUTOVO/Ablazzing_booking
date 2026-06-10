package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.service.AdvertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advert")
@RequiredArgsConstructor
public class AdvertController {
    private final AdvertService advertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertRsDto create(@Valid @RequestBody AdvertRqDto dto) {
        return advertService.save(dto);
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<AdvertRsDto> getAllByCity(
            @RequestParam(defaultValue = "Moscow") String city,
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return advertService.findAllByCity(city, pageable);
    }
}
