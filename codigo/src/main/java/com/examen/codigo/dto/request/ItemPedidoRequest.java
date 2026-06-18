package com.examen.codigo.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo requerido para agregar productos al pedido")
public class ItemPedidoRequest {
    @Schema(description = "ID del producto que este en la compra", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @Schema(description = "la cantidad de productos comprados ", example = "23", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
}