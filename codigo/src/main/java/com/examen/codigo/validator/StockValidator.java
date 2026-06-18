package com.examen.codigo.validator;


import com.examen.codigo.entity.Producto;
import com.examen.codigo.exception.StockInsuficienteException;
import org.springframework.stereotype.Component;

@Component
public class StockValidator {

    /**
     * Valida que el producto tenga stock suficiente para cubrir la cantidad solicitada.
     *
     * @param producto           producto ya recuperado de base de datos
     * @param cantidadSolicitada cantidad pedida por el cliente
     * @throws StockInsuficienteException si el stock disponible es menor a lo solicitado
     */
    public void validar(Producto producto, Integer cantidadSolicitada) {
        if (producto.getStock() < cantidadSolicitada) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto '" + producto.getNombre() +
                            "'. Disponible: " + producto.getStock() + ", solicitado: " + cantidadSolicitada);
        }
    }
}