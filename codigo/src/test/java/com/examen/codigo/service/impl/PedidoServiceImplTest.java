package com.examen.codigo.service.impl;


import com.examen.codigo.dto.request.ItemPedidoRequest;
import com.examen.codigo.dto.request.PedidoRequest;
import com.examen.codigo.dto.response.PedidoResponse;
import com.examen.codigo.entity.*;
import com.examen.codigo.exception.ClienteNotFoundException;
import com.examen.codigo.exception.PedidoNotFoundException;
import com.examen.codigo.exception.ProductoNotFoundException;
import com.examen.codigo.exception.StockInsuficienteException;
import com.examen.codigo.mapper.PedidoMapper;
import com.examen.codigo.repository.ClienteRepository;
import com.examen.codigo.repository.PedidoRepository;
import com.examen.codigo.repository.ProductoRepository;
import com.examen.codigo.validator.StockValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private StockValidator stockValidator;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Cliente cliente;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .correo("juan.perez@gmail.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .descripcion("Teclado RGB")
                .precio(new BigDecimal("10.00"))
                .stock(5)
                .estado(true)
                .build();
    }

    @Test
    void crearPedido_cuandoDatosSonValidos_retornaPedidoCreado() {
        // Arrange
        ItemPedidoRequest item = ItemPedidoRequest.builder()
                .productoId(1L)
                .cantidad(2)
                .build();
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .items(List.of(item))
                .build();

        DetallePedido detalle = DetallePedido.builder()
                .productoId(1L)
                .nombreProducto("Teclado mecánico")
                .cantidad(2)
                .precioUnitario(new BigDecimal("10.00"))
                .subtotal(new BigDecimal("20.00"))
                .build();

        PedidoResponse responseEsperado = PedidoResponse.builder()
                .id(100L)
                .clienteId(1L)
                .cliente("Juan Pérez")
                .estado(Estado.CREADO)
                .total(new BigDecimal("20.00"))
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoMapper.toDetalleEntity(item, producto)).thenReturn(detalle);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocacion -> {
            Pedido pedidoGuardado = invocacion.getArgument(0);
            pedidoGuardado.setId(100L);
            return pedidoGuardado;
        });
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(responseEsperado);

        // Act
        PedidoResponse resultado = pedidoService.crear(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(responseEsperado, resultado);
        assertEquals(3, producto.getStock(), "El stock debe descontarse en 2 unidades (5 - 2 = 3)");

        ArgumentCaptor<Pedido> pedidoCapturado = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(pedidoCapturado.capture());
        Pedido pedidoGuardado = pedidoCapturado.getValue();
        assertEquals(Estado.CREADO, pedidoGuardado.getEstado());
        assertEquals(new BigDecimal("20.00"), pedidoGuardado.getTotal());
        assertEquals(1, pedidoGuardado.getDetalles().size());

        verify(productoRepository).save(producto);
        verify(pedidoMapper).toResponse(pedidoGuardado);
    }

    @Test
    void crearPedido_cuandoStockEsInsuficiente_lanzaStockInsuficienteException() {
        // Arrange
        producto.setStock(1);
        ItemPedidoRequest item = ItemPedidoRequest.builder()
                .productoId(1L)
                .cantidad(5)
                .build();
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .items(List.of(item))
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        doThrow(new StockInsuficienteException("Stock insuficiente"))
                .when(stockValidator).validar(producto, 5);

        // Act & Assert
        assertThrows(StockInsuficienteException.class, () -> pedidoService.crear(request));

        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(productoRepository, never()).save(any(Producto.class));
        assertEquals(1, producto.getStock(), "El stock no debe descontarse si la validación falla");
    }

    @Test
    void crearPedido_cuandoClienteNoExiste_lanzaClienteNotFoundException() {
        // Arrange
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(99L)
                .items(List.of(ItemPedidoRequest.builder().productoId(1L).cantidad(1).build()))
                .build();

        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> pedidoService.crear(request));

        verify(productoRepository, never()).findById(any());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void crearPedido_cuandoProductoNoExiste_lanzaProductoNotFoundException() {
        // Arrange
        ItemPedidoRequest item = ItemPedidoRequest.builder().productoId(99L).cantidad(1).build();
        PedidoRequest request = PedidoRequest.builder()
                .clienteId(1L)
                .items(List.of(item))
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductoNotFoundException.class, () -> pedidoService.crear(request));

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void buscarPedido_cuandoNoExiste_lanzaPedidoNotFoundException() {
        // Arrange
        when(pedidoRepository.findById(404L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PedidoNotFoundException.class, () -> pedidoService.buscarPorId(404L));
    }

    @Test
    void buscarPedido_cuandoExiste_retornaPedido() {
        // Arrange
        Pedido pedido = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .estado(Estado.CREADO)
                .total(BigDecimal.TEN)
                .build();
        PedidoResponse responseEsperado = PedidoResponse.builder()
                .id(1L)
                .clienteId(1L)
                .cliente("Juan Pérez")
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(responseEsperado);

        // Act
        PedidoResponse resultado = pedidoService.buscarPorId(1L);

        // Assert
        assertEquals(responseEsperado, resultado);
    }

    @Test
    void listarPedidosPorCliente_cuandoClienteNoExiste_lanzaClienteNotFoundException() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> pedidoService.listarPorCliente(99L));

        verify(pedidoRepository, never()).findByClienteId(any());
    }
}
