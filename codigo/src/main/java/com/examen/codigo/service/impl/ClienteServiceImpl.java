package com.examen.codigo.service.impl;


import com.examen.codigo.dto.request.ClienteRequest;
import com.examen.codigo.dto.response.ClienteResponse;
import com.examen.codigo.entity.Cliente;
import com.examen.codigo.exception.ClienteNotFoundException;
import com.examen.codigo.mapper.ClienteMapper;
import com.examen.codigo.repository.ClienteRepository;
import com.examen.codigo.service.ClienteService;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toResponse(clienteGuardado);
    }

    @Override
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("No se encontró el cliente con id " + id));
        return clienteMapper.toResponse(cliente);
    }
}