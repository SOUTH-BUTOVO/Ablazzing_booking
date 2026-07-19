package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.service.AdvertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Объявления", description = "Операции с объявлениями")
public class AdvertController {
    private final AdvertService advertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание объявления",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания объявления"))
    public AdvertRsDto create(@Valid @RequestBody AdvertRqDto dto) {
        return advertService.save(dto);
    }

    @GetMapping
    @PageableAsQueryParam
    @Operation(summary = "Получить все объявления в городе (по умолчанию, город Москва)")
    public Page<AdvertRsDto> getAllByCity(
            @Parameter(description = "Получение всех объявлений по городу")
            @RequestParam(defaultValue = "Moscow") String city,
            @Parameter(description = "Параметры пагинации по умолчанию: size = 10, sort = price, direction = desc")
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return advertService.findAllByCity(city, pageable);
    }
}
