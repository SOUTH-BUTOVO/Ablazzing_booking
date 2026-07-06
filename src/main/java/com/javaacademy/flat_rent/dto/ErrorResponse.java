package com.javaacademy.flat_rent.dto;

import java.time.LocalDateTime;

// 1. Создаю record DTO для ответа об ошибке, чтобы красиво отдавать ошибку, наружу обычно exception не показывают.
public record ErrorResponse(LocalDateTime timestamp, int status, String message) {}
