package com.javaacademy.flat_rent.exception;

import com.javaacademy.flat_rent.dto.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Это механизм глобальной обработки ошибок в Spring.
// @RestControllerAdvice: Это аннотация-маркер. Она говорит Spring: «Этот класс — общий помощник для всех
// контроллеров. Если в любом контроллере случится ошибка, сначала проверь методы внутри этого класса».

// 2. Создаю обработчик исключений, который будет перехватывать EntityExistsException и превращать его в 409.
@RestControllerAdvice
public class GlobalExceptionHandler {
    // В проде никогда не позволяют стеку исключения (stack trace) уходить клиенту. Это утечка информации о
    // структуре проекта. Все исключения ловятся в одном месте и конвертируются в понятный JSON-ответ.

    // @ExceptionHandler: Это метод внутри класса с @RestControllerAdvice. У него есть параметр — тип исключения,
    // которое он умеет ловить (например, EntityExistsException.class).

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException ex) {
        // Создаю красивый объект ответа
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage()) // Берем сообщение из исключения ("Date is occupied")
                ;

        // Возвращаю объект со статусом 409 (CONFLICT)
        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(error);
    }
}
