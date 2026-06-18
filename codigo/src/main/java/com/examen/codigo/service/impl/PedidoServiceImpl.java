package com.examen.codigo.service.impl;

import com.examen.codigo.dto.request.ItemPedidoRequest;
import com.examen.codigo.dto.request.PedidoRequest;
import com.examen.codigo.dto.response.PedidoResponse;
import com.examen.codigo.entity.*;
import com.examen.codigo.exception.ClienteNotFoundException;
import com.examen.codigo.exception.PedidoNotFoundException;
import com.examen.codigo.exception.ProductoNotFoundException;
import com.examen.codigo.mapper.PedidoMapper;
import com.examen.codigo.repository.ClienteRepository;
import com.examen.codigo.repository.PedidoRepository;
import com.examen.codigo.repository.ProductoRepository;
import com.examen.codigo.service.PedidoService;
import com.examen.codigo.validator.StockValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {


    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoMapper pedidoMapper;
    private final StockValidator stockValidator;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ProductoRepository productoRepository,
                             ClienteRepository clienteRepository,
                             PedidoMapper pedidoMapper,
                             StockValidator stockValidator) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoMapper = pedidoMapper;
        this.stockValidator = stockValidator;
    }

    @Override
    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        // 1. El cliente debe existir
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(
                        "No se encontró el cliente con id " + request.getClienteId()));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estado(Estado.CREADO)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest item : request.getItems()) {
            // 2. Cada producto enviado debe existir
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ProductoNotFoundException(
                            "No se encontró el producto con id " + item.getProductoId()));

            // 4. El producto debe tener stock suficiente (la cantidad > 0 ya la valida @Positive en el DTO)
            stockValidator.validar(producto, item.getCantidad());

            // 5. Calcular el subtotal de cada detalle
            DetallePedido detalle = pedidoMapper.toDetalleEntity(item, producto);
            pedido.agregarDetalle(detalle);
            total = total.add(detalle.getSubtotal());

            // 7. Descontar el stock del producto
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        // 6. Calcular el total del pedido
        pedido.setTotal(total);
        // 8. El pedido queda con estado CREADO (ya seteado en el builder)

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(pedidoGuardado);
    }


    @Override
    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("No se encontró el pedido con id " + id));
        return pedidoMapper.toResponse(pedido);
    }

    @Override
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(
                        "No se encontró el cliente con id " + clienteId));

        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
