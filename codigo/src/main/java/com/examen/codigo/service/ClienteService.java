package com.examen.codigo.service;

import com.examen.codigo.dto.request.ClienteRequest;
import com.examen.codigo.dto.response.ClienteResponse;
import com.examen.codigo.entity.Cliente;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);
    ClienteResponse buscarPorId(Long id);
}
