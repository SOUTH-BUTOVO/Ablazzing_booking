package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ClientRqDto;
import com.javaacademy.flat_rent.dto.ClientRsDto;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.ClientMapper;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import com.javaacademy.flat_rent.validate.ClientValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper clientMapper;
    private final ClientValidator clientValidator;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ClientRsDto save(ClientRqDto dto) {
        clientValidator.validateClientRequest(dto);

        Client client = clientRepository.save(clientMapper.toEntity(dto));

        System.out.println(client);
        ClientRsDto clientRsDto = clientMapper.toDto(client);
        System.out.println(clientRsDto);
        return clientRsDto;
    }

    @Transactional
    public void removeById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client id: %s, not found".formatted(id));
        }
        bookingRepository.deleteAllByClientId(id);
        clientRepository.deleteById(id);
    }

    public Client resolveOrCreateClient(ClientRqDto dto) {
        clientValidator.validateClientRequest(dto);
        if (dto.id() != null) {
            return clientRepository.findById(dto.id()).orElseThrow(() ->
                    new EntityNotFoundException("Client id: %s, not found".formatted(dto.id())));
        }
        return clientRepository.save(clientMapper.toEntity(dto));
    }
}
