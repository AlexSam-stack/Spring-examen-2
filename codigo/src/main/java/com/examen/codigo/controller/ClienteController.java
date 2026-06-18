package com.examen.codigo.controller;


import com.examen.codigo.dto.request.ClienteRequest;
import com.examen.codigo.dto.response.ClienteResponse;
import com.examen.codigo.response.BaseResponse;
import com.examen.codigo.service.ClienteService;
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

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestión y registro de clientes del sistema")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los campos enviados")
    })
    public ResponseEntity<BaseResponse<ClienteResponse>> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.crear(request);
        BaseResponse<ClienteResponse> body = BaseResponse.<ClienteResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Cliente creado correctamente")
                .objeto(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "El cliente con el ID proporcionado no existe")
    })
    public ResponseEntity<BaseResponse<ClienteResponse>> buscarPorId(@PathVariable Long id) {
        ClienteResponse response = clienteService.buscarPorId(id);
        BaseResponse<ClienteResponse> body = BaseResponse.<ClienteResponse>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Cliente encontrado")
                .objeto(response)
                .build();
        return ResponseEntity.ok(body);
    }
}
