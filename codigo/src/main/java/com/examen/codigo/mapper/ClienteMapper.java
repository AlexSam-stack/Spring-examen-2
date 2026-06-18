package com.examen.codigo.mapper;


import com.examen.codigo.dto.request.ClienteRequest;
import com.examen.codigo.dto.response.ClienteResponse;
import com.examen.codigo.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest request) {
        if (request == null) {
            return null;
        }
        return Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dni(request.getDni())
                .correo(request.getCorreo())
                .build();
    }

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .correo(cliente.getCorreo())
                .fechaRegistro(cliente.getFechaRegistro())
                .build();
    }
}
