package com.examen.codigo.controller;


import com.examen.codigo.dto.request.ProductoRequest;
import com.examen.codigo.dto.response.ProductoResponse;
import com.examen.codigo.response.BaseResponse;
import com.examen.codigo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Mantenimiento del catálogo de productos y control de stock")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Añade un producto al inventario con su precio base y stock inicial.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Campos inválidos (ej. stock negativo o precio menor a cero)")
    })
    public ResponseEntity<BaseResponse<ProductoResponse>> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        BaseResponse<ProductoResponse> body = BaseResponse.<ProductoResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Producto creado correctamente")
                .objeto(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    @Operation(summary = "Listar catálogo de productos", description = "Retorna todos los productos registrados, activos e inactivos.")
    @ApiResponse(responseCode = "200", description = "Listado de productos obtenido")
    public ResponseEntity<BaseResponse<List<ProductoResponse>>> listar() {
        List<ProductoResponse> productos = productoService.listar();
        BaseResponse<List<ProductoResponse>> body = BaseResponse.<List<ProductoResponse>>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Listado de productos")
                .objeto(productos)
                .build();
        return ResponseEntity.ok(body);
    }
}
