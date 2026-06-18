package com.examen.codigo.mapper;


import com.examen.codigo.dto.request.ProductoRequest;
import com.examen.codigo.dto.response.ProductoResponse;
import com.examen.codigo.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        if (request == null) {
            return null;
        }
        return Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .estado(true)
                .build();
    }

    public ProductoResponse toResponse(Producto producto) {
        if (producto == null) {
            return null;
        }
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .estado(producto.getEstado())
                .build();
    }
}
