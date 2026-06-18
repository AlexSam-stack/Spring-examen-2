package com.examen.codigo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura estándar de respuesta de la API")
public class BaseResponse<T> {

    @Schema(description = "Código de estado HTTP", example = "200")
    private Integer codigo;
    @Schema(description = "Mensaje informativo del resultado", example = "Operación exitosa")
    private String mensaje;
    @Schema(description = "Cuerpo de la respuesta con los datos solicitados")
    private T objeto;

    public static <T> BaseResponse<T> ok(String mensaje, T objeto) {
        return BaseResponse.<T>builder()
                .codigo(200)
                .mensaje(mensaje)
                .objeto(objeto)
                .build();
    }

    public static <T> BaseResponse<T> created(String mensaje, T objeto) {
        return BaseResponse.<T>builder()
                .codigo(201)
                .mensaje(mensaje)
                .objeto(objeto)
                .build();
    }
}