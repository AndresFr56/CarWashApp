package com.grupo5.carwashapp.models.enums;

public enum FormaPago {
    EFECTIVO("Efectivo"),
    TRANSFERENCIA("Transferencia Bancaria"),
    TARJETA("Tarjeta de Crédito/Débito");

    private final String etiqueta;

    FormaPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}