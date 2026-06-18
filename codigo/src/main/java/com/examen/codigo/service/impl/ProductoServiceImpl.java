package com.examen.codigo.service.impl;

import com.examen.codigo.dto.request.ProductoRequest;
import com.examen.codigo.dto.response.ProductoResponse;
import com.examen.codigo.entity.Producto;
import com.examen.codigo.mapper.ProductoMapper;
import com.examen.codigo.repository.ProductoRepository;
import com.examen.codigo.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {


    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = productoMapper.toEntity(request);
        Producto productoGuardado = productoRepository.save(producto);
        return productoMapper.toResponse(productoGuardado);
    }

    @Override
    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
