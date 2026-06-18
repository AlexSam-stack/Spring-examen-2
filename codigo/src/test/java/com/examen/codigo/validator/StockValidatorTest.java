package com.examen.codigo.validator;

import com.examen.codigo.entity.Producto;
import com.examen.codigo.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockValidatorTest {

    private final StockValidator stockValidator = new StockValidator();

    @Test
    void validar_cuandoStockEsSuficiente_noLanzaExcepcion() {
        // Arrange
        Producto producto = Producto.builder()
                .nombre("Mouse")
                .precio(BigDecimal.TEN)
                .stock(10)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> stockValidator.validar(producto, 5));
    }

    @Test
    void validar_cuandoStockEsInsuficiente_lanzaStockInsuficienteException() {
        // Arrange
        Producto producto = Producto.builder()
                .nombre("Mouse")
                .precio(BigDecimal.TEN)
                .stock(2)
                .build();

        // Act & Assert
        assertThrows(StockInsuficienteException.class, () -> stockValidator.validar(producto, 5));
    }
}
