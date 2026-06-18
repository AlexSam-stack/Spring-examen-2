package com.examen.codigo.controller;


import com.examen.codigo.dto.request.PedidoRequest;
import com.examen.codigo.dto.response.PedidoResponse;
import com.examen.codigo.response.BaseResponse;
import com.examen.codigo.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Controlador para la gestión y creación de órdenes de compra") // 👈 Agrupa en la UI
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Registra un pedido en el sistema junto con su lista de productos (detalles).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor (ej. violación de restricciones en BD)")
    })
    public ResponseEntity<BaseResponse<PedidoResponse>> crear(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse response = pedidoService.crear(request);
        BaseResponse<PedidoResponse> body = BaseResponse.<PedidoResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Pedido creado correctamente")
                .objeto(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Devuelve los datos de un pedido específico utilizando su identificador único.")
    public ResponseEntity<BaseResponse<PedidoResponse>> buscarPorId(@PathVariable Long id) {
        PedidoResponse response = pedidoService.buscarPorId(id);
        BaseResponse<PedidoResponse> body = BaseResponse.<PedidoResponse>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Pedido encontrado")
                .objeto(response)
                .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Historial de pedidos de un cliente", description = "Busca y lista todas las compras asociadas al ID de un cliente.")
    @ApiResponse(responseCode = "200", description = "Listado de pedidos del cliente obtenido")
    public ResponseEntity<BaseResponse<List<PedidoResponse>>> listarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponse> pedidos = pedidoService.listarPorCliente(clienteId);
        BaseResponse<List<PedidoResponse>> body = BaseResponse.<List<PedidoResponse>>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Listado de pedidos del cliente")
                .objeto(pedidos)
                .build();
        return ResponseEntity.ok(body);
    }
}
