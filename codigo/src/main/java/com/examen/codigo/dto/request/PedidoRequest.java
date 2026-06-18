package com.examen.codigo.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo requerido para registrar un nuevo pedido")
public class PedidoRequest {
    @Schema(description = "ID del cliente que realiza la compra", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    @JsonProperty("detalles")
    @Schema(description = "Lista de productos incluidos en el pedido")
    private List<ItemPedidoRequest> items;
}

