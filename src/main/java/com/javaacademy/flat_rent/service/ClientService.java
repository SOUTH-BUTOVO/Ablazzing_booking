package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ClientRqDto;
import com.javaacademy.flat_rent.dto.ClientRsDto;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.ClientMapper;
import com.javaacademy.flat_rent.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    public ClientRsDto save(ClientRqDto dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        System.out.println(saved);
        ClientRsDto clientRsDto = clientMapper.toDto(saved);
        System.out.println(clientRsDto);
        return clientRsDto;
    }
}
