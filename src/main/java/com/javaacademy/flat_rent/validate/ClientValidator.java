package com.javaacademy.flat_rent.validate;

import com.javaacademy.flat_rent.dto.ClientRqDto;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {

    public void validateClientRequest(ClientRqDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Client must be required");
        }
        if (dto.id() == null && (dto.name() == null || dto.email() == null)) {
            throw new IllegalArgumentException("Client requires id or name + email");
        }
    }
}
