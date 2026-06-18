package com.examen.codigo.exception;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
