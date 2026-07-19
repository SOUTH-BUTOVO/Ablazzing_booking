package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "Операции с клиентами")
public class ClientController {
    private final ClientService clientService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление клиента по id")
    public void delete(@Parameter(description = "id клиента") @PathVariable Long id) {
        clientService.removeById(id);
    }
}
