package com.examen.codigo.service;

import com.examen.codigo.dto.request.PedidoRequest;
import com.examen.codigo.dto.response.PedidoResponse;


import java.util.List;

public interface PedidoService {
    PedidoResponse crear(PedidoRequest request);

    PedidoResponse buscarPorId(Long id);

    List<PedidoResponse> listarPorCliente(Long clienteId);
}
