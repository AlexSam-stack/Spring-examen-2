package com.examen.codigo.service.impl;


import com.examen.codigo.dto.request.ClienteRequest;
import com.examen.codigo.dto.response.ClienteResponse;
import com.examen.codigo.entity.Cliente;
import com.examen.codigo.exception.ClienteNotFoundException;
import com.examen.codigo.mapper.ClienteMapper;
import com.examen.codigo.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void crearCliente_cuandoDatosSonValidos_retornaClienteCreado() {
        // Arrange
        ClienteRequest request = ClienteRequest.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .correo("juan.perez@gmail.com")
                .build();

        Cliente clienteSinId = Cliente.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .correo("juan.perez@gmail.com")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .correo("juan.perez@gmail.com")
                .build();

        ClienteResponse responseEsperado = ClienteResponse.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .correo("juan.perez@gmail.com")
                .build();

        when(clienteMapper.toEntity(request)).thenReturn(clienteSinId);
        when(clienteRepository.save(clienteSinId)).thenReturn(clienteGuardado);
        when(clienteMapper.toResponse(clienteGuardado)).thenReturn(responseEsperado);

        // Act
        ClienteResponse resultado = clienteService.crear(request);

        // Assert
        assertEquals(responseEsperado, resultado);
        verify(clienteRepository).save(clienteSinId);
    }

    @Test
    void buscarClientePorId_cuandoExiste_retornaCliente() {
        // Arrange
        Cliente cliente = Cliente.builder().id(1L).nombre("Juan").apellido("Pérez").build();
        ClienteResponse responseEsperado = ClienteResponse.builder().id(1L).nombre("Juan").apellido("Pérez").build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(cliente)).thenReturn(responseEsperado);

        // Act
        ClienteResponse resultado = clienteService.buscarPorId(1L);

        // Assert
        assertEquals(responseEsperado, resultado);
    }

    @Test
    void buscarClientePorId_cuandoNoExiste_lanzaClienteNotFoundException() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarPorId(99L));
        verify(clienteMapper, never()).toResponse(any());
    }
}
