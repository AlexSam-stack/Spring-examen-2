package com.examen.codigo.mapper;

import com.examen.codigo.dto.request.ItemPedidoRequest;
import com.examen.codigo.dto.response.DetallePedidoResponse;
import com.examen.codigo.dto.response.PedidoResponse;
import com.examen.codigo.entity.DetallePedido;
import com.examen.codigo.entity.Pedido;
import com.examen.codigo.entity.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    /**
     * Construye el detalle a partir del item solicitado y el producto ya validado.
     * El cálculo de stock y del total general del pedido sigue siendo responsabilidad
     * del PedidoService, aquí solo se arma el detalle con su subtotal.
     */
    public DetallePedido toDetalleEntity(ItemPedidoRequest item, Producto producto) {
        if (item == null || producto == null) {
            return null;
        }
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

        return DetallePedido.builder()
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .cantidad(item.getCantidad())
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .build();
    }

    public DetallePedidoResponse toDetalleResponse(DetallePedido detalle) {
        if (detalle == null) {
            return null;
        }
        return DetallePedidoResponse.builder()
                .productoId(detalle.getProductoId())
                .nombreProducto(detalle.getNombreProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubtotal())
                .build();
    }

    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        List<DetallePedidoResponse> detalles = pedido.getDetalles() == null
                ? Collections.emptyList()
                : pedido.getDetalles().stream()
                    .map(this::toDetalleResponse)
                    .collect(Collectors.toList());

        Long clienteId = pedido.getCliente() != null ? pedido.getCliente().getId() : null;
        String nombreCliente = pedido.getCliente() != null
                ? pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido()
                : null;

        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(clienteId)
                .cliente(nombreCliente)
                .fechaPedido(pedido.getFechaPedido())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .detalles(detalles)
                .build();
    }
}
