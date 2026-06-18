package com.examen.codigo.service;

import com.examen.codigo.dto.request.ProductoRequest;
import com.examen.codigo.dto.response.ProductoResponse;


import java.util.List;

public interface ProductoService {
    ProductoResponse crear(ProductoRequest request);

    List<ProductoResponse> listar();
}
