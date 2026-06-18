package com.examen.codigo.dto.response;

import com.examen.codigo.entity.Estado;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private String cliente;
    private LocalDateTime fechaPedido;
    private Estado estado;
    private BigDecimal total;
    private List<DetallePedidoResponse> detalles;
}