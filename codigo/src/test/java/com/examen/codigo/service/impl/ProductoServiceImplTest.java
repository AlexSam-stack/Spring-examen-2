package com.examen.codigo.service.impl;


import com.examen.codigo.dto.request.ProductoRequest;
import com.examen.codigo.dto.response.ProductoResponse;
import com.examen.codigo.entity.Producto;
import com.examen.codigo.mapper.ProductoMapper;
import com.examen.codigo.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void crearProducto_cuandoDatosSonValidos_retornaProductoCreado() {
        // Arrange
        ProductoRequest request = ProductoRequest.builder()
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("150.00"))
                .stock(20)
                .build();

        Producto productoSinId = Producto.builder()
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("150.00"))
                .stock(20)
                .estado(true)
                .build();

        Producto productoGuardado = Producto.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("150.00"))
                .stock(20)
                .estado(true)
                .build();

        ProductoResponse responseEsperado = ProductoResponse.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("150.00"))
                .stock(20)
                .estado(true)
                .build();

        when(productoMapper.toEntity(request)).thenReturn(productoSinId);
        when(productoRepository.save(productoSinId)).thenReturn(productoGuardado);
        when(productoMapper.toResponse(productoGuardado)).thenReturn(responseEsperado);

        // Act
        ProductoResponse resultado = productoService.crear(request);

        // Assert
        assertEquals(responseEsperado, resultado);
        verify(productoRepository).save(productoSinId);
    }

    @Test
    void listarProductos_cuandoHayProductos_retornaListaDeProductos() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Mouse")
                .precio(new BigDecimal("50.00"))
                .stock(10)
                .estado(true)
                .build();
        ProductoResponse responseEsperado = ProductoResponse.builder()
                .id(1L)
                .nombre("Mouse")
                .precio(new BigDecimal("50.00"))
                .stock(10)
                .estado(true)
                .build();

        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoMapper.toResponse(producto)).thenReturn(responseEsperado);

        // Act
        List<ProductoResponse> resultado = productoService.listar();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(responseEsperado, resultado.get(0));
    }

    @Test
    void listarProductos_cuandoNoHayProductos_retornaListaVacia() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductoResponse> resultado = productoService.listar();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(productoMapper, never()).toResponse(any());
    }
}
